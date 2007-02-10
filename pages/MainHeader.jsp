<%@ page import="edu.wustl.common.util.XMLPropertyHandler"%>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
    	<td align="left">
        	<a href="http://cabig.nci.nih.gov/">
          		<img class=logo alt="Cancer Biomedical Informatics Center" src="images/logotype.jpg" width="525" height="45" border="0">
			</a>
		</td>
		<td align="right">
			<% 
				String url  = XMLPropertyHandler.getValue("institution.url");
				String name = XMLPropertyHandler.getValue("institution.logo.tooltip");
			%>
			<a href='<%=url%>'>
				<img class=logo alt='<%=name%>' src="images/InstitutionLogo.gif" width="240" height="45" border="0">
			</a>
		</td>
		<td align="left">
			<a href="#">
				<img src="images/appLogo.jpg" alt="Application Logo" width="230" height="50" border="0">
			</a>
		</td>

</table>