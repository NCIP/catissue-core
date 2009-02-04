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
import java.util.Map;
import java.util.Set;

import edu.wustl.catissuecore.domain.Biohazard;
import edu.wustl.catissuecore.domain.ConsentTierStatus;
import edu.wustl.catissuecore.domain.ExternalIdentifier;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenEventParameters;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.util.ApiSearchUtil;
import edu.wustl.catissuecore.util.StorageContainerUtil;
import edu.wustl.catissuecore.util.WithdrawConsentUtil;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.dao.DAO;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.security.SecurityManager;
import edu.wustl.common.security.exceptions.SMException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.logger.Logger;

/**
 * CreateSpecimenHDAO is used to add new specimen information into the database using Hibernate.
 * @author aniruddha_phadnis
 */
public class CreateSpecimenBizLogic extends DefaultBizLogic
{
	
	
	protected boolean validate(Object obj, DAO dao, String operation) throws DAOException
	{
		Specimen specimen = (Specimen) obj;
		
		if (specimen.getStorageContainer() != null
				&& (specimen.getStorageContainer().getId() == null && specimen.getStorageContainer().getName() == null))
		{
			String message = ApplicationProperties.getValue("specimen.storageContainer");
			throw new DAOException(ApplicationProperties.getValue("errors.invalid", message));
		}
		
		if (specimen.getStorageContainer() != null && specimen.getStorageContainer().getName() != null)
		{
			StorageContainer storageContainerObj = specimen.getStorageContainer();
			String sourceObjectName = StorageContainer.class.getName();
			String[] selectColumnName = {"id"};
			String[] whereColumnName = {"name"};
			String[] whereColumnCondition = {"="};
			Object[] whereColumnValue = {specimen.getStorageContainer().getName()};
			String joinCondition = null;

			List list = dao.retrieve(sourceObjectName, selectColumnName, whereColumnName, whereColumnCondition, whereColumnValue, joinCondition);

			if (!list.isEmpty())
			{
				storageContainerObj.setId((Long) list.get(0));
				specimen.setStorageContainer(storageContainerObj);
			}
			else
			{
				String message = ApplicationProperties.getValue("specimen.storageContainer");
				throw new DAOException(ApplicationProperties.getValue("errors.invalid", message));
			}
		}
		/**
		 * This method gives first valid storage position to a specimen if it is not given. 
		 * If storage position is given it validates the storage position 
		 **/
		StorageContainerUtil.validateStorageLocationForSpecimen(specimen);
		return true;
	}


	/**
	 * Saves the storageType object in the database.
	 * @param obj The storageType object to be saved.
	 * @param session The session in which the object is saved.
	 * @throws DAOException 
	 */
	protected void insert(Object obj, DAO dao, SessionDataBean sessionDataBean)
			throws DAOException, UserNotAuthorizedException
	{
		Set protectionObjects = new HashSet();
		Specimen specimen = (Specimen) obj;
		
		/**
		 * Start: Change for API Search   --- Jitendra 06/10/2006
		 * In Case of Api Search, previoulsy it was failing since there was default class level initialization 
		 * on domain object. For example in User object, it was initialized as protected String lastName=""; 
		 * So we removed default class level initialization on domain object and are initializing in method
		 * setAllValues() of domain object. But in case of Api Search, default values will not get set 
		 * since setAllValues() method of domainObject will not get called. To avoid null pointer exception,
		 * we are setting the default values same as we were setting in setAllValues() method of domainObject.
		 */
		ApiSearchUtil.setSpecimenDefault(specimen);
		//End:-Change for API Search

		specimen.setSpecimenCollectionGroup(null);

		//Load & set the Parent Specimen of this specimen
		Object specimenObj = dao.retrieve(Specimen.class.getName(), specimen.getParentSpecimen()
				.getId());
		if (specimenObj != null)
		{
			//Setting the Biohazard Collection
			Specimen parentSpecimen = (Specimen) specimenObj;

			// check for closed ParentSpecimen
			checkStatus(dao, parentSpecimen, "Parent Specimen");
			
			//Mandar:-18-Jan-07 Get parent consent status : start
			WithdrawConsentUtil.setConsentsFromParent(specimen, parentSpecimen);
			//Mandar:-18-Jan-07 Get parent consent status : end
			specimen.setParentSpecimen(parentSpecimen);
			specimen.setSpecimenCharacteristics(parentSpecimen.getSpecimenCharacteristics());
			specimen.setSpecimenCollectionGroup(parentSpecimen.getSpecimenCollectionGroup());
			// set event parameters from parent specimen - added by Ashwin for bug id# 2476
			specimen.setSpecimenEventCollection(populateDeriveSpecimenEventCollection(parentSpecimen,specimen));
			specimen.setPathologicalStatus(parentSpecimen.getPathologicalStatus());
			if(parentSpecimen != null)
			{
				Set set = new HashSet();
				
				Collection biohazardCollection = parentSpecimen.getBiohazardCollection();
				if(biohazardCollection != null)
				{
					Iterator it = biohazardCollection.iterator();
					while(it.hasNext())
					{
						Biohazard hazard = (Biohazard)it.next();
						set.add(hazard);
					}
				}
				specimen.setBiohazardCollection(set);
			}
		}
		try
		{
			if (specimen.getStorageContainer() != null)
			{
				if (specimen.getStorageContainer() != null && specimen.getStorageContainer().getName() != null)				
				{			
					StorageContainer storageContainerObj = specimen.getStorageContainer();			
					String sourceObjectName = StorageContainer.class.getName();
					String[] selectColumnName = {"id"};
					String[] whereColumnName = {"name"};
					String[] whereColumnCondition = {"="};
					Object[] whereColumnValue = {specimen.getStorageContainer().getName()};
					String joinCondition = null;

					List list = dao.retrieve(sourceObjectName, selectColumnName, whereColumnName, whereColumnCondition, whereColumnValue, joinCondition);
					
					if (!list.isEmpty())
					{
						storageContainerObj.setId((Long) list.get(0));
						specimen.setStorageContainer(storageContainerObj);
					}
					else
					{
						String message = ApplicationProperties.getValue("specimen.storageContainer");
						throw new DAOException(ApplicationProperties.getValue("errors.invalid", message));
					}
				}
				else
				{
				
				
					//Load & set Storage Container
	//				Object storageContainerObj = dao.retrieve(StorageContainer.class.getName(),
	//						specimen.getStorageContainer().getId());
					StorageContainer storageContainerObj = new StorageContainer();
					storageContainerObj.setId(specimen.getStorageContainer().getId());
					String sourceObjectName = StorageContainer.class.getName();
					String[] selectColumnName = {"name"};
					String[] whereColumnName = {"id"}; //"storageContainer."+Constants.SYSTEM_IDENTIFIER
					String[] whereColumnCondition = {"="};
					Object[] whereColumnValue = {specimen.getStorageContainer().getId()};
					String joinCondition = null;
	
					List list = dao.retrieve(sourceObjectName, selectColumnName, whereColumnName, whereColumnCondition, whereColumnValue, joinCondition);
	
					if (!list.isEmpty())
					{
						storageContainerObj.setName((String)list.get(0));
						specimen.setStorageContainer(storageContainerObj);
					}
					else
					{
						String message = ApplicationProperties.getValue("specimen.storageContainer");
						throw new DAOException(ApplicationProperties.getValue("errors.invalid", message));
					}
				}
				
				StorageContainer storageContainerObj = specimen.getStorageContainer();
//				if (storageContainerObj != null)
//				{
//					StorageContainer container = (StorageContainer) storageContainerObj;

					// check for closed Storage Container
					checkStatus(dao, storageContainerObj, "Storage Container");

					StorageContainerBizLogic storageContainerBizLogic = (StorageContainerBizLogic) BizLogicFactory
							.getInstance().getBizLogic(Constants.STORAGE_CONTAINER_FORM_ID);
					NewSpecimenBizLogic newSpecimenBizLogic = (NewSpecimenBizLogic) BizLogicFactory.getInstance().getBizLogic(Constants.NEW_SPECIMEN_FORM_ID);
					// --- check for all validations on the storage container.
					storageContainerBizLogic.checkContainer(dao, storageContainerObj.getId().toString(),
							specimen.getPositionDimensionOne().toString(), specimen
									.getPositionDimensionTwo().toString(), sessionDataBean,false);
					//newSpecimenBizLogic.chkContainerValidForSpecimen(storageContainerObj, specimen,dao);
					specimen.setStorageContainer(storageContainerObj);
//				}
			}
			specimen.setActivityStatus(Constants.ACTIVITY_STATUS_ACTIVE);
			specimen.setLineage(Constants.DERIVED_SPECIMEN);
			
//			Setting the External Identifier Collection
			Collection externalIdentifierCollection = specimen.getExternalIdentifierCollection();
			// -- Mandar : code to add an empty External Identifier if externalIdentifier is not added.
			if (externalIdentifierCollection==null || externalIdentifierCollection.isEmpty())
			{
				ExternalIdentifier externalIdentifierObject = new ExternalIdentifier();
				externalIdentifierObject.setSpecimen(specimen);
				externalIdentifierCollection.add(externalIdentifierObject);
			}
			Iterator it = externalIdentifierCollection.iterator();

			while (it.hasNext())
			{
				ExternalIdentifier exId = (ExternalIdentifier) it.next();
				exId.setSpecimen(specimen);
			    //	dao.insert(exId, sessionDataBean, true, true);
			    //	protectionObjects.add(exId);
			}
			
			if(specimen.getAvailableQuantity().getValue().doubleValue() == 0)
			{
				specimen.setAvailable(new Boolean(false));
			}
			dao.insert(specimen, sessionDataBean, true, true);
			protectionObjects.add(specimen);

			if (specimen.getSpecimenCharacteristics() != null)
			{
				protectionObjects.add(specimen.getSpecimenCharacteristics());
			}

			

					

			//Inserting data for Authorization
			SecurityManager.getInstance(this.getClass()).insertAuthorizationData(null,
					protectionObjects, getDynamicGroups(specimen));
		}
		catch (SMException e)
		{
			throw handleSMException(e);
		}
	}

	public void postInsert(Object obj, DAO dao, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException
	{
		Specimen specimen = (Specimen) obj;
		try
		{
			if (specimen.getStorageContainer() != null)
			{
				
				Map containerMap = StorageContainerUtil.getContainerMapFromCache();
				StorageContainerUtil.deleteSinglePositionInContainerMap(specimen.getStorageContainer(), containerMap, specimen.getPositionDimensionOne().intValue(),specimen.getPositionDimensionTwo().intValue());
				
				
			}
		}
		catch (Exception e)
		{

		}

	}

	private String[] getDynamicGroups(AbstractDomainObject obj) throws SMException
	{
		Specimen specimen = (Specimen) obj;
		String[] dynamicGroups = new String[1];

		dynamicGroups[0] = SecurityManager.getInstance(this.getClass()).getProtectionGroupByName(
				specimen.getParentSpecimen(), Constants.getCollectionProtocolPGName(null));
		Logger.out.debug("Dynamic Group name: " + dynamicGroups[0]);
		return dynamicGroups;
	}
	
	// added by Ashwin for bug id# 2476 
	/**
	 * Set event parameters from parent specimen to derived specimen
	 * @param parentSpecimen specimen
	 * @return set
	 */
	private Set populateDeriveSpecimenEventCollection(Specimen parentSpecimen,Specimen deriveSpecimen)
	{
		Set deriveEventCollection = new HashSet();
		Set parentSpecimeneventCollection = (Set) parentSpecimen.getSpecimenEventCollection();
		SpecimenEventParameters specimenEventParameters = null;
		SpecimenEventParameters deriveSpecimenEventParameters = null;
		
		try
		{
			if (parentSpecimeneventCollection != null)
			{	
				for (Iterator iter = parentSpecimeneventCollection.iterator(); iter.hasNext();)
				{
					specimenEventParameters = (SpecimenEventParameters) iter.next();
					deriveSpecimenEventParameters = (SpecimenEventParameters) specimenEventParameters.clone();
					deriveSpecimenEventParameters.setId(null);
					deriveSpecimenEventParameters.setSpecimen(deriveSpecimen);
					deriveEventCollection.add(deriveSpecimenEventParameters);
				}
			}	
		}
		catch (CloneNotSupportedException exception)
		{
			exception.printStackTrace();
		}
		
		return deriveEventCollection;
	}
	
}