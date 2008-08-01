<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/nlevelcombo.tld" prefix="ncombo" %>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ taglib uri="/WEB-INF/specimenDetails.tld" prefix="md" %>
<link rel="stylesheet" type="text/css" href="css/styleSheet.css" />
<link href="css/catissue_suite.css" rel="stylesheet" type="text/css" />
<%@ page language="java" isELIgnored="false" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
<%
String formAction = "SubmitSpecimenCollectionProtocol.do";
if(request.getAttribute(Constants.PAGEOF) != null)
{
	formAction = formAction + "?pageOf="+request.getAttribute(Constants.PAGEOF);
}

%>
	<script language="JavaScript">
		//window.parent.frames['CPTreeView'].location="ShowCollectionProtocol.do?pageOf=specimenEventsPage&operation=${requestScope.operation1}";
		
		function saveCollectionProtocol()
		{
				var action ="SubmitSpecimenCollectionProtocol.do?action=collectionprotocol";
				document.forms[0].action = action;
				document.forms[0].submit();
		}
		function toggleLayer( whichLayer )
		{
		  var elem, vis;
		  if( document.getElementById ) // this is the way the standards work
		    elem = document.getElementById( whichLayer );
		  else if( document.all ) // this is the way old msie versions work
		      elem = document.all[whichLayer];
		  else if( document.layers ) // this is the way nn4 works
		    elem = document.layers[whichLayer];
		  vis = elem.style;
		  // if the style.display value is blank we try to figure it out here
		  if(vis.display==''&&elem.offsetWidth!=undefined&&elem.offsetHeight!=undefined)
		    vis.display = (elem.offsetWidth!=0&&elem.offsetHeight!=0)?'block':'none';
		  vis.display = (vis.display==''||vis.display=='block')?'none':'block';
		}		
		
		function confirmDisable()
		{		
			
			var go = confirm("Disabling any data will disable ALL its associated data also. Once disabled you will not be able to recover any of the data back from the system. Please refer to the user manual for more details. \n Do you really want to disable?");
			if (go==true)
			{
				document.forms[0].target = "_self";
				var action ="SubmitSpecimenCollectionProtocol.do";
				document.forms[0].action = action;
				document.forms[0].submit();
			}
							
		}
		function refreshTreeOnCPSubmit()
		{
			window.parent.frames['CPTreeView'].location="ShowCollectionProtocol.do?pageOf=specimenEventsPage&operation=edit";
		}
		function confirmDisableTop()
		{		
			
			var go = confirm("Disabling any data will disable ALL its associated data also. Once disabled you will not be able to recover any of the data back from the system. Please refer to the user manual for more details. \n Do you really want to disable?");
			if (go==true)
			{
				document.forms[0].target = "_top";
				var action ="SubmitSpecimenCollectionProtocol.do";
				document.forms[0].action = action;
				document.forms[0].submit();
			}
							
		}
		function showhide()
		{
			toggleLayer('wait'); 
			toggleLayer('summary');
		}
		function onParentRadioBtnClick()
		{
			var url = 'GenericSpecimenSummary.do';
			document.forms[0].action =url;
			document.forms[0].submit();
		}
	</script>
</head>

		<html:errors />
		<html:messages id="messageKey" message="true" header="messages.header" footer="messages.footer">
			<%=messageKey%>
		</html:messages>
	<div id="summary">	
		<html:form action="<%=formAction%>" onsubmit="showhide()">		
		<table summary="" cellpadding="3" cellspacing="0" border="0" bgcolor="#FFFFFF" width="100%">
		 <tr class="td_color_F7F7F7">
				 <td height="20" colspan="2" align="left"></td>
	     </tr>
		 <tr class="td_color_F7F7F7">
			<td height="25" colspan="2" align="left" class="tr_bg_blue1"><span class="blue_ar_b">
					<bean:write name="viewSpecimenSummaryForm" property="title" /></span>
			</td>				
				<logic:equal name="viewSpecimenSummaryForm" property="requestType" value="Multiple Specimen">
					<script language="javascript">
							refreshTree('<%=Constants.CP_AND_PARTICIPANT_VIEW%>','<%=Constants.CP_TREE_VIEW%>','<%=Constants.CP_SEARCH_CP_ID%>','<%=Constants.CP_SEARCH_PARTICIPANT_ID%>','1');	
					</script>
				</logic:equal>
			</tr>
			<logic:empty name="viewSpecimenSummaryForm" property="specimenList" >
				<tr>
					<td colspan="9">  
							No specimens to display for current action!!
					</td>
				</tr>		
			</logic:empty>

			<logic:notEmpty name="viewSpecimenSummaryForm" property="specimenList" >	
			
				<tr class="td_color_F7F7F7">	
				<td colspan="2" style="padding-top:10px; padding-bottom:15px;">
					<table summary="" cellpadding="4" cellspacing="0" border="0"  width="100%">
						<tr class="tableheading">
							<th class="black_ar_b"> &nbsp; </th>
							<th class="black_ar_b">Label</th>
							<th class="black_ar_b"> Class</th>
							<th class="black_ar_b"> Type</th>
							<th class="black_ar_b"> Tissue Site</th>
							<th class="black_ar_b"> Tissue Side</th>
							<th class="black_ar_b"> Pathological Status</th>
							<th class="black_ar_b"> Storage</th>
							<th class="black_ar_b"> Qty</th>
						</tr>
					
			<md:genericSpecimenDetails columnHeaderListName="colHeaderListP" formName="viewSpecimenSummaryForm" dataListName="specimenList" dataListType="Parent" columnListName="colOrderListP" isReadOnly="true" displayColumnListName="displayColumnListP" />
			<%-- Custom tag By Mandar for specimen display --%>
<%-- 						<tr>
								<th class="formSerialNumberLabelForTable" scope="col" > &nbsp </th>
								<logic:equal name="viewSpecimenSummaryForm" property="requestType" value="anticipatory specimens">
								<th class="formSerialNumberLabelForTable" scope="col">Received</th>
								</logic:equal>
									<th class="formSerialNumberLabelForTable" scope="col">Label</th>
									<th class="formSerialNumberLabelForTable" scope="col"> Class</th>
									<th class="formSerialNumberLabelForTable" scope="col"> Type</th>
									<th class="formSerialNumberLabelForTable" scope="col"> Tissue Site</th>
									<th class="formSerialNumberLabelForTable" scope="col"> Tissue Side</th>
									<th class="formSerialNumberLabelForTable" scope="col"> Pathological Status</th>
									<th class="formSerialNumberLabelForTable" scope="col"> Storage</th>
									<th class="formSerialNumberLabelForTable" scope="col"> Qty</th>
					</tr>
				  <logic:iterate name="viewSpecimenSummaryForm" property="specimenList" id="specimen" indexId="counter">
					<tr>
						<td class="dataCellText">
						<html:radio property="selectedSpecimenId" value="uniqueIdentifier" idName="specimen" 
						onclick=" form.action='GenericSpecimenSummary.do'; submit()"/> 
						<html:hidden name="specimen" indexed="true" property="uniqueIdentifier" /></td>
						
						<logic:equal name="viewSpecimenSummaryForm" property="requestType" value="anticipatory specimens">
							<logic:equal name="specimen" property="readOnly" value="false">
								<td>
									<html:checkbox name="specimen" indexed="true" property="checkedSpecimen" />					
								</td>
							</logic:equal>
							<logic:equal name="specimen" property="readOnly" value="true">
								<td>
									<html:checkbox name="specimen" indexed="true" property="checkedSpecimen" disabled="true" value="true"/>
									<html:hidden name="specimen" indexed="true" property="checkedSpecimen" />					
								</td>
							</logic:equal>
							<html:hidden name="specimen" indexed="true" property="readOnly"/>
						</logic:equal>
						<td class="dataCellText" > <bean:write name="specimen" property="displayName" />
						<html:hidden name="specimen" indexed="true" property="displayName" /></td>
						<td class="dataCellText"> <bean:write name="specimen" property="className" />
						<html:hidden name="specimen" indexed="true" property="className" /></td>
						<td class="dataCellText"> <bean:write name="specimen" property="type" />
						<html:hidden name="specimen" indexed="true" property="type" /></td>
						<td class="dataCellText"> <bean:write name="specimen" property="tissueSite" />
						<html:hidden name="specimen" indexed="true" property="tissueSite" /></td>
						<td class="dataCellText"> <bean:write name="specimen" property="tissueSide" />
						 <html:hidden name="specimen" indexed="true" property="tissueSide" /></td>
						<td class="dataCellText"> <bean:write name="specimen" property="pathologicalStatus" />
						<html:hidden name="specimen" indexed="true" property="pathologicalStatus" /></td>

						<logic:empty name="viewSpecimenSummaryForm" property="eventId">
							<logic:equal name="specimen" property="storageContainerForSpecimen" value="Virtual">
							
								<td class="dataCellText"> <bean:write name="specimen" property="storageContainerForSpecimen" />
								<html:hidden name="specimen" indexed="true" property="storageContainerForSpecimen" /></td>
							</logic:equal>
							
							<logic:notEqual name="specimen" property="storageContainerForSpecimen" value="Virtual">
								<td class="dataCellText"> <html:text size="10" name="specimen" indexed="true" property="storageContainerForSpecimen" /></td>
							</logic:notEqual>
						</logic:empty>
						<logic:notEmpty name="viewSpecimenSummaryForm" property="eventId">
								<td class="dataCellText"> <bean:write name="specimen" property="storageContainerForSpecimen" />
								<html:hidden name="specimen" indexed="true" property="storageContainerForSpecimen" /></td>
						</logic:notEmpty>
												
						<td class="dataCellText"> <bean:write name="specimen" property="quantity" />
						<html:hidden name="specimen" indexed="true" property="quantity" />
						</td>
						
					</tr>
				  </logic:iterate>	
--%>	  
				</table>
				</td>
			</tr>
		</logic:notEmpty>
		
		
			<logic:notEmpty name="viewSpecimenSummaryForm" property="eventId">
				<html:hidden property="eventId"  />
			</logic:notEmpty>
				<html:hidden property="userAction" />
				<html:hidden property="requestType" />
			<logic:notEmpty name="viewSpecimenSummaryForm" property="aliquotList" >
				
				<tr>
					<td>			
					&nbsp;&nbsp;
					</td>
				</tr>				

				  <tr class="td_color_F7F7F7">
					<td height="25" colspan="2" align="left" class="tr_bg_blue1">
					  <span class="blue_ar_b">			
						Aliquot details
					  </span>
					</td>
				</tr>
				
				<tr>
					<td>
						<table summary="" cellpadding="4" cellspacing="0" border="0"  width="100%">
						<tr>	
							<th class="black_ar_b">Parent</th>
							<th class="black_ar_b">Label</th>
							<th class="black_ar_b">Storage</th>
							<th class="black_ar_b">Qty</th>
						</tr>
			<md:genericSpecimenDetails columnHeaderListName="colHeaderListA" formName="viewSpecimenSummaryForm" dataListName="aliquotList" dataListType="Aliquot" columnListName="colOrderListA" isReadOnly="true" displayColumnListName="displayColumnListA" />
			<%-- Custom tag By Mandar for specimen display --%>
						
<%-- 							<tr>	
								<th class="formSerialNumberLabelForTable" scope="col">Parent</th>
								<th class="formSerialNumberLabelForTable" scope="col">Label</th>
								<th class="formSerialNumberLabelForTable" scope="col">Storage</th>
								<th class="formSerialNumberLabelForTable" scope="col">Qty</th>
							</tr>
							<logic:iterate name="viewSpecimenSummaryForm" property="aliquotList" id="aliquot">
							<tr>
								<td class="dataCellText"> <bean:write name="aliquot" property="parentName" /></td>		      		
								<td class="dataCellText"> <bean:write name="aliquot" property="displayName" /></td>
								<td class="dataCellText"> <bean:write name="aliquot" property="storageContainerForSpecimen" /></td>
								<td class="dataCellText">&nbsp; <bean:write name="aliquot" property="quantity" /></td>
							</tr>
						  </logic:iterate>	
--%>													  
					 </table>			
				 </td>
			 </tr>
		</logic:notEmpty>		
		
		
		 
		<logic:notEmpty name="viewSpecimenSummaryForm" property="derivedList" >		
		<tr>
			<td>			
				&nbsp;&nbsp;
			</td>
		</tr>				

		<tr class="td_color_F7F7F7">
		 <td height="25" colspan="2" align="left" class="tr_bg_blue1">
		   <span class="blue_ar_b">
				 Derived details
             </span>
        	</td>
		</tr>
			 <td>
			    <table summary="" cellpadding="4" cellspacing="0" border="0"  width="100%">
			    <tr>
					<th class="black_ar_b">Parent</th>
					<th class="black_ar_b">Label</th>
					<th class="black_ar_b"> Class</th>
					<th class="black_ar_b"> Type</th>
					<th class="black_ar_b"> Qty</th>
					<th class="black_ar_b"> Storage</th>
					<th class="black_ar_b"> concentration</th>
				</tr>
			    
		<md:genericSpecimenDetails columnHeaderListName="colHeaderListD" formName="viewSpecimenSummaryForm" dataListName="derivedList" dataListType="Derived" columnListName="colOrderListD" isReadOnly="true" displayColumnListName="displayColumnListD" />
			<%-- Custom tag By Mandar for specimen display --%>
<%--
					<tr>
						<th class="formSerialNumberLabelForTable" scope="col">Parent</th>
						<th class="formSerialNumberLabelForTable" scope="col">Label</th>
						<th class="formSerialNumberLabelForTable" scope="col"> Class</th>
						<th class="formSerialNumberLabelForTable" scope="col"> Type</th>
						<th class="formSerialNumberLabelForTable" scope="col"> Qty</th>
						<th class="formSerialNumberLabelForTable" scope="col"> Storage</th>
						<th class="formSerialNumberLabelForTable" scope="col"> concentration</th>
					</tr>
					  <logic:iterate name="viewSpecimenSummaryForm" property="derivedList" id="derived">
		      		<tr>
						<td class="dataCellText"> <bean:write name="derived" property="parentName" /></td>
						<td class="dataCellText" > <bean:write name="derived" property="displayName" /></td>
						<td class="dataCellText"> <bean:write name="derived" property="className" /></td>
						<td class="dataCellText"> <bean:write name="derived" property="type" /></td>
						<td class="dataCellText"> <bean:write name="derived" property="quantity" /></td>
						<td class="dataCellText"> <bean:write name="derived" property="storageContainerForSpecimen" /></td>
						<td class="dataCellText"> <bean:write name="derived" property="concentration" /></td>
			      	</tr>
					  </logic:iterate>	
--%>					  
				</table>
			</td>
		</tr>
	</logic:notEmpty>		
	</table>

		<table align="bottom">
		

			<logic:equal name="viewSpecimenSummaryForm" property="requestType" value="Collection Protocol">
				<logic:equal name="viewSpecimenSummaryForm" property="collectionProtocolStatus" value="Disabled">
						<logic:equal name="viewSpecimenSummaryForm" property="specimenExist" value="true">
							<tr>
								<td>			
									&nbsp;&nbsp;
								</td>
							</tr>
							<tr>
								<td>
									<html:button  styleClass="actionButton" 
										property="submitPage" 
										title=""
										value="Save Collection Protocol" 
										onclick="confirmDisable()">
									</html:button>
								</td>
							</tr>
					</logic:equal>

					<logic:equal name="viewSpecimenSummaryForm" property="specimenExist" value="false">
							<tr>
								<td>			
									&nbsp;&nbsp;
								</td>
							</tr>
							<tr>
								<td>
									<html:button  styleClass="actionButton" 
										property="submitPage" 
										title=""
										value="Save Collection Protocol" 
										onclick="confirmDisableTop()">
									</html:button>
								</td>
							</tr>
					</logic:equal>
				</logic:equal>
				
				<logic:notEqual name="viewSpecimenSummaryForm" property="collectionProtocolStatus" value="Disabled">
					<tr>
						<td>			
							&nbsp;&nbsp;
						</td>
					</tr>
					<tr>
						<td>
							<html:submit  value="Save Collection Protocol" onclick="refreshTreeOnCPSubmit()"/>
						</td>
					</tr>
				</logic:notEqual>
					
			</logic:equal>
			
			<logic:equal name="viewSpecimenSummaryForm" property="requestType" value="Multiple Specimen">		
			<tr>
				<td>
				<html:submit value="Save Specimens" />
				</td>
			 </tr>
			</logic:equal>
		
		<logic:equal name="viewSpecimenSummaryForm" property="requestType" value="anticipatory specimens">
		<tr>
			<td>
			<html:submit value="Update Specimen status" onclick="form.action='GenericSpecimenSummary.do?save=SCGSpecimens'; submit()" />
			</td>
		 </tr>
		</logic:equal>
		
		</table>		
		</html:form>
	</div>
	<div id="wait" style="display:none;" >
	    Creating collection protocol....
	</div> 		
</html>