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
	Organization[] organizations = (Organization[]) session.getAttribute("COPPA_ORGANIZATIONS");
	List dataList = null;
	if (organizations != null) {
	dataList = Arrays.asList(organizations);
	}
	String pageName = "CTRPInstitution.do";	
	String[] columns = { "Name", "City", "State" };
	List columnList = Arrays.asList(columns);
	String title = "coppa.institution.select";
%>
		

	<script language="javascript">

	function closeWindowPopup(){
		this.parent.dhtmlmodal.close(this.parent.window.document.getElementById('CTRPInstitutionSelect'));
		
	}

	function submitOperation(operation)
	{
		
		var form;
		if (operation == 'Link'){
			var remoteSelection;
			for (index=0; index < document.forms[0].coppaSelection.length; index++) {
				if (document.forms[0].coppaSelection[index].checked) {
					remoteSelection = document.forms[0].coppaSelection[index].value;
					break;
				}
			}
			this.parent.window.document.getElementById("remoteId").value = remoteSelection;
			this.parent.window.document.getElementById("remoteOperation").value = "linkRemote";
			form = this.parent.window.document.forms[0];				
			form.action = this.parent.window.parentFormName;
			form.target="_parent";
		} else if (operation == 'Create'){
			this.parent.window.document.getElementById("remoteOperation").value = "noRemoteLink";
			form = this.parent.window.document.forms[0];				
			form.action = this.parent.window.parentFormName;
			form.target="_parent";
		} else if (operation == 'Search'){
			form = this.document.forms[0];
		}
		form.submit();
	}

	function enableLinkButton()
	{
		document.getElementById("linkButton").disabled = false;
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
						NCI Enterprise Organization Matches
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
<html:form action="CTRPInstitution.do" style="margin:0;padding:0;">
      <tr>
        <td align="left" class="tr_bg_blue1" colspan="4"><span class="blue_ar_b"> &nbsp;<bean:message key="<%=title%>" />&nbsp;</span></td>

      </tr>
      <tr>
            <td width="10%" align="left" class="black_ar">
            	<bean:message key="institution.name"/>
            </td>
            <td width="50%" align="left">
            	  <html:text styleClass="black_ar" maxlength="255"  size="30" styleId="entityName" property="entityName"/> 
            </td>
			<td width="20%" align="left"> 
				<html:button property="searchButton" styleId="searchButton" value="Search" styleClass="blue_ar_b" onclick="submitOperation('Search')"/>
            </td>
			<td width="20%" align="left"> 
            </td>
      </tr>
	<%
		if(dataList == null)
		{
		%>
		<tr>
			<td>
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
			String identifier;
			String orgName;
			String orgCity;
			String orgState;
			for(int j=0;j<dataList.size();j++)
			{
				Organization org = (Organization) dataList.get(j);
				identifier = org.getIdentifier().getExtension() ;
				orgName = org.getName().getPart().get(0).getValue();
				orgCity = COPPAUtil.getCity(org.getPostalAddress());
				orgState = COPPAUtil.getState(org.getPostalAddress());
			%>
			<tr>
				<td>
					<input type="radio" name="coppaSelection" id="<%= identifier%>" property="coppaSelection" value="<%= identifier%>" onclick="enableLinkButton()"/>
				 </td>		
				<td class="black_ar" nowrap="nowrap">	
					<%= orgName%>
				</td>
				<td class="black_ar" nowrap="nowrap">	
					<%= orgCity%>
				</td>
				<td class="black_ar" nowrap="nowrap">	
					<%= orgState%>
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
						<html:button property="linkButton" styleId="linkButton" value="Link to Existing Institution" styleClass="blue_ar_b" onclick="submitOperation('Link')" onkeypress="submitOperation('Link')" disabled="true"/>
						<c:choose>
							 <c:when test="${(operation eq 'add')}">
								<html:button property="cancelButton" value="Create New Institution" styleClass="blue_ar_b" onclick="submitOperation('Create')" onkeypress="submitOperation('Create')"/>
							</c:when>
							<c:otherwise>
								<html:cancel value="Cancel" styleClass="blue_ar_b" onclick="closeWindowPopup()" onkeypress="closeWindowPopup()"/>
							</c:otherwise>
						</c:choose>
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
