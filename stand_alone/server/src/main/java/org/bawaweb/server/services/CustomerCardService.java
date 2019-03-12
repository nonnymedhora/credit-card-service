package org.bawaweb.server.services;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.bawaweb.core.model.Card;
import org.bawaweb.core.model.CustomerCard;



/**
 * @author medhoran
 * Root level interface for customer card services - to get or add a card
 */

@Path("/services/")
public interface CustomerCardService {

	/**
	 * This method is used to create a card for the customer, it will contain the
	 * location of the card created in the Response
	 * 
	 * @param card - the card info we wish to add. which comes from the enterprise domain
	 * Response will contain location of created card in header or  Response has an error
	 */
	@POST
    @Path("/cards/")
//	@Produces({ "application/xml", "application/*+xml", "text/xml" })
//	@Consumes({ "application/xml", "application/*+xml", "text/xml" })
    @Produces("application/json")
    @Consumes({"application/json", "text/json"})
    Response addCustomerCard(Card card);
//    Response addCustomerCard(CustomerCard customerCard);

    /**
    * This method returns the card with number (decrypted) given the
    * card id. the cardId connects with the card_id in customer_card table
     * @param cardId - connects to the value from card_id in customer_card table
    */
	@GET
	@Path("/cards/{cardId}")
//	@Produces({ "application/xml", "application/*+xml", "text/xml" })
    @Produces("application/json")
	public Response getCustomerCard(@PathParam("cardId") String cardId);



}
