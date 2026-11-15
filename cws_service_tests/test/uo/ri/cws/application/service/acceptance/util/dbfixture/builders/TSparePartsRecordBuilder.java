package uo.ri.cws.application.service.acceptance.util.dbfixture.builders;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

import javax.annotation.processing.Generated;

import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TSparePartsRecord;
import uo.ri.util.random.Random;

@Generated("LLM")
public class TSparePartsRecordBuilder {

	private TSparePartsRecord record = createDefaultRecord();

	public TSparePartsRecordBuilder withId(String id) {
		record.id = id;
		return this;
	}

	public TSparePartsRecordBuilder withCode(String code) {
		record.code = code;
		return this;
	}

	public TSparePartsRecordBuilder withCreatedAt(Timestamp createdAt) {
		record.createdAt = createdAt;
		return this;
	}

	public TSparePartsRecordBuilder withDescription(String description) {
		record.description = description;
		return this;
	}

	public TSparePartsRecordBuilder withEntityState(String entityState) {
		record.entityState = entityState;
		return this;
	}

	public TSparePartsRecordBuilder withUpdatedAt(Timestamp updatedAt) {
		record.updatedAt = updatedAt;
		return this;
	}

	public TSparePartsRecordBuilder withVersion(Long version) {
		record.version = version;
		return this;
	}

	public TSparePartsRecord build() {
		return record;
	}

	private TSparePartsRecord createDefaultRecord() {
		TSparePartsRecord defaultRecord = new TSparePartsRecord();
		defaultRecord.id = UUID.randomUUID().toString();
		defaultRecord.code = "DEFAULT-CODE " + Random.nextInt(10000);
		defaultRecord.createdAt = new Timestamp(System.currentTimeMillis());
		defaultRecord.description = "Default Spare Part";
		defaultRecord.entityState = "ENABLED";
		defaultRecord.price = BigDecimal.ZERO;
		defaultRecord.updatedAt = new Timestamp(System.currentTimeMillis());
		defaultRecord.version = 1L;
		return defaultRecord;
	}
}
