<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<!-- Mandar : 434 : for tooltip -->
<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
<%
        String operation = (String) request.getAttribute(Constants.OPERATION);
        String formName, specimenId=null , fromPositionData =null;
        String posOne = null, posTwo = null, storContId = null;

		specimenId = (String) request.getAttribute(Constants.SPECIMEN_ID);
		fromPositionData = (String) request.getAttribute(Constants.FROM_POSITION_DATA);
		posOne = (String) request.getAttribute(Constants.POS_ONE);
		posTwo = (String) request.getAttribute(Constants.POS_TWO);
		storContId = (String) request.getAttribute(Constants.STORAGE_CONTAINER_ID);
		System.out.println("\n\n\n\n\n***********\n JSP FromPos: " + fromPositionData + "\n ************ \n\n\n\n\n");
	
        boolean readOnlyValue;
        if (operation.equals(Constants.EDIT))
        {
            formName = Constants.TRANSFER_EVENT_PARAMETERS_EDIT_ACTION;
            readOnlyValue = true;
        }
        else
        {
            formName = Constants.TRANSFER_EVENT_PARAMETERS_ADD_ACTION;
			
            readOnlyValue = false;
        }
		
%>	
			
<html:errors/>
    
<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="600">

<html:form action="<%=Constants.TRANSFER_EVENT_PARAMETERS_ADD_ACTION%>">


	<!-- NEW TRANSFER_EVENT_PARAMETERS REGISTRATION BEGINS-->
	<tr>
	<td>
	
	<table summary="" cellpadding="3" cellspacing="0" border="0">
		<tr>
			<td><html:hidden property="operation" value="<%=operation%>"/></td>
		</tr>
		
		<tr>
			<td><html:hidden property="systemIdentifier" /></td>
		</tr>
		
		<tr>
			 <td class="formMessage" colspan="3">* indicates a required field</td>
		</tr>
		
		<tr>
			<td>
				<html:hidden property="specimenId" value="<%=specimenId%>"/>
			</td>
		</tr>

		<tr>
			<td class="formTitle" height="20" colspan="3">
				<logic:equal name="operation" value="<%=Constants.ADD%>">
					<bean:message key="transfereventparameters.title"/>
				</logic:equal>
				<logic:equal name="operation" value="<%=Constants.EDIT%>">
					<bean:message key="transfereventparameters.edittitle"/>
				</logic:equal>
			</td>
		</tr>
		
		<!-- Name of the transfereventparameters -->
<!-- User -->		
		<tr>
			<td class="formRequiredNotice" width="5">*</td>
			<td class="formRequiredLabel">
				<label for="type">
					<bean:message key="eventparameters.user"/> 
				</label>
			</td>
			<td class="formField">
<!-- Mandar : 434 : for tooltip -->
				<html:select property="userId" styleClass="formFieldSized" styleId="userId" size="1"
				 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
					<html:options collection="<%=Constants.USERLIST%>" labelProperty="name" property="value"/>
				</html:select>
			</td>
		</tr>

<!-- date -->		
		<tr>
			<td class="formRequiredNotice" width="5">*</td>
			<td class="formRequiredLabel">
				<label for="type">
					<bean:message key="eventparameters.dateofevent"/> 
				</label>
			</td>
			<td class="formField">
				 <div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>
					<html:text styleClass="formDateSized15" maxlength="10"  size="15" styleId="dateOfEvent" property="dateOfEvent" />
					&nbsp;<bean:message key="page.dateFormat" />&nbsp;
						<a href="javascript:show_calendar('transferEventParametersForm.dateOfEvent',null,null,'MM-DD-YYYY');">
							<img src="images\calendar.gif" width=24 height=22 border=0>
						</a>
			</td>
		</tr>

<!-- hours & minutes -->		
		<tr>
			<td class="formRequiredNotice" width="5">*</td>
			<td class="formRequiredLabel">
				<label for="eventparameters.time">
					<bean:message key="eventparameters.time"/>
				</label>
			</td>
			<td class="formField">
<!-- Mandar : 434 : for tooltip -->
				<html:select property="timeInHours" styleClass="formFieldSized5" styleId="timeInHours" size="1"
				 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
					<html:options name="<%=Constants.HOUR_LIST%>" labelName="<%=Constants.HOUR_LIST%>" />
				</html:select>&nbsp;
				<label for="eventparameters.timeinhours">
					<bean:message key="eventparameters.timeinhours"/>&nbsp; 
				</label>
<!-- Mandar : 434 : for tooltip -->
				<html:select property="timeInMinutes" styleClass="formFieldSized5" styleId="timeInMinutes" size="1"
				 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
					<html:options name="<%=Constants.MINUTES_LIST%>" labelName="<%=Constants.MINUTES_LIST%>" />
				</html:select>
				<label for="eventparameters.timeinhours">
					&nbsp;<bean:message key="eventparameters.timeinminutes"/> 
				</label>
			</td>
		</tr>



<!-- fromPosition -->		
		<tr>
			<td class="formRequiredNotice" width="5">*</td>
			<td class="formRequiredLabel">
				<label for="type">
					<bean:message key="transfereventparameters.fromposition"/> 
				</label>
			</td>
			<td class="formField">
				<html:hidden property="fromPositionDimensionOne" value="<%=posOne%>" />
				<html:hidden property="fromPositionDimensionTwo" value="<%=posTwo%>" />
				<html:hidden property="fromStorageContainerId" value="<%=storContId%>" />
				<%
					if(fromPositionData == null)
					{
				%>
				<html:text styleClass="formDateSized" size="35" styleId="fromPosition" property="fromPosition" readonly="true" />
				<%
					}
					else
					{
				%>
				<html:text styleClass="formDateSized" size="35" styleId="fromPosition" property="fromPosition" value="<%=fromPositionData%>" readonly="true" />
				<%
					}
				%>
			</td>
		</tr>

<!-- toPosition -->		
		<tr>
			<td class="formRequiredNotice" width="5">*</td>
			<td class="formRequiredLabel">
				<label for="type">
					<bean:message key="transfereventparameters.toposition"/> 
				</label>
			</td>
			<td class="formField">
			<%
				boolean isReadOnly = true ;
					if(operation.equals("add"))
					{
						isReadOnly = false ;
					}
					
			
			%>
				&nbsp;<bean:message key="storageContainer.parentID" />
     			<html:text styleClass="formFieldSized3" maxlength="10"  styleId="storageContainer" property="storageContainer" readonly="<%=isReadOnly%>" />
     			&nbsp;<bean:message key="storageContainer.positionOne" />
     			<html:text styleClass="formFieldSized3" maxlength="10"  styleId="positionDimensionOne" property="positionDimensionOne" readonly="<%=isReadOnly%>" />
     			&nbsp;<bean:message key="storageContainer.positionTwo" />
     			<html:text styleClass="formFieldSized3" maxlength="10"  styleId="positionDimensionTwo" property="positionDimensionTwo" readonly="<%=isReadOnly%>" />

				<html:hidden property="positionInStorageContainer" />
				<%
					if(operation.equals("add"))
					{
				%>
					<html:button property="mapButton" styleClass="actionButton" styleId="Map"
								onclick="javascript:NewWindow('ShowFramedPage.do?pageOf=pageOfSpecimen','name','810','320','yes');return false" >
						<bean:message key="buttons.map"/>
					</html:button>
				<%
					}
				%>				
				&nbsp;
			</td>
		</tr>




<!-- comments -->		
		<tr>
			<td class="formRequiredNotice" width="5">&nbsp;</td>
			<td class="formLabel">
				<label for="type">
					<bean:message key="eventparameters.comments"/> 
				</label>
			</td>
			<td class="formField">
				<html:textarea styleClass="formFieldSized"  styleId="comments" property="comments" />
			</td>
		</tr>

<!-- buttons -->
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

		</table>
		
	  </td>
	 </tr>

	 <!-- NEW TRANSFER_EVENT_PARAMETERS ends-->
	 
	 </html:form>
 </table>