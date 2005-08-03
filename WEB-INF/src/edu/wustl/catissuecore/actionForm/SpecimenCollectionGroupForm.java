/**
 * <p>Title: SpecimenCollectionGroupForm Class>
 * <p>Description:  SpecimenCollectionGroupForm Class is used to encapsulate 
 * all the request parameters passed from New SpecimenCollectionGroup webpage. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Ajay Sharma
 * @version 1.00
 */

package edu.wustl.catissuecore.actionForm;
import edu.wustl.catissuecore.domain.AbstractDomainObject;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;

/**
 * SpecimenCollectionGroupForm Class is used to encapsulate 
 * all the request parameters passed from New SpecimenCollectionGroup webpage.
 * @author ajay_sharma
 */
public class SpecimenCollectionGroupForm extends AbstractActionForm
{
    private long systemIdentifier;
    
	private String clinicalDiagnosis = "";
    
	private String clinicalStatus;
	
	private String activityStatus = "";
	
	private String surgicalPathologyNumber = "";
	private long participantsMedicalIdentifierId;
	
	/**
	 * An id which refers to the site of the container if it is parent container.
	 */
	private long siteId;
	
	private long  collectionProtocolId;
	private long collectionProtocolEventId;
		
	private String radioProperty;
	
	private long participantId;
	private String protocolParticipantIdentifier;
	
	/**
     * @return Returns the systemIdentifier.
     */
    public long getSystemIdentifier()
    {
	    return systemIdentifier;
    }
    
    /**
    * @param systemIdentifier The systemIdentifier to set.
    */
    public void setSystemIdentifier(long systemIdentifier)
    {
	    this.systemIdentifier = systemIdentifier;
    }    
    
    /**
     * @return Returns the clinicalDiagnosis.
     */
    public String getClinicalDiagnosis()
    {
        return clinicalDiagnosis;
    }
    /**
     * @param clinicalDiagnosis The clinicalDiagnosis to set.
     */
    public void setClinicalDiagnosis(String cinicalDiagnosis)
    {
        this.clinicalDiagnosis = cinicalDiagnosis;
    }
           
    /**
     * @return Returns the surgicalPathologyNumber.
     */
    public String getSurgicalPathologyNumber()
    {
        return surgicalPathologyNumber;
    }
    /**
     * @param surgicalPathologyNumber The surgicalPathologyNumber to set.
     */
    public void setSurgicalPathologyNumber(String surgicalPathologyNumber)
    {
        this.surgicalPathologyNumber = surgicalPathologyNumber;
    }
  
		
	/**
	 * @return
	 */
	public long getParticipantId() {
		return participantId;
	}

	/**
	 * @param participantId
	 */
	public void setParticipantId(long participantId) {
		this.participantId = participantId;
	}

	/**
	 * @return
	 */
	public String getRadioProperty() {
		return radioProperty;
	}

	/**
	 * @param radioProperty
	 */
	public void setRadioProperty(String radioProperty) {
		this.radioProperty = radioProperty;
	}

	/**
	   * This function Copies the data from an storage type object to a StorageTypeForm object.
	   * @param storageType A StorageType object containing the information about storage type of the container.  
	   */
	  public void setAllValues(AbstractDomainObject abstractDomain)
	  {
		  try
		  {
			  SpecimenCollectionGroup specimenCollectionGroup = (SpecimenCollectionGroup) abstractDomain;
        	  
		  }
		  catch (Exception excp)
		  {
			  excp.printStackTrace();

		  }
	  }
	  
	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.actionForm.AbstractActionForm#getFormId()
	 */
	public int getFormId() {
		// TODO Auto-generated method stub
		return 0;
	}
	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.actionForm.AbstractActionForm#isAddOperation()
	 */
	public boolean isAddOperation() {
		// TODO Auto-generated method stub
		return false;
	}
	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.actionForm.AbstractActionForm#getActivityStatus()
	 */
	public String getActivityStatus() {
		// TODO Auto-generated method stub
		return null;
	}
	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.actionForm.AbstractActionForm#setActivityStatus(java.lang.String)
	 */
	public void setActivityStatus(String activityStatus) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @return
	 */
	public long getSiteId() {
		return siteId;
	}

	/**
	 * @param siteId
	 */
	public void setSiteId(long siteId) {
		this.siteId = siteId;
	}

	/**
	 * @return
	 */
	public String getClinicalStatus() {
		return clinicalStatus;
	}

	/**
	 * @param clinicalStatus
	 */
	public void setClinicalStatus(String clinicalStatus) {
		this.clinicalStatus = clinicalStatus;
	}

	/**
	 * @return
	 */
	public long getCollectionProtocolEventId() {
		return collectionProtocolEventId;
	}

	/**
	 * @param collectionProtocolEventId
	 */
	public void setCollectionProtocolEventId(long collectionProtocolEventId) {
		this.collectionProtocolEventId = collectionProtocolEventId;
	}

	/**
	 * @return
	 */
	public long getCollectionProtocolId() {
		return collectionProtocolId;
	}

	/**
	 * @param collectionProtocolId
	 */
	public void setCollectionProtocolId(long collectionProtocolId) {
		this.collectionProtocolId = collectionProtocolId;
	}

	/**
	 * @return
	 */
	public long getParticipantsMedicalIdentifierId() {
		return participantsMedicalIdentifierId;
	}

	/**
	 * @param participantsMedicalIdentifierId
	 */
	public void setParticipantsMedicalIdentifierId(long participantsMedicalIdentifierId) {
		this.participantsMedicalIdentifierId = participantsMedicalIdentifierId;
	}

	/**
	 * @return
	 */
	public String getProtocolParticipantIdentifier() {
		return protocolParticipantIdentifier;
	}

	/**
	 * @param protocolParticipantIdentifier
	 */
	public void setProtocolParticipantIdentifier(String protocolParticipantIdentifier) {
		this.protocolParticipantIdentifier = protocolParticipantIdentifier;
	}

}
