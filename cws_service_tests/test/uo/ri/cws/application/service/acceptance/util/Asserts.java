package uo.ri.cws.application.service.acceptance.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;

import uo.ri.cws.application.service.client.ClientCrudService.ClientDto;
import uo.ri.cws.application.service.invoice.InvoicingService.InvoiceDto;
import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TClientsRecord;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TInvoicesRecord;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TMechanicsRecord;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TCreditCardsRecord;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TPaymentMeansRecord;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TVouchersRecord;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService.CardDto;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService.PaymentMeanDto;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService.VoucherDto;

public class Asserts {

	public static void dtoMatchesRecord(TPaymentMeansRecord expected, PaymentMeanDto dto) {
		assertEquals(expected.id, dto.id);
		assertEquals(expected.client_Id, dto.clientId);
		assertEquals(0, expected.accumulated.compareTo(dto.accumulatedAmount));
		assertEquals(expected.version, dto.version);
	}

	public static void dtoMatchesRecords(
			TPaymentMeansRecord paymentMean,
			TCreditCardsRecord card,
			CardDto dto) {
		dtoMatchesRecord(paymentMean, dto);
		assertEquals(card.number, dto.cardNumber);
		assertEquals(card.type, dto.cardType);
		YearMonth expiration = YearMonth.from(card.validThru.toLocalDate());
		assertEquals(expiration, dto.cardExpiration);
	}

	public static void dtoMatchesRecords(
			TPaymentMeansRecord paymentMean,
			TVouchersRecord voucher,
			VoucherDto dto) {
		dtoMatchesRecord(paymentMean, dto);
		assertEquals(voucher.code, dto.code);
		assertEquals(voucher.description, dto.description);
		assertEquals(0, voucher.available.compareTo(dto.availableAmount));
	}

	public static void dtoMatchesRecords(
			TPaymentMeansRecord paymentMean,
			TVouchersRecord voucher,
			uo.ri.cws.application.service.paymentmean.VoucherGenerationService.VoucherDto dto) {
		assertEquals(paymentMean.id, dto.id);
		assertEquals(paymentMean.client_Id, dto.clientId);
		assertEquals(0, paymentMean.accumulated.compareTo(dto.accumulatedAmount));
		assertEquals(paymentMean.version, dto.version);
		assertEquals(voucher.code, dto.code);
		assertEquals(voucher.description, dto.description);
		assertEquals(0, voucher.available.compareTo(dto.availableAmount));
	}

	public static void matches(ClientDto expected, ClientDto dto) {
		assertEquals(expected.nif, dto.nif);
		assertEquals(expected.name, dto.name);
		assertEquals(expected.surname, dto.surname);
		assertEquals(expected.addressStreet, dto.addressStreet);
		assertEquals(expected.addressCity, dto.addressCity);
		assertEquals(expected.addressCountry, dto.addressCountry);
		assertEquals(expected.addressZipcode, dto.addressZipcode);
		assertEquals(expected.phone, dto.phone);
		assertEquals(expected.email, dto.email);
	}

	public static void recordMatchesDto(ClientDto expected, TClientsRecord rec) {
		assertEquals(expected.id, rec.id);
		assertEquals(expected.nif, rec.nif);
		assertEquals(expected.name, rec.name);
		assertEquals(expected.surname, rec.surname);
		assertEquals(expected.addressStreet, rec.street);
		assertEquals(expected.addressCity, rec.city);
		assertEquals(expected.addressCountry, rec.country);
		assertEquals(expected.addressZipcode, rec.postalCode);
		assertEquals(expected.phone, rec.phone);
		assertEquals(expected.email, rec.email);
		assertEquals(expected.version, rec.version);
	}

	public static void dtoMatchesRecord(TClientsRecord expected, ClientDto dto) {
		assertEquals(expected.id, dto.id);
		assertEquals(expected.nif, dto.nif);
		assertEquals(expected.name, dto.name);
		assertEquals(expected.surname, dto.surname);
		assertEquals(expected.street, dto.addressStreet);
		assertEquals(expected.city, dto.addressCity);
		assertEquals(expected.country, dto.addressCountry);
		assertEquals(expected.postalCode, dto.addressZipcode);
		assertEquals(expected.phone, dto.phone);
		assertEquals(expected.email, dto.email);
		assertEquals(expected.version, dto.version);
	}

	public static void matches(MechanicDto expected, MechanicDto dto) {
		assertEquals(expected.nif, dto.nif);
		assertEquals(expected.name, dto.name);
		assertEquals(expected.surname, dto.surname);
	}

	public static void recordMatchesDto(MechanicDto expected, TMechanicsRecord rec) {
		assertEquals(expected.id, rec.id);
		assertEquals(expected.nif, rec.nif);
		assertEquals(expected.name, rec.name);
		assertEquals(expected.surname, rec.surname);
		assertEquals(expected.version, rec.version);
	}

	public static void dtoMatchesRecord(TMechanicsRecord expected, MechanicDto dto) {
		assertEquals(expected.id, dto.id);
		assertEquals(expected.nif, dto.nif);
		assertEquals(expected.name, dto.name);
		assertEquals(expected.surname, dto.surname);
		assertEquals(expected.version, dto.version);
	}

	public static void isNow(Timestamp ts) {
        LocalDateTime ts1 = ts.toLocalDateTime().truncatedTo(ChronoUnit.SECONDS);
        LocalDateTime ts2 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        long difference = Math.abs(ts1.until(ts2, ChronoUnit.SECONDS));
        
        assertTrue(difference <= 1, "Difference is greater than 1 sec");
    }

	public static void recordMatchesDto(InvoiceDto expected, TInvoicesRecord loaded) {
		assertEquals(expected.id, loaded.id);
		assertEquals(expected.date, loaded.date.toLocalDate());
		
		assertEquals(0, expected.total.compareTo(loaded.total) );
		assertEquals(0, expected.subtotal.compareTo(loaded.subtotal));
		assertEquals(0, expected.vatAmount.compareTo(loaded.vatamount));
		assertEquals(0, expected.vatPercentage.compareTo(loaded.vatrate));

		assertEquals(expected.state, loaded.state);
		assertEquals(expected.version, loaded.version);
	}

	public static void rightAmounts(InvoiceDto expected, InvoiceDto actual) {
		assertEquals(0, expected.total.compareTo(actual.total));
		assertEquals(0, expected.subtotal.compareTo(actual.subtotal));
		assertEquals(0, expected.vatAmount.compareTo(actual.vatAmount));
		assertEquals(0, expected.vatPercentage.compareTo(actual.vatPercentage));

		assertEquals(expected.state, actual.state);
	}
}
