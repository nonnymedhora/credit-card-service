///**
// * 
// */
//package org.bawaweb.server.services;
//
//import static org.junit.Assert.*;
//import static org.mockito.Mockito.*;
//
//import java.lang.reflect.Method;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//
//import javax.ws.rs.core.Response;
//
//import org.junit.Before;
//import org.junit.Test;
//
//import org.bawaweb.server.crypt.CreditCardEncrypter;
//import org.bawaweb.server.dao.CustomerCardDAO;
//import org.bawaweb.server.dao.CustomerCardDAOImpl;
//import org.bawaweb.core.model.CustomerCard;
//
///**
// * @author medhoran
// * 
// */
//public class CustomerCardServiceImplTest {
//
////	@Mock
//	private CustomerCardServiceImpl customerCardService = new CustomerCardServiceImpl();
//
//	private CustomerCardDAO customerCardDao;
//	
//	private CustomerCardDAO ccDao = new CustomerCardDAOImpl();
//
//	/**
//	 * @throws java.lang.Exception
//	 */
//	@Before
//	public void setUp() throws Exception {
//		if (customerCardDao == null) {
//			customerCardDao = mock(CustomerCardDAOImpl.class);
//			customerCardService.setCustomerCardDao(customerCardDao);
//		}
//	}
//	
//
//	/**
//	 * Test method for
//	 * {@link org.bawaweb.server.services.CustomerCardServiceImpl#getCustomerCards(java.lang.String)}
//	 * .
//	 */
//	@Test
//	public void testGetCardNumber() {
//		/*String customerXAID = "22222222";
//		String cNumber = "1234324243224532";
//		CustomerCard customerCard = new CustomerCard();
//		customerCard.setCardNumber(cNumber);
//		customerCard.setCustomerUUID(customerXAID);
//		
//		when (customerCardDao.getCardNumber(customerXAID)).thenReturn(customerCard);
//		assertEquals(customerXAID, String.valueOf(customerCard.getCustomerUUID()));*/
//		
//		
//		
//		/*
//		
//		
//		System.out.println(customerCardDao == null);
//		Method[] m = customerCardDao.getClass().getDeclaredMethods();
//		for(Method mm : m) {
//			System.out.println("Methods name " + mm.getName());
//		}
//
//		CustomerCard cc = customerCardDao.getCardNumber(customerXAID);
//
//		assertEquals(customerXAID, String.valueOf(cc.getCustomerXAID()));*/
//	}
//
//	/**
//	 * Test method for
//	 * {@link org.bawaweb.server.services.CustomerCardServiceImpl#addCustomerCard(org.bawaweb.core.model.CustomerCard)}
//	 * makes a jdbc call and tests against that
//	 * .
//	 */
//	@Test
//	public void testAddCustomerCardCCD() {
//		String encryptedCardNumber = null;
////		long custId = 0l;
//		
//		// create a card
//		final CustomerCard card = new CustomerCard();
//		String ID = "99954452";
//		String number = "371449635398431";
//		
//		card.setCardNumber(number);
//		card.setCustomerUUID(ID);
//		
//		// insert the card thru the service
//		customerCardService.setCustomerCardDao(ccDao);
//		Response resp = customerCardService.addCustomerCard(card);
//		System.out.println("class name is " + resp.getEntity().getClass().getName());
//		System.out.println("entity is " + resp.getEntity());
//		
//		CustomerCard theRespCard = (CustomerCard) resp.getEntity();
//		assertEquals(theRespCard, card);
//		
//		// retrieve the inserted object using jdbc
//		try {
//			Class.forName("com.mysql.jdbc.Driver");
//			Connection conn = null;
//			conn = DriverManager
//					.getConnection("jdbc:mysql://localhost:3306/test");
//			String getCardNumberForCustomer = "select * from customer_card where customer_uuid = ?";
//			PreparedStatement ps1 = conn
//					.prepareStatement(getCardNumberForCustomer);
//			ps1.setString(1, ID);
//			ResultSet rs = ps1.executeQuery();
//
//			if (rs.next()) {
//				encryptedCardNumber = rs.getString("card_number");//System.out.println("enc is " + encryptedCardNumber);
//				custId = rs.getLong("customer_id");
//				
//			}
//			conn.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		String decryptedCardNumber = CreditCardEncrypter.decryptCardNumber(encryptedCardNumber);
//		
//		assertEquals(number, decryptedCardNumber);
//		assertEquals(custId, ID);
//
//		// delete the card from the database
//		deleteCard(ID);
//		
//		
//		
//	}
//	
//	
//	
//
//	private void deleteCard(String ID) {
//		try {
//			Class.forName("com.mysql.jdbc.Driver");
//			Connection conn = null;
//			conn = DriverManager
//					.getConnection("jdbc:mysql://localhost:3306/test");
//			String getCardNumberForCustomer = "delete from customer_card where customer_id = ?";
//			PreparedStatement ps1 = conn
//					.prepareStatement(getCardNumberForCustomer);
//			ps1.setString(1, ID);
//			int result = ps1.executeUpdate();
//			assertEquals(1, result);
//
//			conn.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//	}
//
//	/**
//	 * Test method for
//	 * {@link org.bawaweb.server.services.CustomerCardServiceImpl#getCustomerCards(java.lang.String)}
//	 * makes a jdbc call and tests against that
//	 * .
//	 */
//	@Test
//	public void testGetCardNumberCCD() {
//		// get a card using jdbc
//		final int LIMIT = 1;
//		String encryptedCardNumber = null;
//		String cardNumber = null;
////		long custId = 0l;
//		String custUuid = null;
//		Connection conn = null;
//		
//		try {
//			Class.forName("com.mysql.jdbc.Driver");
//			conn = DriverManager
//					.getConnection("jdbc:mysql://localhost:3306/test");
//			String getCardNumberForCustomer = "select * from customer_card limit ?";
//			PreparedStatement ps1 = conn
//					.prepareStatement(getCardNumberForCustomer);
//			ps1.setInt(1, LIMIT);
//			ResultSet rs = ps1.executeQuery();
//
//			while (rs.next()) {
//				encryptedCardNumber = rs.getString("card_number");
////				custId = rs.getLong("customer_id");
//				custUuid = rs.getString("customer_uuid");
//				
//			}
//			conn.close();
//		} catch (Exception e) {
//			try {
//				conn.close();
//			} catch (SQLException e1) {
//				e1.printStackTrace();
//			}
//			e.printStackTrace();
//		}
//		
//		// decrypt the encrypted card# from database
//		cardNumber = CreditCardEncrypter.decryptCardNumber(encryptedCardNumber);
//
//		if ( custUuid != null ) { //(custId != 0l) {
//		/*	// check its the same thru the dao
//			CustomerCard custCard = ccDao.getCardNumber(String.valueOf(custId));
//			System.out.println("cardNumber is " + cardNumber);
//			System.out.println("custCard.getCardNumber is " + custCard.getCardNumber());
//			assertEquals(cardNumber, custCard.getCardNumber());
//		*/	
//			// thru the service
//			customerCardService.setCustomerCardDao(ccDao);
//			Response resp = customerCardService.getCustomerCards(String.valueOf(custId));
//			assertEquals(Response.Status.OK.getStatusCode(),resp.getStatus());
//			
//			/*System.out.println(resp.getEntity());//CustomerCard [customerXAID=2392341, cardNumber=4432432231486]
//			System.out.println(resp.getEntityTag());//null
//			System.out.println(resp.getEntity().getClass());*/
//			
//			CustomerCard customerCard = (CustomerCard) resp.getEntity();
//			assertEquals(cardNumber, customerCard.getCardNumber());
//			assertEquals(custId, customerCard.getCustomerUUID());
//			
//			
//		}
//		
//	}
//
//}
//
///*http://www.auricsystems.com/support-center/sample-credit-card-numbers/
//// other valid numbers  amex
//343434343434343 
//371144371144376 
//341134113411347
//
//// mc
// * 5500005555555559 
//5555555555555557 
//5454545454545454 
//5555515555555551 
//5405222222222226 
//5478050000000007 
//5111005111051128 
//5112345112345114 
//
// */
// 
