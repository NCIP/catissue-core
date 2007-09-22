package edu.wustl.catissuecore.namegenerator;

import java.util.List;

import edu.wustl.catissuecore.bizlogic.StorageContainerBizLogic;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.dbManager.DAOException;


/**
 * DefaultSpecimenLabelGenerator is a class which contains the default 
 * implementations AbstractSpecimenGenerator classe.
 * @author virender_mehta
 */
public class DefaultStorageContainerBarcodeGenerator implements BarcodeGenerator
{
	/**
	 * Current label 
	 */
	protected Long currentBarcode ;
	
	/**
	 * Default Constructor
	 */
	public DefaultStorageContainerBarcodeGenerator()
	{
		super();
		init();
	}
	
	/**
	 * This is a init() function it is called from the default constructor of Base class.When getInstance of base class
	 * called then this init function will be called.
	 * This method will first check the Datatbase Name and then set function name that will convert
	 * lable from int to String
	 */
	protected void init()
	{
		try 
		{
			
			currentBarcode = new StorageContainerBizLogic().getNextContainerNumber();
		}
		catch (DAOException daoException) 
		{
			daoException.printStackTrace();
			
		}
		
	}


	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.namegenerator.LabelGenerator#setLabel(edu.wustl.common.domain.AbstractDomainObject)
	 */
	public void setBarcode(AbstractDomainObject obj )
	{
		StorageContainer objStorageContainer = (StorageContainer)obj;
		//TODO :Write a logic to generate barcode.
		String barcode = "";
		objStorageContainer.setBarcode(barcode);

		
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.namegenerator.LabelGenerator#setLabel(java.util.List)
	 */
	public void setBarcode(List<AbstractDomainObject> storageContainerList) {
		
		for(int i=0; i< storageContainerList.size(); i++)
		{
			StorageContainer objStorageContainer = (StorageContainer)storageContainerList.get(i);
			setBarcode(objStorageContainer);
			
		}	
		
	}	
}
