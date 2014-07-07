<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>



<head>
<style>
.active-column-1 {width:200px}
</style>
<script src="jss/fileUploader.js" type="text/javascript"></script>
<script src="jss/ajax.js" type="text/javascript"></script>
<link href="css/catissue_suite.css" rel="stylesheet" type="text/css" />

<script>
var upload = function() {
var uploader = new FileUploader({
	element: document.getElementById('sprSCGReport'),
	endpoint: '<%=request.getContextPath()%>/CatissueCommonAjaxAction.do?type=uploadConsent',
	params:{},
	onComplete:function(response){
		if(response.success = "true"){
			alert("File uploaded successfully");
		}else{
			alert("Error occured while uploading file");
		}
	}
});
};
var downloadTemplate = function(){
var dwdIframe = document.getElementById("exportTemplateFrame");
dwdIframe.src = '<%=request.getContextPath()%>/CatissueCommonAjaxAction.do?type=downloadConsentTemplate';
}
</script>


<div  class="tr_bg_blue1" style=" padding-top: 7px;margin-left: 10px;margin-left: 10px; margin-top: 10px;width:100%;min-width:100%">
	<span class="black_ar_b"> Upload Consents Response CSV</span>
</div>
<div style="margin-left: 10px; margin-top: 10px;">
	
	<input type="file" name="sprSCGReport" id="sprSCGReport">
	<input type="submit" value="Upload" onclick="upload()">
	<a href="#" class="black_ar"  onClick="downloadTemplate()" >Click Here</a><span class="black_ar"> to download template</span>
	
</div>
<iframe id = "exportTemplateFrame" width = "0%" height = "0%" frameborder="0">
	</iframe>