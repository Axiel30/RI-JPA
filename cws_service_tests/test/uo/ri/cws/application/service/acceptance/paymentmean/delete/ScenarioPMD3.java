package uo.ri.cws.application.service.acceptance.paymentmean.delete;

import java.util.UUID;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.acceptance.util.ExceptionBox;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService;

public class ScenarioPMD3 {
	private ExceptionBox context = new ExceptionBox();
	private PaymentMeanCrudService service = Factories.service.forPaymentMeanCrudService();

	@When("[PM.D.3] I try to remove a non existing payment mean")
	public void whenITryToRemoveANonExistingPaymentMean() {
		String id = UUID.randomUUID().toString();
		context.tryAndKeep(() -> service.deletePaymentMean(id));
	}

	@Then("[PM.D.3] a business error happens with an explaining message")
	public void thenABusinessErrorHappensWithAnExplainingMessage() {
		context.assertBusinessExceptionWithMessage();
	}
}
