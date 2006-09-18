/**
 * <p>Title: NewSpecimenHDAO Class>
 * <p>Description:	AliquotBizLogic is used to add new aliquots into the database using Hibernate.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 * Created on Jun 27, 2006
 */

package edu.wustl.catissuecore.bizlogic;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

import edu.wustl.catissuecore.domain.ExternalIdentifier;
import edu.wustl.catissuecore.domain.MolecularSpecimen;
import edu.wustl.catissuecore.domain.Quantity;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.dao.DAO;
import edu.wustl.common.security.exceptions.SMException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.logger.Logger;

public class AliquotBizLogic extends NewSpecimenBizLogic
{

	/**
	 * Saves the AliquotSpecimen object in the database.
	 * @param obj The storageType object to be saved.
	 * @param session The session in which the object is saved.
	 * @throws DAOException 
	 */
	protected void insert(Object obj, DAO dao, SessionDataBean sessionDataBean)
			throws DAOException, UserNotAuthorizedException
	{
		Specimen aliquot = (Specimen) obj;
		String specimenKey = "Specimen:";
		Map aliquotMap = aliquot.getAliqoutMap();

		//Retrieving the parent specimen of the aliquot
		Specimen parentSpecimen = (Specimen) dao.retrieve(Specimen.class.getName(), aliquot
				.getParentSpecimen().getId());
		double dQuantity = 0;

		for (int i = 1; i <= aliquot.getNoOfAliquots(); i++)
		{
			//Preparing the map keys
			String quantityKey = specimenKey + i + "_quantity";
			String barcodeKey = specimenKey + i + "_barcode";
			String containerIdKey = specimenKey + i + "_StorageContainer_id";
			String posDim1Key = specimenKey + i + "_positionDimensionOne";
			String posDim2Key = specimenKey + i + "_positionDimensionTwo";
			String idKey = specimenKey + i + "_id";
			String labelKey = specimenKey + i + "_label";
			String virtuallyLocatedKey = specimenKey + i + "_virtuallyLocated";

			//Retrieving the quantity, barcode & location values for each aliquot
			String quantity = (String) aliquotMap.get(quantityKey);
			String barcode = (String) aliquotMap.get(barcodeKey);
			String containerId = (String) aliquotMap.get(containerIdKey);
			String posDim1 = (String) aliquotMap.get(posDim1Key);
			String posDim2 = (String) aliquotMap.get(posDim2Key);
			String label = (String) aliquotMap.get(labelKey);
			String virtuallyLocated = (String) aliquotMap.get(virtuallyLocatedKey);
			Logger.out.info("---------------virtually located value:"
					+ aliquotMap.get(virtuallyLocatedKey));
			dQuantity = dQuantity + Double.parseDouble(quantity);

			//Create an object of Specimen Subclass
			Specimen aliquotSpecimen = Utility.getSpecimen(parentSpecimen);
			aliquotSpecimen.setSpecimenCollectionGroup(parentSpecimen.getSpecimenCollectionGroup());

			if (parentSpecimen != null)
			{
				//check for closed ParentSpecimen
				checkStatus(dao, parentSpecimen, "Parent Specimen");
				aliquotSpecimen.setParentSpecimen(parentSpecimen);
				aliquotSpecimen.setSpecimenCharacteristics(parentSpecimen
						.getSpecimenCharacteristics());
				aliquotSpecimen.setType(parentSpecimen.getType());
				aliquotSpecimen.setPathologicalStatus(parentSpecimen.getPathologicalStatus());

				if (aliquotSpecimen instanceof MolecularSpecimen)
				{
					Double concentration = ((MolecularSpecimen) parentSpecimen)
							.getConcentrationInMicrogramPerMicroliter();

					if (concentration != null)
					{
						((MolecularSpecimen) aliquotSpecimen)
								.setConcentrationInMicrogramPerMicroliter(concentration);
					}
				}
			}

			//Setting quantities & barcode values
			aliquotSpecimen.setQuantity(new Quantity(quantity));
			aliquotSpecimen.setAvailableQuantity(new Quantity(quantity));

			//Explicity set barcode to null if it is empty as its a unique key in the database
			if (barcode != null && barcode.trim().length() == 0)
			{
				barcode = null;
			}

			aliquotSpecimen.setBarcode(barcode);

			if (label != null && label.trim().length() == 0)
			{
				label = null;
			}

			aliquotSpecimen.setLabel(label);

			try
			{
				if (virtuallyLocated == null && containerId != null)
				{
					//Setting the storage container of the aliquot
					StorageContainer container = (StorageContainer) dao.retrieve(
							StorageContainer.class.getName(), new Long(containerId));

					if (container != null)
					{
						//check for closed Storage Container
						checkStatus(dao, container, "Storage Container");

						StorageContainerBizLogic storageContainerBizLogic = (StorageContainerBizLogic) BizLogicFactory
								.getInstance().getBizLogic(Constants.STORAGE_CONTAINER_FORM_ID);

						//check for all validations on the storage container.
						storageContainerBizLogic.checkContainer(dao, containerId, posDim1, posDim2,
								sessionDataBean);

						aliquotSpecimen.setStorageContainer(container);
					}
				}
				else
				{
					aliquotSpecimen.setStorageContainer(null);
				}
			}
			catch (SMException sme)
			{
				sme.printStackTrace();
				throw handleSMException(sme);
			}

			//Setting the attributes - storage positions, available, acivity status & lineage
			if (virtuallyLocated == null && containerId != null)
			{
				aliquotSpecimen.setPositionDimensionOne(new Integer(posDim1));
				aliquotSpecimen.setPositionDimensionTwo(new Integer(posDim2));
			}
			else
			{
				aliquotSpecimen.setPositionDimensionOne(null);
				aliquotSpecimen.setPositionDimensionTwo(null);
			}
			aliquotSpecimen.setAvailable(Boolean.TRUE);
			aliquotSpecimen.setActivityStatus(Constants.ACTIVITY_STATUS_ACTIVE);
			aliquotSpecimen.setLineage(Constants.ALIQUOT);

			//Inserting an aliquot in the database
			dao.insert(aliquotSpecimen, sessionDataBean, true, false);//NEEDS TO BE FIXED FOR SECURE INSERT

			//Setting the identifier values in the map
			aliquotMap.put(idKey, String.valueOf(aliquotSpecimen.getId()));

			//TO BE DELETED LATER
			aliquot.setId(aliquotSpecimen.getId());

			//Adding dummy entry of external identifier for Query Module to fulfil the join condition
			Collection externalIdentifierCollection = new HashSet();
			ExternalIdentifier exId = new ExternalIdentifier();
			exId.setName(null);
			exId.setValue(null);
			exId.setSpecimen(aliquotSpecimen);
			externalIdentifierCollection.add(exId);
			dao.insert(exId, sessionDataBean, true, true);
		}

		//Setting the no. of aliquots in the map
		aliquotMap.put(Constants.SPECIMEN_COUNT, String.valueOf(aliquot.getNoOfAliquots()));

		//Adjusting the available quantity of parent specimen
		if (parentSpecimen != null)
		{
			double availableQuantity = parentSpecimen.getAvailableQuantity().getValue()
					.doubleValue();
			availableQuantity = availableQuantity - dQuantity;
			parentSpecimen.setAvailableQuantity(new Quantity(String.valueOf(availableQuantity)));
			if(availableQuantity<=0) {
				parentSpecimen.setAvailable(new Boolean(false));
			}

			//dao.update(parentSpecimen,sessionDataBean,Constants.IS_AUDITABLE_TRUE,Constants.IS_SECURE_UPDATE_TRUE, Constants.HAS_OBJECT_LEVEL_PRIVILEGE_FALSE);
		}

		//Populate aliquot map with parent specimen's data
		populateParentSpecimenData(aliquotMap, parentSpecimen);

		//Setting the aliquot map populated with identifiers
		aliquot.setAliqoutMap(aliquotMap);

		//Setting value of isAliquot as true for ForwardTo processor
		aliquot.setLineage(Constants.ALIQUOT);
	}

	/**
	 * Overriding the parent class's method to validate the enumerated attribute values
	 */
	protected boolean validate(Object obj, DAO dao, String operation) throws DAOException
	{
		return true;
	}

	/* This function populates the parent specimen information in aliquot map. This map will be
	 * be retrieved in ForwardToProcessor & will be set in the request scope. Then the map will
	 * be retrieved from request scope in AliquotAction if the page is of "Aliquot Summary" &
	 * the formbean will be populated with the appropriate data.
	 */
	private void populateParentSpecimenData(Map aliquotMap, Specimen parentSpecimen)
	{
		aliquotMap.put(Constants.CDE_NAME_SPECIMEN_CLASS, parentSpecimen.getClassName());
		aliquotMap.put(Constants.CDE_NAME_SPECIMEN_TYPE, parentSpecimen.getType());
		aliquotMap.put(Constants.CDE_NAME_TISSUE_SITE, parentSpecimen.getSpecimenCharacteristics()
				.getTissueSite());
		aliquotMap.put(Constants.CDE_NAME_TISSUE_SIDE, parentSpecimen.getSpecimenCharacteristics()
				.getTissueSide());
		aliquotMap.put(Constants.CDE_NAME_PATHOLOGICAL_STATUS, parentSpecimen
				.getPathologicalStatus());
		aliquotMap.put(Constants.SPECIMEN_TYPE_QUANTITY, parentSpecimen.getAvailableQuantity()
				.toString());

		if (parentSpecimen instanceof MolecularSpecimen)
		{
			aliquotMap.put("concentration", Utility.toString(((MolecularSpecimen) parentSpecimen)
					.getConcentrationInMicrogramPerMicroliter()));
		}
		else
		{
			aliquotMap.put("concentration", "");
		}
	}
}