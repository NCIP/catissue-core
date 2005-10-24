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
import java.util.Set;

import edu.wustl.catissuecore.dao.DAO;
import edu.wustl.catissuecore.domain.AbstractDomainObject;
import edu.wustl.catissuecore.domain.ExternalIdentifier;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.security.SecurityManager;
import edu.wustl.common.security.exceptions.SMException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.logger.Logger;

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
	    Set protectionObjects = new HashSet();
		Specimen specimen = (Specimen)obj;
		
		specimen.setSpecimenCollectionGroup(null);
		
		
        //Load & set the Parent Specimen of this specimen
		Object specimenObj = dao.retrieve(Specimen.class.getName(), specimen.getParentSpecimen().getSystemIdentifier());
		if(specimenObj!=null)
		{
			//Setting the Biohazard Collection
			Specimen parentSpecimen = (Specimen)specimenObj;
			
			// check for closed ParentSpecimen
			checkStatus(dao,parentSpecimen, "Parent Specimen" );
			
			specimen.setParentSpecimen(parentSpecimen);
			specimen.setSpecimenCharacteristics(parentSpecimen.getSpecimenCharacteristics());
			specimen.setSpecimenCollectionGroup(parentSpecimen.getSpecimenCollectionGroup());
		}

		//Load & set Storage Container
		Object storageContainerObj = dao.retrieve(StorageContainer.class.getName(), specimen.getStorageContainer().getSystemIdentifier());
		if(storageContainerObj!=null)
		{
			StorageContainer container = (StorageContainer)storageContainerObj;
			
			// check for closed Storage Container
			checkStatus(dao, container, "Storage Container" );
			
			specimen.setStorageContainer(container);
		}

		specimen.setActivityStatus(Constants.ACTIVITY_STATUS_ACTIVE);
		dao.insert(specimen,sessionDataBean, true,true);
		protectionObjects.add(specimen);
		
		if(specimen.getSpecimenCharacteristics()!=null)
		{
		    protectionObjects.add(specimen.getSpecimenCharacteristics());
		}
		
		//Setting the External Identifier Collection
		Collection externalIdentifierCollection = specimen.getExternalIdentifierCollection();
		if(externalIdentifierCollection != null)
		{
			Iterator it = externalIdentifierCollection.iterator();
			
			while(it.hasNext())
			{
				ExternalIdentifier exId = (ExternalIdentifier)it.next();
				exId.setSpecimen(specimen);
				dao.insert(exId,sessionDataBean, true,true);
//				protectionObjects.add(exId);
			}
		}
		
//		if(parentSpecimen != null)
//		{
//			Set set = new HashSet();
//			
//			Collection biohazardCollection = parentSpecimen.getBiohazardCollection();
//			if(biohazardCollection != null)
//			{
//				Iterator it = biohazardCollection.iterator();
//				while(it.hasNext())
//				{
//					Biohazard hazard = (Biohazard)it.next();
//					set.add(hazard);
//				}
//			}
//			specimen.setBiohazardCollection(set);
//		}
		
		//Inserting data for Authorization
		try
        {
            SecurityManager.getInstance(this.getClass()).insertAuthorizationData(null,protectionObjects,getDynamicGroups(specimen));
        }
        catch (SMException e)
        {
            Logger.out.error("Exception in Authorization: "+e.getMessage(),e);
        }
	}
	
	public String[] getDynamicGroups(AbstractDomainObject obj)
	{
        String[] dynamicGroups=null;
        Specimen specimen = (Specimen)obj;
        dynamicGroups = new String[1];
        
        try
        {
            dynamicGroups[0] = SecurityManager.getInstance(this.getClass()).getProtectionGroupByName(specimen.getParentSpecimen(),Constants.getCollectionProtocolPGName(null));
        }
        catch (SMException e)
        {
            Logger.out.error("Exception in Authorization: "+e.getMessage(),e);
        }
        Logger.out.debug("Dynamic Group name: "+dynamicGroups[0]);
        return dynamicGroups;
	}
}