package uo.ri.cws.persistence.association;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import uo.ri.cws.domain.Associations;
import uo.ri.cws.domain.Charge;
import uo.ri.cws.domain.Client;
import uo.ri.cws.domain.DomainTestFixturesFactory;
import uo.ri.cws.domain.Invoice;
import uo.ri.cws.domain.Voucher;
import uo.ri.cws.domain.types.Money;
import uo.ri.cws.domain.types.Nif;
import uo.ri.cws.domain.types.VoucherCode;
import uo.ri.cws.persistence.util.UnitOfWork;

/**
 * Scenarios:
 * - A charge recovers its payment mean after being persisted and reloaded
 * - Persisting an invoice cascades persistence to the charge
 */
class WithdrawsMappingTests {

	private UnitOfWork unitOfWork;
	private EntityManagerFactory factory;
	private Client client;
	private Voucher voucher;
	private Invoice invoice;

	@BeforeEach
	void setUp() {
		factory = Persistence.createEntityManagerFactory("carworkshop");
		unitOfWork = UnitOfWork.over(factory);

		client = new Client(new Nif("12345678Z"));
		voucher = new Voucher(
				new VoucherCode("voucher-code"),
				"voucher description",
				Money.of("100.0")
			);
		Associations.Holds.link(client, voucher);

		invoice = new Invoice(1L);
		DomainTestFixturesFactory.newCharge(
				invoice,
				voucher,
				Money.TEN
			);

		unitOfWork.persist(client, voucher, invoice);
	}

	@AfterEach
	void tearDown() {
		unitOfWork.remove(invoice, voucher, client);
		factory.close();
	}

	@Test
	void testChargeRecoversPaymentMean() {
		Invoice restoredInvoice = unitOfWork.findById(
				Invoice.class,
				invoice.getId()
			);
		Charge restored = restoredInvoice.getCharges().iterator().next();

		assertEquals(voucher, restored.getPaymentMean());
	}

	@Test
	void testInvoiceCascadesPersistenceToCharge() {
		Invoice restoredInvoice = unitOfWork.findById(
				Invoice.class,
				invoice.getId()
			);
		Charge restored = restoredInvoice.getCharges().iterator().next();

		assertNotNull(restored);
	}

}
