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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import edu.wustl.catissuecore.dao.DAO;
import edu.wustl.catissuecore.domain.Biohazard;
import edu.wustl.catissuecore.domain.ExternalIdentifier;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.dbManager.DAOException;

/**
 * CreateSpecimenHDAO is used to add new specimen information into the database using Hibernate.
 * @author aniruddha_phadnis
 */
public class CreateSpecimenBizLogic extends DefaultBizLogic
{
	/**
     * Saves the storageType object in the database.
	 * @param obj The storageType object to be saved.
	 * @param session The session in which the object is saved.
	 * @throws DAOException 
     */
	protected void insert(Object obj, DAO dao, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException
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

		dao.insert(specimen.getSpecimenCharacteristics(),sessionDataBean, true,true);
		specimen.setActivityStatus(Constants.ACTIVITY_STATUS_ACTIVE);
		dao.insert(specimen,sessionDataBean, true,true);
		
		//Setting the External Identifier Collection
		Collection externalIdentifierCollection = specimen.getExternalIdentifierCollection();
		if(externalIdentifierCollection != null && externalIdentifierCollection.size() > 0)
		{
			Iterator it = externalIdentifierCollection.iterator();
			
			while(it.hasNext())
			{
				ExternalIdentifier exId = (ExternalIdentifier)it.next();
				exId.setSpecimen(specimen);
				dao.insert(exId,sessionDataBean, true,true);
			}
		}
		
		//Setting the Biohazard Collection
		Specimen parentSpecimen = null;
		
		List parentSpecimenList = dao.retrieve(Specimen.class.getName(),"systemIdentifier",specimen.getParentSpecimen().getSystemIdentifier());
		
		if(parentSpecimenList!=null && parentSpecimenList.size()!=0)
		{
			parentSpecimen = (Specimen)parentSpecimenList.get(0);
		}
		
		if(parentSpecimen != null)
		{
			Set set = new HashSet();
			
			Collection biohazardCollection = parentSpecimen.getBiohazardCollection();
			if(biohazardCollection != null && biohazardCollection.size() > 0)
			{
				Iterator it = biohazardCollection.iterator();
	
				while(it.hasNext())
				{
					Biohazard hazard = (Biohazard)it.next();
					Object bioObj = dao.retrieve(Biohazard.class.getName(), hazard.getSystemIdentifier());
					if(bioObj!=null)
					{
						Biohazard hazardObj = (Biohazard)bioObj;
						set.add(hazardObj);
					}
				}
			}
			
			specimen.setBiohazardCollection(set);
		}
	}
	
	/**
     * Updates the persistent object in the database.
     * @param session The session in which the object is saved.
     * @param obj The object to be updated.
     * @throws DAOException 
     */
    public void update(Object obj) throws DAOException
    {
    }
}