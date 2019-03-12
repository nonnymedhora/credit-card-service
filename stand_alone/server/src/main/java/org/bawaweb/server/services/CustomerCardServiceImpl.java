package org.bawaweb.server.services;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.bawaweb.core.model.Card;
import org.bawaweb.server.dao.CustomerCardDAO;
import org.bawaweb.server.dao.CustomerCardDAOImpl;
import org.bawaweb.server.managers.CustomerCardManager;
import org.bawaweb.core.model.CustomerCard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author medhoran
 *
 */
//@Produces({ "application/xml", "application/*+xml", "text/xml" })
@Produces("application/json")
@Provider
@ApplicationPath("/services/")
public class CustomerCardServiceImpl extends Application implements CustomerCardService {

    @SuppressWarnings("unused")
    // Logger.
    private final static Logger _logger = LoggerFactory.getLogger(CustomerCardServiceImpl.class);

    private CustomerCardManager cardManager = new CustomerCardManager();
	
	private CustomerCardDAO customerCardDao = new CustomerCardDAOImpl();

    /**
     * This method is used to create a card for the customer, it will contain the
     * location of the card created in the Response
     *
     * @param card - the card info we wish to add.
     * @return Response will contain location of created card in header or  Response has an error
     */
	@Override
	public Response addCustomerCard(Card card) {
        if ( _logger.isInfoEnabled() )  { _logger.info("Entered CustomerCardServiceImpl.addCustomerCard for card: " + card); }
        CustomerCard customerCard = new CustomerCard(card);
		return cardManager.addCustomerCard(customerCard);
	}

    /**
     * This method returns the card with number (decrypted) given the
     * card id. the cardId connects with the card_id in customer_card table
     * @param cardId - connects to the value from card_id in customer_card table
     */
	@Override
	public Response getCustomerCard(String cardId) {
        if ( _logger.isInfoEnabled() )  { _logger.info("Entered CustomerCardServiceImpl.getCustomerCard for cardId: " + cardId); }

        return cardManager.getCustomerCard(cardId);
	}
	
	public CustomerCardDAO getCustomerCardDao() {
		return customerCardDao;
	}

	public void setCustomerCardDao(CustomerCardDAO customerCardDao) {
		this.customerCardDao = customerCardDao;
	}

}
