package edu.wustl.catissuecore.actionForm;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;

/****
 * @author srikalyan
 */
public class GSIDBatchUpdateForm extends AbstractActionForm  {
	private boolean force;
	private boolean locked;
	private String currentUser;
	private String lastProcessedLabel;
	private int percentage;
	private Exception lastException;
	private long unassignedSpecimenCount;	

	public boolean isForce() {
		return force;
	}

	public void setForce(boolean force) {
		this.force = force;
	}	

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	public String getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(String currentUser) {
		this.currentUser = currentUser;
	}

	public String getLastProcessedLabel() {
		return lastProcessedLabel;
	}

	public void setLastProcessedLabel(String lastProcessedLabel) {
		this.lastProcessedLabel = lastProcessedLabel;
	}

	public int getPercentage() {
		return percentage;
	}

	public void setPercentage(int percentage) {
		this.percentage = percentage;
	}	

	public Exception getLastException() {
		return lastException;
	}

	public void setLastException(Exception lastException) {
		this.lastException = lastException;
	}

	public long getUnassignedSpecimenCount() {
		return unassignedSpecimenCount;
	}

	public void setUnassignedSpecimenCount(long unassignedSpecimenCount) {
		this.unassignedSpecimenCount = unassignedSpecimenCount;
	}

	public void setAllValues(AbstractDomainObject arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getFormId() {
		return Constants.GSID_BATCH_UPDATE_FORM_ID;
	}

	@Override
	protected void reset() {
		force=false;		
	}

	@Override
	public void setAddNewObjectIdentifier(String arg0, Long arg1) {
		// TODO Auto-generated method stub
		
	}	
}
