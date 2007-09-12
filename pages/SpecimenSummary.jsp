<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/nlevelcombo.tld" prefix="ncombo" %>
<link rel="stylesheet" type="text/css" href="css/styleSheet.css" />
<html>
		<html:form action="/SubmitSpecimenCollectionProtocol.do">		
		<table summary="" cellpadding="0" cellspacing="0" border="0">
				<tr>
					<td class="dataTablePrimaryLabel" height="20">
						Specimen Requirement
					</td>
				</tr>
	
		<tr>	
		   <td>
			<table summary="" cellpadding="3"
							cellspacing="0" border="0" class="dataTable" width="100%">
				<tr>
					<th class="formSerialNumberLabelForTable" scope="col" > &nbsp </th>
					<th class="formSerialNumberLabelForTable" scope="col">Label</th>
					<th class="formSerialNumberLabelForTable" scope="col"> Class</th>
					<th class="formSerialNumberLabelForTable" scope="col"> Type</th>
					<th class="formSerialNumberLabelForTable" scope="col"> Tissue Site</th>
					<th class="formSerialNumberLabelForTable" scope="col"> Tissue Side</th>
					<th class="formSerialNumberLabelForTable" scope="col"> Pathological Status</th>
					<th class="formSerialNumberLabelForTable" scope="col"> Storage</th>
					<th class="formSerialNumberLabelForTable" scope="col"> Qty</th>
					
				</tr>
			
			  <logic:iterate name="viewSpecimenSummaryForm" property="specimenList" id="specimen">
				<tr>
					<td> <html:radio property="selectedSpecimenId" value="uniqueIdentifier" idName="specimen" 
					onclick=" form.action='GenericSpecimenSummary.do'; submit()"/> </td>
					<td class="dataCellText" > <bean:write name="specimen" property="displayName" /></td>
					<td class="dataCellText"> <bean:write name="specimen" property="className" /></td>
					<td class="dataCellText"> <bean:write name="specimen" property="type" /></td>
					<td class="dataCellText"> <bean:write name="specimen" property="tissueSite" /></td>
					<td class="dataCellText"> <bean:write name="specimen" property="tissueSide" /></td>
					<td class="dataCellText"> <bean:write name="specimen" property="pathologicalStatus" /></td>
					<td class="dataCellText"> <bean:write name="specimen" property="storageContainerForSpecimen" /></td>
					<td class="dataCellText"> <bean:write name="specimen" property="quantity" /></td>
					
				</tr>
			  </logic:iterate>	
			</table>
			</td>
		</tr>
		<tr>
		
			<logic:notEmpty name="viewSpecimenSummaryForm" property="eventId">
				<html:hidden property="eventId"  />
			</logic:notEmpty>
			
			<td class="dataTablePrimaryLabel" height="20">

			<logic:notEmpty name="viewSpecimenSummaryForm" property="aliquoteList" >
				<p>Aliquot details
		</td>
		</tr>
		<tr>
		<td>
			<table summary="" cellpadding="3"
							cellspacing="0" border="0" class="dataTable" width="100%">
				<tr>	
					<th class="formSerialNumberLabelForTable" scope="col">Parent</th>
					<th class="formSerialNumberLabelForTable" scope="col">Label</th>
					<th class="formSerialNumberLabelForTable" scope="col"> Storage</th>
					<th class="formSerialNumberLabelForTable" scope="col"> Qty</th>
					</tr>
				  <logic:iterate name="viewSpecimenSummaryForm" property="aliquoteList" id="aliquot">
					<tr>
						<td > <bean:write name="aliquot" property="parentName" /></td>		      		
						<td > <bean:write name="aliquot" property="displayName" /></td>
						<td > <bean:write name="aliquot" property="storageContainerForSpecimen" /></td>
						<td > <bean:write name="aliquot" property="quantity" /></td>
					</tr>
				  </logic:iterate>	
				</table>
			</logic:notEmpty>		
		 </td>
		</tr>
		<tr>
		 
		<logic:notEmpty name="viewSpecimenSummaryForm" property="derivedList" >		
		<td class="dataTablePrimaryLabel" height="20">
			<p>Derived details
		 </td>
		 </tr>
		 <td>
				    <table summary="" cellpadding="3"
						cellspacing="0" border="0" class="dataTable" width="100%">
			<tr>
				<th class="formSerialNumberLabelForTable" scope="col">Parent</th>
	      		<th class="formSerialNumberLabelForTable" scope="col">Label</th>
	      		<th class="formSerialNumberLabelForTable" scope="col"> Class</th>
	      		<th class="formSerialNumberLabelForTable" scope="col"> Type</th>
	      		<th class="formSerialNumberLabelForTable" scope="col"> Qty</th>
	      		<th class="formSerialNumberLabelForTable" scope="col"> Storage</th>
	      		<th class="formSerialNumberLabelForTable" scope="col"> concentration</th>

				</tr>
		      <logic:iterate name="viewSpecimenSummaryForm" property="derivedList" id="derived">
		      	<tr>
		      		<td > <bean:write name="derived" property="parentName" /></td>
		      		<td > <bean:write name="derived" property="displayName" /></td>
		      		<td > <bean:write name="derived" property="className" /></td>
		      		<td > <bean:write name="derived" property="type" /></td>
		      		<td > <bean:write name="derived" property="quantity" /></td>
		      		<td > <bean:write name="derived" property="storageContainerForSpecimen" /></td>
		      		<td > <bean:write name="derived" property="concentration" /></td>
		      	</tr>
		      </logic:iterate>	
		    </table>
          </td>
		</logic:notEmpty>
		</tr>
		<tr>
		<logic:notEmpty name="viewSpecimenSummaryForm" property="eventId">		
		   <td>
			<html:submit value="Collection Protocol"/>
			</td>
		</logic:notEmpty>
		
		<logic:empty name="viewSpecimenSummaryForm" property="eventId">		
		    <td>
			<html:submit value="Save Specimens"/>
			</td>
		</logic:empty>
		</tr>
		</table>
		</html:form>		
</html>