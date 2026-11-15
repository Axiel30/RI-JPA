
package uo.ri.cws.application.service.acceptance.util.dbfixture.builders;

import java.sql.Timestamp;
import java.util.UUID;

import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TMechanicsRecord;
import uo.ri.cws.application.service.acceptance.util.dtobuilders.NifGenerator;

public class TMechanicsRecordBuilder {

	private TMechanicsRecord record = createDefaultRecord();

	public TMechanicsRecordBuilder withId(String id) {
		record.id = id;
		return this;
	}

	public TMechanicsRecordBuilder withName(String name) {
		record.name = name;
		return this;
	}

	public TMechanicsRecordBuilder withNif(String nif) {
		record.nif = nif;
		return this;
	}

	public TMechanicsRecordBuilder withSurname(String surname) {
		record.surname = surname;
		return this;
	}

	public TMechanicsRecordBuilder withVersion(Long version) {
		record.version = version;
		return this;
	}

	public TMechanicsRecordBuilder withCreatedAt(Timestamp createdAt) {
		record.createdAt = createdAt;
		return this;
	}

	public TMechanicsRecordBuilder withUpdatedAt(Timestamp updatedAt) {
		record.updatedAt = updatedAt;
		return this;
	}

	public TMechanicsRecordBuilder withEntityState(String entityState) {
		record.entityState = entityState;
		return this;
	}

	public TMechanicsRecord build() {
		return record;
	}

	private TMechanicsRecord createDefaultRecord() {
		TMechanicsRecord defaultRecord = new TMechanicsRecord();
		defaultRecord.id = UUID.randomUUID().toString();
		defaultRecord.name = "Default Name " + defaultRecord.id.substring(0, 5);
		defaultRecord.nif = NifGenerator.generate();
		defaultRecord.surname = "Default Surname " + defaultRecord.id.substring(0, 5);;
		defaultRecord.version = 1L;
		defaultRecord.createdAt = new Timestamp(System.currentTimeMillis());
		defaultRecord.updatedAt = new Timestamp(System.currentTimeMillis());
		defaultRecord.entityState = "ENABLED";
		return defaultRecord;
	}
}
