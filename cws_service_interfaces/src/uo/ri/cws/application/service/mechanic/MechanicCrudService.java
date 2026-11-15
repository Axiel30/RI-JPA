package uo.ri.cws.application.service.mechanic;

import java.util.List;
import java.util.Optional;

import uo.ri.util.exception.BusinessException;

/**
 * This service is intended to be used by the Manager
 * It follows the ISP principle (@see SOLID principles from RC Martin)
 */
public interface MechanicCrudService {

	/**
	 * Add a new mechanic to the system with the data specified in the dto.
	 * 		The id value will be ignored
	 * @param mecanico dto
	 * @return the fully filled dto with the id and version assigned
	 * @throws BusinessException if there already exist another
	 * 		mechanic with the same nif
	 * @throws IllegalArgumentException if 
	 * 		- the dto is null
	 * 		- any of the nif, name, surname fields is null or blank
	 */
	MechanicDto create(MechanicDto dto) throws BusinessException;

	/**
	 * Removes the mechanic from the system if the mechanic exists and has no
	 *     work orders assigned or interventions
	 * @param idMechanic
	 * @throws BusinessException if:
	 * 		- the mechanic does not exist
	 *      - the mechanic has workorders assigned
	 *      - the mechanic has interventions done
	 * @throws IllegalArgumentException if the id is null
	 */
	void delete(String mechanicId) throws BusinessException;

	/**
	 * Updates values for the mechanic specified by the id field, nif, name and surname will be updated
	 * @param mechanic dto, the id field, nif, name and surname cannot be null nor blank
	 * @throws BusinessException if the mechanic does not exist
	 * @throws IllegalArgumentException if 
	 * 		- the dto is null
	 * 		- any of the id, nif, name, surname fields is null or blank
	 */
	void update(MechanicDto dto) throws BusinessException;

	/**
	 * @param id of the mechanic
	 * @return an optional with the dto. Might be empty if the nif does not exist
	 * @throws BusinessException DOES NOT
	 * @throws IllegalArgumentException if the id is null
	 */
	Optional<MechanicDto> findById(String id) throws BusinessException;

	/**
	 * @param nif of the mechanic
	 * @return an optional with the dto. Might be empty if the nif does not exist
	 * @throws BusinessException DOES NOT
	 * @throws IllegalArgumentException if the nif is null
	 */
	Optional<MechanicDto> findByNif(String nif) throws BusinessException;

	/**
	 * @return the list of all mechanics without regarding their contract 
	 * state or if they have contract or not. It might be an empty list if 
	 * there is no mechanic.
	 *
	 * @throws BusinessException DOES NOT
	 */
	List<MechanicDto> findAll() throws BusinessException;

	public static class MechanicDto {
		public String id;
		public long version;

		public String nif;
		public String name;
		public String surname;
	}

}
