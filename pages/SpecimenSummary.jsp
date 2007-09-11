<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/nlevelcombo.tld" prefix="ncombo" %>

<html>
		<html:form action="/GenericSpecimenSummary.do">
		
		<p>Specimen Requirement
	    <table border="1" cellspecing="0" cellpadding="0">
	      	<tr>
	      		<td class="dataTableHeader" > </td>
	      		<td class="dataTableHeader">Label</td>
	      		<td class="dataTableHeader"> Class</td>
	      		<td class="dataTableHeader"> Type</td>
	      		<td class="dataTableHeader"> Tissue Site</td>
	      		<td class="dataTableHeader"> Tissue Side</td>
	      		<td class="dataTableHeader"> Pathological Status</td>
	      		<td class="dataTableHeader"> Storage</td>
	      		<td class="dataTableHeader"> Qty</td>
				
	      	</tr>
	    
	      <logic:iterate name="viewSpecimenSummaryForm" property="specimenList" id="specimen">
	      	<tr>
	      		<td> <html:radio property="selectedSpecimenId" value="uniqueIdentifier" idName="specimen" onclick="submit()"/> </td>
	      		<td> <bean:write name="specimen" property="specimenLabel" /></td>
	      		<td> <bean:write name="specimen" property="specimenClassName" /></td>
	      		<td> <bean:write name="specimen" property="specimenType" /></td>
	      		<td> <bean:write name="specimen" property="tissueSite" /></td>
	      		<td> <bean:write name="specimen" property="tissueSide" /></td>
	      		<td> <bean:write name="specimen" property="pathologyStatus" /></td>
	      		<td> <bean:write name="specimen" property="storage" /></td>
	      		<td> <bean:write name="specimen" property="quantity" /></td>
				
	      	</tr>
	      </logic:iterate>	
	    </table>
	    <logic:notEmpty name="viewSpecimenSummaryForm" property="eventId">
	    	<html:hidden property="eventId"  />
	    </logic:notEmpty>
		<logic:notEmpty name="viewSpecimenSummaryForm" property="aliquoteList" >
			<p>Aliquot details
			<table border="1" cellspecing="0" cellpadding="0">
	      		<tr>	
	      		<td class="dataTableHeader">Parent</td>
	      		<td class="dataTableHeader">Label</td>
	      		<td class="dataTableHeader"> Storage</td>
	      		<td class="dataTableHeader"> Qty</td>
				</tr>
		      <logic:iterate name="viewSpecimenSummaryForm" property="aliquoteList" id="aliquot">
		      	<tr>
		      		<td> <bean:write name="aliquot" property="parentName" /></td>		      		
		      		<td> <bean:write name="aliquot" property="specimenLabel" /></td>
		      		<td> <bean:write name="aliquot" property="storage" /></td>
		      		<td> <bean:write name="aliquot" property="quantity" /></td>
		      	</tr>
		      </logic:iterate>	
		    </table>
		</logic:notEmpty>		

		<logic:notEmpty name="viewSpecimenSummaryForm" property="derivedList" >		
			<p>Derived details
			<table border="1" cellspecing="0" cellpadding="0">
				<tr>
				<td class="dataTableHeader">Parent</td>
	      		<td class="dataTableHeader">Label</td>
	      		<td class="dataTableHeader"> Class</td>
	      		<td class="dataTableHeader"> Type</td>
	      		<td class="dataTableHeader"> Qty</td>
	      		<td class="dataTableHeader"> Storage</td>
	      		<td class="dataTableHeader"> concentration</td>

				</tr>
		      <logic:iterate name="viewSpecimenSummaryForm" property="derivedList" id="derived">
		      	<tr>
		      		<td> <bean:write name="derived" property="parentName" /></td>
		      		<td> <bean:write name="derived" property="specimenLabel" /></td>
		      		<td> <bean:write name="derived" property="specimenClassName" /></td>
		      		<td> <bean:write name="derived" property="specimenType" /></td>
		      		<td> <bean:write name="derived" property="quantity" /></td>
		      		<td> <bean:write name="derived" property="storage" /></td>
		      		<td> <bean:write name="derived" property="concentration" /></td>
		      	</tr>
		      </logic:iterate>	
		    </table>
		</logic:notEmpty>
		</html:form>		
</html>