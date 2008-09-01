<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/nlevelcombo.tld" prefix="ncombo" %>
<%@ page language="java" isELIgnored="false"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="java.util.*"%>
<%@ page import="edu.wustl.catissuecore.util.global.Variables"%>
<%@ page import="edu.wustl.catissuecore.actionForm.ViewSurgicalPathologyReportForm"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.domain.pathology.ConceptReferent"%>
<%@ page import="edu.wustl.catissuecore.domain.pathology.Concept"%>
<%@ page import="edu.wustl.catissuecore.bean.ConceptHighLightingBean"%>
<%@ page import="java.util.regex.Matcher"%>
<%@ page import="java.util.regex.Pattern"%>

<script src="jss/ajax.js"></script>
<script language="JavaScript" type="text/javascript" src="jss/viewSPR.js"></script>
<LINK href="css/styleSheet.css" type=text/css rel=stylesheet>
<link href="css/catissue_suite.css" rel="stylesheet" type="text/css" />
<%
	Map mapPMI = null;
	int noOfRowsPMI=0;
	ViewSurgicalPathologyReportForm formSPR=null;
	Object objAbsForm=null;
	String deidText=null;
	boolean hasAccess=false;
	if(operation.equals("viewSPR"))
	{		
		objAbsForm = request.getAttribute("viewSurgicalPathologyReportForm");
   		
		if(objAbsForm != null && objAbsForm instanceof ViewSurgicalPathologyReportForm)
		{
			formSPR=(ViewSurgicalPathologyReportForm)objAbsForm;
			mapPMI = formSPR.getValues();
			noOfRowsPMI = formSPR.getCounter();
			deidText=formSPR.getDeIdentifiedReportTextContent();
			hasAccess=formSPR.isHasAccess();
		}
	}
	List conceptClassificationList1 = (List)request.getAttribute(Constants.CONCEPT_BEAN_LIST);
	String[] onClickMethod = null;
	String[] colours = Constants.CATEGORY_HIGHLIGHTING_COLOURS;
	String[] conceptName = null;
	String[] startOff = null;
	String[] endOff = null;	
	if(conceptClassificationList1!=null)
	{
		onClickMethod=new String[conceptClassificationList1.size()];
		conceptName = new String[conceptClassificationList1.size()];
		startOff = new String[conceptClassificationList1.size()];
		endOff = new String[conceptClassificationList1.size()];	
		for(int i=0;i<conceptClassificationList1.size();i++)
		{
			ConceptHighLightingBean referentClassificationObj=(ConceptHighLightingBean) conceptClassificationList1.get(i);
			conceptName[i] = referentClassificationObj.getConceptName();
			startOff[i] = referentClassificationObj.getStartOffsets();
			endOff[i] = referentClassificationObj.getEndOffsets();	
			Pattern p = Pattern.compile("['\"]");
			Matcher m = p.matcher(conceptName[i]);
			conceptName[i] = m.replaceAll("");
		}
	}
%>

<head>
	<script>
	<%for(int i=0;i<colours.length;i++)
	{
	%>
		colours[<%=i%>]='<%=colours[i]%>';
	<%
	}
	%>
	<%
	if(conceptName!=null)
	{
		for(int i=0;i<conceptName.length;i++)
		{
	%>
		conceptName[<%=i%>]='<%=conceptName[i]%>';
		startOff[<%=i%>]='<%=startOff[i]%>';
		endOff[<%=i%>]='<%=endOff[i]%>';
	<%
		}
	}
	%>

	function checkBoxClicked()
	{
		document.getElementById("deidentifiedReportText").innerHTML="<span class="+"pre"+"><PRE class='pre'>"+document.getElementById("deidText").innerHTML+"</PRE></span>";
		for(i=0;i<conceptName.length;i++)
		{
			selectByOffset(document.getElementById("select"+i),startOff[i],endOff[i],colours[i],conceptName[i]);	
		}		
	}
	</script>
</head>
<%
if(!hasAccess)
{
%>
	<body onload="setUI();">
<%
}
%>
<html:form action="<%=Constants.VIEW_SPR_ACTION%>">
<!-- ------------------------------------- -->

      <table width="100%" border="0" cellpadding="3" cellspacing="0" id="reportTable" class="whitetable_bg">
        <tr>
          <td colspan="2" align="left" class="toptd"></td>
        </tr>
		<html:hidden property="id" />
			<html:hidden property="identifiedReportId" />
			<html:hidden property="deIdentifiedReportId" />
			<html:hidden property="submittedFor"/>
			<html:hidden property="forwardTo"/>
			<html:hidden property="pageOf"/>
			<html:hidden property="acceptReject"/>
			<html:hidden property="participantIdForReport"/>

        <tr>
          <td colspan="2" align="left">
		  <% if(pageOf.equals(Constants.PAGEOF_PARTICIPANT) || pageOf.equals(Constants.PAGE_OF_PARTICIPANT_CP_QUERY))
			{
		  %>
		  <table width="100%" border="0" cellspacing="0" cellpadding="2">
                <tr>
                  <td width="27%" align="left" valign="middle" class="black_ar">&nbsp;<bean:message key="viewSPR.reportInfo.spn"/></td>
                  <td width="73%" align="left" class="black_ar"><label>
				  <logic:notEmpty name="viewSurgicalPathologyReportForm" property="reportIdList" >

                    <c:set var="reportIdElt" value="${viewSurgicalPathologyReportForm.reportIdList}"/>
				<jsp:useBean id="reportIdElt" type="java.util.List"/>
				<html:select property="reportId" styleClass="formFieldSized18" styleId="reportId" size="1"
				 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)" onchange="sendRequestForReportInfo()">
					<html:options collection="reportIdElt" labelProperty="name" property="value"/>
				</html:select>
                  </logic:notEmpty>
				  </label></td>
                </tr>
            </table>
		  <%
			}
		%>
			  </td>
        </tr>
        <tr>
          <td colspan="2" align="left" class="tr_bg_blue1"><span class="blue_ar_b"> &nbsp;<bean:message key="viewSPR.view.title" /></span></td>
        </tr>
		
        <tr>
          <td colspan="2" align="left" class="showhide">
		  <table width="100%" border="0" cellspacing="0" cellpadding="3">
		  <% 
	if(hasAccess)
	{
	%>	
                <tr>
                  <td width="27%" align="left" class="black_ar_t"><input type=radio name="review" value="abc1" checked="checked" onClick="clickOnLinkReport()" />
							<bean:message key="viewSPR.identifiedReport" /></td>
                <td width="28%" align="left" class="black_ar_t"><input type=radio name="review" value="abc2" onClick="clickOnLinkShowDeidReport()" />
							<bean:message key="viewSPR.deIdenfiedReport" /></td>
                  <td width="28%" align="left" class="black_ar_t"><input type=radio name="review" value="abc3" onClick="clickOnLinkCompareReport()" />
							<bean:message key="viewSPR.compareReports" /></td>
                  <td width="17%" align="left" class="black_ar_t"><input type=radio name="review" value="abc4" onClick="" disabled="true" />
							<bean:message key="viewSPR.myRequests" /></td>
                </tr>
	<% 	
	}
	else
	{
	%>
				<tr>
                  <td width="50%" colspan="2" align="left" class="black_ar_t"><input type=radio name="review" value="abc2" checked="checked" onClick="setUI()" />
							<bean:message key="viewSPR.deIdenfiedReport" /></td>
                <td width="50%" colspan="2" align="left" class="black_ar_t"><input type=radio name="review" value="abc4" onClick="" disabled="true" />
							<bean:message key="viewSPR.myRequests" /></td>
                 
                </tr>
				<%
	}
	%>

	<%
				String requestFor=(String)request.getParameter(Constants.REQUEST_FOR);
				if(requestFor!=null||pageOf.equals(Constants.PAGEOF_REVIEW_SPR)||pageOf.equals(Constants.PAGEOF_QUARANTINE_SPR)||requestFor!=null)
				{
				%>
					<tr>
					<td width="20%" class="formTitle">
						<% if(formSPR.getUserName()!=null||requestFor!=null)
							{
						%>
								<%=formSPR.getUserName()%> <bean:message key="viewSPR.label.comment"/>
						<%
							}
						%>
					</td>
				
					<td width="80%">
						<html:textarea property="userComments" rows="3" cols="32" readonly="true"/>
					</td>
				</tr>
				<%
				}
				%>

              
          </table></td>
        </tr>
		<tr>
		<td colspan="2">
		<table id="participantTable" border="0" cellpadding="0" cellspacing="0"   width="100%" id="table2" >
		
        <tr onclick="javascript:showHide('add_id')">
          <td width="90%" align="left" class="tr_bg_blue1"><span class="blue_ar_b">&nbsp;&nbsp;<bean:message key="viewSPR.participantInformation.title"/></span></td>
          <td width="10%" align="right" class="tr_bg_blue1"><a id="imgArrow_add_id" href="#"><img src="images/uIEnhancementImages/dwn_arrow1.gif" width="80" height="9" hspace="10" border="0" alt="Show Details"/></a></td>
        </tr>
        <tr>
          <td colspan="2" class="showhide1"><div id="add_id" style="display:none; margin:0px; padding:0px;">
              <table width="100%" border="0" cellspacing="0" cellpadding="3" >
                <tr>
                  <td width="23%" class="noneditable"><label for="type"><strong><bean:message key="participant.name" /></strong></label></td>
                  <td width="25%" colspan="3" class="noneditable">-  <logic:notEmpty name="viewSurgicalPathologyReportForm" property="participantName" >
								<%=formSPR.getParticipantName()%>
							</logic:notEmpty></td>
                  <td width="23%" class="noneditable"><span><strong><bean:message key="participant.vitalStatus" /></strong></span></td>
                  <td width="29%" class="noneditable">-  <logic:notEmpty name="viewSurgicalPathologyReportForm" property="vitalStatus" >
				     			<%=formSPR.getVitalStatus()%>
							</logic:notEmpty></td>
                </tr>
                <tr>
                  <td class="noneditable"><label for="type"><strong><bean:message key="participant.birthDate" /></strong></label></td>
                  <td colspan="3" class="noneditable">- <logic:notEmpty name="viewSurgicalPathologyReportForm" property="birthDate" >
				     			<%=formSPR.getBirthDate()%>
							</logic:notEmpty></td>
                  <td class="noneditable"><span><strong><bean:message key="participant.deathDate" /></strong></span></td>
                  <td class="noneditable">- <logic:notEmpty name="viewSurgicalPathologyReportForm" property="deathDate" >
				     			<%=formSPR.getDeathDate()%>
							</logic:notEmpty></td>
                </tr>
                <tr>
                  <td class="noneditable"><label for="User"><strong><bean:message key="participant.ethnicity" /></strong></label></td>
                  <td colspan="3" class="noneditable">- <logic:notEmpty name="viewSurgicalPathologyReportForm" property="ethinicity" >
				     			<%=formSPR.getEthinicity()%>
							</logic:notEmpty></td>
                  <td class="noneditable"><span><strong><bean:message key="participant.race" /></strong></span></td>
                  <td class="noneditable">- <logic:notEmpty name="viewSurgicalPathologyReportForm" property="race" >
									<%=formSPR.getRace()%>
							</logic:notEmpty></td>
                </tr>
                <tr>
                  <td class="noneditable"><strong><bean:message key="participant.gender" /></strong></td>
                  <td colspan="3" class="noneditable">- <logic:notEmpty name="viewSurgicalPathologyReportForm" property="gender" >
				     			<%=formSPR.getGender()%>
							</logic:notEmpty></td>
                  <td class="noneditable"><span><strong><bean:message key="participant.genotype" /></strong></span></td>
                  <td class="noneditable">- <logic:notEmpty name="viewSurgicalPathologyReportForm" property="sexGenotype" >
				     			<%=formSPR.getSexGenotype()%>
							</logic:notEmpty></td>
                </tr>
                <tr>
                  <td class="noneditable" ><strong><bean:message key="participant.socialSecurityNumber" /></strong></td>
                  <td colspan="3" valign="top" class="noneditable">- <logic:notEmpty name="viewSurgicalPathologyReportForm" property="socialSecurityNumber" >
				     			<%=formSPR.getSocialSecurityNumber()%>
							</logic:notEmpty></td>
                  <td class="noneditable" >&nbsp;</td>
                  <td class="noneditable">&nbsp;</td>
                </tr>
              </table>
              <table width="100%" border="0" cellspacing="0" cellpadding="3">
                
                <tr class="black_ar_b">
                  <td colspan="3" class="bottomtd" align="left"></td>
                </tr>
                <tr class="black_ar_b">
                  <td colspan="3" align="left"><strong><bean:message key="viewSPR.participantInformation.medicalIdentifiers"/></strong> </td>
                </tr>
                <tr class="tableheading">
                  <td width="3%" align="left" class="black_ar_b">#</td>
                  <td width="12%" align="left" class="black_ar_b"><bean:message key="medicalrecord.source"/></td>
                  <td width="85%" class="black_ar_b"><bean:message key="medicalrecord.number"/></td>
              </tr>
                <tbody id="addMore">
				<%
				for(int i=1;i<=noOfRowsPMI;i++)
				{
					String siteName = "ParticipantMedicalIdentifier:"+i+"_Site_id";
					String medicalRecordNumber = "ParticipantMedicalIdentifier:"+i+"_medicalRecordNumber";
					if(!((String)formSPR.getValue(siteName)).startsWith("--"))
					{
				%>
				 <tr>
				 	<td class="formFieldNoBordersSimple" width="5"><%=i%>.
				 		
				 	</td>
				      <td class="formFieldNoBordersSimple">
				     	<%=formSPR.getValue(siteName)%>

				    </td>
				    <td  class="formFieldNoBordersSimple" colspan="2">
						<%=formSPR.getValue(medicalRecordNumber)%>
				    </td>
				 </tr>
				 <%
				 	}
				}
				%>
				 </tbody>
          </table></td>
        </tr>
		</table>
		</td></tr>
        <tr>
          <td></td>
        </tr>

		

        <tr>
          <td colspan="2" align="left">
	<table border="0" cellpadding="5" cellspacing="0" width="100%" id="identifiedReportInfo" >
		  <tr>
		  <td colspan="5" class="tr_bg_blue1">
		  <span class="blue_ar_b"> &nbsp;<bean:message key="viewSPR.identifiedReportInformation.title"/></span></td>
        </tr>
        <tr>
          <td colspan="5">
		  
			<table width="100%" border="0" cellspacing="0" cellpadding="3" >
              <tr>
                <td width="27%" class="noneditable"><label for="type"><strong><bean:message key="viewSPR.reportInfo.spn" /></strong></label></td>
                <td width="20%" colspan="3" class="noneditable">-  <logic:notEmpty name="viewSurgicalPathologyReportForm" property="surgicalPathologyNumber" >
				     		<%=formSPR.getSurgicalPathologyNumber()%>
						</logic:notEmpty></td>
                <td width="25%" class="noneditable"><span><strong><bean:message key="specimenCollectionGroup.site"/></strong></span></td>
                <td width="28%" class="noneditable">- <SPAN id="identifiedReportSite"><logic:notEmpty name="viewSurgicalPathologyReportForm" property="identifiedReportSite" >
				     		<%=formSPR.getIdentifiedReportSite()%>
						</logic:notEmpty></SPAN> </td>
              </tr>
          </table></td>
        </tr>
        <tr>
          <td colspan="5" style="padding-left:6px; padding-right:6px;" class="black_ar">
           		<div id="identifiedReportText" class="pre" style="overflow:auto;height:125px;width:650px;border:2px solid #eef5fb;" onmousemove='scrollInSync();'><table  style="table-layout: fixed;"><tr><td><PRE class="pre"><logic:notEmpty name="viewSurgicalPathologyReportForm" property="identifiedReportTextContent" ><%=formSPR.getIdentifiedReportTextContent()%></logic:notEmpty>
				</PRE></td></tr></table></div>
          </td>
        </tr>
		</table>

		<tr>
		<td colspan="2">

		<table border="0" cellpadding="5" cellspacing="0" width="100%" id="deidReportInfo" style='display:none'>
			<tr>
		  <td class="tr_bg_blue1">
		  <span class="blue_ar_b"> &nbsp;<bean:message key="viewSPR.deIdentifiedReportInformation.title"/>&nbsp;</span></td>
        </tr>
			<tr>
				<td>
					<table border="0" cellpadding="0" width="100%" cellspacing="0" id="categoryHighlighter" >
						<tr>
							<td class="noneditable"><strong>
								<bean:message key="viewSPR.categoryHighlighter.title"/>
							</strong></td>
						</tr>
						 <tr>
						  <td>
						  <table id="classificationNames" border="0"  cellpadding="5" width="100%" cellspacing="0">
						<%
						  List conceptClassificationList = (List)request.getAttribute(Constants.CONCEPT_BEAN_LIST);
						  int chkBoxNo = 0;			  
						  if(conceptClassificationList != null && conceptClassificationList.size() > 0)
						  {%>
						 
						  <tr id="classificationNamesRow">
						<logic:iterate id="referentClassificationObj" collection="<%= conceptClassificationList %>" type="edu.wustl.catissuecore.bean.ConceptHighLightingBean">
							<td class="black_ar">
								<% String chkBoxId = "select"+chkBoxNo; %>
								<input type="checkbox" id="<%=chkBoxId %>" onclick="checkBoxClicked()" />
								<% String spanStyle = "background-color:"+colours[chkBoxNo];%>
								<span id="classificationName" style="<%=spanStyle %>">
									<%=referentClassificationObj.getClassificationName() %>	
								</span>		
							</td>
						
						<% chkBoxNo++;%>
						</logic:iterate>
						</tr>
						
					<%} %>
					</table>
						</td>
					</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td class="black_ar" style="padding-left:6px; padding-right:6px;">
					<div id="deidentifiedReportText" class="pre" style="overflow:auto;height:125px;width:650px;border: 2px solid #eef5fb;"><table style="table-layout: fixed;"><tr><td><PRE class="pre"><logic:notEmpty name="viewSurgicalPathologyReportForm" property="deIdentifiedReportTextContent" ><%=formSPR.getDeIdentifiedReportTextContent()%></logic:notEmpty></PRE></td></tr></table>
				</div>
				</td>
			</tr>
		</table>
		</td></tr>
		
        <tr>
          <td colspan="2" class="bottomtd"><table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td width="20%" valign="top" class="black_ar">&nbsp;<bean:message key="viewSPR.label.comment"/></td>
                <td width="80%" valign="top">
                  <html:textarea property="comments" rows="3" cols="50"/>                </td>
              </tr>
          </table></td>
        </tr>

        <tr>
          <td colspan="2" class="buttonbg"><%
	pageOf=request.getParameter(Constants.PAGEOF);
	if(pageOf.equals(Constants.PAGEOF_REVIEW_SPR))
	{
%>
		<input type="button" name="doneButton" style="actionButton" value="Finish Review " onclick="finishReview()"/>
		
<%					
	}
	else if(pageOf.equals(Constants.PAGEOF_QUARANTINE_SPR))
	{
%>
		
		<input type="button" name="doneButton" style="blue_ar_c" value="Accept" onclick="submitAcceptComments()"/>
		
		<input type="button" name="doneButton" style="blue_ar_c" value="Reject" onclick="submitRejectComments()"/>
		
<%
	}
	else
	{
%>

<%
	String consentTier =(String)request.getParameter("consentTierCounter");
	String submitReviewComments = "submitReviewComments('"+ consentTier+"')";
	String submitQuarantineComments = "submitQuarantineComments('"+ consentTier+"')";
	Boolean isReviewDisabled = new Boolean(true);
	Boolean isQuarantineDisabled = new Boolean(true);
	if((!(formSPR.getIdentifiedReportId().equals("")) || formSPR.getDeIdentifiedReportId()!=0)) 
	{
		isReviewDisabled=new Boolean(false);
	}
	if(formSPR.getDeIdentifiedReportId()!=0)
	{
		isQuarantineDisabled=new Boolean(false);
	}
%>
					<html:button property="action1" styleClass="blue_ar_c" onclick="<%=submitReviewComments%>" disabled="<%=isReviewDisabled.booleanValue()%>" >
						<bean:message key="viewSPR.button.requestForReview" />
					</html:button> &nbsp;|&nbsp;
					<html:button property="action2" styleClass="blue_ar_c" onclick="<%=submitQuarantineComments%>" disabled="<%=isQuarantineDisabled.booleanValue()%>" >
						<bean:message key="viewSPR.button.requestForQuarantine" />
					</html:button> &nbsp;|&nbsp;
					<html:link	page="/QueryManageBioSpecimen.do" styleClass="cancellink">
						<bean:message key="buttons.cancel" />
					</html:link>
<%
	}
%>
				</td>
        </tr>
		<tr>
		<td colspan="2">
			<span id="deidText" class="pre" style="display:none;width:650px;"><PRE class="pre"><%=deidText%></PRE></span>
		</td>
	</tr>
    </table>
</html:form>
