package edu.wustl.common.util.dbManager;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.SessionFactory;
import net.sf.hibernate.cfg.Configuration;
import edu.wustl.common.util.global.Variables;
import edu.wustl.common.util.logger.Logger;

/**
 * <p>Title: DBUtil Class>
 * <p>Description:  Utility class provides database specific utilities methods </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Kapil Kaveeshwar
 * @version 1.00
 */
public class DBUtil
{
	//A factory for DB Session which provides the Connection for client. 
	private static  SessionFactory m_sessionFactory;

	//ThreadLocal to hold the Session for the current executing thread. 
	private static final ThreadLocal threadLocal = new ThreadLocal();
	//Initialize the session Factory in the Static block.
	static 
	{
		try
		{
			Configuration cfg = new Configuration();
			m_sessionFactory = cfg.configure().buildSessionFactory();
			HibernateMetaData.initHibernateMetaData(cfg);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		    Logger.out.debug("Exception: "+ex.getMessage(),ex);
			throw new RuntimeException(ex.getMessage());
		}
		
		
//		try
//
//		{
//
//		File file = new File(Variables.catissueHome+System.getProperty("file.separator")+"db.properties");
//
//		Logger.out.info("File "+file);
//
//		BufferedInputStream stram = new BufferedInputStream(new FileInputStream(file));
//
//		Properties p = new Properties();
//
//		p.load(stram);
//
//
//		stram.close();
//
//		Configuration cfg = new Configuration();
//
//		cfg.setProperties(p);
//
//		m_sessionFactory = cfg.configure().buildSessionFactory();
//
//		HibernateMetaData.initHibernateMetaData(cfg);
//
//		}
//
//		catch(Exception ex)
//
//		{
//
//		ex.printStackTrace();
//
//		Logger.out.debug("Exception: "+ex.getMessage(),ex);
//
//		throw new RuntimeException(ex.getMessage());

//		}


	}

	/**
	 * Follows the singleton pattern and returns only current opened session.
	 * @return Returns the current db session.  
	 * */
	public static Session currentSession() throws HibernateException
	{
		Session s = (Session) threadLocal.get();
		
		//Open a new Session, if this Thread has none yet
		if (s == null)
		{
			s = m_sessionFactory.openSession();
			try
			{
				s.connection().setAutoCommit(false);
			}
			catch(SQLException ex)
			{
				throw new HibernateException(ex.getMessage(),ex);
			}
			threadLocal.set(s);
		}
		return s;
	}

	/**
	 * Close the currently opened session.
	 * */
	public static void closeSession() throws HibernateException
	{
		Session s = (Session) threadLocal.get(); 
		threadLocal.set(null);
		if (s != null)
			s.close();
	}
	
	public static Connection getConnection() throws HibernateException
	{
		return currentSession().connection();
	}
	
	public static void closeConnection() throws HibernateException
	{
		closeSession();
	}
}