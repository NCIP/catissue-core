package edu.wustl.catissuecore.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.itextpdf.text.Chunk;

import edu.wustl.catissuecore.bizlogic.OrderBizLogic;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.ApplicationException;
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
			Map<String,Object> returnMap  = orderBizLogic.getOrderCsv(orderId,exportedBy, dao);
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
