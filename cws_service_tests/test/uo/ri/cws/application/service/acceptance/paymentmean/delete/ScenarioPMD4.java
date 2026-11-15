package uo.ri.cws.application.service.acceptance.paymentmean.delete;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.acceptance.util.ExceptionBox;
import uo.ri.cws.application.service.acceptance.util.dbfixture.Db;
import uo.ri.cws.application.service.acceptance.util.dbfixture.DbFixtures;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TCashesRecord;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TClientsRecord;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TPaymentMeansRecord;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService;

public class ScenarioPMD4 {
	private ExceptionBox context = new ExceptionBox();
	private PaymentMeanCrudService service = Factories.service.forPaymentMeanCrudService();
	private TPaymentMeansRecord cash;

	@Given("[PM.D.4] a registered client with cash")
	public void givenARegisteredClientWithCash() {
		TClientsRecord client = DbFixtures.aClient();
		cash = DbFixtures.aCashForClient(client.id);
	}

	@When("[PM.D.4] I try to remove the cash payment mean")
	public void whenITryToRemoveTheCashPaymentMean() {
		context.tryAndKeep(() -> service.deletePaymentMean(cash.id));
	}

	@Then("[PM.D.4] a business error happens with an explaining message")
	public void thenABusinessErrorHappensWithAnExplainingMessage() {
		context.assertBusinessExceptionWithMessage();
		TPaymentMeansRecord base = Db.findById(TPaymentMeansRecord.class, cash.id);
		TCashesRecord subtype = Db.findById(TCashesRecord.class, cash.id);
		assertNotNull(base);
		assertNotNull(subtype);
	}
}
