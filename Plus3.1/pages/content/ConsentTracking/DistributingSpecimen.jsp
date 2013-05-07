<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/nlevelcombo.tld" prefix="ncombo" %>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.actionForm.CollectionProtocolRegistrationForm"%>
<%@ page import="edu.wustl.catissuecore.util.global.AppUtility"%>
<%@ page import="edu.wustl.catissuecore.bean.ConsentBean"%>
<%@ page import="java.util.*"%>
<%@ include file="/pages/content/common/BioSpecimenCommonCode.jsp" %>

<!-- 
	 @author Virender Mehta 
	 @version 1.1	
	 Jsp name: DistributingSpecimen.jsp
	 Description: This jsp includes ConsentTracking.jsp and is associated with pageOf Distribution.
	 Company: Washington University, School of Medicine, St. Louis.
-->						

<html>
	<head>
		<title>caTissue Core v 1.1</title>
			<link rel="stylesheet" type="text/css" href="css/styleSheet.css" />
			<script src="jss/script.js" type="text/javascript"></script>
			<script src="jss/overlib_mini.js" type="text/javascript"></script>
			<script language="JavaScript">
			
			<%	
				String barcodeLabelStatus=(String)request.getAttribute("barcodeStatus"); 
				String normalSubmit="";
			%>	

			<%		
				if(barcodeLabelStatus.equalsIgnoreCase(Constants.INVALID))//"Invalid"
				{
			%>		
					alert("Please enter valid value");
					window.close();
			<%		
				}
			%>
			</script>
			<script language="JavaScript">
			<%
			if(barcodeLabelStatus.equalsIgnoreCase(Constants.VALID))//valid
			{
			%>	
			<!-- This function will check if the verifyCheck box is checked or not -->			
			
				function submitAllResponses()
				{
					var checkboxInstance = document.getElementById('verifyAllCheckBox');
					var parentId=parent.opener.document.getElementById('<%=request.getParameter("barcodelableId")%>');
					var theId = '<%=request.getParameter("verificationKey")%>';
					if(checkboxInstance.checked)
					{
						parentId.innerHTML="<%=Constants.VERIFIED%>"+"<input type='hidden' name='" + theId + "' value='Verified' id='" + theId + "'/>";
					}
					else
					{
						parentId.innerHTML="View"+"<input type='hidden' name='" + theId + "' value='View' id='" + theId + "'/>";
					}
					window.close ();
				}
				
				
			</script>
	</head>

	<body>
	<%-- Get Consents and get Participant response and Specimen Level response  --%>
	<%
		Object obj = request.getAttribute("distributionForm");
		DistributionForm form =null;

		if(obj != null && obj instanceof DistributionForm)
		{
			form = (DistributionForm)obj;
		}	
		String pageOf=(String)request.getParameter(Constants.PAGE_OF);
		String operation = (String)request.getParameter(Constants.OPERATION);
		String signedConsentDate="";
	%>
		<html:form action="Distribution.do">
			<%
				if(form.getConsentTierCounter()>0)
				{
		    %>
					<%@ include file="/pages/content/ConsentTracking/ConsentTracking.jsp" %> 
			<%
				}
				else			
				{
			%>
					<script language="JavaScript">
						alert("No Consents available");
					</script>
			<%
				}
			%>
		</html:form>
	</body>
</html>

				<%
					}
				%>
			