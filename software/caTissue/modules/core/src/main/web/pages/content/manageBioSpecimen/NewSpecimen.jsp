<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/nlevelcombo.tld" prefix="ncombo" %>
<%@ page language="java" isELIgnored="false"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="java.util.Map,java.util.List,java.util.ListIterator"%>
<%@ page import="edu.wustl.common.beans.NameValueBean"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.actionForm.NewSpecimenForm"%>
<%@ page import="edu.wustl.catissuecore.util.global.AppUtility"%>
<%@ page import="edu.wustl.common.util.global.Status" %>
<%@ page import="java.util.*"%>
<%@ page import="java.io.*"%>
<%@ page import="edu.wustl.common.util.global.ApplicationProperties" %>
<%@ page import="edu.wustl.common.util.tag.ScriptGenerator" %>
<%@ page import="edu.wustl.catissuecore.bean.ConsentBean"%>
<%@ page import="edu.wustl.catissuecore.util.global.Variables"%>
<%@ page import="edu.wustl.catissuecore.action.annotations.AnnotationConstants"%>
<%@ page import="edu.wustl.common.util.global.CommonServiceLocator"%>
<%@ page import="org.apache.commons.lang.StringUtils"%>
<%@ page import="edu.wustl.catissuecore.GSID.GSIDConstant" %>
<%@ page import="edu.wustl.catissuecore.GSID.GSIDClient" %>

<%@ include file="/pages/content/common/BioSpecimenCommonCode.jsp" %>
<%@ include file="/pages/content/common/AutocompleterCommon.jsp" %>
<script type="text/javascript" src="jss/wz_tooltip.js"></script>
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
	String pageOf = (String)request.getAttribute(Constants.PAGE_OF);
	boolean isWithinFrame = "pageOfNewSpecimenCPQuery".equalsIgnoreCase(pageOf);
	boolean isAddNew = false;
	String signedConsentDate = "";
	String selectProperty="";
	String operation = (String)request.getAttribute(Constants.OPERATION);
	String transferStatus = (String)request.getAttribute("transferStatus");
	String fromPositionDimensionOne = (String)request.getAttribute("fromPositionDimensionOne");
	String fromPositionDimensionTwo = (String)request.getAttribute("fromPositionDimensionTwo");
	String fromStorageContainer = (String)request.getAttribute("fromStorageContainer");
	String reqPath = (String)request.getAttribute(Constants.REQ_PATH);
	String appendingPath = "/NewSpecimen.do?operation=add&pageOf=pageOfNewSpecimen";
	String currentReceivedDate = "";
	String currentCollectionDate = "";

	String staticEntityName=null;
	staticEntityName = AnnotationConstants.ENTITY_NAME_SPECIMEN_REC_ENTRY;
	//Falguni:Performance Enhancement.
	Long specimenEntityId = null;
	specimenEntityId =(Long)request.getAttribute(AnnotationConstants.SPECIMEN_REC_ENTRY_ENTITY_ID);

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
	List dataList = (List) request.getAttribute(edu.wustl.simplequery.global.Constants.SPREADSHEET_DATA_LIST);
	//String pageOf = (String)request.getAttribute(Constants.PAGE_OF);


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
<script language="JavaScript" >
		//Set last refresh time
		if(window.parent!=null)
		{
			if(window.parent.lastRefreshTime!=null)
			{
				window.parent.lastRefreshTime = new Date().getTime();
			}
		}
</script>
<script language="JavaScript">
	function hidedisposeArea()
	{
		// alert("hidedisposeArea"+document.getElementById('dispose'));
		if(document.getElementById("dispose").checked)
		{

			document.getElementById("disposediv").style.display = "block"
		}
		else
		{
			document.getElementById("disposediv").style.display = "none";
		}
	}
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
	//this method is called when aliquot or derivative is selected
	function onCheckboxButtonClick(radioButton)
		{
			var childSpecimenCount  = document.getElementById("noOfAliquots");
			var qtyPerAliquotTextBox = document.getElementById("quantityPerAliquot");
			var countForDerive=document.getElementById("derivedDiv");
			var countForAliquot=document.getElementById("aliquotDiv");


			if(radioButton.value==1)
			{
				childSpecimenCount.disabled = true;
				qtyPerAliquotTextBox.disabled = true;
				countForDerive.style.display="none";
				countForAliquot.style.display="block";
			}
			else if(radioButton.value==2)
			{

				childSpecimenCount.disabled = false;
				qtyPerAliquotTextBox.disabled = false;
				countForDerive.style.display="none";
				countForAliquot.style.display="block";
			}
			else if(radioButton.value==3)
			{
				childSpecimenCount.disabled = false;
				qtyPerAliquotTextBox.disabled = true;
				countForDerive.style.display="block";
				countForAliquot.style.display="none";
			}
			else
			{
				childSpecimenCount.disabled = true;
				qtyPerAliquotTextBox.disabled = true;
				countForDerive.style.display="none";
				countForAliquot.style.display="block";
			}
		}


	function onDeriveSubmit()
	{
		var submubitoperation= document.forms[0].operation.value;
		var transferStatus=document.getElementById("transferStatus");
		if(transferStatus!=null)
		{
			var transfervalue=transferStatus.value;
			if(transfervalue=="transferred"&& submubitoperation=="edit")
			{
				document.getElementById("transferStatus").value="transferDone";
			}
		}

		var disposeStatus=document.getElementById("disposeStatus");
		if(disposeStatus!=null)
		{
			if(disposeStatus.value == "Disabled")
			{
				if(confirm("Are you sure you want to disable the specimen ?"))
				{
				}
				else
				{
					disposeStatus.value="";
					return false;
				}
			}
			else if(disposeStatus.value == "Closed")
			{
				if(confirm("Are you sure you want to close the specimen ?"))
				{

				}
				else
				{
					disposeStatus.value="";
					return false;
				}

			}
		}

	var operation = document.forms[0].operation.value;
		<%String forwardToPrintPath = "PrintSpecimenAdd";%>

		if(document.getElementById("deriveChk").checked == true)
		{
			document.forms[0].derivedClicked.value=true;
			document.forms[0].checkedButton.value=false;
			if(document.getElementById("numberOfSpecimens").value > 1)
			{
				if(operation == "add")
				{
					<%
				    if(pageOf.equals(Constants.PAGE_OF_SPECIMEN_CP_QUERY))
				   {
				  	    forwardToPrintPath = "CPQueryPrintSpecimenAdd";
				    }%>
					setSubmitted("ForwardTo",'<%=forwardToPrintPath%>',"deriveMultiple");
					confirmDisable("<%=formName%>","document.forms[0].activityStatus");
				}
				else
				{
					<%
					forwardToPrintPath = "PrintSpecimenEdit";
				    if(pageOf.equals(Constants.PAGE_OF_SPECIMEN_CP_QUERY))
				   {
				  	    forwardToPrintPath = "CPQueryPrintSpecimenEdit";
				    }%>
					setSubmitted("ForwardTo",'<%=forwardToPrintPath%>',"deriveMultiple");
					confirmDisable("<%=formName%>","document.forms[0].activityStatus");
				}


			}
			else
			{
				if(operation == "add")
				{
						<%
				    if(pageOf.equals(Constants.PAGE_OF_SPECIMEN_CP_QUERY))
				   {
				  	    forwardToPrintPath = "CPQueryPrintSpecimenAdd";
				    }%>
					setSubmitted("ForwardTo",'<%=forwardToPrintPath%>',"createNew");
				    confirmDisable("<%=formName%>","document.forms[0].activityStatus");
				}
				else
				{
					<%
					forwardToPrintPath = "PrintSpecimenEdit";
				    if(pageOf.equals(Constants.PAGE_OF_SPECIMEN_CP_QUERY))
				   {
				  	      forwardToPrintPath = "CPQueryPrintSpecimenEdit";
				    }%>

					setSubmitted("ForwardTo",'<%=forwardToPrintPath%>',"createNew");
					confirmDisable("<%=formName%>","document.forms[0].activityStatus");
				}

			}


		}
		else
		{
			if(operation =="add")
				onNormalSubmit();
			else
			{
			document.forms[0].checkedButton.value=false;
			document.forms[0].derivedClicked.value=false;
			<%	ConsentTierData consentForm =(ConsentTierData)form;
			List consentTier=(List)consentForm.getConsentTiers();

			if(consentTier.size()>0)
			{%>
				popupWindow("<%=consentTier.size()%>");
			<%}else{%>
				onNormalSubmit();
			<%}%>
			}
		}
	}


		function onNormalSubmit()
		{

			var operation = document.forms[0].operation.value;
			document.forms[0].checkedButton.value=false;
			document.forms[0].derivedClicked.value=false;
			var checked = false;
			if(document.getElementById("aliquotChk").checked == true)
			{
			   checked = true;
			   document.forms[0].checkedButton.value=true;
			}


			var temp = "<%=frdTo%>";
			<%String forwardToValue = "PrintSpecimenAdd";%>
			//Bug ID: 4040(Virender)
			if(checked)
			{
				<% String actionToCall = null;

				   %>
				if(operation == "add")
				{
					<%actionToCall = "NewSpecimenAdd.do";
					if(pageOf.equals(Constants.PAGE_OF_SPECIMEN_CP_QUERY))
					{
					  	actionToCall = Constants.CP_QUERY_SPECIMEN_ADD_ACTION;
						forwardToValue = "CPQueryPrintSpecimenAdd";
					}%>

				    if(temp == "orderDetails")
					{
						setSubmitted('ForwardTo','<%=forwardToValue%>','orderDetails');
					}
					else
					{

						setSubmitted('ForwardTo','<%=forwardToValue%>','pageOfCreateAliquot');
                    }

					confirmDisable('<%=actionToCall%>',document.forms[0].activityStatus);
				}
				else
				{
					<%
					actionToCall = "NewSpecimenEdit.do";
					forwardToValue = "PrintSpecimenEdit";
					if(pageOf.equals(Constants.PAGE_OF_SPECIMEN_CP_QUERY))
					{
						actionToCall = Constants.CP_QUERY_SPECIMEN_EDIT_ACTION;
                        forwardToValue = "CPQueryPrintSpecimenEdit";

					}%>

					if(temp == "orderDetails")
					{
						setSubmitted('ForwardTo','<%=forwardToValue%>','orderDetails');
					}
					else
					{
						setSubmitted('ForwardTo','<%=forwardToValue%>','pageOfCreateAliquot');
					}


					confirmDisable('<%=actionToCall%>',document.forms[0].activityStatus);
				}
			}
			else
			{


			// for cp child submit

				var cpChildChkFlag = false;
				if(operation == "edit" && document.getElementById("createCpChildCheckBox").checked == true)
				{
				   cpChildChkFlag = true;
				}

				if(cpChildChkFlag)
				{
						<%
						String cpChildSubmitAction = "NewSpecimenEdit.do";
						actionToCall = "NewSpecimenEdit.do";
						forwardToValue = "PrintSpecimenEdit";
						if(pageOf.equals(Constants.PAGE_OF_SPECIMEN_CP_QUERY))
						{
							actionToCall = Constants.CP_QUERY_SPECIMEN_EDIT_ACTION;
							cpChildSubmitAction = "CPQueryPrintSpecimenEdit";//Constants.CP_QUERY_SPECIMEN_EDIT_ACTION;

						}%>
						//added this for print specimen when createCpChildCheckBox is clicked
						var printFlag = document.getElementById("printCheckbox");
			            if(printFlag.checked)
					    {
			            	<% if(pageOf.equals(Constants.PAGE_OF_SPECIMEN_CP_QUERY))
							{
							   	cpChildSubmitAction = "CPQueryPrintSpecimenEdit";
							}
			            	else
			            	{
			            		 	cpChildSubmitAction = "PrintSpecimenEdit";
			            	} %>
					    }
						setSubmitted("ForwardTo","<%=cpChildSubmitAction%>","cpChildSubmit");
						confirmDisable("<%=actionToCall%>","document.forms[0].activityStatus");
				}
				else
				{
					if(operation == "add")
					{
						<%
						actionToCall = "NewSpecimenAdd.do";
						forwardToValue = "PrintSpecimenAdd";
						if(pageOf.equals(Constants.PAGE_OF_SPECIMEN_CP_QUERY))
						{
							actionToCall = Constants.CP_QUERY_SPECIMEN_ADD_ACTION;
							forwardToValue = "CPQueryPrintSpecimenAdd";
						}%>

						if(temp == "orderDetails")
						{
						   	setSubmitted('ForwardTo','<%=forwardToValue%>','orderDetails');
						}
						else
						{
							setSubmitted('null','<%=forwardToValue%>','success');
						}

						if(document.forms[0].nextForwardTo.value!=null)
						{
						 confirmDisable('<%=actionToCall%>'+"?nextForwardTo="+document.forms[0].nextForwardTo.value,document.forms[0].activityStatus);
						}
						else
						{
							confirmDisable('<%=actionToCall%>',document.forms[0].activityStatus);
						}

					}
					else //none + submit
					{
						<%
						actionToCall = "NewSpecimenEdit.do";
						forwardToValue = "PrintSpecimenEdit";
						if(pageOf.equals(Constants.PAGE_OF_SPECIMEN_CP_QUERY))
						{
							actionToCall = Constants.CP_QUERY_SPECIMEN_EDIT_ACTION;
							forwardToValue = "CPQueryPrintSpecimenEdit";
						}%>
						if(temp == "orderDetails")
						{
							setSubmitted('ForwardTo','<%=forwardToValue%>','orderDetails');
						}
						else
						{
							setSubmitted('null','<%=forwardToValue%>','success');
						}
						confirmDisable('<%=actionToCall%>',document.forms[0].activityStatus);
					}
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
			else
			{
				alert("Please select the appropriate specimen class");
				resetVirtualLocated();
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
				}

					String forwardTo = form.getForwardTo();
				%>
				var action = "<%=actionOnCollOrClassChange%>"+"&value="+value;
				document.forms[0].action = action + "&onCollOrClassChange=true"+"&forwardTo="+"<%=forwardTo%>";
				document.forms[0].submit();

			}
		}

		function onCollOrClassChange2(element)
		{
			var specimenCollGroupElement = document.getElementById("specimenCollectionGroupName");

			var classNameElement = document.getElementById("className").value;
			classNameElement = trim(classNameElement);
			var classSet = false;
			if(classNameElement == "Fluid" || classNameElement == "Cell"||classNameElement == "Tissue"||classNameElement == "Molecular")
			{

			    classSet = true;
			}
			else
			{
				alert("Please select the appropriate specimen class");
				resetVirtualLocated();
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
				String actionOnStorageTypeChange = "NewSpecimen.do?pageOf=pageOfNewSpecimen&transferStatus=transferred&virtualLocated=false&tab=newSpecimenForm&showConsents=yes&tableId4=disable";
				if(pageOf.equals(Constants.PAGE_OF_SPECIMEN_CP_QUERY))
				{
					actionOnStorageTypeChange = "CPQueryNewSpecimen.do?pageOf=pageOfNewSpecimenCPQuery&virtualLocated=false";
				}

					String forwardToPage = form.getForwardTo();
				%>
				var action = "<%=actionOnStorageTypeChange%>"+"&value="+value;
				document.forms[0].action = action + "&onCollOrClassChange=true"+"&forwardTo="+"<%=forwardToPage%>";
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
		//FUNCTION CALLED FOR RESETTING THE SELECT BOX TO VIRTUAL
		function resetVirtualLocated()
		{
			document.getElementById("stContSelection").value=1;
			document.getElementById("autoDiv").style.display="none";
			document.getElementById("manualDiv").style.display="none";
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

		function sppEventClicked()
		{
			document.forms[0].onSubmit.value="";
			var specimenId = document.forms[0].id.value;
			var consentTier=document.forms[0].consentTierCounter.value;
			var action= "DisplaySPPEventsAction.do?pageOf=<%=pageOf%>&menuSelected=15&specimenId="+specimenId+"&consentTierCounter="+consentTier;
			document.forms[0].action = action;
			document.forms[0].submit();
		}

		function eventClicked()
		{
			  // Clear the value of onSubmit
		    document.forms[0].onSubmit.value="";
		    var consentTier=document.forms[0].consentTierCounter.value;
			//var answer = confirm("Do you want to submit any changes?");
			var formName;
			<% String formNameAction = null;%>
			/*if (answer){
				setSubmitted('ForwardTo','CPQueryPrintSpecimenEdit','eventParameters');
				<%
				formNameAction = "NewSpecimenEdit.do";
				if(pageOf.equals(Constants.PAGE_OF_SPECIMEN_CP_QUERY))
				{
					formNameAction = "CPQueryNewSpecimenEdit.do";
				}%>
				formName = "<%=formNameAction%>?consentTierCounter="+consentTier;
			}
			else{*/
				var id = document.forms[0].id.value;
				//var label = document.getElementById("label").value;

				<%
				formNameAction = "ListSpecimenEventParameters.do?pageOf=pageOfListSpecimenEventParameters";
				if(pageOf.equals(Constants.PAGE_OF_SPECIMEN_CP_QUERY))
				{
					formNameAction = "CPQueryListSpecimenEventParameters.do?pageOf=pageOfListSpecimenEventParametersCPQuery";
				}%>

				formName = "<%=formNameAction%>&specimenId="+id+"&menuSelected=15&consentTierCounter="+consentTier;
			//}


			confirmDisable(formName,document.forms[0].activityStatus);
		}

    // Consent Tracking Module Virender mehta
	function switchToTab(selectedTab)
	{
		var switchImg1=document.getElementById("specimenDetailsTab");
		var switchImg2=document.getElementById("consentViewTab");
		if(selectedTab=="specimenDetailsTab")
		{
			document.getElementById("consentTable").style.display='none';
			document.getElementById("mainTable").style.display='block';
			switchImg1.innerHTML="<img src='images/uIEnhancementImages/tab_specimen_details1.gif' alt='Specimen Details' width='126' height='22' border='0'>";
			switchImg2.innerHTML="<img src='images/uIEnhancementImages/tab_consents2.gif' alt='Consents' width='76' height='22' border='0'>";
		}
		else
		{

			document.getElementById("consentTable").style.display='block';
			document.getElementById("mainTable").style.display='none';
			switchImg2.innerHTML="<img src='images/uIEnhancementImages/tab_consents1.gif' alt='Consents' width='76' height='22' border='0'>";
			switchImg1.innerHTML="<img src='images/uIEnhancementImages/tab_specimen_details2.gif' alt='Specimen Details' width='126' height='22' border='0'>";
		}

	}

	function switchToNewTab(selectedTab)
	{
		specimenImage=document.getElementById("newSpecimenTab");
		consentImage=document.getElementById("newConsentTab");

		if(selectedTab == "newSpecimenTab")
		{
			document.getElementById("consentTable").style.display='none';
			document.getElementById("mainTable").style.display='block';
			specimenImage.innerHTML="<img src='images/uIEnhancementImages/new_specimen_selected.gif' alt='Specimen Details'  width='115' height='22' border='0'>";
			consentImage.innerHTML="<img src='images/uIEnhancementImages/tab_consents2.gif' alt='Consents' width='76' height='22' border='0'>";
		}
		else
		{
			document.getElementById("consentTable").style.display='block';
			document.getElementById("mainTable").style.display='none';
			specimenImage.innerHTML="<img src='images/uIEnhancementImages/new_specimen_unselected.gif' alt='Specimen Details'  width='115' height='22' border='0'>";
			consentImage.innerHTML="<img src='images/uIEnhancementImages/tab_consents1.gif' alt='Consents' width='76' height='22' border='0'>";
		}

	}




		//This function will Switch tab to newSpecimen page
		function newspecimenPage()
		{
			switchToTab("specimenDetailsTab");
		}

		//This function will switch page to consentPage
		function consentPage()
		{
			checkForConsents();
		}

		function newSpecimenTab()
		{
			switchToNewTab("newSpecimenTab");
		}

		function newConsentTab()
		{
			switchToNewTab("newConsentTab");
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
			else if(reportId==null || reportId==-2)
			{
				alert("Associated report is under quarantined request! Please contact administrator for further details.");
			}
			else
			{
		    	var action="<%=Constants.VIEW_SPR_ACTION%>?operation=viewSPR&pageOf=<%=pageOf%>&reportId="+reportId;
				document.forms[0].action=action;
				document.forms[0].submit();
			}
		}

		function setSubmitted(forwardTo,printaction,nextforwardTo)
		{
				//alert(forwardTo+"   "+printaction+"    "+nextforwardTo);
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

	    function setSize()
	    {

		  //  var container = document.getElementById("Container");
			var tempWidth =document.body.clientWidth;

    	   //  container.style.width=tempWidth-50;
        }

	function showConsent()
        {

		   <%
			if( tab == null)
            {
			%>
                switchToTab("specimenDetailsTab");
            <%
           	}
            if( tab != null)
            {
              if(tab.equals("consent"))
			  {
			  %>
                consentTab();
              <%
              }
             else
             {
             %>
              	switchToTab("specimenDetailsTab");
             <%
             }
            }
            %>
	 }
		function consentTab()
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
					alert("No consents available for selected Specimen");
				<%
				}
			%>
		}
		//Function called on storage position change
    function onStorageRadioClickInSpecimen(element)
	{
		var autoDiv=document.getElementById("autoDiv");
		var manualDiv=document.getElementById("manualDiv");

		if(element.value==1)
		{
			autoDiv.style.display = 'none';
			manualDiv.style.display = 'none';
			document.forms[0].selectedContainerName.disabled = true;
			document.forms[0].pos1.disabled = true;
			document.forms[0].pos2.disabled = true;
			document.forms[0].containerMap.disabled = true;
			document.forms[0].customListBox_1_0.disabled = true;
			document.forms[0].customListBox_1_1.disabled = true;
			document.forms[0].customListBox_1_2.disabled = true;
		}
		else if(element.value==2)
		{
			autoDiv.style.display = 'block';
			manualDiv.style.display = 'none';
			document.forms[0].selectedContainerName.disabled = true;
			document.forms[0].pos1.disabled = true;
			document.forms[0].pos2.disabled = true;
			document.forms[0].containerMap.disabled = true;
			document.forms[0].customListBox_1_0.disabled = false;
			document.forms[0].customListBox_1_1.disabled = false;
			document.forms[0].customListBox_1_2.disabled = false;
			onCollOrClassChange();
		}
		else
		{
			autoDiv.style.display = 'none';
			manualDiv.style.display = 'block';
			document.forms[0].selectedContainerName.disabled = false;
			document.forms[0].pos1.disabled = false;
			document.forms[0].pos2.disabled = false;
			document.forms[0].containerMap.disabled = false;
			document.forms[0].customListBox_1_0.disabled = true;
			document.forms[0].customListBox_1_1.disabled = true;
			document.forms[0].customListBox_1_2.disabled = true;
			onCollOrClassChange();
		}
	}

    function onStorageTypeChange(element)
	{
		var autoDiv2=document.getElementById("autoDiv2");
		var manualDiv2=document.getElementById("manualDiv2");
		var autoDiv=document.getElementById("autoDiv");
		var manualDiv=document.getElementById("manualDiv");
		alert("element.value = "+element.value);
		if(element.value==1)
		{
			autoDiv2.style.display = 'none';
			manualDiv2.style.display = 'none';
			document.forms[0].selectedContainerName.disabled = true;
			document.forms[0].pos1.disabled = true;
			document.forms[0].pos2.disabled = true;
			document.forms[0].containerMap.disabled = true;
			document.forms[0].customListBox_1_0.disabled = true;
			document.forms[0].customListBox_1_1.disabled = true;
			document.forms[0].customListBox_1_2.disabled = true;
		}
		else if(element.value==2)
		{
			alert("Auto selected");
			//autoDiv2.style.display = 'none';
			//manualDiv2.style.display = 'none';
			alert("987");
			document.forms[0].selectedContainerName.disabled = true;
			document.forms[0].pos1.disabled = true;
			document.forms[0].pos2.disabled = true;
			document.forms[0].containerMap.disabled = true;
			document.forms[0].customListBox_1_0.disabled = false;
			document.forms[0].customListBox_1_1.disabled = false;
			document.forms[0].customListBox_1_2.disabled = false;
			alert("Before onCollOrClassChange");
			onCollOrClassChange2();
		}
		else
		{
			autoDiv2.style.display = 'none';
			manualDiv2.style.display = 'block';
			document.forms[0].selectedContainerName.disabled = false;
			document.forms[0].pos1.disabled = false;
			document.forms[0].pos2.disabled = false;
			document.forms[0].containerMap.disabled = false;
			document.forms[0].customListBox_1_0.disabled = true;
			document.forms[0].customListBox_1_1.disabled = true;
			document.forms[0].customListBox_1_2.disabled = true;
			alert("Before onCollOrClassChange");
			onCollOrClassChange2();
		}
	}
	/*
	    Added separate mappings for pages opened from CP based view(without menubar) and
	    Biospecimen data -> Specimen (with menubar)
	*/

    function onAddToCart()
	{
	    <% String actionToCall1 = "NewSpecimenEdit.do";
	       String nextForwardToForAddToCart = "";
	       String forwardToPath = "";
	    %>
	    var operation = document.forms[0].operation.value;
	    if(document.getElementById("aliquotChk").checked == true)
		 {
	    	<%forwardToPath = "addSpecimenToCartForwardtoAliquot";
	  			if(pageOf == (Constants.PAGE_OF_SPECIMEN_CP_QUERY))
	  			{
	  				nextForwardToForAddToCart = "pageOfCreateAliquot";
	  			}
	  			else
	  			{
	  				nextForwardToForAddToCart = "pageOfCreateAliquotSpecimenEdit";

	  			}%>
			   setSubmittedForAddToMyList("ForwardTo",'<%=forwardToPath%>','<%=nextForwardToForAddToCart%>');
		 }
		else if(document.getElementById("deriveChk").checked == true)
		{
			<%forwardToPath = "addSpecimenToCartForwardtoDerive";
			   if(pageOf == (Constants.PAGE_OF_SPECIMEN_CP_QUERY))
	  			{
	  				nextForwardToForAddToCart = "createNew";
	  			}
	  			else
	  			{
	  				nextForwardToForAddToCart = "createNewDerivative";
	  			}%>
               setSubmittedForAddToMyList("ForwardTo",'<%=forwardToPath%>','<%=nextForwardToForAddToCart%>');
		}
		else if(document.getElementById("createCpChildCheckBox").checked == true)
		{
			<%forwardToPath = "addSpecimenToCartForwardtoCpChild";

			 if(pageOf == (Constants.PAGE_OF_SPECIMEN_CP_QUERY))
	  			{
	  				nextForwardToForAddToCart = "CPQuerycpChildSubmit";
	  			}
	  			else
	  			{
	  				nextForwardToForAddToCart = "cpChildSubmit";
	  			}%>
               setSubmittedForAddToMyList("ForwardTo",'<%=forwardToPath%>','<%=nextForwardToForAddToCart%>');
		}
		else
		{
			setSubmittedFor("ForwardTo",'addSpecimenToCart');
		}
		<%
		if(pageOf.equals(Constants.PAGE_OF_SPECIMEN_CP_QUERY))
		{
			actionToCall1 = Constants.CP_QUERY_SPECIMEN_EDIT_ACTION;
		}%>
		confirmDisable('<%=actionToCall1%>',document.forms[0].activityStatus);
	}

	    function showTransferArea()
	    {
	        //alert("transferButton ::: "+document.getElementById("transferButton").style.display);
	        //alert("transferComment ::: "+document.getElementById("transferComment").style.display);
	        //alert("transferStatus = "+document.forms[0].transferStatus.value);
	       // document.forms[0].transferStatus.value = "true";
	        //alert("transferStatus after modification= "+document.forms[0].transferStatus.value);
			document.getElementById("transferStatus").value="transferred";
			//document.getElementById("fromlocationdiv").style.display = "block";
	        //manualDisplayStyle="display:block";
	    	document.getElementById("transferButton").style.display = "none";
	    	//document.getElementById("transferComment").style.display = "block";
	    	//document.getElementById("fromlocationdiv").style.display = "none";
	    	//document.getElementById("tolocationdiv").style.display = "block";

			//document.getElementById("virtualdiv").style.display="none";

				<%
				String actionOnStorageTypeChange2 = "NewSpecimen.do?pageOf=pageOfNewSpecimen&transferStatus=transferred&virtualLocated=false&tab=newSpecimenForm&showConsents=yes&tableId4=disable";
				if(pageOf.equals(Constants.PAGE_OF_SPECIMEN_CP_QUERY))
				{
					actionOnStorageTypeChange2 = "CPQueryNewSpecimen.do?pageOf=pageOfNewSpecimenCPQuery&virtualLocated=false";
				}

					String forwardToPage2 = form.getForwardTo();
				%>
				//var stContSelection2 = documment.getElementById("stContSelection");

				var action2 = "<%=actionOnStorageTypeChange2%>"+"&value=false";
				document.forms[0].action = action2 +"&stContSelection=2"+ "&onCollOrClassChange=true"+"&forwardTo="+"<%=forwardToPage2%>";
				document.forms[0].submit();
	    }

	</script>
	<link href="css/catissue_suite.css" rel="stylesheet" type="text/css" />
	<link href="css/styleSheet.css" rel="stylesheet" type="text/css" />
</head>
<body onload="showConsent()">
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
				String deriveMultipleSubmitFunctionName = "setSubmitted('ForwardTo','"+printAction+"','" + Constants.SPECIMEN_FORWARD_TO_LIST[5][1]+"')";

				String normalSubmit = normalSubmitFunctionName + ","+confirmDisableFuncName;
				String deriveNewSubmit = deriveNewSubmitFunctionName + ","+confirmDisableFuncName;
				String addEventsSubmit = addEventsSubmitFunctionName + ","+confirmDisableFuncName;
				String addMoreSubmit = addMoreSubmitFunctionName + ","+confirmDisableFuncName;
				String submitAndDistribute = "setSubmitted('ForwardTo','"+printAction+"','" + Constants.SPECIMEN_FORWARD_TO_LIST[4][1]+"')," + confirmDisableFuncName;
				String deriveMultipleSubmit = deriveMultipleSubmitFunctionName + ","+confirmDisableFuncName;

				String specimenCollectionGroupId = (String)request.getAttribute("SpecimenCollectionGroupId");
				String specimenCollectionGroupName = (String)request.getAttribute("SpecimenCollectionGroupName");
	%>

								<html:hidden property="operation" value="<%=operation%>"/>
								<html:hidden property="submittedFor" value="<%=submittedFor%>"/>
								<html:hidden property="fromPositionDimensionOne" value="<%=fromPositionDimensionOne%>"/>
								<html:hidden property="fromPositionDimensionTwo" value="<%=fromPositionDimensionTwo%>"/>
								<html:hidden property="fromStorageContainerId" value="<%=fromStorageContainer%>"/>
								<%
								if(form.getForwardTo().equalsIgnoreCase("orderDetails"))
								{%>
								 	<html:hidden property="forwardTo" value="orderDetails"/>
						 	  <%}else
						    	{ %>
								<html:hidden property="forwardTo" value=""/>
							  <%} %>
							  <html:hidden property="generateLabel"/>
								<html:hidden property="virtuallyLocated"/>
								<html:hidden property="containerId" styleId="containerId"/>
								<html:hidden property="withdrawlButtonStatus"/>
								<html:hidden property="stringOfResponseKeys"/>
								<html:hidden property="applyChangesTo"/>
								<html:hidden property="consentTierCounter"/>
								<html:hidden property="parentSpecimenId"/>
								<html:hidden property="parentSpecimenGSID"/> 
								<html:hidden property="onSubmit"/>
								<html:hidden property="id"/>
								<html:hidden property="positionInStorageContainer" />
								<html:hidden property="parentPresent" />
								<html:hidden property="specimenCollectionGroupId"/>
								<html:hidden property="checkedButton"/>
								<html:hidden property="derivedClicked"/>
								<html:hidden property="transferStatus"  styleId="transferStatus" value="${requestScope.transferStatus}"/>

<table width="100%" border="0" cellpadding="0" cellspacing="0" class="maintable">
<logic:equal name="<%=Constants.PAGE_OF%>" value="pageOfNewSpecimen">
<tr>
		<td class="td_color_bfdcf3">
			<table border="0" cellpadding="0" cellspacing="0">
		      <tr>
				<td class="td_table_head">
					<span class="wh_ar_b">
						<bean:message key="app.newSpecimen" />
					</span>
				</td>
		        <td>
					<img src="images/uIEnhancementImages/table_title_corner2.gif" alt="Page Title - Specimen" width="31" height="24" hspace="0" vspace="0" />
				</td>
		      </tr>
		    </table>
		</td>
	  </tr>
</logic:equal>
	  <tr>
		<td class="tablepadding">
	<%
	if(pageView.equals("edit"))
	{
	%>
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
		      <tr><td class="td_tab_bg" ><img src="images/uIEnhancementImages/spacer.gif" alt="spacer" width="50" height="1"></td><td valign="bottom"><a onclick="newspecimenPage()" id="specimenDetailsTab" href="#">	<img src="images/uIEnhancementImages/tab_specimen_details1.gif" alt="Specimen Details"  width="126" height="22" border="0"></a></td><td valign="bottom"><a href="#"><img src="images/uIEnhancementImages/tab_events2.gif" alt="Events" width="56" height="22" onclick="eventClicked();" border="0"></a></td><td valign="bottom"><a href="#"><img src="images/uIEnhancementImages/tab_spp2.gif" alt="SPP" width="42" height="22" onclick="sppEventClicked();" border="0"></a></td><td valign="bottom"><a href="#"><img src="images/uIEnhancementImages/tab_view_surgical2.gif" alt="View Surgical Pathology Report" width="216" height="22" border="0" onclick="viewSPR();"></a></td><td valign="bottom"><a href="#"><img src="images/uIEnhancementImages/tab_view_annotation2.gif" alt="View Annotation" width="116" height="22" border="0" onClick="viewAnnotations(<%=specimenEntityId%>,document.forms[0].id.value,'','<%=staticEntityName%>','<%=pageOf%>')"></a></td><td align="left" valign="bottom" class="td_color_bfdcf3" ><a id="consentViewTab" href="#" onClick="consentTab()"><img src="images/uIEnhancementImages/tab_consents2.gif" alt="Consents" width="76" border="0" height="22" ></a></td>
		        <td width="90%" align="left" valign="bottom" class="td_tab_bg" >&nbsp;
				</td></tr>
		    </table>
	<%
	}
	%>
		<%
	if(pageView.equals("add"))
	{
	%><table width="100%" border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td class="td_tab_bg" ><img src="images/uIEnhancementImages/spacer.gif" alt="spacer" width="50" height="1"></td>
			<td valign="bottom"><a onclick="newSpecimenTab()" id="newSpecimenTab" href="#"><img src="images/uIEnhancementImages/new_specimen_selected.gif" alt="Specimen Details"  width="115" height="22" border="0"></a></td><td onClick="newConsentTab()" valign="bottom" ><a id="newConsentTab" href="#" onClick="consentTab()"><img src="images/uIEnhancementImages/tab_consents2.gif" alt="Consents" width="76" height="22" border="0" ></a></td><td width="90%" class="td_tab_bg" >&nbsp;</td></tr>
		</table>

	<%
	}
	%>
		    <table width="100%" border="0" cellpadding="3" cellspacing="0" class="whitetable_bg">
			<tr>
			<td><div id="mainTable"style="display:block"><table width="100%"  border="0" cellpadding="3" cellspacing="0" >
				<tr>
		          <td><%@ include file="/pages/content/common/ActionErrors.jsp" %></td>
		        </tr>
				<tr>
		          <td align="left" class="showhide">
					<table width="100%" border="0" cellpadding="3" cellspacing="0" >
					<!-- NEW SPECIMEN REGISTRATION BEGINS-->
						<tr>
				<logic:equal name="newSpecimenForm" property="parentPresent" value="false">
		                  <td width="1%" align="center" class="black_ar">
							<span class="blue_ar_b">
								<img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" />
							</span>
						  </td>
		                  <td width="17%" align="left" class="black_ar">
							<label for="specimenCollectionGroupName">
								<bean:message key="newSpecimen.groupName"/>
							</label>
						  </td>
						  <td width="34%" align="left" class="black_ar">
							<html:hidden property="specimenCollectionGroupName" styleId="specimenCollectionGroupName"/>
								<label for="specimenCollectionGroupName">
									<%=form.getSpecimenCollectionGroupName()%>
								</label>
						  </td>


				</logic:equal>


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
				<logic:equal name="newSpecimenForm" property="parentPresent" value="true">
						  <td width="1%" align="center" class="black_ar">
							<span class="blue_ar_b">
								&nbsp;
							</span>
						  </td>
						  <td width="17%" align="left" class="black_ar">
								<label for="parentSpecimenId">
									<bean:message key="newSpecimen.parentLabel"/>
								</label>
    					  </td>
 			        	  <td width="34%" align="left" class="black_ar">
						   		<html:hidden property="specimenCollectionGroupName" styleId="specimenCollectionGroupName"/>
								<html:hidden property="parentSpecimenName"/>
								<label for="parentSpecimenId">
									<% if (isWithinFrame) { 
										out.print("<a class='blue_ar_b' href=\"QuerySpecimenSearch.do?pageOf=pageOfNewSpecimenCPQuery&operation=edit&refresh=false&id="+form.getParentSpecimenId()+"\">"+form.getParentSpecimenName()+"</a>");
									   } else {
										out.print("<a class='blue_ar_b' href=\"SearchObject.do?pageOf=pageOfNewSpecimen&operation=search&id="+form.getParentSpecimenId()+"\">"+form.getParentSpecimenName()+"</a>");										
									   }
									%>
								</label>

 			        	  </td>
				</logic:equal>

				<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.EDIT%>">
						  <td width="1%" align="center">
							<img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" />
						  </td>
						  <td width="17%" align="left" class="black_ar">
							<label for="lineage">
								<bean:message key="specimen.lineage"/>
							</label>
						  </td>
						  <td width="34%" align="left" class="black_ar">
							<label for="lineage">
								<%=form.getLineage()%>
							</label>
						  </td>
				</logic:equal>
						</tr>
						<!--  for GSId -->
						<logic:equal name="newSpecimenForm" property="gsidPresent" value="true">
						<tr>
						<td width="1%" align="center" class="black_ar">
							<span class="blue_ar_b">
								&nbsp;
							</span>
						  </td>
						  <td width="17%" align="left" class="black_ar">
								<label for="globalSpecimenIdentifier">
									<%= GSIDConstant.GSID_UI_LABEL %>
								</label>
    					  </td>
 			        	  <td width="34%" align="left" class="black_ar">
								<!--<html:hidden property="globalSpecimenIdentifer"/>-->
								<html:text styleClass="black_ar" maxlength="255"  size="30" styleId="globalSpecimenIdentifer" property="globalSpecimenIdentifer" 
									readonly="<%=form.isParentPresent() && StringUtils.isBlank(form.getParentSpecimenGSID()) %>"
									onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)"/>
								<label for="globalSpecimenIdentifierValue"> 
								<%
									GSIDClient gsidClient = new GSIDClient();
								    boolean buttonEnabled = gsidClient.isAssignButtonEnabled();
									if(form.isParentPresent())
									{

										if(StringUtils.isBlank(form.getParentSpecimenGSID()))
										{

											if (isWithinFrame)
												out.print("Please first assign a GSID to <a class='blue_ar_b' href=\"QuerySpecimenSearch.do?pageOf=pageOfNewSpecimenCPQuery&operation=edit&refresh=false&id="+form.getParentSpecimenId()+"\">Parent</a>");
											else
												out.print("Please first assign a GSID to <a class='blue_ar_b' href=\"SearchObject.do?pageOf=pageOfNewSpecimen&operation=search&id="+form.getParentSpecimenId()+"\">Parent</a>");
										}
										else
										{
											if (buttonEnabled) {
												out.print("<button class='blue_ar_b' id='updateGSID'>"+GSIDConstant.GSID_ASSIGN_BUTTON_LABEL+"</button>");
											}
										}
									}
									else
									{
										if (buttonEnabled) {
											out.print("<button class='blue_ar_b' id='updateGSID'>"+GSIDConstant.GSID_ASSIGN_BUTTON_LABEL+"</button>");
										}
									}

								%>
								</label>
 			        	  </td>
 			        	  <td colspan="3"></td>
						</tr>
						</logic:equal>


				<% // if( operation.equals(Constants.EDIT) || (!Variables.isSpecimenLabelGeneratorAvl && !Variables.isSpecimenBarcodeGeneratorAvl))
				if(form.getCollectionStatus() != null &&( (!form.getCollectionStatus().equals("Pending") && operation.equals(Constants.EDIT))
				|| (!form.getCollectionStatus().equals("Collected") && operation.equals(Constants.EDIT) && (!form.isGenerateLabel()))
				|| ((!form.isGenerateLabel()) && !Variables.isSpecimenBarcodeGeneratorAvl)))
				{
				%>

						<tr>
							<td align="center" class="black_ar">
				<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">
								<img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" />
				</logic:notEqual>
				<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">&nbsp;
				</logic:equal>
							</td>
							<td align="left" class="black_ar">
								<label for="label">
									<bean:message key="specimen.label"/>
								</label>
							</td>
							<td align="left">
								<html:text styleClass="black_ar" size="30" maxlength="255"  styleId="label" property="label" onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)"/>
							</td>

							<td align="center" class="black_ar">&nbsp;</td>
							<td align="left" class="black_ar">
								<label for="barcode">
									<bean:message key="specimen.barcode"/>
								</label>
							</td>
						<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.EDIT%>" >
							<logic:equal name="newSpecimenForm" property="isBarcodeEditable" value="<%=Constants.FALSE%>" >
								<td width="34%" align="left" class="black_ar">
								<%
								if(form.getBarcode()!=null)
								{
								%>
									<label for="barcode">
										<%=form.getBarcode()%>
									</label>

								<%
								}
								else
								{%>
									<label for="barcode">
									</label>

								<%} %>

								<html:hidden property="barcode" />
								</td>
								</logic:equal>
								<logic:notEqual name="newSpecimenForm" property="isBarcodeEditable" value="<%=Constants.FALSE%>">
								<td align="left">
									<html:text styleClass="black_ar" maxlength="255"  size="30" styleId="barcode" property="barcode" onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)"/>
								</td>
								</logic:notEqual>
							</logic:equal>
							<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.EDIT%>" >
								<td align="left">
									<html:text styleClass="black_ar" maxlength="255"  size="30" styleId="barcode" property="barcode" onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)"/>
								</td>
							</logic:notEqual>
						</tr>

				<%}
							//else  if(((!edu.wustl.catissuecore.util.global.Variables.isSpecimenLabelGeneratorAvl)
								//	&& !form.isGenerateLabel() && Variables.isSpecimenBarcodeGeneratorAvl) && operation.equals(Constants.ADD) )
					else  if(!form.isGenerateLabel() && operation.equals(Constants.ADD) )


				{
				%>

						<tr>
							<td align="center" class="black_ar">
				<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">
								<img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" />
				</logic:notEqual>
				<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">&nbsp;
				</logic:equal>
							</td>
							<td align="left" class="black_ar">
								<label for="label">
									<bean:message key="specimen.label"/>
								</label>
							</td>
							<td align="left">
								<html:text styleClass="black_ar" size="30" maxlength="255"  styleId="label" property="label" />
							</td>
							<td colspan="3">&nbsp;
							</td>
						</tr>
				<%
						 }else if((form.isGenerateLabel()) && operation.equals(Constants.ADD) )
						{

				%>
						<tr>
							<td align="center" class="black_ar">&nbsp;</td>
							<td align="left" class="black_ar">
								<label for="barcode">
									<bean:message key="specimen.barcode"/>
								</label>
							</td>
							<td align="left">
								<html:text styleClass="black_ar" maxlength="255"  size="30" styleId="barcode" property="barcode" />
							</td>
							<td colspan="3">&nbsp;
							</td>
						</tr>
				<%
					}else
					{ %>
							<html:hidden property="barcode" />
					   <%
					}
			 %>

						<tr>
							<td align="center" class="black_ar">
								<span class="blue_ar_b">
									<img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" />
								</span>
							</td>
			                <td align="left" class="black_ar">
								<label for="className">
								    <bean:message key="specimen.type"/>
						     	</label>
							</td>
							<td align="left" class="black_new">
							<input type="hidden" id="initialClassValue" value="<%=form.getClassName()%>" >
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
								  onChange="onTypeChange(this)"
								  readOnly="<%=classReadOnly%>"
								  size="27"
								  styleClass="black_ar"
							    />
							</td>

							<td align="center" class="black_ar">
								<span class="blue_ar_b">
									<img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" />
								</span>
							</td>
			                <td align="left" class="black_ar">
								<label for="type">
								     <bean:message key="specimen.subType"/>
						     	</label>
							</td>
							<td align="left" class="black_new">
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
									<div id="specimenTypeId">
									<autocomplete:AutoCompleteTag property="type"
										  optionsList = "<%=request.getAttribute(Constants.SPECIMEN_TYPE_MAP)%>"
										  initialValue="<%=form.getType()%>" onChange="<%=subTypeFunctionName%>"
										  readOnly="<%=readOnlyForAliquot%>" dependsOn="<%=form.getClassName()%>"
										  size="25"
										  styleClass="black_ar"
										/>
									</div>
								</td>
							</tr>
							<tr>
								<td align="center" class="black_ar">
									<span class="blue_ar_b">
					<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">
										<img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" />
					</logic:notEqual>
					<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">
										&nbsp;
					</logic:equal>
									</span>
								</td>
			                    <td align="left" class="black_ar">
									<label for="tissueSite">
										<bean:message key="specimen.tissueSite"/>
									</label>
								</td>
								<td align="left" class="black_new" noWrap>
									<autocomplete:AutoCompleteTag property="tissueSite" size="27"
										  optionsList = "<%=request.getAttribute(Constants.TISSUE_SITE_LIST)%>"
	 									  initialValue="<%=form.getTissueSite()%>" readOnly="<%=readOnlyForAliquot%>"
										  styleClass="black_ar"
										/>
									<span class="black_ar">
					<%
							String url = "TissueSiteTree.do?pageOf=pageOfTissueSite&propertyName=tissueSite&cdeName=Tissue Site";
					%>					<a href="#"																						onclick="NewWindow('<%=url%>','tissuesite','360','525','no');return							false">
											<img src="images/uIEnhancementImages/ic_cl_diag.gif" alt="Clinical Diagnosis" width="16" height="16" border="0"/></a></span></td>

			                    <td align="center" class="black_ar">
				<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">
									<img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0"/>
				</logic:notEqual>
				<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">
									&nbsp;
				</logic:equal>
								</td>
			                    <td align="left" class="black_ar">
									<label for="tissueSide">
										<bean:message key="specimen.tissueSide"/>
									</label>
								</td>
								<td align="left" class="black_new">
									<autocomplete:AutoCompleteTag property="tissueSide"
										optionsList = "<%=request.getAttribute(Constants.TISSUE_SIDE_LIST)%>"
										initialValue="<%=form.getTissueSide()%>" readOnly="<%=readOnlyForAliquot%>"
										styleClass="black_ar"
										size="25"
										/>
								</td>
							</tr>
							<tr>
								<td align="center" class="black_ar">
				<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">
									<img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" />
				</logic:notEqual>
				<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">
									&nbsp;
				</logic:equal>
								</td>
			                    <td align="left" class="black_ar">
									<label for="pathologicalStatus">
										<bean:message key="specimen.pathologicalStatus"/>
									</label>
								</td>
				<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.EDIT%>">
								<td align="left" class="black_new">
									<autocomplete:AutoCompleteTag property="pathologicalStatus"
										optionsList="<%=request.getAttribute(Constants.PATHOLOGICAL_STATUS_LIST)%>"
										initialValue="<%=form.getPathologicalStatus()%>"
										 readOnly="<%=readOnlyForAliquot%>"
										 styleClass="black_ar"
										 size="27"
										/>
					        	</td>
				</logic:notEqual>
				<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.EDIT%>">
								<td align="left" class="black_new">
									<autocomplete:AutoCompleteTag property="pathologicalStatus"
										optionsList="<%=request.getAttribute(Constants.PATHOLOGICAL_STATUS_LIST)%>"
										initialValue="<%=form.getPathologicalStatus()%>"
										readOnly="<%=readOnlyForAliquot%>"
										styleClass="black_ar"
										size="27"
										/>
						        </td>
				</logic:equal>


				<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.EDIT%>">
								<td align="center" class="black_ar">&nbsp;
								</td>
								<td align="left" class="black_ar">
									<label for="createdDate">
										<bean:message key="specimen.createdDate"/>
									</label>
								</td>
								<td class="black_ar" >
									<ncombo:DateTimeComponent name="createdDate" id="createdDate"
			  							formName="newSpecimenForm"
			  							pattern="<%=CommonServiceLocator.getInstance().getDatePattern()%>"
			  							value='${requestScope.createdDate}'
			  							styleClass="black_ar"/>
										<span class="grey_ar_s">
									<bean:message key="page.dateFormat" /></span>&nbsp;
								</td>
				</logic:equal>
							</tr>
							<tr>
								<td align="center" class="black_ar">
				<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">
									<img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0"/>
				</logic:notEqual>
				<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">
									&nbsp;
				</logic:equal>
								</td>
			                    <td align="left" class="black_ar">
									<label for="quantity">
										<bean:message key="specimen.quantity"/>
									</label>
								</td>
								<td align="left" class="black_ar">
									<html:text styleClass="black_ar" size="10" maxlength="10"  styleId="quantity" property="quantity" style="text-align:right"/>
								     <span id="unitSpan">
										<%=unitSpecimen%>
									 </span>
								     <html:hidden property="unit"/>
								</td>

				                <td align="center" class="black_ar">&nbsp;
								</td>
								<td align="left" class="black_ar">
									<label for="concentration">
										<bean:message key="specimen.concentration"/>
									</label>
								</td>
								<td align="left" class="black_ar">
					<%
						//boolean concentrationDisabled = true;
						/*if(form.getClassName().equals("Molecular") && Constants.ALIQUOT.equals(form.getLineage()))
						{
								//concentrationDisabled = false;
								// Fix for bug #9950
								readOnlyForAll = true;
						}*/
						readOnlyForAll = true;
						if(form.getClassName().equals("Molecular")&& !Constants.ALIQUOT.equals(form.getLineage()))
						{
							readOnlyForAll = false;
						}
					%>
									<html:text styleClass="black_ar" maxlength="10"  size="10" styleId="concentration" property="concentration" style="text-align:right"
							     		readonly="<%=readOnlyForAll%>"/>
										<bean:message key="specimen.concentrationUnit"/>
								</td>
							</tr>
			<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.EDIT%>">
							<tr>
								 <td align="center" class="black_ar">&nbsp;
								 </td>
								 <td align="left" class="black_ar">&nbsp;
								 </td>
								 <td align="left" valign="top">
									<html:checkbox property="available">
									</html:checkbox>
									<span class="black_ar">
										<label for="available">
											<bean:message key="specimen.available" />
										</label>
									</span>
								</td>

								<td align="center" class="black_ar">
				<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">
								<img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" />
				</logic:notEqual>
				<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">&nbsp;
				</logic:equal>
							</td>
							<td align="left" class="black_ar">
								<label for="availableQuantity">
										<bean:message key="specimen.availableQuantity" />
								</label>
							</td>
								<td width="28%" align="left" class="black_ar">
									<html:text styleClass="black_ar" maxlength="10"  size="10"						styleId="availableQuantity" property="availableQuantity"					style="text-align:right" />
									<span id="unitSpan1">
										<%=unitSpecimen%>
									</span>
								</td>
							</tr>
			</logic:equal>

			<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.EDIT%>">
				<html:hidden property="collectionStatus"/>
			</logic:notEqual>
			<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.EDIT%>">
							<tr>
								<td align="center" class="black_ar">
									<img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" />
								</td>
				                <td align="left" class="black_ar">
									<label for="collectionStatus">
									<bean:message key="specimenCollectionGroup.collectionStatus" />

									</label>
								</td>
								<td class="black_new">
									<autocomplete:AutoCompleteTag property="collectionStatus"
										optionsList = "<%=request.getAttribute(Constants.COLLECTIONSTATUSLIST)%>"
										initialValue="<%=form.getCollectionStatus()%>" onChange="<%=strCheckStatus%>"
										styleClass="black_ar"
										size="27"
										/>
								</td>

								<td align="center" class="black_ar">
									&nbsp;
								</td>
				                <td align="left" class="black_ar">
									<label for="activityStatus">
										<bean:message key="participant.activityStatus" />
									</label>
								</td>
								<td align="left" class="black_ar">
								<logic:equal name="newSpecimenForm" property="activityStatus"
									value="<%=Status.ACTIVITY_STATUS_CLOSED.toString()%>">
										<autocomplete:AutoCompleteTag property="activityStatus"
										  optionsList = '${requestScope.specimenActivityStatus}'
										  onChange=''
										  initialValue='<%=form.getActivityStatus()%>'
										  styleClass="black_ar" size="25"/>
								</logic:equal>
								<logic:notEqual name="newSpecimenForm" property="activityStatus"
									value="<%=Status.ACTIVITY_STATUS_CLOSED.toString()%>">
									<label for="activityStatus">
										<%=form.getActivityStatus()%>
									</label>
									<html:hidden property="activityStatus"/>
								</logic:notEqual>
								</td>
							</tr>
						</logic:equal>
							<tr>
								<td width="1%" align="center">
								<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.ADD%>">
									<img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" />
								</logic:equal>
								<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.ADD%>">
								&nbsp;
								</logic:notEqual>
								</td>
			                    <td width="16%" align="left" class="black_ar">
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

										String rowNumber = "1";
										String styClass = "formFieldSized5";
										String tdStyleClass = "customFormField";
										String onChange = "onCustomListBoxChange(this)";
										String className = form.getClassName();
										String sptype = form.getType();

										String collectionProtocolId =null;
										if (request.getAttribute(Constants.COLLECTION_PROTOCOL_ID)==null)
											collectionProtocolId="";
										else
											collectionProtocolId =(String) request.getAttribute(Constants.COLLECTION_PROTOCOL_ID);
										if (className==null)
											className="";
										String frameUrl = "ShowFramedPage.do?pageOf=pageOfSpecimen&amp;selectedContainerName=selectedContainerName&amp;pos1=pos1&amp;pos2=pos2&amp;containerId=containerId"
											+ "&" + Constants.CAN_HOLD_SPECIMEN_CLASS+"="+className
											+ "&" + Constants.CAN_HOLD_SPECIMEN_TYPE+"="+sptype
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
										String autoDisplayStyle= null;
										String manualDisplayStyle=null;
										if(radioSelected == 1)
										{
											dropDownDisable = true;
											textBoxDisable = true;
											autoDisplayStyle = "display:none";
											manualDisplayStyle = "display:none";
										}
										else if(radioSelected == 2)
										{
											textBoxDisable = true;
											autoDisplayStyle = "display:block";
											manualDisplayStyle = "display:none";
										}
										else if(radioSelected == 3)
										{
											dropDownDisable = true;
											autoDisplayStyle = "display:none";
											manualDisplayStyle = "display:block";
										}


									%>

									<%=ScriptGenerator.getJSForOutermostDataTable()%>
									<%//System.out.println("after getJSForOutermostDataTable in specimen jsp");%>
									<%=ScriptGenerator.getJSEquivalentFor(dataMap,rowNumber)%>
									<%//System.out.println("after getJSEquivalentFor in specimen jsp");%>

									<script language="JavaScript" type="text/javascript" src="jss/CustomListBox.js"></script>
									<logic:equal name="newSpecimenForm" property="gsidPresent" value="true">
										<script language="JavaScript" type="text/javascript" src="jss/jquery/jquery-1.5.1.min.js"></script>
										<script language="JavaScript" type="text/javascript" src="jss/jquery/jquery-ui-1.8.10.custom.min.js"></script>
										<script language="JavaScript" type="text/javascript" src="jss/jquery/gsid/updateSpecimen.js"></script>
									</logic:equal>
								<td colspan="4" >
					<!-------Select Box Begins----->

								<table border="0" cellpadding="3" cellspacing="0" width="100%">
								<%
												NewSpecimenForm newSpecimenForm=(NewSpecimenForm)request.getAttribute("newSpecimenForm");
												String showContainer = (String) request.getAttribute("showContainer");


								%>

								<% if( operation.equals("add") || (transferStatus != null && transferStatus.equals("transferred")) || (showContainer!=null&&showContainer.equals("Pending")))
						{%>
								<tr >
							<td width="20%">
									<html:select property="stContSelection" styleClass="black_new"
											styleId="stContSelection" size="1"	onmouseover="showTip(this.id)"
											onmouseout="hideTip(this.id)" onchange="onStorageRadioClickInSpecimen(this)">
											<html:options collection="storageList"
														labelProperty="name" property="value" />
									</html:select>
								</td>
								<td width="80%" >
			<div  id="autoDiv" Style="<%=autoDisplayStyle%>">
									<ncombo:nlevelcombo dataMap="<%=dataMap%>"
														attributeNames="<%=attrNames%>"
														tdStyleClassArray="<%=tdStyleClassArray%>"
														initialValues="<%=initValues%>"
														styleClass = "black_new"
														tdStyleClass = "black_new"
														labelNames="<%=labelNames%>"
														rowNumber="<%=rowNumber%>"
														onChange = "<%=onChange%>"
														formLabelStyle="nComboGroup"
														disabled = "<%=dropDownDisable%>"
														noOfEmptyCombos = "<%=noOfEmptyCombos%>"/>
														</tr>
														</table>
			</div>
			<div  id="manualDiv" style="<%=manualDisplayStyle%>"">
				<table cellpadding="0" cellspacing="0" border="0" >
						<tr>
							<td class="groupelements">
								<html:text styleClass="black_ar"  size="20" styleId="selectedContainerName" onmouseover="showTip(this.id)" property="selectedContainerName" disabled= "<%=textBoxDisable%>"/>
							</td>
							<td class="groupelements">
								<html:text styleClass="black_ar"  size="2" styleId="pos1" property="pos1" disabled= "<%=textBoxDisable%>"/>
							</td>
							<td class="groupelements">
								<html:text styleClass="black_ar"  size="2" styleId="pos2" property="pos2" disabled= "<%=textBoxDisable%>"/>
							</td>
							<td class="groupelements">
								<html:button styleClass="black_ar" property="containerMap" onclick="<%=buttonOnClicked%>" disabled= "<%=textBoxDisable%>">
											<bean:message key="buttons.map"/>
								</html:button>
							</td>
						</tr>
					</table>
			</div>
		</td>
				<!---<td align="center" class="black_ar">&nbsp;</td>-->
		</tr>

		<%}%>
											<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.ADD%>">

											<% if(showContainer!=null&&showContainer.equals("Pending"))
										{
										}
										else{%>
												<%
												if((newSpecimenForm.getTransferStatus()==null || !newSpecimenForm.getTransferStatus().equals("transferred")) &&(newSpecimenForm.getStContSelection()==1)&&(newSpecimenForm.getStorageContainer().equals("")||newSpecimenForm.getStorageContainer().equals("-1"))&&newSpecimenForm.getCollectionStatus().equals("Collected"))
												{%>


													<td class="black_ar" colspan="2" style="width: 15">
														<bean:message key="specimen.virtualLocation" />
													</td>
													<td>
																										</td>
												</tr>
													<%
												}
												else
												{
													%>
													<tr>
														<td colspan="1">

					<logic:equal name="transferStatus" value="">
												<!--div that is not getting popuulated -->
												<div id="fromlocationdiv" display="display:block">														<div id="fromlocationdiv" display="display:block">
														<table cellpadding="0" cellspacing="0" border="0" >
						<tr>
							<td class="groupelements">
															<html:text styleClass="black_ar"  size="25" styleId="selectedContainerName" onmouseover="showTip(this.id)" property="selectedContainerName" readonly= "true"/>
														</td>
							<td class="groupelements">
															<html:text styleClass="black_ar"  size="2" styleId="positionDimensionOne" property="positionDimensionOne" readonly= "true" style="text-align:right"/>
											</td>
							<td class="groupelements">
															<html:text styleClass="black_ar"  size="2" styleId="positionDimensionTwo" property="positionDimensionTwo" readonly= "true" style="text-align:right"/>
													</td>
							<td class="groupelements">
															<html:button styleClass="black_ar" property="containerMap" onclick="<%=buttonOnClicked%>" disabled= "true">
																<bean:message key="buttons.map"/>
															</html:button>
													</td>
												</tr>
											</table>
											</div>
										</logic:equal>

														</td>
													</tr>
													<%
												}
										}%>

											</logic:notEqual>



									</table>
											</td>
											</tr>

									<%//System.out.println("End of tag in jsp");%>
									<%-- n-combo-box end --%>

								<logic:equal name="exceedsMaxLimit" value="true">
								<tr>
									<td colspan="6" class="black_ar>
											<bean:message key="container.maxView"/>
									</td>
								</tr>
								</logic:equal>

								<tr>
								<td align="left">&nbsp;</td>
									<td align="left" valign="top" class="black_ar">
										<label for="comments">
											<bean:message key="specimen.comments"/>
										</label>
									</td>

								 	<td align="left" valign="top" colspan="4">
										<html:textarea styleClass="black_ar_s"  rows="3" cols="90" styleId="comments" property="comments"/>
									</td>
								</tr>
							</table>
							</td>
							</tr>
							<!-- collectionstatus -->

							<tr>
							<td width="100%" class="bottomtd">

							<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.ADD%>">


							</logic:equal>


		  <!--------------------------including External identifier.jsp ----------------------------------->
							<%@ include file="ExternalIdentifiers.jsp" %>
							</td></tr>

		<tr>
		<td>
							<%@ include file="BioHazards.jsp" %>
							</td>
							</tr>
							</table>
							</div>
							</td>
							</tr>
							<!--  Consent Tracking Module Virender mehta	 -->								<tr>
							<td class="bottomtd"><div id="consentTable" style="display:none">
								<%
								List requestParticipantResponse = (List)request.getAttribute(Constants.SPECIMEN_RESPONSELIST);
								if(requestParticipantResponse!=null&&form.getConsentTierCounter()>0)
								{
								%>
								    <%@ include file="/pages/content/ConsentTracking/ConsentTracking.jsp" %>
								<%
								}
								%>
								</div></td>
							<!--  Consent Tracking Module Virender mehta	 -->
			 				</tr>

        <tr>
          <td valign="middle" class="tr_bg_blue1"><span class="blue_ar_b">&nbsp;<bean:message key="childSpecimen.label" /></span></td>
        </tr>

								<tr>
								<!--
          <td valign="middle" class="black_ar" >
		  -->
		  <td valign="top">
		  <table width="100%" border="0" cellpadding="3" cellspacing="0">

                <tr>
                  <td width="17%" align="left" nowrap class="black_ar" colspan="2">
						<input type="radio" value="1" id="aliquotCheck" name="specimenChild" onclick="onCheckboxButtonClick(this)" checked="true"/>
						<bean:message key="app.none" />&nbsp;
						<input type="radio" value="2" id="aliquotChk" name="specimenChild" onclick="onCheckboxButtonClick(this)"/>
										<bean:message key="aliquots.title"/>
									&nbsp;
										<input type="radio" value="3" id="deriveChk" name="specimenChild" onclick="onCheckboxButtonClick(this)"/>
										<bean:message key="specimen.derivative" />
									&nbsp;
										<!-- 11706 S Desctiption : Remove equal check for Edit operation only....-->
										<input type="radio" value="4" id="createCpChildCheckBox" name="specimenChild" onclick="onCheckboxButtonClick(this)"/>
										<bean:message key="create.CpChildSp"/>
										<!-- 11706 E -->
									</td>
								</tr>


							<!--specimenPageButton-->

								<tr><td colspan="2"></td></tr>

								<tr>
								<td class="black_ar" width="18%" nowrap>
										 <div style="display:none" id="derivedDiv">
										 <bean:message key="summary.page.count" />&nbsp;
										<html:text styleClass="black_ar" styleId="numberOfSpecimens" size="10" property="numberOfSpecimens" style="text-align:right"/></div>
										<div style="display:block" id="aliquotDiv"><bean:message key="summary.page.count" />&nbsp;
										<html:text styleClass="black_ar" styleId="noOfAliquots" size="10" property="noOfAliquots" disabled="true" style="text-align:right"/></div>
										</td>
										<td class="black_ar" width="75%">
						                <bean:message key="aliquots.qtyPerAliquot"/>&nbsp;

										<html:text styleClass="black_ar" styleId="quantityPerAliquot" size="10" property="quantityPerAliquot" disabled="true" style="text-align:right"/>

								    </td>
								</tr>

							<!--	//The check box is changed to radio button
								<tr>
											<td class="dividerline" colspan="2" valign="center">
													<html:checkbox styleId="createCpChildCheckBox" property="createCpChildCheckBox" value="true" onclick="">
														<span class="black_ar">

														<bean:message key="create.CpChildSp"/>

														</span>
														</html:checkbox>
											</td>
								</tr>					-->


 							       <tr>
								<td class="dividerline" colspan="3"><span class="black_ar"></td>
								</tr>
								<tr>
								<logic:notEqual name="<%=Constants.PAGE_OF%>" value="<%=Constants.QUERY%>">

											<td colspan="1" valign="center">
													<html:checkbox styleId="printCheckbox" property="printCheckbox" value="true" onclick="showPriterTypeLocation()">
														<span class="black_ar">
															<bean:message key="print.checkboxLabel"/>
														</span>
														</html:checkbox>
											</td>

								</logic:notEqual>
	<!--  Added for displaying  printer type and location -->
								  <td>
					   			     <%@ include file="/pages/content/common/PrinterLocationTypeComboboxes.jsp" %>
			 				        </td>

			 				</tr>
			<!--  End : Displaying   printer type and location -->
							<tr>
          <td class="bottomtd"></td>
        </tr>
								<!-- Bio-hazards End here -->
						   	 	<tr>
							  		<td align="left" colspan="2" class="buttonbg">
										<%
											String changeAction = "setFormAction('"+formName+"')";
							 			%>
										<%@ include file="NewSpecimenPageButtons.jsp" %>
							  		</td>
							 	</tr>

<!-- NEW SPECIMEN REGISTRATION ends-->
				</table>
			</td>
		</tr>
		<tr>
			<td height="*">&nbsp;</td>
		</tr>

		</table>
								</td>
								</tr>
	</table>

<html:hidden property="stContSelection"/>
<html:hidden property="lineage"/>
<html:hidden property="nextForwardTo" />
<html:hidden property="restrictSCGCheckbox"/>
</html:form>
<script language="JavaScript" type="text/javascript">
showPriterTypeLocation();
</script>
</body>
</body>
</body>

