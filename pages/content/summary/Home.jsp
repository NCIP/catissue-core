<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
		 "http://www.w3.org/TR/html4/loose.dtd">

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/twoColDetailsTag.tld" prefix="twoCol" %>

<%@ page import="edu.wustl.catissuecore.actionForm.SummaryForm"%>
<%@ page import="edu.wustl.common.beans.NameValueBean"%>
<%@ page import="java.util.*"%>
<%@ page language="java" isELIgnored="false" %>
<%@ page import="edu.wustl.catissuecore.util.global.Variables"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants" %>
<%@ page import="edu.wustl.catissuecore.util.global.Utility" %>
<%@ page import="java.lang.Integer" %>
<%@ page import="edu.wustl.common.util.tag.ScriptGenerator" %>

<%
	Object obj = request.getAttribute("summaryForm");
	SummaryForm sForm = null;
	if(obj != null && obj instanceof SummaryForm)
	{
		sForm = (SummaryForm)obj;
	}			
%>
<c:set var="v35" value="35%" />
<c:set var="v34" value="30%" />
<c:set var="v50" value="50%" />
<!-- Mandar : 27Jan09 : table addition -->
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="maintable">
	<tr>
		<td class="td_color_bfdcf3">
			<table border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td class="td_table_head" nowrap="nowrap">
						<span class="wh_ar_b"><bean:message key="app.summary" /></span>
					</td>
					<td>
						<img src="images/uIEnhancementImages/table_title_corner2.gif" width="31" height="24" alt="Page Title - Report Problems"/>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td class="tablepadding">
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td width="90%" valign="bottom" class="td_tab_bg">&nbsp;</td>
				</tr>
			</table>
			<table width="100%" border="0" cellpadding="3" cellspacing="0" class="whitetable_bg">
				<tr>
					<td width="100%" height="100%">
						<table border="0" width="100%">
							<tr class="tr_h1_bggray">
								<td width="100%" class="gray_h1_md"><bean:message key="summary.page.contactInfo" /></td>
							</tr>
							<!-- Mandar : 4Feb09 Admin user details start -->
							<tr class="tr_h2_bggray">
								<td>
									<table width="100%">
										<tr class="tr_h2_bggray">
										    <td width="${v35}" class="gray_h2_md"><bean:message key="summary.page.admin" /></td>
										    <td width="${v35}" class="gray_h2_md"><bean:message key="summary.page.contact" /></td>
										    <td width="${v34}" class="gray_h2_md"><bean:message key="summary.page.email" /></td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td>
									<table width="100%">
										  <!-- For User Data -->
										<c:forEach var="userRow" items="${summaryForm.adminDetails.adminInfo}">
										  <tr>
											<td class="black_ar" width="${v35}">${userRow[0]}</td>
											<td class="black_ar" width="${v35}">${userRow[2]}</td>
											<td class="black_ar" width="${v34}"><a href="mailto:${userRow[1]}">${userRow[1]}</a></td>
										  </tr>
										</c:forEach>
									</table>
								</td>
							</tr>
							<!-- Mandar : 4Feb09 Admin user details end -->
							<tr>
								<td width="100%">&nbsp;</td>
							</tr> 
<!-- Mandar : 4Feb09 Admin general count details start -->
							<tr class="tr_h1_bggray">
								<td width="100%" class="gray_h1_md"><bean:message key="summary.page.adminRecords" /></td>
							</tr>
							<tr>
								<td width="100%">
									<table border="0" width="100%">
										<tr>
											<td class="black_ar" width="${v35}"><bean:message key="summary.page.tru" /></td>
											<td class="black_ar" width="15%">${summaryForm.adminDetails.regUsers}</td>
											<td class="black_ar" width="${v35}"><bean:message key="summary.page.tcs" /></td>
											<td class="black_ar" width="15%">${summaryForm.adminDetails.colSites}</td>
										</tr>
										<tr>
											<td class="tabletd1" width="${v35}"><bean:message key="summary.page.tcp" /></td>
											<td class="tabletd1" width="15%">${summaryForm.adminDetails.cpTot}</td>
											<td class="tabletd1" width="${v35}"><bean:message key="summary.page.trps" /></td>
											<td class="tabletd1" width="15%">${summaryForm.adminDetails.repSites}</td>
										</tr>
										<tr>
											<td class="black_ar" width="${v35}"><bean:message key="summary.page.tdp" /></td>
											<td class="black_ar" width="15%">${summaryForm.adminDetails.dpTot}</td>
											<td class="black_ar" width="${v35}"><bean:message key="summary.page.tls" /></td>
											<td class="black_ar" width="15%">${summaryForm.adminDetails.labSites}</td>
										</tr>
									</table>
								</td>
							</tr>
<!-- Mandar : 4Feb09 Admin general count details end -->
							<tr>
								<td width="100%" colspan="1">&nbsp;</td>
							</tr> 
<!-- Mandar : 4Feb09 Participant details start -->
							<tr class="tr_h1_bggray">
								<td width="100%" colspan="1" class="gray_h1_md"><bean:message key="summary.page.prDetails" /></td>
							</tr>
							<tr class="boldText">
							    <td width="100%">
								    <table width="100%"><tr class="boldText"><td width="${v50}"><bean:message key="summary.page.trp" /></td>
									<td> ${summaryForm.partDetails.totPartCount}</td></tr></table>
								</td>
							</tr>
							<!-- Mandar : 4Feb09 Participant by CD details start -->
							<tr class="tr_h3_bggray">
								<td class="gray_h3_md"><bean:message key="summary.page.cd" /> </td>
							</tr>
							<tr>
								<td width="100%">
									<twoCol:twoColDetailsTag formName="summaryForm" displayList="<%=sForm.getPartDetails().getPByCDDetails() %>"
									divId="cddiv" hdrKey="" />
								</td>
							</tr>
							<!-- Mandar : 4Feb09 Participant by CD details end -->
							<tr>
								<td width="100%" colspan="1">&nbsp;</td>
							</tr> 
							<!-- Mandar : 4Feb09 Participant by CS details start -->
							<tr class="tr_h3_bggray">
								<td class="gray_h3_md"><bean:message key="summary.page.cs" /> </td>
							</tr>
							<tr>
								<td width="100%">
									<twoCol:twoColDetailsTag formName="summaryForm" displayList="<%=sForm.getPartDetails().getPByCSDetails() %>"
									divId="csdiv" hdrKey="" />
								</td>
							</tr>
							<!-- Mandar : 4Feb09 Participant by CS details end -->
							
<!-- Mandar : 4Feb09 Participant details end -->
							<tr>
								<td width="100%">&nbsp;</td>
							</tr>
<!-- Mandar : 4Feb09 Specimen details start -->
							<tr class="tr_h1_bggray">
								<td width="100%" colspan="1" class="gray_h1_md"><bean:message key="summary.page.spDetails" /></td>
							</tr>
							<tr class="boldText">
								<td>
								<table width="100%"><tr class="boldText"><td width="${v50}"><bean:message key="summary.page.caption"/>:</td>
								<td>${summaryForm.totalSpCount}</td></tr></table>
								</td>
							</tr>
							<!-- Mandar : 4Feb09 Cell Specimen details start -->
							<tr class="tr_h3_bggray">
								<td class="gray_h3_md">
								<table width="100%"><tr class="gray_h3_md"><td width="${v50}"><bean:message key="summary.page.total.cell.count"/>:
								</td> <td>${summaryForm.specDetails.cellCount}</td></tr></table>
								</td>
							</tr>
							<tr>
								<td width="100%">
									<twoCol:twoColDetailsTag formName="summaryForm" displayList="<%=sForm.getSpecDetails().getCellTypeDetails()
									%>" divId="spCldiv" hdrKey=""/>
								</td>
							</tr>
							<!-- Mandar : 4Feb09 Cell Specimen details end -->
							<tr>
								<td width="100%" colspan="1">&nbsp;</td>
							</tr> 
							<!-- Mandar : 4Feb09 Fluid Specimen details start -->
							<tr class="tr_h3_bggray">
								<td class="gray_h3_md">
								<table width="100%"><tr class="gray_h3_md"><td width="${v50}"><bean:message  key="summary.page.total.fluid.count"
								/>:</td><td>${summaryForm.specDetails.fluidCount}</td></tr></table>
								</td>
							</tr>
							<tr>
								<td width="100%">
									<twoCol:twoColDetailsTag formName="summaryForm" displayList="<%=sForm.getSpecDetails().getFluidTypeDetails()
									%>" divId="spFldiv" hdrKey=""/>
								</td>
							</tr>
							<!-- Mandar : 4Feb09 Fluid Specimen details end -->
							<tr>
								<td width="100%" colspan="1">&nbsp;</td>
							</tr> 
							<!-- Mandar : 4Feb09 Tissue Specimen details start -->
							<tr class="tr_h3_bggray">
								<td class="gray_h3_md">
								<table width="100%"><tr class="gray_h3_md"><td width="${v50}"><bean:message key="summary.page.total.tissue.count"
								/>:</td><td> ${summaryForm.specDetails.tissueCount}</td></tr></table>
								</td>
							</tr>
							<tr>
								<td width="100%">
									<twoCol:twoColDetailsTag formName="summaryForm" displayList="<%=sForm.getSpecDetails().getTissueTypeDetails()
									%>" divId="spTsdiv" hdrKey=""/>
								</td>
							</tr>
							<!-- Mandar : 4Feb09 Tissue Specimen details end -->
							<tr>
								<td width="100%" colspan="1">&nbsp;</td>
							</tr> 
							<!-- Mandar : 4Feb09 Molecular Specimen details start -->
							<tr class="tr_h3_bggray">
								<td class="gray_h3_md">
								<table width="100%"><tr class="gray_h3_md"><td width="${v50}"><bean:message key="summary.page.total.molecular.count"/>:</td><td> ${summaryForm.specDetails.moleculeCount} </td></tr></table>
								</td>
							</tr>
							<tr>
								<td width="100%">
									<twoCol:twoColDetailsTag formName="summaryForm" displayList="<%=sForm.getSpecDetails().getMolTypeDetails() %>"
									divId="spModiv" hdrKey=""/>
								</td>
							</tr>
							<!-- Mandar : 4Feb09 Molecular Specimen details end -->
							<tr>
								<td width="100%">&nbsp;</td>
							</tr>
							<!-- Mandar : 4Feb09 Specimen by PathStat details start -->
							<tr class="tr_h3_bggray">
								<td class="gray_h3_md"><bean:message key="summary.page.spPathStat" /> </td>
							</tr>
							<tr>
								<td width="100%">
									<twoCol:twoColDetailsTag formName="summaryForm" displayList="<%=sForm.getSpecDetails().getPatStDetails() %>"
									divId="spPSdiv" hdrKey=""/>
								</td>
							</tr>
							<!-- Mandar : 4Feb09 Specimen by PathStat details end -->
							<tr>
								<td width="100%" colspan="1">&nbsp;</td>
							</tr> 
							<!-- Mandar : 4Feb09 Specimen by Anot. Site details start -->
							<tr class="tr_h3_bggray">
								<td class="gray_h3_md"><bean:message key="summary.page.spAnatSite" /> </td>
							</tr>
							<tr>
								<td width="100%">
									<twoCol:twoColDetailsTag formName="summaryForm" displayList="<%=sForm.getSpecDetails().getTSiteDetails() %>"
									divId="spAsdiv" hdrKey=""/>
								</td>
							</tr>
							<!-- Mandar : 4Feb09 Specimen by Anot. Site details end -->

<!-- Mandar : 4Feb09 Specimen details end -->
						
						</table>
					</td>
				</tr>
			</table>
<!-- container td complete  -->			
		</td>
	</tr>
</table>	

