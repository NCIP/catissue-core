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
		return selectedLabel;
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
		return searchString;
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
		return sortedResultMap;
	}

	/**
	 * @param sortedResultMap
	 *            the sortedResultMap to set
	 */
	public void setSortedResultMap(SortedResultMapInterface sortedResultMap)
	{
		this.sortedResultMap = sortedResultMap;

		titliResultMap = new LinkedHashMap<Name, TitliResultGroup>();

		for (ResultGroupInterface i : sortedResultMap.values())
		{
			titliResultMap.put(i.getTableName(), new TitliResultGroup(i));
		}

	}

	/**
	 * 
	 * @return the result group corresponding to the selected label
	 */
	public TitliResultGroup getSelectedGroup()
	{
		ResultGroupInterface i=null;
		try
		{
			i = sortedResultMap.get(TitliTableMapper.getInstance().getTable(selectedLabel));
			
		}
		catch(Exception e)
		{
			Logger.out.error("Exception in TitliSearchForm : "+ e.getMessage(), e);
			
		}
		
		return new TitliResultGroup(i);

	}

	/**
	 * @return the titliResultMap
	 */
	public Map<Name, TitliResultGroup> getTitliResultMap()
	{
		return titliResultMap;
	}

	
	/**
	 * validate the input
	 * @param mapping the cation mapping
	 * @param request the request
	 * @return acttion errors
	 */
	public ActionErrors validate(ActionMapping mapping,HttpServletRequest request)
	{
		ActionErrors errors = new ActionErrors();

		String requestSearchString = request.getParameter("searchString");

		// searchString is null or empty
		if (requestSearchString == null|| requestSearchString.trim().equals(""))
		{
			errors.add("empty search string", new ActionError("  "));
		}

		if (requestSearchString.startsWith("*")|| requestSearchString.startsWith("?"))
		{
			errors.add("search string starts with * or ? ", new ActionError(	"  "));
		}

		return errors;

	}

	/**
	 * @return the displaysearchString
	 */
	public String getDisplaySearchString()
	{
		return displaySearchString;
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
		return displayStats;
	}

	/**
	 * @param displayStats the displayStats to set
	 */
	public void setDisplayStats(String displayStats)
	{
		this.displayStats = displayStats;
	}

}
