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
	var defualtHelpText="Enter comma separated label list";
	function clearContents(fld)
	{
		if(fld.value ==defualtHelpText)
		{
			fld.value='';
			fld.style.color='';
		}	
	}
	function showHelpText(fld)
	{
		if(fld.value =="")
		{
			fld.style.color='grey';
			fld.value=defualtHelpText;	
		}
	}
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
		function onParameterChange(dropDownFld)
		{
			var specimenLabelText=document.forms[0].specimenLabel.value;
			var selectedEvent=document.forms[0].specimenEventParameter.value;
			if(specimenLabelText == defualtHelpText || specimenLabelText =="")
			{
				alert("please enter specimen label(s)");
				document.forms[0].specimenEventParameter.value="-- Select --";
			}
			else
			{
				if(selectedEvent !="-- Select --")
				{
					document.forms[0].action = "QuickEventsSearch.do";//action;			
					document.forms[0].submit();	
				}
				else
				{
					document.forms[0].action = "QuickEvents.do";//action;			
					document.forms[0].submit();
				}	
					
			} 	
					
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
window.onresize = function() {  mdFrmResizer("newEventFrame",65); }
//window.onload = function() { adjFrmHt('newEventFrame', .5,slope);}
//window.onresize = function() { adjFrmHt('newEventFrame', .5,slope); }
	</script>
</head>
<html:form method='POST' action="<%=Constants.QUICKEVENTS_ACTION%>">

<%

String eventSelected = (String)request.getAttribute(Constants.EVENT_SELECTED);
String specimenIdentifier = (String)request.getAttribute(Constants.SPECIMEN_ID);
String iframeSrc="blankScreenAction.do";
String defualtLabelText="Enter comma separated label list";
if((String)request.getParameter("specimenLabel") !=null)
{
	defualtLabelText=(String)request.getParameter("specimenLabel");
}
if(eventSelected != null)	
{
	iframeSrc = getEventAction(eventSelected, specimenIdentifier);
	//formAction = Constants.QUICKEVENTSPARAMETERS_ACTION;
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
										<bean:message key="specimen.details.label"/>
									</td>
									<td align="left" valign="middle" width="84%">
										<table width="53%" border="0" cellspacing="0" cellpadding="0" >
											<tr >
												<td>
												
													<%-- <html:radio styleClass="" styleId="checkedButton" property="checkedButton" value="1"
													onclick="onRadioButtonClick(this)">
													</html:radio>
													
													<bean:message key="specimen.label"/>&nbsp;
													<logic:equal name="quickEventsForm" 	property="checkedButton" value="1">
														<html:text styleClass="black_ar"  maxlength="50"  size="20" styleId="specimenLabel" property="specimenLabel" disabled="false" />
														
													</logic:equal> --%>
													<%
														if(defualtLabelText.equals("Enter comma separated label list"))
														{
															%>
																<html:textarea styleClass="black_ar" rows = "2" cols="28"  styleId ='labels' property="specimenLabel" style="width:40em;color:grey;" onfocus="clearContents(this);" value="<%=defualtLabelText %>" onblur="showHelpText(this);">
																</html:textarea>
															<%
														}
														else
														{
															%>
																<html:textarea styleClass="black_ar" rows = "2" cols="28"  styleId ='labels' property="specimenLabel" style="width:40em;" onfocus="clearContents(this);" value="<%=defualtLabelText %>" onblur="showHelpText(this);">
																</html:textarea>
															<%
														}
													%>
													
													<%-- <logic:equal name="quickEventsForm" property="checkedButton" value="2">
														<html:text styleClass="black_ar"  maxlength="50"  size="20" styleId="specimenLabel" 	property="specimenLabel" disabled="true" />
													</logic:equal>&nbsp;&nbsp; --%> 
													
												</td>
												<%-- <td align="left" valign="middle" nowrap="nowrap" >
													<html:radio styleClass="" styleId="checkedButton" property="checkedButton" value="2" 	onclick="onRadioButtonClick(this)">
													</html:radio>
													<span class="black_ar">
														<bean:message key="storageContainer.barcode"/>&nbsp;
														<logic:equal name="quickEventsForm" property="checkedButton" value="1">
														<html:text styleClass="black_ar"  maxlength="50"  size="20" styleId="barcode" property="barCode" disabled="true" />
														</logic:equal>
														<logic:equal name="quickEventsForm" property="checkedButton" value="2">
														<html:text styleClass="black_ar"  maxlength="50"  size="20" styleId="barcode" property="barCode" disabled="false" />
														</logic:equal>
													</span>
												</td> --%>
												<!-- <td align="left" valign="top">&nbsp;</td> -->
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
										<html:select property="specimenEventParameter" styleClass="formFieldSized15" styleId="className" size="1" disabled="false" onchange="onParameterChange(this);" onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
											<html:options name="<%=Constants.EVENT_PARAMETERS_LIST%>" labelName="<%=Constants.EVENT_PARAMETERS_LIST%>"/>
										</html:select>
									</td>
									<td align="left" valign="top">&nbsp;</td>
								</tr>
							</table>
						</td>
					</tr>
					<%-- <tr>
						<td class="dividerline" >&nbsp;
							<html:button property="quickEventsButton" onclick="onParameterChange()" styleClass="blue_ar_b"><bean:message key="quickEvents.add" />
							</html:button>
						</td>
					</tr>
					<tr>
						<td align="left" class="toptd">&nbsp;</td>
					</tr> --%>
					<tr height="*">
						<td align="left" class="black_ar" >
							<iframe name="newEventFrame" id="newEventFrame" src="<%=iframeSrc %>" width="100%" height="321px" frameborder="0" scrolling="auto">
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
 mdFrmResizer("newEventFrame",65);
//-->
</SCRIPT>