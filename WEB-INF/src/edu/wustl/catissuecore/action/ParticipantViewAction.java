package edu.wustl.catissuecore.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.wustl.catissuecore.bizlogic.CollectionProtocolRegistrationBizLogic;
import edu.wustl.catissuecore.bizlogic.ParticipantBizLogic;
import edu.wustl.catissuecore.bizlogic.SpecimenCollectionGroupBizLogic;
import edu.wustl.catissuecore.dao.SCGDAO;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.dto.ParticipantDTO;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.global.Validator;
import edu.wustl.dao.HibernateDAO;
import edu.wustl.security.global.Permissions;

public class ParticipantViewAction  extends CatissueBaseAction
{

	public ActionForward executeCatissueAction(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
			
		     SessionDataBean sessionData =  (SessionDataBean)request.getSession().getAttribute(Constants.SESSION_DATA);
		     String pageOf = request.getParameter(Constants.PAGE_OF);
		     if(Validator.isEmpty(pageOf))
		     {
		    	 pageOf = (String)request.getAttribute(Constants.PAGE_OF);
		     }
		     String participantId = request.getParameter("id");
		     if(Validator.isEmpty(participantId))
		     {
		    	 participantId = (String)request.getAttribute("id");
		     }
		     String cpId = request.getParameter(Constants.CP_SEARCH_CP_ID);
		     if(Validator.isEmpty(cpId))
    		 {
		    	 cpId = (String)request.getAttribute(Constants.CP_SEARCH_CP_ID);
    		 }
		       HibernateDAO hibernateDao = null;
		      try{ 
		       hibernateDao = (HibernateDAO)AppUtility.openDAOSession(sessionData);
		       CollectionProtocolRegistrationBizLogic cprbizlogic = new CollectionProtocolRegistrationBizLogic();
		       Long registrationId = cprbizlogic.getRegistrationId(hibernateDao,new Long(participantId),
		    		   new Long(cpId));
		       request.setAttribute("cprId", registrationId);
		       request.setAttribute("cpTitleList",cprbizlogic.getCpTitlelistForParticipant(new Long(participantId),hibernateDao));
		       SpecimenCollectionGroupBizLogic scgBizlogic = new SpecimenCollectionGroupBizLogic();
		       SCGDAO scgdao= new SCGDAO();
		       List<NameValueBean> scgLabels = scgdao.getSCGNameList(hibernateDao,registrationId);
		       String scgLabelString = getJsonArrayFromList(scgLabels); 
		       List<NameValueBean> eventLabels = scgdao.getEventLabelsList(hibernateDao,new Long(cpId));
		       String cpeLabelString = getJsonArrayFromList(eventLabels);
		       List<NameValueBean> specimenLabels = scgdao.getspecimenLabelsList(hibernateDao,registrationId);
		       String specLabelString = getJsonArrayFromList(specimenLabels);
		       request.setAttribute("specLabelString", specLabelString);
		        request.setAttribute("eventPointLabels",cpeLabelString);
		        request.setAttribute("scgLabels",scgLabelString);
		       	ParticipantBizLogic bizLogic=new ParticipantBizLogic();
				ParticipantDTO participantDTO=bizLogic.getParticipantDTO(hibernateDao,new Long(participantId), 
						new Long(cpId));
				if(! AppUtility.hasPrivilegeToView(CollectionProtocol.class.getName(), new Long(cpId), sessionData, Permissions.REGISTRATION)){
				    participantDTO.setFirstName("");
				    participantDTO.setLastName("");
				    participantDTO.setDob(null);
				    participantDTO.getMrns().clear();
				}
				request.setAttribute("participantDto", participantDTO);
				request.setAttribute("datePattern", CommonServiceLocator.getInstance().getDatePattern());
			}
			  finally
			 {
			    if(hibernateDao!=null)
			    {
				  hibernateDao.closeSession();	
			    }
			 }
 				return mapping.findForward(pageOf);
 			}
	
	private String getJsonArrayFromList(List<NameValueBean> list) throws JSONException
	{
		  JSONArray innerDataArray = new JSONArray();
		  for(NameValueBean bean:list)
		  {
		    JSONObject innerJsonObject = new JSONObject();
			innerJsonObject.put("text",bean.getName());
			innerJsonObject.put("value",bean.getValue());
		    innerDataArray.put(innerJsonObject);
		    innerDataArray.toString();
		  }
		  return innerDataArray.toString();
		
	}

}


	
