<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="edu.wustl.catissuecore.util.global.Variables,edu.wustl.catissuecore.util.global.Constants"%>
<%
        String operation = (String) request.getAttribute(Constants.OPERATION);
		String formName,prevPage=null,nextPage=null;
		boolean readOnlyValue;
		if (operation.equals(Constants.EDIT))
        {
            formName = Constants.REPORTED_PROBLEM_EDIT_ACTION;
			Long identifier = (Long)request.getAttribute(Constants.PREVIOUS_PAGE);
			prevPage = Constants.PROBLEM_DETAILS_ACTION+"?"+Constants.SYSTEM_IDENTIFIER+"="+identifier;
			identifier = (Long)request.getAttribute(Constants.NEXT_PAGE);
			nextPage = Constants.PROBLEM_DETAILS_ACTION+"?"+Constants.SYSTEM_IDENTIFIER+"="+identifier;
            readOnlyValue = true;
        }
        else
        {
            formName = Constants.REPORTED_PROBLEM_ADD_ACTION;
            readOnlyValue = false;
        }
%>
<!-- Mandar : 434 : for tooltip -->
<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>

<table width="100%" border="0" cellpadding="0" cellspacing="0" class="maintable">
<html:form action="<%=formName%>">	
<html:hidden property="operation" value="<%=operation%>" />
								<html:hidden property="id" />
  <tr>
    <td class="td_color_bfdcf3"><table border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td class="td_table_head" nowrap="nowrap"><span class="wh_ar_b"><bean:message key="app.reportProblem" /></span></td>
        <td><img src="images/uIEnhancementImages/table_title_corner2.gif" width="31" height="24" alt="Page Title - Report Problems"/></td>
      </tr>
    </table></td>
  </tr>
  <tr>
    <td class="tablepadding"><table width="100%" border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td width="90%" valign="bottom" class="td_tab_bg">&nbsp;</td>
      </tr>
    </table>
      <table width="100%" border="0" cellpadding="3" cellspacing="0" class="whitetable_bg">
      
      <tr>
        <td align="left" class="bottomtd"><%@ include file="/pages/content/common/ActionErrors.jsp" %></td>
      </tr>
	  <logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.EDIT%>">
	  <tr>
	  <% 
									String backPage = Constants.REPORTED_PROBLEM_SHOW_ACTION+"?"+Constants.PAGE_NUMBER+"="+Constants.START_PAGE; 
								%>
		<td class="black_ar" >&nbsp;&nbsp;&nbsp;
			<a	class="view" href="<%=backPage%>">
		
												<bean:message key="reportedProblem.home" />
											</a>&nbsp;
		
										<logic:notEmpty name="prevpage">
											|&nbsp;
												
													<a	class="view" href="<%=prevPage%>">
														<bean:message key="approveUser.previous" />
													</a>
												&nbsp;								
										</logic:notEmpty>

										<logic:notEmpty name="nextPage">
											|&nbsp; 
												
													<a	class="view" href="<%=nextPage%>">
														<bean:message key="approveUser.next" />
													</a>
												&nbsp;								
										</logic:notEmpty>
									</td>
	  </tr>
	  </logic:equal>
      <tr>
        <td align="left" class="tr_bg_blue1"><span class="blue_ar_b">&nbsp;<bean:message key="reportProblem.title"/></span></td>
      </tr>
      <tr>
        <td align="left" valign="top" class="showhide"><table width="100%" border="0" cellpadding="3" cellspacing="0">
            
              <tr>
                <td width="1%" align="center" class="black_ar"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory Field" width="6" height="6" hspace="0" vspace="0" /></td>
                <td width="16%" align="left" class="black_ar"><bean:message key="fields.from" /></td>
                <td width="83%" align="left" valign="middle"><span class="black_ar">
                  <html:text  styleClass="black_ar" maxlength="255" size="30"
															styleId="from" property="from"	readonly="<%=readOnlyValue%>" />
                </span>                  </tr>
              <tr>
                <td align="center" class="black_ar"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory Field" width="6" height="6" hspace="0" vspace="0" /></td>
                <td align="left" class="black_ar"><bean:message key="fields.nameofreporter" /></td>
                <td align="left" valign="middle"><span class="black_ar">
                  <html:text  styleClass="black_ar" maxlength="255" size="30"
															styleId="nameOfReporter" property="nameOfReporter"	readonly="<%=readOnlyValue%>" />
                </span>                   </tr>
              <tr>
                <td align="center" class="black_ar"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory Field" width="6" height="6" hspace="0" vspace="0" /></td>
                <td align="left" class="black_ar"><bean:message key="fields.affiliation" /></td>
                <td align="left" valign="middle"><span class="black_ar">
                  <html:text  styleClass="black_ar" maxlength="255" size="30"
															styleId="affiliation" property="affiliation"
															readonly="<%=readOnlyValue%>" />
                </span>                   </tr>
              <tr>
                <td align="center" class="black_ar"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory Field" width="6" height="6" hspace="0" vspace="0" /></td>
                <td align="left" class="black_ar"><bean:message key="fields.title" /> </td>
                <td align="left" valign="middle"><span class="black_ar">
                  <html:text  styleClass="black_ar" maxlength="255" size="30"
															styleId="subject" property="subject"
															readonly="<%=readOnlyValue%>" />
                </span>                   </tr>
              <tr>
                <td align="center" class="black_ar"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory Field" width="6" height="6" hspace="0" vspace="0" /></td>
                <td align="left" class="black_ar"><bean:message key="fields.message" /> </td>
                <td align="left" valign="middle"><html:textarea styleClass="formFieldSizedNew" cols="34" rows="5"
														styleId="messageBody" property="messageBody" readonly="<%=readOnlyValue%>" />                </tr>
				<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.EDIT%>">
				<tr>
				<td align="center" class="black_ar"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory Field" width="6" height="6" hspace="0" vspace="0" /></td>
                <td align="left" class="black_ar"><bean:message key="reportProblem.status" /></td>	
				<td align="left" valign="middle"><html:select property="activityStatus" styleClass="formFieldSizedNew"
															styleId="activityStatus" size="1"	onmouseover="showTip(this.id)"
															onmouseout="hideTip(this.id)">
															<html:options name="activityStatusList" labelName="activityStatusList" />
														</html:select></td>
														</tr>
							</logic:equal>
            
        </table></td>
      </tr>
      <tr>
        <td align="left" class="buttonbg"><html:submit styleClass="blue_ar_b" accesskey="Enter">
											<bean:message key="buttons.submit" />
										</html:submit>
          &nbsp;|&nbsp; <html:link	page="/ManageAdministrativeData.do" styleClass="cancellink">
												<bean:message key="buttons.cancel" />
											</html:link></td>
      </tr>
    </table></td>
  </tr>
  </html:form>
</table>
