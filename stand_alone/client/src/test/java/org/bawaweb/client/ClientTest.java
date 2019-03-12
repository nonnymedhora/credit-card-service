///**
// *
// */
//package org.bawaweb.client;
//
//import static org.junit.Assert.*;
//
//import org.bawaweb.core.model.CustomerCard;
//import org.junit.Before;
//import org.junit.Test;
//
//import javax.ws.rs.core.Response;
//import java.lang.reflect.Constructor;
//import java.lang.reflect.InvocationTargetException;
//import java.sql.*;
//
///**
// * @author medhoran
// *
// */
//public class ClientTest {
//
//	/**
//	 * @throws java.lang.Exception
//	 */
//	@Before
//	public void setUp() throws Exception {
//	}
//
//	/**
//	 * Test method for {@link org.bawaweb.client.Client#doGetCardIdRequest(String)} ()}.
//	 */
//	@Test
//	public void testDoGetCardNumberRequest() {
//        // get a card id from jdbc
//        // get a card using jdbc
//        final int LIMIT = 1;
//        String encryptedCardNumber = null;
//        String cardId = null;
//        Connection conn = null;
//
//        try {
//            Class.forName("com.mysql.jdbc.Driver");
//            conn = DriverManager
//                    .getConnection("jdbc:mysql://localhost:3306/test");
//            String getCardNumberForCustomer = "select * from customer_card limit ?";
//            PreparedStatement ps1 = conn
//                    .prepareStatement(getCardNumberForCustomer);
//            ps1.setInt(1, LIMIT);
//            ResultSet rs = ps1.executeQuery();
//
//            while (rs.next()) {
//                cardId = rs.getString("card_id");
//                encryptedCardNumber = rs.getString("card_number");
//                System.out.println("VIA JDBC --- Retrieved card with encrypted number " + encryptedCardNumber);
//
//            }
//
//
//            conn.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//            try {
//                if (conn != null) {
//                    conn.close();
//                }
//            } catch (SQLException e1) {
//                e1.printStackTrace();
//            }
//        }
//
//
//        Client cc = null;
//        final Constructor<?>[] constructors = Client.class.getDeclaredConstructors();
//
//        try {
//            constructors[0].setAccessible(true);
//            cc = (Client) constructors[0].newInstance((Object[]) null);
//
//
//            Response response = cc.doGetCardIdRequest(cardId);
//
//            assertNotNull(response);
//
//            System.out.println(response.readEntity(CustomerCard.class));
//
//
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
//
//	/**
//	 * Test method for {@link org.bawaweb.client.Client#doAddCustomerCardPostRequest(org.bawaweb.core.model.CustomerCard)}.
//`   	 */
//	@Test
//	public void testDoAddCustomerCardPostRequest() {
////		fail("Not yet implemented");
//	}
//
//}
