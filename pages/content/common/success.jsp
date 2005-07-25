<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>

<br/>
<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="600">
	<tr>
		<td>
			<table summary="" cellpadding="3" cellspacing="0" border="0">
				<p>
					<font color="#000000" size="2" face="Verdana">
					<%String statusMessageKey = (String)request.getAttribute(Constants.STATUS_MESSAGE_KEY);%>
						<strong><bean:message key="<%=statusMessageKey%>" /></strong>
					</font>
				</p>
			 </table>
		</td>
	</tr>
</table>