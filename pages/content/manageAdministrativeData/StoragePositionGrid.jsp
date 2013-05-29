<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants,edu.wustl.catissuecore.storage.StorageContainerGridObject,edu.wustl.catissuecore.util.global.AppUtility"%>
<%@ page import="java.util.*"%>
 
<head>

<link rel="STYLESHEET" type="text/css" href="dhtmlx_suite/css/dhtmlxcombo.css">
<link rel="stylesheet" type="text/css" href="dhtmlx_suite/css/dhtmlxwindows.css"/>
<link rel="stylesheet" type="text/css" href="dhtmlx_suite/skins/dhtmlxwindows_dhx_skyblue.css"/>
<link rel="stylesheet" type="text/css"	href="dhtmlx_suite/css/dhtmlxtree.css"/>
<link rel="stylesheet" type="text/css"	href="dhtmlx_suite/css/dhtmlxgrid.css"/>
<link rel="stylesheet" type="text/css" href="css/dhtmldropdown.css"/>
<link rel="stylesheet" type="text/css"	href="dhtmlx_suite/css/dhtmlxcombo.css"/>
<link rel="stylesheet" type="text/css"	href="dhtmlx_suite/ext/dhtmlxgrid_pgn_bricks.css"/>
<link rel="stylesheet" type="text/css"	href="dhtmlx_suite/skins/dhtmlxtoolbar_dhx_blue.css"/>
<LINK href="css/catissue_suite.css" type="text/css" rel="stylesheet"/>
<link rel="stylesheet" type="text/css" href="css/styleSheet.css" />
<link rel="stylesheet" type="text/css"	href="css/clinicalstudyext-all.css" />
<link rel="stylesheet" type="text/css" href="dhtmlx_suite/skins/dhtmlxcalendar_dhx_skyblue.css" />
<link rel="stylesheet" type="text/css" href="dhtmlx_suite/css/dhtmlxcalendar.css" />
<link rel="stylesheet" type="text/css" href="dhtmlx_suite/css/dhtmlxwindows.css"/>
<link rel="stylesheet" type="text/css" href="dhtmlx_suite/skins/dhtmlxwindows_dhx_skyblue.css">

<style>
body { overflow:auto; }
td {
 width: 40px;
 height: 20px;
}
</style>
</head>

<script language="JavaScript" type="text/javascript"	src="javascripts/de/ajax.js"></script>
<script src="dhtmlx_suite/js/dhtmlxcommon.js"></script>
<script src="dhtmlx_suite/js/dhtmlxcontainer.js"></script>
<script src="dhtmlx_suite/js/dhtmlxwindows.js"></script>

<script language="JavaScript" type="text/javascript" src="jss/dhtmlDropDown.js"></script>

<script src="dhtmlx_suite/js/dhtmlxcombo.js"></script>
<script src="dhtmlx_suite/js/dhtmlxtree.js"></script>
<script src="dhtmlx_suite/ext/dhtmlxtree_li.js"></script>
<script src="jss/script.js" type="text/javascript"></script>
<script language="JavaScript" type="text/javascript"	src="jss/javaScript.js"></script>
<script language="JavaScript" type="text/javascript"	src="jss/caTissueSuite.js"></script>
<script>var imgsrc="images/de/";</script>

<SCRIPT>var imgsrc="images/";</SCRIPT>
<script language="JavaScript" type="text/javascript"	src="javascripts/de/prototype.js"></script>
<script language="JavaScript" type="text/javascript"	src="javascripts/de/scr.js"></script>
<script language="JavaScript" type="text/javascript"	src="jss/ext-base.js"></script>
<script language="JavaScript" type="text/javascript"	src="jss/ext-all.js"></script>
<script language="JavaScript" type="text/javascript"	src="javascripts/de/ajax.js"></script>

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
<script type="text/javascript" src="jss/wz_tooltip.js"></script>
<%
	String collectionProtocolId="";
		if (request.getAttribute(Constants.COLLECTION_PROTOCOL_ID)==null)
			collectionProtocolId="";
		else
		 collectionProtocolId =(String) request.getAttribute(Constants.COLLECTION_PROTOCOL_ID);

	String storageContainerName =(String) request.getAttribute(Constants.CONTAINER_NAME);

	String controlName =(String) request.getAttribute("controlName");
	System.out.println("Control name :: "+controlName);
%>

<script>
var globalPageOf="";
var globalContName="";
var globalPos1="";
var globalPos2="";
function getActionToDoURL()
{
	var className="${requestScope.holdSpecimenClass}";//parent.window.document.getElementById('className').value;
	//alert(className);
	var sptype="${requestScope.holdSpecimenType}";
	var collectionProtocolId="<%=collectionProtocolId%>";
	var containerName="${requestScope.containerName}";
	if(dhtmlxCombo!=null)
	{
		containerName=dhtmlxCombo.getComboText();//dhtmlxCombo.getActualValue();
	}
	var url="CatissueCommonAjaxAction.do?type=getStorageContainerListForDHTMLXcombo&holdSpecimenClass="+className+"&specimenType="+sptype+ "&holdCollectionProtocol=" + collectionProtocolId+"&selectedContainerName=${requestScope.containerName}";
	if(reloadGrid!=null && reloadGrid)
	{
		url=url+"&containerName="+containerName;
	}
	if(populateValueInCombo!=null)
	{
		//alert(populateValueInCombo);
		url=url+"&populateValueInCombo="+populateValueInCombo;
	}
	
	return url;
}
</script>
<body onload="checkSpecimenStatus();showContainerGrid();">
<%@ include file="/pages/content/common/ActionErrors.jsp" %>
<table summary="" cellpadding="0" cellspacing="0" border="0"  width="100%" ><tr><td class="black_ar" align="right" style="padding-left:5px;padding-right:5px"><b>Container</b></td><td class="black_ar" align="left"><b>Map</b></td><td></td></tr><tr><td class="black_ar" colspan="3"><div id="error" style="height:50%;width:100%;"></div></td></tr>
<tr>
	
	<td align="right" class="black_ar_md" style="padding-top:5px;padding-right:5px;padding-left:5px"><b>Name:</b></td>
	<td align="left">
	
	<table valign="center" cellpadding="0" cellspacing="0" border="0" >
						<tr><td width="30%">
		<div id="comboDiv" style="width:200px;display:block;"></div>
		<script>
			var reloadGrid=true,populateValueInCombo=true;
			var dhtmlxCombo=new dhtmlXCombo("comboDiv","storageContainerDropDown",200);
			var url=getActionToDoURL();
			dhtmlxCombo.loadXML(url,function(){showContainerGrid();});
			dhtmlxCombo.attachEvent("onOpen", 
			function(){
				reloadGrid=false;
				populateValueInCombo=false;
				dhtmlxCombo.loadXML(getActionToDoURL());
			});  
			dhtmlxCombo.attachEvent("onChange", 
			function()
			{
				reloadGrid=true;
				document.getElementById('containerGrid').innerHTML="";
				showContainerGrid();
			});
			dhtmlxCombo.attachEvent("onKeyPressed", 
			function(keyCode){
				if(keyCode != 8 || keyCode != 46)
				{
					document.getElementById('containerGrid').innerHTML="";
					document.getElementById('error').innerHTML="";
					reloadGrid=true;
					populateValueInCombo=false;

				}
				if(keyCode != 13 && keyCode != 39 && keyCode != 37 )
				{	
							populateValueInCombo=false;
							dhtmlxCombo.enableFilteringMode(true,getActionToDoURL(),false);
				}
			});
	</script>
	
			&nbsp;<td class="black_ar_md"  style="padding-left:15px;" >
				<input type="text" class="black_ar_md_new"  size="1" id="pos11" name="pos11" value="${sessionScope.pos1Val}"disabled= "false"/>
			</td>
			<td class="black_ar_md" align="left" style="padding-left:5px;">
				<input type="text" class="black_ar_md_new"  size="1" id="pos22" name="pos22" value="${sessionScope.pos2Val}" disabled= "false"/>
				<input type="hidden" id="pos1" name="pos1" />
				<input type="hidden" id="pos2" name="pos2" />
			</td>
			<td class="black_ar_md" align="right" style="padding-top:2px">
					<input type="radio" name="virtualCont" onClick="setVirtual()"/>&nbsp;</td><td class="black_ar_md" align="left" style="padding-top:5px">Virtual
			</td>
			
			</tr>
			</table>
		
			<div id="storageContainerNameDiv" style="display:block">
			
			</div>
		</td>
		<td></td>
	</tr>
	<tr>
		
		<td colspan="3">
			<div id="containerGrid" style="height:100%;width:100%;">
			</div>
		</td>
	</tr>
</table>
<table  style="table-layout: fixed;" cellpadding="0" cellspacing="0" border="0"  width="100%" >
<tr>
	<td>
			
	</td>
	</tr>
</table>

</body>
<script>
var fromStoragePosition1=parent.window.document.getElementById('storageContainerPosition').value;
	//alert(fromStoragePosition);
	var fromContainerName1=fromStoragePosition1.substring(0,fromStoragePosition1.lastIndexOf('(')-1);
	var storagePositions1=fromStoragePosition1.substring(fromStoragePosition1.lastIndexOf('(')+1,fromStoragePosition1.lastIndexOf(')')).split(",");
	document.getElementById('pos1').value=storagePositions1[0];
	document.getElementById('pos2').value=storagePositions1[1];
function setTextBoxValue(elementId1,elementValue1,elementId2,elementValue2,pageOf)
{
	if(pageOf == 'pageOfSpecimen')
	{
		var id1 = document.getElementById("pos1");	
	id1.value = elementValue1;
	var id2 = document.getElementById("pos2");	
	id2.value = elementValue2;
	}
	else
	{
		var id1 = document.getElementById(elementId1);	
	id1.value = elementValue1;
	var id2 = document.getElementById(elementId2);	
	id2.value = elementValue2;
	}
	
	var speCollStat = '${requestScope.collStatus}';
	//alert(speCollStat);
	//alert(dhtmlxCombo.getActualValue());
	if(speCollStat != null && speCollStat != "" )
	{
		if(speCollStat == 'Collected')
		{
			transferSpecimen();
		}
		else
		{
			updatePosition();
		}
	}
	
}

function updatePosition()
{
	var pos1=document.getElementById('pos1').value;
	var pos2=document.getElementById('pos2').value;
	var containerName=dhtmlxCombo.getComboText();
	
	var storagePosition=parent.window.document.getElementById('storageContainerPosition');
		var newStoragePosition= containerName+" ("+pos1+","+pos2+")";
		storagePosition.value=newStoragePosition;
		storagePosition.title=newStoragePosition;
		var isVirtual=parent.window.document.getElementById('isVirtual');
		isVirtual.value=false;
		var container = parent.window.document.getElementById('containerName');
		var position1= parent.window.document.getElementById('pos1');
		var position2= parent.window.document.getElementById('pos2');
		
		container.value = containerName;
		position1.value = pos1;
		position2.value= pos2;
		var isVirtual = parent.window.document.getElementById('isVirtual');
		
		if(isVirtual != null)
		{
			isVirtual.value="false";
			
		}
		parent.window.dhxWins.window("containerPositionPopUp").close();
}

function transferVrtualSpecimen()
{
	var toPos1="";
	var toPos2="";
	var toContainerName="";//dhtmlxCombo.getComboText();//dhtmlxCombo.getActualValue();//document.getElementById('storageContainerDropDown').value;
	var fromStoragePosition=parent.window.document.getElementById('storageContainerPosition').value;
	//alert(fromStoragePosition);
	var fromContainerName=fromStoragePosition.substring(0,fromStoragePosition.lastIndexOf('(')-1);
	var storagePositions=fromStoragePosition.substring(fromStoragePosition.lastIndexOf('(')+1,fromStoragePosition.lastIndexOf(')')).split(",");
	var fromPos1=storagePositions[0];
	var fromPos2=storagePositions[1];
	var specimenId=parent.window.document.getElementsByName('id')[0].value;
	
	var speCollStat = '${requestScope.collStatus}';
	
	//alert(dhtmlxCombo.getActualValue());
	if(speCollStat != null && speCollStat != "" )
	{
		if(speCollStat == 'Collected')
		{
	
			var request = newXMLHTTPReq();
			if(request == null)
			{
				alert ("Your browser does not support AJAX!");
				return;
			}
			var handlerFunction = getReadyStateHandler(request,updateSpecimenValues,true);
			
			if((fromContainerName == '' || fromContainerName=='undefined'))
			{
				updateSpecimenValues('virtual');
				return false;
			}
			
			request.onreadystatechange = handlerFunction;
			var param="fromContainerName="+fromContainerName+"&fromPos1="+fromPos1+"&fromPos2="+fromPos2+"&toPos1="+toPos1+"&toPos2="+toPos2+"&toContainerName="+toContainerName+"&isVirtual=${requestScope.isVirtual}&specimenId="+specimenId;

			var url = "TransferEventAction.do";
			request.open("POST",url,true);
			
			request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");	
			request.send(param);
		}
		else
		{
			updateSpecimenValues('virtual');
			return false;
		}
	}
	
	return false;
}
function transferSpecimen()
{

	var toPos1=document.getElementById('pos1').value;
	var toPos2=document.getElementById('pos2').value;
	var toContainerName=dhtmlxCombo.getComboText();//dhtmlxCombo.getActualValue();//document.getElementById('storageContainerDropDown').value;
	var fromStoragePosition=parent.window.document.getElementById('storageContainerPosition').value;
	//alert(fromStoragePosition);
	var fromContainerName=fromStoragePosition.substring(0,fromStoragePosition.lastIndexOf('(')-1);
	var storagePositions=fromStoragePosition.substring(fromStoragePosition.lastIndexOf('(')+1,fromStoragePosition.lastIndexOf(')')).split(",");
	var fromPos1=storagePositions[0];
	var fromPos2=storagePositions[1];
	var specimenId=parent.window.document.getElementsByName('id')[0].value;
	
	var request = newXMLHTTPReq();
	if(request == null)
    {
		alert ("Your browser does not support AJAX!");
		return;
	}
	var handlerFunction = getReadyStateHandler(request,updateSpecimenValues,true);
	
    request.onreadystatechange = handlerFunction;
	var param="fromContainerName="+fromContainerName+"&fromPos1="+fromPos1+"&fromPos2="+fromPos2+"&toPos1="+toPos1+"&toPos2="+toPos2+"&toContainerName="+toContainerName+"&isVirtual=${requestScope.isVirtual}&specimenId="+specimenId;

    var url = "TransferEventAction.do";
 	request.open("POST",url,true);
	
	request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");	
	request.send(param);
	return false;
}

function updateSpecimenValues(responseText)
{
	var msg =responseText;
	
	if(msg.indexOf('#') != -1)
	{
		var tag=(msg).split("#");
		var containerName=tag[0];
		var pos1=tag[1];
		var pos2=tag[2];
		var storagePosition=parent.window.document.getElementById('storageContainerPosition');
		var newStoragePosition= containerName+" ("+pos1+","+pos2+")";
		storagePosition.value=newStoragePosition;
		storagePosition.title=newStoragePosition;
		var isVirtual=parent.window.document.getElementById('isVirtual');
		isVirtual.value=false;
		var container = parent.window.document.getElementById('containerName');
		var position1= parent.window.document.getElementById('pos1');
		var position2= parent.window.document.getElementById('pos2');
		//alert(container.value);
		//alert(position2.value);
		//alert(position1.value);
		container.value = containerName;
		position1.value = pos1;
		position2.value= pos2;
		parent.window.dhxWins.window("containerPositionPopUp").close();
	}
	else if(msg == 'virtual')
	{
		var storagePosition=parent.window.document.getElementById('storageContainerPosition');
		//alert('ddd');
		storagePosition.value='Virtually Located';
		storagePosition.title='Virtually Located';
		var isVirtual=parent.window.document.getElementById('isVirtual');
		isVirtual.value=true;
		parent.window.dhxWins.window("containerPositionPopUp").close();
	}
	else
	{
		document.getElementById('error').innerHTML="<font color='red'>"+ msg+"</font>";
	}
}

/*var containerDropDownInfo, scGrid;
var scGridVisible = false;


function doOnLoad()
{
	var url=getActionToDoURL();
	//Drop Down components information
	containerDropDownInfo = {gridObj:"storageContainerGrid", gridDiv:"storageContainer", dropDownId:"storageContainerDropDown", pagingArea:"storageContainerPagingArea", infoArea:"storageContainerInfoArea", onOptionSelect:"containerOnRowSelect", actionToDo:url, callBackAction:onContainerListReady, visibilityStatusVariable:scGridVisible, propertyId:'selectedContainerName'};
	// initialising grid
	scGrid = initDropDownGrid(containerDropDownInfo,false); 
}

function containerOnRowSelect(id,ind)
{
	document.getElementById('selectedContainerName').value = id;
	document.getElementById(containerDropDownInfo['dropDownId']).value = scGrid.cellById(id,ind).getValue();
	document.getElementById("pos1").value="";
	document.getElementById("pos2").value="";
	hideGrid(containerDropDownInfo['gridDiv']);
	scGridVisible = false;
	document.getElementById('containerGrid').innerHTML="";
	document.getElementById('error').innerHTML="";
	onContainerDetailDisplay("storageContainerDropDown");
}*/ 

function onContainerDetailDisplay(controlName)
{


	var name;
	
	 var specimenPosition=parent.window.document.getElementById("storageContainerPosition")
		
		if(dhtmlxCombo!=null)// && specimenPosition!=null)
	{
		name=dhtmlxCombo.getComboText();//dhtmlxCombo.getActualValue();
	}
	else if(controlName!=null)
	{
///		name = parent.window.document.getElementById(controlName).value;
	}
	
	else
	{
		name='<%=storageContainerName%>';
	}
	//alert(name);
    var request = newXMLHTTPReq();	
	if(request == null)
    {
		alert ("Your browser does not support AJAX!");
		return;
	}
	var handlerFunction =  getReadyStateHandler(request,createPositionGrid,true);
    request.onreadystatechange = handlerFunction;
	var param = "containerName="+name;
    var url = "StoragePositionAjaxAction.do";
 	request.open("POST",url,true);
	
	request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");	
	request.send(param);
	return false;
}

function createPositionGrid(responseString)
{
	var obj = eval('( '+ responseString +')');
	//alert(obj);
	var htmlString = "";
	var posx = obj.posx;
	var posy = obj.posy;
	var dimensionOne = eval('( '+ obj.dimensionOne +')');
	var dimensionTwo = eval('( '+ obj.dimensionTwo +')');
	var dimensionOneLabel = obj.dimensionOneLabel;
	var dimensionTwoLabel = obj.dimensionTwoLabel;
	var pos1ControlName = obj.pos1ControlName;
	var pos2ControlName = obj.pos2ControlName;
	var pageOf = obj.pageOf;
	var controlName = obj.controlName;
	//alert("globalPageOf   "+globalPageOf);
	globalPageOf=pageOf;
	globalContName=controlName;
	globalPos1 = pos1ControlName;
	globalPos2 = pos2ControlName;
	var oneDimensionCapacity = obj.dimensionOneCapacity;
	var twoDimensionCapacity = obj.dimensionTwoCapacity;
	var occupiedPositon = obj.occupiedPositions;
	var containerMap = eval('( '+ obj.containerMap +')');
	var titleMap = eval('( '+ obj.titleMap +')');
	var containerDiv = document.getElementById('containerGrid');
	if(responseString != '')
	{
		/*var tbl = document.createElement("table");
		//tbl.setStyle("table-layout: fixed");
		tbl.style.cssText ="table-layout: fixed;width=100%;";
                tbl.setAttribute("id", "tblId");
                document.getElementById("containerGrid").appendChild(tbl);
				var tr = document.getElementById("tblId").insertRow(0);
				var cell=tr.insertCell(0);
				cell.style.cssText ="width:4px";
				cell.className="black_ar_md";
				cell.innerHTML='rest';
				cell=tr.insertCell(1);
				cell.style.cssText ="width:*";
				cell.className="black_ar_md";
				cell.innerHTML='test';
				cell.*/
                //document.getElementById("tblId").innerHTML = '<tr><td>Product name</td><td>Price</td><td>Competitor</td><td>Price</td></tr><tr><td><input type="text"></td><td><input type="text"><td><input type="text"></td><td><input type="text"></td><td><input type="button" value="Add" onclick="addRow()"></td><td></td></tr>';
}
	/*var html="<table  style=\"table-layout: fixed;\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"  width=\"100%\" >";
	html=html+"<tr>";
	html=html+"	<td style='width:4px;'/>";
	html=html+"	<td style='width:*' class=\"black_ar_md\"></td>";
	html=html+"	</tr>";
	html=html+"<tr><td colspan=2>";*/
	var html="<table style='table-layout: fixed;' cellspacing='1' cellpadding='0'>";
	html=html+"<tr>";
	var temp=parent.window.document.getElementById('storageContainerPosition');
	var functionName="setTextBoxValueInParent"
	if(temp!=null)
	{
		functionName="setTextBoxValue";
	}
	//alert(functionName);
	for(var i=0;i<=twoDimensionCapacity;i++)
	{
		if(i!=0)
		{
			html=html+"<td height='40px' width='20px' class='subtdPosGrid' align='center'>";
			html=html+dimensionTwo[i];
			html=html+"</td>";
		}else
		{
			html=html+"<td height='40px' width='20px' class='subtdPosGrid' align='center'>"+"</td>";
		}
	}
	html=html+"</tr>";
	for(var i=0;i<oneDimensionCapacity;i++)
	{
		html=html+"<tr>";
		for(var j=0;j<=twoDimensionCapacity;j++)
		{ 
			if(j==0)
			{
				html=html+"<td height='40px' width='20px' class='subtdPosGrid' align='center'>";
				html=html+dimensionOne[i+1];
				html=html+"</td>";
			}
			else
			{
				var k=j;
				html=html+"<td height='40px' width='20px'";
				var title=titleMap[i][j-1][k];
				var position=title.split(",");
				if(containerMap[i][j-1][k])
				{
					html=html+" style='min-width:20px;cursor:pointer;' onMouseOver=\"this.bgColor='##83F2B9'\" onMouseOut=\"this.bgColor='#008000'\" title='"+title+"' bgColor=#008000 onclick=\""+functionName+"('"+pos1ControlName+"','"+position[0]+"','"+pos2ControlName+"','"+position[1]+"','"+pageOf+"','"+controlName+"');\">";
				}			
				else
				{
					html=html+" style='min-width:20px' title='"+title+"' bgcolor='#F25252;'>";
				}
				html=html+"</td>";
			}
		}
		html=html+"</tr>";
	}
	html=html+"</table></td></tr></table>";
	document.getElementById('containerGrid').innerHTML=html;
	
}
function onContainerListReady()
	{
	}
	
function showContainerGrid()
{
	if(reloadGrid)
	{
		var controlName='<%=controlName%>';
		//alert(controlName);
		var contName='<%=storageContainerName%>';
		var temp=parent.window.document.getElementById('storageContainerPosition');
		if(null!=temp)
		{
			controlName="storageContainerDropDown";
			//document.getElementById(containerDropDownInfo['dropDownId']).value=contName;
			//document.getElementById("storageContainerDropDown").value=contName;
		}
		//alert(controlName);
		onContainerDetailDisplay(controlName);
	}
}
function setVirtual()
{
//alert(globalPageOf);
//alert(globalContName);

	if(globalPageOf == 'pageOfSpecimen')
	{
		transferVrtualSpecimen();
	}
	else
	{
		var id1 = parent.window.document.getElementById(globalPos1);
		if(id1 != null)
		{
			id1.value = "";
		}
		var id2 = parent.window.document.getElementById(globalPos2);	
		if(id2 != null)
		{
			id2.value = "";
		}
		if(globalPageOf == 'pageOfAntispec')
		{
			var rowIndex = globalContName.substring(globalContName.indexOf('_')+1,globalContName.length);
			//alert(rowIndex);
			var contId = parent.window.document.getElementById("storageContainerDropDown_"+rowIndex);
			contId.value='-1';
			var contName = parent.window.document.getElementById(globalContName);
			contName.value='Virtual';
		}
		else if(globalPageOf == 'pageOfAliquot')
		{
			var rowIndex = globalContName.substring(globalContName.indexOf('_')+1,globalContName.length);
			var contId = parent.window.document.getElementById("value(Specimen:"+rowIndex+"_StorageContainer_id_fromMap)");
			contId.value="-1";
			var contName = parent.window.document.getElementById(globalContName);
			contName.value='Virtual';
		}
		else if(globalPageOf == 'pageOfShipping')
		{
			var rowIndex = globalContName.substring(globalContName.indexOf('_')+1,globalContName.length);
			var contId = parent.window.document.getElementById("specimenDetails(selectedContainerName_"+rowIndex+")");
			contId.value="-1";
			var contName = parent.window.document.getElementById(globalContName);
			contName.value='Virtual';
		}
		else if(globalPageOf == 'pageOfNewSpecimen')
		{
			var contId = parent.window.document.getElementById('containerId');
			contId.value='-1';
			var contName = parent.window.document.getElementById('storageContainerDropDown');
			contName.value='Virtual';
		}
		else if(globalPageOf == 'pageOfderivative')
		{
			var contId = parent.window.document.getElementById('containerId');
			contId.value='-1';
			var contName = parent.window.document.getElementById(globalContName);
			contName.value='Virtual';
		}
		else if(globalPageOf == 'pageOfBulkEvent')
		{
			var rowIndex = globalContName.substring(globalContName.indexOf('_')+1,globalContName.length);
			var contId = parent.window.document.getElementById(globalContName);
			contId.value='-1';
			var contName = parent.window.document.getElementById(globalContName);
			contName.value='Virtual';
		}
		else if(globalPageOf == 'pageOfTransfer')
		{
			
			var contId = parent.window.document.getElementById('containerId');
			contId.value='-1';
			var contName = parent.window.document.getElementById(globalContName);
			contName.value='Virtual';
		}
		parent.window.dhxWins.window("containerPositionPopUp").close();
	}
	
	
}
function setTextBoxValueInParent(elementId1,elementValue1,elementId2,elementValue2,pageOf,controlName)
{//alert("elementId1:  "+elementId1+"  elementValue1:  "+elementValue1+"  elementId2:   "+elementId2+"elementValue2:   "+elementValue2);
	var id1 = parent.window.document.getElementById(elementId1);	
	id1.value = elementValue1;
	var id2 = parent.window.document.getElementById(elementId2);	
	id2.value = elementValue2;
	
	//alert(dhtmlxCombo.getSelectedValue());
	if(pageOf == 'pageOfAntispec')
	{

		var rowIndex = controlName.substring(controlName.indexOf('_')+1,controlName.length);
		//alert(rowIndex);
		var contId = parent.window.document.getElementById("storageContainerDropDown_"+rowIndex);
		contId.value=dhtmlxCombo.getSelectedValue();
		var contName = parent.window.document.getElementById(controlName);
		contName.value=dhtmlxCombo.getSelectedText();
	}
	else if(pageOf == 'pageOfAliquot')
	{
		var rowIndex = controlName.substring(controlName.indexOf('_')+1,controlName.length);
		var contId = parent.window.document.getElementById("value(Specimen:"+rowIndex+"_StorageContainer_id_fromMap)");
		contId.value=dhtmlxCombo.getSelectedValue();
		var contName = parent.window.document.getElementById(controlName);
		contName.value=dhtmlxCombo.getSelectedText();
	}
	else if(globalPageOf == 'pageOfShipping')
		{
			var rowIndex = globalContName.substring(globalContName.indexOf('_')+1,globalContName.length);
			var contId = parent.window.document.getElementById("specimenDetails(selectedContainerName_"+rowIndex+")");
			contId.value=dhtmlxCombo.getSelectedValue();
			var contName = parent.window.document.getElementById(globalContName);
			contName.value=dhtmlxCombo.getSelectedText();
		}
	else if(pageOf == 'pageOfNewSpecimen')
	{
		var contId = parent.window.document.getElementById('containerId');
		contId.value=dhtmlxCombo.getSelectedValue();
		var contName = parent.window.document.getElementById('storageContainerDropDown');
		contName.value=dhtmlxCombo.getSelectedText();
	}
	else if(pageOf == 'pageOfderivative')
	{
		var contId = parent.window.document.getElementById('containerId');
		contId.value=dhtmlxCombo.getSelectedValue();
		var contName = parent.window.document.getElementById(controlName);
		contName.value=dhtmlxCombo.getSelectedText();
	}
	else if(globalPageOf == 'pageOfBulkEvent')
		{
		//alert(globalContName);
			var rowIndex = globalContName.substring(globalContName.indexOf('_')+1,globalContName.length);
			var contId = parent.window.document.getElementById(globalContName);
			contId.value=dhtmlxCombo.getSelectedValue();
			var contName = parent.window.document.getElementById(globalContName);
			contName.value=dhtmlxCombo.getSelectedText();
		}
	else if(globalPageOf == 'pageOfTransfer')
		{
		//alert(globalContName);
			
			var contId = parent.window.document.getElementById('containerId');
			contId.value=dhtmlxCombo.getSelectedValue();
			var contName = parent.window.document.getElementById(globalContName);
			contName.value=dhtmlxCombo.getSelectedText();
		}
	
	parent.window.dhxWins.window("containerPositionPopUp").close();
}

function checkSpecimenStatus()
{
	var temp=parent.window.document.getElementById('storageContainerPosition');

	if(temp!=null)
	{
		document.getElementById("storageContainerNameDiv").style.display="none";
	}
	var fromStoragePosition=parent.window.document.getElementById('storageContainerPosition').value;

	if(fromStoragePosition != 'Virtually Located')
	{
		var fromContainerName=fromStoragePosition.substring(0,fromStoragePosition.lastIndexOf('(')-1);
		var storagePositions=fromStoragePosition.substring(fromStoragePosition.lastIndexOf('(')+1,fromStoragePosition.lastIndexOf(')')).split(",");
		var fromPos1=storagePositions[0];
		var fromPos2=storagePositions[1];

		document.getElementById("pos11").value=fromPos1;
		document.getElementById("pos22").value=fromPos2;
	}
	else
	{
		document.getElementById("pos11").value="";
		document.getElementById("pos22").value="";
	}
}


</script>
