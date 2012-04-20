<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="edu.wustl.catissuecore.actionForm.GSIDBatchUpdateForm"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/PagenationTag.tld" prefix="custom"%>
<%@ page language="java" isELIgnored="false"%>
<%@ page import="edu.wustl.catissuecore.util.global.Variables"%>
<%@ page import="edu.wustl.catissuecore.util.global.AppUtility"%>
<%@ page import="edu.wustl.common.util.global.CommonServiceLocator"%>
<%@ page import="java.util.*"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page import="edu.wustl.catissuecore.GSID.GSIDConstant"%>

<script type="text/javascript" src="jss/jquery/jquery-1.5.1.min.js"></script>
<script type="text/javascript" src="jss/jquery/gsid/updateStatus.js"></script>
<%
    GSIDBatchUpdateForm form = (GSIDBatchUpdateForm) request
            .getAttribute("GSIDBatchUpdateForm");
%>

<table width="100%" border="0" cellpadding="0" cellspacing="0"
	class="maintable">
	<tr>
		<td class="tablepadding">
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td width="90%" valign="bottom" class="td_tab_bg">&nbsp;</td>
				</tr>
			</table>
			<table width="100%" border="0" cellpadding="3" cellspacing="0"
				class="whitetable_bg">
				<tr>
					<td width="99%" align="left" class="bottomtd"><%@ include
							file="/pages/content/common/ActionErrors.jsp"%></td>
				</tr>
			</table>
		</td>
	</tr>
</table>
