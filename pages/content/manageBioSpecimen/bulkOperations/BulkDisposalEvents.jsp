<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/nlevelcombo.tld" prefix="ncombo" %>
<%@ page import="edu.wustl.catissuecore.util.global.Utility"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ include file="/pages/content/common/AutocompleterCommon.jsp" %> 
<head>
	<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
</head>

<script language="JavaScript" type="text/javascript">
	
		function ApplyToAll()
		{
			if(document.getElementById("chk").checked)
			{
				var fields = document.getElementsByTagName("select");
				var i =0;
				var text="";
				var valueToSet = "";
				var isFirstField = true;
				for (i=0; i<fields.length;i++)
				{
					text = fields[i].name;
					if(text.indexOf("_STATUS")>=0)
					{
						if(isFirstField)
						{
							valueToSet = fields[i].value;
							isFirstField = false;
						}
						fields[i].value = valueToSet;
					}
				}
			}
		}
	
</script>
<html:form action="BulkTransferEventsSubmit.do" >
	<jsp:include page="/pages/content/manageBioSpecimen/bulkOperations/BulkEventsCommonAttributes.jsp" />
	 
	 
	  <tr>
        <td colspan="3" class="showhide"><table width="100%" border="0" cellpadding="2" cellspacing="0">
            <tr>
              <td width="1%" align="center" class="black_ar">&nbsp;</td>
              <td width="15%" align="left" valign="top" class="black_ar_t"><bean:message key="disposaleventparameters.reason" />  </td>
              <td align="left" width="84%"><html:textarea styleClass="black_ar" cols="70" rows="3"  styleId="comments" property="fieldValue(ID_ALL_REASON)" /></td>
            </tr>
            <tr>
              <td align="center" class="black_ar">&nbsp;</td>
              <td align="left" valign="top" class="black_ar"><bean:message key="specimenLabels" /></td>
              <td align="left" class="black_ar"><%
							String specimenList = "";
							String specimenLabelField = "";
							String commaString = "";
							
						%>
						<logic:iterate id="specimenId" name="<%=Constants.SPECIMEN_ID_LIST%>" scope="request" indexId="id">
							<%
								specimenList = "specimenId("+specimenId+")";
								specimenLabelField = "fieldValue(ID_"+specimenId+"_LABEL)";
								if( id != 0 )
								{
									commaString = ", ";
								}
							%>
							<html:hidden property="<%=specimenList%>" />
							<%=commaString%><label for="type">
								<bean:write name="bulkEventOperationsForm" property="<%=specimenLabelField%>" />
							</label>
						</logic:iterate></td>
            </tr>
            <tr>
              <td align="center" class="black_ar">&nbsp;</td>
              <td align="left" class="black_ar"><LABEL for="type">Status</LABEL></td>
              <td align="left">	
			   <html:select styleId="comments" property="fieldValue(ID_ALL_STATUS)"    styleClass="formFieldSized4" size="1"> <html:options      
			    name="<%=Constants.ACTIVITYSTATUSLIST%>" />
			   </html:select></td>
            </tr>
       </table></td>
      </tr>
        
      <tr>
        <td colspan="2" class="buttonbg"><html:submit styleClass="blue_ar_b"/>&nbsp; | &nbsp;<html:link page="/ManageAdministrativeData.do" styleClass="cancellink">
		<bean:message key="buttons.cancel" /> </html:link></td>
      </tr>
    
	</table></td>
  </tr>
</table>
</html:form>