package uo.ri.cws.application.service.mechanic.crud.commands;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.MechanicRepository;
import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.cws.application.service.mechanic.crud.DtoAssembler;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Mechanic;
import uo.ri.cws.domain.types.FullName;
import uo.ri.cws.domain.types.Nif;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessException;

public class AddMechanic implements Command<MechanicDto> {

    private MechanicDto dto;
    private MechanicRepository repo = Factories.repository.forMechanic();

    public AddMechanic(MechanicDto dto) {
        ArgumentChecks.isNotNull(dto, "Mechanic DTO cannot be null");
        ArgumentChecks.isNotBlank(dto.nif, "NIF cannot be null or blank");
        ArgumentChecks.isNotBlank(dto.name, "Name cannot be null or blank");
        ArgumentChecks.isNotBlank(dto.surname, "Surname cannot be null or blank");
        this.dto = dto;
    }

    @Override
    public MechanicDto execute() throws BusinessException {
        Nif nif = new Nif(dto.nif);
        if (repo.findByNif(nif).isPresent()) {
            throw new BusinessException("A mechanic with NIF " + dto.nif + " already exists");
        }
        FullName fullName = new FullName(dto.name, dto.surname);
        Mechanic m = new Mechanic(nif, fullName);
        repo.add(m);
        return DtoAssembler.toDto(m);
    }

}
