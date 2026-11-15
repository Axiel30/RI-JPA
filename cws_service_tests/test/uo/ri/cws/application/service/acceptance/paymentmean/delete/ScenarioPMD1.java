package uo.ri.cws.application.service.acceptance.paymentmean.delete;

import static org.junit.jupiter.api.Assertions.assertNull;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.acceptance.util.dbfixture.Db;
import uo.ri.cws.application.service.acceptance.util.dbfixture.DbFixtures;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TClientsRecord;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TCreditCardsRecord;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TPaymentMeansRecord;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService;
import uo.ri.util.exception.BusinessException;

public class ScenarioPMD1 {
	private PaymentMeanCrudService service = Factories.service.forPaymentMeanCrudService();
	private TPaymentMeansRecord paymentMean;

	@Given("[PM.D.1] a registered client with an unused credit card")
	public void givenARegisteredClientWithAnUnusedCreditCard() {
		TClientsRecord client = DbFixtures.aClient();
		paymentMean = DbFixtures.aCreditCardForClient(client.id);
	}

	@When("[PM.D.1] I remove the payment mean")
	public void whenIRemoveThePaymentMean() throws BusinessException {
		service.deletePaymentMean(paymentMean.id);
	}

	@Then("[PM.D.1] the payment mean no longer exists")
	public void thenThePaymentMeanNoLongerExists() {
		TPaymentMeansRecord base = Db.findById(TPaymentMeansRecord.class, paymentMean.id);
		TCreditCardsRecord card = Db.findById(TCreditCardsRecord.class, paymentMean.id);
		assertNull(base);
		assertNull(card);
	}
}
