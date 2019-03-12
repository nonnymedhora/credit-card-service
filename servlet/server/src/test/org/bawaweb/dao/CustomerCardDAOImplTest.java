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
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

import org.bawaweb.core.model.CustomerCard;
import org.bawaweb.server.crypt.CreditCardEncrypter;

import junit.framework.TestCase;

/**
 * @author medhoran
 * 
 */

public class CustomerCardDAOImplTest extends TestCase {

	private CustomerCardDAO customerCardDAOMock;
	private CustomerCardDAOImpl customerCardDAO = new CustomerCardDAOImpl();

//	/*
//	 * (non-Javadoc)
//	 * 
//	 * @see junit.framework.TestCase#setUp()
//	 */
//	@Before
//	public void setUp() throws Exception {
//		customerCardDAOMock = mock(CustomerCardDAOImpl.class);
//	}
//
//	/**
//	 * Test method for
//	 * {@link org.bawaweb.server.dao.CustomerCardDAOImpl#getCustomerCards(java.lang.String)}
//	 * .
//	 */
//	@Test
//	public void testGetCardNumberMock() throws Exception {
//
//		// fail("Not yet implemented");
//		final String customerXAID = "1234534567";
//
//		final CustomerCard customerCard = new CustomerCard();
//		customerCard.setCardNumber("4536278934756");
//		customerCard.setCustomerUUID(customerXAID);
//
//		when(customerCardDAOMock.getCardNumber(customerXAID)).thenReturn(customerCard);
//
//		assertEquals(customerXAID, String.valueOf(customerCard.getCustomerUUID()));
//	}
	
	/**
	 * Test method for
	 * {@link org.bawaweb.server.dao.CustomerCardDAOImpl#getCustomerCards(java.lang.String)}
	 * .
	 */
	@Test
	public void testGetCardNumber() throws Exception {
		// get a card using jdbc
		final int LIMIT = 1;
		String encryptedCardNumber = null;
		String cardNumber = null;
		String custId = null;
		String custUUID = null;
		Connection conn = null;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager
					.getConnection("jdbc:mysql://localhost:3306/test");
			String getCardNumberForCustomer = "select * from customer_card limit ?";
			PreparedStatement ps1 = conn
					.prepareStatement(getCardNumberForCustomer);
			ps1.setInt(1, LIMIT);
			ResultSet rs = ps1.executeQuery();

			while (rs.next()) {
				encryptedCardNumber = rs.getString("card_number");
				custUUID = rs.getString("customer_uuid");
				System.out.println("VIA JDBC --- Retrieved customer_uuid " + custUUID + " with encrypted number " + encryptedCardNumber);
				
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
		List<CustomerCard> daoCards = customerCardDAO.getCustomerCards(custUUID);
		for( CustomerCard daoCard : daoCards ) {
			String daoUid = daoCard.getCustomerUUID();
			String daoCardNumber = daoCard.getCardNumber();
			
			if ( daoUid.equals(custUUID) && daoCardNumber.equals(encryptedCardNumber) ) {
				found = true;
			}
		}
		
		assertEquals(true, found);
		
		
		
//		System.out.println("daoCard.getCustomerXAID() is >>" + daoCard.getCustomerUUID() + "<<");
//		System.out.println("String.valueOf(custId) is >>" + String.valueOf(custId) + "<<");
//		assertEquals(daoCard.getCustomerUUID(), custId);
//		assertEquals(daoCard.getCardNumber(), CreditCardEncrypter.decryptCardNumber(encryptedCardNumber));

	}
	


	/**
	 * Test method for
	 * {@link org.bawaweb.server.dao.CustomerCardDAOImpl#addCustomerCard(org.bawaweb.core.model.CustomerCard)}
	 * .
	 */
	@Test
	public void testAddCustomerCard() throws Exception {
		String encryptedCardNumber = null;

		final String customerId = "778899";
		final String number = "5111005111051128";// "5405222222222226";
		
		// create a card
		final CustomerCard CARD2 = new CustomerCard();
		CARD2.setCardNumber(number);//("5111005111051128");//("5405222222222226");//("5111005111051128");//("5405222222222226");//("5111005111051821");//("5112345112345114");//("5111005111051128");			// see http://www.auricsystems.com/support-center/sample-credit-card-numbers/ for examples
		CARD2.setCustomerId(customerId);		/// wont work --> 2231423736l === will work 43254536
		CARD2.setCreatedBy("TUUUMEEEEEE");
		CARD2.setCvv(779);
		CARD2.setExpirationMonth(77);
		CARD2.setExpirationYear(99);
		CARD2.setStatus("ACT");
		CARD2.setPrimary(true);
		CARD2.setModifiedBy("TUUUMEEEEEE");
		
		
		// insert the card thru the dao
		System.out.println("Adding card " + CARD2 + " via daoCard");
		CustomerCard daoCard = customerCardDAO.addCustomerCard(CARD2);
		customerCardDAO.commit();
		customerCardDAO.close();
		
		assertEquals(daoCard, CARD2);
		
		String customerUUId = null;
		Integer cvv = null;
		String status = null;
		Integer expirationMonth = null;
		Integer expirationYear = null;
		Boolean isPrimary = null;
		
		// retrieve the inserted object using jdbc
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = null;
			conn = DriverManager
					.getConnection("jdbc:mysql://localhost:3306/test");
			String getCardNumberForCustomer = "select * from customer_card where customer_uuid = (select customer_uuid from customer_uuid where customer_id = ?)";
			PreparedStatement ps1 = conn
					.prepareStatement(getCardNumberForCustomer);
			ps1.setString(1, customerId);
			ResultSet rs = ps1.executeQuery();

			if (rs.next()) {
				encryptedCardNumber = rs.getString("card_number");
				customerUUId = rs.getString("customer_uuid");
				cvv = rs.getInt("cvv"); 
				status = rs.getString("status");
				isPrimary = rs.getBoolean("is_primary");
				expirationMonth= rs.getInt("expiration_month");
				expirationYear = rs.getInt("expiration_year");
			}
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("encrypted card is " + encryptedCardNumber);
		
		String decryptedCardNumber = CreditCardEncrypter.decryptCardNumber(encryptedCardNumber);
		
		assertEquals(number, decryptedCardNumber);
		assertEquals(cvv, CARD2.getCvv());
		assertEquals(status, CARD2.getStatus());
		assertEquals(expirationMonth, CARD2.getExpirationMonth());
		assertEquals(expirationYear, CARD2.getExpirationYear());
		assertEquals(isPrimary.booleanValue(), CARD2.isPrimary());

		// delete the card from the database
		deleteCard(CARD2);
		
		
		
	
	}

	/**
	 * Test method for
	 * {@link org.bawaweb.server.dao.CustomerCardDAOImpl#addCustomerCard(org.bawaweb.core.model.CustomerCard)}
	 * .
	 */
	@Test
	public void testAddCustomerCardMock() throws Exception {
		final String customerXAID = "1234534567";

		final CustomerCard customerCard = new CustomerCard();
		customerCard.setCardNumber("4536278934756");
		customerCard.setCustomerUUID(customerXAID);
		
		when(customerCardDAOMock.addCustomerCard(customerCard)).thenReturn(customerCard);

		assertEquals(customerXAID, String.valueOf(customerCard.getCustomerUUID()));

	}
	
	
	
	private void deleteCard(CustomerCard card) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = null;
			conn = DriverManager
					.getConnection("jdbc:mysql://localhost:3306/test");
			String getCardNumberForCustomer = "delete from customer_card where card_id = ?";
			PreparedStatement ps1 = conn
					.prepareStatement(getCardNumberForCustomer);
			ps1.setInt(1, card.getCardId());
			int result = ps1.executeUpdate();
			assertEquals(1, result);

			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}



}
