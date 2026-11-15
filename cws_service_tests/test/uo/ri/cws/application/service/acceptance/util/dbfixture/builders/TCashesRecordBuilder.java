package uo.ri.cws.application.service.acceptance.util.dbfixture.builders;

import java.util.UUID;

import javax.annotation.processing.Generated;

import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TCashesRecord;

@Generated("LLM")
public class TCashesRecordBuilder {

	private TCashesRecord record = createDefaultRecord();

	public TCashesRecordBuilder withId(String id) {
		record.id = id;
		return this;
	}

	public TCashesRecord build() {
		return record;
	}

	private TCashesRecord createDefaultRecord() {
		TCashesRecord defaultRecord = new TCashesRecord();
		defaultRecord.id = UUID.randomUUID().toString();
		return defaultRecord;
	}
}
