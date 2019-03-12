package org.bawaweb.core.model;


import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by medhoran on 12/9/13.
 */
public class CardAuditLogTest {

    private CardAuditLog cardAuditLog;

    private static final Integer    AUDIT_ID        = 1;
    private static final String     CARD_ID         = "54632789-7aa1-484a-93b7-bf60bbed566f";
    private static final Integer    CLIENT_ID       = 333333;
    private static final Integer    CONCIERGE_ID    = 44444444;
    private static final String     CREATED_BY      = "MEEEEEEEE";
    private static final String     MODIFIED_BY     = "TUMEEEEE";

    @Before
    public void setUp() throws Exception {
        cardAuditLog = new CardAuditLog();

        cardAuditLog.setAuditId(AUDIT_ID);
        cardAuditLog.setCardId(CARD_ID);
        cardAuditLog.setClientId(CLIENT_ID);
        cardAuditLog.setConciergeId(CONCIERGE_ID);
        cardAuditLog.setCreatedBy(CREATED_BY);
        cardAuditLog.setModifiedBy(MODIFIED_BY);
    }


    @Test
    public void testGetAuditId() throws Exception {
        assertEquals(AUDIT_ID, cardAuditLog.getAuditId());
    }

    @Test
    public void testGetCardId() throws Exception {
        assertEquals(CARD_ID, cardAuditLog.getCardId());
    }

    @Test
    public void testGetClientId() throws Exception {
        assertEquals(CLIENT_ID, cardAuditLog.getClientId());
    }

    @Test
    public void getConciergeId() throws Exception {
        assertEquals(CONCIERGE_ID, cardAuditLog.getConciergeId());
    }

    @Test
    public void testGetCreatedBy() throws Exception {
        assertEquals(CREATED_BY, cardAuditLog.getCreatedBy());
    }

    @Test
    public void testGetModifiedBy() throws Exception {
        assertEquals(MODIFIED_BY, cardAuditLog.getModifiedBy());
    }



}
