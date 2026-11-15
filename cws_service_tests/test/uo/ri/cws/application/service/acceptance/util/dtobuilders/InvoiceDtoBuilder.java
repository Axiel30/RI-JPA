package uo.ri.cws.application.service.acceptance.util.dtobuilders;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import uo.ri.cws.application.service.invoice.InvoicingService.InvoiceDto;
import uo.ri.util.random.Random;

public class InvoiceDtoBuilder {

	private InvoiceDto dto = createDefaultInvoiceRecord();

	public InvoiceDto build() {
		return dto;
	}
	
	private LocalDate randomDate() {
		LocalDate dateBefore = LocalDate.parse("2020-01-01");
		LocalDate dateAfter = LocalDate.now();
		long noOfDaysBetween = ChronoUnit.DAYS.between(dateBefore, dateAfter);
		return dateBefore.plusDays( Random.inRange(0, noOfDaysBetween) );
	}

	private InvoiceDto createDefaultInvoiceRecord() {
	    InvoiceDto res = new InvoiceDto();

	    res.date = randomDate();
	    res.state = "ISSUED";

	    res.subtotal = BigDecimal.valueOf(Random.inRange(100.0, 500.0))
	            .setScale(2, RoundingMode.HALF_EVEN);

	    res.vatPercentage = new BigDecimal("21");

	    res.vatAmount = res.subtotal
	            .multiply(res.vatPercentage)
	            .divide(new BigDecimal("100"), 2, RoundingMode.HALF_EVEN);

	    res.total = res.subtotal.add(res.vatAmount);

	    return res;
	}
	
	public InvoiceDtoBuilder withSubtotal(double amount) {
	    dto.subtotal = BigDecimal.valueOf(amount)
	            .setScale(2, RoundingMode.HALF_EVEN);
	    return this;
	}

	public InvoiceDtoBuilder withTotal(double amount) {
	    dto.total = BigDecimal.valueOf(amount)
	            .setScale(2, RoundingMode.HALF_EVEN);
	    return this;
	}

	public InvoiceDtoBuilder withVatAmount(double amount) {
	    dto.vatAmount = BigDecimal.valueOf(amount)
	            .setScale(2, RoundingMode.HALF_EVEN);
	    return this;
	}

	public InvoiceDtoBuilder withVatPercentage(double percent) {
	    dto.vatPercentage = BigDecimal.valueOf(percent)
	            .setScale(2, RoundingMode.HALF_EVEN);
	    return this;
	}

	public InvoiceDtoBuilder withDate(String date) {
	    dto.date = LocalDate.parse(date);
	    return this;
	}

	public InvoiceDtoBuilder withState(String state) {
	    dto.state = state;
	    return this;
	}
	
	public InvoiceDtoBuilder withNumber(long arg) {
		dto.number = arg;
		return this;
	}
}
