package org.bawaweb.server.loader;

import org.bawaweb.server.crypt.DesEncrypter;

import java.sql.*;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by medhoran on 12/23/13.
 * // gets customer, card infor from the uat2 schema
 */
public class UATDataExtractor {

    // sql server jdbc
    private Connection connection;
//    private HashMap<String, CustomerCardTriplet> existingUATCustomerMap;

    public UATDataExtractor() {
        connection = getDBConnection();
    }

    private Connection getDBConnection() {
        Connection conn = null;
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            conn = DriverManager
                    .getConnection(
                            "jdbc:jtds:sqlserver://10.0.103.80:1433/smartagent_UAT2",
                            "appUser2", "Pm8!RbRG");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    public HashMap<String, CustomerCardTriplet> getCustomerCardUATData() {
        HashMap<String, CustomerCardTriplet> custCardMap = new HashMap<String, CustomerCardTriplet>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement
                    .executeQuery("select top(1000)\n" +
                            "\tC.customerId,\n" +
                            "\tC.creditCardId,\n" +
                            "\tCu.cardNumber\n" +
                            "from CustCreditCards c, CreditCards cu\n" +
                            "where C.creditCardID = Cu.creditCardID;");
            while (resultSet.next()) {
                /*System.out.println("CUSTOMER_ID:"
                        + resultSet.getString("customerId"));
                System.out.println("CREDIT_CARD_ID:"
                        + resultSet.getString("creditCardId"));
                System.out.println("CREDIT_CARD_NUMBER:"
                        + resultSet.getString("cardNumber"));*/

                String customerId = resultSet.getString("customerId");
                String cardId = resultSet.getString("creditCardId");
                String encryptedCardNumber = resultSet.getString("cardNumber");

                CustomerCardTriplet triplet = new CustomerCardTriplet(customerId, cardId, encryptedCardNumber);

                custCardMap.put(customerId, triplet);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return custCardMap;
    }



    public void decryptUAT2CardNumbers(HashMap<String, CustomerCardTriplet> custMap) {
        Set<String> customers = custMap.keySet();

        for (String customerId : customers ) {
            CustomerCardTriplet custTriplet = custMap.get(customerId);

            String encryptedNumber = custTriplet.getCardNumber();

            // decrypt the number using the DesEncrypter
            DesEncrypter desEncrypter = null;
            try {

                desEncrypter = new DesEncrypter("noZb8fEcg1ieKhDE5scC8QhKQJS/zQgV");

                String decryptedNumber = desEncrypter.decryptString(encryptedNumber);

                custTriplet.setCardNumber(decryptedNumber);


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
