package org.bawaweb.core.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by medhoran on 1/9/14.
 */
public interface AuditableEntity extends Serializable {


    public String getCreatedBy();

    public void setCreatedBy(String createdBy);

    public Timestamp getDateCreated();

    public String getModifiedBy();

    public void setModifiedBy(String modifiedBy);

    public Timestamp getDateModified();

    public void setDateModified(Timestamp dateModified);

}
