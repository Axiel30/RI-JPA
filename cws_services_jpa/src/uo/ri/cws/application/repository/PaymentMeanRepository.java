package uo.ri.cws.application.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import uo.ri.cws.domain.CreditCard;
import uo.ri.cws.domain.PaymentMean;
import uo.ri.cws.domain.Voucher;
import uo.ri.cws.domain.types.CreditCardNumber;
import uo.ri.cws.domain.types.Nif;
import uo.ri.cws.domain.types.VoucherCode;

public interface PaymentMeanRepository extends Repository<PaymentMean> {
	
	/**
	 * @param nif of the client
	 * @return a list with all the payment means owned by the client
	 */
	List<PaymentMean> findPaymentMeansByClientNif(Nif nif);

	/**
	 * @param nif of the client
	 * @return all vouchers owned by the client
	 */
	List<Voucher> findVouchersByClientNif(Nif nif);

	/**
	 * @param code of the voucher
	 * @return the voucher identified by the code, or empty if it does not exist
	 */
	Optional<Voucher> findVoucherByCode(VoucherCode code);

	/**
	 * @param number of the credit card
	 * @return the credit card identified by the number, or empty if it does not exist
	 */
	Optional<CreditCard> findCreditCardByNumber(CreditCardNumber number);

	/**
	 * @param keySet of payment mean ids
	 * @return all payment means identified by the ids in the collection
	 */
	List<PaymentMean> findByIds(Collection<String> ids);
}
