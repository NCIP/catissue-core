/*
 * Created on Jul 26, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package edu.wustl.common.cde;

import java.util.HashMap;
import java.util.Map;

import edu.wustl.common.cde.xml.XMLCDE;

/**@author kapil_kaveeshwar
 *  This is a communication link between the caTISSUE Core Application and the caDSR interface. */
public class CDEManager 
{
	public static CDEManager cdeManager = new CDEManager();
	Map cdeMAP; 
	CDEHandler cdeHandler;
	public CDEManager()
	{
		cdeMAP = new HashMap();
		
		XMLCDE xmCde1 = new XMLCDE();
		xmCde1.setCache(true);
		xmCde1.setLazyLoading(false);
		xmCde1.setName("Sex");
		xmCde1.setPublicId("p1");
		
		XMLCDE xmCde2 = new XMLCDE();
		xmCde2.setCache(true);
		xmCde2.setLazyLoading(false);
		xmCde2.setName("Tissue");
		xmCde2.setPublicId("p2");
		
		cdeMAP.put("Sex",xmCde1);
		cdeMAP.put("Tissue",xmCde2);
		
		cdeHandler = new CDEHandler(cdeMAP);
	}
	
	/** Retrieves live CDEs from the caDSR and updates the database cache. */
	public void refreshCache()
	{
		//CDECacheManager.refresh();
	}
	
	/**
	 * Populates the “in-memory cache” from the “database cache”. The loading
	 * operation will be performed at application startup time. For faster startup
	 * time of the application, all CDE’s can be configured for lazy loading at
	 * the application level as well as individual CDE level. In lazy loading,
	 * required CDE’s will be loaded in-memory in first call to that CDE.
	 */
	public void loadCDEInMemory()
	{
		cdeHandler.loadCDE();
	 }
	
	/** 
	 * Returns CDE for given CDE Name. 
	 * */
	public CDE getCDE(String CDEName)
	{
		return cdeHandler.retrieve(CDEName);
	}
}