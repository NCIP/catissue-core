<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.actionForm.StorageTypeForm"%>

<head>
	<script language="javascript">
//Mandar : 18-Apr-06 : bugid: 644 : - Dimension 2 capacity label 
	function capacityChanged(element)
	{
		var elementValue = element.value;
		if(elementValue.length>0)
		{
			try
			{
				var num = parseInt(elementValue);
				col1= document.getElementById("col1");
				col2= document.getElementById("col2");
				if(num>1)
				{
					col1.innerHTML="*";
					col2.className="formRequiredLabel";
				}
				else
				{
					col1.innerHTML="&nbsp;";
					col2.className="formLabel";
				}
			}
			catch(err)
			{
				//alert("Please enter a valid number.");
			}
		}
	}

	</script>
	<script language="JavaScript" src="jss/script.js" type="text/javascript"></script>
</head>
	
<%
        String operation = (String) request.getAttribute(Constants.OPERATION);
        String reqPath = (String)request.getAttribute(Constants.REQ_PATH);  
		String submittedFor=(String)request.getAttribute(Constants.SUBMITTED_FOR);
        String formName;

        boolean readOnlyValue;
        if (operation.equals(Constants.EDIT))
        {
            formName = Constants.STORAGE_TYPE_EDIT_ACTION;
            readOnlyValue = true;
        }
        else
        {
            formName = Constants.STORAGE_TYPE_ADD_ACTION;
            readOnlyValue = false;
        }
		
	//Mandar : 18-Apr-06 : bugid: 644 : - Dimension 2 capacity label
	StorageTypeForm form = null;
	int dimTwoCapacity = 0;
	Object obj = request.getAttribute("storageTypeForm");
	if(obj != null && obj instanceof StorageTypeForm)
	{
		 form = (StorageTypeForm)obj;
	}
	if(form!=null)
	{
		dimTwoCapacity = form.getTwoDimensionCapacity();  
	}
	//Mandar : 18-Apr-06 : bugid: 644 : - Dimension 2 capacity label end	

%>	
			
<html:errors/>
<html:messages id="messageKey" message="true" header="messages.header" footer="messages.footer">
	<%=messageKey%>
</html:messages>
    
<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="600">

<html:form action="<%=formName%>">
<!-- NEW Institute REGISTRATION BEGINS-->
	<tr>
	<td>
	
	<table summary="" cellpadding="3" cellspacing="0" border="0">
		<tr>
			<td>
				<html:hidden property="operation" value="<%=operation%>"/>
				<html:hidden property="submittedFor" value="<%=submittedFor%>"/>
			</td>
		</tr>
		
		<tr>
			<td><html:hidden property="systemIdentifier" />
				<html:hidden property="<%=Constants.REQ_PATH%>" value="<%=reqPath%>" />
			</td>
		</tr>

		<tr>
			 <td class="formMessage" colspan="3">* indicates a required field</td>
		</tr>

		<tr>
			 <td class="formTitle" height="20" colspan="3">
				<logic:equal name="operation" value="<%=Constants.ADD%>">
					<bean:message key="storageType.title"/>
				</logic:equal>
				<logic:equal name="operation" value="<%=Constants.EDIT%>">
					<bean:message key="storageType.editTitle"/>&nbsp;<bean:message key="for.identifier"/>&nbsp;<bean:write name="storageTypeForm" property="systemIdentifier" />
				</logic:equal>
			 </td>
		</tr>

		<!-- Name of the storageType -->
		
		<tr>
			<td class="formRequiredNotice" width="5">*</td>
			<td class="formRequiredLabel">
				<label for="type">
					<bean:message key="storageType.type"/> 
				</label>
			</td>
			<td class="formField">
				<html:text styleClass="formFieldSized10"  maxlength="50"  size="30" styleId="type" property="type"/>
			</td>
		</tr>

		<tr>
			<td class="formRequiredNotice" width="5">&nbsp;</td>
			<td class="formLabel">
				<label for="defaultTemperature">
					<bean:message key="storageType.defaultTemperature"/>
				</label>
			</td>
			<td class="formField">
				<html:text styleClass="formFieldSized10"  maxlength="10"  size="30" styleId="defaultTemperature" property="defaultTemperature"/>
				°C
			</td>
		</tr>


		<tr>			
			<td class="formTitle" colspan="3">
				<label for="defaultCapacity">
					<bean:message key="storageType.defaultCapacity"/>
				</label>
			</td>
		</tr>

		<tr>
			<td class="formRequiredNotice" width="5">*</td>
			<td class="formRequiredLabel">
				<label for="oneDimensionCapacity">
					<bean:message key="storageType.oneDimensionCapacity"/>
				</label>
			</td>
			<td class="formField">
					<html:text styleClass="formFieldSized10"  maxlength="10"  size="10" styleId="oneDimensionCapacity" property="oneDimensionCapacity"/>
			</td>
		</tr>

		<tr>
			<td class="formRequiredNotice" width="5">*</td>
			<td class="formRequiredLabel">
				<label for="oneDimensionLabel">
					<bean:message key="storageType.oneDimensionLabel"/>
				</label>
			</td>
			<td class="formField">
				<html:text styleClass="formFieldSized10"  maxlength="50"  size="30" styleId="oneDimensionLabel" property="oneDimensionLabel"/>
			</td>
		</tr>

		<tr>
			<td class="formRequiredNotice" width="5">*</td>
			<td class="formRequiredLabel">
				<label for="twoDimensionCapacity">
					<bean:message key="storageType.twoDimensionCapacity"/>
				</label>
			</td>
			<td class="formField">
				<html:text styleClass="formFieldSized10"  maxlength="10"  size="10" styleId="twoDimensionCapacity" property="twoDimensionCapacity" onkeyup="capacityChanged(this)" />
			</td>
		</tr>
<!--  Mandar : 18-Apr-06 : bugid: 644 : - Dimension 2 capacity label -->
<% 
	String tdClassName ="formLabel";
	String strStar = "&nbsp;";
	if(dimTwoCapacity > 1)
	{
		tdClassName="formRequiredLabel";
		strStar = "*";
	}
 %>
		<tr>
			<td class="formRequiredNotice" width="5" id="col1"><%=strStar%></td>
			<td class="<%=tdClassName%>" id="col2">
<!--  Mandar : 18-Apr-06 : bugid: 644 : - Dimension 2 capacity label end -->
				<label for="twoDimensionLabel">
					<bean:message key="storageType.twoDimensionLabel"/>
				</label>
			</td>
			<td class="formField">
				<html:text styleClass="formFieldSized10"  maxlength="50" size="30" styleId="twoDimensionLabel" property="twoDimensionLabel"/>
			</td>
		</tr>

		<tr>
		  <td align="right" colspan="3">
			<!-- action buttons begins -->
			<%
        		String changeAction = "setFormAction('" + formName + "');";
			%> 
			<table cellpadding="4" cellspacing="0" border="0">
				<tr>
					<td>
						<html:submit styleClass="actionButton">
							<bean:message  key="buttons.submit" />
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

		</table>
		
	  </td>
	 </tr>

	 <!-- NEW Institute REGISTRATION ends-->
	 
	 </html:form>
 </table>