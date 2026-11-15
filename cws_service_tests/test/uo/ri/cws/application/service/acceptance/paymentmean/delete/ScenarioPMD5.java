package uo.ri.cws.application.service.acceptance.paymentmean.delete;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.acceptance.util.ExceptionBox;
import uo.ri.cws.application.service.acceptance.util.dbfixture.Db;
import uo.ri.cws.application.service.acceptance.util.dbfixture.DbFixtures;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TClientsRecord;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TCreditCardsRecord;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TPaymentMeansRecord;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService;

public class ScenarioPMD5 {
	private ExceptionBox context = new ExceptionBox();
	private PaymentMeanCrudService service = Factories.service.forPaymentMeanCrudService();
	private TPaymentMeansRecord paymentMean;

	@Given("[PM.D.5] a registered client with a payment mean used in charges")
	public void givenARegisteredClientWithAPaymentMeanUsedInCharges() {
		TClientsRecord client = DbFixtures.aClient();
		paymentMean = DbFixtures.aChargedCreditCardForClient(client.id);
	}

	@When("[PM.D.5] I try to remove the payment mean")
	public void whenITryToRemoveThePaymentMean() {
		context.tryAndKeep(() -> service.deletePaymentMean(paymentMean.id));
	}

	@Then("[PM.D.5] a business error happens with an explaining message")
	public void thenABusinessErrorHappensWithAnExplainingMessage() {
		context.assertBusinessExceptionWithMessage();
		TPaymentMeansRecord base = Db.findById(TPaymentMeansRecord.class, paymentMean.id);
		TCreditCardsRecord card = Db.findById(TCreditCardsRecord.class, paymentMean.id);
		assertNotNull(base);
		assertNotNull(card);
	}
}
