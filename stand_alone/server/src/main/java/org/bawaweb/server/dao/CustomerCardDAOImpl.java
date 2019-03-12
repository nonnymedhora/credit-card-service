package org.bawaweb.server.dao;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.bawaweb.core.model.CustomerCard;
import org.bawaweb.server.util.UUIDUtil;


/**
 * @author medhoran
 * Interacts with customer_card table
 *
 */
public class CustomerCardDAOImpl extends HibernateDAO implements CustomerCardDAO {
    @SuppressWarnings("unused")
    // Logger.
    private final static Logger _logger = LoggerFactory.getLogger(CustomerCardDAOImpl.class);
	public CustomerCardDAOImpl() {}

	@Override
	public CustomerCard getCustomerCard(String cardId) {
		if (_logger.isDebugEnabled())	_logger.debug("Accessed CustomerCardDAOImpl.getCustomerCard with cardId " +  cardId);
		
		try {
			CustomerCard theCard = entityManager.createQuery(" from CustomerCard where cardId = ?1", CustomerCard.class ).setParameter(1, cardId).getSingleResult();
			
			return theCard;
			
		} catch (Exception e) {
	    	_logger.error("Failed to retrieve card for cardId " + cardId, e);
//	    	e.printStackTrace();
	    }
		return null;
	}	



	@Override
	public CustomerCard addCustomerCard(CustomerCard customerCard) {
		if (_logger.isDebugEnabled())	_logger.debug("ENtered CustomerCardDAOImpl.addCustomerCard for card " + customerCard);
		
		try {
            // generate the unique card identifier
			String cardUID = UUIDUtil.generateUUID();
			customerCard.setCardId(cardUID);
			
			entityManager.persist(customerCard);
	        
			return customerCard;
	      
	    } catch (Exception e) {
	    
	    	_logger.error("Error in adding CustomerCard " + customerCard, e);
	    
	    }
		
	    return null;

	}

}
