package edu.wustl.catissuecore.domain.deintegration;

import java.io.Serializable;

import edu.common.dynamicextensions.domain.integration.AbstractRecordEntry;
import edu.wustl.catissuecore.domain.sop.ActionApplication;
/**
	*
	**/

public class ActionApplicationRecordEntry extends AbstractRecordEntry implements Serializable
{
	/**
	* An attribute to allow serialization of the domain objects
	*/
	private static final long serialVersionUID = 1234567890L;

	/**
	* An associated edu.wustl.catissuecore.domain.sop.ActionApplication object
	**/

	private ActionApplication actionApplication;
	/**
	* Retrieves the value of the actionApplication attribute
	* @return actionApplication
	**/

	public ActionApplication getActionApplication(){
		return actionApplication;
	}
	/**
	* Sets the value of actionApplication attribute
	**/

	public void setActionApplication(ActionApplication actionApplication){
		this.actionApplication = actionApplication;
	}

	/**
	* Compares <code>obj</code> to it self and returns true if they both are same
	*
	* @param obj
	**/
	public boolean equals(Object obj)
	{
		if(obj instanceof ActionApplicationRecordEntry)
		{
			ActionApplicationRecordEntry c =(ActionApplicationRecordEntry)obj;
			if(getId() != null && getId().equals(c.getId()))
				return true;
		}
		return false;
	}

	/**
	* Returns hash code for the primary key of the object
	**/
	public int hashCode()
	{
		if(getId() != null)
			return getId().hashCode();
		return 0;
	}

}