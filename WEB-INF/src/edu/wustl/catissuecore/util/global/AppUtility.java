/**
 * <p>Title: AppUtility Class>
 * <p>Description:  AppUtility Class contain general methods used through out the application. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 */

package edu.wustl.catissuecore.util.global;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.common.util.logger.LoggerConfig;
import edu.wustl.dao.DAO;
import edu.wustl.dao.JDBCDAO;
import edu.wustl.dao.daofactory.DAOConfigFactory;
import edu.wustl.dao.exception.DAOException;

/**
 * AppUtility Class contain general methods used through out the application.
 * 
 * @author kapil_kaveeshwar
 */
public class AppUtility
{

	/**
	 * Class Logger.
	 */
	static
	{
		LoggerConfig.configureLogger(System.getProperty("user.dir"));
	}

	private static final String[] RCODE = {"M", "CM", "D", "CD", "C", "XC",
			"L", "XL", "X", "IX", "V", "IV", "I"};
	private static final int[] BVAL = {1000, 900, 500, 400, 100, 90, 50, 40,
			10, 9, 5, 4, 1};

	private static Logger logger = Logger.getCommonLogger(AppUtility.class);

	public static JDBCDAO openJDBCSession() throws ApplicationException
	{
		JDBCDAO jdbcDAO = null;
		try
		{
			final String applicationName = CommonServiceLocator.getInstance()
					.getAppName();
			jdbcDAO = DAOConfigFactory.getInstance()
					.getDAOFactory(applicationName).getJDBCDAO();
			jdbcDAO.openSession(null);
		}
		catch (final DAOException daoExp)
		{
			AppUtility.logger.error(daoExp.getMessage(), daoExp);
			throw getApplicationException(daoExp, daoExp.getErrorKeyName(),
					daoExp.getErrorKeyName());
		}
		return jdbcDAO;
	}

	public static void closeJDBCSession(final JDBCDAO jdbcDAO)
			throws ApplicationException
	{
		try
		{
			if (jdbcDAO != null)
			{
				jdbcDAO.closeSession();
			}
		}
		catch (final DAOException daoExp)
		{
			AppUtility.logger.error(daoExp.getMessage(), daoExp);
			throw getApplicationException(daoExp, daoExp.getErrorKeyName(),
					daoExp.getMsgValues());
		}

	}

	public static DAO openDAOSession(final SessionDataBean sessionDataBean)
			throws ApplicationException
	{
		DAO dao = null;
		try
		{
			final String applicationName = CommonServiceLocator.getInstance()
					.getAppName();
			dao = DAOConfigFactory.getInstance().getDAOFactory(applicationName)
					.getDAO();
			dao.openSession(sessionDataBean);
		}
		catch (final DAOException daoExp)
		{
			AppUtility.logger.error(daoExp.getMessage(), daoExp);
			throw getApplicationException(daoExp, daoExp.getErrorKeyName(),
					daoExp.getMsgValues());
		}
		return dao;
	}

	public static void closeDAOSession(final DAO dao)
			throws ApplicationException
	{
		try
		{
			if (dao != null)
			{
				dao.closeSession();
			}
		}
		catch (final DAOException daoExp)
		{
			AppUtility.logger.error(daoExp.getMessage(), daoExp);
			throw getApplicationException(daoExp, daoExp.getErrorKeyName(),
					daoExp.getMsgValues());
		}

	}
	
	public static ApplicationException getApplicationException(
			final Exception exception, final String errorName,
			final String msgValues)
	{
		return new ApplicationException(ErrorKey.getErrorKey(errorName),
				exception, msgValues);

	}
	
	public static GsonBuilder initGSONBuilder()
	{
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Date.class,
				new JsonDeserializer<Date>()
				{

					DateFormat df = new SimpleDateFormat(ApplicationProperties
							.getValue("date.pattern"));

					@Override
					public Date deserialize(final JsonElement json,
							final Type typeOfT,
							final JsonDeserializationContext context)
							throws JsonParseException
					{
						try
						{
							return df.parse(json.getAsString());
						}
						catch (ParseException e)
						{
							return null;
						}
					}
				});
		return gsonBuilder;
	}
}