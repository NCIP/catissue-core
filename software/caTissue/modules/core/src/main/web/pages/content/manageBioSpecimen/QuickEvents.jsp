<!--
	This JSP page is to create specimen events. Specimens are to be searched based on specimen id or barCode.
	Author : Mandar Deshmukh
	Date   : July 03, 2006
-->

<%@ page import="org.apache.struts.action.ActionMessages"%>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.util.global.AppUtility"%>
<%@ page import="edu.wustl.catissuecore.actionForm.QuickEventsForm"%>
<%@ page import="java.util.*"%>
<%@ include file="/pages/content/common/AutocompleterCommon.jsp" %>
<%@ include file="/pages/content/common/EventAction.jsp" %>


<head>
<link href="css/catissue_suite.css" rel="stylesheet" type="text/css" />

	<script language="JavaScript">
		function onRadioButtonClick(element)
		{

			if(element.value == 1)
			{
				document.forms[0].specimenLabel.disabled = false;
				document.forms[0].barCode.disabled = true;
			}
			else
			{
				document.forms[0].barCode.disabled = false;
				document.forms[0].specimenLabel.disabled = true;
			}
		}

		// called when the event is selected from the combo
		function onParameterChange()
		{

			document.forms[0].action = "QuickEventsSearch.do";//action;
			document.forms[0].submit();
		}

	//code for auto height of iframe
	if ( document.getElementById && !(document.all) )
	{
		var slope=-10;
	}
	else
	{
		var slope=-30;
	}
window.onresize = function() {  mdFrmResizer("newEventFrame",30); }
//window.onload = function() { adjFrmHt('newEventFrame', .5,slope);}
//window.onresize = function() { adjFrmHt('newEventFrame', .5,slope); }
	</script>
</head>
<html:form method='POST' action="<%=Constants.QUICKEVENTS_ACTION%>">

<%

String eventSelected = (String)request.getAttribute(Constants.EVENT_SELECTED);
String specimenIdentifier = (String)request.getAttribute(Constants.SPECIMEN_ID);
String iframeSrc="blankScreenAction.do";
if(eventSelected != null)
{
	iframeSrc = getEventAction(eventSelected, specimenIdentifier);
	//formAction = Constants.QUICKEVENTSPARAMETERS_ACTION;
}

if(iframeSrc != null && request.getAttribute(Globals.ERROR_KEY)!=null)
{
	if(iframeSrc.contains("?"))
	{
		iframeSrc = iframeSrc + "&containsErrors=true";
	}
	else
	{
		iframeSrc = iframeSrc + "?containsErrors=true";
	}
}
session.setAttribute("EventOrigin", "QuickEvents");
%>

	<table width="100%" border="0" cellpadding="0" cellspacing="0" class="maintable"  height="100%">
		<tr>
			<td class="td_color_bfdcf3">
				<table border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td class="td_table_head">
							<span class="wh_ar_b"><bean:message key="cpbasedentry.specimenevents"/></span>
						</td>
						<td>
							<img src="images/uIEnhancementImages/table_title_corner2.gif" alt="Page Title - Specimen Events" width="31" height="24" />
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr height="98%">
			<td class="tablepadding" height="100%">
				<table width="100%" border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td width="4%" class="td_tab_bg" >
							<img src="images/uIEnhancementImages/spacer.gif" alt="spacer" width="50" height="1">
						</td>
		                    <td valign="bottom"><html:link page="/SimpleQueryInterface.do?pageOf=pageOfNewSpecimen&aliasName=Specimen"><img src="images/uIEnhancementImages/tab_edit_user.jpg"  border="0"alt="Edit" width="59" height="22" /></html:link></td>
							<td valign="bottom"><html:link page="/CreateSpecimen.do?operation=add&pageOf=pageOfDeriveSpecimen&virtualLocated=true"><img src="images/uIEnhancementImages/tab_derive2.gif" alt="Derive" width="56" height="22" border="0" /></html:link></td>
							 <td valign="bottom"><html:link page="/Aliquots.do?pageOf=pageOfAliquot"><img src="images/uIEnhancementImages/tab_aliquot2.gif" alt="Aliquot" width="66" height="22" /></html:link></td>
							<td valign="bottom"><img src="images/uIEnhancementImages/tab_events1.gif" alt="Events" width="56" height="22" border="0"/></td>
							<td align="left" valign="bottom" class="td_color_bfdcf3" ><html:link page="/MultipleSpecimenFlexInitAction.do?pageOf=pageOfMultipleSpWithMenu"><img src="images/uIEnhancementImages/tab_multiple2.gif" alt="Multiple" width="66" height="22" border="0" /></html:link></td>
						<td width="90%" align="left" valign="bottom" class="td_tab_bg" >&nbsp;</td>
					</tr>
				</table>
				<table width="100%" border="0" cellpadding="3" cellspacing="0" class="whitetable_bg" height="95%"><!-- Mandar : 17Nov08 height adjusted -->
					<tr>
						<td align="left" class="bottomtd">
							<%@ include file="/pages/content/common/ActionErrors.jsp" %>
						</td>
					</tr>
					<tr>
						<td align="left" class="tr_bg_blue1">
							<span class="blue_ar_b"> &nbsp;<bean:message key="mylist.label.specimenEvent"/></span>
						</td>
					</tr>
					<tr><!-- Mandar : 17Nov08 Row containing actual data  -->
						<td align="left" class="showhide">
							<table width="100%" border="0" cellpadding="3" cellspacing="0"><!-- Mandar : 17Nov08 height adjusted -->
								<tr>
									<td width="1%" align="center" class="black_ar">
										<span class="blue_ar_b"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory Field" width="6" height="6" hspace="0" vspace="0" /></span>
									</td>
									<td width="15%" align="left" class="black_ar">
										<bean:message key="specimen.label"/>
									</td>
									<td align="left" valign="middle" width="84%">
										<table width="53%" border="0" cellspacing="0" cellpadding="0" >
											<tr class="groupElements">
											<td>
											<html:textarea styleClass="black_ar" cols="100" rows="3" style="overflow:auto" styleId="specimenLabel" property="specimenLabel" />
											</td>
												<td align="left" valign="top">&nbsp;</td>
											</tr>
										</table>
									</td>
								</tr>
								<tr>
									<td align="center" class="black_ar">
										<span class="blue_ar_b"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" />
										</span>
									</td>
									<td align="left" class="black_ar">
										<bean:message key="eventparameters.event"/>
									</td>
									<td align="left" nowrap="nowrap" class="black_ar">
										<autocomplete:AutoCompleteTag property="specimenEventParameter"
										optionsList = "<%=request.getAttribute(Constants.EVENT_PARAMETERS_LIST)%>" initialValue='<%=request.getAttribute("specimenEventParameter")%>'
										styleClass="black_ar" size="45"/>
									</td>
									<td align="left" valign="top">&nbsp;</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td class="dividerline" >&nbsp;
							<html:button styleId="quickEventsButton" property="quickEventsButton" onclick="onParameterChange()" styleClass="blue_ar_b"><bean:message key="quickEvents.add" />
							</html:button>
						</td>
					</tr>
					<tr>
						<td align="left" class="toptd">&nbsp;</td>
					</tr>
					<tr height="*">
						<td align="left" class="black_ar" >
							<iframe name="newEventFrame" id="newEventFrame" src="<%=iframeSrc %>" width="100%" frameborder="0" scrolling="no">
							</iframe>
						</td>
					</tr>
<!-- Mandar : 18Nov08
					<tr height="*">
						<td align="left" class="toptd">&nbsp;</td>
					</tr>
-->
				</table>
			</td>
		</tr>
	</table>
</html:form>
<SCRIPT LANGUAGE="JavaScript">
<!--
 mdFrmResizer("newEventFrame",30);
//-->
</SCRIPT>