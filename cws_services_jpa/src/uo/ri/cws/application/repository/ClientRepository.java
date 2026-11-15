package uo.ri.cws.application.repository;

import java.util.List;
import java.util.Optional;

import uo.ri.cws.domain.Client;
import uo.ri.cws.domain.types.Nif;

public interface ClientRepository extends Repository<Client> {

	/**
	 * @return a list with all clients, or an empty list if there are none
	 */
	List<Client> findAll();

	/**
	 * @param nif
	 * @return the Client identified by the nif or null if none
	 */
	Optional<Client> findByNif(Nif nif);

	/**
	 * @param recommenderNif nif of the client who made the recommendations
	 * @return the recommended clients, or an empty list if there are none
	 */
	List<Client> findRecommendedBy(Nif recommenderNif);

	/**
	 * @param minimumWorkOrders minimum number of unused work orders to be selected as potential voucher recipient
	 * @return clients with at least the specified number of unused work orders
	 */
	List<Client> findWithWorkOrdersUnusedForVoucher(int minimumWorkOrders);

	/**
	 * @param minimumRecommendations minimum number of unused recommendations to be selected as potential voucher recipient
	 * @return clients with at least the specified number of unused recommendations
	 */
	List<Client> findWithRecommendationsUnusedForVoucher(int minimumRecommendations);
}
