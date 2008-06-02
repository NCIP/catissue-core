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
<script src="jss/script.js" type="text/javascript"></script>
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
		Object obj = request.getAttribute("distributionProtocolForm");
		int noOfRows=1;
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
<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>

<SCRIPT LANGUAGE="JavaScript">
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

	// class type
	var spreqtype=x.insertCell(0)
	spreqtype.className="black_ar";
	var rowno=(q-1);
	sname="";
	objname = "value(SpecimenRequirement:" + rowno + "_specimenClass)";
	var specimenClassName = objname;
//value(SpecimenRequirement:`_quantity_value)	
	
	var objunit = "value(SpecimenRequirement:"+rowno+"_unitspan)";
	var subtypename =  "value(SpecimenRequirement:"+rowno+"_specimenType)";
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
	var spreqsubtype=x.insertCell(1)
	spreqsubtype.className="black_ar";
	sname="";
	objname = "value(SpecimenRequirement:"+rowno+"_specimenType)";
	var functionName = "onSubTypeChangeUnit('" + specimenClassName + "',this,'" + objunit + "')" ;
	
	sname= "<select name='" + objname + "' size='1' class='formFieldSized8' id='" + objname + "' onChange=" + functionName + " onmouseover=showTip(this.id) onmouseout=hideTip(this.id)>";
			
	sname = sname + "<option value='-1'><%=Constants.SELECT_OPTION%></option>";
	sname = sname + "</select>"
	
	spreqsubtype.innerHTML="" + sname;
	
	//tissuesite
	var spreqtissuesite=x.insertCell(2)
	spreqtissuesite.className="black_new";
	sname="";
	objname = "value(SpecimenRequirement:"+rowno+"_tissueSite)";
	
	sname = "<select name='" + objname + "' size='1' class='formFieldSizedNew' id='" + objname + "' onmouseover=showTip(this.id) onmouseout=hideTip(this.id)>";
	<%for(int i=0;i<tissueSiteList.size();i++)
	{%>
		sname = sname + "<option value='<%=((NameValueBean)tissueSiteList.get(i)).getValue()%>'><%=((NameValueBean)tissueSiteList.get(i)).getName()%></option>";
	<%}%>
	sname = sname + "</select>"
	var url = "ShowFramedPage.do?pageOf=pageOfTissueSite&propertyName="+objname+"&cdeName=Tissue%20Site";
	// Patch ID: Bug#3090_8
	sname = sname + "<a href='#' onclick=javascript:NewWindow('" + url + "','name','360','525','no');return false>";
	sname = sname + "<img src='images\\uIEnhancementImages\\ic_cl_diag.gif' border='0' align='absmiddle' height='16' hspace='5' width='16' title='Tissue Site Selector'></a>";
	
	spreqtissuesite.innerHTML="" + sname;
	
	//pathologystatus
	var spreqpathologystatus=x.insertCell(3)
	spreqpathologystatus.className="black_ar";
	
	sname="";
	objname = "value(SpecimenRequirement:"+rowno+"_pathologyStatus)";
	
	sname="<select name='" + objname + "' size='1' class='formFieldSized8' id='" + objname + "' onmouseover=showTip(this.id) onmouseout=hideTip(this.id)>";
	<%for(int i=0;i<pathologyStatusList.size();i++)
	{%>
		sname = sname + "<option value='<%=((NameValueBean)pathologyStatusList.get(i)).getValue()%>'><%=((NameValueBean)pathologyStatusList.get(i)).getName()%></option>";
	<%}%>
	sname = sname + "</select>";
	
	spreqpathologystatus.innerHTML="" + sname;
	
	//qty
	var spreqqty=x.insertCell(4)
	spreqqty.className="black_ar";
	sname="";
	objname = "value(SpecimenRequirement:"+rowno+"_quantity_value)";

	sname="<input type='text' name='" + objname + "' value='0'  size='12'  class='black_ar' id='" + objname + "'>"        	
	sname = sname + "&nbsp;<span id='" + objunit + "'>&nbsp;</span>"
					
	spreqqty.innerHTML="" + sname;

	//CheckBox
	var checkb=x.insertCell(5);
	checkb.className="black_ar";
	//checkb.colSpan=2;
	sname="";
	var name = "chk_"+rowno;
	sname="&nbsp;&nbsp;&nbsp;&nbsp;<input type='checkbox' name='" + name +"' id='" + name +"' value='C' class='black_ar' onClick=\"enableButton(document.forms[0].deleteValue,document.forms[0].counter,'chk_')\">";
	checkb.innerHTML=""+sname;

}

//-->
</SCRIPT>

<style>
	div#d1
	{
		display:none;
	}
	div#d999
	{
	 	display:none;
	}
</style>
</head>
<body>

<html:errors />
<html:messages id="messageKey" message="true" header="messages.header" footer="messages.footer">
	<%=messageKey%>
</html:messages>

<html:form action="<%=formName%>">
<html:hidden property="operation" value="<%=operation%>" />
<html:hidden property="submittedFor" value="<%=submittedFor%>"/>
<html:hidden property="id" />
<html:hidden property="onSubmit"/>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="newMaintable">
  <tr>
    <td><table width="100%" border="0" cellpadding="0" cellspacing="0" class="td_color_bfdcf3">
      <tr>
        <td><table width="100%" border="0" cellpadding="0" cellspacing="0" class="whitetable_bg">
          <tr>
            <td width="100%" colspan="2" valign="top"><table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td colspan="3" valign="top" class="td_color_bfdcf3"><table width="22%" border="0" cellpadding="0" cellspacing="0" background="images/uIEnhancementImages/table_title_bg.gif">
                    <tr>
                      <td width="82%"><span class="wh_ar_b">&nbsp;&nbsp;&nbsp;
						<bean:message key="DistributionProtocol.header" /></span></td>
                      <td width="18%" align="right"><img src="images/uIEnhancementImages/table_title_corner2.gif" width="31" height="24" /></td>
                    </tr>
                </table></td>
              </tr>
              <tr>
                <td width="1%" valign="top" class="td_color_bfdcf3">&nbsp;</td>
                <td width="9%" valign="top" class="td_tab_bg">&nbsp;</td>
                <td width="90%" valign="bottom" class="td_color_bfdcf3" style="padding-top:4px;"><table width="100%" border="0" cellpadding="0" cellspacing="0">
                    <tr>
                      <td width="4%" class="td_tab_bg" >&nbsp;</td>
			<!-- for tabs selection -->
				<%
						if(operation.equals(Constants.ADD))
						{ 
				%>
                      <td width="6%" valign="bottom" background="images/uIEnhancementImages/tab_bg.gif" ><img src="images/uIEnhancementImages/tab_add_selected.jpg" alt="Add" width="57" height="22" /></td>
                      <td width="6%" valign="bottom" background="images/uIEnhancementImages/tab_bg.gif"><html:link page="/SimpleQueryInterface.do?pageOf=pageOfDistributionProtocol&aliasName=DistributionProtocol&menuSelected=10" ><img src="images/uIEnhancementImages/tab_edit_notSelected.jpg" alt="Edit" width="59" height="22" border="0" /></html:link></td>
                      <td valign="bottom" background="images/uIEnhancementImages/tab_bg.gif">&nbsp;</td>
				<% 
						}
						   if(operation.equals(Constants.EDIT))
						{
				%>
						<td width="6%" valign="bottom" background="images/uIEnhancementImages/tab_bg.gif" ><html:link page="/DistributionProtocol.do?operation=add&pageOf=pageOfDistributionProtocol&menuSelected=10"><img src="images/uIEnhancementImages/tab_add_notSelected.jpg" alt="Add" width="57" height="22" border="0" /></html:link></td>
                      <td width="6%" valign="bottom" background="images/uIEnhancementImages/tab_bg.gif"><img src="images/uIEnhancementImages/tab_edit_selected.jpg" alt="Edit" width="59" height="22" /></td>
                      <td valign="bottom" background="images/uIEnhancementImages/tab_bg.gif">&nbsp;</td>
				<%
						}
				%>
                      <td width="1%" align="left" valign="bottom" class="td_color_bfdcf3" >&nbsp;</td>
                    </tr>
                </table></td>
              </tr>
            </table></td>
            </tr>
          
          <tr>
            <td colspan="2" class="td_color_bfdcf3" style="padding-left:10px; padding-right:10px; padding-bottom:10px;">
				<table width="100%" border="0" cellpadding="3" cellspacing="0" bgcolor="#FFFFFF">
					<tr>
		                <td colspan="3" align="left">
							<table width="99%" border="0" cellpadding="1" cellspacing="0">
			                    <tr>
						          <td>
									<table width="100%" border="0" cellpadding="0" cellspacing="0"						class="td_color_ffffff">
				                         <tr>
									          <td class=" grey_ar_s">
												<img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" />
												<bean:message key="commonRequiredField.message" /></td>
										 </tr>
									</table>
								</td>
		                    </tr>
				        </table>
					</td>
	              </tr>
	            <tr>
		<!-- Page Title -->
		            <td colspan="2" align="left" class="tr_bg_blue1">
						<span class="blue_ar_b"> 
							<logic:equal name="operation" value="<%=Constants.ADD%>">
								<bean:message key="distributionprotocol.title"/>
							</logic:equal>
							<logic:equal name="operation" value="<%=Constants.EDIT%>">
								<bean:message key="distributionprotocol.editTitle"/>
							</logic:equal>
						</span>
					</td>
                </tr>
                <tr>
					<td colspan="3" align="left" style="padding-top:10px; padding-bottom:15px;">
						<div id="part_det" >
		                    <table width="100%" border="0" cellpadding="3" cellspacing="0">
								<tr>
		                          <td width="1%" align="left" class="black_ar">
									<span class="blue_ar_b">
										<img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" />
									</span>
								   </td>
			                       <td width="16%" align="left" class="black_ar">
										<label for="principalInvestigatorId">
											<bean:message key="distributionprotocol.principalinvestigator" />
										</label>
									</td>
			                        <td width="23%" align="left">
										<label>
				                            <html:select property="principalInvestigatorId" styleClass="formFieldSizedNew"			styleId="principalInvestigatorId" size="1"
											 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
											 <html:options collection="<%=Constants.USERLIST%>" labelProperty="name" property="value"/>
											</html:select>
											</td>
											<td align="left" class="smalllink">
											<html:link href="#" styleId="newUser" styleClass="blue_ar_s_b" onclick="addNewAction('DistributionProtocolAddNew.do?addNewForwardTo=principalInvestigator&forwardTo=distributionProtocol&addNewFor=principalInvestigator')">
												<bean:message key="buttons.addNew" />
											</html:link>
										</label>
									</td>
		                        </tr>
				                <tr>
						          <td align="left" class="black_ar">
									<span class="blue_ar_b">
										<img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" />
									</span>
								  </td>
		                          <td align="left" class="black_ar">
										<label for="title">
											<bean:message key="distributionprotocol.protocoltitle" />
										</label>
								  </td>
		                          <td colspan="2" align="left">
										<html:text styleClass="black_ar" maxlength="255"  size="30" styleId="title" property="title" readonly="<%=readOnlyValue%>" />
								  </td>
	                          </tr>
		                      <tr>
				                 <td align="left" class="black_ar">
									<span class="blue_ar_b">
										<img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" />
									</span>
								 </td>
		                         <td align="left" class="black_ar">
										<label for="shortTitle">
											<bean:message key="distributionprotocol.shorttitle" />
										</label>
								 </td>
								 <td colspan="2" align="left">
									 <html:text styleClass="black_ar" maxlength="255"  size="30" styleId="shortTitle" property="shortTitle" readonly="<%=readOnlyValue%>" />
								 </td>
	                          </tr>
		                      <tr>
					             <td align="left" class="black_ar">&nbsp;
								 </td>
	                             <td align="left" class="black_ar">
									<label for="irbID">
										<bean:message key="distributionprotocol.irbid" />
									</label>
								 </td>
		                         <td colspan="2" align="left">
									<html:text styleClass="black_ar" maxlength="255"  size="30" styleId="irbID" property="irbID" readonly="<%=readOnlyValue%>" />
								 </td>
	                          </tr>
		                      <tr>
				                <td align="left" class="black_ar">
									<span class="blue_ar_b">
										<img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" />
									</span>
								</td>
		                        <td align="left" class="black_ar">
									<label for="startDate">
										<bean:message key="distributionprotocol.startdate" />
									</label>
								</td>
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
					%>				<span class="grey_ar_s">
									<bean:message key="page.dateFormat" />
									</span>&nbsp;
								</td>
							</tr>
				<!-- enddate: should be displayed only in case of edit  -->	
				<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.EDIT%>">
							<tr>
								<td align="left" class="black_ar">&nbsp;
								</td>
								<td class="black_ar">
									<label for="endDate">
										<bean:message key="distributionprotocol.enddate" />
									</label>
								</td>
			   				    <td colspan=2>
									 <div id="enddateDiv" style="position:absolute; visibility:hidden; index:1000;">
									 </div>
									 <html:text styleClass="black_ar" maxlength="10"  size="20" styleId="endDate" property="endDate" readonly="true" />
									 &nbsp;<span class="grey_ar_s"><bean:message key="page.dateFormat" /> </span>&nbsp;
								 </td>
				  			  </tr>
				</logic:equal>
  							  <tr>
								<td align="left" class="black_ar">&nbsp;
								</td>
								<td align="left" class="black_ar">
									<label for="enrollment">
										<bean:message key="distributionprotocol.participants" />
									</label>
								</td>
								<td colspan="2" align="left">
									<html:text styleClass="black_ar" maxlength="10"  size="30" styleId="enrollment" property="enrollment" readonly="<%=readOnlyValue%>" />
								</td>
							</tr>
							<tr>
								<td align="left" class="black_ar">&nbsp;
								</td>
								<td align="left" class="black_ar">
									<label for="descriptionURL">
										<bean:message key="distributionprotocol.descriptionurl" />
									</label>
								</td>
		                        <td colspan="2" align="left">
									<html:text styleClass="black_ar"  maxlength="255" size="30" styleId="descriptionURL" property="descriptionURL" readonly="<%=readOnlyValue%>" /> 
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
								<td colspan="2" align="left">
									 <html:select property="activityStatus" styleClass="formFieldSizedNew"				styleId="activityStatus" size="1"  onchange="<%=strCheckStatus%>"
			   						    onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
											<html:options name="<%=Constants.ACTIVITYSTATUSLIST%>" labelName="<%=Constants.ACTIVITYSTATUSLIST%>" />
									</html:select>
								</td>
							</tr>
					</logic:equal>
                        </table>
					</div>
				</td>
			</tr>
            <tr onclick="javascript:showHide('add_specimen_requirements')">
	           <td height="20" align="left" class="tr_bg_blue1">
					<span class="blue_ar_b">
						<bean:message key="distributionprotocol.specimenreq" />
					</span>
					<html:hidden property="counter"/>
				</td>
                <td width="2%" align="right" class="tr_bg_blue1"><a href="#" id="imgArrow_add_specimen_requirements">
					<img src="images/uIEnhancementImages/dwn_arrow1.gif" width="7" height="8" hspace="10" border="0" class="tr_bg_blue1" /></a>
				</td>
             </tr>
             <tr>
                <td colspan="3" style="padding-top:10px;">
					<div id="add_specimen_requirements" style="display:none" >
						<table width="100%" border="0" cellspacing="0" cellpadding="4">
							<tbody id="SpecimenRequirementData">
							    <tr>
									<td width="16%" class="tableheading"><span class="black_ar_b"><span class="blue_ar_b"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /> </span>
											<bean:message key="distributionprotocol.specimenclass" />
										</span>
									</td>
				                    <td width="16%" class="tableheading"><span class="black_ar_b"><span class="blue_ar_b"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /></span>
											<bean:message key="distributionprotocol.specimentype" />
										</span>
									</td>
									<td width="29%" class="tableheading"><span class="black_ar_b"><span class="blue_ar_b"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /></span>
											<bean:message key="distributionprotocol.specimensite" />
										</span>
									</td>
				                    <td width="17%" class="tableheading"><span class="black_ar_b"><span class="blue_ar_b"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /></span>
											<bean:message key="distributionprotocol.pathologystatus" />
										</span>
									</td>
			                        <td width="14%" class="tableheading"><span class="black_ar_b">
											<bean:message key="distributionprotocol.quantity" />
										</span>
									</td>
			                        <td width="8%" class="tableheading"><span class="black_ar_b">
                        <label for="delete" align="center">
												<bean:message key="addMore.delete" />
											</label>
					                     </span>
									</td>
			                    </tr>
						        
		<%
				int maxcount=1;
				for(int counter=noOfRows;counter>=1;counter--)
				{		
					String objname = "value(SpecimenRequirement:" + counter + "_specimenClass)";
					String srCommonName = "SpecimenRequirement:" + counter;
					String srKeyName = srCommonName + "_specimenClass";
					String srSubTypeKeyName = srCommonName + "_specimenType";
					
					String objunit = "value(SpecimenRequirement:"+ counter +"_unitspan)";
					String objsubTypeName = "value(SpecimenRequirement:" + counter + "_specimenType)";
					String identifier = "value(SpecimenRequirement:"+ counter +"_id)";
					String check = "chk_"+counter;
					String mapIdKey = "SpecimenRequirement:" + counter + "_id";

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
									<td class="black_ar"><html:select property="<%=objname%>"							styleClass="formFieldSized8" styleId="<%=objname%>" size="1"					onchange="<%=functionName%>" 
										onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
									
			<%

						if(operation.equals(Constants.EDIT) && sysId > 0)
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

					                    <td class="black_ar">
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
						objname = "value(SpecimenRequirement:" + counter + "_specimenType)";
						
						
						
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
						objname = "value(SpecimenRequirement:" + counter + "_tissueSite)";
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
									<td class="black_ar">
			<%
						objname="";
						objname = "value(SpecimenRequirement:" + counter + "_pathologyStatus)";
			%>						
									<html:select property="<%=objname%>" styleClass="formFieldSized8" styleId="<%=objname%>" size="1" 
									 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
									<html:options collection="<%=Constants.PATHOLOGICAL_STATUS_LIST%>" labelProperty="name" property="value"/>
									</html:select>
									</td>
                      <td class="black_ar">
			<%
						objname="";
						objname = "value(SpecimenRequirement:"+ counter +"_quantity_value)";
						String typeclassValue = (String)form.getValue(srSubTypeKeyName);
						String strHiddenUnitValue = "" + changeUnit(classValue,typeclassValue);
						String srQtyKeyName = srCommonName + "_quantity_value";
						String qtyValue = (String)form.getValue(srQtyKeyName);
						if(qtyValue == null)
							qtyValue="0";
			%>
					  <html:text styleClass="black_ar" 
							styleId="<%=objname%>"  size="12" 
							property="<%=objname%>"
							readonly="<%=readOnlyValue%>"
							value="<%=qtyValue %>" />
					&nbsp;
					<span id=' <%= objunit %>'>
						<%=strHiddenUnitValue%>
					</span>
					</td>
			<%
					
					String key = "SpecimenRequirement:"+ counter +"_id";
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
                    </tr>
			<%
				} // for 
			%>
					
                    <tr>
                      <td colspan="7" valign="bottom" class="black_ar">
						
					  <html:button property="addDistributionProtocolEvents" styleClass="blue_ar_b" onclick="insRow('SpecimenRequirementData')" > <bean:message key="buttons.addMore" /></html:button> |&nbsp;<html:button property="deleteValue" styleClass="blue_ar_b" onclick="deleteChecked('SpecimenRequirementData','DistributionProtocol.do?operation=<%=operation%>&pageOf=pageOfDistributionProtocol&status=true',document.forms[0].counter,'chk_',false)" disabled="true"><bean:message key="buttons.delete" /></html:button></td>
                      </tr>
					  </tbody>
                  </table></td>
              </tr>
              <tr class="td_color_F7F7F7">
                <td colspan="3">&nbsp;</td>
              </tr>
              <tr  class="td_color_F7F7F7">
                <td colspan="3" class="buttonbg">
			<%
				   	String action = "confirmDisable('" + formName +"',document.forms[0].activityStatus)";
			%>
			<%	
							String deleteAction = "deleteObject('" + formName +"','" + Constants.ADMINISTRATIVE + "')";
			%>
				<html:button styleClass="blue_ar_b" property="submitPage" onclick="<%=action%>">
						   		<bean:message key="buttons.submit"/>
				</html:button>
				<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.EDIT%>">
							&nbsp;| 
							<span class="cancellink"><html:link
												href="#" onclick="<%=deleteAction%>" styleClass="blue_ar_s_b">
													<bean:message key="buttons.delete" />
												</html:link></span>
				</logic:equal>
                  &nbsp;| <span class="cancellink"><html:link
												page="/ManageAdministrativeData.do" styleClass="blue_ar_s_b">
													<bean:message key="buttons.cancel" />
												</html:link></span></td>
              </tr>
            </table></td>
          </tr>
          
        </table></td>
      </tr>
    </table></td>
  </tr>
</table>
<p><!--end content -->
</p>
</html:form>
</body>