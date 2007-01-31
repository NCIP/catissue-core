
package edu.wustl.catissuecore.bizlogic.querysuite;

import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import edu.common.dynamicextensions.domain.DateAttributeTypeInformation;
import edu.common.dynamicextensions.domaininterface.AttributeTypeInformationInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.catissuecore.util.global.Constants;



public class GenerateHTMLForBuildNewTree {

	
	public String generateHTMLToDisplayList(Set entityCollection, String selectTagName, String functionName)
	{
		
		StringBuffer html = new StringBuffer();		
		if (!entityCollection.isEmpty())
		{
			html.append("\n<td width=\"100%\">");			
			
			//html.append("\n<select name='"+ selectTagName + "' multiple ='true' size='10' onblur='" + Constants.SEARCH_CATEGORY_LIST_FUNCTION_NAME + "()' >");
			html.append("\n<select name='"+ selectTagName + "' multiple ='true' size='10'>");
			
			Iterator iter = entityCollection.iterator();

			while (iter.hasNext())
			{
				EntityInterface entity = (EntityInterface)iter.next();
				int lastIndex = entity.getName().lastIndexOf(".");
				String entityName = entity.getName().substring(lastIndex + 1);				
				html.append("\n<option value=\"" + entityName + "\">" + entityName + "</option>");
			}
			html.append("\n</select>");
			html.append("\n</td>");
			
		}
		return html.toString();
				
	}
	
}
