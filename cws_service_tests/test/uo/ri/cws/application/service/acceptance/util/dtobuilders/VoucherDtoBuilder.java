package uo.ri.cws.application.service.acceptance.util.dtobuilders;

import java.math.BigDecimal;
import java.util.UUID;

import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService.VoucherDto;

public class VoucherDtoBuilder {

	private VoucherDto dto = createDefaultVoucher();

	public VoucherDtoBuilder withClientId(String clientId) {
		dto.clientId = clientId;
		return this;
	}

	public VoucherDtoBuilder withCode(String code) {
		dto.code = code;
		return this;
	}

	public VoucherDtoBuilder withDescription(String description) {
		dto.description = description;
		return this;
	}

	public VoucherDtoBuilder withAvailableAmount(BigDecimal availableAmount) {
		dto.availableAmount = availableAmount;
		return this;
	}

	public VoucherDto build() {
		return dto;
	}

	private VoucherDto createDefaultVoucher() {
		VoucherDto result = new VoucherDto();
		result.accumulatedAmount = BigDecimal.ZERO;
		result.code = "VOUCHER-" + UUID.randomUUID();
		result.description = "Test voucher";
		result.availableAmount = new BigDecimal("100.00");
		return result;
	}
}
