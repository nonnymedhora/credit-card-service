/**
*
*/
package org.bawaweb.server.services;

import static org.junit.Assert.*;

import java.sql.*;

import javax.ws.rs.core.Response;

import org.bawaweb.core.model.Card;
import org.bawaweb.server.EntityManagerUtil;
import org.junit.Before;
import org.junit.Test;

import org.bawaweb.core.model.CustomerCard;

/**
* @author medhoran
*
*/
public class CustomerCardServiceImplTest {

	private CustomerCardServiceImpl     customerCardService;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
        // TODO: write utility class to find proper properties file?
        final String propFilePath = System.getProperty("user.home") + System.getProperty("file.separator") + "app.properties";

        // note that we are using the same properties file
        // but is on another location in the server's file system
        EntityManagerUtil.initialize(propFilePath);

        customerCardService = new CustomerCardServiceImpl();
	}


    @Test
    public void testGetCustomerCard () {
        // get one from jdbc
        // get a card using jdbc
        final int LIMIT = 1;
        String cardId = null;
        Connection conn = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager
                    .getConnection("jdbc:mysql://sac_nonprod_db:3306/bw_services_secure", "bw_ws", "bwwsftw");
            String getCardNumberForCustomer = "select card_id from customer_card limit ?";
            PreparedStatement ps1 = conn
                    .prepareStatement(getCardNumberForCustomer);
            ps1.setInt(1, LIMIT);
            ResultSet rs = ps1.executeQuery();

            while (rs.next()) {
                cardId = rs.getString("card_id");
                System.out.println("VIA JDBC --- Retrieved card with id " + cardId);

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


        if ( cardId != null ) {
            Response response = customerCardService.getCustomerCard(cardId);
            assertNotNull(response);
        }

    }

    @Test
    public void testAddCard() {
        // get existing card last one
        // get one from jdbc
        // get a card using jdbc
//        final int LIMIT = 1;
        String cardId = null;
        Timestamp existingMaxCreationDate = null;
        Timestamp maxCreationDate = null;

        Connection conn = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager
                    .getConnection("jdbc:mysql://sac_nonprod_db:3306/bw_services_secure", "bw_ws", "bwwsftw");
            String getCardNumberForCustomer = "select max(creation_date) from customer_card";
            PreparedStatement ps1 = conn
                    .prepareStatement(getCardNumberForCustomer);
//            ps1.setInt(1, LIMIT);
            ResultSet rs = ps1.executeQuery();

            while (rs.next()) {
//                cardId = rs.getString("card_id");
                    existingMaxCreationDate = rs.getTimestamp("max(creation_date)");
//                System.out.println("VIA JDBC --- Retrieved card with id " + cardId);

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


        CustomerCard aCard = new CustomerCard();
        aCard.setValue("5111005111051128");//("3411-341-1341-1347");//("5405-2222-2222-2226");//("5111005111051128");;
        aCard.setCreatedBy("TEST");
        aCard.setModifiedBy("TEST");
        Card theCard = aCard.transformToCard(aCard);

        Response response = customerCardService.addCustomerCard(theCard);

        assertNotNull(response);


        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager
                    .getConnection("jdbc:mysql://sac_nonprod_db:3306/bw_services_secure", "bw_ws", "bwwsftw");
            String getCardNumberForCustomer = "select max(creation_date) from customer_card";
            PreparedStatement ps1 = conn
                    .prepareStatement(getCardNumberForCustomer);
            ResultSet rs = ps1.executeQuery();

            while (rs.next()) {
                maxCreationDate = rs.getTimestamp("max(creation_date)");
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

        assert( maxCreationDate.getTime() >= existingMaxCreationDate.getTime()) ;

        // now delete the row added

    }
}
