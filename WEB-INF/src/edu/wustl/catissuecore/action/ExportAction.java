package edu.wustl.catissuecore.action;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import edu.wustl.catissuecore.bizlogic.OrderBizLogic;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.dao.HibernateDAO;

public class ExportAction  extends DispatchAction {
	
	public ActionForward exportOrder(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		HibernateDAO dao=null;
		byte[] byteArr={};
		try
		{
			SessionDataBean sessionDataBean = (SessionDataBean)request.getSession().getAttribute(Constants.SESSION_DATA);
			dao=(HibernateDAO)AppUtility.openDAOSession(sessionDataBean);
			String exportedBy = getExportedName(sessionDataBean);
			OrderBizLogic orderBizLogic = new OrderBizLogic();
			Long orderId = Long.parseLong(request.getParameter("orderId"));
			String exportedItems = request.getParameter("items");
//			List list = Arrays.asList(exportedItems.split(","));
//			
//			list.remove("specimen");
//			list.remove("scg");
//			list.remove("participant");
//			list.remove("cpr");
//			exportedItems = list.toString();  
			Map<String,Object> returnMap  = orderBizLogic.getOrderCsv(orderId,exportedBy, dao, exportedItems);
			byteArr = (byte[]) returnMap.get("fileData");
			response.setHeader(Constants.CONTENT_DISPOSITION, "attachment;"
						+ "filename=\""+returnMap.get("fileName")+"\"");
			response.setContentType("application/download");
			response.getOutputStream().write(byteArr);
			
			
		}finally{
			AppUtility.closeDAOSession(dao);
			response.flushBuffer();
		}
		return null;
	}
	
	private String getExportedName(SessionDataBean sessionDataBean){
		String firstName = sessionDataBean.getFirstName()==null? "" : sessionDataBean.getFirstName();
		String lastName = sessionDataBean.getLastName() == null ? "" : sessionDataBean.getLastName();
		return (lastName+" "+firstName).trim();
	}
}
