/**
 * 
 */
package org.bawaweb.core.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlRootElement;

import org.bawaweb.core.model.id.CustomerUUIDClass;

/**
 * @author medhoran
 *
create table customer_uuid (
	customer_id 		varchar(16) not null,	
	customer_uuid		varchar(128) not null,
	created_by 			varchar(20) not null,
	creation_date 		timestamp not null default current_timestamp,	
	modified_by 		varchar(20),
	modification_date 	timestamp not null default current_timestamp,
	primary key (customer_id),
	index customer_uuid_ind1 (customer_id),
	index customer_uuid_ind2 (customer_uuid)
) engine=innodb;

@IdClass(CustomerUUIDClass.class)
 */
@Entity
@Table(name = "CUSTOMER_UUID")
@XmlRootElement(name = "CustomerUUID")
public class CustomerUUID implements Serializable {
	
	private static final long serialVersionUID = 3452328734l;
	
	@Id
	@Column(name = "CUSTOMER_UUID")
	private String customerUUID;
	
	@Column(name = "CUSTOMER_ID")
	private String customerID;
	
	 /// auditing data
	@Column(name = "CREATED_BY")
	private String createdBy;
	
	@Column(
		name="CREATION_DATE", 
		columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP", 
		insertable=false, updatable=false
	)
	private Timestamp creationDate;
	
	@Column(name = "MODIFIED_BY")
	private String modifiedBy;
	
	
	@Version
	@Column(
		name="MODIFICATION_DATE",
		columnDefinition="TIMESTAMP", 
		insertable=false, updatable=false
	)
	private Timestamp modificationDate;
	// ends auditing data


	/**
	 * @return the customerID
	 */
	public String getCustomerID() {
		return customerID;
	}

	/**
	 * @param customerID the customerID to set
	 */
	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}

	/**
	 * @return the customerUUID
	 */
	public String getCustomerUUID() {
		return customerUUID;
	}

	/**
	 * @param customerUUID the customerUUID to set
	 */
	public void setCustomerUUID(String customerUUID) {
		this.customerUUID = customerUUID;
	}
	

	/**
	 * @return the createdBy
	 */
	public String getCreatedBy() {
		return createdBy;
	}

	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * @return the creationDate
	 */
	public Timestamp getCreationDate() {
		return creationDate;
	}

	/**
	 * @param creationDate the creationDate to set
	 */
	public void setCreationDate(Timestamp creationDate) {
		this.creationDate = creationDate;
	}

	/**
	 * @return the modifiedBy
	 */
	public String getModifiedBy() {
		return modifiedBy;
	}

	/**
	 * @param modifiedBy the modifiedBy to set
	 */
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	/**
	 * @return the modificationDate
	 */
	public Timestamp getModificationDate() {
		return modificationDate;
	}

	/**
	 * @param modificationDate the modificationDate to set
	 */
	public void setModificationDate(Timestamp modificationDate) {
		this.modificationDate = modificationDate;
	}
	
	@PrePersist 
	void onCreate() { 
		this.setCreationDate(new Timestamp((new java.util.Date()).getTime()));
	}
	
	@PreUpdate 
	void onPersist() {
		this.setModificationDate(new Timestamp((new Date()).getTime())); 
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CustomerUUID [customerID=" + customerID + ", customerUUID="
				+ customerUUID + ", createdBy=" + createdBy + ", creationDate="
				+ creationDate + ", modifiedBy=" + modifiedBy
				+ ", modificationDate=" + modificationDate + "]";
	}




}


