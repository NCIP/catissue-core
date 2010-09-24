<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/nlevelcombo.tld" prefix="ncombo" %>
<%@ page import="edu.wustl.catissuecore.util.global.AppUtility"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.util.global.Variables"%>
<%@ page import="edu.wustl.common.util.global.CommonServiceLocator"%>
<%@ include file="/pages/content/common/AutocompleterCommon.jsp" %>

<link href="css/catissue_suite.css" rel="stylesheet" type="text/css" />

<table width="100%" border="0" cellpadding="0" cellspacing="0" class="maintable">
 <html:hidden property="operation" />
  <tr>
    <td class="td_color_bfdcf3"><table border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td class="td_table_head"><span class="wh_ar_b"><bean:message key="app.shoppingCart"/> </span></td>
        <td><img src="images/uIEnhancementImages/table_title_corner2.gif" alt="Page Title - My List" width="31" height="24" /></td>
      </tr>
    </table></td>
  </tr>
  <tr>
    <td class="tablepadding"><table width="100%" border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td width="90%" valign="bottom" class="td_tab_bg">&nbsp;</td>
      </tr>
    </table>
      <table width="100%" border="0" cellpadding="3" cellspacing="0" class="whitetable_bg">

      <tr>
        <td width="1%"  align="left" class="bottomtd">
        	<%@ include file="/pages/content/common/ActionErrors.jsp" %>
		</td>
      </tr>
      <tr>
        <td  align="left" class="tr_bg_blue1"><span class="blue_ar_b"> &nbsp;<logic:equal name="bulkEventOperationsForm" property="operation" value="<%=Constants.BULK_TRANSFERS%>">
								<bean:message key="bulk.events.operation"/>
							</logic:equal>
							<logic:equal name="bulkEventOperationsForm" property="operation" value="<%=Constants.BULK_DISPOSALS%>">
								<bean:message key="bulk.events.disposals"/>
							</logic:equal></span></td>
      </tr>

      <tr>
        <td  class="showhide"><table width="100%" border="0" cellpadding="3" cellspacing="0">
            <tr>
              <td width="1%" align="center" class="black_ar"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory Field" width="6" height="6" hspace="0" vspace="0" /></td>
              <td width="15%" align="left" nowrap class="black_ar"><bean:message key="eventparameters.user"/> </td>
              <td width="84%" align="left" valign="middle"><html:select property="userId" styleClass="formFieldSizedNew" styleId="userId" size="1" ><html:options collection="<%=Constants.USERLIST%>" labelProperty="name" property="value"/></html:select> </td>
			 </tr>
            <tr>
              <td align="center" class="black_ar"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory Field" width="6" height="6" hspace="0" vspace="0" /></td>
              <td align="left" class="black_ar"><bean:message key="eventparameters.dateofevent"/> </td>
              <td align="left"><bean:define id="eventDate" name="bulkEventOperationsForm" type="java.lang.String" property="dateOfEvent"/>
							<ncombo:DateTimeComponent name="dateOfEvent"
							  id="dateOfEvent"
							  formName="bulkEventOperationsForm"
							  month= "<%=AppUtility.getMonth(eventDate)%>"
							  year= "<%=AppUtility.getYear(eventDate)%>"
							  day= "<%=AppUtility.getDay(eventDate)%>"
    		  			      pattern="<%=CommonServiceLocator.getInstance().getDatePattern()%>"
    		  			      value="<%= eventDate %>"
							  styleClass="black_ar"
							/><span class="grey_ar_s">
                   <bean:message key="page.dateFormat" />&nbsp</span></td>
				</tr>
            <tr>
              <td align="center" class="black_ar"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory Field" width="6" height="6" hspace="0" vspace="0" /></td>
              <td align="left" class="black_ar"><bean:message key="eventparameters.time"/></td>
              <td align="left"><span class="black_ar">
			  <bean:define id="hours" name="bulkEventOperationsForm" type="java.lang.String" property="timeInHours"/>
			  <bean:define id="minutes" name="bulkEventOperationsForm" type="java.lang.String" property="timeInMinutes"/>
        		<autocomplete:AutoCompleteTag property="timeInHours"
								  optionsList = "<%=request.getAttribute(Constants.HOUR_LIST)%>"
								  initialValue="<%=hours%>"
								  styleClass="black_ar"
								  size="4"
								  staticField="false"
						    	/>&nbsp;<bean:message key="eventparameters.timeinhours"/>&nbsp;&nbsp;
               <autocomplete:AutoCompleteTag property="timeInMinutes"
									  optionsList = "<%=request.getAttribute(Constants.MINUTES_LIST)%>"
									  initialValue="<%=minutes%>"
									  styleClass="black_ar"
									  size="4"
									  staticField="false"
							   />	&nbsp;<bean:message key="eventparameters.timeinminutes"/> </span></td>
            </tr>


            <tr>
              <td align="center" class="black_ar">&nbsp;</td>
              <td align="left" valign="top" class="black_ar_t"><bean:message key="eventparameters.comments"/></td>
              <td align="left" valign="top" class="black_ar_t"><html:textarea styleClass="black_ar" cols="70" rows="3"  styleId="comments" property="comments"/></td>
            </tr>




