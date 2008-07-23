<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/nlevelcombo.tld" prefix="ncombo" %>
<%@ page import="edu.wustl.catissuecore.util.global.Utility"%>
<%@ page import="edu.wustl.catissuecore.actionForm.MolecularSpecimenReviewParametersForm"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
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
			
<html:errors/>
    
<table border="0" cellpadding="0" cellspacing="0" width="100%"> 
<html:form action='${requestScope.formName}'>
	<html:hidden property="operation" />
	<html:hidden property="id" />
	<html:hidden property="specimenId" value='${requestScope.specimenId}'/>   
<tr>
          <td align="left" class="tr_bg_blue1"><span class="blue_ar_b">&nbsp;Event Parameters &quot;<em>Molecular Specimen Review</em>&quot;</span></td>
        </tr>
        <tr>
          <td colspan="4" class="showhide1"></td>
        </tr>
        <tr>
          <td colspan="4" class="showhide"><table width="100%" border="0" cellpadding="3" cellspacing="0">
                <tr>
                  <td width="1%" align="center" class="black_ar"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory Field" width="6" height="6" hspace="0" vspace="0" /></td>
                  <td width="15%" align="left" nowrap class="black_ar"><bean:message key="eventparameters.user"/></td>
	          <td align="left" valign="middle">
			<html:select property="userId" styleClass="formFieldSized18" styleId="userId" size="1"
				onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
				<html:options collection='${requestScope.userListforJSP}' labelProperty="name" property="value"/>
			</html:select>
		  </td>
		</tr>
                <tr>
                  <td align="center" class="black_ar"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory Field" width="6" height="6" hspace="0" vspace="0" /></td>
                  <td align="left" class="black_ar"><bean:message key="eventparameters.dateofevent"/></td>
                  <td align="left">
			  <logic:notEmpty name="currentEventParametersDate">
				<ncombo:DateTimeComponent name="dateOfEvent"
					 id="dateOfEvent"
					 formName="molecularSpecimenReviewParametersForm"
			                 month='${requestScope.eventParametersMonth}'
					 year='${requestScope.eventParametersYear}'
					 day='${requestScope.eventParametersDay}'
					 value='${requestScope.currentEventParametersDate}'
					 styleClass="black_ar" />
			  </logic:notEmpty>
			  <logic:empty name="currentEventParametersDate">
				<ncombo:DateTimeComponent name="dateOfEvent"
					  id="dateOfEvent"
					  formName="molecularSpecimenReviewParametersForm"
					  styleClass="black_ar"	/>
			  </logic:empty>
			  <span class="grey_ar_s"><bean:message key="page.dateFormat" /></span></td>
                </tr>
                <tr>
                  <td align="center" class="black_ar"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory Field" width="6" height="6" hspace="0" vspace="0" /></td>
                  <td align="left" class="black_ar"><bean:message key="eventparameters.time"/></td>
                  <td align="left"><span class="black_ar">
			<autocomplete:AutoCompleteTag property="timeInHours"
				optionsList = '${requestScope.hourList}'
				initialValue='${molecularSpecimenReviewParametersForm.timeInHours}'
				styleClass="black_ar"
				staticField="false" size="3" />	&nbsp;<bean:message key="eventparameters.timeinhours"/>&nbsp;&nbsp;
			<autocomplete:AutoCompleteTag property="timeInMinutes"
			   optionsList = '${requestScope.minutesList}'
		  initialValue='${molecularSpecimenReviewParametersForm.timeInMinutes}'
			   styleClass="black_ar"
			   staticField="false" size="3" />	                  
			   &nbsp;<bean:message key="eventparameters.timeinminutes"/></span></td>
                </tr>  

<!-- Mandar : For gelImageURL Start -->
                <tr>
                  <td class="black_ar" align="center">&nbsp;</td>
                  <td class="black_ar" align="left" valign="top">
		    <label for="molecularspecimenreviewparameters.gelimageurl">
			<bean:message key="molecularspecimenreviewparameters.gelimageurl"/> 
		    </label>
		  </td>
                  <td align="left">
		  <html:text styleClass="black_ar" maxlength="250"  size="34" styleId="gelImageURL" property="gelImageURL" />
		  </td>
                </tr>
 <!-- Mandar : For gelImageURL End -->
<!-- Mandar : For qualityIndex Start -->
                <tr>
                  <td class="black_ar" align="center">&nbsp;</td>
                  <td class="black_ar" align="left" valign="top">
			<label for="molecularspecimenreviewparameters.qualityindex">
			<bean:message key="molecularspecimenreviewparameters.qualityindex"/> 
			</label>
		  </td>
                  <td align="left">
		  <html:text styleClass="black_ar" maxlength="50"  size="34" styleId="qualityIndex" property="qualityIndex" />
		  </td>
                </tr>
 <!-- Mandar : For qualityIndex End -->
<!-- Mandar : For laneNumber Start -->
                <tr>
                  <td class="black_ar" align="center">&nbsp;</td>
                  <td class="black_ar" align="left" valign="top">
			<label for="molecularspecimenreviewparameters.lanenumber">
				<bean:message key="molecularspecimenreviewparameters.lanenumber"/> 
			</label></td>
                  <td align="left">
		  <html:text styleClass="black_ar" maxlength="50"  size="34" styleId="laneNumber" property="laneNumber" />
		  </td>
                </tr>
 <!-- Mandar : For laneNumber End -->
<!-- Mandar : For gelNumber Start -->
                <tr>
                  <td class="black_ar" align="center">&nbsp;</td>
                  <td class="black_ar" align="left" valign="top">
			<label for="molecularspecimenreviewparameters.gelnumber">
				<bean:message key="molecularspecimenreviewparameters.gelnumber"/> 
			</label></td>
                  <td align="left">
		  <html:text styleClass="black_ar" maxlength="10"  size="34" styleId="gelNumber" property="gelNumber" />
		  </td>
                </tr>
 <!-- Mandar : For gelNumber End -->
<!-- Mandar : For absorbanceAt260 Start -->
                <tr>
                  <td class="black_ar" align="center">&nbsp;</td>
                  <td class="black_ar" align="left" valign="top">
			<label for="molecularspecimenreviewparameters.absorbanceat260">
			<bean:message key="molecularspecimenreviewparameters.absorbanceat260"/> 
			</label></td>
                  <td align="left">
		  <html:text styleClass="black_ar" maxlength="10"  size="34" styleId="absorbanceAt260" property="absorbanceAt260" />
		  </td>
                </tr>
 <!-- Mandar : For absorbanceAt260 End -->
<!-- Mandar : For absorbanceAt280 Start -->
                <tr>
                  <td class="black_ar" align="center">&nbsp;</td>
                  <td class="black_ar" align="left" valign="top" width="25%">
			<label for="molecularspecimenreviewparameters.absorbanceat280">
				<bean:message key="molecularspecimenreviewparameters.absorbanceat280"/> 
			</label></td>
                  <td align="left">
		  <html:text styleClass="black_ar" maxlength="10"  size="34" styleId="absorbanceAt280" property="absorbanceAt280" />
		  </td>
                </tr>
 <!-- Mandar : For absorbanceAt280 End -->
<!-- Mandar : For ratio28STo18S Start -->
<logic:equal name="molecularSpecimenReviewParametersForm" property="checkRNA" value="true">
                <tr>
                  <td class="black_ar" align="center">&nbsp;</td>
                  <td class="black_ar" align="left" valign="top">
			<label for="molecularspecimenreviewparameters.ratio28STo18S">
			<bean:message key="molecularspecimenreviewparameters.ratio28STo18S"/> 
			</label></td>
                  <td align="left">
		  <html:text styleClass="black_ar" maxlength="10"  size="34" styleId="ratio28STo18S" property="ratio28STo18S" />
		  </td>
                </tr>
</logic:equal>
 <!-- Mandar : For ratio28STo18S End -->
                <tr>
                  <td align="center" class="black_ar">&nbsp;</td>
                  <td align="left" valign="top" class="black_ar"><bean:message key="eventparameters.comments"/></td><td align="left"><html:textarea styleClass="black_ar" cols="35" rows="4" styleId="comments" property="comments" /></td>
                </tr>
          </table></td>
        </tr>
        <tr >
          <td class="buttonbg">
		  <html:submit styleClass="blue_ar_b" value="Submit" onclick='${requestScope.changeAction}' />
            &nbsp;|&nbsp;
			<html:link	page="/QueryManageBioSpecimen.do" styleClass="cancellink">
				<bean:message key="buttons.cancel" />
			</html:link>
			</td>
        </tr>
      </table></td>
  </tr>
  	 </html:form>
</table>