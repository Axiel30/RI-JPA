
package uo.ri.cws.application.service.acceptance.util.dbfixture.builders;

import java.sql.Timestamp;
import java.util.UUID;

import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TInterventionsRecord;

public class TInterventionsRecordBuilder {

	private TInterventionsRecord record = createDefaultRecord();

	public TInterventionsRecordBuilder withId(String id) {
		record.id = id;
		return this;
	}

	public TInterventionsRecordBuilder withCreatedAt(Timestamp createdAt) {
		record.createdAt = createdAt;
		return this;
	}

	public TInterventionsRecordBuilder withDate(Timestamp date) {
		record.date = date;
		return this;
	}

	public TInterventionsRecordBuilder withEntityState(String entityState) {
		record.entityState = entityState;
		return this;
	}

	public TInterventionsRecordBuilder withMinutes(Integer minutes) {
		record.minutes = minutes;
		return this;
	}

	public TInterventionsRecordBuilder withUpdatedAt(Timestamp updatedAt) {
		record.updatedAt = updatedAt;
		return this;
	}

	public TInterventionsRecordBuilder withVersion(Long version) {
		record.version = version;
		return this;
	}

	public TInterventionsRecordBuilder forMechanicId(String mechanicId) {
		record.mechanic_Id = mechanicId;
		return this;
	}

	public TInterventionsRecordBuilder forWorkOrderId(String workOrderId) {
		record.workOrder_Id = workOrderId;
		return this;
	}

	public TInterventionsRecord build() {
		return record;
	}

	private TInterventionsRecord createDefaultRecord() {
		TInterventionsRecord defaultRecord = new TInterventionsRecord();
		defaultRecord.id = UUID.randomUUID().toString();
		defaultRecord.createdAt = new Timestamp(System.currentTimeMillis());
		defaultRecord.updatedAt = new Timestamp(System.currentTimeMillis());
		defaultRecord.entityState = "ENABLED";
		defaultRecord.version = 1L;
		
		defaultRecord.date = new Timestamp(System.currentTimeMillis());
		defaultRecord.minutes = 0;
		return defaultRecord;
	}
}
