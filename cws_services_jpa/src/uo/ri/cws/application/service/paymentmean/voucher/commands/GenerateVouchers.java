package uo.ri.cws.application.service.paymentmean.voucher.commands;

import java.util.List;
import java.util.UUID;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.ClientRepository;
import uo.ri.cws.application.repository.PaymentMeanRepository;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Associations;
import uo.ri.cws.domain.Client;
import uo.ri.cws.domain.Voucher;
import uo.ri.cws.domain.types.Money;
import uo.ri.cws.domain.types.VoucherCode;
import uo.ri.util.exception.BusinessException;

public class GenerateVouchers implements Command<int[]> {

    private ClientRepository clientRepo = Factories.repository.forClient();
    private PaymentMeanRepository pmRepo =
            Factories.repository.forPaymentMean();

    @Override
    public int[] execute() throws BusinessException {
        int count25 = 0;

        List<Client> clients = clientRepo.findAll();
        for (Client client : clients) {
            while (client.isEligibleForVoucher25()) {
                Voucher voucher = new Voucher(
                        new VoucherCode("V-" + UUID.randomUUID()),
                        "Por recomendación",
                        Money.of("25.00")
                );
                Associations.Holds.link(client, voucher);
                client.markThreeRecommendationsAsUsedForVoucher25();
                pmRepo.add(voucher);
                count25++;
            }
        }

        return new int[] { 0, count25, 0 };
    }

}
