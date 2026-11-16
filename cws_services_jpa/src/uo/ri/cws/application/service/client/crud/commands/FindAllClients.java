package uo.ri.cws.application.service.client.crud.commands;

import java.util.List;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.ClientRepository;
import uo.ri.cws.application.service.client.ClientCrudService.ClientDto;
import uo.ri.cws.application.service.client.crud.DtoAssembler;
import uo.ri.cws.application.util.command.Command;
import uo.ri.util.exception.BusinessException;

public class FindAllClients implements Command<List<ClientDto>> {

    private ClientRepository repo = Factories.repository.forClient();

    @Override
    public List<ClientDto> execute() throws BusinessException {
        return DtoAssembler.toDtoList(repo.findAll());
    }
}