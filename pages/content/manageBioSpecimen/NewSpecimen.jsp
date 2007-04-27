<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/nlevelcombo.tld" prefix="ncombo" %>


<%@ page import="java.util.Map,java.util.List,java.util.ListIterator"%>
<%@ page import="edu.wustl.common.beans.NameValueBean"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.actionForm.NewSpecimenForm"%>
<%@ page import="edu.wustl.catissuecore.util.global.Utility"%>
<%@ page import="java.util.*"%>
<%@ page import="edu.wustl.common.util.tag.ScriptGenerator" %>

<%@ include file="/pages/content/common/BioSpecimenCommonCode.jsp" %>
<%@ include file="/pages/content/common/AutocompleterCommon.jsp" %> 

<script src="jss/script.js"></script>
<script src="jss/calendarComponent.js"></script>
<SCRIPT>var imgsrc="images/";</SCRIPT>
<LINK href="css/calanderComponent.css" type=text/css rel=stylesheet>

<%
	//System.out.println("Start of specimen jsp");
	List biohazardList = (List)request.getAttribute(Constants.BIOHAZARD_TYPE_LIST);
	NewSpecimenForm form = (NewSpecimenForm)request.getAttribute("newSpecimenForm");
	String nodeId="";
	String exceedsMaxLimit = (String)request.getAttribute(Constants.EXCEEDS_MAX_LIMIT);
	String submittedFor=(String)request.getAttribute(Constants.SUBMITTED_FOR);
	boolean isAddNew = false;

	String operation = (String)request.getAttribute(Constants.OPERATION);
	String reqPath = (String)request.getAttribute(Constants.REQ_PATH);
	String appendingPath = "/NewSpecimen.do?operation=add&pageOf=pageOfNewSpecimen";

	String currentReceivedDate = "";
	String currentCollectionDate = "";
	if (form != null) 
	{
		currentReceivedDate = form.getReceivedEventDateOfEvent();
		if(currentReceivedDate == null)
				currentReceivedDate = "";
		currentCollectionDate = form.getCollectionEventdateOfEvent();
		if(currentCollectionDate == null)
				currentCollectionDate = "";
	}
	
	if (reqPath != null)
		appendingPath = reqPath + "|/NewSpecimen.do?operation=add&pageOf=pageOfNewSpecimen";
	
	   	if(!operation.equals(Constants.ADD) )
	   	{
	   		if(form != null)
	   		{
		   		appendingPath = "/NewSpecimenSearch.do?operation=search&pageOf=pageOfNewSpecimen&id="+form.getId() ;
		   		//System.out.println("---------- NSP JSP -------- : "+ appendingPath);				
		   	}
	   	}
	
	Map map = form.getExternalIdentifier();
%>
<head>

<script language="JavaScript" type="text/javascript" src="jss/Hashtable.js"></script>
<script language="JavaScript" type="text/javascript" src="jss/ajax.js"></script>

<%
	String[] columnList = (String[]) request.getAttribute(Constants.SPREADSHEET_COLUMN_LIST);
	List dataList = (List) request.getAttribute(Constants.SPREADSHEET_DATA_LIST);
	String pageOf = (String)request.getAttribute(Constants.PAGEOF);

	
	String formName,pageView=operation,editViewButton="buttons."+Constants.EDIT;
	boolean readOnlyValue=false,readOnlyForAll=false;

	if(operation.equals(Constants.EDIT))
	{
		editViewButton="buttons."+Constants.VIEW;
		formName = Constants.SPECIMEN_EDIT_ACTION;
		readOnlyValue=true;
		if(pageOf.equals(Constants.QUERY))
			formName = Constants.QUERY_SPECIMEN_EDIT_ACTION + "?pageOf="+pageOf;
		if(pageOf.equals(Constants.PAGE_OF_SPECIMEN_CP_QUERY))
		{
			formName = Constants.CP_QUERY_SPECIMEN_EDIT_ACTION + "?pageOf="+pageOf;
		}
		nodeId= "Specimen_"+form.getId();
	}
	else
	{
		formName = Constants.SPECIMEN_ADD_ACTION;
		readOnlyValue=false;
		if(pageOf.equals(Constants.PAGE_OF_SPECIMEN_CP_QUERY))
		{
			formName = Constants.CP_QUERY_SPECIMEN_ADD_ACTION + "?pageOf="+pageOf;
		}
		nodeId= "SpecimenCollectionGroup_"+form.getSpecimenCollectionGroupId();
	}

	String formNameForCal = "newSpecimenForm"; 
	
%>
<%@ include file="/pages/content/common/SpecimenCommonScripts.jsp" %>

	<%
	
	if(pageOf.equals(Constants.PAGE_OF_SPECIMEN_CP_QUERY))
	{
		strCheckStatus= "checkActivityStatus(this,'" + Constants.CP_QUERY_BIO_SPECIMEN + "')";
		%>
		<script language="javascript">
			refreshTree('<%=Constants.CP_AND_PARTICIPANT_VIEW%>','<%=Constants.CP_TREE_VIEW%>','<%=Constants.CP_SEARCH_CP_ID%>','<%=Constants.CP_SEARCH_PARTICIPANT_ID%>','<%=nodeId%>');					
		</script>
	<%}%>

<script language="JavaScript">
	function deleteExternalIdentifiers()
	{
	<%if(!pageOf.equals(Constants.PAGE_OF_SPECIMEN_CP_QUERY))
	{%>
		deleteChecked('addExternalIdentifier','NewSpecimen.do?operation=<%=operation%>&pageOf=pageOfNewSpecimen&status=true&button=deleteExId',document.forms[0].exIdCounter,'chk_ex_',false);
	<%} else {%>
		deleteChecked('addExternalIdentifier','CPQueryNewSpecimen.do?operation=<%=operation%>&pageOf=pageOfNewSpecimenCPQuery&status=true&button=deleteExId',document.forms[0].exIdCounter,'chk_ex_',false);
	<%}%>
		
	}
	function deleteBioHazards()
	{
	<%if(!pageOf.equals(Constants.PAGE_OF_SPECIMEN_CP_QUERY))
	{%>
		deleteChecked('addBiohazardRow','NewSpecimen.do?operation=<%=operation%>&pageOf=pageOfNewSpecimen&status=true&button=deleteBiohazard',document.forms[0].bhCounter,'chk_bio_',false);
	<%} else {%>
		deleteChecked('addBiohazardRow','CPQueryNewSpecimen.do?operation=<%=operation%>&pageOf=pageOfNewSpecimenCPQuery&status=true&button=deleteBiohazard',document.forms[0].bhCounter,'chk_bio_',false);
	<%}%>
		

	}
	
		function onCheckboxButtonClick(chkBox)
		{
			//var aliquotCountTextBox  = document.getElementById("noOfAliquots");
			//var qtyPerAliquotTextBox = document.getElementById("quantityPerAliquot");
			
			if(chkBox.checked)
			{
				//aliquotCountTextBox.disabled = false;
				//qtyPerAliquotTextBox.disabled = false;				
				document.forms[0].deriveButton.disabled=true;
				document.forms[0].moreButton.disabled=true;
				document.forms[0].submitAndDistributeButton.disabled=true;
				document.forms[0].noOfAliquots.disabled=false;
				document.forms[0].quantityPerAliquot.disabled=false;
								
			}
			else
			{
				//aliquotCountTextBox.disabled = true;
				//qtyPerAliquotTextBox.disabled = true;				
				document.forms[0].deriveButton.disabled=false;
				document.forms[0].moreButton.disabled=false;
				document.forms[0].submitAndDistributeButton.disabled=false;
				document.forms[0].noOfAliquots.disabled=true;
				document.forms[0].quantityPerAliquot.disabled=true;
			}
		}
		
		function onNormalSubmit()
		{
			var checked = false;
			if(document.forms[0].checkedButton != null)
			{
			   checked = document.forms[0].checkedButton.checked;
			}
			var operation = document.forms[0].operation.value;
			//Bug ID: 4040(Virender)
			if(checked)
			{
			<% String actionToCall = null;%>
				if(operation == "add")
				{
					setSubmittedFor('ForwardTo','pageOfCreateAliquot');
					<%
					actionToCall = "NewSpecimenAdd.do";
					if(pageOf.equals(Constants.PAGE_OF_SPECIMEN_CP_QUERY)){
					actionToCall = Constants.CP_QUERY_SPECIMEN_ADD_ACTION;

					}%>
					confirmDisable('<%=actionToCall%>',document.forms[0].activityStatus);
				}
				else
				{
				
					setSubmittedFor('ForwardTo','pageOfCreateAliquot');
					<%
					actionToCall = "NewSpecimenEdit.do";
					if(pageOf.equals(Constants.PAGE_OF_SPECIMEN_CP_QUERY)){
					actionToCall = Constants.CP_QUERY_SPECIMEN_EDIT_ACTION;

					}%>
					confirmDisable('<%=actionToCall%>',document.forms[0].activityStatus);
				}
			}
			else
			{
				if(operation == "add")
				{
					setSubmittedFor('null','success');
					<%
					actionToCall = "NewSpecimenAdd.do";
					if(pageOf.equals(Constants.PAGE_OF_SPECIMEN_CP_QUERY)){
					actionToCall = Constants.CP_QUERY_SPECIMEN_ADD_ACTION;

					}%>
					confirmDisable('<%=actionToCall%>',document.forms[0].activityStatus);
				}
				else
				{
					setSubmittedFor('null','success');
					<%
					actionToCall = "NewSpecimenEdit.do";
					if(pageOf.equals(Constants.PAGE_OF_SPECIMEN_CP_QUERY)){
					actionToCall = Constants.CP_QUERY_SPECIMEN_EDIT_ACTION;

					}%>
					
					confirmDisable('<%=actionToCall%>',document.forms[0].activityStatus);
				}
			}
			
		}

		function onCollOrClassChange()
		{
		   	var specimenCollGroupElement = document.getElementById("selectedSpecimenCollectionGroupId");
			var classNameElement = document.getElementById("className").value;
			classNameElement = trim(classNameElement);
			var classSet = false;
			if(classNameElement == "Fluid" || classNameElement == "Cell"||classNameElement == "Tissue"||classNameElement == "Molecular")
			{
			    classSet = true;
			}
		
			if(specimenCollGroupElement.value != "-1" && classSet)
			{
				<%
				String actionOnCollOrClassChange = "NewSpecimen.do?pageOf=pageOfNewSpecimen&virtualLocated=false";
				if(pageOf.equals(Constants.PAGE_OF_SPECIMEN_CP_QUERY))
				{
					actionOnCollOrClassChange = "CPQueryNewSpecimen.do?pageOf=pageOfNewSpecimenCPQuery&virtualLocated=false";
				}%>
				var action = "<%=actionOnCollOrClassChange%>";
				document.forms[0].action = action + "&onCollOrClassChange=true";
				document.forms[0].submit();
			}	
		}
		
		function setVirtuallyLocated(element)
		{
			var containerName = document.getElementById("customListBox_1_0");
			var pos1 = document.getElementById("customListBox_1_1");
			var pos2 = document.getElementById("customListBox_1_2");
			if(element.checked)
			{
				containerName.disabled = true;
				pos1.disabled = true;
				
				pos2.disabled = true;
				document.forms[0].mapButton.disabled = true;
			}
			else
			{
				onCollOrClassChange();
				/*containerName.disabled = false;
				pos1.disabled = false;;
				pos2.disabled = false;;
				document.forms[0].mapButton.disabled = false;*/
			}
		}
		function resetVirtualLocated()
		{			
			var radioArray = document.getElementsByName("stContSelection");	
			radioArray[0].checked= true;
			document.forms[0].selectedContainerName.disabled = true;
			document.forms[0].pos1.disabled = true;
			document.forms[0].pos2.disabled = true;
			document.forms[0].containerMap.disabled = true;

			document.forms[0].customListBox_1_0.disabled = true;
			document.forms[0].customListBox_1_1.disabled = true;
			document.forms[0].customListBox_1_2.disabled = true;
		}
		/**
 			* Name : Ashish Gupta
 			* Reviewer Name : Sachin Lale 
 			* Bug ID: 2741
 			* Patch ID: 2741_20 			
 			* Description: Function to make ajax call to server to get all events associated with the selected scg
			*/
		
	/**
	 *  This function updates the events for selected scg (Added by Ashish)
	 */
		var url,request;
		function getEventsFromSCG()
		{		
			var scgId = document.getElementById("selectedSpecimenCollectionGroupId").value;			
			url = "GetEventsFromScg.do?scgId="+scgId;
			sendRequestForEvents();	
		}
	
	
	
	/**
	 * This function sends 'GET' request to the server for updating quantity (Added by Ashish)
	 */
	function sendRequestForEvents()
	{
		request = newXMLHTTPReq();
		
		if(request)
		{  					
			request.onreadystatechange = handleResponseForEvents; 	
			try
			{		
				request.open("GET", url, true);
				request.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
				request.send("");
			}
			catch(e)
			{}			
		}
	}
	function handleResponseForEvents()
	{
		if(request.readyState == 4)
		{  
			//Response is ready
			if(request.status == 200)
			{
				/* Response contains required output.
				 * Get the response from server.
				 */				
				var responseString = request.responseText;
						
				var xmlDocument = getDocumentElementForXML(responseString); 
				
				var collectionUserId = xmlDocument.getElementsByTagName('CollectorId')[0].firstChild.nodeValue;	
				var collectorName = xmlDocument.getElementsByTagName('CollectorName')[0].firstChild.nodeValue;			
				var collectionDate = xmlDocument.getElementsByTagName('CollectionDate')[0].firstChild.nodeValue;
				var collectionTimeHrs = xmlDocument.getElementsByTagName('CollectionTimeHours')[0].firstChild.nodeValue;
				var collectionTimeMinutes = xmlDocument.getElementsByTagName('CollectionTimeMinutes')[0].firstChild.nodeValue;
				var collectionProcedure = xmlDocument.getElementsByTagName('CollectionProcedure')[0].firstChild.nodeValue;			
				var collectionContainer = xmlDocument.getElementsByTagName('CollectionContainer')[0].firstChild.nodeValue;
				
				var tempCollComments = xmlDocument.getElementsByTagName('CollectionComments')[0].firstChild;
				if(tempCollComments != null)
				{
					var collectionComments = tempCollComments.nodeValue;
					document.getElementById("collectionEventComments").value = collectionComments;
				}
				else
				{
					document.getElementById("collectionEventComments").value = " ";
				}				
				
				var receivedDate = xmlDocument.getElementsByTagName('ReceivedDate')[0].firstChild.nodeValue;
				var receivedUserId = xmlDocument.getElementsByTagName('ReceiverId')[0].firstChild.nodeValue;
				var receiverName = xmlDocument.getElementsByTagName('ReceiverName')[0].firstChild.nodeValue;
				var receivedTimeHrs = xmlDocument.getElementsByTagName('ReceivedTimeHours')[0].firstChild.nodeValue;
				var receivedTimeMinutes = xmlDocument.getElementsByTagName('ReceivedTimeMinutes')[0].firstChild.nodeValue;
				var receivedQuality = xmlDocument.getElementsByTagName('ReceivedQuality')[0].firstChild.nodeValue;
				var tempRecComments = xmlDocument.getElementsByTagName('ReceivedComments')[0].firstChild;
				
				if(tempRecComments != null)
				{
					var receivedComments = tempRecComments.nodeValue;
					document.getElementById("receivedEventComments").value = receivedComments;
				}
				else
				{
					document.getElementById("receivedEventComments").value = " ";
				}				
				
				document.getElementById("collectionEventdateOfEvent").value = collectionDate;
				document.getElementById("collectionEventUserId").value = collectionUserId;
				document.getElementById("displaycollectionEventUserId").value = collectorName;
				document.getElementById("displaycollectionEventTimeInHours").value = collectionTimeHrs;
				document.getElementById("displaycollectionEventTimeInMinutes").value = collectionTimeMinutes;
				document.getElementById("collectionEventCollectionProcedure").value = collectionProcedure;
			    document.getElementById("collectionEventContainer").value = collectionContainer;
			    
				var recDate = document.getElementById("receivedEventdateOfEvent");
				if(recDate != null)
				{
					recDate.value = receivedDate;
				}
				document.getElementById("receivedEventUserId").value = receivedUserId;
				document.getElementById("displayreceivedEventUserId").value = receiverName;
				document.getElementById("displayreceivedEventTimeInHours").value = receivedTimeHrs;
				document.getElementById("displayreceivedEventTimeInMinutes").value = receivedTimeMinutes;
				document.getElementById("receivedEventReceivedQuality").value = receivedQuality;
				
				
			}//End if(request.status == 200)
		}//End if(request.readyState == 4)	
	}
		
				     

	function getDocumentElementForXML(xmlString)
	{
	    var document = null;
	    if (window.ActiveXObject) // code for IE
	    {
	                document = new ActiveXObject("Microsoft.XMLDOM");
	                document.async="false";
	                document.loadXML(xmlString);
	    }
	    else // code for Mozilla, Firefox, Opera, etc.
	    {
	                var parser = new DOMParser();
	                document = parser.parseFromString(xmlString,"text/xml");
	    }           
	return document;
	}	
		
		
		function eventClicked()
		{		
               
			  // Clear the value of onSubmit 
		    document.forms[0].onSubmit.value="";
			var answer = confirm("Do you want to submit any changes?");
			var formName;
			<% String formNameAction = null;%>
			if (answer){
				setSubmittedFor('ForwardTo','eventParameters');
				<%
				formNameAction = "NewSpecimenEdit.do";
				if(pageOf.equals(Constants.PAGE_OF_SPECIMEN_CP_QUERY))
				{
					formNameAction = "CPQueryNewSpecimenEdit.do";
				}%>
				formName = "<%=formNameAction%>";
			}
			else{
				var id = document.forms[0].id.value;			
				<%
				formNameAction = "ListSpecimenEventParameters.do?pageOf=pageOfListSpecimenEventParameters";
				if(pageOf.equals(Constants.PAGE_OF_SPECIMEN_CP_QUERY))
				{
					formNameAction = "CPQueryListSpecimenEventParameters.do?pageOf=pageOfListSpecimenEventParametersCPQuery";
				}%>
						
				formName = "<%=formNameAction%>&specimenId="+id+"&menuSelected=15";				
			}			

			confirmDisable(formName,document.forms[0].activityStatus);
		}
			/**
			* Patch ID: Improve_Space_Usability_On_Specimen_Page_5
			* See also: 1-5
			* Description: Following function is provided to display tables (Collected/recieved Events table, external identifier table, biohazard table) in collapse style on new specimen page.
			*/   	
			window.onload=newSpecimenInit;
	</script>
</head>

<% 
		int exIdRows=1;
		int bhRows=1;

		String unitSpecimen = "";
		if(form != null)
		{
			exIdRows = form.getExIdCounter();
			bhRows	 = form.getBhCounter();
			if(form.getClassName().equals("Tissue"))
				{
					//Mandar : 25-Apr-06 :Bug 1414
					if((form.getType()!=null) && (form.getType().equals(Constants.FROZEN_TISSUE_SLIDE)||form.getType().equals(Constants.FIXED_TISSUE_BLOCK)||form.getType().equals(Constants.FROZEN_TISSUE_BLOCK)||form.getType().equals(Constants.NOT_SPECIFIED)||form.getType().equals(Constants.FIXED_TISSUE_SLIDE)))
					{
						unitSpecimen = Constants.UNIT_CN;
					}
					else if((form.getType()!=null) && (form.getType().equals(Constants.MICRODISSECTED)))
					{
						unitSpecimen = Constants.UNIT_CL;
					}
					else 
					{
						unitSpecimen = Constants.UNIT_GM;
					}
						
				}
				if(form.getClassName().equals("Fluid"))
				{
					unitSpecimen = Constants.UNIT_ML;
				}
				if(form.getClassName().equals("Cell"))
				{
					unitSpecimen = Constants.UNIT_CC;
				}
				if(form.getClassName().equals("Molecular"))
				{
					unitSpecimen = Constants.UNIT_MG;
				}
				
		}
		
		
		

%>


<html:form action="<%=Constants.SPECIMEN_ADD_ACTION%>">
	<html:errors />
	<%
				String normalSubmitFunctionName = "setSubmittedFor('" + submittedFor+ "','" + Constants.SPECIMEN_FORWARD_TO_LIST[0][1]+"')";
				String deriveNewSubmitFunctionName = "setSubmittedFor('ForwardTo','" + Constants.SPECIMEN_FORWARD_TO_LIST[1][1]+"')";									
				String addEventsSubmitFunctionName = "setSubmittedFor('ForwardTo','" + Constants.SPECIMEN_FORWARD_TO_LIST[2][1]+"')";
				String addMoreSubmitFunctionName = "setSubmittedFor('ForwardTo','" + Constants.SPECIMEN_FORWARD_TO_LIST[3][1]+"')";
				
				String confirmDisableFuncName = "confirmDisable('" + formName +"',document.forms[0].activityStatus)";
				
				String normalSubmit = normalSubmitFunctionName + ","+confirmDisableFuncName;
				String deriveNewSubmit = deriveNewSubmitFunctionName + ","+confirmDisableFuncName;
				String addEventsSubmit = addEventsSubmitFunctionName + ","+confirmDisableFuncName;
				String addMoreSubmit = addMoreSubmitFunctionName + ","+confirmDisableFuncName;		
				String submitAndDistribute = "setSubmittedFor('ForwardTo','" + Constants.SPECIMEN_FORWARD_TO_LIST[4][1]+"')," + confirmDisableFuncName;
				
				String specimenCollectionGroupId = (String)request.getAttribute("SpecimenCollectionGroupId");
				String specimenCollectionGroupName = (String)request.getAttribute("SpecimenCollectionGroupName");
	%>

	<%
	if(pageView.equals("edit"))
	{
	%>
		<table summary="" cellpadding="0" cellspacing="0" border="0" height="20" class="tabPage" width="600">
			<tr>
				<td height="20" class="tabMenuItemSelected">
					<bean:message key="tab.specimen.details"/>
				</td>
				<%
					String eventLinkAction = "'ListSpecimenEventParameters.do?pageOf=pageOfListSpecimenEventParameters&menuSelected=15&specimenId="+form.getId()+"'" ;
				%>
				<td height="20" class="tabMenuItem" onmouseover="changeMenuStyle(this,'tabMenuItemOver'),showCursor()" onmouseout="changeMenuStyle(this,'tabMenuItem'),hideCursor()" onclick="eventClicked();">
					<bean:message key="tab.specimen.eventparameters"/>
				</td>

				<td height="20" class="tabMenuItem" onmouseover="changeMenuStyle(this,'tabMenuItemOver'),showCursor()" onmouseout="changeMenuStyle(this,'tabMenuItem'),hideCursor()" onClick="featureNotSupported()">
					<bean:message key="edit.tab.surgicalpathologyreport"/>
				</td>
				
				<td height="20" class="tabMenuItem" onmouseover="changeMenuStyle(this,'tabMenuItemOver'),showCursor()" onmouseout="changeMenuStyle(this,'tabMenuItem'),hideCursor()" onClick="featureNotSupported()">
					<bean:message key="edit.tab.clinicalannotation"/>
				</td>

				<td width="450" class="tabMenuSeparator" colspan="2">&nbsp;</td>
			</tr>

			<tr>
				<td class="tabField" colspan="6">
	<%
	}
	%>

		<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="500">

			
			<!-- If operation is equal to edit or search but,the page is for query the identifier field is not shown -->
			   	
			  <!-- NEW SPECIMEN REGISTRATION BEGINS-->
	    	<tr>
			    <td width = "100%">
					<table summary="" cellpadding="3" cellspacing="0" border="0" width="100%">
						<tr>
							<td colspan="6">

								<html:messages id="messageKey" message="true" header="messages.header" footer="messages.footer">
									<%=messageKey%>
								</html:messages>
							</td>
						</tr>		
						<tr>
							<td>
								<html:hidden property="operation" value="<%=operation%>"/>
								<html:hidden property="submittedFor" value="<%=submittedFor%>"/>
								<html:hidden property="forwardTo" value=""/>
								<html:hidden property="virtuallyLocated"/>
								<html:hidden property="containerId" styleId="containerId"/>
							</td>
							<td>

							</td>
							<td>

							</td>
							<td>
								<html:hidden property="onSubmit"/>
							</td>
							<td>
								<html:hidden property="id"/>
								<html:hidden property="positionInStorageContainer" />
								<html:hidden property="parentPresent" />
							</td>
						</tr>				 
						<tr>
							<td class="formMessage" colspan="6">
								<bean:message key="requiredfield.message"/>  
							</td>
						</tr>				 
					</table>
				</td>
			</tr>
				
			<tr>
				<td>
					<table summary="" cellpadding="3" cellspacing="0" border="0" width="700">
				 
						<tr>
							<td class="formTitle" height="20" width="100%" colspan="6">
								<%String title = "specimen."+pageView+".title";%>
								<bean:message key="<%=title%>"/>    
							</td>
						</tr>

						<tr>
							<td class="formRequiredNotice" >*</td>
							<%
								String specimenColSpan;
								if(operation.equals(Constants.EDIT))
								{
									specimenColSpan="1";
								}
								else
								{
									specimenColSpan="4";
								}
							%>
							<!-- To remove horizontal line between label column and text box column label with out border is used.
							style class formRequiredLabel is replaced with formRequiredLableWithoutBorder and formLabel is replaced with formLableWithoutBorder.
							For this formLableWithoutBorder styleclass is added to css/styleSheet.jss file-->
							<logic:equal name="newSpecimenForm" property="parentPresent" value="false">
							<td class="formRequiredLabelWithoutBorder">
								<label for="specimenCollectionGroupName">
									<bean:message key="specimenCollectionGroup.groupName"/>
								</label>
							</td>							
							<td class="formField" colspan="<%=specimenColSpan%>">
								<!--
									Patch ID: Bug#3184_4
									Description: The following change shows read-only textbox on specimen page, if specimen is being added
									from specimen collection group page, otherwise combobox having names of specimen collection group is displayed.
								-->
								<html:select property="specimenCollectionGroupId" styleClass="formFieldSized15" styleId="selectedSpecimenCollectionGroupId" 
									size="1" onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)" onchange="resetVirtualLocated();getEventsFromSCG()">
								<%
									if((specimenCollectionGroupId != null && !specimenCollectionGroupId.equals("")) &&
										(specimenCollectionGroupName != null && !specimenCollectionGroupName.equals("")))
									{
								%>
										<html:option value="<%=specimenCollectionGroupId%>"><%=specimenCollectionGroupName%></html:option>		
								<%
									}
									else
									{
								%>
										<html:options collection="<%=Constants.SPECIMEN_COLLECTION_GROUP_LIST%>" labelProperty="name" property="value"/>
								<%
									}
								%>
								</html:select>
																
								&nbsp;
								<logic:notEqual name="<%=Constants.PAGEOF%>" value="<%=Constants.QUERY%>">
		   						<logic:notEqual name="<%=Constants.PAGEOF%>" value="<%=Constants.PAGE_OF_SPECIMEN_CP_QUERY%>">
								<html:link href="#" styleId="newUser" onclick="addNewAction('NewSpecimenAddNew.do?addNewForwardTo=specimenCollectionGroup&forwardTo=createNewSpecimen&addNewFor=specimenCollectionGroupId')">
									<bean:message key="buttons.addNew" />
								</html:link>					   
		   						</logic:notEqual>
		   						<logic:equal name="<%=Constants.PAGEOF%>" value="<%=Constants.PAGE_OF_SPECIMEN_CP_QUERY%>">
								<html:link href="#" styleId="newUser" onclick="addNewAction('NewSpecimenAddNew.do?addNewForwardTo=specimenCollectionGroupCPQuery&forwardTo=createNewSpecimen&addNewFor=specimenCollectionGroupId')">
									<bean:message key="buttons.addNew" />
								</html:link>					   
		   						</logic:equal>
		   						</logic:notEqual>
		   						
		   						
								 <!-- <a href="SpecimenCollectionGroup.do?operation=add&pageOf=pageOfSpecimenCollectionGroup">
									<bean:message key="app.addNew" />
									</a> 
								-->			
				        	</td>							
							</logic:equal>
		        	
							<logic:equal name="newSpecimenForm" property="parentPresent" value="true">
				        	<td class="formRequiredLabelWithoutBorder" >
								<label for="parentSpecimenId">
									<bean:message key="createSpecimen.parentLabel"/>
								</label>
							</td>
							
				        	<td class="formField" colspan="<%=specimenColSpan%>">
				        		<html:hidden property="specimenCollectionGroupId"/>
								<!-- Mandar : 434 : for tooltip -->
								<html:select property="parentSpecimenId" styleClass="formFieldSized10" styleId="parentSpecimenId" size="1" disabled="<%=readOnlyForAll%>"
								 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
									<html:options collection="<%=Constants.PARENT_SPECIMEN_ID_LIST%>" labelProperty="name" property="value"/>
								</html:select>
				        	</td>
							</logic:equal>	
							
							<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.EDIT%>">
							<td class="formRequiredNotice">*</td>
							<td class="formRequiredLabelWithoutBorder">
								<label for="lineage">
									<bean:message key="specimen.lineage"/>
								</label>
							</td>
							<td class="formField" >								
					     		<html:text styleClass="formFieldSized15" maxlength="10"  size="30" styleId="lineage" property="lineage" 
					     		readonly="true"/>														     	
							</td>
							</logic:equal>
							
						</tr>
						
						<tr>
							<td class="formRequiredNotice" >
						     	<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">*</logic:notEqual>
						     	<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">&nbsp;</logic:equal>
						    </td>
						    <td class="formRequiredLabelWithoutBorder" >
								<label for="label">
									<bean:message key="specimen.label"/>
								</label>
							</td>
						    <td class="formField" >
						     	<html:text styleClass="formFieldSized15" size="30" maxlength="255"  styleId="label" property="label" readonly="<%=readOnlyForAll%>"/>
						    </td>							
							<td class="formRequiredNotice" width="5">&nbsp;</td>
						    <td class="formLabelWithoutBorder">							
						    	<label for="barcode">
									<bean:message key="specimen.barcode"/>
								</label>								
							</td>
						    <td class="formField" >
								<html:text styleClass="formFieldSized15" maxlength="255"  size="30" styleId="barcode" property="barcode" readonly="<%=readOnlyForAll%>" />
				        	</td>
						</tr>
				 
						<tr>
						 	<td class="formRequiredNotice" width="5">*</td>
						    <td class="formRequiredLabelWithoutBorder">
						     	<label for="className">
						     		<bean:message key="specimen.type"/>
						     	</label>
						    </td>
						    <td class="formField">
							
								<%
									String classReadOnly = "false";
									if(operation.equals(Constants.EDIT))
									{
									    classReadOnly = "true";
									}
								%>
							    <autocomplete:AutoCompleteTag property="className"
										  optionsList = "<%=request.getAttribute(Constants.SPECIMEN_CLASS_LIST)%>"
										  initialValue="<%=form.getClassName()%>"
										  onChange="onTypeChange(this);resetVirtualLocated()"
										  readOnly="<%=classReadOnly%>"
									    />
				 			   
							
				        	</td>
						 
						    <td class="formRequiredNotice" width="5">*</td>
						    <td class="formRequiredLabelWithoutBorder">
						     	<label for="type">
						     		<bean:message key="specimen.subType"/>
						     	</label>
						    </td>				    
						    <td class="formField" >
						    <!-- --------------------------------------- -->
						    <%
										String classValue = (String)form.getClassName();
										specimenTypeList = (List)specimenTypeMap.get(classValue);
										
										boolean subListEnabled = false;
								
										if(specimenTypeList == null)
										{
											specimenTypeList = new ArrayList();
											specimenTypeList.add(new NameValueBean(Constants.SELECT_OPTION,"-1"));
										}
										
										if(Constants.ALIQUOT.equals(form.getLineage()))
										{
											specimenTypeList = new ArrayList();
											specimenTypeList.add(new NameValueBean(form.getType(),form.getType()));
										}
										
										pageContext.setAttribute(Constants.SPECIMEN_TYPE_LIST, specimenTypeList);
										
										String subTypeFunctionName ="onSubTypeChangeUnit('className',this,'unitSpan')"; 
										
										String readOnlyForAliquot = "false";
										if(Constants.ALIQUOT.equals(form.getLineage())&&operation.equals(Constants.EDIT)) {
										      readOnlyForAliquot = "true";
										}
							%>
						  
						   <autocomplete:AutoCompleteTag property="type"
										  optionsList = "<%=request.getAttribute(Constants.SPECIMEN_TYPE_MAP)%>"
										  initialValue="<%=form.getType()%>"
										  onChange="<%=subTypeFunctionName%>"
										  readOnly="<%=readOnlyForAliquot%>"
										  dependsOn="<%=form.getClassName()%>"
					        />
			
				        	</td>
						</tr>
				 
				 
						<tr>
						     <td class="formRequiredNotice" width="5">
						     	<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">*</logic:notEqual>
						     	<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">&nbsp;</logic:equal>
						     </td>
						     <td class="formRequiredLabelWithoutBorder">
								<label for="tissueSite">
									<bean:message key="specimen.tissueSite"/>
								</label>
							</td>
						     <td class="formField" >
					
                                       <autocomplete:AutoCompleteTag property="tissueSite"
										  size="150"
										  optionsList = "<%=request.getAttribute(Constants.TISSUE_SITE_LIST)%>"
										  initialValue="<%=form.getTissueSite()%>"
										  readOnly="<%=readOnlyForAliquot%>"
									    />
								
								<%
									String url = "ShowFramedPage.do?pageOf=pageOfTissueSite&propertyName=tissueSite&cdeName=Tissue Site";
								%>
								<!-- Patch ID: Bug#3090_19 -->
								<a href="#" onclick="javascript:NewWindow('<%=url%>','name','360','525','no');return false">
									<img src="images\Tree.gif" border="0" width="24" height="18" title='Tissue Site Selector'>
								</a>
				        	  </td>
						
						     <td class="formRequiredNotice" width="5">
						     	<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">*</logic:notEqual>
						     	<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">&nbsp;</logic:equal>
						     </td>
						     <td class="formRequiredLabelWithoutBorder">
								<label for="tissueSide">
									<bean:message key="specimen.tissueSide"/>
								</label>
							</td>
						     <td class="formField" >
							 
							  <autocomplete:AutoCompleteTag property="tissueSide"
										  optionsList = "<%=request.getAttribute(Constants.TISSUE_SIDE_LIST)%>"
										  initialValue="<%=form.getTissueSide()%>"
										  readOnly="<%=readOnlyForAliquot%>"
									    />

				        	  </td>
						</tr>
				 
				 
						<tr>
						    <td class="formRequiredNotice" width="5">
						     	<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">*</logic:notEqual>
						     	<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">&nbsp;</logic:equal>
						    </td>
						    <td class="formRequiredLabelWithoutBorder">
								<label for="pathologicalStatus">
									<bean:message key="specimen.pathologicalStatus"/>
								</label>
							</td>
							<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.EDIT%>">
						    <td colspan="4" class="formField" >
							
							<autocomplete:AutoCompleteTag property="pathologicalStatus"
										  optionsList = "<%=request.getAttribute(Constants.PATHOLOGICAL_STATUS_LIST)%>"
										  initialValue="<%=form.getPathologicalStatus()%>"
										  readOnly="<%=readOnlyForAliquot%>"
							/>
							
				        	</td>
							</logic:notEqual>
					     	
							<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.EDIT%>">
							<td class="formField" >
							<autocomplete:AutoCompleteTag property="pathologicalStatus"
										  optionsList = "<%=request.getAttribute(Constants.PATHOLOGICAL_STATUS_LIST)%>"
										  initialValue="<%=form.getPathologicalStatus()%>"
										  readOnly="<%=readOnlyForAliquot%>"
							/>
				        	</td>	
							<!-- activitystatus -->
							</logic:equal>

			
							 <!--
							 * Patch ID: 3835_1_22
							 * See also: 1_1 to 1_5
							 * Description : show CreatedOn date field if operation is edit.				 
							-->	 
					<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.EDIT%>">
							<td class="formRequiredNotice" width="5">&nbsp;</td>
						    <td class="formLabelWithoutBorder">							
						    	<label for="createdDate">
									<bean:message key="specimen.createdDate"/>
								</label>								
							</td>
						   <td class="formField" >
							<%								
								String createdDate = form.getCreatedDate();
								String nameOfForm ="newSpecimenForm";
								String dateFormName = "createdDate";
							%>
								<%@ include file="/pages/content/common/CommonDateComponent.jsp" %>
        				   </td>
        	     </logic:equal>




						</tr>
						
						<tr>					
					     	<td class="formRequiredNotice" width="5">
						     	<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">*</logic:notEqual>
						     	<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">&nbsp;</logic:equal>
						    </td>
						    <td class="formRequiredLabelWithoutBorder">
								<label for="quantity">
									<bean:message key="specimen.quantity"/>
								</label>
							</td>
						    <td class="formField" >
						     	<html:text styleClass="formFieldSized15" size="30" maxlength="10"  styleId="quantity" property="quantity" readonly="<%=readOnlyForAll%>"/>
						     	<span id="unitSpan"><%=unitSpecimen%></span>
						     	<html:hidden property="unit"/>
						    </td>
							<td class="formRequiredNotice" width="5">
						     	&nbsp;
						    </td>
						    <td class="formLabelWithoutBorder">
								<label for="concentration">
									<bean:message key="specimen.concentration"/>
								</label>
							</td>
							<td class="formField" >
							<%
								boolean concentrationDisabled = true;
								
								if(form.getClassName().equals("Molecular") && !Constants.ALIQUOT.equals(form.getLineage()))
									concentrationDisabled = false;
							%>
					     		<html:text styleClass="formFieldSized15" maxlength="10"  size="30" styleId="concentration" property="concentration" 
					     		readonly="<%=readOnlyForAll%>" disabled="<%=concentrationDisabled%>"/>
								&nbsp;<bean:message key="specimen.concentrationUnit"/>
							</td>
						</tr>
				 
						<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.EDIT%>">
						
						<tr>
							<!-- Available -->
							<td class="formRequiredNotice" width="5">&nbsp;</td>
							<td class="formLabelWithoutBorder">
								<label for="available">
									<bean:message key="specimen.available" />
								</label>
							</td>
							<td class="formField">
								<html:checkbox property="available">
								</html:checkbox>
							</td>	
							<!-- Available Quantity -->							
							<td class="formRequiredNotice" width="5">&nbsp;</td>
							<td class="formLabelWithoutBorder" >
								<label for="availableQuantity">
									<bean:message key="specimen.availableQuantity" />
								</label>
							</td>
							<td class="formField">
								<html:text styleClass="formFieldSized15" maxlength="10"  size="30" styleId="availableQuantity" property="availableQuantity" readonly="true" />
								<span id="unitSpan1"><%=unitSpecimen%></span>
							</td>
						</tr>
						
						</logic:equal>						 
						
						<tr>
						 	<td class="formRequiredNotice" width="5">*</td>
							<td class="formRequiredLabelWithoutBorder">
							   <label for="className">
							   		<bean:message key="specimen.positionInStorageContainer"/>
							   </label>
							</td>
							
							<%
								boolean readOnly=true;
								if(operation.equals(Constants.ADD))
									readOnly=false;
							%>
							
							<%-- n-combo-box start --%>
							<%
								Map dataMap = (Map) request.getAttribute(Constants.AVAILABLE_CONTAINER_MAP);
													
								String[] labelNames = {"ID","Pos1","Pos2"};
								labelNames = Constants.STORAGE_CONTAINER_LABEL;
								String[] attrNames = { "storageContainer", "positionDimensionOne", "positionDimensionTwo"};
					            String[] tdStyleClassArray = { "formFieldSized15", "customFormField", "customFormField"}; 
								//String[] initValues = new String[3];
								//initValues[0] = form.getStorageContainer();
								//initValues[1] = form.getPositionDimensionOne();
								//initValues[2] = form.getPositionDimensionTwo();
								String[] initValues = new String[3];
								List initValuesList = (List)request.getAttribute("initValues");
								if(initValuesList != null)
								{
									initValues = (String[])initValuesList.get(0);
								}
								//System.out.println("NewSpecimen :: "+initValues[0]+"<>"+initValues[1]+"<>"+initValues[2]);			
								String rowNumber = "1";
								String styClass = "formFieldSized5";
								String tdStyleClass = "customFormField";
								String onChange = "onCustomListBoxChange(this)";
								String className = form.getClassName();
								String collectionProtocolId =(String) request.getAttribute(Constants.COLLECTION_PROTOCOL_ID);
								if (collectionProtocolId==null)
									collectionProtocolId="";
								if (className==null)
									className="";
								String frameUrl = "ShowFramedPage.do?pageOf=pageOfSpecimen&amp;selectedContainerName=selectedContainerName&amp;pos1=pos1&amp;pos2=pos2&amp;containerId=containerId"
									+ "&" + Constants.CAN_HOLD_SPECIMEN_CLASS+"="+className
									+ "&" + Constants.CAN_HOLD_COLLECTION_PROTOCOL +"=" + collectionProtocolId;
								System.out.println(frameUrl);
								String buttonOnClicked = "mapButtonClickedOnSpecimen('"+frameUrl+"')";  
								
								//"javascript:NewWindow('"+frameUrl+"','name','800','600','no');return false"; 
								//javascript:NewWindow('"+frameUrl+"','name','800','600','no');return false";
								String noOfEmptyCombos = "3";

								boolean disabled = false;
								boolean buttonDisabled = false;
								if(request.getAttribute("disabled") != null && request.getAttribute("disabled").equals("true"))
								{
									disabled = true;
								}	
								 int radioSelected = form.getStContSelection();
								boolean dropDownDisable = false;
								boolean textBoxDisable = false;
								
								if(radioSelected == 1)
								{
									dropDownDisable = true;
									textBoxDisable = true;
								}
								else if(radioSelected == 2)
								{									
									textBoxDisable = true;
								}
								else if(radioSelected == 3)
								{
									dropDownDisable = true;									
								}
								
								
							%>
				
							<%=ScriptGenerator.getJSForOutermostDataTable()%>
							<%//System.out.println("after getJSForOutermostDataTable in specimen jsp");%>
							<%=ScriptGenerator.getJSEquivalentFor(dataMap,rowNumber)%>
							<%//System.out.println("after getJSEquivalentFor in specimen jsp");%>
				
							<script language="JavaScript" type="text/javascript" src="jss/CustomListBox.js"></script>
				
							<td class="formField" colSpan="4">
							<table border="0">
								<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.ADD%>">
								<tr>
									<td ><html:radio value="1" onclick="onRadioButtonGroupClick(this)" styleId="stContSelection" property="stContSelection"/></td>
									<td class="formFieldNoBorders">																			
											<bean:message key="specimen.virtuallyLocated" />											
									</td>
								</tr>
								<tr>
									<td ><html:radio value="2" onclick="onRadioButtonGroupClick(this)" styleId="stContSelection" property="stContSelection"/></td>
									<td>
										<ncombo:nlevelcombo dataMap="<%=dataMap%>" 
											attributeNames="<%=attrNames%>" 
											tdStyleClassArray="<%=tdStyleClassArray%>"
											initialValues="<%=initValues%>"  
											styleClass = "<%=styClass%>" 
											tdStyleClass = "<%=tdStyleClass%>" 
											labelNames="<%=labelNames%>" 
											rowNumber="<%=rowNumber%>" 
											onChange = "<%=onChange%>"
											formLabelStyle="formLabelBorderless"
											disabled = "<%=dropDownDisable%>"
											noOfEmptyCombos = "<%=noOfEmptyCombos%>"/>
											</tr>
											</table>
									</td>
								</tr>
								<tr>
									<td ><html:radio value="3" onclick="onRadioButtonGroupClick(this)" styleId="stContSelection" property="stContSelection"/></td>
									<td class="formLabelBorderlessLeft">
										<html:text styleClass="formFieldSized10"  size="30" styleId="selectedContainerName" property="selectedContainerName" disabled= "<%=textBoxDisable%>"/>
										<html:text styleClass="formFieldSized3"  size="5" styleId="pos1" property="pos1" disabled= "<%=textBoxDisable%>"/>
										<html:text styleClass="formFieldSized3"  size="5" styleId="pos2" property="pos2" disabled= "<%=textBoxDisable%>"/>
										<html:button styleClass="actionButton" property="containerMap" onclick="<%=buttonOnClicked%>" disabled= "<%=textBoxDisable%>">
											<bean:message key="buttons.map"/>
										</html:button>
									</td>
								</tr>
								</logic:equal>								
								
								<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.ADD%>">								
								
								<%
									
									NewSpecimenForm newSpecimenForm = (NewSpecimenForm) request.getAttribute("newSpecimenForm");
								
									if(newSpecimenForm.getStContSelection() == 1)
									{%>Specimen is virtually Located <%}									
									else
									{
									%>
										<tr>											
											<td class="formLabelBorderless">
												<html:text styleClass="formFieldSized10"  size="30" styleId="selectedContainerName" property="selectedContainerName" readonly= "true"/>
												<html:text styleClass="formFieldSized3"  size="5" styleId="positionDimensionOne" property="positionDimensionOne" readonly= "true"/>
												<html:text styleClass="formFieldSized3"  size="5" styleId="positionDimensionTwo" property="positionDimensionTwo" readonly= "true"/>
												<html:button styleClass="actionButton" property="containerMap" onclick="<%=buttonOnClicked%>" disabled= "true">
													<bean:message key="buttons.map"/>
												</html:button>
											</td>
										</tr>
									<%
									}
									
								%>
								</logic:notEqual>	
								
								
								
								
							</table>

							</td>
							<%//System.out.println("End of tag in jsp");%>
				<%-- n-combo-box end --%>
				</tr>

					<!--%System.out.println("Inside if condition in jsp"+exceedsMaxLimit);
					if(exceedsMaxLimit!=null && exceedsMaxLimit.equals("true")){
						%-->
					<logic:equal name="exceedsMaxLimit" value="true">
					<tr>
						<td>
								<bean:message key="container.maxView"/>
						</td>
					</tr>
					</logic:equal>
					<!--%}%-->				 				 
						<tr>
					     	<td class="formRequiredNotice" width="5">&nbsp;</td>
						    <td class="formLabelWithoutBorder">
								<label for="comments">
									<bean:message key="specimen.comments"/>
								</label>
							</td>

						 <!-- 
										 * Patch ID: 3835_1_23
										 * See also: 1_1 to 1_5
										 * Description : just manipulated the TD according to operation	
										 * and activity status is made after commnets.		 
							-->	 

							<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.EDIT%>">
						    <td class="formField" >
							</logic:equal>

							<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.ADD%>">
							<td class="formField" colspan="4">
							</logic:equal>

						    	<html:textarea styleClass="formFieldSized"  rows="3" styleId="comments" property="comments" readonly="<%=readOnlyForAll%>"/>
						    </td>


							<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.EDIT%>">
							<td class="formRequiredNotice" width="5">*</td>
							<td class="formRequiredLabel" >
								<label for="activityStatus">
									<bean:message key="participant.activityStatus" />
								</label>
							</td>
							<td class="formField">
							
							<autocomplete:AutoCompleteTag property="activityStatus"
										  optionsList = "<%=request.getAttribute(Constants.ACTIVITYSTATUSLIST)%>"
										  initialValue="<%=form.getActivityStatus()%>"
										  onChange="<%=strCheckStatus%>"
							/>
							
							</td>					
							</logic:equal>

							<!--End -->

						</tr>
				</table>	
				
				
				<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.ADD%>">
				<%@ include file="CollAndRecEvents.jsp" %>
				</logic:equal>
				<%@ include file="ExternalIdentifiers.jsp" %>

 				<%@ include file="BioHazards.jsp" %>
 				<table summary="" cellpadding="3" cellspacing="0" border="0" width="100%">
				<logic:notEqual name="<%=Constants.PAGEOF%>" value="<%=Constants.QUERY%>">				 			
				<tr>					
					<td class="formFieldNoBordersBold" height="20" colspan="5">
						<html:checkbox property="checkedButton" onclick="onCheckboxButtonClick(this)">
							&nbsp; <bean:message key="specimen.aliquot.message"/>
						</html:checkbox>
						
						
						&nbsp;&nbsp;&nbsp;
		                <bean:message key="aliquots.noOfAliquots"/>
	                    &nbsp;
                        <input type="text" id="noOfAliquots"
				        name="noOfAliquots" class = "formFieldSized5" disabled="true"
				         />						
						&nbsp;&nbsp;&nbsp;
		                <bean:message key="aliquots.qtyPerAliquot"/>
	                    &nbsp;
                        <input type="text" id="quantityPerAliquot"
				        name="quantityPerAliquot" class = "formFieldSized5" disabled="true"
				         />
						
				    </td>
				</tr>								
				</logic:notEqual>
				 
				 <!-- Bio-hazards End here -->	
			   	 	<tr>
				  		<td align="left" colspan="6">
							<%
								String changeAction = "setFormAction('"+formName+"')";
				 			%>
							<%@ include file="NewSpecimenPageButtons.jsp" %>
				  		</td>
				 	</tr>
	</table>
  </td>
</tr>
<!-- NEW SPECIMEN REGISTRATION ends-->
</table>
<%
if(pageView.equals("edit"))
{
%>
		</td>
	</tr>
</table>
<%
}
%>
<html:hidden property="stContSelection"/>
<html:hidden property="lineage"/>
<!--
	Patch ID: Bug#3184_8
-->
<html:hidden property="restrictSCGCheckbox"/>
</html:form>