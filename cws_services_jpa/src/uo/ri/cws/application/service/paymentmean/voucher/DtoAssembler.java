package uo.ri.cws.application.service.paymentmean.voucher;

import java.util.ArrayList;
import java.util.List;

import uo.ri.cws.application.service.paymentmean.VoucherGenerationService.VoucherDto;
import uo.ri.cws.domain.Client;
import uo.ri.cws.domain.Voucher;
import uo.ri.cws.domain.types.Money;
import uo.ri.cws.domain.types.VoucherCode;

public class DtoAssembler {

	public static List<VoucherDto> toDtoList(List<Voucher> vouchers) {
		List<VoucherDto> result = new ArrayList<>();
		for (Voucher voucher : vouchers) {
			VoucherDto dto = toDto(voucher);
			result.add(dto);
		}
		return result;
	}

	public static VoucherDto toDto(Voucher voucher) {
		VoucherDto dto = new VoucherDto();
		Client client = voucher.getClient();
		Money accumulated = voucher.getAccumulated();
		Money available = voucher.getAvailableAmount();
		VoucherCode code = voucher.getCode();

		dto.id = voucher.getId();
		dto.version = voucher.getVersion();
		dto.clientId = client.getId();
		dto.accumulatedAmount = accumulated.amount();
		dto.code = code.code();
		dto.description = voucher.getDescription();
		dto.availableAmount = available.amount();
		return dto;
	}
}
