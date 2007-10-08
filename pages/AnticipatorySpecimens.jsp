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
						<th class="formSerialNumberLabelForTable" scope="col">Label</th>
						<th class="formSerialNumberLabelForTable" scope="col"> Barcode</th>
						<th class="formSerialNumberLabelForTable" scope="col"> Type</th>
						<th class="formSerialNumberLabelForTable" scope="col"> Qty</th>
						<th class="formSerialNumberLabelForTable" scope="col">Conc.</th>
						<th class="formSerialNumberLabelForTable" scope="col"> Location</th>
						<th class="formSerialNumberLabelForTable" scope="col">Created</th>
						
					</tr>
				
				  <logic:iterate name="viewSpecimenSummaryForm" property="specimenList" id="specimen" indexId="counter">
					<tr>
						<html:hidden indexed="true" name="specimen" property="containerId" />
						<td class="dataCellText"> <html:radio property="selectedSpecimenId" value="uniqueIdentifier" idName="specimen" 
						onclick=" form.action='GenericSpecimenSummary.do'; submit()"/> 
						<html:hidden name="specimen" indexed="true" property="uniqueIdentifier" /></td>
					<!--Editable Row -->	
						<logic:equal name="specimen" property="readOnly" value="false">
								<html:hidden name="specimen" indexed="true" property="readOnly"/>
							<td class="dataCellText" > 
							<html:text  styleClass="formFieldSized10" name="specimen" indexed="true" property="displayName" />

							</td>
							<td class="dataCellText"> 
							<html:text   styleClass="formFieldSized10" name="specimen" indexed="true" property="barCode" />

							</td>
							<td class="dataCellText"> 
							<bean:write name="specimen" property="type" />
							<html:hidden name="specimen" indexed="true" property="type" />
							</td>

							<td class="dataCellText"> 
							<html:text styleClass="formFieldSized3" name="specimen" indexed="true" property="quantity" />
							</td>
							<td class="dataCellText"> 
							<html:text styleClass="formFieldSized3" name="specimen" indexed="true" property="concentration" />
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
									<span>
									<html:text styleClass="formFieldSized7"  styleId="<%=selectedContainerName%>" indexed="true"  name="specimen" property="selectedContainerName" readonly= "true"/>
									<html:text styleClass="formFieldSized3"  styleId="<%=positionDimensionOne%>" indexed="true"  name="specimen" property="positionDimensionOne" readonly= "true"/>
									<html:text styleClass="formFieldSized3"  styleId="<%=positionDimensionTwo%>" indexed="true"  name="specimen" property="positionDimensionTwo" readonly= "true"/>
									<a href="#" onclick="<%=functionCall%>">
									<img src="images\Tree.gif" border="0" width="13" height="15" title='View storage locations'>
									</a>
									</span>									
									<html:hidden  styleId="<%=containerId%>" name="specimen" property="containerId" />
									</td>

							</logic:notEqual>												
							
							<td class="dataCellText">
								<html:checkbox name="specimen" indexed="true" property="checkedSpecimen" />					
							</td>

						</logic:equal>
					<!--/Editable Row -->

					<!---Readonly Row -->						
						<logic:equal name="specimen" property="readOnly" value="true">

							<html:hidden name="specimen" indexed="true" property="readOnly"/>
							
							<td class="dataCellText" > <bean:write name="specimen" property="displayName" />
							<html:hidden name="specimen" indexed="true" property="displayName" /></td>
							<td class="dataCellText">&nbsp;<bean:write name="specimen" property="barCode" />
							<html:hidden name="specimen" indexed="true" property="barCode" /></td>
							<td class="dataCellText"> 
							<bean:write  name="specimen" property="type" />
							<html:hidden  name="specimen" indexed="true" property="type" />
							</td>
							<td class="dataCellText"> 
							<bean:write name="specimen" property="quantity" />
							<html:hidden name="specimen" indexed="true" property="quantity" />
							</td>
							<td class="dataCellText"> 
							<html:hidden  name="specimen" indexed="true" property="concentration" />
							&nbsp; <bean:write  name="specimen" property="concentration" />
							</td>

							<logic:equal name="specimen" property="storageContainerForSpecimen" value="Virtual">
								
									<td class="dataCellText"> <bean:write name="specimen" property="storageContainerForSpecimen" />
									<html:hidden name="specimen" indexed="true" property="storageContainerForSpecimen" /></td>
							</logic:equal>
							<logic:notEqual name="specimen" property="storageContainerForSpecimen" value="Virtual">
									<td class="dataCellText">	
									<html:hidden indexed="true" name="specimen" property="selectedContainerName"/>
									<html:hidden  indexed="true"  name="specimen" property="positionDimensionOne" />
									<html:hidden  indexed="true" name="specimen" property="positionDimensionTwo" />
									<span>
										<bean:write name="specimen" property="selectedContainerName"/>
										<B>:</B>
										<bean:write  name="specimen" property="positionDimensionOne" />,
										<bean:write name="specimen" property="positionDimensionTwo" />
									</span>
							</logic:notEqual>												
							<td class="dataCellText">
								<html:checkbox name="specimen" indexed="true" property="checkedSpecimen" disabled="true" value="true"/>
								<html:hidden name="specimen" indexed="true" property="checkedSpecimen" />					
							</td>
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
				<html:hidden property="selectedSpecimenId"  />
			</logic:notEmpty>
				<html:hidden property="userAction" />
				<html:hidden property="requestType" />
			<logic:notEmpty name="viewSpecimenSummaryForm" property="aliquotList" >
			&nbsp;
			<table>
			<tr>
			<td class="dataTablePrimaryLabel" height="20">		
				Aliquot details
			</td>
			</tr>
			</table>
			<table summary="" cellpadding="3"
							cellspacing="0" border="0" class="dataTable" >
			<tr>
	      		<th class="formSerialNumberLabelForTable" scope="col" colspan="7"> &nbsp;</th>
	      		<th class="formSerialNumberLabelForTable" scope="col"> 
					<input type="checkbox" name="chkAllDerived" onclick="ChangeCheckBoxStatus('aliquotId',this);"/>			
				</th>
			</tr>

				<tr>	
					<th class="formSerialNumberLabelForTable" scope="col">Parent</th>
						<th class="formSerialNumberLabelForTable" scope="col">Label</th>
						<th class="formSerialNumberLabelForTable" scope="col"> Barcode</th>
						<th class="formSerialNumberLabelForTable" scope="col"> Type</th>
						<th class="formSerialNumberLabelForTable" scope="col"> Qty</th>
						<th class="formSerialNumberLabelForTable" scope="col">Conc.</th>
						<th class="formSerialNumberLabelForTable" scope="col"> Location</th>
						<th class="formSerialNumberLabelForTable" scope="col">Created</th>
					</tr>
				  <logic:iterate name="viewSpecimenSummaryForm" property="aliquotList" id="aliquot" indexId="ctr">
					<tr>

						<td class="dataCellText"> <html:hidden  name="aliquot" indexed="true" property="displayName" />
						<bean:write  name="aliquot" property="displayName" />
						<html:hidden name="aliquot" indexed="true" property="uniqueIdentifier" /></td>
					<!--Editable Row -->	
						<logic:equal name="aliquot" property="readOnly" value="false">
								<html:hidden name="aliquot" indexed="true" property="readOnly"/>
							<td class="dataCellText" > 
							<html:text  styleClass="formFieldSized10" name="aliquot" indexed="true" property="displayName" />

							</td>
							<td class="dataCellText"> 
							<html:text   styleClass="formFieldSized10" name="aliquot" indexed="true" property="barCode" />

							</td>
							<td class="dataCellText"> 
							<bean:write name="aliquot" property="type" />
							<html:hidden name="aliquot" indexed="true" property="type" />
							</td>

							<td class="dataCellText"> 
							<html:text styleClass="formFieldSized3" name="aliquot" indexed="true" property="quantity" />
							</td>
							<td class="dataCellText"> 
							<html:text styleClass="formFieldSized3" name="aliquot" indexed="true" property="concentration" />
							</td>
							
							<logic:equal name="aliquot" property="storageContainerForSpecimen" value="Virtual">
								
							<td class="dataCellText"> <bean:write name="aliquot" property="storageContainerForSpecimen" />
									<html:hidden name="aliquot" indexed="true" property="storageContainerForSpecimen" /></td>
							</logic:equal>
								
							<logic:notEqual name="aliquot" property="storageContainerForSpecimen" value="Virtual">
									<td class="dataCellText">
									
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
									<span>
									<html:text styleClass="formFieldSized7"  styleId="<%=selectedContainerName%>" indexed="true"  name="aliquot" property="selectedContainerName" readonly= "true"/>
									<html:text styleClass="formFieldSized3"  styleId="<%=positionDimensionOne%>" indexed="true"  name="aliquot" property="positionDimensionOne" readonly= "true"/>
									<html:text styleClass="formFieldSized3"  styleId="<%=positionDimensionTwo%>" indexed="true"  name="aliquot" property="positionDimensionTwo" readonly= "true"/>
									<a href="#" onclick="<%=functionCall%>">
									<img src="images\Tree.gif" border="0" width="13" height="15" title='View storage locations'>
									</a>
									</span>									
									<html:hidden  styleId="<%=containerId%>" name="aliquot" property="containerId" />
									</td>

							</logic:notEqual>												
							
							<td class="dataCellText">
								<html:checkbox name="aliquot" indexed="true" property="checkedSpecimen" />					
							</td>

						</logic:equal>
					<!--/Editable Row -->

					<!---Readonly Row -->						
						<logic:equal name="aliquot" property="readOnly" value="true">

							<html:hidden name="aliquot" indexed="true" property="readOnly"/>
							
							<td class="dataCellText" > <bean:write name="aliquot" property="displayName" />
							<html:hidden name="aliquot" indexed="true" property="displayName" /></td>
							<td class="dataCellText">&nbsp;<bean:write name="aliquot" property="barCode" />
							<html:hidden name="aliquot" indexed="true" property="barCode" /></td>
							<td class="dataCellText"> 
							<bean:write  name="aliquot" property="type" />
							<html:hidden  name="aliquot" indexed="true" property="type" />
							</td>
							<td class="dataCellText"> 
							<bean:write name="aliquot" property="quantity" />
							<html:hidden name="aliquot" indexed="true" property="quantity" />
							</td>
							<td class="dataCellText"> 
							<html:hidden  name="aliquot" indexed="true" property="concentration" />
							&nbsp; <bean:write  name="aliquot" property="concentration" />
							</td>

							<logic:equal name="aliquot" property="storageContainerForSpecimen" value="Virtual">
								
									<td class="dataCellText"> <bean:write name="aliquot" property="storageContainerForSpecimen" />
									<html:hidden name="aliquot" indexed="true" property="storageContainerForSpecimen" /></td>
							</logic:equal>
							<logic:notEqual name="aliquot" property="storageContainerForSpecimen" value="Virtual">
									<td class="dataCellText">	
									<html:hidden indexed="true" name="aliquot" property="selectedContainerName"/>
									<html:hidden  indexed="true"  name="aliquot" property="positionDimensionOne" />
									<html:hidden  indexed="true" name="aliquot" property="positionDimensionTwo" />
									<span>
										<bean:write name="aliquot" property="selectedContainerName"/>
										<B>:</B>
										<bean:write  name="aliquot" property="positionDimensionOne" />,
										<bean:write name="aliquot" property="positionDimensionTwo" />
									<html:hidden name="aliquot" property="containerId" />

									</span>
							</logic:notEqual>												
							<td class="dataCellText">
								<html:checkbox name="aliquot" indexed="true" property="checkedSpecimen" disabled="true" value="true"/>
								<html:hidden name="aliquot" indexed="true" property="checkedSpecimen" />					
							</td>
							</td>
						</logic:equal>					
					<!--/Readonly Row -->
					</tr>
				  </logic:iterate>	
				</table>			
			</logic:notEmpty>				 
		<logic:notEmpty name="viewSpecimenSummaryForm" property="derivedList" >
		&nbsp;
		<table>
		<tr>
		<td class="dataTablePrimaryLabel" height="20">
			Derivative details
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
				<th class="formSerialNumberLabelForTable" scope="col"> Parent </th>
				<th class="formSerialNumberLabelForTable" scope="col">Label</th>
				<th class="formSerialNumberLabelForTable" scope="col"> Barcode</th>
				<th class="formSerialNumberLabelForTable" scope="col"> Type</th>
				<th class="formSerialNumberLabelForTable" scope="col"> Qty</th>
				<th class="formSerialNumberLabelForTable" scope="col">Conc.</th>
				<th class="formSerialNumberLabelForTable" scope="col"> Location</th>
				<th class="formSerialNumberLabelForTable" scope="col">Created</th>
			</tr>

			  
			  <logic:iterate name="viewSpecimenSummaryForm" property="derivedList" id="derived" indexId="count">
					<html:hidden indexed="true" name="derived" property="uniqueIdentifier" />
					<tr>
							<td class="dataCellText" > 
							<html:hidden name="derived" indexed="true" property="parentName" />
							<bean:write name="derived" property="parentName" />
							</td>
					<!--Editable Row -->	
						<logic:equal name="derived" property="readOnly" value="false">
								<html:hidden name="derived" indexed="true" property="readOnly"/>
							<td class="dataCellText" > 
							<html:text  styleClass="formFieldSized10" name="derived" indexed="true" property="displayName" />

							</td>

							<td class="dataCellText"> 
							<html:text styleClass="formFieldSized10" name="derived" indexed="true" property="barCode" />

							</td>
							<td class="dataCellText"> 
							<bean:write name="derived" property="type" />
							<html:hidden name="derived" indexed="true" property="type" />
							</td>

							<td class="dataCellText"> 
							<html:text styleClass="formFieldSized3" name="derived" indexed="true" property="quantity" />
							</td>
							<td class="dataCellText"> 
							<html:text styleClass="formFieldSized3" name="derived" indexed="true" property="concentration" />
							</td>
							
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
									<span>
									<html:text styleClass="formFieldSized7"  styleId="<%=selectedContainerName%>" indexed="true"  name="derived" property="selectedContainerName" readonly= "true"/>
									<html:text styleClass="formFieldSized3"  styleId="<%=positionDimensionOne%>" indexed="true"  name="derived" property="positionDimensionOne" readonly= "true"/>
									<html:text styleClass="formFieldSized3"  styleId="<%=positionDimensionTwo%>" indexed="true"  name="derived" property="positionDimensionTwo" readonly= "true"/>
									<a href="#" onclick="<%=functionCall%>">
									<img src="images\Tree.gif" border="0" width="13" height="15" title='View storage locations'>
									</a>
									</span>									
									<html:hidden  styleId="<%=containerId%>" name="derived" property="containerId" />
									</td>

							</logic:notEqual>												
							
							<td class="dataCellText">
								<html:checkbox name="derived" indexed="true" property="checkedSpecimen" />					
							</td>

						</logic:equal>
					<!--/Editable Row -->

					<!---Readonly Row -->						
						<logic:equal name="derived" property="readOnly" value="true">

							<html:hidden name="derived" indexed="true" property="readOnly"/>
							
							<td class="dataCellText" > <bean:write name="derived" property="displayName" />
							<html:hidden name="derived" indexed="true" property="displayName" /></td>
							<td class="dataCellText">&nbsp;<bean:write name="derived" property="barCode" />
							<html:hidden name="derived" indexed="true" property="barCode" /></td>
							<td class="dataCellText"> 
							<bean:write  name="derived" property="type" />
							<html:hidden  name="derived" indexed="true" property="type" />
							</td>
							<td class="dataCellText"> 
							<bean:write name="derived" property="quantity" />
							<html:hidden name="derived" indexed="true" property="quantity" />
							</td>
							<td class="dataCellText"> 
							<html:hidden  name="derived" indexed="true" property="concentration" />
							&nbsp; <bean:write  name="derived" property="concentration" />
							</td>

							<logic:equal name="derived" property="storageContainerForSpecimen" value="Virtual">
								
									<td class="dataCellText"> <bean:write name="derived" property="storageContainerForSpecimen" />
									<html:hidden name="derived" indexed="true" property="storageContainerForSpecimen" /></td>
							</logic:equal>
							<logic:notEqual name="derived" property="storageContainerForSpecimen" value="Virtual">
									<td class="dataCellText">	
									<html:hidden indexed="true" name="derived" property="selectedContainerName"/>
									<html:hidden  indexed="true"  name="derived" property="positionDimensionOne" />
									<html:hidden  indexed="true" name="derived" property="positionDimensionTwo" />
									<html:hidden  name="derived" property="containerId" />

									<span>
										<bean:write name="derived" property="selectedContainerName"/>
										<B>:</B>
										<bean:write  name="derived" property="positionDimensionOne" />,
										<bean:write name="derived" property="positionDimensionTwo" />
									</span>
							</logic:notEqual>												
							<td class="dataCellText">
								<html:checkbox name="derived" indexed="true" property="checkedSpecimen" disabled="true" value="true"/>
								<html:hidden name="derived" indexed="true" property="checkedSpecimen" />					
							</td>
							</td>
						</logic:equal>					
					<!--/Readonly Row -->
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
			<html:submit value="Submit" onclick="form.action='GenericSpecimenSummary.do?save=SCGSpecimens'; submit()" />
			</td>
		 </tr>
		</logic:equal>
		
		</table>
		</td>
		</tr>
		</table>
		</html:form>
</html>