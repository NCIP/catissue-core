<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<script src="jss/queryModule.js"></script>
<%
String formAction = Constants.DefineSearchResultsViewJSPAction;
           boolean mac = false;
	        Object os = request.getHeader("user-agent");
			if(os!=null && os.toString().toLowerCase().indexOf("mac")!=-1)
			{
			    mac = true;
			}
	String height = "480";		
	if(mac)
	{
	  height="480";
    }
%>

<table border="0" width="100%" cellspacing="0" cellpadding="0" height="<%=height%>" bordercolor="#000000" id="table1" >
<html:form method="GET" action="<%=formAction%>" style="margin:0;padding:0;">
		<tr>	
			<td width="33%" align="center" class="bgWizardImage">
				<img src="images/1_inactive.gif" /> <!-- width="118" height="25" /-->
			</td>
			<td width="33%" align="center" class="bgWizardImage">
				<img src="images/2_inactive.gif" /> <!-- width="199" height="38" /-->
			</td>
			<td width="33%" align="center" class="bgWizardImage">
				<img src="images/3_active.gif" /> <!--  width="139" height="38" /-->
			</td>
		</tr>
		<tr height="100%">
		<td colspan="4" height="100%">
<table border="0" height="100%" width="100%">
	<tr height="100%">
		<td width="25%" colspan="1" valign="top">
			<iframe id="<%=Constants.TREE_VIEW_FRAME%>" src="<%=Constants.QUERY_TREE_VIEW_ACTION%>?pageOf=pageOfQueryResults" scrolling="auto" frameborder="0" width="100%" height="100%">
				Your Browser doesn't support IFrames.
			</iframe>
		</td>
		<td width="75%" colspan="3" valign="top">
			<iframe name="<%=Constants.GRID_DATA_VIEW_FRAME%>" src="<%=Constants.QUERY_GRID_VIEW_ACTION%>?pageOf=pageOfQueryModule" <%=Constants.VIEW_TYPE%>=<%=Constants.SPREADSHEET_VIEW%>" scrolling="auto" frameborder="0" width="100%" height="100%">
				Your Browser doesn't support IFrames.
			</iframe>
		</td>
	</tr>
</table>
</td>
</tr>
<tr bgcolor="#DFE9F3" height="30" valign="top"> 
		<td colspan="4" valign="bottom" height="10%">
		<table border="0"  width="100%" cellspacing="0" cellpadding="0"  background="images/bot_bg_wiz.gif" height="24"  >
		<tr height="1%" valign="bottom">
		 <td width="2%" valign="bottom" >&nbsp;</td>
		 <td valign="bottom" align="left" width="90-%" >
		 <img src="images/b_save.gif" width="51"  hspace="3" vspace="3" onclick="saveClientQueryToServer('save')" />
	 </td>
   	 <td align="right" valign="bottom">
		 <img src="images/b_prev.gif" width="72"  hspace="3" vspace="3" onclick="defineSearchResultsView()"/>
	 </td>	
		 
		 <td width="2%">&nbsp;</td>
</tr>

</table>	

</td>
</tr>
</html:form>
</table>			
