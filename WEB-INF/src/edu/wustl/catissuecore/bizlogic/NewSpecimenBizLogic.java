/**
 * <p>Title: NewSpecimenHDAO Class>
 * <p>Description:	NewSpecimenBizLogicHDAO is used to add new specimen information into the database using Hibernate.</p>
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
import edu.wustl.catissuecore.dao.AbstractDAO;
import edu.wustl.catissuecore.dao.DAOFactory;
import edu.wustl.catissuecore.domain.Biohazard;
import edu.wustl.catissuecore.domain.ExternalIdentifier;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.util.dbManager.DAOException;

/**
 * NewSpecimenHDAO is used to add new specimen information into the database using Hibernate.
 * @author aniruddha_phadnis
 */
public class NewSpecimenBizLogic extends DefaultBizLogic
{
	/**
     * Saves the storageType object in the database.
     * @param session The session in which the object is saved.
     * @param obj The storageType object to be saved.
     * @throws HibernateException Exception thrown during hibernate operations.
     * @throws DAOException 
     */
	public void insert(Object obj) throws DAOException 
	{
		Specimen type = (Specimen)obj;

        AbstractDAO dao = DAOFactory.getDAO(Constants.HIBERNATE_DAO);
		dao.openSession();		
	    
		//Load & set Specimen Collection Group
		List list = dao.retrieve(SpecimenCollectionGroup.class.getName(), "systemIdentifier", type.getSpecimenCollectionGroup().getSystemIdentifier());
		
		if(list!=null && list.size()!=0)
		{
			SpecimenCollectionGroup spg = (SpecimenCollectionGroup)list.get(0);
			type.setSpecimenCollectionGroup(spg);
		}
		
		//Load & set Storage Container
		List scList = dao.retrieve(StorageContainer.class.getName(), "systemIdentifier", type.getStorageContainer().getSystemIdentifier());
		
		if(list!=null && list.size()!=0)
		{
			StorageContainer container = (StorageContainer)scList.get(0);
			type.setStorageContainer(container);
		}
		
		dao.insert(type.getSpecimenCharacteristics());
		dao.insert(type);
		
		Collection externalIdentifierCollection = type.getExternalIdentifierCollection();
		if(externalIdentifierCollection != null && externalIdentifierCollection.size() > 0)
		{
			Iterator it = externalIdentifierCollection.iterator();
			
			while(it.hasNext())
			{
				ExternalIdentifier exId = (ExternalIdentifier)it.next();
				exId.setSpecimen(type);
				dao.insert(exId);
			}
		}
		
		Collection biohazardCollection = type.getBiohazardCollection();
		if(biohazardCollection != null && biohazardCollection.size() > 0)
		{
			Iterator it = biohazardCollection.iterator();
			
			while(it.hasNext())
			{
				Biohazard hazard = (Biohazard)it.next();
				hazard.getSpecimenCollection().add(type);
				dao.insert(hazard);
			}
		}
		
	    dao.closeSession();
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