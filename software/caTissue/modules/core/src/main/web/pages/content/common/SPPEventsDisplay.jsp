<div style="overflow:no; height: 100%;float: none" align="left" title="sppForms" id ="sppForms" ></div>
<%
List<Map<String, Object>> sppEventDataList = (List<Map<String, Object>>) request.getAttribute("SPPEvents");
if(sppEventDataList!=null)
{
	for(Map<String, Object> sppDataList : sppEventDataList)
	{

		Object actionAppId = sppDataList.get(Constants.ID);
		Object formContextId = sppDataList.get("formContextId");
		String eventName = sppDataList.get(Constants.CONTAINER_IDENTIFIER).toString();
		String pageOfStr = sppDataList.get(Constants.PAGE_OF).toString();
		String caption = sppDataList.get("Caption").toString();
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

	var formIframe = document.createElement("iframe");
	formIframe.id = formName;
	formIframe.name= formName;
	formIframe.style.border ="0";
	formIframe.style.width ="100%";
	formIframe.style.height ="355px";
	formIframe.scrolling="auto";
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
			url = "SearchObject.do?pageOf=pageOfDynamicEvent&operation=search&hideSubmitButton=true&id="+actionAppId;
		}
		System.out.println("URL::: "+url);
	%>

	var iframeElement = document.getElementById(formName);
	formIframe.src = "<%=url%>";
</script>
<%
}
}
%>