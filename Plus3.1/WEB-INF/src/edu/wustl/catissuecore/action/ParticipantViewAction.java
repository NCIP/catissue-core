package edu.wustl.catissuecore.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.wustl.catissuecore.bizlogic.CollectionProtocolRegistrationBizLogic;
import edu.wustl.catissuecore.bizlogic.ParticipantBizLogic;
import edu.wustl.catissuecore.bizlogic.SpecimenCollectionGroupBizLogic;
import edu.wustl.catissuecore.dto.ParticipantDTO;
import edu.wustl.catissuecore.dto.SCGEventPointDTO;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.dao.DAO;

public class ParticipantViewAction  extends Action
{

	public ActionForward execute(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
			
		     SessionDataBean sessionData =  (SessionDataBean)request.getSession().getAttribute(Constants.SESSION_DATA);
		     String pageOf = request.getParameter(Constants.PAGE_OF);
		       DAO dao = null;
		      try{ 
		       dao = AppUtility.openDAOSession(sessionData);
		       CollectionProtocolRegistrationBizLogic cprbizlogic = new CollectionProtocolRegistrationBizLogic();
		       Long registrationId = cprbizlogic.getRegistrationId(dao,new Long(request.getParameter("id")),
		    		   new Long(request.getParameter(Constants.CP_SEARCH_CP_ID)));
		       SpecimenCollectionGroupBizLogic scgBizlogic = new SpecimenCollectionGroupBizLogic();
		       
		        List<SCGEventPointDTO> scgEvents = scgBizlogic.getscgeventPoint(dao,registrationId);
		        String eventPointData = getScgEventPointJson(scgEvents);
		        request.setAttribute("eventPointData",eventPointData);		
		       	ParticipantBizLogic bizLogic=new ParticipantBizLogic();
				ParticipantDTO participantDTO=bizLogic.getParticipantDTO(dao,new Long(request.getParameter("id")), 
						new Long(request.getParameter(Constants.CP_SEARCH_CP_ID)));
				request.setAttribute("participantDto", participantDTO);
				request.setAttribute("datePattern", CommonServiceLocator.getInstance().getDatePattern());
			}
			  finally
			 {
			    if(dao!=null)
			    {
				  dao.closeSession();	
			    }
			 }
 				return mapping.findForward(pageOf);
 			}
	
	private String getScgEventPointJson(List<SCGEventPointDTO> eventPointList) throws JSONException
	{
		  JSONArray innerDataArray = new JSONArray();
		  for(SCGEventPointDTO scgEventDto:eventPointList)
		  {
		    JSONObject innerJsonObject = new JSONObject();
	        StringBuffer label = new StringBuffer();
	        label.append(scgEventDto.getEventPointLabel()).append("(").append(scgEventDto.getStudyCalendarEventPoint()).append(")");
	         if(!scgEventDto.getScgName().isEmpty() && !"null".equalsIgnoreCase(scgEventDto.getScgName()))
	         {
	        	 label.append("<br>").append("<div id=\"scg_").append(scgEventDto.getScgId()).append("\" style=\"color:#aaa\">").append(scgEventDto.getScgName()).append("</div>");
	         }
			innerJsonObject.put("text",label.toString());
			innerJsonObject.put("value",scgEventDto.getScgId());
		    innerDataArray.put(innerJsonObject);
		    innerDataArray.toString();
		  }
		  return innerDataArray.toString();
		
	}

}


	
