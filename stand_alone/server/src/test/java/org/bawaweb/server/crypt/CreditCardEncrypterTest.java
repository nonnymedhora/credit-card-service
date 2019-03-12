/**
 * 
 */
package org.bawaweb.server.crypt;

import static org.junit.Assert.*;

import org.bawaweb.server.crypt.CreditCardEncrypter;
import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * @author medhoran
 *
 */
public class CreditCardEncrypterTest extends TestCase {
	
	private ArrayList<String> numbers = new ArrayList<String>();
	private ArrayList<String> encryptedNumbers = new ArrayList<String>();
	
	private ArrayList<String> decryptedNumbers = new ArrayList<String>();

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		numbers.add("7777777777777777");
		numbers.add("6666666666666666");
		numbers.add("5555555555555555");
		numbers.add("4444444444444444");
	}

	/**
	 * Test method for {@link org.bawaweb.server.crypt.CreditCardEncrypter#encryptCardNumber(java.lang.String)}.
	 */
	@Test
	public void testEncryptDecryptCardNumber() throws Exception {
		
		for( String b : numbers ) {
			String encryptedNumber = CreditCardEncrypter.encryptCardNumber(b);
			encryptedNumbers.add(encryptedNumber);
		}
		
		for ( String c : encryptedNumbers ) {
			String decryptedNumber = CreditCardEncrypter.decryptCardNumber(c);
			decryptedNumbers.add(decryptedNumber);
		}
		
		assertEquals(encryptedNumbers.size(), numbers.size());
		assertEquals(decryptedNumbers.size(), numbers.size());
		
		for ( int i = 0; i < numbers.size(); i++ ) {
			assertEquals(decryptedNumbers.get(i), numbers.get(i));
		}
	}
	
	
	/**
	 * gets ten encrypted card numbers from the database and decrypts them
	 * we can set the number - limit ---test checks that the values
	 * can indeed be decrypted and dont throw an exception, plus does size of list check
	 * @throws Exception
	 */
	@Test
	public void testTop10FromDatabaseConnection() throws Exception {
		decryptedNumbers = new ArrayList<String>();
		ArrayList<String> list = new ArrayList<String>();
		final int LIMIT = 10;

		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = null;
			conn = DriverManager
					.getConnection("jdbc:mysql://sac_nonprod_db:3306/bw_services_secure", "bw_ws", "bwwsftw");
			String getCardNumberForCustomer = "select card_number from customer_card limit ?";
			PreparedStatement ps1 = conn
					.prepareStatement(getCardNumberForCustomer);
			ps1.setInt(1, LIMIT);
			ResultSet rs = ps1.executeQuery();

			while (rs.next()) {
				String cardNumber = rs.getString("card_number");
				list.add(cardNumber);
			}
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		for ( String c : list ) {
			String decryptedNumber = CreditCardEncrypter.decryptCardNumber(c);
			decryptedNumbers.add(decryptedNumber);
		}

        // there may be less than 10 cards in database - so rewriting this for now
        assert(decryptedNumbers.size() > 0);
        assert(LIMIT >= decryptedNumbers.size());

		for(String decryp : decryptedNumbers ) {
			assertNotNull(decryp);
			System.out.println("DB decrypt is " + decryp);
		}
	}
	
}
