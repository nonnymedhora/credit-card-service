/**
 * 
 */
package org.bawaweb.server.validator;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import org.bawaweb.core.model.CustomerCard;

/**
 * @author medhoran
 *
 */
public class CreditCardValidatorTest {
	
	private CreditCardValidator validator;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
        validator = new CreditCardValidator();
	}

	/**
	 * Test method for {@link org.bawaweb.server.validator.CreditCardValidator#validateCardNumber(org.bawaweb.core.model.CustomerCard)}.
	 */
	@Test
	// see http://www.auricsystems.com/support-center/sample-credit-card-numbers/
	// http://www.crazysquirrel.com/finance/test-cc.jspx
	public void testValidateCardNumberValidNum() throws Exception {
		
		List<String> validCardNumbers = getValidCardNumbers();
		for ( String number : validCardNumbers ) {
			CustomerCard customerCard = new CustomerCard();
			customerCard.setValue(number);
			boolean validated = validator.validateCardNumber(customerCard);
			
			assertEquals(true, validated);
		}
	}

    @Test
    public void testValidateCardNumberInvalidNum() throws Exception {
        List<String> invalidCardNumbers = getInvalidCardNumbers();

        for ( String number : invalidCardNumbers) {
            CustomerCard customerCard = new CustomerCard();
            customerCard.setValue(number);
            boolean validated = validator.validateCardNumber(customerCard);

            assertEquals(false, validated);
        }
     }

	private List<String> getValidCardNumbers() {
		List<String> validCardNumbers = new ArrayList<String>();
		// American Express
		 
		validCardNumbers.add("371449635398431");
		validCardNumbers.add("343434343434343"); 
		validCardNumbers.add("371144371144376"); 
		validCardNumbers.add("341134113411347");
        validCardNumbers.add("3411-341-1341-1347");

		// Diner's Club 

		validCardNumbers.add("36438936438936"); 

		// Discover 

		validCardNumbers.add("6011016011016011");
        validCardNumbers.add("6011-0160-1101-6011");
		validCardNumbers.add("6011000990139424");
		validCardNumbers.add("6011000000000004"); 
		validCardNumbers.add("6011000995500000"); 
		validCardNumbers.add("6500000000000002");       // note in code of org.apache.commons.validator.CreditCardValidator - Discover allows only 6011 prefix

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

    private List<String> getInvalidCardNumbers() {
        List<String> invalidCardNumbers = new ArrayList<String>();
        // note -- rearranged and replaced number from the above getValidCardNumbers method to force create invalid numbers
        // American Express format
        invalidCardNumbers.add("251442625228421");
        invalidCardNumbers.add("242424242424242");
        invalidCardNumbers.add("251144251144256");
        invalidCardNumbers.add("241124112411245");

        // Diner's Club format
        invalidCardNumbers.add("26428226428226");

        // Discover format
        invalidCardNumbers.add("6711716711716711");
        invalidCardNumbers.add("6711777227122424");
        invalidCardNumbers.add("6711777777777774");
        invalidCardNumbers.add("6711777225577777");
        invalidCardNumbers.add("6577777777777772");

        // Discover Diners format
        invalidCardNumbers.add("26117261172612");

        // Sample JCB Credit Card Numbers format
        invalidCardNumbers.add("2566772566772566");
        invalidCardNumbers.add("2528777777777775");

        // MasterCard format
        invalidCardNumbers.add("5577775555555552");
        invalidCardNumbers.add("5555555555555555");
        invalidCardNumbers.add("5454545454545459");
        invalidCardNumbers.add("5555515555555559");
        invalidCardNumbers.add("5475222222222226");
        invalidCardNumbers.add("5458757777777775");
        invalidCardNumbers.add("5111775111751128");
        invalidCardNumbers.add("5112245112245114");

        // MasterCard Diners format
        invalidCardNumbers.add("26111111111111");

        // MasterCard II format
        invalidCardNumbers.add("5115215115215119");

        // MasterCard III format
        invalidCardNumbers.add("4761524761524761");
        invalidCardNumbers.add("5115215115215119");

        // Sample Visa Credit Card Numbers format
        invalidCardNumbers.add("4444444444444449");
        invalidCardNumbers.add("4444424444444447");
        invalidCardNumbers.add("4944414444444449");
        invalidCardNumbers.add("4712888888881881");
        invalidCardNumbers.add("4755711111111111");

        // Visa Commercial Card format
        invalidCardNumbers.add("4117144117144115");


        // Visa Corporate Card II format
        invalidCardNumbers.add("4114267122456589");


        // Visa Purchasing Card III format
        invalidCardNumbers.add("4761524761524769");

        return invalidCardNumbers;
    }
	
	@Test
	public void testCheckLast7DigitsInvalid() {
	}
	
	private List<String> getCardsLast7DisgitsSame() {
		List<String> invalidCardNumbers = new ArrayList<String>();
	}

}
