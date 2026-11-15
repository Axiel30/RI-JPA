package uo.ri.cws.application.service.acceptance.util.dbfixture.records;

import java.sql.Timestamp;

public class TRecommendationsRecord {
    public String id;
    public Timestamp createdAt;
    public String entityState;
    public Timestamp updatedAt;
    public Boolean usedForVoucher;
    public long version;

    public String recommender_Id;
    public String recommended_Id;
}
