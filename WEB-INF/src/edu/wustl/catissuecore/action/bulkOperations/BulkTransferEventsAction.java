
package edu.wustl.catissuecore.action.bulkOperations;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import edu.wustl.catissuecore.actionForm.BulkEventOperationsForm;

/**
 * @author renuka_bajpai
 */
public class BulkTransferEventsAction extends BulkOperationAction
{

	/**
	 * @param eventParametersForm : eventParametersForm
	 * @param specimenRow : specimenRow
	 * @param specimenId : specimenId
	 * @param request : request
	 */
	@Override
	protected void fillFormData(BulkEventOperationsForm eventParametersForm, List specimenRow,
			String specimenId, HttpServletRequest request)
	{
		eventParametersForm.setFieldValue("ID_" + specimenId + "_CLASS", specimenRow.get(2)
				.toString());
		eventParametersForm.setFieldValue("ID_" + specimenId + "_LABEL", specimenRow.get(1)
				.toString());
		if (specimenRow.get(3).equals(""))
		{
			eventParametersForm.setFieldValue("ID_" + specimenId + "_FROMLOC",
					"Is virtually located");
		}
		else
		{
			eventParametersForm.setFieldValue("ID_" + specimenId + "_FROMLOC", specimenRow.get(3)
					+ " (" + specimenRow.get(4) + "," + specimenRow.get(5) + ")");
		}

		eventParametersForm.setFieldValue("ID_" + specimenId + "_FROMLOCID", specimenRow.get(6)
				.toString());
		eventParametersForm.setFieldValue("ID_" + specimenId + "_FROMLOCPOS1", specimenRow.get(4)
				.toString());
		eventParametersForm.setFieldValue("ID_" + specimenId + "_FROMLOCPOS2", specimenRow.get(5)
				.toString());
		//bug 14417
		/**
		 * org.apache.struts.action.ERROR will return ActionError if same storage positions
		 * are added to multiple specimens.
		 * If error occurs then destination positions should be retained.
           bug 15083
		 */
		if(request.getAttribute( "org.apache.struts.action.ERROR" )== null) //bug 15083
		{
			eventParametersForm.setFieldValue("ID_" + specimenId + "_TOSCPOS1","");
			eventParametersForm.setFieldValue("ID_" + specimenId + "_TOSCPOS2", "");
			eventParametersForm.setFieldValue("ID_" + specimenId + "_TOSCLABEL", "");
		}
		eventParametersForm.setSpecimenId(specimenId, specimenId);
	}
}
