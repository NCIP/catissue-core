package edu.wustl.catissuecore.namegenerator;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.dao.DAOFactory;
import edu.wustl.common.dao.JDBCDAO;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.dbManager.DAOException;

/**
 * SpecimenBarcodeGenerator for Michgan University.
 * 
 */
public class SpecimenBarcodeGeneratorForMichigan extends DefaultSpecimenBarcodeGenerator {

	public SpecimenBarcodeGeneratorForMichigan() {
		//init();//TODO :Commented by Falguni because we are not using separate table for Michigan ,as not able to persist barcode count. 
		super();
	}

	/**
	 * This is a init() function it is called from the default constructor of
	 * Base class. When getInstance of base class called then this init function
	 * will be called. This method will first check the Datatbase Name and then
	 * set function name that will convert lable from int to String
	 */
	protected void init() 
	{
		String sql = "select MAX(LABEL_COUNT) from CATISSUE_SPECIMEN_LABEL_COUNT";
		JDBCDAO jdbcDao = (JDBCDAO) DAOFactory.getInstance().getDAO(
				Constants.JDBC_DAO);
		currentBarcode = new Long("0");
		try {
			jdbcDao.openSession(null);
			List resultList = jdbcDao.executeQuery(sql, null, false, null);
			if (resultList != null && !resultList.isEmpty()) {
				String number = (String) ((List) resultList.get(0)).get(0);
				if (number != null && !number.equals("")) {
					currentBarcode = Long.parseLong(number);
				}
			}
			jdbcDao.closeSession();
		} catch (DAOException daoexception) {
			daoexception.printStackTrace();
		} catch (ClassNotFoundException classnotfound) {
			classnotfound.printStackTrace();
		}
	}

	
	/**
	 * @param input
	 * @param pattern
	 * @return
	 */
	private String format(long input, String pattern) 
	{
		DecimalFormat df = new DecimalFormat(pattern);
		return df.format(input);
	}

	
	private void persistLabelCount() 
	{
		String sql = "update CATISSUE_SPECIMEN_LABEL_COUNT SET LABEL_COUNT='"
				+ currentBarcode + "'";

		JDBCDAO jdbcDao = (JDBCDAO) DAOFactory.getInstance().getDAO(
				Constants.JDBC_DAO);
		try {
			jdbcDao.openSession(null);
			jdbcDao.executeUpdate(sql);
			jdbcDao.closeSession();
		} catch (DAOException daoexception) {
			daoexception.printStackTrace();
		}
	}
	/**
	 * This function is overridden as per Michgam requirement. 
	 */
	
	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.namegenerator.DefaultSpecimenBarcodeGenerator#setBarcode(edu.wustl.common.domain.AbstractDomainObject)
	 */
	public void setBarcode(AbstractDomainObject obj) {
		
		Specimen objSpecimen = (Specimen)obj;

		if(!barcodeCountTreeMap.containsKey(objSpecimen) &&	objSpecimen.getLineage().equals(Constants.NEW_SPECIMEN))				
		{
			String siteName = objSpecimen.getSpecimenCollectionGroup().getGroupName();
			currentBarcode = currentBarcode + 1;

			String year = new SimpleDateFormat("yy").format(new Date());
			String day = format(Calendar.getInstance().get(Calendar.DAY_OF_YEAR),
					"000");
			String nextNumber = format(currentBarcode, "0000");

			//TODO :Commented by Falguni because hibernate session is getting closed by calling this method. 
			//persistLabelCount();
			//String label = siteName + "-" + year + "-" + day + "-" + nextNumber;
			//Modification suggested for Michigan only -as per catissuecore 1.2.0.1
			String barcode = siteName + "_" + nextNumber;
			objSpecimen.setBarcode(barcode);
			barcodeCountTreeMap.put(objSpecimen,0);
		}
	
	
		else if(!barcodeCountTreeMap.containsKey(objSpecimen) && objSpecimen.getLineage().equals(Constants.ALIQUOT))				
		{
			setNextAvailableAliquotSpecimenBarcode(objSpecimen.getParentSpecimen(),objSpecimen);
		}
	
	
		else if(!barcodeCountTreeMap.containsKey(objSpecimen) && objSpecimen.getLineage().equals(Constants.DERIVED_SPECIMEN))				
		{
			setNextAvailableDeriveSpecimenBarcode(objSpecimen.getParentSpecimen(),objSpecimen);
		}
		
		if(objSpecimen.getChildrenSpecimen().size()>0)
		{
			Collection specimenCollection = objSpecimen.getChildrenSpecimen();
			Iterator it = specimenCollection.iterator();
			while(it.hasNext())
			{
				Specimen objChildSpecimen = (Specimen)it.next();
				setBarcode(objChildSpecimen);
			}
			
		}	
		
	}
	
	
	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.namegenerator.DefaultSpecimenBarcodeGenerator#setNextAvailableDeriveSpecimenBarcode(edu.wustl.catissuecore.domain.Specimen, edu.wustl.catissuecore.domain.Specimen)
	 */
	synchronized void setNextAvailableDeriveSpecimenBarcode(Specimen parentObject,Specimen specimenObject)
	{
					
		String parentSpecimenBarcode = (String) parentObject.getBarcode();				
		long aliquotCount = parentObject.getChildrenSpecimen().size();
		specimenObject.setBarcode(parentSpecimenBarcode + "_" + (format((aliquotCount + 1), "00")));
		barcodeCountTreeMap.put(specimenObject,0);
	}

	/**
	 * This function is overridden as per Michgam requirement. 
	 */
	synchronized void setNextAvailableAliquotSpecimenBarcode(Specimen parentObject, Specimen specimenObject) 
	{
		
		String parentSpecimenBarcode = (String) parentObject.getBarcode();
		long aliquotCount =  0;
		if(barcodeCountTreeMap.containsKey(parentObject))
		{
			aliquotCount= Long.parseLong(barcodeCountTreeMap.get(parentObject).toString());	
		}
		else
		{
			// biz logic 
			aliquotCount = parentObject.getChildrenSpecimen().size();	
			
		}		
		specimenObject.setBarcode( parentSpecimenBarcode + "_"+ format((++aliquotCount), "00"));
		barcodeCountTreeMap.put(parentObject,aliquotCount);	
		barcodeCountTreeMap.put(specimenObject,0);
		
	}
	
}
