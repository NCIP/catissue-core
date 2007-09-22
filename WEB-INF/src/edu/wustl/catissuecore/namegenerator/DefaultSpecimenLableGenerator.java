package edu.wustl.catissuecore.namegenerator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Variables;
import edu.wustl.common.dao.DAOFactory;
import edu.wustl.common.dao.JDBCDAO;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.dbManager.DAOException;


/**
 * DefaultSpecimenLabelGenerator is a class which contains the default 
 * implementations AbstractSpecimenGenerator classe.
 * @author virender_mehta
 */
public class DefaultSpecimenLableGenerator implements LabelGenerator
{
	/**
	 * Current label 
	 */
	protected Long currentLable;
	
	/**
	 * Default Constructor
	 */
	public DefaultSpecimenLableGenerator()
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
			currentLable = getLastAvailableSpecimenLabel(Constants.ORACLE_NUM_TO_STR_FUNCTION_NAME_FOR_LABEL_GENRATION);
		}
		else
		{
			currentLable = getLastAvailableSpecimenLabel(Constants.MYSQL_NUM_TO_STR_FUNCTION_NAME_FOR_LABEL_GENRATION);
		}
	}
	
	/**
	 * This method will retrive unique specimen Lable.
	 * @return Total No of Specimen
	 * @throws ClassNotFoundException
	 * @throws DAOException 
	 */
	private Long getLastAvailableSpecimenLabel(String databaseConstant)  
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
	
	Map labelCountTreeMap = new HashMap();
	
	
	/**
	 * @param parentObject
	 * @param specimenObject
	 */
	private synchronized  void setNextAvailableAliquotSpecimenlabel(Specimen parentObject,Specimen specimenObject) {
				
		String parentSpecimenLabel = (String) parentObject.getLabel();
		long aliquotChildCount = 0;
		if(labelCountTreeMap.containsKey(parentObject))
		{
			 aliquotChildCount= Long.parseLong(labelCountTreeMap.get(parentObject).toString());	
		}
		else
		{
			// biz logic 
			aliquotChildCount = parentObject.getChildrenSpecimen().size();	
			
		}
		
		specimenObject.setLabel( parentSpecimenLabel + "_" + (++aliquotChildCount) );
		labelCountTreeMap.put(parentObject,aliquotChildCount);	
		labelCountTreeMap.put(specimenObject,0);
	}
	/**
	 * @param parentObject
	 * @param specimenObject
	 */
	private void setNextAvailableDeriveSpecimenlabel(Specimen parentObject, Specimen specimenObject) {
		
		currentLable= currentLable+1;
		specimenObject.setLabel(currentLable.toString());
		labelCountTreeMap.put(specimenObject,0);
	}
	
	
	
	public void setLabel(AbstractDomainObject obj) {
		
		Specimen objSpecimen = (Specimen)obj;

		if(!labelCountTreeMap.containsKey(objSpecimen) &&	objSpecimen.getLineage().equals(Constants.NEW_SPECIMEN))				
		{
			currentLable= currentLable+1;
			objSpecimen.setLabel(currentLable.toString());
			labelCountTreeMap.put(objSpecimen,0);
		}
	
	
		else if(!labelCountTreeMap.containsKey(objSpecimen) && objSpecimen.getLineage().equals(Constants.ALIQUOT))				
		{
			setNextAvailableAliquotSpecimenlabel(objSpecimen.getParentSpecimen(),objSpecimen);
		}
	
	
		else if(!labelCountTreeMap.containsKey(objSpecimen) && objSpecimen.getLineage().equals(Constants.DERIVED_SPECIMEN))				
		{
			setNextAvailableDeriveSpecimenlabel(objSpecimen.getParentSpecimen(),objSpecimen);
		}
		
		if(objSpecimen.getChildrenSpecimen().size()>0)
		{
			List specimenList = (List)objSpecimen.getChildrenSpecimen();
			for (int index=0;index <specimenList.size();index++) 
			{
				Specimen objChildSpecimen = (Specimen)specimenList.get(index);
							
				setLabel(objChildSpecimen);
					
			}	
			
		}	
		
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.namegenerator.LabelGenerator#setLabel(java.util.List)
	 */
	public void setLabel(List<AbstractDomainObject> objSpecimenList) {

		List specimenList = objSpecimenList;
		for (int index=0;index <specimenList.size();index++) 
		{
			Specimen objSpecimen = (Specimen)specimenList.get(index);
			setLabel(objSpecimen);
		}
		
	}
	

 
	
}
