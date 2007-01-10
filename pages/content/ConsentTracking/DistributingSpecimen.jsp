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
				String barcodeStatus=(String)request.getAttribute("barcodeStatus"); 
			%>	

			<%		
				if(barcodeStatus.equalsIgnoreCase("invalid"))
				{
			%>		
					alert("Please enter Valid Barcode");
					window.close();
			<%		
				}
			%>
			</script>
			<script language="JavaScript">
			<%
			if(barcodeStatus.equalsIgnoreCase("valid"))
			{
			%>	
			
			
			<!-- This function will check if the verifyCheck box is checked or not -->			
			
				function submitAllResponses()
				{
					var checkboxInstance = document.getElementById('verifyAllCheckBox');
					if(checkboxInstance.checked)
					{
						var parentId=parent.opener.document.getElementById('<%=request.getParameter("barcodeId")%>');
						var theId = '<%=request.getParameter("verificationKey")%>';
						parentId.innerHTML="Complete"+"<input type='hidden' name='" + theId + "' value='Complete' id='" + theId + "'/>";
						window.close ();
					}
					else
					{
						window.close ();
					}
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
		String pageOf=(String)request.getParameter(Constants.PAGEOF);
		String operation = (String)request.getParameter(Constants.OPERATION);
		String signedConsentDate="";
				
	%>
			<html:form action="Distribution.do">
				<%@ include file="/pages/content/ConsentTracking/ConsentTracking.jsp" %> 
			</html:form>

	</body>
</html>

				<%
					}
				%>
			