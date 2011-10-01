<%!
// returns the url corresponding to the event selected
private String getEventAction(String event, String specimenId)
{
	String action = "";
	action = "DynamicEvent.do?operation=add&pageOf=pageOfDynamicEvent&eventName="+event;
	action = action + "&specimenId=" + specimenId;
	return action;
}

private String getEventAction(String event, String scgId, String scg)
{
	String action = "";
	action = "DynamicEvent.do?operation=add&pageOf=pageOfDynamicEvent&eventName="+event;
	action = action + "&scgId=" + scgId;
	return action;
}



%>