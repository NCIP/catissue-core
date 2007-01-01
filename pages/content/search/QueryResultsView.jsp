<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%
           boolean mac = false;
	        Object os = request.getHeader("user-agent");
			if(os!=null && os.toString().toLowerCase().indexOf("mac")!=-1)
			{
			    mac = true;
			}
	String height = "100%";		
	if(mac)
	{
	  height="300";
    }
%>
<table border="0" height="100%" width="100%">
	<tr height="100%">
		<td width="25%">
			<iframe id="<%=Constants.APPLET_VIEW_FRAME%>" src="<%=Constants.TREE_VIEW_ACTION%>?pageOf=pageOfQueryResults" scrolling="yes" frameborder="1" width="100%" height="<%=height%>">
				Your Browser doesn't support IFrames.
			</iframe>
		</td>
		<td width="74%">
			<iframe name="<%=Constants.DATA_VIEW_FRAME%>" src="<%=Constants.DATA_VIEW_ACTION+Constants.ROOT%>&<%=Constants.VIEW_TYPE%>=<%=Constants.SPREADSHEET_VIEW%>" scrolling="yes" frameborder="1" width="100%" height="<%=height%>">
				Your Browser doesn't support IFrames.
			</iframe>
		</td>
	</tr>
</table>			