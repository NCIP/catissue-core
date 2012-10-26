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
	ajaxTreeGridRowSelectCall(rId); 
}
var popupmygrid;
function doInItTreeGrid()
{
	popupmygrid = new dhtmlXGridObject('treegridbox');
	popupmygrid.selMultiRows = true;
	popupmygrid.imgURL = "dhtmlx_suite/dhtml_pop/imgs/";
	popupmygrid.setHeader(",<div style='text-align:center;'></div>,");
	//popupmygrid.setNoHeader(true);
	popupmygrid.setInitWidths("25,*,40");
	popupmygrid.setColAlign("left,left,left");
	popupmygrid.setColTypes("txt,tree,txt");
	popupmygrid.setColSorting("str,str,str");
	popupmygrid.attachEvent("onRowSelect", doOnTreeGridRowSelected);
	popupmygrid.setEditable(false);
	popupmygrid.init();
	popupmygrid.setSkin("dhx_skyblue");
	doInitParseTree();
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

//function called for AllQueries,SharedQueries,MyQueries
function submitTheForm(theNewAction,btn) {
	var theForm = parent.document.forms['saveQueryForm']
	theForm.action = theNewAction;
	theForm.submit();
}
//Function called on click of folder in Grid of "left" div  
function submitTagName(tagId) {
	document.forms['saveQueryForm'].action = "TagClickAction.do?tagId=" + tagId;
	document.forms['saveQueryForm'].submit();
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
		for (i = 0; i < objCheckBoxAr.length; i++) {
		if (objCheckBoxAr[i].checked == true) {
			 objCheckBoxAr[i].checked = false;
			}
		}
xmlHttpobj.send("&tagChkBoxString=" + tagChkBoxString + "&objChkBoxString="
			+ objChkBoxString + "&tagName=" + tagName +"&entityTag="+ entityTag+"&entityTagItem="+ entityTagItem);
	}
	else if((queryChkBoxString!=''&& tagChkBoxString!=''&& tagName!=''))
	{
		 xmlHttpobj.onreadystatechange =assignTagItemFunction;
		for (i = 0; i < objCheckBoxAr.length; i++) {
		if (objCheckBoxAr[i].checked == true) {
			 objCheckBoxAr[i].checked = false;
			}
		}
xmlHttpobj.send("&tagChkBoxString=" + tagChkBoxString + "&objChkBoxString="
			+ objChkBoxString + "&tagName=" + tagName +"&entityTag="+ entityTag+"&entityTagItem="+ entityTagItem);
	}

 	

}
function assignTagItemFunction() {
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
	}
} 
//Ajax Function for Delete Folders of TreeGrid 
function ajaxTagDeleteCall(tagId,msg) {
 var childCount=popupmygrid.hasChildren(tagId);
folderDeleteMsg = folderDeleteMsg.replace('{0}',1);
folderDeleteMsg = folderDeleteMsg.replace(/\d+/g,childCount); 
var url = "TagDeleteAction.do";
var answer = confirm (folderDeleteMsg)
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
 var selId = popupmygrid.getSelectedId()
        popupmygrid.deleteRow(selId);
	grid.deleteRow(selId);
      	}
 
}
//Ajax Function for Delete Queries in Folders  of TreeGrid 
function ajaxObjDeleteCall(tagItemId) {
var answer = confirm (queryDeleteMsg)
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
   var selId = popupmygrid.getSelectedId()
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
			 		
popupmygrid.addRow((new Date()).valueOf(),[,arrObj.treeData[count].name,"<img src='images/advQuery/delete.gif' width='12' height='12' id='tagItemId' onclick='ajaxObjDeleteCall("+tagItemId+")'/>"],(new Date()).valueOf(),tagID);
	pause(10);
 			}
		}
 	
	 }	 
}
//Ajax function to get Count of Queries Inside the Folder 
function ajaxDeleteCall(tagId) {
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
	xmlHttpobj.onreadystatechange =getJsonDeleteObj;
	xmlHttpobj.send("&tagId=" +tagId+"&entityTag=" +entityTag);
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
 function getJsonDeleteObj()
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
			 		
popupmygrid.addRow((new Date()).valueOf(),[,arrObj.treeData[count].name,"<img src='images/advQuery/delete.gif' width='12' height='12' id='tagItemId' onclick='ajaxObjDeleteCall('"+tagItemId+"','"+popupFolderDeleteMessage+"')'/>"],0,tagID);
 	pause(10);			}
		}
ajaxTagDeleteCall(tagID);	}	
}
