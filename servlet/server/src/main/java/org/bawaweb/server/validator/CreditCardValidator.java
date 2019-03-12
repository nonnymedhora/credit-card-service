/**
 * 
 */
package org.bawaweb.server.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import org.bawaweb.core.model.CustomerCard;

/**
 * @author medhoran
 *
 */
public final class CreditCardValidator {
	
    public static final int MASTERCARD = 0, VISA = 1;
    public static final int AMEX = 2, DISCOVER = 3, DINERS = 4;

	String pattern = "(\\w)(\\s+)([\\.,])";
//private static final Strinf MY_CC_PATTERN = "";
//	private static final String CC_PATTERN = "^(?:(4[0-9]{12}(?:[0-9]{3})?)|(5[1-5][0-9]{14})|?(6(?:011|5[0-9][0-9])[0-9]{12})|(3[47][0-9]{13})|(3(?:0[0-5]|[68][0-9])?[0-9]{11})|((?:2131|1800|35\d{3})\d{11}))$";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public static boolean validateCardNumber(CustomerCard customerCard) {
		System.out.println("in validateCard " + customerCard);
		String number = customerCard.getCardNumber();
		
		if (StringUtils.isBlank (number)) { //number == null || number.equals("")) {
			System.out.println("here   1");
//            setMessage("Field cannnot be blank.");
            return false;
        }
		
		if ( number.length() > 16 ) {
			System.out.println("here   2");
			// set message 
			return false;
		}
		
        Matcher m = Pattern.compile("[^\\d\\s.-]").matcher(number);
        
        if (m.find()) {
//            setMessage("Credit card number can only contain numbers, spaces, \"-\", and \".\"");
        	System.out.println("here   3");
            return false;
        }

        
        Matcher matcher = Pattern.compile("[\\s.-]").matcher(number);
        number = matcher.replaceAll("");
        boolean isValid = isValidLuhnNumber(number);//luhnValidate(number);
        System.out.println("is luhn valid " + isValid);
        return isValid;

	}
	
	//http://www.chriswareham.demon.co.uk/software/Luhn.java
	private static final Pattern PATTERN = Pattern.compile("^\\d{13,19}$");



    /**
     * Checks whether a string is a valid credit card number according to the
     * Luhn algorithm.
     *
     * 1. Starting with the second to last digit and moving left, double the
     *    value of all the alternating digits. For any digits that thus become
     *    10 or more, add their digits together. For example, 1111 becomes 2121,
     *    while 8763 becomes 7733 (from (1+6)7(1+2)3).
     *
     * 2. Add all these digits together. For example, 1111 becomes 2121, then
     *    2+1+2+1 is 6; while 8763 becomes 7733, then 7+7+3+3 is 20.
     *
     * 3. If the total ends in 0 (put another way, if the total modulus 10 is
     *    0), then the number is valid according to the Luhn formula, else it is
     *    not valid. So, 1111 is not valid (as shown above, it comes out to 6),
     *    while 8763 is valid (as shown above, it comes out to 20).
     *
     * @param number the credit card number to validate.
     * @return true if the number is valid, false otherwise.
     */
    private static boolean isValidLuhnNumber(final String number) {
        if (PATTERN.matcher(number).matches()) {
            int sum = 0;

            boolean alternate = false;
            for (int i = number.length() - 1; i >= 0; i--) {
                int n = Integer.parseInt(number.substring(i, i + 1));
                if (alternate) {
                    n *= 2;
                    if (n > 9) {
                        n = (n % 10) + 1;
                    }
                }
                sum += n;
                alternate = !alternate;
            }

            return (sum % 10 == 0);
        }
        return false;
    }



}



/** original
for (int i=0; i < number.length; i++)
total += number[i];

if (total % 10 != 0)
    return false;

return true;

**/