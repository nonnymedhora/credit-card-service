/**
 * 
 */
package org.bawaweb.server.dao;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.bawaweb.core.model.CardAuditLog;

/**
 * @author medhoran
 *
 */
public class CardAuditLogDAOImpl extends HibernateDAO implements CardAuditLogDAO {

	@SuppressWarnings("unused")
    // Logger.
    private final static Logger _logger = LoggerFactory.getLogger(CardAuditLogDAOImpl.class);

	/* (non-Javadoc)
	 * @see org.bawaweb.dao.CardAuditLogDAO#addCardAuditLog(org.bawaweb.model.CardAuditLog)
	 */
	@Override
	public boolean addCardAuditLog(CardAuditLog cardAuditLog) {
		_logger.info("ENtered CardAuditLogDAOImpl.addCardAuditLog for card " + cardAuditLog);

		try {

	      entityManager.persist(cardAuditLog);

	      return true;
	      
	    } catch (Exception e) {
	    	_logger.error("Error in adding CardAuditLog " + cardAuditLog, e);
	    }
		
	    return false;
	}

}
