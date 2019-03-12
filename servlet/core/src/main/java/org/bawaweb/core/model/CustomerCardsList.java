package org.bawaweb.core.model;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

 @XmlRootElement(name = "CustomerCardsList") 
public class CustomerCardsList implements Serializable {
	private static final long serialVersionUID = 54637283746l;
	
	@XmlElement(name="CustomerCard")
    public List<CustomerCard> cardsList;
    
    public List<CustomerCard> getCardsList() {
		return cardsList;
	}

	public void setCardsList(List<CustomerCard> cardsList) {
		this.cardsList = cardsList;
	}

	public CustomerCardsList() {}

    @Override
	public String toString() {
		return "CustomerCardsList [cardsList=" + cardsList + "]";
	}

    
}