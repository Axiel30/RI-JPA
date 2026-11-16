package uo.ri.cws.application.service.client.crud.commands;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.ClientRepository;
import uo.ri.cws.application.repository.PaymentMeanRepository;
import uo.ri.cws.application.repository.RecommendationRepository;
import uo.ri.cws.application.service.client.ClientCrudService.ClientDto;
import uo.ri.cws.application.service.client.crud.DtoAssembler;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Cash;
import uo.ri.cws.domain.Client;
import uo.ri.cws.domain.Recommendation;
import uo.ri.cws.domain.types.Address;
import uo.ri.cws.domain.types.Email;
import uo.ri.cws.domain.types.FullName;
import uo.ri.cws.domain.types.Nif;
import uo.ri.cws.domain.types.PhoneNumber;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class AddClient implements Command<ClientDto> {

    private ClientDto dto;
    private String recommenderNif;
    private ClientRepository clientRepo = Factories.repository.forClient();
    private PaymentMeanRepository pmRepo = Factories.repository.forPaymentMean();
    private RecommendationRepository recRepo = Factories.repository.forRecommendation();

    public AddClient(ClientDto dto, String recommenderNif) {
        ArgumentChecks.isNotNull(dto, "Client DTO cannot be null");
        ArgumentChecks.isNotBlank(dto.nif, "NIF cannot be null or blank");
        ArgumentChecks.isNotBlank(dto.name, "Name cannot be null or blank");
        ArgumentChecks.isNotBlank(dto.surname, "Surname cannot be null or blank");
        ArgumentChecks.isNotBlank(dto.addressStreet, "Street cannot be null or blank");
        ArgumentChecks.isNotBlank(dto.addressCity, "City cannot be null or blank");
        ArgumentChecks.isNotBlank(dto.addressCountry, "Country cannot be null or blank");
        ArgumentChecks.isNotBlank(dto.addressZipcode, "Zipcode cannot be null or blank");
        ArgumentChecks.isNotBlank(dto.phone, "Phone cannot be null or blank");
        ArgumentChecks.isNotBlank(dto.email, "Email cannot be null or blank");
        if (recommenderNif != null) {
            ArgumentChecks.isNotBlank(recommenderNif, "Recommender NIF cannot be blank");
        }
        this.dto = dto;
        this.recommenderNif = recommenderNif;
    }

    @Override
    public ClientDto execute() throws BusinessException {
        Nif nif = new Nif(dto.nif);
        BusinessChecks.isFalse(clientRepo.findByNif(nif).isPresent(), "Client already exists with NIF " + dto.nif);

        Client recommender = null;
        if (recommenderNif != null) {
            recommender = clientRepo.findByNif(new Nif(recommenderNif))
                .orElseThrow(() -> new BusinessException("Recommender client does not exist"));
        }

        Client client = new Client(nif, new FullName(dto.name, dto.surname), new Email(dto.email),
            new PhoneNumber(dto.phone),
            new Address(dto.addressStreet, dto.addressCity, dto.addressCountry, dto.addressZipcode));

        Cash cash = new Cash(client);
        clientRepo.add(client);
        pmRepo.add(cash);

        if (recommender != null) {
            Recommendation recommendation = new Recommendation(recommender, client);
            recRepo.add(recommendation);
        }

        return DtoAssembler.toDto(client);
    }
}