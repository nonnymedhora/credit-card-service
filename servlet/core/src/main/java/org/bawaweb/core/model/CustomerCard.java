package org.bawaweb.core.model;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;

/**
 * @author medhoran
 *
create table if not exists customer_card (
	card_id				varchar(128) not null,
	card_number 		mediumtext not null,
	created_by 			varchar(20) not null,
	creation_date 		timestamp not null default current_timestamp,	
	modified_by 		varchar(20),
	modification_date 	timestamp not null default current_timestamp,
	primary key (card_id)
) engine=innodb;

 */
@Entity
@Table(name = "CUSTOMER_CARD")
@XmlRootElement(name = "CustomerCard")
@XmlAccessorType(XmlAccessType.FIELD)
public class CustomerCard implements Serializable {
	
	private static final long serialVersionUID = 3456787654l;
	
    @Id
	@Column(name="CARD_ID")
	private String cardId;

    // the card number (decrypted) of the customer
	 @Column(name = "CARD_NUMBER")
	private String cardNumber;
	 
	// auditing data
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
	
	
	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
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
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cardId == null) ? 0 : cardId.hashCode());
		result = prime * result
				+ ((cardNumber == null) ? 0 : cardNumber.hashCode());
		result = prime * result
				+ ((createdBy == null) ? 0 : createdBy.hashCode());
		result = prime * result
				+ ((creationDate == null) ? 0 : creationDate.hashCode());
		result = prime
				* result
				+ ((modificationDate == null) ? 0 : modificationDate.hashCode());
		result = prime * result
				+ ((modifiedBy == null) ? 0 : modifiedBy.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof CustomerCard))
			return false;
		CustomerCard other = (CustomerCard) obj;
		if (cardId == null) {
			if (other.cardId != null)
				return false;
		} else if (!cardId.equals(other.cardId))
			return false;
		if (cardNumber == null) {
			if (other.cardNumber != null)
				return false;
		} else if (!cardNumber.equals(other.cardNumber))
			return false;
		if (createdBy == null) {
			if (other.createdBy != null)
				return false;
		} else if (!createdBy.equals(other.createdBy))
			return false;
		if (creationDate == null) {
			if (other.creationDate != null)
				return false;
		} else if (!creationDate.equals(other.creationDate))
			return false;
		if (modificationDate == null) {
			if (other.modificationDate != null)
				return false;
		} else if (!modificationDate.equals(other.modificationDate))
			return false;
		if (modifiedBy == null) {
			if (other.modifiedBy != null)
				return false;
		} else if (!modifiedBy.equals(other.modifiedBy))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CustomerCard [cardId=" + cardId + ", cardNumber=" + cardNumber
				+ ", createdBy=" + createdBy + ", creationDate=" + creationDate
				+ ", modifiedBy=" + modifiedBy + ", modificationDate="
				+ modificationDate + "]";
	}
}
