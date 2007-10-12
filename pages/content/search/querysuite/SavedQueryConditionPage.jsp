<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>

<%-- Imports --%>
<%@
	page language="java" contentType="text/html"
	import="edu.wustl.catissuecore.util.global.Constants"
%>

<html:html>
	<head>
		<link rel="stylesheet" type="text/css" href="css/styleSheet.css" />
		<script language="JavaScript" type="text/javascript" src="jss/queryModule.js"></script>
		<script language="JavaScript" type="text/javascript" src="jss/script.js"></script>
		<script language="JavaScript" type="text/javascript" src="jss/overlib_mini.js"></script>
		<script language="JavaScript" type="text/javascript" src="jss/calender.js"></script>
		
		<script>
			function closeSaveQueryWindow()
			{
				var parentWindowForm = window.opener.document.forms[0];
				parentWindowForm.action = "RetrieveQueryAction.do";
				parentWindowForm.submit();
				
				window.self.close();
			}
		</script>
		
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>
			<bean:message key="savequery.conditionInformationTitle"/>
		</title>
	</head>

	<body>
		<html:errors/>
		<html:form styleId='saveQueryForm' action='<%=Constants.SAVE_QUERY_ACTION%>'>
		<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="100%">
		<tr>
		  <td height="20">&nbsp;
		  </td>
		 <tr>
		  <td>
			<table summary="" cellpadding="3" cellspacing="0" border="0"  width="100%">
				<tr>
					<td colspan='3'  class="formTitle" height="20">
						<bean:message key="savequery.conditionInformationTitle"/>
					</td>
				</tr>
				<tr>
					<td width='5' class="formFieldNoBordersQuery">*</td>
					<td class="formFieldNoBordersQuery"><b>
								<bean:message key="query.title"/> </b>
					</td>
					<td class="formFieldNoBordersQuery">
						<html:text       styleClass="formFieldSized" maxlength="255"      styleId="title" property="title" />
					</td>
					
				</tr>
				<tr>
					<td width='5' class="formFieldNoBordersQuery">&nbsp;</td>
					<td class="formFieldNoBordersQuery"><bean:message key="query.description"/> </td>
					<td class="formFieldNoBordersQuery">
						<html:textarea styleClass="formFieldSized"   cols="32" rows="5"  property="description"> </html:textarea>
					</td>
				</tr>
				<tr>
                   <td colspan="3" height="20">&nbsp;
				</td>
				</tr>
				<tr>
					<td colspan='3' class="formTitle" height="20">
						<bean:message key="savequery.setConditionParametersTitle" />
					</td>
				</tr>
				</table>
            </td>
          </tr>
		   <tr>
		    <td>
		      <div  style="width:100%; max-height:300px; min-height:50px; overflow-y:auto;">
			    <%=request.getAttribute(Constants.HTML_CONTENTS)%>
			   </div>
			</td> 
		   </tr>
		   <tr>
		    <td colspan="3" height="20"/>
		   </tr>
		   <tr>
		    <td>
		    </td>
		   </tr>
			<tr>
					<td colspan='3'  align="right">
					    <input type="hidden" name="queryString" id="queryString" value=""/>
					    <input type="hidden" name="buildQueryString" id="buildQueryString" value=""/>
						<input type="button" name="preview" value="Preview" class="actionButton"  disabled="true"/>
						<c:choose>
							<c:when test="${querySaved eq 'true'}">
								<input type="button" name="close" value="Close" class="actionButton" onClick="closeSaveQueryWindow()"/>
							</c:when>
							<c:otherwise>
								<input type="button" name="save" value="Save" class="actionButton" onClick="produceSavedQuery()"/>
								<input type="button" name="cancel" value="Cancel" class="actionButton" onClick="window.close();"/>
							</c:otherwise>
						</c:choose>
					</td>
				</tr>
		 </table>
		</html:form>
	</body>
</html:html>
