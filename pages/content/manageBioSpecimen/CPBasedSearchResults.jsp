<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%
	String frame1Ysize = "200";
	String frame2Ysize = "200";
	String access = (String)session.getAttribute("Access");
	if(access != null && access.equals("Denied"))
	{
		frame1Ysize = "80";
		frame2Ysize = "320";
	}
%>
<table border="0" height="100%" width="100%" cellpadding="0" cellspacing="0">
	<tr height="100%">
		<td width="15%" valign="top">
			<table border="0" width="175">
				<tr height="45%">
					<td>				
					<iframe id="<%=Constants.CP_AND_PARTICIPANT_VIEW%>" src="<%=Constants.SHOW_CP_AND_PARTICIPANTS_ACTION%>?pageOf=<%=Constants.PAGE_OF_CP_QUERY_RESULTS%>" scrolling="no" frameborder="0" width="175" height="<%=frame1Ysize%>" marginheight=0 marginwidth=0>
						Your Browser doesn't support IFrames.
					</iframe>

					</td>
				</tr>	
				<tr height="55%">
					<td align="left" valign="top">		

					<iframe id="<%=Constants.CP_TREE_VIEW%>" src="<%=Constants.CP_TREE_VIEW_ACTION%>" scrolling="no" frameborder="0" width="175" height="<%=frame2Ysize%>" marginheight=0 marginwidth=0 valign="top">
						Your Browser doesn't support IFrames.
					</iframe>

					</td>
				</tr>	
			</table>	
		</td>
		<td width="85%" valign="top">
			<iframe name="<%=Constants.DATA_DETAILS_VIEW%>" scrolling="yes" frameborder="1" width="600" height="400">
				Your Browser doesn't support IFrames.
			</iframe>

		</td>
	</tr>
</table>			