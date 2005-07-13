<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
	
<%
        String operation = (String) request.getAttribute(Constants.OPERATION);
        String formName = Constants.INSTITUTE_ADD_ACTION;
        String searchFormName = new String(Constants.INSTITUTE_SEARCH_ACTION);

        boolean readOnlyValue;
        if (operation.equals(Constants.EDIT))
        {
            formName = Constants.INSTITUTE_EDIT_ACTION;
            readOnlyValue = true;
        }
        else//Add
        {
            formName = Constants.INSTITUTE_ADD_ACTION;
            readOnlyValue = false;
        }
%>		
			
<html:errors/>
    
<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="600">

<html:form action="<%=Constants.INSTITUTE_ADD_ACTION%>">

	<logic:notEqual name="operation" value="<%=Constants.ADD%>"> 
	<!-- ENTER IDENTIFIER BEGINS-->	
	<br/>	
	<tr>
		<td>
			<table summary="" cellpadding="3" cellspacing="0" border="0">
				<tr>
					<td class="formTitle" height="20" colspan="3">
						<bean:message key="institute.searchTitle"/>
					</td>
				</tr>	
		  
				<tr>
					<td class="formRequiredNotice" width="5">*</td>
					<td class="formRequiredLabel">
						<label for="identifier">
							<bean:message key="institute.identifier"/>
						</label>
					</td>
					<td class="formField">
						<html:text styleClass="formFieldSized" size="30" styleId="identifier" property="identifier"/>
					</td>
				</tr>	
				<%
					String changeAction = "setFormAction('" + searchFormName
							  + "');setOperation('" + Constants.SEARCH + "');";
				%>
				<tr>
					<td align="right" colspan="3">
					<table cellpadding="4" cellspacing="0" border="0">
						<tr>
							<td>
								<html:submit styleClass="actionButton" value="Search" onclick="<%=changeAction%>" />
							</td>
						</tr>
					</table>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<!-- ENTER IDENTIFIER ENDS-->	
	</logic:notEqual> 
	

	<!-- NEW Institute REGISTRATION BEGINS-->
	<tr>
	<td>
	
	<table summary="" cellpadding="3" cellspacing="0" border="0">
		<tr>
			<td><html:hidden property="operation" value="<%=operation%>"/></td>
		</tr>

		<logic:notEqual name="operation" value="<%=Constants.SEARCH%>">
		<tr>
			 <td class="formMessage" colspan="3">* indicates a required field</td>
		</tr>

		<tr>
			 <td class="formTitle" height="20" colspan="3">
				<bean:message key="institute.title"/>
			 </td>
		</tr>

		<!-- Name of the institute -->
		<tr>
			<td class="formRequiredNotice" width="5">*</td>
			<td class="formRequiredLabel">
				<label for="name">
					<bean:message key="institute.name"/>
				</label>
			</td>
			<td class="formField">
				<html:text styleClass="formFieldSized" size="30" styleId="name" property="name"/>
			</td>
		</tr>

		<tr>
			<td class="formRequiredNotice" width="5">*</td>
			<td class="formRequiredLabel">
				<label for="type">
					<bean:message key="institute.type"/> 
				</label>
			</td>
			<td class="formField">
				<html:text styleClass="formFieldSized" size="30" styleId="type" property="type"/>
			</td>
		</tr>

		<tr>
			<td class="formRequiredNotice" width="5">*</td>
			<td class="formRequiredLabel">
				<label for="street">
					<bean:message key="institute.street"/>
				</label>
			</td>
			<td class="formField">
				<html:text styleClass="formFieldSized" size="30" styleId="street" property="street"/>
			</td>
		</tr>


		<tr>
			<td class="formRequiredNotice" width="5">*</td>
			<td class="formRequiredLabel">
				<label for="city">
					<bean:message key="institute.city"/>
				</label>
			</td>
			<td class="formField">
				<html:text styleClass="formFieldSized" size="30" styleId="city" property="city"/>
			</td>
		</tr>

		<tr>
			<td class="formRequiredNotice" width="5">*</td>
			<td class="formRequiredLabel">
				<label for="state">
					<bean:message key="institute.state"/>
				</label>
			</td>
			<td class="formField">
				<html:select property="state" styleClass="formFieldSized" 	styleId="state" size="1">
					<html:options name="stateList" labelName="stateList"/>		
				</html:select>
			</td>
		</tr>

		<tr>
			<td class="formRequiredNotice" width="5">*</td>
			<td class="formRequiredLabel">
				<label for="country">
					<bean:message key="institute.country"/>
				</label>
			</td>
			<td class="formField">
				<html:select property="country" styleClass="formFieldSized" styleId="country" size="1">
					<html:options name="countryList" labelName="countryList"/>
				</html:select>
			</td>
		</tr>

		<tr>
			<td class="formRequiredNotice" width="5">*</td>
			<td class="formRequiredLabel">
				<label for="zip">
					<bean:message key="institute.zip"/>
				</label>
			</td>
			<td class="formField">
				<html:text styleClass="formFieldSized" size="30" styleId="zip" property="zip"/>
			</td>
		</tr>


		<tr>
			<td class="formRequiredNotice" width="5">&nbsp;</td>
			<td class="formLabel">
				<label for="phone">
					<bean:message key="institute.phone"/>
				</label>
			</td>
			<td class="formField">
				<html:text styleClass="formFieldSized" size="30" styleId="phone" property="phone"/>
			</td>
		</tr>

		<tr>
			<td class="formRequiredNotice" width="5">&nbsp;</td>
			<td class="formLabel">
				<label for="fax">
					<bean:message key="institute.fax"/>
				</label>
			</td>
			<td class="formField">
				<html:text styleClass="formFieldSized" size="30" styleId="fax" property="fax"/>
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
						<html:submit styleClass="actionButton" value="Submit" onclick="<%=changeAction%>" />
					</td>
					<td><html:reset styleClass="actionButton"/></td> 
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