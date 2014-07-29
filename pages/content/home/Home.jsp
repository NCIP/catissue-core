<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants,edu.wustl.common.util.XMLPropertyHandler,edu.wustl.common.util.global.ApplicationProperties,edu.wustl.catissuecore.util.global.Variables,edu.wustl.common.beans.SessionDataBean"%>


<link rel="stylesheet" type="text/css" href="css/login.css" />
<link rel="stylesheet" type="text/css" href="css/login-theam.css" />
<style type="text/css">
table#browserDetailsContainer {
    font-family: arial, helvetica, verdana, sans-serif;
    font-size: 0.7em;
    padding-left: 10px;
}
</style>
<script language="JavaScript">
   
var pageOf="${param.pageOf}";
var file = "${param.file}";
var url="";
var  downloadUrl= "";
  
    function forgotId()
    {
        var url= "https://connect.wustl.edu/login/wuforgotID.aspx";
        window.open(url,'WustlLoginForm','height=630,width=800,scrollbars=1,resizable=1');
    }

    function forgotPassword()
    {
        var url= "https://connect.wustl.edu/login/wuforgotpwd.aspx";
        window.open(url,'WustlLoginForm','height=630,width=800,scrollbars=1,resizable=1');
    }


</script>

<table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td colspan="5" class="td_orange_line" height="1"></td>
    </tr>
    <tr>
        <td width="23%" align="center" valign="top"><br />
        <table width="95%" border="0" cellpadding="0" cellspacing="0">
            <tr>
                <td  valign="top">
                    
                    </br>
                    </br>
                    
                    
                    <div class="black_ar box-border box-content box-class" style="width:231px;">
                        <div class="help-header theam-font-color container-header-spacing" >
                            <span><bean:message key="login.help.desk" /></span>
                        </div>
                        
                        <div class="help-summary">
                            <div class="container-text container-text-top-margin">
                                <img height="14px" src="images/uIEnhancementImages/contact.png">
                                &nbsp&nbsp<span id="contactNumber"></span>
                            </div>
                            
                        <div class="container-text  container-text-top-margin" >
                                <img  height="14px" src="images/uIEnhancementImages/ic_mail.gif">
                                &nbsp <a id="contactus" href="mailto:"><bean:message key="login.contact.us" /></a>
                        </div>
                            
        <div class="container-text  container-text-top-margin">
                                <img  height="14px" src="images/uIEnhancementImages/help-book-open.png">
                                &nbsp&nbsp <a id="userManual" href=""  target="_blank"><bean:message key="login.usermanual" /></a>
                                
                            </div>
                            <br>
                            
                            <!--ul class="container-ul">
                                <li><div style="float:left; margin-top: 2px;"><img src="images/uIEnhancementImages/contact.png"></div><div  style="float:left;">&nbsp +61 2 9385 1493</div></li>
                                <li><a href="ContactUs.do?PAGE_TITLE_KEY=app.contactUs&amp;FILE_NAME_KEY=app.contactUs.file">Email</a></li>
                                <li>User Manual</li>
                            </ul-->
                            
                        </div>
                        
                    </div>
                    
                     <div class="black_ar box-border box-content box-class" style="width:400px;display:none;" id="dataAtGlanceDiv" >
                        <div class="help-header theam-font-color container-header-spacing" >
                            <span><bean:message key="login.data.at.glance" /></span>
                        </div>
                        
                        <div class="help-summary" style="width:40%;float:left;">
                            <div  class="container-text container-text-top-margin">
                                <span><bean:message key="login.users" /></span>&nbsp<span class="theam-font-color-orange" id="userCount"></span>
                            </div>
                            <div  class="container-text container-text-top-margin">
                                <span><bean:message key="login.protocols" /></span>&nbsp<span class="theam-font-color-orange" id="protocolCount"></span>
                            </div>
                            <div  class="container-text container-text-top-margin">
                                <span><bean:message key="login.participants" /></span>&nbsp<span class="theam-font-color-orange" id="participantCount"></span>
                            </div>
                            
                            <div  class="container-text container-text-top-margin">
                                <span><bean:message key="login.specimens" /></span>&nbsp<span class="theam-font-color-orange" id="specimensCount"></span>
                            </div>
                        
                            <!--ul class="container-ul">
                                <li>Protocols (2)</li>
                                <li>Participants (300)</li>
                                <li>Specimen(4000)</li>
                                login.fluid.specimens=Fluid Specimens
login.tissue.specimens=Tissue Specimens
login.molecular.specimens=Molecular Specimens
login.detailed.summary=Detailed Summary
login.forgot.password=Forgot password?
                            </ul-->
                        </div>
                        <div class="help-summary" style="width:59%; float: left;" >
                            <div class="container-text container-text-top-margin">
                                <span><bean:message key="login.cell.specimens" /></span>&nbsp<span class="theam-font-color-orange" id="cellsCount"></span>
                            </div>
                            <div class="container-text container-text-top-margin">
                                <span><bean:message key="login.fluid.specimens" /></span>&nbsp<span class="theam-font-color-orange" id="fluidCount"></span>
                            </div>
                            
                            <div class="container-text container-text-top-margin">
                                <span><bean:message key="login.tissue.specimens" /></span>&nbsp<span class="theam-font-color-orange" id="tissueCount"></span>
                            </div>
                            
                            <div  class="container-text container-text-top-margin">
                                <span><bean:message key="login.molecular.specimens" /></span>&nbsp<span class="theam-font-color-orange" id="molecularCount"></span>
                            </div>
                        
                        
                            <!--ul class="container-ul" >
                                <li>Cell Specimen(10)</li>
                                <li>Tissue Specimen(5)</li>
                                <li>Moleculer Specimen(3)</li>
                            </ul-->
                            
                        <a href="Summary.do" style="text-decoration: none"><span  class="black_ar detail-summary-link theam-font-color-orange"><bean:message key="login.detailed.summary" /></span></a>
                        </div>

                        
                        
                    </div>
                    <br/>
                </td>
        
        <td width="28%"><br />
        
        <div class="black_ar login-box-content box-border">
            <!--span class="signin">Sign in</span-->
            <%@ include file="/pages/content/common/ActionErrors.jsp"%>
            <html:form styleId="form1" action="/Login.do">
                             <logic:notEmpty scope="session" name="<%=Constants.SESSION_DATA%>">
                              <script>
                               if(pageOf!=null && pageOf!='null'  && pageOf == "pageOfDownload")
                              {
                                url = "Home.do?pageOf="+pageOf+"&file="+file;
                                document.getElementById("form1").action = url;
                                document.getElementById("form1").submit();
                                
                              }
                             </script>
                            </logic:notEmpty>
                            <logic:empty scope="session" name="<%=Constants.SESSION_DATA%>">
                             <script>  
                              if(pageOf!=null && pageOf!='null'  && pageOf == "pageOfDownload")
                              {
                                url = "Login.do?pageOf="+pageOf+"&file="+file;
                                document.getElementById("form1").action = url;                              
                              }
                             </script>
                            </logic:empty>   

                <%
                    String redirectTo =(String) request.getParameter("redirectTo"); 
                    String pageOf=null;
                    if(request.getParameter("pageOf")!=null)
                    {
                        pageOf=(String) request.getParameter("pageOf");
                    }
                    if(redirectTo != null){ 
                %>
                        <input type='hidden' name='redirectTo' value='<%=redirectTo%>'/>
                <% 
                    }  
                %>      
                <div class="login-inner-div">
                <span class="login-label"><bean:message key="app.UserID" /></span>
                <input type="text" class="black_ar text-field-login" name="loginName" autocomplete="off"/>
                </div>
                <div class="login-inner-div">
                <span class="login-label"><bean:message key="app.password" /></span>
                <input type="password" class="black_ar text-field-login" name="password" autocomplete="off"/>
                </div>
                <div class="login-inner-div" style="overflow:auto;">
                    <div style="width:60px;float:left">
                        <input name="Submit" type="submit" class="black_ar login-button" value="Login" /> 
                        <a href="#" class="blue"><span class="wh_ar_b"></span></a>
                    </div>
                    <div class="signup-link-div">
                        <%
                            if (Boolean.parseBoolean(XMLPropertyHandler
                                                    .getValue(Constants.IDP_ENABLED))) {
                        %>
                            <a href="SignUp.do?operation=add&pageOf=pageOfSignUp" style="text-decoration: none;"> <span  class="black_ar theam-font-color-orange"><bean:message key="app.signup" /></span></a>
                            &nbsp&nbsp&nbsp
                        <%
                            } else {
                        %>
                            <a href="SignUp.do?operation=add&amp;pageOf=pageOfSignUp" style="text-decoration: none;">
                                <span  class="black_ar theam-font-color-orange"> <bean:message key="app.signup" /></span></a>&nbsp&nbsp&nbsp
                        <%
                            }
                            if (Boolean.parseBoolean(XMLPropertyHandler
                                                    .getValue(Constants.FORGOT_PASSWORD_ENABLED))) {
                        %>
                            <a href="ForgotPassword.do" style="text-decoration: none"><span  class="black_ar theam-font-color-orange"><bean:message key="login.forgot.password" /></span></a>
                    <% }%>
                        
                        
                    </div>
                </div>
            </html:form>
        </div>
        

        <br/>
        </td>
    </tr>
    <tr>
        <!--        <td align="center" valign="bottom">&nbsp;<img border="0" src="images/pspllogo.gif" width="75" height="71"></td>
 -->
        <!--td align="center" valign="bottom">&nbsp</td-->
        <!--td rowspan="2" align="right" valign="bottom">
        <table width="96%" border="0" cellspacing="0" cellpadding="0">
            <tr>
                <td class="black_ar"><bean:message key="app.adobe.required" /></td>
            </tr>
            <tr>
                <td>&nbsp;</td>
            </tr>
            <tr>
                <td class="blue_ar_b"><bean:message
                    key="app.certified.browsers" /></td>
            </tr>

            <tr>
                <td class="black_ar"><img
                    src="images/uIEnhancementImages/logo_ie.gif"
                    alt="Internet Explorer 9.0" width="16" height="16" hspace="3"
                    vspace="3" align="absmiddle" /><bean:message
                    key="app.certified.ie" /><br />
                <img src="images/uIEnhancementImages/logo_firefox.gif"
                    alt="Mozilla Firefox-10.0.1 " width="16" height="16" hspace="3"
                    vspace="3" align="absmiddle" /><bean:message
                    key="app.certified.mozilla" /> <br />
                <img src="images/uIEnhancementImages/logo_safari.gif"
                    alt="Mac Safari 5.1.2 " width="16" height="16" hspace="3"
                    vspace="3" align="absmiddle" /><bean:message
                    key="app.certified.mac" /></td>
            </tr>
            <tr>
                <td>&nbsp;</td>
            </tr>
        </table>
        </td-->
        
    </tr>
    
    <tr>
        <td align="left" valign="bottom" style="padding-top:45px;">
                <table width="100%" border="0" cellspacing="0" cellpadding="4">
                    <tr>

                        <td align="left" width="70%">
                        
                            <!--a  class="logo-margin"  href="https://cabig.nci.nih.gov/"><img
                            src="images/uIEnhancementImages/CaBIGLogo.JPG"
                            alt="National Institues of Health" 
                            border="0" /></a-->
                            <a  class="logo-margin"  href="http://krishagni.com"  target="_blank">
                            <img
                            src="images/uIEnhancementImages/krishagni_logo.png"
                            alt="" border="0"  /></a>
                            <a   class="logo-margin" id="instituteLink" href="" style="display:none;"  target="_blank"><img
                            src=""
                            alt="" order="0" id="InstituteLogo"/></a>
                            
                            <br />
                        </td>
                    </tr>
                </table>
                
        <br>
        </td>
        <!--td  align="right" width="30%">
                             <div style="position: absolute;bottom: 54px;right:121px;">
                                <a href="http://www.linkedin.com/company/krishagni-solutions-pvt-ltd/catissue-support-services-807487/product"><img
                                src="images/uIEnhancementImages/linked_buttons.png"
                                alt="" border="0"  /></a>
                            </div>
                        </td-->
    </tr>
    <!--tr>
        <td colspan="5" align="center" valign="top" bgcolor="#bcbcbc"><img
            src="images/uIEnhancementImages/spacer.gif" alt="Spacer" width="1"
            height="1" /></td>
    </tr-->

</table>
<br/>
<div class="footer-class black_ar">
        <div style="padding-top:18px;"><span>OpenSpecimen is an open source biobanking software built on top of NCI caTissue. For more details visit <a href="http://www.catissueplus.org">www.openspecimen.org</a> or mail us at <a href="mailto:support@krishagni.com">support@krishagni.com</a></span></div>
</div>
<script>

window.onload = homePageLoad();

function homePageLoad(){
  //  loadSummaryCount();
    loadInstituteLogo();
    var rv = getInternetExplorerVersion();
    if(rv>9){
        alert("WARNING! Browser version not supported. Use IE9 or lower.");
    }
}
function getInternetExplorerVersion()
// Returns the version of Windows Internet Explorer or a -1
// (indicating the use of another browser).
{
   var rv = -1; // Return value assumes failure.
   if (navigator.appName == 'Microsoft Internet Explorer')
   {
      var ua = navigator.userAgent;
      var re  = new RegExp("MSIE ([0-9]{1,}[\.0-9]{0,})");
      if (re.exec(ua) != null)
         rv = parseFloat( RegExp.$1 );
   }
   return rv;
}
function loadInstituteLogo(){
    var request = new  XMLHttpRequest();
    request.onreadystatechange=function(){
        if(request.readyState == 4)
        {  
            //Response is ready
            if(request.status == 200)
            {
                var responseString = request.responseText;
                var myJsonResponse = eval('(' + responseString + ')');
                document.getElementById("InstituteLogo").src = myJsonResponse.logo;
                document.getElementById("instituteLink").href =  myJsonResponse.link;
                document.getElementById("instituteLink").style.display="";
                document.getElementById("userManual").href =  myJsonResponse.userManual;
                document.getElementById("contactNumber").innerHTML = myJsonResponse.contactNumber;
                document.getElementById("whatsNew").href =  myJsonResponse.whatsNew;
				document.getElementById("contactus").href="mailto:"+myJsonResponse.contactus;
				if(myJsonResponse.dataAtGlanceDiv != undefined && myJsonResponse.dataAtGlanceDiv == "true"){
					document.getElementById("dataAtGlanceDiv").style.display="";
					loadSummaryCount();
				}else{
					document.getElementById("dataAtGlanceDiv").innerHTML = "";
				}
                
                
            }
            
            
        }   
    };
    request.open("POST","CatissueCommonAjaxAction.do?type=getInstituteLogoName",true);
    request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
    request.send();

}
function loadSummaryCount() {
        var request = new  XMLHttpRequest();
            request.onreadystatechange=function(){
                if(request.readyState == 4)
                {  
                    //Response is ready
                    if(request.status == 200)
                    {
                        var responseString = request.responseText;
                        var myJsonResponse = eval('(' + responseString + ')');
                               
                        document.getElementById("userCount").innerHTML = myJsonResponse.TotalUserCount;
                        document.getElementById("protocolCount").innerHTML = myJsonResponse.TotalCPCount;
                        document.getElementById("participantCount").innerHTML = myJsonResponse.TotalParticipantCount;
                        document.getElementById("specimensCount").innerHTML = myJsonResponse.TotalSpecimenCount;
                        document.getElementById("cellsCount").innerHTML = myJsonResponse.CellCount;
                        document.getElementById("fluidCount").innerHTML = myJsonResponse.FluidCount;
                        document.getElementById("tissueCount").innerHTML = myJsonResponse.TissueCount
                        document.getElementById("molecularCount").innerHTML = myJsonResponse.MoleculeCount;
                        
                    }
                    interVeil.style.display="none";
                    
                }   
            };
            request.open("POST","CatissueCommonAjaxAction.do?type=getSummaryCount",true);
            request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
            request.send();
            
    }

</script>