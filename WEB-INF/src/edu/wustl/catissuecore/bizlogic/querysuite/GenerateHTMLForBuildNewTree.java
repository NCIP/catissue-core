
package edu.wustl.catissuecore.bizlogic.querysuite;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.common.util.EntityInterfaceComparator;



public class GenerateHTMLForBuildNewTree {

	
	public String generateHTMLToDisplayList(Set entityCollection, String selectTagName, String functionName)
	{
		
		StringBuffer html = new StringBuffer();		
		int size = entityCollection.size();
		
		if (!entityCollection.isEmpty())
		{
			List resultList = new ArrayList(entityCollection);
			Collections.sort(resultList, new EntityInterfaceComparator());
			//html.append("\n<td height=\"100%\" width=\"100%\">");			
			
			//html.append("\n<select name='"+ selectTagName + "' multiple ='true' size='10' onblur='" + Constants.SEARCH_CATEGORY_LIST_FUNCTION_NAME + "()' >");
			html.append("\n<select id='"+selectTagName+"' name='"+ selectTagName + "' MULTIPLE size = '"+size+"'>");
			
			for(int i=0;i<size;i++)
			{
				EntityInterface entity = (EntityInterface)resultList.get(i);
				int lastIndex = entity.getName().lastIndexOf(".");
				String entityName = entity.getName().substring(lastIndex + 1);				
				html.append("\n<option class=\"dropdownQuery\" value=\"" + entity.getName() + "\">" + entityName + "</option>");
			}
			html.append("\n</select>");
		//	html.append("\n</td>");
			
		}
		return html.toString();
				
	}
	
}
