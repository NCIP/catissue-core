<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/nlevelcombo.tld" prefix="ncombo" %>
<%@ page import="edu.wustl.catissuecore.util.global.AppUtility"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.util.global.Variables"%>
<%@ page import="edu.wustl.common.util.global.CommonServiceLocator"%>
<%@ page import="edu.wustl.catissuecore.actionForm.MolecularSpecimenReviewParametersForm"%>
<%@ include file="/pages/content/common/AutocompleterCommon.jsp" %>
<%@ page language="java" isELIgnored="false" %>
<head>
<!-- Mandar : 434 : for tooltip -->
<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
	<script language="javascript">

	</script>
<!-- Mandar 21-Aug-06 : For calendar changes -->
<script src="jss/calendarComponent.js"></script>

	<link rel="stylesheet" type="text/css" href="dhtmlx_suite/css/dhtmlxcombo.css">
<link rel="stylesheet" type="text/css" href="dhtmlx_suite/skins/dhtmlxwindows_dhx_skyblue.css">

<script src="dhtmlx_suite/js/dhtmlxcommon.js"></script>
<script src="dhtmlx_suite/js/dhtmlxcombo.js"></script>
<SCRIPT>var imgsrc="images/";

function calculateAbsorbanceRatio()
{
	var absorbanceAt260 = document.getElementById('absorbanceAt260').value;
	var absorbanceAt280 = document.getElementById('absorbanceAt280').value;
	var ratio = absorbanceAt260/absorbanceAt280;
	ratio = roundOff(ratio, 5);
	if(isNaN(parseInt(ratio)))
	{
		if(!((document.getElementById('absorbanceAt260').value == "") || (document.getElementById('absorbanceAt280').value == "")))
		{
			alert('<bean:message key="molecularspecimenreviewparameters.absorbanceRatioOf260/280.validationMsg"/>');
		}
		document.getElementById('absorbanceRatioOf260/280').value  = "";
		return;
	}
	document.getElementById('absorbanceRatioOf260/280').value = ratio;
}

function roundOff(valueToBeRoundOff, precision)
{
	var multiplier = Math.pow(10,precision);
	return Math.round(valueToBeRoundOff * multiplier)/multiplier;
}


</SCRIPT>
<LINK href="css/calanderComponent.css" type=text/css rel=stylesheet>
<link href="css/catissue_suite.css" rel="stylesheet" type="text/css" />
<!-- Mandar 21-Aug-06 : calendar changes end -->

</head>
	<body onLoad = calculateAbsorbanceRatio();>

<%@ include file="/pages/content/common/ActionErrors.jsp" %>
<table width="100%" border="0" cellpadding="3" cellspacing="0" class="whitetable_bg">
<html:form action='${requestScope.formName}'>
<html:hidden property="operation" />
<html:hidden property="id" />
<html:hidden property="specimenId" value='${requestScope.specimenId}'/>
				<html:hidden property="isRNA" value='${requestScope.isRNA}'/>



        <tr>
          <td align="left" class="tr_bg_blue1"><span class="blue_ar_b">
          	&nbsp;<bean:message key="eventparameters"/> &quot;<em><bean:message key="molecularspecimenreviewparameters"/></em>&quot;</span></td>
        </tr>
        <tr>
          <td colspan="4" class="showhide1"></td>
        </tr>
<tr>
 <td>
  <table width="100%" border="0" cellpadding="3" cellspacing="0">


		<!-- Name of the molecularspecimenreviewparameters -->

				<tr>
                  <td width="1%" align="center" class="black_ar"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory Field" width="6" height="6" hspace="0" vspace="0" /></td>
                  <td width="15%" align="left" nowrap class="black_ar"><bean:message key="eventparameters.user"/></td>
                <td width="30%" align="left" valign="middle" class="black_ar"><html:select property="userId" styleClass="formFieldSized18" styleId="userId" size="1"
				 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
					<html:options collection='${requestScope.userListforJSP}' labelProperty="name" property="value"/>
				</html:select></td>
                </tr>

				<tr>
                  <td align="center" class="black_ar"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory Field" width="6" height="6" hspace="0" vspace="0" /></td>
                  <td align="left" class="black_ar"><bean:message key="eventparameters.dateofevent" /></td>
                  <td align="left" class="black_ar"><logic:notEmpty name="currentEventParametersDate">
<ncombo:DateTimeComponent name="dateOfEvent"
			  id="dateOfEvent"
			  formName="molecularSpecimenReviewParametersForm"
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
			  formName="molecularSpecimenReviewParametersForm"
			  pattern="<%=CommonServiceLocator.getInstance().getDatePattern()%>"
			  styleClass="formDateSized10"
					/>
</logic:empty><span class="grey_ar_s capitalized">[<bean:message key="date.pattern" />]</span>&nbsp;</td>

                  <!-- Time -->

				  <td align="left" class="black_ar"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory Field" width="6" height="6" hspace="0" vspace="0" /></td>
                  <td align="left" class="black_ar" width="15%"><bean:message key="eventparameters.time" /></td>
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
<html:hidden property="timeInHours" value='${molecularSpecimenReviewParametersForm.timeInHours}'/>
<html:hidden property="timeInMinutes"  value='${molecularSpecimenReviewParametersForm.timeInMinutes}'/>

						</div>
								<script>
							 window.dhx_globalImgPath="dhtmlx_suite/imgs/";
							  var timeHr = new dhtmlXCombo("timeInHours1","timeInHours1","100px");
							  timeHr.setSize(60);
							  timeHr.enableFilteringMode(true);
							  if('${molecularSpecimenReviewParametersForm.timeInHours}'!=0){
								timeHr.setComboValue('${molecularSpecimenReviewParametersForm.timeInHours}');
							  }
							  timeHr.attachEvent("onChange", function(){
								document.getElementsByName("timeInHours")[0].value= timeHr.getSelectedValue();
							  });  

							   var timeMinute = new dhtmlXCombo("timeInMinutes1","timeInMinutes1","100px");
							  timeMinute.setSize(60);
							  timeMinute.enableFilteringMode(true);
							  if('${molecularSpecimenReviewParametersForm.timeInMinutes}'!=0){
								timeMinute.setComboValue('${molecularSpecimenReviewParametersForm.timeInMinutes}');
							  }
							  timeMinute.attachEvent("onChange", function(){
								document.getElementsByName("timeInMinutes")[0].value= timeMinute.getSelectedValue();
							  });  

						</script>	
	  
					
					
					</td>


                </tr>

       <!-- GelUrl and QualityIndex -->

                <tr>
                  <td align="center" class="black_ar">&nbsp;</td>
                  <td align="left" class="black_ar"><LABEL for="molecularspecimenreviewparameters.gelimageurl"><bean:message key="molecularspecimenreviewparameters.gelimageurl"/>  </LABEL></td>
                  <td align="left"><html:text styleClass="black_ar" maxlength="255"  size="30" styleId="gelImageURL" property="gelImageURL" /></td>
                  <td align="left">&nbsp;</td>
                   <td align="left" class="black_ar"><LABEL for="molecularspecimenreviewparameters.qualityindex"><bean:message key="molecularspecimenreviewparameters.qualityindex" /> </LABEL></td>
                  <td align="left"><html:text styleClass="black_ar" maxlength="50"  size="30" styleId="qualityIndex" property="qualityIndex" /></td>

                </tr>

				<tr>
				  <td align="center" class="black_ar">&nbsp;</td>
				  <td align="left" class="black_ar"><bean:message key="molecularspecimenreviewparameters.gelnumber"/>  </td>
                  <td align="left"><html:text styleClass="black_ar" maxlength="10"  size="30" styleId="gelNumber" property="gelNumber" /></td>
				  <td align="left">&nbsp;</td>

				  <td align="left" class="black_ar"><bean:message key="molecularspecimenreviewparameters.lanenumber"/>  </td>
                  <td align="left"><html:text styleClass="black_ar" maxlength="50"  size="30" styleId="laneNumber" property="laneNumber" /></td>
				  <td align="left"></td>

				</tr>

				<tr>
				  <td align="center" class="black_ar">&nbsp;</td>
				  <td align="left" class="black_ar"><bean:message key="molecularspecimenreviewparameters.absorbanceat260"/> </td>
                  <td align="left"><html:text styleClass="black_ar" maxlength="10"  size="30" styleId="absorbanceAt260" property="absorbanceAt260" onblur="calculateAbsorbanceRatio();"/></td>
                  <td align="left">&nbsp;</td>
				  <td align="left" class="black_ar"><bean:message key="molecularspecimenreviewparameters.absorbanceat280"/> </td>
                  <td align="left"><html:text styleClass="black_ar" maxlength="10"  size="30" styleId="absorbanceAt280" property="absorbanceAt280" onblur="calculateAbsorbanceRatio();"/></td>

				</tr>




				 <tr>
				  <td align="center" class="black_ar">&nbsp;</td>
				  <td align="left" class="black_ar"><bean:message key="molecularspecimenreviewparameters.absorbanceRatioOf260/280"/></td>
                  <td align="left">
					<input type="text" class="black_ar_disabled" maxlength="10"  size="30" id="absorbanceRatioOf260/280" readOnly> </input>
				  </td>
                  <td align="left">&nbsp;</td>
				  <td align="left" class="black_ar"><LABEL for="molecularspecimenreviewparameters.ratio28STo18S"><bean:message key="molecularspecimenreviewparameters.ratio28STo18S"/> </LABEL></td>
                  <td align="left"><html:text styleClass="black_ar" maxlength="10"  size="30" styleId="ratio28STo18S" property="ratio28STo18S" /></td>
                </tr>



                <tr>
                  <td align="center" class="black_ar">&nbsp;</td>
                  <td align="left" valign="top" class="black_ar_t"><bean:message key="eventparameters.comments"/> </td><td colspan="4" align="left"><html:textarea styleClass="black_ar" cols="73" rows="4"  styleId="comments" property="comments" /></td>
                </tr>

 </td>
  </tr>
  </table>
   <tr>
          <td class="buttonbg">
		  <html:submit styleClass="blue_ar_b" value="Submit" accesskey="Enter" onclick='${requestScope.changeAction}' />
          </td>
    </tr>


		   </html:form>
		  </table></body>