package uo.ri.cws.application.service.paymentmean.crud.commands;

import uo.ri.conf.Factories;
import uo.ri.cws.application.query.PaymentMeanQueries;
import uo.ri.cws.application.repository.PaymentMeanRepository;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Associations;
import uo.ri.cws.domain.Cash;
import uo.ri.cws.domain.PaymentMean;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class DeletePaymentMean implements Command<Void> {

    private String id;
    private PaymentMeanRepository repo = Factories.repository.forPaymentMean();
    private PaymentMeanQueries query = Factories.query.forPaymentMean();

    public DeletePaymentMean(String id) {
        ArgumentChecks.isNotBlank(id, "Id cannot be null or blank");
        this.id = id;
    }

    @Override
    public Void execute() throws BusinessException {
        PaymentMean pm = repo.findById(id)
                .orElseThrow(() ->
                        new BusinessException("Payment mean does not exist"));

        BusinessChecks.isFalse(pm instanceof Cash,
                "Cannot delete cash payment mean");
        BusinessChecks.isFalse(query.hasCharges(id),
                "Cannot delete payment mean with charges");

        Associations.Holds.unlink(pm);
        repo.remove(pm);

        return null;
    }

}
