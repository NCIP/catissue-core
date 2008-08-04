<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="java.util.*"%>
<%@ page import="java.util.List,edu.wustl.catissuecore.util.global.Constants,edu.wustl.catissuecore.util.global.Utility"%>
<%@ page import="edu.wustl.catissuecore.util.global.Variables"%>
<%@ page import="java.util.ArrayList"%>
<%@ page language="java" isELIgnored="false"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<!-- 
	 @author Virender Mehta 
	 @version 1.1	
	 Jsp name: consentDialog.jsp
	 Description: This jsp is associated with ConsentTracking.jsp for pageOf CollectionprotocolResistration,
	 SpecimenCollectionGroup and NewSpecimen.
	 Company: Washington University, School of Medicine, St. Louis.
-->		


<%
	String withdrawAll = request.getParameter(Constants.WITHDRAW_ALL);
	String getConsentResponse = request.getParameter(Constants.RESPONSE);
	String pageOf = request.getParameter("pageOf");
	request.setAttribute("pageOf",pageOf);
	Integer identifierFieldIndex = new Integer(4);
	request.setAttribute("identifierFieldIndex",identifierFieldIndex);
%>
<script language="JavaScript">

function getButtonStatus(element)
{
	var answer;
	if(element.value=="<%=Constants.WITHDRAW_RESPONSE_DISCARD %>")
	{
		answer= confirm("Are you sure you want to discard the Specimen and all Sub Specimen(disable)?");
	}
	else
	{
		if(element.value=="<%=Constants.WITHDRAW_RESPONSE_RESET%>")
		{
			answer= confirm("Are you sure you don't want to perform any action on the specimen?");	
		}
		else
		{
			answer= confirm("Are you sure you want to return Specimen to Collection Site?");	
		}
	}
	if(answer)
	{
		parent.opener.document.forms[0].withdrawlButtonStatus.value=element.value;
		if(parent.opener.document.forms[0].name == "<%=Constants.NEWSPECIMEN_FORM%>")
		{
			if(element.value != "<%=Constants.WITHDRAW_RESPONSE_RESET %>")
			{
				parent.opener.document.forms[0].activityStatus.value="<%=Constants.ACTIVITY_STATUS_DISABLED%>" ;
				parent.opener.document.forms[0].onSubmit.value="<%=Constants.BIO_SPECIMEN%>";
				parent.opener.document.forms[0].target = "_top";
			}
			else
			{
					<%	if(pageOf.equals(Constants.PAGE_OF_SPECIMEN))
					{
					%>	
						parent.opener.document.forms[0].action="<%=Constants.SPECIMEN_EDIT_ACTION%>";	
					<%
					}
					else
				    {
					%>
						parent.opener.document.forms[0].action="<%=Constants.CP_QUERY_SPECIMEN_EDIT_ACTION%>";				
					<%
					}
					%>
			}
		}
		parent.opener.document.forms[0].submit();
		if(parent.opener.document.forms[0].name == "consentForm")
		{
			parent.opener.self.close();
		}
		self.close();
	}

}

function getStatus(element)
{
	var answer= confirm("Are you sure about your action on the Specimens");
	if(answer)
	{
		parent.opener.document.forms[0].applyChangesTo.value=element.value;
		if(parent.opener.document.forms[0].name == "<%=Constants.NEWSPECIMEN_FORM%>")
		{
			<%	
			if(pageOf.equals(Constants.PAGE_OF_SPECIMEN) ||pageOf.equals(Constants.PAGEOF_NEW_SPECIMEN) )
			{
			%>	
				parent.opener.document.forms[0].action="<%=Constants.SPECIMEN_EDIT_ACTION%>";
			<%
			}
			else
			{
			%>
				parent.opener.document.forms[0].action="<%=Constants.CP_QUERY_SPECIMEN_EDIT_ACTION%>";
			<%
			}
			%>
		}
		else
		{
			parent.opener.document.forms[0].action="<%=Constants.CP_QUERY_SPECIMEN_COLLECTION_GROUP_EDIT_ACTION%>";
		}
		parent.opener.document.forms[0].submit();
		if(parent.opener.document.forms[0].name == "consentForm")
		{
			parent.opener.self.close();
		}
	self.close();
	}
}

function cancelWindow()
{
  self.close();
}

var useDefaultRowClickHandler=2;
var useFunction = "derivedSpecimenGrid";	

</script>


<html>
	<head>
		<%
			if(withdrawAll.equals(Constants.TRUE))
			{
		%>	
				<title><bean:message key="consent.withdrawconsents"/></title>	 
		<%		
			}
			else
			{
		%>	
				<title><bean:message key="consent.withdrawconsenttier" /></title>	 
		<%		
			}	
		%>	
		
	</head>
		<body >
		<script  src="../../../dhtml_comp/js/dhtmlXCommon.js"></script>
		<script  src="../../../dhtml_comp/js/dhtmlXGrid.js"></script>		
		<script  src="../../../dhtml_comp/js/dhtmlXGridCell.js"></script>	
		<script  src="../../../dhtml_comp/js/dhtmlXGrid_mcol.js"></script>	
		<link rel="STYLESHEET" type="text/css" href="../../../dhtml_comp/css/dhtmlXGrid.css"/> 
		<link href="../../../css/catissue_suite.css" rel="stylesheet" type="text/css" />

		

<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
  <tr>
    <td align="left" class="toptd"></td>
  </tr>
  <tr>
    <td align="left" class="tr_bg_blue1"><p class="blue_ar_b">&nbsp;<bean:message key="specimen.details.label" /></td>
  </tr>
 <tr>
    <td align="center" class="showhide">
    <%
			List dataList = (List)session.getAttribute(Constants.SPECIMEN_LIST);
			List columnList = (List)session.getAttribute(Constants.COLUMNLIST);
			Utility.setGridData(dataList,columnList,request);//JSP refactoring...to be removed after refacoring of this JSP(consent).
			request.setAttribute("colWidthInPercent",Utility.getcolWidth(columnList,true));
		if(dataList!=null&&dataList.size()>0)
			{
	%>
	
		   	<%@ include file="/pages/content/search/GridPage.jsp" %> 

		<%
			}
		%>
  </td>
  </tr>
 
  <tr >
          <td class="showhide"><table width="99%" border="0" cellspacing="0" cellpadding="3">
   
   <%
		if(getConsentResponse.equals(Constants.WITHDRAW))
		{
  %> 
         <tr>
           <td align="left" class="tr_bg_blue1"><span
					class="blue_ar_b"><%
							if(withdrawAll.equals(Constants.TRUE))
							{
						%>	
								<bean:message key="consent.withdrawspecimens" />
						<%		
							}
							else
							{
						%>	
								<bean:message key="consent.withdrawquestion" />
						<%		
							}
						%>		
			</span></td>
	  </tr>
      <tr>
        <td width="45%" class="black_ar"><bean:message key="consent.discard" /></td>
        </tr>
      <tr>
        <td class="black_ar"><bean:message key="consent.returntocollectionsite" /></td>
      </tr>
     
	
	<%	if(withdrawAll.equals(Constants.FALSE))									
	  {
	%>
	      <tr>
            <td width="45%" class="black_ar"><bean:message key="consent.noaction" /></td>
          </tr>
    <%
	  }
	%>

	</table></td>
  </tr>
   <tr >
    <td class="buttonbg"><input name="Submit" type="submit" class="blue_ar_b" value="<%=Constants.WITHDRAW_RESPONSE_DISCARD %>" onclick="getButtonStatus(this)" accesskey="Enter" />| <input name="Submit" type="submit" class="blue_ar_b" value="<%=Constants.WITHDRAW_RESPONSE_RETURN%>" onclick="getButtonStatus(this)" accesskey="Enter" />
	 <% if(withdrawAll.equals(Constants.FALSE))									
					{
					%>
					 |<input name="Submit" type="submit" class="blue_ar_b" value="<%=Constants.WITHDRAW_RESPONSE_RESET %>" onclick="getButtonStatus(this)" accesskey="Enter" />
					 <% } %>
    
<%
		}
		else
		{
		%>   	
		         <tr>
                    <td align="left" class="tr_bg_blue1"><span
					class="blue_ar_b"> <bean:message key="consent.applystatusquestion" />
                  </tr>


		
		        <tr>
                   <td width="45%" class="black_ar"><bean:message key="consent.currentstatusonnonconflictingspecimen" /></td>
                </tr>
                <tr>
                  <td width="45%" class="black_ar"><bean:message key="consent.currentstatusonallspecimen" /></td>
                </tr>
			</table> </td> </tr>
			<tr>
			   <td class="buttonbg"><input name="Submit" type="submit" class="blue_ar_b" value="<%=Constants.APPLY %>"  onclick="getStatus(this)" accesskey="Enter" />|

			   <input name="Submit" type="submit" class="blue_ar_b" value="<%=Constants.APPLY_ALL%>"  onclick="getStatus(this)" accesskey="Enter" />
			   
			  
		<%  
		}
		%>

| <a href="javascript:cancelWindow()" class="cancellink"><bean:message key="buttons.cancel"/> </a></td>



  </tr>    
</table>	
		
		
		</body>
</html>