package uo.ri.cws.application.service.client.crud;

import java.util.List;
import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.client.ClientCrudService;
import uo.ri.cws.application.service.client.crud.commands.AddClient;
import uo.ri.cws.application.service.client.crud.commands.DeleteClient;
import uo.ri.cws.application.service.client.crud.commands.FindAllClients;
import uo.ri.cws.application.service.client.crud.commands.FindClientByNif;
import uo.ri.cws.application.service.client.crud.commands.FindClientsRecommendedBy;
import uo.ri.cws.application.service.client.crud.commands.UpdateClient;
import uo.ri.cws.application.util.command.CommandExecutor;
import uo.ri.util.exception.BusinessException;

public class ClientCrudServiceImpl implements ClientCrudService {

    private CommandExecutor executor = Factories.executor.forExecutor();

    @Override
    public ClientDto addClient(ClientDto client, String recommenderNif)
            throws BusinessException {
        return executor.execute(new AddClient(client, recommenderNif));
    }

    @Override
    public void deleteClientByNif(String nif) throws BusinessException {
        executor.execute(new DeleteClient(nif));
    }

    @Override
    public void updateClient(ClientDto client) throws BusinessException {
        executor.execute(new UpdateClient(client));
    }

    @Override
    public List<ClientDto> findAllClients() throws BusinessException {
        return executor.execute(new FindAllClients());
    }

    @Override
    public Optional<ClientDto> findClientByNif(String nif)
            throws BusinessException {
        return executor.execute(new FindClientByNif(nif));
    }

    @Override
    public List<ClientDto> findClientsRecommendedBy(String recommenderNif)
            throws BusinessException {
        return executor.execute(new FindClientsRecommendedBy(recommenderNif));
    }
}