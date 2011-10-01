<%@page import="edu.wustl.catissuecore.ctrp.COPPAUtil"%>
<%@page import="edu.wustl.common.beans.SessionDataBean"%>
<%@page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@page import="java.util.Iterator"%>
<%@page import="org.apache.struts.action.ActionMessages"%>
<%@page import="org.apache.struts.action.ActionMessage"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page language="java" isELIgnored="false" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>	
<script language="JavaScript" type="text/javascript"
	src="jss/caTissueSuite.js"></script>	
<%
	final SessionDataBean sessionDataBean = (SessionDataBean) request
	.getSession().getAttribute(Constants.SESSION_DATA);
	boolean isAdminFlag = sessionDataBean.isAdmin();
	boolean isCOPPAEnabledFlag = COPPAUtil.isCOPPAEnabled();
%>
<c:set var="isAdminFlag" value="<%=isAdminFlag%>" scope="page" />
<c:set var="isCOPPAEnabledFlag" value="<%=isCOPPAEnabledFlag%>" scope="page" />
<script>	
var parentFormName = "${requestScope.formName}";
function checkCoppa(isCoppaEnabled) 
{
	if (isCoppaEnabled == "true") {
		//COPPA Enabled and possible matches found in COPPA
		var action = "CTRPInstitution.do";
		document.forms[0].operation.value = null;
		document.forms[0].action = action;
		document.forms[0].target = "_iframe-CTRPInstitutionSelect";
		openCRTPOrganizationWindow();
	}
}

function syncRemoteChangesFunc() {
	var form = document.forms[0];
	var prompt = confirm("Entity data will be synchronized with NCI. Any changes done locally will be lost. Do you want to continue? ");
	if (prompt == true){
		document.getElementById("dirtyEditFlag").value = false;
		document.getElementById("remoteOperation").value = "<%=Constants.REMOTE_OPERATION_SYNC%>";
		form.submit();
	}
}

function linkRemoteEntityFunc() {
	var form = document.forms[0];
		document.getElementById("remoteOperation").value = "<%=Constants.REMOTE_OPERATION_SEARCH_LINK%>";
		form.submit();
}
function enableLocalChangesFunc() {
	var form = document.forms[0];
	var prompt = confirm("Entity data will not be synchronized with NCI after editing locally. Do you want to continue?");
	if (prompt == true){
		form.name.disabled = false;
		form.name.focus();
		form.name.className="black_ar";
		document.getElementById("dirtyEditFlag").value = true;
		document.getElementById("enableLocalChanges").style.display = "none";
		document.getElementById("remoteOperation").value = "<%=Constants.REMOTE_OPERATION_EDIT%>";
	}
}


function closeCRTPOrganizationWindow() {
	dhtmlmodal.close(document.getElementById('CTRPInstitutionSelect'));
}

function openCRTPOrganizationWindow()
{
	crtpInsWindow=dhtmlmodal.open('CTRPInstitutionSelect', 'iframe', 'CTRPInstitution.do?operation=<%=request.getParameter("operation")%>','Select NCI Enterprise Organization', 'width=600px,height=200px,max-height=200px,center=1,resize=1,scrolling=1')
	crtpInsWindow.controls.onclick=function(){dhtmlmodal.close(this._parent)}
	crtpInsWindow.onclose=function()
	{
		if (document.getElementById("remoteId") != null) {
			var form = document.forms[0];
	 		form.action = parentFormName;
	 		form.target = "_self";
		}
		return true;
	}
}
</script>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="maintable">
<html:form action='${requestScope.formName}'>
	<html:hidden property="operation" />
	<html:hidden property="submittedFor"/>
	<html:hidden property="id" />
	<input type="hidden" name="remoteId" id="remoteId" property="remoteId" value="0">
	<input type="hidden" name="remoteManagedFlag" id="remoteManagedFlag" property="remoteManagedFlag" value=${institutionForm.remoteManagedFlag}>
	<input type="hidden" name="dirtyEditFlag" id="dirtyEditFlag" property="dirtyEditFlag" value=${institutionForm.dirtyEditFlag}>
	<input type="hidden" name="syncRemoteChanges" id="syncRemoteChanges" property="syncRemoteChanges">
	<input type="hidden" name="selectRemoteEntity" id="selectRemoteEntity" property="selectRemoteEntity">
	<input type="hidden" name="linkRemoteEntity" id="linkRemoteEntity" property="linkRemoteEntity">
	<html:hidden property="remoteOperation" styleId="remoteOperation" value="searchRemote"/>
  <tr>
    <td class="td_color_bfdcf3"><table border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td class="td_table_head"><span class="wh_ar_b"><bean:message key="Institution.header" /></span></td>
        <td><img src="images/uIEnhancementImages/table_title_corner2.gif" alt="Page Title - Institution" width="31" height="24" /></td>
      </tr>
    </table></td>
  </tr>
  <tr>
    <td class="tablepadding"><table width="100%" border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td width="4%" class="td_tab_bg" ><img src="images/spacer.gif" alt="spacer" width="50" height="1"></td>
         <logic:equal name="operation" value="add">
                      <td  valign="bottom" ><img src="images/uIEnhancementImages/tab_add_selected.jpg" alt="Add" width="57" height="22" /></td>
                      <td  valign="bottom"><html:link page="/SimpleQueryInterface.do?pageOf=pageOfInstitution&aliasName=Institution"><img src="images/uIEnhancementImages/tab_edit_notSelected.jpg" alt="Edit" width="59" height="22" border="0" /></html:link></td>
                       </logic:equal>
					  <logic:equal name="operation" value="edit">
						<td  valign="bottom" ><html:link page="/Institution.do?operation=add&pageOf=pageOfInstitution"><img src="images/uIEnhancementImages/tab_add_notSelected.jpg" alt="Add" width="57" height="22" border ="0" /></html:link></td>
                      <td  valign="bottom" ><img src="images/uIEnhancementImages/tab_edit_selected.jpg" alt="Edit" width="59" height="22"/></td>
                      </logic:equal>
        <td width="90%" valign="bottom" class="td_tab_bg">&nbsp;</td>
      </tr>
    </table>
      <table width="100%" border="0" cellpadding="3" cellspacing="0" class="whitetable_bg">
       <tr>
          <td align="left" class="bottomtd">
			<%@ include file="/pages/content/common/ActionErrors.jsp" %>
		  </td>
        </tr>
        <tr>
          <td align="left" class="tr_bg_blue1"><span class="blue_ar_b"> &nbsp;<logic:equal name="operation" value='${requestScope.operationAdd}'><bean:message key="institution.title"/></logic:equal><logic:equal name="operation" value='${requestScope.operationEdit}'><bean:message key="institution.editTitle"/></logic:equal></span></td>
        </tr>
        <tr>
          <td align="left" class="showhide">
	          <table width="100%" border="0" cellpadding="3" cellspacing="0">
	                <tr>
	                  <td width="1%" align="center" class="black_ar"><span class="blue_ar_b"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /></span></td>
	                  <td width="15%" align="left" class="black_ar"><bean:message key="institution.name"/></td>
	                  <td width="84%" align="left">
					 <c:choose>
					   <c:when test="${(institutionForm.remoteManagedFlag == true)}">
							 <c:choose>
							   <c:when test="${(institutionForm.dirtyEditFlag == false)}">
			                   <label>
			                   <html:text styleClass="black_ar_disabled" maxlength="255"  size="30" styleId="name" property="name" disabled="true"/>
			                   <img src="images/uIEnhancementImages/nci_remote.png" title="NCI enterprise entity managed remotely" width="16" height="16" hspace="0" vspace="0" />
			                   <c:if test="${(isAdminFlag == true)}">
				                   <html:link href="#" styleClass="view" styleId="enableLocalChanges" onclick="enableLocalChangesFunc();">
										<bean:message key="ctrp.edit.link.text" />
								   </html:link>
							  </c:if>
			                  </label>
							   </c:when>
							   <c:otherwise>
			                   <c:if test="${(isAdminFlag == true)}">
				                   <html:text styleClass="black_ar" maxlength="255"  size="30" styleId="name" property="name"/>
							   </c:if>
			                   <c:if test="${(isAdminFlag == false)}">
				                   <html:text styleClass="black_ar_disabled" maxlength="255"  size="30" styleId="name" property="name"/>
							   </c:if>
			                   <img src="images/uIEnhancementImages/nci_remote_dirty.png" title="NCI enterprise entity managed locally" height="15" width="15" hspace="0" vspace="0" />
			                   <c:if test="${(isCOPPAEnabledFlag == true) && (isAdminFlag == true)}">
			                   		<html:link href="#" styleClass="view" styleId="syncRemoteChangesLink" onclick="syncRemoteChangesFunc();">
				                   		<bean:message key="ctrp.edit.sync.text" />
								   </html:link>
			                   	</c:if>
			                  </label>
							   </c:otherwise>
			                 </c:choose>
					   </c:when>
					   <c:otherwise>
	                   <html:text styleClass="black_ar" maxlength="255"  size="30" styleId="name" property="name"/>
	                   <c:if test="${formName == 'InstitutionEdit.do'}">
			                   <c:if test="${(isCOPPAEnabledFlag == true)}">
				                   <html:link href="#" styleClass="view" styleId="linkRemoteEntityLink" onclick="linkRemoteEntityFunc();">
				                   		<bean:message key="ctrp.edit.remotelink.text" />
								   </html:link>
			                   	</c:if>
	                   </c:if>
	                  </label>
					   </c:otherwise>
	                  </c:choose>
	                  </td>
	                </tr>
	          </table>
          </td>
        </tr>
        <tr>
          <td class="buttonbg"><html:submit styleClass="blue_ar_b">
			<bean:message  key="buttons.submit" />
		</html:submit>
          </td>
        </tr>
      </table></td>
  </tr>
   </html:form>
</table>
<%
String hasCoppaData = (String)request.getAttribute("COPPA_MATCH_FOUND");
 ActionMessages messages = (ActionMessages)session.getAttribute("org.apache.struts.action.ACTION_MESSAGE");
 if (messages != null){ 
	 Iterator iter =  messages.get();
	  while(iter.hasNext()){
		  ActionMessage message = (ActionMessage) iter.next();
		  	System.out.println("Action Messages:"+message.getKey());
	  }
 }
session.setAttribute("org.apache.struts.action.ACTION_MESSAGE", null);
%>
 
<script>
//call after page loaded
window.onload=function(){checkCoppa("<%=hasCoppaData%>");}
</script>
<!--end content -->
