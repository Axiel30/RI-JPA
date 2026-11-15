package uo.ri.cws.application.service.acceptance.voucher.findbyclientnif;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.List;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.acceptance.util.Asserts;
import uo.ri.cws.application.service.acceptance.util.dbfixture.Db;
import uo.ri.cws.application.service.acceptance.util.dbfixture.DbFixtures;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TClientsRecord;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TPaymentMeansRecord;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TVouchersRecord;
import uo.ri.cws.application.service.paymentmean.VoucherGenerationService;
import uo.ri.cws.application.service.paymentmean.VoucherGenerationService.VoucherDto;
import uo.ri.util.exception.BusinessException;

public class ScenarioVFbN1 {
	private final VoucherGenerationService service = Factories.service.forVoucherService();
	private TClientsRecord client;
	private List<TPaymentMeansRecord> expected;
	private List<VoucherDto> result;

	@Given("[V.FbN.1] a registered client with several vouchers")
	public void givenARegisteredClientWithSeveralVouchers() {
		client = DbFixtures.aClient();
		expected = List.of(
				DbFixtures.aVoucherForClientOf(client.id, new BigDecimal("20.00"),
						new BigDecimal("20.00"), "First voucher"),
				DbFixtures.aVoucherForClientOf(client.id, new BigDecimal("25.00"),
						new BigDecimal("10.00"), "Second voucher"),
				DbFixtures.aVoucherForClientOf(client.id, new BigDecimal("30.00"),
						BigDecimal.ZERO, "Third voucher")
			);
	}

	@When("[V.FbN.1] I search vouchers by client nif")
	public void whenISearchVouchersByClientNif() throws BusinessException {
		result = service.findVouchersByClientNif(client.nif);
	}

	@Then("[V.FbN.1] I get all client vouchers")
	public void thenIGetAllClientVouchers() {
		assertEquals(expected.size(), result.size());
		for (TPaymentMeansRecord paymentMean : expected) {
			VoucherDto dto = result.stream()
					.filter(voucher -> paymentMean.id.equals(voucher.id))
					.findFirst()
					.orElseThrow();
			TVouchersRecord voucher = Db.findById(TVouchersRecord.class, paymentMean.id);
			Asserts.dtoMatchesRecords(paymentMean, voucher, dto);
		}
	}
}
