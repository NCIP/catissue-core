<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="edu.wustl.catissuecore.bizlogic.magetab.MagetabExportWizardBean"%>

<% 
MagetabExportWizardBean wizardBean = (MagetabExportWizardBean)session.getAttribute(
		MagetabExportWizardBean.MAGETAB_EXPORT_WIZARD_BEAN);
%>
<table height="100%" width="100%">
	<tr>
		<td align="left" class="tr_bg_blue1">
			<span class="blue_ar_b">
				&nbsp;Extract Type
			</span>
		</td>
	</tr>
	<tr>
		<td class="black_ar">Select the type for extract
		</td>
	</tr>
	<tr height="100%">
		<td>
			<div style="height: 100%; width: 100%; overflow: auto;">
<table width="100%">
	<tr>
		<td class="black_new">
		<% if (wizardBean.isDna()) {%>
			<input type="radio" name="defaultExtractType" value="DNA" <%="DNA".equals(wizardBean.getDefaultExtractType()) ? " checked=\"checked\"" : "" %> >
				<bean:message key="magetab.dna"/> 
			</input>
		<%}%>
			<br>
		<% if (wizardBean.isRna()) {%>
			<input type="radio" name="defaultExtractType" value="RNA" <%="RNA".equals(wizardBean.getDefaultExtractType()) ? " checked=\"checked\"" : "" %> >
				<bean:message key="magetab.rna"/>
			</input>
		<%}%>
		</td>
	</tr>
        <tr>
            <td class="black_new">
                <input type="checkbox" name="caArrayEnabled" value="Yes" checked="true"/>Do not include biospecimens that have incomplete information about sources, samples, and extracts.
            </td>
        </tr>
        
</table>
			</div>
		</td>
	</tr>
</table>
			