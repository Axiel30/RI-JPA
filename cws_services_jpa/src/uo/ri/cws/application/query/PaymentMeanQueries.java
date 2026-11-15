package uo.ri.cws.application.query;

import java.util.List;

import uo.ri.cws.application.service.paymentmean.VoucherGenerationService.VoucherSummaryDto;

public interface PaymentMeanQueries {

	boolean hasCharges(String paymentMeanId);

	List<VoucherSummaryDto> getVouchersSummary();
}
