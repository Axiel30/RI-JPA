package uo.ri.cws.application.service.mechanic.crud.commands;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.MechanicRepository;
import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Mechanic;
import uo.ri.cws.domain.types.FullName;
import uo.ri.cws.domain.types.Nif;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessException;

public class UpdateMechanic implements Command<Void> {

    private MechanicDto dto;
    private MechanicRepository repo = Factories.repository.forMechanic();

    public UpdateMechanic(MechanicDto dto) {
        ArgumentChecks.isNotNull(dto, "Mechanic DTO cannot be null");
        ArgumentChecks.isNotBlank(dto.id, "Mechanic ID cannot be null or blank");
        ArgumentChecks.isNotBlank(dto.nif, "NIF cannot be null or blank");
        ArgumentChecks.isNotBlank(dto.name, "Name cannot be null or blank");
        ArgumentChecks.isNotBlank(dto.surname, "Surname cannot be null or blank");
        this.dto = dto;
    }

    @Override
    public Void execute() throws BusinessException {
        Optional<Mechanic> om = repo.findById(dto.id);
        if (om.isEmpty()) {
            throw new BusinessException("Mechanic does not exist: " + dto.id);
        }
        Mechanic m = om.get();
        if (m.getVersion() != dto.version) {
            throw new BusinessException("Version mismatch: mechanic has been modified concurrently");
        }
        Nif newNif = new Nif(dto.nif);
        Optional<Mechanic> byNif = repo.findByNif(newNif);
        if (byNif.isPresent() && !byNif.get().getId().equals(m.getId())) {
            throw new BusinessException("A mechanic with NIF " + dto.nif + " already exists");
        }
        m.setNif(newNif);
        m.setFullName(new FullName(dto.name, dto.surname));
        return null;
    }

}
