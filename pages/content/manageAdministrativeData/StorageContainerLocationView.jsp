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


<iframe id="<%=Constants.APPLET_VIEW_FRAME%>" src="<%=treeViewAction%>" scrolling="no" frameborder="1" width="35%" height="100%">
	Your Browser doesn't support IFrames.
</iframe>
<iframe name="<%=Constants.DATA_VIEW_FRAME%>" scrolling="yes" frameborder="1" width="65%" height="100%">
	Your Browser doesn't support IFrames.
</iframe>