<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/nlevelcombo.tld" prefix="ncombo" %>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.actionForm.CollectionProtocolRegistrationForm"%>
<%@ page import="edu.wustl.catissuecore.util.global.Utility"%>
<%@ page import="edu.wustl.catissuecore.bean.ConsentBean"%>
<%@ page import="java.util.*"%>
<%@ include file="/pages/content/common/BioSpecimenCommonCode.jsp" %>

<script src="jss/script.js" type="text/javascript"></script>
<script src="jss/calendarComponent.js"></script>
<script src="jss/ajax.js"></script>
<SCRIPT>var imgsrc="images/";</SCRIPT>
<LINK href="css/calanderComponent.css" type=text/css rel=stylesheet>

	<%
		String normalSubmit="";
		String signedConsentDate="";
		String pageOf=(String)request.getAttribute(Constants.PAGEOF);
		String operation = (String) request.getAttribute(Constants.OPERATION);
		Object obj = request.getAttribute("collectionProtocolRegistrationForm");
		CollectionProtocolRegistrationForm form =null;
		if(obj != null && obj instanceof CollectionProtocolRegistrationForm)
		{
			form = (CollectionProtocolRegistrationForm)obj;
			signedConsentDate=form.getConsentDate();
			if(signedConsentDate == null)
			{
				signedConsentDate="";
			}
			
		}	
		
	%>
	<!--
		<html:form action="GetConsents.do">
	-->
			<%
				List responseList =(List)request.getAttribute("responseList");
				if(form.getConsentTierCounter()>0)
				{
			%>
				 <%@ include file="/pages/content/ConsentTracking/ConsentTracking.jsp" %>  
			<%
				}
			%>			
<!--
	</html:form>
-->
