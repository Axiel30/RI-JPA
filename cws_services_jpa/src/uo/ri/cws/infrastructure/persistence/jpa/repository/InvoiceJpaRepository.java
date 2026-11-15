package uo.ri.cws.infrastructure.persistence.jpa.repository;

import java.util.List;
import java.util.Optional;

import uo.ri.cws.application.repository.InvoiceRepository;
import uo.ri.cws.domain.Invoice;
import uo.ri.cws.domain.types.Money;
import uo.ri.cws.infrastructure.persistence.jpa.util.BaseJpaRepository;
import uo.ri.cws.infrastructure.persistence.jpa.util.Jpa;

public class InvoiceJpaRepository
		extends BaseJpaRepository<Invoice>
		implements InvoiceRepository {

	@Override
	public Optional<Invoice> findByNumber(Long number) {
		return Jpa.getManager()
				.createNamedQuery("Invoice.findByNumber", Invoice.class)
				.setParameter(1, number)
				.getResultStream()
				.findFirst();
	}

	@Override
	public List<Invoice> findOverAmountUnusedForVoucher(Money minimumAmount) {
		return Jpa.getManager()
				.createNamedQuery("Invoice.findOverAmountUnusedForVoucher", Invoice.class)
				.setParameter(1, minimumAmount.amount())
				.getResultList();
	}

}
