<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/nlevelcombo.tld" prefix="ncombo" %>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.bean.GenericSpecimen"%>

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
  String specimenClassName;
  String cpId;
  String functionCall;
  

if(request.getAttribute(Constants.PAGEOF) != null)
{
	formAction = formAction + "?pageOf="+request.getAttribute(Constants.PAGEOF);
}

%>
	<script language="JavaScript">
		window.parent.frames['SpecimenEvents'].location="ShowCollectionProtocol.do?pageOf=specimenEventsPage&operation=ViewSummary";

		
		//kalpana bug#6001:Reviewer-Vaishali
		//Apply storage container name at first location to all.
		
		function ApplyToAll(object,type)
		{
			
			if(object.checked )
			{
				var fields = document.getElementsByTagName("input");
				var i =0;
				var text="";
				var valueToSet = "";
				var isFirstField = true;
				
				for (i=0; i<fields.length;i++)
				{
					text = fields[i].name;
					if(text.indexOf(type)>=0 && text.indexOf(".selectedContainerName")>=0)
					{
						if(isFirstField)
						{
							valueToSet = fields[i].value;
							isFirstField = false;
						}
						if(fields[i].value=="")
						{
							fields[i].value = valueToSet;
						}
				
					}
				}
				
			}
		}

		function saveCollectionProtocol()
		{
				var action ="SubmitSpecimenCollectionProtocol.do?action=collectionprotocol";
				document.forms[0].action = action;
				document.forms[0].submit();
		}

		function showMap(selectedContainerName,positionDimensionOne,positionDimensionTwo,containerId,specimenClassName,cpId)
		{
			frameUrl="ShowFramedPage.do?pageOf=pageOfSpecimen&"+
				"selectedContainerName=" + selectedContainerName +
				"&pos1=" + positionDimensionOne + 
				"&pos2=" + positionDimensionTwo +
				"&containerId=" +containerId +
				"&<%=Constants.CAN_HOLD_SPECIMEN_CLASS %>="+specimenClassName +
				"&<%=Constants.CAN_HOLD_COLLECTION_PROTOCOL%>=" + cpId;
			
			var storageContainer = document.getElementById(selectedContainerName).value;
			frameUrl+="&storageContainerName="+storageContainer;

			openPopupWindow(frameUrl,'newSpecimenPage');
			//mapButtonClickedOnSpecimen(frameUrl,'newSpecimenPage');
		}
		
		function ChangeCheckBoxStatus(type,chkInstance)
		{
			var checkedSpecimen ='].checkedSpecimen';
			var elementType = type +'[';
			var ctr=0;
			do
			{
				var elementName = elementType + ctr + checkedSpecimen;

				var chkCount= document.getElementsByName(elementName).length;
				//alert(chkCount);
				//alert(document.getElementsByName(elementName));
				if (chkCount >0)
				{
					var element = document.getElementsByName(elementName)[0];
					if (element.disabled == false)
					{
						element.checked = chkInstance.checked;
					}
					ctr++;
				}
			}
			while(chkCount>0);

		}
		
		function UpdateCheckBoxStatus()
		{
			var checkedSpecimen ='].checkedSpecimen';
			var aliquotType = "aliquot[";
			var ctr = 0;
			if (document.forms[0].chkAllAliquot != null)
			{
				document.forms[0].chkAllAliquot.disabled = true;
				document.forms[0].chkAllAliquot.checked = true;
				do
				{
					var elementName = aliquotType + ctr + checkedSpecimen;
					var chkCount= document.getElementsByName(elementName).length;
					if (chkCount >0)
					{
						var element = document.getElementsByName(elementName)[0];
						if (element.disabled == false)
						{
							document.forms[0].chkAllAliquot.disabled = false;							
						}
						ctr++;
					}
				}while(chkCount>0);
			}
			
			if (document.forms[0].chkAllAliquot != null)
			{
				ctr = 0;
				var derivedType = "derived[";
				document.forms[0].chkAllDerived.disabled = true;
				document.forms[0].chkAllDerived.checked = true;
				do
				{
					var elementName = derivedType + ctr + checkedSpecimen;
					var chkCount= document.getElementsByName(elementName).length;
					if (chkCount >0)
					{
						var element = document.getElementsByName(elementName)[0];
						if (element.disabled == false)
						{
							document.forms[0].chkAllDerived.disabled = false;							
						}
						ctr++;
					}
				}while(chkCount>0);
			}		
		}
		function pageSubmit()
		{
			alert("page submit");
			var printFlag = document.getElementById("printCheckbox");
			if(printFlag.checked)
			{
				document.forms[0].action ='GenericSpecimenSummary.do?save=SCGSpecimens&printflag=1'; 
				document.forms[0].submit();
			}
			else
			{
				document.forms[0].action ='GenericSpecimenSummary.do?save=SCGSpecimens&printflag=0'; 
				document.forms[0].submit();
			}
		}
	</script>
</head>
<body onload="UpdateCheckBoxStatus()">
<script language="javascript">
	refreshTree('<%=Constants.CP_AND_PARTICIPANT_VIEW%>','<%=Constants.CP_TREE_VIEW%>','<%=Constants.CP_SEARCH_CP_ID%>','<%=Constants.CP_SEARCH_PARTICIPANT_ID%>','1');	
</script>

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
		<logic:equal name="viewSpecimenSummaryForm" property="readOnly" value="false">
		<tr>					
				<td class="formFieldNoBorders" colspan="5"  height="20" >
				<html:checkbox styleId="printCheckbox" property="printCheckbox" value="true" onclick="">
						<bean:message key="print.checkboxLabel"/>
				</html:checkbox>
		    	</td>
		</tr>
		<tr>
		<td>
		<input type="button" value="Submit" onclick="pageSubmit()" />
		</td>
		</tr>
		</logic:equal>
		<tr>
			<td>
				&nbsp;
			</td>
		</tr>
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
						<logic:equal name="viewSpecimenSummaryForm" property="showLabel" value="true">
							<th class="formSerialNumberLabelForTable" scope="col"><bean:message key="specimen.label"/></th>
						</logic:equal>
						<logic:equal name="viewSpecimenSummaryForm" property="showbarCode" value="true">
							<th class="formSerialNumberLabelForTable" scope="col"> <bean:message key="specimen.barcode"/></th>
						</logic:equal>
						<th class="formSerialNumberLabelForTable" scope="col"> <bean:message key="specimen.subType"/></th>
						<th class="formSerialNumberLabelForTable" scope="col"> <bean:message key="anticipatorySpecimen.Quantity"/></th>
						<th class="formSerialNumberLabelForTable" scope="col"><bean:message key="anticipatorySpecimen.Concentration"/></th>
						<th class="formSerialNumberLabelForTable" scope="col"><bean:message key="anticipatorySpecimen.Location"/>
							<input id="chkSpecimen" type="checkbox" onClick="ApplyToAll(this,'specimen')"/> Apply First to All
						</th>
						<logic:equal name="viewSpecimenSummaryForm" property="showCheckBoxes" value="true">
							<th class="formSerialNumberLabelForTable" scope="col"><bean:message key="anticipatorySpecimen.Collected"/></th>
						</logic:equal>
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
							
							<logic:equal name="viewSpecimenSummaryForm" property="showLabel" value="true">
								<td class="dataCellText" > 
								<html:text  styleClass="formFieldSized10" name="specimen" indexed="true" property="displayName" />
								</td>
							</logic:equal>
							
							<logic:equal name="viewSpecimenSummaryForm" property="showbarCode" value="true">
								<td class="dataCellText"> 
								<html:text   styleClass="formFieldSized10" name="specimen" indexed="true" property="barCode" />
								</td>
							</logic:equal>

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
									<bean:define id="specimenClass" name="specimen" property="className"/>
									<bean:define id="collectionProtocolId" name="specimen" property="collectionProtocolId"/>
									<%
									  containerId = "containerId_"+specimenId;
									  selectedContainerName = "selectedContainerName_"+specimenId;
									  positionDimensionOne = "positionDimensionOne_"+specimenId;
  									  positionDimensionTwo = "positionDimensionTwo_"+specimenId;
  									  specimenClassName = (String)specimenClass;
  									  cpId = collectionProtocolId.toString();
  									  String classNameStyleId = "className_"+specimenId;
  									  String cpStyleId = "cp_"+specimenId;
									  functionCall="showMap('" + selectedContainerName + "','"+
																	positionDimensionOne +"','"
																	+ positionDimensionTwo +"','"
																	+containerId +"','"+
																	specimenClassName +"','"+
																	cpId +"')" ;
									%>
									
									<logic:equal name="viewSpecimenSummaryForm" property="showParentStorage" value="true">
									 <table style="font-size:1em" size="100%">
										<tr>
											<td>
												<html:text styleClass="formFieldSized7"  styleId="<%=selectedContainerName%>" indexed="true"  name="specimen" property="selectedContainerName" />
											</td>
											<td>
												<html:text styleClass="formFieldSized3"  styleId="<%=positionDimensionOne%>" indexed="true"  name="specimen" property="positionDimensionOne" />
											</td>
											<td>
												<html:text styleClass="formFieldSized3"  styleId="<%=positionDimensionTwo%>" indexed="true"  name="specimen" property="positionDimensionTwo" />
											</td>
											<td>
												<a href="#" onclick="<%=functionCall%>">
												<img src="images\Tree.gif" border="0" width="13" height="15" title='View storage locations'>
												</a>
												<html:hidden  styleId="<%=containerId%>" name="specimen" property="containerId" />
												
											</td>
										</tr>										
									 </table>
									 </logic:equal>

									<logic:equal name="viewSpecimenSummaryForm" property="showParentStorage" value="false">
									
										<html:hidden indexed="true" name="specimen" property="selectedContainerName"/>
										<html:hidden  indexed="true"  name="specimen" property="positionDimensionOne" />
										<html:hidden  indexed="true" name="specimen" property="positionDimensionTwo" />
										<span>
											<bean:write name="specimen" property="selectedContainerName"/>
											<B>:</B>
											<bean:write  name="specimen" property="positionDimensionOne" />,
											<bean:write name="specimen" property="positionDimensionTwo" />
										</span>
									 </logic:equal>
									</td>
							<html:hidden  name="specimen" property="className" styleId="<%=classNameStyleId%>" indexed="true"/>
							<html:hidden  name="specimen" property="collectionProtocolId" styleId="<%=cpStyleId%>" indexed="true"/>
							</logic:notEqual>												
							<logic:equal name="viewSpecimenSummaryForm" property="showCheckBoxes" value="true">
								<td class="dataCellText">
									<html:checkbox name="specimen" indexed="true" property="checkedSpecimen" />					
								</td>
							</logic:equal>
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
							<logic:equal name="viewSpecimenSummaryForm" property="showCheckBoxes" value="true">
								<td class="dataCellText">
									<html:checkbox name="specimen" indexed="true" property="checkedSpecimen" disabled="true" value="true"/>
									<html:hidden name="specimen" indexed="true" property="checkedSpecimen" />					
								</td>
							</logic:equal>
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
				
				
				
			<logic:empty name="viewSpecimenSummaryForm" property="aliquotList" >
				
				<logic:empty name="viewSpecimenSummaryForm" property="derivedList" >
				<table>
				<tr> <td> <br> </td> </tr>
					<tr>
						<td class="dataTablePrimaryLabel" colspan="6" height="20">  
							Child specimens not defined.
						</td>						
					</tr>
					<tr> <td> <br> </td> </tr>
				</table>	
				</logic:empty>				
				
			</logic:empty>
			
			<logic:notEmpty name="viewSpecimenSummaryForm" property="aliquotList" >
			
			&nbsp;
			<table>
				<tr>
					<td class="dataTablePrimaryLabel" height="20">	
					<bean:message key="anticipatorySpecimen.AliquotDetails"/>
					</td>
				</tr>
			</table>
			<table summary="" cellpadding="3"
							cellspacing="0" border="0" class="dataTable" >
			

				<tr>	
						<logic:equal name="viewSpecimenSummaryForm" property="showLabel" value="true">
							<th class="formSerialNumberLabelForTable" scope="col"> <bean:message key="anticipatorySpecimen.Parent"/> </th>				
							<th class="formSerialNumberLabelForTable" scope="col"><bean:message key="specimen.label"/></th>
						</logic:equal>
						<logic:equal name="viewSpecimenSummaryForm" property="showbarCode" value="true">
							<th class="formSerialNumberLabelForTable" scope="col"> <bean:message key="specimen.barcode"/></th>
						</logic:equal>
						<th class="formSerialNumberLabelForTable" scope="col"> <bean:message key="specimen.subType"/></th>
						<th class="formSerialNumberLabelForTable" scope="col"> <bean:message key="anticipatorySpecimen.Quantity"/></th>
						<th class="formSerialNumberLabelForTable" scope="col"><bean:message key="anticipatorySpecimen.Concentration"/></th>
						<th class="formSerialNumberLabelForTable" scope="col"> <bean:message key="anticipatorySpecimen.Location"/>
							<input id="chkAliquot" type="checkbox" onClick="ApplyToAll(this,'aliquot')"/> Apply First to All
						</th>
						<logic:equal name="viewSpecimenSummaryForm" property="showCheckBoxes" value="true">
							<th class="formSerialNumberLabelForTable" scope="col">
							<input type="checkbox" name="chkAllAliquot" onclick="ChangeCheckBoxStatus('aliquot',this)"/>
							<bean:message key="anticipatorySpecimen.Created"/></th>
						</logic:equal>
					</tr>
				  <logic:iterate name="viewSpecimenSummaryForm" property="aliquotList" id="aliquot" indexId="ctr">
					<tr>
						<logic:equal name="viewSpecimenSummaryForm" property="showLabel" value="true">
							<td class="dataCellText"> <html:hidden  name="aliquot" indexed="true" property="parentName" />
							<bean:write  name="aliquot" property="parentName" />
						</logic:equal>
						<html:hidden name="aliquot" indexed="true" property="uniqueIdentifier" /></td>
					<!--Editable Row -->	
						<logic:equal name="aliquot" property="readOnly" value="false">
								<html:hidden name="aliquot" indexed="true" property="readOnly"/>
							
							<logic:equal name="viewSpecimenSummaryForm" property="showLabel" value="true">
								<td class="dataCellText" > 
								<html:text  styleClass="formFieldSized10" name="aliquot" indexed="true" property="displayName" />
								</td>
							</logic:equal>
							
							<logic:equal name="viewSpecimenSummaryForm" property="showbarCode" value="true">
								<td class="dataCellText"> 
								<html:text   styleClass="formFieldSized10" name="aliquot" indexed="true" property="barCode" />
								</td>
							</logic:equal>

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
									<bean:define id="specimenClass" name="aliquot" property="className"/>
									<bean:define id="collectionProtocolId" name="aliquot" property="collectionProtocolId"/>
									<%
									  containerId = "containerId_"+specimenId;
									  selectedContainerName = "selectedContainerName_"+specimenId;
									  positionDimensionOne = "positionDimensionOne_"+specimenId;
  									  positionDimensionTwo = "positionDimensionTwo_"+specimenId;
  									  specimenClassName = (String)specimenClass;
  									  cpId = collectionProtocolId.toString();
  									  String classNameStyleId = "className_"+specimenId;
  									  String cpStyleId = "cp_"+specimenId;
									  functionCall="showMap('" + selectedContainerName + "','"+
																	positionDimensionOne +"','"
																	+ positionDimensionTwo +"','"
																	+containerId +"','"+
																	specimenClassName +"','"+
																	cpId +"')" ;
									%>
									<table style="font-size:1em" size="100%">
										<tr>
											<td>
												<html:text styleClass="formFieldSized7"  styleId="<%=selectedContainerName%>" indexed="true"  name="aliquot" property="selectedContainerName" />
											</td>
											<td>
												<html:text styleClass="formFieldSized3"  styleId="<%=positionDimensionOne%>" indexed="true"  name="aliquot" property="positionDimensionOne" />
											</td>
											<td>
												<html:text styleClass="formFieldSized3"  styleId="<%=positionDimensionTwo%>" indexed="true"  name="aliquot" property="positionDimensionTwo" />
											</td>
											<td>
												<a href="#" onclick="<%=functionCall%>">
												<img src="images\Tree.gif" border="0" width="13" height="15" title='View storage locations'>
												</a>
												<html:hidden  styleId="<%=containerId%>" name="aliquot" property="containerId" />
											</td>
										</tr>
									</table>
									</td>
								<html:hidden  name="aliquot" property="className" styleId="<%=classNameStyleId%>" indexed="true"/>
								<html:hidden  name="aliquot" property="collectionProtocolId" styleId="<%=cpStyleId%>" indexed="true"/>
							</logic:notEqual>												
							
							<logic:equal name="viewSpecimenSummaryForm" property="showCheckBoxes" value="true">
								<td class="dataCellText">
									<html:checkbox name="aliquot" styleId="aliquotId"
									indexed="true" property="checkedSpecimen" />					
								</td>
							</logic:equal>

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

							<logic:equal name="viewSpecimenSummaryForm" property="showCheckBoxes" value="true">
								<td class="dataCellText">
									<html:checkbox name="aliquot" indexed="true" property="checkedSpecimen" disabled="true" value="true"/>
									<html:hidden name="aliquot" indexed="true" property="checkedSpecimen" />					
								</td>
							</logic:equal>
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
			<bean:message key="anticipatorySpecimen.DerivativeDetails"/>
		 </td>
		 </tr>
		 </table>
		    <table summary="" cellpadding="3"
						cellspacing="0" border="0" class="dataTable" >
			
			<tr>
				<logic:equal name="viewSpecimenSummaryForm" property="showLabel" value="true">
					<th class="formSerialNumberLabelForTable" scope="col"> <bean:message key="anticipatorySpecimen.Parent"/> </th>				
					<th class="formSerialNumberLabelForTable" scope="col"><bean:message key="specimen.label"/></th>
				</logic:equal>
				<logic:equal name="viewSpecimenSummaryForm" property="showbarCode" value="true">
					<th class="formSerialNumberLabelForTable" scope="col"> <bean:message key="specimen.barcode"/></th>
				</logic:equal>
				<th class="formSerialNumberLabelForTable" scope="col"> <bean:message key="specimen.subType"/></th>
				<th class="formSerialNumberLabelForTable" scope="col"> <bean:message key="anticipatorySpecimen.Quantity"/></th>
				<th class="formSerialNumberLabelForTable" scope="col"><bean:message key="anticipatorySpecimen.Concentration"/></th>
				<th class="formSerialNumberLabelForTable" scope="col"> <bean:message key="anticipatorySpecimen.Location"/>
					<input id="chkDrived" type="checkbox" onClick="ApplyToAll(this,'derived')"/> Apply First to All
				</th>
				<logic:equal name="viewSpecimenSummaryForm" property="showCheckBoxes" value="true">
					<th class="formSerialNumberLabelForTable" scope="col">
					<input type="checkbox" name="chkAllDerived" onclick="ChangeCheckBoxStatus('derived',this)"/>
					<bean:message key="anticipatorySpecimen.Created"/></th>
				</logic:equal>
			</tr>

			  
			  <logic:iterate name="viewSpecimenSummaryForm" property="derivedList" id="derived" indexId="count">
					<html:hidden indexed="true" name="derived" property="uniqueIdentifier" />
					<tr>
						<logic:equal name="viewSpecimenSummaryForm" property="showLabel" value="true">
							<td class="dataCellText" > 
							<html:hidden name="derived" indexed="true" property="parentName" />
							<bean:write name="derived" property="parentName" />
							</td>
						</logic:equal>

					<!--Editable Row -->	
						<logic:equal name="derived" property="readOnly" value="false">
							<html:hidden name="derived" indexed="true" property="readOnly"/>

							<logic:equal name="viewSpecimenSummaryForm" property="showLabel" value="true">
								<td class="dataCellText" > 
									<html:text  styleClass="formFieldSized10" name="derived" indexed="true" property="displayName" />
								</td>
							</logic:equal>
							<logic:equal name="viewSpecimenSummaryForm" property="showbarCode" value="true">
								<td class="dataCellText"> 
								<html:text styleClass="formFieldSized10" name="derived" indexed="true" property="barCode" />
								</td>
							</logic:equal>

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
									<bean:define id="specimenClass" name="derived" property="className"/>
									<bean:define id="collectionProtocolId" name="derived" property="collectionProtocolId"/>
									<%
									  containerId = "containerId_"+specimenId;
									  selectedContainerName = "selectedContainerName_"+specimenId;
									  positionDimensionOne = "positionDimensionOne_"+specimenId;
  									  positionDimensionTwo = "positionDimensionTwo_"+specimenId;
									  specimenClassName = (String)specimenClass;
  									  cpId = collectionProtocolId.toString();
  									  String classNameStyleId = "className_"+specimenId;
  									  String cpStyleId = "cp_"+specimenId;
									  functionCall="showMap('" + selectedContainerName + "','"+
																	positionDimensionOne +"','"
																	+ positionDimensionTwo +"','"
																	+containerId +"','"+
																	specimenClassName +"','"+
																	cpId +"')" ;
									%>
									<table style="font-size:1em" size="100%">
										<tr>
											<td>
												<html:text styleClass="formFieldSized7"  styleId="<%=selectedContainerName%>" indexed="true"  name="derived" property="selectedContainerName" />
											</td>
											<td>
												<html:text styleClass="formFieldSized3"  styleId="<%=positionDimensionOne%>" indexed="true"  name="derived" property="positionDimensionOne" />
											</td>
											<td>
												<html:text styleClass="formFieldSized3"  styleId="<%=positionDimensionTwo%>" indexed="true"  name="derived" property="positionDimensionTwo" />
											</td>
											<td>
												<a href="#" onclick="<%=functionCall%>">
												<img src="images\Tree.gif" border="0" width="13" height="15" title='View storage locations'>
												</a>
												<html:hidden  styleId="<%=containerId%>" name="derived" property="containerId" />
											</td>
										</tr>
									</table>
									</td>
							<html:hidden  name="derived" property="className" styleId="<%=classNameStyleId%>" indexed="true"/>
							<html:hidden  name="derived" property="collectionProtocolId" styleId="<%=cpStyleId%>" indexed="true"/>
							</logic:notEqual>	
							
							<logic:equal name="viewSpecimenSummaryForm" property="showCheckBoxes" value="true">
								<td class="dataCellText">
									<html:checkbox name="derived" indexed="true" 
									styleId="deriveId" property="checkedSpecimen" />					
								</td>
							</logic:equal>

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
							
							<logic:equal name="viewSpecimenSummaryForm" property="showCheckBoxes" value="true">
								<td class="dataCellText">
									<html:checkbox name="derived" indexed="true" property="checkedSpecimen" disabled="true" value="true"/>
									<html:hidden name="derived" indexed="true" property="checkedSpecimen" />					
								</td>
							</logic:equal>
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
		<html:hidden property="showCheckBoxes" />
		<html:hidden property="showLabel" />
		<html:hidden property="showbarCode" />
		<html:hidden property="readOnly" />
		<html:hidden property="showParentStorage" />
		
		<logic:equal name="viewSpecimenSummaryForm" property="readOnly" value="false">
		 <tr>
			<td>
			<input type="button" value="Submit" onclick="pageSubmit()" />
			</td>
		 </tr>
		</logic:equal>
		
		</table>
		</td>
		</tr>
		</table>
		</html:form>
</body>
</html>