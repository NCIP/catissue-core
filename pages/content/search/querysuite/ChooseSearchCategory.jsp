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
</head>
<html:errors />
<%
	String formName = Constants.categorySearchForm ;
	String SearchCategory = Constants.SearchCategory ;
%>
<html:form method="GET" action="SearchCategory.do">
	<table border="1" width="20%" cellspacing="0" cellpadding="0" bgcolor="#FFFFFF" height="100%" bordercolorlight="#000000" id="table11">
		<tr>
			<td valign="top"> 
				<table border="0" width="208" valign="top" cellspacing="0" height="100%">
					<tr valign="top" valign="top" bgcolor="#EAEAEA" width="100%">
						<th height="5%"  valign="top" colspan="2">
							<b><font face="Arial" size="2"><bean:message key="query.categorySearchHeader"/></font></b>									
						</th>
						<th valign="top" height="5%" width="20%" colspan="2">
							<a id="imageCategorySearch" style="display:block"><img src="images/nolines_plus.gif" /></a>
						</th>
					</tr>
					<tr>
						<td height="10px"></td>
					</tr>
					<tr bordercolorlight="#000000">
						<td>&nbsp;</td>
						<td width="75%" valign="top" ><html:text property="textField"/></td>
						<td width="25%" valign="top">
							<input type="button" value="Search" name="searchButton" onclick="retriveSearchedEntities('<%= SearchCategory %>','<%=formName%>');"/>
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
									<td><font face="Arial"><html:checkbox property="classChecked" value="on" ><font size="2">Class</font><font size="2">&nbsp;&nbsp;</font></html:checkbox></font></td>
								</tr>
								<tr id="attribute_view" >
									<td><font face="Arial"><html:checkbox property="attributeChecked" value="on"><font size="2">Attribute&nbsp;&nbsp;</font></html:checkbox></font></td>
								</tr>
								<tr id="permissible_view" >
									<td><font face="Arial"><html:checkbox property="permissibleValuesChecked" value="on"><font size="2">Permissible</font><font size="2">Values</font></html:checkbox></font></td>
								</tr>
								<tr id="blank_view" >
									<td>&nbsp;</td>
								</tr>
								<tr id="radio_view" >
									<td>
										<html:radio property="selected" value="text_radioButton" /><font face="Arial"><font size="2">Text&nbsp;&nbsp;&nbsp;&nbsp;</font></font>&nbsp;&nbsp;&nbsp;&nbsp;
										<html:radio property="selected" value="conceptCode_radioButton"/><font face="Arial"><font size="2">Concept </font><font size="2">Code</font></font>
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
						<td height="400"  width="100%" colspan="2" id='resultSetDiv'  style="border:solid 1px;bordercolorlight:#EAEAEA">
							<div id="resultSet" style="overflow:auto;height:100%;width:100%"></div>
						</td>
						<td>&nbsp;</td>
					</tr>
					<tr valign="top" class="row" width="98%" height="1">						
						<td height="3" colspan="4"></td>								
					</tr>
			</table>
		</td>
	</tr>														
</table>
</html:form>
</html>