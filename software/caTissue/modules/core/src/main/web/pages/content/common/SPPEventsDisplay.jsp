<div style="overflow:no; height: 100%;float: none" align="left" title="sopForms" id ="sopForms" ></div>
<%
List<Map<String, Object>> sopEventDataList = (List<Map<String, Object>>) request.getAttribute("SOPEvents");
if(sopEventDataList!=null)
{
	for(Map<String, Object> sopDataList : sopEventDataList)
	{

		Object actionAppId = sopDataList.get(Constants.ID);
		Object formContextId = sopDataList.get("formContextId");
		String eventName = sopDataList.get(Constants.CONTAINER_IDENTIFIER).toString();
		String pageOfStr = sopDataList.get(Constants.PAGE_OF).toString();
		String caption = sopDataList.get("Caption").toString();
		String eventDate = null;
		if(sopDataList.get(Constants.EVENT_DATE)!=null)
		{
			eventDate = sopDataList.get(Constants.EVENT_DATE).toString();
		}
%>
<script>
	var sopEventDiv = document.getElementById('sopForms');
	var formName = "<%=formContextId%>";

	var formContextIdElement = document.createElement("input");
	formContextIdElement.id = "formContextId";
	formContextIdElement.type= "hidden";
	formContextIdElement.name ="formContextId";
	formContextIdElement.value=<%=formContextId%>;
	sopEventDiv.appendChild(formContextIdElement);

	var formIframe = document.createElement("iframe");
	formIframe.id = formName;
	formIframe.name= formName;
	formIframe.style.border ="0";
	formIframe.style.width ="100%";
	formIframe.style.height ="355px";
	formIframe.scrolling="auto";
	sopEventDiv.appendChild(formIframe);
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