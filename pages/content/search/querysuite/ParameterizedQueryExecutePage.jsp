<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/PagenationTag.tld" prefix="custom"%>

<%-- Imports --%>
<%@
	page language="java" contentType="text/html"
	import="edu.wustl.catissuecore.util.global.Constants,edu.wustl.catissuecore.applet.AppletConstants"%>
<%
			ParameterizedQuery query = (ParameterizedQuery) session
			.getAttribute(AppletConstants.QUERY_OBJECT);
%>
<%@page
	import="edu.wustl.common.querysuite.queryobject.impl.ParameterizedQuery"%>
<html>
<head>
<link rel="stylesheet" type="text/css" href="css/styleSheet.css" />
<script src="jss/queryModule.js"></script>
<script src="jss/calender.js"></script>
<script>
		   function GotoRetriveAction()
		   {
		      var frm = document.forms[0];
		      frm.action="RetrieveQueryAction.do";
		      frm.submit();
		   }
		</script>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title><bean:message key="savequery.queryConditionTitle" /></title>
</head>



<body>
<html:errors />
<html:form styleId='saveQueryForm'
	action='<%=Constants.EXECUTE_QUERY_ACTION%>'>
	<table summary="" cellpadding="0" cellspacing="0" border="0"
		class="contentPage" width="850">
		<tr>
		  <td height="20">&nbsp;
		  </td>
		</tr>
		<tr>
			<td>
			<table summary="" cellpadding="3" cellspacing="0" border="0"  
				width="100%">
				
				<tr>
					<td  width="2%" valign="top" class="formSaveQueryTitle">
					  <b><bean:message	key="query.title" /></b>
					 </td>
					 <td class="formSaveQueryTitle" align="left">
					 : <%=query.getName()%>
					 </td>
				</tr>
				<tr>
					<td  valign="top" width="2%" class="formSaveQueryTitle">
						<b><bean:message	key="query.description" /></b>
					
					 </td>
					 <td  class="formSaveQueryTitle" align="left">
					: <%=query.getDescription()%>
					 </td>
				</tr>
				
				<tr>
					<td colspan="4" height="20">&nbsp;</td>
				</tr>
			</table>
			</td>
		</tr>

		<tr>
			<td>
			<table summary="" cellpadding="3" cellspacing="0" border="0"
				width="100%">
				<tr>
					<td class="formTitle"><bean:message
						key="savequery.queryConditionTitle" /></td>
				</tr>
			</table>
			</td>
		</tr>
		<tr>
			<td>
			<div
				style="width:100%;  max-height:300px; min-height:50px; overflow-y:auto;">
			<%=request.getAttribute(Constants.HTML_CONTENTS)%> <html:hidden
				property="queryString" /></div>


			</td>
		</tr>
		<tr>
			<td><!--  	<table summary="" cellpadding="0" cellspacing="0" border="0" width="100%" height="4%">
							<tr>
								<td class="formTitle">
									<bean:message key="savequery.definedConditionTitle" />
								</td>
							</tr>
						</table>
					--></td>
		</tr>
		<tr>
			<td height="20">&nbsp;</td>
		</tr>
		<tr>
			<td align="right"><input type="hidden" name="queryString"
				value="" /> <input type="button" name="execute" value="Execute" class="actionButton" 
				onClick="ExecuteSavedQuery()" /> <input type="button" name="cancel" class="actionButton" 
				value="Cancel" onClick="GotoRetriveAction();" /></td>
		</tr>
	</table>
</html:form>
</body>
</html>

