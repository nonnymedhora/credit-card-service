package org.bawaweb.server.services;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.bawaweb.core.model.CustomerCard;



/**
 * @author medhoran
 * individual
 * https://localhost:9000/customerCardService/customerCards/778899
 */

@Path("/customerCardService/")
public interface CustomerCardService {
	
//	/**
//	 * This method returns the card number (decrypted) given the [external]
//	 * customer id
//	 * 
//	 * @param customerUUID
//	 * @return response - with the decrypted card number 
//	 */
//	@GET
//	@Path("/customerCards/{id}/")
//	@Produces({ "application/xml", "application/*+xml", "text/xml" })
//	public Response getCustomerCards(@PathParam("id") String customerUUID);
	
	/**
	 * This method is used to create a card for the customer, it will contain the
	 * generated (decrypted) card number
	 * 
	 * @param customerCard - the card info we wish to add.
	 * @return the decryoted card number created for the customer
	 */
	@POST
    @Path("/customerCards/")
	@Produces({ "application/xml", "application/*+xml", "text/xml" })
	@Consumes({ "application/xml", "application/*+xml", "text/xml" })
    Response addCustomerCard(CustomerCard customerCard);
	
//	
//	
//	@GET
//	@Path("/customerCards/{customerId}/{cardId}")
//	@Produces({ "application/xml", "application/*+xml", "text/xml" })
//	public Response getCustomerCard(@PathParam("customerId") String customerId,@PathParam("cardId") String cardId);
//	
//	
	
	@GET
	@Path("/customerCards/{cardId}")
	@Produces({ "application/xml", "application/*+xml", "text/xml" })
	public Response getCustomerCard(@PathParam("cardId") String cardId);
	
	

}
