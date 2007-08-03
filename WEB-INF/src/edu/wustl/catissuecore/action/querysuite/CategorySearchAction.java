
package edu.wustl.catissuecore.action.querysuite;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.client.metadatasearch.MetadataSearch;
import edu.wustl.cab2b.common.beans.MatchedClass;
import edu.wustl.cab2b.common.util.Constants;
import edu.wustl.cab2b.common.util.EntityInterfaceComparator;
import edu.wustl.cab2b.server.cache.EntityCache;
import edu.wustl.catissuecore.actionForm.CategorySearchForm;
import edu.wustl.catissuecore.util.querysuite.EntityCacheFactory;
import edu.wustl.common.action.BaseAction;

/**
 * This class loads screen for categorySearch.
 * When search button is clicked it checks for the input : Text , checkbox , radiobutton etc. And depending upon the selections made by user,
 * the list of entities is populated. This list is kept in session.
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
		HttpSession session = request.getSession();
		CategorySearchForm searchForm = (CategorySearchForm) form;
		String currentPage = searchForm.getCurrentPage();
		String textfieldValue = searchForm.getTextField();
		Map<String, EntityInterface> searchedEntitiesMap = (Map<String, EntityInterface>) session.getAttribute(edu.wustl.catissuecore.util.global.Constants.SEARCHED_ENTITIES_MAP);
		if (searchedEntitiesMap == null)
		{
			searchedEntitiesMap = new HashMap<String, EntityInterface>();
		}
		if(currentPage != null && currentPage.equalsIgnoreCase("prevToAddLimits"))
		{
			textfieldValue = "";
		}
		if (textfieldValue != null && !textfieldValue.equals(""))
		{
			int[] searchTarget = prepareSearchTarget(searchForm);
			int basedOn = prepareBaseOn(searchForm.getSelected());
			Set<EntityInterface> entityCollection = new HashSet<EntityInterface>();
			String[] searchString = null;
			searchString = prepareSearchString(textfieldValue);
			String entitiesString = "";
			EntityCache cache = EntityCacheFactory.getInstance();
			MetadataSearch advancedSearch = new MetadataSearch(cache);
			MatchedClass matchedClass = advancedSearch.search(searchTarget, searchString, basedOn);
			entityCollection = matchedClass.getEntityCollection();
			List resultList = new ArrayList(entityCollection);
			Collections.sort(resultList, new EntityInterfaceComparator());
			if(currentPage != null && currentPage.equalsIgnoreCase(edu.wustl.catissuecore.util.global.Constants.DEFINE_RESULTS_VIEW))
			{
				entitiesString = generateHTMLToDisplayList(resultList,searchedEntitiesMap);
			}
			else if(currentPage != null)
			{
				if(currentPage.equalsIgnoreCase("") || currentPage.equalsIgnoreCase(edu.wustl.catissuecore.util.global.Constants.ADD_LIMITS))
				{
					for (int i = 0; i < resultList.size(); i++)
					{
						EntityInterface entity = (EntityInterface) resultList.get(i);
						String fullyQualifiedEntityName = entity.getName();
						String description = entity.getDescription();
						entitiesString = entitiesString + ";" + fullyQualifiedEntityName + "|" + description;
						searchedEntitiesMap.put(fullyQualifiedEntityName, entity);
					}
				}
				request.getSession().setAttribute(edu.wustl.catissuecore.util.global.Constants.SEARCHED_ENTITIES_MAP, searchedEntitiesMap);
			}
			response.setContentType("text/html");
			response.getWriter().write(entitiesString);
			return null;
		}
		return mapping.findForward(edu.wustl.catissuecore.util.global.Constants.SUCCESS);
	}
	/**
	 * Generates HTML for entities to be displayed on define results page.
	 * These entities will be shown in a Listbox.
	 * @param resultArray array of entities in ascending alphabetical order 
	 * @param searchedEntitiesMap map to store the entities found for given criteria
	 * @return String representing html for a listbox 
	 */
	String generateHTMLToDisplayList(List resultList,Map<String, EntityInterface> searchedEntitiesMap)
	{
		String selectTagName =edu.wustl.catissuecore.util.global.Constants.SEARCH_CATEGORY_LIST_SELECT_TAG_NAME;				
		StringBuffer html = new StringBuffer();		
		int size = resultList.size();
		if ( size != 0)
		{
			html.append("\n<select id='"+selectTagName+"' name='"+ selectTagName + "' MULTIPLE size = '"+size+"'>");
			for(int i=0;i<size;i++)
			{
				EntityInterface entity = (EntityInterface)resultList.get(i);
				String fullyQualifiedEntityName = entity.getName();
				int lastIndex = fullyQualifiedEntityName.lastIndexOf(".");
				String entityName = fullyQualifiedEntityName.substring(lastIndex + 1);				
				searchedEntitiesMap.put(fullyQualifiedEntityName, entity);
				html.append("\n<option class=\"dropdownQuery\" value=\"" + entity.getName() + "\">" + entityName + "</option>");
			}
			html.append("\n</select>");
		}
		return html.toString();
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
		if (basedOnStr != null)
		{
			if (basedOnStr.equalsIgnoreCase("conceptCode_radioButton"))
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

		if (classCheckBoxChecked != null && (classCheckBoxChecked.equalsIgnoreCase("on") || classCheckBoxChecked.equalsIgnoreCase("true")))
		{
			target.add(new Integer(Constants.CLASS));
		}
		if (attributeCheckBoxChecked != null && (attributeCheckBoxChecked.equalsIgnoreCase("on") || attributeCheckBoxChecked.equalsIgnoreCase("true")))
		{
			target.add(new Integer(Constants.ATTRIBUTE));
		}
		if (permissiblevaluesCheckBoxChecked != null && (permissiblevaluesCheckBoxChecked.equalsIgnoreCase("on") || permissiblevaluesCheckBoxChecked.equalsIgnoreCase("true")))
		{
			target.add(new Integer(Constants.PV));
		}
		int[] searchTarget = new int[target.size()];
		for (int i = 0; i < target.size(); i++)
		{
			searchTarget[i] = ((Integer) (target.get(i))).intValue();
		}
		return searchTarget;
	}


}