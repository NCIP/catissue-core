//<!-- JavaScript for ViewSurigicalPathologyReport.jsp  start -->
	
//<!-- function collapse or expand the participant information table   -->
function switchStyle(action)
{
	imageObj = document.getElementById('image');	
	if(action== 'hide') //Clicked on - image
	{
		hide('paricipantInformation');				
		imageObj.innerHTML = '<img src="images/nolines_plus.gif" border="0" /> ';
		imageObj.href="javascript:switchStyle('show');";
	}
	else  							   //Clicked on + image
	{
		show('paricipantInformation');
		imageObj.innerHTML = '<img src="images/nolines_minus.gif" border="0" /> ';
		imageObj.href="javascript:switchStyle('hide');";
	}
}
//<!--function to show object on UI -->
function show(obj)
{
	switchObj=document.getElementById(obj);
	switchObj.style.display = '';
}
//<!--function to hide object from UI -->
function hide(obj)
{
	switchObj=document.getElementById(obj);
	switchObj.style.display='none'
}
//<!--function to display deidentified report table on UI -->
function clickOnLinkShowDeidReport()
{
	hide('identifiedReportInfo');
	hide('participantTable');
	show('deidReportInfo');
}
//<!--function to display default view  -->
function clickOnLinkReport()
{
	hide('deidReportInfo');
	show('reportTable');
	show('categoryHighlighter');
	show('identifiedReportInfo');
	show('participantTable');
	switchStyle('show');
}
//<!--function to display identified and de-identified report compare view  -->
function clickOnLinkCompareReport()
{
	hide('participantTable');
	show('reportTable');
	show('identifiedReportInfo');
	show('deidReportInfo');
	show('categoryHighlighter');
}
//<!--function to display user requests table  -->
function clickOnLinkMyRequests()
{
	
}
//<!--function to display dialog box for confirmation of submitting comments-->
function confirmSubmit()
{
	if (confirm('Are you sure you want to Submit Comments?'))
  {
    return true;
  }
  else
  {
    return false;
  }
}
//<!--function to submit rview comments-->
function submitReviewComments(consentTierCounter)
{
	
	if(confirmSubmit())
	{
		document.forms[0].submittedFor.value='review';
		document.forms[0].forwardTo.value='success';
		var action="SurgicalPathologyReportEventParam.do?operation=add&consentTierCounter="+consentTierCounter;
		document.forms[0].action=action;
		document.forms[0].submit();
	}
}
//<!--function to submit quarantine comments-->
function submitQuarantineComments(consentTierCounter)
{
	if(confirmSubmit())
	{
		document.forms[0].submittedFor.value='quarantine';
		document.forms[0].forwardTo.value='success';
		var action="SurgicalPathologyReportEventParam.do?operation=add&consentTierCounter="+consentTierCounter;
		document.forms[0].action=action;
		document.forms[0].submit();
	}
}

function finishReview()
{
	if(confirmSubmit())
	{
		document.forms[0].submittedFor.value='review';
		document.forms[0].forwardTo.value='success';
		var action="SPRAdminComment.do?operation=edit&requestFor=review&onSubmit=review"
		document.forms[0].action=action;
		document.forms[0].submit();
	}
}

function submitAcceptComments()
{
	if(confirmSubmit())
	{
		document.forms[0].submittedFor.value='quarantine';
		document.forms[0].forwardTo.value='success';
		var action="SPRAdminComment.do?operation=edit&requestFor=ACCEPT&onSubmit=quarantine"
		document.forms[0].acceptReject.value=1;
		document.forms[0].action=action;
		document.forms[0].submit();
	}
}
//<!--function to submit quarantine comments-->
function submitRejectComments()
{
	if(confirmSubmit())
	{
		document.forms[0].submittedFor.value='quarantine';
		document.forms[0].forwardTo.value='success';
		var action="SPRAdminComment.do?operation=edit&requestFor=REJECT&onSubmit=quarantine"
		document.forms[0].acceptReject.value=2;
		document.forms[0].action=action;
		document.forms[0].submit();
		
	}
}
var spanSize=70;
function replaceString(startOff,innerHtml,subStr,text)
{
	var tempStr=innerHtml.substring(0,startOff);
	var tempArray=tempStr.split("<span");
	var spanCount=tempArray.length;
	var total=startOff;
	if(spanCount>1)
	{
		total=startOff+((spanCount-1)*spanSize);
	}

	var textBeforeString = innerHtml.substring(0,total);
	var toReplace=innerHtml.substring(total);
	var textAfterString = toReplace.replace(subStr,text);
	return (textBeforeString.concat(textAfterString));
}
function getNewLineCount(str)
{
	var count=0;
	var tempStr=str.split("\r");
	count=tempStr.length;
	tempStr=str.split("&gt");
	if(tempStr.length>1)
	{
		count+=(tempStr.length-1)*3;
	}
	tempStr=str.split("&lt");
	if(tempStr.length>1)
	{
		count+=(tempStr.length-1)*3;
	}
	return count;
}

//Added by Ashish
function selectByOffset(checkbox,start,end,colour,conceptName)
{
	if(checkbox.checked==true)
	{
		var innerHtml = document.getElementById("deidentifiedReportText").innerHTML;
		var tempStr = ReplaceTags(innerHtml);

		var startArr = start.split(",");
		var endArr = end.split(",");
		var conceptNameArr = conceptName.split(",");
		var newtext = "";
		var count = 0;
		for(var x=0;x<startArr.length;x++)
		{		
			var startOff=startArr[x]-1;
			var endOff=endArr[x]-1;
			
			var newLineCount=getNewLineCount(tempStr.substring(0,startOff));
			newLineCount=getNewLineCount(tempStr.substring(0,startOff+newLineCount));
			newLineCount=getNewLineCount(tempStr.substring(0,startOff+newLineCount));
			startOff=startOff+newLineCount-1;
			endOff=endOff+newLineCount-1;
			
			var subStr = tempStr.substring(startOff,endOff);
			var textBeforeString = tempStr.substring(0,startOff);
			var textAfterString = tempStr.substring(endOff);
			if(trimString(subStr)!="")
			{
				if(checkbox.checked==false)
				{
					//background color is set to default 'light-gray' color. Refer stylesheet.jss
					colour='#F4F4F5';
					//conceptName="";
				}
				var text = "<span title="+conceptNameArr[x]+" style='background-color:"+colour+"'>"+subStr+"</span>";
					
				if(count == 0)
				{
					innerHtml = replaceString(startOff,innerHtml,subStr,text);
					count++;
				}
				else
				{
					innerHtml = replaceString(startOff,innerHtml,subStr,text)
				}	
			}
			//newtext = 	textBeforeString + text + textAfterString;
		}
		document.getElementById("deidentifiedReportText").innerHTML=innerHtml;
	}
	
}
function trimString(sInString) {
  sInString = sInString.replace( /^\s+/g, "" );// strip leading
  return sInString.replace( /\s+$/g, "" );// strip trailing
}

var regExp = /<\/?[^>]+>/gi;
function ReplaceTags(xStr)
{
  xStr = xStr.replace(regExp,"");
  return xStr;
}

//>>>>>>>>>>>>>>>>>>>>>>>>>> AJAX code start
	var colours=new Array();
	var conceptName=new Array();
	var startOff=new Array();
	var endOff=new Array();
	// function to send request to server	
	var request;
	function sendRequestForReportInfo()
	{
		request = newXMLHTTPReq();			
		var actionURL;
		var handlerFunction = getReadyStateHandler(request,setReport,true);	
		request.onreadystatechange = handlerFunction;				
		actionURL = "reportId="+document.getElementById('reportId').value;			
		var url = "FetchReport.do";
		<!-- Open connection to servlet -->
		request.open("POST",url,true);	
		request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");	
		request.send(actionURL);	
			
	}
//To set the values of form that are fetched using AJAX call
	function setReport()
	{
		if(request.readyState==4 && request.status == 200)
		{
			/* Response contains required output.
			 * Get the response from server.
			 */				
			var responseString = request.responseText;
			
			if(responseString != null && responseString != "")
			{	
				var xmlDocument = getDocumentElementForXML(responseString); 
				
				var surgicalPathologyNumber ="";
						
				if(xmlDocument.getElementsByTagName('SurgicalPathologyNumber')[0].firstChild!=null)
				{
				  surgicalPathologyNumber = xmlDocument.getElementsByTagName('SurgicalPathologyNumber')[0].firstChild.nodeValue;	
				}
				var reportSite = xmlDocument.getElementsByTagName('IdentifiedReportSite')[0].firstChild.nodeValue;			
				var identifierReportText = xmlDocument.getElementsByTagName('IdentifiedReportTextContent')[0].firstChild.nodeValue;
				var deIdentifierReportText;
				if(xmlDocument.getElementsByTagName('DeIdentifiedReportTextContent')[0].firstChild!=null)
				{
					deIdentifierReportText = xmlDocument.getElementsByTagName('DeIdentifiedReportTextContent')[0].firstChild.nodeValue;
				}
				
				document.getElementById("surgicalPathologyNumber").innerHTML = surgicalPathologyNumber;
				document.getElementById("identifiedReportSite").innerHTML = reportSite;
				document.getElementById("identifiedReportText").innerHTML = "<PRE class='pre'>"+identifierReportText+"</PRE>";
				document.getElementById("deidentifiedReportText").innerHTML = "<PRE class='pre'>"+deIdentifierReportText+"</PRE>";
				document.getElementById("deidText").innerHTML = "<PRE>"+deIdentifierReportText+"</PRE>";
				
				conceptName=new Array();
				startOff=new Array();
				endOff=new Array();
				var spanText="";
				
				var table = document.getElementById('classificationNames');
				if(table!=null)
				{
					if(document.getElementById('classificationNamesRow')!=null)
					{
						table.deleteRow(0);
					}
				}
				var row =document.getElementById('classificationNames').insertRow(0);
				row.setAttribute('id', 'classificationNamesRow');
				for(i=0;i<xmlDocument.getElementsByTagName('ConceptBean').length;i++)
				{
					var conceptBeans = xmlDocument.getElementsByTagName('ConceptBean')[i];
					var classificationName = conceptBeans.getElementsByTagName('ClassificationName')[0].firstChild.nodeValue;
					conceptName[i] = conceptBeans.getElementsByTagName('ConceptName')[0].firstChild.nodeValue;
					startOff[i] = conceptBeans.getElementsByTagName('StartOff')[0].firstChild.nodeValue;
					endOff[i] = conceptBeans.getElementsByTagName('EndOff')[0].firstChild.nodeValue;
					
					spanText="";
	//				spanText += "<td class=\"formRequiredLabelWithAllBorder\">";
					spanText += "<input type=\"checkbox\" id=select"+i+" onclick=\"checkBoxClicked()\" />";
					spanText += "<span id=\"classificationName\" style="+"background-color:"+colours[i]+">";
					spanText += classificationName + "</span>";	
	//				spanText += "</td>";
					var cell = document.createElement("TD");
					var inputTag=document.createElement('input');
					
					cell.className="formRequiredLabelWithAllBorder";
					cell.innerHTML=spanText;
					row.appendChild(cell);
				}
			}
		}
	}
	
	function getDocumentElementForXML(xmlString)
	{
	    var document = null;
	    if (window.ActiveXObject) // code for IE
	    {
	                document = new ActiveXObject("Microsoft.XMLDOM");
	                document.async="false";
	                document.loadXML(xmlString);
	    }
	    else // code for Mozilla, Firefox, Opera, etc.
	    {
	                var parser = new DOMParser();
	                document = parser.parseFromString(xmlString,"text/xml");
	    }           
		return document;
	}	
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>AJAX code end

//Function to auto scroll deidentified report text's scroll bar while moving identified report's scrollbar
function scrollInSync()
{
	ideReportDiv=document.getElementById('identifiedReportText');
	deidReportDiv=document.getElementById('deidentifiedReportText');
	deidReportDiv.scrollTop=ideReportDiv.scrollTop+45;
}
// Function to set intial UI based on Access right
function setUI()
{
	hide('identifiedReportInfo');
	hide('participantTable');
	show('deidReportInfo');
}

//This function is called when action button is pressed to resolve the conflict:
function onButtonClick(buttonPressed, reportQueueId)
{
	var actionUrl = "ConflictResolver.do?reportQueueId="+reportQueueId+"&conflictButton="+buttonPressed;
	document.forms[0].action = actionUrl;
	document.forms[0].submit();
	
}

