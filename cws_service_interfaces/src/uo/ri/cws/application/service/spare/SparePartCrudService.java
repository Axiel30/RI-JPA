package uo.ri.cws.application.service.spare;

import java.math.BigDecimal;
import java.util.Optional;

import uo.ri.util.exception.BusinessException;

/**
 * This service is intended to be used by the Manager
 * It follows the ISP principle (@see SOLID principles from RC Martin)
 */
public interface SparePartCrudService {

	/**
	 * Registers a new spare part in the system with the information received
	 * @param dto
	 * @return
	 * @throws BusinessException in case of:
	 * 	- there is another spare part registered with the same code
	 * 	- any of the price, stock, min stock or max stock is negative
	 * @throws IllegalArgumentException in case of:
	 * 	- the code or description are null or empty
	 *  - the dto is null
	 */
	SparePartDto create(SparePartDto dto) throws BusinessException;

	/**
	 * Removes the spare part from the system if the spare part has not been
	 * 		used under any of its associations
	 * @param spare part code
	 * @throws BusinessException if case of:
	 * 	- there is no spare part with such code
	 * 	- the spare part has supplies
	 * 	- the spare part is involved in any order
	 * 	- the spare part has substitutions
	 * @throws IllegalArgumentException if the code is null
	 */
	void delete(String code) throws BusinessException;

	/**
	 * Updates the fields of the spare part identified by code except
	 * 	code and id. Version will be internally updated. The new values has to 
	 *  pass the same validation rules as for the add operation.
	 *  
	 * @param dto
	 * @throws BusinessException in case of:
	 * 	- the spare part code does not exist
	 * 	- any of id or description is null or empty
	 * 	- any of stock, min stock or max stock is negative
	 * 	- the price is negative
	 * 	- the version field of the incoming dto does not match the version of
	 * 		the persistent entity
	 * @throws IllegalArgumentException if:
	 *  - the dto is null
	 * 	- any of id or description is null or empty
	 */
	void update(SparePartDto dto) throws BusinessException;

	/**
	 * Note: this operation is quite similar to
	 * 		@see SparePartReportService.findByCode, but this one returns
	 * 		@see SparePartDto instead of
	 * 		@see SparePartReportDto (that).
	 * @param spare part code
	 * @return the spare part identified by the code or Optional.empty() if
	 * 		does not exist
	 * @throws BusinessException DOES NOT
	 * @throws IllegalArgumentException if the code is null
	 */
	Optional<SparePartDto> findByCode(String code) throws BusinessException;

	public static class SparePartDto {
		public String id;
		public long version;

		public String code;
		public String description;
		public int stock;
		public int minStock;
		public int maxStock;
		public BigDecimal price;
	}

}
