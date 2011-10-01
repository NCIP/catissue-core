package edu.wustl.catissuecore.actionForm;

import java.io.Serializable;

import org.apache.struts.action.ActionForm;

/**
 * @author Ravi.Batchu
 * 
 */
public class CTRPEntityForm extends ActionForm implements Serializable {

	private static final long serialVersionUID = 3036534617240025582L;
	private String coppaSelection;
	private String operation;
	private String entityName;
	private String firstName;
	private String lastName;
	private String emailAddress;
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}



	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getCoppaSelection() {
		return coppaSelection;
	}

	public void setCoppaSelection(String coppaSelection) {
		this.coppaSelection = coppaSelection;
	}

}
