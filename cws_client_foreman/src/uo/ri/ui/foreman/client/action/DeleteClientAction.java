package uo.ri.ui.foreman.client.action;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.client.ClientCrudService;
import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.menu.Action;

public class DeleteClientAction implements Action {

	@Override
	public void execute() throws BusinessException {
		String nif = Console.readString("Client nif");

		ClientCrudService service = Factories.service.forClientCrudService();
		service.deleteClientByNif(nif);

		Console.println("The client has been deleted");
	}

}
