/**
 * 
 */
package org.bawaweb.server.dao;

import java.util.List;

import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.bawaweb.core.model.CardAuditLog;
import org.bawaweb.core.model.CustomerCard;

/**
 * @author medhoran
 *
 */
public class CardAuditLogDAOImpl extends HibernateDAO implements CardAuditLogDAO {
	
	private static final Log _logger = LogFactory.getLog(CardAuditLogDAOImpl.class);

	/* (non-Javadoc)
	 * @see org.bawaweb.dao.CardAuditLogDAO#addCardAuditLog(org.bawaweb.model.CardAuditLog)
	 */
	@Override
	public boolean addCardAuditLog(CardAuditLog cardAuditLog) {
		_logger.info("ENtered CardAuditLogDAOImpl.addCardAuditLog for card " + cardAuditLog);
		System.out.println("ENtered CardAuditLogDAOImpl.addCardAuditLog for card " + cardAuditLog);
		
		try {
			
//		  beginAndClear();

	      entityManager.persist(cardAuditLog);
System.out.println("Calling persist - the cardAuditLog is \n" + cardAuditLog);	      
//	      commit();
	      
	      
	      return true;
	      
	    } catch (Exception e) {
	    	_logger.error("Error in adding CardAuditLog " + cardAuditLog, e);
	      /*if (isActive()) {
			rollback();
	      }*/
	    }
		
		System.out.println("cardauditlaogdaoimpl added record of card audit log " + cardAuditLog);
	    return false;
	}

	@Override
	public boolean addCardAuditLog(List<CustomerCard> customerCards) {
		
		boolean allAdded = true;//false;
		boolean added = false;
		
		int requestId = getNextRequestId();
		
		for (CustomerCard customerCard : customerCards) {
			// add the audit log info
			CardAuditLog cardAuditLog = new CardAuditLog();
			cardAuditLog.setRequestId(requestId);
			cardAuditLog.setCardId(customerCard.getCardId());
			cardAuditLog.setClientId(981287);///@TODAO - how to get these
			cardAuditLog.setConciergeId(77777);
//			cardAuditLog.setCustomerId(customerCard.getCustomerId());
			cardAuditLog.setRequestType('G');
			cardAuditLog.setCreatedBy("Me16666");
			cardAuditLog.setUserId(222222);
			
			added = addCardAuditLog(cardAuditLog);
			
			allAdded = allAdded && added;//allAdded || ( allAdded && added );
		}
		
		return allAdded;
	}

	public int getNextRequestId() {
		System.out.println("in GET_NEXT_REQUEST_ID");
/*//		https://www.java.net/node/683944
		List resultList = entityManager.createQuery(
				 "SELECT requestId  FROM CardAuditLog c ")// +
//				 "ORDER BY c.id DESC")
				 .setMaxResults(1)
				 .getResultList();

		int result = resultList.isEmpty() ? 0 : (Integer) resultList.get(0);
		return result+1;
*/
		
		
		
		/*
		//http://stackoverflow.com/questions/10992645/unable-to-have-correct-value-with-select-query-using-max-in-jpa
		// user2399459
		 Integer maxId = null;
		 try {
			maxId = (Integer) entityManager.createNamedQuery("CardAuditLog.getMaxRequestID").getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
		}
		 
		 if(maxId==null) { 
			 return 1; 
		 }
		 
		 return (Integer)maxId+1;
		 */
		
		
		final String maxRequestQry = "select max(c.requestId) from CardAuditLog c";
		Query qry = entityManager.createQuery(maxRequestQry);
	    Object obj = qry.getSingleResult();
	    System.out.println("maxdReequestid " + obj);

	    if(obj==null) return 1;
	    return (Integer)obj+1;
		
		
//		int result = entityManager.createQuery( "select max(c.request_id) from card_audit_log c", Integer.class ).getSingleResult();
//		
//		System.out.println("maxdReequestid " + result);
//
//		System.out.println("result is " + result);
//		return result+1;
	}

}
