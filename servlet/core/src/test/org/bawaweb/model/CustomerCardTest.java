/**
 * 
 */
package org.bawaweb.core.model;

import static org.junit.Assert.*;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

/**
 * @author medhoran
 *
 */
public class CustomerCardTest extends TestCase {
	
	private CustomerCard card;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		card = new CustomerCard();
		String custId = "778899";
		String uuId = "43562785678";
		String number = "453627890";
		
		card.setCustomerId(custId);
		card.setCardNumber(number);
		card.setCustomerUUID(uuId);
		card.setCreatedBy("TUUUMEEEEEE");
		card.setExpirationYear(33);
		card.setExpirationMonth(11);
		card.setStatus("ACT");
		card.setPrimary(true);
		card.setModifiedBy("TUMEEEE");
		
	}

	/**
	 * Test method for {@link org.bawaweb.core.model.CustomerCard#getCustomerUUID()}.
	 */
	@Test
	public void testGetCustomerXAID() {
		CustomerCard card = new CustomerCard();
		String ID = "43562785678";
		String number = "453627890";
		
		card.setCardNumber(number);
		card.setCustomerUUID(ID);
		
		assertEquals(ID, card.getCustomerUUID());
	}

	/**
	 * Test method for {@link org.bawaweb.core.model.CustomerCard#setCustomerUUID(long)}.
	 */
	@Test
	public void testSetCustomerXAID() {
		CustomerCard card = new CustomerCard();
		String ID = "43562785678";
		String number = "453627890";
		
		card.setCardNumber(number);
		card.setCustomerUUID(ID);
		
		assertEquals(ID, card.getCustomerUUID());
		
		String ID2 = "23434432";
		card.setCustomerUUID(ID2);
		
		assertEquals(ID2, card.getCustomerUUID());
		
	}

	/**
	 * Test method for {@link org.bawaweb.core.model.CustomerCard#getCardNumber()}.
	 */
	@Test
	public void testGetCardNumber() {
		CustomerCard card = new CustomerCard();
		String ID = "43562785678";
		String number = "453627890";
		
		card.setCardNumber(number);
		card.setCustomerUUID(ID);
		
		assertEquals(number, card.getCardNumber());
	}

	/**
	 * Test method for {@link org.bawaweb.core.model.CustomerCard#setCardNumber(java.lang.String)}.
	 */
	@Test
	public void testSetCardNumber() {
		CustomerCard card = new CustomerCard();
		String ID = "43562785678";
		String number = "453627890";
		
		card.setCardNumber(number);
		card.setCustomerUUID(ID);
		
		assertEquals(number, card.getCardNumber());
		
		number = "5463785647";
		card.setCardNumber(number);
		
		assertEquals(number, card.getCardNumber());
		
	}


}
