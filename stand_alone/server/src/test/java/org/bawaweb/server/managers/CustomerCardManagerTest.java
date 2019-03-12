package org.bawaweb.server.managers;

import org.bawaweb.core.model.CustomerCard;
import org.bawaweb.server.EntityManagerUtil;
import org.bawaweb.server.crypt.CreditCardEncrypter;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.Response;
import java.security.GeneralSecurityException;
import java.sql.*;

import static org.junit.Assert.assertEquals;

/**
 * Created by medhoran on 12/10/13.
 */
public class CustomerCardManagerTest {

    private CustomerCardManager customerCardManager;

    @Before
    public void setUp() throws Exception {
        // note that we are using the same properties file
        // but is on another location in the server's file system
        // TODO: write utility class to find proper properties file?
        final String propFilePath = System.getProperty("user.home") + System.getProperty("file.separator") + "app.properties";
        EntityManagerUtil.initialize(propFilePath);
        customerCardManager = new CustomerCardManager();
    }

    @Test
    public void testGetCustomerCard() throws Exception {
        // get a card id from the database
//        String cardId = getSampleCardId();
        CustomerCard aCard = getSampleDBCard();
        String cardId = aCard.getCardId();
        System.out.println("Retrieved card with ID " + cardId);

        Response resp = customerCardManager.getCustomerCard(cardId);
        Integer auditId = getDBMaxAuditId();
        System.out.println("Found max audit id " + auditId);

        deleteFromCardAuditLog(auditId, cardId);


    }

    private void deleteFromCardAuditLog(Integer auditId, String cardId) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = null;
            conn = DriverManager
                    .getConnection("jdbc:mysql://sac_nonprod_db:3306/bw_services_secure", "bw_ws", "bwwsftw");
            String getCardNumberForCustomer = "delete from card_audit_log where audit_id = ? and card_id = ?";
            PreparedStatement ps1 = conn
                    .prepareStatement(getCardNumberForCustomer);
            ps1.setInt(1, auditId);
            ps1.setString(2, cardId);
            int result = ps1.executeUpdate();
            assertEquals(1, result);

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Integer getDBMaxAuditId() {
        Integer auditId = null;
        Connection conn = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager
                    .getConnection("jdbc:mysql://sac_nonprod_db:3306/bw_services_secure", "bw_ws", "bwwsftw");
            String maxAuditId = "select max(audit_id) from card_audit_log";
            PreparedStatement ps1 = conn
                    .prepareStatement(maxAuditId);
            ResultSet rs = ps1.executeQuery();

            while (rs.next()) {
                auditId = rs.getInt("max(audit_id)");
            }

            conn.close();
        } catch (Exception e) {
            try {
                conn.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }

        return auditId;
    }

    @Test
    public void testAddCustomerCard() {
        // create a dummy card
        CustomerCard aCard = new CustomerCard();
        aCard.setCreatedBy("AAAAMMMMEEEEE");
        aCard.setDateCreated(new Timestamp(new java.util.Date().getTime()));
        aCard.setModifiedBy("TUMEEEEEEEE");
        aCard.setDateModified(new Timestamp(new java.util.Date().getTime()));
        final String cardNumber = "5112345112345114";
        aCard.setValue(cardNumber);    // a valid card number


        // add it using manager
        Response resp = customerCardManager.addCustomerCard(aCard);
        System.out.println("response location is " + resp.getLocation());
        String location = resp.getLocation().toString();

        String cardId = location.substring(location.lastIndexOf('/')+1);
        System.out.println("cardId is " + cardId);

        // retreive it from jdbc
        CustomerCard dbcard = getCardInfoFromDB(cardId);
        String encryptedCardNumber = dbcard.getValue();
        String decryptedCardNumber = null;
        try {
            decryptedCardNumber = CreditCardEncrypter.decryptCardNumber(encryptedCardNumber);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }

        // asserts - comp aCard with dbcard
        assertEquals(aCard.getCardId(), dbcard.getCardId());
        assertEquals(cardNumber, decryptedCardNumber);
        assertEquals(aCard.getCreatedBy(), dbcard.getCreatedBy());
//        assertEquals(String.valueOf(aCard.getDateCreated().getTime()).substring(0,10), String.valueOf(dbcard.getDateCreated().getTime()).substring(0,10));
        assertEquals(aCard.getModifiedBy(), dbcard.getModifiedBy());
//        assertEquals(String.valueOf(aCard.getDateModified().getTime()).substring(0,10), String.valueOf(dbcard.getDateModified().getTime()).substring(0,10));

        // delete the card audit log from table using jdbc
        Integer auditId = getDBMaxAuditId();
        System.out.println("Found max audit id " + auditId);

        deleteFromCardAuditLog(auditId, cardId);

        // delete the card from table using jdbc
        deleteCardInfo(cardId);

    }

    private void deleteCardInfo(String cardId) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = null;
            conn = DriverManager
                    .getConnection("jdbc:mysql://sac_nonprod_db:3306/bw_services_secure", "bw_ws", "bwwsftw");
            String getCardNumberForCustomer = "delete from customer_card where card_id = ?";
            PreparedStatement ps1 = conn
                    .prepareStatement(getCardNumberForCustomer);

            ps1.setString(1, cardId);
            int result = ps1.executeUpdate();
            assertEquals(1, result);

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private CustomerCard getCardInfoFromDB(String cardId) {
        CustomerCard dbCard = new CustomerCard();
        Connection conn = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager
                    .getConnection("jdbc:mysql://sac_nonprod_db:3306/bw_services_secure", "bw_ws", "bwwsftw");
            String getCardNumberForCustomer = "select * from customer_card where card_id = ?";
            PreparedStatement ps1 = conn
                    .prepareStatement(getCardNumberForCustomer);
            ps1.setString(1, cardId);
            ResultSet rs = ps1.executeQuery();
            System.out.println("Is rs null " + (rs==null));

            if (rs.next()) {
                dbCard.setCardId(rs.getString("card_id"));
                dbCard.setModifiedBy(rs.getString("modified_by"));
                dbCard.setDateModified(rs.getTimestamp("modification_date"));
                dbCard.setCreatedBy(rs.getString("created_by"));
                dbCard.setDateCreated(rs.getTimestamp("creation_date"));

                dbCard.setValue(rs.getString("card_number"));      // note this is the encrypted value
            }
            conn.close();
        } catch (Exception e) {
            try {
                conn.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }

        return dbCard;
    }

    private CustomerCard getSampleDBCard() {
        CustomerCard aCard = new CustomerCard();

        final int LIMIT = 1;
        String cardId = null;
        Connection conn = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager
                    .getConnection("jdbc:mysql://sac_nonprod_db:3306/bw_services_secure", "bw_ws", "bwwsftw");
            String getCardNumberForCustomer = "select * from customer_card limit ?";
            PreparedStatement ps1 = conn
                    .prepareStatement(getCardNumberForCustomer);
            ps1.setInt(1, LIMIT);
            ResultSet rs = ps1.executeQuery();
            System.out.println("Is rs null " + (rs==null));

            if (rs.next()) {
                aCard.setCardId(rs.getString("card_id"));
                aCard.setModifiedBy(rs.getString("modified_by"));
                aCard.setDateModified(rs.getTimestamp("modification_date"));
                aCard.setCreatedBy(rs.getString("created_by"));
                aCard.setDateCreated(rs.getTimestamp("creation_date"));

            }


            conn.close();
        } catch (Exception e) {
            try {
                conn.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }


        return aCard;
    }


    private String getSampleCardId() {
        // get a card using jdbc
        final int LIMIT = 1;
        String encryptedCardNumber = null;
        String cardId = null;
        Connection conn = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager
                    .getConnection("jdbc:mysql://sac_nonprod_db:3306/bw_services_secure", "bw_ws", "bwwsftw");
            String getCardNumberForCustomer = "select * from customer_card limit ?";
            PreparedStatement ps1 = conn
                    .prepareStatement(getCardNumberForCustomer);
            ps1.setInt(1, LIMIT);
            ResultSet rs = ps1.executeQuery();

            while (rs.next()) {
                cardId = rs.getString("card_id");
            }

            conn.close();
        } catch (Exception e) {
            try {
                conn.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }

        return cardId;

    }

}
