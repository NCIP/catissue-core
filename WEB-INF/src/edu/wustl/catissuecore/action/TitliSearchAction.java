
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
import edu.wustl.common.action.XSSSupportedAction;
import edu.wustl.common.util.global.Constants;
import edu.wustl.common.util.global.TitliSearchConstants;
import edu.wustl.common.util.logger.Logger;

/**
 * perform Titli Search/Keyword Search.
 *
 * @author Juber Patel
 */
public class TitliSearchAction extends XSSSupportedAction
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
	@Override
	public ActionForward executeXSS(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	{

		final TitliSearchForm titliSearchForm = (TitliSearchForm) form;
		this.logger.info("Search string entered is...... :" + titliSearchForm.getSearchString());

		try
		{
			final TitliInterface titli = Titli.getInstance();
			final String searchString = titliSearchForm.getSearchString().trim();
			final MatchListInterface matchList = titli.search(searchString);

			final SortedResultMapInterface sortedResultMap = matchList.getSortedResultMap();

			// set the result in the action form
			titliSearchForm.setSortedResultMap(sortedResultMap);
			request.getSession().setAttribute(TitliSearchConstants.TITLI_SORTED_RESULT_MAP,
					sortedResultMap);

			// set the internationalized displaySearchString
			List placeHolders = new ArrayList();
			placeHolders.add(searchString);
			// String displaySearchString =
			// ApplicationProperties.getValue(Constants
			// .TITLI_SEARCH_STRING_PROPERTY, placeHolders);
			titliSearchForm.setDisplaySearchString("Keyword Search");

			// set the internationalized displayStats
			placeHolders = new ArrayList();
			placeHolders.add(Integer.toString(matchList.getNumberOfMatches()));
			placeHolders.add(Double.toString(matchList.getTimeTaken()));
			// String displayStats =
			// ApplicationProperties.getValue(Constants.TITLI_STATS_PROPERTY,
			// placeHolders);
			titliSearchForm.setDisplayStats("Found " + matchList.getNumberOfMatches()
					+ " matches in " + matchList.getTimeTaken() + " seconds");

			// if matches are from just one table, go directly to
			// TitliFetchAction, skip TitliResultUpdatable.jsp
			if (sortedResultMap.size() == 1)
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
					e.printStackTrace() ;
				}

				final String path = TitliSearchConstants.TITLI_FETCH_ACTION;
				return this.getActionForward(TitliSearchConstants.TITLI_SINGLE_RESULT, path);

			}

		}
		catch (final TitliException e)
		{
			this.logger.error("TitliException in TitliSearchAction : " + e.getMessage(), e);
			e.printStackTrace() ;
		}
		this.logger.info("from keyword search action..............!!");
		// System.out.println("from titli search action..............!!");
		return mapping.findForward(Constants.SUCCESS);
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
