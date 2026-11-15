package uo.ri.cws.application.service.acceptance.util.dtobuilders;

import org.apache.commons.lang3.RandomStringUtils;

import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;

public class MechanicDtoBuilder {

	private MechanicDto dto = createDefaultMechanic();

	public MechanicDtoBuilder withId(String id) {
		dto.id = id;
		return this;
	}

	public MechanicDtoBuilder withNif(String nif) {
		dto.nif = nif;
		return this;
	}

	public MechanicDtoBuilder withName(String name) {
		dto.name = name;
		return this;
	}

	public MechanicDtoBuilder withSurname(String surname) {
		dto.surname = surname;
		return this;
	}
 
	public MechanicDtoBuilder withVersion(long version) {
		dto.version = version;
		return this;
	}

	public MechanicDto build() {
		return dto;
	}

	private MechanicDto createDefaultMechanic() {
		MechanicDto res = new MechanicDto();

		res.nif = NifGenerator.generate();
		res.name = RandomStringUtils.randomAlphabetic(4) + " name";
		res.surname = RandomStringUtils.randomAlphabetic(6) + " surname";
		return res;
	}

}
