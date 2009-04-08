<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/nlevelcombo.tld" prefix="ncombo" %>
<%@ page import="edu.wustl.catissuecore.util.global.AppUtility"%>
<%@ page import="edu.wustl.catissuecore.util.global.Variables"%>
<%@ page import="edu.wustl.catissuecore.actionForm.FluidSpecimenReviewEventParametersForm"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ include file="/pages/content/common/AutocompleterCommon.jsp" %> 
<%@ page language="java" isELIgnored="false" %>

<head>
<!-- Mandar : 434 : for tooltip -->
<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
<!-- Mandar 21-Aug-06 : For calendar changes -->
<script src="jss/calendarComponent.js"></script>
<SCRIPT>var imgsrc="images/";</SCRIPT>
<LINK href="css/calanderComponent.css" type=text/css rel=stylesheet>
<link href="css/catissue_suite.css" rel="stylesheet" type="text/css" />
<!-- Mandar 21-Aug-06 : calendar changes end -->
</head>
			
<%@ include file="/pages/content/common/ActionErrors.jsp" %>
    
<table border="0" cellpadding="0" cellspacing="0" width="100%"> 
<html:form action='${requestScope.formName}'>
	<html:hidden property="operation" />
	<html:hidden property="id" />
	<html:hidden property="specimenId" value='${requestScope.specimenId}'/>        
<tr>
          <td align="left" class="tr_bg_blue1"><span class="blue_ar_b">&nbsp;<bean:message  key="eventparameters"/> &quot;<em>
          <bean:message  key="fluidspecimenreviewparameter"/></em>&quot;</span></td>
        </tr>
        <tr>
          <td colspan="4" class="showhide1"></td>
        </tr>
        <tr>
          <td colspan="4" class="showhide"><table width="100%" border="0" cellpadding="1" cellspacing="0">
                <tr>
                  <td width="1%" align="center" class="black_ar"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory Field" width="6" height="6" hspace="0" vspace="0" /></td>
                  <td width="15%" align="left" nowrap class="black_ar"><bean:message key="eventparameters.user"/></td>
	          <td align="left" valign="middle" width="30%">
			<html:select property="userId" styleClass="formFieldSized18" styleId="userId" size="1"
				onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
				<html:options collection='${requestScope.userListforJSP}' labelProperty="name" property="value"/>
			</html:select>
		  </td>
		   <td width="1%"></td>
				<td colspan="2"></td>
		</tr>
                <tr>
                  <td align="center" class="black_ar"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory Field" width="6" height="6" hspace="0" vspace="0" /></td>
                  <td align="left" class="black_ar"><bean:message key="eventparameters.dateofevent"/></td>
                  <td align="left">
			  <logic:notEmpty name="currentEventParametersDate">
				<ncombo:DateTimeComponent name="dateOfEvent"
					 id="dateOfEvent"
					 formName="fluidSpecimenReviewEventParametersForm"
			                 month='${requestScope.eventParametersMonth}'
					 year='${requestScope.eventParametersYear}'
					 day='${requestScope.eventParametersDay}'
					 pattern="<%=CommonServiceLocator.getInstance().getDatePattern()%>"
					 value='${requestScope.currentEventParametersDate}'
					 styleClass="black_ar" />
			  </logic:notEmpty>
			  <logic:empty name="currentEventParametersDate">
				<ncombo:DateTimeComponent name="dateOfEvent"
					  id="dateOfEvent"
					  formName="fluidSpecimenReviewEventParametersForm"
					  pattern="<%=CommonServiceLocator.getInstance().getDatePattern()%>"
					  styleClass="black_ar"	/>
			  </logic:empty>
			  <span class="grey_ar_s"><bean:message key="page.dateFormat" /></span></td>
                
                  <td align="center" class="black_ar"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory Field" width="6" height="6" hspace="0" vspace="0" /></td>
                  <td align="left" class="black_ar" width="8%"><bean:message key="eventparameters.time"/></td>
                  <td align="left"><span class="black_ar">
			<autocomplete:AutoCompleteTag property="timeInHours"
				optionsList = '${requestScope.hourList}'
				initialValue='${fluidSpecimenReviewEventParametersForm.timeInHours}'
				styleClass="black_ar"
				staticField="false" size="3" />	&nbsp;<bean:message key="eventparameters.timeinhours"/>&nbsp;&nbsp;
			<autocomplete:AutoCompleteTag property="timeInMinutes"
			   optionsList = '${requestScope.minutesList}'
			   initialValue='${fluidSpecimenReviewEventParametersForm.timeInMinutes}'
			   styleClass="black_ar"
			   staticField="false" size="3" />	                  
			   &nbsp;<bean:message key="eventparameters.timeinminutes"/></span></td>
                </tr>  

<!-- Mandar : For cellcount Start -->
                <tr>
                  <td align="center" class="black_ar">&nbsp;</td>
                  <td align="left" class="black_ar">
			<label for="cellCount">
			<bean:message key="fluidspecimenrevieweventparameters.cellcount"/> 
			</label></td>
                  <td align="left">
			<html:text styleClass="black_ar"  maxlength="10" size="30" styleId="cellCount" property="cellCount" />
		  </td>
                </tr>
<!-- Mandar : For procedure End -->
                <tr>
                  <td align="center" class="black_ar">&nbsp;</td>
                  <td align="left" valign="top" class="black_ar_t"><bean:message key="eventparameters.comments"/></td><td align="left" colspan="5"><html:textarea styleClass="black_ar" cols="73" rows="4" styleId="comments" property="comments" /></td>
                </tr>
          </table></td>
        </tr>
        <tr >
          <td class="buttonbg">
		  <html:submit styleClass="blue_ar_b" value="Submit" onclick='${requestScope.changeAction}' />
           	</td>
        </tr>
      </table></td>
  </tr>
  	 </html:form>
</table>