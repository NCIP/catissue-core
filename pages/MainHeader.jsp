<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<script src="jss/titli.js"></script>
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


<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td width="5%" valign="top"><img
			src="images/uIEnhancementImages/top_bg1.jpg" width="53" height="20" /></td>
		<td width="95%" valign="top" align="right"
			background="images/uIEnhancementImages/top_bg.jpg"
			style="background-repeat:repeat-x;">
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td width="86%" align="right" valign="top">
				<logic:notEmpty name="<%=Constants.SESSION_DATA%>">
				<%
				String searchString = request.getParameter("searchString");
				if(searchString == null)
				{
					searchString = "Keyword Search";
				}
				%>
						<input class="form-text"  value="Search for specimen, containers, and others..."    type ="text" name = "searchString" id = "searchString" onkeypress="return titliOnEnter(event, this, document.getElementById('go'))" 
						size="50"  onfocus="inputFocus(this)" onblur="inputBlur(this)" />
						<a class = "white"  id="go" href ="TitliInitialiseSearch.do" onclick = "this.href= this.href + '?searchString='+document.getElementById('searchString').value"></a>
				</logic:notEmpty>
					<a href="RequestListView.do" class="white"> 
						<img src="images/uIEnhancementImages/orders_icon.png" width="16"
					height="13" hspace="2" vspace="0"><bean:message
					key="app.orders" /> 
					</a>
					&nbsp;
					<a	href="ShowQueryDashboardAction.do"
						class="white">
						<img src="images/uIEnhancementImages/search_basic_blue.png" alt="Summary"
						width="16" height="13" hspace="2" vspace="0" border="0" /><bean:message	key="app.queries" /></a>&nbsp;
					<a	href="CpBasedSearch.do" class="white"> <img
						src="images/uIEnhancementImages/data_entry_icon.png" alt="Summary"
						width="16" height="12" hspace="2" vspace="0" border="0" />
						<label align="top"><bean:message key="app.dataEntry" /> </label>
					</a>
					</td>

				<logic:notEmpty scope="session" name="<%=Constants.SESSION_DATA%>">
					<td width="14%" align="right" valign="top"><a
						href="Logout.do"> <img
						src="images/uIEnhancementImages/logout_button1.gif" name="Image1"
						width="86" height="19" id="Image1"
						onmouseover="MM_swapImage('Image1','','images/uIEnhancementImages/logout_button.gif',1)"
						onmouseout="MM_swapImgRestore()" /> </a></td>
				</logic:notEmpty>
				<logic:empty scope="session" name="<%=Constants.SESSION_DATA%>">
					<td width="14%" valign="middle" align="right"><a
						href="Home.do" class="white"> <bean:message
						key="app.loginMessage" /> </a> <img
						src="images/uIEnhancementImages/spacer.gif" width="10" height="10"
						align="absmiddle" /></td>
				</logic:empty>
			</tr>
		</table>
		</td>
	</tr>
</table>