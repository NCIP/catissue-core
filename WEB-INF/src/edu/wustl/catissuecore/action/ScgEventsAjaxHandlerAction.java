/**
 * <p>Title: ScgEventsAjaxHandlerAction Class>
 * <p>Description:  This class populates the events fields in the Specimen page based on the SCG selected.</p>
 * Copyright:  Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Ashish Gupta
 * @version 1.00
 * Created on April 17, 2007
 */
package edu.wustl.catissuecore.action;

import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.SpecimenCollectionGroupBizLogic;
import edu.wustl.catissuecore.domain.CollectionEventParameters;
import edu.wustl.catissuecore.domain.ReceivedEventParameters;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.global.CommonServiceLocator;


/**
 * @author ashish_gupta
 *
 */
public class ScgEventsAjaxHandlerAction extends BaseAction
{
	CollectionEventParameters collectionEventParameters = null;
	ReceivedEventParameters receivedEventParameters = null;
	/**
     * Overrides the execute method of Action class.
     * Sets the various Collection and Received events based on SCG id.
     */
	public ActionForward  executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
    {
    	String scgId = request.getParameter("scgId");
    	
    	StringBuffer xmlData = new StringBuffer();
    	if(scgId != null && !scgId.equals(""))
    	{
    		SpecimenCollectionGroupBizLogic specimenCollectionGroupBizLogic = (SpecimenCollectionGroupBizLogic)BizLogicFactory.getInstance().getBizLogic(Constants.SPECIMEN_COLLECTION_GROUP_FORM_ID);
    		
    		Object object = specimenCollectionGroupBizLogic.retrieve(SpecimenCollectionGroup.class.getName(), new Long(scgId));
    		if(object != null)
    		{
    			SpecimenCollectionGroup specimenCollectionGroup = (SpecimenCollectionGroup)object;
    			Collection eventsColl = specimenCollectionGroup.getSpecimenEventParametersCollection();
    			if(eventsColl != null && !eventsColl.isEmpty())
    			{
    				Iterator iter = eventsColl.iterator();
    				while(iter.hasNext())
    				{
    					Object temp = iter.next();
    					if(temp instanceof CollectionEventParameters)
    					{
    						collectionEventParameters = (CollectionEventParameters)temp;
    					}
    					else if(temp instanceof ReceivedEventParameters)
    					{
    						receivedEventParameters = (ReceivedEventParameters)temp;
    					}
    					
    				}
    				xmlData = makeXMLData(xmlData);
    			}
    		}
    	}
//    	Writing to response
		PrintWriter out = response.getWriter();	
		response.setContentType("text/xml");
		out.write(xmlData.toString());
		
    	return null;
    }
	/**
	 * @return
	 */
	private StringBuffer makeXMLData(StringBuffer xmlData)
	{		
		
		xmlData.append("<Events>");
		
		xmlData.append("<CollectionEvents>");
		xmlData = appendCollectionEvents(xmlData);
		xmlData.append("</CollectionEvents>");
		
		xmlData.append("<ReceivedEvents>");
		xmlData = appendReceivedEvents(xmlData);
		xmlData.append("</ReceivedEvents>");
		
		xmlData.append("</Events>");
		return xmlData;
		
	}
	/**
	 * @param xmlData
	 * @return
	 */
	private StringBuffer appendCollectionEvents(StringBuffer xmlData)
	{
		xmlData.append("<CollectorId>");
		xmlData.append(collectionEventParameters.getUser().getId().toString());
		xmlData.append("</CollectorId>");
		
		xmlData.append("<CollectorName>");
		xmlData.append(collectionEventParameters.getUser().getLastName() + "," + collectionEventParameters.getUser().getFirstName());
		xmlData.append("</CollectorName>");
		
		xmlData.append("<CollectionDate>");
		xmlData.append(Utility.parseDateToString(collectionEventParameters.getTimestamp(),CommonServiceLocator.getInstance().getDatePattern()));
		xmlData.append("</CollectionDate>");
		
		Calendar calender = Calendar.getInstance();
		calender.setTime(collectionEventParameters.getTimestamp());
		
		xmlData.append("<CollectionTimeHours>");
		xmlData.append(Utility.toString(Integer.toString( calender.get(Calendar.HOUR_OF_DAY))));
		xmlData.append("</CollectionTimeHours>");  
		
		xmlData.append("<CollectionTimeMinutes>");
		xmlData.append(Utility.toString(Integer.toString(calender.get(Calendar.MINUTE))));
		xmlData.append("</CollectionTimeMinutes>");
		
		xmlData.append("<CollectionProcedure>");
		xmlData.append(collectionEventParameters.getCollectionProcedure());
		xmlData.append("</CollectionProcedure>");
		
		xmlData.append("<CollectionContainer>");
		xmlData.append(collectionEventParameters.getContainer());
		xmlData.append("</CollectionContainer>");
		
		xmlData.append("<CollectionComments>");
		/* Bug Id: 4134
		 Patch ID: 4134_2			
		 Description: Added AppUtility.toString()		*/
		xmlData.append(Utility.toString(collectionEventParameters.getComment()));
		xmlData.append("</CollectionComments>");	
		
		return xmlData;
	}
	/**
	 * @param xmlData
	 * @return
	 */
	private StringBuffer appendReceivedEvents(StringBuffer xmlData)
	{
		xmlData.append("<ReceiverId>");
		xmlData.append(receivedEventParameters.getUser().getId().toString());
		xmlData.append("</ReceiverId>");
		
		xmlData.append("<ReceiverName>");
		xmlData.append(receivedEventParameters.getUser().getLastName() + "," + receivedEventParameters.getUser().getFirstName());
		xmlData.append("</ReceiverName>");
		
		xmlData.append("<ReceivedDate>");
		xmlData.append(Utility.parseDateToString(receivedEventParameters.getTimestamp(),CommonServiceLocator.getInstance().getDatePattern()));
		xmlData.append("</ReceivedDate>");
		
		Calendar calender = Calendar.getInstance();
		calender.setTime(receivedEventParameters.getTimestamp());
		
		xmlData.append("<ReceivedTimeHours>");
		xmlData.append(Utility.toString(Integer.toString( calender.get(Calendar.HOUR_OF_DAY))));
		xmlData.append("</ReceivedTimeHours>");  
		
		xmlData.append("<ReceivedTimeMinutes>");
		xmlData.append(Utility.toString(Integer.toString(calender.get(Calendar.MINUTE))));
		xmlData.append("</ReceivedTimeMinutes>");
		
		xmlData.append("<ReceivedQuality>");
		xmlData.append(receivedEventParameters.getReceivedQuality());
		xmlData.append("</ReceivedQuality>");
		
		xmlData.append("<ReceivedComments>");
		/*Bug Id: 4134
	 	Patch ID: 4134_1			
	 	Description: Added AppUtility.toString()		
		*/
		xmlData.append(Utility.toString(receivedEventParameters.getComment()));
		xmlData.append("</ReceivedComments>");	
		
		return xmlData;
	}
}
