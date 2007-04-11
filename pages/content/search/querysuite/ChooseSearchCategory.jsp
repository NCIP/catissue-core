<%@ page import="edu.wustl.catissuecore.actionForm.CategorySearchForm"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<html>
<head>
<meta http-equiv="Content-Language" content="en-us">
<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
<script src="jss/queryModule.js"></script>
<script type="text/javascript" src="jss/ajax.js"></script> 
<script>
<%
String height = "";
String currentPage = (String)request.getAttribute(Constants.CURRENT_PAGE);

System.out.println("currentPage         "+currentPage);
%>
	if(navigator.appName == "Microsoft Internet Explorer")
		{
			<%
				height = "60%";
			%>
		}
		else
		{
			<%
				height ="400" ;
			%>
		}

</script>
</head>
<body>
<html:errors />
<%
	String formName = Constants.categorySearchForm ;
	String SearchCategory = Constants.SearchCategory ;
%>
<html:form method="GET" action="SearchCategory.do">
<html:hidden property="currentPage" value=""/>
	<table border="1" width="100%" cellspacing="0" cellpadding="0" bgcolor="#FFFFFF" height="100%" bordercolorlight="#000000" id="table11">
		<tr>
			<td valign="top"> 
				<table border="0" width="100%" valign="top" cellspacing="0" height="100%">
					<tr valign="top" valign="top" bgcolor="#EAEAEA" width="100%">
						<th height="5%"  valign="top" colspan="4">
							<b><font face="Arial" size="2"><bean:message key="query.categorySearchHeader"/></font></b>									
						</th>
						<!--th valign="top" height="5%" width="20%" colspan="2">
							<a id="imageCategorySearch" style="display:block"><img src="images/nolines_plus.gif" /></a>
						</th-->
					</tr>
					<tr>
						<td height="10px"></td>
					</tr>
					<tr bordercolorlight="#000000">
						<td>&nbsp;</td>
						<td width="75%" valign="top" ><html:text property="textField" onkeydown="setFocusOnSearchButton(event)"/></td>
						<td width="25%" valign="top">
					
							<input type="button" value="Search" name="searchButton" id="searchButton" onclick="retriveSearchedEntities('<%= SearchCategory %>','<%=formName%>','<%=currentPage%>');"/>
					
						</td>
						<td valign="top">&nbsp;</td>
					</tr>
					<tr>
						<td height="10px" valign="top"></td>
					</tr>
					<tr id="collapsableHeader" valign="top" class="row" width="98%">
						<td valign="top">&nbsp;</td>
						<td height="5%" valign="top" bgcolor="#EAEAEA" style="border-left:solid 1px;border-top:solid 1px;border-bottom:solid 1px;bordercolorlight:#EAEAEA">
							<b><font face="Arial" size="2"><bean:message key="query.advancedSearchHeader"/></font></b>									
						</td>
						<th valign="top" height="5%" bgcolor="#EAEAEA" style="border-top:solid 1px;border-bottom:solid 1px;border-right:solid 1px;bordercolorlight:#EAEAEA">
							<a id="image" onClick="expand()" style="display:block"><img src="images/nolines_plus.gif" /></a>
						</th>
						<td>&nbsp;</td>
					</tr>
					<tr valign="top">
						<td valign="top">&nbsp;</td>
						<td colspan="2" valign="top">
							<table valign="top" class="collapsableTable" style="display:none" width="100%" cellspacing="0" cellpadding="0" bgcolor="#FFFFFF" id="collapsableTable">
								<tr id="class_view">
									<td class="standardTextQuery"><html:checkbox property="classChecked" value="on"> <bean:message key="query.class"/></html:checkbox></td>
								</tr>
								<tr id="attribute_view" >
									<td class="standardTextQuery"><html:checkbox property="attributeChecked" value="on" > <bean:message key="query.attribute"/></html:checkbox></td>
								</tr>
								<tr id="permissible_view" >
									<td class="standardTextQuery"><html:checkbox property="permissibleValuesChecked" value="on" > <bean:message key="query.permissibleValues"/></html:checkbox></td>
								</tr>
								<tr id="blank_view" >
									<td>&nbsp;</td>
								</tr>
								<tr id="radio_view" >
									<td class="standardTextQuery">
										<html:radio property="selected" value="text_radioButton" /><bean:message key="query.text"/>
										<html:radio property="selected" value="conceptCode_radioButton"/><bean:message key="query.conceptCode"/>
									</td>
								</tr>											
							</table>
						</td>
						<td valign="top">&nbsp;</td>
					</tr>							 	
					<tr>
						<td height="5px"></td>
					</tr>
					<tr valign="top" class="row" width="98%">
						<td>&nbsp;</td>
						<td colspan="2" height="5%" valign="top" bgcolor="#EAEAEA" style="border:solid 1px;bordercolorlight:#EAEAEA">
							<b><font face="Arial" size="2"><bean:message key="query.searchResults"/></font>	</b>									
						</td>
						<td>&nbsp;</td>
					</tr>
					<tr valign="top" class="row" width="100%">
						<td width="100%">&nbsp;</td>
						<td height=<%=height%>  width="100%" colspan="2" id='resultSetDiv'  style="border:solid 1px;bordercolorlight:#EAEAEA">
							<div id="resultSet" style="overflow:auto;height:100%;width:100%"></div>
						</td>
						<td>&nbsp;</td>
					</tr>
					<tr valign="top" class="row" width="100%" height="1">						
						<td height="3" colspan="4"></td>								
					</tr>
			</table>
		</td>
	</tr>														
</table>
</html:form>
</body>
</html>