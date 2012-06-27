//Functions for Popup
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


//Ajax Function for Initialize Grid and open Popup
var queryDeleteMsg=null;
var folderDeleteMsg=null;
var xmlHttpobj = false;
function ajaxPopupFunctionCall() {

var url = "TagAction.do";
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
//Function called onclick of foldar in Grid of "left" div  
function submitTagName(tagId) {
	document.forms['saveQueryForm'].action = "TagClickAction.do?tagId=" + tagId;
	document.forms['saveQueryForm'].submit();
}
//Ajax Function for Assign Query to folder (Assign Button on Popup) 
function ajaxAssignTagFunctionCall(url,msg,msg1,id) 
{
if (window.XMLHttpRequest)
  { 
  xmlHttpobj=new XMLHttpRequest();
  }
else
  { 
  xmlHttpobj=new ActiveXObject("Microsoft.XMLHTTP");
  }
	var chkBoxString = new String();
	var queryChkBoxString = new String();
	var i;
	
	url = url+'?id='+id;
	xmlHttpobj.open("POST", url, true);
	xmlHttpobj.setRequestHeader("Content-Type",
			"application/x-www-form-urlencoded");
	
	var chkBoxAr = document.getElementsByName("tagCheckbox");
	//var queryCheckBoxAr = document.getElementById("id");
	
	var tagName = document.getElementById('newTagName').value;
	document.getElementById('newTagName').value ='';


	for (i = 0; i < chkBoxAr.length; i++) {
		if (chkBoxAr[i].checked == true) {
			chkBoxString = chkBoxAr[i].value + "," + chkBoxString;
			chkBoxAr[i].checked = false;
		}

	}
	/*for (i = 0; i < queryCheckBoxAr.length; i++) {
		if (queryCheckBoxAr[i].checked == true) {
			queryChkBoxString = queryCheckBoxAr[i].value + ","
					+ queryChkBoxString;
			 
		}
	}*/
	queryChkBoxString = id;
	//alert(queryChkBoxString);
	if (queryChkBoxString=='')
	{
		alert(msg1);
		//alert('ffff');
		 
 	}
	else if(chkBoxString==''&&tagName=='')
 	{
		alert("Please select existing list or give the name for the new list");
		 //alert('ffff23');
	}
	else if((queryChkBoxString!=''&& chkBoxString!='')||(queryChkBoxString!=''&& tagName!=''))
 	{
		 xmlHttpobj.onreadystatechange =assignQueryFunction;
		/*for (i = 0; i < queryCheckBoxAr.length; i++) {
		if (queryCheckBoxAr[i].checked == true) {
			 queryCheckBoxAr[i].checked = false;
			}
		}*/
xmlHttpobj.send("&chkBoxString=" + chkBoxString + "&queryChkBoxString="
			+ queryChkBoxString + "&tagName=" + tagName);
	}
	else if((queryChkBoxString!=''&& chkBoxString!=''&& tagName!=''))
	{
		 xmlHttpobj.onreadystatechange =assignQueryFunction;
		/*for (i = 0; i < queryCheckBoxAr.length; i++) {
		if (queryCheckBoxAr[i].checked == true) {
			 queryCheckBoxAr[i].checked = false;
			}
		}*/
xmlHttpobj.send("&chkBoxString=" + chkBoxString + "&queryChkBoxString="
			+ queryChkBoxString + "&tagName=" + tagName);
	}

 	

}
function assignQueryFunction() {
		if (xmlHttpobj.readyState == 4) {
			//doInitGrid();	
			popup('popUpDiv');
			//doInItTreeGrid();		
}
}
//Ajax Function for Initialize TreeGrid
function ajaxTreeGridInitCall(msg,msg1) {
queryDeleteMsg=msg;
folderDeleteMsg=msg1;
var url = "TreeTagAction.do";
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
	 	xmlHttpobj.send(null);
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
	xmlHttpobj.send("&tagId=" +tagId );
}}
function doOnDelete()
{  
   if (xmlHttpobj.readyState == 4) 
	 {
 var selId = popupmygrid.getSelectedId()
        popupmygrid.deleteRow(selId);
//	grid.deleteRow(selId);
      	}
 
}
//Ajax Function for Delete Queries in Folders  of TreeGrid 
function ajaxObjDeleteCall(assignId) {
var answer = confirm (queryDeleteMsg)
if (answer)
 {
var url = "ObjDeleteAction.do";
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
	xmlHttpobj.send("&assignId=" +assignId +"&tagId="+tagID );

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
	xmlHttpobj.send("&tagId=" +tagId);
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
			var assignId=arrObj.treeData[count].id;
			 		
popupmygrid.addRow((new Date()).valueOf(),[,arrObj.treeData[count].name,"<img src='images/delete.gif' width='12' height='12' id='assignId' onclick='ajaxObjDeleteCall("+assignId+")'/>"],(new Date()).valueOf(),tagID);
	pause(10);
 			}
		}
 	
	 }
	 //alert('hhh');
popupmygrid.expandAll();	 
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
	xmlHttpobj.send("&tagId=" +tagId);
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
		 //alert(childCount);
	 if(childCount==0)
		{
		 childCount=arrObj.childCount;
 		for(count=0;count<objLength;count++)
 			{
			var assignId=arrObj.treeData[count].id;
			 		
popupmygrid.addRow((new Date()).valueOf(),[,arrObj.treeData[count].name,"<img src='images/delete.gif' width='12' height='12' id='assignId' onclick='ajaxObjDeleteCall('"+assignId+"','"+assignId+"')'/>"],0,tagID);
 	pause(10);			}
		}
//ajaxTagDeleteCall(tagID);	
}	
}