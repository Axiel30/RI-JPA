package uo.ri.ui.foreman.client.action;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.client.ClientCrudService;
import uo.ri.cws.application.service.client.ClientCrudService.ClientDto;
import uo.ri.ui.util.Printer;
import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.menu.Action;

public class FindClientByNifAction implements Action {

	@Override
	public void execute() throws BusinessException {
		String nif = Console.readString("Client nif");

		ClientCrudService service = Factories.service.forClientCrudService();
		Optional<ClientDto> result = service.findClientByNif(nif);
		if (!result.isPresent()) {
			throw new BusinessException("There is no client with that nif");
		}

		Printer.printClient(result.get());
	}

}
