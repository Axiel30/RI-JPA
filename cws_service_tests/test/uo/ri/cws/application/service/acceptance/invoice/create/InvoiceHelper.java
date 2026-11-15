package uo.ri.cws.application.service.acceptance.invoice.create;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import uo.ri.cws.application.service.invoice.InvoicingService.InvoiceDto;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TWorkOrdersRecord;
import uo.ri.cws.application.service.acceptance.util.dtobuilders.InvoiceDtoBuilder;

public class InvoiceHelper {

    private static final BigDecimal VAT_PERCENTAGE = new BigDecimal("21");

    // ----------------------------
    // Single work order
    // ----------------------------
    public static InvoiceDto computeFor(TWorkOrdersRecord wo) {
        return buildInvoice(wo.total_amount);
    }

    // ----------------------------
    // List of work orders
    // ----------------------------
    public static InvoiceDto computeFor(List<TWorkOrdersRecord> workOrders) {

        BigDecimal net = workOrders.stream()
                .map(wo -> wo.total_amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_EVEN);

        return buildInvoice(net);
    }

    // ----------------------------
    // Core invoice computation
    // ----------------------------
    private static InvoiceDto buildInvoice(BigDecimal subtotal) {

        BigDecimal vatAmount = subtotal
                .multiply(VAT_PERCENTAGE)
                .divide(new BigDecimal("100"), 2, RoundingMode.HALF_EVEN);

        BigDecimal total = subtotal.add(vatAmount);

        return new InvoiceDtoBuilder()
                .withSubtotal(subtotal.doubleValue())
                .withVatAmount(vatAmount.doubleValue())
                .withTotal(total.doubleValue())
                .withVatPercentage(21)
                .withState("ISSUED")
                .build();
    }
}
