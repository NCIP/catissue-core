/*
 * Created on Sep 15, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package edu.wustl.common.util.dbManager;

import java.util.Iterator;

import net.sf.hibernate.cfg.Configuration;
import net.sf.hibernate.mapping.Column;
import net.sf.hibernate.mapping.Property;
import net.sf.hibernate.mapping.Table;
import edu.wustl.common.util.logger.Logger;


/**
 * @author kapil_kaveeshwar
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class HibernateMetaData
{
	private static Configuration cfg; 
	public static void initHibernateMetaData(Configuration configuration)
	{
		cfg = configuration;
	}
	
	public static String getTableName(Class classObj)
	{
		Table tbl = cfg.getClassMapping(classObj).getTable();
		if(tbl!=null)
			return tbl.getName();
		return "";
	}
	
	public static String getColumnName(Class classObj, String attributeName)
	{
		Logger.out.debug("classObj, String attributeName "+classObj+" "+attributeName);
		Iterator it = cfg.getClassMapping(classObj).getPropertyIterator();
		while(it.hasNext())
		{
			Property property = (Property)it.next();
			
			Logger.out.debug("property.getName() "+property.getName());
			if(property!=null && property.getName().equals(attributeName))
			{
				//System.out.println("property.getColumnSpan() "+property.getColumnSpan());
				Iterator colIt = property.getColumnIterator();
				while(colIt.hasNext())
				{
					Column col = (Column)colIt.next();
					return col.getName();
				}
			}
		}
		
		
		Property property = cfg.getClassMapping(classObj).getIdentifierProperty();
		Logger.out.debug("property.getName() "+property.getName());
		if(property.getName().equals(attributeName))
		{
			Iterator colIt = property.getColumnIterator();//y("systemIdentifier").getColumnIterator();
			while(colIt.hasNext())
			{
				Column col = (Column)colIt.next();
				return col.getName();
				//System.out.println(col.getName());
			}
		}
		
		return "";
	}
}
