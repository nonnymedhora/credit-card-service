/**
 * 
 */
package org.bawaweb.core.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author medhoran
 *
 * create table card_audit_log (
    audit_id			integer(9) not null auto_increment,
    session_id			varchar(128) not null,
    user_id				integer(9) not null,
    card_id				varchar(128) not null,
    client_id			integer(9) not null,
    sub_client_id		integer(9),
    concierge_id		integer(9),
    customer_id			integer,
    request_type		char not null,
    created_by 			varchar(20) not null,
    creation_date 		timestamp not null default current_timestamp,
    modified_by 		varchar(20),
    modification_date 	timestamp not null default current_timestamp,
    primary key (audit_id),
    index card_audit_ind1 (card_id),
    index card_audit_ind2 (customer_id),
    index card_audit_ind3 (session_id),
    constraint fk_card_audit_card
    foreign key (card_id) references customer_card(card_id)
    ) engine=innodb;
 */

@Entity
@Table(name = "card_audit_log")
@XmlRootElement(name = "CardAuditLog")
@XmlAccessorType(XmlAccessType.FIELD)
@TableGenerator(
		name="cardAuditLogGen",
		table="id_gen",
		pkColumnName="gen_name",
		valueColumnName="gen_value",
        pkColumnValue="audit_id",
        initialValue=10000,
        allocationSize=1)
public class CardAuditLog implements Serializable {
	
	private static final long serialVersionUID = 456789876543l;
	
	@Id
    @GeneratedValue(strategy=GenerationType.TABLE, generator="cardAuditLogGen")
	@Column(name="AUDIT_ID")
	private Integer auditId;

	// connects with SessionInfo.sessionId which is the server's request id
    // for processing the incoming message
    @Column(name="SESSION_ID")
	private String sessionId;
	
	@Column(name="USER_ID")
	private Integer userId;

	@Column(name="CARD_ID")
	private String cardId;
	
	@Column(name="CLIENT_ID")
	private Integer clientId;
	
	@Column(name="SUB_CLIENT_ID")
	private Integer subClientId;
	
	@Column(name="CONCIERGE_ID")
	private Integer conciergeId;
	
	@Column(name = "CUSTOMER_ID")
	private Integer customerId;
	
	@Column(name="REQUEST_TYPE")
	private Character requestType;		// can be 'A' for Add, 'U' update, 'G' for get
	
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
	 * @return the auditId
	 */
	public Integer getAuditId() {
		return auditId;
	}


	/**
	 * @param auditId the auditId to set
	 */
	public void setAuditId(Integer auditId) {
		this.auditId = auditId;
	}


	/**
	 * @return the sessionId
	 */
	public String getSessionId() {
		return sessionId;
	}


	/**
	 * @param sessionId the sessionId to set
	 */
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}


	/**
	 * @return the userId
	 */
	public Integer getUserId() {
		return userId;
	}


	/**
	 * @param userId the userId to set
	 */
	public void setUserId(Integer userId) {
		this.userId = userId;
	}


	/**
	 * @return the cardId
	 */
	public String getCardId() {
		return cardId;
	}


	/**
	 * @param cardId the cardId to set
	 */
	public void setCardId(String cardId) {
		this.cardId = cardId;
	}


	/**
	 * @return the clientId
	 */
	public Integer getClientId() {
		return clientId;
	}


	/**
	 * @param clientId the clientId to set
	 */
	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}


	/**
	 * @return the subClientId
	 */
	public Integer getSubClientId() {
		return subClientId;
	}


	/**
	 * @param subClientId the subClientId to set
	 */
	public void setSubClientId(Integer subClientId) {
		this.subClientId = subClientId;
	}


	/**
	 * @return the conciergeId
	 */
	public Integer getConciergeId() {
		return conciergeId;
	}


	/**
	 * @param conciergeId the conciergeId to set
	 */
	public void setConciergeId(Integer conciergeId) {
		this.conciergeId = conciergeId;
	}


	/**
	 * @return the customerID
	 */
	public Integer getCustomerId() {
		return customerId;
	}


	/**
	 * @param customerID 
	 */
	public void setCustomerId(Integer customerID) {
		this.customerId = customerID;
	}


	/**
	 * @return the requestType
	 */
	public Character getRequestType() {
		return requestType;
	}


	/**
	 * @param requestType the requestType to set
	 */
	public void setRequestType(Character requestType) {
		this.requestType = requestType;
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


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CardAuditLog [auditId=" + auditId + ", sessionId=" + sessionId
				+ ", userId=" + userId + ", cardId=" + cardId + ", clientId="
				+ clientId + ", subClientId=" + subClientId + ", conciergeId="
				+ conciergeId + ", customerId=" + customerId + ", requestType="
				+ requestType + ", createdBy=" + createdBy + ", creationDate="
				+ creationDate + ", modifiedBy=" + modifiedBy
				+ ", modificationDate=" + modificationDate + "]";
	}

	

}
