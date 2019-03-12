/**
 * 
 */
package org.bawaweb.core.model;

import java.util.Date;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;

/**
 * @author medhoran
 *- See more at: http://blog.octo.com/en/audit-with-jpa-creation-and-update-date/#sthash.u6pAwg7X.dpuf
 */
@MappedSuperclass
public abstract class AuditableEntity {
	
	
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

}
