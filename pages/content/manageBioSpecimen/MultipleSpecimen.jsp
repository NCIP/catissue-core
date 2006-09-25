<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/nlevelcombo.tld" prefix="ncombo"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.actionForm.MultipleSpecimenForm"%>


<head>
<script language="JavaScript" type="text/javascript"
	src="jss/javaScript.js"></script>

<%MultipleSpecimenForm form = (MultipleSpecimenForm) request
					.getAttribute("multipleSpecimenForm");
			String action = Constants.NEW_MULTIPLE_SPECIMEN_ACTION;
%>


<APPLET
    CODEBASE = "<%=Constants.APPLET_CODEBASE%>"
    ARCHIVE = "CaTissueApplet.jar"
    CODE = "edu/wustl/catissuecore/applet/ui/MultipleSpecimenApplet.class"
    ALT = "Mulitple specimen Applet"
    NAME = "<%=Constants.MULTIPLE_SPECIMEN_APPLET_NAME%>"
    width="650" height="500" MAYSCRIPT>
	<PARAM name="type" value="application/x-java-applet;jpi-version=1.4.2">
	<PARAM name="name" value="<%=Constants.MULTIPLE_SPECIMEN_APPLET_NAME%>">
	<PARAM name="session_id" value="<%=session.getId()%>">
	<PARAM name="noOfSpecimen" value="<%=form.getNumberOfSpecimen()%>"
</APPLET>
