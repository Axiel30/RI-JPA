package uo.ri.cws.application.service.acceptance.paymentmean.addvoucher;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.acceptance.util.ExceptionBox;
import uo.ri.cws.application.service.acceptance.util.dbfixture.Db;
import uo.ri.cws.application.service.acceptance.util.dbfixture.DbFixtures;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TClientsRecord;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TPaymentMeansRecord;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TVouchersRecord;
import uo.ri.cws.application.service.acceptance.util.dtobuilders.VoucherDtoBuilder;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService.VoucherDto;

public class ScenarioPMAV3 {
	private ExceptionBox context = new ExceptionBox();
	private PaymentMeanCrudService service = Factories.service.forPaymentMeanCrudService();
	private TClientsRecord client;
	private TVouchersRecord existingVoucher;

	@Given("[PM.AV.3] a registered client with a voucher")
	public void givenARegisteredClientWithAVoucher() {
		client = DbFixtures.aClient();
		TPaymentMeansRecord paymentMean = DbFixtures.aVoucherForClient(client.id);
		existingVoucher = Db.findById(TVouchersRecord.class, paymentMean.id);
	}

	@When("[PM.AV.3] I try to add another voucher with same code")
	public void whenITryToAddAnotherVoucherWithSameCode() {
		VoucherDto voucher = new VoucherDtoBuilder()
				.withClientId(client.id)
				.withCode(existingVoucher.code)
				.build();
		context.tryAndKeep(() -> service.addVoucher(voucher));
	}

	@Then("[PM.AV.3] a business error happens with an explaining message")
	public void thenABusinessErrorHappensWithAnExplainingMessage() {
		context.assertBusinessExceptionWithMessage();
	}
}
