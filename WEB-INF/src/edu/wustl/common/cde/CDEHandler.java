/*
 * Created on Jul 26, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package edu.wustl.common.cde;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.wustl.catissuecore.dao.AbstractBizLogic;
import edu.wustl.catissuecore.dao.BizLogicFactory;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.cde.xml.XMLCDE;
import edu.wustl.common.util.dbManager.DAOException;
import gov.nih.nci.common.util.Constant;

/**
 * @author kapil_kaveeshwar
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class CDEHandler 
{
	private Map inMemCacheMap = new HashMap(); 
	private Map cdeMAP;
	
	
	public CDEHandler(Map cdeMAP)
	{
		this.cdeMAP = cdeMAP;
	}
	
	public void loadCDE()
	{
		AbstractBizLogic bizLogic = BizLogicFactory.getBizLogic(-1);
		
		Set ketSet = cdeMAP.keySet();
		Iterator it = ketSet.iterator();
		while(it.hasNext())
		{
			XMLCDE xmlCDE = (XMLCDE)it.next();
			if(xmlCDE.isCache())
			{
				if(!xmlCDE.isLazyLoading())
				{
					try
					{
						List list = bizLogic.retrieve(CDEImpl.class.getName(),"publicid",xmlCDE.getPublicId());
						if(!list.isEmpty())
						{
							CDE cde = (CDE)list.get(0);
							inMemCacheMap.put(xmlCDE.getPublicId(),cde);
						}
					}
					catch(DAOException ex)
					{
						ex.printStackTrace();
					}
				}
			}
		}
	}
	
	
	public CDE retrieve(String CDEName)
	{
		Object obj = cdeMAP.get(CDEName);
		if(obj!= null)
		{
			XMLCDE xmlCDE = (XMLCDE)obj;
			
			/** Retrieve CDE from cache */
			if(xmlCDE.isCache())
			{
				//Case Use cached CDE: 
				//If (CDE available in in-memory_cache)
				String publicID = xmlCDE.getPublicId();
				Object cdeObj = inMemCacheMap.get(publicID);
				
				CDE cde = null;
				if(cdeObj!=null)
				{
					//Return in-memory_cache.get(CDE_NAME);
					cde = (CDE)cdeObj;
					return cde;
				}
				else
				{
					//Else load the CDE from database cache
					try
					{
						AbstractBizLogic bizLogic = BizLogicFactory.getBizLogic(-1);
						cde =  (CDE)bizLogic.retrieve(CDEImpl.class.getName(),"publicid",publicID);
					}
					catch(DAOException ex)
					{
						ex.printStackTrace();
					}
				}
				//Return CDE;
				return cde;
			}
			else /** Retrieve CDE from caDSR */
			{
				//Case Download from caDSR: 
				//CDE = CDEDownloader.load(CDE_NAME);
				//Return CDE; 
				//CDEDownloader 
			}
		}
		return null;
	}
}