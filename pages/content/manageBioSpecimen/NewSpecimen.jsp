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
<%@ page import="edu.wustl.catissuecore.util.HelpXMLPropertyHandler"%>
<%@ include file="/pages/content/common/BioSpecimenCommonCode.jsp" %>
<%@ include file="/pages/content/common/AutocompleterCommon.jsp" %>

<%
String pageOf = (String)request.getAttribute(Constants.PAGE_OF);
List<String[]> initValueForContainer = (List<String[]>)request.getAttribute("initValues");
String[] containerValues=initValueForContainer.get(0);
String containerName=containerValues[0];
String pos1=containerValues[1];
String pos2=containerValues[2];
String collectionProtocolId ="";
String showSpecList = (String)request.getAttribute("showSpecList");
		if (request.getAttribute(Constants.COLLECTION_PROTOCOL_ID)==null)
			collectionProtocolId="";
		else
		 collectionProtocolId =(String) request.getAttribute(Constants.COLLECTION_PROTOCOL_ID);	
%>
<script type="text/javascript" src="jss/tag-popup.js"></script>
<link rel="STYLESHEET" type="text/css" href="dhtmlx_suite/dhtml_pop/css/dhtmlXTree.css">
<script src="dhtmlx_suite/dhtml_pop/js/dhtmlXCommon.js"></script>
<link rel="STYLESHEET" type="text/css"
	href="dhtmlx_suite/dhtml_pop/css/dhtmlXGrid.css" />
<link rel="STYLESHEET" type="text/css"
	href="dhtmlx_suite/dhtml_pop/css/dhtmlxgrid_dhx_skyblue.css" />
<script src="dhtmlx_suite/dhtml_pop/js/dhtmlx.js"></script>
<script src="dhtmlx_suite/dhtml_pop/js/dhtmlXTree.js"></script>
<script src="dhtmlx_suite/dhtml_pop/js/dhtmXTreeCommon.js"></script>
<script src="dhtmlx_suite/dhtml_pop/js/dhtmlXGridCell.js"></script>

<link rel="stylesheet" type="text/css" href="css/tag-popup.css" />

<link rel="stylesheet" type="text/css" href="dhtmlx_suite/css/dhtmlxwindows.css">
<link rel="stylesheet" type="text/css" href="dhtmlx_suite/skins/dhtmlxwindows_dhx_skyblue.css">

<script src="dhtmlx_suite/js/dhtmlxcommon.js"></script>
<script src="dhtmlx_suite/js/dhtmlxcontainer.js"></script>
<script src="dhtmlx_suite/js/dhtmlxwindows.js"></script>
<link rel="stylesheet" type="text/css"	href="dhtmlx_suite/css/dhtmlxtree.css">
<link rel="STYLESHEET" type="text/css"	href="dhtmlx_suite/css/dhtmlxgrid.css">
<link rel="STYLESHEET" type="text/css" href="css/dhtmlDropDown.css">
<link rel="STYLESHEET" type="text/css"	href="dhtmlx_suite/css/dhtmlxcombo.css">
<link rel="STYLESHEET" type="text/css"	href="dhtmlx_suite/ext/dhtmlxgrid_pgn_bricks.css">
<link rel="STYLESHEET" type="text/css"	href="dhtmlx_suite/skins/dhtmlxtoolbar_dhx_blue.css">
<script language="JavaScript" type="text/javascript" src="jss/dhtmlDropDown.js"></script>

<script src="dhtmlx_suite/js/dhtmlxcombo.js"></script>
<script src="dhtmlx_suite/js/dhtmlxtree.js"></script>
<script src="dhtmlx_suite/ext/dhtmlxtree_li.js"></script>
<script src="jss/script.js" type="text/javascript"></script>
<script language="JavaScript" type="text/javascript"	src="jss/javaScript.js"></script>
<script language="JavaScript" type="text/javascript"	src="jss/caTissueSuite.js"></script>
<script src="jss/calendarComponent.js" language="JavaScript"	type="text/javascript"></script>
<script>var imgsrc="images/de/";</script>

<SCRIPT>var imgsrc="images/";</SCRIPT>
<LINK href="css/calanderComponent.css" type=text/css rel=stylesheet>
<script language="JavaScript" type="text/javascript"	src="javascripts/de/prototype.js"></script>
<script language="JavaScript" type="text/javascript"	src="javascripts/de/scr.js"></script>
<script language="JavaScript" type="text/javascript"	src="jss/ext-base.js"></script>
<script language="JavaScript" type="text/javascript"	src="jss/ext-all.js"></script>
<script language="JavaScript" type="text/javascript"	src="javascripts/de/ajax.js"></script>
<script language="JavaScript" type="text/javascript"	src="/jss/multiselectUsingCombo.js"></script>
<LINK href="css/catissue_suite.css" type="text/css" rel="stylesheet">
<link rel="stylesheet" type="text/css" href="css/styleSheet.css" />
<link rel="stylesheet" type="text/css"	href="css/clinicalstudyext-all.css" />
<script language="JavaScript" type="text/javascript" src="jss/antiSpecAjax.js"></script>
<script language="JavaScript" type="text/javascript" src="jss/GenericSpecimenDetailsTag.js"></script>
<script src="jss/ajax.js" type="text/javascript"></script>

<script type="text/javascript" src="dhtmlx_suite/js/dhtmlxgrid.js"></script>
<script src="dhtmlx_suite/dhtml_pop/js/dhtmlXTreeGrid.js"></script>
<script type="text/javascript" src="dhtmlx_suite/js/dhtmlxgridcell.js"></script>
<script type="text/javascript" src="dhtmlx_suite/js/connector.js"></script>
<script type="text/javascript" src="dhtmlx_suite/ext/dhtmlxgrid_filter.js"></script>
<script type="text/javascript" src="dhtmlx_suite/ext/dhtmlxgrid_pgn.js"></script>
<script type="text/javascript" src="dhtmlx_suite/js/dhtmlxtoolbar.js"></script>
<script language="JavaScript" type="text/javascript" src="jss/newSpecimen.js"></script>
<script language="JavaScript" type="text/javascript" src="jss/commonSpecimen.js"></script>

<script src="dhtmlx_suite/js/dhtmlxcalendar.js"></script>
<link rel="stylesheet" type="text/css" href="dhtmlx_suite/skins/dhtmlxcalendar_dhx_skyblue.css" />
<link rel="stylesheet" type="text/css" href="dhtmlx_suite/css/dhtmlxcalendar.css" />
<script>
	function updateHelpURL()
	{
		var URL="";
		if("pageOfNewSpecimenCPQuery"=="<%=pageOf%>")
		{
			URL="<%=HelpXMLPropertyHandler.getValue("edu.wustl.catissuecore.actionForm.NewSpecimenForm")%>";
		}
		return URL;
	}
	

</script>
<%
	List biohazardList = (List)request.getAttribute(Constants.BIOHAZARD_TYPE_LIST);
	NewSpecimenForm form = (NewSpecimenForm)request.getAttribute("newSpecimenForm");
	String frdTo = form.getForwardTo();
	String nodeId="";
	String tab = (String)request.getAttribute(Constants.SELECTED_TAB);
	String exceedsMaxLimit = (String)request.getAttribute(Constants.EXCEEDS_MAX_LIMIT);
	String submittedFor=(String)request.getAttribute(Constants.SUBMITTED_FOR);
	//String pageOf = (String)request.getAttribute(Constants.PAGE_OF);
	boolean isAddNew = false;
	String signedConsentDate = "";
	String selectProperty="";
	String operation = (String)request.getAttribute(Constants.OPERATION);
	String reqPath = (String)request.getAttribute(Constants.REQ_PATH);
	String appendingPath = "/NewSpecimen.do?operation=add&pageOf=pageOfNewSpecimen";
	String currentReceivedDate = "";
	String currentCollectionDate = "";
	StringBuffer specimenPositionAsString=new StringBuffer();
	specimenPositionAsString.append(form.getSelectedContainerName())
	.append(" (")
	.append(form.getPos1())
	.append(",")
	.append(form.getPos2()).append(")");

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

	   	if(!(Constants.ADD).equals(operation))
	   	{
	   		if(form != null)
	   		{
		   		appendingPath = "/NewSpecimenSearch.do?operation=search&pageOf=pageOfNewSpecimen&id="+form.getId() ;
		   		//System.out.println("---------- NSP JSP -------- : "+ appendingPath);
		   	}
	   	}

	Map map = form.getExternalIdentifier();
	Long reportId=(Long)session.getAttribute(Constants.IDENTIFIED_REPORT_ID);
%>
<head>
<script language="JavaScript" type="text/javascript" src="jss/Hashtable.js"></script>
<script language="JavaScript" type="text/javascript" src="jss/ajax.js"></script>

<%
	String[] columnList = (String[]) request.getAttribute(Constants.SPREADSHEET_COLUMN_LIST);
	List dataList = (List) request.getAttribute(edu.wustl.simplequery.global.Constants.SPREADSHEET_DATA_LIST);
	//String pageOf = (String)request.getAttribute(Constants.PAGE_OF);

	String formName,pageView=operation,editViewButton="buttons."+Constants.EDIT;
	boolean readOnlyValue=false,readOnlyForAll=false;

	if(Constants.EDIT.equals(operation))
	{
		editViewButton="buttons."+Constants. VIEW;
		formName = Constants.SPECIMEN_EDIT_ACTION;
		readOnlyValue=true;
		if(Constants.QUERY.equals(pageOf))
			formName = Constants.QUERY_SPECIMEN_EDIT_ACTION + "?pageOf="+pageOf;
		if(Constants.PAGE_OF_SPECIMEN_CP_QUERY.equals(pageOf))
		{
			formName = Constants.CP_QUERY_SPECIMEN_EDIT_ACTION + "?pageOf="+pageOf;
		}
		nodeId= "Specimen_"+form.getId();
	}
	else
	{
		formName = Constants.SPECIMEN_ADD_ACTION;
		readOnlyValue=false;
		if(Constants.PAGE_OF_SPECIMEN_CP_QUERY.equals(pageOf))
		{
			formName = Constants.CP_QUERY_SPECIMEN_ADD_ACTION + "?pageOf="+pageOf;
		}
		nodeId= "SpecimenCollectionGroup_"+form.getSpecimenCollectionGroupId();
	}

	String formNameForCal = "newSpecimenForm";
	String className = form.getClassName();
	String sptype = form.getType();
	if (className==null)
			className="";
	String frameUrl="";
%>
<%@ include file="/pages/content/common/SpecimenCommonScripts.jsp" %>

	<%
	String refreshTree = (String)request.getAttribute("refresh");
	if(Constants.PAGE_OF_SPECIMEN_CP_QUERY.equals(pageOf) && (refreshTree==null || !(refreshTree.equalsIgnoreCase("false"))))
	{
		strCheckStatus= "checkActivityStatus(this,'" + Constants.CP_QUERY_BIO_SPECIMEN + "')";
	%>
		<script language="javascript">

	refreshTree('<%=Constants.CP_AND_PARTICIPANT_VIEW%>','<%=Constants.CP_TREE_VIEW%>','<%=Constants.CP_SEARCH_CP_ID%>','<%=Constants.CP_SEARCH_PARTICIPANT_ID%>','<%=nodeId%>');
		</script>
	<%}
%>

<script language="JavaScript">
var scGridVisible = false;

	function loadDHTMLXWindowForTransferEvent()
{
	var w =700;
	var h =450;
	var x = (screen.width / 3) - (w / 2);
	var y = 0;
	dhxWins.createWindow("containerPositionPopUp", x, y, w, h);
	var url = "ShowStoragePositionGridView.do?pageOf=pageOfSpecimen&forwardTo=gridView&pos1=pos1&pos2=pos2&holdSpecimenClass=<%=form.getClassName()%>&holdSpecimenType=<%=form.getType()%>&containerName=<%=form.getSelectedContainerName()%>&collectionProtocolId=<%=collectionProtocolId%>";
	dhxWins.window("containerPositionPopUp").attachURL(url);                      //url : either an action class or you can specify jsp page path directly here
	dhxWins.window("containerPositionPopUp").button("park").hide();
	dhxWins.window("containerPositionPopUp").allowResize();
	dhxWins.window("containerPositionPopUp").setModal(true);
	dhxWins.window("containerPositionPopUp").setText("");    //it's the title for the popup
}
	function loadDHTMLXWindowForNewSpecimen()
{
	var w =700;
	var h =450;
	var x = (screen.width / 3) - (w / 2);
	var y = 0;
	var className = document.getElementById('className').value;
	var spType = document.getElementById('type').value;
	var selectedContName = document.getElementById('storageContainerDropDown').value;
	//alert(className +' , '+spType+' , '+selectedContName);
	
	dhxWins.createWindow("containerPositionPopUp", x, y, w, h);
	var url = "ShowStoragePositionGridView.do?pageOf=pageOfNewSpecimen&forwardTo=gridView&pos1=pos1&pos2=pos2&holdSpecimenClass="+className+"&holdSpecimenType="+spType+"&containerName="+selectedContName+"&collectionProtocolId=<%=collectionProtocolId%>";
	dhxWins.window("containerPositionPopUp").attachURL(url);                      //url : either an action class or you can specify jsp page path directly here
	dhxWins.window("containerPositionPopUp").button("park").hide();
	dhxWins.window("containerPositionPopUp").allowResize();
	dhxWins.window("containerPositionPopUp").setModal(true);
	dhxWins.window("containerPositionPopUp").setText("");    //it's the title for the popup
}
function showPopUp() 
{
	var storageContainer =document.getElementById("storageContainerDropDown").value;
    if(storageContainer!="")
	{
		loadDHTMLXWindowForNewSpecimen();
	}
	else
	{
		var className=document.getElementById("className").value;
		var sptype=document.getElementById("type").value;
		var frameUrl="ShowFramedPage.do?pageOf=pageOfSpecimen&selectedContainerName=storageContainerDropDown&pos1=pos1&pos2=pos2&containerId=containerId"
						+ "&holdSpecimenClass="+className+ "&holdSpecimenType="+sptype	+ "&holdCollectionProtocol=" + '<%=collectionProtocolId%>';
		mapButtonClickedOnNewSpecimen(frameUrl,'newSpecimenPage');
	}
}
function onContainerListReady()
{
		var containerName = '${newSpecimenForm.selectedContainerName}';
		if(containerName != "" && containerName != 0 && containerName != null)
			containerOnRowSelect(containerName,0);
}
	
function getActionToDoURL()
{
	var className=document.getElementById("className").value;
	var sptype=document.getElementById("type").value;
	var collectionProtocolId="<%=collectionProtocolId%>";
	var url="CatissueCommonAjaxAction.do?type=getStorageContainerList&<%=Constants.CAN_HOLD_SPECIMEN_CLASS%>="+className+"&specimenType="+sptype+ "&<%=Constants.CAN_HOLD_COLLECTION_PROTOCOL%>=" + collectionProtocolId+"&stContSelection="+"<%=form.getStContSelection()%>";
	return url;
}

	function deleteExternalIdentifiers()
	{
	<%if(!(Constants.PAGE_OF_SPECIMEN_CP_QUERY).equals(pageOf))
	{%>
		deleteChecked('addExternalIdentifier','NewSpecimen.do?operation=<%=operation%>&pageOf=pageOfNewSpecimen&status=true&button=deleteExId',document.forms[0].exIdCounter,'chk_ex_',false);
	<%} else {%>
		deleteChecked('addExternalIdentifier','CPQueryNewSpecimen.do?operation=<%=operation%>&pageOf=pageOfNewSpecimenCPQuery&status=true&button=deleteExId',document.forms[0].exIdCounter,'chk_ex_',false);
	<%}%>
	}
	
	function deleteBioHazards()
	{
	<%if(!(Constants.PAGE_OF_SPECIMEN_CP_QUERY).equals(pageOf))
	{%>
		deleteChecked('addBiohazardRow','NewSpecimen.do?operation=<%=operation%>&pageOf=pageOfNewSpecimen&status=true&button=deleteBiohazard',document.forms[0].bhCounter,'chk_bio_',false);
	<%} else {%>
		deleteChecked('addBiohazardRow','CPQueryNewSpecimen.do?operation=<%=operation%>&pageOf=pageOfNewSpecimenCPQuery&status=true&button=deleteBiohazard',document.forms[0].bhCounter,'chk_bio_',false);
	<%}%>
	}

	function onDeriveSubmit()
	{
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
				    if(Constants.PAGE_OF_SPECIMEN_CP_QUERY.equals(pageOf))
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
				    if(Constants.PAGE_OF_SPECIMEN_CP_QUERY.equals(pageOf))
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
				    if(Constants.PAGE_OF_SPECIMEN_CP_QUERY.equals(pageOf))
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
				    if(Constants.PAGE_OF_SPECIMEN_CP_QUERY.equals(pageOf))
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
					if(Constants.PAGE_OF_SPECIMEN_CP_QUERY.equals(pageOf))
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
					if(Constants.PAGE_OF_SPECIMEN_CP_QUERY.equals(pageOf))
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
						if(Constants.PAGE_OF_SPECIMEN_CP_QUERY.equals(pageOf))
						{
							actionToCall = Constants.CP_QUERY_SPECIMEN_EDIT_ACTION+"?pageOf=pageOfNewSpecimenCPQuery";
							cpChildSubmitAction = "CPQueryPrintSpecimenEdit";//Constants.CP_QUERY_SPECIMEN_EDIT_ACTION;

						}%>
						//added this for print specimen when createCpChildCheckBox is clicked
						var printFlag = document.getElementById("printCheckbox");
			            if(printFlag.checked)
					    {
			            	<% if(Constants.PAGE_OF_SPECIMEN_CP_QUERY.equals(pageOf))
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
						if(Constants.PAGE_OF_SPECIMEN_CP_QUERY.equals(pageOf))
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
							if('<%=actionToCall%>'.indexOf('?')==-1)
							{
								confirmDisable('<%=actionToCall%>'+"?nextForwardTo="+document.forms[0].nextForwardTo.value,document.forms[0].activityStatus);
							}
							else
							{
								confirmDisable('<%=actionToCall%>'+"&nextForwardTo="+document.forms[0].nextForwardTo.value,document.forms[0].activityStatus);
							}
						}
					}
					else //none + submit
					{
						<%
						actionToCall = "NewSpecimenEdit.do";
						forwardToValue = "PrintSpecimenEdit";
						if(Constants.PAGE_OF_SPECIMEN_CP_QUERY.equals(pageOf))
						{
							actionToCall = Constants.CP_QUERY_SPECIMEN_EDIT_ACTION+"?pageOf=pageOfNewSpecimenCPQuery";
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
	/*
	    Added separate mappings for pages opened from CP based view(without menubar) and
	    Biospecimen data -> Specimen (with menubar)
	*/

 <%--    function onAddToCart()
	{
	    <% String actionToCall1 = "NewSpecimenEdit.do";
	       String nextForwardToForAddToCart = "";
	       String forwardToPath = "";
	    %>
	    var operation = document.forms[0].operation.value;
	    if(document.getElementById("aliquotChk").checked == true)
		 {
	    	<%forwardToPath = "addSpecimenToCartForwardtoAliquot";
	  			if((Constants.PAGE_OF_SPECIMEN_CP_QUERY) == pageOf)
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
			   if((Constants.PAGE_OF_SPECIMEN_CP_QUERY) == pageOf)
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

			 if((Constants.PAGE_OF_SPECIMEN_CP_QUERY) == pageOf )
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
		if(Constants.PAGE_OF_SPECIMEN_CP_QUERY.equals(pageOf))
		{
			actionToCall1 = Constants.CP_QUERY_SPECIMEN_EDIT_ACTION;
		}%>
		confirmDisable('<%=actionToCall1%>',document.forms[0].activityStatus);
	} --%>
	
	</script>
	<link href="css/catissue_suite.css" rel="stylesheet" type="text/css" />
	<link href="css/styleSheet.css" rel="stylesheet" type="text/css" />
</head>
<%
if(showSpecList.equals("true"))
						{%>
<body onload="initWindow();doOnLoad();loadContainerValues('<%=form.isVirtuallyLocated()%>');setContainerValues('<%=containerName%>','<%=pos1%>','<%=pos2%>');">
<%
}
else
{%>
<body onload="initWindow();doOnLoad();loadContainerValues('<%=form.isVirtuallyLocated()%>');setContainerValues('<%=containerName%>','<%=pos1%>','<%=pos2%>');">
<%
}		int exIdRows=1;
		int bhRows=1;
		String unitSpecimen = "";
		if(form != null)
		{
			exIdRows = form.getExIdCounter();
			bhRows	 = form.getBhCounter();
			if(form.getClassName().equals("Tissue"))
				{
					//Mandar : 25-Apr-06 :Bug 1414
					if((form.getType()!=null) && (Constants.FROZEN_TISSUE_SLIDE.equals(form.getType()))|| (Constants.FIXED_TISSUE_BLOCK.equals(form.getType()))|| (Constants.FROZEN_TISSUE_BLOCK.equals(form.getType()))|| (Constants.NOT_SPECIFIED.equals(form.getType()))|| (Constants.FIXED_TISSUE_SLIDE.equals(form.getType())))
					{
						unitSpecimen = Constants.UNIT_CN;
					}
					else if((form.getType()!=null) && (Constants.MICRODISSECTED.equals(form.getType())))
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
				if(Constants.EDIT.equals(operation))
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
								<%
								if(form.getForwardTo().equalsIgnoreCase("orderDetails"))
								{%>
								 	<html:hidden property="forwardTo" value="orderDetails"/>
						 	  <%}else
						    	{ %>
								<html:hidden property="forwardTo" value=""/>
							  <%} %>
							  <html:hidden property="generateLabel"/>
							  <html:hidden property="virtuallyLocated" styleId="virtuallyLocated"/>
	
								<html:hidden property="containerId" styleId="containerId"/>
								<html:hidden property="withdrawlButtonStatus"/>
								<html:hidden property="stringOfResponseKeys"/>
								<html:hidden property="applyChangesTo"/>
								<html:hidden property="parentSpecimenId"/>
								<html:hidden property="onSubmit"/>
								<html:hidden property="id"/>
								<html:hidden property="positionInStorageContainer" />
								<html:hidden property="parentPresent" />
								<html:hidden property="specimenCollectionGroupId"/>
								<html:hidden property="checkedButton"/>
								<html:hidden property="derivedClicked"/>

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
		      <tr>
				<td class="td_tab_bg" >
					<img src="images/uIEnhancementImages/spacer.gif" alt="spacer" width="50" height="1"></td>
						<td valign="bottom">
							<a onclick="newspecimenPage()" id="specimenDetailsTab" href="#">	
								<img src="images/uIEnhancementImages/tab_specimen_details1.gif" alt="Specimen Details"  width="126" height="22" border="0">
							</a>
						</td>
					<td valign="bottom">
					<a href="#">
						<img src="images/uIEnhancementImages/tab_events2.gif" alt="Events" width="56" height="22" onclick="eventClicked('<%=pageOf%>');" border="0"></a></td><td valign="bottom"><a href="#"><img src="images/uIEnhancementImages/tab_view_surgical2.gif" alt="View Surgical Pathology Report" width="216" height="22" border="0" onclick="viewSPR('${identifiedReportId}','${pageOf}');"></a></td><td valign="bottom"><a href="#"><img src="images/uIEnhancementImages/tab_view_annotation2.gif" alt="View Annotation" width="116" height="22" border="0" onClick="viewAnnotations(<%=specimenEntityId%>,document.forms[0].id.value,'','<%=staticEntityName%>','<%=pageOf%>')"></a></td><td align="left" valign="bottom" class="td_color_bfdcf3" >
							<a id="consentViewTab" href="#" onClick=""><img src="images/uIEnhancementImages/tab_consents2.gif" alt="Consents" width="76" border="0" height="22" >
					</a>
					</td>
					<td width="90%" align="left" valign="bottom" class="td_tab_bg" >&nbsp;
					</td>
				</tr>
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
			<td valign="bottom">
				<a onclick="newSpecimenTab()" id="newSpecimenTab" href="#">
					<img src="images/uIEnhancementImages/new_specimen_selected.gif" alt="Specimen Details"  width="115" height="22" border="0">
				</a>
			</td>
			
			<td width="90%" class="td_tab_bg" >&nbsp;</td>
		</tr>
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
										if(Constants.EDIT.equals(operation))
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
									<%=form.getParentSpecimenName()%>
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
				<% // if( operation.equals(Constants.EDIT) || (!Variables.isSpecimenLabelGeneratorAvl && !Variables.isSpecimenBarcodeGeneratorAvl))
				if(form.getCollectionStatus() != null &&( (!form.getCollectionStatus().equals("Pending") && Constants.EDIT.equals(operation))
				|| (!form.getCollectionStatus().equals("Collected") && Constants.EDIT.equals(operation) && (!form.isGenerateLabel()))
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
								//	&& !form.isGenerateLabel() && Variables.isSpecimenBarcodeGeneratorAvl) && Constants.ADD.equals(operation))
					else  if(!form.isGenerateLabel() && Constants.ADD.equals(operation))
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
						 }else if((form.isGenerateLabel()) && (Constants.ADD).equals(operation) )
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
							if(Constants.EDIT.equals(operation))
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
												String subTypeFunctionName ="onSubTypeChangeUnit('className',this,'unitSpan');doOnLoad();";
												String readOnlyForAliquot = "false";
												if(Constants.ALIQUOT.equals(form.getLineage())&& (Constants.EDIT).equals(operation)) {
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
								<html:text property="createdDate" styleClass="black_ar"
							       styleId="createdDate" size="10" value='${requestScope.createdDate}'
                                   onclick="doInitCalendar('createdDate',false,'${uiDatePattern}');" />
							   	<span class="grey_ar_s capitalized">[<bean:message key="date.pattern" />]</span>&nbsp;
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
				                <td align="center" class="black_ar">&nbsp;</td>
								<td align="left" class="black_ar">
									<label for="concentration">
										<bean:message key="specimen.concentration"/>
									</label>
								</td>
								<td align="left" class="black_ar">
					<%
						//boolean concentrationDisabled = true;
						readOnlyForAll = true;
						if(form.getClassName().equals("Molecular")&& !(Constants.ALIQUOT).equals(form.getLineage()))
						{
							readOnlyForAll = false;
						}
					%>
									<html:text styleClass="black_ar" maxlength="10"  size="10" styleId="concentration" property="concentration" style="text-align:right"
							     		readonly="<%=readOnlyForAll%>" disabled="false"/>
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
										if(Constants.ADD.equals(operation))
											readOnly=false;
									%>

									<%-- n-combo-box start --%>
									<%
										Map dataMap = (Map) request.getAttribute(Constants.AVAILABLE_CONTAINER_MAP);

										String[] labelNames = {"ID","Pos1","Pos2"};
										labelNames = Constants.STORAGE_CONTAINER_LABEL;
										String[] attrNames = { "storageContainer", "positionDimensionOne", "positionDimensionTwo"};
							            String[] tdStyleClassArray = { "formFieldSized15", "customFormField", "customFormField"};
										
										
										List containerList=(List)request.getAttribute("containerList");
										String rowNumber = "1";
										String styClass = "formFieldSized5";
										String tdStyleClass = "customFormField";
										String onChange = "onCustomListBoxChange(this)";

										/**
		                  				* bug ID: 4225
		                 				* Patch id: 4225_2
		                  				* Description : Passing a different name to the popup window
		                 				 */
										 String forward_to_action = form.getForwardTo();
										 
										frameUrl="ShowStoragePositionGridView.do?pageOf=pageOfSpecimen&amp;forwardTo=gridView&amp;pos1=pos1&amp;pos2=pos2";
										String buttonOnClicked = "mapButtonClickedOnNewSpecimen('"+frameUrl+"','newSpecimenPage')";
										//getContainerPositions();
										System.out.println(buttonOnClicked);
										//String buttonOnClicked = "mapButtonClickedOnNewSpecimen('"+frameUrl+"','newSpecimenPage')";

										//"javascript:NewWindow('"+frameUrl+"','name','800','600','no');return false";
										//javascript:NewWindow('"+frameUrl+"','name','800','600','no');return false";
										String noOfEmptyCombos = "3";

										boolean disabled = false;
										boolean buttonDisabled = false;
										if(request.getAttribute("disabled") != null && request.getAttribute("disabled").equals("true"))
										{
											disabled = true;
										}
										boolean isVirtuallyLocated = form.isVirtuallyLocated();
										boolean dropDownDisable = false;
										boolean textBoxDisable = false;
										String autoDisplayStyle= null;
										String manualDisplayStyle=null;
										if(isVirtuallyLocated)
										{
											dropDownDisable = true;
											textBoxDisable = true;
											manualDisplayStyle = "display:none";
										}
										else
										{
											dropDownDisable = true;
											manualDisplayStyle = "display:block";
										}
									%>

								<script language="JavaScript" type="text/javascript" src="jss/CustomListBox.js"></script>
								<td colspan="4" >
					<!-------Select Box Begins----->

								<table border="0" cellpadding="3" cellspacing="0" width="100%">
								<%
												NewSpecimenForm newSpecimenForm=(NewSpecimenForm)request.getAttribute("newSpecimenForm");
												String showContainer = (String) request.getAttribute("showContainer");


								%>

								<% if( operation.equals("add") || (showContainer!=null&&showContainer.equals("Pending")))
						{%>
								<tr >
							
								<td width="100%" >
			<div class="black_ar" id="manualDiv" style="display:block">
											<table cellpadding="0" cellspacing="0" border="0" >
						<tr>
							<td class="black_ar" size="48" align="left">
								
								<td width="50%" align="left" class="black_ar">
						<html:hidden property="selectedContainerName" styleId="selectedContainerName" />
						<div>
							<table border="0" width="29%" id="outerTable2" cellspacing="0" cellpadding="0">
								<tr class="black_ar">
									<td align="left" class="black_ar" width="88%" height="100%" >
										<div id="scDropDownIddiv" class="x-form-field-wrap" >
											<input id="storageContainerDropDown"
													onkeydown="keyNavigation(event,containerDropDownInfo,scGrid,scGridVisible);"
													onKeyUp="autoCompleteControl(event,containerDropDownInfo,scGrid);"
													onClick="noEventPropogation(event)"
													autocomplete="off"
													size="30"
													class="black_ar x-form-text x-form-field x-form-focus"/><img id="scDropDownId" style="top : 0px !important;" class="x-form-trigger x-form-arrow-trigger" 
												onclick="showHideStorageContainerGridOnSpecimenPage(event,'storageContainer','storageContainerDropDown',containerDropDownInfo);"
												src="images/uIEnhancementImages/s.gif"/>
										</div>
									</td>
								</tr>
								<tr>
									<td>
									<div id="storageContainer" class="black_ar" style="z-index: 100"
										onClick="noEventPropogation(event)">
									<div id="storageContainerGrid" class="black_ar" style="height: 40px;"
										onClick="noEventPropogation(event)"></div>
									<div id="storageContainerPagingArea" class="black_ar" onClick="noEventPropogation(event)"></div>
									<div id="storageContainerInfoArea" class="black_ar" onClick="noEventPropogation(event)"></div>
									</div>
									</td>
								</tr>
							</table>
					</td>
					</td>

							</td>
							<td>&nbsp;&nbsp;</td>
							<td class="groupelements"  width="8%" style="left-padding:165px;">
								<html:text styleClass="black_ar"  size="1" styleId="pos1" property="pos1"  disabled= "false" style="display:block"/>
							</td>
							<td class="groupelements" width="8%">
								<html:text styleClass="black_ar"  size="1" styleId="pos2" property="pos2" disabled= "false" style="display:block"/>
							</td>
							<td class="groupelements">
								<html:button styleClass="black_ar" property="containerMap" onclick="showPopUp()">
											<bean:message key="buttons.map"/>
								</html:button>
							</td>
						</tr>
					</table>
					</div>
		</td>
		</tr>
		<%}%>
									<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.ADD%>">
										<% if(showContainer!=null&&showContainer.equals("Pending"))
										{
										}
										else{%>
												<%
												//if((newSpecimenForm.getStContSelection()==1)&&(newSpecimenForm.getStorageContainer().equals("")||newSpecimenForm.getStorageContainer().equals("-1"))&&(newSpecimenForm.getCollectionStatus().equals("Collected") || newSpecimenForm.getCollectionStatus().equals("Complete")))
												if(
														(
															(
																newSpecimenForm.getSelectedContainerName()==null
																||
																"".equals(newSpecimenForm.getSelectedContainerName())
																||
																"Virtual".equals(newSpecimenForm.getSelectedContainerName().trim())
															)
															&&
															(
																"".equals(newSpecimenForm.getStorageContainer())
																||
																"-1".equals(newSpecimenForm.getStorageContainer())
															)
														)
													&&
														(
															"Collected".equals(newSpecimenForm.getCollectionStatus()) 
															|| 
															"Complete".equals(newSpecimenForm.getCollectionStatus())
														)
												   )
												{%>
												<tr>
														<td class="black_ar" colspan="2">
														<bean:message key="specimen.virtualLocation" />
													</td>
												</tr>
													<%
												}
													else{
												%>
												<tr>
														<td colspan="2" class="black_ar">
														<input type="text" size="30" maxlength="255"  class="black_ar"  value='<%=specimenPositionAsString.toString()%>' readonly style="border:0px" id="storageContainerPosition" /> <input type="button" class="blue_ar_b" value="Edit" onclick="loadDHTMLXWindowForTransferEvent()" />
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
							<%@ include file="CollAndRecEvents.jsp" %>
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