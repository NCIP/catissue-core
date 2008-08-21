<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/nlevelcombo.tld" prefix="ncombo" %>
<%@ page import="edu.wustl.catissuecore.util.global.Utility"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
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
<SCRIPT>var imgsrc="images/";</SCRIPT>
<LINK href="css/calanderComponent.css" type=text/css rel=stylesheet>
<link href="css/catissue_suite.css" rel="stylesheet" type="text/css" />
<!-- Mandar 21-Aug-06 : calendar changes end -->

</head>
	<body>
		
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
							  value='${requestScope.currentEventParametersDate}'
			  styleClass="black_ar"
					/>
</logic:notEmpty>
<logic:empty name="currentEventParametersDate">
<ncombo:DateTimeComponent name="dateOfEvent"
			  id="dateOfEvent"
			  formName="molecularSpecimenReviewParametersForm"
			  styleClass="formDateSized10"
					/>
</logic:empty><span class="grey_ar_s">
                   <bean:message key="page.dateFormat" />&nbsp</span></td>
                  
                  <!-- Time -->
				  
				  <td align="left" class="black_ar"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory Field" width="6" height="6" hspace="0" vspace="0" /></td>
                  <td align="left" class="black_ar" width="15%"><bean:message key="eventparameters.time" /></td>
                  <td align="left"><span class="black_ar"><autocomplete:AutoCompleteTag property="timeInHours"
					  optionsList = '${requestScope.hourList}'
					   initialValue='${molecularSpecimenReviewParametersForm.timeInHours}'
					  styleClass="black_ar"
					  staticField="false"
					  size="3"
			    />&nbsp;<bean:message key="eventparameters.timeinhours" />&nbsp;&nbsp;
                     <autocomplete:AutoCompleteTag property="timeInMinutes"
						  optionsList = '${requestScope.minutesList}'
						  initialValue='${molecularSpecimenReviewParametersForm.timeInMinutes}'
						  styleClass="black_ar"
						  staticField="false"
						  size="3"
				    />	                  &nbsp;<bean:message key="eventparameters.timeinminutes" /></span></td>
                  
				  
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
                  <td align="left"><html:text styleClass="black_ar" maxlength="10"  size="30" styleId="absorbanceAt260" property="absorbanceAt260" /></td>
                  <td align="left">&nbsp;</td>
				  <td align="left" class="black_ar"><bean:message key="molecularspecimenreviewparameters.absorbanceat280"/> </td>
                  <td align="left"><html:text styleClass="black_ar" maxlength="10"  size="30" styleId="absorbanceAt280" property="absorbanceAt280" /></td>
				
				</tr>
				
				
				
				
				 <tr>
                  <td align="center" class="black_ar">&nbsp;</td>
				  <td align="left" class="black_ar"><LABEL for="molecularspecimenreviewparameters.ratio28STo18S"><bean:message key="molecularspecimenreviewparameters.ratio28STo18S"/> </LABEL></td>
                  <td align="left"><html:text styleClass="black_ar" maxlength="10"  size="30" styleId="ratio28STo18S" property="ratio28STo18S" /></td>
				
                  <td align="left">&nbsp;</td>
                  <td align="left">&nbsp;</td>
                  <td align="left">&nbsp;</td>
                </tr>
                

                
                <tr>
                  <td align="center" class="black_ar">&nbsp;</td>
                  <td align="left" valign="top" class="black_ar"><bean:message key="eventparameters.comments"/> </td><td colspan="4" align="left"><html:textarea styleClass="black_ar" cols="73" rows="4"  styleId="comments" property="comments" /></td>
                </tr>
               
 </td>
  </tr>
  </table>
   <tr>
          <td class="buttonbg">
		  <html:submit styleClass="blue_ar_b" value="Submit" accesskey="Enter" onclick='${requestScope.changeAction}' />
            &nbsp;|&nbsp;<html:link  page="/QueryManageBioSpecimen.do" styleClass="cancellink">
                        <bean:message key="buttons.cancel" />
                  </html:link></td>
    </tr>

		 
		   </html:form>
		  </table></body>