package uo.ri.cws.application.repository;

import java.util.List;

import uo.ri.cws.domain.WorkOrder;
import uo.ri.cws.domain.types.Nif;

public interface WorkOrderRepository extends Repository<WorkOrder>{

	/**
	 * @param idsAveria, lista de los id de avería a recuperar
	 * @return lista con averias cuyo id aparece en idsAveria,
	 * 	o lista vacía si no hay ninguna
	 */
	List<WorkOrder> findByIds(List<String> workOrderIds);
	
	/**
	 * @param nif of the client
	 * @return a list with the work orders of the vehicles of the client that
	 *         are not invoiced yet or an empty list
	 */
	List<WorkOrder> findNotInvoicedByClientNif(Nif nif);
}