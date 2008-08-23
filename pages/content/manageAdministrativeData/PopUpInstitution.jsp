<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page language="java" isELIgnored="false" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>	

<%@ page import="edu.wustl.catissuecore.actionForm.InstitutionForm"%>

<link rel="stylesheet" type="text/css" href="css/catissue_suite.css" />	
<script src="jss/ajax.js" type="text/javascript"></script>
<script type="text/javascript">
function addInstitution()
{
    var name = document.getElementById("name").value;
    var request = newXMLHTTPReq();
	if(request == null)
    {
		alert ("Your browser does not support AJAX!");
		return;
	}
	var handlerFunction = getReadyStateHandler(request,setInstitutionValues,true);
    request.onreadystatechange = handlerFunction;
	var param = "instituteName="+name;
    var url = "AddInstitution.do";
 	request.open("POST",url,true);
	
	request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");	
	request.send(param);
	return false;

}
function setInstitutionValues(response)
{
	var values = response.split("#@#");
	if(values.length == 1)
	{
        var divTag = document.getElementById("errorDiv");
        divTag.innerHTML = "<font size='3' color='red'>"+values[0]  +"</font>"; 
	}
	else
	{
		institutionCtrl = window.parent.document.getElementById("institutionId");
        institutionCtrl.value = values[0];
	
		displayInstitutionCtrl = window.parent.document.getElementById("displayinstitutionId");    
	    displayInstitutionCtrl.value =values[1];

		//To hide the Modal Window opened from 
		parent.institutionwindow.hide();
    }
}

function cancelWindow()
{
  parent.institutionwindow.hide();
}
</script>

<!--begin content -->
<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
<tr>
          <td> <div id="errorDiv"> </div></td>
	    </tr>		
	<html:form action='/InstitutionAdd.do' onsubmit="return addInstitution()"> 
	<html:hidden property="operation" />
				<html:hidden property="submittedFor"/>
				<html:hidden property="id" />
  <tr>
    <td align="left"><table width="100%" border="0" cellpadding="3" cellspacing="0">
      <tr>
        <td><%@ include file="/pages/content/common/ActionErrors.jsp" %></td>
      </tr>
    </table></td>
  </tr>
  <tr>
    <td align="left" class="tr_bg_blue1"><span class="blue_ar_b">&nbsp;<bean:message key="user.institution"/></span></td>
  </tr>
  <tr>
    <td align="left" class="showhide">
	<div id="part_det" >
      <table width="100%" border="0" cellpadding="3" cellspacing="2">
        
          <tr>
            <td width="1%" align="center" class="black_ar"><span class="blue_ar_b"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /></span></td>
            <td width="12%" align="left" class="black_ar"><label for="name">
					<bean:message key="institution.name"/>
				</label></td>
            <td width="87%" align="left"><label>
              <html:text styleClass="black_ar" maxlength="255"  size="50" styleId="name" property="name"/>
            </label></td>
          </tr>
        
      </table></div>    </td>
  </tr>
  
  <tr >
    <td class="buttonbg">
      <html:button property="clickButton" styleClass="blue_ar_b" onclick="addInstitution();" >
	      <bean:message  key="buttons.submit" /> 
      </html:button>
      &nbsp;| <html:link href="#" styleClass="cancellink" onclick= "cancelWindow()">
													<bean:message key="buttons.cancel" />
												</html:link></td>
  </tr>
  </html:form>
</table><!--end content -->
