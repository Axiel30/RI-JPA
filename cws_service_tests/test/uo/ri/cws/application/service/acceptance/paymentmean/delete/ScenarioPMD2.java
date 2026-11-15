package uo.ri.cws.application.service.acceptance.paymentmean.delete;

import static org.junit.jupiter.api.Assertions.assertNull;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.acceptance.util.dbfixture.Db;
import uo.ri.cws.application.service.acceptance.util.dbfixture.DbFixtures;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TClientsRecord;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TPaymentMeansRecord;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TVouchersRecord;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService;
import uo.ri.util.exception.BusinessException;

public class ScenarioPMD2 {
	private PaymentMeanCrudService service = Factories.service.forPaymentMeanCrudService();
	private TPaymentMeansRecord paymentMean;

	@Given("[PM.D.2] a registered client with an unused voucher")
	public void givenARegisteredClientWithAnUnusedVoucher() {
		TClientsRecord client = DbFixtures.aClient();
		paymentMean = DbFixtures.aVoucherForClient(client.id);
	}

	@When("[PM.D.2] I remove the payment mean")
	public void whenIRemoveThePaymentMean() throws BusinessException {
		service.deletePaymentMean(paymentMean.id);
	}

	@Then("[PM.D.2] the payment mean no longer exists")
	public void thenThePaymentMeanNoLongerExists() {
		TPaymentMeansRecord base = Db.findById(TPaymentMeansRecord.class, paymentMean.id);
		TVouchersRecord voucher = Db.findById(TVouchersRecord.class, paymentMean.id);
		assertNull(base);
		assertNull(voucher);
	}
}
