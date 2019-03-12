package org.bawaweb.server.loader;


/**
 * Created by medhoran on 12/23/13.
 */
public class CustomerCardTriplet {
    // a triplet object which contains
    // customerId, creditCardId, cardNumber(decrypted)
    // these values are initially taken from the smartagent_uat2 schema

    private String customerId;
    private String creditCardId;
    private String cardNumber;

    public CustomerCardTriplet() {}

    public CustomerCardTriplet(String custId, String cardId, String cardNum) {
        this.customerId = custId;
        this.creditCardId = cardId;
        this.cardNumber = cardNum;
    }


    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCreditCardId() {
        return creditCardId;
    }

    public void setCreditCardId(String creditCardId) {
        this.creditCardId = creditCardId;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }
}
