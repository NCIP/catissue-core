
package edu.wustl.catissuecore.action;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.google.gson.Gson;

import edu.wustl.catissuecore.bizlogic.SpecimenBizlogic;
import edu.wustl.catissuecore.dto.BiohazardDTO;
import edu.wustl.catissuecore.dto.ExternalIdentifierDTO;
import edu.wustl.catissuecore.dto.SpecimenDTO;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.dao.DAO;

public class SpecimenUpdateAction extends CatissueBaseAction
{

	@Override
	protected ActionForward executeCatissueAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{

		String dataJSON = request.getParameter("dataJSON");
		DAO dao = null;
		try
		{
			if (dataJSON != null)
			{
				Gson gson = new Gson();
				SpecimenDTO specDTO = gson.fromJson(dataJSON, SpecimenDTO.class);

				String extidJSON = request.getParameter("extidJSON");
				java.lang.reflect.Type listType = new com.google.gson.reflect.TypeToken<Collection<ExternalIdentifierDTO>>()
				{
				}.getType();

				Collection<ExternalIdentifierDTO> externalIdentifierDTOColl = gson.fromJson(
						extidJSON, listType);
				specDTO.setExternalIdentifiers(externalIdentifierDTOColl);

				String biohazardJSON = request.getParameter("biohazardJSON");
				java.lang.reflect.Type listType1 = new com.google.gson.reflect.TypeToken<Collection<BiohazardDTO>>()
				{
				}.getType();

				Collection<BiohazardDTO> biohazardDTOColl = gson.fromJson(biohazardJSON, listType1);
				specDTO.setBioHazards(biohazardDTOColl);

				SpecimenBizlogic bizlogic = new SpecimenBizlogic();
				SessionDataBean sessionDataBean = (SessionDataBean) request.getSession()
						.getAttribute(Constants.SESSION_DATA);
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
