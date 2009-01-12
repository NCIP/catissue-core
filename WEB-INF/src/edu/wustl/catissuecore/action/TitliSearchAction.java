/**
 * 
 */
package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
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
import edu.wustl.common.util.global.Constants;
import edu.wustl.common.util.logger.Logger;

/**
 * perform Titli Search
 * @author Juber Patel
 * 
 */
public class TitliSearchAction extends Action
{

	/**
	 * @param mapping the mapping
	 * @param form the action form
	 * @param request the request
	 * @param response the response
	 * @return action forward
	 * 
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,	HttpServletRequest request, HttpServletResponse response)
	{   

		TitliSearchForm titliSearchForm = (TitliSearchForm) form;
		Logger.out.info("Search string entered is...... :"+ titliSearchForm.getSearchString());

		try 
		{    
			TitliInterface titli = Titli.getInstance();
			String searchString = titliSearchForm.getSearchString().trim();
			MatchListInterface matchList = titli.search(searchString);
			
			SortedResultMapInterface sortedResultMap = matchList.getSortedResultMap();
						
			// set the result in the action form
			titliSearchForm.setSortedResultMap(sortedResultMap); 
			request.getSession().setAttribute(Constants.TITLI_SORTED_RESULT_MAP,sortedResultMap);
			
			
			//set the internationalized displaySearchString 
			List placeHolders = new ArrayList();
			placeHolders.add(searchString);
			//String displaySearchString = ApplicationProperties.getValue(Constants.TITLI_SEARCH_STRING_PROPERTY, placeHolders);
			titliSearchForm.setDisplaySearchString("TiTLi Search");
			
			
			//set the internationalized displayStats
			placeHolders = new ArrayList();
			placeHolders.add(Integer.toString(matchList.getNumberOfMatches()));
			placeHolders.add(Double.toString(matchList.getTimeTaken()));
			//String displayStats = ApplicationProperties.getValue(Constants.TITLI_STATS_PROPERTY, placeHolders);
			titliSearchForm.setDisplayStats("Found "+matchList.getNumberOfMatches()+" matches in "+matchList.getTimeTaken()+" seconds");
			
			//if matches are from just one table, go directly to TitliFetchAction, skip TitliResultUpdatable.jsp 
			if(sortedResultMap.size()==1)
			{
				try
				{
					Name tableName = sortedResultMap.keySet().toArray(new Name[0])[0];
					String label = TitliTableMapper.getInstance().getLabel(tableName);
					
					//set the selectedLabel to the label of the only table
					//setting the selectedLabel is necessary for getSelectedGroup() to work properly
					titliSearchForm.setSelectedLabel(label);
					
				}
				catch (Exception e)
				{
					Logger.out.error("Exception in TitliFetchAction : "	+ e.getMessage(), e);
				}
				
				String path =Constants.TITLI_FETCH_ACTION;
				return getActionForward(Constants.TITLI_SINGLE_RESULT, path);
				
			}
			
		}
		catch (TitliException e)
		{
			Logger.out.error("TitliException in TitliSearchAction : "+ e.getMessage(), e);
		}
		System.out.println("from titli search action..............!!");
		return mapping.findForward(Constants.SUCCESS);
	}
	
	
	/**
	 * @param name name 
	 * @param path path
	 * @return the created ActionForward
	 */
	private ActionForward getActionForward(String name, String path)
	{
		ActionForward actionForward = new ActionForward();
		actionForward.setName(name);
		actionForward.setPath(path);

		return actionForward;
	}
	
}
