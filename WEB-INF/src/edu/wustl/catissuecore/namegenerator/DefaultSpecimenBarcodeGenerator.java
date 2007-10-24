package edu.wustl.catissuecore.namegenerator;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import edu.wustl.common.dao.DAOFactory;
import edu.wustl.common.dao.JDBCDAO;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.util.global.Variables;
import edu.wustl.catissuecore.util.global.Constants;
import java.util.Collection;
/**
 * DefaultSpecimenBarcodeGenerator is a class which contains the default 
 * implementations AbstractSpecimenGenerator classe.
 * @author virender_mehta
 */
public class DefaultSpecimenBarcodeGenerator implements BarcodeGenerator
{
	/**
	 * Current Barcode 
	 */
	protected Long currentBarcode;
	
	/**
	 * Default Constructor
	 */
	public DefaultSpecimenBarcodeGenerator()
	{
		super();
		init();
	}
	
	/**
	 * This is a init() function it is called from the default constructor of Base class.When getInstance of base class
	 * called then this init function will be called.
	 * This method will first check the Datatbase Name and then set function name that will convert
	 * lable from int to String
	 */
	protected void init()
	{
		if(Variables.databaseName.equals(Constants.ORACLE_DATABASE))
		{
			currentBarcode = getLastAvailableSpecimenBarcode(Constants.ORACLE_NUM_TO_STR_FUNCTION_NAME_FOR_LABEL_GENRATION);
		}
		else
		{
			currentBarcode = getLastAvailableSpecimenBarcode(Constants.MYSQL_NUM_TO_STR_FUNCTION_NAME_FOR_LABEL_GENRATION);
		}
	}
	
	/**
	 * This method will retrive unique specimen Lable.
	 * @return Total No of Specimen
	 * @throws ClassNotFoundException
	 * @throws DAOException 
	 */
	private Long getLastAvailableSpecimenBarcode(String databaseConstant)  
	{
		String sql = "select MAX("+databaseConstant+") from CATISSUE_SPECIMEN";
 		JDBCDAO jdbcDao = (JDBCDAO)DAOFactory.getInstance().getDAO(Constants.JDBC_DAO);
        Long noOfRecords = new Long("0");
        try
		{
        	jdbcDao.openSession(null);
        	List resultList = jdbcDao.executeQuery(sql,null,false,null);
        	if(resultList!=null&&!resultList.isEmpty())
        	{
        		String number = (String)((List)resultList.get(0)).get(0);
	        	if(number!=null && !number.equals(""))
	        	{
	        		noOfRecords = Long.parseLong(number);
	        	}
        	}
	        jdbcDao.closeSession();
		}
        catch(DAOException daoexception)
		{
        	daoexception.printStackTrace();
		}
        catch(ClassNotFoundException classnotfound)
        {
        	classnotfound.printStackTrace();
        }
        return noOfRecords;
	}

	
	
	/**Functions for Tree base new specimen entry 's label generation***/
	//Falguni...
	
	Map barcodeCountTreeMap = new HashMap();
	
	
	/**
	 * @param parentObject
	 * @param specimenObject
	 */
	public synchronized  void setNextAvailableAliquotSpecimenBarcode(Specimen parentObject,Specimen specimenObject) {
				
		String parentSpecimenBarcode = (String) parentObject.getBarcode();
		long aliquotChildCount = 0;
		if(barcodeCountTreeMap.containsKey(parentObject))
		{
			 aliquotChildCount= Long.parseLong(barcodeCountTreeMap.get(parentObject).toString());	
		}
		else
		{
			// biz logic 
			aliquotChildCount = parentObject.getChildrenSpecimen().size();	
			
		}
		
		specimenObject.setBarcode( parentSpecimenBarcode + "_" + (++aliquotChildCount) );
		barcodeCountTreeMap.put(parentObject,aliquotChildCount);	
		barcodeCountTreeMap.put(specimenObject,0);
	}
	/**
	 * @param parentObject
	 * @param specimenObject
	 */
	private void setNextAvailableDeriveSpecimenBarcode(Specimen parentObject, Specimen specimenObject) {
		
		currentBarcode= currentBarcode+1;
		specimenObject.setBarcode(currentBarcode.toString());
		barcodeCountTreeMap.put(specimenObject,0);
	}
	
	
	
	public void setBarcode(AbstractDomainObject obj) {
		
		Specimen objSpecimen = (Specimen)obj;

		if(!barcodeCountTreeMap.containsKey(objSpecimen) &&	objSpecimen.getLineage().equals(Constants.NEW_SPECIMEN))				
		{
			currentBarcode= currentBarcode+1;
			objSpecimen.setBarcode(currentBarcode.toString());
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
	 * @see edu.wustl.catissuecore.namegenerator.LabelGenerator#setBarcode(java.util.List)
	 */
	public void setBarcode(List<AbstractDomainObject> objSpecimenList) {

		List specimenList = objSpecimenList;
		for (int index=0;index <specimenList.size();index++) 
		{
			Specimen objSpecimen = (Specimen)specimenList.get(index);
			setBarcode(objSpecimen);
		}
		
	}

}
