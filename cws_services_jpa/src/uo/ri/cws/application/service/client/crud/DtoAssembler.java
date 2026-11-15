package uo.ri.cws.application.service.client.crud;

import java.util.ArrayList;
import java.util.List;

import uo.ri.cws.application.service.client.ClientCrudService.ClientDto;
import uo.ri.cws.domain.Client;
import uo.ri.cws.domain.types.Address;
import uo.ri.cws.domain.types.Email;
import uo.ri.cws.domain.types.FullName;
import uo.ri.cws.domain.types.Nif;
import uo.ri.cws.domain.types.PhoneNumber;

public class DtoAssembler {

	public static List<ClientDto> toDtoList(List<Client> clients) {
		List<ClientDto> result = new ArrayList<>();
		for (Client client : clients) {
			ClientDto dto = toDto(client);
			result.add(dto);
		}
		return result;
	}

	public static ClientDto toDto(Client client) {
		Nif nif = client.getNif();
		FullName fullName = client.getFullName();
		Address address = client.getAddress();
		PhoneNumber phone = client.getPhone();
		Email email = client.getEmail();

		ClientDto dto = new ClientDto();
		dto.id = client.getId();
		dto.version = client.getVersion();
		
		dto.nif = nif.nif();
		dto.name = fullName.name();
		dto.surname = fullName.surname();
		dto.addressStreet = address.street();
		dto.addressCity = address.city();
		dto.addressCountry = address.country();
		dto.addressZipcode = address.postalCode();
		dto.phone = phone.phone();
		dto.email = email.email();
		return dto;
	}
}
