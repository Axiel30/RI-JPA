package uo.ri.cws.application.service.acceptance.util.dbfixture.builders;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

import javax.annotation.processing.Generated;

import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TChargesRecord;

@Generated("LLM")
public class TChargesRecordBuilder {

	private TChargesRecord record = createDefaultRecord();

	public TChargesRecordBuilder withId(String id) {
		record.id = id;
		return this;
	}

	public TChargesRecordBuilder withAmount(BigDecimal amount) {
		record.charge = amount;
		return this;
	}

	public TChargesRecordBuilder withCreatedAt(Timestamp createdAt) {
		record.createdAt = createdAt;
		return this;
	}

	public TChargesRecordBuilder withEntityState(String entityState) {
		record.entityState = entityState;
		return this;
	}

	public TChargesRecordBuilder withUpdatedAt(Timestamp updatedAt) {
		record.updatedAt = updatedAt;
		return this;
	}

	public TChargesRecordBuilder withVersion(Long version) {
		record.version = version;
		return this;
	}

	public TChargesRecordBuilder forInvoiceId(String invoiceId) {
		record.invoice_Id = invoiceId;
		return this;
	}

	public TChargesRecordBuilder forPaymentMeanId(String paymentMeanId) {
		record.paymentMean_Id = paymentMeanId;
		return this;
	}

	public TChargesRecord build() {
		return record;
	}

	private TChargesRecord createDefaultRecord() {
		TChargesRecord defaultRecord = new TChargesRecord();
		defaultRecord.id = UUID.randomUUID().toString();
		defaultRecord.charge = BigDecimal.ZERO; 
		defaultRecord.createdAt = new Timestamp(System.currentTimeMillis());
		defaultRecord.entityState = "ENABLED";
		defaultRecord.updatedAt = new Timestamp(System.currentTimeMillis());
		defaultRecord.version = 1L;
		defaultRecord.invoice_Id = null;
		defaultRecord.paymentMean_Id = null;
		return defaultRecord;
	}
}
