<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<table border="0" height="100%" width="100%">
	<tr height="100%">
		<td width="25%">
			<table border="0" height="100%" width="100%">
				<tr height="45%">
					<td>				
					<iframe id="<%=Constants.CP_AND_PARTICIPANT_VIEW%>" src="<%=Constants.SHOW_CP_AND_PARTICIPANTS_ACTION%>?pageOf=<%=Constants.PAGE_OF_CP_QUERY_RESULTS%>" scrolling="no" frameborder="0" width="185" height="200" marginheight=0 marginwidth=0>
						Your Browser doesn't support IFrames.
					</iframe>

					</td>
				</tr>	
				<tr height="55%">
					<td align="left" valign="top">		

					<iframe id="<%=Constants.CP_TREE_VIEW%>" src="<%=Constants.CP_TREE_VIEW_ACTION%>" scrolling="no" frameborder="0" width="185" height="200" marginheight=0 marginwidth=0>
						Your Browser doesn't support IFrames.
					</iframe>

					</td>
				</tr>	
			</table>	
		</td>
		<td width="75%">
			<iframe name="<%=Constants.DATA_DETAILS_VIEW%>" scrolling="yes" frameborder="1" width="600" height="410%">
				Your Browser doesn't support IFrames.
			</iframe>

		</td>
	</tr>
</table>			