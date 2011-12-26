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
	
}