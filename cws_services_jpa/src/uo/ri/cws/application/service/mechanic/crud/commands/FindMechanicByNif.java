package uo.ri.cws.application.service.mechanic.crud.commands;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.MechanicRepository;
import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.cws.application.service.mechanic.crud.DtoAssembler;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.types.Nif;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessException;

public class FindMechanicByNif implements Command<Optional<MechanicDto>> {

    private Nif nif;
    private MechanicRepository repo = Factories.repository.forMechanic();

    public FindMechanicByNif(String nif) {
        ArgumentChecks.isNotBlank( nif, "Nif cannot be null or blank" );
        this.nif = new Nif(nif);
    }

    @Override
    public Optional<MechanicDto> execute() throws BusinessException {
        return repo.findByNif( nif ).map( m -> DtoAssembler.toDto( m ) );
    }

}
