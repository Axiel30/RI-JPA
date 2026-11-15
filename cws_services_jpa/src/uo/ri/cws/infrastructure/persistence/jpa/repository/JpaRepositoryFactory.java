package uo.ri.cws.infrastructure.persistence.jpa.repository;

import uo.ri.cws.application.repository.ClientRepository;
import uo.ri.cws.application.repository.InvoiceRepository;
import uo.ri.cws.application.repository.MechanicRepository;
import uo.ri.cws.application.repository.PaymentMeanRepository;
import uo.ri.cws.application.repository.PersistenceFactory;
import uo.ri.cws.application.repository.RecommendationRepository;
import uo.ri.cws.application.repository.SparePartRepository;
import uo.ri.cws.application.repository.VehicleRepository;
import uo.ri.cws.application.repository.VehicleTypeRepository;
import uo.ri.cws.application.repository.WorkOrderRepository;

public class JpaRepositoryFactory implements PersistenceFactory {

	@Override
	public MechanicRepository forMechanic() {
		return new MechanicJpaRepository();
	}

	@Override
	public WorkOrderRepository forWorkOrder() {
		return new WorkOrderJpaRepository();
	}

	@Override
	public PaymentMeanRepository forPaymentMean() {
		return new PaymentMeanJpaRepository();
	}

	@Override
	public RecommendationRepository forRecommendation() {
		return new RecommendationJpaRepository();
	}

	@Override
	public InvoiceRepository forInvoice() {
		return new InvoiceJpaRepository();
	}

	@Override
	public ClientRepository forClient() {
		return new ClientJpaRepository();
	}

	@Override
	public SparePartRepository forSparePart() {
		return new SparePartJpaRepository();
	}

	@Override
	public VehicleRepository forVehicle() {
		return new VehicleJpaRepository();
	}

	@Override
	public VehicleTypeRepository forVehicleType() {
		return new VehicleTypeJpaRepository();
	}

}
