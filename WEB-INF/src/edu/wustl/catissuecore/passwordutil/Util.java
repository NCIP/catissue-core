/*
 * <p>Title: CommonAppletUtil.java</p>
 * <p>Description:	This class initializes the fields of CommonAppletUtil.java</p>
 * Copyright: Copyright (c) year 2006
 * Company: Washington University, School of Medicine, St. Louis.
 * @version 1.1
 * Created on Sep 20, 2006
 */

package edu.wustl.catissuecore.passwordutil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.dao.JDBCDAO;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.dao.query.generator.ColumnValueBean;



public final class Util
{

	private static Util util= new Util();
	
	/*
	 * Private constructor 
	 */
	private Util()
	{
		
	}
	/**
	 * returns the single object.
	 * 
	 */
	public static Util getInstance()
	{
		return util;
	}
	public static ArrayList<Password> getPasswordCollection(User user) throws ApplicationException
	{
	  
		ArrayList<Password> passwords=new ArrayList<Password>();
	    
	    JDBCDAO jdbcDAO =null;
	    try
		{
	    	
	    jdbcDAO = AppUtility.openJDBCSession();
	    final StringBuilder query = new StringBuilder();
	    Date date ; 
	    SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
		query.append("select * from catissue_password password ");
		query.append("where password.USER_ID=?");

		ColumnValueBean columnValueBean = new ColumnValueBean(user.getId());
		List<ColumnValueBean> columnValueBeanList = new ArrayList<ColumnValueBean>();
		columnValueBeanList.add(columnValueBean);
		if (jdbcDAO != null)
		{
				final List results = jdbcDAO.executeQuery(query.toString(),null,columnValueBeanList);
				for (int i = 0; i < results.size(); i++)
				{  
					Password password=new Password();
					final ArrayList<String> columnList = (ArrayList<String>) results.get(i);
					if ((columnList != null) )
					{
						password.setId(Long.parseLong(columnList.get(0)));
						password.setPassword(columnList.get(1));
						password.setUpdateDate((Date)formatter.parse(columnList.get(2)));
						password.setUser(user);
					}
					passwords.add(password);
				}
			
		}
		}
		catch (final DAOException daoExp)
		{
			throw new BizLogicException(daoExp);
		} catch (ParseException parException) {
			// TODO Auto-generated catch block
			//throw new BizLogicException(parException);
		}
		finally
		{
			if(jdbcDAO!=null)
			AppUtility.closeJDBCSession(jdbcDAO);
		} 
	   return passwords;
	}
}