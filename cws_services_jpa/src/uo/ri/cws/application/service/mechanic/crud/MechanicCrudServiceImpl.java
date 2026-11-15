package uo.ri.cws.application.service.mechanic.crud;

import java.util.List;
import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.mechanic.MechanicCrudService;
import uo.ri.cws.application.service.mechanic.crud.commands.AddMechanic;
import uo.ri.cws.application.service.mechanic.crud.commands.DeleteMechanic;
import uo.ri.cws.application.service.mechanic.crud.commands.FindAllMechanics;
import uo.ri.cws.application.service.mechanic.crud.commands.FindMechanicById;
import uo.ri.cws.application.service.mechanic.crud.commands.FindMechanicByNif;
import uo.ri.cws.application.service.mechanic.crud.commands.UpdateMechanic;
import uo.ri.cws.application.util.command.CommandExecutor;
import uo.ri.util.exception.BusinessException;

public class MechanicCrudServiceImpl implements MechanicCrudService {
    
    private CommandExecutor executor = Factories.executor.forExecutor();

    @Override
    public MechanicDto create(MechanicDto mecanico) throws BusinessException {
        return executor.execute( new AddMechanic( mecanico ) );
    }

    @Override
    public void update(MechanicDto mecanico) throws BusinessException {
        executor.execute( new UpdateMechanic( mecanico ) );
    }

    @Override
    public void delete(String idMecanico) throws BusinessException {
        executor.execute( new DeleteMechanic(idMecanico) );
    }

    @Override
    public List<MechanicDto> findAll() throws BusinessException {
        return executor.execute( new FindAllMechanics() );
    }

    @Override
    public Optional<MechanicDto> findById(String id) throws BusinessException {
        return executor.execute( new FindMechanicById(id) );
    }

    @Override
    public Optional<MechanicDto> findByNif(String nif)
            throws BusinessException {
        return executor.execute( new FindMechanicByNif(nif) );
    }

}
