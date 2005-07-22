/**
 * <p>Title: DistributionProtocolForm Class>
 * <p>Description:  DistributionProtocolForm Class is used to encapsulate all the request parameters passed 
 * from ApplicationUser Add/Edit webpage. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 * Created on Mar 3, 2005
 */

package edu.wustl.catissuecore.actionForm;

import java.util.Collection;
import java.util.HashSet;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.domain.AbstractDomainObject;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Validator;

/**
 * DistributionProtocolForm Class is used to encapsulate all the request parameters passed 
 * from Distribution Add/Edit webpage.
 * @author Mandar Deshmukh
 * */
public class DistributionProtocolForm extends AbstractActionForm
{
    /**
     * identifier is a unique id assigned to each ApplicationUser.
     * */
    private long identifier;

    /**
     * Represents the operation(Add/Edit) to be performed.
     * */
    private String operation;
    
    private String activityStatus;
    
    
    private String principalinvestigator;
    private String protocolcoordinator;
    private String irbid;
    private String descriptionurl;
    private String title;
    private String   shorttitle;

    private java.util.Date startDate;
    private java.util.Date endDate;
    private String    participants;
	
    private Collection protocolCollection = new HashSet();
    
    private String enrollment;
    private String tissueType;
    private String tissueSide;
    private String tissueSite;
    private String specimenType;
    
    
	/**
	 * @return Returns the protocolCollection.
	 */
	public Collection getProtocolCollection() {
		return protocolCollection;
	}
	
	/**
	 * @param protocolCollection The protocolCollection to set.
	 */
	public void setProtocolCollection(Collection protocolCollection) {
		this.protocolCollection = protocolCollection;
	}
    
    
	/**
	 * @return Returns the enrollment.
	 */
	public String getEnrollment() {
		return enrollment;
	}
	/**
	 * @param enrollment The enrollment to set.
	 */
	public void setEnrollment(String enrollment) {
		this.enrollment = enrollment;
	}
	/**
	 * @return Returns the specimenType.
	 */
	public String getSpecimenType() {
		return specimenType;
	}
	/**
	 * @param specimenType The specimenType to set.
	 */
	public void setSpecimenType(String specimenType) {
		this.specimenType = specimenType;
	}
	/**
	 * @return Returns the tissueSide.
	 */
	public String getTissueSide() {
		return tissueSide;
	}
	/**
	 * @param tissueSide The tissueSide to set.
	 */
	public void setTissueSide(String tissueSide) {
		this.tissueSide = tissueSide;
	}
	/**
	 * @return Returns the tissueSite.
	 */
	public String getTissueSite() {
		return tissueSite;
	}
	/**
	 * @param tissueSite The tissueSite to set.
	 */
	public void setTissueSite(String tissueSite) {
		this.tissueSite = tissueSite;
	}
	/**
	 * @return Returns the tissueType.
	 */
	public String getTissueType() {
		return tissueType;
	}
	/**
	 * @param tissueType The tissueType to set.
	 */
	public void setTissueType(String tissueType) {
		this.tissueType = tissueType;
	}
    /**
     * No argument constructor for DistributionProtocolForm class. 
     */
    public DistributionProtocolForm()
    {
        reset();
    }

    /**
     * Copies the data from an AbstractDomain object to a DistributionProtocolForm object.
     * @param user An AbstractDomain object.  
     */
    public void setAllValues(AbstractDomainObject abstractDomain)
    {
        try
        {
        	;
        }
        catch (Exception excp)
        {
        	;
        }
    }

    
    
    
	/**
	 * @return Returns the descriptionurl.
	 */
	public String getDescriptionurl() {
		return descriptionurl;
	}
	/**
	 * @param descriptionurl The descriptionurl to set.
	 */
	public void setDescriptionurl(String descriptionurl) {
		this.descriptionurl = descriptionurl;
	}
	/**
	 * @return Returns the endDate.
	 */
	public java.util.Date getEndDate() {
		return endDate;
	}
	/**
	 * @param endDate The endDate to set.
	 */
	public void setEndDate(java.util.Date endDate) {
		this.endDate = endDate;
	}
	/**
	 * @return Returns the irbid.
	 */
	public String getIrbid() {
		return irbid;
	}
	/**
	 * @param irbid The irbid to set.
	 */
	public void setIrbid(String irbid) {
		this.irbid = irbid;
	}
	/**
	 * @return Returns the participants.
	 */
	public String getParticipants() {
		return participants;
	}
	/**
	 * @param participants The participants to set.
	 */
	public void setParticipants(String participants) {
		this.participants = participants;
	}
	/**
	 * @return Returns the principalinvestigator.
	 */
	public String getPrincipalinvestigator() {
		return principalinvestigator;
	}
	/**
	 * @param principalinvestigator The principalinvestigator to set.
	 */
	public void setPrincipalinvestigator(String principalinvestigator) {
		this.principalinvestigator = principalinvestigator;
	}
	/**
	 * @return Returns the protocolcoordinator.
	 */
	public String getProtocolcoordinator() {
		return protocolcoordinator;
	}
	/**
	 * @param protocolcoordinator The protocolcoordinator to set.
	 */
	public void setProtocolcoordinator(String protocolcoordinator) {
		this.protocolcoordinator = protocolcoordinator;
	}
	/**
	 * @return Returns the shorttitle.
	 */
	public String getShorttitle() {
		return shorttitle;
	}
	/**
	 * @param shorttitle The shorttitle to set.
	 */
	public void setShorttitle(String shorttitle) {
		this.shorttitle = shorttitle;
	}
	/**
	 * @return Returns the startDate.
	 */
	public java.util.Date getStartDate() {
		return startDate;
	}
	/**
	 * @param startDate The startDate to set.
	 */
	public void setStartDate(java.util.Date startDate) {
		this.startDate = startDate;
	}
	/**
	 * @return Returns the title.
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title The title to set.
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @param operation The operation to set.
	 */
	public void setOperation(String operation) {
		this.operation = operation;
	}
    /**
     * Returns the identifier assigned to ApplicationUser.
     * @return int representing the id assigned to ApplicationUser.
     * @see #setIdentifier(int)
     * */
    public long getIdentifier()
    {
        return (this.identifier);
    }

    /**
     * Sets an id for the ApplicationUser.
     * @param identifier id to be assigned to the ApplicationUser.
     * @see #getIdentifier()
     * */
    public void setIdentifier(long identifier)
    {
        this.identifier = identifier;
    }
    
    
    
    /**
     * Returns the id assigned to form bean
     */
    public int getFormId()
    {
        return Constants.DISTRIBUTIONPROTOCOL_FORM_ID ;
    }
    
    /**
     * Resets the values of all the fields.
     * This method defined in ActionForm is overridden in this class.
     */
    public void reset(ActionMapping mapping, HttpServletRequest request)
    {
        reset();
    }
    
    /**
     * Resets the values of all the fields.
     * Is called by the overridden reset method defined in ActionForm.  
     * */
    private void reset()
    {
    
    }

    /**
     * @return Returns the activityStatus.
     */
    public String getActivityStatus()
    {
        return activityStatus;
    }
    /**
     * @param activityStatus The activityStatus to set.
     */
    public void setActivityStatus(String activityStatus)
    {
        this.activityStatus = activityStatus;
    }
    /**
     * Checks the operation to be performed is add operation.
     * @return Returns true if operation is equal to "add", else it returns false
     * */
    public boolean isAddOperation()
    {
        return(getOperation().equals(Constants.ADD));
    }
    
    /**
     * Returns the operation(Add/Edit) to be performed.
     * @return Returns the operation.
     */
    public String getOperation()
    {
        return operation;
    }  
    
    /**
    * Overrides the validate method of ActionForm.
    * */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
    {
        ActionErrors errors = new ActionErrors();
        Validator validator = new Validator();
        try
        {
        }
        catch(Exception excp)
        {
        }
        return errors;
     }
    
    public void setSystemIdentifier(long l)
    {
    	setIdentifier(l);
    }
    public long getSystemIdentifier()
    {
    	return getIdentifier();
    }
    
}