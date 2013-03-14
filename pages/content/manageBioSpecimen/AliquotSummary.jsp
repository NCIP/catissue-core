<!-- 
	This is the aliquot summary page.
-->

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.util.global.AppUtility"%>
<%@ page import="edu.wustl.catissuecore.actionForm.AliquotForm"%>
<%@ page import="edu.wustl.catissuecore.util.global.AppUtility"%>
<%@ page import="java.util.*"%>
<%@ page import="edu.wustl.catissuecore.domain.Specimen"%>
<script type="text/javascript" src="jss/tag-popup.js"></script>
<link rel="STYLESHEET" type="text/css"
	href="dhtmlx_suite/dhtml_pop/css/dhtmlXTree.css">
<script src="dhtmlx_suite/dhtml_pop/js/dhtmlXCommon.js"></script>
<link rel="STYLESHEET" type="text/css"
	href="dhtmlx_suite/dhtml_pop/css/dhtmlXGrid.css" />
<link rel="STYLESHEET" type="text/css"
	href="dhtmlx_suite/dhtml_pop/css/dhtmlxgrid_dhx_skyblue.css" />
<script src="dhtmlx_suite/dhtml_pop/js/dhtmlx.js"></script>
<script src="dhtmlx_suite/dhtml_pop/js/dhtmlXTree.js"></script>
<script src="dhtmlx_suite/dhtml_pop/js/dhtmXTreeCommon.js"></script>
<script src="dhtmlx_suite/dhtml_pop/js/dhtmlXGridCell.js"></script>
<script src="dhtmlx_suite/dhtml_pop/js/dhtmlXTreeGrid.js"></script>


<link rel="stylesheet" type="text/css" href="css/tag-popup.css" />
<link rel="stylesheet" type="text/css" href="dhtmlx_suite/css/dhtmlxwindows.css">
<link rel="stylesheet" type="text/css" href="dhtmlx_suite/skins/dhtmlxwindows_dhx_skyblue.css">
<script src="dhtmlx_suite/js/dhtmlxcommon.js"></script>
<script src="dhtmlx_suite/js/dhtmlxcontainer.js"></script>
<script src="dhtmlx_suite/js/dhtmlxwindows.js"></script>
<link rel="stylesheet" type="text/css"	href="dhtmlx_suite/css/dhtmlxtree.css">
<link rel="STYLESHEET" type="text/css"	href="dhtmlx_suite/css/dhtmlxgrid.css">
<link rel="STYLESHEET" type="text/css" href="css/dhtmlDropDown.css">
<link rel="STYLESHEET" type="text/css"	href="dhtmlx_suite/css/dhtmlxcombo.css">
<link rel="STYLESHEET" type="text/css"	href="dhtmlx_suite/ext/dhtmlxgrid_pgn_bricks.css">
<link rel="STYLESHEET" type="text/css"	href="dhtmlx_suite/skins/dhtmlxtoolbar_dhx_blue.css">
<script language="JavaScript" type="text/javascript" src="jss/dhtmlDropDown.js"></script>
<script src="dhtmlx_suite/js/dhtmlxcommon.js"></script>
<script src="dhtmlx_suite/js/dhtmlxcombo.js"></script>
<script src="dhtmlx_suite/js/dhtmlxtree.js"></script>
<script src="dhtmlx_suite/ext/dhtmlxtree_li.js"></script>
<script src="jss/script.js" type="text/javascript"></script>
<script language="JavaScript" type="text/javascript"	src="jss/javaScript.js"></script>
<script language="JavaScript" type="text/javascript"	src="jss/caTissueSuite.js"></script>
<script src="jss/calendarComponent.js" language="JavaScript"	type="text/javascript"></script>
<script>var imgsrc="images/de/";</script>
<link rel="stylesheet" type="text/css" href="dhtmlx_suite/css/dhtmlxwindows.css">
<link rel="stylesheet" type="text/css" href="dhtmlx_suite/skins/dhtmlxwindows_dhx_skyblue.css">
<script src="dhtmlx_suite/js/dhtmlxcommon.js"></script>
<script src="dhtmlx_suite/js/dhtmlxcontainer.js"></script>
<script src="dhtmlx_suite/js/dhtmlxwindows.js"></script>
<link rel="stylesheet" type="text/css"	href="dhtmlx_suite/css/dhtmlxtree.css">
<link rel="STYLESHEET" type="text/css"	href="dhtmlx_suite/css/dhtmlxgrid.css">
<link rel="STYLESHEET" type="text/css" href="css/dhtmlDropDown.css">
<link rel="STYLESHEET" type="text/css"	href="dhtmlx_suite/css/dhtmlxcombo.css">
<link rel="STYLESHEET" type="text/css"	href="dhtmlx_suite/ext/dhtmlxgrid_pgn_bricks.css">
<link rel="STYLESHEET" type="text/css"	href="dhtmlx_suite/skins/dhtmlxtoolbar_dhx_blue.css">
<script language="JavaScript" type="text/javascript" src="jss/dhtmlDropDown.js"></script>
<script src="dhtmlx_suite/js/dhtmlxcommon.js"></script>
<script src="dhtmlx_suite/js/dhtmlxcombo.js"></script>
<script src="dhtmlx_suite/js/dhtmlxtree.js"></script>
<script src="dhtmlx_suite/ext/dhtmlxtree_li.js"></script>
<script src="jss/script.js" type="text/javascript"></script>

<script src="jss/script.js"></script>
<script src="jss/calendarComponent.js"></script>
<SCRIPT>var imgsrc="images/";</SCRIPT>
<LINK href="css/calanderComponent.css" type=text/css rel=stylesheet>
<script language="JavaScript" type="text/javascript"	src="javascripts/de/prototype.js"></script>
<script language="JavaScript" type="text/javascript"	src="javascripts/de/scr.js"></script>
<script language="JavaScript" type="text/javascript"	src="javascripts/de/combobox.js"></script>
<script language="JavaScript" type="text/javascript"	src="jss/ext-base.js"></script>
<script language="JavaScript" type="text/javascript"	src="jss/ext-all.js"></script>
<script language="JavaScript" type="text/javascript"	src="javascripts/de/ajax.js"></script>
<script language="JavaScript" type="text/javascript"	src="/jss/multiselectUsingCombo.js"></script>
<LINK href="css/catissue_suite.css" type="text/css" rel="stylesheet">
<link rel="stylesheet" type="text/css" href="css/styleSheet.css" />
<link rel="stylesheet" type="text/css"	href="css/clinicalstudyext-all.css" />
<link rel="STYLESHEET" type="text/css"	href="dhtmlx_suite/css/dhtmlxgrid.css">
<link rel="STYLESHEET" type="text/css" href="css/dhtmlDropDown.css">
<link rel="STYLESHEET" type="text/css"	href="dhtmlx_suite/css/dhtmlxcombo.css">
<link rel="STYLESHEET" type="text/css"	href="dhtmlx_suite/ext/dhtmlxgrid_pgn_bricks.css">
<link rel="STYLESHEET" type="text/css"	href="dhtmlx_suite/skins/dhtmlxtoolbar_dhx_blue.css">
<script language="JavaScript" type="text/javascript" src="jss/dhtmlDropDown.js"></script>


<script language="JavaScript" type="text/javascript" src="jss/script.js"></script>
<link rel="stylesheet" type="text/css" href="css/styleSheet.css" />
<link href="css/catissue_suite.css" rel="stylesheet" type="text/css" />
<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
<script language="JavaScript" type="text/javascript" src="jss/antiSpecAjax.js"></script>
<script language="JavaScript" type="text/javascript" src="jss/GenericSpecimenDetailsTag.js"></script>
<script src="jss/ajax.js" type="text/javascript"></script>






<link rel="stylesheet" type="text/css" href="dhtmlx_suite/css/dhtmlxwindows.css">
<link rel="stylesheet" type="text/css" href="dhtmlx_suite/skins/dhtmlxwindows_dhx_skyblue.css">
<script src="dhtmlx_suite/js/dhtmlxcommon.js"></script>
<script src="dhtmlx_suite/js/dhtmlxcontainer.js"></script>
<script src="dhtmlx_suite/js/dhtmlxwindows.js"></script>
<link rel="stylesheet" type="text/css"	href="dhtmlx_suite/css/dhtmlxtree.css">
<link rel="STYLESHEET" type="text/css"	href="dhtmlx_suite/css/dhtmlxgrid.css">
<link rel="STYLESHEET" type="text/css" href="css/dhtmlDropDown.css">
<link rel="STYLESHEET" type="text/css"	href="dhtmlx_suite/css/dhtmlxcombo.css">
<link rel="STYLESHEET" type="text/css"	href="dhtmlx_suite/ext/dhtmlxgrid_pgn_bricks.css">
<link rel="STYLESHEET" type="text/css"	href="dhtmlx_suite/skins/dhtmlxtoolbar_dhx_blue.css">
<script language="JavaScript" type="text/javascript" src="jss/dhtmlDropDown.js"></script>
<script src="dhtmlx_suite/js/dhtmlxcommon.js"></script>
<script src="dhtmlx_suite/js/dhtmlxcombo.js"></script>
<script src="dhtmlx_suite/js/dhtmlxtree.js"></script>
<script src="dhtmlx_suite/ext/dhtmlxtree_li.js"></script>


<script type="text/javascript" src="dhtmlx_suite/js/dhtmlxgridcell.js"></script>
<script type="text/javascript" src="dhtmlx_suite/js/connector.js"></script>
<script type="text/javascript" src="dhtmlx_suite/ext/dhtmlxgrid_filter.js"></script>
<script type="text/javascript" src="dhtmlx_suite/ext/dhtmlxgrid_pgn.js"></script>
<script type="text/javascript" src="dhtmlx_suite/js/dhtmlxtoolbar.js"></script>
<script src="jss/script.js" type="text/javascript"></script>
<script language="JavaScript" type="text/javascript"	src="jss/javaScript.js"></script>
<script language="JavaScript" type="text/javascript"	src="jss/caTissueSuite.js"></script>
<script src="jss/calendarComponent.js" language="JavaScript"	type="text/javascript"></script>
<script>var imgsrc="images/de/";</script>
<script language="JavaScript" type="text/javascript"	src="javascripts/de/prototype.js"></script>
<script language="JavaScript" type="text/javascript"	src="javascripts/de/scr.js"></script>
<script language="JavaScript" type="text/javascript"	src="javascripts/de/combobox.js"></script>
<script language="JavaScript" type="text/javascript"	src="jss/ext-base.js"></script>
<script language="JavaScript" type="text/javascript"	src="jss/ext-all.js"></script>
<script language="JavaScript" type="text/javascript"	src="javascripts/de/ajax.js"></script>
<script language="JavaScript" type="text/javascript"	src="/jss/multiselectUsingCombo.js"></script>
<LINK href="css/catissue_suite.css" type="text/css" rel="stylesheet">
<link rel="stylesheet" type="text/css" href="css/styleSheet.css" />
<link rel="stylesheet" type="text/css"	href="css/clinicalstudyext-all.css" />
<link rel="STYLESHEET" type="text/css"	href="dhtmlx_suite/css/dhtmlxgrid.css">
<link rel="STYLESHEET" type="text/css" href="css/dhtmlDropDown.css">
<link rel="STYLESHEET" type="text/css"	href="dhtmlx_suite/css/dhtmlxcombo.css">
<link rel="STYLESHEET" type="text/css"	href="dhtmlx_suite/ext/dhtmlxgrid_pgn_bricks.css">
<link rel="STYLESHEET" type="text/css"	href="dhtmlx_suite/skins/dhtmlxtoolbar_dhx_blue.css">
<script language="JavaScript" type="text/javascript" src="jss/dhtmlDropDown.js"></script>
<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
<script language="JavaScript" >
		//Set last refresh time
		if(window.parent!=null)
		{
			if(window.parent.lastRefreshTime!=null)
			{
				window.parent.lastRefreshTime = new Date().getTime();
			}
		}	
</script>
<%
	String specimenLabelsWithComma="";
	String formName = Constants.ALIQUOT_SUMMARY_ACTION;
	String pageOf = (String)request.getAttribute(Constants.PAGE_OF);
	String CPQuery = (String)request.getAttribute(Constants.CP_QUERY);
	if(CPQuery != null)
	{
		formName = Constants.CP_QUERY_ALIQUOT_SUMMARY_ACTION;
	String nodeId="Specimen_";
	if(request.getAttribute(Constants.PARENT_SPECIMEN_ID) != null )
	{
		String parentSPId = (String) request.getAttribute(Constants.PARENT_SPECIMEN_ID);
		nodeId = nodeId+parentSPId;
%>
    
		<script language="javascript">
			refreshTree('<%=Constants.CP_AND_PARTICIPANT_VIEW%>','<%=Constants.CP_TREE_VIEW%>','<%=Constants.CP_SEARCH_CP_ID%>','<%=Constants.CP_SEARCH_PARTICIPANT_ID%>','<%=nodeId%>');			
		</script>
	<%
		}}
	%>

<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
<link href="css/catissue_suite.css" rel="stylesheet" type="text/css" /> 
<script>
	var newWindow;
	function showNewPage(action)
	{
	   	if(newWindow!=null)
		{
		   newWindow.close();
		}
	     newWindow = window.open(action,'','scrollbars=yes,status=yes,resizable=yes,width=860, height=600');
	     newWindow.focus(); 
	
    }
    
    function CPQueryAliquot(action)
	{
		window.parent.frames[1].location = action;
	}	
		 
function giveCall(url,msg,msg1,id)
{
	document.getElementsByName('objCheckbox').value=id;
	document.getElementsByName('objCheckbox').checked = true;
	ajaxAssignTagFunctionCall(url,msg,msg1);
}
 
</script>
<html:form action="<%=formName%>">
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="maintable">
<logic:empty name="CPQuery">
  <tr>
    <td class="td_color_bfdcf3"><table border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td class="td_table_head"><span class="wh_ar_b"> <bean:message key="aliquots.header" /></span></td>
        <td><img src="images/uIEnhancementImages/table_title_corner2.gif" alt="Page Title - Specimen Aliquot" width="31" height="24" /></td>
      </tr>
    </table></td>
  </tr>
  </logic:empty>
  <tr>
    <td class="tablepadding">
	
	<table width="100%" border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td class="td_tab_bg"><img src="images/uIEnhancementImages/spacer.gif" alt="spacer" width="50" height="1"></td>
		<logic:empty name="CPQuery">
        <td valign="bottom"><html:link page="/SimpleQueryInterface.do?pageOf=pageOfNewSpecimen&aliasName=Specimen"><img src="images/uIEnhancementImages/tab_edit_notSelected.jpg"  border="0"alt="Edit" width="59" height="22" /></html:link></td>
        <td valign="bottom"><html:link page="/CreateSpecimen.do?operation=add&pageOf=pageOfDeriveSpecimen&virtualLocated=true"><img src="images/uIEnhancementImages/tab_derive2.gif" alt="Derive" width="56" height="22" border="0" /></html:link></td>
		</logic:empty>
		
        <td valign="bottom"><img src="images/uIEnhancementImages/tab_aliquot.gif" alt="Aliquot" width="66" height="22" /></td>
		<logic:empty name="CPQuery">
        <td valign="bottom"><html:link page="/QuickEvents.do?operation=add"><img src="images/uIEnhancementImages/tab_events2.gif" alt="Events" width="56" height="22" border="0"/></html:link></td>
        <td valign="bottom"><html:link page="/MultipleSpecimenFlexInitAction.do?pageOf=pageOfMultipleSpWithMenu"><img src="images/uIEnhancementImages/tab_multiple2.gif" alt="Multiple" width="66" height="22" border="0" /></html:link></td>
		</logic:empty>
        <td width="90%" class="td_tab_bg">&nbsp;</td>
      </tr>
    </table>
	
      <table width="100%" border="0" cellpadding="3" cellspacing="0" class="whitetable_bg">
        <tr>
          <td width="61%" align="left" class="toptd"><%@ include file="/pages/content/common/ActionErrors.jsp" %></td>
        </tr>
        
        <tr>
		<%
			AliquotForm form = (AliquotForm)request.getAttribute("aliquotForm");
			String unit = "";
			String conc = "";
			if(form.getConcentration()!=null)
			{
				conc = form.getConcentration();
			}

			if(form != null)
			{
				unit = AppUtility.getUnit(form.getClassName(),form.getType());
			}
		%>
          <td colspan="2" align="left" class="tr_bg_blue1"><span class="blue_ar_b"> &nbsp;<bean:message key="aliquots.summaryTitle"/></span></td>
        </tr>
		 <tr>
          <td align="left" class="showhide"><table width="100%" border="0" cellspacing="0" cellpadding="3">
              <tr>
                <td width="20%" class="noneditable"><strong>
					<label for="type">
				<bean:message key="specimen.type"/> 
			</label>
			</strong>
			</td>
			<td width="30%" class="noneditable">- <%=form.getClassName()%>
			<html:hidden styleId="className" property="className" />
		</td>
		<td width="20%" class="noneditable"><strong>
			<label for="subType">
				<bean:message key="specimen.subType"/> 
			</label>
			</strong>
		</td>
		<td width="30%" class="noneditable">-  <%=form.getType()%>
			<html:hidden styleId="type" property="type" />
		</td>
	</tr>
	<tr>
                <td class="noneditable"><strong>
					<label for="tissueSite">
				<bean:message key="specimen.tissueSite"/> 
			</label>
			</strong>
			</td>
			<td class="noneditable">-  <%=form.getTissueSite()%>
			<html:hidden styleId="tissueSite" property="tissueSite" />
		</td>
		<td class="noneditable"><strong>
			<label for="tissueSide">
				<bean:message key="specimen.tissueSide"/> 
			</label>
			</strong>
		</td>
		<td class="noneditable">- <%=form.getTissueSide()%>
			<html:hidden styleId="tissueSide" property="tissueSide" />
		</td>
			</tr>
              <tr>
                <td class="noneditable"><strong>
					<label for="pathologicalStatus">
				<bean:message key="specimen.pathologicalStatus"/> 
			</label>
			</strong>
			</td>
			<td class="noneditable">- <%=form.getPathologicalStatus()%>
			<html:hidden styleId="pathologicalStatus" property="pathologicalStatus" />
		</td>
		<td class="noneditable"><strong>
			<label for="concentration">
				<bean:message key="specimen.concentration"/> 
			</label>
			</strong>
		</td>
		<td class="noneditable">- <bean:write name="aliquotForm" property="concentration"/> &nbsp;
		<bean:message key="specimen.concentrationUnit"/>
			<html:hidden styleId="concentration" property="concentration" />
		</td>
		</tr>
              <tr>
                <td class="noneditable"><strong>
					<label for="availableQuantity">
				<bean:message key="specimen.availableQuantity"/> 
			</label>
			</strong>
			</td>
			<td class="noneditable">- <%=form.getAvailableQuantity()%>&nbsp; <%=unit%>
			<html:hidden styleId="availableQuantity" property="availableQuantity" />
			</td>
			<td class="noneditable"><strong><label for="createdDate">
				<bean:message key="specimen.createdDate"/>
			</label></strong></td>
			<td class="noneditable">- 
				<%=form.getCreatedDate()%>&nbsp;<span class="grey_ar_s"><bean:message key="date.pattern"/></span>
			<html:hidden styleId="createdDate" property="createdDate" />	
			<html:hidden styleId="spCollectionGroupId" property="spCollectionGroupId" />
			<html:hidden styleId="scgName" property="scgName" />
			</td>
			</tr>
			
          </table></td>
        </tr>
		<tr>
          <td class="showhide1"><table width="100%" border="0" cellspacing="0" cellpadding="4">
              <tr class="tableheading">
                <td width="2%" align="left" class="black_ar_b">#</td>
                <td width="15%" align="left" nowrap="nowrap" class="black_ar_b">
					<bean:message key="specimen.label"/>
					</td>
					<td width="15%" class="black_ar_b">&nbsp;
					<bean:message key="specimen.barcode"/>
				</td>
                <td width="15%" align="left" nowrap="nowrap" class="black_ar_b">
				<bean:message key="specimen.quantity"/>
				</td>
                
                <td width="55%" class="black_ar_b" >
					<bean:message key="cpbasedentry.storagelocation"/>
				</td>
              </tr>
			  <%
		int counter=0;
	%>
		<logic:iterate name="aliquotForm" property="specimenList" id="aliquot">
		<bean:define id="specimenId" name="aliquot" property="id"/> 
		<bean:define id="specimenLabel" name="aliquot" property="label"/>
			<% 
				String onClickSpecimenFunction = "showNewPage('SearchObject.do?pageOf=pageOfNewSpecimen&operation=search&id=" + specimenId + "')";
				if(CPQuery != null)
				{
					onClickSpecimenFunction = "CPQueryAliquot('QuerySpecimenSearch.do?pageOf=pageOfNewSpecimenCPQuery&id=" + specimenId + "')";
				}
				specimenLabelsWithComma = specimenLabelsWithComma + specimenLabel + ",";
			%>
			
	
              <tr>
				<td align="left" class="black_ar" ><%=++counter%>.  </td>
				<td class="black_ar"><html:link href="#" styleClass="view" styleId="label" onclick="<%=onClickSpecimenFunction%>">
					<bean:write name="aliquot" property="label"/>
					<html:hidden styleId="label" property="createdDate" />
				</html:link>
				</td>
                
                <td class="black_ar">&nbsp;
					<bean:write name="aliquot" property="barcode"/>
				</td>
				<td class="black_ar">
					<bean:write name="aliquot" property="initialQuantity"/>		
					&nbsp; <%=unit%>
				</td>
                <td class="black_ar" align="left">
					<logic:empty name="aliquot" property="specimenPosition">
					Virtually Located 
				</logic:empty>
			<logic:notEmpty name="aliquot" property="specimenPosition">
				<bean:define id="position" name="aliquot" property="specimenPosition"/>
				<logic:notEmpty name="position" property="positionDimensionOne">
					<bean:define id="sc" name="position" property="storageContainer"/>
					<bean:write name="sc" property="name"/> &nbsp;
					
					<bean:write name="position" property="positionDimensionOneString"/> &nbsp;
					<bean:write name="position" property="positionDimensionTwoString"/>
				</logic:notEmpty>
			</logic:notEmpty>
			</td>
              </tr>
			  </logic:iterate>
			      </tr>
          </table></td>
        </tr>
        
        
        <tr>
          <td class=" bottomtd"></td>
        </tr>
        <tr>
          <td class="buttonbg">
			<html:hidden property="submittedFor" value=""/>				
					<html:hidden property="forwardTo" value=""/>					
					<html:hidden property="noOfAliquots"/>					
					<%

						String confirmDisableFuncName = "confirmDisable('" + formName +"',document.forms[0].activityStatus)";			
						String addMoreSubmitFunctionName= "setSubmittedFor('ForwardTo','" + Constants.SPECIMEN_FORWARD_TO_LIST[3][1]+"')";	
						String addMoreSubmit = addMoreSubmitFunctionName + ","+confirmDisableFuncName;
					%>						

				<html:button
							styleClass="blue_ar_b" property="submitAndDistributeButton"
							title="<%=Constants.SPECIMEN_BUTTON_TIPS[3]%>"
							value="<%=Constants.SPECIMEN_FORWARD_TO_LIST[3][0]%>"
							onclick="<%=addMoreSubmit%>">
						</html:button>
						<logic:equal name="IsToShowButton" value="true">
					&nbsp;|&nbsp;
					<%
 						String	organizeTarget = "ajaxTreeGridInitCall('Are you sure you want to delete this specimen from the list?','List contains specimens, Are you sure to delete the selected list?','SpecimenListTag','SpecimenListTagItem')";
 %>
					<html:button
							styleClass="blue_ar_b" property="Add To Specimen List"
							title="Add To Specimen List"
							value="Add To Specimen List"
							onclick="<%=organizeTarget%>">
						</html:button>
					</logic:equal>
				<input type="checkbox" name="objCheckbox"  id="objCheckbox" style="display:none" value="team" checked/>
				</td>
				</tr>
    </table></td>
  </tr>
  <script>
			
		</script>
</table>
</html:form>
<!----------------------------------------------------------------------------------------->
<%
	String specId = (String)request.getAttribute("popUpSpecList");
	String	assignTargetCall = "giveCall('AssignTagAction.do?entityTag=SpecimenListTag&entityTagItem=SpecimenListTagItem&objChkBoxString="+specId+"','Select at least one existing list or create a new list.','No specimen has been selected to assign.','"+specId+"')";
 %>
<%@ include file="/pages/content/manageBioSpecimen/SpecimenTagPopup.jsp" %>
