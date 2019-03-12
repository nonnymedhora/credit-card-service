/**
 * 
 */
package org.bawaweb.server.dao;

/**
 * DAO interfacing with customer_uuid
 * @author medhoran
 *
 */
public interface CustomerUUIDDAO {
	
	/**
	 * Adds a customer_id, customer_uuid 
	 * @param customerId
	 * @return the customer_uuid
	 */
	public String addCustomerUUID(String customerId);
	
	/**
	 * Returns the customer_uuid associated with customer_id in customer_uuid table.
	 * @param customerId
	 * @return
	 */
	public String getCustomerUUID(String customerId);

}
