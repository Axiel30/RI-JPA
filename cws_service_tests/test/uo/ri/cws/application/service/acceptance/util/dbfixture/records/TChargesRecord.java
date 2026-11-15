package uo.ri.cws.application.service.acceptance.util.dbfixture.records;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.annotation.processing.Generated;

@Generated("LLM")
public class TChargesRecord {
    public String id;
    public Timestamp createdAt;
    public Timestamp updatedAt;
    public String entityState;
    public long version;
    
    public BigDecimal charge;
    public String invoice_Id;
    public String paymentMean_Id;
}
