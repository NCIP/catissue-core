package edu.wustl.catissuecore.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.cfg.Configuration;
import net.sf.hibernate.tool.hbm2ddl.SchemaExport;

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
		
		File file = new File("db.properties");
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