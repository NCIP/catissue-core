package edu.wustl.catissuecore.actionForm;

import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;

public class CPSearchForm extends AbstractActionForm
{

	Long cpId = null;
	Long participantId = null;
	
	public Long getParticipantId()
	{
		return participantId;
	}


	public void setParticipantId(Long participantId)
	{
		this.participantId = participantId;
	}


	/**
     * Returns the identifier assigned to form bean.
     * @return The identifier assigned to form bean.
     */
    public int getFormId()
    {
        return 0;
    }
    
    
    /**
     * This method Copies the data from an Specimen object to a AliquotForm object.
     * @param abstractDomain An object of Specimen class.  
     */
    public void setAllValues(AbstractDomainObject abstractDomain)
    {
    }
    
    /**
     * This method resets the form fields.
     */
    public void reset()
    {
    }


	public Long getCpId()
	{
		return cpId;
	}


	public void setCpId(Long cpId)
	{
		this.cpId = cpId;
	}
}
