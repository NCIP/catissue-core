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
	
	
	String containerName = request.getParameter(Constants.STORAGE_CONTAINER);
	if(containerName!=null) 
	{
	    treeViewAction += "&" + Constants.STORAGE_CONTAINER + "=" + containerName;
	}

	        boolean mac = false;
	        Object os = request.getHeader("user-agent");
			if(os!=null && os.toString().toLowerCase().indexOf("mac")!=-1)
			{
			    mac = true;
			}
	
	String height = "100%";
	if(mac)
   {	
        height = "320";
   }
%>

<table border="0" height="100%" width="100%">
	<tr height="100%">
		<td width="25%">
			<iframe id="<%=Constants.APPLET_VIEW_FRAME%>" src="<%=treeViewAction%>" scrolling="yes" frameborder="1" width="100%" height="<%=height%>">
				Your Browser doesn't support IFrames.
			</iframe>
		</td>
		<td width="74%">
			<iframe name="<%=Constants.DATA_VIEW_FRAME%>" scrolling="yes" frameborder="1" width="100%" height="<%=height%>">
				Your Browser doesn't support IFrames.
			</iframe>
		</td>
	</tr>
</table>

