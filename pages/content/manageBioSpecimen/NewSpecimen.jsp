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
<%@ page import="edu.wustl.catissuecore.bean.ConsentBean"%>
<%@ page import="edu.wustl.catissuecore.util.global.Variables"%>
<%@ page import="edu.wustl.catissuecore.action.annotations.AnnotationConstants"%>
<%@ page import="edu.wustl.catissuecore.util.CatissueCoreCacheManager"%>
<%@ page import="edu.wustl.catissuecore.bizlogic.AnnotationUtil"%>

<%@ include file="/pages/content/common/BioSpecimenCommonCode.jsp" %>
<%@ include file="/pages/content/common/AutocompleterCommon.jsp" %> 

<script src="jss/script.js"></script>
<script src="jss/calendarComponent.js"></script>
<SCRIPT>var imgsrc="images/";</SCRIPT>
<LINK href="css/calanderComponent.css" type=text/css rel=stylesheet>

<%
	List biohazardList = (List)request.getAttribute(Constants.BIOHAZARD_TYPE_LIST);
	NewSpecimenForm form = (NewSpecimenForm)request.getAttribute("newSpecimenForm");
	String frdTo = form.getForwardTo();
	String nodeId="";
	String tab = (String)request.getAttribute(Constants.SELECTED_TAB);
	String exceedsMaxLimit = (String)request.getAttribute(Constants.EXCEEDS_MAX_LIMIT);
	String submittedFor=(String)request.getAttribute(Constants.SUBMITTED_FOR);
	boolean isAddNew = false;
	String signedConsentDate = "";
	String selectProperty="";
	String operation = (String)request.getAttribute(Constants.OPERATION);
	String reqPath = (String)request.getAttribute(Constants.REQ_PATH);
	String appendingPath = "/NewSpecimen.do?operation=add&pageOf=pageOfNewSpecimen";
	String currentReceivedDate = "";
	String currentCollectionDate = "";

	String staticEntityName=null;
	staticEntityName = AnnotationConstants.ENTITY_NAME_SPECIMEN;	
	Long specimenEntityId = null;
	if (CatissueCoreCacheManager.getInstance().getObjectFromCache("specimenEntityId") != null)
	{
		specimenEntityId = (Long) CatissueCoreCacheManager.getInstance().getObjectFromCache("specimenEntityId");
	}
	else
	{
		specimenEntityId = AnnotationUtil.getEntityId(AnnotationConstants.ENTITY_NAME_SPECIMEN);
		CatissueCoreCacheManager.getInstance().addObjectToCache("specimenEntityId",specimenEntityId);		
	}	
	
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
<style>
	.hidden
	{
	 display:none;
	}
</style>

<script language="JavaScript" type="text/javascript" src="jss/Hashtable.js"></script>
<script language="JavaScript" type="text/javascript" src="jss/ajax.js"></script>

<%
	String[] columnList = (String[]) request.getAttribute(Constants.SPREADSHEET_COLUMN_LIST);
	List dataList = (List) request.getAttribute(Constants.SPREADSHEET_DATA_LIST);
	String pageOf = (String)request.getAttribute(Constants.PAGEOF);

	
	String formName,pageView=operation,editViewButton="buttons."+Constants.EDIT;
	boolean readOnlyValue=false,readOnlyForAll=true;

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
	
	String refreshTree = (String)request.getAttribute("refresh");
	
	if(pageOf.equals(Constants.PAGE_OF_SPECIMEN_CP_QUERY) && (refreshTree==null || !(refreshTree.equalsIgnoreCase("false"))))
	{
		strCheckStatus= "checkActivityStatus(this,'" + Constants.CP_QUERY_BIO_SPECIMEN + "')";
	%>
		<script language="javascript">
		
	refreshTree('<%=Constants.CP_AND_PARTICIPANT_VIEW%>','<%=Constants.CP_TREE_VIEW%>','<%=Constants.CP_SEARCH_CP_ID%>','<%=Constants.CP_SEARCH_PARTICIPANT_ID%>','<%=nodeId%>');					
		</script>
	<%}
		
%>

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
			var aliquotCountTextBox  = document.getElementById("noOfAliquots");
			var qtyPerAliquotTextBox = document.getElementById("quantityPerAliquot");
			
			if(chkBox.checked)
			{
				aliquotCountTextBox.disabled = false;
				qtyPerAliquotTextBox.disabled = false;				
				document.forms[0].deriveButton.disabled=true;
				document.forms[0].moreButton.disabled=true;				
				document.forms[0].noOfAliquots.disabled=false;
				document.forms[0].quantityPerAliquot.disabled=false;
								
			}
			else
			{
				aliquotCountTextBox.disabled = true;
				qtyPerAliquotTextBox.disabled = true;				
				document.forms[0].deriveButton.disabled=false;
				document.forms[0].moreButton.disabled=false;				
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
           
			var temp = "<%=frdTo%>";
			//Bug ID: 4040(Virender)
			if(checked)
			{
			<% String actionToCall = null;%>
				if(operation == "add")
				{
				  if(temp == "orderDetails")
					{
						setSubmitted('ForwardTo','CPQueryPrintSpecimenAdd','orderDetails');
					}
					else
					{
					    setSubmitted('ForwardTo','CPQueryPrintSpecimenAdd','pageOfCreateAliquot');
                    }
					<%
					actionToCall = "NewSpecimenAdd.do";
					if(pageOf.equals(Constants.PAGE_OF_SPECIMEN_CP_QUERY))
					{
					  	actionToCall = Constants.CP_QUERY_SPECIMEN_ADD_ACTION;
					}%>
					
					confirmDisable('<%=actionToCall%>',document.forms[0].activityStatus);
				}
				else
				{
					if(temp == "orderDetails")
					{
						setSubmitted('ForwardTo','CPQueryPrintSpecimenEdit','orderDetails');
					}
					else
					{
						setSubmitted('ForwardTo','CPQueryPrintSpecimenEdit','pageOfCreateAliquot');
					}
					<%
					actionToCall = "NewSpecimenEdit.do";
					if(pageOf.equals(Constants.PAGE_OF_SPECIMEN_CP_QUERY))
					{
						actionToCall = Constants.CP_QUERY_SPECIMEN_EDIT_ACTION;
                    
					}%>
					
					confirmDisable('<%=actionToCall%>',document.forms[0].activityStatus);
				}
			}
			else
			{
				if(operation == "add")
				{
					if(temp == "orderDetails")
					{
					   	setSubmitted('ForwardTo','CPQueryPrintSpecimenAdd','orderDetails');
					}
					else
					{

					setSubmitted('null','CPQueryPrintSpecimenAdd','success');
					}
					<%
					actionToCall = "NewSpecimenAdd.do";
					if(pageOf.equals(Constants.PAGE_OF_SPECIMEN_CP_QUERY))
					{
						actionToCall = Constants.CP_QUERY_SPECIMEN_ADD_ACTION;
					}%>
					if(document.forms[0].nextForwardTo.value!=null)
					{
					 confirmDisable('<%=actionToCall%>'+"?nextForwardTo="+document.forms[0].nextForwardTo.value,document.forms[0].activityStatus);
					}
					else
					{
						confirmDisable('<%=actionToCall%>',document.forms[0].activityStatus);
					}
					 
				}
				else
				{
					if(temp == "orderDetails")
					{
						setSubmitted('ForwardTo','CPQueryPrintSpecimenEdit','orderDetails');
					}
					else
					{
					setSubmitted('null','CPQueryPrintSpecimenEdit','success');
					}
					<%
					actionToCall = "NewSpecimenEdit.do";
					if(pageOf.equals(Constants.PAGE_OF_SPECIMEN_CP_QUERY))
					{
						
						actionToCall = Constants.CP_QUERY_SPECIMEN_EDIT_ACTION;

					}%>
					confirmDisable('<%=actionToCall%>',document.forms[0].activityStatus);
				}
			}
			
		}

		function onCollOrClassChange(element)
		{			
			var specimenCollGroupElement = document.getElementById("specimenCollectionGroupName");
			var classNameElement = document.getElementById("className").value;
			classNameElement = trim(classNameElement);
			var classSet = false;
			if(classNameElement == "Fluid" || classNameElement == "Cell"||classNameElement == "Tissue"||classNameElement == "Molecular")
			{
			    classSet = true;
			}
			var value;
			
			if(specimenCollGroupElement.value != "-1" && classSet)
			{
				if(element=='1')
				{
					value=true;
				}
				else
				{
					value=false;						
				}
				<%
				String actionOnCollOrClassChange = "NewSpecimen.do?pageOf=pageOfNewSpecimen&virtualLocated=false&tab=newSpecimenForm&showConsents=yes&tableId4=disable";
				System.out.println(actionOnCollOrClassChange);
				if(pageOf.equals(Constants.PAGE_OF_SPECIMEN_CP_QUERY))
				{
					actionOnCollOrClassChange = "CPQueryNewSpecimen.do?pageOf=pageOfNewSpecimenCPQuery&virtualLocated=false";
				}%>
				var action = "<%=actionOnCollOrClassChange%>"+"&value="+value;
				document.forms[0].action = action + "&onCollOrClassChange=true";
				document.forms[0].submit();
			}	
		}
		function onCollectionGroupChange(element)
		{
			var specimenCollGroupElement = document.getElementById("specimenCollectionGroupName");
			if(specimenCollGroupElement.value != "-1")
			{
				if(element=='1')
				{
					value=true;
				}
				else
				{
					value=false;						
				}
				<%
				actionOnCollOrClassChange = "NewSpecimen.do?pageOf=pageOfNewSpecimen&virtualLocated=false&tab=newSpecimenForm&showConsents=yes&tableId4=disable";
				if(pageOf.equals(Constants.PAGE_OF_SPECIMEN_CP_QUERY))
				{
					actionOnCollOrClassChange = "CPQueryNewSpecimen.do?pageOf=pageOfNewSpecimenCPQuery&virtualLocated=false";
				}%>
				var action = "<%=actionOnCollOrClassChange%>"+"&value="+value+"&scgName="+specimenCollGroupElement.value;
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
			document.forms[0].selectedContainerName.readonly = true;
			if(document.forms[0].pos1 != null)
				document.forms[0].pos1.disabled = true;
			if(document.forms[0].pos2 != null)
				document.forms[0].pos2.disabled = true;
			if(document.forms[0].containerMap != null)
				document.forms[0].containerMap.disabled = true;
			if(document.forms[0].customListBox_1_0 != null)
				document.forms[0].customListBox_1_0.disabled = true;
			if(document.forms[0].customListBox_1_1 != null)
				document.forms[0].customListBox_1_1.disabled = true;
			if(document.forms[0].customListBox_1_2 != null)
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
			var scgId = document.getElementById("specimenCollectionGroupName").value;			
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
				/* Bug Id: 4138 */
				if(responseString != null && responseString != "")
				{		
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
					if(collectionUserId != null)
					{
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
					}
				}
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
		    var consentTier=document.forms[0].consentTierCounter.value;
			var answer = confirm("Do you want to submit any changes?");
			var formName;
			<% String formNameAction = null;%>
			if (answer){
				setSubmitted('ForwardTo','CPQueryPrintSpecimenEdit','eventParameters');
				<%
				formNameAction = "NewSpecimenEdit.do";
				if(pageOf.equals(Constants.PAGE_OF_SPECIMEN_CP_QUERY))
				{
					formNameAction = "CPQueryNewSpecimenEdit.do";
				}%>
				formName = "<%=formNameAction%>?consentTierCounter="+consentTier;
			}
			else{
				var id = document.forms[0].id.value;			
				var label = document.getElementById("label").value;
				
				<%
				formNameAction = "ListSpecimenEventParameters.do?pageOf=pageOfListSpecimenEventParameters";
				if(pageOf.equals(Constants.PAGE_OF_SPECIMEN_CP_QUERY))
				{
					formNameAction = "CPQueryListSpecimenEventParameters.do?pageOf=pageOfListSpecimenEventParametersCPQuery";
				}%>
						
				formName = "<%=formNameAction%>&specimenId="+id+"&menuSelected=15&consentTierCounter="+consentTier;
			}			

			 
			confirmDisable(formName,document.forms[0].activityStatus);
		}
        
    // Consent Tracking Module Virender mehta	
	function switchToTab(selectedTab)
	{
		var operation = document.forms[0].operation.value;
		var displayKey="block";
		var showAlways="block";
		if(!document.all)
		{
			displayKey="table";
			showAlways="table";
		}
			
		var displayTable=displayKey;
		var tabSelected="none";
		if(selectedTab=="newSpecimenTab")
		{
			tabSelected=displayKey;
			displayTable="none";
		}	
		if(operation=="add")
		{
			var display4=document.getElementById('collectionEvent');
			display4.style.display=tabSelected;
		}
		var display=document.getElementById('addSpecimen');
		display.style.display=tabSelected;
		
		var display2=document.getElementById('externalIdentifiersTable'); 
		display2.style.display=tabSelected;
			
		var display3=document.getElementById('bioHazardsTable'); 
		display3.style.display=tabSelected;
	
		var displayConsentTable=document.getElementById('consentTable');
		if(displayConsentTable!=null)
		{
			displayConsentTable.style.display=displayTable;
		}
		
		var display5=document.getElementById('specimenPageButton');
		display5.style.display=showAlways;
				
		var aliquotTable=document.getElementById('aliquotId');
		aliquotTable.style.display=tabSelected;
				
		var collectionTab=document.getElementById('newSpecimenTab');
		var consentTab=document.getElementById('consentTab');
		
		if(selectedTab=="newSpecimenTab")
		{
			updateTab(newSpecimenTab,consentTab);
		}
		else		
		{
			updateTab(consentTab,newSpecimenTab);
		}
		
	}
	
	//This function is for changing the behaviour of TABs
	function updateTab(tab1, tab2)
	{
		tab1.onmouseover=null;
		tab1.onmouseout=null;
		tab1.className="tabMenuItemSelected";
	
		tab2.className="tabMenuItem";
		tab2.onmouseover=function() { changeMenuStyle(this,'tabMenuItemOver'),showCursor();};
		tab2.onmouseout=function() {changeMenuStyle(this,'tabMenuItem'),hideCursor();};
	}

		//This function will Switch tab to newSpecimen page
		function newspecimenPage()
		{
			switchToTab("newSpecimenTab");
		}
	
		//This function will switch page to consentPage
		function consentPage()
		{	
			checkForConsents();
		}
		
		function checkForConsents()
		{
			<%
				if(form.getConsentTierCounter()>0)			
				{
				%>
					switchToTab("consentTab");
				<%
				}
				else
				{
				%>
					alert("No consents available for selected Specimen Collection Group");
				<%
				}
				%>
		}

	  function showConsents()
	  {
		var showConsents = "<%=tab%>";
		if(showConsents=="<%=Constants.NULL%>" || showConsents=="specimen" || showConsents=="<%=Constants.NEWSPECIMEN_FORM%>")
		{
			newspecimenPage();
		}
		else
		{
			consentPage();			
		}
	  }
// Consent Tracking Module Virender mehta	  
	 	//View SPR Vijay pande
		function viewSPR()
		{
			<% Long reportId=(Long)session.getAttribute(Constants.IDENTIFIED_REPORT_ID); %>
			var reportId='<%=reportId%>';
			if(reportId==null || reportId==-1)
			{
				alert("There is no associate report in the system!");
			}
			else
			{
		    	var action="<%=Constants.VIEW_SPR_ACTION%>?operation=viewSPR&pageOf=<%=pageOf%>&id="+reportId;
				document.forms[0].action=action;
				document.forms[0].submit();
			}
		}

		function setSubmitted(forwardTo,printaction,nextforwardTo)
		{
				
			var printFlag = document.getElementById("printCheckbox");

			if(printFlag.checked)
			{
		
			  setSubmittedForPrint(forwardTo,printaction,nextforwardTo);
			}
			else
			{
			  setSubmittedFor(forwardTo,nextforwardTo);
			}
		
		}
	</script>
</head>
<body onload="newSpecimenInit();showConsents();">
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
		
				String printAction = "CPQueryPrintSpecimenAdd";
				if(operation.equals(Constants.EDIT))
				{
					 printAction = "CPQueryPrintSpecimenEdit";
				}
				
				String normalSubmitFunctionName = "setSubmitted('" + submittedFor+ "','"+printAction+"','" + Constants.SPECIMEN_FORWARD_TO_LIST[0][1]+"')";
				String deriveNewSubmitFunctionName = "setSubmitted('ForwardTo','"+printAction+"','" + Constants.SPECIMEN_FORWARD_TO_LIST[1][1]+"')";						
				String addEventsSubmitFunctionName = "setSubmitted('ForwardTo','"+printAction+"','" + Constants.SPECIMEN_FORWARD_TO_LIST[2][1]+"')";
				String addMoreSubmitFunctionName = "setSubmitted('ForwardTo','"+printAction+"','" + Constants.SPECIMEN_FORWARD_TO_LIST[3][1]+"')";				
				String confirmDisableFuncName = "confirmDisable('" + formName +"',document.forms[0].activityStatus)";
				
				String normalSubmit = normalSubmitFunctionName + ","+confirmDisableFuncName;
				String deriveNewSubmit = deriveNewSubmitFunctionName + ","+confirmDisableFuncName;
				String addEventsSubmit = addEventsSubmitFunctionName + ","+confirmDisableFuncName;
				String addMoreSubmit = addMoreSubmitFunctionName + ","+confirmDisableFuncName;		
				String submitAndDistribute = "setSubmitted('ForwardTo','"+printAction+"','" + Constants.SPECIMEN_FORWARD_TO_LIST[4][1]+"')," + confirmDisableFuncName;
				
				String specimenCollectionGroupId = (String)request.getAttribute("SpecimenCollectionGroupId");
				String specimenCollectionGroupName = (String)request.getAttribute("SpecimenCollectionGroupName");
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
								<%						
								if(form.getForwardTo().equalsIgnoreCase("orderDetails"))
								{%>
								 	<html:hidden property="forwardTo" value="orderDetails"/>								
						 	  <%}else
						    	{ %>
								<html:hidden property="forwardTo" value=""/>
							  <%} %>
								<html:hidden property="virtuallyLocated"/>
								<html:hidden property="containerId" styleId="containerId"/>
								<html:hidden property="withdrawlButtonStatus"/>
								<html:hidden property="activityStatus"/>
								<html:hidden property="stringOfResponseKeys"/>
								<html:hidden property="applyChangesTo"/>
								<html:hidden property="consentTierCounter"/>
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
								<html:hidden property="specimenCollectionGroupId"/>
							</td>
						</tr>				 
					</table>
				</td>
			</tr>
	</table>		
			
	<%
	if(pageView.equals("edit"))
	{
	%>
		<table summary="" cellpadding="1" cellspacing="0" border="0" height="20" class="tabPage" width="750">
			<tr>
				<td height="20" class="tabMenuItemSelected" onclick="newspecimenPage()" id="newSpecimenTab">
					<bean:message key="tab.specimen.details"/>
				</td>
				<%
					String eventLinkAction = "'ListSpecimenEventParameters.do?pageOf=pageOfListSpecimenEventParameters&menuSelected=15&specimenId="+form.getId()+"'" ;
				%>
				<td height="20" class="tabMenuItem" onmouseover="changeMenuStyle(this,'tabMenuItemOver'),showCursor()" onmouseout="changeMenuStyle(this,'tabMenuItem'),hideCursor()" onclick="eventClicked();">
					<bean:message key="tab.specimen.eventparameters"/>
				</td>

				<td height="20" class="tabMenuItem" onmouseover="changeMenuStyle(this,'tabMenuItemOver'),showCursor()" onmouseout="changeMenuStyle(this,'tabMenuItem'),hideCursor()" onClick="viewSPR()">
					<bean:message key="edit.tab.surgicalpathologyreport"/>
				</td>
				
				<td height="20" class="tabMenuItem" onmouseover="changeMenuStyle(this,'tabMenuItemOver'),showCursor()" onmouseout="changeMenuStyle(this,'tabMenuItem'),hideCursor()" onClick="viewAnnotations(<%=specimenEntityId%>,document.forms[0].id.value,'','<%=staticEntityName%>','<%=pageOf%>')">
					<bean:message key="edit.tab.clinicalannotation"/>
				</td>
				   <td height="20" class="tabMenuItem" onmouseover="changeMenuStyle(this,'tabMenuItemOver'),showCursor()" onmouseout="changeMenuStyle(this,'tabMenuItem'),hideCursor()" onClick="consentPage()" id="consentTab">
					<bean:message key="consents.consents"/>            
				</td>								
		
				<td width="300" class="tabMenuSeparator" colspan="1">&nbsp;</td>
			</tr>

			<tr>
				<td class="tabField" colspan="6">
	<%
	}
	%>
<!--  Consent Tracking Module Virender mehta	 -->
	<%
	if(pageView.equals("add"))
	{
	%>
	
	 <table summary="" cellpadding="1" cellspacing="0" border="0" height="20" class="tabPage" width="700">
		<tr>
			<td height="20" width="9%" nowrap class="tabMenuItemSelected" onclick="newspecimenPage()" id="newSpecimenTab">
				<bean:message key="consents.newspecimen"/>
			</td>

	        <td height="20" width="9%" class="tabMenuItem" onmouseover="changeMenuStyle(this,'tabMenuItemOver'),showCursor()" onmouseout="changeMenuStyle(this,'tabMenuItem'),hideCursor()" onClick="consentPage()" id="consentTab">
	          <bean:message key="consents.consents"/>      
	        </td>								
			<td width="600" class="tabMenuSeparator" colspan="1">&nbsp;</td>
		</tr>
		<tr>
			<td class="tabField" colspan="4" width="*">
	<%
	}
	%>
	
				<table summary="" cellpadding="1" cellspacing="0" border="0" class="contentPage" width="100%">
					<!-- If operation is equal to edit or search but,the page is for query the identifier field is not shown -->
					<!-- NEW SPECIMEN REGISTRATION BEGINS-->
			    	<tr>
						<td width="100%">
							<table summary="" cellpadding="1" border="0" cellspacing="0" id="addSpecimen" width="100%">
							<tr>
								<br>
							</tr>

										<%--
											<span style="width:85%;">
												<bean:message key="<%=title%>"/>    
											</span>
											<span style="text-align:right;margin-left:70%;">
												<html:link href="#" styleId="newUser" onclick="consentPage()">
													<bean:message key="consent.defineconsents" />
												</html:link>	
											</span>	
										 --%>
								 	
								<!--  Consent Tracking Module Virender mehta	 -->	

								<tr>
									<td class="formFieldNoBordersSimple" ><b>*</b></td>
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
									<td class="formFieldNoBordersSimple">
										<label for="specimenCollectionGroupName">
											<b><bean:message key="newSpecimen.groupName"/></b>
										</label>
									</td>							
									<td class="formFieldNoBordersSimple" colspan="<%=specimenColSpan%>">
									<!--
										Patch ID: Bug#3184_4
										Description: The following change shows read-only textbox on specimen page, if specimen is being added
										from specimen collection group page, otherwise combobox having names of specimen collection group is displayed.
									-->
									<html:hidden property="specimenCollectionGroupName" styleId="specimenCollectionGroupName"/>				
									
									<label for="specimenCollectionGroupName">
										<b><%=form.getSpecimenCollectionGroupName()%></b>
									</label>
									
									<!-- <a href="SpecimenCollectionGroup.do?operation=add&pageOf=pageOfSpecimenCollectionGroup">
										<bean:message key="app.addNew" />
									</a> 
										-->			
						        	</td>							
									</logic:equal>
				        	
									<logic:equal name="newSpecimenForm" property="parentPresent" value="true">
						        	<td class="formFieldNoBordersSimple" >
										<label for="parentSpecimenId">
											<b><bean:message key="newSpecimen.parentLabel"/></b>
										</label>
									</td>
							
						        	<td class="formFieldNoBordersSimple" colspan="<%=specimenColSpan%>">
						        		<html:hidden property="specimenCollectionGroupName" styleId="specimenCollectionGroupName"/>
										<!-- Mandar : 434 : for tooltip -->
										<html:hidden property="parentSpecimenName"/>
										
										<label for="parentSpecimenId">
											<b><%=form.getParentSpecimenName()%></b>
										</label>
										
						        	</td>
									</logic:equal>	
									
									<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.EDIT%>">
									<td class="formFieldNoBordersSimple"><b>*</b></td>
									<td class="formFieldNoBordersSimple">
										<label for="lineage">
											<b><bean:message key="specimen.lineage"/></b>
										</label>
									</td>
									<td class="formFieldNoBordersSimple" >								
							     		
							     		
							     		<label for="lineage">
										<b><%=form.getLineage()%></b>
									</label>
									</td>
									</logic:equal>
								</tr>
								<%  if( operation.equals(Constants.EDIT))
									{
								%>	
									<tr>
										<td class="formFieldNoBordersSimple" >
									     	<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>"><b>*</b></logic:notEqual>
									     	<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">&nbsp;</logic:equal>
									    </td>
									    <td class="formFieldNoBordersSimple" >
											<label for="label">
												<b><bean:message key="specimen.label"/></b>
											</label>
										</td>
									    <td class="formFieldNoBordersSimple" >
									     	<html:text styleClass="formFieldSized15" size="30" maxlength="255"  styleId="label" property="label"/>
									    </td>							
										<td class="formFieldNoBordersSimple" width="5">&nbsp;</td>
										<td class="formFieldNoBordersSimple">							
								    	<label for="barcode">
											<bean:message key="specimen.barcode"/>
										</label>								
									</td>
								    <td class="formFieldNoBordersSimple" >
										<html:text styleClass="formFieldSized15" maxlength="255"  size="30" styleId="barcode" property="barcode"/>
						        	</td>
								   </tr>
									<%}
									else  if((!Variables.isSpecimenLabelGeneratorAvl && Variables.isSpecimenBarcodeGeneratorAvl) && operation.equals(Constants.ADD) )
										{
									%>  <tr>
										<td class="formFieldNoBordersSimple" >
									     	<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>"><b>*</b></logic:notEqual>
									     	<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">&nbsp;</logic:equal>
									    </td>
									    <td class="formFieldNoBordersSimple" >
											<label for="label">
												<b><bean:message key="specimen.label"/></b>
											</label>
										</td>
									    <td class="formFieldNoBordersSimple"  colspan=4>
									     	<html:text styleClass="formFieldSized15" size="30" maxlength="255"  styleId="label" property="label"/>
									    </td>							
										
								        </tr>
									<%
									 }else if((Variables.isSpecimenLabelGeneratorAvl && !Variables.isSpecimenBarcodeGeneratorAvl) && operation.equals(Constants.ADD) )
										{

									%>
										<tr>
																			
										<td class="formFieldNoBordersSimple">							
								    	<label for="barcode">
											<bean:message key="specimen.barcode"/>
										</label>								
										</td>
									    <td class="formFieldNoBordersSimple"  colspan=4>
											<html:text styleClass="formFieldSized15" maxlength="255"  size="30" styleId="barcode" property="barcode" />
							        	</td>
										</tr>
									
									<% 
										}
									 %>
								    								
									
						 		<tr>
								 	<td class="formFieldNoBordersSimple" width="5"><b>*</b></td>
								    <td class="formFieldNoBordersSimple">
								     	<label for="className">
								     		<b><bean:message key="specimen.type"/></b>
								     	</label>
								    </td>
								    <td class="formFieldNoBordersSimple">
									
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
								 
								    <td class="formFieldNoBordersSimple" width="5"><b>*</b></td>
								    <td class="formFieldNoBordersSimple">
								     	<label for="type">
								     		<b><bean:message key="specimen.subType"/></b>
								     	</label>
								    </td>				    
								    <td class="formFieldNoBordersSimple" >
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
								     <td class="formFieldNoBordersSimple" width="5">
								     	<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>"><b>*</b></logic:notEqual>
								     	<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">&nbsp;</logic:equal>
								     </td>
								     <td class="formFieldNoBordersSimple">
										<label for="tissueSite">
											<b><bean:message key="specimen.tissueSite"/></b>
										</label>
									</td>
								     <td class="formFieldNoBordersSimple" >
							
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
								
								     <td class="formFieldNoBordersSimple" width="5">
								     	<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>"><b>*</b></logic:notEqual>
								     	<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">&nbsp;</logic:equal>
								     </td>
								     <td class="formFieldNoBordersSimple">
										<label for="tissueSide">
											<b><bean:message key="specimen.tissueSide"/></b>
										</label>
									</td>
								     <td class="formFieldNoBordersSimple" >
									 
									  <autocomplete:AutoCompleteTag property="tissueSide"
												  optionsList = "<%=request.getAttribute(Constants.TISSUE_SIDE_LIST)%>"
												  initialValue="<%=form.getTissueSide()%>"
												  readOnly="<%=readOnlyForAliquot%>"
											    />

						        	  </td>
								</tr>
								<tr>
								    <td class="formFieldNoBordersSimple" width="5">
								     	<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>"><b>*</b></logic:notEqual>
								     	<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">&nbsp;</logic:equal>
								    </td>
								    <td class="formFieldNoBordersSimple">
										<label for="pathologicalStatus">
											<b><bean:message key="specimen.pathologicalStatus"/></b>
										</label>
									</td>
									<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.EDIT%>">
								    <td colspan="4" class="formFieldNoBordersSimple" >
									
									<autocomplete:AutoCompleteTag property="pathologicalStatus"
												  optionsList = "<%=request.getAttribute(Constants.PATHOLOGICAL_STATUS_LIST)%>"
												  initialValue="<%=form.getPathologicalStatus()%>"
												  readOnly="<%=readOnlyForAliquot%>"
									/>
									
						        	</td>
									</logic:notEqual>
							     	
									<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.EDIT%>">
									<td class="formFieldNoBordersSimple" >
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
										<td class="formFieldNoBordersSimple" width="5">&nbsp;</td>
									    <td class="formFieldNoBordersSimple">							
									    	<label for="createdDate">
												<bean:message key="specimen.createdDate"/>
											</label>								
										</td>
									   <td class="formFieldNoBordersSimple" >
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
							     	<td class="formFieldNoBordersSimple" width="5">
								     	<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>"><b>*</b></logic:notEqual>
								     	<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">&nbsp;</logic:equal>
								    </td>
								    <td class="formFieldNoBordersSimple">
										<label for="quantity">
											<b><bean:message key="specimen.quantity"/></b>
										</label>
									</td>
								    <td class="formFieldNoBordersSimple" >
								     	<html:text styleClass="formFieldSized15" size="30" maxlength="10"  styleId="quantity" property="quantity" />
								     	<span id="unitSpan"><%=unitSpecimen%></span>
								     	<html:hidden property="unit"/>
								    </td>
									<td class="formFieldNoBordersSimple" width="5">
								     	&nbsp;
								    </td>
								    <td class="formFieldNoBordersSimple">
										<label for="concentration">
											<bean:message key="specimen.concentration"/>
										</label>
									</td>
									<td class="formFieldNoBordersSimple" >
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
										<td class="formFieldNoBordersSimple" width="5">&nbsp;</td>
										<td class="formFieldNoBordersSimple">
											<label for="available">
												<bean:message key="specimen.available" />
											</label>
										</td>
										<td class="formFieldNoBordersSimple">
											<html:checkbox property="available">
											</html:checkbox>
										</td>	
										<!-- Available Quantity -->							
										<td class="formFieldNoBordersSimple" width="5">&nbsp;</td>
										<td class="formFieldNoBordersSimple" >
											<label for="availableQuantity">
												<bean:message key="specimen.availableQuantity" />
											</label>
										</td>
										<td class="formFieldNoBordersSimple">
											<html:text styleClass="formFieldSized15" maxlength="10"  size="30" styleId="availableQuantity" property="availableQuantity" />
											<span id="unitSpan1"><%=unitSpecimen%></span>
										</td>
									</tr>
								
								</logic:equal>						 
						
								<tr>
								 	<td class="formFieldNoBordersSimple" width="5"><b>*</b></td>
									<td class="formFieldNoBordersSimple">
									   <label for="className">
									   		<b><bean:message key="specimen.positionInStorageContainer"/></b>
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
										
										/**
		                  				* bug ID: 4225
		                 				* Patch id: 4225_2
		                  				* Description : Passing a different name to the popup window
		                 				 */
										String buttonOnClicked = "mapButtonClickedOnNewSpecimen('"+frameUrl+"','newSpecimenPage')";  
										
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
						
									<td class="formFieldNoBordersSimple" colSpan="4">
										<table border="0">
											<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.ADD%>">
											<tr>
												<td ><html:radio value="1" onclick="onRadioButtonGroupClick(this)" styleId="stContSelection" property="stContSelection"/></td>
												<td class="formFieldNoBordersSimple">																			
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
												<td class="formFieldNoBordersSimple">
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
														<td class="formFieldNoBordersSimple">
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
									<td class="formFieldNoBordersSimple" width="5">&nbsp;</td>
									<td class="formFieldNoBordersSimple">
										<label for="comments">
											<bean:message key="specimen.comments"/>
										</label>
									</td>
									
								 	<td class="formFieldNoBordersSimple" colspan="4">
										<html:textarea styleClass="formFieldSized"  rows="3" styleId="comments" property="comments"/>
									</td>
								</tr>
								<!-- collectionstatus -->							
								<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.EDIT%>">
								<tr>
									<td class="formFieldNoBordersSimple" width="5"><b>*<b></td>
									<td class="formFieldNoBordersSimple" >
										<label for="activityStatus">
											<b><bean:message key="participant.activityStatus" /></b>
										</label>
									</td>
									<td class="formFieldNoBordersSimple">
									
									
									<label for="activityStatus">
										<b><%=form.getActivityStatus()%></b>
									</label>
									
									</td>
									
									<td class="formFieldNoBordersSimple" width="5"><b>*</b></td>
									<td class="formFieldNoBordersSimple">
										<label for="collectionStatus">
											<b><bean:message key="specimenCollectionGroup.collectionStatus" /></b>
										</label>
									</td>
									<td class="formFieldNoBordersSimple">
									<autocomplete:AutoCompleteTag property="collectionStatus"
												  optionsList = "<%=request.getAttribute(Constants.COLLECTIONSTATUSLIST)%>"
												  initialValue="<%=form.getCollectionStatus()%>"
												  onChange="<%=strCheckStatus%>"
									/>
									</td>
								</tr>
								</logic:equal>
							</table>	
							<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.ADD%>">
							<%@ include file="CollAndRecEvents.jsp" %>
							</logic:equal>
							<%@ include file="ExternalIdentifiers.jsp" %>
							<%@ include file="BioHazards.jsp" %>
							
							<!--  Consent Tracking Module Virender mehta	 -->														
								<%
								List requestParticipantResponse = (List)request.getAttribute(Constants.SPECIMEN_RESPONSELIST);	
								if(requestParticipantResponse!=null&&form.getConsentTierCounter()>0)
								{
								%>
								    <%@ include file="/pages/content/ConsentTracking/ConsentTracking.jsp" %> 
								<%
								}
								%>
							<!--  Consent Tracking Module Virender mehta	 -->										
	 				
							<!--specimenPageButton-->
				 			<table summary="" cellpadding="3" cellspacing="1" border="0" width="100%" id="aliquotId">
								<logic:notEqual name="<%=Constants.PAGEOF%>" value="<%=Constants.QUERY%>">				 			
								<tr>					
									<td class="formFieldNoBordersSimple" height="20" colspan="5">
										<html:checkbox styleId="aliquotChk" property="checkedButton" onclick="onCheckboxButtonClick(this)">
											&nbsp; <bean:message key="specimen.aliquot.message"/>
										</html:checkbox>
										&nbsp;&nbsp;&nbsp;
						                <bean:message key="aliquots.noOfAliquots"/>
					                    &nbsp;
					                    <!-- Resolved bug# 4287
					                    	 Name: Virender Mehta
					                    	 Reviewer: Sachin Lale
					                    	 Description: replaced input type with html:text
					                     -->
										<html:text styleClass="formFieldSized5" styleId="noOfAliquots" property="noOfAliquots" disabled="true" />
										&nbsp;&nbsp;&nbsp;
						                <bean:message key="aliquots.qtyPerAliquot"/>
					                    &nbsp;
										<html:text styleClass="formFieldSized5" styleId="quantityPerAliquot" property="quantityPerAliquot" disabled="true" />
								    </td>
								</tr>			
								<tr>
											<td class="formFieldNoBorders" colspan="3" valign="center">
													<html:checkbox styleId="printCheckbox" property="printCheckbox" value="true" onclick="">
															<bean:message key="print.checkboxLabel"/>
														</html:checkbox>
											</td>
								</tr>					
								</logic:notEqual>
							</table>	 
						
							<table>
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

<!--  Consent Tracking Module Virender mehta	 -->
<%
if(pageView.equals("edit")||pageView.equals("add"))
{
%>
<!--  Consent Tracking Module Virender mehta	 -->
			</td>
		</tr>
	</table>
<%
}
%>
<html:hidden property="stContSelection"/>
<html:hidden property="lineage"/>
<html:hidden property="nextForwardTo" />
<!--
	Patch ID: Bug#3184_8
-->
<html:hidden property="restrictSCGCheckbox"/>
</html:form>
</body>
<!-- 
 Name : Virender Mehta
 Reviewer: Sachin Lale
 Bug ID: 4231
-->
<script language="JavaScript">
	onCheckboxButtonClick(document.getElementById("aliquotChk"));
</script>