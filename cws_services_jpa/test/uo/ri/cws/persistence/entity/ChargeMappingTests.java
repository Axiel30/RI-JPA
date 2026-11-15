package uo.ri.cws.persistence.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceException;
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
 * - All fields and the artificial identity of a charge are persisted
 * - A charge cannot be repeated for the same invoice and payment mean
 */
class ChargeMappingTests {

	private UnitOfWork unitOfWork;
	private EntityManagerFactory factory;
	private Client client;
	private Voucher voucher;
	private Invoice invoice;
	private Charge charge;

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

		invoice = new Invoice(1001L);
		charge = DomainTestFixturesFactory.newCharge(
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
	void testAllFieldsPersisted() {
		Charge restored = unitOfWork.findById(Charge.class, charge.getId());

		assertEquals(charge.getId(), restored.getId());
		assertEquals(invoice, restored.getInvoice());
		assertEquals(voucher, restored.getPaymentMean());
		assertEquals(charge.getAmount(), restored.getAmount());
	}

	@Test
	void testRepeated() {
		assertThrows(PersistenceException.class, () -> unitOfWork.execute(mapper -> {
			Invoice restoredInvoice = mapper.find(Invoice.class, invoice.getId());
			Voucher restoredVoucher = mapper.find(Voucher.class, voucher.getId());
			DomainTestFixturesFactory.newCharge(
					restoredInvoice,
					restoredVoucher,
					Money.of("5.0")
				);
		}));
	}
}
