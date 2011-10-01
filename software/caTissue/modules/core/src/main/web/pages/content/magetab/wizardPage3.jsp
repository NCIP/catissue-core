<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="edu.wustl.catissuecore.bizlogic.magetab.MagetabExportWizardBean"%>
<%@ page import="edu.wustl.catissuecore.bizlogic.magetab.TransformerSelections"%>
<%@ page import="java.util.Map"%>

<!-- 
<STYLE>
TABLE {
border : 0px solid black;
border-collapse: collapse;
font-size : 11px;
font-family : Tahoma;
}

TH {
	text-align : left;
	border-width: 0px;
	padding: 3px;
	border-style: solid;
	border-color: gray;
}

TD {
	text-align : center;
	border-width: 0px;
	padding: 2px;
	border-style: solid;
	border-color: gray;
}
</STYLE>
 -->
<table height="100%" width="100%">
	<tr>
		<td align="left" class="tr_bg_blue1">
			<span class="blue_ar_b">
				&nbsp;Attributes and Characteristics
			</span>
		</td>
	</tr>
	<tr>
		<td class="black_ar">
			Select the attributes and characteristics to export
		</td>
	</tr>
        <tr>
            <td class="black_ar">
                
            </td>
        </tr>
	<tr height="100%">
		<td>
			<div style="height: 100%; width: 100%; overflow: auto;">
<table width="100%">
<% 
MagetabExportWizardBean wizardBean = (MagetabExportWizardBean)session.getAttribute(
		MagetabExportWizardBean.MAGETAB_EXPORT_WIZARD_BEAN);
String displayColumns = wizardBean.getDisplayColumns();
boolean source = false;
boolean sample = false;
boolean extract = false ; 

if (displayColumns.indexOf("Source") != -1) {
	source = true;
}
if (displayColumns.indexOf("Sample") != -1) {
	sample = true;
}
if (displayColumns.indexOf("Extract") != -1) {
	extract = true;
}
%>
<input type="hidden" name="sourceColumnSelected" value="<%=source%>"/>
<input type="hidden" name="sampleColumnSelected" value="<%=sample%>"/>
<input type="hidden" name="extractColumnSelected" value="<%=extract%>"/>

<tr class="tableheading">
	<th class="black_ar_b">&nbsp;</th>
	<%if(source) {%><th class="black_ar_b">Source</th><%} %>
	<%if(sample) {%><th class="black_ar_b">Sample</th><%} %>
	<%if(extract) {%><th class="black_ar_b">Extract</th><%} %>
</tr>
<% 
Map<String, TransformerSelections> selections = wizardBean.getTransformersSelections();
for (Map.Entry<String, TransformerSelections> sel : selections.entrySet()) {
%>
<tr>
	<th class="black_new" align="left" title="<%= sel.getValue().getLocalName() %>"><%=sel.getValue().getUserFriendlyName() +"&nbsp;("+sel.getValue().getLocalName()+")" %></th>
	<%if(source) {%>
		<td class="black_new" align="center"><input type="checkbox" name="transformers" value="<%="chk_source_" + sel.getKey() %>" <%=sel.getValue().isSelectedForSource() ? " checked=\"checked\"" : "" %> ></input></td>
	<%} %>
	
	<%if(sample) {%>
		<td class="black_new" align="center"><input type="checkbox" name="transformers" value="<%="chk_sample_" + sel.getKey() %>" <%=sel.getValue().isSelectedForSample() ? " checked=\"checked\"" : "" %> ></input></td>
	<%} %>
	
	<%if(extract) {%>
		<td class="black_new" align="center"><input type="checkbox" name="transformers" value="<%="chk_extract_" + sel.getKey() %>" <%=sel.getValue().isSelectedForExtract() ? " checked=\"checked\"" : "" %> ></input></td>
	<%} %>
</tr>

<%
}	
%>

</table>
			</div>
		</td>
	</tr>
</table>
			