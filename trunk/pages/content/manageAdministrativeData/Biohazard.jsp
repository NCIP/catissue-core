<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>

<head>
<!-- Mandar : 434 : for tooltip -->
<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
	<script language="javascript">
		
	</script>
</head>
	
<%
        String operation = (String) request.getAttribute(Constants.OPERATION);
        String formName;

        boolean readOnlyValue;
        if (operation.equals(Constants.EDIT))
        {
            formName = Constants.BIOHAZARD_EDIT_ACTION;
            readOnlyValue = true;
        }
        else
        {
            formName = Constants.BIOHAZARD_ADD_ACTION;
            readOnlyValue = false;
        }
%>	
			
<html:errors/>
<html:messages id="messageKey" message="true" header="messages.header" footer="messages.footer">
	<%=messageKey%>
</html:messages>
    
<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="600">

<html:form action="<%=formName%>">
<!-- NEW Biohazard REGISTRATION BEGINS-->
	<tr>
	<td>
	
	<table summary="" cellpadding="3" cellspacing="0" border="0">
		<tr>
			<td><html:hidden property="operation" value="<%=operation%>"/></td>
		</tr>

		<tr>
			<td><html:hidden property="id" /></td>
		</tr>
		
		<logic:notEqual name="operation" value="<%=Constants.SEARCH%>">
		<tr>
			 <td class="formMessage" colspan="3">* indicates a required field</td>
		</tr>

		<tr>
			 <td class="formTitle" height="20" colspan="3">
				<logic:equal name="operation" value="<%=Constants.ADD%>">
					<bean:message key="biohazard.title"/>
				</logic:equal>
				<logic:equal name="operation" value="<%=Constants.EDIT%>">
					<bean:message key="biohazard.editTitle"/>
				</logic:equal>
			 </td>
		</tr>

		<!-- Name of the storageType -->
		
		<tr>
			<td class="formRequiredNotice" width="5">*</td>
			<td class="formRequiredLabel">
				<label for="name">
					<bean:message key="biohazard.name"/> 
				</label>
			</td>
			<td class="formField">
				<html:text styleClass="formFieldSized15"  maxlength="255"  size="30" styleId="name" property="name"/>
			</td>
		</tr>
		
		<tr>
			<td class="formRequiredNotice" width="5">*</td>
			<td class="formRequiredLabel">
				<label for="type">
					<bean:message key="biohazard.type"/>
				</label>
			</td>
		
			<td class="formField">
<!-- Mandar : 434 : for tooltip -->
				<html:select property="type" styleClass="formFieldSized15" styleId="type" size="1" onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
					<%--html:options name="biohazardTypeList" labelName="biohazardTypeList" /--%>
					<html:options collection="<%=Constants.BIOHAZARD_TYPE_LIST%>" labelProperty="name" property="value"/>
				</html:select>
			</td>
		</tr>

		<tr>
			<td class="formRequiredNotice" width="5">&nbsp;</td>
			<td class="formLabel">
				<label for="comments">
					<bean:message key="biohazard.comments"/>
				</label>
			</td>
			<td class="formField">
				<html:textarea styleClass="formFieldSized" property="comments" styleId="comments" cols="32" rows="5"/>
			</td>
		</tr>
		
		<tr>
		  <td align="right" colspan="3">
			<!-- action buttons begins -->
			<table cellpadding="4" cellspacing="0" border="0">
			<tr>
				<td>
					<html:submit styleClass="actionButton" >
						<bean:message key="buttons.submit"/>
					</html:submit>
				</td>
					<%-- td>
						<html:reset styleClass="actionButton" >
							<bean:message  key="buttons.reset" />
						</html:reset>
					</td --%>
			</tr>
			</table>				
			<!-- action buttons end -->
			</td>
		</tr>

		</logic:notEqual>
		</table>
		
	  </td>
	 </tr>

	 <!-- NEW Institute REGISTRATION ends-->
	 
	 </html:form>
 </table>