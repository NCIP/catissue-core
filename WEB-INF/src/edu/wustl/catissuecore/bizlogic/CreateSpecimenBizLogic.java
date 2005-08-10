/**
 * <p>Title: CreateSpecimenHDAO Class>
 * <p>Description:	CreateSpecimenBizLogicHDAO is used to add new specimen information into the database using Hibernate.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 * Created on Jul 21, 2005
 */

package edu.wustl.catissuecore.bizlogic;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.sf.hibernate.HibernateException;
import edu.wustl.catissuecore.dao.DAO;
import edu.wustl.catissuecore.domain.ExternalIdentifier;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.util.dbManager.DAOException;

/**
 * CreateSpecimenHDAO is used to add new specimen information into the database using Hibernate.
 * @author aniruddha_phadnis
 */
public class CreateSpecimenBizLogic extends DefaultBizLogic
{
	/**
     * Saves the storageType object in the database.
     * @param session The session in which the object is saved.
     * @param obj The storageType object to be saved.
     * @throws HibernateException Exception thrown during hibernate operations.
     * @throws DAOException 
     */
	protected void insert(DAO dao, Object obj) throws DAOException
	{
		Specimen specimen = (Specimen)obj;
		
		specimen.setSpecimenCollectionGroup(null);
		
        //Load & set the Parent Specimen of this specimen
		List list = dao.retrieve(Specimen.class.getName(), "systemIdentifier", specimen.getParentSpecimen().getSystemIdentifier());
		
		if(list!=null && list.size()>0)
		{
			Specimen parentSpecimen = (Specimen)list.get(0);
			specimen.setParentSpecimen(parentSpecimen);
			specimen.setSpecimenCharacteristics(parentSpecimen.getSpecimenCharacteristics());
			//parentSpecimen.getChildrenSpecimen().add(specimen);
		}

		//Load & set Storage Container
		List scList = dao.retrieve(StorageContainer.class.getName(), "systemIdentifier", specimen.getStorageContainer().getSystemIdentifier());
		
		if(scList!=null && scList.size()!=0)
		{
			StorageContainer container = (StorageContainer)scList.get(0);
			specimen.setStorageContainer(container);
		}

		dao.insert(specimen.getSpecimenCharacteristics());
		specimen.setActivityStatus(Constants.ACTIVITY_STATUS_ACTIVE);
		dao.insert(specimen);
		
		Collection externalIdentifierCollection = specimen.getExternalIdentifierCollection();
		if(externalIdentifierCollection != null && externalIdentifierCollection.size() > 0)
		{
			Iterator it = externalIdentifierCollection.iterator();
			
			while(it.hasNext())
			{
				ExternalIdentifier exId = (ExternalIdentifier)it.next();
				exId.setSpecimen(specimen);
				dao.insert(exId);
			}
		}
	}
	
	/**
     * Updates the persistent object in the database.
     * @param session The session in which the object is saved.
     * @param obj The object to be updated.
     * @throws HibernateException Exception thrown during hibernate operations.
     * @throws DAOException 
     */
    public void update(Object obj) throws DAOException
    {
    }
}