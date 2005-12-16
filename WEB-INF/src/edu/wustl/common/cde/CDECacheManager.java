/*
 * Created on Aug 22, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package edu.wustl.common.cde;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import edu.wustl.common.cde.xml.XMLCDE;


/**
 * @author kapil_kaveeshwar
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CDECacheManager
{
	public void refresh(Map cdeXMLMAP)
	{
		CDEDownloader cdeDownloader = new CDEDownloader(); 
		CDEConConfig cdeCon = new CDEConConfig("scproxy.persistent.co.in","80","kapil_kaveeshwar","02march@00^","http://cabio.nci.nih.gov/cacore30/server/HTTPServer");
		
		Set ketSet = cdeXMLMAP.keySet();
		Iterator it = ketSet.iterator();
		while(it.hasNext())
		{
			
			XMLCDE xmlCDE = (XMLCDE)cdeXMLMAP.get(it.next());
			if(xmlCDE.isCache())
			{
				try
				{
					CDE cde = cdeDownloader.loadCDE(cdeCon,xmlCDE,"NCI_Thesaurus",10);
					System.out.println("cde "+cde);
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}
			}
		}
	}
}
