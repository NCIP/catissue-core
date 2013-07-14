package krishagni.catissueplus.action;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import krishagni.catissueplus.bizlogic.SpecimenBizLogic;
import krishagni.catissueplus.dto.BiohazardDTO;
import krishagni.catissueplus.dto.ExternalIdentifierDTO;
import krishagni.catissueplus.dto.SpecimenDTO;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.ibm.icu.text.DateFormat;
import com.ibm.icu.text.SimpleDateFormat;

import edu.wustl.catissuecore.util.PrintUtil;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.HibernateDAO;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.dao.query.generator.DBTypes;
import edu.wustl.dao.util.NamedQueryParam;


public class SpecimenAjaxAction extends DispatchAction
{
	private static final Logger LOGGER = Logger.getCommonLogger(SpecimenBizLogic.class);
	public ActionForward getParentDetails(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws ApplicationException,
			IOException
	{
		String parentLabel = request.getParameter("label");
		String barcode = request.getParameter("barcode");
		HashMap<String, String> returnMap = new HashMap<String, String>();
		if (Validator.isEmpty(parentLabel) && Validator.isEmpty(barcode))
		{
			returnMap.put("msg",ApplicationProperties.getValue("errors.parentDetails"));
			return null;
		}
		HibernateDAO hibernateDao = null;
		Gson gson = new Gson();
		try
		{
			SpecimenBizLogic specimenBizlogic = new SpecimenBizLogic();
			hibernateDao = (HibernateDAO)AppUtility.openDAOSession(null);
			returnMap = specimenBizlogic.getParentDetails(parentLabel, barcode,hibernateDao);
		}
		catch (Exception ex)
		{
			LOGGER.debug(ex);
			returnMap.put("msg", "Invalid parent details.");
		}
		finally
		{
			AppUtility.closeDAOSession(hibernateDao);
		}
		response.setContentType("application/json");
		response.getWriter().write(gson.toJson(returnMap));
		return null;
	}

	
	
	public ActionForward updateSpecimen(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws ApplicationException,
			IOException
	{
		String dataJSON = request.getParameter("dataJSON");
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
	        DateFormat df = new SimpleDateFormat(ApplicationProperties.getValue("date.pattern"));
	        @Override
	        public Date deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context)
	                throws JsonParseException {
	            try {
	            	return df.parse(json.getAsString());
	            } catch (ParseException e) {
	            	return null;
	            }
	        }
	    });
		Gson gson =  gsonBuilder.create();
		HashMap<String, String> returnMap = new HashMap<String, String>();
		HibernateDAO hibernateDao = null;
		try
		{
			if (dataJSON != null)
			{
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

				SpecimenBizLogic bizlogic = new SpecimenBizLogic();
				SessionDataBean sessionDataBean = (SessionDataBean) request.getSession()
						.getAttribute(Constants.SESSION_DATA);

				hibernateDao = (HibernateDAO)AppUtility.openDAOSession(sessionDataBean);
				SpecimenDTO updatedSpecimenDTO = bizlogic.updateSpecimen(hibernateDao, specDTO,
						sessionDataBean);

				hibernateDao.commit();

				returnMap.put("msg", "Successfully updated");
				returnMap.put("success", "success");
				returnMap.put("updatedSpecimenDTO", gson.toJson(updatedSpecimenDTO));

				boolean isToPrintLabel = Boolean.valueOf(request.getParameter("printLabel"));
				if (isToPrintLabel)
				{
					boolean printStatus = PrintUtil.printSpecimenLabel(" ", " ", sessionDataBean,
							specDTO.getId());
					if (printStatus)
					{
						returnMap.put("printLabelSuccess",
								ApplicationProperties.getValue("specimen.label.print.success"));
						returnMap.put("printLabel", "success");
					}
					else
						returnMap.put("printLabelError",
								ApplicationProperties.getValue("specimen.label.print.fail"));
				}
			}
		}
		catch (Exception ex)
		{
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
