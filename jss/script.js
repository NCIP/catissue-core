	
function showHide(elementid)
{
	var switchImg = document.getElementById('imgArrow_'+elementid);
	if (document.getElementById(elementid).style.display == 'none'){
		document.getElementById(elementid).style.display = '';
		switchImg.innerHTML = '<img src="images/uIEnhancementImages/up_arrow.gif" alt="Hide Details" border="0" width="80" height="9" hspace="10" vspace="3 "/>';
							
	} else {
		document.getElementById(elementid).style.display = 'none';
		switchImg.innerHTML = '<img src="images/uIEnhancementImages/dwn_arrow1.gif" alt="Show Details" border="0" width="80" height="9" hspace="10" vspace="3"/>';
				
	}
} 

function onCoordinatorChange()
{
	var submittedForValue = document.forms[0].submittedFor.value;
	var action = "Site.do?"+"operation="+document.forms[0].operation.value+"&pageOf=pageOfSite&isOnChange=true&coordinatorId="+document.getElementById("coordinatorId").value+"&submittedFor="+submittedForValue;
	document.forms[0].action = action;
	document.forms[0].submit();
}

function enableLastCheckbox()
{
	var lastRowNo = document.forms[0].counter.value;
	var chkBox = document.getElementById("chk_"+lastRowNo);
	if(lastRowNo>1)
		chkBox.disabled = false;
}

function enablePreviousCheckBox(element)
{
   	var elementName = element.name;
   	var index = elementName.indexOf('_');
   	var previousRowNo = parseInt(elementName.substring(index+1))-1;
    if (element.checked == true)
    {
      	if(previousRowNo > 1)
      	{
			var previousElement = document.getElementById("chk_"+previousRowNo);
			previousElement.disabled = false;     
		}
    }
    else if(element.checked == false)
    {
      	for(var i=previousRowNo;i>=1;i--)
      	{
			var previousElement = document.getElementById("chk_"+i);
			previousElement.checked = false;
			previousElement.disabled = true;     
		}
	}
}


function changeMenuStyle(obj, new_style) { 
if (objclick != obj)
  obj.className = new_style; 
}

function showCursor(){
	document.body.style.cursor='hand';
}

function hideCursor(){
	document.body.style.cursor='default';
}

function waitCursor(){
	document.body.style.cursor='wait';
}

function confirmDelete(){
  if (confirm('Are you sure you want to delete?'))
  {
    return true;
  }
  else
  {
    return false;
  }
}

function setFormAction(formAction)
{
	document.forms[0].action=formAction;
}

function setOperation(operation)
{
	document.forms[0].operation.value=operation;
}

function enableAll()
{
	document.forms[0].loginName.disabled="false";
}

function CheckAll(chk)
{
	for (var i=0;i < document.forms[0].elements.length;i++)
    {
    	var e = document.forms[0].elements[i];
        if (e.type == "checkbox")
        {
        	e.checked = chk.checked;
        }
    }
}

function showdatecontrol(strFormname,strElementName,strElementName2) 
{
	var calWnd=window.open("cal.asp?WCI=cal&caltype=0&formname="+strFormname+
						   "&elementname="+strElementName+"&elementname2="+
						   strElementName2,"strFormname","toolbar=no,location=no,directories=no,status=yes,width="+ 
						   screen.availWidth/4.2 +",height="+ screen.availHeight/2.8+ 
						   ",menubar=no,scrollbars=no,top=0,left=0, resizable=no, border=thin");
}

function send(pageNum,numResultsPerPage,prevPage,pageName) 
{
	document.forms[0].action = pageName+'?pageNum='+pageNum+'&numResultsPerPage='+numResultsPerPage;
	document.forms[0].submit();
}

function changeRecordPerPage(pageNum,element,pageName) 
{
	document.forms[0].action = pageName+'?pageNum=1&numResultsPerPage='+element.value;
	document.forms[0].submit();
}

// function for mouse click
var objclick;
function changeMenuSelected(obj, new_style) { 
	
	
	objclick = obj;
	window.location.reload
  obj.className = new_style; 
  obj.onmouseout="";
}


function changeUrl(element,str)
{
			var redirectTo1 = "redirectTo=" + str;
			
			var redirectTo = replaceAll("&","_",redirectTo1);
			var url = element.href + "&" + redirectTo;
			//alert(redirectTo);
			//alert(url);
			element.href = url;
}		

		function replaceAll(oldStr,newStr,fromStr)
		{
			var i = fromStr.indexOf(oldStr);
			while(i != -1)
			{
				var s = fromStr.replace(oldStr,newStr);
				fromStr = s;
				i = fromStr.indexOf(oldStr);
			}
			return fromStr;
		}

		function getURL(str)
		{
			var ind = str.lastIndexOf("/");
			var s = str.substring(ind);
			return s;
		}
		
		function checkActivityStatus(element,url)
		{
		  var str;
		  if (element.type!=null && element.type == "text") {
		      str = element.value;
		  } 
		   else
		   {
		       str = element.options[element.selectedIndex].text;
	       }
			if(str == "Disabled")
			{
				document.forms[0].onSubmit.value=url;
			}
		}
		
	
		function changeSubmitTo(url)
		{
			var str = document.forms[0].onSubmit.value;
	//		alert(str);
			if(str == null || str == "")
			{
				document.forms[0].onSubmit.value=url;
			}
		}

		
		var win = null;
		function StorageMapWindow(mypage,myname,w,h,scroll)
		{
			LeftPosition = (screen.width) ? (screen.width-w)/2 : 0;
			TopPosition = (screen.height) ? (screen.height-h)/2 : 0;

			// Sri: Added position one and two as parameters
            // with format positionOne:positionTwo
			mypage=mypage+document.forms[0].typeId.value + 
					"&storageToBeSelected="+ document.forms[0].customListBox_1_0.value +  //parentContainerId.value +
					"&position=" + document.forms[0].customListBox_1_1.value +   //positionDimensionOne.value + 
					":" + document.forms[0].customListBox_1_2.value;
					
		   var storageContainer = document.getElementById('selectedContainerName').value;
		   mypage+="&storageContainerName="+storageContainer;		
					// alert(mypage);
			settings =
				'height='+h+',width='+w+',top='+TopPosition+',left='+LeftPosition+',scrollbars='+scroll+',resizable'
			win = open(mypage,myname,settings)
			if (win.opener == null)
				win.opener = self;
		}

		function NewWindow(mypage,myname,w,h,scroll)
		{
			LeftPosition = (screen.width) ? (screen.width-w)/2 : 0;
			TopPosition = (screen.height) ? (screen.height-h)/2 : 0;

			settings =
				'height='+h+',width='+w+',top='+TopPosition+',left='+LeftPosition+',scrollbars='+scroll+',resizable'
			win = open(mypage,myname,settings)
			if (win.opener == null)
				win.opener = self;
		}

//This function submits JSP page to AddNewAction with passed action
function addNewAction(action)
{
	var action = action;
	
	document.forms[0].action = action;

	document.forms[0].submit();
}

//This function sets value of submittedFor attribute of JSP form object
function setSubmittedFor(submittedFor,forwardTo)
{
	document.forms[0].submittedFor.value = submittedFor;
	document.forms[0].forwardTo.value    = forwardTo;
}
//Falguni
//This function sets value of submittedFor Print attribute of JSP form object
function setSubmittedForPrint(submittedFor,forwardTo,nextForwardTo)
{
	
	document.forms[0].submittedFor.value = submittedFor;
	document.forms[0].forwardTo.value    = forwardTo;
	document.forms[0].nextForwardTo.value   = nextForwardTo;
	
	
}
function setSubmittedForAddToMyList(forwardTo,forward,nextForwardTo)
{ 
	document.forms[0].submittedFor.value = forwardTo;
	document.forms[0].forwardTo.value    = forward;
	document.forms[0].nextForwardTo.value   = nextForwardTo;
}

//this function is used in Specimen and Events page for non functional tabs 
function featureNotSupported()
{
	alert("This feature is not supported in version 1.2 and will be supported in the next release of caTissue.");
}

function validateAny(element)
{

	var len = element.length;
	var anySelected=false;
	if(len>0 && element[0].selected)
	{
		anySelected=true;

	}
	for (var i = 1; i < len; i++) 
	{

		if (element[i].selected && anySelected==true) 
		{
			return false;
		}
	}
	return true;
}
// Name : Prafull_kadam
// Bug ID: 3624
// Patch ID: 3624_2
// Description: Added code to handle hyperlinks String sorting in dhtml grid.

// TO check whether the given String is Hyperlink or not.
function isHyperLink(str)
{
	 var reg = new RegExp("^<[aA][ ].*</[aA]>$");
	 return reg.test(str);
}
// TO get the Lable of the Hyperlink for the given hyperlink string, returns same string if its not hyperlink.
function getHyperlinkLable(link)
{
	if (isHyperLink(link)==true)
	{
		var index = link.indexOf('>');
		var lastIndex = link.lastIndexOf('</');
		link = link.substring(index+1, lastIndex);
	}
	return link;
}

// methods of simple query interface
function onObjectChange(element,action)
{
	var index = element.name.indexOf("(");
	var lastIndex = element.name.lastIndexOf(")");
	
	var saveObject = document.getElementById("objectChanged");	
	saveObject.value = element.name.substring(index+1,lastIndex);
	
	callSerachAction(action);
}

function setPropertyValue(propertyName, value)
{
	for (var i=0;i < document.forms[0].elements.length;i++)
    {
    	var e = document.forms[0].elements[i];
        if (e.name == propertyName)
        {
        	document.forms[0].elements[i].value = value;
		}
    }
}

function callSerachAction(action)
{
	document.forms[0].action = action;
	document.forms[0].submit();
}

function incrementCounter()
{
	document.forms[0].counter.value = parseInt(document.forms[0].counter.value) + 1;
}
function decrementCounter()
{
	document.forms[0].counter.value = parseInt(document.forms[0].counter.value) - 1;
} 

function elementHt(eleid,ht)
{
	//alert(ht);
		var ele = document.getElementById(eleid);
		if(ele)
		{
			var newHt=0;
			if(document.all && document.documentElement && document.documentElement.clientHeight )
				newHt=(document.documentElement.clientHeight*ht/100);
			else if(window.innerHeight)
				newHt=(window.innerHeight*ht/100);
			else if(document.body && document.body.clientHeight)
				newHt=(document.body.clientHeight*ht/100);
			ele.height=newHt;
	//		alert(newHt);
		}
}


function macRelated()
{
	if(navigator.userAgent.indexOf('Mac') >=0)
	{
		//alert(window.height);
		var frms = document.frames;
		for(i=0;i<frms.length; i++)
		{
			frms[i].height=window.innerHeight*.70;
		}
	}
}

function macEleHt(eleid,ht)
{
	if(navigator.userAgent.indexOf('Mac') >=0)
	{
//		alert((window.innerHeight*ht/100));
		var ele = document.getElementById(eleid);
		ele.height=(window.innerHeight*ht/100);
	}
}

function mdFrmResizer(frmId,pcnt)
{
	if(pcnt >100 || pcnt <= 0)
	{
		pcnt=35;
	}
//	alert(screen.width + "*" + screen.height);
	var ht = screen.height;
	var frmHt = Math.round(ht * pcnt/100);
	//alert(frmHt);
	if((screen.height > 1000) && (pcnt >50))frmHt=frmHt+75;
	if(frmHt < 220)frmHt=270;
	if(document.getElementById(frmId) != null)
	{ 
		document.getElementById(frmId).height=frmHt+" px"	;
	}
}
