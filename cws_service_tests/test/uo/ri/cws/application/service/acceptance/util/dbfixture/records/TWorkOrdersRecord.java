package uo.ri.cws.application.service.acceptance.util.dbfixture.records;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.annotation.processing.Generated;

@Generated("LLM")
public class TWorkOrdersRecord {
    public String id;
    public long version;
    public Timestamp createdAt;
    public Timestamp updatedAt;
    public String entityState;

    public Timestamp date;
    public String description;
    
    // Money concepts (still flattened for DB record)
    public BigDecimal total_amount;
    
    public String state;
    public boolean usedForVoucher;

    public String invoice_Id;
    public String mechanic_Id;
    public String vehicle_Id;
}

