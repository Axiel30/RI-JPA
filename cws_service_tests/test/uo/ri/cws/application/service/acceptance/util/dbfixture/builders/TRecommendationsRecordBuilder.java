package uo.ri.cws.application.service.acceptance.util.dbfixture.builders;

import java.sql.Timestamp;
import java.util.UUID;

import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TRecommendationsRecord;

public class TRecommendationsRecordBuilder {

	private TRecommendationsRecord record = createDefaultRecord();

	public TRecommendationsRecordBuilder withId(String id) {
		record.id = id;
		return this;
	}

	public TRecommendationsRecordBuilder withCreatedAt(Timestamp createdAt) {
		record.createdAt = createdAt;
		return this;
	}

	public TRecommendationsRecordBuilder withEntityState(String entityState) {
		record.entityState = entityState;
		return this;
	}

	public TRecommendationsRecordBuilder withUpdatedAt(Timestamp updatedAt) {
		record.updatedAt = updatedAt;
		return this;
	}

	public TRecommendationsRecordBuilder withUsedForVoucher(boolean usedForVoucher) {
		record.usedForVoucher = usedForVoucher;
		return this;
	}

	public TRecommendationsRecordBuilder withVersion(long version) {
		record.version = version;
		return this;
	}

	public TRecommendationsRecordBuilder forRecommenderId(String recommenderId) {
		record.recommender_Id = recommenderId;
		return this;
	}

	public TRecommendationsRecordBuilder forRecommendedId(String recommendedId) {
		record.recommended_Id = recommendedId;
		return this;
	}

	public TRecommendationsRecord build() {
		return record;
	}

	private TRecommendationsRecord createDefaultRecord() {
		TRecommendationsRecord result = new TRecommendationsRecord();
		result.id = UUID.randomUUID().toString();
		result.createdAt = new Timestamp(System.currentTimeMillis());
		result.entityState = "ENABLED";
		result.updatedAt = new Timestamp(System.currentTimeMillis());
		result.usedForVoucher = false;
		result.version = 1L;
		return result;
	}
}
