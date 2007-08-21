package edu.wustl.catissuecore.action;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.util.global.Constants;

//import edu.wustl.common.action.SecureAction;

public class MultipleSpecimenFlexInitAction extends Action
{
	public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        //Gets the value of the operation parameter.
        //String operation = request.getParameter(Constants.OPERATION);
		String mode = "ADD";
		String parentType = "SCG";
		
		String numberOfSpecimens = getNumberOfSpecimens(request);
		String parentName = getParentName(request, parentType);
        
		setMSPRequestParame(request, mode, parentType, parentName, numberOfSpecimens);
        return mapping.findForward("success");
    }
	
	private void setMSPRequestParame(HttpServletRequest request, String mode, String parentType, String parentName, String numberOfSpecimens)
	{
		//Sets the operation attribute to be used in the Add/Edit Department Page. 
        request.setAttribute("MODE",mode);
        request.setAttribute("PARENT_TYPE", parentType);
        request.setAttribute("PARENT_NAME", parentName);
        request.setAttribute("SP_COUNT",numberOfSpecimens);
	}
	
	private String getParentName(HttpServletRequest request, String parentType)
	{
		if(parentType.equals("SCG"))
		{
			String specimenCollectionGroupName = "";
			HashMap forwardToHashMap = (HashMap) request.getAttribute("forwardToHashMap");
			if(forwardToHashMap!=null)
			{
				Object obj = forwardToHashMap.get("specimenCollectionGroupName");
				if(obj!=null)
				{
					specimenCollectionGroupName = (String)obj;
				}
			}
			return specimenCollectionGroupName;
		}
		else if(parentType.equals("SP"))
		{
			//TODO
			return ""; 
		}
		return "";	
	}
	
	private String getNumberOfSpecimens(HttpServletRequest request)
	{
		String numberOfSpecimens = request.getParameter(Constants.NUMBER_OF_SPECIMENS);
		System.out.println("numberOfSpecimens "+numberOfSpecimens);
		if( numberOfSpecimens==null || numberOfSpecimens.equals(""))
		{
			numberOfSpecimens = "1";
		}
		return numberOfSpecimens;
	}
}