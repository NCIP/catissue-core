/**
 * This class is a Tag Handler class doe SpecimenDetails tag.
 */
package edu.wustl.catissuecore.tag;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

import edu.wustl.catissuecore.actionForm.ViewSpecimenSummaryForm;
import edu.wustl.catissuecore.bean.GenericSpecimen;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.util.global.ApplicationProperties;

/**
 * This class is a Tag Handler class doe SpecimenDetails tag.
 * @author mandar_deshmukh
 *
 */
public class SpecimenDetailsTag extends TagSupport
{

	// data type list used to display different types of data.
	private final String PARENT = "Parent"; 
	private final String dataListTypes[] = {PARENT,"Aliquot","Derived"};
	
	// ---------- attributes section start ------------
	private static final long serialVersionUID = 1234567890L;
	private List columnHeaderList = null;
	private String formName = "";
	private List dataList = null;
	private boolean showParentId = false;
	private String elementPrefixPart1="";
	private String displayOnly ="";
	private List displayStatusList = null;
	
	ViewSpecimenSummaryForm specimenSummaryForm = null;
	private String columnHeaderListName = "";
	private String dataListName = "";
	private String dataListType = "";
	private String displayStatusListName = "";
	
	// ---------- attributes section end ------------

	/**
     * A call back function, which gets executed by JSP runtime when opening tag for this
     * custom tag is encountered. 
     */
	public int doStartTag() throws JspException
	{
		try
		{
			JspWriter out = pageContext.getOut();

			out.print("");
			if(validate() )
			{
				initialiseElements();
				
				if(displayOnly.equalsIgnoreCase(Constants.TRUE))
				{
					out.print(generateHeaderForDisplay());
					out.print(generateDataForDisplay());
				}
				else
				{
					out.print(generateHeaderRowOutput());
					out.print(generateDataRowsOutput());
				}
				

			}	
				
		}
		catch(IOException ioe) 
		{
				throw new JspTagException("Error:IOException while writing to the user");
		}

		return SKIP_BODY;
	}

	/**
     * A call back function
     */
	public int doEndTag() throws JspException
	{
		columnHeaderList = null;
		formName = "";
		dataList = null;
		showParentId = false;
		elementPrefixPart1="";
		displayOnly ="";
		displayStatusList = null;
		
		specimenSummaryForm = null;
		columnHeaderListName = "";
		dataListName = "";
		dataListType = "";
		displayStatusListName = "";

		return EVAL_PAGE;
	}
	
	/* method to validate the given values for the attributes.
	 * Returns true if all required attributes are in proper valid format. Otherwise returns false. 
	 */
	private boolean validate()
	{
		boolean result = true;
		
	//	if(result == true) return true;
		
		ServletRequest request = pageContext.getRequest(); 
		ActionErrors errors = (ActionErrors)request.getAttribute(Globals.ERROR_KEY);
		
		if(errors == null)
		{
			errors = new ActionErrors();
		}
		
		if(columnHeaderListName == null || columnHeaderListName.trim().length()<1)
		{
			errors.add(ActionErrors.GLOBAL_ERROR,new ActionError("Column Header List Name is null or empty"));
			System.out.println("Column Header List Name is null or empty");
			result =  false;
		}
		if(displayOnly == null || displayOnly.trim().length()<1)
		{
			errors.add(ActionErrors.GLOBAL_ERROR,new ActionError("DisplayOnly parameter is null or empty"));
			System.out.println("DisplayOnly parameter is null or empty");
			result =  false;
		}
		else if(!displayOnly.equalsIgnoreCase(Constants.TRUE))
		{
			if(formName == null || formName.trim().length()<1)
			{
				errors.add(ActionErrors.GLOBAL_ERROR,new ActionError("Form name is null or empty"));
				System.out.println("Form name is null or empty");
				result =  false;
			}
		}
		if(dataListName == null || dataListName.trim().length()<1)
		{
			errors.add(ActionErrors.GLOBAL_ERROR,new ActionError("Data List Name is null or empty"));
			System.out.println("Data List Name is null or empty");
			result =  false;
		}
		if(dataListType == null || dataListType.trim().length()<1)
		{
			errors.add(ActionErrors.GLOBAL_ERROR,new ActionError("Data List Type is null or empty"));
			System.out.println("Data List Type is null or empty");
			result =  false;
		}
		else if(!isListDataTypeOK())
		{
			errors.add(ActionErrors.GLOBAL_ERROR,new ActionError("Data List Type is invalid"));
			System.out.println("Data List Type is invalid");
			result =  false;
		}
		if(displayStatusListName == null || displayStatusListName.trim().length()<1)
		{
			errors.add(ActionErrors.GLOBAL_ERROR,new ActionError("Display Status List Name is null or empty"));
			System.out.println("Display Status List Name is null or empty");
			result =  false;
		}
		
		request.setAttribute(Globals.ERROR_KEY,errors);
		return result;
	}
	
	//method to generate the column header row
	private String generateHeaderRowOutput() throws IOException
	{
		StringBuffer sb = new StringBuffer();
		sb.append("<tr>");
		if(specimenSummaryForm != null)
		{
			if(((String)columnHeaderList.get(0)).trim().length() == 0)
				sb.append("<th class=\"formSerialNumberLabelForTable\" scope=\"col\" > &nbsp </th>");
			else
				sb.append("<th class=\"formSerialNumberLabelForTable\" scope=\"col\">"+ApplicationProperties.getValue((String)columnHeaderList.get(0))+"</th>");
			if(specimenSummaryForm.getShowLabel())
				sb.append("<th class=\"formSerialNumberLabelForTable\" scope=\"col\">"+ApplicationProperties.getValue((String)columnHeaderList.get(1))+"</th>");
			if(specimenSummaryForm.getShowbarCode())
				sb.append("<th class=\"formSerialNumberLabelForTable\" scope=\"col\">"+ApplicationProperties.getValue((String)columnHeaderList.get(2))+"</th>");

			sb.append("<th class=\"formSerialNumberLabelForTable\" scope=\"col\">"+ApplicationProperties.getValue((String)columnHeaderList.get(3))+"</th>");
			sb.append("<th class=\"formSerialNumberLabelForTable\" scope=\"col\">"+ApplicationProperties.getValue((String)columnHeaderList.get(4))+"</th>");
			sb.append("<th class=\"formSerialNumberLabelForTable\" scope=\"col\">"+ApplicationProperties.getValue((String)columnHeaderList.get(5))+"</th>");

			sb.append("<th class=\"formSerialNumberLabelForTable\" scope=\"col\">"+ApplicationProperties.getValue((String)columnHeaderList.get(6))+"");
			sb.append("<input id=\"chkSpecimen\" type=\"checkbox\" onClick=\"ApplyToAll(this,'specimen')\"/> Apply First to All");
			sb.append("</th>");

			if(specimenSummaryForm.getShowCheckBoxes())
				sb.append("<th class=\"formSerialNumberLabelForTable\" scope=\"col\">"+ApplicationProperties.getValue((String)columnHeaderList.get(7))+"</th>");
		}
		else
		{
			sb.append("<b>ViewSpecimenSummaryForm instance is null</b>");
		}
		
		sb.append("</tr>");
		
		String output =sb.toString();		
		System.out.println(output);
		return output;
	}
	

	//method to generate the data rows. Have to write logic for generating the output for these around 160+ lines 
	private String generateDataRowsOutput() throws IOException
	{
		StringBuffer sb = new StringBuffer();
		sb.append("");
		if(specimenSummaryForm != null)
		{
			 
			 List specimenList = getDataList(specimenSummaryForm);
			 
			  String containerId;
			  String selectedContainerName ;
			  String positionDimensionOne;
			  String positionDimensionTwo;
			  String specimenClassName;
			  String cpId;
			  String functionCall;

			 for(int counter=0; counter<specimenList.size(); counter++)
			 {
				 GenericSpecimen specimen = (GenericSpecimen)specimenList.get(counter);
				 String elementNamePrefix = elementPrefixPart1+counter+"].";
				 sb.append("<tr>");
				 sb.append("<input type=\"hidden\" name=\""+elementNamePrefix+"containerId\" value=\""+getFormattedValue(specimen.getContainerId())+"\">");

				 if(showParentId == false)
				 {
					 if(specimenSummaryForm.getSelectedSpecimenId().equalsIgnoreCase(specimen.getUniqueIdentifier()))
						 sb.append("<td class=\"dataCellText\"> <input type=\"radio\" name=\"selectedSpecimenId\" value=\""+getFormattedValue(specimen.getUniqueIdentifier())+"\" checked=\"checked\" onclick=\" form.action=\'GenericSpecimenSummary.do\'; submit()\">");	 
					 else
						 sb.append("<td class=\"dataCellText\"> <input type=\"radio\" name=\"selectedSpecimenId\" value=\""+getFormattedValue(specimen.getUniqueIdentifier())+"\" onclick=\" form.action=\'GenericSpecimenSummary.do\'; submit()\">");	 
				 }
				 else
				 {
					if(specimenSummaryForm.getShowLabel() == true)
					{
						sb.append("<td class=\"dataCellText\" >");
						sb.append("<input type=\"hidden\" name=\""+elementNamePrefix+"parentName\" value=\""+getFormattedValue(specimen.getParentName())+"\">");
						sb.append(getFormattedValue(specimen.getParentName(),1));
					}
				 }

				 sb.append("<input type=\"hidden\" name=\""+elementNamePrefix+"uniqueIdentifier\" value=\""+getFormattedValue(specimen.getUniqueIdentifier())+"\"></td>");
//				 <!--Editable Row start -->

				 if(specimen.getReadOnly() == false)
				 {
					 sb.append("<input type=\"hidden\" name=\""+elementNamePrefix+"readOnly\" value=\"false\">");
					 
					 if(specimenSummaryForm.getShowLabel() == true)
					 {
						 sb.append("<td class=\"dataCellText\" >"); 
						 sb.append("<input type=\"text\" name=\""+elementNamePrefix+"displayName\" value=\""+getFormattedValue(specimen.getDisplayName())+"\" class=\"formFieldSized10\">");
						 sb.append("</td>");
					 }
					 
					 if(specimenSummaryForm.getShowbarCode() == true)
					 {
						 sb.append("<td class=\"dataCellText\" >"); 
						 sb.append("<input type=\"text\" name=\""+elementNamePrefix+"barCode\" value=\""+getFormattedValue(specimen.getBarCode())+"\" class=\"formFieldSized10\">");
						 sb.append("</td>");
					 }
					 sb.append("<td class=\"dataCellText\" >"); 
					 sb.append(getFormattedValue(specimen.getType(),1));
					 sb.append("<input type=\"hidden\" name=\""+elementNamePrefix+"type\" value=\""+getFormattedValue(specimen.getType())+"\">");
					 sb.append("</td>");
			
					 sb.append("<td class=\"dataCellText\" >"); 
					 sb.append("<input type=\"text\" name=\""+elementNamePrefix+"quantity\" value=\"" +getFormattedValue(specimen.getQuantity())+"\" class=\"formFieldSized3\">"); 
					 sb.append("</td>");

					 sb.append("<td class=\"dataCellText\" >"); 
					 sb.append("<input type=\"text\" name=\""+elementNamePrefix+"concentration\" value=\"" +getFormattedValue(specimen.getConcentration())+"\" class=\"formFieldSized3\">"); 
					 sb.append("</td>");

					 if(specimen.getStorageContainerForSpecimen()!= null && specimen.getStorageContainerForSpecimen().equalsIgnoreCase("Virtual"))
					 {
						 sb.append("<td class=\"dataCellText\" >");
						 sb.append(getFormattedValue(specimen.getStorageContainerForSpecimen(),1));
						 sb.append("<input type=\"hidden\" name=\""+elementNamePrefix+"storageContainerForSpecimen\" value=\""+getFormattedValue(specimen.getStorageContainerForSpecimen())+"\">");
						 
					 }	// storageContainerForSpecimen virtual
					 else
					 {

						 sb.append("<td class=\"dataCellText\" >");
						 sb.append(getFormattedValue(specimen.getStorageContainerForSpecimen(),1));

						 String specimenId = getFormattedValue(specimen.getUniqueIdentifier());
						 String specimenClass = getFormattedValue(specimen.getClassName());
						 Long collectionProtocolId = specimen.getCollectionProtocolId();
						 
			 			  containerId = "containerId_"+specimenId;
			 			  selectedContainerName = "selectedContainerName_"+specimenId;
			 			  positionDimensionOne = "positionDimensionOne_"+specimenId;
			 			  positionDimensionTwo = "positionDimensionTwo_"+specimenId;
			 			  specimenClassName = (String)specimenClass;
			 			  cpId = getFormattedValue(collectionProtocolId);
			 			  String classNameStyleId = "className_"+specimenId;
			 			  String cpStyleId = "cp_"+specimenId;
			 			  functionCall="showMap('" + selectedContainerName + "','"+
			 											positionDimensionOne +"','"
			 											+ positionDimensionTwo +"','"
			 											+containerId +"','"+
			 											specimenClassName +"','"+
			 											cpId +"')" ;
						 
			 			  if(specimenSummaryForm.getShowParentStorage() == true)
			 			  {
			 				 sb.append("<table style=\"font-size:1em\" size=\"100%\">");
			 				 sb.append("<tr>");
			 				 sb.append("<td>");
		 				 	 sb.append("<input type=\"text\" name=\""+elementNamePrefix+"selectedContainerName\" value=\"" + getFormattedValue(specimen.getSelectedContainerName())+"\" class=\"formFieldSized7\" id=\""+selectedContainerName+"\" >");
				 			 sb.append("</td>");
					 		 sb.append("<td>");
				 			 sb.append("<input type=\"text\" name=\""+elementNamePrefix+"positionDimensionOne\" value=\"" + getFormattedValue(specimen.getPositionDimensionOne())+"\" class=\"formFieldSized3\" id=\""+positionDimensionOne+"\" >");
				 			 sb.append("</td>");
					 		 sb.append("<td>");
				 			 sb.append("<input type=\"text\" name=\""+elementNamePrefix+"positionDimensionTwo\" value=\"" + getFormattedValue(specimen.getPositionDimensionTwo())+"\" class=\"formFieldSized3\" id=\""+positionDimensionTwo+"\" >");
 	 						 sb.append("</td>");
					 		 sb.append("<td>");
					 		 sb.append("<a href=\"#\" onclick=\""+functionCall+"\">");
					 		 sb.append("<img src=\"images\\Tree.gif\" border=\"0\" width=\"13\" height=\"15\" title=\'View storage locations\'>");
					 		 sb.append("</a>");
							 sb.append("<input type=\"hidden\" name=\""+elementNamePrefix+"containerId\" value=\""+getFormattedValue(specimen.getContainerId())+"\" id=\""+containerId+"\">");
							 sb.append("</td>");
							 sb.append("</tr>");										
							 sb.append("</table>");
			 			  }	// ShowParentStorage() == true
			 			  else
			 			  {
								sb.append("<input type=\"hidden\" name=\""+elementNamePrefix+"selectedContainerName\" value=\""+getFormattedValue(specimen.getSelectedContainerName())+"\">");
								sb.append("<input type=\"hidden\" name=\""+elementNamePrefix+"positionDimensionOne\" value=\""+getFormattedValue(specimen.getPositionDimensionOne())+"\">");
								sb.append("<input type=\"hidden\" name=\""+elementNamePrefix+"positionDimensionTwo\" value=\""+getFormattedValue(specimen.getPositionDimensionTwo())+"\">");
				 				sb.append("<span>");
				 				sb.append(getFormattedValue(specimen.getSelectedContainerName(),1));
			 					sb.append("<B>:</B>");
			 					sb.append(getFormattedValue(specimen.getPositionDimensionOne(),1)+",");
			 					sb.append(getFormattedValue(specimen.getPositionDimensionTwo(),1));
			 					sb.append("</span>");
			 			  }// ShowParentStorage() == false
			 			 sb.append("</td>");
						 sb.append("<input type=\"hidden\" name=\""+elementNamePrefix+"className\" value=\""+getFormattedValue(specimen.getClassName())+"\" id=\""+classNameStyleId+"\">");
						 sb.append("<input type=\"hidden\" name=\""+elementNamePrefix+"collectionProtocolId\" value=\""+getFormattedValue(specimen.getCollectionProtocolId())+"\" id=\""+cpStyleId+"\">");
					 } // storageContainerForSpecimen not virtual

					 if(specimenSummaryForm.getShowCheckBoxes() == true)
					 {
						 sb.append("<td class=\"dataCellText\" >");
						 if(specimen.getCheckedSpecimen() == true)
							 sb.append("<input type=\"checkbox\" name=\""+elementNamePrefix+"checkedSpecimen\" value=\"on\" checked=\"checked\">");
						 else
							 sb.append("<input type=\"checkbox\" name=\""+elementNamePrefix+"checkedSpecimen\" value=\"on\">");					
			 			 sb.append("</td>");
					 }
				 } // specimen.getReadOnly() == false		 <!--/Editable Row End -->
				 else	//				 <!---Readonly Row Start -->
				 {
					 sb.append("<input type=\"hidden\" name=\""+elementNamePrefix+"readOnly\" value=\""+getFormattedValue(specimen.getReadOnly())+"\">");
					 sb.append("<td class=\"dataCellText\" >");
					 sb.append(getFormattedValue(specimen.getDisplayName(),1));	
				 	 sb.append("<input type=\"hidden\" name=\""+elementNamePrefix+"displayName\" value=\""+getFormattedValue(specimen.getDisplayName())+"\">");	
					 sb.append("</td>");

					 sb.append("<td class=\"dataCellText\" >");
					 sb.append(getFormattedValue(specimen.getBarCode(),1));	
				 	 sb.append("<input type=\"hidden\" name=\""+elementNamePrefix+"barCode\" value=\""+getFormattedValue(specimen.getBarCode())+"\">");	
					 sb.append("</td>");

					 sb.append("<td class=\"dataCellText\" >");
					 sb.append(getFormattedValue(specimen.getType(),1));	
				 	 sb.append("<input type=\"hidden\" name=\""+elementNamePrefix+"type\" value=\""+getFormattedValue(specimen.getType())+"\">");	
					 sb.append("</td>");

					 sb.append("<td class=\"dataCellText\" >");
					 sb.append(getFormattedValue(specimen.getQuantity(),1));	
				 	 sb.append("<input type=\"hidden\" name=\""+elementNamePrefix+"quantity\" value=\""+getFormattedValue(specimen.getQuantity())+"\">");	
					 sb.append("</td>");

					 sb.append("<td class=\"dataCellText\" >");
					 sb.append(getFormattedValue(specimen.getConcentration(),1));	
				 	 sb.append("<input type=\"hidden\" name=\""+elementNamePrefix+"concentration\" value=\""+getFormattedValue(specimen.getConcentration())+"\">");	
					 sb.append("</td>");

					 if(specimen.getStorageContainerForSpecimen().equalsIgnoreCase("Virtual"))
					 {
						 sb.append("<td class=\"dataCellText\" >");
						 sb.append(getFormattedValue(specimen.getStorageContainerForSpecimen(),1));	
					 	 sb.append("<input type=\"hidden\" name=\""+elementNamePrefix+"storageContainerForSpecimen\" value=\""+getFormattedValue(specimen.getStorageContainerForSpecimen())+"\">");	
						 sb.append("</td>");
					 }	//	storageContainerForSpecimen == virtual
					 else
					 {
						 sb.append("<td class=\"dataCellText\" >");
					 	 sb.append("<input type=\"hidden\" name=\""+elementNamePrefix+"selectedContainerName\" value=\""+getFormattedValue(specimen.getSelectedContainerName())+"\">");	
					 	 sb.append("<input type=\"hidden\" name=\""+elementNamePrefix+"positionDimensionOne\" value=\""+getFormattedValue(specimen.getPositionDimensionOne())+"\">");	
					 	 sb.append("<input type=\"hidden\" name=\""+elementNamePrefix+"positionDimensionTwo\" value=\""+getFormattedValue(specimen.getPositionDimensionTwo())+"\">");	
					 	 sb.append("<span>");
					 	 sb.append(getFormattedValue(specimen.getSelectedContainerName(),1));	
					 	 sb.append("<B>:</B>");
					 	 sb.append(getFormattedValue(specimen.getPositionDimensionOne(),1)+",");
					 	 sb.append(getFormattedValue(specimen.getPositionDimensionTwo(),1));
					 	 sb.append("</span>");
					 } //	storageContainerForSpecimen != virtual

					 if(specimenSummaryForm.getShowCheckBoxes() == true)
					 {
						 sb.append("<td class=\"dataCellText\" >");
						 sb.append("<input type=\"checkbox\" name=\""+elementNamePrefix+"checkedSpecimen\" value=\"true\" disabled=\"true\">");
			 		 	 sb.append("<input type=\"hidden\" name=\""+elementNamePrefix+"checkedSpecimen\" value=\""+getFormattedValue(specimen.getCheckedSpecimen())+"\">");
			 		 	 sb.append("</td>");
					 }
					 sb.append("</td>");
				 }// specimen.getReadOnly() == true   //			 <!--/Readonly Row end -->
				 sb.append("</tr>");
			 } // outer most loop for specimenList
		}
		else
		{
			sb.append("<b>(DataRows)ViewSpecimenSummaryForm instance is null</b>");
		}
		sb.append("");


		String output =sb.toString();		
		System.out.println(output);
		return output;
	}

	
	public static void main(String [] args) throws Exception
	{
		List colHeaderList = new ArrayList();
		colHeaderList.add("");	colHeaderList.add("specimen.label");	colHeaderList.add("specimen.barcode");
		colHeaderList.add("specimen.subType");	colHeaderList.add("anticipatorySpecimen.Quantity");	colHeaderList.add("anticipatorySpecimen.Concentration");
		colHeaderList.add("anticipatorySpecimen.Location");	colHeaderList.add("anticipatorySpecimen.Collected");

		SpecimenDetailsTag obj = new SpecimenDetailsTag();
		obj.setColumnHeaderList(colHeaderList);
		System.out.println(obj.generateHeaderRowOutput());
	}


	// ------------ Get-Set Method section starts ------------------
	public List getColumnHeaderList() {
		return columnHeaderList;
	}

	public void setColumnHeaderList(List columnHeaderList) {
		this.columnHeaderList = columnHeaderList;
	}

	public List getDataList() {
		return dataList;
	}

	public void setDataList(List dataList) {
		this.dataList = dataList;
	}

	public String getFormName() {
		return formName;
	}

	public void setFormName(String formName) {
		this.formName = formName;
	}

	public String getColumnHeaderListName() {
		return columnHeaderListName;
	}

	public void setColumnHeaderListName(String columnHeaderListName) {
		this.columnHeaderListName = columnHeaderListName;
	}

	public String getDataListName() {
		return dataListName;
	}

	public void setDataListName(String dataListName) {
		this.dataListName = dataListName;
	}

	public String getDataListType() {
		return dataListType;
	}

	public void setDataListType(String dataListType) {
		this.dataListType = dataListType;
	}

	public String getDisplayOnly() {
		return displayOnly;
	}

	public void setDisplayOnly(String displayOnly) {
		this.displayOnly = displayOnly;
	}

	public String getDisplayStatusListName() {
		return displayStatusListName;
	}

	public void setDisplayStatusListName(String displayStatusListName) {
		this.displayStatusListName = displayStatusListName;
	}

	private void initialiseElements()
	{
		ServletRequest request = pageContext.getRequest();
		
		specimenSummaryForm = (ViewSpecimenSummaryForm)request.getAttribute(formName);
		columnHeaderList = (List)request.getAttribute(columnHeaderListName);
		dataList = (List)request.getAttribute(dataListName);
		displayStatusList = (List)request.getAttribute(displayStatusListName);
		
		if(columnHeaderList == null || columnHeaderList.isEmpty())
		{
			List colHeaderList = new ArrayList();
			if(dataListType.equalsIgnoreCase(PARENT))
				colHeaderList.add("");
			else
			{
				colHeaderList.add("anticipatorySpecimen.Parent");
//				showParentId = true;
			}
			colHeaderList.add("specimen.label");	colHeaderList.add("specimen.barcode");
			colHeaderList.add("specimen.subType");	colHeaderList.add("anticipatorySpecimen.Quantity");
			colHeaderList.add("anticipatorySpecimen.Concentration");
			colHeaderList.add("anticipatorySpecimen.Location");	
			if(!displayOnly.equalsIgnoreCase(Constants.TRUE)){
				colHeaderList.add("anticipatorySpecimen.Collected");}
			columnHeaderList = colHeaderList; 
		}
		if(!dataListType.equalsIgnoreCase(PARENT))
		{
			showParentId = true;
		}
		
		if(displayStatusList == null || displayStatusList.isEmpty())
		{
			List dispStatusList = new ArrayList();
			dispStatusList.add(Constants.TRUE);	dispStatusList.add(Constants.TRUE);	dispStatusList.add(Constants.TRUE);
			dispStatusList.add(Constants.TRUE);	dispStatusList.add(Constants.TRUE);	dispStatusList.add(Constants.TRUE);	
			dispStatusList.add(Constants.TRUE);
			if(!displayOnly.equalsIgnoreCase(Constants.TRUE)){
				dispStatusList.add(Constants.TRUE);}
			else
				dispStatusList.add(Constants.FALSE);
			
			displayStatusList = dispStatusList;
		}


	}
	
	private boolean isListDataTypeOK()
	{
		boolean result = false;
		for(int cnt=0;cnt<dataListTypes.length; cnt++)
		{
			if(dataListType.equalsIgnoreCase(dataListTypes[cnt]))
			{
				if(PARENT.equalsIgnoreCase(dataListTypes[cnt]))
					elementPrefixPart1 = "specimen[";
				else
					elementPrefixPart1 = dataListTypes[cnt].toLowerCase().trim()+"[";
				result = true;
			}
				
		}
		return result;
	}
	
	private List getDataList(ViewSpecimenSummaryForm form)
	{
		List l = null;
		//setting the data list to be used to display the specimens.
		if(dataList == null || dataList.isEmpty())
		{
			if(dataListType.equalsIgnoreCase(PARENT))
				l = form.getSpecimenList();
			else if(dataListType.equalsIgnoreCase(dataListTypes[1]))
				l = form.getAliquotList();
			else if(dataListType.equalsIgnoreCase(dataListTypes[2]))
				l = form.getDerivedList();
		}
		else	// get the list from ViewSpecimenSummaryForm based on list type
		{
			l = dataList;
		}
		return l;
	}
	
	private String getFormattedValue(Object obj)
	{
		String str = "";
		if(obj == null || obj.toString().trim().length() == 0)
		{
			str = "";
		}
		else
			str = obj.toString();
		return str;
	}

	private String getFormattedValue(Object obj, int space)
	{
		String str = getFormattedValue(obj);
		return (str.trim().length()>0 ? str : "&nbsp;");
	}

	// -------------- For generic display ---------------------------------------
	private String generateHeaderForDisplay() //throws IOException
	{
		StringBuffer sb = new StringBuffer();
		sb.append("<tr>");
		for(int cnt=0;cnt < columnHeaderList.size(); cnt++ )
		{
			if(((String)displayStatusList.get(cnt)).trim().equalsIgnoreCase(Constants.TRUE))
			{
				if(((String)columnHeaderList.get(cnt)).trim().length()>0)
					sb.append("<th class=\"formSerialNumberLabelForTable\" scope=\"col\">"+ApplicationProperties.getValue((String)columnHeaderList.get(cnt))+"</th>");
				else
					sb.append("<th class=\"formSerialNumberLabelForTable\" scope=\"col\">&nbsp;</th>");
			}
		}
		sb.append("</tr>");
		
		String output =sb.toString();		
		System.out.println(output);
		return output;
	}

	private String generateDataForDisplay() throws IOException
	{
		StringBuffer sb = new StringBuffer();
		sb.append("");
		 List specimenList = dataList;
		 for(int counter=0; counter<specimenList.size(); counter++)
		 {
			 GenericSpecimen specimen = (GenericSpecimen)specimenList.get(counter);
			 sb.append("<tr>");

//			 sb.append("<td class=\"dataCellText\" >");
//			 if(showParentId)
//					sb.append(getFormattedValue(specimen.getParentName(),1));
//			 else
//				 sb.append(getFormattedValue("",1));
//			 sb.append("</td>");
//			 
//			 sb.append("<td class=\"dataCellText\" >");
//			 sb.append(getFormattedValue(specimen.getDisplayName(),1));	
//			 sb.append("</td>");
//
//			 sb.append("<td class=\"dataCellText\" >");
//			 sb.append(getFormattedValue(specimen.getBarCode(),1));	
//			 sb.append("</td>");
//
//			 sb.append("<td class=\"dataCellText\" >");
//			 sb.append(getFormattedValue(specimen.getType(),1));	
//			 sb.append("</td>");
//
//			 sb.append("<td class=\"dataCellText\" >");
//			 sb.append(getFormattedValue(specimen.getQuantity(),1));	
//			 sb.append("</td>");
//
//			 sb.append("<td class=\"dataCellText\" >");
//			 sb.append(getFormattedValue(specimen.getConcentration(),1));	
//			 sb.append("</td>");
//
//			 if(specimen.getStorageContainerForSpecimen().equalsIgnoreCase("Virtual"))
//			 {
//				 sb.append("<td class=\"dataCellText\" >");
//				 sb.append(getFormattedValue(specimen.getStorageContainerForSpecimen(),1));	
//				 sb.append("</td>");
//			 }	//	storageContainerForSpecimen == virtual
//			 else
//			 {
//				 sb.append("<td class=\"dataCellText\" >");
//			 	 sb.append("<span>");
//			 	 sb.append(getFormattedValue(specimen.getSelectedContainerName(),1));	
//			 	 sb.append("<B>:</B>");
//			 	 sb.append(getFormattedValue(specimen.getPositionDimensionOne(),1)+",");
//			 	 sb.append(getFormattedValue(specimen.getPositionDimensionTwo(),1));
//			 	 sb.append("</span>");
//				 sb.append("</td>");
//			 } //	storageContainerForSpecimen != virtual

			 //-----------
			 if(showParentId == true)
				 generateTDForDisplay(sb, getFormattedValue(specimen.getParentName(),1), 0);
			 else
				 generateTDForDisplay(sb, getFormattedValue("",1), 0);
			 
			 generateTDForDisplay(sb, getFormattedValue(specimen.getDisplayName(),1), 1);
			 generateTDForDisplay(sb, getFormattedValue(specimen.getBarCode(),1), 2);
			 generateTDForDisplay(sb, getFormattedValue(specimen.getType(),1), 3);
			 generateTDForDisplay(sb, getFormattedValue(specimen.getQuantity(),1), 4);
			 generateTDForDisplay(sb, getFormattedValue(specimen.getConcentration(),1), 5);
			 
			 if(specimen.getStorageContainerForSpecimen().equalsIgnoreCase("Virtual"))
			 {
				 generateTDForDisplay(sb, getFormattedValue(specimen.getStorageContainerForSpecimen(),1), 6);
			 }	//	storageContainerForSpecimen == virtual
			 else
			 {
				 String storageLoc ="<span>" + getFormattedValue(specimen.getSelectedContainerName(),1)
				 + " <B>:</B> " + getFormattedValue(specimen.getPositionDimensionOne(),1)+" ," 
				 + getFormattedValue(specimen.getPositionDimensionTwo(),1) + "</span>";

				 generateTDForDisplay(sb, storageLoc, 6);
			 } //	storageContainerForSpecimen != virtual

			 generateTDForDisplay(sb, getFormattedValue("",1), 7);

			 sb.append("</tr>");
		 } // outer most loop for specimenList
		 sb.append("");
		 String output =sb.toString();				
		 System.out.println(output);
		 return output;
	}

	private void generateTDForDisplay(StringBuffer sb, String value, int displayStatusListIndex)
	{
		if(((String)displayStatusList.get(displayStatusListIndex)).trim().equalsIgnoreCase(Constants.TRUE))
		{
			 sb.append("<td class=\"dataCellText\" >");
			 sb.append(value);	
			 sb.append("</td>");
		}
	}

}
