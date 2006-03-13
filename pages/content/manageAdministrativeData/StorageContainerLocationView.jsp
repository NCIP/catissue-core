<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>

<%
	String pageOf = (String)request.getAttribute(Constants.PAGEOF);
	String treeViewAction = Constants.TREE_VIEW_ACTION + "?" + Constants.PAGEOF + "=" + pageOf;
	
	//Get the storage type.
	String storageContainerType = (String)request.getAttribute(Constants.STORAGE_CONTAINER_TYPE);
	if (storageContainerType != null)
	{
		treeViewAction = Constants.TREE_VIEW_ACTION +
						 "?" + Constants.PAGEOF + "=" + pageOf +
						 "&"+ Constants.STORAGE_CONTAINER_TYPE + "=" + storageContainerType;
	}
	String storageContainerID = (String)request.getAttribute(Constants.STORAGE_CONTAINER_TO_BE_SELECTED);
	if(null != storageContainerID) // If container ID is specified in the Edit boxes
	{
        // Forward the request with container ID, position IDs
		String position = (String)request.getAttribute(Constants.STORAGE_CONTAINER_POSITION);
		treeViewAction += "&" + Constants.STORAGE_CONTAINER_TO_BE_SELECTED + "=" + storageContainerID +
						"&" + 	Constants.STORAGE_CONTAINER_POSITION + "=" + position;
	}
%>
<SPAN id="ifr1"> <IFRAME id="<%=Constants.APPLET_VIEW_FRAME%>"
	src="<%=treeViewAction%>" scrolling="no" frameborder="1" width="35%"
	height="100%"> Your Browser doesn't support IFrames. </IFRAME> </SPAN>
<SPAN id="ifr2"> <IFRAME name="<%=Constants.DATA_VIEW_FRAME%>"
	scrolling="yes" frameborder="1" width="65%" height="100%"> Your Browser
doesn't support IFrames. </IFRAME> </SPAN>
<!-- Code to display iframe on Mac  -->
<SCRIPT type="text/javascript">
var platformName = navigator.platform;
//alert(platformName + " : " + platformName.substring(0,3));
if(platformName.substring(0,3) == "Mac")
{
	var w1 = Math.round(.25 * screen.availWidth);
	var h1 = Math.round(.50 * screen.availHeight);

	var d1 = document.getElementById("ifr1");

	var str = "<iframe id='" + "<%=Constants.APPLET_VIEW_FRAME%>" + "' src='" + "<%=treeViewAction%>" + "' scrolling='no' frameborder='1' width='"+w1+"' height='"+h1+"'>";
	str = str + "Your Browser doesn't support IFrames.</iframe>";
	d1.innerHTML = str;

	var w2 = Math.round(.45 * screen.availWidth);
	var h2 = Math.round(.50 * screen.availHeight);

	var d2 = document.getElementById("ifr2");
	var str2 = 
	"<iframe name='" + "<%=Constants.DATA_VIEW_FRAME%>" + "' scrolling='yes' frameborder='1' width='" + w2 + "' height='"+h2+"'>Your Browser doesn't support IFrames.</iframe>";
	d2.innerHTML = str2;
}
</SCRIPT>



