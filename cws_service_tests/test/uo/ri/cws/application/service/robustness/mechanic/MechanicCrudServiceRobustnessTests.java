package uo.ri.cws.application.service.robustness.mechanic;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.mechanic.MechanicCrudService;
import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;

/**
 * Scenarios:
 *   - Trying to create a mechanic with a null DTO
 *   - Trying to create a mechanic with a null, empty or blank NIF
 *   - Trying to create a mechanic with a null, empty or blank name
 *   - Trying to create a mechanic with a null, empty or blank surname
 *   - Trying to delete a mechanic with a null identifier
 *   - Trying to find a mechanic by a null identifier
 *   - Trying to find a mechanic by a null NIF
 *   - Trying to update a mechanic with a null, empty or blank NIF
 *   - Trying to update a mechanic with a null, empty or blank name
 *   - Trying to update a mechanic with a null, empty or blank surname
 */
class MechanicCrudServiceRobustnessTests {

	private final MechanicCrudService service = Factories.service.forMechanicCrudService();

	/**
	 * Given a null mechanic DTO
	 * When trying to create a mechanic
	 * Then the argument is rejected with an explaining message
	 */
	@Test
	void createRejectsNullDto() {
		assertRejected(() -> service.create(null));
	}

	/**
	 * Given a mechanic DTO with a null, empty or blank NIF
	 * When trying to create the mechanic
	 * Then the argument is rejected with an explaining message
	 */
	@ParameterizedTest
	@NullSource // covers the null case
	@ValueSource(strings = { "", "   " }) // covers the empty and blank cases
	void createRejectsNullOrBlankNif(String nif) {
		MechanicDto dto = validMechanic();
		dto.nif = nif;

		assertRejected(() -> service.create(dto));
	}

	/**
	 * Given a mechanic DTO with a null, empty or blank name
	 * When trying to create the mechanic
	 * Then the argument is rejected with an explaining message
	 */
	@ParameterizedTest
	@NullSource
	@ValueSource(strings = { "", "   " })
	void createRejectsNullOrBlankName(String name) {
		MechanicDto dto = validMechanic();
		dto.name = name;

		assertRejected(() -> service.create(dto));
	}

	/**
	 * Given a mechanic DTO with a null, empty or blank surname
	 * When trying to create the mechanic
	 * Then the argument is rejected with an explaining message
	 */
	@ParameterizedTest
	@NullSource
	@ValueSource(strings = { "", "   " })
	void createRejectsNullOrBlankSurname(String surname) {
		MechanicDto dto = validMechanic();
		dto.surname = surname;

		assertRejected(() -> service.create(dto));
	}

	/**
	 * Given a null mechanic identifier
	 * When trying to delete the mechanic
	 * Then the argument is rejected with an explaining message
	 */
	@Test
	void deleteRejectsNullId() {
		assertRejected(() -> service.delete(null));
	}

	/**
	 * Given a null mechanic identifier
	 * When trying to find a mechanic by identifier
	 * Then the argument is rejected with an explaining message
	 */
	@Test
	void findByIdRejectsNullId() {
		assertRejected(() -> service.findById(null));
	}

	/**
	 * Given a null mechanic NIF
	 * When trying to find a mechanic by NIF
	 * Then the argument is rejected with an explaining message
	 */
	@Test
	void findByNifRejectsNullNif() {
		assertRejected(() -> service.findByNif(null));
	}

	/**
	 * Given a mechanic DTO with a null, empty or blank name
	 * When trying to update the mechanic
	 * Then the argument is rejected with an explaining message
	 */
	@ParameterizedTest
	@NullSource
	@ValueSource(strings = { "", "   " })
	void updateRejectsNullOrBlankName(String name) {
		MechanicDto dto = validMechanic();
		dto.name = name;

		assertRejected(() -> service.update(dto));
	}

	/**
	 * Given a mechanic DTO with a null, empty or blank surname
	 * When trying to update the mechanic
	 * Then the argument is rejected with an explaining message
	 */
	@ParameterizedTest
	@NullSource
	@ValueSource(strings = { "", "   " })
	void updateRejectsNullOrBlankSurname(String surname) {
		MechanicDto dto = validMechanic();
		dto.surname = surname;

		assertRejected(() -> service.update(dto));
	}

	/**
	 * Given a mechanic DTO with a null, empty or blank NIF
	 * When trying to update the mechanic
	 * Then the argument is rejected with an explaining message
	 */
	@ParameterizedTest
	@NullSource
	@ValueSource(strings = { "", "   " })
	void updateRejectsNullOrBlankNif(String nif) {
		MechanicDto dto = validMechanic();
		dto.nif = nif;

		assertRejected(() -> service.update(dto));
	}

	private static MechanicDto validMechanic() {
		MechanicDto dto = new MechanicDto();
		dto.id = "mechanic-id";
		dto.version = 1L;
		dto.nif = "12345678Z";
		dto.name = "Mechanic";
		dto.surname = "Surname";
		return dto;
	}

	private static void assertRejected(Executable action) {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, action);

		assertNotNull(exception.getMessage());
		assertFalse(exception.getMessage().isBlank());
	}
}
