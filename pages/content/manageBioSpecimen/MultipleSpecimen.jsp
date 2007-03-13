<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/nlevelcombo.tld" prefix="ncombo"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.actionForm.MultipleSpecimenForm"%>

<html>
<head>
<script language="JavaScript" type="text/javascript"
	src="jss/javaScript.js"></script>
<script language="JavaScript" type="text/javascript"
	src="jss/script.js"></script>
	

<%MultipleSpecimenForm form = (MultipleSpecimenForm) request
					.getAttribute("multipleSpecimenForm");
			String action = Constants.MULTIPLE_SPECIMEN_APPLET_ACTION;
			String pageOf = form.getPageOf();
%>

<%if(pageOf != null && pageOf.equals(Constants.PAGE_OF_MULTIPLE_SPECIMEN_CP_QUERY))
	{
	String nodeId = "SpecimenCollectionGroup_";
	if(session.getAttribute("specimenCollectionGroupId") != null) {
		String scgId = (String) session.getAttribute("specimenCollectionGroupId");
		nodeId = nodeId + scgId;
	}
	%>

		<script language="javascript">
			refreshTree('<%=Constants.CP_AND_PARTICIPANT_VIEW%>','<%=Constants.CP_TREE_VIEW%>','<%=Constants.CP_SEARCH_CP_ID%>','<%=Constants.CP_SEARCH_PARTICIPANT_ID%>','<%=nodeId%>');								
		</script>
	<%}%>
<script>

	function activateApplet()
	{
		var w = window.self;
		w.focus=true;
		platform = navigator.platform.toLowerCase();
		if(platform.indexOf("Win32") != -1)
		{
			var x = document.getElementById("msa");
			x.focus= true;
			x.click();
		}
	}
</script>
</head>
<body>
<FORM METHOD=POST ACTION="<%=action%>">
<div id="errorMessages"></div>
</FORM>
			<table cellpadding="0" cellspacing="0" width="100%" height="100%" border="1">
				<tr width="100%" height="100%">
				<td width="100%" height="100%">
							<script language="JavaScript" type="text/javascript">
									platform = navigator.platform.toLowerCase();
									
									document.writeln('<APPLET\n' +
													'CODEBASE = "<%=Constants.APPLET_CODEBASE%>"\n'+
													'ARCHIVE = "CaTissueApplet.jar"\n'+
													'CODE = "edu/wustl/catissuecore/applet/ui/MultipleSpecimenApplet.class"\n'+
													'ALT = "Mulitple specimen Applet"\n'+
													'tabindex=0\n'+
													'NAME = "<%=Constants.MULTIPLE_SPECIMEN_APPLET_NAME%>"\n'+
													'id="msa"'
													);
									if (platform.indexOf("mac") != -1)
									{
										document.writeln('width="1000" height="550" MAYSCRIPT>');
									}
									else
									{
										document.writeln('width="100%" height="550" MAYSCRIPT>');
									} 
									var url = location.protocol + '//' + location.hostname + ':' + location.port + location.pathname + '/../';
	                                								
									document.writeln('<PARAM name="type" value="application/x-java-applet;jpi-version=1.3">\n' +
							                        '<PARAM name="name" value="<%=Constants.MULTIPLE_SPECIMEN_APPLET_NAME%>">\n'+
													'<PARAM name="session_id" value="<%=session.getId()%>">\n'+
													'<PARAM name="noOfSpecimen" value="<%=form.getNumberOfSpecimen()%>">\n'+
													'<PARAM name = "<%=Constants.APPLET_SERVER_URL_PARAM_NAME%>" value= "' + url + '">\n'+ 
													'</APPLET>'
												    );
													
												
							</script>
				</td>
				</tr>
		   	</table>
</body>
</html>