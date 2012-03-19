<link rel="STYLESHEET" type="text/css" href="dhtmlx_suite/css/dhtmlxgrid.css">
<link rel="STYLESHEET" type="text/css" href="dhtmlx_suite/skins/dhtmlxgrid_dhx_skyblue.css">
<link rel="stylesheet" type="text/css" href="dhtmlx_suite/css/dhtmlxcombo.css">
<script src="dhtmlx_suite/js/dhtmlxcommon.js"></script>
<script src="dhtmlx_suite/js/dhtmlxcombo.js"></script>
<script src="dhtmlx_suite/js/dhtmlxgrid.js"></script>
<script src="dhtmlx_suite/js/dhtmlxgridcell.js"></script>
<script src="dhtmlx_suite/gridexcells/dhtmlxgrid_excell_combo.js"></script>
<script src="dhtmlx_suite/ext/dhtmlxgrid_srnd.js"></script>
<script src="dhtmlx_suite/ext/dhtmlxgrid_filter.js"></script>
<script src="jss/configureCPDashboard.js"></script>

<html:hidden property="dashboardLabelJsonValue" styleId="dashboardLabelJsonValue" />
<table style="display:none;" cellpadding="5" cellspacing="0" border="0" class="contentPage" height="385" width="100%" id="defineDashboardItemsTable">
	<tbody height="100%">
		<tr height="100%">
			<td>
				<table summary="" cellpadding="0" cellspacing="0" border="0"  id ="CStable1" width="100%">
					<tr bgcolor="#ffffff">
						<td colspan="7">
							<table summary="" cellpadding="3" cellspacing="0" border="0" width="100%">
								<tr>
									<td colspan="6" align="left" class="tr_bg_blue1" width="90%">
									<span class="black_new"><b>* Double Click to edit a row</b></span>
									</td>
									<td align="right" class="tr_bg_blue1" width="5%">
									<html:button property="addSpecimenReq" styleClass="black_new" styleId="addSpecimenReq" value="Add More" onclick="addCSLevelFormRow()"/>
									</td>
									<td class="tr_bg_blue1" align="Right" width="5%">
									<html:button property="deleteStudyForm" styleClass="black_new" styleId="deleteStudyForm" onclick="checkValue()">
									<bean:message key="buttons.delete"/>
								</html:button>
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td>
							<div id="cpDashboard_container" style="width:100%;height:320px;"></div>
						</td>
					</tr>
				</table>
				
			</td>
		</tr>
		
	</tbody>
</table>
<%-- Main table End --%>