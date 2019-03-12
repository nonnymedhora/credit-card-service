/**
 * 
 */
package org.bawaweb.core.model.id;

import java.io.Serializable;

/**
 * @author medhoran
 *
 */
public class CustomerUUIDClass implements Serializable {
	private String customerID;
	private String customerUUID;
	public CustomerUUIDClass(String customerId, String customerUuid) {
		super();
		this.customerID = customerId;
		this.customerUUID = customerUuid;
	}
	public CustomerUUIDClass() {}
	public String getCustomerId() {
		return customerID;
	}
	public void setCustomerId(String customerId) {
		this.customerID = customerId;
	}
	public String getCustomerUUID() {
		return customerUUID;
	}
	public void setCustomerUUID(String customerUuid) {
		this.customerUUID = customerUuid;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((customerID == null) ? 0 : customerID.hashCode());
		result = prime * result
				+ ((customerUUID == null) ? 0 : customerUUID.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CustomerUUIDClass other = (CustomerUUIDClass) obj;
		if (customerID == null) {
			if (other.customerID != null)
				return false;
		} else if (!customerID.equals(other.customerID))
			return false;
		if (customerUUID == null) {
			if (other.customerUUID != null)
				return false;
		} else if (!customerUUID.equals(other.customerUUID))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "CustomerUUIDClass [customerID=" + customerID
				+ ", customerUUID=" + customerUUID + "]";
	}
	
	
	
}

