package uo.ri.cws.application.repository;

import java.util.Optional;

import uo.ri.cws.domain.Vehicle;
import uo.ri.cws.domain.types.LicensePlate;

public interface VehicleRepository extends Repository<Vehicle> {

	Optional<Vehicle> findByPlate(LicensePlate plate);

}
