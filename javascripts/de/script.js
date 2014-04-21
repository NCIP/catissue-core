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

function send(pageNum,numresultsPerPage,prevPage,pageName) 
{
	document.forms[0].action = pageName+'?pageNum='+pageNum;
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
			var str = element.options[element.selectedIndex].text;
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
					"&storageToBeSelected="+ document.forms[0].parentContainerId.value +
					"&position=" + document.forms[0].positionDimensionOne.value + 
					":" + document.forms[0].positionDimensionTwo.value;
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
function setSubmittedFor(submittedFor)
{
	document.forms[0].submittedFor.value = submittedFor;
}