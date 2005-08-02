<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="edu.wustl.catissuecore.domain.StorageType"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.actionForm.StorageContainerForm"%>


<head>
	<script language="JavaScript">
		
		var win = null;
		function NewWindow(mypage,myname,w,h,scroll)
		{
			LeftPosition = (screen.width) ? (screen.width-w)/2 : 0;
			TopPosition = (screen.height) ? (screen.height-h)/2 : 0;
			settings =
				'height='+h+',width='+w+',top='+TopPosition+',left='+LeftPosition+',scrollbars='+scroll+',resizable'
			win = open(mypage,myname,settings)
			if (win.opener == null)
				win.opener = self;
		}

		function onRadioButtonClick(element)
		{
			if(element.value == 1)
			{
				document.forms[0].siteId.disabled = false;
				document.forms[0].positionInParentContainer.disabled = true;
				document.forms[0].Map.disabled = true;
			}
			else
			{
				document.forms[0].positionInParentContainer.disabled = false;
				document.forms[0].Map.disabled = false;

				document.forms[0].siteId.disabled = true;
				//window.location.reload();
			}
		}
		
		function onTypeChange(element)
		{
			var action = "/catissuecore/StorageContainer.do?operation=add&typeSelected=" + element.value;
			document.forms[0].action = action;
			document.forms[0].submit();
		}
		
		function onSiteChange(element)
		{
			var list = document.getElementById('typeId');
			var type = list.options[list.selectedIndex].value;
			var action = "/catissuecore/StorageContainer.do?operation=add&typeSelected=" + type;
			document.forms[0].action = action;
			document.forms[0].submit();
		}
		
		function onContainerChange(element)
		{
		}
		
//  function to insert a row in the inner block
function insRow(subdivtag)
{
	var r = new Array(); 
	r = document.getElementById(subdivtag).rows;
	var q = r.length;
	var x=document.getElementById(subdivtag).insertRow(q);
	
	// First Cell
	var spreqno=x.insertCell(0);
	spreqno.className="formSerialNumberField";
	sname=(q+1);
	spreqno.innerHTML="" + sname;

	//Second Cell
	var spreqtype=x.insertCell(1);
	spreqtype.className="formField";
	spreqtype.colSpan=2;
	sname="";
	
	var name = "value(StorageContainerDetails:" + (q+1) +"_parameterName)";
	sname="<input type='text' name='" + name + "' class='formFieldSized10' id='" + name + "'>"        


	spreqtype.innerHTML="" + sname;

	//Third Cell
	var spreqsubtype=x.insertCell(2);
	spreqsubtype.className="formField";
	sname="";

	name = "value(StorageContainerDetails:" + (q+1) +"_parameterValue)";
	sname= "";

	var name = "value(StorageContainerDetails:" + (q+1) +"_parameterValue)";

	sname="<input type='text' name='" + name + "' class='formFieldSized10' id='" + name + "'>"        

	spreqsubtype.innerHTML="" + sname;
}
	</script>
</head>

<%
        String operation = (String) request.getAttribute(Constants.OPERATION);
        String formName;
        String searchFormName = new String(Constants.STORAGE_CONTAINER_SEARCH_ACTION);

        boolean readOnlyValue;
        if (operation.equals(Constants.EDIT))
        {
            formName = Constants.STORAGE_CONTAINER_EDIT_ACTION;
            readOnlyValue = true;
        }
        else
        {
            formName = Constants.STORAGE_CONTAINER_ADD_ACTION;
            readOnlyValue = false;
        }

		String number = (String)request.getAttribute("startNumber");
%>

<html:errors />


<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="600">
	
	<html:form action="<%=Constants.STORAGE_CONTAINER_ADD_ACTION%>">
		<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.ADD%>">
			<!-- ENTER IDENTIFIER BEGINS-->
			<br />
			<tr>
				<td>
				<table summary="" cellpadding="3" cellspacing="0" border="0">
					<tr>
						<td class="formTitle" height="20" colspan="3">
							<bean:message key="storageContainer.searchTitle" />
						</td>
					</tr>

					<tr>
						<td class="formRequiredNotice" width="5">*</td>
						<td class="formRequiredLabel">
							<label for="identifier">
								<bean:message key="storageContainer.systemIdentifier" />
							</label>
						</td>
						<td class="formField">
							<html:text styleClass="formFieldSized" size="30" styleId="systemIdentifier" property="systemIdentifier" />
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


		<!-- NEW STORAGE CONTAINER REGISTRATION BEGINS-->
		<tr>
		<td>
			<table summary="" cellpadding="3" cellspacing="0" border="0" width="500">
				<tr>
					<td><html:hidden property="operation" value="<%=operation%>" /></td>
				</tr>
				<tr>
					<td><html:hidden property="positionDimensionOne" /></td>
				</tr>
				<tr>
					<td><html:hidden property="positionDimensionTwo" /></td>
				</tr>
				<tr>
					<td><html:hidden property="parentContainerId" /></td>
				</tr>
				<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.SEARCH%>">
					<tr>
						<td class="formMessage" colspan="4">* indicates a required field</td>
					</tr>
					
					<tr>
						<td class="formTitle" height="20" colspan="4">
							<bean:message key="storageContainer.title" />
						</td>
					</tr>
					
					<tr>
						<td class="formRequiredNotice" width="5">*</td>
						<td class="formRequiredLabel" colspan="2">
							<label for="type">
								<bean:message key="storageContainer.type" />
							</label>
						</td>
						<td class="formField">
							<html:select property="typeId" styleClass="formFieldSized" styleId="typeId" size="1" onchange="onTypeChange(this)">
								<html:options name="storageTypeIdList" labelName="storageTypeList" />
							</html:select>
							&nbsp;
							<html:link page="/StorageType.do?operation=add">
		 						<bean:message key="buttons.addNew" />
	 						</html:link>
						</td>
					</tr>

					<tr>
						<td class="formRequiredNotice" width="5">&nbsp;</td>
						<td class="formRequiredRadio">
							<html:radio styleClass="" styleId="checkedButton" property="checkedButton" value="1" onclick="onRadioButtonClick(this)">
								<label for="siteId">
									<%--<bean:message key="storageContainer.site" />--%>
								</label>
							</html:radio>
							<br>
							<html:radio styleClass="" styleId="checkedButton" property="checkedButton" value="2" onclick="onRadioButtonClick(this)">
								<label for="site">
									<%--<bean:message key="storageContainer.parentContainer" />--%>
								</label>
							</html:radio>							
						</td>
						<td class="formLabel">
							<label for="siteId">
								<bean:message key="storageContainer.site" />
							</label>
							<br>
							<label for="site">
								<bean:message key="storageContainer.parentContainer" />
							</label>
						</td>
						<td class="formField">
							<logic:equal name="storageContainerForm" property="checkedButton" value="1">
							<html:select property="siteId" styleClass="formFieldSized" styleId="siteId" size="1" onchange="onSiteChange(this)">
								<html:options name="siteIdList" labelName="siteList" />
							</html:select>
							</logic:equal>
							<logic:equal name="storageContainerForm" property="checkedButton" value="2">
							<html:select property="siteId" styleClass="formFieldSized" styleId="siteId" size="1" onchange="onSiteChange(this)" disabled="true">
								<html:options name="siteIdList" labelName="siteList" />
							</html:select>
							</logic:equal>
							&nbsp;
							<html:link page="/Site.do?operation=add" styleId="newSite">
		 						<bean:message key="buttons.addNew" />
	 						</html:link>
	 						
	 						<%-- LOGIC TAG TO ENABLE/DISABLE THE FIELDS OF PARENT CONTAINER --%>
	 						<logic:equal name="storageContainerForm" property="checkedButton" value="1">							
							<html:text styleClass="formFieldSized" size="30" styleId="positionInParentContainer" property="positionInParentContainer" readonly="true" disabled="true"/>
							&nbsp;
							<html:button property="mapButton" styleClass="actionButton" styleId="Map" 
								onclick="NewWindow('ShowFramedPage.do?pageOf=pageOfStorageLocation','name','810','320','yes');return false" disabled="true">
								<bean:message key="buttons.map"/>
							</html:button>
							
							</logic:equal>
							
							<logic:equal name="storageContainerForm" property="checkedButton" value="2">
							
							<html:text styleClass="formFieldSized" size="30" styleId="positionInParentContainer" property="positionInParentContainer" readonly="true"/>
							&nbsp;
							<html:button property="mapButton" styleClass="actionButton" styleId="Map" 
								onclick="NewWindow('ShowFramedPage.do?pageOf=pageOfStorageLocation','name','810','320','yes');return false" >
								<bean:message key="buttons.map"/>
							</html:button>
							
							</logic:equal>
						</td>
					</tr>
					<tr>
						<td class="formRequiredNotice" width="5">&nbsp;</td>
						<td class="formLabel" colspan="2">
							<label for="noOfContainers">
								<bean:message key="storageContainer.noOfContainers" />
							</label>
						</td>
						<td class="formField">
							<html:text styleClass="formFieldSized10" size="30" styleId="noOfContainers" property="noOfContainers" readonly="<%=readOnlyValue%>" onchange="onContainerChange(this)"/>
						</td>
					</tr>
					
					<tr>
						<td class="formRequiredNotice" width="5">&nbsp;</td>
						<td class="formLabel" colspan="2">
							<label for="startNumber">
								<bean:message key="storageContainer.startNumber" />
							</label>
						</td>
						<td class="formField">
							<html:text styleClass="formFieldSized10" size="30" styleId="startNumber" property="startNumber" value="<%=number%>" readonly="TRUE"/>
						</td>
					</tr>
					
					<tr>
						<td class="formRequiredNotice" width="5">&nbsp;</td>
						<td class="formLabel" colspan="2">
							<label for="startNumber">
								<bean:message key="storageContainer.barcode" />
							</label>
						</td>
						<td class="formField">
							<html:text styleClass="formFieldSized10" size="30" styleId="barcode" property="barcode" readonly="<%=readOnlyValue%>" />
						</td>
					</tr>

					<tr>
						<td class="formRequiredNotice" width="5">&nbsp;</td>
						<td class="formLabel" colspan="2">
							<label for="defaultTemperature">
								<bean:message key="storageContainer.temperature" />
							</label>
						</td>
						<td class="formField">
							<html:text styleClass="formFieldSized10" size="30" styleId="defaultTemperature" property="defaultTemperature" readonly="<%=readOnlyValue%>" />
							°C
						</td>
					</tr>
					
					<tr>
						<td class="formTitle" colspan="4">
							<label for="capacity">
								<bean:message key="storageContainer.capacity" />
							</label>
						</td>
					</tr>
					
					<tr>
						<td class="formRequiredNotice" width="5">&nbsp;</td>
						<td class="formLabel" colspan="2">
							<label for="oneDimensionLabel">
								<%--<bean:message key="storageContainer.oneDimensionLabel" />--%>
								<bean:write name="storageContainerForm" property="oneDimensionLabel"/>
							</label>
						</td>
						<td class="formField">
							<html:text styleClass="formFieldSized10" size="30" styleId="oneDimensionCapacity" property="oneDimensionCapacity" readonly="<%=readOnlyValue%>" />
						</td>
					</tr>
					
					<tr>
						<td class="formRequiredNotice" width="5">&nbsp;</td>
						<td class="formLabel" colspan="2">
							<label for="twoDimensionLabel">
								<%--<bean:message key="storageContainer.twoDimensionLabel" />--%>								
								<bean:write name="storageContainerForm" property="twoDimensionLabel"/>
							</label>
						</td>
						<td class="formField">
							<html:text styleClass="formFieldSized10" size="30" styleId="twoDimensionCapacity" property="twoDimensionCapacity" readonly="<%=readOnlyValue%>" />
						</td>
					</tr>
					
					<tr>
						<td class="formTitle" colspan="3" nowrap>
							<label for="details">
								<bean:message key="storageContainer.details" />
							</label>
						</td>
						<td class="formTitle" align="Right">
						<html:button property="addKeyValue" styleClass="actionButton" onclick="insRow('addMore')">
						<bean:message key="buttons.addMore"/>
						</html:button>
						</td>
					</tr>
									
					<tr>
						<td class="formLeftSubTableTitle" width="5">#</td>
						<td class="formRightSubTableTitle" colspan="2">
							<label for="key">
								<bean:message key="storageContainer.key" />
							</label>
						</td>
						<td class="formRightSubTableTitle">
							<label for="value">
								<bean:message key="storageContainer.value" />
							</label>
						</td>
					</tr>
					
					<tbody id="addMore">
					<tr>
						<td class="formSerialNumberField" width="5">1.</td>
						<td class="formField" colspan="2">
							<html:text styleClass="formFieldSized10" size="30" styleId="key" property="value(StorageContainerDetails:1_parameterName)" readonly="<%=readOnlyValue%>" />
						</td>
						<td class="formField">
							<html:text styleClass="formFieldSized10" size="30" styleId="typeId" property="value(StorageContainerDetails:1_parameterValue)" value="" readonly="<%=readOnlyValue%>" />
						</td>
					</tr>
					</tbody>
				<%--	</logic:equal> --%>
				</table>
				
				<table summary="" cellpadding="3" cellspacing="0" border="0" width="500">	
					<tr>
						<td align="right">
						<%
        					String changeAction = "setFormAction('" + formName + "');";
				        %> 
						
						<!-- action buttons begins -->
						<table cellpadding="4" cellspacing="0" border="0">
							<tr>
								<td>
						   				<html:submit styleClass="actionButton" onclick="<%=changeAction%>">
						   					<bean:message key="buttons.submit"/>
						   				</html:submit>
						   		</td>
								<td>
										<html:reset styleClass="actionButton">
											<bean:message key="buttons.reset"/>
										</html:reset>
								</td> 
							</tr>
						</table>
						<!-- action buttons end -->
						</td>
					</tr>
					
				</logic:notEqual>
			</table>

			</td>
		</tr>

		<!-- NEW STORAGE CONTAINER REGISTRATION ends-->
	</html:form>
</table>