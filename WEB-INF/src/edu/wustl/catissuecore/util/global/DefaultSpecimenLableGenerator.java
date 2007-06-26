package edu.wustl.catissuecore.util.global;

import java.util.List;

import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.dao.DAOFactory;
import edu.wustl.common.dao.JDBCDAO;
import edu.wustl.common.util.dbManager.DAOException;


/**
 * DefaultSpecimenLabelGenerator is a class which contains the default 
 * implementations AbstractSpecimenGenerator classe.
 * @author virender_mehta
 */
public class DefaultSpecimenLableGenerator extends AbstractSpecimenLabelGenerator
{
	/**
	 * Default Constructor
	 */
	public DefaultSpecimenLableGenerator()
	{
		super();
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
	
	@Override
	/**
	 * This method will increment current Specimen Label. 
	 * @param  obj
	 * @return Specimen label
	 */
	public synchronized  String getNextAvailableSpecimenlabel(Object obj)
	{
		currentLable= currentLable+1;
		return currentLable.toString();
	}
	@Override
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
	@Override
	/**
	 * This method will return no of aliquot chids present for a passed specimenID. 
	 * @param  obj
	 * @return aliquotChild Count
	 */
	public synchronized String getNextAvailableAliquotSpecimenlabel(Object specimenId)
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
		String nextAliquotLabel = String.valueOf(aliquotChildCount+1);
		return nextAliquotLabel;
	}
}
