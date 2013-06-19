//Functions for Popup
var count;
  var tCount=300;
var tagCount;
count=tCount;
function toggle(div_id) {
	var el = document.getElementById(div_id);
	if ( el.style.display == 'none' ) {	el.style.display = 'block';}
	else {el.style.display = 'none';}
}
function blanket_size(popUpDivVar) { 
	if (typeof window.innerWidth != 'undefined') {
		viewportheight = window.innerHeight;
	} else {
		viewportheight = document.documentElement.clientHeight;
	}
	if ((viewportheight > document.body.parentNode.scrollHeight) && (viewportheight > document.body.parentNode.clientHeight)) {
		blanket_height = viewportheight;
	} else {
		if (document.body.parentNode.clientHeight > document.body.parentNode.scrollHeight) {
			blanket_height = document.body.parentNode.clientHeight;
		} else {
			blanket_height = document.body.parentNode.scrollHeight;
		}
	}
	var blanket = document.getElementById('blanket');
	blanket.style.height = blanket_height +230+'px';
	var popUpDiv = document.getElementById(popUpDivVar);
	popUpDiv_height=blanket_height/2-228;//200 is half popup's height
	popUpDiv.style.top = popUpDiv_height + 'px';
}
function window_pos(popUpDivVar) {
	if (typeof window.innerWidth != 'undefined') {
		viewportwidth = window.innerHeight;
	} else {
		viewportwidth = document.documentElement.clientHeight;
	}
	if ((viewportwidth > document.body.parentNode.scrollWidth) && (viewportwidth > document.body.parentNode.clientWidth)) {
		window_width = viewportwidth;
	} else {
		if (document.body.parentNode.clientWidth > document.body.parentNode.scrollWidth) {
			window_width = document.body.parentNode.clientWidth;
		} else {
			window_width = document.body.parentNode.scrollWidth;
		}
	}
	var popUpDiv = document.getElementById(popUpDivVar);
	window_width=window_width/2-270;//200 is half popup's width
	popUpDiv.style.left = window_width + 'px';
}

function popup(windowname) {
	blanket_size(windowname);
	window_pos(windowname);
	toggle('blanket');
	toggle(windowname);		
}
//---------------------------popup fuctions End---------------------------

//---------------------------Tree Grid Function---------------------------
function doOnTreeGridRowSelected(rId)
{
	//ajaxTreeGridRowSelectCall(rId); 
}
var popupmygrid;
function doInItTreeGrid()
{
	popupmygrid = new dhtmlXGridObject('treegridbox');
	popupmygrid.selMultiRows = true;
	popupmygrid.imgURL = "dhtmlx_suite/dhtml_pop/imgs/";
	popupmygrid.setHeader(",<img  style='height: 3px;' src='dhtmlx_suite/dhtml_pop/imgs/blank.gif' >,");
	//popupmygrid.setNoHeader(true);
	popupmygrid.setInitWidths("25,*,40");
	popupmygrid.setColAlign("left,left,left");
	popupmygrid.setColTypes("txt,tree,txt");
	popupmygrid.setColSorting("str,str_custom,str");
	popupmygrid.attachEvent("onRowSelect", doOnTreeGridRowSelected);
	popupmygrid.setCustomSorting(str_custom, 1);
	popupmygrid.setEditable(false);
	popupmygrid.init();
	popupmygrid.setSkin("dhx_skyblue");
	doInitParseTree();
}

function str_custom(a,b,order){     
    if (order=="asc")
        return (a.toLowerCase()>b.toLowerCase()?1:-1);
    else
        return (a.toLowerCase()>b.toLowerCase()?-1:1);
} 

function doInitParseTree()
{
	popupmygrid.loadXML("TreeGridInItAction.do?entityTag="+entityTag);

}
//---------------------------Tree Grid Function End-----------------------
//Ajax Function for Initialize Grid and open Pop up
var queryDeleteMsg=null;
var folderDeleteMsg=null;
//entityTag name is the name u have to set On Jsp that is similar to entity-name from hbm file.
var entityTag;
var entityTagItem;
var xmlHttpobj = false;
function ajaxPopupFunctionCall() {

var url = "TagGridInItAction.do";
 if (window.XMLHttpRequest)
  { 
  xmlHttpobj=new XMLHttpRequest();
  }
else
  { 
  xmlHttpobj=new ActiveXObject("Microsoft.XMLHTTP");
  }
	  
	xmlHttpobj.onreadystatechange = showPopup;
	xmlHttpobj.open("POST", url, true);
	xmlHttpobj.send(null);

}
function showPopup() {
	if (xmlHttpobj.readyState == 4) 
	{
	}
}
 
//Ajax Function for Assign Query to folder (Assign Button on Pop up) 
function ajaxAssignTagFunctionCall(url,msg,msg1) 
{
 
if (window.XMLHttpRequest)
  { 
  xmlHttpobj=new XMLHttpRequest();
  }
else
  { 
  xmlHttpobj=new ActiveXObject("Microsoft.XMLHTTP");
  }
	var tagChkBoxString = new String();
	var objChkBoxString = new String();
	var i;
	xmlHttpobj.open("POST", url, true);
	xmlHttpobj.setRequestHeader("Content-Type",
			"application/x-www-form-urlencoded");
	
	var tagChkBoxAr = document.getElementsByName("tagCheckbox");
	var objCheckBoxAr = document.getElementsByName("objCheckbox");
	var tagName = document.getElementById('newTagName').value;
	document.getElementById('newTagName').value ='';


	for (i = 0; i < tagChkBoxAr.length; i++) {
		if (tagChkBoxAr[i].checked == true) {
			tagChkBoxString = tagChkBoxAr[i].value + "," + tagChkBoxString;
			tagChkBoxAr[i].checked = false;
			
		}

	}
	
	for (i = 0; i < objCheckBoxAr.length; i++) {
		if (objCheckBoxAr[i].checked == true) {
			objChkBoxString = objCheckBoxAr[i].value + ","
					+ objChkBoxString;
			 
		}
	}
	
	if (objChkBoxString=='')
	{
		alert(msg1);
		 
 	}
	else if(tagChkBoxString==''&&tagName=='')
 	{
		alert(msg);
		 
	}
	else if((objChkBoxString!=''&& tagChkBoxString!='')||(objChkBoxString!=''&& tagName!=''))
 	{
		 xmlHttpobj.onreadystatechange =assignTagItemFunction;
		 if (entityTag != 'SpecimenListTag'){
			 clearCheckboxes(objCheckBoxAr)
		 }
xmlHttpobj.send("&tagChkBoxString=" + tagChkBoxString + "&objChkBoxString="
			+ objChkBoxString + "&tagName=" + tagName +"&entityTag="+ entityTag+"&entityTagItem="+ entityTagItem);
	}
	else if((queryChkBoxString!=''&& tagChkBoxString!=''&& tagName!=''))
	{
		 xmlHttpobj.onreadystatechange =assignTagItemFunction;
		 if (entityTag != 'SpecimenListTag'){
			 clearCheckboxes(objCheckBoxAr);
		 }
xmlHttpobj.send("&tagChkBoxString=" + tagChkBoxString + "&objChkBoxString="
			+ objChkBoxString + "&tagName=" + tagName +"&entityTag="+ entityTag+"&entityTagItem="+ entityTagItem);
	}

 	

}

function clearCheckboxes(objCheckBoxAr){
	for (i = 0; i < objCheckBoxAr.length; i++) 
	{
		if (objCheckBoxAr[i].checked == true) 
		{
			 objCheckBoxAr[i].checked = false;
		}
	}	
}



function assignTagItemFunction() {
		if (xmlHttpobj.readyState == 4) {	
			popup('popUpDiv');
			doInitGrid();	 
		}
}

function ajaxShareTagFunctionCall(url,msg) 
{
	if (window.XMLHttpRequest)
	  { 
	  xmlHttpobj=new XMLHttpRequest();
	  }
	else
	  { 
	  xmlHttpobj=new ActiveXObject("Microsoft.XMLHTTP");
	  }
		var tagChkBoxString = new String();
		var userString = new String();
		var i;
		xmlHttpobj.open("POST", url, true);
		xmlHttpobj.setRequestHeader("Content-Type",
				"application/x-www-form-urlencoded");
	 
		var tagChkBoxAr = document.getElementsByName("tagCheckbox");
		var userIdAr = document.getElementById('protocolCoordinatorIds');
		var tagName = document.getElementById('newTagName').value;
		document.getElementById('newTagName').value ='';
		
		xmlHttpobj.onreadystatechange = closePopup;
		for (i = 0; i < tagChkBoxAr.length; i++) {
			if (tagChkBoxAr[i].checked == true) {
				tagChkBoxString = tagChkBoxAr[i].value + "," + tagChkBoxString;
				tagChkBoxAr[i].checked = false;
				
			}
		}
		for (i = 0; i < userIdAr.options.length; i++) {
				userString = userIdAr.options[i].value + "," + userString;		
		}
		if(tagChkBoxString==''&&tagName=='')
	 	{
			alert(msg);
		}else{
			document.getElementById("shareButton").style.display="none";
			document.getElementById("loadingImg").style.display="block";
			xmlHttpobj.send("&tagChkBoxString=" + tagChkBoxString + "&tagName=" + tagName +"&entityTag="+ entityTag
				+"&userString=" + userString);
		}
}

function closePopup() {
	if (xmlHttpobj.readyState == 4) {	
		popup('popUpDiv');
		doInitGrid();	 
	}
}
 
//Ajax Function for Initialize TreeGrid
function ajaxTreeGridInitCall(msg,msg1,entityTagName,entityTagItemName) {
queryDeleteMsg=msg;
folderDeleteMsg=msg1;
entityTag=entityTagName;
entityTagItem=entityTagItemName;
var url = "TreeGridInItAction.do";
if (window.XMLHttpRequest)
  { 
  xmlHttpobj=new XMLHttpRequest();
  }
else
  { 
  xmlHttpobj=new ActiveXObject("Microsoft.XMLHTTP");
  }
		xmlHttpobj.open("POST", url, true); 
		xmlHttpobj.setRequestHeader("Content-Type",
				"application/x-www-form-urlencoded");
		xmlHttpobj.onreadystatechange = showTreeMessage;
	 	xmlHttpobj.send("&entityTag=" +entityTag);
} 
function showTreeMessage() {
	if (xmlHttpobj.readyState == 4) {
		doInItTreeGrid(); 
		popup('popUpDiv');
	
		var objCheckBoxAr = document.getElementsByName("objCheckbox");
		var isObjChecked = false;
		
		for (i = 0; i < objCheckBoxAr.length; i++) {
		if (objCheckBoxAr[i].checked == true) {
				isObjChecked = true;
				break;
			}  
		} 

		if (isObjChecked == true) {		  
			document.getElementById("assignButton").style.display="block"
			document.getElementById("shareButton").style.display="none"
			document.getElementById("multiSelectId").style.display="none";
			document.getElementById("multiSelectId").style.visibility = 'hidden';
			document.getElementById("newTagName").style.display="inline";
			document.getElementById("newTagLabel").style.display="inline";
			document.getElementById("shareLabel").style.display="none";
			document.getElementById("treegridbox").style.height="240px";
			document.getElementById("loadingImg").style.display="none";
	
			setHeader(isObjChecked);
		} else {
			document.getElementById("assignButton").style.display="none"
			document.getElementById("shareButton").style.display="block"
			document.getElementById("multiSelectId").style.display="block";
			document.getElementById("multiSelectId").style.visibility = 'visible';
			document.getElementById("newTagName").style.display="none";
			document.getElementById("newTagLabel").style.display="none";
			document.getElementById("shareLabel").style.display="inline";
			document.getElementById("treegridbox").style.height="170px";
			document.getElementById("loadingImg").style.display="none";
			setHeader(isObjChecked);
		} 

		document.getElementById('coord').value= "--Select--";
		document.getElementById('protocolCoordinatorIds').options.length = 0;		
	}
} 
//Ajax Function for Delete Folders of TreeGrid 
function ajaxTagDeleteCall(tagId) {
var url = "TagDeleteAction.do";
var answer = confirm ("Are you sure you want to delete?")
if (answer)
 {

if (window.XMLHttpRequest)
  { 
  xmlHttpobj=new XMLHttpRequest();
  }
else
  { 
  xmlHttpobj=new ActiveXObject("Microsoft.XMLHTTP");
  }
	xmlHttpobj.open("POST", url, true);
	xmlHttpobj.setRequestHeader("Content-Type",
			"application/x-www-form-urlencoded");
	xmlHttpobj.onreadystatechange =doOnDelete ;
	xmlHttpobj.send("&tagId=" +tagId +"&entityTag=" +entityTag);
}}
function doOnDelete()
{  
   if (xmlHttpobj.readyState == 4) 
   {

	var selId = popupmygrid.getSelectedId();
	popupmygrid.deleteRow(selId);
       grid.deleteRow(selId);
   } 
}
//Ajax Function for Delete Queries in Folders  of TreeGrid 
function ajaxObjDeleteCall(tagItemId) {
var answer = confirm ("Are you sure you want to delete?")
if (answer)
 {
var url = "TagItemDeleteAction.do";
	if (window.XMLHttpRequest)
  { 
  xmlHttpobj=new XMLHttpRequest();
  }
else
  { 
  xmlHttpobj=new ActiveXObject("Microsoft.XMLHTTP");
  }
	xmlHttpobj.open("POST", url, true);
	xmlHttpobj.setRequestHeader("Content-Type",
			"application/x-www-form-urlencoded");
  
	xmlHttpobj.onreadystatechange =doOnObjDelete;
	
	xmlHttpobj.send("&tagItemId=" +tagItemId  +"&entityTagItem=" +entityTagItem);

 }}
function doOnObjDelete()
{
   if (xmlHttpobj.readyState == 4) 
   {
   	var selId = popupmygrid.getSelectedId();
       popupmygrid.deleteRow(selId);
   }
} 
var tagID;
//Ajax Function for Selection of rows in  TreeGrid Add Child By lazyLoad 

function ajaxTreeGridRowSelectCall(tagId) {
	var url = "GetTreeGridChildAction.do";
	if (window.XMLHttpRequest)
  { 
  xmlHttpobj=new XMLHttpRequest();
  }
else
  { 
  xmlHttpobj=new ActiveXObject("Microsoft.XMLHTTP");
  }
	xmlHttpobj.open("POST", url, true);
	tagID=tagId;	
	xmlHttpobj.setRequestHeader("Content-Type",
			"application/x-www-form-urlencoded");
	 
	xmlHttpobj.onreadystatechange =getJsonObj;
	xmlHttpobj.send("&tagId=" +tagId +"&entityTag=" +entityTag);
}
 //function for Adding Child by LazyLoad
 var childCount;
 var arrObj;
 function getJsonObj()
 {
	 if (xmlHttpobj.readyState == 4) 
	 {
		 var tree=xmlHttpobj.responseText;
		 arrObj = eval('(' +tree+ ')');	 
		 var objLength=arrObj.treeData.length;
		 var count;
		 
 		 childCount=popupmygrid.hasChildren(tagID);
	 if(childCount==0)
		{
		 childCount=arrObj.childCount;
 		for(count=0;count<objLength;count++)
 			{
 				var tagItemId=arrObj.treeData[count].id; 		
				if(arrObj.userId != arrObj.treeData[count].ownerId){
					popupmygrid.addRow((new Date()).valueOf(),[,arrObj.treeData[count].name,],0,tagID);
				}else{
					popupmygrid.addRow((new Date()).valueOf(),[,arrObj.treeData[count].name,"<img src='images/advQuery/delete.gif' width='12' height='12' id='tagItemId' onclick='ajaxObjDeleteCall("+tagItemId+")'/>"],(new Date()).valueOf(),tagID);
				}
				pause(10);
 			}
 		 	popupmygrid.openItem(tagID);
		}
	 }	 
}
 
 function pause(numberMillis) {
	var now = new Date();
	var exitTime = now.getTime() + numberMillis;
	while (true) {
	now = new Date();
	if (now.getTime() > exitTime)
	return;
	}
}
 
