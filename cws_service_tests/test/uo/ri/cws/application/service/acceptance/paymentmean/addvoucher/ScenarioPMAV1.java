package uo.ri.cws.application.service.acceptance.paymentmean.addvoucher;

import static org.junit.jupiter.api.Assertions.assertNotNull;

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
import uo.ri.cws.application.service.acceptance.util.dtobuilders.VoucherDtoBuilder;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService.VoucherDto;
import uo.ri.util.exception.BusinessException;

public class ScenarioPMAV1 {
	private PaymentMeanCrudService service = Factories.service.forPaymentMeanCrudService();
	private TClientsRecord client;
	private VoucherDto result;

	@Given("[PM.AV.1] a registered client")
	public void givenARegisteredClient() {
		client = DbFixtures.aClient();
	}

	@When("[PM.AV.1] I add a new voucher for that client")
	public void whenIAddANewVoucherForThatClient() throws BusinessException {
		VoucherDto voucher = new VoucherDtoBuilder()
				.withClientId(client.id)
				.build();
		result = service.addVoucher(voucher);
	}

	@Then("[PM.AV.1] the voucher results added to the system")
	public void thenTheVoucherResultsAddedToTheSystem() {
		assertNotNull(result.id);
		TPaymentMeansRecord paymentMean = Db.findById(TPaymentMeansRecord.class, result.id);
		TVouchersRecord voucher = Db.findById(TVouchersRecord.class, result.id);
		assertNotNull(paymentMean);
		assertNotNull(voucher);
		Asserts.dtoMatchesRecords(paymentMean, voucher, result);
	}
}
