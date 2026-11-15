package uo.ri.cws.application.service.mechanic.crud.commands;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.MechanicRepository;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Mechanic;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessException;

public class DeleteMechanic implements Command<Void> {

    private String mechanicId;
    private MechanicRepository repo = Factories.repository.forMechanic();

    public DeleteMechanic(String mechanicId) {
        ArgumentChecks.isNotNull(mechanicId, "Mechanic ID cannot be null");
        ArgumentChecks.isNotBlank(mechanicId, "Mechanic ID cannot be blank");
        this.mechanicId = mechanicId;
    }

    @Override
    public Void execute() throws BusinessException {
        Optional<Mechanic> om = repo.findById(mechanicId);
        if (om.isEmpty()) {
            throw new BusinessException("Mechanic does not exist: " + mechanicId);
        }
        Mechanic m = om.get();
        if (!m.getAssigned().isEmpty()) {
            throw new BusinessException("Mechanic has assigned work orders");
        }
        if (Factories.query.forMechanic().hasInterventions(mechanicId)) {
            throw new BusinessException("Mechanic has interventions done");
        }
        repo.remove(m);
        return null;
    }

}
