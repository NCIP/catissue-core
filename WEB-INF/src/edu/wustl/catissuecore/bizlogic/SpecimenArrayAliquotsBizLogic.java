/*
 * Created on Sep 22, 2006
 */
package edu.wustl.catissuecore.bizlogic;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import edu.wustl.catissuecore.domain.MolecularSpecimen;
import edu.wustl.catissuecore.domain.Quantity;
import edu.wustl.catissuecore.domain.SpecimenArray;
import edu.wustl.catissuecore.domain.SpecimenArrayContent;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.dao.DAO;
import edu.wustl.common.security.exceptions.SMException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.dbManager.DAOException;


/**
 * SpecimenArrayAliquotsBizLogic class is used to create SpecimenArray aliquots from the parent SpecimenArray
 * and to inserts all the aliquotes into the database 
 * @author jitendra_agrawal
 */
public class SpecimenArrayAliquotsBizLogic extends DefaultBizLogic
{
	protected void insert(Object obj, DAO dao, SessionDataBean sessionDataBean)
	throws DAOException, UserNotAuthorizedException
	{
		SpecimenArray specimenArray = (SpecimenArray) obj;
		
		String specimenKey = "SpecimenArray:";
		Map aliquotMap = specimenArray.getAliqoutMap();

		//Retrieving the parent specimenArray of the aliquot
		SpecimenArray parentSpecimenArray = (SpecimenArray) dao.retrieve(SpecimenArray.class.getName(), specimenArray.getId());
		
		SpecimenArrayBizLogic specimenArrayBizLogic = (SpecimenArrayBizLogic) BizLogicFactory
		.getInstance().getBizLogic(Constants.SPECIMEN_ARRAY_FORM_ID);

		for (int i = 1; i <= specimenArray.getAliquotCount(); i++)
		{
			//Preparing the map keys			
			String labelKey = specimenKey + i + "_label";
			String barcodeKey = specimenKey + i + "_barcode";
			String containerIdKey = specimenKey + i + "_StorageContainer_id";
			String posDim1Key = specimenKey + i + "_positionDimensionOne";
			String posDim2Key = specimenKey + i + "_positionDimensionTwo";
			String virtuallyLocatedKey = specimenKey + i + "_virtuallyLocated";
						
			//Retrieving the quantity, barcode & location values for each aliquot
			String label = (String) aliquotMap.get(labelKey);
			String barcode = (String) aliquotMap.get(barcodeKey);
			String containerId = (String) aliquotMap.get(containerIdKey);
			String posDim1 = (String) aliquotMap.get(posDim1Key);
			String posDim2 = (String) aliquotMap.get(posDim2Key);			
			String virtuallyLocated = (String) aliquotMap.get(virtuallyLocatedKey);			
			
			
			//Create an object of Specimen Subclass
			SpecimenArray aliquotSpecimenArray = new SpecimenArray();			

			if (parentSpecimenArray != null)
			{
				//check for closed ParentSpecimenArray
				checkStatus(dao, parentSpecimenArray, "Parent SpecimenArray");
				aliquotSpecimenArray.setParent(parentSpecimenArray);
				aliquotSpecimenArray.setSpecimenArrayType(parentSpecimenArray.getSpecimenArrayType());				
				aliquotSpecimenArray.setCreatedBy(parentSpecimenArray.getCreatedBy());
				aliquotSpecimenArray.setCapacity(parentSpecimenArray.getCapacity());				
				Collection specimenArrayContentCollection = PopulateSpecimenArrayContentCollectionForAliquot(parentSpecimenArray,aliquotSpecimenArray,specimenArray.getAliquotCount());
				aliquotSpecimenArray.setSpecimenArrayContentCollection(specimenArrayContentCollection);
				aliquotSpecimenArray.setAliquot(true);
			}
			
			//Explicity set barcode to null if it is empty as its a unique key in the database
			if (barcode != null && barcode.trim().length() == 0)
			{
				barcode = null;
			}
			aliquotSpecimenArray.setBarcode(barcode);

			//Explicity set barcode to null if it is empty as its a unique key in the database
			if (label != null && label.trim().length() == 0)
			{
				label = null;
			}
			aliquotSpecimenArray.setName(label);

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

						aliquotSpecimenArray.setStorageContainer(container);
					}
				}
				else
				{
					aliquotSpecimenArray.setStorageContainer(null);
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
				aliquotSpecimenArray.setPositionDimensionOne(new Integer(posDim1));
				aliquotSpecimenArray.setPositionDimensionTwo(new Integer(posDim2));
			}
			else
			{
				aliquotSpecimenArray.setPositionDimensionOne(null);
				aliquotSpecimenArray.setPositionDimensionTwo(null);
			}
			aliquotSpecimenArray.setAvailable(Boolean.TRUE);
			aliquotSpecimenArray.setActivityStatus(Constants.ACTIVITY_STATUS_ACTIVE);
			//aliquotSpecimenArray.setLineage(Constants.ALIQUOT);

			//Inserting an aliquot in the database
			//TODO NEEDS TO BE FIXED FOR SECURE INSERT
			
			
			specimenArrayBizLogic.insert(aliquotSpecimenArray, dao, sessionDataBean);		
			
		}
		
		if (parentSpecimenArray != null)
		{	
			SpecimenArray oldSpecimenArray = (SpecimenArray) dao.retrieve(SpecimenArray.class.getName(), specimenArray.getId());;
			parentSpecimenArray.setAvailable(Boolean.valueOf(false));
			specimenArrayBizLogic.update(dao, parentSpecimenArray, oldSpecimenArray, sessionDataBean);	
		}
		
		//Populate aliquot map with parent specimenArray's data
		populateParentSpecimenArrayData(aliquotMap, specimenArray, parentSpecimenArray);
		
	}	 
	
	/**
	 * This function populates the parent specimenArray information in aliquot map. This map will be
	 * be retrieved in ForwardToProcessor & will be set in the request scope. Then the map will
	 * be retrieved from request scope in SpecimenArrayAliquotAction if the page is of "SpecimenArrayAliquotSummary" &
	 * the formbean will be populated with the appropriate data.
	 * 
	 * @param aliquotMap Map
	 * @param specimenArray SpecimenArray
	 */
	private void populateParentSpecimenArrayData(Map aliquotMap, SpecimenArray specimenArray, SpecimenArray parentSpecimenArray)
	{
		aliquotMap.put(Constants.ALIQUOT_SPECIMEN_ARRAY_TYPE, parentSpecimenArray.getSpecimenArrayType().getName());
		aliquotMap.put(Constants.ALIQUOT_SPECIMEN_CLASS, parentSpecimenArray.getSpecimenArrayType().getSpecimenClass());
		aliquotMap.put(Constants.ALIQUOT_SPECIMEN_TYPES, parentSpecimenArray.getSpecimenArrayType().getSpecimenTypeCollection());	
		aliquotMap.put(Constants.ALIQUOT_ALIQUOT_COUNTS, String.valueOf(specimenArray.getAliquotCount()));
		
		specimenArray.setAliqoutMap(aliquotMap);
	}
	
	/**
	 * 
	 * @param parentSpecimenArray SpecimenArray
	 * @param aliquotSpecimenArray SpecimenArray
	 * @param aliquotCount int
	 * @return Collection
	 */
	private Collection PopulateSpecimenArrayContentCollectionForAliquot(SpecimenArray parentSpecimenArray, SpecimenArray aliquotSpecimenArray, int aliquotCount)
	{
		Collection parentSpecimenArrayContentCollection = parentSpecimenArray.getSpecimenArrayContentCollection();
		Collection specimenArrayContentCollection =  new HashSet();		
		Iterator iter = parentSpecimenArrayContentCollection.iterator();
		SpecimenArrayContent specimenArrayContent = null;	
		
		for(int i=0; iter.hasNext(); i++)
		{
			SpecimenArrayContent parentSpecimenArrayContent = (SpecimenArrayContent) iter.next();
			specimenArrayContent = new SpecimenArrayContent();
			specimenArrayContent.setSpecimen(parentSpecimenArrayContent.getSpecimen());
			specimenArrayContent.setPositionDimensionOne(parentSpecimenArrayContent.getPositionDimensionOne());
			specimenArrayContent.setPositionDimensionTwo(parentSpecimenArrayContent.getPositionDimensionTwo());
			specimenArrayContent.setSpecimenArray(aliquotSpecimenArray);				
			Quantity quantity = null;
			if (parentSpecimenArrayContent.getSpecimen() instanceof MolecularSpecimen) 
			{
				if(aliquotCount > 0)
				{
					Double parentInitialQuantity = parentSpecimenArrayContent.getInitialQuantity().getValue();
					double initialQuantity = parentInitialQuantity.doubleValue()/aliquotCount;		
					quantity = new Quantity();
					quantity.setValue(new Double(initialQuantity));	
					specimenArrayContent.setInitialQuantity(quantity);
					// reset quantity value of parent array content to 0.0
					parentSpecimenArrayContent.getInitialQuantity().setValue(Double.valueOf("0"));
				}
				
				specimenArrayContent.setConcentrationInMicrogramPerMicroliter(parentSpecimenArrayContent.getConcentrationInMicrogramPerMicroliter());
			}
					
			specimenArrayContentCollection.add(specimenArrayContent);
		}
		return specimenArrayContentCollection;
	}
}
