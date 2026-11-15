package uo.ri.cws.application.service.paymentmean.crud.commands;

import java.time.YearMonth;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.ClientRepository;
import uo.ri.cws.application.repository.PaymentMeanRepository;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService.CardDto;
import uo.ri.cws.application.service.paymentmean.crud.DtoAssembler;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Associations;
import uo.ri.cws.domain.Client;
import uo.ri.cws.domain.CreditCard;
import uo.ri.cws.domain.types.CreditCardNumber;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class AddCard implements Command<CardDto> {

    private CardDto dto;
    private ClientRepository clientRepo = Factories.repository.forClient();
    private PaymentMeanRepository pmRepo =
            Factories.repository.forPaymentMean();

    public AddCard(CardDto dto) {
        ArgumentChecks.isNotNull(dto, "Card dto cannot be null");
        ArgumentChecks.isNotBlank(dto.clientId,
                "Client id cannot be null or blank");
        ArgumentChecks.isNotBlank(dto.cardNumber,
                "Card number cannot be null or blank");
        ArgumentChecks.isNotBlank(dto.cardType,
                "Card type cannot be null or blank");
        ArgumentChecks.isNotNull(dto.cardExpiration,
                "Card expiration date cannot be null");
        this.dto = dto;
    }

    @Override
    public CardDto execute() throws BusinessException {
        Client client = clientRepo.findById(dto.clientId)
                .orElseThrow(() ->
                        new BusinessException("Client does not exist"));

        BusinessChecks.isFalse(
                pmRepo.findCreditCardByNumber(
                        new CreditCardNumber(dto.cardNumber)).isPresent(),
                "Credit card already exists with this number"
        );

        BusinessChecks.isFalse(
                dto.cardExpiration.isBefore(YearMonth.now()),
                "Credit card is outdated"
        );

        CreditCard card = new CreditCard(
                new CreditCardNumber(dto.cardNumber),
                dto.cardType,
                dto.cardExpiration
        );
        Associations.Holds.link(client, card);
        pmRepo.add(card);

        return DtoAssembler.toDto(card);
    }

}
