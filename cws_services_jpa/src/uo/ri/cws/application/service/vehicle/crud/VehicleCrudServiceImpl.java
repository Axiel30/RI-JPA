package uo.ri.cws.application.service.vehicle.crud;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.vehicle.VehicleCrudService;
import uo.ri.cws.application.service.vehicle.crud.commands.FindVehicleByPlate;
import uo.ri.cws.application.util.command.CommandExecutor;
import uo.ri.util.exception.BusinessException;

public class VehicleCrudServiceImpl implements VehicleCrudService {

    private CommandExecutor executor = Factories.executor.forExecutor();

    @Override
    public Optional<VehicleDto> findByPlate(String plate) throws BusinessException {
        return executor.execute(new FindVehicleByPlate(plate));
    }

}
