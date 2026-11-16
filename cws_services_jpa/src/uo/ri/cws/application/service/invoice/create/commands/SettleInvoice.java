package uo.ri.cws.application.service.invoice.create.commands;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.InvoiceRepository;
import uo.ri.cws.application.repository.PaymentMeanRepository;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Invoice;
import uo.ri.cws.domain.PaymentMean;
import uo.ri.cws.domain.types.Money;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class SettleInvoice implements Command<Void> {

    private String invoiceId;
    private Map<String, BigDecimal> charges;
    private InvoiceRepository invoiceRepo = Factories.repository.forInvoice();
    private PaymentMeanRepository pmRepo = Factories.repository.forPaymentMean();

    public SettleInvoice(String invoiceId, Map<String, BigDecimal> charges) {
        ArgumentChecks.isNotBlank(invoiceId, "Invoice ID cannot be null or blank");
        ArgumentChecks.isNotNull(charges, "Charges map cannot be null");
        ArgumentChecks.isFalse(charges.isEmpty(), "Charges map cannot be empty");
        for (Map.Entry<String, BigDecimal> entry : charges.entrySet()) {
            ArgumentChecks.isNotBlank(entry.getKey(), "Payment mean ID cannot be null or blank");
            ArgumentChecks.isNotNull(entry.getValue(), "Charge amount cannot be null");
        }
        this.invoiceId = invoiceId;
        this.charges = charges;
    }

    @Override
    public Void execute() throws BusinessException {
        Invoice invoice = invoiceRepo.findById(invoiceId)
            .orElseThrow(() -> new BusinessException("Invoice does not exist"));

        BusinessChecks.isFalse(invoice.isSettled(), "Invoice is already paid");

        Map<PaymentMean, Money> requestedCharges = new HashMap<>();
        for (Map.Entry<String, BigDecimal> entry : charges.entrySet()) {
            PaymentMean pm = pmRepo.findById(entry.getKey())
                .orElseThrow(() -> new BusinessException("Payment mean does not exist: " + entry.getKey()));
            Money amount = Money.of(entry.getValue()).roundToCents();
            requestedCharges.put(pm, amount);
        }

        Money.MoneyCalculator calc = Money.calc();
        requestedCharges.values().forEach(calc::add);
        Money totalCharges = calc.toMoneyRoundedToCents();
        BusinessChecks.isTrue(invoice.getTotal().equals(totalCharges),
            "Total charges must equal invoice total at cent precision");

        for (Map.Entry<PaymentMean, Money> entry : requestedCharges.entrySet()) {
            BusinessChecks.isTrue(entry.getKey().canBeCharged(entry.getValue()),
                "Payment mean cannot accept charge of amount " + entry.getValue());
        }

        // Delegar en el modelo de dominio
        invoice.settle(requestedCharges);

        return null;
    }
}