package uo.ri.cws.application.service.acceptance.util.dtobuilders;

import org.apache.commons.lang3.RandomStringUtils;

import uo.ri.cws.application.service.client.ClientCrudService.ClientDto;

public class ClientDtoBuilder {

	private ClientDto dto = createDefaultClient();

	public ClientDtoBuilder withId(String id) {
		dto.id = id;
		return this;
	}

	public ClientDtoBuilder withVersion(long version) {
		dto.version = version;
		return this;
	}

	public ClientDtoBuilder withNif(String nif) {
		dto.nif = nif;
		return this;
	}

	public ClientDtoBuilder withName(String name) {
		dto.name = name;
		return this;
	}

	public ClientDtoBuilder withSurname(String surname) {
		dto.surname = surname;
		return this;
	}

	public ClientDtoBuilder withAddressStreet(String street) {
		dto.addressStreet = street;
		return this;
	}

	public ClientDtoBuilder withAddressCity(String city) {
		dto.addressCity = city;
		return this;
	}

	public ClientDtoBuilder withAddressCountry(String country) {
		dto.addressCountry = country;
		return this;
	}

	public ClientDtoBuilder withAddressZipcode(String zipcode) {
		dto.addressZipcode = zipcode;
		return this;
	}

	public ClientDtoBuilder withPhone(String phone) {
		dto.phone = phone;
		return this;
	}

	public ClientDtoBuilder withEmail(String email) {
		dto.email = email;
		return this;
	}

	public ClientDto build() {
		return dto;
	}

	private ClientDto createDefaultClient() {
		ClientDto result = new ClientDto();
		String affix = RandomStringUtils.randomAlphabetic(6);

		result.nif = NifGenerator.generate();
		result.name = affix + " name";
		result.surname = affix + " surname";
		result.addressStreet = affix + " street";
		result.addressCity = affix + " city";
		result.addressCountry = affix + " country";
		result.addressZipcode = RandomStringUtils.randomNumeric(5);
		result.phone = PhoneGenerator.generate();
		result.email = affix.toLowerCase() + "@example.com";
		return result;
	}
}
