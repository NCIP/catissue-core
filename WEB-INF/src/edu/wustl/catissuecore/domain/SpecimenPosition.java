/**
 * 
 */
package edu.wustl.catissuecore.domain;

import edu.wustl.common.actionForm.IValueObject;
import edu.wustl.common.exception.AssignDataException;


/**
 * @author ashish_gupta
 *
 */
public class SpecimenPosition extends AbstractPosition
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -490240810474768321L;
	protected Specimen specimen;
	protected StorageContainer storageContainer;
	
	/**
	 * @return the specimen
	 */
	public Specimen getSpecimen()
	{
		return specimen;
	}
	
	/**
	 * @param specimen the specimen to set
	 */
	public void setSpecimen(Specimen specimen)
	{
		this.specimen = specimen;
	}
	
	/**
	 * @return the storageContainer
	 */
	public StorageContainer getStorageContainer()
	{
		return storageContainer;
	}
	
	/**
	 * @param storageContainer the storageContainer to set
	 */
	public void setStorageContainer(StorageContainer storageContainer)
	{
		this.storageContainer = storageContainer;
	}

	@Override
	public void setAllValues(IValueObject arg0) throws AssignDataException
	{
		// TODO Auto-generated method stub
		
	}
	
}
