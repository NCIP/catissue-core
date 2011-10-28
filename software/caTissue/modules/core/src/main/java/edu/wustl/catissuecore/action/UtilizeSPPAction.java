
package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.processingprocedure.SpecimenProcessingProcedure;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.exception.ApplicationException;

public class UtilizeSPPAction extends BaseAction
{

	/* (non-Javadoc)
	 * @see edu.wustl.common.action.BaseAction#executeAction(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		Collection<NameValueBean> coll = new ArrayList<NameValueBean>();
		List<SpecimenProcessingProcedure> sppList = getSppList();

		Long sppId = 0L;
		for (int i = 0; i < sppList.size(); i++)
		{
			SpecimenProcessingProcedure sppObj = sppList
					.get(i);
			final NameValueBean nvb = new NameValueBean(sppObj.getName(), sppObj.getId());
			coll.add(nvb);
			if (i == 0)
			{
				sppId = sppObj.getId();
			}
		}
		if (request.getParameter("sppValue") != null)
		{
			sppId = Long.parseLong(request.getParameter("sppValue").toString());
		}
		if (request.getAttribute("sppValue") != null)
		{
			sppId = Long.parseLong(request.getAttribute("sppValue").toString());
		}
		request.setAttribute("sppList", coll);
		if (request.getParameter("operation").equals("scg"))
		{
			setScgGridData(request, sppId);
		}
		else
		{
			setSpecimenGridData(request, sppId);
		}

		request.setAttribute("selectedSpp", sppId);
		return mapping.findForward("specimenCollectionGroup");
	}

	/**
	 * Sets the scg grid data.
	 *
	 * @param request the request
	 * @param sppId the spp id
	 *
	 * @throws ApplicationException the application exception
	 */
	public static void setScgGridData(HttpServletRequest request, Long sppId)
			throws ApplicationException
	{
		final String[] columnList1 = {
				"<input type='checkbox' id='gridselectall' name='vehicle' onclick='onSelectAll(this)' />",
				"CP Title", "PPI", "Participant Name", "SCG Name", "Collection Time Point"};
		final List<String> columnList = new ArrayList<String>();
		for (final String element : columnList1)
		{
			columnList.add(element);
		}
		request.setAttribute("columns", AppUtility.getcolumns(columnList));
		request.setAttribute("colWidth", "\"40,100,100,100,100,100\"");
		request.setAttribute("isWidthInPercent", false);
		request.setAttribute("colTypes", "\"txt,txt,txt,txt,txt,txt\"");

		request.setAttribute("heightOfGrid", 450);

	}

	/**
	 * Sets the specimen grid data.
	 *
	 * @param request the request
	 * @param sppId the spp id
	 *
	 * @throws ApplicationException the application exception
	 */
	public static void setSpecimenGridData(HttpServletRequest request, Long sppId)
			throws ApplicationException
	{
		final String[] columnList1 = {
				"<input type='checkbox'  id='gridselectall' name='vehicle' onclick='onSelectAll(this)' />",
				"CP Title", "PPI", "Participant Name", "SCG Name", "Collection Time Point",
				"Specimen Label", "Specimen Type"};
		final List<String> columnList = new ArrayList<String>();
		for (final String element : columnList1)
		{
			columnList.add(element);
		}
		request.setAttribute("columns", AppUtility.getcolumns(columnList));
		request.setAttribute("colWidth", "\"40,100,100,100,100,100,100,100\"");
		request.setAttribute("isWidthInPercent", false);
		request.setAttribute("colTypes", "\"txt,txt,txt,txt,txt,txt,txt,txt\"");

		request.setAttribute("heightOfGrid", 450);

	}

	/**
	 * Gets the spp list.
	 *
	 * @return the spp list
	 *
	 * @throws Exception the exception
	 */
	public static List<SpecimenProcessingProcedure> getSppList() throws Exception
	{
		List<SpecimenProcessingProcedure> sppList = null;
		edu.wustl.dao.DAO dao = null;
		try
		{
			dao = edu.wustl.catissuecore.util.global.AppUtility.openDAOSession(null);

			String hql = "from edu.wustl.catissuecore.domain.processingprocedure.SpecimenProcessingProcedure";

			sppList = dao.executeQuery(hql);
		}
		catch (Exception ex)
		{

		}
		finally
		{
			AppUtility.closeDAOSession(dao);
		}
		return sppList;
	}

	/**
	 * Gets the participant name.
	 *
	 * @param participant the participant
	 *
	 * @return name of participant
	 */
	public static String getParticipantName(Participant participant)
	{
		StringBuffer participantName = new StringBuffer();
		if (participant.getFirstName() == null && participant.getLastName() == null)
		{
			participantName.append("N/A");
		}
		else
		{
			appendFNLName(participant, participantName);
		}
		return participantName.toString();
	}

	/**
	 * Append fnl name.
	 *
	 * @param participant the participant
	 * @param participantName the participant name
	 */
	public static void appendFNLName(Participant participant, StringBuffer participantName)
	{
		if (participant.getLastName() != null && participant.getLastName().trim().length() > 0
				&& participant.getFirstName() != null
				&& participant.getFirstName().trim().length() > 0)
		{
			participantName.append(participant.getLastName());
			participantName.append("&nbsp;");
			participantName.append(",");
			participantName.append("&nbsp;");
			participantName.append(participant.getFirstName());
		}
		else
		{
			appendFirstNorLastN(participant, participantName);
		}
	}

	/**
	 * Append first nor last n.
	 *
	 * @param participant the participant
	 * @param participantName the participant name
	 */
	public static void appendFirstNorLastN(Participant participant, StringBuffer participantName)
	{
		if (participant.getFirstName() != null && participant.getFirstName().trim().length() > 0)
		{
			participantName.append(participant.getFirstName());
		}
		else if (participant.getLastName() != null && participant.getLastName().trim().length() > 0)
		{
			participantName.append(participant.getLastName());
		}
	}

}
