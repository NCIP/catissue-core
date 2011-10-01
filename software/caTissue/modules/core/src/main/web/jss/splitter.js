var ie = false;
var splitterOpenStatus = true;

if (document.all)
{ 
	ie = true;
}

//Check for browser
function getObj(id) 
{
	if (ie) 
	{ 
		return document.all[id]; 
	}
	else 
	{    
		return document.getElementById(id);    
	}
}

/*
 * This method should be called on initialization of the page. This initializes the menu status as per last set value. 
 * If the last state is true it shows the menu otherwise hides it. 
 */
function initSplitterOpenStatus(splitterOpenStatusLast) 
{
	splitterOpenStatus = splitterOpenStatusLast;

	if (splitterOpenStatus)
	{
		showMenu();
	} 
	else 
	{  
		hideMenu();		
	} 
}

/*
 * This method should be called on click on splitter. This toggles the state of menu status. 
 * If the last state is true it hides the menu otherwise shows it. 
 */
function toggleSplitterStatus()
{
	if (splitterOpenStatus)
	{
		hideMenu();
	} 
	else 
	{  
		showMenu();
	}

	//Send the current menu state to server using AJAX to retain the state on next pages.	
	updateSplitterStatusInSession();
}

/*
 * Hides the menu by setting display property as hidden of TD which holds menu.
 */	
function hideMenu()
{
	var arrow = getObj('splitter');
	var td = getObj('sideMenuTd');

	splitterOpenStatus = false;
	//setting the display style as 'none' for Internet explorer and Safari browsers.
	if((navigator.appCodeName=="Mozilla" && navigator.appName=="Netscape" && (navigator.userAgent.search(/Safari/)!=-1)) || (document.all))
	{
		td.style.display='none';
	}
	(document.getElementById("contentTd")).style.width="100%";
	arrow.innerHTML = '<img src="images/leftPane_expandButton.gif"/>';  
}

/*
 * Shows the menu by setting display property as show of TD which holds menu.
 */	
function showMenu()
{
	var arrow = getObj('splitter');
	var td = getObj('sideMenuTd');

	splitterOpenStatus = true;
	
	//UI adjustments
	td.style.height = '100%';
	(document.getElementById("cpAndParticipantView")).style.width="100%";
	(document.getElementById("contentTd")).style.width="72%";
	td.style.display = 'block';

	arrow.innerHTML = '<img src="images/leftPane_collapseButton.gif"/>';  
}

/***  code using ajax :Update the status of Menu open in session ***/
function updateSplitterStatusInSession()
{
	var request = newXMLHTTPReq();
	var handlerFunction = getReadyStateHandler(request,onResponse,true);
	
	//no brackets after the function name and no parameters are passed because we are assigning a reference to the function and not actually calling it
	request.onreadystatechange = handlerFunction;
	var param = "SPLITTER_STATUS="+splitterOpenStatus;
	
	//Open connection to servlet
	request.open("POST","UpdateSpillterStatus.do",true);	
	request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");	
	
	//send data to ActionServlet
	request.send(param);
}

function onResponse(str) 
{
	//DO nothing after receiving the response from server.
}