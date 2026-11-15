package uo.ri.cws.application.service.acceptance.util.dbfixture.builders;

import java.sql.Timestamp;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.processing.Generated;

import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TVehiclesRecord;

@Generated("LLM")
public class TVehiclesRecordBuilder {
	private static final AtomicInteger PLATE_SEQUENCE = new AtomicInteger();

	private TVehiclesRecord record = createDefaultRecord();

	public TVehiclesRecordBuilder withId(String id) {
		record.id = id;
		return this;
	}

	public TVehiclesRecordBuilder withCreatedAt(Timestamp createdAt) {
		record.createdAt = createdAt;
		return this;
	}

	public TVehiclesRecordBuilder withEntityState(String entityState) {
		record.entityState = entityState;
		return this;
	}

	public TVehiclesRecordBuilder withMake(String make) {
		record.make = make;
		return this;
	}

	public TVehiclesRecordBuilder withModel(String model) {
		record.model = model;
		return this;
	}

	public TVehiclesRecordBuilder withPlateNumber(String plateNumber) {
		record.licensePlate = plateNumber;
		return this;
	}

	public TVehiclesRecordBuilder withUpdatedAt(Timestamp updatedAt) {
		record.updatedAt = updatedAt;
		return this;
	}

	public TVehiclesRecordBuilder withVersion(Long version) {
		record.version = version;
		return this;
	}

	public TVehiclesRecordBuilder forClientId(String clientId) {
		record.client_Id = clientId;
		return this;
	}

	public TVehiclesRecordBuilder forVehicleTypeId(String vehicleTypeId) {
		record.vehicleType_Id = vehicleTypeId;
		return this;
	}

	public TVehiclesRecord build() {
		return record;
	}

	private TVehiclesRecord createDefaultRecord() {
		TVehiclesRecord defaultRecord = new TVehiclesRecord();
		defaultRecord.id = UUID.randomUUID().toString();
		defaultRecord.createdAt = new Timestamp(System.currentTimeMillis());
		defaultRecord.entityState = "ENABLED";
		defaultRecord.make = "Default Make " + defaultRecord.id.substring(0, 4);
		defaultRecord.model = "Default Model " + defaultRecord.id.substring(0, 4);
		defaultRecord.licensePlate = String.format("%04dBBB", PLATE_SEQUENCE.getAndIncrement());
		defaultRecord.updatedAt = new Timestamp(System.currentTimeMillis());
		defaultRecord.version = 1L;
		defaultRecord.client_Id = null;
		defaultRecord.vehicleType_Id = null;
		return defaultRecord;
	}
}
