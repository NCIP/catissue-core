package edu.wustl.catissuecore.util.global.namegenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.dao.DAOFactory;
import edu.wustl.common.dao.JDBCDAO;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Variables;


/**
 * DefaultSpecimenLabelGenerator is a class which contains the default 
 * implementations AbstractSpecimenGenerator classe.
 * @author virender_mehta
 */
public class DefaultSpecimenLableGenerator implements SpecimenLabelGenerator
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

	
	/**
	 * This method will increment current Specimen Label. 
	 * @param  obj
	 * @return Specimen label
	 */
	public synchronized String getNextAvailableSpecimenlabel(Object obj)
	{
		currentLable= currentLable+1;
		return currentLable.toString();
	}
	
	/**
	 * Returns the next 'count' number of specimen labels 
	 * @param  obj input values
	 * @return Collection of Specimen label 
	 */
	public synchronized List<String> getNextAvailableSpecimenlabel(Object obj, int count)
	{
		List<String> labels = new ArrayList<String> ();
		
		for(int i=0; i<count; i++)
		{
			currentLable= currentLable+1;
			labels.add(currentLable.toString());
		}
		
		return labels;
	}

	/**
	 * This method will increment current Derive Specimen Label. 
	 * @param  obj
	 * @return Derive Specimen label
	 */
	public synchronized String getNextAvailableDeriveSpecimenlabel(Object obj)
	{
		currentLable= currentLable+1;
		return currentLable.toString();
	}
	
	/**
	 * This method will return no of aliquot chids present for a passed specimenID. 
	 * @param  obj
	 * @return aliquotChild Count
	 */
	public synchronized List<String> getNextAvailableAliquotSpecimenlabel(Object parentSpecimenMap, int count)
	{
		String parentSpecimenId = (String) ( ((Map)parentSpecimenMap).get(Constants.PARENT_SPECIMEN_ID_KEY) );
		String parentSpecimenLabel = (String) ( ((Map)parentSpecimenMap).get(Constants.PARENT_SPECIMEN_LABEL_KEY) );

		long aliquotChildCount = getChildCount(parentSpecimenId); 
		List<String> labels = new ArrayList<String>();
		
		for(int i=0; i<count; i++)
		{
			labels.add( parentSpecimenLabel + "_" + (++aliquotChildCount) );
		}
		
		return labels;
	}
	
	protected long getChildCount(Object specimenId)
	{
		long aliquotChildCount =0;
		try
		{
			String[] selectColumnName = {"id"};
			String[] whereColumnName = {"parentSpecimen"};
			String[] whereColumnCondition = {"="};
			String[] whereColumnValue = {(String)specimenId};
			DefaultBizLogic bizLogic = new DefaultBizLogic();
			List AliquotChildList = bizLogic.retrieve(Specimen.class.getName(), selectColumnName, whereColumnName,
					whereColumnCondition, whereColumnValue ,null);
			if(AliquotChildList!=null && !AliquotChildList.isEmpty())
			{
				aliquotChildCount=AliquotChildList.size();
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		return aliquotChildCount;
	}
	
}
