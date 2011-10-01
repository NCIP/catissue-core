<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/nlevelcombo.tld" prefix="ncombo" %>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.actionForm.ConsentResponseForm"%>
<%@ page import="edu.wustl.catissuecore.util.global.AppUtility"%>
<%@ page import="edu.wustl.catissuecore.util.global.Variables"%>
<%@ page import="edu.wustl.common.util.global.CommonServiceLocator"%>

<%@ page import="java.util.*"%>
<%@ include file="/pages/content/common/BioSpecimenCommonCode.jsp" %>

<script src="jss/script.js" type="text/javascript"></script>
<script src="jss/calendarComponent.js"></script>
<script src="jss/ajax.js"></script>
<SCRIPT>var imgsrc="images/";</SCRIPT>
<LINK href="css/calanderComponent.css" type=text/css rel=stylesheet>
<LINK href="css/styleSheet.css" type=text/css rel=stylesheet>

	<%
		String normalSubmit="";
			String pageOf=(String)request.getAttribute(Constants.PAGE_OF);
			String operation = (String)request.getAttribute(Constants.OPERATION);
			List responseList =(List)request.getAttribute("responseList");
			List witnessList = (List)request.getAttribute("witnessList");
			String cpId = (String)request.getAttribute("cpId");
			String consentResponse = "CP_"+cpId;
			
			Object obj = request.getAttribute("consentForm");
			String signedConsentDate="";
			ConsentResponseForm form =null;
			long collectionProtocolID = -1;
			if(obj != null && obj instanceof ConsentResponseForm)
			{
		form = (ConsentResponseForm)obj;
		signedConsentDate = form.getConsentDate();
		if(signedConsentDate == null || signedConsentDate.trim().length()==0){
			signedConsentDate = edu.wustl.common.util.Utility.parseDateToString(Calendar.getInstance().getTime(), CommonServiceLocator.getInstance().getDatePattern());
		}
		collectionProtocolID = form.getCollectionProtocolID();
		
			}
			
			String formName = "ConsentSubmit.do";
	%>
	<script type="text/javascript">
		function submitConsentResponses()
		{
			<%
				ConsentTierData consentForm =(ConsentTierData)form;
				List consentTier=(List)consentForm.getConsentTiers();
				if(consentTier.size()>0)
				{ %>
					popupWindow('<%=consentTier.size()%>');
				<%
				}
			%>
		}
	</script>

	<html:form action="<%=formName%>">
		<input type="hidden" name="collectionProtocolID" value="<%=collectionProtocolID%>"/>
		<html:hidden property="withdrawlButtonStatus"/>
		<%@ include file="/pages/content/ConsentTracking/ConsentTracking.jsp" %>
		
	</html:form>