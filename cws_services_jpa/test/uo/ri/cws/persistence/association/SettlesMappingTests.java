package uo.ri.cws.persistence.association;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import uo.ri.cws.domain.Associations;
import uo.ri.cws.domain.Cash;
import uo.ri.cws.domain.Charge;
import uo.ri.cws.domain.Client;
import uo.ri.cws.domain.CreditCard;
import uo.ri.cws.domain.Invoice;
import uo.ri.cws.domain.Mechanic;
import uo.ri.cws.domain.PaymentMean;
import uo.ri.cws.domain.Vehicle;
import uo.ri.cws.domain.VehicleType;
import uo.ri.cws.domain.Voucher;
import uo.ri.cws.domain.WorkOrder;
import uo.ri.cws.domain.types.CreditCardNumber;
import uo.ri.cws.domain.types.LicensePlate;
import uo.ri.cws.domain.types.Money;
import uo.ri.cws.domain.types.Nif;
import uo.ri.cws.domain.types.VoucherCode;
import uo.ri.cws.persistence.util.UnitOfWork;

/**
 * Scenarios:
 * - An invoice recovers all its charges after being persisted and reloaded
 * - A charge for cash recovers its invoice and payment mean after being persisted and reloaded
 * - A charge for voucher recovers its invoice and payment mean after being persisted and reloaded
 * - A charge for card recovers its invoice and payment mean after being persisted and reloaded
 * - Unsettling an invoice deletes all its charges as orphans
 */
class SettlesMappingTests {

	private UnitOfWork unitOfWork;
	private EntityManagerFactory factory;
	private Invoice invoice;
	private Client client;
	private VehicleType vehicleType;
	private Vehicle vehicle;
	private Mechanic mechanic;
	private WorkOrder workOrder;
	private CreditCard card;
	private Voucher voucher;
	private Cash cash;
	private static final Money ONE_HUNDRED = Money.of("100.0");


	@BeforeEach
	void setUp() {
		factory = Persistence.createEntityManagerFactory("carworkshop");
		unitOfWork = UnitOfWork.over( factory );

		vehicleType = new VehicleType("car", Money.of("50.0"));
		vehicle = new Vehicle(new LicensePlate("1234 GJK"), "seat", "ibiza");
		Associations.Classifies.link(vehicleType, vehicle);
		mechanic = new Mechanic(new Nif("87654321X"));
		workOrder = new WorkOrder(vehicle);
		workOrder.assignTo(mechanic);
		workOrder.addIntervention(LocalDateTime.now(), 60);
		workOrder.finish();
		workOrder.approve();

		client = new Client(new Nif("12345678Z"));
		invoice = new Invoice( 1L );
		invoice.addWorkOrder(workOrder);
		card = new CreditCard(
				new CreditCardNumber("4111111111111111")
				, "visa"
				, YearMonth.now().plusMonths(1)
			);

		voucher = new Voucher(
				new VoucherCode("code")
				, "a voucher"
				, ONE_HUNDRED);
		cash = new Cash( client );

		Associations.Holds.link(client, card);
		Associations.Holds.link(client, voucher);
		// cash automatically linked

		Money[] amounts = Money.calc(invoice.getTotal()).allocate(3);
		invoice.settle(Map.of(
				cash, amounts[0],
				card, amounts[1],
				voucher, amounts[2]
			));

		unitOfWork.persist(
				vehicleType,
				vehicle,
				mechanic,
				workOrder,
				client,
				cash,
				voucher,
				card,
				invoice
			);
	}

	@AfterEach
	void tearDown() {
		unitOfWork.remove(
				invoice,
				workOrder,
				vehicle,
				mechanic,
				vehicleType,
				cash,
				voucher,
				card,
				client
			);
		factory.close();
	}

	/**
	 * GIVEN: an invoice with three charges (cash, card and voucher), all persisted
	 * WHEN: the invoice is reloaded from the database
	 * THEN: the reloaded invoice's charges contains all three charges
	 */
	@Test
	void testInvoiceRecoversCharges() {

		Invoice restored = unitOfWork.findById( Invoice.class, invoice.getId() );

		assertFalse( restored.getCharges().isEmpty() );
		assertEquals( 3, restored.getCharges().size() );
	}

	/**
	 * GIVEN: a charge made with cash for an invoice, persisted
	 * WHEN: the charge is reloaded from the database
	 * THEN: the reloaded charge's invoice equals the given invoice
	 * AND: the reloaded charge's payment mean equals the given cash
	 */
	@Test
	void testChargeForCashRecoversCharges() {
		Invoice restoredInvoice = unitOfWork.findById(
				Invoice.class,
				invoice.getId()
			);
		Charge restored = findChargeFor(restoredInvoice, cash);

		assertEquals( invoice, restored.getInvoice() );
		assertEquals( cash, restored.getPaymentMean() );
	}

	/**
	 * GIVEN: a charge made with a voucher for an invoice, persisted
	 * WHEN: the charge is reloaded from the database
	 * THEN: the reloaded charge's invoice equals the given invoice
	 * AND: the reloaded charge's payment mean equals the given voucher
	 */
	@Test
	void testChargeForVoucherRecoversCharges() {
		Invoice restoredInvoice = unitOfWork.findById(
				Invoice.class,
				invoice.getId()
			);
		Charge restored = findChargeFor(restoredInvoice, voucher);

		assertEquals( invoice, restored.getInvoice() );
		assertEquals( voucher, restored.getPaymentMean() );
	}

	/**
	 * GIVEN: a charge made with a credit card for an invoice, persisted
	 * WHEN: the charge is reloaded from the database
	 * THEN: the reloaded charge's invoice equals the given invoice
	 * AND: the reloaded charge's payment mean equals the given card
	 */
	@Test
	void testChargeForCardRecoversCharges() {
		Invoice restoredInvoice = unitOfWork.findById(
				Invoice.class,
				invoice.getId()
			);
		Charge restored = findChargeFor(restoredInvoice, card);

		assertEquals( invoice, restored.getInvoice() );
		assertEquals( card, restored.getPaymentMean() );
	}

	/**
	 * GIVEN: a settled invoice whose charges were persisted by cascade
	 * WHEN: the persisted invoice is unsettled
	 * THEN: every charge is deleted by orphan removal
	 */
	@Test
	void testUnSettleDeletesChargeOrphans() {
		unitOfWork.execute(mapper -> {
			Invoice restored = mapper.find(Invoice.class, invoice.getId());
			restored.unSettle();
		});

		Invoice restored = unitOfWork.findById(Invoice.class, invoice.getId());
		assertTrue(restored.getCharges().isEmpty());
	}

	private Charge findChargeFor(Invoice anInvoice, PaymentMean paymentMean) {
		return anInvoice.getCharges().stream()
				.filter(charge -> charge.getPaymentMean().equals(paymentMean))
				.findFirst()
				.orElseThrow();
	}
}
