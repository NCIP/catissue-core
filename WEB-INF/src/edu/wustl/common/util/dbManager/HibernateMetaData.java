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
import net.sf.hibernate.mapping.PersistentClass;
import net.sf.hibernate.mapping.Property;
import net.sf.hibernate.mapping.Subclass;
import net.sf.hibernate.mapping.Table;
import edu.wustl.catissuecore.util.global.Constants;
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
	
	/**
	 * This method returns the list of subclasses of the className
	 * @author aarti_sharma
	 * @param className
	 * @return
	 * @throws ClassNotFoundException
	 */
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
	
	/**
	 * This method returns the super class of the obj passed
	 * @author aarti_sharma
	 * @param objClass
	 * @return
	 */
	public static Class getSuperClass(Object  obj)
	{
		Class objClass = obj.getClass();
		PersistentClass persistentClass = cfg.getClassMapping(objClass);
		PersistentClass superClass = persistentClass.getSuperclass();
		return superClass.getClass();
	}
	
	/**
	 * This method returns the supermost class
	 * of the class passed that is in the same package as class
	 * @author aarti_sharma
	 * @param objClass
	 * @return
	 */
	public static Class getSupermostClassInPackage(Object obj) {
		Class objClass = obj.getClass();
		Package objPackage = objClass.getPackage();
		Logger.out.debug("Input Class: " + objClass.getName()+" Package:"+objPackage.getName());
		
		PersistentClass persistentClass = cfg.getClassMapping(objClass);
		PersistentClass superClass;
		if (persistentClass != null && persistentClass.getSuperclass()!=null) {
			superClass = persistentClass;
			Logger.out.debug(objPackage.getName()+" "+persistentClass.getName()+"*********"+persistentClass.getSuperclass().getMappedClass().getPackage().getName()
					);
			Logger.out.debug("!!!!!!!!!!! "+persistentClass.getSuperclass().getMappedClass().getPackage().getName()
					.equals(objPackage.getName()));
			do {
				persistentClass = persistentClass.getSuperclass();
			}while(persistentClass !=null);
			Logger.out.debug("Supermost class in the same package:"
					+ persistentClass.getMappedClass().getName());
		} else {
			return objClass;
		}
		return persistentClass.getMappedClass();
	}
	
	
	public static String getTableName(Class classObj)
	{
		Table tbl = cfg.getClassMapping(classObj).getTable();
		if(tbl!=null)
			return tbl.getName();
		return "";
		
	}
	public static String getRootTableName(Class classObj)
	{
		Table tbl = cfg.getClassMapping(classObj).getRootTable();
		if(tbl!=null)
			return tbl.getName();
		return "";
		
	}
	public static  String getClassName(String tableName)
	{
		Iterator it = cfg.getClassMappings();
		PersistentClass persistentClass;
		String className;
		while(it.hasNext())
		{
			persistentClass = (PersistentClass) it.next();
			if(tableName.equalsIgnoreCase(persistentClass.getTable().getName()))
			{
				return persistentClass.getName();
			}
		}
		
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
	public static String getDataBaseName()
	{
		String dbName="";
		String dialect = cfg.getProperty("hibernate.dialect");
		if(dialect.toLowerCase().indexOf("oracle")!=-1)
		{
			dbName=Constants.ORACLE_DATABASE;
		}
		else if(dialect.toLowerCase().indexOf("mysql")!=-1)
		{
			dbName=Constants.MYSQL_DATABASE;
		}
		return dbName;
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
	
//	public static void main(String[] args) throws Exception
//	{
//		Variables.catissueHome = System.getProperty("user.dir");
//		Logger.out = org.apache.log4j.Logger.getLogger("");
//		PropertyConfigurator.configure(Variables.catissueHome+"\\WEB-INF\\src\\"+"ApplicationResources.properties");
//		
//		AbstractBizLogic bizLogic = BizLogicFactory.getBizLogic(Constants.COLLECTION_PROTOCOL_FORM_ID);
//		bizLogic.retrieve(CollectionProtocol.class.getName(),Constants.SYSTEM_IDENTIFIER,new Long(1));
//		
//		//HibernateMetaData.getDATA(CollectionProtocol.class);
//		HibernateMetaData.getSubClassList(Specimen.class.getName());
//		//System.out.println(str);
//		
//		System.out.println(HibernateMetaData.getClassName("CATISSUE_MOLECULAR_SPECIMEN"));
//		
//	}
}
