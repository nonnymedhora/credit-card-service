/**
 * 
 */
package org.bawaweb.server.dao;

import java.util.List;

import javax.persistence.EntityExistsException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.bawaweb.core.model.CustomerUUID;
import org.bawaweb.server.util.UUIDUtil;

/**
 * @author medhoran
 *
 */
public class CustomerUUIDDAOImpl extends HibernateDAO implements CustomerUUIDDAO {
	
	private static final Log _logger = LogFactory.getLog(CustomerCardDAOImpl.class);
	
	public CustomerUUIDDAOImpl() {}

	/* Generates a UUID value from the UUIDUtil and then adds info to the customer_uuid table
	 * @see org.bawaweb.dao.CustomerUUIDDAO#addCustomerUUID(java.lang.String)
	 */
	@Override
	public String addCustomerUUID(String customerId) {
		if( _logger.isDebugEnabled() ) {  
			_logger.info("ENtered addCustomerUUID for customerId " + customerId);
		} 
		
		String uuid = UUIDUtil.generateUUID();

		CustomerUUID customerUuid = new CustomerUUID();
		customerUuid.setCustomerID(customerId);
		customerUuid.setCustomerUUID(uuid);
		customerUuid.setCreatedBy("MeeMeeMeee");


		
		try {
//			beginAndClear();
			
			entityManager.persist(customerUuid);
	      System.out.println("customerUuid persisted" + customerUuid);
//			commit();
			
	      return uuid;
	      
	    } catch(EntityExistsException e){
	    	System.out.println("Caught EExistsException for " + uuid);
	        //Entity you are trying to insert already exist, then call merge method
	    	customerUuid = entityManager.merge(customerUuid);
	    	System.out.println("Caught entityexistsexception");
	    	System.out.println("uuid = " + uuid);
	    	System.out.println("customerUuid.getCustomerUUID() is " + customerUuid.getCustomerUUID());
	    	
	    	return customerUuid.getCustomerUUID();
	    	
	    } catch (Exception e) {
	    	
	      _logger.error("Error in adding CustomerUUID for customerId " + customerId, e);
	      /*if (isActive()) {
			rollback();
	      }*/
	    }
	    return null;

	}

	/* Returns the customer_uuid value for the customer id
	 * @see org.bawaweb.dao.CustomerUUIDDAO#getCustomerUUID(java.lang.String)
	 */
	@Override
	public String getCustomerUUID(String customerId) {

		_logger.info("Accessed CustomerUUIDAOImpl.getCustomerUUID with custID " +  customerId);
		
		try {
			
			// for some reason 
			// CustomerUUID customerUuid = entityManager.find(org.bawaweb.model.CustomerUUID.class, customerId);
			// was not working
			List<CustomerUUID> result = entityManager.createQuery( "from CustomerUUID", org.bawaweb.core.model.CustomerUUID.class ).getResultList();
			for ( CustomerUUID customerUUID : result ) {
				String custId = customerUUID.getCustomerID();
				if (custId.equals(customerId)) {
					
					_logger.info("Found UUID in UUIDDao.getCust for customer  " + customerId);					
					return customerUUID.getCustomerUUID();
				}
			}
			
		} catch (Exception e) {
	    	_logger.error("Failed to retrieve uuid for customer " + customerId, e);
	    	e.printStackTrace();
	    }
		
		_logger.info("Customer with id not in database " + customerId);
		return null;
	}

}
