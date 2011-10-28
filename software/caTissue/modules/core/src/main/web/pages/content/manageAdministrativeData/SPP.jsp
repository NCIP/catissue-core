<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page language="java" isELIgnored="false" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<script language="JavaScript" type="text/javascript"
	src="jss/caTissueSuite.js"></script>

<%
	request.setAttribute("id", request.getAttribute("id"));
	String operation = (String) request.getAttribute(Constants.OPERATION);
	request.setAttribute("operation", operation);
	String formAction;

	if (operation.equals(Constants.EDIT))
	{
    	formAction = Constants.SPP_EDIT_ACTION;
	}
	else
	{
		formAction = Constants.SPP_ADD_ACTION;
	}
%>
<body>
	<script>
		function saveSPP()
		{
			var action = document.forms[0].action + "?setDefaultValue=true";
			<%
			if(request.getAttribute(Constants.ID) != null)
			{ %>
				action =  action+"&id=<%=request.getAttribute(Constants.ID)%>";
			<%}%>
			document.forms[0].action =  action;
			document.forms[0].submit();
		}
	</script>
<html:form action="<%=formAction%>" enctype="multipart/form-data">
	<html:hidden property="operation" value="<%=operation%>"/>
	<html:hidden property="submittedFor"/>
	<html:hidden property="id"/>
	<table width="100%" border="0" cellpadding="0" cellspacing="0" class="maintable">
		<tr>
			<td class="td_color_bfdcf3"><table border="0" cellpadding="0" cellspacing="0">
			  <tr>
				<td class="td_table_head"><span class="wh_ar_b">Specimen Processing Procedure</span></td>
				<td align="right"><img src="images/uIEnhancementImages/table_title_corner2.gif" alt="Page Title - Specimen Processing Procedure" width="31" height="24" /></td>
			  </tr>
			</table></td>
		</tr>
		<tr>
			<td class="tablepadding">
				<table width="100%" border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td width="4%" class="td_tab_bg" >
						<img src="images/spacer.gif" alt="spacer" width="50" height="1">
					</td>
					<logic:equal name="operation" value="add">
						<td  valign="bottom" ><img src="images/uIEnhancementImages/tab_add_selected.jpg" alt="Add" width="57" height="22" /></td>
						<td  valign="bottom"><html:link page="/SimpleQueryInterface.do?pageOf=pageOfSPP&aliasName=SpecimenProcessingProcedure"><img src="images/uIEnhancementImages/tab_edit_notSelected.jpg" alt="Edit" width="59" height="22" border="0" /></html:link></td>
					</logic:equal>
					<logic:equal name="operation" value="edit">
						<td  valign="bottom" ><html:link page="/SPP.do?operation=add&pageOf=pageOfSPP"><img src="images/uIEnhancementImages/tab_add_notSelected.jpg" alt="Add" width="57" height="22" border ="0" /></html:link></td>
						<td  valign="bottom" ><img src="images/uIEnhancementImages/tab_edit_selected.jpg" alt="Edit" width="59" height="22"/></td>
					</logic:equal>
					<td width="90%" valign="bottom" class="td_tab_bg">&nbsp;
					</td>
				</tr>
				</table>
				<table width="100%" border="0" cellpadding="3" cellspacing="0" class="whitetable_bg">
				<tr>
					<td align="left" class="bottomtd">
						<%@ include file="/pages/content/common/ActionErrors.jsp" %>
					</td>
				</tr>
				<tr>
					<td align="left" class="tr_bg_blue1"><span class="blue_ar_b"> &nbsp;<logic:equal name="operation" value="<%=Constants.ADD%>">Add Specimen Processing Procedure</logic:equal>
						<logic:equal name="operation" value="<%=Constants.EDIT%>">Edit Specimen Processing Procedure</logic:equal></span>
					</td>
				</tr>
				<tr>
					<td align="left" class="showhide">
						<table width="100%" border="0" cellpadding="3" cellspacing="0">
							<tr>
								<td width="1%" align="center" class="black_ar"><span class="blue_ar_b"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /></span>
								</td>
								<td align="left" class="black_ar" width="25%">
									<b>SPP Name</b>
								</td>
								<td align="left" width="74%">
									<html:text styleClass="black_ar" maxlength="255"  size="30" styleId="name" property="name"/>
								</td>
							</tr>
							<tr>
								<td width="1%" align="center" class="black_ar"><span class="blue_ar_b"></span>
								</td>
								<td align="left" class="black_ar" width="25%">
									<b>SPP Barcode</b>
								</td>
								<td align="left" width="74%">
									<html:text styleClass="black_ar" maxlength="255"  size="30" styleId="barcode" property="barcode"/>
								</td>
							</tr>
							<tr>
								<td width="1%" align="center" class="black_ar"><span class="blue_ar_b"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /></span>
								</td>
								<td align="left" class="black_ar" width="25%">
									<b>Upload XML file</b>
								</td>
								<td align="left" valign="top" width="75%">
									<input id="file" type="file" name="xmlFileName" value="Browse" size="34">
									</input>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td align="left" class="bottomtd">
						<logic:equal name="operation" value="<%=Constants.EDIT%>">
							<span class="messagetextwarning">
								Existing events cannot be modified or deleted during SPP edit if the SPP is already associated to a protocol or specimen. You are only allowed to add new events.
							</span>
						</logic:equal>
					</td>
				</tr>
				<tr>
					<td class="buttonbg" width="5">
						<table width="100%" border="0" cellpadding="3" cellspacing="0">
							<tr>
								<td>
									<html:submit styleClass="blue_ar_b">Save
									</html:submit>
								</td>
								<td>
									<input class="blue_ar_b" type="button" value="Save and Set Default Values" style="width:200" onclick="saveSPP()">
								</td>
								<td style="width:100%">
								&nbsp;
								</td>
							</tr>
						</table>
					</td>
				</tr>
				</table>
			</td>
		</tr>
	</table>
</html:form>
</body>
<!--end content -->
