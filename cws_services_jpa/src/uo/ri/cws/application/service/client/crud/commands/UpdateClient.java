package uo.ri.cws.application.service.client.crud.commands;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.ClientRepository;
import uo.ri.cws.application.service.client.ClientCrudService.ClientDto;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Client;
import uo.ri.cws.domain.types.Address;
import uo.ri.cws.domain.types.Email;
import uo.ri.cws.domain.types.FullName;
import uo.ri.cws.domain.types.Nif;
import uo.ri.cws.domain.types.PhoneNumber;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class UpdateClient implements Command<Void> {

    private ClientDto dto;
    private ClientRepository clientRepo = Factories.repository.forClient();

    public UpdateClient(ClientDto dto) {
        ArgumentChecks.isNotNull(dto, "Client DTO cannot be null");
        ArgumentChecks.isNotBlank(dto.id, "Client ID cannot be null or blank");
        ArgumentChecks.isNotBlank(dto.nif, "NIF cannot be null or blank");
        ArgumentChecks.isNotBlank(dto.name, "Name cannot be null or blank");
        ArgumentChecks.isNotBlank(dto.surname,
                "Surname cannot be null or blank");
        ArgumentChecks.isNotBlank(dto.addressStreet,
                "Street cannot be null or blank");
        ArgumentChecks.isNotBlank(dto.addressCity,
                "City cannot be null or blank");
        ArgumentChecks.isNotBlank(dto.addressCountry,
                "Country cannot be null or blank");
        ArgumentChecks.isNotBlank(dto.addressZipcode,
                "Zipcode cannot be null or blank");
        ArgumentChecks.isNotBlank(dto.phone, "Phone cannot be null or blank");
        ArgumentChecks.isNotBlank(dto.email, "Email cannot be null or blank");
        this.dto = dto;
    }

    @Override
    public Void execute() throws BusinessException {
        Client client = clientRepo.findById(dto.id)
                .orElseThrow(() ->
                        new BusinessException("Client does not exist"));

        BusinessChecks.isTrue(client.getVersion() == dto.version,
                "Version mismatch: client has been modified concurrently");

        Nif newNif = new Nif(dto.nif);
        Optional<Client> existing = clientRepo.findByNif(newNif);
        if (existing.isPresent()
                && !existing.get().getId().equals(client.getId())) {
            throw new BusinessException(
                    "Another client already has NIF " + dto.nif);
        }

        client.setNif(newNif);
        client.setFullName(new FullName(dto.name, dto.surname));
        client.setEmail(new Email(dto.email));
        client.setPhone(new PhoneNumber(dto.phone));
        client.setAddress(new Address(dto.addressStreet, dto.addressCity,
                dto.addressCountry, dto.addressZipcode));

        return null;
    }
}