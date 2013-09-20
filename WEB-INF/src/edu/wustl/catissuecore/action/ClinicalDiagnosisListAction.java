package edu.wustl.catissuecore.action;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.bizlogic.ComboDataBizLogic;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.dao.JDBCDAO;

public class ClinicalDiagnosisListAction extends BaseAction
{
	@Override
	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		
		Collection<NameValueBean> clinicalDiagnosisCollection = new ArrayList<NameValueBean>();
		Long collectionProtocolId = null;
		String query = request.getParameter("mask")!=null?request.getParameter("mask"):"";
		if(request.getParameter("collectionProtocolId")!=null && !"".equals(request.getParameter("collectionProtocolId")))
		{
		  collectionProtocolId = Long.parseLong(request.getParameter("collectionProtocolId"));
		}
		
		SessionDataBean sessionDataBean = (SessionDataBean) request
				.getSession().getAttribute(Constants.SESSION_DATA);
		JDBCDAO dao=null;
		try{
			dao = AppUtility.openJDBCSession();
			final ComboDataBizLogic comboDataBizObj = new ComboDataBizLogic();
			int listSize = 100;
			if(collectionProtocolId!=null && query.equals("")){
				clinicalDiagnosisCollection = comboDataBizObj.getClinicalDiagnosisList(collectionProtocolId,listSize,dao);
			}else{
				clinicalDiagnosisCollection = comboDataBizObj.getClinicalDiagnosisList(query,listSize,dao);
			}
			
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			AppUtility.closeDAOSession(dao);
		}
			
		
		
		response.setContentType("text/xml");
		final PrintWriter out = response.getWriter();
//		String encodedurl = URLEncoder.encode(createXml(clinicalDiagnosisCollection).toString(),"UTF-8"); 
//		URLDecoder.decode(encodedurl);
		out.write(createXml(clinicalDiagnosisCollection).toString());
		
      /*  final String limit = request.getParameter("limit");
		final String query = request.getParameter("query");
		final String start = request.getParameter("start");
		final String  showOption = request.getParameter("showOption");
		final Integer limitFetch = Integer.parseInt(limit);
		final Integer startFetch = Integer.parseInt(start);
		Long collectionProtocolId = null;
		if(!"undefined".equals(request.getParameter("collectionProtocolId")) && !"".equals(request.getParameter("collectionProtocolId")))
		{
		  collectionProtocolId = Long.parseLong(request.getParameter("collectionProtocolId"));
		}
		final HttpSession newSession = request.getSession();
		final CollectionProtocolBean collectionProtocolBean = (CollectionProtocolBean) newSession
				.getAttribute(Constants.COLLECTION_PROTOCOL_SESSION_BEAN);

		Collection<NameValueBean> clinicalDiagnosisBean = new ArrayList<NameValueBean>();

		if(collectionProtocolId != null)
		{
			final String cprHQL = "select clinicalDiag.name"
				+ " from edu.wustl.catissuecore.domain.ClinicalDiagnosis as clinicalDiag" + " where " +
						"clinicalDiag.collectionProtocol.id="+collectionProtocolId;
			final List<Object[]> dataList = AppUtility.executeQuery(cprHQL);
		    List clinicalDiagnosisValues = dataList;
		    getClinicalDiagnosisBean(clinicalDiagnosisBean, clinicalDiagnosisValues,true);
			
		}else if(collectionProtocolBean != null && collectionProtocolBean.getClinicalDiagnosis() != null && (collectionProtocolBean.getClinicalDiagnosis()).length != 0)
		{
			List<String> dataList = Arrays.asList(collectionProtocolBean.getClinicalDiagnosis());
			getClinicalDiagnosisBean(clinicalDiagnosisBean, dataList,false);
		}	
		final ComboDataBizLogic comboDataBizObj = new ComboDataBizLogic();
		final JSONObject jsonObject = comboDataBizObj.getClinicalDiagnosisData(limitFetch,
				startFetch, query,clinicalDiagnosisBean,showOption);

		response.setContentType("text/javascript");
		final PrintWriter out = response.getWriter();
		out.write(jsonObject.toString());*/
		return null;
	}

	public StringBuffer createXml(Collection<NameValueBean> clinicalDiagnosisBean){
		StringBuffer buffer = new StringBuffer();
		buffer.append("<?xml version=\"1.0\"?>");
		buffer.append("<complete>");
		if(clinicalDiagnosisBean!=null){
		final ListIterator iterator =  ((List) clinicalDiagnosisBean)
				.listIterator();
		int count = 0;
		while(iterator.hasNext() && count<100){
			final NameValueBean nameValueBean = (NameValueBean) iterator
					.next();
			
			buffer.append("<option value=\""+StringEscapeUtils.escapeXml(nameValueBean.getName())+"\"><![CDATA["+nameValueBean.getValue()+"]]></option>");
			count++;
		}
		}
		buffer.append("</complete>");
		
		return buffer;
	}

}
