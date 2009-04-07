<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List,edu.wustl.catissuecore.util.global.Constants,edu.wustl.catissuecore.util.global.AppUtility"%>

<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ page import="edu.wustl.catissuecore.util.global.Constants" %>
<%@ page import="edu.wustl.catissuecore.domain.Participant" %>
<%@ page import="edu.wustl.catissuecore.caties.util.CaTIESConstants"%>
<script type="text/javascript" src="jss/dhtmlwindow.js"></script>
<script type="text/javascript" src="jss/modal.js"></script>
<link href="css/catissue_suite.css" rel="stylesheet" type="text/css" />

<%
	String reportQueueId = (String)request.getParameter(Constants.REPORT_ID);
	String conflictStatus = (String)request.getParameter(Constants.CONFLICT_STATUS);
%>

<script language="JavaScript">
   
   //This function will be called to view the report
   	function reportPage()
	{
		var reportId=<%=reportQueueId%> 
		var url="ConflictReportAction.do?reportQueueId="+reportId;
		window.open(url,'ConflictSCGForm','height=330,width=800,scrollbars=1,resizable=1');
		//popupwindow=dhtmlmodal.open('consentDialog', 'iframe', url,'ConflictSCGForm',
			 //'width=700px,height=150px,center=1,resize=0,scrolling=1', 'recal');
	} 
</script>
      <table width="100%" border="0" cellpadding="3" cellspacing="0" class="whitetable_bg">
      
      <tr>
        <td width="1%" colspan="2" align="left" class="toptd"></td>
      </tr>
      <tr>
        <td colspan="2" align="left" class="tr_bg_blue1"><span class="blue_ar_b">&nbsp;<bean:message key="caTies.conflict.details.title"/></span></td>
      </tr>
      
      <tr>
        <td colspan="2" class="showhide"><table class="noneditable" width="99%" border="0" align="center" cellpadding="4" cellspacing="0">
            
            <tr>
              <td width="18%" ><strong><bean:message key="caTies.conflict.report.participant.name"/></strong></td>
              <td width="25%" ><bean:write name="conflictCommonForm" property="participantName"/></td>
              <td width="22%" ><strong><bean:message key="caTies.conflict.report.spn"/></strong></td>
              <td width="35%" ><bean:write name="conflictCommonForm" property="surgicalPathologyNumber"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<html:link href="#" styleClass="view" styleId="viewReport" onclick="reportPage();">
						<bean:message key="caTies.conflict.view.report"/></span>
						</html:link></td>
            </tr>
            <tr>
              <td nowrap="nowrap" ><strong><bean:message key="participant.birthDate"/></strong></td>
              <td ><bean:write name="conflictCommonForm" property="birthDate"/></td>
              <td ><strong><bean:message key="caTies.conflict.report.collection.date"/></strong></td>
              <td ><bean:write name="conflictCommonForm" property="reportCollectionDate"/></td>
            </tr>
            <tr>
              <td ><strong><bean:message key="participant.socialSecurityNumber"/><br />
              </strong></td>
              <td ><bean:write name="conflictCommonForm" property="socialSecurityNumber"/></td>
              <td ><strong><bean:message key="caTies.conflict.site"/></strong></td>
              <td ><bean:write name="conflictCommonForm" property="siteName"/></td>
            </tr>
            
        </table></td>
      </tr>
      <table width="100%" border="0" cellspacing="0" cellpadding="4">
          <tr>
            <td width="16%" class="tableheading"><span class="black_ar_b"><%if(conflictStatus.equals(CaTIESConstants.STATUS_PARTICIPANT_CONFLICT))
					  {
					%>
						<bean:message key="caTies.conflict.matching.participant"/>
					<%
					  }else
					  {
					%>	
						<bean:message key="caTies.conflict.matching.accession"/>
					<%}%></span></td>
            </tr>
        </table>      
    </table>