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
<body>
<!-- Mandar : 25Nov08  -->
		<TABLE border=1 width="100%" height="100%" cellpadding="0" cellspacing="0">
		<TR>
			<TD width="30%" valign="top">
				<iframe id="<%=Constants.CP_AND_PARTICIPANT_VIEW%>" name="<%=Constants.CP_AND_PARTICIPANT_VIEW%>" src="<%=Constants.SHOW_CP_AND_PARTICIPANTS_ACTION%>?pageOf=<%=Constants.PAGE_OF_CP_QUERY_RESULTS%>" scrolling="no" frameborder="0" width="100%" height="100%">
						Your Browser doesn't support IFrames.
				</iframe>

			</td><td width="70%" valign="top">
				<iframe id="cpFrameNew" name="<%=Constants.DATA_DETAILS_VIEW%>" src="<%=Constants.BLANK_SCREEN_ACTION%>" scrolling="auto" frameborder="0" width="100%" height="100%">
				Your Browser doesn't support IFrames.
			</iframe>
			</TD>
		</TR>
		</TABLE>
</body>