package uo.ri.cws.application.service.acceptance.util.dbfixture.builders;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

import javax.annotation.processing.Generated;

import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TVehicleTypesRecord;

@Generated("LLM")
public class TVehicleTypesRecordBuilder {

	private TVehicleTypesRecord record = createDefaultRecord();

	public TVehicleTypesRecordBuilder withId(String id) {
		record.id = id;
		return this;
	}

	public TVehicleTypesRecordBuilder withCreatedAt(Timestamp createdAt) {
		record.createdAt = createdAt;
		return this;
	}

	public TVehicleTypesRecordBuilder withEntityState(String entityState) {
		record.entityState = entityState;
		return this;
	}

	public TVehicleTypesRecordBuilder withName(String name) {
		record.name = name;
		return this;
	}

	public TVehicleTypesRecordBuilder withPricePerHour(BigDecimal pricePerHour) {
		record.price_Per_Hour = pricePerHour;
		return this;
	}

	public TVehicleTypesRecordBuilder withUpdatedAt(Timestamp updatedAt) {
		record.updatedAt = updatedAt;
		return this;
	}

	public TVehicleTypesRecordBuilder withVersion(Long version) {
		record.version = version;
		return this;
	}

	public TVehicleTypesRecord build() {
		return record;
	}

	private TVehicleTypesRecord createDefaultRecord() {
		TVehicleTypesRecord defaultRecord = new TVehicleTypesRecord();
		defaultRecord.id = UUID.randomUUID().toString();
		String affix = defaultRecord.id.substring(0, 5);
		
	    Timestamp now = new Timestamp(System.currentTimeMillis());

	    defaultRecord.createdAt = now;
	    defaultRecord.updatedAt = now;

	    defaultRecord.entityState = "ENABLED";
	    defaultRecord.name = "Default Vehicle Type " + affix;
	    defaultRecord.price_Per_Hour = BigDecimal.valueOf(50.0);
	    defaultRecord.version = 1L;
	    
		return defaultRecord;
	}
}
