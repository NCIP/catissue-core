<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/nlevelcombo.tld" prefix="ncombo" %>
<%@ page language="java" isELIgnored="false" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>	
<LINK href="css/catissue_suite.css" type=text/css rel=stylesheet>
<script src="jss/javaScript.js" type="text/javascript"></script>
<script language="JavaScript">

function cancelWindow()
{
  parent.consentWindow.hide();
}
</script>							
	<%-- Main table Start --%>
	<table width="100%" border="0" cellpadding="3" cellspacing="0"   id="consentTabForSCG">
		<%--Title of the form i.e Consent Form --%>				
	
		<tr>
			<td align="left" class="tr_bg_blue1"><span class="blue_ar_b">
				<div style="margin-top:2px;">
					<bean:message key="collectionprotocolregistration.consentform"/>
				</div>
			</span>
		  </td>
		</tr>
			<tr>
				<td>

				<div id="" style="overflow:auto;"> <table width="100%" border="0" cellspacing="0"         cellpadding="4">
						<%-- Serial No # --%>	
						<tr class="tableheading">
							<td width="8%" align="left" class="black_ar_b"><bean:message key="requestlist.dataTabel.serialNo.label" /></td>
							<%-- Title ( Consent Tiers) --%>									
							<td width="17%" align="left" nowrap="nowrap" class="black_ar_b">
									<bean:message key="collectionprotocolregistration.consentTiers" />
							</td>
							<%--Title (Participant response) --%>										
							<td width="24%" align="left" nowrap="nowrap" class="black_ar_b">
									<bean:message key="collectionprotocolregistration.participantResponses" />
							</td>
							
							<%-- Title ( Response Status if page of SCG or New Specimen --%>									
							<td class="black_ar_b">
									<bean:message key="consent.responsestatus" />
							</td>
						</tr>
						<tr>
							<td class="black_ar">
								<input type="checkbox" name="verifyAllCheckBox" id="verifyAllCheckBox"/>
							</td>
							<td class="black_ar" colspan="3">
								<label><b><bean:message key="consent.verificationmessage" /><b></label>
							</td>
						</tr>
					   </table>	</div>
					<%-- Inner table that will show Consents--%>
				</td>	
			</tr>
			<tr>
			<td class="buttonbg" align="left">


						<input type="button" name="doneButton" class="blue_ar_b" value="Ok" onclick="submitAllResponses()"/>
			</td></tr>
	</table>
	<%-- Main table End --%>
