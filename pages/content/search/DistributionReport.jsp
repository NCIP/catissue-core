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
<html:hidden property="distributionId" />
			<html:hidden property="nextAction"/>
			<html:hidden property="reportAction" value="true"/>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="maintable">
  <tr>
    <td class="td_color_bfdcf3"><table border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td class="td_table_head"><span class="wh_ar_b"><bean:message key="distribution.name"/>
		</span></td>
        <td><img src="images/uIEnhancementImages/table_title_corner2.gif" alt="Page Title - Distribution" width="31" height="24"></td>
      </tr>
    </table></td>
  </tr>
  <tr>
    <td class="tablepadding"><table width="100%" border="0" cellpadding="0" cellspacing="0">
	<logic:equal name="pageOf" value="<%=Constants.PAGE_OF_DISTRIBUTION%>">
      <tr>
        <td class="td_tab_bg" >
			<img src="images/uIEnhancementImages/spacer.gif" alt="spacer" width="50" height="1"></td>
			 <td valign="bottom" ><img src="images/uIEnhancementImages/tab_specimen_user_selected.gif" alt="Specimen Report" width="126" height="22" border="0" /></td>
			 <td valign="bottom">
				<a href="SimpleQueryInterface.do?pageOf=pageOfArrayDistribution&aliasName=Distribution_array"> <img src="images/uIEnhancementImages/tab_array_user.gif" alt="Array Report" width="107" height="22" /></a></td>
	</logic:equal>
	  
		<logic:equal name="pageOf" value="<%=Constants.PAGE_OF_DISTRIBUTION_ARRAY%>">
			    <tr>
        <td class="td_tab_bg" >
			<img src="images/uIEnhancementImages/spacer.gif" alt="spacer" width="50" height="1"></td>
			 <td valign="bottom" ><a href="SimpleQueryInterface.do?pageOf=pageOfDistribution&aliasName=Distribution"><img src="images/uIEnhancementImages/tab_specimen_user.gif" alt="Specimen Report" width="126" height="22" border="0" /></a></td>
                     <td valign="bottom"><img src="images/uIEnhancementImages/tab_array_user_selected.gif" alt="Array Report" width="107" height="22" /></td>
                    
		</logic:equal>
			<td width="90%" valign="bottom" class="td_tab_bg">&nbsp;</td>
      </tr>
    </table>
      <table width="100%" border="0" cellpadding="3" cellspacing="0" class="whitetable_bg">
        <tr>
          <td colspan="3" align="left" class="toptd"></td>
        </tr>
        <tr>
          <td colspan="3" align="left" class="tr_bg_blue1"><span class="blue_ar_b">&nbsp;
			<bean:message key="distribution.reportTitle"/>
			</span></td>
        </tr>
        <tr>
          <td colspan="3" align="left" class="showhide"><table width="100%" border="0" cellpadding="3" cellspacing="0" class="noneditable">
              <tr>
                <td width="17%"><label for="type"><strong>
				<bean:message key="distribution.identifier"/></strong>  </label></td>
				<td width="83%">- <%=distForm.getDistributionId()%></td>
				</tr>
              <tr>
                <td>
					<label for="type" class="noneditable"><strong><bean:message key="distribution.protocol"/> </strong></label></td>
					<td>- <%=distForm.getDistributionProtocolTitle()%></td>
					</tr>
              <tr>
                <td><label for="User"><strong><bean:message key="eventparameters.user"/>
				</strong></label></td>
                <td>- <%=distForm.getUserName()%></td>
				</tr>
              <tr>
                <td><strong><bean:message key="eventparameters.dateofevent"/>
				</strong></td>
                <td>- <%=distForm.getDateOfEvent()%></td>
				</tr>
              <tr>
                <td><strong><bean:message key="eventparameters.time"/>
				</strong></td>
                <td>- <%=distForm.getTimeInHours()%>:<%=distForm.getTimeInMinutes()%></td>
				</td>
              </tr>
              <tr>
                <td><strong><bean:message key="distribution.toSite"/>
				</strong></td>
                <td>- <%=distForm.getToSite()%> </td>
				</tr>
              <tr>
                <td><strong><bean:message key="eventparameters.comments"/>
				</strong></td>
                <td>- <%=distForm.getComments()%></td>
				</tr>
          </table></td>
        </tr>
		<tr>

	   <td colspan="3" align="left" style="padding-top:10px; padding-bottom:15px; padding-left:6px;">
	<table cellpadding="3" cellspacing="0" border="0">
		<tr id="hiddenCombo" rowspan="2">
			<td class="black_new" >
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
		
	</table>
	</td>
	</tr>
        <tr onclick="javascript:showHide('distributedItems')">
          <td width="80%" align="left" class="tr_bg_blue1"><span class="blue_ar_b">&nbsp;
		  <bean:message key="distribution.distributedItem"/> </span></td>
		  
		  <td width="20%" align="right" class="tr_bg_blue1"><a href="#" id="imgArrow_distributedItems"><img src="images/uIEnhancementImages/dwn_arrow1.gif" width="80" height="9" hspace="10" border="0" alt="Show Details"></a></td>
        </tr>
		
		<tr>
          <td colspan="3" class="showhide1"><div id="distributedItems" style="display:none" >
		  <table width="100%" border="0" cellspacing="0" cellpadding="3">
			<tr>
			<td>&nbsp;</td>
			<td colspan="<%=columnNames.length%>" align="right" nowrap >
		  
						<img src="images/uIEnhancementImages/viewall_icon.gif" alt="View All" />
						<a href="#" onclick="changeActionOnConfig()" class="view">
						<span class="view">
						<bean:message  key="buttons.configure" /></span></a>
						
					</span></td>
		</tr>

                  <tr class="tableheading">
                    <td align="left" class="black_ar_b">#</td>
					<% 
				 		for(int i=0;i<columnNames.length;i++)
				 		{
				 	%>
                    <td align="left" class="black_ar_b"><%=columnNames[i]%></td>
						<%
						}
					%>
                  </tr>
				  	
                  <tr>
				   <%						
				 		//List rowData = (List)itr.next();					
				 		Iterator innerItr= dataList.iterator();
						int i1=1;
				 		while(innerItr.hasNext())
				 		{
							String fontStyle="black_ar";
								if(i1%2==0)
									fontStyle="tabletd1";
				 %>
                    <td align="left" class="<%=fontStyle%>" ><%=i1%></td>
						 <%			
							
				 			List rowElements = (List)innerItr.next();							
				 			Iterator elementItr= rowElements.iterator();
				 			int j=0;
				 			while(elementItr.hasNext() && j<columnNames.length)
				 			{
								

				 %>
                    <td class="<%=fontStyle%>"><label><%=elementItr.next()%></label></td>
						<%
								j++;
				 			}
				 			i1++;
				 	%>
                  </tr>
				<%
				 		}
				 	%>
				 	
				
                </table>
				</div></td>
              </tr>
			   <tr>
          <td colspan="3" class="toptd"></td>
        </tr>
			    <tr>
          <td colspan="3" class="buttonbg">
				<span class="blue_ar_b">
					<html:submit property="expButton" onclick="changeAction()" >
							<bean:message  key="buttons.export" />
					</html:submit>
					</span>
			</td>
        </tr>
      </table></td>
  </tr>
</table>
</html:form>			
