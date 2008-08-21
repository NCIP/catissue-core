<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List,edu.wustl.catissuecore.util.global.Constants,edu.wustl.catissuecore.util.global.Utility"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.caties.util.CaTIESConstants"%>

<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>

<%
	String frame1Ysize = "166";
	String frame2Ysize = "264";
	String frame3Ysize = "264";
	Boolean useSelPartDisable=false;
	Boolean	useSelSCGDisable=false;
	Boolean	crtNewPartDisable=false;
	Boolean	crtNewSCGDisable=false;
	
	String reportQueueId = (String)request.getParameter(Constants.REPORT_ID);
	String conflictStatus = (String)request.getParameter(Constants.CONFLICT_STATUS);
	String surgicalPathologyNumber = (String)request.getParameter(Constants.SURGICAL_PATHOLOGY_NUMBER);
	String reportDate = (String)request.getParameter(Constants.REPORT_DATE);
	String siteName = (String)request.getParameter(Constants.SITE_NAME);
	String reportCollectionDate = (String)request.getParameter(Constants.REPORT_COLLECTION_DATE);
%>


<head>
	<link rel="stylesheet" type="text/css" href="css/styleSheet.css" />
	<link href="css/catissue_suite.css" rel="stylesheet" type="text/css" />
	<title>DHTML Tree samples. dhtmlXTree - Action handlers</title>
	<link rel="STYLESHEET" type="text/css" href="dhtml_comp/css/dhtmlXTree.css">
	<script language="JavaScript" type="text/javascript" src="dhtml_comp/js/dhtmXTreeCommon.js"></script>
	<script language="JavaScript" type="text/javascript" src="dhtml_comp/js/dhtmlXTree.js"></script>
	<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
</head>


<body>
<script language="javascript">

//This function is called when action button is pressed to resolve the conflict:
function onButtonClick(buttonPressed)
	{
				
		var actionUrl = "ConflictResolver.do?reportQueueId=<%=reportQueueId%>&conflictButton="+buttonPressed;
		document.forms[0].action = actionUrl;
		document.forms[0].submit();
		
	}
</script>
<script>
//Set the slope for the IFrame
if ( document.getElementById && !(document.all) ) 
{
	var slope=-7	;
}
else
{
	var slope=-8;
}

window.onload = function()
				{ 
						setFrameHeight('<%=Constants.CONFLICT_COMMON_VIEW%>', .31,slope);
						setFrameHeight('<%=Constants.CONFLICT_TREE_VIEW%>', .42,slope);
						setFrameHeight('<%=Constants.CONFLICT_DATA_VIEW%>', .42,slope); 
				}

window.onresize = function() 
				{
					setFrameHeight('<%=Constants.CONFLICT_COMMON_VIEW%>', .31,slope); 
					setFrameHeight('<%=Constants.CONFLICT_TREE_VIEW%>', .42,slope);
					setFrameHeight('<%=Constants.CONFLICT_DATA_VIEW%>', .42,slope);
				}
</script>

<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
<html:form action="ConflictResolver.do">
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="maintable">
  <tr>
    <td class="td_color_bfdcf3"><table border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td nowrap="nowrap" class="td_table_head"><span class="wh_ar_b"><bean:message key="app.reportedConflicts"/></span></td>
        <td><img src="images/uIEnhancementImages/table_title_corner2.gif" alt="Page Title - Conflicting Reports" width="31" height="24" /></td>
      </tr>
    </table></td>
  </tr>
  <tr>
    <td class="tablepadding"><table width="100%" border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td width="90%" valign="bottom" class="td_tab_bg">&nbsp;</td>
      </tr>
    </table>
	<table width="100%" border="0" cellpadding="0" cellspacing="0" class="whitetable_bg">
		<tr>
		<td>
			<%@ include file="/pages/content/common/ActionErrors.jsp" %>
		</td>
				<td>
						<iframe id="<%=Constants.CONFLICT_COMMON_VIEW%>" name="<%=Constants.CONFLICT_COMMON_VIEW%>" src="<%=Constants.CONFLICT_COMMON_VIEW_ACTION%>?conflictStatus=<%=conflictStatus%>&reportQueueId=<%=reportQueueId%>&surgicalPathologyNumber=<%=surgicalPathologyNumber%>&reportDate=<%=reportDate%>&siteName=<%=siteName%>&reportCollectionDate=<%=reportCollectionDate%>" scrolling="no" frameborder="0" width="100%" marginheight=0 marginwidth=0>
									<bean:message key="errors.browser.not.supports.iframe"/>
						</iframe>
				</td>
		  </tr>
	</table>
	
	<table width="100%" border="0" cellpadding="0" cellspacing="0" class="whitetable_bg">

				<tr>
					<td width="100%" valign="top">
					 <%if(conflictStatus.equals(CaTIESConstants.STATUS_SCG_CONFLICT))
						{
					%>
						<iframe name="" src="ConflictSCGAction.do?reportQueueId=<%=reportQueueId%>" scrolling="no" frameborder="0" width="100%" >
									<bean:message key="errors.browser.not.supports.iframe"/>
						</iframe>

					<%}
					   else
						{
					%>
						<table width="100%" border="0" cellpadding="0" cellspacing="0">
							<tr>
								<td width="30%"  valign="top">		
			
								<iframe id="<%=Constants.CONFLICT_TREE_VIEW%>" name="<%=Constants.CONFLICT_TREE_VIEW%>" src="<%=Constants.CONFLICT_TREE_VIEW_ACTION%>?reportQueueId=<%=reportQueueId%>&conflictStatus=<%=conflictStatus%>" scrolling="no" frameborder="0" width="100%" marginheight=0 marginwidth=0 valign="top">
									<bean:message key="errors.browser.not.supports.iframe"/>
								</iframe>
			
								</td>
							
								<td width="70%" valign="top" >				
									<iframe id="<%=Constants.CONFLICT_DATA_VIEW%>" name="<%=Constants.CONFLICT_DATA_VIEW%>" src="<%=Constants.BLANK_SCREEN_ACTION%>" scrolling="no" frameborder="0" width="100%" >
									<bean:message key="errors.browser.not.supports.iframe"/>
									</iframe>
								</td>
							</tr>	
						</table>	
				<%}%>
					</td>
					
				</tr>
				<tr >
					<td align="left">
					<%if(conflictStatus.equals(CaTIESConstants.STATUS_SCG_CONFLICT))
						{
					%>
						<%@ include file="/pages/content/manageAdministrativeData/ConflictSCGButtons.jsp"%>
					<%}
					   else
						{
					%>
						<%@ include file="/pages/content/manageAdministrativeData/ConflictPageButtons.jsp"%>

					<%}%>
					</td>
				</tr>
	</table>
				</td>
		  </tr>
	

			</table>	
</html:form>
	
