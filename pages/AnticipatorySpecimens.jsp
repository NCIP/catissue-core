<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/nlevelcombo.tld" prefix="ncombo"%>
<%@ taglib uri="/WEB-INF/specimenDetails.tld" prefix="md"%>
<%@ taglib uri="/WEB-INF/AutoCompleteTag.tld" prefix="autocomplete"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.bean.GenericSpecimen"%>
<%@ page import="edu.wustl.catissuecore.actionForm.ViewSpecimenSummaryForm"%>
<%@ page import="java.io.*"%>
<%@ page import="java.util.*"%>
<%@ page import="edu.wustl.common.util.global.ApplicationProperties"%>
<%@ page import="edu.wustl.catissuecore.util.global.Variables"%>
<%@ page import="edu.wustl.catissuecore.util.global.AppUtility"%>
<%@ include file="/pages/content/common/AutocompleterCommon.jsp"%>
<%@ page language="java" isELIgnored="false"%>
<% ViewSpecimenSummaryForm form = (ViewSpecimenSummaryForm)request.getAttribute("viewSpecimenSummaryForm");
List<GenericSpecimen> specimenList=form.getSpecimenList();
List<GenericSpecimen> aliquotList=form.getAliquotList();
List<GenericSpecimen> derivedList=form.getDerivedList();
String clinicalDataEntryURL = null;
if(Constants.TRUE.equals(request.getParameter("isClinicalDataEntry")))
{
	clinicalDataEntryURL = request.getParameter("clinicalDataEntryURL");
}
%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
<script type="text/javascript" src="jss/tag-popup.js"></script>
<link rel="STYLESHEET" type="text/css"
	href="dhtmlx_suite/dhtml_pop/css/dhtmlXTree.css">
<script src="dhtmlx_suite/dhtml_pop/js/dhtmlXCommon.js"></script>
<link rel="STYLESHEET" type="text/css"
	href="dhtmlx_suite/dhtml_pop/css/dhtmlXGrid.css" />
<link rel="STYLESHEET" type="text/css"
	href="dhtmlx_suite/dhtml_pop/css/dhtmlxgrid_dhx_skyblue.css" />
<script src="dhtmlx_suite/dhtml_pop/js/dhtmlx.js"></script>
<script src="dhtmlx_suite/dhtml_pop/js/dhtmlXTree.js"></script>
<script src="dhtmlx_suite/dhtml_pop/js/dhtmXTreeCommon.js"></script>
<script src="dhtmlx_suite/dhtml_pop/js/dhtmlXGridCell.js"></script>
<script src="dhtmlx_suite/dhtml_pop/js/dhtmlXTreeGrid.js"></script>


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
<script src="dhtmlx_suite/js/dhtmlxcommon.js"></script>
<script src="dhtmlx_suite/js/dhtmlxcombo.js"></script>
<script src="dhtmlx_suite/js/dhtmlxtree.js"></script>
<script src="dhtmlx_suite/ext/dhtmlxtree_li.js"></script>
<script src="jss/script.js" type="text/javascript"></script>
<script language="JavaScript" type="text/javascript"	src="jss/javaScript.js"></script>
<script language="JavaScript" type="text/javascript"	src="jss/caTissueSuite.js"></script>
<script src="jss/calendarComponent.js" language="JavaScript"	type="text/javascript"></script>
<script>var imgsrc="images/de/";</script>
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
<script src="dhtmlx_suite/js/dhtmlxcommon.js"></script>
<script src="dhtmlx_suite/js/dhtmlxcombo.js"></script>
<script src="dhtmlx_suite/js/dhtmlxtree.js"></script>
<script src="dhtmlx_suite/ext/dhtmlxtree_li.js"></script>
<script src="jss/script.js" type="text/javascript"></script>

<script src="jss/script.js"></script>
<script src="jss/calendarComponent.js"></script>
<SCRIPT>var imgsrc="images/";</SCRIPT>
<LINK href="css/calanderComponent.css" type=text/css rel=stylesheet>
<script language="JavaScript" type="text/javascript"	src="javascripts/de/prototype.js"></script>
<script language="JavaScript" type="text/javascript"	src="javascripts/de/scr.js"></script>
<script language="JavaScript" type="text/javascript"	src="javascripts/de/combobox.js"></script>
<script language="JavaScript" type="text/javascript"	src="jss/ext-base.js"></script>
<script language="JavaScript" type="text/javascript"	src="jss/ext-all.js"></script>
<script language="JavaScript" type="text/javascript"	src="javascripts/de/ajax.js"></script>
<script language="JavaScript" type="text/javascript"	src="/jss/multiselectUsingCombo.js"></script>
<LINK href="css/catissue_suite.css" type="text/css" rel="stylesheet">
<link rel="stylesheet" type="text/css" href="css/styleSheet.css" />
<link rel="stylesheet" type="text/css"	href="css/clinicalstudyext-all.css" />
<link rel="STYLESHEET" type="text/css"	href="dhtmlx_suite/css/dhtmlxgrid.css">
<link rel="STYLESHEET" type="text/css" href="css/dhtmlDropDown.css">
<link rel="STYLESHEET" type="text/css"	href="dhtmlx_suite/css/dhtmlxcombo.css">
<link rel="STYLESHEET" type="text/css"	href="dhtmlx_suite/ext/dhtmlxgrid_pgn_bricks.css">
<link rel="STYLESHEET" type="text/css"	href="dhtmlx_suite/skins/dhtmlxtoolbar_dhx_blue.css">
<script language="JavaScript" type="text/javascript" src="jss/dhtmlDropDown.js"></script>


<script language="JavaScript" type="text/javascript" src="jss/script.js"></script>
<link rel="stylesheet" type="text/css" href="css/styleSheet.css" />
<link href="css/catissue_suite.css" rel="stylesheet" type="text/css" />
<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
<script language="JavaScript" type="text/javascript" src="jss/antiSpecAjax.js"></script>
<script language="JavaScript" type="text/javascript" src="jss/GenericSpecimenDetailsTag.js"></script>
<script src="jss/ajax.js" type="text/javascript"></script>






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
<script src="dhtmlx_suite/js/dhtmlxcommon.js"></script>
<script src="dhtmlx_suite/js/dhtmlxcombo.js"></script>
<script src="dhtmlx_suite/js/dhtmlxtree.js"></script>
<script src="dhtmlx_suite/ext/dhtmlxtree_li.js"></script>

<logic:equal name="IsToShowButton" value="true">
<script type="text/javascript" src="dhtmlx_suite/js/dhtmlxgrid.js"></script>
</logic:equal>
<script type="text/javascript" src="dhtmlx_suite/js/dhtmlxgridcell.js"></script>
<script type="text/javascript" src="dhtmlx_suite/js/connector.js"></script>
<script type="text/javascript" src="dhtmlx_suite/ext/dhtmlxgrid_filter.js"></script>
<script type="text/javascript" src="dhtmlx_suite/ext/dhtmlxgrid_pgn.js"></script>
<script type="text/javascript" src="dhtmlx_suite/js/dhtmlxtoolbar.js"></script>
<script src="jss/script.js" type="text/javascript"></script>
<script language="JavaScript" type="text/javascript"	src="jss/javaScript.js"></script>
<script language="JavaScript" type="text/javascript"	src="jss/caTissueSuite.js"></script>
<script src="jss/calendarComponent.js" language="JavaScript"	type="text/javascript"></script>
<script>var imgsrc="images/de/";</script>
<script language="JavaScript" type="text/javascript"	src="javascripts/de/prototype.js"></script>
<script language="JavaScript" type="text/javascript"	src="javascripts/de/scr.js"></script>
<script language="JavaScript" type="text/javascript"	src="javascripts/de/combobox.js"></script>
<script language="JavaScript" type="text/javascript"	src="jss/ext-base.js"></script>
<script language="JavaScript" type="text/javascript"	src="jss/ext-all.js"></script>
<script language="JavaScript" type="text/javascript"	src="javascripts/de/ajax.js"></script>
<script language="JavaScript" type="text/javascript"	src="/jss/multiselectUsingCombo.js"></script>
<LINK href="css/catissue_suite.css" type="text/css" rel="stylesheet">
<link rel="stylesheet" type="text/css" href="css/styleSheet.css" />
<link rel="stylesheet" type="text/css"	href="css/clinicalstudyext-all.css" />
<link rel="STYLESHEET" type="text/css"	href="dhtmlx_suite/css/dhtmlxgrid.css">
<link rel="STYLESHEET" type="text/css" href="css/dhtmlDropDown.css">
<link rel="STYLESHEET" type="text/css"	href="dhtmlx_suite/css/dhtmlxcombo.css">
<link rel="STYLESHEET" type="text/css"	href="dhtmlx_suite/ext/dhtmlxgrid_pgn_bricks.css">
<link rel="STYLESHEET" type="text/css"	href="dhtmlx_suite/skins/dhtmlxtoolbar_dhx_blue.css">
<script language="JavaScript" type="text/javascript" src="jss/dhtmlDropDown.js"></script>




<script src="jss/script.js"></script>
<script src="jss/calendarComponent.js"></script>
<SCRIPT>var imgsrc="images/";</SCRIPT>
<LINK href="css/calanderComponent.css" type=text/css rel=stylesheet>






<script language="JavaScript">
var clinDataEntryURL = "<%=clinicalDataEntryURL%>";
if(clinDataEntryURL != null && clinDataEntryURL != "" && clinDataEntryURL != "null")
{
	var clinportalPath = clinDataEntryURL.split("?");
	var clinportalPath1 = clinportalPath[0];
	var clinportalPath2 = clinportalPath[1];

	var request = newXMLHTTPReq();
	request.onreadystatechange = getReadyStateHandler(request,openClinportalPage,true);
	//send data to ActionServlet
	//Open connection to servlet
	request.open("POST","AjaxAction.do?method=encryptData",true);
	request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
	var dataToSend = clinportalPath2;
	request.send(dataToSend);

	//logout();
	//window.top.location=clinDataEntryURL;
}

function logout()
{
	var request = newXMLHTTPReq();
	request.onreadystatechange = getReadyStateHandler(request,"",true);
	//send data to ActionServlet
	//Open connection to servlet
	request.open("POST","AjaxAction.do?method=logout",true);
	request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
	var dataToSend = "";
	request.send(dataToSend);
}

function openClinportalPage(dataString)
{
	var clinportalUrl=clinDataEntryURL;
    var clinportalUrlPath = clinportalUrl.split("?");
	var clinportalUrlPath1 = clinportalUrlPath[0];

	logout();
	window.top.location=clinportalUrlPath1 + "?method=login&path=" + dataString;
}
</script>

<script language="JavaScript" type="text/javascript">
 var isPrintChecked = false;
	//window.parent.frames['SpecimenEvents'].location="ShowCollectionProtocol.do?pageOf=specimenEventsPage&operation=ViewSummary";
	function ApplyToAll(object,type)
		{
			var cnt = getCountByType(type);
			var elemId = type+"[0]"+".storageContainerForSpecimen";
			var uId=type+"[0]"+".uniqueIdentifier";
			var uniqueId= document.getElementById(uId).value;
			var name="storageContainerDropDown_"+uniqueId;
			var val=document.getElementById(name);
			var valueToSet=val.value;
			for(i=1;i<cnt;i++)	// change values for all remaining
			{
				uId=type+"["+i+"]"+".uniqueIdentifier";
				uniqueId= document.getElementById(uId).value;
				name="storageContainerDropDown_"+uniqueId;
				document.getElementById(name).value=valueToSet;
							
				elemName = type+"["+i+"]"+".selectedContainerName";
				elemName = type+"["+i+"]"+".positionDimensionOne";
				getElement(elemName).value="";
				
				elemName = type+"["+i+"]"+".positionDimensionTwo";
				getElement(elemName).value="";
			}
		}

//Mandar : 15Dec08 ---
function getCountByType(type)
{
	var elements=document.getElementsByClassName(type);
	return elements.length;
}
function getElement(name)
{
	var fields = document.getElementsByName(name);
	if(fields.length > 0)
		return fields[0];
	else
		return "";
}

function initWindow()
{
//alert("initializing DHTMLX window");
    dhxWins = new dhtmlXWindows();
    dhxWins.enableAutoViewport(true);
    dhxWins.setImagePath("dhtmlx_suite/imgs/");
    dhxWins.setSkin("dhx_skyblue");
}

function loadDHTMLXWindow(storageContainerDropDown,positionDimensionOne,positionDimensionTwo)
{
	var w = 400;
    var h = 400;
    var x = (screen.width / 2) - (w / 2);
    var y = 0;
    dhxWins.createWindow("containerPositionPopUp", x, y, w, h);
	var storageContainer =document.getElementById(storageContainerDropDown).value;
    var url = "ShowStoragePositionGridView.do?pageOf=pageOfSpecimen&forwardTo=gridView&pos1="+positionDimensionOne+"&pos2="+positionDimensionTwo+"&containerName="+storageContainer;
    dhxWins.window("containerPositionPopUp").attachURL(url);                      //url : either an action class or you can specify jsp page path directly here
    dhxWins.window("containerPositionPopUp").button("park").hide();
    dhxWins.window("containerPositionPopUp").button("minmax1").hide();
    dhxWins.window("containerPositionPopUp").allowResize();
	dhxWins.window("containerPositionPopUp").setModal(true);
    dhxWins.window("containerPositionPopUp").setText("Container Positions");    //it's the title for the popup
}


		function saveCollectionProtocol()
		{
				var action ="SubmitSpecimenCollectionProtocol.do?action=collectionprotocol";
				document.forms[0].action = action;
				document.forms[0].submit();
		}

		function showMap(storageContainerDropDown,selectedContainerName,positionDimensionOne,positionDimensionTwo,containerId,specimenClassName,spType,cpId)
		{
			var storageContainer =document.getElementById(storageContainerDropDown).value;
			if(storageContainer!="")
			{
				loadDHTMLXWindow(storageContainerDropDown,positionDimensionOne,positionDimensionTwo);
			}
			else
			{
				var frameUrl="ShowFramedPage.do?pageOf=pageOfSpecimen&"+
					"selectedContainerName=" + storageContainerDropDown +
					"&pos1=" + positionDimensionOne +
					"&pos2=" + positionDimensionTwo +
					"&containerId=" +containerId +
					"&${requestScope.CAN_HOLD_SPECIMEN_CLASS}="+specimenClassName +
					"&${requestScope.CAN_HOLD_SPECIMEN_TYPE}="+spType +
					"&${requestScope.CAN_HOLD_COLLECTION_PROTOCOL}=" + cpId;
					frameUrl+="&storageContainerName="+storageContainer;
					openPopupWindow(frameUrl,'newSpecimenPage');
			}
			//var storageContainer = document.getElementById(selectedContainerName).value;
			

			
			//mapButtonClickedOnSpecimen(frameUrl,'newSpecimenPage');
		}
		//bug 11169 start
		function ChangeCheckBoxStatus(type,chkInstance)
		{
             onCheckBoxClick(type,chkInstance,'checkedSpecimen');
		}
		function ChangePrintCheckBoxStatus(type,chkInstance)
		{
             onCheckBoxClick(type,chkInstance,'printSpecimen');
		}
		function onCheckBoxClick(type,chkInstance,prefix)
		{
           var checkedSpecimen ='].'+prefix;
			var elementType = type +'[';
			var ctr=0;
			do
			{
				var elementName = elementType + ctr + checkedSpecimen;

				var chkCount= document.getElementsByName(elementName).length;
				if (chkCount >0)
				{
					var element = document.getElementsByName(elementName)[0];
					if (element.disabled == false)
					{
						element.checked = chkInstance.checked;
						//Mandar : for event propagation
						if(type == "specimen" && element.onclick)
						{
							if(document.all)
								element.fireEvent("onclick");
							else
								element.onclick();
						}
					}
					ctr++;
				}
			}while(chkCount>0);
		}

	function UpdateCheckBoxStatus()
	{
		var checkedSpecimen = '].checkedSpecimen';
		var aliquotType = "aliquot[";
		var ctr = 0;
		if (document.forms[0].chkAllAliquot != null) {
			document.forms[0].chkAllAliquot.disabled = true;
			do {
				var elementName = aliquotType + ctr + checkedSpecimen;
				var chkCount = document.getElementsByName(elementName).length;
				if (chkCount > 0) {
					var element = document.getElementsByName(elementName)[0];
					if (element.disabled == false) {
						document.forms[0].chkAllAliquot.disabled = false;
					}else {
						document.forms[0].chkAllAliquot.checked = true;
					}
					ctr++;
				}
			} while (chkCount > 0);
		}


	if (document.forms[0].chkAllDerived != null)
	{
			ctr = 0;
			var derivedType = "derived[";
			document.forms[0].chkAllDerived.disabled = true;
			do {
				var elementName = derivedType + ctr + checkedSpecimen;
				var chkCount = document.getElementsByName(elementName).length;
				if (chkCount > 0) {
					var element = document.getElementsByName(elementName)[0];
					if (element.disabled == false) {
						document.forms[0].chkAllDerived.disabled = false;
					}else {
						document.forms[0].chkAllDerived.checked = true;
					}
					ctr++;
				}
			} while (chkCount > 0);
		}
	}
	function checkPrintStatusOfAllSpecimens() {
		checkPrintStatus('specimen');
		if (!isPrintChecked) {
			checkPrintStatus('aliquot');
		}
		if (!isPrintChecked) {
			checkPrintStatus('derived');
		}

	}
	function checkPrintStatus(type) {
		var checkedSpecimen = '].printSpecimen';
		var elementType = type + '[';
		var ctr = 0;
		do {
			var elementName = elementType + ctr + checkedSpecimen;
			var chkCount = document.getElementsByName(elementName).length;
			if (chkCount > 0) {
				var element = document.getElementsByName(elementName)[0];
				if (element.checked) {
					isPrintChecked = true;
					break;
				}
				ctr++;
			}
		} while (chkCount > 0 && !isPrintChecked);
	}
	//bug 11169 end
function pageSubmit() {
		checkPrintStatusOfAllSpecimens();
		<%
		   String isSCGSubmit = null;
		   if( request.getAttribute( Constants.IS_SCG_SUBMIT )!=null)
           {
	         isSCGSubmit = request.getAttribute( Constants.IS_SCG_SUBMIT ).toString();
	       }
		%>
		var url;
		<%if(isSCGSubmit!=null)
		{%>
		  url = 'GenericSpecimenSummary.do?save=SCGSpecimens&pageOf=specimenSummaryPage&isSCGSubmit=<%=isSCGSubmit%>';
		  <%}
		  else
		  {%>
		  url = 'GenericSpecimenSummary.do?save=SCGSpecimens&pageOf=specimenSummaryPage';
		 <% }
		 %>
		//bug 12656
		<% if(request.getAttribute(Constants.PAGE_OF) != null && request.getAttribute(Constants.PAGE_OF).equals(Constants.PAGE_OF_MULTIPLE_SPECIMEN_WITHOUT_MENU))
		{
		  if(isSCGSubmit!=null)
		  {%>
              url = 'GenericSpecimenSummary.do?save=SCGSpecimens&pageOf=pageOfMultipleSpWithoutMenu&isSCGSubmit=<%=request.getAttribute( Constants.IS_SCG_SUBMIT )%>';
		<%}
		  else
		  {%>
		  url = 'GenericSpecimenSummary.do?save=SCGSpecimens&pageOf=pageOfMultipleSpWithoutMenu';
		 <% }
		 }%>


<%	if(request.getAttribute(Constants.PAGE_OF) != null && request.getAttribute(Constants.PAGE_OF).equals(Constants.CP_CHILD_SUBMIT)) {

			 if(isSCGSubmit!=null)
			  {%>
  url = 'GenericSpecimenSummaryForSpecimen.do?save=SCGSpecimens&pageOf=specimenSummaryPage&isSCGSubmit=<%=request.getAttribute( Constants.IS_SCG_SUBMIT )%>';
<%}
			  else
			  {%>
	url = 'GenericSpecimenSummaryForSpecimen.do?save=SCGSpecimens&pageOf=specimenSummaryPage';
<% }
			 }%>
	//var printFlag = document.getElementById("printCheckbox");
		if (isPrintChecked) {
			//changes added to work in Mozilla
			document.getElementsByName("printCheckbox")[0].value = true;
			document.forms[0].action = url + '&printflag=1';
			document.forms[0].submit();
		} else {
			document.getElementsByName("printCheckbox")[0].value = false;
			document.forms[0].action = url + '&printflag=0';
			document.forms[0].submit();
		}

	}

	function onAddToCart() {
		document.forms[0].forwardTo.value = "addMltipleSpecimenToCart";
		pageSubmit();

	}
	function onParentRadioBtnClick() {
		var url = 'GenericSpecimenSummary.do?isSubmitRequest=false&IsToShowButton=true&pageOf=specimenSummaryPage';
<%	if(request.getAttribute(Constants.PAGE_OF) != null && request.getAttribute(Constants.PAGE_OF).equals(Constants.CP_CHILD_SUBMIT)) {%>
			 url = 	'GenericSpecimenSummaryForSpecimen.do?pageOf=<%=Constants.CP_CHILD_SUBMIT%>&isSubmitRequest=false';
			<%}%>
			document.forms[0].action =url;
			document.forms[0].submit();
		}

//Mandar : 6Aug08 ----- ajax call for storage location
var sid="";
function scForSpecimen(element,spid)
{
	sid=spid;
//	alert(element.value + " : "+element.name);
	var name= element.name;
	var prefix = name.substring(0,name.indexOf('.'));
	var cpidName = prefix+".collectionProtocolId";
	//var cpid = document.getElementsByName(cpidName)[0].value;
	var cpid = document.getElementById(cpidName).value;
	var className = prefix+".className";
//	var cName = document.getElementsByName(className)[0].value;
	var cName = document.getElementById(className).value;
	var spType = prefix+".type";
	var type = document.getElementById(spType).value;

	//alert(prefix+" : " + cpid + " : " + cName);
	if(element.value == "Auto")
	{
		var responseHandlerFn = setSCLocation;
		var bool = true;
		var reqType = "GET";
		var url = "GenericSpecimenSummaryForSpecimen.do?sid="+sid+"&cpid="+cpid+"&cName="+cName+"&type="+type;
		ajaxCall(reqType, url, bool, responseHandlerFn);
	}
	else
	{
		setContainerType(element.value,sid);
	}
}

function setContainerType(containerType, sid)
{
		if(containerType == "Manual")
		{
			updateSCFields(sid, false);
		}
		else
		{
			updateSCFields(sid, true);
		}
}
function updateSCFields(sid, isDisabled)
{
	var scName = "selectedContainerName_"+sid;
	var scPos1 = "positionDimensionOne_"+sid;
	var scPos2 = "positionDimensionTwo_"+sid;
	var scCntr = "containerId_"+sid;
	var t1 = document.getElementById(scName);
	var t2 = document.getElementById(scPos1);
	var t3 = document.getElementById(scPos2);
	var t4 = document.getElementById(scCntr);
	t1.value="";
	t2.value="";
	t3.value="";
	t4.value="";
	t1.disabled = isDisabled;
	t2.disabled = isDisabled;
	t3.disabled = isDisabled;
	t4.disabled = isDisabled;
}



function setSCLocation()
{
	if ((xmlHttpReq.readyState == 4) && (xmlHttpReq.status == 200))
	{
		var msg = xmlHttpReq.responseText;
	//	alert(msg);
		if(msg.indexOf('#') != -1)
		{
			updateSCDetails(msg);
		}
		else
		{
			alert("Container info not available right now. Try again after some time.");
		}
	}
/*	else
	{
		alert("Could not fetch location right now");
	}
*/
}

function updateSCDetails(msg)
{
	var scName = "selectedContainerName_"+sid;
	var scPos1 = "positionDimensionOne_"+sid;
	var scPos2 = "positionDimensionTwo_"+sid;
	var scontid = "containerId_"+sid;
	var t1 = document.getElementById(scName);
	var t2 = document.getElementById(scPos1);
	var t3 = document.getElementById(scPos2);
	var t4 = document.getElementById(scontid);

	//alert(msg);
	var data = msg.split("#");
	t1.value=data[0];
	t2.value=data[2];
	t3.value=data[3];
	t4.value=data[1];
	t1.disabled = false;
	t2.disabled = false;
	t3.disabled = false;

}

<%

String collectionProtocolId ="";
if (request.getAttribute(Constants.COLLECTION_PROTOCOL_ID)==null)
	collectionProtocolId="";
else
 collectionProtocolId =(String) request.getAttribute(Constants.COLLECTION_PROTOCOL_ID);

	 %>
//declaring DHTMLX Drop Down controls required variables
<%if(specimenList!=null)
	{
		for(int i=0;i<specimenList.size();i++)
		{
			GenericSpecimen gs1=specimenList.get(i);
			String lineage=AppUtility.getLineageSubString(gs1);
			Long idToAppend=gs1.getId();
			String stringToAppend=null;
			if(Long.valueOf(gs1.getId())==-1)
			{
				idToAppend=Long.valueOf(i+1);
				stringToAppend=idToAppend.toString();
			}
			else
			{
				stringToAppend="S_"+gs1.getId();
			}
			
	%>
var containerDropDownInfo_<%=stringToAppend%>, scGrid_<%=stringToAppend%>;
var scGridVisible_<%=stringToAppend%> = false;
var gridDivObject_<%=stringToAppend%> ;
<%}}%>
<%if(aliquotList!=null)
	{
		//Iterator<GenericSpecimen> iter=aliquotList.Iterator();
		for(int i=0;i<aliquotList.size();i++)
		{
			GenericSpecimen gs1=aliquotList.get(i);
			String lineage=AppUtility.getLineageSubString(gs1);
			Long idToAppend=gs1.getId();
			String stringToAppend=null;
			if(Long.valueOf(gs1.getId())==-1)
			{
				idToAppend=Long.valueOf(i+1);
				stringToAppend=idToAppend.toString();
			}
			else
			{
				stringToAppend=lineage+"_"+gs1.getId();
			}
	%>
var containerDropDownInfo_<%=stringToAppend%>, scGrid_<%=stringToAppend%>;
var scGridVisible_<%=stringToAppend%> = false;
var gridDivObject_<%=stringToAppend%> ;
<%}}%>
<%if(derivedList!=null)
	{
		//Iterator<GenericSpecimen> iter=derivedList.Iterator();
		for(int i=0;i<derivedList.size();i++)
		{
			GenericSpecimen gs1=derivedList.get(i);
			String lineage=AppUtility.getLineageSubString(gs1);
			Long idToAppend=gs1.getId();
			String stringToAppend=null;
			if(Long.valueOf(gs1.getId())==-1)
			{
				idToAppend=Long.valueOf(i+1);
				stringToAppend=idToAppend.toString();
			}
			else
			{
				stringToAppend=lineage+"_"+gs1.getId();
			}
	%>
var containerDropDownInfo_<%=stringToAppend%>, scGrid_<%=stringToAppend%>;
var scGridVisible_<%=stringToAppend%> = false;
var gridDivObject_<%=stringToAppend%> ;
<%}}%>

function showHideStorageContainerGrid(e,gridDivId, dropDownId,scGridVisible,containerDropDownInfo,scGrid)
{		
		setValue(e,containerDropDownInfo['gridDiv'], containerDropDownInfo['dropDownId']);
		if(containerDropDownInfo['visibilityStatusVariable'])
		{
			hideGrid(containerDropDownInfo['gridDiv']);
			containerDropDownInfo['visibilityStatusVariable'] = false;
		}
		else 
		 {	
			showGrid(containerDropDownInfo['gridDiv'],containerDropDownInfo['dropDownId']);
			containerDropDownInfo['visibilityStatusVariable'] = true;
			scGrid.load(containerDropDownInfo['actionToDo'],"");
		 }
}

function doOnLoad()
{
	<%if(specimenList!=null)
	{
		for(int i=0;i<specimenList.size();i++)
		{
			GenericSpecimen gs=specimenList.get(i);
			String lineage=AppUtility.getLineageSubString(gs);
			Long idToAppend=gs.getId();
			String stringToAppend=null;
			if(-1==Long.valueOf(gs.getId()))
			{
				idToAppend=Long.valueOf(gs.getUniqueIdentifier());
				stringToAppend=idToAppend.toString();
			}
			else
			{
				stringToAppend=lineage+"_"+gs.getId();
			}
			if(gs.getCollectionStatus()== null || "null".equals(gs.getCollectionStatus()) || "Pending".equals(gs.getCollectionStatus()))
			{
	%>
		var className<%=stringToAppend%>=document.getElementById("className_<%=stringToAppend%>").value;
		var sptype<%=stringToAppend%>=document.getElementById("type_<%=stringToAppend%>").value;
		var collectionProtocolId="<%=collectionProtocolId%>";
		var containerName=document.getElementById("storageContainerDropDown_<%=stringToAppend%>").value;
		var url="CatissueCommonAjaxAction.do?type=getStorageContainerList&<%=Constants.CAN_HOLD_SPECIMEN_CLASS%>="
		+className<%=stringToAppend%>+"&specimenType="+sptype<%=stringToAppend%>+ "&<%=Constants.CAN_HOLD_COLLECTION_PROTOCOL%>=" + collectionProtocolId;

		//Drop Down components information
		containerDropDownInfo_<%=stringToAppend%> = {gridObj:"storageContainerGrid_<%=stringToAppend%>", gridDiv:"storageContainer_<%=stringToAppend%>", dropDownId:"storageContainerDropDown_<%=stringToAppend%>", pagingArea:"storageContainerPagingArea_<%=stringToAppend%>", infoArea:"storageContainerInfoArea_<%=stringToAppend%>", onOptionSelect:
		function (id,ind)
		{
			document.getElementsByName('selectedContainerName_<%=stringToAppend%>')[0].value = containerName;
			document.getElementById(containerDropDownInfo_<%=stringToAppend%>['dropDownId']).value = scGrid_<%=stringToAppend%>.cellById(id,ind).getValue();
			hideGrid(containerDropDownInfo_<%=stringToAppend%>['gridDiv']);
			scGridVisible_<%=stringToAppend%> = false;
			document.getElementById("positionDimensionOne_<%=stringToAppend%>").value="";
			document.getElementById("positionDimensionTwo_<%=stringToAppend%>").value="";
		}
		, actionToDo:url, callBackAction:
		function(){
					var containerName= document.getElementsByName('selectedContainerName_<%=stringToAppend%>')[0].value;
					if(containerName != "" && containerName != 0 && containerName != null)
					{
						document.getElementsByName('selectedContainerName_<%=stringToAppend%>')[0].value = containerName;
						document.getElementById(containerDropDownInfo_<%=stringToAppend%>['dropDownId']).value = scGrid_<%=stringToAppend%>.cellById(containerName,0).getValue();
						hideGrid(containerDropDownInfo_<%=stringToAppend%>['gridDiv']);
						scGridVisible_<%=stringToAppend%> = false;
					}
				}
			, visibilityStatusVariable:scGridVisible_<%=stringToAppend%>, propertyId:'selectedContainerName_<%=stringToAppend%>'};
		// initialising grid
		scGrid_<%=stringToAppend%> = initDropDownGrid(containerDropDownInfo_<%=stringToAppend%>); 
	<%}}}%>
	
	
	//aliquot list
	<%if(aliquotList!=null)
	{
		//Iterator<GenericSpecimen> iter=aliquotList.Iterator();
		for(int i=0;i<aliquotList.size();i++)
		{
			GenericSpecimen gs=aliquotList.get(i);
			String lineage=AppUtility.getLineageSubString(gs);
			Long idToAppend=gs.getId();
			String stringToAppend=null;
			if(-1==Long.valueOf(gs.getId()))
			{
				idToAppend=Long.valueOf(gs.getUniqueIdentifier());
				stringToAppend=idToAppend.toString();
			}
			else
			{
				stringToAppend=lineage+"_"+gs.getId();
			}
			if(gs.getCollectionStatus()==null || "null".equals(gs.getCollectionStatus()) ||"Pending".equals(gs.getCollectionStatus()))
			{
	%>
		var className<%=stringToAppend%>=document.getElementById("className_<%=stringToAppend%>").value;
		var sptype<%=stringToAppend%>=document.getElementById("type_<%=stringToAppend%>").value;
		var collectionProtocolId="<%=collectionProtocolId%>";
		var containerName=document.getElementById("storageContainerDropDown_<%=stringToAppend%>").value;
		var url="CatissueCommonAjaxAction.do?type=getStorageContainerList&<%=Constants.CAN_HOLD_SPECIMEN_CLASS%>="
		+className<%=stringToAppend%>+"&specimenType="+sptype<%=stringToAppend%>+ "&<%=Constants.CAN_HOLD_COLLECTION_PROTOCOL%>=" + collectionProtocolId;

		//Drop Down components information
		containerDropDownInfo_<%=stringToAppend%> = {gridObj:"storageContainerGrid_<%=stringToAppend%>", gridDiv:"storageContainer_<%=stringToAppend%>", dropDownId:"storageContainerDropDown_<%=stringToAppend%>", pagingArea:"storageContainerPagingArea_<%=stringToAppend%>", infoArea:"storageContainerInfoArea_<%=stringToAppend%>", onOptionSelect:
		function (id,ind)
		{
			document.getElementsByName('selectedContainerName_<%=stringToAppend%>')[0].value = containerName;
			document.getElementById(containerDropDownInfo_<%=stringToAppend%>['dropDownId']).value = scGrid_<%=stringToAppend%>.cellById(id,ind).getValue();
			hideGrid(containerDropDownInfo_<%=stringToAppend%>['gridDiv']);
			scGridVisible_<%=stringToAppend%> = false;
			document.getElementById("positionDimensionOne_<%=stringToAppend%>").value="";
			document.getElementById("positionDimensionTwo_<%=stringToAppend%>").value="";
		}
		, actionToDo:url, callBackAction:
		function(){
					var containerName= document.getElementsByName('selectedContainerName_<%=stringToAppend%>')[0].value;
					if(containerName != "" && containerName != 0 && containerName != null)
					{
						document.getElementsByName('selectedContainerName_<%=stringToAppend%>')[0].value = containerName;
						document.getElementById(containerDropDownInfo_<%=stringToAppend%>['dropDownId']).value = scGrid_<%=stringToAppend%>.cellById(containerName,0).getValue();
						hideGrid(containerDropDownInfo_<%=stringToAppend%>['gridDiv']);
						scGridVisible_<%=stringToAppend%> = false;
					}
				}
			, visibilityStatusVariable:scGridVisible_<%=stringToAppend%>, propertyId:'selectedContainerName_<%=stringToAppend%>'};
		// initialising grid
		scGrid_<%=stringToAppend%> = initDropDownGrid(containerDropDownInfo_<%=stringToAppend%>); 
	<%}}}%>
	
	<%if(derivedList!=null)
	{
		//Iterator<GenericSpecimen> iter=derivedList.Iterator();
		for(int i=0;i<derivedList.size();i++)
		{
			GenericSpecimen gs=derivedList.get(i);
			String lineage=AppUtility.getLineageSubString(gs);
			Long idToAppend=gs.getId();
			String stringToAppend=null;
			if(-1==Long.valueOf(gs.getId()))
			{
				idToAppend=Long.valueOf(gs.getUniqueIdentifier());
				stringToAppend=idToAppend.toString();
			}
			else
			{
				stringToAppend=lineage+"_"+gs.getId();
			}
			if(gs.getCollectionStatus()==null || "null".equals(gs.getCollectionStatus()) ||"Pending".equals(gs.getCollectionStatus()))
			{
	%>
		var className<%=stringToAppend%>=document.getElementById("className_<%=stringToAppend%>").value;
		var sptype<%=stringToAppend%>=document.getElementById("type_<%=stringToAppend%>").value;
		var collectionProtocolId="<%=collectionProtocolId%>";
		var containerName=document.getElementById("storageContainerDropDown_<%=stringToAppend%>").value;
		var url="CatissueCommonAjaxAction.do?type=getStorageContainerList&<%=Constants.CAN_HOLD_SPECIMEN_CLASS%>="
		+className<%=stringToAppend%>+"&specimenType="+sptype<%=stringToAppend%>+ "&<%=Constants.CAN_HOLD_COLLECTION_PROTOCOL%>=" + collectionProtocolId;

		//Drop Down components information
		containerDropDownInfo_<%=stringToAppend%> = {gridObj:"storageContainerGrid_<%=stringToAppend%>", gridDiv:"storageContainer_<%=stringToAppend%>", dropDownId:"storageContainerDropDown_<%=stringToAppend%>", pagingArea:"storageContainerPagingArea_<%=stringToAppend%>", infoArea:"storageContainerInfoArea_<%=stringToAppend%>", onOptionSelect:
		function (id,ind)
		{
			document.getElementsByName('selectedContainerName_<%=stringToAppend%>')[0].value = containerName;
			document.getElementById(containerDropDownInfo_<%=stringToAppend%>['dropDownId']).value = scGrid_<%=stringToAppend%>.cellById(id,ind).getValue();
			hideGrid(containerDropDownInfo_<%=stringToAppend%>['gridDiv']);
			scGridVisible_<%=stringToAppend%> = false;
			document.getElementById("positionDimensionOne_<%=stringToAppend%>").value="";
			document.getElementById("positionDimensionTwo_<%=stringToAppend%>").value="";
		}
		, actionToDo:url, callBackAction:
		function(){
					var containerName= document.getElementsByName('selectedContainerName_<%=stringToAppend%>')[0].value;
					if(containerName != "" && containerName != 0 && containerName != null)
					{
						document.getElementsByName('selectedContainerName_<%=stringToAppend%>')[0].value = containerName;
						document.getElementById(containerDropDownInfo_<%=stringToAppend%>['dropDownId']).value = scGrid_<%=stringToAppend%>.cellById(containerName,0).getValue();
						hideGrid(containerDropDownInfo_<%=stringToAppend%>['gridDiv']);
						scGridVisible_<%=stringToAppend%> = false;
					}
				}
			, visibilityStatusVariable:scGridVisible_<%=stringToAppend%>, propertyId:'selectedContainerName_<%=stringToAppend%>'};
		// initialising grid
		scGrid_<%=stringToAppend%> = initDropDownGrid(containerDropDownInfo_<%=stringToAppend%>); 
	<%}}}%>
}

function setContainerValues()
{
<%if(specimenList!=null)
	{
		for(int i=0;i<specimenList.size();i++)
		{
			GenericSpecimen gs2=specimenList.get(i);
			String lineage=AppUtility.getLineageSubString(gs2);
			Long idToAppend=gs2.getId();
			String stringToAppend=null;
			if(-1==Long.valueOf(gs2.getId()))
			{
				idToAppend=Long.valueOf(gs2.getUniqueIdentifier());
				stringToAppend=idToAppend.toString();
			}
			else
			{
				stringToAppend=lineage+"_"+gs2.getId();
			}
			if(!"Collected".equals(gs2.getCollectionStatus())) 
			{
				if("Virtual".equals(gs2.getStorageContainerForSpecimen()))
				{
					gs2.setSelectedContainerName("Virtual");
					gs2.setPositionDimensionOne("");
					gs2.setPositionDimensionTwo("");
				}
				else if("Manual".equals(gs2.getStorageContainerForSpecimen()))
				{
					gs2.setSelectedContainerName("");
					gs2.setPositionDimensionOne("");
					gs2.setPositionDimensionTwo("");
				}
			%>
				document.getElementById(containerDropDownInfo_<%=stringToAppend%>['dropDownId']).value='<%=gs2.getSelectedContainerName()%>';
				document.getElementById("positionDimensionOne_<%=stringToAppend%>").value='<%=gs2.getPositionDimensionOne()%>';
				document.getElementById("positionDimensionTwo_<%=stringToAppend%>").value='<%=gs2.getPositionDimensionTwo()%>';
<%}}}%>	

<%if(aliquotList!=null)
	{
		for(int i=0;i<aliquotList.size();i++)
		{
			GenericSpecimen gs2=aliquotList.get(i);
			String lineage=AppUtility.getLineageSubString(gs2);
			Long idToAppend=gs2.getId();
			String stringToAppend=null;
			if(-1==Long.valueOf(gs2.getId()))
			{
				idToAppend=Long.valueOf(gs2.getUniqueIdentifier());
				stringToAppend=idToAppend.toString();
			}
			else
			{
				stringToAppend=lineage+"_"+gs2.getId();
			}
			if(!"Collected".equals(gs2.getCollectionStatus())) 
			{
				if("Virtual".equals(gs2.getStorageContainerForSpecimen()))
				{
					gs2.setSelectedContainerName("Virtual");
					gs2.setPositionDimensionOne("");
					gs2.setPositionDimensionTwo("");
				}
				else if("Manual".equals(gs2.getStorageContainerForSpecimen()))
				{
					gs2.setSelectedContainerName("");
					gs2.setPositionDimensionOne("");
					gs2.setPositionDimensionTwo("");
				}
				%>
				document.getElementById(containerDropDownInfo_<%=stringToAppend%>['dropDownId']).value='<%=gs2.getSelectedContainerName()%>';
				document.getElementById("positionDimensionOne_<%=stringToAppend%>").value='<%=gs2.getPositionDimensionOne()%>';
				document.getElementById("positionDimensionTwo_<%=stringToAppend%>").value='<%=gs2.getPositionDimensionTwo()%>';
<%}}}%>	

<%if(derivedList!=null)
	{
		for(int i=0;i<derivedList.size();i++)
		{
			GenericSpecimen gs2=derivedList.get(i);
			String lineage=AppUtility.getLineageSubString(gs2);
			Long idToAppend=gs2.getId();
			String stringToAppend=null;
			if(-1==Long.valueOf(gs2.getId()))
			{
				idToAppend=Long.valueOf(gs2.getUniqueIdentifier());
				stringToAppend=idToAppend.toString();
			}
			else
			{
				stringToAppend=lineage+"_"+gs2.getId();
			}
			if(!"Collected".equals(gs2.getCollectionStatus())) 
			{
				if("Virtual".equals(gs2.getStorageContainerForSpecimen()))
				{
					gs2.setSelectedContainerName("Virtual");
					gs2.setPositionDimensionOne("");
					gs2.setPositionDimensionTwo("");
				}
				else if("Manual".equals(gs2.getStorageContainerForSpecimen()))
				{
					gs2.setSelectedContainerName("");
					gs2.setPositionDimensionOne("");
					gs2.setPositionDimensionTwo("");
				}
				%>
				document.getElementById(containerDropDownInfo_<%=stringToAppend%>['dropDownId']).value='<%=gs2.getSelectedContainerName()%>';
				document.getElementById("positionDimensionOne_<%=stringToAppend%>").value='<%=gs2.getPositionDimensionOne()%>';
				document.getElementById("positionDimensionTwo_<%=stringToAppend%>").value='<%=gs2.getPositionDimensionTwo()%>';
<%}}}%>	
}

	


</script>
</head>
<body onload="doOnLoad();initWindow();UpdateCheckBoxStatus();setContainerValues()">
<script type="text/javascript" src="jss/wz_tooltip.js"></script>
<script language="javascript" type="text/javascript">
	${requestScope.refreshTree}
</script>
<logic:equal name="viewSpecimenSummaryForm" property="requestType"
	value="Multiple Specimen">
	<script language="javascript" type="text/javascript">
		${requestScope.refreshTree}
	</script>
</logic:equal>
<%
String lbl = "Apply first to all";
%>
<%@ include file="/pages/content/common/ActionErrors.jsp"%>
<logic:notEmpty name="messageKey">
	<html:messages id="messageKey" message="true" header="messages.header"
		footer="messages.footer">
			${requestScope.messageKey}
		</html:messages>
</logic:notEmpty>

<html:form action="${requestScope.formAction}">
	<!-- Mandar : New Table design starts -->
	<TABLE width="100%" cellspacing="0" cellpadding="0">
		<TR>
			<TD align="left" class="tr_anti_hdrbg_blue" width="100%" colspan=3>
			<TABLE width="100%" border="0">
				<TR>
					<TD align="left" rowspan="2" valign="middle"
						class="tr_anti_hdrbg_blue" width="${requestScope.sfCol}%"><span
						class="blue_ar_b"> <bean:write
						name="viewSpecimenSummaryForm" property="title" /> </span></TD>
					<TD class="tr_anti_hdrbg_blue" width=33% rowspan="2"
						valign="middle" align="left"><A class="black_ar"
						name="parent" HREF="#parent" onClick="ApplyToAll(this,'specimen')"
						onmouseover="Tip(' Apply first location to all')"><bean:message
						key="aliquots.applyFirstToAll" /></A></TD>
					<td nowrap class="tr_anti_hdrbg_blue" scope="col" width="2%"
						valign="middle" align="left" onmouseover="Tip('Collect All')">
					<logic:equal name="viewSpecimenSummaryForm"
						property="showCheckBoxes" value="true">
						<span class="blue_ar_b"> <bean:message
							key="anticipatorySpecimen.Collected" /> </span>
					</logic:equal></td>
					<td nowrap class="tr_anti_hdrbg_blue" scope="col" width="2%"
						valign="middle" align="left" onmouseover="Tip('Print All')">
					<logic:equal name="viewSpecimenSummaryForm"
						property="showCheckBoxes" value="true">
						<span class="blue_ar_b"> <bean:message
							key="specimen.printLabel" /> </span>
					</logic:equal></td>
				</TR>
				<TR>
					<td nowrap class="tr_anti_hdrbg_blue_small" scope="col" width="2%"
						valign="middle" align="center"><logic:equal
						name="viewSpecimenSummaryForm" property="showCheckBoxes"
						value="true">
						<input type="checkbox" name="chkAllSpecimen"
							onclick="ChangeCheckBoxStatus('specimen',this)" />
					</logic:equal></td>
					<td nowrap class="tr_anti_hdrbg_blue_small" scope="col" width="2%"
						valign="middle" align="center"><logic:equal
						name="viewSpecimenSummaryForm" property="showCheckBoxes"
						value="true">
						<input type="checkbox" name="printAll"
							onclick="ChangePrintCheckBoxStatus('specimen',this)" />
					</logic:equal></td>
				</TR>
			</TABLE>
			</TD>
		</TR>

		<logic:empty name="viewSpecimenSummaryForm" property="specimenList">
			<tr>
				<td class="dataTableWhiteCenterHeader" colspan="3">No specimens
				to display for current action!!</td>
			</tr>
		</logic:empty>
		<TR>
			<TD colspan="3">
			<table border=0 width="100%">
				<md:specDetFormat4 columnHeaderListName="columnHeaderList"
					formName="viewSpecimenSummaryForm" dataListName="specimenList"
					dataListType="Parent" columnListName="columnListName"
					isReadOnly="false" displayColumnListName="dispColumnsList" />
			</table>
			<logic:notEmpty name="viewSpecimenSummaryForm" property="eventId">
				<html:hidden property="eventId" />
				<html:hidden property="lastSelectedSpecimenId" />
				<html:hidden property="selectedSpecimenId" />
			</logic:notEmpty> <html:hidden property="userAction" /> <html:hidden
				property="requestType" /></TD>
		</TR>
		<logic:empty name="viewSpecimenSummaryForm" property="aliquotList">
			<logic:empty name="viewSpecimenSummaryForm" property="derivedList">
				<TR>
					<TD colspan="3">
					<table>
						<tr>
							<td class="dataTablePrimaryLabel" colspan="6" height="20">
							Child specimens not defined.</td>
						</tr>
						<tr>
							<td><br>
							</td>
						</tr>
					</table>
					</TD>
				</TR>
			</logic:empty>
		</logic:empty>

		<logic:notEmpty name="viewSpecimenSummaryForm" property="derivedList">
			<TR>
				<TD align="left" valign="middle" class="tr_anti_hdrbg_blue"
					width="100%" colspan=3>
				<TABLE width="100%" border="0">
					<TR>
						<TD rowspan="2" align="left" class="tr_anti_hdrbg_blue"
							width="${requestScope.fCol}%"><span class="blue_ar_b">
						<bean:message key="anticipatorySpecimen.DerivativeDetails" /> </span></TD>
						<TD class="tr_anti_hdrbg_blue" width=40% rowspan="2"
							valign="middle" align="left"><A class="black_ar"
							name="derived" HREF="#derived"
							onClick="ApplyToAll(this,'derived')"
							onmouseover="Tip(' Apply first location to all')"><bean:message
							key="aliquots.applyFirstToAll" /></A></TD>
						<td nowrap class="tr_anti_hdrbg_blue" scope="col" width="2%"
							align="left" valign="middle" onmouseover="Tip('Create All')">
						<logic:equal name="viewSpecimenSummaryForm"
							property="showCheckBoxes" value="true">
							<span class="blue_ar_b"> <bean:message
								key="anticipatorySpecimen.Collected" /> </span>
						</logic:equal></td>
						<td nowrap class="tr_anti_hdrbg_blue" scope="col" width="2%"
							align="left" valign="middle" onmouseover="Tip('Print All')">
						<logic:equal name="viewSpecimenSummaryForm"
							property="showCheckBoxes" value="true">
							<span class="blue_ar_b"> <bean:message
								key="specimen.printLabel" /> </span>
						</logic:equal></td>
					</TR>
					<TR>
						<td nowrap class="tr_anti_hdrbg_blue_small" scope="col" width="3%"
							align="center" valign="middle"><logic:equal
							name="viewSpecimenSummaryForm" property="showCheckBoxes"
							value="true">
							<input type="checkbox" name="chkAllDerived"
								onclick="ChangeCheckBoxStatus('derived',this)" />
						</logic:equal></td>
						<td nowrap class="tr_anti_hdrbg_blue_small" scope="col" width="2%"
							align="center" valign="middle"><logic:equal
							name="viewSpecimenSummaryForm" property="showCheckBoxes"
							value="true">
							<input type="checkbox" name="printAllDerived"
								onclick="ChangePrintCheckBoxStatus('derived',this)" />
						</logic:equal></td>
					</TR>
				</TABLE>
				</TD>
			</TR>
			<TR>
				<TD colspan="3">
				<table border=0 width="100%">
					<md:specDetFormat4 columnHeaderListName="subSpecimenColHeaderList"
						formName="viewSpecimenSummaryForm" dataListName="derivedList"
						dataListType="Derived" columnListName="columnListName"
						isReadOnly="false" displayColumnListName="subSpecdispColumnsList" />
				</table>
				</TD>
			</TR>
		</logic:notEmpty>

		 <logic:notEmpty name="viewSpecimenSummaryForm" property="aliquotList">
         <TR>
		 <TD align="left" valign="middle" class="tr_anti_hdrbg_blue" width="100%" colspan=3>
		 <TABLE width="100%" border="0">
		  <TR>
		  <TD colspan="1" rowspan="2" align="left" class="tr_anti_hdrbg_blue" width="${requestScope.fCol}%">
		     <span class="blue_ar_b">
			   <bean:message key="anticipatorySpecimen.AliquotDetails"/>
			  </span>
		   </TD>

		   <TD class="tr_anti_hdrbg_blue" rowspan="2" width=47% align="left" valign="middle">
		     <A class="black_ar" name="aliquot" HREF="#aliquot" onClick="ApplyToAll(this,'aliquot')" onmouseover="Tip(' Apply first location to all')">
			 <bean:message key="aliquots.applyFirstToAll" />
			 </A>
			 </TD>

			 <td nowrap align="left" valign="middle" class="tr_anti_hdrbg_blue"
							scope="col" width="2%" onmouseover="Tip('Create All')">
				<logic:equal name="viewSpecimenSummaryForm" property="showCheckBoxes" value="true">
					<span class="blue_ar_b">
					<bean:message key="anticipatorySpecimen.Collected"/>
					</span>
                </logic:equal>
			</td>

			<td nowrap align="left" valign="middle" class="tr_anti_hdrbg_blue" scope="col" width="2%" onmouseover="Tip('Print  All')">
			<logic:equal name="viewSpecimenSummaryForm" property="showCheckBoxes" value="true">
				<span class="blue_ar_b">
				<bean:message key="specimen.printLabel"/>
				</span>
			</logic:equal>
			</td>
          </TR>

		  <tr>
		  	<td nowrap class="tr_anti_hdrbg_blue" scope="col" width="2%" align="center" valign="middle">
			  <logic:equal name="viewSpecimenSummaryForm" property="showCheckBoxes" value="true">
					<input type="checkbox" name="chkAllAliquot" onclick="ChangeCheckBoxStatus('aliquot',this)"/>
			 </logic:equal>
		    </td>

			<td nowrap class="tr_anti_hdrbg_blue" scope="col" width="2%" align="center" valign="middle">
			  <logic:equal name="viewSpecimenSummaryForm" property="showCheckBoxes" value="true">
			     <input type="checkbox" name="printAllAliquot" onclick="ChangePrintCheckBoxStatus('aliquot',this)"/>
			 </logic:equal>
			</td>
		  </tr>
         </TABLE>
          </TD>
		 </TR>
         <TR>
          <TD colspan="3">
		    <table width="100%">
			  <tr>
			    <td>
				<md:specDetFormat4 columnHeaderListName="subSpecimenColHeaderList"
						formName="viewSpecimenSummaryForm" dataListName="aliquotList"
						dataListType="Aliquot" columnListName="columnListName"
						isReadOnly="false" displayColumnListName="subSpecdispColumnsList" />
				</td>
			  </tr>
			 </table>
		   </TD>
         </TR>
        </logic:notEmpty>

	</TABLE>

	<table width="100%">
		<tr>
			<TD align="left" valign="middle" class="tr_anti_dividerbg_blue"
				colspan=4></td>
		</tr>
	</table>

	<!-- New Table design ends -->
	<table align="bottom">
		<logic:equal name="viewSpecimenSummaryForm" property="requestType"
			value="Collection Protocol">
			<tr>
				<td><html:submit value="Save Collection Protocol" /></td>
			</tr>
		</logic:equal>
		<logic:equal name="viewSpecimenSummaryForm" property="requestType"
			value="Multiple Specimen">
			<tr>
				<td><html:submit value="Save Specimens" /></td>
			</tr>
		</logic:equal>
		<html:hidden property="generateLabel" />
		<html:hidden property="targetSuccess" />
		<html:hidden property="submitAction" />
		<html:hidden property="showCheckBoxes" />
		<html:hidden property="showLabel" />
		<html:hidden property="showbarCode" />
		<html:hidden property="readOnly" />
		<html:hidden property="showParentStorage" />
		<html:hidden property="forwardTo" />
		<html:hidden property="multipleSpEditMode" />
		<tr>
				<!-- <td class="formFieldNoBorders" colspan="5"  height="20" nowrap width="16%"> -->
				<html:hidden property="printCheckbox" style="printCheckbox"
					styleId="printCheckbox" />
				<!-- </td> -->
				<!--  Added for displaying  printer type and location -->

				<td>
				<%@ include	file="/pages/content/common/PrinterLocationTypeComboboxes.jsp"%>
				</td>
				<!--  End : Displaying   printer type and location -->
			</tr>

			<tr>
				<table border="0">
					<tr>
						<td><input class="blue_ar_b" type="button" value="Submit"
							onclick="pageSubmit()" /></td>
						<td>|&nbsp;<input class="blue_ar_b" type="button"  id="addToCart"
							value="Add To My List" onclick="onAddToCart()" /></td>
							<%
 						String	organizeTarget = "ajaxTreeGridInitCall('Are you sure you want to delete this specimen from the list?','List contains specimens, Are you sure to delete the selected list?','SpecimenListTag','SpecimenListTagItem')";
 %>
 <logic:equal name="IsToShowButton" value="false">
						<td>|&nbsp;<input class="blue_ar_b" type="button"  id="addToCart"
							value="Add To Specimen List" onclick="<%=organizeTarget%>" /></td>
							</logic:equal>
					</tr>
				</table>
			</tr>
		</table>
	<div id="divForHiddenChild"></div>
</html:form>
</body>
<script language="JavaScript" type="text/javascript">
identifyDisabledCheckBox();
displayPrinterTypeLocation();
</script>
</html>
<div id="blanket" style="display: none;"></div>
<div id="popUpDiv" style="display: none; top: 100px; left: 210.5px;">

					<a onclick="popup('popUpDiv')"><img style="float: right;"
						height='23' width='24' src='images/close_button.gif'
						border='0'> </a>
					<table class=" manage tags" width="100%" cellspacing="0"
						cellpadding="5" border="0">

						<tbody>
							<tr valign="center" height="35" bgcolor="#d5e8ff">
								<td width="28%" align="left"
									style="font-size: .82em; font-family: verdana;">
									<p>
										&nbsp&nbsp&nbsp&nbsp<b> Specimen Lists</b>
									</p>
								</td>
							</tr>
					</table>


					<div id="treegridbox"
						style="width: 530px; height: 237px; background-color: white;"></div>




					<p>
						&nbsp&nbsp&nbsp<label width="28%" align="left"
							style="font-size: .82em; font-family: verdana;"><b> List Name
								: </b> </label> <input type="text" id="newTagName" name="newTagName"
							size="20" onclick="this.value='';" maxlength="50" /><br>
					</p>
					<p>
						<%
						String specId = (String)request.getAttribute("popUpSpecList");
 String	assignTarget = "giveCall('AssignTagAction.do?entityTag=SpecimenListTag&entityTagItem=SpecimenListTagItem&objChkBoxString="+specId+"','Select at least one existing list or create a new list.','No specimen has been selected to assign.','"+specId+"')";
 %>
						<input type="button" value="ASSIGN" onclick="<%=assignTarget%> "
							onkeydown="<%=assignTarget%> " class="btn3">
														<input type="checkbox" name="objCheckbox"  id="objCheckbox" style="display:none" value="team" checked/>
					</p>
				</div>
			</div>
			<script>
function doInitGrid()
{
	grid = new dhtmlXGridObject('mygrid_container');
	grid.setImagePath("dhtmlx_suite/dhtml_pop/imgs/");
 	grid.setHeader("My Specimen Lists");
 	grid.setInitWidths("175");
 	grid.setColAlign("left");
 	grid.setSkin("dhx_skyblue");
 	grid.setEditable(false);
   	grid.attachEvent("onRowSelect", doOnRowSelected);
 	grid.init();
 	grid .load ("TagGridInItAction.do");
}
function doOnRowSelected(rId)
{
	submitTagName(rId);	 
}	
function giveCall(url,msg,msg1,id)
{
	
	document.getElementById('objCheckbox').value=id;
	document.getElementById('objCheckbox').checked=true;
	ajaxAssignTagFunctionCall(url,msg,msg1);
}
			var popupmygrid;
function doInItTreeGrid1()
{
	popupmygrid = new dhtmlXGridObject1('treegridbox');
	popupmygrid.selMultiRows = true;
	popupmygrid.imgURL = "dhtmlx_suite/dhtml_pop/imgs/";
	popupmygrid.setHeader(",<div style='text-align:center;'>My Specimen Lists</div>,");
	//popupmygrid.setNoHeader(true);
	popupmygrid.setInitWidths("25,*,40");
	popupmygrid.setColAlign("left,left,left");
	popupmygrid.setColTypes("txt,tree,txt");
	popupmygrid.setColSorting("str,str,str");
	popupmygrid.attachEvent("onRowSelect", doOnTreeGridRowSelected);
	popupmygrid.setEditable(false);
	popupmygrid.init();
	//popupmygrid.setOnOpenHandler(expand);
	popupmygrid.setSkin("dhx_skyblue");
	doInitParseTree();
	//	alert(popupmygrid.getTree(1));
}
function doOnTreeGridRowSelectedaaaa(rId)
{
	ajaxTreeGridRowSelectCall(rId); 
	//alert('sss');
	//popupmygrid.expandAll();
}
 
function doInitParseTree()
{
	popupmygrid.loadXML("TreeTagAction.do?entityTag=SpecimenListTag");
}

			</script>