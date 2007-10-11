<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="edu.wustl.common.util.global.ApplicationProperties" %>

<logic:notEmpty scope="session" name="<%=Constants.SESSION_DATA%>">
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>

<%--<tr>
	<td class="subMenuPrimaryTitle" height="22">
		<a href="#content">
    		<img src="images/shim.gif" alt="Skip Menu" width="1" height="1" border="0" />
    	</a>
	</td>
</tr>--%>

<tr>
	<td class="subMenuPrimaryItemsWithBorder" onmouseover="changeMenuStyle(this,'subMenuPrimaryItemsWithBorderOver')" onmouseout="changeMenuStyle(this,'subMenuPrimaryItemsWithBorder')">				
		<div>
			<!--img src="images/subMenuArrow.gif" width="7" height="7" alt="" /--> 
				<b> <bean:message key="app.userProfile" /> </b>
		</div>		
		
		<div>
			<a class="subMenuPrimary" href="UserSearch.do?pageOf=pageOfUserProfile&id=<bean:write name="<%=Constants.SESSION_DATA%>" property="userId" scope="session"/>" >
				<bean:message key="app.edit" />
			</a> |  
			<a class="subMenuPrimary" href="ChangePassword.do?operation=edit&amp;pageOf=pageOfChangePassword" >
					<bean:message key="user.changePassword" />
			</a>			
		</div>
	</td>
</tr>
<tr>
	<td class="subMenuPrimaryItemsWithBorder" onmouseover="changeMenuStyle(this,'subMenuPrimaryItemsWithBorderOver')" onmouseout="changeMenuStyle(this,'subMenuPrimaryItemsWithBorder')">				
		<div>
			<b> <bean:message key="app.Privileges" /> </b>
		</div>
		
		<div>
	  		<a class="subMenuPrimary" href="AssignPrivilegesPage.do?pageOf=pageOfAssignPrivilegesPage">
	  			<bean:message key="app.assign" />
	  		</a>
	  	</div>
	</td>
</tr>


</logic:notEmpty>
<tr>
<td class="subMenuPrimaryTitle" height="21">
	<bean:message key="app.quickLinks" />
<!-- anchor to skip sub menu -->
	<a href="#content">
		<img src="images/shim.gif" alt="Skip Menu" width="1" height="1" border="0" />
	</a>
</td>
</tr>

<tr>
	<td class="subMenuSecondaryTitle" onmouseover="changeMenuStyle(this,'subMenuSecondaryTitleOver'),showCursor()" onmouseout="changeMenuStyle(this,'subMenuSecondaryTitle'),hideCursor()" height="20" onclick="document.location.href='http://cabig.nci.nih.gov/'">
	<a class="subMenuSecondary" href="http://cabig.nci.nih.gov/">
		<bean:message key="app.cabigHome" />
	</a>
</td>
</tr>

<tr>
	<td class="subMenuSecondaryTitle" onmouseover="changeMenuStyle(this,'subMenuSecondaryTitleOver'),showCursor()" onmouseout="changeMenuStyle(this,'subMenuSecondaryTitle'),hideCursor()" height="20" onclick="document.location.href='http://ncicb.nci.nih.gov/'">
		<a class="subMenuSecondary" href="http://ncicb.nci.nih.gov/">
			<bean:message key="app.ncicbHome" />
		</a>
	</td>
</tr>

<tr>
	<% String str = ApplicationProperties.getValue("allPage.sitHome"); %>
	<td class="subMenuSecondaryTitle" onmouseover="changeMenuStyle(this,'subMenuSecondaryTitleOver'),showCursor()" onmouseout="changeMenuStyle(this,'subMenuSecondaryTitle'),hideCursor()" height="20" onclick="document.location.href='<%=str %>'">
		<a class="subMenuSecondary" href="<%=str %>">
			<bean:message key="app.siteHome" />
		</a>
	</td>
</tr>