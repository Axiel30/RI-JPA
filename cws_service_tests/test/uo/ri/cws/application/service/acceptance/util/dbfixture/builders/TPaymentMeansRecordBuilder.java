package uo.ri.cws.application.service.acceptance.util.dbfixture.builders;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

import javax.annotation.processing.Generated;

import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TPaymentMeansRecord;

@Generated("LLM")
public class TPaymentMeansRecordBuilder {

	private TPaymentMeansRecord record = createDefaultRecord();

	public TPaymentMeansRecordBuilder withId(String id) {
		record.id = id;
		return this;
	}

	public TPaymentMeansRecordBuilder withDtype(String dtype) {
		record.dtype = dtype;
		return this;
	}

	public TPaymentMeansRecordBuilder withAccumulated(BigDecimal accumulated) {
		record.accumulated = accumulated;
		return this;
	}

	public TPaymentMeansRecordBuilder withCreatedAt(Timestamp createdAt) {
		record.createdAt = createdAt;
		return this;
	}

	public TPaymentMeansRecordBuilder withEntityState(String entityState) {
		record.entityState = entityState;
		return this;
	}

	public TPaymentMeansRecordBuilder withUpdatedAt(Timestamp updatedAt) {
		record.updatedAt = updatedAt;
		return this;
	}

	public TPaymentMeansRecordBuilder withVersion(Long version) {
		record.version = version;
		return this;
	}

	public TPaymentMeansRecordBuilder forClientId(String clientId) {
		record.client_Id = clientId;
		return this;
	}

	public TPaymentMeansRecord build() {
		return record;
	}

	private TPaymentMeansRecord createDefaultRecord() {
		TPaymentMeansRecord defaultRecord = new TPaymentMeansRecord();
		defaultRecord.id = UUID.randomUUID().toString();
		defaultRecord.dtype = "Cash";
		defaultRecord.accumulated = BigDecimal.ZERO;
		defaultRecord.createdAt = new Timestamp(System.currentTimeMillis());
		defaultRecord.entityState = "ENABLED";
		defaultRecord.updatedAt = new Timestamp(System.currentTimeMillis());
		defaultRecord.version = 1L;
		defaultRecord.client_Id = null;
		return defaultRecord;
	}
}
