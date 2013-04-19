<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<script	src="dhtmlx_suite/js/dhtmlxcommon.js"></script>
<script	src="dhtmlx_suite/js/dhtmlxcombo.js"></script>
<script	src="dhtmlx_suite/ext/dhtmlxcombo_extra.js"></script>
<script language="JavaScript" type="text/javascript" src="jss/ajax.js"></script>
<script language="JavaScript" type="text/javascript" src="jss/participantView.js"></script>
<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>

<script>
      window.dhx_globalImgPath="dhtmlx_suite/imgs/";
     var scgEvenData = ${requestScope.eventPointData}; 
</script>
<html>
<head>
<LINK type=text/css rel=stylesheet href="css/participantEffects.css" />
<link rel="stylesheet" type="text/css" href="dhtmlx_suite/css/dhtmlxcombo.css" />
</head>
<body onload="initComboForSCGEvents()">

<input type="hidden" name="requestFrom" value="participantView" />
<input type="hidden" name="CPQuery" value="CPQuery" />
<input type="hidden" name="pId" id="pId" value="${participantDto.participantId}" />
<input type="hidden" name="cpId" id="cpId" value="${participantDto.cpId}" />

<table width="100%" border="0"  cellpadding="10" cellspacing="0" class="whitetable_bg">	
	<tr class="tr_bg_blue1 blue_ar_b">
			<td  class="heading_text_style">
				<bean:message key="participant.view.profile.summary"/> <bean:write name="participantDto" property="lastName" />,
				<bean:write name="participantDto" property="firstName" />
				[<bean:write name="participantDto" property="cpId" />_<bean:write name="participantDto" property="participantId" />]
				 
			</td>
	</tr>
	<tr>
		<td class="bottomtd"></td>
	</tr>
</table>

<div id="participantDetails" class="align_left_style">
<fieldset class="field_set"> 
  <legend class="blue_ar_b legend_font_size"> <bean:message key="participant.view.participant.details"/></legend>
	<table width="100%" border="0"  cellpadding="5px" cellspacing="0"	class="whitetable_bg">
		<tr>
			<td  align="right" class="black_ar  padding_right_style" width="20%">
				<bean:message key="participant.view.birth.date"/> 
			</td>
			<td class="black_ar" width="20%">
				<fmt:formatDate value="${participantDto.dob}" pattern="${datePattern}" />
			</td>
			<td align="right" class="black_ar padding_right_style" width="30%"> 
				<bean:message key="participant.view.MRN"/></td>
			<td class="black_ar" width="30%"> 
                  <logic:iterate id="partMrn" name="participantDto"  property="mrns" indexId="indx">
<c:if test="${indx > 0}">
                      ,
                   </c:if>
                    <bean:write name="partMrn" property="mrn" /> ( <bean:write name="partMrn" property="siteName" /> )
                 </logic:iterate>  
	   </tr>
	   
	   <tr class="tr_alternate_color_lightGrey">
		 <td  align="right" class="black_ar bottomtd  padding_right_style" width="20%"> 
			<bean:message key="participant.view.registration.date"/>
		 </td> 
		 <td class="black_ar bottomtd" width="20%">
				<fmt:formatDate value="${participantDto.registrationDate}" pattern="${datePattern}" />
		</td>
		<td  align="right" class="black_ar bottomtd  padding_right_style" width="30%"> 
			<bean:message key="participant.view.isConsented"/>
		</td>
		<td class="black_ar bottomtd" width="30%"> 
			<bean:write name="participantDto" property="isConsented" />
		</td>
	  </tr>
	
	<tr>
		<td  width="20%" align="right" class="black_ar padding_right_style">
			<bean:message key="participant.gender"/>
		</td>
		<td  width="20%" class="black_ar"> <bean:write name="participantDto" property="gender" /></td>
		<td  width="30%" align="right" class="black_ar padding_right_style"><bean:message key="participant.race"/></td>
		<td  width="30%" class="black_ar" > <logic:iterate id="prace" name="participantDto"  property="race" indexId="indx">
<c:if test="${indx > 0}">
                      ,
                   </c:if>
                    <bean:write name="prace" /> 
                 </logic:iterate>   </td>
	</tr>
	
	<tr class="tr_alternate_color_lightGrey">
			<td width="20%" align="right" class="black_ar padding_right_style">
				<html:button  styleClass="blue_ar_b"	property="editParticipant"	title="Edit Only" value="Edit"  onclick="showEditPage()">
				</html:button>
			</td>
			<td width="20%">
			<!--	<html:button styleClass="blue_ar_b"	property="deleteParticipant"	title="Delete Only" value="Delete" >
				</html:button> -->
				
			</td>
			<td colspan="2"> 
			</td>
	</tr>
	</table>
</fieldset>
</div>
<p></p>


	<div id="specimen&SCGDetails" class="align_left_style">
	<fieldset class="field_set"> 	
     
	<table width="100%" border="0"  cellpadding="5px" cellspacing="0" class="whitetable_bg">
	
		<tr class="tr_bg_blue1 blue_ar_b">
			<td  class="heading_text_style">
			    <bean:message key="participant.view.scg.actions"/>
			</td> <td colspan="3"/>
	  </tr>	
	    	<tr>	
			<td align="right" width="20%" class="black_ar padding_right_style"> 
				<bean:message key="participant.view.select.event.point"/>
		        </td>
			<td width="35%">
				<div id="addSCGEvents"> </div>
			</td>
			<td width="35%">	
			 <span style="vertical-align:bottom">
				<a href="#" title="Add" style="padding-right:7px;"><img src="images/Action-add.png" alt="Create SCG" onclick="addNewScg()"></a>
				<a href="#" title="Edit" style="padding-right:7px;"> <img src="images/Action-edit.png" alt="Edit" onclick="editScg()"></a>
					<html:button styleClass="blue_ar_b" property="collectSpecimenPerCP"	title="Collect Specimens  As Per CP" value="Collect Specimens" onclick="collectSpecimen();">
				    </html:button>
				<!-- <a href="#" title="View"> <img src="images/Action-view.png" alt="View" onclick="scgOperation('view')"></a> &nbsp;
				<a href="#" title="Delete"><img src="images/Action-close.png" alt="Delete" onclick="scgOperation('delete')"></a> &nbsp;
				<a href="#" title="Print"><img src="images/Action-print.png" alt="Print" onclick="scgOperation('print')"></a> -->
			 </span>
			</td>
</td> <td width="20%"></td>
</tr>
			<!-- <td align="left" width="*">
				 <html:button styleClass="blue_ar_b"	property="collectSpecimenPerCP"	title="Collect Specimens  As Per CP" value="Collect Specimens" onclick="collectSpecimen();">
				 </html:button>
			</td> -->
 <!--	<tr height="33px" class="tr_alternate_color_lightGrey">
		<td width="25%" class="black_ar  padding_right_style" align="right">
					<bean:message key="participant.view.add.specimen"/>
		</td>
		<td width="25%">
			<input type="text" name="createSpecimen" id="createSpecimen" size ="20" value="Count" class="text_box_style"/>
		</td>	
		<td width="25%">
			 	<html:button styleClass="blue_ar_b"	property="createAdditionalSpecimen"	title="Create Additional Specimen" value="Go">
				</html:button> 
		</td> <td width="25%"></td>
	</tr> -->

<tr> <td colspan="4"> </td> </tr>
	<tr class="tr_bg_blue1 blue_ar_b">
		<td  class="heading_text_style" width="30%">
			     <bean:message key="participant.view.specimen.actions"/>
			</td> <td colspan="3"/>
	 </tr>		
	<tr>	
		<td class="black_ar  padding_right_style" width="20%" align="right"> 
					 <bean:message key="participant.view.select.specimen"/>
		</td>
		<td width="35%">
				<div id="specimenLabels"></div>
		</td>
		<td width="25%">		
			<span style="vertical-align:bottom">
				<a href="#" title="Edit"> <img src="images/Action-edit.png" alt="Edit" onclick="editSpecimen()"></a>&nbsp;
				<!-- <a href="#" title="View"> <img src="images/Action-view.png" alt="View"></a> &nbsp;
				<a href="#" title="Delete"><img src="images/Action-close.png" alt="Delete"></a> &nbsp;
				<a href="#" title="Print"><img src="images/Action-print.png" alt="Print"></a> -->
			 </span>
		</td> <td width="20%"></td>
	</tr>

	<tr width="100%" class="tr_alternate_color_lightGrey">
		<td width="20%"  align="right" class="black_ar  padding_right_style">
					 <bean:message key="participant.view.create.aliquot"/>
		</td>
		<td width="25%" >
			<input type="text" name="noOfAliquots" id="noOfAliquots" size ="5" value="Count"  class="text_box_style" onfocus="inputFocus(this)" onblur="inputBlur(this)"/>
						
			<input type="text" name="quantityPerAliquot" id="quantityPerAliquot" size ="5" value="Qty" class="text_box_style" onfocus="inputFocus(this)" onblur="inputBlur(this)"/>
		</td>
		<td width="25%" >
				<html:button  styleClass="blue_ar_b" property="aliquot" title="Aliqut" value=" Go " onclick="createAliquote()">
				</html:button>
				
		</td> <td width="25%" ></td>
	</tr>

	<tr>
		<td width="20%"  class="black_ar padding_right_style" align="right" >
					 <bean:message key="participant.view.create.derivative"/>
		</td>
		<td width="25%">
		     <input type="text" name="count" id="derivative_count" size ="5" value="Count" class="text_box_style" onfocus="inputFocus(this)" onblur="inputBlur(this)"/>
		</td>
		<td width="25%">
				<html:button  styleClass="blue_ar_b" property="derivative" title="derivative" onclick="createDerivative()" value=" Go ">
				</html:button>
		</td>
		<td width="25%" ></td>
	</tr>

<!--	<tr height="33px" class="tr_alternate_color_lightGrey">
		<td class="black_ar padding_right_style" width="25%" align="right">
					<bean:message key="participant.view.add.event"/>  
		</td>
		<td width="25%">	
				<div class="black_ar" id="addSpecimenEvents"></div>
		</td>
		<td width="25%" >
			 <html:button  styleClass="blue_ar_b" property="addEvent" title="Add Event" value=" Go ">
			 </html:button>
		</td>
		<td width="25%"> </td>
	</tr> -->
</table>
</fieldset>
</div>
</body>
</html>