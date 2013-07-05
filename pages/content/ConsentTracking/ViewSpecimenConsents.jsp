<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page language="java" isELIgnored="false" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<%@ page import="edu.common.dynamicextensions.xmi.AnnotationUtil"%>
<%@ page import="edu.wustl.catissuecore.action.annotations.AnnotationConstants"%>
<%@ page import="edu.wustl.catissuecore.util.CatissueCoreCacheManager"%>
<%@ page import="edu.wustl.catissuecore.actionForm.SpecimenCollectionGroupForm"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>

<head>
<style>
.active-column-1 {width:200px}
</style>

<LINK href="css/catissue_suite.css" type="text/css" rel="stylesheet">
<link rel="stylesheet" type="text/css" href="css/styleSheet.css" />
<script src="jss/fileUploader.js" type="text/javascript"></script>
<script language="JavaScript" type="text/javascript" src="jss/newSpecimen.js"></script>
<script language="JavaScript" type="text/javascript" src="jss/specimenCollectionGroup.js"></script>

<logic:equal name="consentLevel" value="scg">
		<table width="100%" border="0" cellpadding="0" cellspacing="0"  class="maintable">
			<tr>
				<td>
					<%@ include file="/pages/content/ConsentTracking/ViewConsents.jsp" %>
				</td>
			</tr>
		</table>
</logic:equal> 
<logic:equal name="consentLevel" value="participant">

		<table width="100%" border="0" cellpadding="0" cellspacing="0"  class="maintable">
			<tr>
				<td>
					<%@ include file="/pages/content/ConsentTracking/ViewConsents.jsp" %>
				</td>
			</tr>
		</table>
</logic:equal> 
	<logic:equal name="consentLevel" value="specimen">
		<table width="100%" border="0" cellpadding="0" cellspacing="0" class="maintable">
			<tr>
				<td>
					<%@ include file="/pages/content/ConsentTracking/ViewConsents.jsp" %>
				</td>
			  </tr>
		</table>
	</logic:equal>