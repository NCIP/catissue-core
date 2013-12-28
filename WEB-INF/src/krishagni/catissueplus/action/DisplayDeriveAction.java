
package krishagni.catissueplus.action;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import krishagni.catissueplus.dto.DerivedDTO;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.google.gson.Gson;

import edu.wustl.catissuecore.action.CatissueBaseAction;
import edu.wustl.catissuecore.bizlogic.SpecimenBizlogic;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Variables;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.global.Validator;
import edu.wustl.dao.HibernateDAO;
import edu.wustl.dao.exception.DAOException;

public class DisplayDeriveAction extends CatissueBaseAction
{
	    
	@Override
	protected ActionForward executeCatissueAction(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{

		HibernateDAO hibernateDao = null;
		try
		{
			SessionDataBean sessionDataBean = (SessionDataBean) request
					.getSession().getAttribute(Constants.SESSION_DATA);
			hibernateDao = (HibernateDAO) AppUtility
					.openDAOSession(sessionDataBean);
			Long parentId = 0l;

			DerivedDTO derivedDTO = new DerivedDTO();
			if (Validator.isEmpty(request.getParameter("parentSpecimenId")))
			{
				derivedDTO.setClassName(Constants.CELL);
				derivedDTO.setType(Constants.NOT_SPECIFIED);
			}
			else
			{
				derivedDTO.setParentSpecimenId(parentId);
				derivedDTO.setParentSpecimenLabel(request
						.getParameter("parentLabel"));
				derivedDTO.setType(request.getParameter("specType"));
				derivedDTO.setClassName(request.getParameter("specClassName"));
				parentId = Long.valueOf(request
						.getParameter("parentSpecimenId"));
				//				request.setAttribute("cpId", SpecimenDAO.getcpId(parentId,  hibernateDao));
			}

			request.setAttribute(
					"isSpecimenLabelGeneratorAvl",
					parentId == 0
							? false
							: new SpecimenBizlogic()
									.isSpecimenLabelGeneratorAvl(parentId,
											hibernateDao));
			request.setAttribute("isBarcodeGeneratorAvl",
					Variables.isSpecimenBarcodeGeneratorAvl);
			derivedDTO.setCreatedOn(new Date());

			derivedDTO.setInitialQuantity(0.0);
			request.setAttribute("pageOf", request.getParameter("pageOf"));
			request.setAttribute("deriveDTO", derivedDTO);
			setSpecimenCharsInRequest(request);
		}
		finally
		{
			AppUtility.closeDAOSession(hibernateDao);
		}
		return mapping.findForward("success");
	}

	private void setSpecimenCharsInRequest(HttpServletRequest request)
			throws BizLogicException, DAOException
	{
		Gson gson = new Gson();
		request.setAttribute(Constants.TISSUE_TYPE_LIST_JSON,
				gson.toJson(AppUtility.getSpecimenTypes(Constants.TISSUE)));

		request.setAttribute(Constants.FLUID_TYPE_LIST_JSON,
				gson.toJson(AppUtility.getSpecimenTypes(Constants.FLUID)));

		request.setAttribute(Constants.CELL_TYPE_LIST_JSON,
				gson.toJson(AppUtility.getSpecimenTypes(Constants.CELL)));

		request.setAttribute(Constants.MOLECULAR_TYPE_LIST_JSON,
				gson.toJson(AppUtility.getSpecimenTypes(Constants.MOLECULAR)));
		String className = request.getParameter("specClassName");
		request.setAttribute("cellType", AppUtility
				.getSpecimenTypes(className == null
						? Constants.CELL
						: className));
		request.setAttribute("isSpecimenBarcodeGeneratorAvl",
				Variables.isSpecimenBarcodeGeneratorAvl);
		request.setAttribute(Constants.SPECIMEN_CLASS_LIST,
				AppUtility.getSpecimenClassList());
	}

}
