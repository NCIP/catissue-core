/**
 *
 */

package edu.wustl.catissuecore.actionForm;

import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;

/**
 * @author suhas_khot
 *
 */
public class DisplaySPPEventForm extends AbstractActionForm
{

	private static final long serialVersionUID = 1L;

	/**
	 * logger Logger - Generic logger.
	 */
	private String specimenId;

	/**
	 * @return the specimenId
	 */
	public String getSpecimenId()
	{
		return specimenId;
	}

	/**
	 * @param specimenId the specimenId to set
	 */
	public void setSpecimenId(String specimenId)
	{
		this.specimenId = specimenId;
	}

	private String scgId;

	/**
	 * Gets the scg id.
	 *
	 * @return the scg id
	 */
	public String getScgId() {
		return scgId;
	}

	/**
	 * Sets the scg id.
	 *
	 * @param scgId the new scg id
	 */
	public void setScgId(String scgId) {
		this.scgId = scgId;
	}

	private String sppName;

	/**
	 * Gets the spp name.
	 *
	 * @return the spp name
	 */
	public String getSppName() {
		return sppName;
	}

	/**
	 * Sets the spp name.
	 *
	 * @param sppName the new spp name
	 */
	public void setSppName(String sppName) {
		this.sppName = sppName;
	}

	/**
	 * No argument constructor for UserForm class
	 */
	public DisplaySPPEventForm()
	{
		//      reset();
	}

	/**
	 * Populates all the fields from the domain object to the form bean.
	 * @param abstractDomain An AbstractDomain Object
	 */
	public void setAllValues(AbstractDomainObject abstractDomain)
	{
	}

	/* (non-Javadoc)
	 * @see edu.wustl.common.actionForm.AbstractActionForm#getFormId()
	 */
	@Override
	public int getFormId()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected void reset()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void setAddNewObjectIdentifier(String arg0, Long arg1)
	{
		// TODO Auto-generated method stub

	}

}
