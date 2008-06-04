<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="java.util.*" %>
<%@ page import="edu.wustl.catissuecore.actionForm.DistributionReportForm" %>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.actionForm.ConfigureResultViewForm"%>
<%@ page import="edu.wustl.catissuecore.util.global.Variables"%>
<%@ page import="edu.wustl.common.util.SendFile"%>
<%
        List dataList = (List)request.getAttribute(Constants.DISTRIBUTED_ITEMS_DATA);
        String []columnNames = (String []) request.getAttribute(Constants.COLUMN_NAMES_LIST);
        DistributionReportForm distForm = (DistributionReportForm)request.getAttribute(Constants.DISTRIBUTION_REPORT_FORM);
		ConfigureResultViewForm form = (ConfigureResultViewForm)request.getAttribute("configureResultViewForm");
		String []selectedColumns=form.getSelectedColumnNames();
		String reportSaveAction = "";
		String pageOf = (String) request.getAttribute(Constants.PAGEOF);
		
		if(distForm.getDistributionType().intValue() == Constants.SPECIMEN_DISTRIBUTION_TYPE) {
			reportSaveAction = Constants.DISTRIBUTION_REPORT_SAVE_ACTION;
			if(pageOf != null && pageOf.equals(Constants.PAGE_OF_DISTRIBUTION_CP_QUERY))
			{
				reportSaveAction = Constants.CP_QUERY_DISTRIBUTION_REPORT_SAVE_ACTION ;
			}

		} else {

			reportSaveAction = Constants.ARRAY_DISTRIBUTION_REPORT_SAVE_ACTION;
			if(pageOf != null && pageOf.equals(Constants.PAGE_OF_DISTRIBUTION_CP_QUERY))
			{
				reportSaveAction = Constants.CP_QUERY_ARRAY_DISTRIBUTION_REPORT_SAVE_ACTION ;
			}

		}
		
%> 
<script language="JavaScript">
	function changeAction()
	{
		document.forms[0].reportAction.value="false";
		selectOptions(document.forms[0].selectedColumnNames);
		setFormAction("<%=reportSaveAction%>");
   }
	
	function changeActionOnConfig()
	{
		document.forms[0].reportAction.value="true";
		selectOptions(document.forms[0].selectedColumnNames);
		<%if(pageOf != null && pageOf.equals(Constants.PAGE_OF_DISTRIBUTION_CP_QUERY))
		{%>
			setFormAction("<%=Constants.CP_QUERY_CONFIGURE_DISTRIBUTION_ACTION%>");
		<%}
		else{%>
		setFormAction("<%=Constants.CONFIGURE_DISTRIBUTION_ACTION%>");
		<%}%>
			document.forms[0].submit();
	}
	
	function selectOptions(element)
	{
		for(i=0;i<element.length;i++) 
		{
			element.options[i].selected=true;
		}
	}
	
</script>
<style>
	tr#hiddenCombo
	{
	 display:none;
	}
	
</style>
<!-- Mandar : 434 : for tooltip -->
<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
<script language="JavaScript" type="text/javascript"	src="jss/caTissueSuite.js"></script>

<html:form action="<%=Constants.CONFIGURE_DISTRIBUTION_ACTION%>">
<table width="100%" border="0" cellpadding="0" cellspacing="0">
<tr>
 <td width="100%" colspan="2" valign="top">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
              <td colspan="3" valign="top" class="td_color_bfdcf3"><table width="15%" border="0" cellpadding="0" cellspacing="0" background="images/uIEnhancementImages/table_title_bg.gif">
                  <tr>
                    <td width="74%"><span class="wh_ar_b"><bean:message key="distribution.name"/></span></td>
                    <td width="26%" align="right"><img src="images/uIEnhancementImages/table_title_corner2.gif" width="31" height="24" /></td>
                  </tr>
              </table>
			  </td>
			  </tr>
			  	<logic:equal name="pageOf"
														value="<%=Constants.PAGE_OF_DISTRIBUTION%>">
			    <tr>
              <td width="1%" valign="top" class="td_color_bfdcf3">&nbsp;</td>
              <td width="9%" valign="top" class="td_tab_bg">&nbsp;</td>
              <td width="90%" valign="bottom" class="td_color_bfdcf3" style="padding-top:4px;">
			  <table width="100%" border="0" cellpadding="0" cellspacing="0">
                  <tr>
                    <td width="4%" class="td_tab_bg" >&nbsp;</td>
                    <td width="6%" valign="bottom" background="images/uIEnhancementImages/tab_bg.gif" >
				
					<a href="SimpleQueryInterface.do?pageOf=pageOfDistribution&aliasName=Distribution"><img src="images/uIEnhancementImages/tab_specimen_user_selected.gif" alt="Specimen Report" width="126" height="22" border="0" /></a>
			
					</td>
                    <td width="6%" valign="bottom" background="images/uIEnhancementImages/tab_bg.gif">
					<a href="SimpleQueryInterface.do?pageOf=pageOfArrayDistribution&aliasName=Distribution_array"><img src="images/uIEnhancementImages/tab_array_user.gif" alt="Array Report" width="107" height="22" /></td>
                    <td valign="bottom" background="images/uIEnhancementImages/tab_bg.gif">&nbsp;</td>
                    <td width="1%" align="left" valign="bottom" class="td_color_bfdcf3" >&nbsp;</td>
                  </tr>
              </table>
			  </td>
            </tr>
		</logic:equal>
		<logic:equal name="pageOf"
														value="<%=Constants.PAGE_OF_DISTRIBUTION_ARRAY%>">
			    <tr>
              <td width="1%" valign="top" class="td_color_bfdcf3">&nbsp;</td>
              <td width="9%" valign="top" class="td_tab_bg">&nbsp;</td>
              <td width="90%" valign="bottom" class="td_color_bfdcf3" style="padding-top:4px;">
			  <table width="100%" border="0" cellpadding="0" cellspacing="0">
                  <tr>
                    <td width="4%" class="td_tab_bg" >&nbsp;</td>
                    <td width="6%" valign="bottom" background="images/uIEnhancementImages/tab_bg.gif" >
				
					<a href="SimpleQueryInterface.do?pageOf=pageOfDistribution&aliasName=Distribution"><img src="images/uIEnhancementImages/tab_specimen_user.gif" alt="Specimen Report" width="126" height="22" border="0" /></a>
			
					</td>
                    <td width="6%" valign="bottom" background="images/uIEnhancementImages/tab_bg.gif">
					<a href="SimpleQueryInterface.do?pageOf=pageOfArrayDistribution&aliasName=Distribution_array"><img src="images/uIEnhancementImages/tab_array_user_selected.gif" alt="Array Report" width="107" height="22" /></td>
                    <td valign="bottom" background="images/uIEnhancementImages/tab_bg.gif">&nbsp;</td>
                    <td width="1%" align="left" valign="bottom" class="td_color_bfdcf3" >&nbsp;</td>
                  </tr>
              </table>
			  </td>
            </tr>
		</logic:equal>
	<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="100%">
	<tr>
		<td align="right" colspan="3">
			<html:hidden property="distributionId" />
			<html:hidden property="nextAction"/>
		</td>
		
		<td align="right" colspan="3">
			<html:hidden property="reportAction" value="true"/>
			
		</td>
		
	</tr>

	<!-- NEW distribution REGISTRATION BEGINS-->
	<tr> 
	<td>
	  <tr>
        <td colspan="2" class="td_color_bfdcf3" style="padding-left:10px; padding-right:10px; padding-bottom:10px;">
		<table width="100%" border="0" cellpadding="3" cellspacing="0" bgcolor="#FFFFFF">
            <tr>
              <td height="15" colspan="3" align="left"></td>
              </tr>
            <tr>
              <td width="80%" height="25" align="left" class="tr_bg_blue1"><span class="blue_ar_b">	
			  <bean:message key="distribution.reportTitle"/></span></td>
              <td width="16%" align="left" class="tr_bg_blue1">&nbsp;</td>
              <td width="4%" height="25" align="left" class="tr_bg_blue1">&nbsp;</td>
            </tr>
            <tr>
              <td colspan="3" align="left" style="padding-top:10px; padding-bottom:15px; padding-left:6px;"><table width="100%" border="0" cellspacing="0" cellpadding="3" bgcolor="#f6f6f6">
                <tr>
                  <td width="17%" class="black_noneditable"><label for="type" class="black_noneditable"><bean:message key="distribution.identifier"/>  </label></td>
                  <td class="black_noneditable2">- <%=distForm.getDistributionId()%></td>
                </tr>
                <tr>
                  <td class="black_noneditable"><label for="type" class="black_noneditable"><bean:message key="distribution.protocol"/> </label></td>
                  <td class="black_noneditable2">- <%=distForm.getDistributionProtocolTitle()%></td>
                </tr>
                <tr>
                  <td class="black_noneditable"><label for="User" class="black_noneditable"><bean:message key="eventparameters.user"/>  </label></td>
                  <td class="black_noneditable2">- <%=distForm.getUserName()%></td>
                </tr>
                <tr>
                  <td class="black_noneditable"><bean:message key="eventparameters.dateofevent"/></td>
                  <td class="black_noneditable2">- <%=distForm.getDateOfEvent()%></td>
                </tr>
                <tr>
                  <td class="black_noneditable"><bean:message key="eventparameters.time"/></td>
                  <td class="black_noneditable2">- <%=distForm.getTimeInHours()%>:<%=distForm.getTimeInMinutes()%></td>
                </tr>
                <tr>
                  <td class="black_noneditable"><bean:message key="distribution.toSite"/></td>
                  <td class="black_noneditable2">- <%=distForm.getToSite()%> </td>
                </tr>
                <tr>
                  <td class="black_noneditable"><bean:message key="eventparameters.comments"/></td>
                  <td class="black_noneditable2"><%=distForm.getComments()%></td>
                </tr>
                
              </table></td>
            </tr>
	
	
	<tr>

	   <td colspan="3" align="left" style="padding-top:10px; padding-bottom:15px; padding-left:6px;">
	<table cellpadding="3" cellspacing="0" border="0">
		<tr id="hiddenCombo" rowspan="4">
			<td class="formField" colspan="4">
<!-- Mandar : 434 : for tooltip -->
	       		<html:select property="selectedColumnNames" styleClass="selectedColumnNames"  size="1" styleId="selectedColumnNames" multiple="true"
				 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
	       		<%
	       			for(int j=0;j<selectedColumns.length;j++)
	       			{
	       		%>
						<html:option value="<%=selectedColumns[j]%>"><%=selectedColumns[j]%></html:option>
				<%
					}
				%>
           	 	</html:select>
        	</td>
		</tr>
		<tr rowspan = 4>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>									
		</tr>
	</table>

	 <tr onclick="javascript:showHide('distributedItems')">
              <td width="80%" height="25" align="left" class="tr_bg_blue1" ><span class="blue_ar_b"><bean:message key="distribution.distributedItem"/> </span></td>
			  	
				
              <td height="25" align="right" class="tr_bg_blue1" colspan="2"><a href="#" id="imgArrow_distributedItems"><img   src="images/uIEnhancementImages/dwn_arrow1.gif" width="7" height="8" hspace="10" border="0" class="tr_bg_blue1" alt="Show Details" /></a></td>
       </tr>
	     <tr>
              <td colspan="3" style="padding-top:10px;">
			  <div id="distributedItems" style="display:none" >
                <table width="100%" border="0" cellspacing="0" cellpadding="3">
                  <tr class="tableheading">
                    <td width="3%" align="left" class="black_ar_b">#</td>
					<% 
				 		for(int i=0;i<columnNames.length;i++)
				 		{
				 	%>
                    <td width="15%" align="left" class="black_ar_b"><%=columnNames[i]%></td>
						<%
						}
					%>
                  </tr>
				  	 <%
				 	Iterator itr= dataList.iterator();
				 	int i1=1;
				 	while(itr.hasNext())
				 	{
				 %>
                  <tr>
				   <%						
				 		List rowData = (List)itr.next();					
				 		Iterator innerItr= rowData.iterator();
				 		while(innerItr.hasNext())
				 		{
				 %>
                    <td align="left" class="black_ar" ><%=i1%></td>
						 <%			
							
				 			List rowElements = (List)innerItr.next();							
				 			Iterator elementItr= rowElements.iterator();
				 			int j=0;
				 			while(elementItr.hasNext() && j<columnNames.length)
				 			{
				 %>
                    <td class="black_ar"><label><%=elementItr.next()%></label></td>
						<%
								j++;
				 			}
				 			i1++;
				 	%>
                  </tr>
				<%
				 		}
				 	%>
				 	</tr>
				 <%
					}
				%>
                </table>
				</div></td>
              </tr>
			    <tr class="td_color_F7F7F7">
              <td height="20" colspan="3"></td>
              </tr>
            
            <tr  class="td_color_F7F7F7">
              <td  class="buttonbg" style="padding-left:10px;">
					<html:submit property="expButton"  onclick="changeAction()" >
							<bean:message  key="buttons.export" />
					</html:submit>
			 </td>
            
              <td  class="buttonbg" style="padding-left:10px;" colspan="2"><span class="viewlink2">
			  	<%
						if(distForm.getDistributionType().intValue() == Constants.SPECIMEN_DISTRIBUTION_TYPE) 
						{
					%>
						<a href="#" onclick="changeActionOnConfig()"><bean:message  key="buttons.configure" /></a></span>
						<%
						}
					%>
			  </td>
					
            </tr>
	  </td>
	 </tr>
	 </table>
	 </td>
   </tr>
</table>
</html:form>			
