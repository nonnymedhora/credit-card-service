package org.bawaweb.server.loader;

/**
 * Created by medhoran on 12/23/13.
 * testing connection to smartagent_uat2
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

//jdbc:jtds:sqlserver://10.0.103.80:1433/smartagent_UAT2
public class ConnectMSSQL {
    public static void main(String[] args) {
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            Connection connection = DriverManager
                    .getConnection(
                            "jdbc:jtds:sqlserver://10.0.103.80:1433/smartagent_UAT2",
                            "appUser2","Pm8!RbRG");

            System.out.println("DATABASE NAME IS:"
                    + connection.getMetaData().getDatabaseProductName());
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement
                    .executeQuery("select top(10)\n" +
                            "\tC.customerId,\n" +
                            "\tC.creditCardId,\n" +
                            "\tCu.cardNumber\n" +
                            "from CustCreditCards c, CreditCards cu\n" +
                            "where C.creditCardID = Cu.creditCardID;");
            while (resultSet.next()) {
                System.out.println("CUSTOMER_ID:"
                        + resultSet.getString("customerId"));
                System.out.println("CREDIT_CARD_ID:"
                        + resultSet.getString("creditCardId"));
                System.out.println("CREDIT_CARD_NUMBER:"
                        + resultSet.getString("cardNumber"));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
