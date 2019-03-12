/**
 *
 */
package org.bawaweb.server.managers;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.text.MessageFormat;
import java.util.*;
import javax.persistence.RollbackException;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.bawaweb.core.model.*;
import org.bawaweb.server.Server;
import org.bawaweb.server.ServerHostProperties;
import org.bawaweb.server.util.ResourceBundleUtil;
import org.apache.commons.lang.StringUtils;

import org.bawaweb.server.crypt.CreditCardEncrypter;
import org.bawaweb.server.dao.CardAuditLogDAO;
import org.bawaweb.server.dao.CardAuditLogDAOImpl;
import org.bawaweb.server.dao.CustomerCardDAO;
import org.bawaweb.server.dao.CustomerCardDAOImpl;
import org.bawaweb.server.dao.HibernateDAO;
import org.bawaweb.server.validator.CreditCardValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author medhoran
 * This class is called by services and will interact with daos, encryption, probly utils too etc
 */
public class CustomerCardManager {
    @SuppressWarnings("unused")
    // Logger.
    final static Logger _logger = LoggerFactory.getLogger(CustomerCardManager.class);


    private CustomerCardDAO customerCardDao = new CustomerCardDAOImpl();
    private CardAuditLogDAO cardAuditLogDao = new CardAuditLogDAOImpl();
    private static final Properties serverProperties = ServerHostProperties.getServerProperties();


    private static final String HTTP_PROTOCOL  = serverProperties.getProperty("host.protocol");
    private static final String SERVER_IP = serverProperties.getProperty("host.ip");
    private static final String PORT = serverProperties.getProperty("host.port");
    // prod
//	private final static String REST_CARD_SERVICE = "http://10.0.104.90:8080/services/cards/";//HTTP_PROTOCOL + SERVER_IP + ":" + PORT + "/services/cards/";


    //qa
//    private final static String REST_CARD_SERVICE = "http://10.0.103.112:8080/services/cards/";//HTTP_PROTOCOL + SERVER_IP + ":" + PORT + "/services/cards/";

    private final static String REST_CARD_SERVICE = HTTP_PROTOCOL + SERVER_IP + ":" + PORT + "/services/cards/";


    public Response getCustomerCard(String cardId) {
        if(_logger.isInfoEnabled()) {_logger.info("Enetered getCustomerCard for cardId " + cardId);}
        System.out.println("\n\n***************\n\nin getCC -- rb call " + ResourceBundleUtil.getValue("abcde") +"\n\n\n****************\n");

        if (!StringUtils.isBlank(cardId)) {

            try {
                HibernateDAO dao = new HibernateDAO();
                dao.beginAndClear();

                CustomerCard theCard = customerCardDao.getCustomerCard(cardId);

                if ( theCard != null ) {
                    if(_logger.isInfoEnabled()) {_logger.info("Found card with id " + cardId); }

                    // we dont want this to be persisted
                    dao.detach(theCard);
                    // get the encrypted number
                    String encryptedNumber = theCard.getValue();
                    String decrytpedNumber = CreditCardEncrypter.decryptCardNumber(encryptedNumber);

                    theCard.setValue(decrytpedNumber);

                    CardAuditLog cardAuditLog = new CardAuditLog();

                    cardAuditLog.setCardId(theCard.getCardId());
                    cardAuditLog.setSessionId(SessionInfo.getSessionId());
                    cardAuditLog.setClientId(SessionInfo.getClientId());///@TODAO - how to get these
                    cardAuditLog.setConciergeId(SessionInfo.getConciergeId());
                    cardAuditLog.setRequestType(RequestType.GET.getRequestType());
                    cardAuditLog.setCreatedBy(SessionInfo.getUserName());
                    cardAuditLog.setUserId(SessionInfo.getUserId());

                    if ( cardAuditLogDao.addCardAuditLog(cardAuditLog)) {

                        dao.commit();
                        Card aCard = theCard.transformToCard(theCard);

                        Result result = getResult(true, null);
                        result.setIsSuccess(true);


                        aCard.setResult(result);
                        ResponseBuilder builder = getResponseBuilder(aCard, true, null);
                        return builder.build();

                    } else {
                        // send a dummy card object
                        /*CustomerCard aDummyCard = new CustomerCard();
                        Card aCard = aDummyCard.transformToCard(aDummyCard);


                        Result result = getResult(false, ResourceBundleUtil.getValue("error.rollback"));
                        result.setIsSuccess(false);

                        aCard.setResult(result);


						dao.rollback();
						dao.close();
//						String errMessage = ResourceBundleUtil.getValue("error.rollback");
						ResponseBuilder builder = getResponseBuilder(aCard, false, null);*/

                        ResponseBuilder builder = getDummyCardResponseBuilder(ResourceBundleUtil.getValue("error.rollback"));
                        return builder.build();

                    }


                } else {
                    System.out.println("in carddaoimpl - getnumber -- else  custCards IZ null");
                    // send a dummy card object
                    /*CustomerCard aDummyCard = new CustomerCard();
                    Card aCard = aDummyCard.transformToCard(aDummyCard);

                    String errMessage = MessageFormat.format(ResourceBundleUtil.getValue("card.does.not.exist"), cardId);
                    Result result = getResult(false, errMessage);
                    result.setIsSuccess(false);

                    aCard.setResult(result);

                    ResponseBuilder builder = getResponseBuilder( aCard, false, null);*/
                    ResponseBuilder builder = getDummyCardResponseBuilder(MessageFormat.format(ResourceBundleUtil.getValue("card.does.not.exist"), cardId));
                    return builder.build();
                }

            } catch (RollbackException r) {
                _logger.error("Error had to roll back transaction for get with cardId " + cardId, r);

                // send a dummy card object
                /*CustomerCard aDummyCard = new CustomerCard();
                Card aCard = aDummyCard.transformToCard(aDummyCard);


                Result result = getResult(false, ResourceBundleUtil.getValue("error.rollback"));
                result.setIsSuccess(false);

                aCard.setResult(result);


                ResponseBuilder builder = getResponseBuilder(aCard, false, null);
                return builder.build();
*/
                ResponseBuilder builder = getDummyCardResponseBuilder(ResourceBundleUtil.getValue("error.rollback"));
                return builder.build();
            } catch (GeneralSecurityException e) {

                _logger.error("Failed to decrypt the card number",e);
/*
                // send a dummy card object
                CustomerCard aDummyCard = new CustomerCard();
                Card aCard = aDummyCard.transformToCard(aDummyCard);


                Result result = getResult(false, ResourceBundleUtil.getValue("card.not.decrypted"));
                result.setIsSuccess(false);

                aCard.setResult(result);

                ResponseBuilder builder = getResponseBuilder(aCard, false, null);*/
                ResponseBuilder builder = getDummyCardResponseBuilder(ResourceBundleUtil.getValue("card.not.decrypted"));
                return builder.build();
            }
        }

        _logger.warn("CARDID was blacnk");
        // send a dummy card object
        /*CustomerCard aDummyCard = new CustomerCard();
        Card aCard = aDummyCard.transformToCard(aDummyCard);


        Result result = getResult(false, ResourceBundleUtil.getValue("card.id.blank"));
        result.setIsSuccess(false);

        aCard.setResult(result);

        ResponseBuilder builder = getResponseBuilder(aCard, false, null);*/
        ResponseBuilder builder = getDummyCardResponseBuilder(ResourceBundleUtil.getValue("card.id.blank"));
        return builder.build();

    }


    public Response addCustomerCard(CustomerCard customerCard) {
        _logger.info("123    " + new java.util.Date().getTime() + "\n    in CCManager.add  customerCard is " + customerCard);

        String cardNumber = customerCard.getValue();	// this is the normal number - not encrypted


        if (!CreditCardValidator.validateCardNumber(customerCard)) {
            // invalid card number
//				ResponseBuilder builder = getResponseBuilder( MessageFormat.format(ResourceBundleUtil.getValue("card.number.invalid"), cardNumber), false, null);
            ResponseBuilder builder = getDummyCardResponseBuilder(MessageFormat.format(ResourceBundleUtil.getValue("card.number.invalid"), cardNumber));
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
                _logger.error("Failed to encrypt card with number " + cardNumber, e1);
//                    e1.printStackTrace();
//                    ResponseBuilder builder = getResponseBuilder( MessageFormat.format(ResourceBundleUtil.getValue("card.not.encrypted"), cardNumber), false, null);
                ResponseBuilder builder = getDummyCardResponseBuilder(MessageFormat.format(ResourceBundleUtil.getValue("card.not.encrypted"), cardNumber));
                return builder.build();
            }

            customerCard.setValue(encryptedCardNumber);
            if (customerCardDao.addCustomerCard(customerCard) != null) {
                _logger.info("custDAO added card -- cardId is " + customerCard.getCardId());

//					dao.flush();

                // add the audit log info
                CardAuditLog cardAuditLog = new CardAuditLog();

                cardAuditLog.setCustomerId(SessionInfo.getCustomerId());//customerId);
                cardAuditLog.setCardId(customerCard.getCardId());
                cardAuditLog.setSessionId(SessionInfo.getSessionId());
                cardAuditLog.setClientId(SessionInfo.getClientId());//@TODAO - how to get these
                cardAuditLog.setConciergeId(SessionInfo.getConciergeId());
                cardAuditLog.setRequestType(RequestType.ADD.getRequestType());
                cardAuditLog.setCreatedBy(SessionInfo.getUserName());
                cardAuditLog.setUserId(SessionInfo.getUserId());

                if ( cardAuditLogDao.addCardAuditLog(cardAuditLog) ) {
                    // note here in the response the card added contains the encrypted card number
                    // and not the actual card numer
                    System.out.println("Calling commit");
                    try {
                        dao.commit();
                        dao.close();
                        String uri = REST_CARD_SERVICE  + customerCard.getCardId();
                        System.out.println("uri is " + uri);

                        Card theCard = customerCard.transformToCard(customerCard);
                        Result result = theCard.getResult();

                        result.setIsSuccess(true);

                        Set<Message> messages = result.getMessage();
                        Iterator<Message> messageIterator = messages.iterator();
                        if(messageIterator.hasNext()) {
                            Message message = messageIterator.next();

                            message.setText(ResourceBundleUtil.getValue("card.created"));
                        }

                        ResponseBuilder builder = getResponseBuilder(theCard, true, uri);
                        return builder.build();
                    } catch (RollbackException e) {
                        _logger.error("Error in peristing info to database ", e  );
                        String errMessage = ResourceBundleUtil.getValue("error.rollback");
                        ResponseBuilder builder = getDummyCardResponseBuilder(errMessage);//getResponseBuilder(errMessage, false, null);
                        return builder.build();
                    }

                } else {
                    dao.rollback();
                    dao.close();
                    String errMessage = ResourceBundleUtil.getValue("audit.not.added");
                    ResponseBuilder builder = getDummyCardResponseBuilder(errMessage);// getResponseBuilder(errMessage, false, null);
                    return builder.build();
                }
            } else {
                dao.rollback();
                dao.close();
                String errMessage = MessageFormat.format( ResourceBundleUtil.getValue("card.not.addded"), customerCard);
                ResponseBuilder builder = getDummyCardResponseBuilder(errMessage);//getResponseBuilder(errMessage, false, null);
                return builder.build();
            }

        }

    }

    public CustomerCardDAO getCustomerCardDao() {
        return customerCardDao;
    }

    public void setCustomerCardDao(CustomerCardDAO customerCardDao) {
        this.customerCardDao = customerCardDao;
    }


    /**
     * for filling up Result within CustomerCard
     * @param isOk -- true == no errors
     * @param messageText  -- for errors
     * @return
     */
    private Result getResult(boolean isOk, String messageText) {
        Result reslt = new Result();
        Set<Message> messageSet = new HashSet<Message>();

        if (isOk) {
            Message message1 = new Message();
            message1.setCode(null);
            message1.setText(messageText);
            message1.setType(Message.Type.INFO);

            messageSet.add(message1);
            reslt.setMessage(messageSet);
        } else {
            Message message1 = new Message();
            message1.setCode(null);
            message1.setText(messageText);
            message1.setType(Message.Type.ERROR);

            messageSet.add(message1);
            reslt.setMessage(messageSet);
        }

        return reslt;
    }


    /**
     * Returns a dummy empty card object in the response with
     * @param message as the message we wish to display for Result -- message are got from messages.properties files
     *                and this method will not be getting em
     * @return
     */
    private ResponseBuilder getDummyCardResponseBuilder(String message) {
        // send a dummy card object
        CustomerCard aDummyCard = new CustomerCard();
        Card aCard = aDummyCard.transformToCard(aDummyCard);


        Result result = getResult(false, message);
        result.setIsSuccess(false);

        aCard.setResult(result);

        ResponseBuilder builder = getResponseBuilder(aCard, false, null);
        return builder;
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

                    builder = builder.entity(entity);//(ResourceBundleUtil.getValue("card.created"));
                } catch (URISyntaxException e) {
                    _logger.error("Could not create URI for " + location, e);
                    builder = Response.notModified().entity(entity);
                }
            }

        } else {
            System.out.println("in NOT OK in getResponseBuilder");
            builder = Response.ok(null, MediaType.APPLICATION_JSON_TYPE).entity(entity);
//            builder = Response.ok(null, MediaType.APPLICATION_XML_TYPE).entity("<message>"+entity+"</message>");
        }

        CacheControl cacheControl = new CacheControl();
        cacheControl.setMaxAge(maxAge);

        builder.cacheControl(cacheControl);
        builder.expires(nextUpdate.getTime());
        builder.type(MediaType.APPLICATION_JSON);//(MediaType.APPLICATION_XML);
        builder.language(MediaType.APPLICATION_JSON);//(MediaType.APPLICATION_XML);
        builder.header("ABCDE", "1234567890");

        return builder;
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


}