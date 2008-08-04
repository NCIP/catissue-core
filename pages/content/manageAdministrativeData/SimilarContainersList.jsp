<!-- 
	This JSP page is to create/display similar containers from/of Parent Storage Container.
	Author : Chetan B H
	Date   : 
-->
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<%@ page import="edu.wustl.catissuecore.util.global.Constants" %>
<%@ page import="edu.wustl.catissuecore.util.global.Utility" %>
<%@ page import="edu.wustl.catissuecore.actionForm.StorageContainerForm" %>
<%@ page import="edu.wustl.catissuecore.domain.StorageContainer" %>
<%@ page import="edu.wustl.common.beans.NameValueBean" %>
<head>
</head>
<link href="css/catissue_suite.css" rel="stylesheet" type="text/css" /> 
<html:messages id="messageKey" message="true" header="messages.header" footer="messages.footer">
	<%=messageKey%>
</html:messages>

<html:errors/>

<html:form action="<%=Constants.SIMILAR_CONTAINERS_ADD_ACTION%>">


	<% int cnt=0;%>
	 <table width="100%" border="0" cellpadding="3" cellspacing="0" class="whitetable_bg">
		<tr>
          <td colspan="2" align="left" class="toptd"></td>
        </tr>
		<tr>
			<td colspan="2" align="left" class="tr_bg_blue1"><span class="blue_ar_b">&nbsp;List of Added Containers</span></td>
		 </tr>	
		 <tr>
          <td colspan="2" align="center" class="showhide"><table width="99%" border="0" cellpadding="2" cellspacing="0">
		   <tr>
              <td align="left"><table width="75%"  class="tableborder" cellspacing="0" cellpadding="4">
			  <tr>
			<logic:iterate id="nvb" name="similarContainerList">
			<%	NameValueBean nameValueBean=(NameValueBean)nvb;
			String hrefString="StorageContainerSearch.do?pageOf=pageOfStorageContainer&id="+nameValueBean.getValue();
			%>
			<td width="33%" class="black_ar"><img src="images/uIEnhancementImages/ic_storageC.gif" alt="Storage Container" width="19" height="14" align="absmiddle">&nbsp;<html:link href="<%=hrefString%>" styleClass="view"><bean:write name="nvb" property="name" /></html:link></td>
				<%cnt=cnt+1;%>
				<%
					if(cnt == 3)
					{	
						cnt=0;
				%>	
				</tr>
				<tr>
				<%}%>
				</logic:iterate>
			
				<%if(cnt%3==2)
				{%>
				<td class="black_ar">&nbsp;</td>
			<%}%>
			<%if(cnt%3==1)
			{%>
				<td class="black_ar">&nbsp;</td><td >&nbsp;</td>
			<%}%>
	
		</tr>
                
              </table></td>
              </tr>
            
          </table></td>
        </tr>
        <tr>
          <td colspan="2" class="bottomtd"></td>
        </tr>
    </table></td>
  </tr>
</table>
</html:form>