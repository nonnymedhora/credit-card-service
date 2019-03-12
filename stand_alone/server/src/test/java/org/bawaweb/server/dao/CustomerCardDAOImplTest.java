/**
 * 
 */
package org.bawaweb.server.dao;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bawaweb.server.EntityManagerUtil;
import org.junit.Before;
import org.junit.Test;

//import static org.mockito.Mockito.*;

import org.bawaweb.core.model.CustomerCard;
import org.bawaweb.server.crypt.CreditCardEncrypter;


/**
 * @author medhoran
 * 
 */

public class CustomerCardDAOImplTest {

//	private CustomerCardDAO customerCardDAOMock;
	private CustomerCardDAOImpl customerCardDAO;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        // note that we are using the same properties file
        // but is on another location in the server's file system
        // TODO: write utility class to find proper properties file?
        final String propFilePath = System.getProperty("user.home") + System.getProperty("file.separator") + "app.properties";
        EntityManagerUtil.initialize(propFilePath);
        customerCardDAO = new CustomerCardDAOImpl();
    }

	/**
	 * Test method for
	 * {@link org.bawaweb.server.dao.CustomerCardDAOImpl#getCustomerCard(java.lang.String)}
	 * .
	 */
	@Test
	public void testGetCardNumber() throws Exception {
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
				encryptedCardNumber = rs.getString("card_number");
				System.out.println("VIA JDBC --- Retrieved card with encrypted number " + encryptedCardNumber);
				
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
		
		boolean found = false;
		
		// use the dao to that same info
		CustomerCard daoCard = customerCardDAO.getCustomerCard(cardId);
		String daoCardNumber = daoCard.getValue();

        if ( daoCardNumber.equals(encryptedCardNumber) ) {
            found = true;
        }

		assertEquals(true, found);

	}
	


	/**
	 * Test method for
	 * {@link org.bawaweb.server.dao.CustomerCardDAOImpl#addCustomerCard(org.bawaweb.core.model.CustomerCard)}
	 * .
	 */
	@Test
	public void testAddCustomerCard() throws Exception {
		String encryptedCardNumber = null;

		final String number = "5111005111051128";// "5405222222222226";
		
		// create a card
		final CustomerCard CARD2 = new CustomerCard();
		CARD2.setValue(number);// see http://www.auricsystems.com/support-center/sample-credit-card-numbers/ for examples
		CARD2.setCreatedBy("TUUUMEEEEEE");
		CARD2.setModifiedBy("TUUUMEEEEEE");

        String encCardNum = CreditCardEncrypter.encryptCardNumber(number);
        CARD2.setValue(encCardNum);
		
		customerCardDAO.beginAndClear();
		// insert the card thru the dao
		System.out.println("Adding card " + CARD2 + " via daoCard");
		CustomerCard daoCard = customerCardDAO.addCustomerCard(CARD2);
		customerCardDAO.commit();
		customerCardDAO.close();
		
		assertEquals(daoCard, CARD2);

        System.out.println("card_id is " + daoCard.getCardId());

        // retrieve the inserted object using jdbc
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = null;
			conn = DriverManager
					.getConnection("jdbc:mysql://sac_nonprod_db:3306/bw_services_secure", "bw_ws", "bwwsftw");

			String getCardNumberForCustomer = "select * from customer_card where card_id = ?";
			PreparedStatement ps1 = conn
					.prepareStatement(getCardNumberForCustomer);
			ps1.setString(1, daoCard.getCardId());
			ResultSet rs = ps1.executeQuery();

			if (rs.next()) {
				encryptedCardNumber = rs.getString("card_number");
			}
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("encrypted card is " + encryptedCardNumber);

		String decryptedCardNumber = CreditCardEncrypter.decryptCardNumber(encryptedCardNumber);

		assertEquals(number, decryptedCardNumber);

		// delete the card from the database
		deleteCard(CARD2);

	}


	
	private void deleteCard(CustomerCard card) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = null;
			conn = DriverManager
					.getConnection("jdbc:mysql://sac_nonprod_db:3306/bw_services_secure", "bw_ws", "bwwsftw");
			String getCardNumberForCustomer = "delete from customer_card where card_id = ?";
			PreparedStatement ps1 = conn
					.prepareStatement(getCardNumberForCustomer);
			ps1.setString(1, card.getCardId());
			int result = ps1.executeUpdate();
			assertEquals(1, result);

			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
