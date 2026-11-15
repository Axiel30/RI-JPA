package uo.ri.cws.application.service.mechanic.crud.commands;

import java.util.List;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.MechanicRepository;
import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.cws.application.service.mechanic.crud.DtoAssembler;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Mechanic;

public class FindAllMechanics implements Command<List<MechanicDto>> {

    private MechanicRepository repo = Factories.repository.forMechanic();

    @Override
    public List<MechanicDto> execute() {
        List<Mechanic> mechanics = repo.findAll();
        return DtoAssembler.toMechanicDtoList(mechanics);
    }

}
