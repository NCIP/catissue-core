package krishagni.catissueplus.action;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import krishagni.catissueplus.bizlogic.DeriveBizLogic;
import krishagni.catissueplus.dto.DerivedDTO;
import krishagni.catissueplus.dto.ExternalIdentifierDTO;
import krishagni.catissueplus.dto.SpecimenDTO;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import edu.wustl.catissuecore.action.CatissueBaseAction;
import edu.wustl.catissuecore.util.PrintUtil;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.HibernateDAO;


public class CreateDeriveAction extends CatissueBaseAction
{

	private static final Logger LOGGER = Logger.getCommonLogger(CreateDeriveAction.class);
	@Override
	protected ActionForward executeCatissueAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
			String dataJSON = request.getParameter("dataJSON");
			if(dataJSON == null)
			{
				return null;
			}
			HibernateDAO hibernateDao = null;
			GsonBuilder gsonBuilder = AppUtility.initGSONBuilder();
			Gson gson =  gsonBuilder.create();
			HashMap<String, String> returnMap = new HashMap<String, String>();
			try
			{
					DerivedDTO deriveDTO = gson.fromJson(dataJSON, DerivedDTO.class);
					String extidJSON = request.getParameter("extidJSON");
					java.lang.reflect.Type listType = new com.google.gson.reflect.TypeToken<Collection<ExternalIdentifierDTO>>()
					{
					}.getType();

					Collection<ExternalIdentifierDTO> externalIdentifierDTOColl = gson.fromJson(
							extidJSON, listType);
					deriveDTO.setExternalIdentifiers(externalIdentifierDTOColl);

					SessionDataBean sessionDataBean = (SessionDataBean) request.getSession()
							.getAttribute(Constants.SESSION_DATA);
					hibernateDao = (HibernateDAO)AppUtility.openDAOSession(sessionDataBean);
					DeriveBizLogic deriveBizlogic = new DeriveBizLogic();
					SpecimenDTO specimenDTO = deriveBizlogic.insertDeriveSpecimen(hibernateDao,deriveDTO,sessionDataBean);

					hibernateDao.commit();

					returnMap.put("msg", "Specimen created successfully");
					returnMap.put("success", "success");
					returnMap.put("specimenDto", gson.toJson(specimenDTO));

					boolean isToPrintLabel = deriveDTO.getIsToPrintLabel();
					if (isToPrintLabel)
					{
						boolean printStatus = PrintUtil.printSpecimenLabel(" ", " ", sessionDataBean,
								specimenDTO.getId());
						if (printStatus)
						{
							returnMap.put("printLabelSuccess",
									ApplicationProperties.getValue("specimen.label.print.success"));
							returnMap.put("printLabel", "success.");
						}
						else
						{
							returnMap.put("printLabelError",
									ApplicationProperties.getValue("specimen.label.print.fail"));
						}
					}
			}
			catch (Exception ex)
			{
				LOGGER.error(ex);
				returnMap.put("msg", ex.getMessage());
				returnMap.put("success", "failure");
			}
			finally
			{
				AppUtility.closeDAOSession(hibernateDao);
			}
			response.setContentType("application/json");
			response.getWriter().write(gson.toJson(returnMap));

			return null;
		}


}