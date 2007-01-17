
package edu.wustl.catissuecore.action.querysuite;

/**
 * This class loads the data for ViewSearchResults screen.
 * 
 * @author Deepti Shelar
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.wustl.catissuecore.bizlogic.TreeBizLogic;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.dao.DAOFactory;
import edu.wustl.common.dao.JDBCDAO;
import edu.wustl.common.querysuite.exceptions.MultipleRootsException;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.logger.Logger;

public class ViewSearchResultsAction extends BaseAction
{
	/**
	 * This method loads the data required for ViewSearchResults.jsp
	 * @param mapping mapping
	 * @param form form
	 * @param request request
	 * @param response response
	 * @throws Exception Exception
	 * @return ActionForward actionForward
	 */
	protected ActionForward executeAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception
	{
		setResultsForView(request);
		return mapping.findForward("success");
	}

	/**
	 * Gets the results by executing query on the database.
	 * @param request request
	 * @throws DAOException DAOException
	 * @throws ClassNotFoundException ClassNotFoundException
	 * @throws MultipleRootsException MultipleRootsException
	 * @throws DynamicExtensionsApplicationException DynamicExtensionsApplicationException
	 * @throws DynamicExtensionsSystemException DynamicExtensionsSystemException
	 */
	public void setResultsForView(HttpServletRequest request) throws DAOException, ClassNotFoundException, DynamicExtensionsSystemException,
	DynamicExtensionsApplicationException, MultipleRootsException
	{
		JDBCDAO dao = (JDBCDAO) DAOFactory.getInstance().getDAO(Constants.JDBC_DAO);
		dao.openSession(null);
		String sql = "";
		Vector treeData = new Vector();
		List list = new ArrayList();
		List<String> columnNames = new ArrayList<String>();
		Logger.out.debug("SQL************" + sql);
		sql = "Select Participant_1.ACTIVITY_STATUS, Participant_1.BIRTH_DATE, Participant_1.DEATH_DATE, Participant_1.ETHNICITY, Participant_1.FIRST_NAME, Participant_1.GENDER, Participant_1.IDENTIFIER, Participant_1.LAST_NAME, Participant_1.MIDDLE_NAME, Participant_1.GENOTYPE, Participant_1.SOCIAL_SECURITY_NUMBER, Participant_1.VITAL_STATUS From catissue_participant Participant_1 left join catissue_part_medical_id ParticipantMedicalIdentif_2 on (Participant_1.IDENTIFIER=ParticipantMedicalIdentif_2.PARTICIPANT_ID) Where (Participant_1.FIRST_NAME like 'A%') And(ParticipantMedicalIdentif_2.IDENTIFIER!=0)";
		list = dao.executeQuery(sql, null, false, false, null);
		if (list != null && list.size() != 0)
		{
			List row = (List) list.get(0);
			for (int i = 0; i < row.size(); i++)
			{
				columnNames.add("Column" + i);
			}
		}
		TreeBizLogic treeBizLogic = new TreeBizLogic();
		treeData = treeBizLogic.getQueryTreeNode();

		dao.closeSession();
		request.setAttribute("treeData", treeData);
		request.setAttribute(Constants.SPREADSHEET_DATA_LIST, list);
		request.setAttribute(Constants.SPREADSHEET_COLUMN_LIST, columnNames);
	}
}
