<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/nlevelcombo.tld" prefix="ncombo" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="edu.wustl.catissuecore.util.global.AppUtility"%>
<%@ page import="edu.wustl.catissuecore.util.global.Variables"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.common.util.global.CommonServiceLocator"%>
<%@ include file="/pages/content/common/AutocompleterCommon.jsp" %>
<%@ page language="java" isELIgnored="false" %>

<head>
<!-- Mandar : 434 : for tooltip -->
<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
	<script language="javascript">

	</script>
<!-- Mandar 21-Aug-06 : For calendar changes -->
<script src="jss/calendarComponent.js"></script>
<SCRIPT>var imgsrc="images/";</SCRIPT>
<LINK href="css/calanderComponent.css" type=text/css rel=stylesheet>
<link href="css/catissue_suite.css" rel="stylesheet" type="text/css" />
<!-- Mandar 21-Aug-06 : calendar changes end -->
</head>
<%
Long formContextId=(Long)request.getAttribute("formContextId");
Long recordIdentifier=(Long)request.getAttribute("recordIdentifier");
Object refreshEventGrid = request.getAttribute("refreshEventGrid");
%>

<script language="JavaScript">
function onIFrameLoad(iframeElement) {
	var oDoc = iframeElement.contentWindow || iframeList[j].contentDocument;
	if (oDoc.document)
	{
		oDoc = oDoc.document;
	}
	var inputCollection = oDoc.getElementsByTagName('input');
	var decontrols =0;
	for(i=0; i<inputCollection.length ;i++)
	{
		var search = 'Control';
		if(inputCollection[i].name.indexOf(search) == 0)
		{
			decontrols=  decontrols +1;
		}
	}
	if(decontrols == 0)
	{
		decontrols =1;
	}
	var frmHeight = (decontrols * 40)+5;
	var outerfrmHt = frmHeight+230;
	iframeElement.style.height= frmHeight+'px';
	if(parent.document.getElementById('newEventFrame') != null)
	{
		parent.document.getElementById('newEventFrame').style.height= outerfrmHt+'px';
	}
 }

function showHideCurrentFormDiv(currentFormId, currentElement, formContextId, isDataEntryPerformed)
{
	var currentIframeHt = Math.round(parent.document.getElementById(currentFormId).value);
	var currentSPPDiv = parent.document.getElementById("sppForms");
	var currentSPPDivHt = currentSPPDiv.style.height;
	if(currentSPPDivHt.indexOf("px")!= -1)
	{
		currentSPPDivHt = currentSPPDivHt.split("px")[0];
	}
	currentSPPDivHt = Math.round(currentSPPDivHt);
	if(currentElement.checked)
	{
		document.getElementById("showHideCurrentForm_"+formContextId).style.display = "block";
		parent.document.getElementById(formContextId).style.height = currentIframeHt+"px";
		var tmpHtt = currentSPPDivHt + currentIframeHt - 35;
		currentSPPDiv.style.height =  tmpHtt+"px";
	}
	else
	{
		if(isDataEntryPerformed)
		{
			var disableRecord = confirm("Note: This action will be deleted. Click OK to continue.");
			if(disableRecord)
			{
				document.getElementById("showHideCurrentForm_"+formContextId).style.display = "none"
				parent.document.getElementById(formContextId).style.height = "35px";
				var tmpHt = currentSPPDivHt - currentIframeHt + 35;
				currentSPPDiv.style.height =  tmpHt+"px";
			}
			else
			{
				currentElement.checked ="true";
			}
		}
		else
		{
			var disableWarnMsg = confirm("Note: This action will not be recorded. Click OK to continue.");
			if(disableWarnMsg)
			{
				document.getElementById("showHideCurrentForm_"+formContextId).style.display = "none"
				parent.document.getElementById(formContextId).style.height = "35px";
				var tmpHt = currentSPPDivHt - currentIframeHt + 35;
				currentSPPDiv.style.height =  tmpHt+"px";
			}
			else
			{
				currentElement.checked ="true";
			}
		}
		
	}
}

function setSkipEventValue(event)
{
	if(event.checked)
	{
		event.value= true;
	}
	else
	{
		event.value= false;
	}

}

function submitButton()
{
	var action="DynamicEventAdd.do";
	var iFrameElement=document.getElementById("name1");
	var search = 'Control';
	var search1 = 'comboControl';
	var a = window.frames[0].document.getElementsByTagName('input');
	var i=0;
	var formContextId=<%=formContextId%>;
	for(i=0; i<a.length ;i++)
	{
		if(a[i].name.indexOf(search) == 0  || a[i].name.indexOf(search1) == 0)
		{
			if(action.indexOf('?') == -1)
			{
			action=action+'?'+a[i].name+'='+a[i].value;
			}
			else
			{
			action=action+'&'+a[i].name+'='+a[i].value;
			}
		}
    }
	var inputCollection = document.getElementsByTagName('input');
			for(i=0; i<inputCollection.length ;i++)
			{
				if(inputCollection[i].title!='submit')
				{
					if(action.indexOf('?') == -1)
					{
						action=action+'?'+formContextId+'!@!'+inputCollection[i].name+'='+inputCollection[i].value;
					}
					else
					{
						action=action+'&'+formContextId+'!@!'+inputCollection[i].name+'='+inputCollection[i].value;
					}
				}
			}
	var selectCollection = document.getElementsByTagName('select');
			for(i=0; i<selectCollection.length ;i++)
			{
				if(action.indexOf('?') == -1)
				{
					action=action+'?'+formContextId+'!@!'+selectCollection[i].name+'='+selectCollection[i].value;
				}
				else
				{
					action=action+'&'+formContextId+'!@!'+selectCollection[i].name+'='+selectCollection[i].value;
				}
			}

			var textAreaCollection = document.getElementsByTagName('textarea');
			for(i=0; i<textAreaCollection.length ;i++)
			{
				if(action.indexOf('?') == -1)
				{
					action=action+'?'+formContextId+'!@!'+textAreaCollection[i].name+'='+textAreaCollection[i].value;
				}
				else
				{
					action=action+'&'+formContextId+'!@!'+textAreaCollection[i].name+'='+textAreaCollection[i].value;
				}
			}
			action=action+"&recordIdentifier="+<%=recordIdentifier%>;
			if(<%=refreshEventGrid%> == true)
			{
				action=action+"&refreshEventGrid="+<%=refreshEventGrid%>;
			}
			if(parent.document.getElementById("specimenLabel")!=null)
			{
				action = action + "&specimenLabel="+parent.document.getElementById("specimenLabel").value;
			}
			if(parent.document.getElementById("specimenEventParameter")!=null)
			{
				action = action + "&specimenEventParameter="+parent.document.getElementById("specimenEventParameter").value;
			}
	document.forms[0].action=action;

}
</script>

<table border="0" cellpadding="0" cellspacing="0" width="100%" height="100%">
<html:form action='${requestScope.formName}'>
	<html:hidden property="operation" />
	<html:hidden property="id" />
	<html:hidden property="contId" />
	<html:hidden property="recordEntry" />
	<html:hidden property="recordIdentifier" />
	<html:hidden property="specimenId" value='${requestScope.specimenId}'/>

		<tr>
			<td align="left" >
				<%@ include file="/pages/content/common/ActionErrors.jsp" %>
			</td>
		</tr>
		<c:choose>
			<c:when test='${param.allowToSkipEvents == "true"}'>
				<c:choose>
					<c:when test='${param.isSPPDataEntryDone == "true"}'>
						<c:choose>
							<c:when test='${param.displayEventsWithDefaultValues == "true"}'>
								<tr class="tr_bg_blue1">
									<td  align="left" class="tr_bg_blue1" width="98%"><span class="blue_ar_b"><input align="middle" type="checkbox" id="eventPerformed" name="eventPerformed" checked="true" onclick="showHideCurrentFormDiv('showHideFormHt_${param.formContextId}', this, '${param.formContextId}', false);setSkipEventValue(this)" value="true"/>&nbsp;${requestScope.formDisplayName}</span></td>
								</tr>
							</c:when>
							<c:otherwise>
								<tr class="tr_bg_blue1">
								<!-- disable event whose data entry is performed. -->	
									<td  align="left" class="tr_bg_blue1" width="98%"><span class="blue_ar_b"><input align="middle" type="checkbox" id="eventPerformed" name="eventPerformed" checked="true" onclick="showHideCurrentFormDiv('showHideFormHt_${param.formContextId}', this, '${param.formContextId}', true);setSkipEventValue(this);" value="true"/>&nbsp;${requestScope.formDisplayName}</span></td>
								</tr>
							</c:otherwise>
						</c:choose>
					</c:when>
					<c:otherwise>
						<!-- SPP data entry not performed. -->	
						<tr class="tr_bg_blue1">
							<td  align="left" class="tr_bg_blue1" width="98%"><span class="blue_ar_b"><input align="middle" type="checkbox" id="eventPerformed" name="eventPerformed" onclick="showHideCurrentFormDiv('showHideFormHt_${param.formContextId}', this, '${param.formContextId}', false);setSkipEventValue(this);" value="false"/>&nbsp;${requestScope.formDisplayName}</span></td>
						</tr>
					</c:otherwise>
				</c:choose>
			</c:when>
			<c:otherwise>
				<tr class="tr_bg_blue1">
				 <td align="left" class="tr_bg_blue1" width="98%"><span class="blue_ar_b">&nbsp;${requestScope.formDisplayName}</span></td>
				 </tr>
			</c:otherwise>
		</c:choose>
		<c:choose>
		<c:when test="${requestScope.isCaCoreGenerated == 'true' || requestScope.isCaCoreGenerated == true}">
		 <tr>
          <td colspan="4" class="showhide1"><Strong><bean:message key="cacore.not.generated"/></Strong></td>
        </tr>
		</c:when>
		<c:otherwise>
        <tr>
          <td colspan="4" class="showhide1"></td>
        </tr>
		<tr height="100%" id="showHideCurrentForm_${param.formContextId}">
          <td colspan="4" class="showhide" height="100%"><table height="100%" width="100%" border="0" cellpadding="3" cellspacing="0">
                <tr height="10%" >
                  <td width="1%" align="center" class="black_ar"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory Field" width="6" height="6" hspace="0" vspace="0" /></td>
                  <td width="15%" align="left" nowrap class="black_ar"><bean:message key="eventparameters.user"/></td>
				<td align="left" valign="middle" width="30%">
			<html:select property="userId" styleClass="formFieldSized18" styleId="userId" size="1"
				onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
				<html:options collection='${requestScope.userListforJSP}' labelProperty="name" property="value"/>
			</html:select>
				</td>
				<td width="1%"></td>
				<td colspan="2"></td></tr>
                <tr height="10%" >
                  <td align="center" class="black_ar"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory Field" width="6" height="6" hspace="0" vspace="0" /></td>
                  <td align="left" class="black_ar"><bean:message key="eventparameters.dateofevent"/></td>
                  <td align="left">
			  <logic:notEmpty name="currentEventParametersDate">
				<ncombo:DateTimeComponent name="dateOfEvent"
					 id="dateOfEvent"
					 formName="dynamicEventForm"
			                 month='${requestScope.dynamicEventParametersMonth}'
					 year='${requestScope.dynamicEventParametersYear}'
					 day='${requestScope.dynamicEventParametersDay}'
					 pattern="<%=CommonServiceLocator.getInstance().getDatePattern()%>"
					 value='${requestScope.currentEventParametersDate}'
					 styleClass="black_ar" />
			  </logic:notEmpty>
			  <logic:empty name="currentEventParametersDate">
				<ncombo:DateTimeComponent name="dateOfEvent"
					  id="dateOfEvent"
					  formName="dynamicEventForm"
					  pattern="<%=CommonServiceLocator.getInstance().getDatePattern()%>"
					  styleClass="black_ar"	/>
			  </logic:empty>
			  <span class="grey_ar_s"><bean:message key="page.dateFormat" /></span></td>

                  <td align="center" class="black_ar"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory Field" width="6" height="6" hspace="0" vspace="0" /></td>
                  <td align="left" class="black_ar" width="8%"><bean:message key="eventparameters.time"/></td>
                  <td align="left"><span class="black_ar">
			<autocomplete:AutoCompleteTag property="timeInHours"
				optionsList = '${requestScope.hourList}'
				initialValue='${dynamicEventForm.timeInHours}'
				styleClass="black_ar"
				staticField="false" size="4" />	&nbsp;<bean:message key="eventparameters.timeinhours"/>&nbsp;&nbsp;
			<autocomplete:AutoCompleteTag property="timeInMinutes"
			   optionsList = '${requestScope.minutesList}'
			   initialValue='${dynamicEventForm.timeInMinutes}'
			   styleClass="black_ar"
			   staticField="false" size="4" />
			   &nbsp;<bean:message key="eventparameters.timeinminutes"/></span></td>
                </tr>
                <tr height="15%" >
					<td align="center" class="black_ar">&nbsp;</td>
					<td align="left" valign="top" class="black_ar_t"><bean:message key="eventparameters.reasonfordeviation"/></td>
					<td align="left">
						<html:textarea styleClass="black_ar" cols="35" rows="2" style="overflow:auto" styleId="comments" property="reasonDeviation" />
					</td>
					<td align="center" class="black_ar">&nbsp;</td>
					<td align="left" valign="top" class="black_ar_t"><bean:message key="eventparameters.comments"/></td>
					<td align="left">
						<html:textarea styleClass="black_ar" cols="35" rows="2" style="overflow:auto" styleId="comments" property="comments" />
					</td>
                </tr>
					
				<tr name="iframetr"><td name="abc" colspan="6" >
                <iframe id="name1" name="name1" frameborder="0" width="100%" scrolling="no" src='${requestScope.iframeURL}' onload='onIFrameLoad(this);'>
                </iframe>
				</td></tr>

          </table></td>
        </tr>
		<c:if test="${param.hideSubmitButton == null || param.hideSubmitButton == 'null' || param.hideSubmitButton == 'false'}">
			<tr>
			  <td class="buttonbg">
			  <html:submit styleClass="blue_ar_b" title="submit" value="Submit" onclick="submitButton();"/>
			   </td>
			</tr>
		</c:if>
		</c:otherwise>
		</c:choose>
      </table></td>
  </tr>
  	 </html:form>
</table>