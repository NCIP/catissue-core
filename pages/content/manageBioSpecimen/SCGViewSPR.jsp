<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>



<%@ page import="edu.wustl.catissuecore.actionForm.SpecimenCollectionGroupForm"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>

<head>
<style>
.active-column-1 {width:200px}
</style>
<script src="jss/fileUploader.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/jss/ajax.js" type="text/javascript"></script>

<%
	String pageOf = (String)request.getAttribute(Constants.PAGE_OF);

	String operation = (String)request.getAttribute(Constants.OPERATION);

		String formAction = Constants.VIEW_SPR_ACTION;



String id = request.getParameter("id");


%>
<script>

	var download = function(type){
			alert(document.getElementsByName("identifiedReportId")[0].value);
			var dwdIframe = document.getElementById("sprExportFrame");
			dwdIframe.src = "ExportSprAction.do?scgId=${scgId}&reportId="+document.getElementsByName("identifiedReportId")[0].value+"&type="+type;
			
		}
</script>



	<table width="100%" border="0" cellpadding="0" cellspacing="0" class="maintable">
		  <tr>
		    <td class="td_color_bfdcf3"></td>
		  </tr>
		  <tr>
			<td>
				<script>
					var upload = function() {
						var uploader = new FileUploader({
							element: document.getElementById('sprSCGReport'),
							endpoint: 'UploadSprReport.do?type=getSpecimenIds',
							params:{scgId:"${scgId}"},
							onComplete:function(response){
								if(response.success = "true"){
									var action="<%=Constants.VIEW_SPR_ACTION%>?scgId=${scgId}&operation=viewSPR&pageOf=<%=pageOf%>&reportId="+response.reportId;
									document.forms[0].action=action;
									document.forms[0].submit();
								}else{
									alert(response.errorMessage);
									
								}
							}
						});
					};
					
					</script>
					<!--form action="/" method="post" onsubmit="return upload();"-->
				<table id="uploadSCGTable" width="100%" border="0" cellpadding="0" cellspacing="0" class="whitetable_bg">
					<tr>
						  <td class="tr_bg_blue1">
							<span class="blue_ar_b"> &nbsp;Upload SPR SCG Report&nbsp;</span>
						  </td>
					</tr>
					<tr>
						<td>
							<div style="margin-left: 10px; margin-top: 10px;">
								<input type="file" name="sprSCGReport" id="sprSCGReport">
								<input type="submit" value="Upload" onclick="upload()">
							</div>
						</td>
					</tr>
				</table>
					
				<table id="downloadSCGTable" style="display:none;" width="100%" border="0" cellpadding="0" cellspacing="0" class="whitetable_bg black_ar_b">
					<tr>
						  <td class="tr_bg_blue1">
							<span class="blue_ar_b"> &nbsp;Download SPR SCG Report&nbsp;</span>
						  </td>
					</tr>
					<tr>
						<td>
							<div style="margin-left: 10px; margin-top: 10px;">
								<div style="float:left;margin-top:3px;">
								<span>Existing SPR report : </span>&nbsp
								<span id="existingSprName" style="font-weight:normal;"></span>
								</div>
								<div style="float:left;">
									<a href="#" style="border:0px"><img  style="border:0px" src='images/delete-alt.png'  title="Click to delete report." onclick='deleteReport()'/></a>
								</div>
							</div>
						</td>
					</tr>
				</table>		
				
				<html:form action="<%=formAction%>">
				<table width="100%" border="0" cellpadding="0" cellspacing="0" class="whitetable_bg">
				<tr>
				<td>
					<%@ include file="/pages/content/common/ActionErrors.jsp" %>
				</td>
				</tr>

				<tr>
				<td colspan="0">
					
					<%@include file="ViewSurgicalPathologyReport.jsp" %>
				<!--</td>
				</tr>
				</table>-->
				</td>
			</tr>
		</table>
</html:form>

