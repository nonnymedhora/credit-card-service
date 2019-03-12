package org.bawaweb.server.loader;

import org.bawaweb.core.model.CustomerCard;
import org.bawaweb.server.EntityManagerUtil;
import org.bawaweb.server.crypt.CreditCardEncrypter;
import org.bawaweb.server.dao.CustomerCardDAO;
import org.bawaweb.server.dao.CustomerCardDAOImpl;
import org.bawaweb.server.dao.HibernateDAO;

import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by medhoran on 12/23/13.
 * // will load? or gather the information which will be pushed into bw_services_secure
 * // note that the decrypted card numbers come from UAT2 schema and are decrypted by the DesEncrypter class.
 * // These are now re-encrypted using the CreditCardEncrypter class (which used BouncyCastle)
 * // a HashMap mapping the customerId to the CustomerCardTriplet will be used to create a cvs file
 * // to send off to the CenterStance people
 */

public class LCServicesSecureLoader {

    public static void main(String[] args) {

        // note that we are using the same properties file
        // but is on another location in the server's file system
        // TODO: write utility class to find proper properties file?
        final String propFilePath = System.getProperty("user.home") + System.getProperty("file.separator") + "app.properties";
        EntityManagerUtil.initialize(propFilePath);

        UATDataExtractor uatDataExtractor = new UATDataExtractor();
        HibernateDAO dao = new HibernateDAO();
        CustomerCardDAO cardDAO = new CustomerCardDAOImpl();

        HashMap<String, CustomerCardTriplet> existingUATCustInfo = uatDataExtractor.getCustomerCardUATData();
        uatDataExtractor.decryptUAT2CardNumbers(existingUATCustInfo);   // now existingUATCustInfo contains decrypted card number


        dao.beginAndClear();

        HashMap<String, CustomerCardTriplet> newCustInfo = new HashMap<String, CustomerCardTriplet>();
        Set<String> customers = existingUATCustInfo.keySet();
        for( String customer: customers ) {
            CustomerCard aCard = new CustomerCard();
            String cardNumbr = existingUATCustInfo.get(customer).getCardNumber();
            try {
                String encryptedNumber = CreditCardEncrypter.encryptCardNumber(cardNumbr);
                aCard.setValue(encryptedNumber);
            } catch (GeneralSecurityException e) {
                e.printStackTrace();
            }
            aCard.setCreatedBy("BAWA");
            aCard.setModifiedBy("Bawa");

            CustomerCard newCard = cardDAO.addCustomerCard(aCard);

            newCustInfo.put(customer, new CustomerCardTriplet(customer, newCard.getCardId(), cardNumbr));
        }

        dao.commit();
        Set<String> newCustomers = newCustInfo.keySet();            // note Set<String> newCustomers must === Set<String> customers
        for( String aNewCustomer: newCustomers ) {
            CustomerCardTriplet newTriplet = newCustInfo.get(aNewCustomer);

            System.out.println( newTriplet.getCustomerId() + ", " + newTriplet.getCreditCardId() );
        }

    }
}
