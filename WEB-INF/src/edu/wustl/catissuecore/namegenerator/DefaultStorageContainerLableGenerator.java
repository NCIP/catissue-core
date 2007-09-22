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
public class DefaultStorageContainerLableGenerator implements LabelGenerator
{
	/**
	 * Current label 
	 */
	protected Long currentLable ;
	
	/**
	 * Default Constructor
	 */
	public DefaultStorageContainerLableGenerator()
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
			
			currentLable = new StorageContainerBizLogic().getNextContainerNumber();
		}
		catch (DAOException daoException) 
		{
			daoException.printStackTrace();
			
		}
		
	}


	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.namegenerator.LabelGenerator#setLabel(edu.wustl.common.domain.AbstractDomainObject)
	 */
	public void setLabel(AbstractDomainObject obj )
	{
		StorageContainer objStorageContainer = (StorageContainer)obj;
		currentLable= currentLable+1;
		String containerName = "";
		String maxSiteName = objStorageContainer.getSite().getName();
		String maxTypeName =  objStorageContainer.getStorageType().getName();
		if (maxSiteName.length() > 40)
		{
			maxSiteName = maxSiteName.substring(0, 39);
		}
		if (maxTypeName.length() > 40)
		{
			maxTypeName = maxTypeName.substring(0, 39);
		}

		containerName = maxSiteName + "_" + maxTypeName + "_" + String.valueOf(currentLable);
		
		objStorageContainer.setName(containerName);	
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.namegenerator.LabelGenerator#setLabel(java.util.List)
	 */
	public void setLabel(List<AbstractDomainObject> storageContainerList) {
		
		for(int i=0; i< storageContainerList.size(); i++)
		{
			StorageContainer objStorageContainer = (StorageContainer)storageContainerList.get(i);
			setLabel(objStorageContainer);
			
		}	
		
	}	
}
