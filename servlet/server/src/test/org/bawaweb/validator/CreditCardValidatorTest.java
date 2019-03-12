/**
 * 
 */
package org.bawaweb.server.validator;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import org.bawaweb.core.model.CustomerCard;

/**
 * @author medhoran
 *
 */
public class CreditCardValidatorTest extends TestCase {
	
	private CreditCardValidator validator = new CreditCardValidator();

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * Test method for {@link org.bawaweb.server.validator.CreditCardValidator#validateCardNumber(org.bawaweb.core.model.CustomerCard)}.
	 */
	@Test
	// see http://www.auricsystems.com/support-center/sample-credit-card-numbers/
	// http://www.crazysquirrel.com/finance/test-cc.jspx
	public void testValidateCardNumberValidNum() {
		
		List<String> validCardNumbers = getValidCardNumbers();
		for ( String number : validCardNumbers ) {
			CustomerCard customerCard = new CustomerCard();
			customerCard.setCardNumber(number);
			boolean validated = validator.validateCardNumber(customerCard);
			
			assertEquals(true, validated);
		}
		
		/*final String number = "5405222222222226";//5111005111051128
		CustomerCard customerCard = new CustomerCard();
		customerCard.setCardNumber(number);
		boolean validated = validator.validateCardNumber(customerCard);
		
		assertEquals(true, validated);*/
	}

	private List<String> getValidCardNumbers() {
		List<String> validCardNumbers = new ArrayList<String>();
		// American Express 
		 
		validCardNumbers.add("371449635398431");
		validCardNumbers.add("343434343434343"); 
		validCardNumbers.add("371144371144376"); 
		validCardNumbers.add("341134113411347"); 

		// Diner's Club 

		validCardNumbers.add("36438936438936"); 

		// Discover 
		 
		validCardNumbers.add("6011016011016011"); 
		validCardNumbers.add("6011000990139424"); 
		validCardNumbers.add("6011000000000004"); 
		validCardNumbers.add("6011000995500000"); 
		validCardNumbers.add("6500000000000002"); 

		// Discover Diners
		 
		validCardNumbers.add("36110361103612"); 

		// Sample JCB Credit Card Numbers
		 
		validCardNumbers.add("3566003566003566"); 
		validCardNumbers.add("3528000000000007"); 

		// MasterCard 
		 
		validCardNumbers.add("5500005555555559"); 
		validCardNumbers.add("5555555555555557"); 
		validCardNumbers.add("5454545454545454"); 
		validCardNumbers.add("5555515555555551"); 
		validCardNumbers.add("5405222222222226"); 
		validCardNumbers.add("5478050000000007"); 
		validCardNumbers.add("5111005111051128"); 
		validCardNumbers.add("5112345112345114"); 


		// MasterCard Diners
		 
		validCardNumbers.add("36111111111111"); 


		// MasterCard II 
		 
		validCardNumbers.add("5115915115915118"); 


		// MasterCard III
		 
		validCardNumbers.add("4061724061724061"); 
		validCardNumbers.add("5115915115915118"); 

		// Sample Visa Credit Card Numbers
		 
		validCardNumbers.add("4444444444444448"); 
		validCardNumbers.add("4444424444444440"); 
		validCardNumbers.add("4444414444444441"); 
		validCardNumbers.add("4012888888881881"); 
		validCardNumbers.add("4055011111111111"); 


		// Visa Commercial Card
		 
		validCardNumbers.add("4110144110144115"); 


		// Visa Corporate Card II
		 
		validCardNumbers.add("4114360123456785"); 


		// Visa Purchasing Card III
		 
		validCardNumbers.add("4061724061724061"); 

		return validCardNumbers;
	}

}
