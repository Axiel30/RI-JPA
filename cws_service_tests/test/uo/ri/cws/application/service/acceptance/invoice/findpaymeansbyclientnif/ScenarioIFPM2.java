package uo.ri.cws.application.service.acceptance.invoice.findpaymeansbyclientnif;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.acceptance.util.dbfixture.Db;
import uo.ri.cws.application.service.acceptance.util.dbfixture.DbFixtures;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TClientsRecord;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TCreditCardsRecord;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TPaymentMeansRecord;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TVouchersRecord;
import uo.ri.cws.application.service.invoice.InvoicingService;
import uo.ri.cws.application.service.invoice.InvoicingService.CardDto;
import uo.ri.cws.application.service.invoice.InvoicingService.CashDto;
import uo.ri.cws.application.service.invoice.InvoicingService.PaymentMeanDto;
import uo.ri.cws.application.service.invoice.InvoicingService.VoucherDto;
import uo.ri.util.exception.BusinessException;

/**
 * Scenario: [I.FPM.2] Find cash, card and voucher payment means.
 */
public class ScenarioIFPM2 {
	private final InvoicingService service = Factories.service.forCreateInvoiceService();
	private TClientsRecord client;
	private TPaymentMeansRecord cash;
	private TPaymentMeansRecord card;
	private TPaymentMeansRecord voucher;
	private List<PaymentMeanDto> result;

	@Given("[I.FPM.2] a registered client with cash, a credit card and a voucher")
	public void givenClientWithThreePaymentMeans() {
		client = DbFixtures.aClient();
		cash = DbFixtures.aCashForClient(client.id);
		card = DbFixtures.aCreditCardForClient(client.id);
		voucher = DbFixtures.aVoucherForClientOf(
				client.id, new BigDecimal("100.00"), new BigDecimal("60.00"), "Settlement voucher");
	}

	@When("[I.FPM.2] I search for payment means by the client's nif")
	public void whenISearchByClientNif() throws BusinessException {
		result = service.findPayMeansByClientNif(client.nif);
	}

	@Then("[I.FPM.2] I get exactly those three payment means with their common data")
	public void thenIGetExactlyThosePaymentMeans() {
		Map<String, TPaymentMeansRecord> expected = new HashMap<>();
		expected.put(cash.id, cash);
		expected.put(card.id, card);
		expected.put(voucher.id, voucher);

		assertEquals(3, result.size());
		Set<String> returnedIds = new HashSet<>();
		for (PaymentMeanDto dto : result) {
			TPaymentMeansRecord record = expected.get(dto.id);
			assertNotNull(record);
			assertTrue(returnedIds.add(dto.id));
			assertEquals(record.version, dto.version);
			assertEquals(client.id, dto.clientId);
			assertEquals(0, record.accumulated.compareTo(dto.accumulated));
			
			if (cash.id.equals(dto.id)) {
				assertInstanceOf(CashDto.class, dto);
			} else if (card.id.equals(dto.id)) {
				assertInstanceOf(CardDto.class, dto);
			} else if (voucher.id.equals(dto.id)) {
				assertInstanceOf(VoucherDto.class, dto);
			}
		}
		assertEquals(expected.keySet(), returnedIds);
	}

	@Then("[I.FPM.2] the CardDto includes its number, type and expiration month")
	public void thenCardDetailsMatch() {
		PaymentMeanDto found = null;
		for (PaymentMeanDto dto : result) {
			if (card.id.equals(dto.id)) {
				found = dto;
			}
		}
		CardDto cardDto = assertInstanceOf(CardDto.class, found);
		
		TCreditCardsRecord cardRecord = Db.findById(TCreditCardsRecord.class, card.id);
		assertEquals(cardRecord.number, cardDto.cardNumber);
		assertEquals(cardRecord.type, cardDto.cardType);
		YearMonth expiration = YearMonth.from(cardRecord.validThru.toLocalDate());
		assertEquals(expiration, cardDto.cardExpiration);
	}

	@Then("[I.FPM.2] the VoucherDto includes its code, description and available amount")
	public void thenVoucherDetailsMatch() {
		PaymentMeanDto found = null;
		for (PaymentMeanDto dto : result) {
			if (voucher.id.equals(dto.id)) {
				found = dto;
			}
		}
		VoucherDto voucherDto = assertInstanceOf(VoucherDto.class, found);
		TVouchersRecord voucherRecord = Db.findById(TVouchersRecord.class, voucher.id);
		assertEquals(voucherRecord.code, voucherDto.code);
		assertEquals(voucherRecord.description, voucherDto.description);
		assertEquals(0, voucherRecord.available.compareTo(voucherDto.availableAmount));
	}
}
