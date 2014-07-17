<!DOCTYPE html>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>


<%@ page language="java" isELIgnored="false"%>

<script src="jss/titli.js"></script>
<script language="JavaScript" type="text/javascript" src="jss/keyBoardShortCutPlugin.js"></script>
<script language="JavaScript" type="text/javascript" src="jss/KeyBoardMenuCode.js"></script>
<script type="text/javascript">
function inputFocus(i){
	if(i.value==i.defaultValue)
		{ 
			i.value="";
		}
}

function inputBlur(i){
	if(i.value=="")
	{ 
		i.value=i.defaultValue;
	}
}
</script>
 <style type="text/css">
    .imgAlign {
        vertical-align: sub;
    }

    </style>



<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td width="5%" valign="top"><img
			src="images/uIEnhancementImages/top_bg1.jpg" width="53" height="20" /></td>
		<td width="95%" valign="top" align="right"
			background="images/uIEnhancementImages/top_bg.jpg"
			style="background-repeat:repeat-x;">
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td width="86%" align="right" valign="top" style="padding-right:10px">
				<logic:notEmpty name="<%=Constants.SESSION_DATA%>">
				<%
				String searchString = request.getParameter("searchString");
				if(searchString == null)
				{
					searchString = "Keyword Search";
				}
				%>
				<input type="hidden" id="tmp_usrName" value="${sessionData.firstName} ${sessionData.lastName}"/>
						<input class="form-text"  value="Quick search for specimens, containers, and others...."    type ="text" name = "searchString" id = "searchString" onkeypress="return titliOnEnter(event, this, document.getElementById('go'))" 
						size="50"  onfocus="inputFocus(this)" onblur="inputBlur(this)" />
						<a class = "white"  id="go" href ="TitliInitialiseSearch.do" onclick = "this.href= this.href + '?searchString='+document.getElementById('searchString').value"></a>
				</logic:notEmpty>
					<a href="OpenStorageContainer.do?operation=showMap" class="white"> 
						<img class="imgAlign"  src="images/uIEnhancementImages/container_icon.png"  title="Containers,Press Alt + Shift + C">
						<span title="Containers, Press Alt + Shift + C" style="vertical-align:top"> 
							<bean:message key="app.containers" />
						</span> 
					</a>
					&nbsp;
					<a href="RequestListView.do" class="white"> 
						<img class="imgAlign" src="images/uIEnhancementImages/order_icon.png"  title="Orders,Press Alt + O">
						<span title="Orders, Press Alt + O" style="vertical-align:top"> 
							<bean:message key="app.orders" /> 
						</span> 
					</a>
					&nbsp;
					<a href="ShowQueryDashboardAction.do" class="white">
						<img class="imgAlign" src="images/uIEnhancementImages/search_icon.png" alt="Summary"
						 border="0" title="Queries,Press Alt + Q"/>
						 <span style="vertical-align:top" title="Query,Press Alt + Q">
							<bean:message key="app.queries" />
						 </span>
					</a>
					&nbsp;
					<a	href="CpBasedSearch.do" class="white"> 
						<img class="imgAlign" src="images/uIEnhancementImages/data_entry_icon.png" alt="CP Based View"
						 border="0" title="CP Based View, Press Alt + C"/>
						 <span title="CP Based View, Press Alt + C" style="vertical-align:top">
							<bean:message key="app.cpBasedView" /> </span>
					</a>&nbsp;
					
					<a	href="BulkOperation.do?pageOf=pageOfBulkOperation" class="white">
							<img class="imgAlign" src="images/uIEnhancementImages/bo.png" alt="Summary"
								  border="0" title="Bulk Upload,,Press Alt + B" />
								  <span title="Bulk Upload,Press Alt + B" style="vertical-align:top">
									<bean:message key="app.bulkUplad" />  </span>
					</a>
				</td>


				<logic:empty scope="session" name="<%=Constants.SESSION_DATA%>">
					<td width="14%" valign="middle" align="right"><a
						href="Home.do" class="white"> <bean:message
						key="app.loginMessage" /> </a> <img class="imgAlign"
						src="images/uIEnhancementImages/spacer.gif" width="10" height="10"
						align="absmiddle" /></td>
				</logic:empty>
			</tr>
		</table>
		</td>
	</tr>
</table>