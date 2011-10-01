<%@page import="gov.nih.nci.coppa.po.Person"%>
<%@page import="edu.wustl.catissuecore.domain.User"%>
<%@page import="edu.wustl.common.beans.SessionDataBean"%>
<%@page import="java.util.Arrays"%>
<%@page import="edu.wustl.catissuecore.ctrp.COPPAUtil"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/PagenationTag.tld" prefix="custom" %>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="edu.wustl.catissuecore.actionForm.AdvanceSearchForm"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.util.global.AppUtility"%>
<%@ page import="edu.wustl.catissuecore.util.global.Variables"%>
<%@ page import="gov.nih.nci.coppa.po.Organization"%>
<%@ page language="java" isELIgnored="false"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<LINK href="css/catissue_suite.css" type=text/css rel=stylesheet>
<script src="jss/script.js"></script>
<script type="text/javascript" src="jss/dhtmlwindow.js"></script>
<script type="text/javascript" src="jss/modal.js"></script>
<script type="text/javascript" src="jss/ajax.js"></script> 
<style>
.active-column-0 {width:30px}
tr#hiddenCombo
{
 display:none;
}
</style>
<head>	
<%
	Person[] users = (Person[]) request.getAttribute("COPPA_USERS");
	List dataList = null;
	if (users != null) {
	dataList = Arrays.asList(users);
	}
	String pageName = "CTRPUser.do";	
	String[] columns = {"Email Address", "Last Name", "First Name"  };
	List columnList = Arrays.asList(columns);
	String title = "ctrp.user.select";
%>
		

	<script language="javascript">

	function closeWindowPopup(){
		this.parent.dhtmlmodal.close(this.parent.window.document.getElementById('CTRPSelectUser'));
		
	}

	function submitOperation(operation)
	{
		var remoteSelection;
		for (index=0; index < document.forms[0].coppaSelection.length; index++) {
			if (document.forms[0].coppaSelection[index].checked) {
				remoteSelection = document.forms[0].coppaSelection[index].value;
				break;
			}
		}
		this.parent.getRemoteUserDetails(remoteSelection, operation);
		closeWindowPopup();
	}

	function enableLinkButton()
	{
		document.getElementById("linkButton").disabled = false;
		if (document.getElementById("createButton") != null) {
			document.getElementById("createButton").disabled = false;
		}
	}
	</script>
	<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
</head>

<body>

<!-------new--->
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="maintable">
<tr>	
		<td class="td_color_bfdcf3">
			<table border="0" cellpadding="0" cellspacing="0">
		      <tr>
				<td class="td_table_head" nowrap="true">
					<span class="wh_ar_b">
						NCI Enterprise User Matches
					</span>
				</td>
		        <td>
					<img src="images/uIEnhancementImages/table_title_corner2.gif" alt="Page Title - Search Results" width="31" height="24" hspace="0" vspace="0" />
				</td>
		      </tr>
		    </table>
 
		</td>
	  </tr>
	   <tr>
		<td class="tablepadding">
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
			
      <tr>
        <td width="90%" valign="bottom" class="td_tab_bg">&nbsp;</td>
      </tr>
	 
    </table>
	<table width="100%" border="0" cellpadding="3" cellspacing="0" class="whitetable_bg">
      
      <tr>
        <td align="left" ><%@ include file="/pages/content/common/ActionErrors.jsp" %></td>
      </tr>
<html:form action="CTRPUser.do" style="margin:0;padding:0;">
      <tr>
        <td align="left" class="tr_bg_blue1" colspan="4"><span class="blue_ar_b"> &nbsp;<bean:message key="<%=title%>" />&nbsp;</span></td>

      </tr>
      <tr>
            <td width="10%" align="left" class="black_ar_b" nowrap="nowrap">
            	<bean:message key="ctrp.user.popup.emailAddress"/>
            </td>
            <td width="50%" align="left">
            	  <html:text styleClass="black_ar" maxlength="255"  size="40" styleId="emailAddress" property="emailAddress"/> 
            </td>
			<td width="20%" align="left"> 
				<html:button property="searchButton" styleId="searchButton" value="Search" styleClass="blue_ar_b" onclick="submit()"/>
            </td>
			<td width="20%" align="left"> 
            </td>
      </tr>
	<%
		if(dataList == null)
		{
		%>
		<tr>
			<td class="messagetexterror" nowrap="nowrap">
				<bean:message key="advanceQuery.noRecordsFound"/>
			</td>
		</tr>
		<%
		}
		else if(dataList != null && dataList.size() != 0)
		{
		%>
		<tr class="tableheading">
			<td>
			</td>
			<%
			for(int j=0;j<columnList.size();j++)
			{
			%>
			<td class="black_ar_b" nowrap="nowrap">	
				<%= columnList.get(j) %>
			</td>
			<%
			}
			%>
		</tr>

		
			<% 
			String remoteIdentifier;
			String userFirstName;
			String userLastName;
			String userEmailAddress;
			for(int j=0;j<dataList.size();j++)
			{
				Person person= (Person) dataList.get(j);
				remoteIdentifier =	COPPAUtil.getRemoteIdentifier(person);
			%>
			<tr>
				<td>
					<input type="radio" name="coppaSelection" id="<%= remoteIdentifier%>" property="coppaSelection" value="<%= remoteIdentifier%>" onclick="enableLinkButton()"/>
				 </td>		
				<td class="black_ar" nowrap="nowrap">	
					<%= COPPAUtil.getEmailAddress(person)%>
				</td>
				<td class="black_ar" nowrap="nowrap">	
					<%= COPPAUtil.getLastName(person)%>
				</td>
				<td class="black_ar" nowrap="nowrap">	
					<%= COPPAUtil.getFirstName(person)%>
				</td>
			</tr>
			<%
			}
			%>
				<!--  dummy input for single row in radio button selection to work -->
				<input type="hidden" name="coppaSelection"  property="coppaSelection"/>
		</tr>
		

		</table>
		<table>
		<tr valign="top" width="100%">
			<td  width="100%" valign="top" >
			</td>
		</tr>
		<tr width="100%" valign="top">
		
		<td width="90%">
		
			<table summary="" cellpadding="0" cellspacing="0" border="0" width="100%" valign="top">
			<tr>
					<td width="5%" nowrap align="left" valign="top">
						<html:button property="linkButton" styleId="linkButton" value="Link to Existing User" styleClass="blue_ar_b" onclick="submitOperation('Link')" onkeypress="submitOperation('Link')" disabled="true"/>
						<c:choose>
							 <c:when test="${(operation ne 'edit')}">
								<html:button property="createButton" styleId="createButton" value="Create New User" styleClass="blue_ar_b" onclick="submitOperation('Create')" onkeypress="submitOperation('Create')" disabled="true"/>
							</c:when>
						</c:choose>
								<html:cancel value="Cancel" styleClass="blue_ar_b" onclick="closeWindowPopup()" onkeypress="closeWindowPopup()"/>
					</td>
			</tr>
			</table>
			
			</td>
		</tr>
	<% } %>

	<tr>
		<td><html:hidden property="operation" value=""/></td>
	</tr>
</html:form>
</table>
</td>
</tr>
</table>
</body>	
