<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/nlevelcombo.tld" prefix="ncombo" %>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<script src="jss/script.js"></script>
<link rel="stylesheet" type="text/css" href="css/styleSheet.css" />
<html>
<head>
<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
<%
String formAction = "SubmitSpecimenCollectionProtocol.do";

  String containerId;
  String selectedContainerName ;
  String positionDimensionOne;
  String positionDimensionTwo;
  String functionCall;

if(request.getAttribute(Constants.PAGEOF) != null)
{
	formAction = formAction + "?pageOf="+request.getAttribute(Constants.PAGEOF);
}

%>
	<script language="JavaScript">
		window.parent.frames['SpecimenEvents'].location="ShowCollectionProtocol.do?pageOf=specimenEventsPage&operation=ViewSummary";
		function saveCollectionProtocol()
		{
				var action ="SubmitSpecimenCollectionProtocol.do?action=collectionprotocol";
				document.forms[0].action = action;
				document.forms[0].submit();
		}

		function showMap(selectedContainerName,positionDimensionOne,positionDimensionTwo,containerId)
		{
			frameUrl="ShowFramedPage.do?pageOf=pageOfSpecimen&"+
				"selectedContainerName=" + selectedContainerName +
				"&pos1=" + positionDimensionOne + 
				"&pos2=" + positionDimensionTwo +
				"&containerId=" +containerId +
			"&<%=Constants.CAN_HOLD_SPECIMEN_CLASS %>=Tissue";
			
			var storageContainer = document.getElementById(selectedContainerName).value;
			frameUrl+="&storageContainerName="+storageContainer;

			openPopupWindow(frameUrl,'newSpecimenPage');
			//mapButtonClickedOnSpecimen(frameUrl,'newSpecimenPage');
		}
		ChangeCheckBoxStatus(type,chkInstance)
		{
		var chkCount= document.getElementsById(type).length;
		for (var i=0;i<chkCount;i++)
		{
			var elements = document.getElementsById(type);
			elements[i].checked = chkInstance.checked;
		}

		}
	</script>
</head>

		<html:errors />
		<html:messages id="messageKey" message="true" header="messages.header" footer="messages.footer">
			<%=messageKey%>
		</html:messages>
		
		<html:form action="<%=formAction%>">		
		<table>
		<tr>
				<td> &nbsp; </td>
		<td>
		<table summary="" cellpadding="0" cellspacing="0" border="0">
				<tr>

					<td class="dataTablePrimaryLabel" height="20">
						<bean:write name="viewSpecimenSummaryForm" property="title" />
					</td>				
				<logic:equal name="viewSpecimenSummaryForm" property="requestType" value="Multiple Specimen">
					<script language="javascript">
							refreshTree('<%=Constants.CP_AND_PARTICIPANT_VIEW%>','<%=Constants.CP_TREE_VIEW%>','<%=Constants.CP_SEARCH_CP_ID%>','<%=Constants.CP_SEARCH_PARTICIPANT_ID%>','1');	
					</script>
				</logic:equal>
				</tr>
		<logic:empty name="viewSpecimenSummaryForm" property="specimenList" >
			<tr>
				<td class="dataTableWhiteCenterHeader" colspan="6">  
									No specimens to display for current action!!
								</td>
			</tr>		
		</logic:empty>
		</table>
		<logic:notEmpty name="viewSpecimenSummaryForm" property="specimenList" >	
				<table summary="" cellpadding="3"
								cellspacing="0" border="0" class="dataTable" >
					<tr>
						<th class="formSerialNumberLabelForTable" scope="col" > &nbsp </th>
						<th class="formSerialNumberLabelForTable" scope="col">Received</th>
						<th class="formSerialNumberLabelForTable" scope="col">Label</th>
						<th class="formSerialNumberLabelForTable" scope="col"> Barcode</th>
						<th class="formSerialNumberLabelForTable" scope="col"> Location</th>
						<th class="formSerialNumberLabelForTable" scope="col"> Qty</th>
						
					</tr>
				
				  <logic:iterate name="viewSpecimenSummaryForm" property="specimenList" id="specimen" indexId="counter">
					<tr>
						<html:hidden indexed="true" name="specimen" property="containerId" />
						<td class="dataCellText"> <html:radio property="selectedSpecimenId" value="uniqueIdentifier" idName="specimen" 
						onclick=" form.action='GenericSpecimenSummary.do'; submit()"/> 
						<html:hidden name="specimen" indexed="true" property="uniqueIdentifier" /></td>
					<!--Editable Row -->	
						<logic:equal name="specimen" property="readOnly" value="false">
							<td class="dataCellText">
								<html:checkbox name="specimen" indexed="true" property="checkedSpecimen" />					
							</td>
								<html:hidden name="specimen" indexed="true" property="readOnly"/>
							<td class="dataCellText" > 
							<html:text  styleClass="formFieldSized10" name="specimen" indexed="true" property="displayName" />

							</td>
							<td class="dataCellText"> 
							<html:text   styleClass="formFieldSized10" name="specimen" indexed="true" property="barCode" />
							<bean:write  name="specimen" property="barCode" />
							</td>
							
							<logic:equal name="specimen" property="storageContainerForSpecimen" value="Virtual">
								
							<td class="dataCellText"> <bean:write name="specimen" property="storageContainerForSpecimen" />
									<html:hidden name="specimen" indexed="true" property="storageContainerForSpecimen" /></td>
							</logic:equal>
								
							<logic:notEqual name="specimen" property="storageContainerForSpecimen" value="Virtual">
									<td class="dataCellText">
									
									<bean:define id="specimenId" name="specimen" property="uniqueIdentifier" />
									
									<%
									  containerId = "containerId_"+specimenId;
									  selectedContainerName = "selectedContainerName_"+specimenId;
									  positionDimensionOne = "positionDimensionOne_"+specimenId;
  									  positionDimensionTwo = "positionDimensionTwo_"+specimenId;
									  functionCall="showMap('" + selectedContainerName + "','"+
																	positionDimensionOne +"','"
																	+ positionDimensionTwo +"','"
																	+containerId +"')" ;
									%>

									<html:text styleClass="formFieldSized10"  size="30" styleId="<%=selectedContainerName%>" indexed="true"  name="specimen" property="selectedContainerName" readonly= "true"/>
									<html:text styleClass="formFieldSized3"  size="5" styleId="<%=positionDimensionOne%>" indexed="true"  name="specimen" property="positionDimensionOne" readonly= "true"/>
									<html:text styleClass="formFieldSized3"  size="5" styleId="<%=positionDimensionTwo%>" indexed="true"  name="specimen" property="positionDimensionTwo" readonly= "true"/>
									<html:button styleClass="actionButton" property="containerMap" 
									onclick="<%=functionCall%>" >
									<bean:message key="buttons.map"/>
									</html:button>
									<html:hidden  styleId="<%=containerId%>" name="specimen" property="containerId" />
									</td>
							</logic:notEqual>												
							
							<td class="dataCellText"> 
							<html:text styleClass="formFieldSized5" name="specimen" indexed="true" property="quantity" />
							</td>

						</logic:equal>
					<!--/Editable Row -->

					<!---Readonly Row -->						
						<logic:equal name="specimen" property="readOnly" value="true">
							<td class="dataCellText">
								<html:checkbox name="specimen" indexed="true" property="checkedSpecimen" disabled="true" value="true"/>
								<html:hidden name="specimen" indexed="true" property="checkedSpecimen" />					
							</td>

							<html:hidden name="specimen" indexed="true" property="readOnly"/>
							
							<td class="dataCellText" > <bean:write name="specimen" property="displayName" />
							<html:hidden name="specimen" indexed="true" property="displayName" /></td>
							<td class="dataCellText">&nbsp;<bean:write name="specimen" property="barCode" />
							<html:hidden name="specimen" indexed="true" property="barCode" /></td>
							<logic:equal name="specimen" property="storageContainerForSpecimen" value="Virtual">
								
									<td class="dataCellText"> <bean:write name="specimen" property="storageContainerForSpecimen" />
									<html:hidden name="specimen" indexed="true" property="storageContainerForSpecimen" /></td>
							</logic:equal>
							<logic:notEqual name="specimen" property="storageContainerForSpecimen" value="Virtual">
									<td class="dataCellText">	<html:text styleClass="formFieldSized10"  size="30" styleId="selectedContainerName" indexed="true" name="specimen" property="selectedContainerName" readonly= "true"/>
									<html:text styleClass="formFieldSized3"  indexed="true" size="5" styleId="positionDimensionOne" name="specimen" property="positionDimensionOne" readonly= "true"/>
									<html:text styleClass="formFieldSized3"  size="5" styleId="positionDimensionTwo" indexed="true" name="specimen" property="positionDimensionTwo" readonly= "true"/>
									</td>
							</logic:notEqual>												
							<td class="dataCellText"> <bean:write name="specimen" property="quantity" />
							<html:hidden name="specimen" indexed="true" property="quantity" />
							</td>
						</logic:equal>					
					<!--/Readonly Row -->
					</tr>
				  </logic:iterate>	
				</table>
		</logic:notEmpty>
		
		
			<logic:notEmpty name="viewSpecimenSummaryForm" property="eventId">
				<html:hidden property="eventId"  />
				<html:hidden property="lastSelectedSpecimenId"  />
			</logic:notEmpty>
				<html:hidden property="userAction" />
				<html:hidden property="requestType" />
			<logic:notEmpty name="viewSpecimenSummaryForm" property="aliquotList" >
			&nbsp;
			<table>
			<tr>
			<td class="dataTablePrimaryLabel" height="20">		
				<p>Aliquot details
			</td>
			</tr>
			</table>
			<table summary="" cellpadding="3"
							cellspacing="0" border="0" class="dataTable" >
			<tr>
	      		<th class="formSerialNumberLabelForTable" scope="col" colspan="5"> &nbsp;</th>
	      		<th class="formSerialNumberLabelForTable" scope="col"> 
					<input type="checkbox" name="chkAllDerived" onclick="ChangeCheckBoxStatus('aliquotId',this);"/>			
				</th>
			</tr>

				<tr>	
					<th class="formSerialNumberLabelForTable" scope="col">Parent</th>
					<th class="formSerialNumberLabelForTable" scope="col">Label</th>
					<th class="formSerialNumberLabelForTable" scope="col">Barcode</th>
					<th class="formSerialNumberLabelForTable" scope="col"> Location</th>
					<th class="formSerialNumberLabelForTable" scope="col"> Qty</th>
					<th class="formSerialNumberLabelForTable" scope="col"> Created</th>
					</tr>
				  <logic:iterate name="viewSpecimenSummaryForm" property="aliquotList" id="aliquot" indexId="ctr">
						<html:hidden name="aliquot" indexed="true" property="uniqueIdentifier" />
						<html:hidden indexed="true" name="aliquot" property="containerId" />
					<tr>

					<td class="dataCellText"> <bean:write name="aliquot" property="parentName" />
								<html:hidden indexed="true" name="aliquot" property="parentName" />
					</td>		      	
					<logic:equal name="aliquot" property="readOnly" value="true">
		
					<td class="dataCellText"> <bean:write name="aliquot" property="displayName" />
							 <html:hidden indexed="true" name="aliquot" property="displayName" /></td>
							<td class="dataCellText">&nbsp; <bean:write name="aliquot" property="barCode" />
								 <html:hidden indexed="true" name="aliquot" property="barCode"/></td>
							
							<td class="dataCellText">
								<logic:equal name="aliquot" property="storageContainerForSpecimen" value="Virtual">
								
									 <bean:write name="aliquot" property="storageContainerForSpecimen" />
									<html:hidden name="aliquot" indexed="true" property="storageContainerForSpecimen" />
								</logic:equal>
								
								<logic:notEqual name="aliquot" property="storageContainerForSpecimen" value="Virtual">
									<html:text styleClass="formFieldSized10"  size="30" styleId="selectedContainerName"  indexed="true" name="aliquot" property="selectedContainerName" readonly= "true"/>
									<html:text styleClass="formFieldSized3"  size="5" styleId="positionDimensionOne"  indexed="true" name="aliquot" property="positionDimensionOne" readonly= "true"/>
									<html:text styleClass="formFieldSized3"  size="5" styleId="positionDimensionTwo" indexed="true" name="aliquot" property="positionDimensionTwo" readonly= "true"/>
								</logic:notEqual>										
								</td>
							<td class="dataCellText"> <bean:write name="aliquot" property="quantity" />
								<html:hidden indexed="true" name="aliquot" property="quantity" />
							</td>
							<td class="dataCellText">
								<html:checkbox name="aliquot" indexed="true" property="checkedSpecimen" disabled = "true"/>	
								<html:hidden name="aliquot" indexed="true" property="checkedSpecimen" />	
							</td>
								<html:hidden name="aliquot" indexed="true" property="readOnly"/>

						</logic:equal>
						
						<logic:equal name="aliquot" property="readOnly" value="false">
		
							<td class="dataCellText">
								 <html:text styleClass="formFieldSized10"  size="10" indexed="true" name="aliquot" property="displayName" /></td>
							<td class="dataCellText">
								 <html:text styleClass="formFieldSized10"  size="10" indexed="true" name="aliquot" property="barCode" /></td>
							<td class="dataCellText">
							
							<logic:equal name="aliquot" property="storageContainerForSpecimen" value="Virtual">
								
									 <bean:write name="aliquot" property="storageContainerForSpecimen" />
									<html:hidden name="aliquot" indexed="true" property="storageContainerForSpecimen" />
							</logic:equal>
								
							<logic:notEqual name="aliquot" property="storageContainerForSpecimen" value="Virtual">
									<bean:define id="specimenId" name="aliquot" property="uniqueIdentifier" />
									
									<%
									  containerId = "containerId_"+specimenId;
									  selectedContainerName = "selectedContainerName_"+specimenId;
									  positionDimensionOne = "positionDimensionOne_"+specimenId;
  									  positionDimensionTwo = "positionDimensionTwo_"+specimenId;
  									  functionCall="showMap('" + selectedContainerName + "','"+
																positionDimensionOne +"','"
																	+ positionDimensionTwo +"','"
																	+containerId +"')" ;

									%>

									<html:text styleClass="formFieldSized10"  size="30" styleId="<%=selectedContainerName%>" indexed="true" name="aliquot" property="selectedContainerName" readonly= "true"/>
									<html:text styleClass="formFieldSized3" indexed="true"  size="5" styleId="<%=positionDimensionOne%>" name="aliquot" property="positionDimensionOne" readonly= "true"/>
									<html:text styleClass="formFieldSized3"  size="5" styleId="<%=positionDimensionTwo%>" indexed="true" name="aliquot" property="positionDimensionTwo" readonly= "true"/>
									<html:button styleClass="actionButton" property="containerMap" 
									onclick="<%=functionCall%>" >
									<bean:message key="buttons.map"/>
									</html:button>
									<html:hidden  styleId="<%=containerId%>" name="aliquot" property="containerId" />

							</logic:notEqual>										
								</td>
							<td class="dataCellText">
								<html:text styleClass="formFieldSized5" indexed="true" name="aliquot" property="quantity" />
							</td>
							<td class="dataCellText">
								<html:checkbox name="aliquot" indexed="true"  property="checkedSpecimen" />					
							</td>
								<html:hidden name="aliquot" indexed="true" property="readOnly"/>

						</logic:equal>


					</tr>
				  </logic:iterate>	
				</table>			
			</logic:notEmpty>				 
		<logic:notEmpty name="viewSpecimenSummaryForm" property="derivedList" >
		&nbsp;
		<table>
		<tr>
		<td class="dataTablePrimaryLabel" height="20">
			<p>Derived details
		 </td>
		 </tr>
		 </table>
		    <table summary="" cellpadding="3"
						cellspacing="0" border="0" class="dataTable" >
			<tr>
	      		<th class="formSerialNumberLabelForTable" scope="col" colspan="7"> &nbsp;</th>
	      		<th class="formSerialNumberLabelForTable" scope="col"> 
					<input type="checkbox" name="chkAllDerived" onclick="ChangeCheckBoxStatus('deriveId',this);"/>					
				</th>
			</tr>
			<tr>
				<th class="formSerialNumberLabelForTable" scope="col">Parent</th>
	      		<th class="formSerialNumberLabelForTable" scope="col">Label</th>
	      		<th class="formSerialNumberLabelForTable" scope="col"> Class</th>
	      		<th class="formSerialNumberLabelForTable" scope="col"> Type</th>
	      		<th class="formSerialNumberLabelForTable" scope="col"> Qty</th>
	      		<th class="formSerialNumberLabelForTable" scope="col"> Location</th>
	      		<th class="formSerialNumberLabelForTable" scope="col"> concentration</th>
	      		<th class="formSerialNumberLabelForTable" scope="col"> Created</th>
				</tr>

			  
			  <logic:iterate name="viewSpecimenSummaryForm" property="derivedList" id="derived" indexId="count">
					<html:hidden indexed="true" name="derived" property="uniqueIdentifier" />
					<html:hidden indexed="true" name="derived" property="containerId" />
		      	<tr>
					<td class="dataCellText"> <bean:write name="derived" property="parentName" />
						 <html:hidden indexed="true" name="derived" property="parentName" /></td>

					<logic:equal name="derived" property="readOnly" value="true">
						<td class="dataCellText"> <bean:write name="derived" property="displayName" />
						  <html:hidden indexed="true" name="derived" property="displayName" /></td>
						<td class="dataCellText"> <bean:write name="derived" property="className" />
						  <html:hidden indexed="true" name="derived" property="className" /></td>
						<td class="dataCellText"> <bean:write name="derived" property="type" />
						  <html:hidden indexed="true" name="derived" property="type" /></td>
						<td class="dataCellText"> <bean:write name="derived" property="quantity" />
						  <html:hidden indexed="true" name="derived" property="quantity" /></td>
						
						<logic:equal name="derived" property="storageContainerForSpecimen" value="Virtual">
							<td class="dataCellText"> <bean:write name="derived" property="storageContainerForSpecimen" />
							<html:hidden name="derived" indexed="true" property="storageContainerForSpecimen" /></td>
						</logic:equal>
								
						<logic:notEqual name="derived" property="storageContainerForSpecimen" value="Virtual">
							<td class="dataCellText">	<html:text styleClass="formFieldSized10"  size="30" styleId="selectedContainerName"  indexed="true" name="derived" property="selectedContainerName" readonly= "true"/>
							<html:text styleClass="formFieldSized3"  size="5" styleId="positionDimensionOne"  indexed="true" name="derived" property="positionDimensionOne" readonly= "true"/>
							<html:text styleClass="formFieldSized3"  size="5" styleId="positionDimensionTwo"  indexed="true" name="derived" property="positionDimensionTwo" readonly= "true"/>
							</td>
						</logic:notEqual>										

						
						<td class="dataCellText"> &nbsp; <bean:write name="derived" property="concentration" />
						  <html:hidden indexed="true" name="derived" property="concentration" /></td>
							<td class="dataCellText">
								<html:checkbox name="derived" indexed="true" 
								property="checkedSpecimen" disabled="true"/>
								<html:hidden name="derived" indexed="true" 
								property="checkedSpecimen" />
							</td>
								<html:hidden name="derived" indexed="true" property="readOnly"/>

					</logic:equal>

					<logic:equal name="derived" property="readOnly" value="false">
						<td class="dataCellText"> <html:text styleClass="formFieldSized10"  size="10" indexed="true" name="derived" property="displayName" /></td>
						
						<td class="dataCellText"> <bean:write name="derived" property="className" />
						  <html:hidden indexed="true" name="derived" property="className" /></td>
						<td class="dataCellText"> <bean:write name="derived" property="type" />
						  <html:hidden indexed="true" name="derived" property="type" /></td>

						<td class="dataCellText">
						  <html:text styleClass="formFieldSized5"  indexed="true" name="derived" property="quantity" /></td>

						<logic:equal name="derived" property="storageContainerForSpecimen" value="Virtual">		
							<td class="dataCellText"> <bean:write name="derived" property="storageContainerForSpecimen" />
							<html:hidden name="derived" indexed="true" property="storageContainerForSpecimen" /></td>
						</logic:equal>
								
						<logic:notEqual name="derived" property="storageContainerForSpecimen" value="Virtual">
							<td class="dataCellText">
								<bean:define id="specimenId" name="derived" property="uniqueIdentifier" />
								
								<%
								  containerId = "containerId_"+specimenId;
								  selectedContainerName = "selectedContainerName_"+specimenId;
								  positionDimensionOne = "positionDimensionOne_"+specimenId;
								  positionDimensionTwo = "positionDimensionTwo_"+specimenId;
								  functionCall="showMap('" + selectedContainerName + "','"+
														positionDimensionOne +"','"
														+ positionDimensionTwo +"','"
														+containerId +"')" ;

								%>

								<html:text styleClass="formFieldSized10"  size="30" styleId="<%=selectedContainerName%>" indexed="true"  name="derived" property="selectedContainerName" readonly= "true"/>
								<html:text styleClass="formFieldSized3"  size="5" styleId="<%=positionDimensionOne%>" indexed="true" name="derived" property="positionDimensionOne" readonly= "true"/>
								<html:text styleClass="formFieldSized3"  size="5" styleId="<%=positionDimensionTwo%>" indexed="true"  name="derived" property="positionDimensionTwo" readonly= "true"/>
								<html:button styleClass="actionButton" property="containerMap" 
								onclick="<%=functionCall%>" >
								<bean:message key="buttons.map"/>
								</html:button>
								<html:hidden  styleId="<%=containerId%>" name="derived" property="containerId" />
							</td>
						</logic:notEqual>
					
						<td class="dataCellText"> 
						  <html:text indexed="true" name="derived" property="concentration" /></td>
						<td class="dataCellText">
							<html:checkbox name="derived" indexed="true"  property="checkedSpecimen" />	
						</td>
						 <html:hidden name="derived" indexed="true" property="readOnly"/>

					</logic:equal>

		      	</tr>
		      </logic:iterate>	
		    </table>
		</logic:notEmpty>		

&nbsp;
&nbsp;
		<table align="bottom">
		

			<logic:equal name="viewSpecimenSummaryForm" property="requestType" value="Collection Protocol">
			<tr>
			   <td>
				<html:submit  value="Save Collection Protocol" />
				</td>
				</tr>
			</logic:equal>
			
			<logic:equal name="viewSpecimenSummaryForm" property="requestType" value="Multiple Specimen">		
			<tr>
				<td>
				<html:submit value="Save Specimens" />
				</td>
			 </tr>
			</logic:equal>
		<html:hidden property="targetSuccess" />
		<html:hidden property="submitAction" />
		<logic:equal name="viewSpecimenSummaryForm" property="requestType" value="anticipatory specimens">
		<tr>
			<td>
			<html:submit value="Update Specimen status" onclick="form.action='GenericSpecimenSummary.do?save=SCGSpecimens'; submit()" />
			</td>
		 </tr>
		</logic:equal>
		
		</table>
		</td>
		</tr>
		</table>
		</html:form>		
</html>