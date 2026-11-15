package uo.ri.cws.application.repository;

import java.util.List;
import java.util.Optional;

import uo.ri.cws.domain.Invoice;
import uo.ri.cws.domain.types.Money;

public interface InvoiceRepository extends Repository<Invoice> {

	/**
	 * @param minimumAmount lower bound for the invoice amount
	 * @return invoices over the specified amount that have not been used to generate a voucher
	 */
	List<Invoice> findOverAmountUnusedForVoucher(Money minimumAmount);

	/**
	 * @param invoice number
	 * @return an optional with the invoice with the given number
	 */
	Optional<Invoice> findByNumber(Long number);

}
