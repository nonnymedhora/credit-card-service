package org.bawaweb.server.dao;

import java.util.List;

import org.bawaweb.core.model.CustomerCard;


/**
 * @author medhoran
 * DAO interfacing with customer_card
 *
 */

// will be called by services layer

public interface CustomerCardDAO {

//	public List<CustomerCard> getCustomerCards(String customerUUID);
	
	public List<CustomerCard> getCustomerCards(Integer customerId);
	
	public CustomerCard addCustomerCard(CustomerCard customerCard);

//	public boolean updateExistingCustomerCardsToNonPrimary(String customerUuid, Integer userId);

	public CustomerCard getCustomerCard(String cardId);
}
