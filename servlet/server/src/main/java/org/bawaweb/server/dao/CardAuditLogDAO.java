/**
 * 
 */
package org.bawaweb.server.dao;

import java.util.List;

import org.bawaweb.core.model.CardAuditLog;
import org.bawaweb.core.model.CustomerCard;

/**
 * @author medhoran
 *
 */
public interface CardAuditLogDAO {
	
	public boolean addCardAuditLog(CardAuditLog cardAuditLog);

	public boolean addCardAuditLog(List<CustomerCard> customerCards);
	
	public int getNextRequestId();
	
}
