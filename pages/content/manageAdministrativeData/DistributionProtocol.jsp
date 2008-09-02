<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/nlevelcombo.tld" prefix="ncombo" %>

<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.actionForm.DistributionProtocolForm"%>
<%@ page import="java.util.*"%>
<%@ page import="edu.wustl.common.beans.NameValueBean"%>
<%@ page import="edu.wustl.catissuecore.util.global.Utility"%>

<%@ include file="/pages/content/common/AdminCommonCode.jsp" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<link href="css/catissue_suite.css" rel="stylesheet" type="text/css" /> 
<script src="jss/javaScript.js"></script>
<%
	
	List tissueSiteList = (List) request.getAttribute(Constants.TISSUE_SITE_LIST);

	List pathologyStatusList = (List) request.getAttribute(Constants.PATHOLOGICAL_STATUS_LIST);
	
    String operation = (String) request.getAttribute(Constants.OPERATION);
	String submittedFor=(String)request.getAttribute(Constants.SUBMITTED_FOR);
    
	String formName;


    boolean readOnlyValue;
    if (operation.equals(Constants.EDIT))
    {
        formName = Constants.DISTRIBUTIONPROTOCOL_EDIT_ACTION;
        readOnlyValue = false;
    }
    else
    {
        formName = Constants.DISTRIBUTIONPROTOCOL_ADD_ACTION;
        readOnlyValue = false;
    }
	    String currentDistributionProtocolDate="";
		String distributionProtocolEndDate="";
		Object obj = request.getAttribute("distributionProtocolForm");
		int noOfRows=0;
		Map map = null;
		DistributionProtocolForm form = null;
		if(obj != null && obj instanceof DistributionProtocolForm)
		{
			form = (DistributionProtocolForm)obj;
			map = form.getValues();
			noOfRows = form.getCounter();

			currentDistributionProtocolDate = form.getStartDate();
			if(currentDistributionProtocolDate == null)
				currentDistributionProtocolDate = "";
			distributionProtocolEndDate=form.getEndDate();
			 if(distributionProtocolEndDate == null)
				 distributionProtocolEndDate="";
		}
		
		
			String reqPath = (String)request.getAttribute(Constants.REQ_PATH);
		String appendingPath = "/DistributionProtocol.do?operation=add&pageOf=pageOfDistributionProtocol";
		if (reqPath != null)
			appendingPath = reqPath + "|/DistributionProtocol.do?operation=add&pageOf=pageOfDistributionProtocol";
	
	   	if(!operation.equals("add") )
	   	{
	   		Object obj1 = request.getAttribute("distributionProtocolForm");
			if(obj1 != null && obj instanceof DistributionProtocolForm)
			{
				DistributionProtocolForm form1 = (DistributionProtocolForm)obj1;
		   		appendingPath = "/DistributionProtocolSearch.do?operation=search&pageOf=pageOfDistributionProtocol&id="+form1.getId() ;
		   		System.out.println("---------- DP JSP appendingPath -------- : "+ appendingPath);
		   	}
	   	}
%>

<%@ include file="/pages/content/common/CommonScripts.jsp" %>
<SCRIPT LANGUAGE="JavaScript">
	var search1='`';
	var insno=0;
</script>
<SCRIPT LANGUAGE="JavaScript">

function showHide(elementid){
if (document.getElementById(elementid).style.display == 'none'){
document.getElementById(elementid).style.display = '';
} else {
document.getElementById(elementid).style.display = 'none';
}
} 
<!--
	// functions for add more


//  function to insert a row in the inner block
function insRow(subdivtag)
{
	var val = parseInt(document.forms[0].counter.value);
		val = val + 1;
		document.forms[0].counter.value = val;
	var sname = "";

	var r = new Array(); 
	r = document.getElementById(subdivtag).rows;
	var q = r.length;
//	var x=document.getElementById(subdivtag).insertRow(q);
	var x=document.getElementById(subdivtag).insertRow(1);
	
	var subdivname = ""+ subdivtag;
	var rowno=(q-1);
	//CheckBox
	var checkb=x.insertCell(0);
	checkb.className="black_ar";
	sname="";
	var name = "chk_"+rowno;
	sname="&nbsp;&nbsp;&nbsp;&nbsp;<input type='checkbox' name='" + name +"' id='" + name +"' value='C' class='black_ar' onClick=\"enableButton(document.forms[0].deleteValue,document.forms[0].counter,'chk_')\">";
	checkb.innerHTML=""+sname;

	// class type
	var spreqtype=x.insertCell(1)
	spreqtype.className="black_new";
	
	sname="";
	objname = "value(DistributionSpecimenRequirement:" + rowno + "_specimenClass)";
	var specimenClassName = objname;
//value(SpecimenRequirement:`_quantity)	
	
	var objunit = "value(DistributionSpecimenRequirement:"+rowno+"_unitspan)";
	var subtypename =  "value(DistributionSpecimenRequirement:"+rowno+"_specimenType)";
	sname = "<select name='" + objname + "' size='1' onchange=changeUnit('" + objname + "','" + objunit +"','" + subtypename + "') class='formFieldSized8' id='" + objname + "' onmouseover=showTip(this.id) onmouseout=hideTip(this.id)>";
	<%for(int i=0;i<specimenClassList.size();i++)
	{
		String specimenClassLabel = "" + ((NameValueBean)specimenClassList.get(i)).getName();
		String specimenClassValue = "" + ((NameValueBean)specimenClassList.get(i)).getValue();
	%>
		sname = sname + "<option value='<%=specimenClassValue%>'><%=specimenClassLabel%></option>";
	<%}%>
	sname = sname + "</select>";
	 
	spreqtype.innerHTML="" + sname;
	
	//subtype
	var spreqsubtype=x.insertCell(2)
	spreqsubtype.className="black_new";
	sname="";
	objname = "value(DistributionSpecimenRequirement:"+rowno+"_specimenType)";
	var functionName = "onSubTypeChangeUnit('" + specimenClassName + "',this,'" + objunit + "')" ;
	
	sname= "<select name='" + objname + "' size='1' class='formFieldSized8' id='" + objname + "' onChange=" + functionName + " onmouseover=showTip(this.id) onmouseout=hideTip(this.id)>";
			
	sname = sname + "<option value='-1'><%=Constants.SELECT_OPTION%></option>";
	sname = sname + "</select>"
	
	spreqsubtype.innerHTML="" + sname;
	
	//tissuesite
	var spreqtissuesite=x.insertCell(3)
	spreqtissuesite.className="black_new";
	sname="";
	objname = "value(DistributionSpecimenRequirement:"+rowno+"_tissueSite)";
	
	sname = "<select name='" + objname + "' size='1' class='formFieldSizedNew' id='" + objname + "' onmouseover=showTip(this.id) onmouseout=hideTip(this.id)>";
	<%for(int i=0;i<tissueSiteList.size();i++)
	{%>
		sname = sname + "<option value='<%=((NameValueBean)tissueSiteList.get(i)).getValue()%>'><%=((NameValueBean)tissueSiteList.get(i)).getName()%></option>";
	<%}%>
	sname = sname + "</select>"
	var url = "ShowFramedPage.do?pageOf=pageOfTissueSite&propertyName="+objname+"&cdeName=Tissue%20Site";
	// Patch ID: Bug#3090_8
	sname = sname + "<a href='#' onclick=javascript:NewWindow('" + url + "','name','360','525','no');return false>";
	sname = sname + "<img src='images/uIEnhancementImages/ic_cl_diag.gif' border='0' align='absmiddle' height='16' hspace='5' width='16' title='Tissue Site Selector'></a>";
	
	spreqtissuesite.innerHTML="" + sname;
	
	//pathologystatus
	var spreqpathologystatus=x.insertCell(4)
	spreqpathologystatus.className="black_new";
	
	sname="";
	objname = "value(DistributionSpecimenRequirement:"+rowno+"_pathologyStatus)";
	
	sname="<select name='" + objname + "' size='1' class='formFieldSized8' id='" + objname + "' onmouseover=showTip(this.id) onmouseout=hideTip(this.id)>";
	<%for(int i=0;i<pathologyStatusList.size();i++)
	{%>
		sname = sname + "<option value='<%=((NameValueBean)pathologyStatusList.get(i)).getValue()%>'><%=((NameValueBean)pathologyStatusList.get(i)).getName()%></option>";
	<%}%>
	sname = sname + "</select>";
	
	spreqpathologystatus.innerHTML="" + sname;
	
	//qty
	var spreqqty=x.insertCell(5)
	spreqqty.className="black_ar";
	sname="";
	objname = "value(DistributionSpecimenRequirement:"+rowno+"_quantity)";

	sname="<input type='text' name='" + objname + "' value='0'  size='7' style='text-align:right' class='black_ar' id='" + objname + "'>"        	
	sname = sname + "&nbsp;<span id='" + objunit + "'>&nbsp;</span>"
					
	spreqqty.innerHTML="" + sname;

	

}

//-->
</SCRIPT>

</head>
<body>
<html:form action="<%=formName%>">
<html:hidden property="operation" value="<%=operation%>" />
<html:hidden property="submittedFor" value="<%=submittedFor%>"/>
<html:hidden property="id" />
<html:hidden property="onSubmit"/>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="maintable">
  <tr>
    <td class="td_color_bfdcf3"><table border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td class="td_table_head"><span class="wh_ar_b"><bean:message key="DistributionProtocol.header" /></span></td>
        <td align="right"><img src="images/uIEnhancementImages/table_title_corner2.gif" alt="Page Title - Distribution Protocol" width="31" height="24" /></td>
      </tr>
    </table></td>
  </tr>
  <tr>
    <td class="tablepadding"><table width="100%" border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td width="4%" class="td_tab_bg" ><img src="images/uIEnhancementImages/spacer.gif" alt="spacer" width="50" height="1"></td>
		<%
						if(operation.equals(Constants.ADD))
						{ 
				%>
        <td valign="bottom"><img src="images/uIEnhancementImages/tab_add_selected.jpg" alt="Add" width="57" height="22" /></td>
        <td valign="bottom"><html:link page="/SimpleQueryInterface.do?pageOf=pageOfDistributionProtocol&aliasName=DistributionProtocol" ><img src="images/uIEnhancementImages/tab_edit_notSelected.jpg" alt="Edit" width="59" height="22" border="0" /></html:link></td>
		<% 
						}
						   if(operation.equals(Constants.EDIT))
						{
				%>
		<td valign="bottom"><html:link page="/DistributionProtocol.do?operation=add&pageOf=pageOfDistributionProtocol"><img src="images/uIEnhancementImages/tab_add_notSelected.jpg" alt="Add" width="57" height="22" /></html:link></td>
        <td valign="bottom"><img src="images/uIEnhancementImages/tab_edit_selected.jpg" alt="Edit" width="59" height="22" border="0" /></td>
		<%
						}
				%>
        <td width="90%" valign="bottom" class="td_tab_bg">&nbsp;</td>
      </tr>
    </table>
      <table width="100%" border="0" cellpadding="3" cellspacing="0" class="whitetable_bg">
        <tr>
          <td colspan="2" align="left" class="bottomtd">
				<%@ include file="/pages/content/common/ActionErrors.jsp" %>		
		  </td>
        </tr>
        <tr>
          <td colspan="2" align="left" class="tr_bg_blue1"><span class="blue_ar_b"> &nbsp;<logic:equal name="operation" value="<%=Constants.ADD%>"><bean:message key="distributionprotocol.title"/></logic:equal>
							<logic:equal name="operation" value="<%=Constants.EDIT%>"><bean:message key="distributionprotocol.editTitle"/></logic:equal></span></td>
        </tr>
        <tr>
          <td colspan="2" align="left" class="showhide"><table width="100%" border="0" cellpadding="3" cellspacing="0">
             
                <tr>
                  <td width="1%" align="center" class="black_ar"><span class="blue_ar_b"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /></span></td>
                  <td width="17%" align="left" class="black_ar"><bean:message key="distributionprotocol.principalinvestigator" /> </td>
                  <td width="20%" align="left" class="black_new">
					<html:select property="principalInvestigatorId" styleClass="formFieldSizedNew"			styleId="principalInvestigatorId" size="1"
											 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
											 <html:options collection="<%=Constants.USERLIST%>" labelProperty="name" property="value"/>
											</html:select>
                  </td>
                  <td width="62%" align="left"><html:link href="#" styleId="newUser" styleClass="view" onclick="addNewAction('DistributionProtocolAddNew.do?addNewForwardTo=principalInvestigator&forwardTo=distributionProtocol&addNewFor=principalInvestigator')">
												<bean:message key="buttons.addNew" />
											</html:link></td>
                </tr>
                <tr>
                  <td align="center" class="black_ar"><span class="blue_ar_b"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /></span></td>
                  <td align="left" class="black_ar"><bean:message key="distributionprotocol.protocoltitle" /> </td>
                  <td colspan="2" align="left"><html:text styleClass="black_ar" maxlength="255"  size="30" styleId="title" property="title" readonly="<%=readOnlyValue%>" /></td>
                </tr>
                <tr>
                  <td align="center" class="black_ar"><span class="blue_ar_b"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /></span></td>
                  <td align="left" class="black_ar"><bean:message key="distributionprotocol.shorttitle" /> </td>
                  <td colspan="2" align="left"><html:text styleClass="black_ar" maxlength="255"  size="30" styleId="shortTitle" property="shortTitle" readonly="<%=readOnlyValue%>" /></td>
                </tr>
                <tr>
                  <td align="center" class="black_ar">&nbsp;</td>
                  <td align="left" class="black_ar"><label for="irbID"><bean:message key="distributionprotocol.irbid" /> </label></td>
                  <td colspan="2" align="left"><html:text styleClass="black_ar" maxlength="255"  size="30" styleId="irbID" property="irbID" readonly="<%=readOnlyValue%>" />
                  </td>
                </tr>
                <tr>
                  <td align="center" class="black_ar"><span class="blue_ar_b"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /></span></td>
                  <td align="left" class="black_ar"><label for="startDate"><bean:message key="distributionprotocol.startdate" /> </label></td>
                  <td colspan="2" align="left" valign="top">
				  <%
							
						if(currentDistributionProtocolDate.trim().length() > 0)
						{
							Integer distributionProtocolYear = new Integer(Utility.getYear(currentDistributionProtocolDate ));
							Integer distributionProtocolMonth = new Integer(Utility.getMonth(currentDistributionProtocolDate ));
							Integer distributionProtocolDay = new Integer(Utility.getDay(currentDistributionProtocolDate ));
					%>
									<ncombo:DateTimeComponent name="startDate"
								  		id="startDate"
								  		formName="distributionProtocolForm"
			  						    month= "<%= distributionProtocolMonth %>"
								  		year= "<%= distributionProtocolYear %>"
								  		day= "<%= distributionProtocolDay %>"
								  		value="<%=currentDistributionProtocolDate %>"
								  		styleClass="black_ar"
									/>
					<% 
						}
						else
						{  
					 %>
									<ncombo:DateTimeComponent name="startDate"
								 	 	id="startDate"
								  		formName="distributionProtocolForm"
								  		styleClass="black_ar"
									/>
					<% 
						} 
					%>
						<span class="grey_ar_s"><bean:message key="page.dateFormat" /></span>
					</td>
                </tr>
				<!-- enddate: should be displayed only in case of edit  -->	
				<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.EDIT%>">
							<tr>
								<td align="center" class="black_ar">&nbsp;</td>
								<td class="black_ar"><label for="endDate"><bean:message key="distributionprotocol.enddate" /></label></td>
					<%
							Integer distributionProtocolEndDateYear = new Integer(Utility.getYear(distributionProtocolEndDate ));
							Integer distributionProtocolEndDateMonth = new Integer(Utility.getMonth(distributionProtocolEndDate ));
							Integer distributionProtocolEndDateDay = new Integer(Utility.getDay(distributionProtocolEndDate ));
					%>
			   				    <td colspan="2" align="left" valign="top">
									<ncombo:DateTimeComponent name="endDate" 
											 id="endDate"
											 formName="distributionProtocolForm"
											month= "<%= distributionProtocolEndDateMonth %>"
								  			year= "<%= distributionProtocolEndDateYear %>"
								  			day= "<%= distributionProtocolEndDateDay %>"
								  			value="<%=distributionProtocolEndDate %>"
								  			styleClass="black_ar"
										/>
									 <span class="grey_ar_s"><bean:message key="page.dateFormat" /> </span>
								 </td>
				  			  </tr>
				</logic:equal>

                <tr>
                  <td align="center" class="black_ar">&nbsp;</td>
                  <td align="left" class="black_ar"><bean:message key="distributionprotocol.participants" /> </td>
                  <td colspan="2" align="left"><html:text styleClass="black_ar" maxlength="10"  size="10" style="text-align:right" styleId="enrollment" property="enrollment" readonly="<%=readOnlyValue%>" /></td>
                </tr>
                <tr>
                  <td align="center" class="black_ar">&nbsp;</td>
                  <td align="left" class="black_ar"><label for="departmentId"><bean:message key="distributionprotocol.descriptionurl" /> </label></td>
                  <td colspan="2" align="left"><html:text styleClass="black_ar"  maxlength="255" size="30" styleId="descriptionURL" property="descriptionURL" readonly="<%=readOnlyValue%>" />
                  </td>
                </tr>
				<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.EDIT%>">
							<tr>
								<td align="left" class="black_ar">
									<span class="blue_ar_b">
										<img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" />
									</span>
								</td>
		                        <td align="left" class="black_ar">
										<label for="activityStatus">
											<bean:message key="site.activityStatus" />
										</label>
								</td>
								<td colspan="2" align="left" class="black_new">
									 <html:select property="activityStatus" styleClass="formFieldSizedNew"				styleId="activityStatus" size="1"  onchange="<%=strCheckStatus%>"
			   						    onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
											<html:options name="<%=Constants.ACTIVITYSTATUSLIST%>" labelName="<%=Constants.ACTIVITYSTATUSLIST%>" />
									</html:select>
								</td>
							</tr>
					</logic:equal>
             
          </table></td>
        </tr>
        <tr onclick="showHide('add_id')">
          <td align="left" class="tr_bg_blue1"><span class="blue_ar_b">&nbsp;<bean:message key="distributionprotocol.specimenreq" /></span></td>
		  <html:hidden property="counter"/>
          <td width="2%" align="right" class="tr_bg_blue1"><a href="#"><img src="images/uIEnhancementImages/dwn_arrow1.gif" alt="Show Details" width="80" height="9" hspace="10" border="0"/></a></td>
        </tr>
        <tr>
          <td colspan="2" class="showhide1"><div id="add_id" style="display:none">
              <table width="100%" border="0" cellspacing="0" cellpadding="4">
			  <tbody id="SpecimenRequirementData">
                <tr>
                  <td width="8%" class="tableheading"><span class="black_ar_b">
                    <label for="delete" align="center"><bean:message key="app.select" /></label>
                  </span></td>
                  <td width="16%" class="tableheading"><span class="black_ar_b"><span class="blue_ar_b"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /> </span><bean:message key="distributionprotocol.specimenclass" /> </span></td>
                  <td width="17%" class="tableheading"><span class="black_ar_b"><span class="blue_ar_b"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /></span> <bean:message key="distributionprotocol.specimentype" /> </span></td>
                  <td width="29%" class="tableheading"><span class="black_ar_b"><span class="blue_ar_b"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /></span> <bean:message key="distributionprotocol.specimensite" /></span></td>
                  <td width="17%" class="tableheading"><span class="black_ar_b"><span class="blue_ar_b"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /></span> <bean:message key="distributionprotocol.pathologystatus" /></span></td>
                  <td width="13%" class="tableheading"><span class="black_ar_b"><bean:message key="distributionprotocol.quantity" /></span></td>
                </tr>
				<%
				int maxcount=1;
				for(int counter=noOfRows;counter>=1;counter--)
				{		
					String objname = "value(DistributionSpecimenRequirement:" + counter + "_specimenClass)";
					String srCommonName = "DistributionSpecimenRequirement:" + counter;
					String srKeyName = srCommonName + "_specimenClass";
					String srSubTypeKeyName = srCommonName + "_specimenType";
					
					String objunit = "value(DistributionSpecimenRequirement:"+ counter +"_unitspan)";
					String objsubTypeName = "value(DistributionSpecimenRequirement:" + counter + "_specimenType)";
					String identifier = "value(DistributionSpecimenRequirement:"+ counter +"_id)";
					String check = "chk_"+counter;
					String mapIdKey = "DistributionSpecimenRequirement:" + counter + "_id";

					String idValue = String.valueOf((map.get(mapIdKey)));
					int sysId = 0;

					try
					{
						sysId = Integer.parseInt(idValue);
					}
					catch(Exception e) //Exception is handled. If NumberFormatException or NullPointerException then identfier value = 0
					{
						sysId = 0;
					}

					String classValue = (String)map.get(srKeyName);
			%>
								<tr>
									<html:hidden property="<%=identifier%>" />
			<%
				String functionName ="changeUnit('" + objname + "',' " + objunit + "','" + objsubTypeName + "')"; 
				String subTypeFunctionName ="onSubTypeChangeUnit('" + objname + "',this,' " + objunit + "')"; 
			%>


			<%
					
					String key = "DistributionSpecimenRequirement:"+ counter +"_id";
					boolean bool = Utility.isPersistedValue(map,key);
					String condition = "";
					if(bool)
						condition = "disabled='disabled'";
			%>
						<td class="black_ar">
										<label>&nbsp; &nbsp;
							<input type=checkbox name="<%=check %>" id="<%=check %>" <%=condition%> class="black_ar" onClick="enableButton(document.forms[0].deleteValue,document.forms[0].counter,'chk_')" />
						</label>
										</td>


									<td class="black_new"><html:select property="<%=objname%>"styleClass="formFieldSized8" styleId="<%=objname%>" size="1"onchange="<%=functionName%>" onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
			<%			if(operation.equals(Constants.EDIT) && sysId > 0)
						{
			%>
											<html:option value="<%=classValue%>"><%=classValue%></html:option>
			<%
						}
						else
						{
			%>
											<html:options collection="<%=Constants.SPECIMEN_CLASS_LIST%>" labelProperty="name" property="value"/>
			<%
						}
			%>
											</html:select>
										</td>
										

					                    <td class="black_new">
			<%
						//String classValue = (String)form.getValue(srKeyName);
					
						specimenTypeList = (List)specimenTypeMap.get(classValue);
						boolean subListEnabled = false;
								
						if(specimenTypeList == null)
						{
							specimenTypeList = new ArrayList();
							specimenTypeList.add(new NameValueBean(Constants.SELECT_OPTION,"-1"));
						}
						pageContext.setAttribute(Constants.SPECIMEN_TYPE_LIST, specimenTypeList);
						String tmpSpecimenClass = objname;
						
						objname="";
						objname = "value(DistributionSpecimenRequirement:" + counter + "_specimenType)";
						
						
						
			%>
										<html:select property="<%=objname%>" styleClass="formFieldSized8"				styleId="<%=objname%>" 
											size="1"  onchange="<%=subTypeFunctionName%>" disabled="<%=subListEnabled%>" 
											 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
											<html:options collection="<%=Constants.SPECIMEN_TYPE_LIST%>" labelProperty="name" property="value"/>
										</html:select>
									</td>
									<td class="black_new">
			<%
						objname="";
						objname = "value(DistributionSpecimenRequirement:" + counter + "_tissueSite)";
			%>
									<html:select property="<%=objname%>" styleClass="formFieldSizedNew" styleId="<%=objname%>" size="1" 
									onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
									<html:options collection="<%=Constants.TISSUE_SITE_LIST%>" labelProperty="name" property="value"/>
									</html:select>
			<%
						String url = "ShowFramedPage.do?pageOf=pageOfTissueSite&propertyName="+objname+"&cdeName=Tissue%20Site";			
			%>
									<a href="#" onclick="javascript:NewWindow('<%=url%>','name','360','525','no');return false">
									<img src="images/uIEnhancementImages/ic_cl_diag.gif" alt="Clinical Diagnosis" width="16" height="16" hspace="5" align="absmiddle" title='Tissue Site Selector' border="0"/>
									</a>
									</td>
									<td class="black_new">
			<%
						objname="";
						objname = "value(DistributionSpecimenRequirement:" + counter + "_pathologyStatus)";
			%>						
									<html:select property="<%=objname%>" styleClass="formFieldSized8" styleId="<%=objname%>" size="1" 
									 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
									<html:options collection="<%=Constants.PATHOLOGICAL_STATUS_LIST%>" labelProperty="name" property="value"/>
									</html:select>
									</td>
                      <td class="black_ar">
			<%
						objname="";
						objname = "value(DistributionSpecimenRequirement:"+ counter +"_quantity)";
						String typeclassValue = (String)form.getValue(srSubTypeKeyName);
						String strHiddenUnitValue = "" + changeUnit(classValue,typeclassValue);
						String srQtyKeyName = srCommonName + "_quantity";
						String qtyValue = (String)form.getValue(srQtyKeyName);
						if(qtyValue == null)
							qtyValue="0";
			%>
					  <html:text styleClass="black_ar" 
							styleId="<%=objname%>"  size="7" 
							property="<%=objname%>"
							readonly="<%=readOnlyValue%>"
							value="<%=qtyValue %>" style="text-align:right"/>
					
					<span id=' <%= objunit %>'>
						<%=strHiddenUnitValue%>
					</span>
					</td>
			<%
					
			    }
			%>
              
                <tr>
                 <td colspan="6" valign="bottom" class="black_ar"><html:button property="addDistributionProtocolEvents" styleClass="black_ar" onclick="insRow('SpecimenRequirementData')" > <bean:message key="buttons.addMore" /></html:button> &nbsp;<html:button property="deleteValue" styleClass="black_ar" onclick="deleteChecked('SpecimenRequirementData','DistributionProtocol.do?operation=<%=operation%>&pageOf=pageOfDistributionProtocol&status=true',document.forms[0].counter,'chk_',false)" disabled="true"><bean:message key="buttons.delete" /></html:button></td>
                </tr>
				 </tbody>
            </table>
          </td>
        </tr>
        <tr >
          <td colspan="2" class="bottomtd"></td>
        </tr>
        <tr  >
          <td colspan="2" class="buttonbg">
			<%
				   	String action = "confirmDisable('" + formName +"',document.forms[0].activityStatus)";
			%>
			<%	
							String deleteAction = "deleteObject('" + formName +"','" + Constants.ADMINISTRATIVE + "')";
			%>
			<html:button styleClass="blue_ar_b" property="submitPage" onclick="<%=action%>" accesskey="Enter">
						   		<bean:message key="buttons.submit"/>
				</html:button>
			<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.EDIT%>">
							&nbsp;| 
							<html:button styleClass="blue_ar_c" property="submitPage" onclick="<%=deleteAction%>" accesskey="Enter">
						   		<bean:message key="buttons.delete" /></html:button>
				</logic:equal>
            &nbsp;| <html:link page="/ManageAdministrativeData.do" styleClass="cancellink">
						<bean:message key="buttons.cancel" />
					</html:link>
			</td>
        </tr>
      </table></td>
  </tr>
</table>
</html:form>
</body>
