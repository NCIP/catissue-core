<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>

<iframe id="<%=Constants.APPLET_VIEW_FRAME%>" src="<%=Constants.TREE_VIEW_ACTION%>?pageOf=pageOfQueryResults" scrolling="no" frameborder="1" width="25%" height="100%">
	Your Browser doesn't support IFrames.
</iframe>
<iframe name="<%=Constants.DATA_VIEW_FRAME%>" src="<%=Constants.DATA_VIEW_ACTION+Constants.ROOT%>&<%=Constants.VIEW_TYPE%>=<%=Constants.SPREADSHEET_VIEW%>" scrolling="yes" frameborder="1" width="74%" height="100%">
	Your Browser doesn't support IFrames.
</iframe>