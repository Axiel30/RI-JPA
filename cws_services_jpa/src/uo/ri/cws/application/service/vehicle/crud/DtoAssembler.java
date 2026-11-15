package uo.ri.cws.application.service.vehicle.crud;


import uo.ri.cws.application.service.vehicle.VehicleCrudService.VehicleDto;
import uo.ri.cws.domain.Vehicle;

public class DtoAssembler {

	public static VehicleDto toDto(Vehicle v) {
		VehicleDto dto = new VehicleDto();
		dto.id = v.getId();
		dto.version = v.getVersion();

		dto.plate = v.getLicensePlate().licensePlate();
		dto.clientId = v.getClient().getId();
		dto.make = v.getMake();
		dto.vehicleTypeId = v.getVehicleType().getId();
		dto.model = v.getModel();

		return dto;
	}

}
