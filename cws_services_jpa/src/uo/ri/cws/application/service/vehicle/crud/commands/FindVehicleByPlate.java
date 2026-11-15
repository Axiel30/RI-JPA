package uo.ri.cws.application.service.vehicle.crud.commands;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.VehicleRepository;
import uo.ri.cws.application.service.vehicle.VehicleCrudService.VehicleDto;
import uo.ri.cws.application.service.vehicle.crud.DtoAssembler;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.types.LicensePlate;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessException;

public class FindVehicleByPlate implements Command<Optional<VehicleDto>> {

    private LicensePlate plate;
    private VehicleRepository repo = Factories.repository.forVehicle();

    public FindVehicleByPlate(String plate) {
        ArgumentChecks.isNotBlank(plate, "Plate cannot be null or blank");
        this.plate = new LicensePlate(plate);
    }

    @Override
    public Optional<VehicleDto> execute() throws BusinessException {
        return repo.findByPlate(plate).map(DtoAssembler::toDto);
    }

}
