
package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.bizlogic.SiteBizLogic;
import edu.wustl.catissuecore.bizlogic.UserBizLogic;
import edu.wustl.catissuecore.dao.SCGDAO;
import edu.wustl.catissuecore.dto.SCGSummaryDTO;
import edu.wustl.catissuecore.dto.UserNameIdDTO;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.dao.DAO;
import edu.wustl.dao.HibernateDAO;

public class DisplaySCGAction extends Action
{

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String target = "success";

		DAO dao = null;
		SessionDataBean sessionData = (SessionDataBean) request.getSession().getAttribute(
				Constants.SESSION_DATA);
		try
		{
			dao = AppUtility.openDAOSession(sessionData);
			SCGDAO scgdao = new SCGDAO();
			Long identifier = 0l;
			if (request.getParameter("scgId") != null)
			{
				identifier = Long.valueOf(request.getParameter("scgId"));
				//request.getSession().setAttribute("scgId", identifier);
			}
			else
			{
				identifier = (Long) request.getSession().getAttribute("scgId");
			}

				SCGSummaryDTO scgSummaryDTO = scgdao.getScgSummary(dao, identifier);
				
				if(scgSummaryDTO.getSite() == null)
				{
					scgSummaryDTO.setSite(scgdao.getEventDefaultSite(scgSummaryDTO.getEventId(),(HibernateDAO)dao));
				}
				final Calendar cal = Calendar.getInstance();
				if(scgSummaryDTO.getReceivedDate() == null)
				{   
				    scgSummaryDTO.setReceivedDate(cal.getTime());
				}
				if(scgSummaryDTO.getCollectedDate() == null)
				{   
					scgSummaryDTO.setCollectedDate(cal.getTime());
					scgSummaryDTO.setCollectionStatus(Constants.COMPLETE); // default collection status
				}
				request.setAttribute("scgSummaryDTO", scgSummaryDTO);
				//setSiteList
				List<NameValueBean> sitelist = new SiteBizLogic().getSiteList(dao);

				//setUserList
				List<UserNameIdDTO> userList = new UserBizLogic().getUserList(dao);
				List<NameValueBean> userNVBList = getUserNVBList(userList);
				request.setAttribute("siteList", sitelist);
				request.setAttribute("userList", userNVBList);
				//sethoursList
				request.setAttribute(Constants.HOUR_LIST, Constants.HOUR_ARRAY);
				//setMinList
				request.setAttribute(Constants.MINUTES_LIST, Constants.MINUTES_ARRAY);
				request.setAttribute(Constants.COLLECTIONSTATUSLIST,
						Constants.SCG_COLLECTION_STATUS_VALUES);

		}
		finally
		{
			if (dao != null)
			{
				dao.closeSession();
			}
		}

		return mapping.findForward(target);
	}

	private List<NameValueBean> getUserNVBList(List<UserNameIdDTO> userList)
	{
		List<NameValueBean> beanlist = new ArrayList<NameValueBean>();

		for (UserNameIdDTO dto : userList)
		{
			NameValueBean nvBean = new NameValueBean();
			StringBuffer name = new StringBuffer();
			nvBean.setName(name.append(dto.getFirstName()).append(",").append(dto.getLastName()));
			nvBean.setValue(dto.getUserId());
			beanlist.add(nvBean);
		}
		return beanlist;
	}

}
