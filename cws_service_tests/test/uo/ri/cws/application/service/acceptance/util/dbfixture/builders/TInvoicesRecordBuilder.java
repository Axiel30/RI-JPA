package uo.ri.cws.application.service.acceptance.util.dbfixture.builders;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.UUID;

import javax.annotation.processing.Generated;

import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TInvoicesRecord;

@Generated("LLM")
public class TInvoicesRecordBuilder {

	private TInvoicesRecord record = createDefaultRecord();

	// ---- identity ----

	public TInvoicesRecordBuilder withId(String id) {
		record.id = id;
		return this;
	}

	public TInvoicesRecordBuilder withVersion(Long version) {
		record.version = version;
		return this;
	}

	// ---- amounts ----

	public TInvoicesRecordBuilder withTotal(BigDecimal amount) {
		record.total = amount;
		return this;
	}

	public TInvoicesRecordBuilder withSubtotal(BigDecimal amount) {
		record.subtotal = amount;
		return this;
	}

	public TInvoicesRecordBuilder withVatAmount(BigDecimal amount) {
		record.vatamount = amount;
		return this;
	}

	public TInvoicesRecordBuilder withVatRate(BigDecimal rate) {
		record.vatrate = rate;
		return this;
	}

	// ---- invoice metadata ----

	public TInvoicesRecordBuilder withCreatedAt(Timestamp createdAt) {
		record.createdat = createdAt;
		return this;
	}

	public TInvoicesRecordBuilder withUpdatedAt(Timestamp updatedAt) {
		record.updatedat = updatedAt;
		return this;
	}

	public TInvoicesRecordBuilder withDate(Date date) {
		record.date = date;
		return this;
	}

	public TInvoicesRecordBuilder withEntityState(String entityState) {
		record.entitystate = entityState;
		return this;
	}

	public TInvoicesRecordBuilder withNumber(Long number) {
		record.number = number;
		return this;
	}

	public TInvoicesRecordBuilder withState(String state) {
		record.state = state;
		return this;
	}

	public TInvoicesRecordBuilder withUsedForVoucher(boolean usedForVoucher) {
		record.usedForVoucher = usedForVoucher;
		return this;
	}

	// ---- shortcuts ----

	public TInvoicesRecordBuilder paid() {
		record.state = "PAID";
		return this;
	}

	public TInvoicesRecordBuilder notPaid() {
		record.state = "ISSUED";
		return this;
	}

	// ---- build ----

	public TInvoicesRecord build() {
		return record;
	}

	// ---- defaults ----

	private TInvoicesRecord createDefaultRecord() {
		TInvoicesRecord r = new TInvoicesRecord();

		r.id = UUID.randomUUID().toString();

		r.total = BigDecimal.ZERO;

		r.subtotal = BigDecimal.ZERO;

		r.vatamount = BigDecimal.ZERO;

//		r.vatrate = BigDecimal.ZERO;

		r.createdat = new Timestamp(System.currentTimeMillis());
		r.updatedat = new Timestamp(System.currentTimeMillis());

		r.date = Date.valueOf(LocalDate.now());

		r.entitystate = "ENABLED";
		r.number = 1L;
		r.state = "ISSUED";
		r.usedForVoucher = false;

		r.version = 1L;

		return r;
	}
}
