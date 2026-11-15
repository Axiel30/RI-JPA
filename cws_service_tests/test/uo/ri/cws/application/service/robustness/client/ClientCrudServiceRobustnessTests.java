package uo.ri.cws.application.service.robustness.client;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.client.ClientCrudService;
import uo.ri.cws.application.service.client.ClientCrudService.ClientDto;

/**
 * Scenarios:
 *   - Trying to add a client with a null DTO
 *   - Trying to add a client with a null, empty or blank NIF
 *   - Trying to add a client with a null, empty or blank name
 *   - Trying to add a client with a null, empty or blank surname
 *   - Trying to add a client with a null, empty or blank street
 *   - Trying to add a client with a null, empty or blank city
 *   - Trying to add a client with a null, empty or blank country
 *   - Trying to add a client with a null, empty or blank zip code
 *   - Trying to add a client with a null, empty or blank phone
 *   - Trying to add a client with a null, empty or blank email
 *   - Trying to add a client with an empty or blank recommender NIF
 *   - Trying to delete a client with a null, empty or blank NIF
 *   - Trying to update a client with a null DTO
 *   - Trying to update a client with a null, empty or blank id
 *   - Trying to find a client with a null, empty or blank NIF
 *   - Trying to find recommended clients with a null, empty or blank recommender NIF
 */
class ClientCrudServiceRobustnessTests {

	private final ClientCrudService service = Factories.service.forClientCrudService();

	/**
	 * Given a null client DTO
	 * When trying to add a client
	 * Then the argument is rejected with an explaining message
	 */
	@Test
	void addRejectsNullDto() {
		assertRejected(() -> service.addClient(null, null));
	}

	/**
	 * Given a client DTO with a null, empty or blank NIF
	 * When trying to add the client
	 * Then the argument is rejected with an explaining message
	 */
	@ParameterizedTest
	@NullSource
	@ValueSource(strings = { "", "   " })
	void addRejectsNullOrBlankNif(String nif) {
		ClientDto dto = validClient();
		dto.nif = nif;

		assertRejected(() -> service.addClient(dto, null));
	}

	/**
	 * Given a client DTO with a null, empty or blank name
	 * When trying to add the client
	 * Then the argument is rejected with an explaining message
	 */
	@ParameterizedTest
	@NullSource
	@ValueSource(strings = { "", "   " })
	void addRejectsNullOrBlankName(String name) {
		ClientDto dto = validClient();
		dto.name = name;

		assertRejected(() -> service.addClient(dto, null));
	}

	/**
	 * Given a client DTO with a null, empty or blank surname
	 * When trying to add the client
	 * Then the argument is rejected with an explaining message
	 */
	@ParameterizedTest
	@NullSource
	@ValueSource(strings = { "", "   " })
	void addRejectsNullOrBlankSurname(String surname) {
		ClientDto dto = validClient();
		dto.surname = surname;

		assertRejected(() -> service.addClient(dto, null));
	}

	/**
	 * Given a client DTO with a null, empty or blank street
	 * When trying to add the client
	 * Then the argument is rejected with an explaining message
	 */
	@ParameterizedTest
	@NullSource
	@ValueSource(strings = { "", "   " })
	void addRejectsNullOrBlankStreet(String street) {
		ClientDto dto = validClient();
		dto.addressStreet = street;

		assertRejected(() -> service.addClient(dto, null));
	}

	/**
	 * Given a client DTO with a null, empty or blank city
	 * When trying to add the client
	 * Then the argument is rejected with an explaining message
	 */
	@ParameterizedTest
	@NullSource
	@ValueSource(strings = { "", "   " })
	void addRejectsNullOrBlankCity(String city) {
		ClientDto dto = validClient();
		dto.addressCity = city;

		assertRejected(() -> service.addClient(dto, null));
	}

	/**
	 * Given a client DTO with a null, empty or blank country
	 * When trying to add the client
	 * Then the argument is rejected with an explaining message
	 */
	@ParameterizedTest
	@NullSource
	@ValueSource(strings = { "", "   " })
	void addRejectsNullOrBlankCountry(String country) {
		ClientDto dto = validClient();
		dto.addressCountry = country;

		assertRejected(() -> service.addClient(dto, null));
	}

	/**
	 * Given a client DTO with a null, empty or blank zip code
	 * When trying to add the client
	 * Then the argument is rejected with an explaining message
	 */
	@ParameterizedTest
	@NullSource
	@ValueSource(strings = { "", "   " })
	void addRejectsNullOrBlankZipcode(String zipcode) {
		ClientDto dto = validClient();
		dto.addressZipcode = zipcode;

		assertRejected(() -> service.addClient(dto, null));
	}

	/**
	 * Given a client DTO with a null, empty or blank phone
	 * When trying to add the client
	 * Then the argument is rejected with an explaining message
	 */
	@ParameterizedTest
	@NullSource
	@ValueSource(strings = { "", "   " })
	void addRejectsNullOrBlankPhone(String phone) {
		ClientDto dto = validClient();
		dto.phone = phone;

		assertRejected(() -> service.addClient(dto, null));
	}

	/**
	 * Given a client DTO with a null, empty or blank email
	 * When trying to add the client
	 * Then the argument is rejected with an explaining message
	 */
	@ParameterizedTest
	@NullSource
	@ValueSource(strings = { "", "   " })
	void addRejectsNullOrBlankEmail(String email) {
		ClientDto dto = validClient();
		dto.email = email;

		assertRejected(() -> service.addClient(dto, null));
	}

	/**
	 * Given an empty or blank recommender NIF
	 * When trying to add a client
	 * Then the argument is rejected with an explaining message
	 */
	@ParameterizedTest
	@ValueSource(strings = { "", "   " })
	void addRejectsBlankRecommenderNif(String recommenderNif) {
		assertRejected(() -> service.addClient(validClient(), recommenderNif));
	}

	/**
	 * Given a null, empty or blank client NIF
	 * When trying to delete the client
	 * Then the argument is rejected with an explaining message
	 */
	@ParameterizedTest
	@NullSource
	@ValueSource(strings = { "", "   " })
	void deleteRejectsNullOrBlankNif(String nif) {
		assertRejected(() -> service.deleteClientByNif(nif));
	}

	/**
	 * Given a null client DTO
	 * When trying to update the client
	 * Then the argument is rejected with an explaining message
	 */
	@Test
	void updateRejectsNullDto() {
		assertRejected(() -> service.updateClient(null));
	}

	/**
	 * Given a client DTO with a null, empty or blank id
	 * When trying to update the client
	 * Then the argument is rejected with an explaining message
	 */
	@ParameterizedTest
	@NullSource
	@ValueSource(strings = { "", "   " })
	void updateRejectsNullOrBlankId(String id) {
		ClientDto dto = validClient();
		dto.id = id;

		assertRejected(() -> service.updateClient(dto));
	}

	/**
	 * Given a null, empty or blank client NIF
	 * When trying to find the client
	 * Then the argument is rejected with an explaining message
	 */
	@ParameterizedTest
	@NullSource
	@ValueSource(strings = { "", "   " })
	void findClientByNifRejectsNullOrBlankNif(String nif) {
		assertRejected(() -> service.findClientByNif(nif));
	}

	/**
	 * Given a null, empty or blank recommender NIF
	 * When trying to find recommended clients
	 * Then the argument is rejected with an explaining message
	 */
	@ParameterizedTest
	@NullSource
	@ValueSource(strings = { "", "   " })
	void findClientsRecommendedByRejectsNullOrBlankNif(String nif) {
		assertRejected(() -> service.findClientsRecommendedBy(nif));
	}

	private static ClientDto validClient() {
		ClientDto dto = new ClientDto();
		dto.id = "client-id";
		dto.version = 1L;
		dto.nif = "12345678Z";
		dto.name = "Client";
		dto.surname = "Surname";
		dto.addressStreet = "Street";
		dto.addressCity = "City";
		dto.addressCountry = "Country";
		dto.addressZipcode = "33001";
		dto.phone = "+34600000000";
		dto.email = "client@example.com";
		return dto;
	}

	private static void assertRejected(Executable action) {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, action);

		assertNotNull(exception.getMessage());
		assertFalse(exception.getMessage().isBlank());
	}
}
