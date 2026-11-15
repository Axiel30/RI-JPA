package uo.ri.cws.application.service.acceptance.voucher;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.List;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.acceptance.util.dbfixture.Db;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TInvoicesRecord;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TPaymentMeansRecord;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TRecommendationsRecord;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TVouchersRecord;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TWorkOrdersRecord;
import uo.ri.cws.application.service.paymentmean.VoucherGenerationService;
import uo.ri.util.exception.BusinessException;

public abstract class AbstractVoucherScenario {

	protected final VoucherGenerationService service = Factories.service.forVoucherService();
	protected int[] generated;

	protected void generateVouchers() throws BusinessException {
		generated = service.generateVouchers();
	}

	protected void assertGenerated(int... expected) {
		assertArrayEquals(expected, generated);
	}

	protected void assertVoucherCount(String clientId, int expected) {
		assertEquals(expected, vouchersFor(clientId).size());
	}

	protected void assertVouchers(String clientId, int expected, String amount) {
		List<TPaymentMeansRecord> paymentMeans = vouchersFor(clientId);
		assertEquals(expected, paymentMeans.size());
		
		BigDecimal expectedAmount = new BigDecimal(amount);
		for (TPaymentMeansRecord paymentMean : paymentMeans) {
			TVouchersRecord voucher = Db.findById(TVouchersRecord.class, paymentMean.id);
			assertEquals(0, BigDecimal.ZERO.compareTo(paymentMean.accumulated));
			assertEquals(0, expectedAmount.compareTo(voucher.available));
		}
	}

	protected void assertVoucherDescriptions(String clientId, String description) {
		for (TPaymentMeansRecord paymentMean : vouchersFor(clientId)) {
			TVouchersRecord voucher = Db.findById(TVouchersRecord.class, paymentMean.id);
			assertEquals(description, voucher.description);
		}
	}

	protected void assertUsedWorkOrders(List<TWorkOrdersRecord> workOrders, int expectedUsed) {
		long used = workOrders.stream()
				.map(workOrder -> Db.findById(TWorkOrdersRecord.class, workOrder.id))
				.filter(workOrder -> workOrder.usedForVoucher)
				.count();
		assertEquals(expectedUsed, used);
	}

	protected void assertUsedRecommendations(List<TRecommendationsRecord> recommendations, int expectedUsed) {
		long used = recommendations.stream()
				.map(recommendation -> Db.findById(TRecommendationsRecord.class, recommendation.id))
				.filter(recommendation -> recommendation.usedForVoucher)
				.count();
		assertEquals(expectedUsed, used);
	}

	protected void assertInvoiceUsed(TInvoicesRecord invoice) {
		TInvoicesRecord reloaded = Db.findById(TInvoicesRecord.class, invoice.id);
		assertTrue(reloaded.usedForVoucher);
	}

	protected void assertInvoiceNotUsed(TInvoicesRecord invoice) {
		TInvoicesRecord reloaded = Db.findById(TInvoicesRecord.class, invoice.id);
		assertFalse(reloaded.usedForVoucher);
	}

	private List<TPaymentMeansRecord> vouchersFor(String clientId) {
		return Db.findAllBy(TPaymentMeansRecord.class, "CLIENT_ID", clientId).stream()
				.filter(paymentMean -> "Voucher".equals(paymentMean.dtype))
				.toList();
	}
}
