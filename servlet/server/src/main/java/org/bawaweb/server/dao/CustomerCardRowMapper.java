//package org.bawaweb.server.dao;
//
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import org.bawaweb.model.CustomerCard;
//import org.springframework.jdbc.core.RowMapper;
//
//public class CustomerCardRowMapper implements RowMapper {
//
//	@Override
//	public CustomerCard mapRow(ResultSet rs, int arg1) throws SQLException {
//		CustomerCard customerCard = new CustomerCard();
//		customerCard.setCustomerXAID(rs.getString("customer_id"));
//		customerCard.setCardNumber(rs.getString("cardNumber"));
//		return customerCard;
//	}
//
//}
