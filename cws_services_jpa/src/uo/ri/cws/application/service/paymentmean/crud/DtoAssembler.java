package uo.ri.cws.application.service.paymentmean.crud;

import java.util.ArrayList;
import java.util.List;

import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService.CardDto;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService.CashDto;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService.PaymentMeanDto;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService.VoucherDto;
import uo.ri.cws.domain.Cash;
import uo.ri.cws.domain.Client;
import uo.ri.cws.domain.CreditCard;
import uo.ri.cws.domain.PaymentMean;
import uo.ri.cws.domain.Voucher;
import uo.ri.cws.domain.types.CreditCardNumber;
import uo.ri.cws.domain.types.Money;
import uo.ri.cws.domain.types.VoucherCode;

public class DtoAssembler {

	public static List<PaymentMeanDto> toDtoList(List<PaymentMean> paymentMeans) {
		List<PaymentMeanDto> result = new ArrayList<>();
		for (PaymentMean paymentMean : paymentMeans) {
			PaymentMeanDto dto = toDto(paymentMean);
			result.add(dto);
		}
		return result;
	}

	public static PaymentMeanDto toDto(PaymentMean paymentMean) {
		if (paymentMean instanceof Cash cash) {
			return toDto(cash);
		}
		if (paymentMean instanceof CreditCard card) {
			return toDto(card);
		}
		if (paymentMean instanceof Voucher voucher) {
			return toDto(voucher);
		}
		throw new IllegalArgumentException("Unknown payment mean type");
	}

	public static CashDto toDto(Cash cash) {
		CashDto dto = new CashDto();
		fillCommonFields(cash, dto);
		return dto;
	}

	public static CardDto toDto(CreditCard card) {
		CardDto dto = new CardDto();
		fillCommonFields(card, dto);

		CreditCardNumber number = card.getNumber();
		dto.cardNumber = number.number();
		dto.cardType = card.getType();
		dto.cardExpiration = card.getValidThru();
		return dto;
	}

	public static VoucherDto toDto(Voucher voucher) {
		VoucherDto dto = new VoucherDto();
		fillCommonFields(voucher, dto);

		VoucherCode code = voucher.getCode();
		Money available = voucher.getAvailableAmount();
		dto.code = code.code();
		dto.description = voucher.getDescription();
		dto.availableAmount = available.amount();
		return dto;
	}

	private static void fillCommonFields(PaymentMean paymentMean,
			PaymentMeanDto dto) {
		Client client = paymentMean.getClient();
		Money accumulated = paymentMean.getAccumulated();

		dto.id = paymentMean.getId();
		dto.version = paymentMean.getVersion();
		dto.clientId = client.getId();
		dto.accumulatedAmount = accumulated.amount();
	}
}
