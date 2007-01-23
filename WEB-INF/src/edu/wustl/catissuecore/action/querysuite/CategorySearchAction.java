
package edu.wustl.catissuecore.action.querysuite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.common.beans.MatchedClass;
import edu.wustl.cab2b.common.util.Constants;
import edu.wustl.cab2b.server.advancedsearch.AdvancedSearch;
import edu.wustl.cab2b.server.cache.EntityCache;
import edu.wustl.catissuecore.actionForm.CategorySearchForm;
import edu.wustl.catissuecore.util.querysuite.EntityCacheFactory;
import edu.wustl.common.action.BaseAction;
/**
 * This class loads screen for categorySearch.When it loads the screen for the first time, default selctions for checkbox and radio buttons
 * will be shown.
 * When search button is clicked it checks for the input : Text , checkbox , radiobutton etc. And depending upon the selections made by user,
 * the list of entities is populated. This list is kept in session.
 * 
 * @author deepti_shelar
 */

public class CategorySearchAction extends BaseAction
{
	/**
	 * This method loads the data required for categorySearch.jsp
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
		CategorySearchForm searchForm = (CategorySearchForm) form;
		String textfieldValue = searchForm.getTextField();
		searchForm = setDefaultSelections(searchForm);
		String [] searchString = null;

		int [] searchTarget = prepareSearchTarget(searchForm);
		int basedOn = prepareBaseOn(searchForm.getSelected());
		if (textfieldValue != null && !textfieldValue.equals(""))
		{
			searchString = prepareSearchString(textfieldValue);
		}
		Set<EntityInterface> entityCollection = new HashSet<EntityInterface>();
		Map<String, EntityInterface> searchedEntitiesMap = new HashMap<String, EntityInterface>();
		if (textfieldValue != null && !textfieldValue.equals(""))
		{
			EntityCache cache = EntityCacheFactory.getInstance();
			AdvancedSearch advancedSearch = new AdvancedSearch(cache);
			MatchedClass matchedClass = advancedSearch.search(searchTarget, searchString, basedOn);
			entityCollection = matchedClass.getEntityCollection();
			String entitiesString = "";
			if (entityCollection != null && !entityCollection.isEmpty())
			{
				Iterator iter = entityCollection.iterator();
				while(iter.hasNext())
				{
					EntityInterface entity = (EntityInterface)iter.next();
					int lastIndex = entity.getName().lastIndexOf(".");
					String entityName = entity.getName().substring(lastIndex + 1);
					entitiesString = entitiesString + ";" + entityName;
					searchedEntitiesMap.put(entityName, entity);
				}
			}
			request.getSession().setAttribute(edu.wustl.catissuecore.util.global.Constants.SEARCHED_ENTITIES_MAP, searchedEntitiesMap);
			response.setContentType("text/html");
			response.getWriter().write(entitiesString);
			return null;
		}

		return mapping.findForward(edu.wustl.catissuecore.util.global.Constants.SUCCESS);
	}
	/**
	 * Prepares a String to be sent to AdvancedSearch logic.
	 * @param textfieldValue String
	 * @return String[] array of strings , taken from user. 
	 */
	private String[] prepareSearchString(String textfieldValue)
	{
		int counter = 0;
		StringTokenizer tokenizer = new StringTokenizer(textfieldValue);
		String[] searchString = new String[tokenizer.countTokens()];
		while (tokenizer.hasMoreTokens()) 
		{
			searchString[counter] = tokenizer.nextToken();
			counter++;
		}
		return searchString;
	}
	/**
	 * Returns a int constant for radil option selected by user which represents Based on.
	 * @param basedOnStr String
	 * @return int basedOn
	 */
	private int prepareBaseOn(String basedOnStr)
	{
		int basedOn = Constants.BASED_ON_TEXT; 
		if(basedOnStr != null)
		{
			if(basedOnStr.equalsIgnoreCase("conceptCode_radioButton"))
			{
				basedOn = Constants.BASED_ON_CONCEPT_CODE;			
			}
		}
		return basedOn;
	}
	/**
	 * Prepares the int [] for search targets from the checkbox values selected by user.
	 * @param searchForm action form
	 * @return int[] Integer array of selections made by user.
	 */
	private int[] prepareSearchTarget(CategorySearchForm searchForm)
	{
		String classCheckBoxChecked = searchForm.getClassChecked();
		String attributeCheckBoxChecked = searchForm.getAttributeChecked();
		String permissiblevaluesCheckBoxChecked = searchForm.getPermissibleValuesChecked();
		List<Integer> target = new ArrayList<Integer>();

		if(classCheckBoxChecked.equalsIgnoreCase("on") || classCheckBoxChecked.equalsIgnoreCase("true"))
		{
			target.add(new Integer(Constants.CLASS));
		}
		if(attributeCheckBoxChecked.equalsIgnoreCase("on") || attributeCheckBoxChecked.equalsIgnoreCase("true"))
		{
			target.add(new Integer(Constants.ATTRIBUTE));
		}
		if(permissiblevaluesCheckBoxChecked.equalsIgnoreCase("on") || permissiblevaluesCheckBoxChecked.equalsIgnoreCase("true"))
		{
			target.add(new Integer(Constants.PV));
		}
		int [] searchTarget = new int[target.size()];
		for(int i=0;i<target.size();i++)
		{
			searchTarget[i] = ((Integer)(target.get(i))).intValue();
		}
		return searchTarget;
	}
	/**
	 * This is used to set the default selections for the UI components when the screen is loaded for the first time.
	 * @param actionForm form bean
	 * @return CategorySearchForm formbean
	 */
	private CategorySearchForm setDefaultSelections(CategorySearchForm actionForm)
	{
		if (actionForm.getClassChecked() == null)
		{
			actionForm.setClassChecked("on");
		}
		if (actionForm.getAttributeChecked() == null)
		{
			actionForm.setAttributeChecked("on");
		}
		if (actionForm.getPermissibleValuesChecked() == null)
		{
			actionForm.setPermissibleValuesChecked("off");
		}
		//TODO check if null and then set the value of seleted.
		actionForm.setSelected("text_radioButton");
		return actionForm;
	}
}