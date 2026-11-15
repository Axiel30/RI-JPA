package uo.ri.cws.infrastructure.persistence.jpa.query;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import uo.ri.cws.application.query.PaymentMeanQueries;
import uo.ri.cws.application.service.paymentmean.VoucherGenerationService.VoucherSummaryDto;
import uo.ri.cws.infrastructure.persistence.jpa.util.Jpa;
import uo.ri.util.assertion.ArgumentChecks;

public class PaymentMeanJpaQueries implements PaymentMeanQueries {

	@Override
	public boolean hasCharges(String paymentMeanId) {
		ArgumentChecks.isNotBlank(paymentMeanId);

		return 0 < Jpa.getManager()
				.createNamedQuery("PaymentMean.countCharges", Long.class)
				.setParameter(1, paymentMeanId)
				.getSingleResult();
	}

	@Override
	public List<VoucherSummaryDto> getVouchersSummary() {
		List<Object[]> rows = Jpa.getManager()
				.createNamedQuery("Voucher.summary", Object[].class)
				.getResultList();

		List<VoucherSummaryDto> result = new ArrayList<>();
		for (Object[] row : rows) {
			String nif = (String) row[0];
			String name = (String) row[1];
			String surname = (String) row[2];
			long issuedQty = ((Number) row[3]).longValue();
			BigDecimal consumed = (BigDecimal) row[4];
			BigDecimal available = (BigDecimal) row[5];
			BigDecimal total = (consumed != null ? consumed : BigDecimal.ZERO)
					.add(available != null ? available : BigDecimal.ZERO);

			result.add(new VoucherSummaryDto(
					nif,
					name,
					surname,
					issuedQty,
					total,
					available,
					consumed
			));
		}
		result.sort(Comparator.comparing(VoucherSummaryDto::availableAmount).reversed());
		return result;
	}

}
