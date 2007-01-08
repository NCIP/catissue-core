<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>

<%
      String access = (String)session.getAttribute("Access");
	  boolean mac = false;
      Object os = request.getHeader("user-agent");
      if(os!=null && os.toString().toLowerCase().indexOf("mac")!=-1)
      {
          mac = true;
      }
	
	String frame1Ysize = "100%";
	String frame2Ysize = "100%";
	String frame3Ysize = "100%";
	String cpAndParticipantViewFrameHeight="45%";
	if(access != null && access.equals("Denied"))
	{
		cpAndParticipantViewFrameHeight="15%";
	}
	
	if(mac)
	{
		frame1Ysize = "200";
		frame2Ysize = "200";
		frame3Ysize = "400";
		

		if(access != null && access.equals("Denied"))
		{
			frame1Ysize = "80";
			frame2Ysize = "320";
		}
	}
%>

<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
<table border="0" height="100%" width="100%" cellpadding="0" cellspacing="0">
	<tr height="100%">
		<td width="15%" valign="top">
			<table border="0" width="175" height="100%">
				<tr height="<%=cpAndParticipantViewFrameHeight%>">
					<td>				
					<iframe id="<%=Constants.CP_AND_PARTICIPANT_VIEW%>" name="<%=Constants.CP_AND_PARTICIPANT_VIEW%>" src="<%=Constants.SHOW_CP_AND_PARTICIPANTS_ACTION%>?pageOf=<%=Constants.PAGE_OF_CP_QUERY_RESULTS%>" scrolling="no" frameborder="0" width="100%" height="<%=frame1Ysize%>" marginheight=0 marginwidth=0>
						Your Browser doesn't support IFrames.
					</iframe>

					</td>
				</tr>	
				<tr>
					<td align="left" valign="top">		

					<iframe id="<%=Constants.CP_TREE_VIEW%>" name="<%=Constants.CP_TREE_VIEW%>" src="<%=Constants.CP_TREE_VIEW_ACTION%>" scrolling="no" frameborder="0" width="100%" height="<%=frame2Ysize%>" marginheight=0 marginwidth=0 valign="top">
						Your Browser doesn't support IFrames.
					</iframe>

					</td>
				</tr>	
			</table>	
		</td>
		<td width="85%" height="100%" valign="top">
			<iframe name="<%=Constants.DATA_DETAILS_VIEW%>" scrolling="yes" frameborder="1" width="100%" height="<%=frame3Ysize%>">
				Your Browser doesn't support IFrames.
			</iframe>

		</td>
	</tr>
</table>			

