package edu.wustl.catissuecore.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import edu.wustl.catissuecore.domain.Biohazard;
import edu.wustl.catissuecore.domain.CollectionEventParameters;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.ExternalIdentifier;
import edu.wustl.catissuecore.domain.ReceivedEventParameters;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCharacteristics;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;

/**
 * <p>This class initializes the fields of MultipleSpecimenValidationUtil.java</p>
 * @author Ashwin Gupta
 * @version 1.1
 */
public final class MultipleSpecimenValidationUtil
{

	/**
	 * validate multiple specimens.
	 * @param specimenMap
	 * @param dao
	 * @param operation
	 * @return
	 * @throws DAOException
	 */
	public static boolean validateMultipleSpecimen(Map specimenMap, IBizLogic bizLogic, String operation) throws DAOException
	{
		System.out.println("Inside validateMultipleSpecimen() ");
		boolean result = true;
/*		IBizLogic bizLogic;
		try
		{
			bizLogic = AbstractBizLogicFactory.getBizLogic(ApplicationProperties.getValue("app.bizLogicFactory"), "getBizLogic",
					Constants.NEW_SPECIMEN_FORM_ID);
		}
		catch (BizLogicException e)
		{
			e.printStackTrace();
			throw new DAOException(e);
		}
*/		
		setSCGinSpecimen(specimenMap,bizLogic);
		Iterator specimenIterator = specimenMap.keySet().iterator();
		int count = 0;
		while (specimenIterator.hasNext() && result == true)
		{
			Specimen specimen = (Specimen) specimenIterator.next();
			count++;
			// TODO uncomment code for label, performance
			try
			{
				result = validateSingleSpecimen(specimen, bizLogic, operation, true);
			}
			catch (DAOException daoException)
			{
				String message = daoException.getMessage();
				message += " (This message is for Specimen number " + count + ")";
				daoException.setMessage(message);
				throw daoException;
			}

			List derivedSpecimens = (List) specimenMap.get(specimen);

			if (derivedSpecimens == null)
			{
				continue;
			}

			//validate derived specimens
			for (int i = 0; i < derivedSpecimens.size(); i++)
			{

				Specimen derivedSpecimen = (Specimen) derivedSpecimens.get(i);
				derivedSpecimen.setSpecimenCharacteristics(specimen.getSpecimenCharacteristics());
				derivedSpecimen.setSpecimenCollectionGroup(specimen.getSpecimenCollectionGroup());
				derivedSpecimen.setPathologicalStatus(specimen.getPathologicalStatus());

				try
				{
					result = validateSingleSpecimen(derivedSpecimen, bizLogic, operation, false);
				}
				catch (DAOException daoException)
				{
					int j = i + 1;
					String message = daoException.getMessage();
					message += " (This message is for Derived Specimen " + j + " of Parent Specimen number " + count + ")";
					daoException.setMessage(message);
					throw daoException;
				}

				if (!result)
				{
					break;
				}
			}

		}
		System.out.println("End Inside validateMultipleSpecimen() " + result);
		return result;
	}
	
	
	/**
	 * Sets SCG in Specimen.
	 * @param specimenMap map
	 * @param dao dao
	 * @throws DAOException dao exception
	 */
	public static void setSCGinSpecimen(Map specimenMap, IBizLogic bizLogic) throws DAOException
	{
		Iterator specimenIterator = specimenMap.keySet().iterator();
		while (specimenIterator.hasNext())
		{
			Specimen specimen = (Specimen) specimenIterator.next();
			//validate single specimen
			if (specimen.getSpecimenCollectionGroup() != null)
			{
				String[] selectColumnName = {"id","collectionProtocolRegistration.id","collectionProtocolRegistration.collectionProtocol.id"};
				String[] whereColumnName = {Constants.NAME};
				String[] whereColumnCondition = {"="};
				String[] whereColumnValue = {specimen.getSpecimenCollectionGroup().getName()};
				List spCollGroupList = bizLogic.retrieve(SpecimenCollectionGroup.class.getName(), selectColumnName, whereColumnName, whereColumnCondition,
						whereColumnValue, null);
				// TODO saperate calls for SCG - ID and cpid
				// SCG - ID will be needed before populateStorageLocations
				
				// TODO test
				if (!spCollGroupList.isEmpty())
				{
					Object idList[] = (Object[]) spCollGroupList.get(0); // Move up + here
					//Long scgId  = (Long) spCollGroupList.get(0);
					//Long scgId = (Long) idList[0];
					Long scgId = (Long) idList[0]; // Move up 
					long cprId = ((Long) idList[1]).longValue();//here
					long cpId = ((Long) idList[2]).longValue();//here
					specimen.getSpecimenCollectionGroup().setId(scgId);
					//TODO instantiate associated objects(CPR & CP) & set IDs
					CollectionProtocol cp = new CollectionProtocol();
					cp.setId(new Long(cpId));
					CollectionProtocolRegistration cpr = new CollectionProtocolRegistration();
					cpr.setId(new Long(cprId));
					cpr.setCollectionProtocol(cp);
					specimen.getSpecimenCollectionGroup().setCollectionProtocolRegistration(cpr);
				}
			}
		}
	}
	
	/**
	 * validates single specimen.
	 * @param specimen
	 * @param dao
	 * @param operation
	 * @param partOfMulipleSpecimen
	 * @return
	 * @throws DAOException
	 */
	public static boolean validateSingleSpecimen(Specimen specimen, IBizLogic bizLogic, String operation, boolean partOfMulipleSpecimen) throws DAOException
	{
		//Added by Ashish		
		//Logger.out.debug("Start-Inside validate method of specimen bizlogic");
		System.out.println("Inside validateSingleSpecimen() ");
		if (specimen == null)
		{
			throw new DAOException(ApplicationProperties.getValue("domain.object.null.err.msg", "Specimen"));
		}

		Validator validator = new Validator();

		if (specimen.getSpecimenCollectionGroup() == null || specimen.getSpecimenCollectionGroup().getId() == null
				|| specimen.getSpecimenCollectionGroup().getId().equals("-1"))
		{
			String message = ApplicationProperties.getValue("specimen.specimenCollectionGroup");
			throw new DAOException(ApplicationProperties.getValue("errors.item.required", message));
		}

		if (specimen.getParentSpecimen() != null
				&& (specimen.getParentSpecimen().getId() == null || validator.isEmpty(specimen.getParentSpecimen().getId().toString())))
		{
			String message = ApplicationProperties.getValue("createSpecimen.parent");
			throw new DAOException(ApplicationProperties.getValue("errors.item.required", message));
		}

		if (validator.isEmpty(specimen.getLabel()))
		{
			String message = ApplicationProperties.getValue("specimen.label");
			throw new DAOException(ApplicationProperties.getValue("errors.item.required", message));
		}

		if (validator.isEmpty(specimen.getClassName()))
		{
			String message = ApplicationProperties.getValue("specimen.type");
			throw new DAOException(ApplicationProperties.getValue("errors.item.required", message));
		}

		if (validator.isEmpty(specimen.getType()))
		{
			String message = ApplicationProperties.getValue("specimen.subType");
			throw new DAOException(ApplicationProperties.getValue("errors.item.required", message));
		}

		/*
		// commented as storage container has been removed from multiple specimen page - Ashwin 
		if (specimen.getStorageContainer() != null && specimen.getStorageContainer().getId() == null)
		{
			String message = ApplicationProperties.getValue("specimen.subType");
			throw new DAOException(ApplicationProperties.getValue("errors.invalid", message));
		}
		*/
		if (specimen.getSpecimenEventCollection() != null)
		{
			Iterator specimenEventCollectionIterator = specimen.getSpecimenEventCollection().iterator();
			while (specimenEventCollectionIterator.hasNext())
			{
				//CollectionEvent validation.
				Object eventObject = specimenEventCollectionIterator.next();
				if (eventObject instanceof CollectionEventParameters)
				{
					CollectionEventParameters collectionEventParameters = (CollectionEventParameters) eventObject;
					collectionEventParameters.getUser();
					if (collectionEventParameters.getUser() == null || collectionEventParameters.getUser().getId() == null)
					{
						String message = ApplicationProperties.getValue("specimen.collection.event.user");
						throw new DAOException(ApplicationProperties.getValue("errors.item.required", message));
					}
					if (!validator.checkDate(Utility.parseDateToString(collectionEventParameters.getTimestamp(), Constants.DATE_PATTERN_MM_DD_YYYY)))
					{

						String message = ApplicationProperties.getValue("specimen.collection.event.date");
						throw new DAOException(ApplicationProperties.getValue("errors.item.required", message));
					}
					// checks the collectionProcedure
					if (!validator.isValidOption(collectionEventParameters.getCollectionProcedure()))
					{
						String message = ApplicationProperties.getValue("collectioneventparameters.collectionprocedure");
						throw new DAOException(ApplicationProperties.getValue("errors.item.required", message));
					}
					List procedureList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_COLLECTION_PROCEDURE, null);
					if (!Validator.isEnumeratedValue(procedureList, collectionEventParameters.getCollectionProcedure()))
					{
						throw new DAOException(ApplicationProperties.getValue("events.collectionProcedure.errMsg"));
					}

					if (!validator.isValidOption(collectionEventParameters.getContainer()))
					{
						String message = ApplicationProperties.getValue("collectioneventparameters.container");
						throw new DAOException(ApplicationProperties.getValue("errors.item.required", message));
					}

					List containerList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_CONTAINER, null);
					if (!Validator.isEnumeratedValue(containerList, collectionEventParameters.getContainer()))
					{
						throw new DAOException(ApplicationProperties.getValue("events.container.errMsg"));
					}

				}
				//ReceivedEvent validation
				else if (eventObject instanceof ReceivedEventParameters)
				{
					ReceivedEventParameters receivedEventParameters = (ReceivedEventParameters) eventObject;
					if (receivedEventParameters.getUser() == null || receivedEventParameters.getUser().getId() == null)
					{
						String message = ApplicationProperties.getValue("specimen.recieved.event.user");
						throw new DAOException(ApplicationProperties.getValue("errors.item.required", message));
					}
					if (!validator.checkDate(Utility.parseDateToString(receivedEventParameters.getTimestamp(), Constants.DATE_PATTERN_MM_DD_YYYY)))
					{
						String message = ApplicationProperties.getValue("specimen.recieved.event.date");
						throw new DAOException(ApplicationProperties.getValue("errors.item.required", message));
					}
					// checks the collectionProcedure
					if (!validator.isValidOption(receivedEventParameters.getReceivedQuality()))
					{
						String message = ApplicationProperties.getValue("collectioneventparameters.receivedquality");
						throw new DAOException(ApplicationProperties.getValue("errors.item.required", message));
					}

					List qualityList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_RECEIVED_QUALITY, null);
					if (!Validator.isEnumeratedValue(qualityList, receivedEventParameters.getReceivedQuality()))
					{
						throw new DAOException(ApplicationProperties.getValue("events.receivedQuality.errMsg"));
					}
				}
			}
		}

		//Validations for Biohazard Add-More Block
		Collection bioHazardCollection = specimen.getBiohazardCollection();
		Biohazard biohazard = null;
		if (bioHazardCollection != null && !bioHazardCollection.isEmpty())
		{
			Iterator itr = bioHazardCollection.iterator();
			while (itr.hasNext())
			{
				biohazard = (Biohazard) itr.next();
				if (!validator.isValidOption(biohazard.getType()))
				{
					String message = ApplicationProperties.getValue("newSpecimen.msg");
					throw new DAOException(ApplicationProperties.getValue("errors.newSpecimen.biohazard.missing", message));
				}
				if (biohazard.getId() == null || biohazard.getId().toString().equals("-1"))
				{
					String message = ApplicationProperties.getValue("newSpecimen.msg");
					throw new DAOException(ApplicationProperties.getValue("errors.newSpecimen.biohazard.missing", message));
				}
			}
		}

		//validations for external identifiers
		Collection extIdentifierCollection = specimen.getExternalIdentifierCollection();
		ExternalIdentifier extIdentifier = null;
		if (extIdentifierCollection != null && !extIdentifierCollection.isEmpty())
		{
			Iterator itr = extIdentifierCollection.iterator();
			while (itr.hasNext())
			{
				extIdentifier = (ExternalIdentifier) itr.next();
				if (validator.isEmpty(extIdentifier.getName()))
				{
					String message = ApplicationProperties.getValue("specimen.msg");
					throw new DAOException(ApplicationProperties.getValue("errors.specimen.externalIdentifier.missing", message));
				}
				if (validator.isEmpty(extIdentifier.getValue()))
				{
					String message = ApplicationProperties.getValue("specimen.msg");
					throw new DAOException(ApplicationProperties.getValue("errors.specimen.externalIdentifier.missing", message));
				}
			}
		}
		//End Ashish

		if (Constants.ALIQUOT.equals(specimen.getLineage()))
		{
			//return true;
		}

		validateFields(specimen, bizLogic, operation, partOfMulipleSpecimen);

		List specimenClassList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_SPECIMEN_CLASS, null);
		String specimenClass = Utility.getSpecimenClassName(specimen);

		if (!Validator.isEnumeratedValue(specimenClassList, specimenClass))
		{
			throw new DAOException(ApplicationProperties.getValue("protocol.class.errMsg"));
		}

		if (!Validator.isEnumeratedValue(Utility.getSpecimenTypes(specimenClass), specimen.getType()))
		{
			throw new DAOException(ApplicationProperties.getValue("protocol.type.errMsg"));
		}

		SpecimenCharacteristics characters = specimen.getSpecimenCharacteristics();

		if (characters == null)
		{
			throw new DAOException(ApplicationProperties.getValue("specimen.characteristics.errMsg"));
		}
		else
		{
			if (specimen.getSpecimenCollectionGroup() != null)
			{
				//				NameValueBean undefinedVal = new NameValueBean(Constants.UNDEFINED,Constants.UNDEFINED);
				List tissueSiteList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_TISSUE_SITE, null);

				if (!Validator.isEnumeratedValue(tissueSiteList, characters.getTissueSite()))
				{
					throw new DAOException(ApplicationProperties.getValue("protocol.tissueSite.errMsg"));
				}

				//		    	NameValueBean unknownVal = new NameValueBean(Constants.UNKNOWN,Constants.UNKNOWN);
				List tissueSideList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_TISSUE_SIDE, null);

				if (!Validator.isEnumeratedValue(tissueSideList, characters.getTissueSide()))
				{
					throw new DAOException(ApplicationProperties.getValue("specimen.tissueSide.errMsg"));
				}

				List pathologicalStatusList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_PATHOLOGICAL_STATUS, null);

				if (!Validator.isEnumeratedValue(pathologicalStatusList, specimen.getPathologicalStatus()))
				{
					throw new DAOException(ApplicationProperties.getValue("protocol.pathologyStatus.errMsg"));
				}
			}
		}

		if (operation.equals(Constants.ADD))
		{
			if (!specimen.getAvailable().booleanValue())
			{
				throw new DAOException(ApplicationProperties.getValue("specimen.available.errMsg"));
			}

			if (!Constants.ACTIVITY_STATUS_ACTIVE.equals(specimen.getActivityStatus()))
			{
				throw new DAOException(ApplicationProperties.getValue("activityStatus.active.errMsg"));
			}
		}
		else
		{
			if (!Validator.isEnumeratedValue(Constants.ACTIVITY_STATUS_VALUES, specimen.getActivityStatus()))
			{
				throw new DAOException(ApplicationProperties.getValue("activityStatus.errMsg"));
			}
		}
		//Logger.out.debug("End-Inside validate method of specimen bizlogic");
		return true;
	}

	/**
	 * validate fields
	 * @param specimen specimen
	 * @param dao dao
	 * @param operation string operation
	 * @param partOfMulipleSpecimen 
	 * @throws DAOException
	 */
	private static void validateFields(Specimen specimen, IBizLogic bizLogic, String operation, boolean partOfMulipleSpecimen) throws DAOException
	{
		Validator validator = new Validator();

		if (partOfMulipleSpecimen)
		{

			if (specimen.getSpecimenCollectionGroup() == null || validator.isEmpty(specimen.getSpecimenCollectionGroup().getName()))
			{
				String quantityString = ApplicationProperties.getValue("specimen.specimenCollectionGroup");
				throw new DAOException(ApplicationProperties.getValue("errors.item.required", quantityString));
			}

			List spgList = bizLogic.retrieve(SpecimenCollectionGroup.class.getName(), Constants.NAME, specimen.getSpecimenCollectionGroup().getName());

			if (spgList.size() == 0)
			{
				throw new DAOException(ApplicationProperties.getValue("errors.item.unknown", "Specimen Collection Group "
						+ specimen.getSpecimenCollectionGroup().getName()));
			}
		}

		if (validator.isEmpty(specimen.getLabel()))
		{
			String labelString = ApplicationProperties.getValue("specimen.label");
			throw new DAOException(ApplicationProperties.getValue("errors.item.required", labelString));
		}

		if (specimen.getInitialquantity() == null || specimen.getInitialquantity().getValue() == null)
		{
			String quantityString = ApplicationProperties.getValue("specimen.quantity");
			throw new DAOException(ApplicationProperties.getValue("errors.item.required", quantityString));
		}

		/**
		 *  If specimen is virtually located, then in that case storage container is being set null explicitly in Specimen
		 *  domain object.Hence to avoid NullPointerException here check null of container is required.
		 *  @author jitendra_agrawal  
		 */
		/*
		// commented as storage container has been removed from multiple specimen page - Ashwin 
		if (specimen.getStorageContainer() != null)
		{
			Long storageContainerId = specimen.getStorageContainer().getId();
			Integer xPos = specimen.getPositionDimensionOne();
			Integer yPos = specimen.getPositionDimensionTwo();

			if (storageContainerId == null || xPos == null || yPos == null || xPos.intValue() < 0 || yPos.intValue() < 0)
			{
				throw new DAOException(ApplicationProperties.getValue("errors.item.format", ApplicationProperties
						.getValue("specimen.positionInStorageContainer")));
			}
		}
		*/
	}
	
}

