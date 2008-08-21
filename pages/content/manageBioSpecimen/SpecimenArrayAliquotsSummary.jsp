<!-- 
	Specimen Array Aliquots summary page.
-->

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.actionForm.SpecimenArrayAliquotForm"%>
<%@ page import="edu.wustl.catissuecore.util.global.Utility"%>
<%@ page import="java.util.*"%>

<script type="text/javascript" src="jss/wz_tooltip.js"></script>
<style type="text/css">
input {
border:0;
}
</style>
<script>
var newWindow;
	function showNewPage(action)
	{
		 document.forms[0].action=action;
		document.forms[0].submit();
    }
</script>
<html:form action="<%=Constants.SPECIMEN_ARRAY_ALIQUOT_ACTION%>">
<%
	SpecimenArrayAliquotForm form = (SpecimenArrayAliquotForm)request.getAttribute("specimenArrayAliquotForm");	
%>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="maintable">
  <tr>
    <td class="td_color_bfdcf3">
		<table border="0" cellpadding="0" cellspacing="0">
	      <tr>
		    <td class="td_table_head">
				<span class="wh_ar_b"> 
					<bean:message key="aliquots.header" />
				</span>
			</td>
			<td>
				<img src="images/uIEnhancementImages/table_title_corner2.gif" alt="Page Title - Specimen Array Aliquot" width="31" height="24" />
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
				<td width="61%" align="left" class="toptd"><%@ include file="/pages/content/common/ActionErrors.jsp" %></td>
			</tr>
	        <tr>
		      <td colspan="2" align="left" class="tr_bg_blue1">
				<span class="blue_ar_b">&nbsp;
					<bean:message key="specimenArrayAliquots.summaryTitle"/>
				</span>
			   </td>
			</tr>
			<tr>
				<td align="left" class="showhide">
					<table width="100%" border="0" cellspacing="0" cellpadding="3">
						<tr>
							<td width="1%" align="center" class="black_ar">
								<img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" />
							</td>
			                <td width="20%" class="black_ar">
								<label for="specimenArrayType">
									<bean:message key="specimenArrayAliquots.specimenArrayType"/> 
								</label>
							</td>
							<td width="20%" align="left">
								<html:text styleClass="black_ar"  maxlength="50"  size="25" styleId="specimenArrayType" property="specimenArrayType" readonly="true"/>
							</td>
							<td width="5%">&nbsp;</td>
							<td width="1%" align="center" class="black_ar">
								<img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" />
							</td>
							<td class="black_ar" width="20%">
								<label for="specimenClass">
									<bean:message key="specimenArrayAliquots.specimenClass"/> 
								</label>
							</td>
							<td align="left" width="33%">
								<html:text styleClass="black_ar"  maxlength="50"  size="25" styleId="specimenClass" property="specimenClass" readonly="true"/>
							</td>
						</tr>
						<tr>
							<td align="center" class="black_ar">
								<img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" />
							</td>
							<td class="black_ar">
								<label for="specimenType">
									<bean:message key="specimenArrayAliquots.specimenType"/> 
								</label>
							</td>
							<td class="black_new">
								<html:select property="specimenTypes" styleClass="formFieldVerySmallSized" styleId="state" size="4" multiple="true" disabled="true">
									<html:options collection="<%=Constants.SPECIMEN_TYPE_LIST%>" labelProperty="name" property="value"/>
								</html:select>
							</td>
							<td colspan="4">&nbsp;</td>
						</tr>	
					</table>
				</td>
	        </tr>
            <tr>
		      <td class="showhide1">
				<table width="100%" border="0" cellspacing="0" cellpadding="4">
	              <tr class="tableheading">
					<td width="2%" align="left" class="black_ar_b">#
					</td>
					<td width="15%" align="left" nowrap="nowrap" class="black_ar_b">
						<bean:message key="specimenArrayAliquots.label"/>
					</td>
					<td width="15%" align="left" nowrap="nowrap" class="black_ar_b">
						<bean:message key="specimenArrayAliquots.barcode"/>
					</td>
					<td width="68%" class="black_ar_b" colspan="3">
						<bean:message key="cpbasedentry.storagelocation"/>
					</td>
				 </tr>
			  
			<%
		
			Map aliquotMap = new HashMap();
			int counter=0;

			if(form != null)
			{
				counter = Integer.parseInt(form.getAliquotCount());
				aliquotMap = form.getSpecimenArrayAliquotMap();
			}

			for(int i=1;i<=counter;i++)
			
			{
				
		%>
				<tr>
					<td align="left" class="black_ar" >
						<%=i%>.
					</td>		
					<td>
			<% 

				Long specimenArrayId=(Long)aliquotMap.get("SpecimenArray:"+i+"_id");
				String onClickSpecimenArrayFunction = "showNewPage('SearchObject.do?pageOf=pageOfSpecimenArray&operation=search&&id="+specimenArrayId+"')";
			%>
						<html:link href="#" styleClass="view" styleId="label" onclick="<%=onClickSpecimenArrayFunction%>">
							<%=aliquotMap.get("SpecimenArray:"+i+"_label")%>
						</html:link>
					</td>
					<td>
						<%=aliquotMap.get("SpecimenArray:"+i+"_barcode")%>
					</td>
					<td class="black_ar">
						<%=aliquotMap.get("SpecimenArray:"+i+"_StorageContainer_name")%>
						&nbsp;
						<%=aliquotMap.get("SpecimenArray:"+i+"_positionDimensionOne")%>
						&nbsp;
						<%=aliquotMap.get("SpecimenArray:"+i+"_positionDimensionTwo")%>
					</td>
				</tr>
			<%
		} //For
	%>
	
			</table>
		</td>
	</tr>
</table>
</html:form>
<!-------------------------------------->

