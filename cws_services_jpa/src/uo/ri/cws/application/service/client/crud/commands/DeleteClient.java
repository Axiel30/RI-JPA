package uo.ri.cws.application.service.client.crud.commands;

import java.util.HashSet;

import uo.ri.conf.Factories;
import uo.ri.cws.application.query.PaymentMeanQueries;
import uo.ri.cws.application.repository.ClientRepository;
import uo.ri.cws.application.repository.PaymentMeanRepository;
import uo.ri.cws.application.repository.RecommendationRepository;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Associations;
import uo.ri.cws.domain.Client;
import uo.ri.cws.domain.PaymentMean;
import uo.ri.cws.domain.Recommendation;
import uo.ri.cws.domain.types.Nif;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class DeleteClient implements Command<Void> {

    private String nif;
    private ClientRepository clientRepo = Factories.repository.forClient();
    private PaymentMeanRepository pmRepo = Factories.repository.forPaymentMean();
    private RecommendationRepository recRepo = Factories.repository.forRecommendation();
    private PaymentMeanQueries query = Factories.query.forPaymentMean();

    public DeleteClient(String nif) {
        ArgumentChecks.isNotBlank(nif, "NIF cannot be null or blank");
        this.nif = nif;
    }

    @Override
    public Void execute() throws BusinessException {
        Client client = clientRepo.findByNif(new Nif(nif))
            .orElseThrow(() -> new BusinessException("Client does not exist"));

        BusinessChecks.isTrue(client.getVehicles().isEmpty(), "Cannot delete client with registered vehicles");

        boolean hasCharges = client.getPaymentMeans().stream().anyMatch(pm -> query.hasCharges(pm.getId()));
        BusinessChecks.isFalse(client.hasPaidInvoices() || hasCharges, "Cannot delete client with paid invoices");

        // Borrar medios de pago
        for (PaymentMean pm : new HashSet<>(client.getPaymentMeans())) {
            Associations.Holds.unlink(pm);
            pmRepo.remove(pm);
        }

        // Borrar recomendación recibida (si existe)
        if (client.getRecommendation() != null) {
            Recommendation rec = client.getRecommendation();
            Associations.Recommends.unlink(rec);
            recRepo.remove(rec);
        }

        // Borrar recomendaciones hechas a otros
        for (Recommendation rec : new HashSet<>(client.getRecommendations())) {
            Associations.Recommends.unlink(rec);
            recRepo.remove(rec);
        }

        clientRepo.remove(client);
        return null;
    }
}