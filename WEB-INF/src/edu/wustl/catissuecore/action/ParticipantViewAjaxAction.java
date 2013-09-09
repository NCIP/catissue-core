package edu.wustl.catissuecore.action;

import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.wustl.catissuecore.dao.SCGDAO;
import edu.wustl.catissuecore.dao.SpecimenDAO;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.dao.HibernateDAO;

public class ParticipantViewAjaxAction extends DispatchAction{

	public ActionForward getSpecimenLabel(ActionMapping actionMapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
	
		String scgId = request.getParameter("scgId");
		SessionDataBean sessionData =  (SessionDataBean)request.getSession().getAttribute(Constants.SESSION_DATA);
		String specimenLabels = "";
		HibernateDAO hibernateDAO = null;
		try{
			hibernateDAO = (HibernateDAO)AppUtility.openDAOSession(sessionData);
	    if(scgId!=null)
	    {
	    	SpecimenDAO specimenDAO = new SpecimenDAO();
		 specimenLabels = getSpecimenLabelJson(specimenDAO.getSpecimenLableAndId(hibernateDAO,Long.valueOf(scgId)));
	    }
		}finally
		 {
			AppUtility.closeDAOSession(hibernateDAO);
		 }
	    response.setContentType("application/json");
	    PrintWriter out = response.getWriter();
	    out.print(specimenLabels);
	    out.flush();
		return null;
	}
	
	/**
	 * @param innerDataArray
	 * @param speicmens
	 * @throws JSONException
	 */
	private String getSpecimenLabelJson(List<NameValueBean> speicmens)
			throws JSONException
	{
		JSONObject mainJsonObject = new JSONObject();
		JSONArray innerDataArray = new JSONArray();
		//Gson gson = new Gson();
		//gson.toJson(speicmens);
		for(NameValueBean specimen:speicmens )
		{			
		        JSONObject innerJsonObject = new JSONObject();
				
				innerJsonObject.put("text",specimen.getName());
				innerJsonObject.put("value",specimen.getValue());
				innerDataArray.put(innerJsonObject);
		 }
		return innerDataArray.toString();
	}
	
	public ActionForward getSCGLabel(ActionMapping actionMapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
	
		String cpeId = request.getParameter("cpeId");
		String cprId = request.getParameter("cprId");
		SessionDataBean sessionData =  (SessionDataBean)request.getSession().getAttribute(Constants.SESSION_DATA);
		String specimenLabels = "";
		HibernateDAO hibernateDAO = null;
		try{
			hibernateDAO = (HibernateDAO)AppUtility.openDAOSession(sessionData);
	    if(cpeId!=null)
	    {
	    	SCGDAO scgdao = new SCGDAO();
		 specimenLabels = getSpecimenLabelJson(scgdao.getSGCFromCPE(hibernateDAO,Long.valueOf(cpeId),Long.valueOf(cprId)));
	    }
		}finally
		 {
		    AppUtility.closeDAOSession(hibernateDAO);
		 }
	    response.setContentType("application/json");
	    PrintWriter out = response.getWriter();
	    out.print(specimenLabels);
	    out.flush();
		return null;
	}
	
	public ActionForward getAllSCGLabels(ActionMapping actionMapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
	
		String cpeId = request.getParameter("cpeId");
		String cprId = request.getParameter("cprId");
		SessionDataBean sessionData =  (SessionDataBean)request.getSession().getAttribute(Constants.SESSION_DATA);
		String specimenLabels = "";
		HibernateDAO hibernateDAO = null;
		try{
			hibernateDAO = (HibernateDAO)AppUtility.openDAOSession(sessionData);
	    if(cpeId!=null)
	    {
	    	SCGDAO scgdao = new SCGDAO();
		 specimenLabels = getSpecimenLabelJson(scgdao.getSGCFromCPE(hibernateDAO,Long.valueOf(cpeId),Long.valueOf(cprId)));
	    }
		}finally
		 {
		    AppUtility.closeDAOSession(hibernateDAO);
		 }
	    response.setContentType("application/json");
	    PrintWriter out = response.getWriter();
	    out.print(specimenLabels);
	    out.flush();
		return null;
	}
	
	public ActionForward getAllSpecimenLabels(ActionMapping actionMapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
	
		String cpeId = request.getParameter("cpeId");
		String cprId = request.getParameter("cprId");
		SessionDataBean sessionData =  (SessionDataBean)request.getSession().getAttribute(Constants.SESSION_DATA);
		String specimenLabels = "";
		HibernateDAO hibernateDAO = null;
		try{
			hibernateDAO = (HibernateDAO)AppUtility.openDAOSession(sessionData);
	    if(cpeId!=null)
	    {
	    	SCGDAO scgdao = new SCGDAO();
		 specimenLabels = getSpecimenLabelJson(scgdao.getSpecimenFromCPE(hibernateDAO,Long.valueOf(cpeId),Long.valueOf(cprId)));
	    }
		}finally
		 {
		    AppUtility.closeDAOSession(hibernateDAO);
		 }
	    response.setContentType("application/json");
	    PrintWriter out = response.getWriter();
	    out.print(specimenLabels);
	    out.flush();
		return null;
	}
	
	public ActionForward getCPELabelsForSCG(ActionMapping actionMapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
	
		String scgId = request.getParameter("scgId");
		SessionDataBean sessionData =  (SessionDataBean)request.getSession().getAttribute(Constants.SESSION_DATA);
		HibernateDAO hibernateDAO = null;
		Long cpeId = null;
		try{
			hibernateDAO = (HibernateDAO)AppUtility.openDAOSession(sessionData);
	    
	    	SCGDAO scgdao = new SCGDAO();
		 cpeId = scgdao.getCPEIdFromSCGId(hibernateDAO,Long.valueOf(scgId));
		}finally
		 {
		    AppUtility.closeDAOSession(hibernateDAO);
		 }
	    response.setContentType("application/json");
	    PrintWriter out = response.getWriter();
	    out.print(cpeId);
	    out.flush();
		return null;
	}
}
