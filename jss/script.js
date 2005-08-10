function changeMenuStyle(obj, new_style) { 
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

