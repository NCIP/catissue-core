<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/nlevelcombo.tld" prefix="ncombo" %>
<%@ page import="edu.wustl.catissuecore.util.global.AppUtility"%>
<%@ page import="edu.wustl.catissuecore.util.global.Variables"%>
<%@ page import="edu.wustl.catissuecore.actionForm.TissueSpecimenReviewEventParametersForm"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.common.util.global.CommonServiceLocator"%>
<%@ include file="/pages/content/common/AutocompleterCommon.jsp" %>
<%@ page language="java" isELIgnored="false" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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

	<link rel="stylesheet" type="text/css" href="dhtmlxSuite_v35/dhtmlxCombo/codebase/dhtmlxcombo.css">
<link rel="stylesheet" type="text/css" href="dhtmlxSuite_v35/dhtmlxWindows/codebase/skins/dhtmlxwindows_dhx_skyblue.css">

<script src="dhtmlxSuite_v35/dhtmlxGrid/codebase/dhtmlxcommon.js"></script>
<script src="dhtmlxSuite_v35/dhtmlxCombo/codebase/dhtmlxcombo.js"></script>

</head>

<body>
<%@ include file="/pages/content/common/ActionErrors.jsp" %>

<table width="100%" border="0" cellpadding="3" cellspacing="0" class="whitetable_bg">
<html:form action='${requestScope.formName}'>

	<!-- NEW TISSUE_SPECIMEN_REVIEW_EVENT_PARAMETERS REGISTRATION BEGINS-->
	<tr>
	<td>

	<table summary="" cellpadding="3" cellspacing="0" border="0" width="100%">
			<html:hidden property="operation"/>
			<html:hidden property="id" />
			<html:hidden property="specimenId" value='${requestScope.specimenId}'/>
			<tr>

          <td align="left" class="tr_bg_blue1"><span class="blue_ar_b">
          	&nbsp;<bean:message key="eventparameters"/> &quot;<em><bean:message key="tissuepecimenreviewparameters"/></em>&quot;</span></td>
        </tr>
        <tr>
          <td colspan="4" class="showhide1"></td>
        </tr>
        <tr >

	<table width="100%" border="0" cellpadding="3" cellspacing="0">


		<!-- Name of the tissueSpecimenReviewEventParameters -->
			<!--  User -->
				<tr>
                  <td width="1%" align="center" class="black_ar"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory Field" width="6" height="6" hspace="0" vspace="0" /></td>
                  <td width="16%" align="left" nowrap class="black_ar"><bean:message key="eventparameters.user"/></td>
                <td width="30%" align="left" valign="middle" class="black_ar"><html:select property="userId" styleClass="formFieldSized18" styleId="userId" size="1"
				 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
					<html:options collection='${requestScope.userListforJSP}' labelProperty="name" property="value" />
				</html:select></td>
                </tr>

			 <!-- date Time -->
				<tr>
                  <td align="center" class="black_ar"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory Field" width="6" height="6" hspace="0" vspace="0" /></td>
                  <td align="left" class="black_ar"><bean:message key="eventparameters.dateofevent" /></td>
                  <td align="left"><logic:notEmpty name="currentEventParametersDate">
<ncombo:DateTimeComponent name="dateOfEvent"
			  id="dateOfEvent"
			  formName="tissueSpecimenReviewEventParametersForm"
			                  month='${requestScope.eventParametersMonth}'
							  year='${requestScope.eventParametersYear}'
							  day='${requestScope.eventParametersDay}'
							  pattern="<%=CommonServiceLocator.getInstance().getDatePattern()%>"
							  value='${requestScope.currentEventParametersDate}'
			  styleClass="black_ar"
					/>
</logic:notEmpty>
<logic:empty name="currentEventParametersDate">
<ncombo:DateTimeComponent name="dateOfEvent"
			  id="dateOfEvent"
			  formName="tissueSpecimenReviewEventParametersForm"
			  pattern="<%=CommonServiceLocator.getInstance().getDatePattern()%>"

			  styleClass="black_ar"
					/>
</logic:empty><span class="grey_ar_s capitalized">[${datePattern}]</span>&nbsp;</td>

                  <!-- Time -->
				  <td align="left" class="black_ar"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory Field" width="6" height="6" hspace="0" vspace="0" /></td>
                  <td class="black_ar" width="15%" ><bean:message key="eventparameters.time" /></td>
                  <td align="left">					
						<div style="width:100%"  class="black_ar"><div style="float:left;">
						<select id="timeInHours1" styleClass="black_ar" styleId="timeInHours" size="1"> 
						<logic:iterate id="hourListd" name="hourList">
								
									<option value="<bean:write name='hourListd'/>" selected><bean:write name='hourListd'/></option>
								
							</logic:iterate>
						<select></div><div style="float:left;">&nbsp;<bean:message key="eventparameters.timeinhours"/>&nbsp;&nbsp;
						</div><div style="float:left;">
						<select id="timeInMinutes1" styleClass="black_ar" styleId="timeInMinutes" size="1"> 
						<logic:iterate id="minutesId" name="minutesList">
								
									<option value="<bean:write name='minutesId'/>" selected><bean:write name='minutesId'/></option>
								
							</logic:iterate>
						</select></div><div>&nbsp;&nbsp;<bean:message key="eventparameters.timeinminutes"/>
						</div>
<html:hidden property="timeInHours" value='${tissueSpecimenReviewEventParametersForm.timeInHours}'/>
<html:hidden property="timeInMinutes"  value='${tissueSpecimenReviewEventParametersForm.timeInMinutes}'/>

						</div>
								<script>
							 window.dhx_globalImgPath="dhtmlxSuite_v35/dhtmlxWindows/codebase/imgs/";
							  var timeHr = new dhtmlXCombo("timeInHours1","timeInHours1","100px");
							  timeHr.setSize(60);
							  timeHr.enableFilteringMode(true);
							  if('${tissueSpecimenReviewEventParametersForm.timeInHours}'!=0){
								timeHr.setComboValue('${tissueSpecimenReviewEventParametersForm.timeInHours}');
							  }
							  timeHr.attachEvent("onChange", function(){
								document.getElementsByName("timeInHours")[0].value= timeHr.getSelectedValue();
							  });  

							   var timeMinute = new dhtmlXCombo("timeInMinutes1","timeInMinutes1","100px");
							  timeMinute.setSize(60);
							  timeMinute.enableFilteringMode(true);
							  if('${tissueSpecimenReviewEventParametersForm.timeInMinutes}'!=0){
								timeMinute.setComboValue('${tissueSpecimenReviewEventParametersForm.timeInMinutes}');
							  }
							  timeMinute.attachEvent("onChange", function(){
								document.getElementsByName("timeInMinutes")[0].value= timeMinute.getSelectedValue();
							  });  

						</script>	
	  
					
					</td>


                </tr>
             <!-- neoPlastic and Necrosis -->
                <tr>
                  <td align="center" class="black_ar">&nbsp;</td>
                  <td align="left" class="black_ar"><LABEL for="type"><bean:message key="tissuespecimenrevieweventparameters.neoplasticcellularitypercentage"/>  </LABEL></td>
                  <td align="left"><html:text styleClass="black_ar" maxlength="10"  size="30" styleId="neoplasticCellularityPercentage" property="neoplasticCellularityPercentage" />
				  </td>
                  <td align="center" class="black_ar">&nbsp;</td>
				  <td align="left" class="black_ar"><bean:message key="tissuespecimenrevieweventparameters.necrosispercentage"/>  </td>
                  <td align="left"><html:text styleClass="black_ar" maxlength="50"  size="30" styleId="necrosisPercentage" property="necrosisPercentage" /></td>
                </tr>

				<tr>
                  <td align="center" class="black_ar">&nbsp;</td>
                  <td align="left" class="black_ar"><LABEL for="type"><bean:message key="tissuespecimenrevieweventparameters.lymphocyticpercentage"/></LABEL></td>
                  <td align="left"><html:text styleClass="black_ar" maxlength="10"  size="30" styleId="lymphocyticPercentage" property="lymphocyticPercentage" />&nbsp;</td>
                  <td align="center" class="black_ar">&nbsp;</td>
				   <td align="left" class="black_ar"><LABEL for="type"><bean:message key="tissuespecimenrevieweventparameters.totalcellularitypercentage"/> </LABEL></td>
                  <td align="left"><html:text styleClass="black_ar" maxlength="10"  size="30" styleId="totalCellularityPercentage" property="totalCellularityPercentage" /></td>
                </tr>

				<tr>
                  <td align="center" class="black_ar">&nbsp;</td>
                  <td align="left" class="black_ar"><LABEL for="type"><bean:message key="tissuespecimenrevieweventparameters.histologicalquality"/> </LABEL></td>
                  <td align="left" class="black_ar">
					
					
					
					  <div style="width:100%"  class="black_ar"><div style="float:left;">
						<select id="histologicalQuality1" styleClass="black_ar" styleId="histologicalQuality1" size="1"> 
						<logic:iterate id="histologicalQuality" name="histologicalQualityList">
								
									<option value="<bean:write name='histologicalQuality'  property="value"/>"><bean:write name="histologicalQuality"  property="name"/></option>
								
							</logic:iterate>
						</select></div>
						<html:hidden property="histologicalQuality" value="${tissueSpecimenReviewEventParametersForm.histologicalQuality}"/>

						</div>
								<script>
							  var histologicalQuality = new dhtmlXCombo("histologicalQuality1","histologicalQuality1","100px");
							  histologicalQuality.setSize(225);
							  histologicalQuality.enableFilteringMode(true);
							  histologicalQuality.setComboValue("${tissueSpecimenReviewEventParametersForm.histologicalQuality}");
							  histologicalQuality.attachEvent("onChange", function(){
								document.getElementsByName("histologicalQuality")[0].value= histologicalQuality.getSelectedValue();
								
							  });  


						</script>	
	  	  
					
					
					
					
					</td>


                </tr>

                <tr>
                  <td align="center" class="black_ar">&nbsp;</td>
                  <td align="left" valign="top" class="black_ar_t"><bean:message key="eventparameters.comments"/> </td><td colspan="4" align="left"><html:textarea styleClass="black_ar" cols="73" rows="4"  styleId="comments" property="comments" /></td>
                </tr>

  </table>
  </tr>
   <tr>
          <td class="buttonbg">
		  <html:submit styleClass="blue_ar_b" value="Submit" accesskey="Enter" onclick='${requestScope.changeAction}' />
        </td>
    </tr>

 </html:form>
 </table>
 </body>