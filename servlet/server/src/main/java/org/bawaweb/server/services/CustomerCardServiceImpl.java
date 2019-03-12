package org.bawaweb.server.services;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.bawaweb.server.dao.CustomerCardDAO;
import org.bawaweb.server.dao.CustomerCardDAOImpl;
import org.bawaweb.server.dao.CustomerUUIDDAO;
import org.bawaweb.server.dao.CustomerUUIDDAOImpl;
import org.bawaweb.server.managers.CustomerCardManager;
import org.bawaweb.core.model.CustomerCard;


/**
 * @author medhoran
 *
 */
@Produces({ "application/xml", "application/*+xml", "text/xml" })
@Provider
@ApplicationPath("/customerCardService/")
public class CustomerCardServiceImpl extends Application implements CustomerCardService {//, MessageBodyWriter<CustomerCard> {
	private static final Log _logger = LogFactory.getLog(CustomerCardServiceImpl.class);
	
	private CustomerCardManager cardManager = new CustomerCardManager();
	
	private CustomerCardDAO customerCardDao = new CustomerCardDAOImpl();
	private CustomerUUIDDAO customerUuidDao = new CustomerUUIDDAOImpl();
	
//	v2
//	/**
//	 * This method returns the card number (decrypted) given the [external]
//	 * customer id
//	 * 
//	 * @param customerUUID
//	 * @return response - with the decrypted card number 
//	 */
//	@Override
//	public Response getCustomerCards(String customerId) {
//		return cardManager.getCustomerCards(customerId);
////		
////		_logger.info("customerId is >>"+customerId+"<<");
////		_logger.info("----invoking getCardNumber, Customer id is: " + customerId);
////		
////		System.out.println("customerId is >>"+customerId+"<<");
////		System.out.println("----invoking getCardNumber, Customer id is: " + customerId);
////        if (!StringUtils.isBlank(customerId)) {
////        	
////        	String customerUUID = customerUuidDao.getCustomerUUID(customerId);
////        	System.out.println("in serrviceimpl getcardnumber - customerUUID is " + customerUUID);
////        	
////			if (!StringUtils.isBlank(customerUUID)) {
////				List<CustomerCard> customerCards = customerCardDao.getCardNumber(customerUUID);
////				System.out.println("in cardserviceimpl getnumber uuid not blank -- list customerCards is nukk " + (customerCards==null) );
////				
////				if (customerCards != null && customerCards.size() > 0) {
////					CustomerCardsWrapper wrapper = new CustomerCardsWrapper();
////					for (CustomerCard aCard : customerCards) {
////						wrapper.add(aCard);
////					}
////					/*CustomerCardsList cardsList = new CustomerCardsList();
////					cardsList.setCardsList(customerCards);
////					
////					 */
////					/*_logger.info("Returning customer --> " + customerCard);
////					_logger.info(" with number " + customerCard.getCardNumber());*/
////					return Response.ok(wrapper).build();
////				
////				} else {
////					System.out.println("in carddaoimpl - getnumber -- else  custCards IZ null");
////					return Response.ok("Customer has NO cards").build();
////				}
////			} else {
////				_logger.info("Invalid info -- customer ID is null, or customer does not exist");
////				return Response.noContent().build();
////			}
////			
////		} else {
////			_logger.info("Invalid info -- customer ID is null");
////			return Response.noContent().build();
////		}
//	}
///// ends v2
	/**
	 * This method is used to create a card for the customer, it will contain the
	 * generated (decrypted) card number
	 * 
	 * @param customerCard - the card info we wish to add.
	 * @return the decryoted card number created for the customer
	 */
	@Override
	public Response addCustomerCard(CustomerCard customerCard) {
		System.out.println("in here1 addCustomerCard - service");
		return cardManager.addCustomerCard(customerCard);
//		
//		String customerId = customerCard.getCustomerId();
//		
//		if (!StringUtils.isBlank(customerId)) {
//			
//			_logger.info("----invoking addCustomerCard, Customer id is: " + customerCard.getCustomerId());
//			
//			// checks if the card number is valid
//			if (!CreditCardValidator.validateCardNumber(customerCard)) {
//				return Response.status(Response.Status.PRECONDITION_FAILED)
//						.entity("You have entered an invalid card number " + customerCard).build();
//			}
//		
//			// adding to uuid
//			String customerUuid = customerUuidDao.getCustomerUUID(customerId);
//			if ( customerUuid == null ) {
//				System.out.println("Did not find uiud for customerid " + customerId);
//				customerUuid = customerUuidDao.addCustomerUUID(customerId);
//			}
//			
//			if (!StringUtils.isBlank(customerUuid)) {
//				customerCard.setCustomerUUID(customerUuid);
//				
//				if (customerCardDao.addCustomerCard(customerCard) != null)
//					return Response.ok(customerCard).build();
//				else
//					return Response.noContent().entity("Error could not add cust card").build();
//			} else {
//				return Response.noContent().entity("Error could not create user uid").build();
//			}
//		} else {
//			return Response.noContent().entity(Response.Status.PRECONDITION_FAILED).build();
//		}
	}
	
	

//
//	@Override
//	public Response getCustomerCard(String customerId, String cardId) {
//		
//		return cardManager.getCustomerCard(customerId, cardId);
//	}
	


	@Override
	public Response getCustomerCard(String cardId) {
		return cardManager.getCustomerCard(cardId);
	}
	
	
	

	public CustomerCardDAO getCustomerCardDao() {
		return customerCardDao;
	}

	public void setCustomerCardDao(CustomerCardDAO customerCardDao) {
		this.customerCardDao = customerCardDao;
	}

/*	************************************
 	@Override
	public long getSize(CustomerCard arg0, Class<?> arg1, Type arg2,
			Annotation[] arg3, MediaType arg4) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations,
			MediaType mediaType) {
		return MediaType.APPLICATION_XML_TYPE.equals ( mediaType ) 
		        && type.equals ( org.bawaweb.model.CustomerCard.class );

	}

	@Override
	public void writeTo(CustomerCard arg0, Class<?> arg1, Type arg2,
			Annotation[] arg3, MediaType arg4,
			MultivaluedMap<String, Object> arg5, OutputStream arg6)
			throws IOException, WebApplicationException {
//		XStream xstream = new XStream ( new JettisonMappedXmlDriver () );

		// TODO Auto-generated method stub	
	}
***********************/	
	
	
	//http://stackoverflow.com/questions/1603404/using-jaxb-to-unmarshal-marshal-a-liststring
	@XmlRootElement(name="CustomerCards")
	private static class CustomerCardsWrapper {
		
	       @XmlElement(name="CustomerCard")
	       List<CustomerCard> list=new ArrayList<CustomerCard>();
	       
	       CustomerCardsWrapper (){}

	       public void add(CustomerCard s){ list.add(s);}
	 }

}
