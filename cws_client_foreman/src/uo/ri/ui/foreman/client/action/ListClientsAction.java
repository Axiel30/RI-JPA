package uo.ri.ui.foreman.client.action;

import java.util.List;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.client.ClientCrudService;
import uo.ri.cws.application.service.client.ClientCrudService.ClientDto;
import uo.ri.ui.util.Printer;
import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.menu.Action;

public class ListClientsAction implements Action {
	private ClientCrudService service = Factories.service.forClientCrudService();

	@Override
	public void execute() throws BusinessException {
		List<ClientDto> clients = service.findAllClients();

		Console.println("\nList of clients\n");
		clients.forEach(c -> Printer.printClient(c));
	}

}
