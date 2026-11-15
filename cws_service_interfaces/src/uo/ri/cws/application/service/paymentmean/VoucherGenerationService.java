package uo.ri.cws.application.service.paymentmean;

import java.math.BigDecimal;
import java.util.List;

import uo.ri.util.exception.BusinessException;

public interface VoucherGenerationService {
	
    /**
     * Generates vouchers according to these policies:
     *   - 20 euro vouchers for paid work orders
     *   - 25 euro vouchers for recommendations
     *   - 30 euro vouchers for paid invoices over 500 euro
     *
     * @return an array of three counters containing, in order, the number of
     *         20, 25 and 30 euro vouchers generated
     * @throws BusinessException DOES NOT
     */
    int[] generateVouchers() throws BusinessException;

    /**
	 * Returns all the vouchers for the client identified by its nif
	 * 
	 * @param nif
	 * @return A list with vouchers or empty if there is no voucher from the client
	 * @throws BusinessException DOES NOT
	 * @throws IllegalArgumentException if the argument is null or blank
	 */
    List<VoucherDto> findVouchersByClientNif(String nif) throws BusinessException;

    /**
     * Returns data for the vouchers, aggregated by client
     * 
     * @return A list of aggregated information of the vouchers of each client
     *         ordered by availableAmount descending, or empty if there is no voucher 
     * @throws BusinessException DOES NOT
     */
    List<VoucherSummaryDto> getVoucherSummary() throws BusinessException;

	public static class VoucherDto {
		public String id;
		public long version;
		public String clientId;
		public BigDecimal accumulatedAmount;
		public String code;
		public String description;
		public BigDecimal availableAmount;
	}

    /**
     * An aggregated result of all vouchers of a client
     */
    public record VoucherSummaryDto (
        String nif, 				// of the client
        String name, 				// of the client
        String surname, 			// of the client
        long issuedQty, 			// how many vouchers has been issued
        BigDecimal totalAmount, 	// the total amount "voucherized" (money)
        BigDecimal availableAmount, // how much remains available for the client
        BigDecimal consumedAmount 	// how much has been consumed by the client
      ) {}
    
}
