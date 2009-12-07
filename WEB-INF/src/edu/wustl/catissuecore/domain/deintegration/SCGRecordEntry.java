/**
 *
 */

package edu.wustl.catissuecore.domain.deintegration;

import edu.common.dynamicextensions.domain.integration.AbstractRecordEntry;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.common.actionForm.IValueObject;
import edu.wustl.common.exception.AssignDataException;

/**
 * @author deepali_ahirrao
 * @hibernate.class table="CATISSUE_SCG_REC_NTRY"
 */
public class SCGRecordEntry extends AbstractRecordEntry
{

	/*
	 *
	 */
	private static final long serialVersionUID = 1234567890L;
	/**
	 *
	 */
	protected SpecimenCollectionGroup specimenCollectionGroup;

	public SpecimenCollectionGroup getSpecimenCollectionGroup()
	{
		return specimenCollectionGroup;
	}

	public void setSpecimenCollectionGroup(SpecimenCollectionGroup specimenCollectionGroup)
	{
		this.specimenCollectionGroup = specimenCollectionGroup;
	}

	@Override
	public void setAllValues(IValueObject arg0) throws AssignDataException
	{
		// TODO Auto-generated method stub

	}

}
