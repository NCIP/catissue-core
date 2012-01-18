<script type="text/javascript" src="jss/ext-base.js"></script>
<script type="text/javascript" src="jss/ext-all.js"></script>
	<table width="100%" height="100%" border="0" cellpadding="3" cellspacing="0" class="whitetable_bg">
		<tr>
			<td class="bottomtd" colspan="3" align="left" >
			</td>
		</tr>
		<tr>
			<td colspan="3" align="left" >
				<%@ include file="/pages/content/common/ActionErrors.jsp" %>
			</td>
		</tr>
		<%
		if(Boolean.parseBoolean((String) request.getAttribute("showSPPHeader")) && !Boolean.parseBoolean((String) request.getAttribute("showSPPDropdown")))
		{
		%>
		<tr class="tr_bg_blue1">
			<td align="left" class="tr_bg_blue1">
				<span class="blue_ar_b">&nbsp;SPP Name&nbsp;&nbsp;</span>
				<span class="blue_ar_b" style="font-weight: normal">&nbsp;&nbsp;&nbsp;${requestScope.nameOfSelectedSpp}</span>
			</td>
		</tr>
		<%
		}
		if(Boolean.parseBoolean((String) request.getAttribute("showSPPDropdown")))
		{
		%>
			<tr>
				<td align="left" class="black_ar">Associated SPPs &nbsp;&nbsp;&nbsp;
					<html:select property="sppName" styleClass="formFieldSized15" styleId="className" size="1" disabled="false" onchange="onParameterChange(this)" onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
						<html:options collection="sppNameList" property="value" labelProperty="name" />
					</html:select>
				</td>
				<td align="left" valign="top">&nbsp;</td>
			</tr>
		<%}%>
		<tr height="100%">
			<td height="100%">
				<table width="100%" height="80" style="overflow:no;" border="0" cellpadding="3" cellspacing="0" class="whitetable_bg">
					<tr>
						<td>
							<div style="overflow:no; height: 100%;float: none" align="left" title="sppForms" id ="sppForms" ></div>
						</td>
					</tr>
					<tr height="10%">
						<td class="buttonbg">
							<html:submit styleId="SPPEventSubmitBtn" styleClass="blue_ar_b" value="Submit" onclick="submitSPPEvents();"/>
						</td>
					</tr>
					<tr>
						<td id="blankTd" height="2">&nbsp;
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
	<script>
		function getiframeheight()
		{
			var theHeight;
			if (window.innerHeight)
			{
			   theHeight = window.innerHeight;
			}
			else if (document.documentElement && document.documentElement.clientHeight)
			{
				theHeight = document.documentElement.clientHeight;
			}
			else if (document.body)
			{
				theHeight = document.body.clientHeight;
			}
			return theHeight;
		}

		var globalListSize=0;
		function calculatePageHtForNonIEBrowser(listsize)
		{
			globalListSize = globalListSize +1;
			if(globalListSize == listsize)
			{
				calculatePageHeight();
			}
		}

		function calculatePageHeightForIE()
		{
			calculatePageHeight();
		}

		function calculatePageHeight()
		{
			var sppEventDiv = document.getElementById('sppForms');
			var iframeList = document.getElementsByTagName('iframe');
			var allFrameHeights = 0;
			for(j =0;j<iframeList.length;j++)
			{
				var oDoc = iframeList[j].contentWindow || iframeList[j].contentDocument;
				if (oDoc.document)
				{
					oDoc = oDoc.document;
				}
				var innerIframe = oDoc.getElementsByTagName('iframe')[0].contentWindow;
				if (innerIframe.document)
				{
					innerIframe = innerIframe.document;
				}
				var inputCollection = innerIframe.getElementsByTagName('input');
				var decontrols =0;
				for(i=0; i<inputCollection.length ;i++)
				{
					var search = 'Control';
					if(inputCollection[i].name.indexOf(search) == 0)
					{
						decontrols=  decontrols +1;
					}
				}
				var divHeight = decontrols * 35;
				var frmHeight = (decontrols * 40)+20;
				if(innerIframe.getElementById('dataEntryFormDiv')!=null)
				{
					oDoc.getElementsByTagName('iframe')[0].height=frmHeight+'px';
					//innerIframe.getElementById('dataEntryFormDiv').style.height = divHeight+'px';
				}
				//alert(divHeight);
				var outerfrmHt = frmHeight+150;
				//alert(oDoc.body.scrollHeight);
				//alert(parent.document.getElementById('cpFrameNew').contentWindow.document.body.scrollHeight);
				iframeList[j].style.height= outerfrmHt+'px';
				//Store height of an iframe in hidden field
				var showHideFormId = "showHideFormHt_" + iframeList[j].id;
				if(document.getElementById(showHideFormId) != null)
				{
					document.getElementById(showHideFormId).value = outerfrmHt;
				}
				
				var rowId = "showHideCurrentForm_"+iframeList[j].id;
				if(oDoc.getElementById("eventPerformed") != null && !oDoc.getElementById("eventPerformed").checked && oDoc.getElementById(rowId)!= null)
				{
					iframeList[j].style.height = "35px"
					allFrameHeights = allFrameHeights + 35;
					//oDoc.getElementById(rowId).style.display = "none";
				}
				else
				{
					allFrameHeights = allFrameHeights+ outerfrmHt;
				}
			}
			if(allFrameHeights != 0)
			{
				sppEventDiv.style.height=allFrameHeights+"px";
			}
		}
		
		document.getElementById('sppForms').style.height = getiframeheight();
	</script>


	<%
List<Map<String, Object>> sppEventDataList = (List<Map<String, Object>>) request.getAttribute("SPPEvents");
if(sppEventDataList!=null && !sppEventDataList.isEmpty())
{
	int listSize = sppEventDataList.size();
	String height = 100/listSize+"%";
	int count = 0;
	for(Map<String, Object> sppDataList : sppEventDataList)
	{
		++count;
		Object actionAppId = sppDataList.get(Constants.ID);
		Object formContextId = sppDataList.get("formContextId");
		String eventName = sppDataList.get(Constants.CONTAINER_IDENTIFIER).toString();
		String pageOfStr = sppDataList.get(Constants.PAGE_OF).toString();
		String caption = sppDataList.get("Caption").toString();
		boolean isSPPDataEntryDone = (Boolean)sppDataList.get(Constants.IS_SPP_DATA_ENTRY_DONE);
		String eventDate = null;
		if(sppDataList.get(Constants.EVENT_DATE)!=null)
		{
			eventDate = sppDataList.get(Constants.EVENT_DATE).toString();
		}
%>
<script>
	var sppEventDiv = document.getElementById('sppForms');
	var formName = "<%=formContextId%>";

	var formContextIdElement = document.createElement("input");
	formContextIdElement.id = "formContextId";
	formContextIdElement.type= "hidden";
	formContextIdElement.name ="formContextId";
	formContextIdElement.value=<%=formContextId%>;
	sppEventDiv.appendChild(formContextIdElement);
	
	var showHideFormHeight = document.createElement("input");
	showHideFormHeight.id = "showHideFormHt_<%=formContextId%>";
	showHideFormHeight.type= "hidden";
	showHideFormHeight.name ="showHideFormHt_<%=formContextId%>";
	sppEventDiv.appendChild(showHideFormHeight);

	var formIframe = document.createElement("iframe");
	formIframe.id = formName;
	formIframe.name= formName;
	formIframe.style.border ="0px";
	formIframe.frameBorder="0";
	formIframe.style.width ="100%";
	formIframe.style.height ="<%=height%>";
	formIframe.scrolling="no";
	sppEventDiv.appendChild(formIframe);
	<%
		String url;
		if("0".equals(actionAppId.toString()))
		{
			String eventURL = getEventAction(caption, specimenIdentifier);
			url = eventURL+"&hideSubmitButton=true&showDefaultValues=true&formContextId="+formContextId;
		}
		else
		{
			url = "SearchObject.do?pageOf=pageOfDynamicEvent&operation=search&hideSubmitButton=true&id="+actionAppId+"&formContextId="+formContextId;
		}

		url = url + "&allowToSkipEvents=true&isSPPDataEntryDone="+isSPPDataEntryDone+"&displayEventsWithDefaultValues="+request.getAttribute("displayEventsWithDefaultValues");
		
		if(request.getAttribute(Globals.ERROR_KEY)!=null)
		{
			url = url + "&containsErrors=true";
		}
	%>

	var iframeElement = document.getElementById(formName);
	formIframe.src = "<%=url%>";

	if(Ext.isIE)
	{
		<%
			if(listSize < 3)
			{
		%>
				document.getElementById('blankTd').style.height = "20%";
		<%
			}
			if(count == listSize)
			{
		%>
				iframeElement.onload = function(){
					calculatePageHeight();
				}
		<%
			}
		%>
	}
	else
	{
		//alert("NON IE browser"+globalListSize);
		iframeElement.onload = function(){
			calculatePageHtForNonIEBrowser(<%=listSize%>);
		}
	}
</script>
<%
}
}
else
{
%>
<script>
	document.getElementById('SPPEventSubmitBtn').disabled="disabled";
</script>
<%
}
%>
