<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%
      String access = (String)session.getAttribute("Access");
	  boolean mac = false;
      Object os = request.getHeader("user-agent");
      if(os!=null && os.toString().toLowerCase().indexOf("mac")!=-1)
      {
          mac = true;
      }
	
	String frame1Ysize = "99.9%";
	String frame2Ysize = "99.9%";
	String frame3Ysize = "99.9%";
	
	String cpAndParticipantViewFrameHeight="50%";
	if(access != null && access.equals("Denied"))
	{
		cpAndParticipantViewFrameHeight="15%";
	}
	
	if(mac)
	{
		frame1Ysize = "180";
		frame2Ysize = "180";
		frame3Ysize = "550";
	 

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
		<td width="25%" valign="top">
			<table border="0" width="275" height="100%">
				<tr height="<%=cpAndParticipantViewFrameHeight%>">
					<td valign="top" width="25%">
					<iframe id="<%=Constants.CP_AND_PARTICIPANT_VIEW%>" name="<%=Constants.CP_AND_PARTICIPANT_VIEW%>" src="<%=Constants.SHOW_CP_AND_PARTICIPANTS_ACTION%>?pageOf=<%=Constants.PAGE_OF_CP_QUERY_RESULTS%>" scrolling="no" frameborder="0" width="100%" height="<%=frame3Ysize%>" marginheight=0 marginwidth=0>
						Your Browser doesn't support IFrames.
					</iframe>

					</td>
				</tr>	
				
			</table>	
		</td>
		<!--P.G. - Start 24May07:Bug 4291:Added source as initial action for blank screen-->
		<td width="75%" height="100%" valign="top">
			<iframe name="<%=Constants.DATA_DETAILS_VIEW%>" src="<%=Constants.BLANK_SCREEN_ACTION%>" scrolling="auto" frameborder="1" width="99.9%" height="<%=frame3Ysize%>">
				Your Browser doesn't support IFrames.
			</iframe>
		</td>
		<!--P.G. - End -->
	</tr>
</table>			

