/*
 * <p>Title: SpecimenArrayBizLogic Class </p>
 * <p>Description:This class performs business level logic for Specimen Array</p>
 * Copyright: Copyright (c) year 2006
 * Company: Washington University, School of Medicine, St. Louis.
 * @version 1.1
 * Created on Aug 28,2006
 */

package edu.wustl.catissuecore.bizlogic;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import edu.wustl.catissuecore.domain.CellSpecimen;
import edu.wustl.catissuecore.domain.FluidSpecimen;
import edu.wustl.catissuecore.domain.MolecularSpecimen;
import edu.wustl.catissuecore.domain.QuantityInMicrogram;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenArray;
import edu.wustl.catissuecore.domain.SpecimenArrayContent;
import edu.wustl.catissuecore.domain.SpecimenArrayType;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.TissueSpecimen;
import edu.wustl.catissuecore.util.ApiSearchUtil;
import edu.wustl.catissuecore.util.StorageContainerUtil;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.dao.AbstractDAO;
import edu.wustl.common.dao.DAO;
import edu.wustl.common.dao.DAOFactory;
import edu.wustl.common.security.exceptions.SMException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;

/**
 * <p>This class initializes the fields of SpecimenArrayBizLogic.java</p>
 * @author Ashwin Gupta
 * @version 1.1
 */
public class SpecimenArrayBizLogic extends DefaultBizLogic
{

	/**
	 * @see edu.wustl.common.bizlogic.AbstractBizLogic#insert(java.lang.Object, edu.wustl.common.dao.DAO, edu.wustl.common.beans.SessionDataBean)
	 */
	protected void insert(Object obj, DAO dao, SessionDataBean sessionDataBean)
			throws DAOException, UserNotAuthorizedException
	{
		SpecimenArray specimenArray = (SpecimenArray) obj;
		
		try
		{
			//Added for Api Search
			checkStorageContainerAvailablePos(specimenArray,dao,sessionDataBean);
		}
		catch (SMException e)
		{
			throw handleSMException(e);
		}
		
		doUpdateSpecimenArrayContents(specimenArray, dao, sessionDataBean, true);

		dao.insert(specimenArray.getCapacity(), sessionDataBean, true, false);
		dao.insert(specimenArray, sessionDataBean, true, false);
		SpecimenArrayContent specimenArrayContent = null;
		// TODO move this method to HibernateDAOImpl for common use (for collection insertion)
		for (Iterator iter = specimenArray.getSpecimenArrayContentCollection().iterator(); iter
				.hasNext();)
		{
			specimenArrayContent = (SpecimenArrayContent) iter.next();
			specimenArrayContent.setSpecimenArray(specimenArray);
			dao.insert(specimenArrayContent, sessionDataBean, true, false);
		}
	}
	
	public void postInsert(Object obj, DAO dao, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException
	{
		SpecimenArray specimenArray = (SpecimenArray) obj;
		try
		{
			if (specimenArray.getStorageContainer() != null)
			{
				
				Map containerMap = StorageContainerUtil.getContainerMapFromCache();
				StorageContainerUtil.deleteSinglePositionInContainerMap(specimenArray.getStorageContainer(), containerMap, specimenArray
						.getPositionDimensionOne().intValue(), specimenArray.getPositionDimensionTwo().intValue());

			}
		}
		catch (Exception e)
		{

		}

	}
	

	/**
	 * @see edu.wustl.common.bizlogic.AbstractBizLogic#update(edu.wustl.common.dao.DAO, java.lang.Object, java.lang.Object, edu.wustl.common.beans.SessionDataBean)
	 */
	protected void update(DAO dao, Object obj, Object oldObj, SessionDataBean sessionDataBean)
			throws DAOException, UserNotAuthorizedException
	{
		SpecimenArray specimenArray = (SpecimenArray) obj;
		doUpdateSpecimenArrayContents(specimenArray, dao, sessionDataBean, false);
		dao.update(specimenArray.getCapacity(), sessionDataBean, true, false, false);
		dao.update(specimenArray, sessionDataBean, true, false, false);
		SpecimenArrayContent specimenArrayContent = null;
		//SpecimenArray oldSpecimenArray = (SpecimenArray) oldObj;
		Collection oldSpecArrayContents = ((SpecimenArray) oldObj)
				.getSpecimenArrayContentCollection();

		for (Iterator iter = specimenArray.getSpecimenArrayContentCollection().iterator(); iter
				.hasNext();)
		{
			specimenArrayContent = (SpecimenArrayContent) iter.next();
			specimenArrayContent.setSpecimenArray(specimenArray);
			// increment by 1 because of array index starts from 0.
			if (specimenArrayContent.getPositionDimensionOne() != null)
			{
				//Bug: 2365: grid location of parent array was getting changed 
				if(specimenArray.isAliquot())
				{
					specimenArrayContent.setPositionDimensionOne(new Integer(specimenArrayContent
							.getPositionDimensionOne().intValue()));
					specimenArrayContent.setPositionDimensionTwo(new Integer(specimenArrayContent
							.getPositionDimensionTwo().intValue()));
				}
				else
				{
					specimenArrayContent.setPositionDimensionOne(new Integer(specimenArrayContent
							.getPositionDimensionOne().intValue() + 1));
					specimenArrayContent.setPositionDimensionTwo(new Integer(specimenArrayContent
							.getPositionDimensionTwo().intValue() + 1));
				}
			}

			if (isNewSpecimenArrayContent(specimenArrayContent, oldSpecArrayContents))
			{
				dao.insert(specimenArrayContent, sessionDataBean, true, false);
			}
			else
			{
				dao.update(specimenArrayContent, sessionDataBean, true, false, false);
			}
		}
	}

	/**
	 * @param specimenArrayContent array contents
	 * @param oldSpecArrayContents old spec array contents
	 * @return whether it is new or old
	 */
	private boolean isNewSpecimenArrayContent(SpecimenArrayContent specimenArrayContent,
			Collection oldSpecArrayContents)
	{
		boolean isNew = true;
		SpecimenArrayContent arrayContent = null;

		for (Iterator iter = oldSpecArrayContents.iterator(); iter.hasNext();)
		{
			arrayContent = (SpecimenArrayContent) iter.next();

			if (specimenArrayContent.getId() == null)
			{
				isNew = true;
				break;
			}
			else if (arrayContent.getId().longValue() == specimenArrayContent.getId().longValue())
			{
				isNew = false;
				break;
			}
		}
		return isNew;
	}

	/**
	 * @param specimenArray specimen array
	 * @param dao dao
	 * @param sessionDataBean session data bean
	 * @param isInsertOperation is insert operation
	 * @throws DAOException 
	 * @throws UserNotAuthorizedException
	 */
	private void doUpdateSpecimenArrayContents(SpecimenArray specimenArray, DAO dao,
			SessionDataBean sessionDataBean, boolean isInsertOperation) throws DAOException,
			UserNotAuthorizedException
	{
		Collection specimenArrayContentCollection = specimenArray
				.getSpecimenArrayContentCollection();
		Collection updatedSpecArrayContentCollection = new HashSet(); 
		SpecimenArrayContent specimenArrayContent = null;
		Specimen specimen = null;
//		try
//		{
//			checkStorageContainerAvailablePos(specimenArray,dao,sessionDataBean);
//		}
//		catch (SMException e)
//		{
//			throw handleSMException(e);
//		}
		
		if (specimenArrayContentCollection != null && !specimenArrayContentCollection.isEmpty())
		{
			double quantity = 0.0;
			// fetch array type to check specimen class
			List arrayTypes = dao.retrieve(SpecimenArrayType.class.getName(),
					Constants.SYSTEM_IDENTIFIER, specimenArray.getSpecimenArrayType().getId());
			SpecimenArrayType arrayType = null;

			if ((arrayTypes != null) && (!arrayTypes.isEmpty()))
			{
				arrayType = (SpecimenArrayType) arrayTypes.get(0);
			}

			for (Iterator iter = specimenArrayContentCollection.iterator(); iter.hasNext();)
			{
				specimenArrayContent = (SpecimenArrayContent) iter.next();
				
				//Added by jitendra 
				if(specimenArrayContent.getPositionDimensionOne() == null || specimenArrayContent.getPositionDimensionTwo() == null)
				{
					throw new DAOException(ApplicationProperties.getValue("array.contentPosition.err.msg"));
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
				//ApiSearchUtil.setSpecimenArrayContentDefault(specimenArrayContent);
				//End:- Change for API Search 
				
				specimen = getSpecimen(dao, specimenArrayContent);
				if (specimen != null)
				{
					// check whether array & specimen are compatible on the basis of class
					if (!isArrayAndSpecimenCompatibile(arrayType, specimen))
					{
						throw new DAOException(
								Constants.ARRAY_SPEC_NOT_COMPATIBLE_EXCEPTION_MESSAGE);
					}

					// set quantity object to null when there is no value.. [due to Hibernate exception]
					if (specimenArrayContent.getInitialQuantity() != null)
					{
						if (specimenArrayContent.getInitialQuantity().getValue() == null)
						{
							specimenArrayContent.setInitialQuantity(null);
						}
					}

					// if molecular then check available quantity
					if (specimen instanceof MolecularSpecimen)
					{
						if (specimenArrayContent.getInitialQuantity() != null)
						{
							quantity = specimenArrayContent.getInitialQuantity().getValue()
									.doubleValue();
							// incase if specimenArray is created from aliquot page, then skip the Available quantity of specimen. 
							if (!specimenArray.isAliquot())
							{
								if (!isAvailableQty(specimen, quantity))
								{
									throw new DAOException(
											" Quantity '"
													+ quantity
													+ "' should be less than current Distributed Quantity '"
													+ specimen.getAvailableQuantity().getValue()
															.doubleValue() + "' of specimen :: "
													+ specimen.getLabel());
								}
							}
							if (specimenArrayContent.getInitialQuantity().getId() == null)
							{
								dao.insert(specimenArrayContent.getInitialQuantity(),
										sessionDataBean, true, false);
							}
							else
							{
								dao.update(specimenArrayContent.getInitialQuantity(),
										sessionDataBean, true, false, false);
							}
						}
						else
						{
							throw new DAOException(Constants.ARRAY_MOLECULAR_QUAN_EXCEPTION_MESSAGE
									+ specimen.getLabel());
						}
					}
					specimenArrayContent.setSpecimen(specimen);
					updatedSpecArrayContentCollection.add(specimenArrayContent);
				}
			}
		}
		
		// There should be at least one valid specimen in array
		if (updatedSpecArrayContentCollection.isEmpty())
		{
			throw new DAOException(Constants.ARRAY_NO_SPECIMEN__EXCEPTION_MESSAGE);
		}
		
		specimenArray.setSpecimenArrayContentCollection(updatedSpecArrayContentCollection); 
	}

	
	private void checkStorageContainerAvailablePos(SpecimenArray specimenArray,DAO dao,SessionDataBean sessionDataBean) 
												  throws DAOException,SMException
	{
		if (specimenArray.getStorageContainer() != null)
		{
			if(specimenArray.getStorageContainer().getId() != null)
			{
				Object containerObj = dao.retrieve(StorageContainer.class.getName(), specimenArray.getStorageContainer().getId());
				if (containerObj != null)
				{
					StorageContainer container = (StorageContainer) containerObj;
					// check for closed Storage Container
					checkStatus(dao, container, "Storage Container");
	
					StorageContainerBizLogic storageContainerBizLogic = (StorageContainerBizLogic) BizLogicFactory.getInstance().getBizLogic(
							Constants.STORAGE_CONTAINER_FORM_ID);
	
					// --- check for all validations on the storage container.
					storageContainerBizLogic.checkContainer(dao, container.getId().toString(), specimenArray.getPositionDimensionOne().toString(), specimenArray
							.getPositionDimensionTwo().toString(), sessionDataBean);
	
					specimenArray.setStorageContainer(container);
				}
				else
				{
					throw new DAOException(ApplicationProperties.getValue("errors.storageContainerExist"));
				}
			}
		}		
	}
	/**
	 * @param specimen specimen
	 * @param quantity quantity
	 * @return whether the quantity is available.
	 */
	private boolean isAvailableQty(Specimen specimen, double quantity)
	{

		if (specimen instanceof MolecularSpecimen)
		{
			MolecularSpecimen molecularSpecimen = (MolecularSpecimen) specimen;
			double availabeQty = Double.parseDouble(molecularSpecimen.getAvailableQuantity()
					.toString());//molecularSpecimen.getAvailableQuantityInMicrogram().doubleValue();
			if (quantity > availabeQty)
				return false;
			else
			{
				availabeQty = availabeQty - quantity;
				molecularSpecimen.setAvailableQuantity(new QuantityInMicrogram(availabeQty));//molecularSpecimen.setAvailableQuantityInMicrogram(new Double(availabeQty));
			}
		}
		return true;
	}

	/**
	 * @param dao dao
	 * @param arrayContent
	 * @return
	 * @throws DAOException
	 */
	private Specimen getSpecimen(DAO dao, SpecimenArrayContent arrayContent) throws DAOException
	{
		//get list of Participant's names
		Specimen specimen = arrayContent.getSpecimen();

		if (specimen != null)
		{
			String columnName = null;
			String columnValue = null;

			if ((specimen.getLabel() != null) && (!specimen.getLabel().trim().equals("")))
			{
				columnName = Constants.SPECIMEN_LABEL_COLUMN_NAME;
				columnValue = specimen.getLabel();
			}
			else if ((specimen.getBarcode() != null) && (!specimen.getBarcode().trim().equals("")))
			{
				columnName = Constants.SPECIMEN_BARCODE_COLUMN_NAME;
				columnValue = specimen.getBarcode();
			}
			else
			{
				return null;
			}
			String sourceObjectName = Specimen.class.getName();
			String whereColumnName = columnName;
			String whereColumnValue = columnValue;

			List list = dao.retrieve(sourceObjectName, whereColumnName, whereColumnValue);
			if (!list.isEmpty())
			{
				specimen = (Specimen) list.get(0);
				//return specimenCollectionGroup;
			}
			else
			{
				throw new DAOException(Constants.ARRAY_SPECIMEN_DOES_NOT_EXIST_EXCEPTION_MESSAGE
						+ columnValue);
			}
		}
		return specimen;
	}

	/**
	 * @param array array
	 * @param specimen specimen 
	 * @return true if compatible else false 
	 *  |
	 *  | 
	 *   ----- on the basis of specimen class
	 */
	private boolean isArrayAndSpecimenCompatibile(SpecimenArrayType arrayType, Specimen specimen)
	{
		boolean compatible = false;
		String arraySpecimenClassName = arrayType.getSpecimenClass();
		String specSpecimenClassName = getClassName(specimen);

		if (arraySpecimenClassName.equals(specSpecimenClassName))
		{
			compatible = true;
		}
		return compatible;
	}

	/**
	 * This function returns the actual type of the specimen i.e Cell / Fluid / Molecular / Tissue.
	 */

	public final String getClassName(Specimen specimen)
	{
		String className = "";

		if (specimen instanceof CellSpecimen)
		{
			className = Constants.CELL;
		}
		else if (specimen instanceof MolecularSpecimen)
		{
			className = Constants.MOLECULAR;
		}
		else if (specimen instanceof FluidSpecimen)
		{
			className = Constants.FLUID;
		}
		else if (specimen instanceof TissueSpecimen)
		{
			className = Constants.TISSUE;
		}
		return className;
	}

	/**
	 * Overriding the parent class's method to validate the enumerated attribute values
	 */
	protected boolean validate(Object obj, DAO dao, String operation) throws DAOException
	{
		SpecimenArray specimenArray = (SpecimenArray) obj;		
	
		/**
		 * Start: Change for API Search   --- Jitendra 06/10/2006
		 * In Case of Api Search, previoulsy it was failing since there was default class level initialization 
		 * on domain object. For example in User object, it was initialized as protected String lastName=""; 
		 * So we removed default class level initialization on domain object and are initializing in method
		 * setAllValues() of domain object. But in case of Api Search, default values will not get set 
		 * since setAllValues() method of domainObject will not get called. To avoid null pointer exception,
		 * we are setting the default values same as we were setting in setAllValues() method of domainObject.
		 */		
		ApiSearchUtil.setSpecimenArrayDefault(specimenArray);
		//End:- Change for API Search
		
		//Added by Ashish	
		if(specimenArray == null)
		{
			throw new DAOException(ApplicationProperties.getValue("domain.object.null.err.msg", "Specimen Array"));
		}
		
		Validator validator = new Validator();
		String message="";
		if (specimenArray.getSpecimenArrayType()== null || specimenArray.getSpecimenArrayType().getId()== null || specimenArray.getSpecimenArrayType().getId().longValue() == -1)
		{
			message = ApplicationProperties.getValue("array.arrayType");
			throw new DAOException(ApplicationProperties.getValue("errors.item.required",message));	
		}
		
		//fetch array type to check specimen class
		List arrayTypes = dao.retrieve(SpecimenArrayType.class.getName(),
				Constants.SYSTEM_IDENTIFIER, specimenArray.getSpecimenArrayType().getId());
		SpecimenArrayType specimenArrayType = null;

		if ((arrayTypes != null) && (!arrayTypes.isEmpty()))
		{
			specimenArrayType = (SpecimenArrayType) arrayTypes.get(0);
		}
		else
		{
			message = ApplicationProperties.getValue("array.arrayType");
			throw new DAOException(ApplicationProperties.getValue("errors.invalid",message));
		}
		
		//	validate name of array
		if (validator.isEmpty(specimenArray.getName()))
		{
			message = ApplicationProperties.getValue("array.arrayLabel");
			throw new DAOException(ApplicationProperties.getValue("errors.item.required",message));	
		}

		// validate storage position
		if (specimenArray.getPositionDimensionOne() == null || specimenArray.getPositionDimensionTwo() == null
				|| !validator.isNumeric(String.valueOf(specimenArray.getPositionDimensionOne()), 1)
				|| !validator.isNumeric(String.valueOf(specimenArray.getPositionDimensionTwo()), 1)
				|| !validator.isNumeric(
						String.valueOf(specimenArray.getStorageContainer().getId()), 1))
		{
			message = ApplicationProperties.getValue("array.positionInStorageContainer");
			throw new DAOException(ApplicationProperties.getValue("errors.item.format",message));	
		}

		// validate user 
		if (specimenArray.getCreatedBy()== null || specimenArray.getCreatedBy().getId()== null || !validator.isValidOption(String.valueOf(specimenArray.getCreatedBy().getId())))
		{
			message = ApplicationProperties.getValue("array.user");
			throw new DAOException(ApplicationProperties.getValue("errors.item.required",message));
		}
		
		//validate capacity 
		if (specimenArray.getCapacity() == null || specimenArray.getCapacity().getOneDimensionCapacity()== null || 
				specimenArray.getCapacity().getTwoDimensionCapacity()==null )
		{			
			throw new DAOException(ApplicationProperties.getValue("array.capacity.err.msg"));
		}
		
		List specimenClassList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_SPECIMEN_CLASS, null);
		String specimenClass = specimenArrayType.getSpecimenClass();
		
		if (!isValidClassName(specimenClass))
		{
			throw new DAOException(ApplicationProperties.getValue("protocol.class.errMsg"));
		}

		if (!Validator.isEnumeratedValue(specimenClassList, specimenClass))
		{
			throw new DAOException(ApplicationProperties.getValue("protocol.class.errMsg"));
		}
		
		Collection specimenTypes = specimenArrayType.getSpecimenTypeCollection();
		if(specimenTypes == null || specimenTypes.isEmpty())
		{			
			throw new DAOException(ApplicationProperties.getValue("protocol.type.errMsg"));
		}
		else
		{
			Iterator itr = specimenTypes.iterator();
			while(itr.hasNext())
			{
				String specimenType = (String) itr.next();
				if (!Validator.isEnumeratedValue(Utility.getSpecimenTypes(specimenClass), specimenType))
				{
					throw new DAOException(ApplicationProperties.getValue("protocol.type.errMsg"));
				}
			}
		}		
		return true;
	}
	
	private boolean isValidClassName(String className)
	{
		if ((className != null) && (className.equalsIgnoreCase(Constants.CELL)) || (className.equalsIgnoreCase(Constants.MOLECULAR)) ||
				(className.equalsIgnoreCase(Constants.FLUID)) || (className.equalsIgnoreCase(Constants.TISSUE)))
		{
			return true;
		}
		return false;
	}
	
	/**
	 * get Unique index to be appended to Name
	 * @return unique no. to be appended to array name
	 * @throws DAOException
	 */
	public int getUniqueIndexForName() throws DAOException
	{
		String sourceObjectName = "CATISSUE_CONTAINER";
		String[] selectColumnName = {"max(IDENTIFIER) as MAX_IDENTIFIER"};
		AbstractDAO dao = DAOFactory.getInstance().getDAO(Constants.JDBC_DAO);

		dao.openSession(null);
		List list = dao.retrieve(sourceObjectName, selectColumnName);
		dao.closeSession();

		if (!list.isEmpty())
		{
			List columnList = (List) list.get(0);
			if (!columnList.isEmpty())
			{
				String str = (String) columnList.get(0);
				if (!str.equals(""))
				{
					int no = Integer.parseInt(str);
					return no + 1;
				}
			}
		}
		return 1;
	}
	//END
}
