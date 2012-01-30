<% response.setHeader("Cache-Control","no-cache");
   response.setHeader("Pragma","no-cache"); %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="java.util.Collection"%>
<%@ page import="java.util.Map"%>
<%@ page import="edu.wustl.catissuecore.domain.processingprocedure.Action"%>
<script type="text/javascript" src="jss/ext-base.js"></script>
<script type="text/javascript" src="jss/ext-all.js"></script>

<head>
	<script>
		function saveSPPEventsForSPP()
		{
			var action=document.forms[0].action;

			var iframeList = document.getElementsByTagName('iframe');
			for(j =0;j<iframeList.length;j++)
			{
				var containerId= iframeList[j].name;
				var oDoc = iframeList[j].contentWindow || iframeList[j].contentDocument;
				if (oDoc.document) {
					oDoc = oDoc.document;
				}
				var search = 'Control';
				var search1 = 'comboControl';
				var inputCollection = oDoc.getElementsByTagName('input');
				for(i=0; i<inputCollection.length ;i++)
				{
					if(inputCollection[i].name.indexOf(search) == 0 || inputCollection[i].name.indexOf(search1) == 0)
					{
						if(action.indexOf('?') == -1)
						{
							action=action+'?'+containerId+'!@!'+inputCollection[i].name+'='+inputCollection[i].value;
						}
						else
						{
							action=action+'&'+containerId+'!@!'+inputCollection[i].name+'='+inputCollection[i].value;
						}
					}
				}
				var inputTags = document.getElementsByTagName('input');

				for(i=0; i<inputTags.length ;i++)
				{
					if(inputTags[i].name.indexOf(search) == 0 || inputTags[i].name.indexOf(search1) == 0)
					{
						if(action.indexOf('?') == -1)
						{
							action=action+'?'+inputTags[i].name+'='+inputTags[i].value;
						}
						else
						{
							action=action+'&'+inputTags[i].name+'='+inputTags[i].value;
						}
					}
				}
			}
			var operationparam = "&operation=<%=request.getAttribute("operation")%>";
			var idParam = "&id=<%=request.getAttribute("id")%>";
			action = action+operationparam+idParam;
			document.forms[0].action=action;
			document.forms[0].submit();
		}
	</script>
</head>

<html:form styleId="defaultValueForm" action="SaveDefaultValueForSPPEvents.do?saveSPPEvents=true">
	<table width="100%" border="0" cellpadding="0" cellspacing="0" class="maintable">
		<tr>
			<td class="tablepadding">
				<table width="100%" border="0" cellpadding="3" cellspacing="0" class="whitetable_bg">
					<tr class="tr_bg_blue1">
						<td align="left" class="tr_bg_blue1">
							<span class="blue_ar_b">&nbsp;Set SPP Default Values</span>
						</td>
					</tr>
					<tr>
						<td align="left" class="bottomtd">
							<%@ include file="/pages/content/common/ActionErrors.jsp" %>
						</td>
					</tr>
					<c:choose>
		<c:when test="${requestScope.isCaCoreGenerated == 'true' || requestScope.isCaCoreGenerated == true}">
		 <tr>
          <td colspan="4" class="showhide1"><Strong><bean:message key="cacore.not.generated"/></Strong></td>
        </tr>
		</c:when>
		<c:otherwise>
					<tr>
						<td>
							<div style="overflow:no; height: 100%;float: none" align="left" id ="sppEvents">
							</div>
						</td>
					</tr>
					<tr>
						<td class="buttonbg">
							<html:submit styleClass="blue_ar_b" value="Save" onclick="saveSPPEventsForSPP();"/>
							<html:hidden styleId="name" property="name"/>
							<html:hidden styleId="barcode" property="barcode"/>
						</td>
					</tr>
		</c:otherwise>
		</c:choose>
				</table>
			</td>
		</tr>
	</table>
</html:form>
<script>

	var globalListSize=0;
	function calculatePageHtForNonIEBrowser(listsize)
	{
		globalListSize = globalListSize +1;
		if(globalListSize == listsize)
		{
			calculatePageHeight();
		}
	}

	function calculatePageHeight()
	{
		var sppEventDiv = document.getElementById('sppEvents');
		var iframeList = document.getElementsByTagName('iframe');
		var allFrameHeights = 0;
		for(j =0;j<iframeList.length;j++)
		{
			var oDoc = iframeList[j].contentWindow || iframeList[j].contentDocument;
			if (oDoc.document)
			{
				oDoc = oDoc.document;
			}
			var inputCollection = oDoc.getElementsByTagName('input');
			var decontrols =0;
			for(i=0; i<inputCollection.length ;i++)
			{
				var search = 'Control';
				if(inputCollection[i].name.indexOf(search) == 0)
				{
					decontrols=  decontrols +1;
				}
			}
			var divHeight = decontrols * 35;
			var frmHeight = (decontrols * 40)+20;
			iframeList[j].style.height= frmHeight+'px';
			allFrameHeights = allFrameHeights+ frmHeight;
		}
		if(allFrameHeights != 0)
		{
			sppEventDiv.style.height=allFrameHeights+"px";
		}
	}
</script>
<%
request.setAttribute("id", request.getAttribute("id"));
request.getSession().setAttribute("OverrideCaption", null);
request.setAttribute("operation", request.getAttribute("operation"));
Collection<Action> actionCollection = (Collection<Action>) request.getAttribute("actionColl");
int listSize = actionCollection.size();
Map<Action, Long> contextRecordIdMap = (Map<Action, Long>) request.getAttribute("contextRecordMap");
int count =1;

String containerIdString="containerIdentifier";
if(request.getAttribute(Globals.ERROR_KEY)!=null)
{
	containerIdString="containerId";
}
for(Action action : actionCollection)
{
	Long formContextId = action.getId();
	Long containerId = action.getContainerId();
	String iframeURL="/catissuecore/LoadDataEntryFormAction.do?dataEntryOperation=edit&showCalculateDefaultValue=false&useApplicationStylesheet=true&showInDiv=false&"+containerIdString+"="+containerId+ "&FormContextIdentifier="+formContextId;
	request.getSession().setAttribute("mandatory_Message", "false");
	if(!contextRecordIdMap.isEmpty() && contextRecordIdMap.get(action) !=null)
	{
		Long recordIdentifier = contextRecordIdMap.get(action);
		iframeURL = iframeURL+"&recordIdentifier=" + recordIdentifier;
	}
%>
	<script>

		var formName<%=count%> = "<%=formContextId%>";
		var sppevents<%=count%> = document.getElementById("sppEvents");
		var formIframe<%=count%> = document.createElement("iframe");
		formIframe<%=count%>.id = formName<%=count%>;
		formIframe<%=count%>.name= formName<%=count%>;
		formIframe<%=count%>.style.border ="0";
		formIframe<%=count%>.style.width ="100%";
		formIframe<%=count%>.scrolling="auto";
		formIframe<%=count%>.setAttribute("src", "<%=iframeURL%>");
		sppevents<%=count%>.appendChild(formIframe<%=count%>);
		if(Ext.isIE)
		{
			<%if(count == listSize)
			{
			%>
				formIframe<%=count%>.onload = function(){
					calculatePageHeight();
				}
			<%
			}
			%>
		}
		else
		{
			formIframe<%=count%>.onload = function(){
				calculatePageHtForNonIEBrowser(<%=listSize%>);
			}
		}
	</script>
<%
count = count +1;
}
%>
