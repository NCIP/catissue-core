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
				height ="530" ;
			%>
		}

function checkKey() 
{
	var platform = navigator.platform.toLowerCase();
	if (platform.indexOf("mac") != -1)
	{		
		var key = event.keyCode; 
		if (key == 13) { event.returnValue=false; } 
	}
	else
	{
		event.returnValue=true;
	}
}

</script>
</head>
<body onKeyPress="checkKey()" >
<html:errors />
<%
	String formName = Constants.categorySearchForm ;
	String SearchCategory = Constants.SearchCategory ;
%>
<html:form method="GET" action="SearchCategory.do" focus="textField">
<html:hidden property="currentPage" value=""/>
	<table border="0" width="100%" cellspacing="0" cellpadding="0" bgcolor="#FFFFFF" height="100%" bordercolorlight="#000000" id="table11">
		<tr>
			<td valign="top"> 
				<table border="0" width="100%" valign="top" cellspacing="0" height="100%">
					<!--<tr valign="top" valign="top"  width="100%">
						<th height="5%"  valign="top" colspan="4">
							<b><font face="Arial" size="2"><bean:message key="query.categorySearchHeader"/></font></b>									
						</th>
						<!--th valign="top" height="5%" width="20%" colspan="2">
							<a id="imageCategorySearch" style="display:block"><img src="images/nolines_plus.gif" /></a>
						</th>
					</tr>-->
					
					<tr bordercolorlight="#000000" >
						<td  height="1%">&nbsp;</td>
						<td width="75%" valign="top" ><html:text property="textField" onkeydown="setFocusOnSearchButton(event)"/></td>
						<td width="25%" valign="top" >
					
							<input type="button"  value="      Go       " name="searchButton" id="searchButton" onclick="retriveSearchedEntities('<%= SearchCategory %>','<%=formName%>','<%=currentPage%>');"/>
					    </td>
					</tr>
					<tr  height="1%">
					    <td  height="1%">&nbsp;</td>
						<td  height="1%" align="left" valign="top" colspan="3"><font face="Arial" size="2"><bean:message key="query.chooseCategoryLable"/></font></td>
					</tr>
					<tr id="collapsableHeader" valign="top" class="row" width="97%">
						<td valign="top"  height="1%">&nbsp;</td>
						<td id="advancedSearchHeaderTd" height="1%" valign="top" bgcolor="#EAEAEA" style="border-left:solid 1px;border-top:solid 1px;border-bottom:solid 1px;bordercolorlight:#EAEAEA">
							<b><font face="Arial" size="2"><bean:message key="query.advancedSearchHeader"/></font></b>									
						</td>
						<th id="imageContainer" valign="top" align="right" height="1%" bgcolor="#EAEAEA" style="border-top:solid 1px;border-bottom:solid 1px;border-right:solid 1px;bordercolorlight:#EAEAEA">
							<a id="image" onClick="expand()" style="display:block"><img src="images/nolines_plus.gif" /></a>
						</th>
						<td  height="1%">&nbsp;</td>
					</tr>
					<tr valign="top" >
						<td valign="top"  style="display:none" id="td1">&nbsp;</td>
						<td colspan="2" valign="top" height="1px" >
							<table valign="top" class="collapsableTable" style="display:none" width="100%" cellspacing="0" cellpadding="0" bgcolor="#FFFFFF" id="collapsableTable">
								<tr id="class_view">
									<td class="standardTextQuery"><html:checkbox  property="classChecked" onclick="setIncludeDescriptionValue()" value="on"> <bean:message key="query.class"/></html:checkbox></td>
								</tr>
								<tr id="attribute_view" >
									<td class="standardTextQuery"><html:checkbox  property="attributeChecked" onclick="setIncludeDescriptionValue()" value="on" > <bean:message key="query.attribute"/></html:checkbox></td>
								</tr>
								<tr id="permissible_view" >
									<td class="standardTextQuery"><html:checkbox property="permissibleValuesChecked" onclick="permissibleValuesSelected(this)" value="on" > <bean:message key="query.permissibleValues"/></html:checkbox></td>
								</tr>
								<tr id="description_view" >
									<td class="standardTextQuery"><html:checkbox  property="includeDescriptionChecked" value="off" ><bean:message key="query.includeDescription"/> </html:checkbox></td>
								</tr>
								<tr><td>&nbsp;</td></tr>
								<tr id="radio_view" >
									<td class="standardTextQuery">
										<html:radio property="selected" value="text_radioButton" onclick="radioButtonSelected(this)"/><bean:message key="query.text"/>
										<html:radio property="selected" value="conceptCode_radioButton" onclick="radioButtonSelected(this)" disabled="true" /><bean:message key="query.conceptCode"/>
									</td>
								</tr>											
							</table>
						</td>
						<td valign="top" style="display:none" id="td3">&nbsp;</td>
					</tr>							 	
					<tr>
						<td height="5px"></td>
				 	</tr>
					<tr valign="top" class="row" width="98%"  height="1%">
						<td  height="1%">&nbsp;</td>
						<td colspan="2"  height="1%" valign="top" bgcolor="#EAEAEA"  class="tdWithoutBottomBorder">
							<b><font face="Arial" size="2"><bean:message key="query.searchResults"/></font>	</b>				
						</td>
						<td  height="1%">&nbsp;</td>
					</tr>
					<tr valign="top" class="row" width="98%" height="100%">
						<td width="100%">&nbsp;</td>
						<td  width="100%" colspan="2" id='resultSetTd' class="tdWithoutTopBorder">
							<div id="resultSet" style="overflow:auto;height:530;width:227"></div>
						</td>
						<td >&nbsp;</td>
					</tr>
					
			</table>
		</td>
	</tr>														
</table>
</html:form>
</body>
</html>