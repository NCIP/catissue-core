
package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import titli.controller.Name;
import titli.controller.interfaces.MatchListInterface;
import titli.controller.interfaces.SortedResultMapInterface;
import titli.controller.interfaces.TitliInterface;
import titli.model.Titli;
import titli.model.TitliException;
import titli.model.util.TitliTableMapper;
import edu.wustl.catissuecore.actionForm.TitliSearchForm;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.util.global.Constants;
import edu.wustl.common.util.global.TitliSearchConstants;
import edu.wustl.common.util.logger.Logger;

/**
 * perform Titli Search/Keyword Search.
 *
 * @author Juber Patel
 */
public class TitliSearchAction extends SecureAction
{

	/**
	 * logger.
	 */
	private transient final Logger logger = Logger.getCommonLogger(TitliSearchAction.class);

	/**
	 * @param mapping
	 *            the mapping
	 * @param form
	 *            the action form
	 * @param request
	 *            the request
	 * @param response
	 *            the response
	 * @return action forward
	 */
	public ActionForward executeSecureAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	{
		final TitliSearchForm titliSearchForm = (TitliSearchForm) form;
		this.logger.info("Search string entered is...... :" + titliSearchForm.getSearchString());
		String target = Constants.SUCCESS;

		try
		{
			final TitliInterface titli = Titli.getInstance();
			final String searchString = titliSearchForm.getSearchString().trim();
			final MatchListInterface matchList = titli.search(searchString);

			if(matchList == null)
			{
				target = edu.wustl.catissuecore.util.global.Constants.MAX_LIMIT_EXCEEDED;
			}
			else
			{
				final SortedResultMapInterface sortedResultMap = matchList.getSortedResultMap();

				// set the result in the action form
				titliSearchForm.setSortedResultMap(sortedResultMap);
				request.getSession().setAttribute(TitliSearchConstants.TITLI_SORTED_RESULT_MAP,
						sortedResultMap);

				// set the internationalized displaySearchString
				List placeHolders = new ArrayList();
				placeHolders.add(searchString);
				titliSearchForm.setDisplaySearchString("Keyword Search");

				// set the internationalized displayStats
				placeHolders = new ArrayList();
				placeHolders.add(Integer.toString(matchList.getNumberOfMatches()));
				placeHolders.add(Double.toString(matchList.getTimeTaken()));

				titliSearchForm.setDisplayStats("Found " + matchList.getNumberOfMatches()
						+ " matches in " + matchList.getTimeTaken() + " seconds");

				// if matches are from just one table, go directly to
				// TitliFetchAction, skip TitliResultUpdatable.jsp
				if (sortedResultMap.size() == 1)
				{
					processSingleResult(titliSearchForm, sortedResultMap);

					final String path = TitliSearchConstants.TITLI_FETCH_ACTION;
					return this.getActionForward(TitliSearchConstants.TITLI_SINGLE_RESULT, path);
				}
			}
		}
		catch (final TitliException e)
		{
			this.logger.error("TitliException in TitliSearchAction : " + e.getMessage(), e);
			logger.error(e.getMessage(), e);
		}
		this.logger.info("from keyword search action..............!!");
		return mapping.findForward(target);
	}

	/**
	 * @param titliSearchForm titliSearchForm
	 * @param sortedResultMap sortedResultMap
	 */
	private void processSingleResult(final TitliSearchForm titliSearchForm,
			final SortedResultMapInterface sortedResultMap)
	{
		try
		{
			final Name tableName = sortedResultMap.keySet().toArray(new Name[0])[0];
			final String label = TitliTableMapper.getInstance().getLabel(tableName);

			// set the selectedLabel to the label of the only table
			// setting the selectedLabel is necessary for
			// getSelectedGroup() to work properly
			titliSearchForm.setSelectedLabel(label);

		}
		catch (final Exception e)
		{
			this.logger.error("Exception in TitliFetchAction : " + e.getMessage(), e);
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * @param name
	 *            name
	 * @param path
	 *            path
	 * @return the created ActionForward
	 */
	private ActionForward getActionForward(String name, String path)
	{
		final ActionForward actionForward = new ActionForward();
		actionForward.setName(name);
		actionForward.setPath(path);
		return actionForward;
	}
}
