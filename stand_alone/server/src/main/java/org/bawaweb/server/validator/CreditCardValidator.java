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
 * This class is used for validating the card number
 * and also carries out the Luhn check
 */
public final class CreditCardValidator {
	
    public static final int MASTERCARD = 0, VISA = 1;
    public static final int AMEX = 2, DISCOVER = 3, DINERS = 4;

	public static boolean validateCardNumber(CustomerCard customerCard) {
		System.out.println("in validateCard " + customerCard);
		String number = customerCard.getValue();
		
		if (StringUtils.isBlank (number)) {
            return false;
        }

        if ( number.indexOf('-') > 0 ) {
            // remove all dashes
            number = number.replaceAll("-","");
        }

		if ( number.length() > 16 ) {
			return false;
		}
		
        Matcher m = Pattern.compile("[^\\d\\s.-]").matcher(number);
        
        if (m.find()) {
//            setMessage("Credit card number can only contain numbers, spaces, \"-\", and \".\"");
            return false;
        }

        
        Matcher matcher = Pattern.compile("[\\s.-]").matcher(number);
        number = matcher.replaceAll("");


        if (checkLast7Digits(number)) {
            boolean isValid = isValidLuhnNumber(number);
            return isValid;
        } else {
            return false;
        }

    }


    /**
     * We do not want to store the number if the last 7 digits
     * are all the same.
     * ref: https://lesconcierges.atlassian.net/browse/bwwsIN-13
     * @param number
     * @return
     */
    private static boolean checkLast7Digits(String number) {
        boolean isLast7DigitsSame;
        final String last7Digits = number.substring(number.length()-7, number.length());
        final char[] firstChar = { last7Digits.charAt(0) };
        isLast7DigitsSame = StringUtils.containsOnly(last7Digits, firstChar);
        return isLast7DigitsSame;
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