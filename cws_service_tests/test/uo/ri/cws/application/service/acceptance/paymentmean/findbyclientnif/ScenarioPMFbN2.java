package uo.ri.cws.application.service.acceptance.paymentmean.findbyclientnif;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.acceptance.util.Asserts;
import uo.ri.cws.application.service.acceptance.util.dbfixture.Db;
import uo.ri.cws.application.service.acceptance.util.dbfixture.DbFixtures;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TClientsRecord;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TCreditCardsRecord;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TPaymentMeansRecord;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TVouchersRecord;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService.CardDto;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService.CashDto;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService.PaymentMeanDto;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService.VoucherDto;
import uo.ri.util.exception.BusinessException;

public class ScenarioPMFbN2 {
	private PaymentMeanCrudService service = Factories.service.forPaymentMeanCrudService();
	private TClientsRecord client;
	private List<TPaymentMeansRecord> expected;
	private List<PaymentMeanDto> result;

	@Given("[PM.FbN.2] a registered client with cash, credit cards and vouchers")
	public void givenARegisteredClientWithCashCreditCardsAndVouchers() {
		client = DbFixtures.aClient();
		expected = DbFixtures.somePaymentMeansForClient(client.id);
	}

	@When("[PM.FbN.2] I search payment means by client nif")
	public void whenISearchPaymentMeansByClientNif() throws BusinessException {
		result = service.findPaymentMeansByClientNif(client.nif);
	}

	@Then("[PM.FbN.2] I get all client payment means")
	public void thenIGetAllClientPaymentMeans() {
		assertEquals(expected.size(), result.size());
		for (TPaymentMeansRecord paymentMean : expected) {
			PaymentMeanDto dto = findById(paymentMean.id);
			assertNotNull(dto);
			assertMatches(paymentMean, dto);
		}
	}

	private PaymentMeanDto findById(String id) {
		return result.stream()
				.filter(dto -> id.equals(dto.id))
				.findFirst()
				.orElse(null);
	}

	private void assertMatches(TPaymentMeansRecord paymentMean, PaymentMeanDto dto) {
		if ("Cash".equals(paymentMean.dtype)) {
			assertTrue(dto instanceof CashDto);
			Asserts.dtoMatchesRecord(paymentMean, dto);
		} else if ("CreditCard".equals(paymentMean.dtype)) {
			TCreditCardsRecord card = Db.findById(TCreditCardsRecord.class, paymentMean.id);
			assertTrue(dto instanceof CardDto);
			Asserts.dtoMatchesRecords(paymentMean, card, (CardDto) dto);
		} else {
			TVouchersRecord voucher = Db.findById(TVouchersRecord.class, paymentMean.id);
			assertTrue(dto instanceof VoucherDto);
			Asserts.dtoMatchesRecords(paymentMean, voucher, (VoucherDto) dto);
		}
	}
}
