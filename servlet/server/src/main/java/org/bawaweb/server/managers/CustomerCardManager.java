/**
 * 
 */
package org.bawaweb.server.managers;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.persistence.RollbackException;
import javax.ws.rs.core.CacheControl;
//import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.bawaweb.server.crypt.CreditCardEncrypter;
import org.bawaweb.server.dao.CardAuditLogDAO;
import org.bawaweb.server.dao.CardAuditLogDAOImpl;
import org.bawaweb.server.dao.CustomerCardDAO;
import org.bawaweb.server.dao.CustomerCardDAOImpl;
import org.bawaweb.server.dao.CustomerUUIDDAO;
import org.bawaweb.server.dao.CustomerUUIDDAOImpl;
import org.bawaweb.server.dao.HibernateDAO;
import org.bawaweb.core.model.CardAuditLog;
import org.bawaweb.core.model.CustomerCard;
import org.bawaweb.server.validator.CreditCardValidator;

/**
 * @author medhoran
 * This class is called by services and will interact with daos, encryption, probly utils too etc
 */
public class CustomerCardManager {
	
	private static final Log _logger = LogFactory.getLog(CustomerCardManager.class);
	
	private CustomerCardDAO customerCardDao = new CustomerCardDAOImpl();
	private CustomerUUIDDAO customerUuidDao = new CustomerUUIDDAOImpl();
	private CardAuditLogDAO cardAuditLogDao = new CardAuditLogDAOImpl();
	
	private final static String REST_CARD_SERVICE = "https://localhost:9000/customerCardService/customerCards/";
	
/*	
	public Response getCustomerCards(String customerId) {
		_logger.info("customerId is >>"+customerId+"<<");
		_logger.info("----invoking getCardNumber, Customer id is: " + customerId);
		
		System.out.println("customerId is >>"+customerId+"<<");
		System.out.println("----invoking getCardNumber, Customer id is: " + customerId);
        if (!StringUtils.isBlank(customerId)) {
        	
        	String customerUUID = customerUuidDao.getCustomerUUID(customerId);
        	System.out.println("in manager getcardnumber - customerUUID is " + customerUUID);
        	
			if ( !StringUtils.isBlank(customerUUID) ) {
				
				HibernateDAO dao = new HibernateDAO();
				dao.beginAndClear();
				
				List<CustomerCard> customerCards = customerCardDao.getCustomerCards(customerUUID);

				System.out.println("in manager getnumber uuid not blank -- list customerCards is nukk " + (customerCards==null) );
				
				if ( customerCards != null && customerCards.size() > 0 ) {
					
					for (CustomerCard aCard : customerCards) {
						String decryptedCardNumber = CreditCardEncrypter.decryptCardNumber(aCard.getCardNumber());
						
						// we dont want to change and replace encrypted card number in db with the decrypted one
						// which we return in the response to the user
						dao.detach(aCard);
						
						aCard.setCardNumber(decryptedCardNumber);
					}
					
					// this is causing a 500 error in the logs
					// not its not - but we wont use anywayz for now
					// and use the wrapper class instead
					// GenericEntity<List<CustomerCard>> customerCardsEntity = new GenericEntity<List<CustomerCard>>(customerCards){};
					
					if ( cardAuditLogDao.addCardAuditLog(customerCards) ) {
						// note here in the response the card added contains the encrypted card number
						// and not the actual card numer
						try {
							dao.commit();
							dao.close();
							
							// return Response.ok(customerCardsEntity).build();
							
							CustomerCardsWrapper wrapper = new CustomerCardsWrapper();
							for (CustomerCard aCard : customerCards) {
								wrapper.add(aCard);
							}
							
							ResponseBuilder builder = getResponseBuilder(wrapper, true, null);
							return builder.build();
							
						} catch (RollbackException e) {
							_logger.error("Uh oh -- need to roll back could not persist", e);
							return Response.ok("Uh oh -- we need to roll back").cacheControl(getCacheControl()).build();
							
							_logger.error("Uh oh -- need to roll back could not persist  LLLLLLLLLLL", e  );
							String errMessage = "Uh oh -- need to roll back could not persist  LLLLLLLLLLL";
							ResponseBuilder builder = getResponseBuilder(errMessage, false, null);
							return builder.build();
							
							
						}
						
					} else { 
						System.out.println("Uh oh -- we need to roll back");
						dao.rollback();
						dao.close();
						
						return Response.ok("Uh oh -- we need to roll back").cacheControl(getCacheControl()).build();
					}
					
				
				} else {
					System.out.println("in carddaoimpl - getnumber -- else  custCards IZ null");
					return Response.ok("Customer has NO cards").cacheControl(getCacheControl()).build();
				
				}
			
			} else {
				_logger.info("Invalid info -- customer ID is null, or customer does not exist");
				return Response.ok("Invalid info -- customer ID is null, or customer UUID does not exist for customer id " + customerId).cacheControl(getCacheControl()).build();
			
			}
			
		} else {
			_logger.info("Invalid info -- customer ID is null");
			return Response.ok("Invalid info -- customer ID is null").cacheControl(getCacheControl()).build();
		}
	}
	
*/

	public Response getCustomerCard(String cardId) {
		if(_logger.isInfoEnabled()) {_logger.info("Enetered getCustomerCard for cardId " + cardId);}
		
		if (!StringUtils.isBlank(cardId)) {
			try {
				HibernateDAO dao = new HibernateDAO();
				dao.beginAndClear();
				
				CustomerCard theCard = customerCardDao.getCustomerCard(cardId);
				
				if ( theCard != null ) {
					System.out.println("Found card with id " + cardId);
					
					// we dont want this to be persisted 
					dao.detach(theCard);
					// get the encrypted number
					String encryptedNumber = theCard.getCardNumber();
					String decrytpedNumber = CreditCardEncrypter.decryptCardNumber(encryptedNumber);
					
					theCard.setCardNumber(decrytpedNumber);
					
					CardAuditLog cardAuditLog = new CardAuditLog();
					int requestId = cardAuditLogDao.getNextRequestId();
System.out.println("next request id is " + requestId);					
//					cardAuditLog.setCustomerId(theCard.getCustomerId());
					cardAuditLog.setCardId(theCard.getCardId());
					cardAuditLog.setRequestId(requestId);
					cardAuditLog.setClientId(987687);///@TODAO - how to get these
					cardAuditLog.setConciergeId(444555566);
					cardAuditLog.setRequestType('A');
					cardAuditLog.setCreatedBy("Me1255344");
					cardAuditLog.setUserId(44444444);
					
					if ( cardAuditLogDao.addCardAuditLog(cardAuditLog)) {
						
						dao.commit();

						ResponseBuilder builder = getResponseBuilder(theCard, true, null);
						return builder.build();
						
					} else {
						
						System.out.println("Uh oh -- we need to roll back");
						dao.rollback();
						dao.close();
						
						ResponseBuilder builder = getResponseBuilder("Uh oh -- we need to roll back", false, null);
						return builder.build();
						
					}
					
					
				} else {
					System.out.println("in carddaoimpl - getnumber -- else  custCards IZ null");
					ResponseBuilder builder = getResponseBuilder("Customer has NO cards", false, null);
					return builder.build();
				}
				
			} catch (RollbackException r) {
				_logger.error("Error had to roll back transaction for get with cardId " + cardId, r);
				String errMessage = "Uh oh -- need to roll back could not persist  LLLLLLLLLLL";
				ResponseBuilder builder = getResponseBuilder(errMessage, false, null);
				return builder.build();
			} catch (GeneralSecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				_logger.error("Failed to decrypt the card number",e);
				String errMessage = "Failed to decrypt the card number";
				ResponseBuilder builder = getResponseBuilder(errMessage, false, null);
				return builder.build();
			}
		}
		
		System.out.println("CARDID was blacnk");
		ResponseBuilder builder = getResponseBuilder("Blank card id entered", false, null);
		return builder.build();
	}
	
	
	public Response addCustomerCard(CustomerCard customerCard) {
		_logger.info("123    " + new java.util.Date().getTime() + "\n    in CCManager.add  customerCard is " + customerCard);

		String cardNumber = customerCard.getCardNumber();	// this is the normal number - not encrypted
		
			
			if (!CreditCardValidator.validateCardNumber(customerCard)) {
				// invalid card number
				ResponseBuilder builder = getResponseBuilder("You have entered an invalid card number " + customerCard, false, null);
				return builder.build();
			} else {
				// valid card number
				HibernateDAO dao = new HibernateDAO();
				dao.beginAndClear();
				
				// card number to be added
				// not doing the primary crap
				
				// encrypt the card first
				String encryptedCardNumber = null;
				try {
					encryptedCardNumber = CreditCardEncrypter.encryptCardNumber(cardNumber);
				} catch (GeneralSecurityException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				customerCard.setCardNumber(encryptedCardNumber);
				if (customerCardDao.addCustomerCard(customerCard) != null) {
System.out.println("custDAO added card -- cardId is " + customerCard.getCardId());

//					dao.flush();
					
					// add the audit log info
					CardAuditLog cardAuditLog = new CardAuditLog();
					
					int requestId = cardAuditLogDao.getNextRequestId();
					cardAuditLog.setCustomerId(99997788);//customerId);
					cardAuditLog.setCardId(customerCard.getCardId());
					cardAuditLog.setRequestId(requestId);
					cardAuditLog.setClientId(new Integer(Integer.MAX_VALUE));///@TODAO - how to get these
					cardAuditLog.setConciergeId(9876543);
					cardAuditLog.setRequestType('A');
					cardAuditLog.setCreatedBy("Me12344");
					cardAuditLog.setUserId(33333333);
					
					if ( cardAuditLogDao.addCardAuditLog(cardAuditLog) ) {
						// note here in the response the card added contains the encrypted card number
						// and not the actual card numer
						System.out.println("Calling commit");
						try {
							dao.commit();
							dao.close();
							String uri = REST_CARD_SERVICE  + customerCard.getCardId();
							System.out.println("uri is " + uri);
							ResponseBuilder builder = getResponseBuilder(customerCard, true, uri);
							return builder.build();
						} catch (RollbackException e) {
							_logger.error("Error in peristing info to database ", e  );
							String errMessage = "Uh oh -- we need to roll back -- 3456765434567543";
							ResponseBuilder builder = getResponseBuilder(errMessage, false, null);
							return builder.build();
						}
						
					} else { 
						dao.rollback();
						dao.close();
						String errMessage = "Uh oh -- we need to roll back -- could not add to the audit log";
						ResponseBuilder builder = getResponseBuilder(errMessage, false, null);
						return builder.build();
					}
				} else {
					dao.rollback();
					dao.close();
					String errMessage = "Error could not add cust card " + customerCard;
					ResponseBuilder builder = getResponseBuilder(errMessage, false, null);
					return builder.build();
				}
				
			}
			
	}
	
	


	public Response getCustomerCard(String customerId, String cardId) {
		// TODO Auto-generated method stub
		return null;
	}

/////////////////////////////////////////  v1 ///////////////////////////////////////////////////////////////////////	
//	
//	public Response addCustomerCardV1(CustomerCard customerCard) {
//		_logger.info("123    " + new java.util.Date().getTime() + "\n    in CCManager.add  customerCard is " + customerCard);
//		String customerId = customerCard.getCustomerId();
//		
//		if (!StringUtils.isBlank(customerId)) {
//			
//			_logger.info("----invoking addCustomerCard, Customer id is: " + customerCard.getCustomerId());
//			
//			// checks if the card number is valid
//			if (!CreditCardValidator.validateCardNumber(customerCard)) {
//				ResponseBuilder builder = getResponseBuilder("You have entered an invalid card number " + customerCard, false, null);
//				return builder.build();
//				
//				/*return Response.status(Response.Status.PRECONDITION_FAILED)
//						.entity("You have entered an invalid card number " + customerCard)
//						.cacheControl(getCacheControl()).build();*/
//			}
//			
//			HibernateDAO dao = new HibernateDAO();
//			dao.beginAndClear();
//		
//			// adding to uuid
//			String customerUuid = customerUuidDao.getCustomerUUID(customerId);
//			if ( customerUuid == null ) {
//				System.out.println("Did not find uiud for customerid " + customerId);
//				customerUuid = customerUuidDao.addCustomerUUID(customerId);
//			}
//			
//			if (!StringUtils.isBlank(customerUuid)) {
//				
//				customerCard.setCustomerUUID(customerUuid);
//				
//
//				String cardNumber = customerCard.getCardNumber();	// unencrypted
//				
//				String encryptedCardNumber = CreditCardEncrypter.encryptCardNumber(cardNumber);
//				customerCard.setCardNumber(encryptedCardNumber);
//
//				// get the existing cards
//				List<CustomerCard> existingCards = customerCardDao.getCustomerCards(customerUuid);
//
//				if ( existingCards != null ) {
//					
//					// get decrypted card number of the existing cards and
//					// make sure we are not adding in a duplicate
//					for (CustomerCard existingCard : existingCards ) {
//						String existEncryptedCardNumber = existingCard.getCardNumber();
//						String decryptedCardNumber = CreditCardEncrypter.decryptCardNumber(existEncryptedCardNumber);
//						if ( cardNumber.equals(decryptedCardNumber) ) {
//							return Response.ok().entity("Error Customer Card " + decryptedCardNumber + " already exists").cacheControl(getCacheControl()).build();
//						}
//					}
//					
//					
//					if ( customerCard.isPrimary() ) { // if current card is primary only
//						
//						boolean existingCardsUpdated = customerCardDao.updateExistingCustomerCardsToNonPrimary(customerUuid, 44444444);
//						System.out.println("in manage add -- within primary before calling dao the customer card is \n"+ customerCard);
//						
//						if (existingCardsUpdated && customerCardDao.addCustomerCard(customerCard) != null) {
//							// add the audit log info
//							
//							CardAuditLog cardAuditLog = new CardAuditLog();
//
//							
//							int requestId = cardAuditLogDao.getNextRequestId();
//							
//							cardAuditLog.setCardId(customerCard.getCardId());
//							cardAuditLog.setRequestId(requestId);
//							cardAuditLog.setClientId(987687);///@TODAO - how to get these
//							cardAuditLog.setConciergeId(444555566);
//							cardAuditLog.setCustomerUUID(customerUuid);
//							cardAuditLog.setRequestType('A');
//							cardAuditLog.setCreatedBy("Me1255344");
//							cardAuditLog.setUserId(44444444);
//							
//							
//							if ( cardAuditLogDao.addCardAuditLog(cardAuditLog) ) {
//								// note here in the response the card added contains the encrypted card number
//								// and not the actual card numer
//								try {
//									System.out.println("Calling commit");
//									dao.commit();
//									dao.close();
//									
//									String uri = REST_CARD_SERVICE + customerId + "/" + customerCard.getCardId();
//									System.out.println("uri is " + uri);
//									ResponseBuilder builder = getResponseBuilder(customerCard, true, uri);
//									return builder.build();
////								return Response.status(Response.Status.OK).entity(customerCard).type(MediaType.APPLICATION_XML).cacheControl(getCacheControl()).build();
//								} catch (RollbackException e) {
//									/*_logger.error("Uh oh -- we need to roll back 5478394756", e);
//									return Response.ok().cacheControl(getCacheControl()).entity("Error could not add cust card").build();
//									*/
//									_logger.error("Uh oh -- we need to roll back 5478394756", e  );
//									String errMessage = "Uh oh -- we need to roll back 5478394756";
//									ResponseBuilder builder = getResponseBuilder(errMessage, false, null);
//									return builder.build();
//								}
//								
//							} else { 
//								System.out.println("Uh oh -- we need to roll back");
//								return Response.ok("Uh oh -- we need to roll back").build();
//							}
//						
//						} else {
//							
//							dao.rollback();
//							dao.close();
//							return Response.ok().cacheControl(getCacheControl()).entity("Error could not add cust card").build();
//						}
//						
//					} else {
//						// just add the card
//						if (customerCardDao.addCustomerCard(customerCard) != null) {
//							// add the audit log info
//							CardAuditLog cardAuditLog = new CardAuditLog();
//							
//							int requestId = cardAuditLogDao.getNextRequestId();
//							
//							cardAuditLog.setCardId(customerCard.getCardId());
//							cardAuditLog.setRequestId(requestId);
//							cardAuditLog.setClientId(new Integer(Integer.MIN_VALUE));///@TODAO - how to get these
//							cardAuditLog.setConciergeId(778722);
//							cardAuditLog.setCustomerUUID(customerUuid);
//							cardAuditLog.setRequestType('A');
//							cardAuditLog.setCreatedBy("JarJar");
//							cardAuditLog.setUserId(7778888);
//							
//							if ( cardAuditLogDao.addCardAuditLog(cardAuditLog) ) {
//								// note here in the response the card added contains the encrypted card number
//								// and not the actual card numer
//								System.out.println("Calling commit");
//								try {
//									dao.commit();
//									dao.close();
//									
//									String uri = REST_CARD_SERVICE + customerId + "/" + customerCard.getCardId();
//									System.out.println("uri is " + uri);
//									ResponseBuilder builder = getResponseBuilder(customerCard, true, uri);
//									return builder.build();
//								} catch (RollbackException e) {
//									_logger.error("Error in peristing info to database ", e  );
//									String errMessage = "Uh oh -- we need to roll back -- 3456765434567543";
//									ResponseBuilder builder = getResponseBuilder(errMessage, false, null);
//									return builder.build();
////									return Response.ok("Uh oh -- we need to roll back -- 3456765434567543").cacheControl(getCacheControl()).build();
//								}
//								
////								return Response.ok(customerCard).build();
//								/*return Response.status(Response.Status.OK).cacheControl(getCacheControl()).entity(customerCard).type(MediaType.APPLICATION_XML).build();*/
//								
//							} else { 
//								dao.rollback();
//								dao.close();
//								return Response.ok("Uh oh -- we need to roll back -- could not add to the audit log").cacheControl(getCacheControl()).build();
//							}
//							
//						
//							
//							/*dao.commit();
//							dao.close();
//							
//							ResponseBuilder builder = getResponseBuilder(customerCard, true, uri);
//							return builder.build();
//							
////							return Response.ok(customerCard).build();
//*/							
//						} else {
//							dao.rollback();
//							dao.close();
//							return Response.ok().cacheControl(getCacheControl()).entity("Error could not add cust card").build();
//						}
//					}
//					
//				} else {
//					// user has no existing cards - so the first one in the system is primary
//					customerCard.setPrimary(true);
//					
//					//
//					if (customerCardDao.addCustomerCard(customerCard) != null) {
//						// add the audit log info
//						CardAuditLog cardAuditLog = new CardAuditLog();
//						
//						int requestId = cardAuditLogDao.getNextRequestId();
//						
//						cardAuditLog.setCardId(customerCard.getCardId());
//						cardAuditLog.setRequestId(requestId);
//						cardAuditLog.setClientId(new Integer(Integer.MAX_VALUE));///@TODAO - how to get these
//						cardAuditLog.setConciergeId(9876543);
//						cardAuditLog.setCustomerUUID(customerUuid);
//						cardAuditLog.setRequestType('A');
//						cardAuditLog.setCreatedBy("Me12344");
//						cardAuditLog.setUserId(33333333);
//						
//						if ( cardAuditLogDao.addCardAuditLog(cardAuditLog) ) {
//							// note here in the response the card added contains the encrypted card number
//							// and not the actual card numer
//							System.out.println("Calling commit");
//							try {
//								dao.commit();
//								dao.close();
//								String uri = REST_CARD_SERVICE + customerId + "/" + customerCard.getCardId();
//								System.out.println("uri is " + uri);
//								ResponseBuilder builder = getResponseBuilder(customerCard, true, uri);
//								return builder.build();
//							} catch (RollbackException e) {
//								_logger.error("Error in peristing info to database ", e  );
//								String errMessage = "Uh oh -- we need to roll back -- 3456765434567543";
//								ResponseBuilder builder = getResponseBuilder(errMessage, false, null);
//								return builder.build();
////								return Response.ok("Uh oh -- we need to roll back -- 3456765434567543").cacheControl(getCacheControl()).build();
//							}
//							
////							return Response.ok(customerCard).build();
//							/*return Response.status(Response.Status.OK).cacheControl(getCacheControl()).entity(customerCard).type(MediaType.APPLICATION_XML).build();*/
//							
//						} else { 
//							dao.rollback();
//							dao.close();
//							return Response.ok("Uh oh -- we need to roll back -- could not add to the audit log").cacheControl(getCacheControl()).build();
//						}
//						
//					} else {
//						
//						dao.rollback();
//						dao.close();
//						return Response.ok().cacheControl(getCacheControl()).entity("Error could not add cust card").build();
//				
//					}
//					//
//				}
//				
//			} else {
//				dao.rollback();
//				dao.close();
//				return Response.ok().cacheControl(getCacheControl()).entity("Error could not create user uid for customer " + customerId).build();
//			}
//			
//			
//		} else {
//			
//			return Response.ok().cacheControl(getCacheControl()).entity(Response.Status.PRECONDITION_FAILED).build();
//		}
//		
//
//	}
//
//	
/////////////////////////////////////////// ends v1 ///////////////////////////////////////////////////////////////////////	
	

/************************ throws return response error *********************************/	
//	public Response addCustomerCard(CustomerCard customerCard) {
//		_logger.info("123    " + new java.util.Date().getTime() + "\n    in CCManager.add  customerCard is " + customerCard);
//		String customerId = customerCard.getCustomerId();
//		
//		if (!StringUtils.isBlank(customerId)) {
//			
//			_logger.info("----invoking addCustomerCard, Customer id is: " + customerCard.getCustomerId());
//			
//			// checks if the card number is valid
//			if (!CreditCardValidator.validateCardNumber(customerCard)) {
//				ResponseBuilder builder = getResponseBuilder("You have entered an invalid card number " + customerCard, false, null);
//				return builder.build();
//				
//				/*return Response.status(Response.Status.PRECONDITION_FAILED)
//						.entity("You have entered an invalid card number " + customerCard)
//						.cacheControl(getCacheControl()).build();*/
//			}
//			
//			HibernateDAO dao = new HibernateDAO();
//			dao.beginAndClear();
//		
//			// adding to uuid
//			String customerUuid = customerUuidDao.getCustomerUUID(customerId);
//			if ( customerUuid == null ) {
//				System.out.println("Did not find uiud for customerid " + customerId);
//				customerUuid = customerUuidDao.addCustomerUUID(customerId);
//			}
//			
//			if (!StringUtils.isBlank(customerUuid)) {
//				
//				customerCard.setCustomerUUID(customerUuid);
//				
//
//				String cardNumber = customerCard.getCardNumber();	// unencrypted
//				
//				String encryptedCardNumber = CreditCardEncrypter.encryptCardNumber(cardNumber);
//				customerCard.setCardNumber(encryptedCardNumber);
//
//
//				URI uri = null;
//				try {
//					uri = new URI(REST_CARD_SERVICE + customerId);
//				} catch (URISyntaxException e) {
//					e.printStackTrace();
//					_logger.error("could not generate endpoint uri for " + REST_CARD_SERVICE + customerId, e);
//					
//					ResponseBuilder builder = getResponseBuilder("could not generate endpoint uri for " + REST_CARD_SERVICE + customerId, false, null);
//					return builder.build();
//				}
//				
//				// get the existing cards
//				List<CustomerCard> existingCards = customerCardDao.getCustomerCards(customerUuid);
//
//				if ( existingCards != null ) {
//					
//					// get decrypted card number of the existing cards and
//					// make sure we are not adding in a duplicate
//					for (CustomerCard existingCard : existingCards ) {
//						String existEncryptedCardNumber = existingCard.getCardNumber();
//						String decryptedCardNumber = CreditCardEncrypter.decryptCardNumber(existEncryptedCardNumber);
//						if ( cardNumber.equals(decryptedCardNumber) ) {
//							return Response.ok().entity("Error Customer Card " + decryptedCardNumber + " already exists").cacheControl(getCacheControl()).build();
//						}
//					}
//					
//					
//					if ( customerCard.isPrimary() ) { // if current card is primary only
//						
//						boolean existingCardsUpdated = customerCardDao.updateExistingCustomerCardsToNonPrimary(customerUuid, 44444444);
//						System.out.println("in manage add -- within primary before calling dao the customer card is \n"+ customerCard);
//						
//						if (existingCardsUpdated && customerCardDao.addCustomerCard(customerCard) != null) {
//							// add the audit log info
//							
//							CardAuditLog cardAuditLog = new CardAuditLog();
//
//							
//							int requestId = cardAuditLogDao.getNextRequestId();
//							
//							cardAuditLog.setCardId(customerCard.getCardId());
//							cardAuditLog.setRequestId(requestId);
//							cardAuditLog.setClientId(987687);///@TODAO - how to get these
//							cardAuditLog.setConciergeId(444555566);
//							cardAuditLog.setCustomerUUID(customerUuid);
//							cardAuditLog.setRequestType('A');
//							cardAuditLog.setCreatedBy("Me1255344");
//							cardAuditLog.setUserId(44444444);
//							
//							
//							if ( cardAuditLogDao.addCardAuditLog(cardAuditLog) ) {
//								// note here in the response the card added contains the encrypted card number
//								// and not the actual card numer
//								try {
//									System.out.println("Calling commit");
//									dao.commit();
//									dao.close();
//									
//									ResponseBuilder builder = getResponseBuilder(customerCard, true, uri);
//									return builder.build();
//									/*Response r = builder.build();
//									return r.created(new URI( REST_CARD_SERVICE + customerId )).build();
//									*/
////									return Response.created(new URI( REST_CARD_SERVICE + customerId )).build();
////								return Response.status(Response.Status.OK).entity(customerCard).type(MediaType.APPLICATION_XML).cacheControl(getCacheControl()).build();
//								} catch (RollbackException e) {
//									/*_logger.error("Uh oh -- we need to roll back 5478394756", e);
//									return Response.ok().cacheControl(getCacheControl()).entity("Error could not add cust card").build();
//									*/
//									_logger.error("Uh oh -- we need to roll back 5478394756", e  );
//									String errMessage = "Uh oh -- we need to roll back 5478394756";
//									ResponseBuilder builder = getResponseBuilder(errMessage, false, null);
//									return builder.build();
//								}
//								
//							} else { 
//								System.out.println("Uh oh -- we need to roll back");
//								return Response.ok("Uh oh -- we need to roll back").build();
//							}
//						
//						} else {
//							
//							dao.rollback();
//							dao.close();
//							return Response.ok().cacheControl(getCacheControl()).entity("Error could not add cust card").build();
//						}
//						
//					} else {
//						// just add the card
//						if (customerCardDao.addCustomerCard(customerCard) != null) {
//							CardAuditLog cardAuditLog = new CardAuditLog();
//
//							
//							int requestId = cardAuditLogDao.getNextRequestId();
//							
//							cardAuditLog.setCardId(customerCard.getCardId());
//							cardAuditLog.setRequestId(requestId);
//							cardAuditLog.setClientId(987687);///@TODAO - how to get these
//							cardAuditLog.setConciergeId(444555566);
//							cardAuditLog.setCustomerUUID(customerUuid);
//							cardAuditLog.setRequestType('A');
//							cardAuditLog.setCreatedBy("Me1255344");
//							cardAuditLog.setUserId(44444444);
//							
//							if ( cardAuditLogDao.addCardAuditLog(cardAuditLog) ) {
//								// note here in the response the card added contains the encrypted card number
//									// and not the actual card numer
//									try {
//										dao.commit();
//										dao.close();
//										
//										ResponseBuilder builder = getResponseBuilder(customerCard, true, uri);
//										return builder.build();
//										
//			//							return Response.ok(customerCard).build();
//									} catch (RollbackException e) {
//										_logger.error("Error in peristing info to database ", e  );
//										String errMessage = "Uh oh -- we need to roll back -- 3456765434567543";
//										ResponseBuilder builder = getResponseBuilder(errMessage, false, null);
//										return builder.build();
////										return Response.ok("Uh oh -- we need to roll back -- 3456765434567543").cacheControl(getCacheControl()).build();
//									}
//								
//							} else {
//								dao.rollback();
//								dao.close();
//								return Response.ok().cacheControl(getCacheControl()).entity("Error could not add cust card").build();
//							}
//						}
//					}
//					
//				} else {
//					// user has no existing cards - so the first one in the system is primary
//					customerCard.setPrimary(true);
//					
//					//
//					if (customerCardDao.addCustomerCard(customerCard) != null) {
//						// add the audit log info
//						CardAuditLog cardAuditLog = new CardAuditLog();
//						
//						int requestId = cardAuditLogDao.getNextRequestId();
//						
//						cardAuditLog.setCardId(customerCard.getCardId());
//						cardAuditLog.setRequestId(requestId);
//						cardAuditLog.setClientId(new Integer(Integer.MAX_VALUE));///@TODAO - how to get these
//						cardAuditLog.setConciergeId(9876543);
//						cardAuditLog.setCustomerUUID(customerUuid);
//						cardAuditLog.setRequestType('A');
//						cardAuditLog.setCreatedBy("Me12344");
//						cardAuditLog.setUserId(33333333);
//						
//						if ( cardAuditLogDao.addCardAuditLog(cardAuditLog) ) {
//							// note here in the response the card added contains the encrypted card number
//							// and not the actual card numer
//							System.out.println("Calling commit");
//							try {
//								dao.commit();
//								dao.close();
//								
//								ResponseBuilder builder = getResponseBuilder(customerCard, true, uri);
//								return builder.build();
//							} catch (RollbackException e) {
//								_logger.error("Error in peristing info to database ", e  );
//								String errMessage = "Uh oh -- we need to roll back -- 3456765434567543";
//								ResponseBuilder builder = getResponseBuilder(errMessage, false, null);
//								return builder.build();
////								return Response.ok("Uh oh -- we need to roll back -- 3456765434567543").cacheControl(getCacheControl()).build();
//							}
//							
////							return Response.ok(customerCard).build();
//							/*return Response.status(Response.Status.OK).cacheControl(getCacheControl()).entity(customerCard).type(MediaType.APPLICATION_XML).build();*/
//							
//						} else { 
//							dao.rollback();
//							dao.close();
//							return Response.ok("Uh oh -- we need to roll back -- could not add to the audit log").cacheControl(getCacheControl()).build();
//						}
//						
//					} else {
//						
//						dao.rollback();
//						dao.close();
//						return Response.ok().cacheControl(getCacheControl()).entity("Error could not add cust card").build();
//				
//					}
//					//
//				}
//				
//			} else {
//				dao.rollback();
//				dao.close();
//				return Response.ok().cacheControl(getCacheControl()).entity("Error could not create user uid for customer " + customerId).build();
//			}
//			
//			
//		} else {
//			
//			return Response.ok().cacheControl(getCacheControl()).entity(Response.Status.PRECONDITION_FAILED).build();
//		}
//		
////		System.out.println("YOU SHOULD NOT BE SEEING ME");
////		return null;
//		
//
//	}
/************************************************************ends return response error *********************/

	public CustomerCardDAO getCustomerCardDao() {
		return customerCardDao;
	}

	public void setCustomerCardDao(CustomerCardDAO customerCardDao) {
		this.customerCardDao = customerCardDao;
	}
	
	
	/**
	 * Generates the ResponseBuilder which is used to send out the Response
	 * @param entity -- the contents of the response
	 * @param ok -- whether the Response is for an 'ok' or 'notAcceptable' Response
	 * @param location -- when Response informs of a newly created resource, used when ok = true -- the location specifies the endpoint
	 * where the new resource is available
	 * @return ResponseBuilder with appropriate Response
	 */
	private ResponseBuilder getResponseBuilder(Object entity, boolean ok, String location) {
		ResponseBuilder builder;
		GregorianCalendar now = new GregorianCalendar();
		GregorianCalendar nextUpdate = getNextUpdateTime(now);
		int maxAge = (int) ((nextUpdate.getTimeInMillis() - now.getTimeInMillis()) / 1000L);
		
		if ( ok ) {
			System.out.println("in ok -- getResponseBuilder");
			if ( location == null ) {
				builder = Response.ok(entity);
			} else {
				try {
					URI uri = new URI(location);
					builder = Response.created(uri);
				} catch (URISyntaxException e) {
					_logger.error("Could not create URI for " + location, e);
					builder = Response.notModified().entity(entity);
				}
			}
			
		} else {
			System.out.println("in NOT OK in getResponseBuilder");
			builder = Response.notAcceptable(null).entity(entity);
		}
		
		CacheControl cacheControl = new CacheControl();
		cacheControl.setMaxAge(maxAge);
		
		builder.cacheControl(cacheControl);
		builder.expires(nextUpdate.getTime());
		builder.type(MediaType.APPLICATION_XML);
		builder.header("ABCDE", "1234567890");
		
		return builder;
	}
	
	
	
	// for setting  cache control header in the response
	private CacheControl getCacheControl() {
		GregorianCalendar now = new GregorianCalendar();
		GregorianCalendar nextUpdate = getNextUpdateTime(now);
		int maxAge = (int) ((nextUpdate.getTimeInMillis() - now.getTimeInMillis()) / 1000L);
		CacheControl cacheControl = new CacheControl();
		cacheControl.setMaxAge(maxAge);
		return cacheControl;
	}
	

	
	
	private GregorianCalendar getNextUpdateTime(GregorianCalendar now) {
		GregorianCalendar nextUpdate = new GregorianCalendar();
		nextUpdate.setTime(now.getTime());
		nextUpdate.set(Calendar.HOUR_OF_DAY, 10);
		nextUpdate.set(Calendar.MINUTE, 0);
		nextUpdate.set(Calendar.SECOND, 0);
		nextUpdate.set(Calendar.MILLISECOND, 0);
		if (now.get(Calendar.HOUR_OF_DAY) >= 10) {
			nextUpdate.add(Calendar.DAY_OF_YEAR, 1);
		}
		return nextUpdate;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {

	}
	
	
	
	//http://stackoverflow.com/questions/1603404/using-jaxb-to-unmarshal-marshal-a-liststring
	@XmlRootElement(name="CustomerCards")
	private static class CustomerCardsWrapper {
		
	       @XmlElement(name="CustomerCard")
	       List<CustomerCard> list=new ArrayList<CustomerCard>();
	       
	       CustomerCardsWrapper (){}

	       public void add(CustomerCard s){ list.add(s);}
	 }




	

}
