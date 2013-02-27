<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>


<link rel="stylesheet" type="text/css" href="css/login.css" />
<link rel="stylesheet" type="text/css" href="css/login-theam.css" />
<br/>
<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="100%">
	<tr>
		<td>
			<table summary="" cellpadding="3" cellspacing="0" border="0">
				<div style=" text-align:center;width:100%;">
					<div class="black_ar box-border box-content box-background form-main-div">
						<div class="black_ar help-header theam-font-color form-header-spacing" >
							<p>
								<font color="#000000" size="2" face="Verdana">
								<%String statusMessageKey = (String)request.getAttribute(Constants.STATUS_MESSAGE_KEY);%>
									<strong><bean:message key="<%=statusMessageKey%>" /></strong>
								</font>
							</p>
						</div>
					</div>
				</div>
				
			 </table>
		</td>
	</tr>
</table>