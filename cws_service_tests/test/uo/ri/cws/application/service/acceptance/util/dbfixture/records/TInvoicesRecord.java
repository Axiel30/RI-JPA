package uo.ri.cws.application.service.acceptance.util.dbfixture.records;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

import javax.annotation.processing.Generated;

@Generated("LLM")
public class TInvoicesRecord {

    public String id;
    public Timestamp createdat;
    public Timestamp updatedat;
    public String entitystate;
    public long version;

    public Long number;
    public String state;
    public boolean usedForVoucher;

    public BigDecimal total;
    public BigDecimal subtotal;
    public BigDecimal vatamount;
    public BigDecimal vatrate;

    public Date date;
}
