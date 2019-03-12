package org.bawaweb.server.dao;


import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.bawaweb.core.model.CustomerCard;
import org.bawaweb.server.util.UUIDUtil;


/**
 * @author medhoran
 * Interacts with customer_card table
 *
 */
public class CustomerCardDAOImpl extends HibernateDAO implements CustomerCardDAO {


	private static final Log _logger = LogFactory.getLog(CustomerCardDAOImpl.class);
	
	public CustomerCardDAOImpl() {}
	

	@Override
	public List<CustomerCard> getCustomerCards(Integer customerId) {
		if (_logger.isDebugEnabled())	_logger.debug("Accessed CustomerCardDAOImpl with customerId " +  customerId);

		try {
			
			List<CustomerCard> result = entityManager.createQuery( "from CustomerCard where customerId = :customerId", org.bawaweb.core.model.CustomerCard.class ).setParameter("customerId", customerId).getResultList();
			return result.size() > 0 ? result : null;
			
	    } catch (Exception e) {
	    	_logger.error("Failed to retrieve card for customerId " + customerId, e);
	    	e.printStackTrace();
	    }
		
		System.out.println("returning null for getcard in carddao for customer customerId " + customerId);
		return null;
	}

	
/* removing v2
	@Override
	public List<CustomerCard> getCustomerCards(String customerUUID) {
		if (_logger.isDebugEnabled())	_logger.debug("Accessed CustomerCardDAOImpl with customerUUID " +  customerUUID);

		try {
			
			List<CustomerCard> result = entityManager.createQuery( "from CustomerCard where customerUUID = ?1", org.bawaweb.model.CustomerCard.class ).setParameter(1, customerUUID).getResultList();
			return result.size() > 0 ? result : null;
			
	    } catch (Exception e) {
	    	_logger.error("Failed to retrieve card for customer " + customerUUID, e);
	    	e.printStackTrace();
	    }
		
		System.out.println("returning null for getcard in carddao for customer uuid " + customerUUID);
		return null;
		
	}
	end v2*/
	
	
	@Override
	public CustomerCard getCustomerCard(String cardId) {
		if (_logger.isDebugEnabled())	_logger.debug("Accessed CustomerCardDAOImpl.getCustomerCard with cardId " +  cardId);
		
		try {
			CustomerCard theCard = entityManager.createQuery(" from CustomerCard where cardId = ?1", org.bawaweb.core.model.CustomerCard.class ).setParameter(1, cardId).getSingleResult();
			
			return theCard;
			
		} catch (Exception e) {
	    	_logger.error("Failed to retrieve card for cardId " + cardId, e);
	    	e.printStackTrace();
	    }
		return null;
	}	



	@Override
	public CustomerCard addCustomerCard(CustomerCard customerCard) {
		if (_logger.isDebugEnabled())	_logger.debug("ENtered CustomerCardDAOImpl.addCustomerCard for card " + customerCard);
		
		try {
			String cardUID = UUIDUtil.generateUUID();
			customerCard.setCardId(cardUID);
			
			entityManager.persist(customerCard);
	        
			return customerCard;
	      
	    } catch (Exception e) {
	    
	    	_logger.error("Error in adding CustomerCard " + customerCard, e);
	    
	    }
		
	    return null;

	}


	/*remove v2
	 * @Override
	public boolean updateExistingCustomerCardsToNonPrimary(String customerUuid, Integer userId) {
		
		final int changes = entityManager
							.createQuery("update CustomerCard set isPrimary = false, "
									+ "modification_date = sysdate(), "
									+ "modified_by = :modified_by "
									+ "where customer_uuid = :customer_uuid")

							.setParameter("customer_uuid", customerUuid)
							.setParameter("modified_by", userId)
							.executeUpdate();
		

		return changes >= 1 ? true : false;
	}
*/



}
