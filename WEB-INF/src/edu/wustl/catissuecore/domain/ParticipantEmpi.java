/**
 * This calss is persistence class for table catissue_participant_Empi
 * this table stores empi_id for the participant added from Dana farber web services
 */
package edu.wustl.catissuecore.domain;

import edu.wustl.common.actionForm.IValueObject;
import edu.wustl.common.bizlogic.IActivityStatus;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.AssignDataException;

/**
 * @author satishchandra_pal
 *
 */
public class ParticipantEmpi extends AbstractDomainObject implements java.io.Serializable, IActivityStatus
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3314236860766272937L;

	protected Long id;
	protected Long empi_id;
	protected boolean isSpecialUpdate = false;
	
	
	

	/**
	 * @return the isSpecialUpdate
	 */
	public boolean isSpecialUpdate() {
		return isSpecialUpdate;
	}



	/**
	 * @param isSpecialUpdate the isSpecialUpdate to set
	 */
	public void setSpecialUpdate(boolean isSpecialUpdate) {
		this.isSpecialUpdate = isSpecialUpdate;
	}



	@Override
	public void setAllValues(IValueObject arg0) throws AssignDataException {
		// TODO Auto-generated method stub
		
	}

	

	public String getActivityStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setActivityStatus(String arg0) {
		// TODO Auto-generated method stub
		
	}



	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}



	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}



	/**
	 * @return the empi_id
	 */
	public Long getEmpi_id() {
		return empi_id;
	}



	/**
	 * @param empi_id the empi_id to set
	 */
	public void setEmpi_id(Long empi_id) {
		this.empi_id = empi_id;
	}

}
