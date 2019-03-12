package org.bawaweb.server.dao;

import org.bawaweb.core.model.CustomerCard;

/**
 * @author medhoran
 * DAO interfacing with customer_card
 *
 */

// will be called by manager layer - interacts with customer_card table

public interface CustomerCardDAO {

    public CustomerCard addCustomerCard(CustomerCard customerCard);

	public CustomerCard getCustomerCard(String cardId);
}
