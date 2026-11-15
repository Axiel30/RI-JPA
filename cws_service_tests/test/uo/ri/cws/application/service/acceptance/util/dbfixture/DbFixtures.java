package uo.ri.cws.application.service.acceptance.util.dbfixture;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import uo.ri.cws.application.service.acceptance.util.dbfixture.builders.TCashesRecordBuilder;
import uo.ri.cws.application.service.acceptance.util.dbfixture.builders.TChargesRecordBuilder;
import uo.ri.cws.application.service.acceptance.util.dbfixture.builders.TClientsRecordBuilder;
import uo.ri.cws.application.service.acceptance.util.dbfixture.builders.TCreditCardsRecordBuilder;
import uo.ri.cws.application.service.acceptance.util.dbfixture.builders.TInterventionsRecordBuilder;
import uo.ri.cws.application.service.acceptance.util.dbfixture.builders.TInvoicesRecordBuilder;
import uo.ri.cws.application.service.acceptance.util.dbfixture.builders.TMechanicsRecordBuilder;
import uo.ri.cws.application.service.acceptance.util.dbfixture.builders.TPaymentMeansRecordBuilder;
import uo.ri.cws.application.service.acceptance.util.dbfixture.builders.TRecommendationsRecordBuilder;
import uo.ri.cws.application.service.acceptance.util.dbfixture.builders.TVehicleTypesRecordBuilder;
import uo.ri.cws.application.service.acceptance.util.dbfixture.builders.TVehiclesRecordBuilder;
import uo.ri.cws.application.service.acceptance.util.dbfixture.builders.TVouchersRecordBuilder;
import uo.ri.cws.application.service.acceptance.util.dbfixture.builders.TWorkOrdersRecordBuilder;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TCashesRecord;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TChargesRecord;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TClientsRecord;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TCreditCardsRecord;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TInterventionsRecord;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TInvoicesRecord;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TMechanicsRecord;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TPaymentMeansRecord;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TRecommendationsRecord;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TVehicleTypesRecord;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TVehiclesRecord;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TVouchersRecord;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TWorkOrdersRecord;

public class DbFixtures {
	public static final boolean PAID = true;
	public static final boolean NOT_PAID = false;
	public static final boolean USED_FOR_VOUCHER = true;
	public static final boolean NOT_USED_FOR_VOUCHER = false;

	public static TMechanicsRecord aMechanic() {
		TMechanicsRecord mechanic = new TMechanicsRecordBuilder().build();
		Db.insert(mechanic);
		return mechanic;
	}

	public static TMechanicsRecord aMechanicOf(String nif) {
		
		TMechanicsRecord mechanic = new TMechanicsRecordBuilder()
				.withNif(nif)
				.build();
		Db.insert(mechanic);
		return mechanic;
	}

	public static TMechanicsRecord aMechanicOf(String nif, 
			String name, String surname) {
		
		TMechanicsRecord mechanic = new TMechanicsRecordBuilder()
				.withNif(nif)
				.withName(name)
				.withSurname(surname)
				.build();
		Db.insert(mechanic);
		return mechanic;
	}

	public static TMechanicsRecord aMechanicWithWorkOrders() {
		TMechanicsRecord m = aMechanic();
		aWorkOrderForMechanic(m.id);
		aWorkOrderForMechanic(m.id);
		return m;
	}

	public static TMechanicsRecord aMechanicWithInterventions() {
		TMechanicsRecord m = aMechanic();
		anInterventionForMechanic(m.id);
		anInterventionForMechanic(m.id);
		return m;
	}

	public static TInterventionsRecord anInterventionForMechanic(String mId) {
		TInterventionsRecord i = new TInterventionsRecordBuilder()
				.forMechanicId( mId )
				.build();
		
		Db.insert( i );
		return i;
	}

	public static TWorkOrdersRecord aWorkOrderForMechanic(String mId) {
		TWorkOrdersRecord wo1 = new TWorkOrdersRecordBuilder()
				.forMechanicId(mId)
				.build();
		
		Db.insert( wo1 );
		return wo1;
	}

	public static TWorkOrdersRecord aClientWithVehicleAndOneApprovedWorkorder() {
		TClientsRecord client = aClient();
		TVehiclesRecord vehicle = aVehicleForClient(client.id);
		TWorkOrdersRecord wo = aWorkOrderApprovedForVehicle(vehicle.id);
		return wo;
	}

	public static List<TWorkOrdersRecord> someWorkOrdersApprovedWithVehicleAndClient() {
		TClientsRecord client = aClient();
		return someWorkOrdersApprovedWithVehicleForClient(client.id);
	}
	
	
	public static List<TWorkOrdersRecord> someWorkOrdersFinishedWithVehicleAndClient() {
		TClientsRecord client = aClient();
		return someWorkOrdersFinishedWithVehicleForClient(client.id);
	}

	public static List<TWorkOrdersRecord> someWorkOrdersFinishedWithVehicleForClient(
			String clientId) {

		TVehiclesRecord vehicle = aVehicleForClient(clientId);
		return List.of(
				aWorkOrderApprovedForVehicle(vehicle.id),
				aWorkOrderApprovedForVehicle(vehicle.id),
				aWorkOrderApprovedForVehicle(vehicle.id)
			);
	}

	public static List<TWorkOrdersRecord> someWorkOrdersApprovedWithVehicleForClient(
			String clientId) {

		TVehiclesRecord vehicle = aVehicleForClient(clientId);
		return List.of(
				aWorkOrderApprovedForVehicle(vehicle.id),
				aWorkOrderApprovedForVehicle(vehicle.id),
				aWorkOrderApprovedForVehicle(vehicle.id)
			);
	}

	
	public static TWorkOrdersRecord aWorkOrderApprovedForVehicle(String vId) {
		TWorkOrdersRecord wo = aWorkOrderApprovedForVehicleOf(vId, new BigDecimal("100.0"));
		return wo;
	}

	public static TWorkOrdersRecord aWorkOrderOpenForVehicle(String vId) {
		return aWorkOrderForVehicleOf(vId, "OPEN");
	}

	public static TWorkOrdersRecord aWorkOrderInvoicedForVehicle(String vId) {
		return aWorkOrderForVehicleOf(vId, "INVOICED");
	}

	public static TWorkOrdersRecord aWorkOrderAssignedForVehicle(String vId) {
		sleep(10); // to ensure different timestamps
		
		TMechanicsRecord m = aMechanic();
		TWorkOrdersRecord wo = new TWorkOrdersRecordBuilder()
				.forVehicleId(vId)
				.forMechanicId(m.id)
				.withState("ASSIGNED")
				.build();
		Db.insert(wo);
		return wo;
	}

	public static TWorkOrdersRecord aWorkOrderFinishedForVehicle(String vId) {
		sleep(10); // to ensure different timestamps
		
		TWorkOrdersRecord wo = new TWorkOrdersRecordBuilder()
				.forVehicleId(vId)
				.withState("FINISHED")
				.build();
		Db.insert(wo);
		return wo;
	}
	
	public static TWorkOrdersRecord aWorkOrderApprovedForVehicleOf(String vId,
			BigDecimal bigDecimal) {
		
		sleep(10); // to ensure different timestamps
		TWorkOrdersRecord wo = new TWorkOrdersRecordBuilder()
				.forVehicleId(vId)
				.withState( "APPROVED" )
				.withAmount( bigDecimal )
				.build();
		Db.insert(wo);
		return wo;
	}
	
	public static TWorkOrdersRecord aWorkOrderForVehicleOf(String vId,
			String state) {
		
		sleep(10); // to ensure different timestamps
		TWorkOrdersRecord wo = new TWorkOrdersRecordBuilder()
				.forVehicleId(vId)
				.withState( state )
				.build();
		Db.insert(wo);
		return wo;
	}

	public static TVehiclesRecord aVehicleForClient(String id) {
		TVehicleTypesRecord vt = aVehicleType();
		TVehiclesRecord vehicle = new TVehiclesRecordBuilder()
				.forClientId(id)
				.forVehicleTypeId(vt.id)
				.build();
		Db.insert(vehicle);
		return vehicle;
	}

	public static TVehicleTypesRecord aVehicleType() {
		TVehicleTypesRecord vt = new TVehicleTypesRecordBuilder().build();
		Db.insert(vt);
		return vt;
	}

	public static TClientsRecord aClient() {
		TClientsRecord client = new TClientsRecordBuilder().build();
		Db.insert(client);
		return client;
	}

	public static List<TClientsRecord> someClients() {
		return List.of(aClient(), aClient(), aClient());
	}

	public static TClientsRecord aClientWithCash() {
		TClientsRecord client = aClient();
		aCashForClient(client.id);
		return client;
	}

	public static TPaymentMeansRecord aCashForClient(String clientId) {
		TPaymentMeansRecord paymentMean = new TPaymentMeansRecordBuilder()
				.forClientId(clientId)
				.withDtype("Cash")
				.build();
		TCashesRecord cash = new TCashesRecordBuilder()
				.withId(paymentMean.id)
				.build();

		Db.insert(paymentMean);
		Db.insert(cash);
		return paymentMean;
	}

	public static TPaymentMeansRecord aCreditCardForClient(String clientId) {
		TPaymentMeansRecord paymentMean = new TPaymentMeansRecordBuilder()
				.forClientId(clientId)
				.withDtype("CreditCard")
				.build();
		TCreditCardsRecord card = new TCreditCardsRecordBuilder()
				.withId(paymentMean.id)
				.build();

		Db.insert(paymentMean);
		Db.insert(card);
		return paymentMean;
	}

	public static TPaymentMeansRecord aCreditCardForClientValidThru(
			String clientId, YearMonth validThru) {
		TPaymentMeansRecord paymentMean = aCreditCardForClient(clientId);
		TCreditCardsRecord card = Db.findById(TCreditCardsRecord.class, paymentMean.id);
		card.validThru = java.sql.Date.valueOf(validThru.atEndOfMonth());
		Db.update(card);
		return paymentMean;
	}

	public static TPaymentMeansRecord aVoucherForClient(String clientId) {
		return aVoucherForClientOf(
				clientId,
				new BigDecimal("100.00"),
				new BigDecimal("100.00"),
				"Default Voucher"
			);
	}

	public static TPaymentMeansRecord aVoucherForClientOf(
			String clientId,
			BigDecimal total,
			BigDecimal available,
			String description) {

		BigDecimal consumed = total.subtract(available);
		TPaymentMeansRecord paymentMean = new TPaymentMeansRecordBuilder()
				.forClientId(clientId)
				.withDtype("Voucher")
				.withAccumulated(consumed)
				.build();
		TVouchersRecord voucher = new TVouchersRecordBuilder()
				.withId(paymentMean.id)
				.withAvailable(available)
				.withDescription(description)
				.build();

		Db.insert(paymentMean);
		Db.insert(voucher);
		return paymentMean;
	}

	public static List<TPaymentMeansRecord> somePaymentMeansForClient(String clientId) {
		return List.of(
				aCashForClient(clientId),
				aCreditCardForClient(clientId),
				aCreditCardForClient(clientId),
				aVoucherForClient(clientId),
				aVoucherForClient(clientId)
			);
	}

	public static TPaymentMeansRecord aChargedCreditCardForClient(String clientId) {
		TPaymentMeansRecord paymentMean = aCreditCardForClient(clientId);
		aPaidInvoiceForPaymentMean(paymentMean);
		return paymentMean;
	}

	public static TRecommendationsRecord aRecommendationForClients(
			String recommenderId, String recommendedId) {
		return aRecommendationForClients(recommenderId, recommendedId, NOT_USED_FOR_VOUCHER);
	}

	public static TRecommendationsRecord aRecommendationForClients(
			String recommenderId,
			String recommendedId,
			boolean usedForVoucher) {

		TRecommendationsRecord recommendation = new TRecommendationsRecordBuilder()
				.forRecommenderId(recommenderId)
				.forRecommendedId(recommendedId)
				.withUsedForVoucher(usedForVoucher)
				.build();
		Db.insert(recommendation);
		return recommendation;
	}

	public static List<TRecommendationsRecord> someRecommendationsForClientOf(
			String recommenderId,
			int recommendationsWithPaidInvoice,
			int recommendationsWithoutPaidInvoice,
			boolean usedForVoucher) {

		List<TRecommendationsRecord> result = new ArrayList<>();
		for (int i = 0; i < recommendationsWithPaidInvoice; i++) {
			TClientsRecord recommended = aClient();
			anInvoiceForClientOf(recommended.id, new BigDecimal("100.00"), PAID, NOT_USED_FOR_VOUCHER);
			result.add(aRecommendationForClients(recommenderId, recommended.id, usedForVoucher));
		}
		for (int i = 0; i < recommendationsWithoutPaidInvoice; i++) {
			TClientsRecord recommended = aClient();
			anInvoiceForClientOf(recommended.id, new BigDecimal("100.00"), NOT_PAID, NOT_USED_FOR_VOUCHER);
			result.add(aRecommendationForClients(recommenderId, recommended.id, usedForVoucher));
		}
		return result;
	}

	public static List<TWorkOrdersRecord> someWorkOrdersInvoicedForClientOf(
			String clientId,
			int paidInvoices,
			int unpaidInvoices,
			boolean usedForVoucher) {

		List<TWorkOrdersRecord> result = new ArrayList<>();
		for (int i = 0; i < paidInvoices; i++) {
			result.add(
				aWorkOrderInvoicedForClientOf(clientId, new BigDecimal("100.00"), PAID, usedForVoucher)
			  );
		}
		for (int i = 0; i < unpaidInvoices; i++) {
			result.add(
				aWorkOrderInvoicedForClientOf(clientId, new BigDecimal("100.00"), NOT_PAID, usedForVoucher)
			 );
		}
		return result;
	}

	public static TInvoicesRecord anInvoiceForClientOf(
			String clientId,
			BigDecimal amount,
			boolean paid,
			boolean usedForVoucher) {

		TWorkOrdersRecord workOrder = aWorkOrderInvoicedForClientOf(clientId, amount, paid, NOT_USED_FOR_VOUCHER);
		TInvoicesRecord invoice = Db.findById(TInvoicesRecord.class, workOrder.invoice_Id);
		invoice.usedForVoucher = usedForVoucher;
		Db.update(invoice);
		return invoice;
	}

	public static TInvoicesRecord anInvoiceIssuedForClientOf(
			String clientId, BigDecimal subtotal) {
		
		BigDecimal vat = subtotal
				.multiply(new BigDecimal("0.21"))
				.setScale(2, RoundingMode.HALF_EVEN);
		
		BigDecimal total = subtotal.add(vat);
		
		TInvoicesRecord invoice = new TInvoicesRecordBuilder()
				.withNumber(Math.abs(UUID.randomUUID().getMostSignificantBits()))
				.withSubtotal(subtotal)
				.withVatRate(new BigDecimal("21"))
				.withVatAmount(vat)
				.withTotal(total)
				.build();
		Db.insert(invoice);

		TVehiclesRecord vehicle = aVehicleForClient(clientId);
		TWorkOrdersRecord workOrder = new TWorkOrdersRecordBuilder()
				.forVehicleId(vehicle.id)
				.forInvoiceId(invoice.id)
				.withAmount(subtotal)
				.invoiced()
				.build();
		Db.insert(workOrder);
		return invoice;
	}

	public static TWorkOrdersRecord aWorkOrderInvoicedForClientOf(
			String clientId,
			BigDecimal invoiceAmount,
			boolean paid,
			boolean usedForVoucher) {

		TInvoicesRecordBuilder invoiceBuilder = new TInvoicesRecordBuilder()
				.withNumber(Math.abs(UUID.randomUUID().getMostSignificantBits()))
				.withSubtotal(invoiceAmount)
				.withTotal(invoiceAmount);
		if (paid) {
			invoiceBuilder.paid();
		} else {
			invoiceBuilder.notPaid();
		}
		TInvoicesRecord invoice = invoiceBuilder.build();
		Db.insert(invoice);

		if (paid) {
			TPaymentMeansRecord cash = aCashForClient(clientId);
			cash.accumulated = invoiceAmount;
			Db.update(cash);
			TChargesRecord charge = new TChargesRecordBuilder()
					.withAmount(invoiceAmount)
					.forInvoiceId(invoice.id)
					.forPaymentMeanId(cash.id)
					.build();
			Db.insert(charge);
		}

		TVehiclesRecord vehicle = aVehicleForClient(clientId);
		TWorkOrdersRecord workOrder = new TWorkOrdersRecordBuilder()
				.forVehicleId(vehicle.id)
				.forInvoiceId(invoice.id)
				.withAmount(invoiceAmount)
				.withUsedForVoucher(usedForVoucher)
				.invoiced()
				.build();
		Db.insert(workOrder);
		return workOrder;
	}

	public static TClientsRecord aClientRecommendedBy(String recommenderId) {
		TClientsRecord recommended = aClient();
		aRecommendationForClients(recommenderId, recommended.id);
		return recommended;
	}

	public static List<TClientsRecord> someClientsRecommendedBy(String recommenderId) {
		return List.of(
				aClientRecommendedBy(recommenderId),
				aClientRecommendedBy(recommenderId),
				aClientRecommendedBy(recommenderId)
			);
	}

	public static TClientsRecord aClientWithMadeAndReceivedRecommendations() {
		TClientsRecord client = aClientWithCash();
		TClientsRecord recommender = aClient();
		TClientsRecord recommended = aClient();

		aRecommendationForClients(recommender.id, client.id);
		aRecommendationForClients(client.id, recommended.id);
		return client;
	}

	public static TClientsRecord aClientWithPaidInvoice() {
		TClientsRecord client = aClient();
		TPaymentMeansRecord cash = aCashForClient(client.id);
		aPaidInvoiceForPaymentMean(cash);
		return client;
	}

	public static TInvoicesRecord aPaidInvoiceForPaymentMean(TPaymentMeansRecord paymentMean) {

		BigDecimal amount = new BigDecimal("100.00");
		TInvoicesRecord invoice = new TInvoicesRecordBuilder()
				.withNumber(Math.abs(UUID.randomUUID().getMostSignificantBits()))
				.withSubtotal(amount)
				.withTotal(amount)
				.paid()
				.build();
		TChargesRecord charge = new TChargesRecordBuilder()
				.withAmount(amount)
				.forInvoiceId(invoice.id)
				.forPaymentMeanId(paymentMean.id)
				.build();

		paymentMean.accumulated = amount;
		Db.update(paymentMean);
		Db.insert(invoice);
		Db.insert(charge);
		return invoice;
	}

	private static void sleep(int miliseconds) {
		try {
			Thread.sleep(miliseconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static List<TWorkOrdersRecord> someWorkOrdersInvoicedWithInterventionsForMechanicOf(
	        String mechanicId,
	        BigDecimal invoicedAmount,
	        LocalDate firstDayOfLastMonth) {

	    BigDecimal third = invoicedAmount
	            .divide(BigDecimal.valueOf(3), 2, RoundingMode.HALF_EVEN);

	    return List.of(
	            aWorkOrderInvoicedWithInterventionForMechanicOf(
	                    mechanicId,
	                    third,
	                    firstDayOfLastMonth.plusDays(1)
	            ),
	            aWorkOrderInvoicedWithInterventionForMechanicOf(
	                    mechanicId,
	                    third,
	                    firstDayOfLastMonth.plusDays(10)
	            ),
	            aWorkOrderInvoicedWithInterventionForMechanicOf(
	                    mechanicId,
	                    third,
	                    firstDayOfLastMonth.plusDays(20)
	            )
	    );
	}
	
	public static TWorkOrdersRecord aWorkOrderInvoicedWithInterventionForMechanicOf(
			String mechanicId, 
			BigDecimal amount,
			LocalDate date) {
		TVehiclesRecord v = aVehicleForClient( aClient().id );
		Timestamp invoiceDate = Timestamp.valueOf(date.atTime(12, 30));
		TWorkOrdersRecord wo = new TWorkOrdersRecordBuilder()
				.forVehicleId(v.id)
				.withAmount(amount)
				.withDate( invoiceDate )
				.invoiced()
				.build();
		Db.insert( wo );
		anInterventionForMechanicAndWorkOrder(mechanicId, wo.id, invoiceDate);
		return wo;
	}

	public static TInterventionsRecord anInterventionForMechanicAndWorkOrder(
			String mechanicId, String workOrderId, Timestamp invoiceDate) {

		TInterventionsRecord i = new TInterventionsRecordBuilder()
				.forMechanicId( mechanicId )
				.forWorkOrderId( workOrderId )
				.withMinutes( 60 )
				.withDate( invoiceDate )
				.build();		
		Db.insert( i );
		return i;
	}


}
