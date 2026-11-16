package uo.ri.cws.application.service.paymentmean.voucher.commands;

import java.util.List;
import java.util.UUID;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.ClientRepository;
import uo.ri.cws.application.repository.InvoiceRepository;
import uo.ri.cws.application.repository.PaymentMeanRepository;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Associations;
import uo.ri.cws.domain.Client;
import uo.ri.cws.domain.Invoice;
import uo.ri.cws.domain.Voucher;
import uo.ri.cws.domain.types.Money;
import uo.ri.cws.domain.types.VoucherCode;
import uo.ri.util.exception.BusinessException;

public class GenerateVouchers implements Command<int[]> {

    private ClientRepository clientRepo = Factories.repository.forClient();
    private PaymentMeanRepository pmRepo = Factories.repository.forPaymentMean();
    private InvoiceRepository invoiceRepo = Factories.repository.forInvoice();

    @Override
    public int[] execute() throws BusinessException {
        // Generar vales de 25€ (recomendaciones)
        int count25 = 0;

        List<Client> clients = clientRepo.findAll();
        for (Client client : clients) {
            while (client.isEligibleForVoucher25()) {
                Voucher voucher = new Voucher(new VoucherCode("V-" + UUID.randomUUID()), "Por recomendación",
                    Money.of("25.00"));
                Associations.Holds.link(client, voucher);
                client.markThreeRecommendationsAsUsedForVoucher25();
                pmRepo.add(voucher);
                count25++;
            }
        }

        // Generar vales de 20€ (órdenes de trabajo)
        int count20 = 0;
        for (Client client : clients) {
            while (client.getWorkOrdersAvailableForVoucher20().size() >= 3) {
                Voucher voucher = new Voucher(new VoucherCode("V-" + UUID.randomUUID()), "Por tres órdenes de trabajo",
                    Money.of("20.00"));
                Associations.Holds.link(client, voucher);
                client.markThreeWorkOrdersAsUsedForVoucher20();
                pmRepo.add(voucher);
                count20++;
            }
        }

        // Generar vales de 30€ (facturas > 500€)
        int count30 = 0;
        List<Invoice> invoices = invoiceRepo.findOverAmountUnusedForVoucher(Money.of("500.00"));
        for (Invoice invoice : invoices) {
            if (invoice.isSettled() && !invoice.isUsedForVoucher()) {
                Client client = invoice.getWorkOrders().stream().map(wo -> wo.getVehicle().getClient()).findFirst()
                    .orElse(null);

                if (client != null) {
                    Voucher voucher = new Voucher(new VoucherCode("V-" + UUID.randomUUID()),
                        "Por factura de más de 500", Money.of("30.00"));
                    Associations.Holds.link(client, voucher);
                    invoice.markAsUsedForVoucher();
                    pmRepo.add(voucher);
                    count30++;
                }
            }
        }

        return new int[] { count20, count25, count30 };
    }

}
