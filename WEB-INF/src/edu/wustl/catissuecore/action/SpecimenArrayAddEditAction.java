
package edu.wustl.catissuecore.action;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.SpecimenArrayForm;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.CommonAddEditAction;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.util.MapDataParser;
import edu.wustl.common.util.logger.Logger;

/**
 * <p>This class initializes the fields of SpecimenArrayAddEditAction.java</p>
 * @author Ashwin Gupta
 * @version 1.1
 */
public class SpecimenArrayAddEditAction extends CommonAddEditAction
{

	private transient Logger logger = Logger.getCommonLogger(SpecimenArrayAddEditAction.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws IOException,
			ServletException
	{

		Map arrayContentMap = (Map) request.getSession().getAttribute(
				Constants.SPECIMEN_ARRAY_CONTENT_KEY);
		if (arrayContentMap != null)
		{
			try
			{
				MapDataParser mapDataParser = new MapDataParser("edu.wustl.catissuecore.domain");
				Collection specimenArrayContentList = mapDataParser.generateData(arrayContentMap);
				AbstractActionForm abstractForm = (AbstractActionForm) form;

				if (abstractForm instanceof SpecimenArrayForm)
				{
					SpecimenArrayForm specimenArrayForm = (SpecimenArrayForm) abstractForm;
					specimenArrayForm.setSpecArrayContentCollection(specimenArrayContentList);
				}
				/*				AbstractDomainObject abstractDomain = abstractDomainObjectFactory.getDomainObject(abstractForm.getFormId(), abstractForm);
								if (abstractDomain instanceof SpecimenArray) 
								{
									SpecimenArray specimenArray = (SpecimenArray) abstractDomain;
									specimenArray.setSpecimenArrayContentCollection(specimenArrayContentList);
								}
				*/
			}
			catch (Exception exception)
			{
				logger.debug(exception.getMessage(), exception);
				exception.printStackTrace();
			}
		}
		return super.execute(mapping, form, request, response);
	}
}
