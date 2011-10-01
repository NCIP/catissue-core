<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="edu.wustl.catissuecore.actionForm.GSIDBatchUpdateForm"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/PagenationTag.tld" prefix="custom"%>
<%@ page language="java" isELIgnored="false"%>
<%@ page import="edu.wustl.catissuecore.util.global.Variables"%>
<%@ page import="edu.wustl.catissuecore.util.global.AppUtility"%>
<%@ page import="edu.wustl.common.util.global.CommonServiceLocator"%>
<%@ page import="java.util.*"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page import="edu.wustl.catissuecore.GSID.GSIDConstant" %>

<script type="text/javascript" src="jss/jquery/jquery-1.5.1.min.js"></script>
<script type="text/javascript" src="jss/jquery/gsid/updateStatus.js"></script>
<%
GSIDBatchUpdateForm form=(GSIDBatchUpdateForm) request.getAttribute("GSIDBatchUpdateForm");
%>
<html:form action="/gsidBatchUpdate">
	<table width="100%" border="0" cellpadding="0" cellspacing="0"
		class="maintable">
		<tr>
			<td class="td_color_bfdcf3">
			<table border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td class="td_table_head"><span class="wh_ar_b"> <%= GSIDConstant.GSID_UI_NOTIFICATION_MSG %> </span></td>
					<td align="right"><img
						src="images/uIEnhancementImages/table_title_corner2.gif"
						alt="Page Title - <%= GSIDConstant.GSID_UI_NOTIFICATION_MSG %>" width="31" height="24" /></td>
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
			<table width="100%" border="0" cellpadding="3" cellspacing="0"
				class="whitetable_bg">
				<tr>
					<td width="99%" align="left" class="bottomtd"><%@ include
						file="/pages/content/common/ActionErrors.jsp"%></td>
				</tr>
				<%
				if(form.isLocked())
				{
					//that means some one is running the batch assign process so just display the result.
				%>
				<tr>
					<td align="left" class="tr_bg_blue1"><span class="blue_ar_b">
					&nbsp;<%= form.getCurrentUser() %> is running this process. Process is <span id="gsidPercentage"><%= form.getPercentage() %></span>% complete.</span>
					<input type="hidden" name="islocked" value="<%= form.isLocked() %>"/>
					</td>
				</tr>
				<%
				}
				else
				{
					//that means no one is running the batch assign process.
					if(!StringUtils.isBlank(form.getCurrentUser()))		
					{
						//that means some one has ran this process atleast once.
						%>
						<tr>
						<td align="left" class="tr_bg_blue1"><span class="blue_ar_b">
						&nbsp;<%= form.getCurrentUser() %> ran this process before and the process was <span id="gsidPercentage"><%= form.getPercentage() %></span>% complete.</span></td>
						</tr>
						<%
						//if the percetage!=100 that means that task got completed but the there were some errors.
						if(form.getPercentage()!=100)
						{
							%>
							<tr>
						<td align="left" class="tr_bg_blue1"><div class="blue_ar_b">
						<%
						String label=StringUtils.isBlank(form.getLastProcessedLabel())?"None":form.getLastProcessedLabel();
						%>
						&nbsp;Error occurred while updating the Specimen. This is mostly because of the GSID service being down. See log files for more details. The last Specimen processed was <%= label %>.</div></td>
						</tr>
							<%
						}
						//check if there any more left.
						if(form.getUnassignedSpecimenCount()>0)
						{
							%>
							<td align="left" class="tr_bg_blue1"><span class="blue_ar_b">
						&nbsp;There are still some specimens for processing.</span></td>
						</tr>
							<%
						}
					}
					
					if(form.getUnassignedSpecimenCount()<=0)
					{
						%>
						<tr>
						<td align="left" class="tr_bg_blue1"><span class="blue_ar_b">
						&nbsp;There are no more specimens for processing.</span></td>
						</tr>
					<%
					}
					else
					{						
					%>
					<tr>
					<td align="left" class="tr_bg_blue1">
						<div class="blue_ar_b">&nbsp;This will assign global specimen identifiers for all <%= form.getUnassignedSpecimenCount() %> specimens.</div>
						<div class="blue_ar_b">&nbsp;Are you sure you want to continue?
							<form action="gsidBatchUpdate.do" method="post">
								<input type="hidden" name="force" value="true"/>
								<input type="hidden" name="islocked" value="<%= form.isLocked() %>"/>
								<input type="submit" value="Yes" class='blue_ar_b'/>
							</form>
						</div>
					</td>
					</tr>
					<%
					}
									
				}
				%>
				<tr>
					<td align="left" class="tr_bg_blue1"><span class="blue_ar_b">
					&nbsp;<html:hidden property="force" value="true" /></span></td>
				</tr>			
			</table>
			</td>
		</tr>
	</table>
</html:form>