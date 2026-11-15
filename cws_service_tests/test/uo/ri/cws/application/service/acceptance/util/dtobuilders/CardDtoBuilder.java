package uo.ri.cws.application.service.acceptance.util.dtobuilders;

import java.math.BigDecimal;
import java.time.YearMonth;

import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService.CardDto;

public class CardDtoBuilder {

	private CardDto dto = createDefaultCard();

	public CardDtoBuilder withClientId(String clientId) {
		dto.clientId = clientId;
		return this;
	}

	public CardDtoBuilder withCardNumber(String cardNumber) {
		dto.cardNumber = cardNumber;
		return this;
	}

	public CardDtoBuilder withCardExpiration(YearMonth cardExpiration) {
		dto.cardExpiration = cardExpiration;
		return this;
	}

	public CardDtoBuilder withCardType(String cardType) {
		dto.cardType = cardType;
		return this;
	}

	public CardDto build() {
		return dto;
	}

	private CardDto createDefaultCard() {
		CardDto result = new CardDto();
		result.accumulatedAmount = BigDecimal.ZERO;
		result.cardNumber = CreditCardNumberGenerator.generate();
		result.cardExpiration = YearMonth.now().plusYears(1);
		result.cardType = "VISA";
		return result;
	}
}
