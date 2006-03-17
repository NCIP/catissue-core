<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.actionForm.AdvanceSearchForm"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %> 
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<head>
    <script language="JavaScript">

    </script>
    <%
    String objectName = request.getParameter("objectName");
   	String action = "ParticipantAdvanceSearch.do?pageOf=pageOfParticipantAdvanceSearch";
	if(objectName != null)
	{
		if(objectName.equals(Constants.PARTICIPANT))
		{
			action = "CollectionProtocolAdvanceSearch.do?pageOf=pageOfCollectionProtocolAdvanceSearch";
		}
		else if(objectName.equals(Constants.COLLECTION_PROTOCOL))
		{
			action = "SpecimenCollectionGroupAdvanceSearch.do?pageOf=pageOfSpecimenCollectionGroupAdvanceSearch";
		}
		else if(objectName.equals(Constants.SPECIMEN_COLLECTION_GROUP))
		{
			action = "SpecimenAdvanceSearch.do?pageOf=pageOfSpecimenAdvanceSearch";
		}
		else if(objectName.equals(Constants.SPECIMEN))
		{
			action = "SpecimenAdvanceSearch.do?pageOf=pageOfSpecimenAdvanceSearch";
		}
	}
	%>
</head>

<html:form action="<%=Constants.ADVANCED_SEARCH_RESULTS_ACTION%>">

<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="100%" height="100%">
       
    <tr width="100%">
        <td height="5%">
			<table width="100%" height="100%">
			<tr>
				<td width="100%">
		        	<html:errors />
				</td>
			</tr>
			</table>
        </td>
    </tr>
    
     <tr width="100%">
       <td height="65%">
       		<table width="100%" height="100%"> 
			<tr>
				<td width="100%"><div id="ifr1">
				    <iframe name="searchPageFrame" id="searchPageFrame" src="<%=action%>" width="90%" height="100%" frameborder="0" scrolling="auto">
		            </iframe></div>
				</td>
			</tr>
			</table>
        </td>
     </tr>

    <tr width="100%">
        <td height="25%">
			<table width="100%" height="100%"> 
			<tr>
				<td width="100%"><div id="ifr2">
		            <iframe name="queryFrame" id="queryFrame" src="AdvanceQueryView.do" width="80%" height="100%" frameborder="0" scrolling="auto">
        		    </iframe></div>
				</td>
			</tr>
			</table>
        </td>
    </tr>
    
    <tr width="100%">
        <td height="5%">
			<table width="100%" height="100%">
			<tr>
				<td width="100%">
		        	<html:submit styleClass="actionButton" >
	        	    	<bean:message  key="buttons.search" />
					</html:submit>
				</td>
			</tr>
			</table>
		</td>
    </tr>
</table>
<!-- Code to display iframe on Mac  -->
<script type="text/javascript">
var platformName = navigator.platform;
//alert(platformName + " : " + platformName.substring(0,3));
if(platformName.substring(0,3) == "Mac")
{
	var d1 = document.getElementById("ifr1");
	var w1 = Math.round(.60 * screen.availWidth);
	var h1 = Math.round(.50 * screen.availHeight);

	var str = "<iframe name='searchPageFrame' id='searchPageFrame' src='" + "<%=action%>" + "' width='"+ w1 +"' height='"+h1+"' frameborder='0' scrolling='auto'></iframe>";
	d1.innerHTML = str;

	var w2 = Math.round(.60 * screen.availWidth);
	var h2 = Math.round(.30 * screen.availHeight);
	var d2 = document.getElementById("ifr2");
	var str2 = "<iframe name='queryFrame' id='queryFrame' src='AdvanceQueryView.do' width='"+w2+"' height='"+h2+"' frameborder='0' scrolling='auto'></iframe>";
	d2.innerHTML = str2;
}
</script>


</html:form>