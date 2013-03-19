package edu.wustl.catissuecore.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.JSONObject;

import com.google.gson.Gson;

import edu.wustl.catissuecore.bizlogic.SpecimenBizlogic;
import edu.wustl.catissuecore.dto.SpecimenDTO;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.dao.DAO;

public class SpecimenUpdateAction extends CatissueBaseAction
{

	@Override
	protected ActionForward executeCatissueAction(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception 
	{

		String dataJSON = request.getParameter("dataJSON");
		DAO dao = null;
		try
		{
			if(dataJSON != null)
			{
				Gson gson = new Gson();
				SpecimenDTO specDTO = gson.fromJson(dataJSON, SpecimenDTO.class);
				SpecimenBizlogic bizlogic = new SpecimenBizlogic();
				SessionDataBean sessionDataBean = (SessionDataBean)request.getSession().getAttribute(Constants.SESSION_DATA);
				dao = AppUtility.openDAOSession(sessionDataBean);
				bizlogic.updateSpecimen(dao, specDTO, sessionDataBean);
				dao.commit();
			}
			response.setContentType(Constants.CONTENT_TYPE_XML);
			response.getWriter().write("success");
		}
		finally
		{
			AppUtility.closeDAOSession(dao);
		}
		
		return null;
		
	}
}
