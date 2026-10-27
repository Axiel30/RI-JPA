package uo.ri.cws.domain;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import uo.ri.cws.domain.types.Money;

/**
 * Test doubles for isolating one domain operation from its collaborators.
 */
final class DomainTestMocksFactory {

	private DomainTestMocksFactory() {
	}

	static WorkOrder oneApprovedWorkOrder(Money amount) {
		WorkOrder workOrder = mock(WorkOrder.class);
		AtomicReference<Invoice> invoice = new AtomicReference<>();

		when(workOrder.isApproved()).thenReturn(true);
		when(workOrder.getAmount()).thenReturn(amount);
		when(workOrder.getInvoice()).thenAnswer(invocation -> invoice.get());

		// Test-only backdoor: emulate the association end maintained by Bills.
		doAnswer(invocation -> {
			invoice.set(invocation.getArgument(0));
			return null;
		}).when(workOrder)._setInvoice( any() );

		return workOrder;
	}

	static WorkOrder oneWorkOrder() {
		WorkOrder workOrder = mock(WorkOrder.class);
		AtomicReference<Vehicle> vehicle = new AtomicReference<>();
		AtomicReference<Mechanic> mechanic = new AtomicReference<>();
		AtomicReference<Invoice> invoice = new AtomicReference<>();

		when(workOrder.getVehicle()).thenAnswer(invocation -> vehicle.get());
		when(workOrder.getMechanic()).thenAnswer(invocation -> mechanic.get());
		when(workOrder.getInvoice()).thenAnswer(invocation -> invoice.get());

		// Test-only backdoors: emulate the association ends maintained by Associations.
		doAnswer(invocation -> {
			vehicle.set(invocation.getArgument(0));
			return null;
		}).when(workOrder)._setVehicle( any() );
		
		doAnswer(invocation -> {
			mechanic.set(invocation.getArgument(0));
			return null;
		}).when(workOrder)._setMechanic( any() );
		
		doAnswer(invocation -> {
			invoice.set(invocation.getArgument(0));
			return null;
		}).when(workOrder)._setInvoice( any() );

		return workOrder;
	}

	static Cash oneCash() {
		Cash cash = mock(Cash.class);
		AtomicReference<Client> client = new AtomicReference<>();

		when(cash.getClient()).thenAnswer(invocation -> client.get());
		
		// Test-only backdoor: emulate the association end maintained by Holds.
		doAnswer(invocation -> {
			client.set(invocation.getArgument(0));
			return null;
		}).when(cash)._setClient( any() );

		return cash;
	}

	static Charge oneCharge() {
		Charge charge = mock(Charge.class);
		AtomicReference<Invoice> invoice = new AtomicReference<>();
		AtomicReference<PaymentMean> paymentMean = new AtomicReference<>();

		when(charge.getInvoice()).thenAnswer(invocation -> invoice.get());
		when(charge.getPaymentMean()).thenAnswer(invocation -> paymentMean.get());
		
		// Test-only backdoors: emulate the association ends maintained by SettlesWithdraws.
		doAnswer(invocation -> {
			invoice.set(invocation.getArgument(0));
			return null;
		}).when(charge)._setInvoice( any() );
		
		doAnswer(invocation -> {
			paymentMean.set(invocation.getArgument(0));
			return null;
		}).when(charge)._setPaymentMean( any() );

		return charge;
	}

	static Intervention oneInterventionWithAmount(Money amount) {
		Intervention intervention = oneIntervention();
		when(intervention.getTotalAmount()).thenReturn(amount);
		return intervention;
	}

	static Intervention oneIntervention() {
		Intervention intervention = mock(Intervention.class);
		AtomicReference<WorkOrder> workOrder = new AtomicReference<>();
		AtomicReference<Mechanic> mechanic = new AtomicReference<>();
		Set<Substitution> substitutions = new HashSet<>();

		when(intervention.getWorkOrder()).thenAnswer(invocation -> workOrder.get());
		when(intervention.getMechanic()).thenAnswer(invocation -> mechanic.get());
		when(intervention._getSubstitutions()).thenReturn(substitutions);
		when(intervention.getSubstitutions()).thenAnswer(invocation -> new HashSet<>(substitutions));

		// Test-only backdoors: emulate the association ends maintained by Associations.
		doAnswer(invocation -> {
			workOrder.set(invocation.getArgument(0));
			return null;
		}).when(intervention)._setWorkOrder( any() );
		
		doAnswer(invocation -> {
			mechanic.set(invocation.getArgument(0));
			return null;
		}).when(intervention)._setMechanic( any() );

		return intervention;
	}

	static Substitution oneSubstitution() {
		Substitution substitution = mock(Substitution.class);
		AtomicReference<Intervention> intervention = new AtomicReference<>();
		AtomicReference<SparePart> sparePart = new AtomicReference<>();

		when(substitution.getIntervention())
				.thenAnswer(invocation -> intervention.get());
		when(substitution.getSparePart())
				.thenAnswer(invocation -> sparePart.get());

		// Test-only backdoors: emulate the association ends maintained by PerformsSubstitutes.
		doAnswer(invocation -> {
			intervention.set(invocation.getArgument(0));
			return null;
		}).when(substitution)._setIntervention( any() );
		
		doAnswer(invocation -> {
			sparePart.set(invocation.getArgument(0));
			return null;
		}).when(substitution)._setSparePart( any() );

		return substitution;
	}
}
