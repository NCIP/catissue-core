<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="/WEB-INF/PagenationTag.tld" prefix="custom" %>
<%@ page import="edu.wustl.catissuecore.util.global.Constants" %>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Iterator"%>
<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
<script language="JavaScript">
	function filteringAction()
	{
		document.forms[0].action="RequestListView.do?pageNum=1&menuSelected=17";
		document.forms[0].submit();
	}		
</script>
  <%
	  int pageNum = Integer.parseInt((String)request.getAttribute(Constants.PAGE_NUMBER));
	  int totalResults = Integer.parseInt((String)session.getAttribute(Constants.TOTAL_RESULTS));
	  int numResultsPerPage = Constants.NUMBER_RESULTS_PER_PAGE;
	  int count = numResultsPerPage * (pageNum -1) + 1;

  %>
<html:messages id="messageKey" message="true" header="messages.header" footer="messages.footer">
	<%=messageKey%>
</html:messages>
<html:errors/>
<head>
</head>
<body >
  <html:form action="RequestListView.do"> 
  	 <jsp:useBean id="requestListForm" class="edu.wustl.catissuecore.actionForm.RequestListFilterationForm" scope="request"/>
	 
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="newMaintable">
  <tr>
    <td class="td_color_bfdcf3"><table width="100%" border="0" cellpadding="0" cellspacing="0" class="whitetable_bg">
      <tr>
        <td width="100%" colspan="2" valign="top"><table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
              <td width="100%" height="31" colspan="4" valign="top" class="td_color_bfdcf3"><table width="10%" border="0" cellpadding="0" cellspacing="0" background="images/uIEnhancementImages/table_title_bg.gif">
                  <tr>
                    <td width="80%"><span class="wh_ar_b">&nbsp;&nbsp;&nbsp;<bean:message key='orderingSystem.orderListPage.buttons.order'/></span></td>
                    <td width="20%" align="right"><img src="images/uIEnhancementImages/table_title_corner2.gif" width="31" height="24" /></td>
                  </tr>
              </table></td>
            </tr>
            
            
        </table></td>
      </tr>
      <tr>
        <td colspan="2" class="td_color_bfdcf3" style="padding-left:10px; padding-right:10px; padding-bottom:10px;">
		<table width="100%" border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
            
            <tr>
              <td align="left" class="td_tab_bg">&nbsp;</td>
              <td colspan="2" align="left" class="td_tab_bg">&nbsp;</td>
            </tr>
            <tr>
              <td width="1%" align="left">&nbsp;</td>
              <td colspan="2" align="left">&nbsp;</td>
            </tr>
            <tr>
              <td align="left" class="tr_bg_blue1">&nbsp;</td>
              <td height="25" colspan="2" align="left" class="tr_bg_blue1"><span class="blue_ar_b"><bean:message key='header.requestList.label'/> &nbsp;</span><span class="black_ar_s">
			  (<% int totalRequests=requestListForm.getNewRequests()+requestListForm.getPendingRequests();%>
							<%= totalRequests %> <bean:message key='order.totalRequests'/>
							&nbsp;&nbsp;&nbsp;&nbsp;
							<bean:write name="requestListForm" property="newRequests"/>&nbsp;
							<bean:message key='order.totalRequestsNew'/>,
							
							<bean:write name="requestListForm" property="pendingRequests"/>&nbsp;
							<bean:message key='order.totalRequestsPending'/> )
							&nbsp;&nbsp;&nbsp;&nbsp;
			  </span></td>
			
            </tr>
            <tr>
              <td align="left">&nbsp;</td>
              <td height="25" align="right"><label>
                <span class="black_ar"><bean:message key="request.status.show.label" />&nbsp;</span>
               <html:select property="requestStatusSelected" name="requestListForm" styleClass="black_ar"  styleId="requestStatusSelected" size="1"   
									 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)" onchange="filteringAction()">
								<html:options collection="<%= Constants.REQUEST_LIST %>" labelProperty="name" property="value"/>		
						</html:select>
              </label></td>
            <td width="1%" align="right"></td>
            </tr>
	
    
            <tr class="td_color_F7F7F7">
              <td>&nbsp;</td>
              <td colspan="2" style="padding-top:10px; padding-bottom:15px;"><table width="99%" border="0" cellspacing="0" cellpadding="4">
                  <tr align="left" >
                    <td align="left" colspan="5" class="black_ar"><custom:test name=" Request List Queue: " pageNum="<%=pageNum%>" totalResults="<%=totalResults%>" numResultsPerPage="<%=numResultsPerPage%>" pageName="RequestListView.do"/></td>
                    </tr>
                  <tr>
                    <td width="2%" class="tableheading"><bean:message key="requestlist.dataTabel.serialNo.label" /></td>
                    <td width="21%" class="tableheading"><strong><bean:message key='requestlist.dataTabel.OrderName.label'/></strong></td>
                    <td width="18%" class="tableheading"><strong><bean:message key='requestlist.dataTabel.DistributionProtocol.label'/></strong></td>
                    <td width="12%" class="tableheading"><strong><bean:message key='requestlist.dataTabel.label.RequestedBy'/></strong></td>
                    <td width="18%" class="tableheading"><strong><bean:message key='requestlist.dataTabel.label.Email'/></strong><span class="grey_ar"></span></td>
                    <td width="15%" class="tableheading"><strong><bean:message key='requestlist.dataTabel.label.RequestDate'/></strong></td>
                     <td class="tableheading"><strong><bean:message key='requestlist.dataTabel.label.Status'/></strong></td>
                  </tr>
				  <%  List requestListData = (List)request.getAttribute("RequestList");						
					%>	
					<logic:iterate id="rowDataString" collection="<%=requestListData%>" type="edu.wustl.catissuecore.bean.RequestViewBean">
         			 <% 
					 String style1 = "black_ar";
					 if (count%2 ==0) { 
						 style1 = "tabletd1";
   					 } %>
					<tr class="<%=style1%>">
                    <td ><bean:write name="rowDataString" property="serialNo"/></td>
                    <td ><a  class="view"  href="RequestDetails.do?id=<bean:write name='rowDataString' property='requestId'/>&menuSelected=17"><bean:write name="rowDataString" property="orderName"/></a></td>
                    <td  ><span class="black_ar"><bean:write name="rowDataString" property="distributionProtocol"/></span></td>
					<td ><bean:write name="rowDataString" property="requestedBy"/></td>
                    <td > <a href='mailto:<bean:write name="rowDataString" property="email"/>' ><bean:write name="rowDataString" property="email"/> <a/></td>
                    <td ><bean:write name="rowDataString" property="requestedDate"/></td>
                    <td ><bean:write name="rowDataString" property="status"/></td>
                  </tr>
				  <% count = count+1;%>
				  </logic:iterate>		
              </table></td>
            </tr>
        </table></td>
      </tr>
    </table></td>
  </tr>
</table>
  </html:form>   
</body>
</html>
