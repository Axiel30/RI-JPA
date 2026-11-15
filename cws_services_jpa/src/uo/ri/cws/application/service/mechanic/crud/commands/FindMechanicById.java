package uo.ri.cws.application.service.mechanic.crud.commands;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.MechanicRepository;
import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.cws.application.service.mechanic.crud.DtoAssembler;
import uo.ri.cws.application.util.command.Command;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessException;

public class FindMechanicById implements Command<Optional<MechanicDto>> {

    private String id;
    private MechanicRepository repo = Factories.repository.forMechanic();

    public FindMechanicById(String id) {
        ArgumentChecks.isNotNull(id, "Mechanic ID cannot be null");
        this.id = id;
    }

    @Override
    public Optional<MechanicDto> execute() throws BusinessException {
        return repo.findById(id).map(DtoAssembler::toDto);
    }

}
