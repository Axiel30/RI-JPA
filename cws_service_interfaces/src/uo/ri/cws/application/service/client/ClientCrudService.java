package uo.ri.cws.application.service.client;

import java.util.List;
import java.util.Optional;

import uo.ri.util.exception.BusinessException;

/**
 * This service is intended to be used by the Foreman. It follows the ISP
 * principle (@see SOLID principles from RC Martin)
 */
public interface ClientCrudService {

	/**
     * Add a new client to the system with the data specified in the dto.
     * The id value will be ignored as it is generated here.
     * A Cash payment mean will be automatically created for the new client.
     * 
     * @param client A dto containing info to be added
     * @param recommenderNif the nif of the recommender client, or null if the
     *        client has not been recommended by another client
     * 
     * @return a dto with the id value set to the UUID generated
     * 
     * @throws BusinessException if:
     * 	- there already exists another client with the same nif
     *  - recommenderNif is not null and there is no client with such nif
     * @throws IllegalArgumentException when: the client argument is null, or any of its fields are null, empty or blank
     */
	ClientDto addClient(ClientDto client, String recommenderNif) throws BusinessException;

	/**
     * Deletes the client with the specified nif. 
     * The client will be deleted only if there are no vehicles registered for it and no invoices paid by it.
     * On deletion, its payment means and its made and received recommendations will be deleted too.
     * 
     * @param nif of the client to be deleted
     * @throws BusinessException if:
     * 	- the client does not exist
     *  - there are vehicles registered for this client
     *  - there are invoices paid by this client
	 *
     * @throws IllegalArgumentException when argument is null or empty string
     */
	void deleteClientByNif(String nif) throws BusinessException;

	/**
     * Updates values for the client specified by the id field, except for id.
     * The nif can be updated.
     * 
     * @param client A dto identifying the client to update by the field id, and new data in the other fields
     * @throws BusinessException if:
     * 	- the client does not exist
     *  - there already exists another client with the new nif
     * @throws IllegalArgumentException when the client argument is null, or its id field is null or empty
     */
	void updateClient(ClientDto client) throws BusinessException;

	/**
     * @return the list of all clients registered in the system. It might be an empty list if there is no client
     *
     * @throws BusinessException DOES NOT 
     */
	List<ClientDto> findAllClients() throws BusinessException;

	/**
     * @param nif of the client to find
     * @return the dto for the client or Optional.empty if there is none with the nif
     * @throws BusinessException DOES NOT 
     * @throws IllegalArgumentException when argument is null or empty string
     */
	Optional<ClientDto> findClientByNif(String nif) throws BusinessException;

	/**
     * Returns the list of clients recommended by the client with the recommenderNif. 
	 *
     * @param recommenderNif the nif of the client who recommended the workshop to the returned clients
     * @return the dto for the clients or empty if there is none recommended by the argument
     * @throws BusinessException DOES NOT 
     * @throws IllegalArgumentException when argument is null or empty string
     */
	List<ClientDto> findClientsRecommendedBy(String recommenderNif) throws BusinessException;

	public static class ClientDto {
		public String id;
		public long version;

		public String nif;
		public String name;
		public String surname;
		public String addressStreet;
		public String addressCity;
		public String addressCountry;
		public String addressZipcode;
		public String phone;
		public String email;	
	}

}
