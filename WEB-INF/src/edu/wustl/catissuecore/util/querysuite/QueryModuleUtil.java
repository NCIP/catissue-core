package edu.wustl.catissuecore.util.querysuite;

import java.util.ArrayList;
import java.util.List;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.dao.DAOFactory;
import edu.wustl.common.dao.JDBCDAO;
import edu.wustl.common.util.dbManager.DAOException;


/**
 * 
 * @author deepti_shelar
 *
 */
public class QueryModuleUtil
{
	/**
	 * Returns the label for attribute's name
	 * eg firstName is converted to First Name
	 * @param attrName
	 * @return
	 */
	public String getAttributeLabel(String attrName)
	{
		String attrLabel = "";
		int len = attrName.length();
		for(int i=0;i<len;i++)
		{
			char attrChar = attrName.charAt(i);
			int asciiValue = attrChar;
			if (i == 0)
			{
				int capitalAsciiValue = asciiValue - 32;
				attrLabel = attrLabel + (char)capitalAsciiValue;
			} 
			else
			{
				if (asciiValue >= 65 && asciiValue <= 90)
				{
					attrLabel = attrLabel + " " + attrChar;
					for(int k=i+1; k<len ;k++)
					{
						attrChar = attrName.charAt(k);
						asciiValue = attrChar;
						if (asciiValue >= 65 && asciiValue <= 90)
						{
							attrLabel = attrLabel + attrChar;
							i++;
						}
					}
				} 
				else
				{
					attrLabel = attrLabel + attrChar;
				}
			}
		}
		return attrLabel;
	}
	/**
	 * Executes the query and returns the results back.
	 * @param selectSql sql to be executed
	 * @param sessionData sessiondata
	 * @return list of results 
	 * @throws ClassNotFoundException 
	 * @throws DAOException 
	 */
	public List<List<String>> executeQuery(String selectSql, SessionDataBean sessionData) throws ClassNotFoundException, DAOException
	{
		JDBCDAO dao = (JDBCDAO) DAOFactory.getInstance().getDAO(Constants.JDBC_DAO);
		List dataList = new ArrayList();
		try
		{
			dao.openSession(sessionData);
			dataList = dao.executeQuery(selectSql, sessionData, false, false, null);
			dao.commit();
		}
//		catch (DAOException e)
//		{
//			e.printStackTrace();
//			throw e;
//		}
//		catch (ClassNotFoundException e)
//		{
//			e.printStackTrace();
//			throw e;
//		}
		finally
		{
			dao.closeSession();	
		}
		return dataList;
	}

}
