package edu.wustl.catissuecore.action;

/**
 * This class is called when user clicks on Delete button on Specimen Array page
 * @author nitesh_marwaha
 */
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
import edu.wustl.common.util.global.Status;


public class DeleteSpecimenArrayAction extends CommonAddEditAction {
	/**
	 * This method sets the activity status to 'Disabled' and then calls execute method of CommonAddEditAction. 
	 * @author nitesh_marwaha
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		Map arrayContentMap = (Map) request.getSession().getAttribute(
				Constants.SPECIMEN_ARRAY_CONTENT_KEY);
		AbstractActionForm abstractForm = (AbstractActionForm) form;
		abstractForm.setActivityStatus(Status.ACTIVITY_STATUS_DISABLED.toString());
		try {
			if (arrayContentMap != null) {
				MapDataParser mapDataParser = new MapDataParser(
						"edu.wustl.catissuecore.domain");
				Collection specimenArrayContentList = mapDataParser
						.generateData(arrayContentMap);
				if (abstractForm instanceof SpecimenArrayForm) {
					SpecimenArrayForm specimenArrayForm = (SpecimenArrayForm) abstractForm;
					specimenArrayForm
							.setSpecArrayContentCollection(specimenArrayContentList);
				}
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return super.execute(mapping, abstractForm, request, response);
	}
}
