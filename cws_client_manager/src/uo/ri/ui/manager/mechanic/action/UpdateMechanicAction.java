package uo.ri.ui.manager.mechanic.action;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.mechanic.MechanicCrudService;
import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.ui.util.Printer;
import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.menu.Action;

public class UpdateMechanicAction implements Action {

	@Override
	public void execute() throws BusinessException {
		
		// Ask the user for the mechanic id
		String id = Console.readString("Mechanic id"); 
		
		// Invoke the service and show the data
		MechanicCrudService as = Factories.service.forMechanicCrudService();

		Optional<MechanicDto> res = as.findById(id);
		if ( ! res.isPresent() /* res.isEmpty() */) {
			throw new BusinessException("There is no mechanic with that id");
		}
		MechanicDto m = res.get();
		Printer.printMechanic(m);
		
		// Ask for new data
		// nif is the identity, cannot be changed
		m.name = Console.readString("Name", m.name); 
		m.surname = Console.readString("Surname", m.surname); 
		
		// Update
		as.update( m );
		
		// Show the result
		Console.println("The mechanic has been updated");
	}

}
