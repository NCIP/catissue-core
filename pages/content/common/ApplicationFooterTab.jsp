<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="edu.wustl.catissuecore.util.global.Constants" %>
<%
	String content = (String)request.getAttribute("CONTENTS");
	String pageName = (String)request.getAttribute("PAGE_TITLE");
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
            			<%=content%>
            		</font>
            	</td>
         	</tr>
           </table>
		 </td>
	 </tr>
</table>