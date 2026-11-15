package uo.ri.cws.application.service.acceptance.util.dbfixture.builders;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

import javax.annotation.processing.Generated;

import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TSubstitutionsRecord;

@Generated("LLM")
public class TSubstitutionsRecordBuilder {

	private TSubstitutionsRecord record = createDefaultRecord();

	public TSubstitutionsRecordBuilder withId(String id) {
		record.id = id;
		return this;
	}

	public TSubstitutionsRecordBuilder withCreatedAt(Timestamp createdAt) {
		record.createdAt = createdAt;
		return this;
	}

	public TSubstitutionsRecordBuilder withEntityState(String entityState) {
		record.entityState = entityState;
		return this;
	}

	public TSubstitutionsRecordBuilder withPrice(BigDecimal price) {
		record.price = price;
		return this;
	}

	public TSubstitutionsRecordBuilder withQuantity(Integer quantity) {
		record.quantity = quantity;
		return this;
	}

	public TSubstitutionsRecordBuilder withUpdatedAt(Timestamp updatedAt) {
		record.updatedAt = updatedAt;
		return this;
	}

	public TSubstitutionsRecordBuilder withVersion(Long version) {
		record.version = version;
		return this;
	}

	public TSubstitutionsRecordBuilder forInterventionId(String interventionId) {
		record.intervention_Id = interventionId;
		return this;
	}

	public TSubstitutionsRecordBuilder forSparePartId(String sparePartId) {
		record.sparePart_Id = sparePartId;
		return this;
	}

	public TSubstitutionsRecord build() {
		return record;
	}

	private TSubstitutionsRecord createDefaultRecord() {
		TSubstitutionsRecord defaultRecord = new TSubstitutionsRecord();
		defaultRecord.id = UUID.randomUUID().toString();
		defaultRecord.createdAt = new Timestamp(System.currentTimeMillis());
		defaultRecord.entityState = "ENABLED";
		defaultRecord.price = BigDecimal.ZERO;
		defaultRecord.quantity = 1;
		defaultRecord.updatedAt = new Timestamp(System.currentTimeMillis());
		defaultRecord.version = 1L;
		defaultRecord.intervention_Id = null;
		defaultRecord.sparePart_Id = null;
		return defaultRecord;
	}
}
