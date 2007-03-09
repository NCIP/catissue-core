<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/nlevelcombo.tld" prefix="ncombo" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>

<%@ page import="java.util.*"%>
<%@ page import="edu.wustl.catissuecore.util.global.Variables"%>
<%@ page import="edu.wustl.catissuecore.actionForm.ViewSurgicalPathologyReportForm"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>

<script src="jss/ajax.js"></script>
<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
<LINK href="css/styleSheet.css" type=text/css rel=stylesheet>

<c:set var="pageOf" value="${viewSurgicalPathologyReportForm.pageOf}"/>
<jsp:useBean id="pageOf" type="java.lang.String"/>

	<%
		int noOfRows=0;
		Map map = null;
		String formName=null;
		ViewSurgicalPathologyReportForm viewSurgicalPathologyReportForm=null;
		ViewSurgicalPathologyReportForm formSPR=viewSurgicalPathologyReportForm;
		Object obj = request.getAttribute("viewSurgicalPathologyReportForm");
		if(obj != null && obj instanceof ViewSurgicalPathologyReportForm)
		{
			formSPR = (ViewSurgicalPathologyReportForm)obj;
			map = formSPR.getValues();
			noOfRows = formSPR.getCounter();
		}
%>
	<%@ include file="/pages/content/manageBioSpecimen/ViewSurgicalPathologyReport.jsp" %>  
	
	