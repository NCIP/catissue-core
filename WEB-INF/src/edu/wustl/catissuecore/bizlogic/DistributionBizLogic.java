/**
 * <p>Title: DistributionHDAO Class>
 * <p>Description:	DistributionHDAO is used to add distribution information into the database using Hibernate.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 * Created on Aug 23, 2005
 */

package edu.wustl.catissuecore.bizlogic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.HibernateException;
import edu.wustl.catissuecore.domain.CellSpecimen;
import edu.wustl.catissuecore.domain.DistributedItem;
import edu.wustl.catissuecore.domain.Distribution;
import edu.wustl.catissuecore.domain.DistributionProtocol;
import edu.wustl.catissuecore.domain.FluidSpecimen;
import edu.wustl.catissuecore.domain.MolecularSpecimen;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenArray;
import edu.wustl.catissuecore.domain.TissueSpecimen;
import edu.wustl.catissuecore.util.ApiSearchUtil;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.dao.AbstractDAO;
import edu.wustl.common.dao.DAO;
import edu.wustl.common.dao.DAOFactory;
import edu.wustl.common.dao.JDBCDAO;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.security.SecurityManager;
import edu.wustl.common.security.exceptions.SMException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.dbManager.HibernateMetaData;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

/**
 * DistributionHDAO is used to add distribution information into the database using Hibernate.
 * @author aniruddha_phadnis
 */
/**
 * @author kalpana_thakur
 *
 */
public class DistributionBizLogic extends DefaultBizLogic
{

	/**
	 * Saves the Distribution object in the database.
	 * @param obj The storageType object to be saved.
	 * @param session The session in which the object is saved.
	 * @throws DAOException 
	 */
	protected void insert(Object obj, DAO dao, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException
	{
		Distribution dist = (Distribution) obj;

		validateAndSetAssociateObject(dist, dao);

		Object siteObj = dao.retrieve(Site.class.getName(), dist.getToSite().getId());
		if (siteObj != null)
		{
			Site site = (Site) siteObj;
			if (!site.getActivityStatus().equals(edu.wustl.common.util.global.Constants.ACTIVITY_STATUS_ACTIVE))
			{
				throw new DAOException(ApplicationProperties.getValue("errors.distribution.closedOrDisableSite"));
			}
			dist.setToSite(site);
		}

		dao.insert(dist, sessionDataBean, Constants.IS_AUDITABLE_TRUE, Constants.IS_SECURE_UPDATE_TRUE);
		
	}

	public boolean isSpecimenArrayDistributed(Long specimenArrayId) throws DAOException
	{
		boolean distributed = false;
		JDBCDAO dao = (JDBCDAO) DAOFactory.getInstance().getDAO(Constants.JDBC_DAO);
		List list = null;
		//String queryStr = "select array.distribution_id from catissue_specimen_array array where array.identifier =" + specimenArrayId;
		String queryStr = "select item.distribution_id from catissue_distributed_item item where item.specimen_array_id =" + specimenArrayId;
		try
		{
			dao.openSession(null);
			list = dao.executeQuery(queryStr, null, false, null);
			if (list != null && !list.isEmpty())
			{
				distributed = true;
			}

			dao.closeSession();
		}
		catch (Exception ex)
		{
			throw new DAOException(ex.getMessage());
		}
		return distributed;
	}

	private Set getProtectionObjects(AbstractDomainObject obj)
	{
		Set protectionObjects = new HashSet();

		Distribution distribution = (Distribution) obj;
		protectionObjects.add(distribution);

		Iterator distributedItemIterator = distribution.getDistributedItemCollection().iterator();
		/*while (distributedItemIterator.hasNext())
		{
			DistributedItem distributedItem = (DistributedItem) distributedItemIterator.next();
			if (distributedItem.getSpecimen() != null)
				protectionObjects.add(distributedItem.getSpecimen());
		}*/
      
		return protectionObjects;
	}

	private String[] getDynamicGroups(AbstractDomainObject obj)
	{
		Distribution distribution = (Distribution) obj;
		String[] dynamicGroups = new String[1];
		dynamicGroups[0] = Constants.getDistributionProtocolPGName(distribution.getDistributionProtocol().getId());

		return dynamicGroups;
	}

	/**
	 * Updates the persistent object in the database.
	 * @param obj The object to be updated.
	 * @param session The session in which the object is saved.
	 * @throws DAOException 
	 * @throws HibernateException Exception thrown during hibernate operations.
	 */
	protected void update(DAO dao, Object obj, Object oldObj, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException
	{
		Distribution distribution = (Distribution) obj;
		dao.update(obj, sessionDataBean, Constants.IS_AUDITABLE_TRUE, Constants.IS_SECURE_UPDATE_TRUE, Constants.HAS_OBJECT_LEVEL_PRIVILEGE_FALSE);

		//Audit of Distribution.
		dao.audit(obj, oldObj, sessionDataBean, Constants.IS_AUDITABLE_TRUE);

		Distribution oldDistribution = (Distribution) oldObj;
		Collection oldDistributedItemCollection = oldDistribution.getDistributedItemCollection();

		Collection distributedItemCollection = distribution.getDistributedItemCollection();
		Iterator it = distributedItemCollection.iterator();
		while (it.hasNext())
		{
			DistributedItem item = (DistributedItem) it.next();

			/**
			 * Start: Change for API Search   --- Jitendra 06/10/2006
			 * In Case of Api Search, previoulsy it was failing since there was default class level initialization 
			 * on domain object. For example in User object, it was initialized as protected String lastName=""; 
			 * So we removed default class level initialization on domain object and are initializing in method
			 * setAllValues() of domain object. But in case of Api Search, default values will not get set 
			 * since setAllValues() method of domainObject will not get called. To avoid null pointer exception,
			 * we are setting the default values same as we were setting in setAllValues() method of domainObject.
			 */
			ApiSearchUtil.setDistributedItemDefault(item);
			//End:-  Change for API Search 

			DistributedItem oldItem = (DistributedItem) getCorrespondingOldObject(oldDistributedItemCollection, item.getId());
			//update the available quantity
			Object specimenObj = dao.retrieve(Specimen.class.getName(), item.getSpecimen().getId());
			Double previousQuantity = (Double) item.getPreviousQuantity();
			double quantity = item.getQuantity().doubleValue();
			if (previousQuantity != null)
			{
				quantity = quantity - previousQuantity.doubleValue();
			}
			boolean availability = checkAndSetAvailableQty((Specimen) specimenObj, quantity);

			if (!availability)
			{
				throw new DAOException(ApplicationProperties.getValue("errors.distribution.quantity.should.equal"));
			}
			else
			{
				if (((Specimen) specimenObj).getAvailableQuantity().doubleValue() == 0)
				{
					((Specimen) specimenObj).setIsAvailable(new Boolean(false));
				}
				dao.update(specimenObj, sessionDataBean, Constants.IS_AUDITABLE_TRUE, Constants.IS_SECURE_UPDATE_TRUE,
						Constants.HAS_OBJECT_LEVEL_PRIVILEGE_FALSE);
				//Audit of Specimen.
				//If a new specimen is distributed.
				if (oldItem == null)
				{
					Object specimenObjPrev = dao.retrieve(Specimen.class.getName(), item.getSpecimen().getId());
					dao.audit(specimenObj, specimenObjPrev, sessionDataBean, Constants.IS_AUDITABLE_TRUE);
				}
				//if a distributed specimen is updated  
				else
					dao.audit(specimenObj, oldItem.getSpecimen(), sessionDataBean, Constants.IS_AUDITABLE_TRUE);
			}
			item.setDistribution(distribution);

			dao.update(item, sessionDataBean, Constants.IS_AUDITABLE_TRUE, Constants.IS_SECURE_UPDATE_TRUE,
					Constants.HAS_OBJECT_LEVEL_PRIVILEGE_FALSE);

			//Audit of Distributed Item.
			dao.audit(item, oldItem, sessionDataBean, Constants.IS_AUDITABLE_TRUE);
		}
		//Mandar : 04-Apr-06 for updating the removed specimens start
		updateRemovedSpecimens(distributedItemCollection, oldDistributedItemCollection, dao, sessionDataBean);
		Logger.out.debug("Update Successful ...04-Apr-06");
		// Mandar : 04-Apr-06 end
	}

	private boolean checkAndSetAvailableQty(Specimen specimen, double quantity)
	{
		/**
		 * Name : Virender
		 * Reviewer: Sachin lale
		 * Calling Domain object from Proxy Object
		 */
		//		Specimen specimen = (Specimen)HibernateMetaData.getProxyObjectImpl(specimenObj);
		if (specimen instanceof TissueSpecimen)
		{
			TissueSpecimen tissueSpecimen = (TissueSpecimen) specimen;
			double availabeQty = Double.parseDouble(tissueSpecimen.getAvailableQuantity().toString());//tissueSpecimen.getAvailableQuantityInGram().doubleValue();
			Logger.out.debug("TissueAvailabeQty" + availabeQty);
			if (availabeQty <= quantity)
			{
				tissueSpecimen.setAvailableQuantity(new Double(0.0));
			}
			else
			{
				availabeQty = availabeQty - quantity;
				Logger.out.debug("TissueAvailabeQty after deduction" + availabeQty);
				tissueSpecimen.setAvailableQuantity(new Double(availabeQty));//tissueSpecimen.setAvailableQuantityInGram(new Double(availabeQty));
			}
		}
		else if (specimen instanceof CellSpecimen)
		{
			CellSpecimen cellSpecimen = (CellSpecimen) specimen;
			int availabeQty = (int) Double.parseDouble(cellSpecimen.getAvailableQuantity().toString());//cellSpecimen.getAvailableQuantityInCellCount().intValue();
			if (availabeQty <= quantity)
			{
				cellSpecimen.setAvailableQuantity(new Double(0.0));
			}
			else
			{
				availabeQty = availabeQty - (int) quantity;
				cellSpecimen.setAvailableQuantity(new Double(availabeQty));//cellSpecimen.setAvailableQuantityInCellCount(new Integer(availabeQty));
			}
		}
		else if (specimen instanceof MolecularSpecimen)
		{
			MolecularSpecimen molecularSpecimen = (MolecularSpecimen) specimen;
			double availabeQty = Double.parseDouble(molecularSpecimen.getAvailableQuantity().toString());//molecularSpecimen.getAvailableQuantityInMicrogram().doubleValue();
			if (availabeQty <= quantity)
			{
				molecularSpecimen.setAvailableQuantity(new Double(0.0));
			}
			else
			{
				availabeQty = availabeQty - quantity;
				molecularSpecimen.setAvailableQuantity(new Double(availabeQty));//molecularSpecimen.setAvailableQuantityInMicrogram(new Double(availabeQty));
			}
		}
		else if (specimen instanceof FluidSpecimen)
		{
			FluidSpecimen fluidSpecimen = (FluidSpecimen) specimen;
			double availabeQty = Double.parseDouble(fluidSpecimen.getAvailableQuantity().toString());//fluidSpecimen.getAvailableQuantityInMilliliter().doubleValue();
			if (availabeQty <= quantity)
			{
				fluidSpecimen.setAvailableQuantity(new Double(0.0));
			}
			else
			{
				availabeQty = availabeQty - quantity;
				fluidSpecimen.setAvailableQuantity(new Double(availabeQty));//fluidSpecimen.setAvailableQuantityInMilliliter(new Double(availabeQty));
			}
		}
		if (specimen.getAvailableQuantity().doubleValue() == 0)
		{
			specimen.setIsAvailable(new Boolean(false));
		}
		return true;
	}

	public void disableRelatedObjects(DAO dao, Long distributionProtocolIDArr[]) throws DAOException
	{
		List listOfSubElement = super.disableObjects(dao, Distribution.class, "distributionProtocol", "CATISSUE_DISTRIBUTION",
				"DISTRIBUTION_PROTOCOL_ID", distributionProtocolIDArr);
	}

	/**
	 * @see edu.wustl.common.bizlogic.IBizLogic#setPrivilege(DAO, String, Class, Long[], Long, String, boolean)
	 * @param dao
	 * @param privilegeName
	 * @param objectIds
	 * @param userId
	 * @param roleId
	 * @param assignToUser
	 * @throws SMException
	 * @throws DAOException
	 */
	public void assignPrivilegeToRelatedObjectsForDP(DAO dao, String privilegeName, Long[] objectIds, Long userId, String roleId,
			boolean assignToUser, boolean assignOperation) throws SMException, DAOException
	{
		List listOfSubElement = super.getRelatedObjects(dao, Distribution.class, "distributionProtocol", objectIds);
		Logger.out.debug("Distribution................" + listOfSubElement.size());
		if (!listOfSubElement.isEmpty())
		{
			Logger.out.debug("Distribution Id : ................" + listOfSubElement.get(0));
			super.setPrivilege(dao, privilegeName, Distribution.class, Utility.toLongArray(listOfSubElement), userId, roleId, assignToUser,
					assignOperation);

			assignPrivilegeToRelatedObjectsForDistribution(dao, privilegeName, Utility.toLongArray(listOfSubElement), userId, roleId, assignToUser,
					assignOperation);
		}
	}

	public void assignPrivilegeToRelatedObjectsForDistribution(DAO dao, String privilegeName, Long[] objectIds, Long userId, String roleId,
			boolean assignToUser, boolean assignOperation) throws SMException, DAOException
	{
		List listOfSubElement = super.getRelatedObjects(dao, DistributedItem.class, "distribution", objectIds);
		Logger.out.debug("Distributed Item................" + listOfSubElement.size());
		if (!listOfSubElement.isEmpty())
		{
			Logger.out.debug("Distribution Item Id : ................" + listOfSubElement.get(0));
			super.setPrivilege(dao, privilegeName, DistributedItem.class, Utility.toLongArray(listOfSubElement), userId, roleId, assignToUser,
					assignOperation);

			NewSpecimenBizLogic bizLogic = (NewSpecimenBizLogic) BizLogicFactory.getInstance().getBizLogic(Constants.NEW_SPECIMEN_FORM_ID);
			bizLogic.assignPrivilegeToRelatedObjectsForDistributedItem(dao, privilegeName, Utility.toLongArray(listOfSubElement), userId, roleId,
					assignToUser, assignOperation);
		}

	}

	/**
	 * Overriding the parent class's method to validate the enumerated attribute values
	 */
	protected boolean validate(Object obj, DAO dao, String operation) throws DAOException
	{
		Distribution distribution = (Distribution) obj;

		/**
		 * Start: Change for API Search   --- Jitendra 06/10/2006
		 * In Case of Api Search, previoulsy it was failing since there was default class level initialization 
		 * on domain object. For example in User object, it was initialized as protected String lastName=""; 
		 * So we removed default class level initialization on domain object and are initializing in method
		 * setAllValues() of domain object. But in case of Api Search, default values will not get set 
		 * since setAllValues() method of domainObject will not get called. To avoid null pointer exception,
		 * we are setting the default values same as we were setting in setAllValues() method of domainObject.
		 */
		ApiSearchUtil.setDistributionDefault(distribution);
		//End:-  Change for API Search 

		//Added By Ashish
		if (distribution == null)
		{
			throw new DAOException(ApplicationProperties.getValue("domain.object.null.err.msg", "Distribution"));
		}
		Validator validator = new Validator();
		String message = "";
		if (distribution.getDistributionProtocol() == null || distribution.getDistributionProtocol().getId() == null)
		{
			message = ApplicationProperties.getValue("distribution.protocol");
			throw new DAOException(ApplicationProperties.getValue("errors.item.required", message));
		}
		else
		{
			Object distributionProtocolObj = dao.retrieve(DistributionProtocol.class.getName(), distribution.getDistributionProtocol().getId());
			if (!((DistributionProtocol) distributionProtocolObj).getActivityStatus().equals(
					edu.wustl.common.util.global.Constants.ACTIVITY_STATUS_ACTIVE))
			{
				throw new DAOException(ApplicationProperties.getValue("errors.distribution.closedOrDisableDP"));
			}
		}

		if (distribution.getDistributedBy() == null || distribution.getDistributedBy().getId() == null)
		{
			message = ApplicationProperties.getValue("distribution.distributedBy");
			throw new DAOException(ApplicationProperties.getValue("errors.item.required", message));
		}

		//date validation 
		String errorKey = validator.validateDate(Utility.parseDateToString(distribution.getTimestamp(), Constants.DATE_PATTERN_MM_DD_YYYY), true);
		if (errorKey.trim().length() > 0)
		{
			message = ApplicationProperties.getValue("distribution.date");
			throw new DAOException(ApplicationProperties.getValue(errorKey, message));
		}

		if (distribution.getToSite() == null || distribution.getToSite().getId() == null)
		{
			message = ApplicationProperties.getValue("distribution.toSite");
			throw new DAOException(ApplicationProperties.getValue("errors.item.required", message));
		}

		//		Collection specimenArrayCollection = distribution.getSpecimenArrayCollection();
		Collection distributedItemCollection = distribution.getDistributedItemCollection();

		if ((distributedItemCollection == null || distributedItemCollection.isEmpty()))
		{
			message = ApplicationProperties.getValue("distribution.distributedItem");
			throw new DAOException(ApplicationProperties.getValue("errors.one.item.required", message));
		}

		//END

		if (operation.equals(Constants.ADD))
		{
			if (!Constants.ACTIVITY_STATUS_ACTIVE.equals(distribution.getActivityStatus()))
			{
				throw new DAOException(ApplicationProperties.getValue("activityStatus.active.errMsg"));
			}
		}
		else
		{
			if (!Validator.isEnumeratedValue(Constants.ACTIVITY_STATUS_VALUES, distribution.getActivityStatus()))
			{
				throw new DAOException(ApplicationProperties.getValue("activityStatus.errMsg"));
			}
		}

		return true;
	}

	private void validateAndSetAssociateObject(Distribution dist, DAO dao) throws DAOException
	{
		String message = "";
		Collection distributedItemCollection = dist.getDistributedItemCollection();
		if (distributedItemCollection != null && !distributedItemCollection.isEmpty())
		{
			Iterator itr = distributedItemCollection.iterator();
			while (itr.hasNext())
			{
				DistributedItem distributedItem = (DistributedItem) itr.next();
				if (distributedItem.getSpecimenArray() == null)
				{
					Specimen specimen = distributedItem.getSpecimen();
					Double quantity = distributedItem.getQuantity();
					if (specimen == null || specimen.getId() == null)
					{
						message = ApplicationProperties.getValue("errors.distribution.item.specimen");
						throw new DAOException(ApplicationProperties.getValue("errors.item.required", message));
					}
					if (quantity == null)
					{
						message = ApplicationProperties.getValue("errors.distribution.item.quantity");
						throw new DAOException(ApplicationProperties.getValue("errors.item.required", message));
					}
					Specimen specimenObj = (Specimen) dao.retrieve(Specimen.class.getName(), specimen.getId());
					

					if (specimenObj == null)
					{
						throw new DAOException(ApplicationProperties.getValue("errors.distribution.specimenNotFound"));
					}
					else if (!((Specimen) specimenObj).getActivityStatus().equals(edu.wustl.common.util.global.Constants.ACTIVITY_STATUS_ACTIVE))
					{
						throw new DAOException(ApplicationProperties.getValue("errors.distribution.closedOrDisableSpecimen"));
					}
					if (!checkAndSetAvailableQty(specimenObj, quantity))
					{
						throw new DAOException(ApplicationProperties.getValue("errors.distribution.quantity.should.equal"));
					}

					distributedItem.setSpecimen(specimenObj);
				}
				else
				{
					SpecimenArray specimenArrayObj = distributedItem.getSpecimenArray();
					specimenArrayObj = (SpecimenArray) HibernateMetaData.getProxyObjectImpl(specimenArrayObj);
					Object object = dao.retrieve(SpecimenArray.class.getName(), specimenArrayObj.getId());
					if (object == null)
					{
						throw new DAOException(ApplicationProperties.getValue("errors.distribution.specimenArrayNotFound"));
					}
					else if (!((SpecimenArray) object).getActivityStatus().equals(edu.wustl.common.util.global.Constants.ACTIVITY_STATUS_ACTIVE))
					{
						throw new DAOException(ApplicationProperties.getValue("errors.distribution.closedOrDisableSpecimenArray"));
					}
					/*else if (chkForSpecimenArrayDistributed(((SpecimenArray) object).getId(), dao))
					{
						throw new DAOException(ApplicationProperties.getValue("errors.distribution.arrayAlreadyDistributed"));
					}*/
					distributedItem.setSpecimenArray(specimenArrayObj);
				}
				/**
				 * Start: Change for API Search   --- Jitendra 06/10/2006
				 * In Case of Api Search, previoulsy it was failing since there was default class level initialization 
				 * on domain object. For example in User object, it was initialized as protected String lastName=""; 
				 * So we removed default class level initialization on domain object and are initializing in method
				 * setAllValues() of domain object. But in case of Api Search, default values will not get set 
				 * since setAllValues() method of domainObject will not get called. To avoid null pointer exception,
				 * we are setting the default values same as we were setting in setAllValues() method of domainObject.
				 */
				ApiSearchUtil.setDistributedItemDefault(distributedItem);
				//End:-  Change for API Search 

				distributedItem.setDistribution(dist);

			}
		}
	}

	//Mandar : 04-Apr-06 : bug id:1545 : - Check for removed specimens
	private void updateRemovedSpecimens(Collection newDistributedItemCollection, Collection oldDistributedItemCollection, DAO dao,
			SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException
	{
		// iterate through the old collection and find the specimens that are removed.
		Iterator it = oldDistributedItemCollection.iterator();
		while (it.hasNext())
		{
			DistributedItem item = (DistributedItem) it.next();
			boolean isPresentInNew = newDistributedItemCollection.contains(item);
			Logger.out.debug("Old Object in New Collection : " + isPresentInNew);
			if (!isPresentInNew)
			{
				Object specimenObj = dao.retrieve(Specimen.class.getName(), item.getSpecimen().getId());
				double quantity = item.getQuantity().doubleValue();
				updateAvailableQty((Specimen) specimenObj, quantity);
				dao.update(specimenObj, sessionDataBean, Constants.IS_AUDITABLE_TRUE, Constants.IS_SECURE_UPDATE_TRUE,
						Constants.HAS_OBJECT_LEVEL_PRIVILEGE_FALSE);
			}

		}
		Logger.out.debug("Update Successful ...04-Apr-06");
	}

	//this method updates the specimen available qty by adding the previously subtracted(during distribution) qty.
	private void updateAvailableQty(Specimen specimen, double quantity)
	{
		if (specimen instanceof TissueSpecimen)
		{
			TissueSpecimen tissueSpecimen = (TissueSpecimen) specimen;
			double availabeQty = Double.parseDouble(tissueSpecimen.getAvailableQuantity().toString());//tissueSpecimen.getAvailableQuantityInGram().doubleValue();
			Logger.out.debug("TissueAvailabeQty" + availabeQty);
			availabeQty = availabeQty + quantity;
			Logger.out.debug("TissueAvailabeQty after addition" + availabeQty);
			tissueSpecimen.setAvailableQuantity(new Double(availabeQty));//tissueSpecimen.setAvailableQuantityInGram(new Double(availabeQty));
		}
		else if (specimen instanceof CellSpecimen)
		{
			CellSpecimen cellSpecimen = (CellSpecimen) specimen;
			int availabeQty = (int) Double.parseDouble(cellSpecimen.getAvailableQuantity().toString());//cellSpecimen.getAvailableQuantityInCellCount().intValue();
			availabeQty = availabeQty + (int) quantity;
			cellSpecimen.setAvailableQuantity(new Double(availabeQty));//cellSpecimen.setAvailableQuantityInCellCount(new Integer(availabeQty));
		}
		else if (specimen instanceof MolecularSpecimen)
		{
			MolecularSpecimen molecularSpecimen = (MolecularSpecimen) specimen;
			double availabeQty = Double.parseDouble(molecularSpecimen.getAvailableQuantity().toString());//molecularSpecimen.getAvailableQuantityInMicrogram().doubleValue();
			availabeQty = availabeQty + quantity;
			molecularSpecimen.setAvailableQuantity(new Double(availabeQty));//molecularSpecimen.setAvailableQuantityInMicrogram(new Double(availabeQty));
		}
		else if (specimen instanceof FluidSpecimen)
		{
			FluidSpecimen fluidSpecimen = (FluidSpecimen) specimen;
			double availabeQty = Double.parseDouble(fluidSpecimen.getAvailableQuantity().toString());//fluidSpecimen.getAvailableQuantityInMilliliter().doubleValue();
			availabeQty = availabeQty + quantity;
			fluidSpecimen.setAvailableQuantity(new Double(availabeQty));//fluidSpecimen.setAvailableQuantityInMilliliter(new Double(availabeQty));
		}

	}

	//Mandar : 04-Apr-06 : end

	public Long getSpecimenId(String barcodeLabel, Integer distributionBasedOn) throws DAOException
	{
		String className = Specimen.class.getName();
		String[] selectColumnName = null;
		String[] whereColumnName = {"barcode"};
		String[] whereColumnCondition = {"="};
		String[] value = {barcodeLabel};
		Object[] whereColumnValue = new Object[]{value};

		if (distributionBasedOn.intValue() == Constants.LABEL_BASED_DISTRIBUTION)
		{
			whereColumnName = new String[]{"label"};
		}

		Specimen specimen = null;
		List specimenList = retrieve(className, selectColumnName, whereColumnName, whereColumnCondition, value, null);

		if (specimenList == null || specimenList.isEmpty())
		{
			throw new DAOException("errors.distribution.specimenNotFound");
		}
		specimen = (Specimen) specimenList.get(0);
		if (specimen.getActivityStatus().equals(Constants.ACTIVITY_STATUS_VALUES[2]))
		{
			throw new DAOException("errors.distribution.closedSpecimen");
		}

		return specimen.getId();
	}

	public Long getSpecimenArrayId(String barcodeLabel, Integer distributionBasedOn) throws DAOException
	{

		String className = SpecimenArray.class.getName();
		String[] selectColumnName = null;
		String[] whereColumnName = {Constants.SYSTEM_BARCODE};
		String[] whereColumnCondition = {"="};
		String[] value = {barcodeLabel};
		Object[] whereColumnValue = new Object[]{value};

		if (distributionBasedOn.intValue() == Constants.LABEL_BASED_DISTRIBUTION)
		{
			whereColumnName = new String[]{Constants.SYSTEM_NAME};
		}

		SpecimenArray specimenArray = null;
		List specimenList = retrieve(className, selectColumnName, whereColumnName, whereColumnCondition, value, null);

		if (specimenList == null || specimenList.isEmpty())
		{
			throw new DAOException("errors.distribution.specimenArrayNotFound");
		}
		specimenArray = (SpecimenArray) specimenList.get(0);
		return specimenArray.getId();
	}

	private boolean chkForSpecimenArrayDistributed(Long spArrayId, DAO dao) throws DAOException
	{
		String[] selectColumnName = {"id"};
		String[] whereColumnName = {"specimenArray.id"};
		Object[] whereColumnValue = {spArrayId};
		String[] whereColumnCond = {"="};
		List list = dao.retrieve(DistributedItem.class.getName(), selectColumnName, whereColumnName, whereColumnCond, whereColumnValue,
				Constants.AND_JOIN_CONDITION);
		if (list != null && list.size() > 0)
		{
			return true;

		}

		return false;
	}
	
	/**
	 * Created the list of Specimen Array details 
	 * @param distribution
	 * @param arrayEntries
	 */
	protected void setSpecimenArrayDetails(Distribution distribution , List arrayEntries)
	{
		
		distribution.getDistributedItemCollection();
		Iterator itr = distribution.getDistributedItemCollection().iterator();
		while (itr.hasNext())
		{
			DistributedItem distributedItem = (DistributedItem) itr.next();
			SpecimenArray specimenArray = (SpecimenArray)distributedItem.getSpecimenArray();
			List arrayDetails = new ArrayList();
			arrayDetails.add(specimenArray.getName());
			arrayDetails.add(Utility.toString(specimenArray.getBarcode()));
			arrayDetails.add(Utility.toString(specimenArray.getSpecimenArrayType().getName()));
			if(specimenArray != null && specimenArray.getLocatedAtPosition() != null)
			{
				arrayDetails.add(Utility.toString(specimenArray.getLocatedAtPosition().getPositionDimensionOne()));
				arrayDetails.add(Utility.toString(specimenArray.getLocatedAtPosition().getPositionDimensionTwo()));
			}
			arrayDetails.add(Utility.toString(specimenArray.getCapacity().getOneDimensionCapacity()));
			arrayDetails.add(Utility.toString(specimenArray.getCapacity().getTwoDimensionCapacity()));
			arrayDetails.add(Utility.toString(specimenArray.getSpecimenArrayType().getSpecimenClass()));
			arrayDetails.add(Utility.toString(specimenArray.getSpecimenArrayType().getSpecimenTypeCollection()));
			arrayDetails.add(Utility.toString(specimenArray.getComment()));
			arrayEntries.add(arrayDetails);
		}
		
		
	}
	
	/**
	 * Get the data for distribution
	 * @param dist
	 * @return
	 * @throws Exception
	 */
	public List getListOfArray(Distribution dist) throws Exception
	{
		List arrayEntries = new ArrayList();
		long startTime = System.currentTimeMillis();
		AbstractDAO dao = DAOFactory.getInstance().getDAO(Constants.HIBERNATE_DAO);
				
		try
    	{
			dao.openSession(null);
			
			Object object = dao.retrieve(Distribution.class.getName(), dist.getId());
			Distribution distribution = (Distribution)object;
			setSpecimenArrayDetails(distribution , arrayEntries);
			
			long endTime = System.currentTimeMillis();
			System.out.println("Execute time of getRequestDetailsList :" + (endTime-startTime));
			return arrayEntries;
			
    	}
    	catch(DAOException e)
    	{
    		Logger.out.error(e.getMessage(), e);
    		return null;	
    	}
    	finally
		{
			try
			{
				dao.closeSession();
			}
			catch(DAOException daoEx)
			{
				Logger.out.error(daoEx.getMessage(), daoEx);
				return null;
			}
		}	
		
	}
}