<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>

<%
	String pageOf = (String)request.getAttribute(Constants.PAGE_OF);
	String treeViewAction = Constants.TREE_VIEW_ACTION + "?" + Constants.PAGE_OF + "=" + pageOf;
	String treeNodeDataAction = Constants.STORAGE_CONTAINER_TREE_ACTION + "?" + Constants.PAGE_OF + "=" + pageOf;
	//Get the storage type.
	String storageContainerType = (String)request.getAttribute(Constants.STORAGE_CONTAINER_TYPE);
	if (storageContainerType != null)
	{
		treeViewAction = Constants.TREE_VIEW_ACTION +
						 "?" + Constants.PAGE_OF + "=" + pageOf +
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
	
	String height = "99%";
	if(mac)
   	{	
        // Patch ID: Bug#3090_12
        // Description: The height value is increased to eleminate the empty space that
        //				appears below the tree applet. 
        height = "580";
   	}
%>
<script language="JavaScript" type="text/javascript" src="jss/caTissueSuite.js"></script>
<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
<script>
//Set the alope for the IFrame
if ( document.getElementById && !(document.all) ) 
{
	var slope=0;
}
else
{
	var slope=-10;
}

window.onload = function() { setFrameHeight('<%=Constants.DATA_VIEW_FRAME%>', 1.0,slope); setFrameHeight('<%=Constants.APPLET_VIEW_FRAME%>', 1.0,slope);}
window.onresize = function() { setFrameHeight('<%=Constants.DATA_VIEW_FRAME%>', 1.0,slope); setFrameHeight('<%=Constants.APPLET_VIEW_FRAME%>', 1.0,slope); }

</script>
<table border="0"  width="100%">
	<tr>
		<td width="30%">
			<!-- 
				Patch ID: Bug#3090_13
				Description: The scrolling is set to value 'no' inorder to disable the outer scrollbar.
			-->
			<!--  changed by Pallavi Mistry -->
			<!-- treeNodeDataAction  added for DHTMLX tree view-->
			<iframe id="<%=Constants.APPLET_VIEW_FRAME%>" src="<%=treeNodeDataAction%>" scrolling="no" frameborder="1" width="99%" >
				Your Browser doesn't support IFrames.
			</iframe>
		</td>
		<td width="70%">
		<!--P.G. - Start 24May07:Bug 4291:Added source as initial action for blank screen-->
			<iframe id="<%=Constants.DATA_VIEW_FRAME%>"name="<%=Constants.DATA_VIEW_FRAME%>" src="<%=Constants.BLANK_SCREEN_ACTION%>" scrolling="auto" frameborder="1" width="99%" >
				Your Browser doesn't support IFrames.
			</iframe>
		<!--P.G. - End -->
		</td>
	</tr>
</table>
