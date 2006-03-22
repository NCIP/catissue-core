<%@ page import="edu.wustl.common.util.global.ApplicationProperties"%>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td width="283" height="37" align="left">
          	<a href="http://cabig.nci.nih.gov/">
          		<img class=logo alt="Cancer Biomedical Informatics Center" src="images/logotype.jpg" width="725" height="45" border="0">
			</a>
		  </td>
		  <td height="37" align="right">
		  <% 
			String url = ApplicationProperties.getValue("institution.url");
			String name = ApplicationProperties.getValue("institution.name");
%>
          	<a href='<%=url%>'>
          		<img class=logo alt='<%=name%>' src="images/InstitutionLogo.gif" width="259" height="45" border="0">
			</a>
		  </td>
</table>