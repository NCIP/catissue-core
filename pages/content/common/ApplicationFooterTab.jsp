<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="edu.wustl.catissuecore.util.global.Constants" %>
<%@ page import="edu.wustl.common.util.global.CommonFileReader" %>
<%@ page import="edu.wustl.common.util.logger.Logger" %>
<%
	String tempFileName = (String)request.getParameter("requestFor");
	Logger.out.debug("tempFileName "+tempFileName);
	String key = "";
	String pageName = "";
	if(tempFileName != null)
	{
		if(tempFileName.equals("ContactUs"))
		{
			key = "app.contactUs.file";
			pageName = "app.contactUs";
		}
		
		else if(tempFileName.equals("PrivacyNotice"))
		{
			key = "app.privacyNotice.file";
			pageName = "app.privacyNotice";
		}
		
		else if(tempFileName.equals("Disclaimer"))
		{
			key = "app.disclaimer.file";
			pageName = "app.disclaimer";
		}
		
		else if(tempFileName.equals("Accessibility"))
		{
			key = "app.accessibility.file";
			pageName = "app.accessibility";
		}
		   
	}
	CommonFileReader reader = new CommonFileReader();
	String fileName = reader.getFileName(key);
	String content = reader.readData(fileName);
	Logger.out.debug("content "+content);
	
	
%>
		<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="800">
			
		   	 <tr>
			    <td>
			 	 <table summary="" cellpadding="3" cellspacing="0" border="0">
			 	   	<tr>
			 	   		<td>&nbsp;&nbsp;</td>
			 	   	</tr> 
			 	   	 
				 	<tr>
				     	<td class="formTitle" height="20" colspan="3">
				     		<bean:message key="<%=pageName%>"/>
				     	</td>
				 	</tr>
				 
				 	<tr>
				 		<td>&nbsp;&nbsp;</td>
				 	</tr>
				 	
				 	<tr> 
		            	<td colspan="3" class="formMessage">
		            		<font color="#000000" size="2" face="Verdana">
		            			<%=content %>
		            		</font>
		            	</td>
                 	</tr>
                   </table>
				 </td>
			 </tr>
		</table>
				 	
			

