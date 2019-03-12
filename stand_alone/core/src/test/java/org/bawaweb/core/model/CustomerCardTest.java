/**
 * 
 */
package org.bawaweb.core.model;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * @author medhoran
 *
 */

public class CustomerCardTest {
	
	private CustomerCard card;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		card = new CustomerCard();
		String number = "2dcf8eab-fe9a-4d84-9e9f-299f89141caf";
		
		card.setValue(number);
		card.setCreatedBy("TUUUMEEEEEE");
		card.setModifiedBy("TUMEEEE");
		
	}

	/**
	 * Test method for {@link org.bawaweb.core.model.CustomerCard#getValue()}.
	 */
	@Test
	public void testGetCardNumber() {
		CustomerCard card = new CustomerCard();
		String number = "2dcf8eab-fe9a-4d84-9e9f-299f89141caf";
		
		card.setValue(number);

		assertEquals(number, card.getValue());
	}

	/**
	 * Test method for {@link org.bawaweb.core.model.CustomerCard#setValue(java.lang.String)}.
	 */
	@Test
	public void testSetCardNumber() {
		CustomerCard card = new CustomerCard();
		String number = "2dcf8eab-fe9a-4d84-9e9f-299f89141caf";
		
		card.setValue(number);

		assertEquals(number, card.getValue());
		
		number = "54632789-7aa1-484a-93b7-bf60bbed566f";
		card.setValue(number);
		
		assertEquals(number, card.getValue());
		
	}


}
