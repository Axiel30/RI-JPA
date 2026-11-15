package uo.ri.cws.application.service.acceptance.paymentmean.findvoucherbycode;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

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
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService.VoucherDto;
import uo.ri.util.exception.BusinessException;

public class ScenarioPMFbC1 {
	private PaymentMeanCrudService service = Factories.service.forPaymentMeanCrudService();
	private TPaymentMeansRecord paymentMean;
	private TVouchersRecord voucher;
	private Optional<VoucherDto> result;

	@Given("[PM.FbC.1] a registered client with a voucher")
	public void givenARegisteredClientWithAVoucher() {
		TClientsRecord client = DbFixtures.aClient();
		paymentMean = DbFixtures.aVoucherForClient(client.id);
		voucher = Db.findById(TVouchersRecord.class, paymentMean.id);
	}

	@When("[PM.FbC.1] I search the voucher by code")
	public void whenISearchTheVoucherByCode() throws BusinessException {
		result = service.findVoucherByCode(voucher.code);
	}

	@Then("[PM.FbC.1] I get the voucher")
	public void thenIGetTheVoucher() {
		assertTrue(result.isPresent());
		VoucherDto dto = result.get();
		Asserts.dtoMatchesRecords(paymentMean, voucher, dto);
	}
}
