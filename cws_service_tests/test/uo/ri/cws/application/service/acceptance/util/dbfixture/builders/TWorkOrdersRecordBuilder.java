
package uo.ri.cws.application.service.acceptance.util.dbfixture.builders;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TWorkOrdersRecord;

public class TWorkOrdersRecordBuilder {

	private TWorkOrdersRecord record = createDefaultRecord();

	public TWorkOrdersRecordBuilder withId(String id) {
		record.id = id;
		return this;
	}

	public TWorkOrdersRecordBuilder withAmount(BigDecimal amount) {
		record.total_amount = amount;
		return this;
	}

	public TWorkOrdersRecordBuilder withCreatedAt(Timestamp createdAt) {
		record.createdAt = createdAt;
		return this;
	}

	public TWorkOrdersRecordBuilder withDate(Timestamp date) {
		record.date = date;
		return this;
	}

	public TWorkOrdersRecordBuilder withDescription(String description) {
		record.description = description;
		return this;
	}

	public TWorkOrdersRecordBuilder withEntityState(String entityState) {
		record.entityState = entityState;
		return this;
	}

	public TWorkOrdersRecordBuilder finished() {
		return withState("FINISHED");
	}

	public TWorkOrdersRecordBuilder invoiced() {
		return withState("INVOICED");
	}

	public TWorkOrdersRecordBuilder withState(String state) {
		record.state = state;
		return this;
	}

	public TWorkOrdersRecordBuilder withUsedForVoucher(boolean usedForVoucher) {
		record.usedForVoucher = usedForVoucher;
		return this;
	}

	public TWorkOrdersRecordBuilder withUpdatedAt(Timestamp updatedAt) {
		record.updatedAt = updatedAt;
		return this;
	}

	public TWorkOrdersRecordBuilder withVersion(Long version) {
		record.version = version;
		return this;
	}

	public TWorkOrdersRecordBuilder forInvoiceId(String invoiceId) {
		record.invoice_Id = invoiceId;
		return this;
	}

	public TWorkOrdersRecordBuilder forMechanicId(String mechanicId) {
		record.mechanic_Id = mechanicId;
		return this;
	}

	public TWorkOrdersRecordBuilder forVehicleId(String vehicleId) {
		record.vehicle_Id = vehicleId;
		return this;
	}

	public TWorkOrdersRecord build() {
		return record;
	}

	private TWorkOrdersRecord createDefaultRecord() {
		TWorkOrdersRecord defaultRecord = new TWorkOrdersRecord();
		defaultRecord.id = UUID.randomUUID().toString();
		defaultRecord.entityState = "ENABLED";
		defaultRecord.createdAt = new Timestamp(System.currentTimeMillis());
		defaultRecord.updatedAt = new Timestamp(System.currentTimeMillis());
		defaultRecord.version = 1L;

		defaultRecord.state = "OPEN";
		defaultRecord.usedForVoucher = false;
		defaultRecord.total_amount = BigDecimal.ZERO;
		defaultRecord.date = new Timestamp(System.currentTimeMillis());
		defaultRecord.description = "Default Description";
		return defaultRecord;
	}

}
