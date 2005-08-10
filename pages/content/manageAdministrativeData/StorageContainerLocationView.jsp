<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>

<%
	String treeViewAction = Constants.TREE_VIEW_ACTION + "?" + Constants.PAGEOF + "=" + Constants.PAGEOF_STORAGE_LOCATION;
	
	//Get the storage type.
	String storageContainerType = (String)request.getAttribute(Constants.STORAGE_CONTAINER_TYPE);
	if (storageContainerType != null)
	{
		treeViewAction = Constants.TREE_VIEW_ACTION +
						 "?" + Constants.PAGEOF + "=" + Constants.PAGEOF_STORAGE_LOCATION +
						 "&"+ Constants.STORAGE_CONTAINER_TYPE + "=" + storageContainerType;
	}
%>


<iframe id="<%=Constants.APPLET_VIEW_FRAME%>" src="<%=treeViewAction%>" scrolling="no" frameborder="1" width="25%" height="100%">
	Your Browser doesn't support IFrames.
</iframe>
<iframe name="<%=Constants.DATA_VIEW_FRAME%>" scrolling="yes" frameborder="1" width="74%" height="100%">
	Your Browser doesn't support IFrames.
</iframe>