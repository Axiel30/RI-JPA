package uo.ri.cws.application.service.acceptance.util.dbfixture.builders;

import java.sql.Timestamp;
import java.util.UUID;

import javax.annotation.processing.Generated;

import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TClientsRecord;
import uo.ri.cws.application.service.acceptance.util.dtobuilders.NifGenerator;
import uo.ri.cws.application.service.acceptance.util.dtobuilders.PhoneGenerator;

@Generated("LLM")
public class TClientsRecordBuilder {

	private TClientsRecord record = createDefaultRecord();

	public TClientsRecordBuilder withId(String id) {
		record.id = id;
		return this;
	}

	public TClientsRecordBuilder withCreatedAt(Timestamp createdAt) {
		record.createdAt = createdAt;
		return this;
	}

	public TClientsRecordBuilder withEmail(String email) {
		record.email = email;
		return this;
	}

	public TClientsRecordBuilder withEntityState(String entityState) {
		record.entityState = entityState;
		return this;
	}

	public TClientsRecordBuilder withName(String name) {
		record.name = name;
		return this;
	}

	public TClientsRecordBuilder withNif(String nif) {
		record.nif = nif;
		return this;
	}

	public TClientsRecordBuilder withPhone(String phone) {
		record.phone = phone;
		return this;
	}

	public TClientsRecordBuilder withSurname(String surname) {
		record.surname = surname;
		return this;
	}

	public TClientsRecordBuilder withUpdatedAt(Timestamp updatedAt) {
		record.updatedAt = updatedAt;
		return this;
	}

	public TClientsRecordBuilder withVersion(Long version) {
		record.version = version;
		return this;
	}

	public TClientsRecordBuilder withCity(String city) {
		record.city = city;
		return this;
	}

	public TClientsRecordBuilder withStreet(String street) {
		record.street = street;
		return this;
	}

	public TClientsRecordBuilder withPostalCode(String zipCode) {
		record.postalCode = zipCode;
		return this;
	}


	public TClientsRecordBuilder withCountry(String c) {
		record.country = c;
		return this;
	}

	public TClientsRecord build() {
		return record;
	}

	private TClientsRecord createDefaultRecord() {
		TClientsRecord defaultRecord = new TClientsRecord();
		defaultRecord.id = UUID.randomUUID().toString();
		defaultRecord.createdAt = new Timestamp(System.currentTimeMillis());
		defaultRecord.updatedAt = new Timestamp(System.currentTimeMillis());
		defaultRecord.entityState = "ENABLED";
		defaultRecord.version = 1L;

		String affix = defaultRecord.id.substring(0, 5);
		defaultRecord.email = "default" + affix + "@example.com";
		defaultRecord.name = "Default Name " + affix;
		defaultRecord.nif = NifGenerator.generate();
		defaultRecord.phone = PhoneGenerator.generate();
		defaultRecord.surname = "Default Surname " + affix;
		defaultRecord.city = "Default City";
		defaultRecord.street = "Default Street " + affix;
		defaultRecord.postalCode = affix;
		defaultRecord.country = "Default Country " + affix;
		return defaultRecord;
	}
}
