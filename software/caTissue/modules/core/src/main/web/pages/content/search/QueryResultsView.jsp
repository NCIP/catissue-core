<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%
           boolean mac = false;
	        Object os = request.getHeader("user-agent");
			if(os!=null && os.toString().toLowerCase().indexOf("mac")!=-1)
			{
			    mac = true;
			}
	String height = "99%";		
	if(mac)
	{
	  // Patch ID: Bug#3090_26
	  // Description: height value is increased and hardcoded for the Safari browser.
	  height="580";
    }
%>
<table border="0" height="100%" width="100%">
	<tr height="100%">
		<td width="30%">
			<!-- 
				Patch ID: Bug#3090_27
				Description: The scrolling is set to value 'no' inorder to disable the outer scrollbar.
			-->
			<iframe id="<%=Constants.APPLET_VIEW_FRAME%>" src="<%=Constants.TREE_VIEW_ACTION%>?pageOf=pageOfQueryResults" scrolling="no" frameborder="1" width="99%" height="<%=height%>">
				Your Browser doesn't support IFrames.
			</iframe>
		</td>
		<td width="70%">
			<iframe name="<%=Constants.DATA_VIEW_FRAME%>" src="<%=Constants.DATA_VIEW_ACTION+Constants.ROOT%>&<%=Constants.VIEW_TYPE%>=<%=Constants.SPREADSHEET_VIEW%>" scrolling="auto" frameborder="1" width="99%" height="<%=height%>">
				Your Browser doesn't support IFrames.
			</iframe>
		</td>
	</tr>
</table>