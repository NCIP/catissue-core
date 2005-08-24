package edu.wustl.common.util.dbManager;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;

/*
 * Created on Feb 20, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */

/**
 * @author kapil_kaveeshwar
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class GenerateSchema
{
	public static void main(String[] args) throws HibernateException , IOException, Exception
	{
		boolean isToPrintOnConsole = false;
		boolean isToExecuteOnDB = false;
		if(args.length!=0)
		{
			String arg = args[0];
			if(arg.equalsIgnoreCase("true"))
			{
				isToPrintOnConsole = true;
				isToExecuteOnDB = true;
			}
			if(arg.equalsIgnoreCase("false"))
			{
				isToPrintOnConsole = false;
				isToExecuteOnDB = false;
			}
		}
		
		File file = new File("hibernate.properties");
		BufferedInputStream stram = new BufferedInputStream(new FileInputStream(file));  
		Properties p = new Properties();
		p.load(stram);
		stram.close();
		
		
			
		Configuration cfg = new Configuration();
		cfg.setProperties(p);
		cfg.addDirectory(new File("./WEB-INF/src"));
		new SchemaExport(cfg).setOutputFile("catissuecore.sql").create(isToPrintOnConsole, isToExecuteOnDB);
//		if(isToExecuteOnDB)
//			new GenerateUser();
	}
}