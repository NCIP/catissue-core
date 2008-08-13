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
		frame3Ysize = "650";
	 

		if(access != null && access.equals("Denied"))
		{
			frame1Ysize = "80";
			frame2Ysize = "320";
		}
	}
%>

<script>
//Set the alope for the IFrame
if ( document.getElementById && !(document.all) ) 
{
	var slope=10;
}
else
{
	var slope=-10;
}

window.onload = function() { setFrameHeight('cpFrameNew', .9,slope);setFrameHeight('<%=Constants.CP_AND_PARTICIPANT_VIEW%>', .9,slope); }
window.onresize = function() { setFrameHeight('cpFrameNew', .9,slope); setFrameHeight('<%=Constants.CP_AND_PARTICIPANT_VIEW%>', .9,slope); }
</script>
<script language="JavaScript" type="text/javascript" src="jss/caTissueSuite.js"></script>
<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
<body >
<table border="0" width="100%" cellpadding="0" cellspacing="0">
	<tr >
		<td width="27%" valign="top">
			<table border="0" width="100%" >
				<tr>
					<td valign="top" >
					<iframe id="<%=Constants.CP_AND_PARTICIPANT_VIEW%>" name="<%=Constants.CP_AND_PARTICIPANT_VIEW%>" src="<%=Constants.SHOW_CP_AND_PARTICIPANTS_ACTION%>?pageOf=<%=Constants.PAGE_OF_CP_QUERY_RESULTS%>" scrolling="no" frameborder="0" width="100%" height="524" marginheight=0 marginwidth=0>
						Your Browser doesn't support IFrames.
					</iframe>

					</td>
				</tr>	
				
			</table>	
		</td>
		<!--P.G. - Start 24May07:Bug 4291:Added source as initial action for blank screen-->
		<td width="72.5%" valign="top">
			<iframe id="cpFrameNew" name="<%=Constants.DATA_DETAILS_VIEW%>" src="<%=Constants.BLANK_SCREEN_ACTION%>" scrolling="auto" frameborder="1" width="99.9%" >
				Your Browser doesn't support IFrames.
			</iframe>
		</td>
		<!--P.G. - End -->
	</tr>
</table>			
</body>
