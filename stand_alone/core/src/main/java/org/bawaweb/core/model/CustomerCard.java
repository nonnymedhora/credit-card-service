package org.bawaweb.core.model;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

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
POJO for persisting to card table
 */
@Entity
@Table(name = "customer_card")
@XmlRootElement(name = "CustomerCard")
//@JsonTypeName(name = "CustomerCard")

@XmlAccessorType(XmlAccessType.FIELD)
public class CustomerCard implements AuditableEntity {
	
	private static final long serialVersionUID = 3456787654l;

    // empty ctor
    public CustomerCard() {}

    // ctor taking Card from Enterprise Domain
    public CustomerCard(Card aCard) {
        this.cardId = aCard.getId();
        this.value = aCard.getValue();

        Audit audit = aCard.getAudit();

        if (audit != null) {
            if ( audit.getDateCreated() != null)
                this.dateCreated = Timestamp.valueOf(aCard.getAudit().getDateCreated());

            if ( audit.getDateModified() != null)
                this.dateModified = Timestamp.valueOf(aCard.getAudit().getDateModified());

            this.createdBy = aCard.getAudit().getCreatedBy();
            this.modifiedBy = aCard.getAudit().getModifiedBy();
        }

        this.result = aCard.getResult();
    }


	
    @Id
	@Column(name="CARD_ID")
	private String cardId;

    // the card number (decrypted) of the customer
	 @Column(name = "CARD_NUMBER")
	private String value;
	 
	// auditing data
	@Column(name = "CREATED_BY")
	private String createdBy;
	
	@Column(
		name="CREATION_DATE", 
		columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP", 
		insertable=false, updatable=false
	)
    @XmlJavaTypeAdapter(TimestampAdapter.class)
	private Timestamp dateCreated;
	
	@Column(name = "MODIFIED_BY")
	private String modifiedBy;
	
	
	@Version
	@Column(
		name="MODIFICATION_DATE",
		columnDefinition="TIMESTAMP", 
		insertable=false, updatable=false
	)
    @XmlJavaTypeAdapter(TimestampAdapter.class)
	private Timestamp dateModified;
	// ends auditing data


    @Transient
    private Result result;
	
	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
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
	 * @return the dateCreated
	 */
	public Timestamp getDateCreated() {
		return dateCreated;
	}

	/**
	 * @param dateCreated the dateCreated to set
	 */
	public void setDateCreated(Timestamp dateCreated) {
		this.dateCreated = dateCreated;
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
	 * @return the dateModified
	 */
	public Timestamp getDateModified() {
		return dateModified;
	}

	/**
	 * @param dateModified the dateModified to set
	 */
	public void setDateModified(Timestamp dateModified) {
		this.dateModified = dateModified;
	}

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    @PrePersist
	void onCreate() { 
		this.setDateCreated(new Timestamp((new java.util.Date()).getTime()));
	}
	
	@PreUpdate 
	void onPersist() {
		this.setDateModified(new Timestamp((new Date()).getTime()));
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CustomerCard that = (CustomerCard) o;

        if (cardId != null ? !cardId.equals(that.cardId) : that.cardId != null) return false;
        if (createdBy != null ? !createdBy.equals(that.createdBy) : that.createdBy != null) return false;
        if (dateCreated != null ? !dateCreated.equals(that.dateCreated) : that.dateCreated != null) return false;
        if (dateModified != null ? !dateModified.equals(that.dateModified) : that.dateModified != null) return false;
        if (modifiedBy != null ? !modifiedBy.equals(that.modifiedBy) : that.modifiedBy != null) return false;
        if (result != null ? !result.equals(that.result) : that.result != null) return false;
        if (value != null ? !value.equals(that.value) : that.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result1 = cardId != null ? cardId.hashCode() : 0;
        result1 = 31 * result1 + (value != null ? value.hashCode() : 0);
        result1 = 31 * result1 + (createdBy != null ? createdBy.hashCode() : 0);
        result1 = 31 * result1 + (dateCreated != null ? dateCreated.hashCode() : 0);
        result1 = 31 * result1 + (modifiedBy != null ? modifiedBy.hashCode() : 0);
        result1 = 31 * result1 + (dateModified != null ? dateModified.hashCode() : 0);
        result1 = 31 * result1 + (result != null ? result.hashCode() : 0);
        return result1;
    }

    public Card transformToCard(CustomerCard customerCard) {
        Card card = new Card();
        Audit audit = new Audit();
        card.setAudit(audit);

        Message message = new Message();
        Set<Message> messageSet = new HashSet<Message>();
        messageSet.add(message);

        Result result = new Result();
        result.setMessage(messageSet);


        if (customerCard != null) {
            card.setId(customerCard.getCardId());
            card.setValue(customerCard.getValue());

            card.getAudit().setCreatedBy(customerCard.getCreatedBy());
            card.getAudit().setModifiedBy(customerCard.getModifiedBy());

            if (customerCard.getDateCreated() != null) {
                card.getAudit().setDateCreated(customerCard.getDateCreated().toString());
            }
            if (customerCard.getDateModified() != null) {
                card.getAudit().setDateModified(customerCard.getDateModified().toString());
            }

            // a blank dummy result which needs to have its values populated once the transformToCard method is called
            card.setResult(result);
        }

        return card;
    }





    public String toJSON() {
        //{"CustomerCard":{"cardId":"2dcf8eab-fe9a-4d84-9e9f-299f89141caf","cardNumber":5405222222222226,"createdBy":"TUUUMEEEEEE","dateCreated":"2013-12-01T06:09:02-08:00","modifiedBy":"TUUUMEEEEEE","dateModified":"2013-12-01T06:09:02-08:00"}}
        // cardNumber now changed to value  nm jan7
        String jsonRep = "{\"CustomerCard\":{\"cardId\":\"";
        jsonRep += cardId + "\", \"value\":\"";
        jsonRep += value + "\", \"audit\":{\"createdBy\":\"";
        jsonRep += createdBy + "\", \"dateCreated\":\"";
        jsonRep += dateCreated + "\", \"modifiedBy\":\"";
        jsonRep += modifiedBy + "\", \"dateModified\":\"";
        jsonRep += dateModified + "\"}\"result\":\"{\"";
        jsonRep += result+ "\"}}";//"\"}}}";

        return jsonRep;
    }


}
