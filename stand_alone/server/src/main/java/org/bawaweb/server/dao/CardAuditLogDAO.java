package org.bawaweb.server.dao;

import org.bawaweb.core.model.CardAuditLog;
/**
 * @author medhoran
 * interacts with card_audit_log table called by manager layer
 *
 */
public interface CardAuditLogDAO {
    /**
     * Adds a record to the card_audit_log table
     * @param cardAuditLog
     * @return
     */
	public boolean addCardAuditLog(CardAuditLog cardAuditLog);
	
}
