//Mandar : 5Aug08 -----------------
var xmlHttpReq;
function initRequest() 
{
	if (window.XMLHttpRequest) 
	{
		xmlHttpReq = new XMLHttpRequest();
	}
	else if (window.ActiveXObject) 
	{
		xmlHttpReq = new ActiveXObject("Msxml2.XMLHTTP");
		if(!xmlHttpReq)
		{
			xmlHttpReq = new ActiveXObject("Microsoft.XMLHTTP");
		}
	}
}

function ajaxCall(reqType, url, bool, responseHandlerFn)
{
	initRequest();
	if(xmlHttpReq)
	{
		try
		{
			xmlHttpReq.onreadystatechange = responseHandlerFn; 
			xmlHttpReq.open(reqType, url, bool);
			xmlHttpReq.send(null);
		}
		catch(errVar)
		{
			alert("The application cannot contact the server at the moment. Please try again after some time.\n"+errVar.message);
		}
	}
	else
	{
		alert("Your browser does not permit the use of all of this applications features!");
	}
}

