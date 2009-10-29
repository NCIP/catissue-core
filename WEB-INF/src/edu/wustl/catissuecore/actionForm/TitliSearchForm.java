/**
 * 
 */

package edu.wustl.catissuecore.actionForm;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import titli.controller.Name;
import titli.controller.interfaces.ResultGroupInterface;
import titli.controller.interfaces.SortedResultMapInterface;
import titli.model.util.TitliResultGroup;
import titli.model.util.TitliTableMapper;
import edu.wustl.common.util.logger.Logger;

/**
 * This Class encapsulates all the request parameters passed from Tilti Search
 * Form.
 * 
 * @author Juber Patel
 * 
 */
public class TitliSearchForm extends ActionForm
{

	private static final long serialVersionUID = 1L;

	/**
	 * logger Logger - Generic logger.
	 */
	private static Logger logger = Logger.getCommonLogger(TitliSearchForm.class);

	private String searchString;

	private String displaySearchString;

	private String displayStats;

	private SortedResultMapInterface sortedResultMap;

	private Map<Name, TitliResultGroup> titliResultMap;

	private String selectedLabel;

	/**
	 * @return the selectedLabel
	 */
	public String getSelectedLabel()
	{
		return this.selectedLabel;
	}

	/**
	 * @param selectedLabel
	 *            the selectedLabel to set
	 */
	public void setSelectedLabel(String selectedLabel)
	{
		this.selectedLabel = selectedLabel;
	}

	/**
	 * @return the search string
	 */
	public String getSearchString()
	{
		return this.searchString;
	}

	/**
	 * @param searchString
	 *            the string to be searched
	 */
	public void setSearchString(String searchString)
	{
		this.searchString = searchString;
	}

	/**
	 * @return the sortedResultMap
	 */
	public SortedResultMapInterface getSortedResultMap()
	{
		return this.sortedResultMap;
	}

	/**
	 * @param sortedResultMap
	 *            the sortedResultMap to set
	 */
	public void setSortedResultMap(SortedResultMapInterface sortedResultMap)
	{
		this.sortedResultMap = sortedResultMap;

		this.titliResultMap = new LinkedHashMap<Name, TitliResultGroup>();

		for (final ResultGroupInterface i : sortedResultMap.values())
		{
			this.titliResultMap.put(i.getTableName(), new TitliResultGroup(i));
		}

	}

	/**
	 * 
	 * @return the result group corresponding to the selected label
	 */
	public TitliResultGroup getSelectedGroup()
	{
		ResultGroupInterface i = null;
		try
		{
			i = this.sortedResultMap.get(TitliTableMapper.getInstance()
					.getTable(this.selectedLabel));

		}
		catch (final Exception e)
		{
			TitliSearchForm.logger.error("Exception in TitliSearchForm : " + e.getMessage(), e);
			e.printStackTrace();
		}

		return new TitliResultGroup(i);

	}

	/**
	 * @return the titliResultMap
	 */
	public Map<Name, TitliResultGroup> getTitliResultMap()
	{
		return this.titliResultMap;
	}

	/**
	 * validate the input
	 * @param mapping the cation mapping
	 * @param request the request
	 * @return acttion errors
	 */
	@Override
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
		final ActionErrors errors = new ActionErrors();

		final String requestSearchString = request.getParameter("searchString");

		// searchString is null or empty
		if (requestSearchString == null || requestSearchString.trim().equals(""))
		{
			errors.add("empty search string", new ActionError("  "));
		}

		if (requestSearchString.startsWith("*") || requestSearchString.startsWith("?"))
		{
			errors.add("search string starts with * or ? ", new ActionError("  "));
		}

		return errors;

	}

	/**
	 * @return the displaysearchString
	 */
	public String getDisplaySearchString()
	{
		return this.displaySearchString;
	}

	/**
	 * @param displaysearchString the displaysearchString to set
	 */
	public void setDisplaySearchString(String displaySearchString)
	{
		this.displaySearchString = displaySearchString;
	}

	/**
	 * @return the displayStats
	 */
	public String getDisplayStats()
	{
		return this.displayStats;
	}

	/**
	 * @param displayStats the displayStats to set
	 */
	public void setDisplayStats(String displayStats)
	{
		this.displayStats = displayStats;
	}

}
