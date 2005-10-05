/*
 * Created on Sep 15, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package edu.wustl.common.util.dbManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.hibernate.cfg.Configuration;
import net.sf.hibernate.mapping.Column;
import net.sf.hibernate.mapping.Property;
import net.sf.hibernate.mapping.Subclass;
import net.sf.hibernate.mapping.Table;

import org.apache.log4j.PropertyConfigurator;

import edu.wustl.catissuecore.bizlogic.AbstractBizLogic;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Variables;
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
	
	public static List getSubClassList(String className) throws ClassNotFoundException
	{
		List list = new ArrayList();
		Class classObj = Class.forName(className);
		Iterator it = cfg.getClassMapping(classObj).getDirectSubclasses();
		while(it.hasNext())
		{
			Subclass subClass = (Subclass)it.next();
			
			System.out.println(subClass.getClass().getName());
			System.out.println("Name "+subClass.getName());
			list.add(subClass.getName());
		}
		return list;
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
		//Logger.out.debug("classObj, String attributeName "+classObj+" "+attributeName);
		Iterator it = cfg.getClassMapping(classObj).getPropertyClosureIterator();
		while(it.hasNext())
		{
			Property property = (Property)it.next();
			
			//Logger.out.debug("property.getName() "+property.getName());
			//System.out.println();
			//System.out.print("property.getName() "+property.getName()+" ");
			if(property!=null && property.getName().equals(attributeName))
			{
				//System.out.println("property.getColumnSpan() "+property.getColumnSpan());
				Iterator colIt = property.getColumnIterator();
				while(colIt.hasNext())
				{
					Column col = (Column)colIt.next();
					//System.out.println("col "+col.getName());
					return col.getName();
				}
			}
		}
		
		
		Property property = cfg.getClassMapping(classObj).getIdentifierProperty();
		//Logger.out.debug("property.getName() "+property.getName());
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
	
	public static void getDATA(Class classObj)
	{
		net.sf.hibernate.mapping.Collection coll = cfg.getCollectionMapping("edu.wustl.catissuecore.domain.CollectionProtocolEvent.specimenRequirementCollection");
		//System.out.println(map);
		
		System.out.println(coll.getCollectionTable().getName());
		System.out.println(coll.getTable().getName());
		//System.out.println();
		
		Iterator it = coll.getColumnIterator();
		
		while(it.hasNext())
		{
			//net.sf.hibernate.mapping.Set set = (net.sf.hibernate.mapping.Set)it.next();
			System.out.println(it.next());
			
		}
	}
	
	public static void main(String[] args) throws Exception
	{
		Variables.catissueHome = System.getProperty("user.dir");
		Logger.out = org.apache.log4j.Logger.getLogger("");
		PropertyConfigurator.configure(Variables.catissueHome+"\\WEB-INF\\src\\"+"ApplicationResources.properties");
		
		AbstractBizLogic bizLogic = BizLogicFactory.getBizLogic(Constants.COLLECTION_PROTOCOL_FORM_ID);
		bizLogic.retrieve(CollectionProtocol.class.getName(),Constants.SYSTEM_IDENTIFIER,new Long(1));
		
		//HibernateMetaData.getDATA(CollectionProtocol.class);
		HibernateMetaData.getSubClassList(Specimen.class.getName());
		//System.out.println(str);
		
	}
}
