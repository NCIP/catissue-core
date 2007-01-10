<%@ page import="edu.wustl.catissuecore.actionForm.CategorySearchForm"%>
<%@ page import="java.util.*"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<html>

	<head>
	</head>

	<body>
		<table summary="" cellpadding="20" cellspacing="0" border="1" width="100%" height="100%">
			<tr>
				<td>
					<table border="1" width="100%" height="100%">
						<tr>
							<td>
								<table summary="" cellpadding="4" cellspacing="0" border="0" width="100%" height="100%">
									<tr valign = "top" >
										<td width="20%" height="30" class="queryTabMenuItemSelected" onclick="alert('This page is still under construction and will be available in the next release');">
											<bean:message key="query.chooseSearchCategory"/>
										</td>

										<td width="20%" height="30" class="queryTabMenuItem" onmouseover="changeMenuStyle(this,'queryTabMenuItemOver'),showCursor()" onmouseout="changeMenuStyle(this,'queryTabMenuItem'),hideCursor()" onClick="document.location.href='NewCategorySearch.jsp'">
											<bean:message key="query.addLimits"/>
										</td>

										<td width="20%" height="30" class="queryTabMenuItem" onmouseover="changeMenuStyle(this,'queryTabMenuItemOver'),showCursor()" onmouseout="changeMenuStyle(this,'queryTabMenuItem'),hideCursor()">
											<bean:message key="query.defineSearchResultsViews"/>
										</td>

										<td width="20%" height="30" class="queryTabMenuItem" onmouseover="changeMenuStyle(this,'queryTabMenuItemOver'),showCursor()" onmouseout="changeMenuStyle(this,'queryTabMenuItem'),hideCursor()" onClick="showFormPreview()" >
											<bean:message key="query.viewSearchResults"/>
										</td>

										<td width="20%" height="30" class="queryTabMenuItem" onmouseover="changeMenuStyle(this,'queryTabMenuItemOver'),showCursor()" onmouseout="changeMenuStyle(this,'queryTabMenuItem'),hideCursor()" onClick="showFormPreview()" >
											<bean:message key="query.dataList"/>
										</td>
									</tr>
								
									<tr>
										<td height="600" >&nbsp;</td>
									</tr>
								
									<tr>
										<td><html:button style="align:right" property="button" onclick="addNewAction('SearchCategory.do')">Next</html:button></td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>

	</body>

</html>
