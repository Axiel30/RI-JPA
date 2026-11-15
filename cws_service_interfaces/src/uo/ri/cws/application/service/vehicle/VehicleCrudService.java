package uo.ri.cws.application.service.vehicle;

import java.util.Optional;

import uo.ri.util.exception.BusinessException;

/**
 * This service is intended to be used by the Foreman
 * It follows the ISP principle (@see SOLID principles from RC Martin)
 */
public interface VehicleCrudService {

	/**
	 * @param plate number
	 * @return an Optional with the vehicle dto specified be the plate number
	 *
	 * @throws BusinessException, DOES NOT
	 * @throws IllegalArgumentException if the plate is null, or does not match the format
	 */
	Optional<VehicleDto> findByPlate(String plate) throws BusinessException;

	public static class VehicleDto {
		public String id;
		public long version;

		public String plate;
		public String make;
		public String model;

		public String clientId;
		public String vehicleTypeId;

	}

}
