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

import edu.wustl.catissuecore.domain.CellSpecimen;
import edu.wustl.catissuecore.domain.FluidSpecimen;
import edu.wustl.catissuecore.domain.MolecularSpecimen;
import edu.wustl.catissuecore.domain.QuantityInMicrogram;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenArray;
import edu.wustl.catissuecore.domain.SpecimenArrayContent;
import edu.wustl.catissuecore.domain.SpecimenArrayType;
import edu.wustl.catissuecore.domain.TissueSpecimen;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.dao.DAO;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.dbManager.DAOException;


/**
 * <p>This class initializes the fields of SpecimenArrayBizLogic.java</p>
 * @author Ashwin Gupta
 * @version 1.1
 */
public class SpecimenArrayBizLogic extends DefaultBizLogic {
	
	/**
	 * @see edu.wustl.common.bizlogic.AbstractBizLogic#insert(java.lang.Object, edu.wustl.common.dao.DAO, edu.wustl.common.beans.SessionDataBean)
	 */
	protected void insert(Object obj, DAO dao, SessionDataBean sessionDataBean) 
					throws DAOException, UserNotAuthorizedException 
	{
		SpecimenArray specimenArray = (SpecimenArray) obj;
		doUpdateSpecimenArrayContents(specimenArray,dao,sessionDataBean,true);
		
		dao.insert(specimenArray.getCapacity(),sessionDataBean, true, false);
		dao.insert(specimenArray,sessionDataBean,true,false);
		SpecimenArrayContent specimenArrayContent = null;
		// TODO move this method to HibernateDAOImpl for common use (for collection insertion)
		for (Iterator iter = specimenArray.getSpecimenArrayContentCollection().iterator(); iter.hasNext();) 
		{
			specimenArrayContent = (SpecimenArrayContent) iter.next();
			specimenArrayContent.setSpecimenArray(specimenArray);
			dao.insert(specimenArrayContent,sessionDataBean,true,false);
		}
	}
	
	/**
	 * @see edu.wustl.common.bizlogic.AbstractBizLogic#update(edu.wustl.common.dao.DAO, java.lang.Object, java.lang.Object, edu.wustl.common.beans.SessionDataBean)
	 */
	protected void update(DAO dao, Object obj, Object oldObj, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException {
		SpecimenArray specimenArray = (SpecimenArray) obj;
		doUpdateSpecimenArrayContents(specimenArray,dao,sessionDataBean,false);
		dao.update(specimenArray.getCapacity(),sessionDataBean, true, false,false);
		dao.update(specimenArray,sessionDataBean, true, false,false);
		SpecimenArrayContent specimenArrayContent = null;
		//SpecimenArray oldSpecimenArray = (SpecimenArray) oldObj;
		Collection oldSpecArrayContents = ((SpecimenArray) oldObj).getSpecimenArrayContentCollection(); 
		
		for (Iterator iter = specimenArray.getSpecimenArrayContentCollection().iterator(); iter.hasNext();) 
		{
			specimenArrayContent = (SpecimenArrayContent) iter.next();
			specimenArrayContent.setSpecimenArray(specimenArray);
			// increment by 1 because of array index starts from 0.
			if (specimenArrayContent.getPositionDimensionOne() != null)
			{	
				specimenArrayContent.setPositionDimensionOne(new Integer(specimenArrayContent.getPositionDimensionOne().intValue() + 1));
				specimenArrayContent.setPositionDimensionTwo(new Integer(specimenArrayContent.getPositionDimensionTwo().intValue() + 1));
			}
			
			if (isNewSpecimenArrayContent(specimenArrayContent,oldSpecArrayContents)) 
			{
				dao.insert(specimenArrayContent,sessionDataBean,true,false);
			}
			else
			{
				dao.update(specimenArrayContent,sessionDataBean,true,false,false);
			}
		}
	}
	
	/**
	 * @param specimenArrayContent array contents
	 * @param oldSpecArrayContents old spec array contents
	 * @return whether it is new or old
	 */
	private boolean isNewSpecimenArrayContent(SpecimenArrayContent specimenArrayContent,Collection oldSpecArrayContents)
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
			} else if (arrayContent.getId().longValue() == specimenArrayContent.getId().longValue()) 
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
	private void doUpdateSpecimenArrayContents(SpecimenArray specimenArray,DAO dao,SessionDataBean sessionDataBean,boolean isInsertOperation)
				 throws DAOException, UserNotAuthorizedException	
	{
		Collection specimenArrayContentCollection = specimenArray.getSpecimenArrayContentCollection();
		Collection updatedSpecArrayContentCollection = new HashSet();
		SpecimenArrayContent specimenArrayContent = null;
		Specimen specimen = null;
		if (specimenArrayContentCollection != null && !specimenArrayContentCollection.isEmpty()) 
		{
			double quantity = 0.0;
			// fetch array type to check specimen class
			List arrayTypes = dao.retrieve(SpecimenArrayType.class.getName(),Constants.SYSTEM_IDENTIFIER,specimenArray.getSpecimenArrayType().getId());
			SpecimenArrayType arrayType = null;
			
			if ((arrayTypes != null) && (!arrayTypes.isEmpty())) {
				arrayType = (SpecimenArrayType) arrayTypes.get(0);
			}
			
			for (Iterator iter = specimenArrayContentCollection.iterator(); iter.hasNext();) 
			{
				specimenArrayContent = (SpecimenArrayContent) iter.next();
				specimen = getSpecimen(dao,specimenArrayContent);
				if(specimen != null)
				{
					 // check whether array & specimen are compatible on the basis of class
					 if (!isArrayAndSpecimenCompatibile(arrayType,specimen))
					 {
						 throw new DAOException(Constants.ARRAY_SPEC_NOT_COMPATIBLE_EXCEPTION_MESSAGE);
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
							  quantity = specimenArrayContent.getInitialQuantity().getValue().doubleValue();
								 // incase if specimenArray is created from aliquot page, then skip the Available quantity of specimen. 
							  if(!specimenArray.isAliquot()) 
							  {
								  if (!isAvailableQty(specimen,quantity))
								  {
										  throw new DAOException(" Quantity '" + quantity + "' should be less than current Distributed Quantity '" + specimen.getAvailableQuantity().getValue().doubleValue() + "' of specimen :: " + specimen.getLabel());				  	
								  }
							  }
							  if (specimenArrayContent.getInitialQuantity().getId() == null) 
							  {
								dao.insert(specimenArrayContent.getInitialQuantity(),sessionDataBean,true,false);
							  } else {
								dao.update(specimenArrayContent.getInitialQuantity(),sessionDataBean,true,false,false);  
							  }
						 	}
						 	else
						 	{
						 		throw new DAOException(Constants.ARRAY_MOLECULAR_QUAN_EXCEPTION_MESSAGE + specimen.getLabel());						 		
						 	}
					 }
					 specimenArrayContent.setSpecimen(specimen);
					 updatedSpecArrayContentCollection.add(specimenArrayContent);
				}
			}
			
			// There should be at least one valid specimen in array
			if (updatedSpecArrayContentCollection.isEmpty()) {
				throw new DAOException(Constants.ARRAY_NO_SPECIMEN__EXCEPTION_MESSAGE);
			}
		}
		specimenArray.setSpecimenArrayContentCollection(updatedSpecArrayContentCollection);
	}
	
	/**
	 * @param specimen specimen
	 * @param quantity quantity
	 * @return whether the quantity is available.
	 */
	private boolean isAvailableQty(Specimen specimen, double quantity)
	{
		
		if(specimen instanceof MolecularSpecimen)
		{
			MolecularSpecimen molecularSpecimen = (MolecularSpecimen) specimen;
			double availabeQty = Double.parseDouble(molecularSpecimen.getAvailableQuantity().toString());//molecularSpecimen.getAvailableQuantityInMicrogram().doubleValue();
			if(quantity > availabeQty)
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
	private Specimen getSpecimen(DAO dao,SpecimenArrayContent arrayContent) throws DAOException
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
			} else if ((specimen.getBarcode() != null) && (!specimen.getBarcode().trim().equals(""))) 
			{
				columnName = Constants.SPECIMEN_BARCODE_COLUMN_NAME;
				columnValue = specimen.getBarcode();
			} else {
				return null;
			}
			String sourceObjectName = Specimen.class.getName();
			String whereColumnName = columnName;
			String whereColumnValue = columnValue;
	
			List list = dao.retrieve(sourceObjectName, whereColumnName,whereColumnValue);
			if (!list.isEmpty())
			{
				specimen = (Specimen) list.get(0);
				//return specimenCollectionGroup;
			} else {
				throw new DAOException(Constants.ARRAY_SPECIMEN_DOES_NOT_EXIST_EXCEPTION_MESSAGE + columnValue);
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
	private boolean isArrayAndSpecimenCompatibile(SpecimenArrayType arrayType,Specimen specimen) {
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
}
