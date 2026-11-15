package uo.ri.cws.application.service.acceptance.util.dbfixture.builders;

import java.sql.Date;
import java.util.UUID;

import javax.annotation.processing.Generated;

import uo.ri.cws.application.service.acceptance.util.dtobuilders.CreditCardNumberGenerator;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TCreditCardsRecord;

@Generated("LLM")
public class TCreditCardsRecordBuilder {

	private TCreditCardsRecord record = createDefaultRecord();

	public TCreditCardsRecordBuilder withId(String id) {
		record.id = id;
		return this;
	}

	public TCreditCardsRecordBuilder withNumber(String number) {
		record.number = number;
		return this;
	}

	public TCreditCardsRecordBuilder withType(String type) {
		record.type = type;
		return this;
	}

	public TCreditCardsRecordBuilder withValidThru(Date validThru) {
		record.validThru = validThru;
		return this;
	}

	public TCreditCardsRecord build() {
		return record;
	}

	private TCreditCardsRecord createDefaultRecord() {
		TCreditCardsRecord defaultRecord = new TCreditCardsRecord();
		defaultRecord.id = UUID.randomUUID().toString();
		defaultRecord.number = CreditCardNumberGenerator.generate();
		defaultRecord.type = "VISA";
		defaultRecord.validThru = new Date(System.currentTimeMillis() + 365L * 24 * 60 * 60 * 1000); // 1 year from now
		return defaultRecord;
	}
}
