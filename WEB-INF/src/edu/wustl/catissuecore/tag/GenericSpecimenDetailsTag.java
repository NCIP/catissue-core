/**
 * 
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

import edu.wustl.catissuecore.bean.GenericSpecimen;
import edu.wustl.catissuecore.bean.SpecimenDetailsInfo;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.util.global.ApplicationProperties;

/**
 * This tag will accept the list of specimens and display them in 
 * editable or readonly mode.
 * It accepts following attributes:
 * 	columnHeaderListName
 * 	formName
 * 	dataListName
 * 	dataListType
 * 	columnListName
 * 	isReadOnly
 * 	displayColumnListName
 * 
 * @author mandar_deshmukh
 *
 */
public class GenericSpecimenDetailsTag extends TagSupport
{
	private static final long serialVersionUID = 1234567890L;
	// data type list used to display different types of data.
	private final String dataListTypes[] = {"Parent","Aliquot","Derived"};
	
	public static final String [] COLUMN_NAMES = {
		"Parent",
		"Label",
		"Barcode",
		"Type",
		"Quantity",
		"Concentration",
		"Location",
		"Collected",
		"Class",
		"Tissue Site",
		"Tissue Side",
		"Pathological Status"
	};

	public static final String [] COLUMN_LABELS = {
	"specimen.label", 
	"specimen.barcode", 
	"specimen.subType", 
	"anticipatorySpecimen.Quantity", 
	"anticipatorySpecimen.Concentration", 
	"anticipatorySpecimen.Location", 
	"anticipatorySpecimen.Collected", 
	"specimen.type", 
	"specimen.tissueSite", 
	"specimen.tissueSide", 
	"specimen.pathologicalStatus"
	};

	//--------------- TAG Attribute Section start [Will be provided by the user of the tag thru the TAG.]------------
	private String displayColumnListName = "";
	private String columnHeaderListName = "";
	private String dataListName = "";
	private String isReadOnly = "";	// ------- as decided 
	private String formName = "";
	private String dataListType = "";
	private String columnListName = "";
	//--------------- TAG Attribute Section end ------------

	//---------------  Attribute Section start ------------
	private List columnList = null;	// List containing column names
	
	private List columnHeaderList = null;
	private List displayColumnList = null;	// List of columns to show. If name not present hide the column.
	private List dataList = null;
	private boolean showParentId = false;
	private String elementPrefixPart1="";
	private String functionCall="";
	
	private int xtra=0;
	private boolean isParentList = false;
	SpecimenDetailsInfo specimenSummaryForm = null;
//	--------------- Attribute Section end ------------


	public GenericSpecimenDetailsTag()
	{
		super();
	}
// ------------------Getter - Setter Section start ---------
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
	public String getDisplayColumnListName() {
		return displayColumnListName;
	}
	public void setDisplayColumnListName(String displayColumnListName) {
		this.displayColumnListName = displayColumnListName;
	}
	public String getIsReadOnly() {
		return isReadOnly;
	}
	public void setIsReadOnly(String isReadOnly) {
		this.isReadOnly = isReadOnly;
	}

	public String getFormName() {
		return formName;
	}

	public void setFormName(String formName) {
		this.formName = formName;
	}

	public String getDataListType() {
		return dataListType;
	}

	public void setDataListType(String dataListType) {
		this.dataListType = dataListType;
	}
	public String getColumnListName() {
		return columnListName;
	}
	public void setColumnListName(String columnListName) {
		this.columnListName = columnListName;
	}
	// ------------------Getter - Setter Section end ---------

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
			if(validateTagAttributes() )
			{
				initialiseElements();
				if(validateLists())
				{
//					out.print(generateHeaderForDisplay());
					out.print(generateRowOutput());
				}
				else
				{
					out.print("<b>Column header list is not matching with the display column list.</b>");		
				}
			}
			else
			{
				out.print("<b>Some of the attributes of the tag are missing or are not proper.</b>");
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
		displayColumnListName = "";
		columnHeaderListName = "";
		dataListName = "";
		isReadOnly = "";	 
		formName = "";
		dataListType = "";
		columnList = null;
		columnHeaderList = null;
		displayColumnList = null;
		dataList = null;
		showParentId = false;
		elementPrefixPart1="";
		specimenSummaryForm = null;
		functionCall = "";
		xtra=0;
		
		return EVAL_PAGE;
	}
	
	/* method to validate the given values for the attributes.
	 * Returns true if all required attributes are in proper valid format. Otherwise returns false. 
	 */
	private boolean validateTagAttributes()
	{
		boolean result = true;
		ServletRequest request = pageContext.getRequest(); 
		ActionErrors errors = (ActionErrors)request.getAttribute(Globals.ERROR_KEY);
		if(errors == null)
		{
			errors = new ActionErrors();
		}

		if(columnHeaderListName == null || columnHeaderListName.trim().length()<1)
		{
			errors.add(ActionErrors.GLOBAL_ERROR,new ActionError("Column Header List Name is null or empty"));
			result =  false;
		}
		if(isReadOnly == null || isReadOnly.trim().length()<1)
		{
			errors.add(ActionErrors.GLOBAL_ERROR,new ActionError("DisplayOnly parameter is null or empty"));
			result =  false;
		}
		else if(!Constants.TRUE.equalsIgnoreCase(isReadOnly))
		{
			if(formName == null || formName.trim().length()<1)
			{
				errors.add(ActionErrors.GLOBAL_ERROR,new ActionError("Form name is null or empty"));
				result =  false;
			}
		}
		if(dataListName == null || dataListName.trim().length()<1)
		{
			errors.add(ActionErrors.GLOBAL_ERROR,new ActionError("Data List Name is null or empty"));
			result =  false;
		}
		if(dataListType == null || dataListType.trim().length()<1)
		{
			errors.add(ActionErrors.GLOBAL_ERROR,new ActionError("Data List Type is null or empty"));
			result =  false;
		}
		else if(!isListDataTypeOK())
		{
			errors.add(ActionErrors.GLOBAL_ERROR,new ActionError("Data List Type is invalid"));
			result =  false;
		}
		if(displayColumnListName == null || displayColumnListName.trim().length()<1)
		{
			errors.add(ActionErrors.GLOBAL_ERROR,new ActionError("Display Column List Name is null or empty"));
			result =  false;
		}
		
		request.setAttribute(Globals.ERROR_KEY,errors);
		return result;
	}
	
	private void initialiseElements()
	{
		ServletRequest request = pageContext.getRequest();
		
		specimenSummaryForm = (SpecimenDetailsInfo)request.getAttribute(formName);
		columnHeaderList = (List)request.getAttribute(columnHeaderListName);
		dataList = (List)request.getAttribute(dataListName);
		displayColumnList = (List)request.getAttribute(displayColumnListName);
		columnList = (List)request.getAttribute(columnListName);
		if(columnHeaderList == null || columnHeaderList.isEmpty())
		{
			columnHeaderList = new ArrayList();
			if(dataListType.equalsIgnoreCase(dataListTypes[0]))
				columnHeaderList.add("");
			else
				columnHeaderList.add("anticipatorySpecimen.Parent");

			for(int i=0;i< GenericSpecimenDetailsTag.COLUMN_LABELS.length; i++)
				columnHeaderList.add(GenericSpecimenDetailsTag.COLUMN_LABELS[i]);				// 0
			
		}
		if(!dataListType.equalsIgnoreCase(dataListTypes[0]))
		{
			showParentId = true;
		}
		if(displayColumnList == null || displayColumnList.isEmpty())
		{
			displayColumnList = new ArrayList();
			setFixedColumnsList(displayColumnList);	
		}
		if(columnList == null || columnList.isEmpty())
		{
			columnList = new ArrayList();
			setFixedColumnsList(columnList);
		}
	}
	
	private boolean isListDataTypeOK()
	{
		boolean result = false;
		for(int cnt=0;cnt<dataListTypes.length; cnt++)
		{
			if(dataListType.equalsIgnoreCase(dataListTypes[cnt]))
			{
				if(dataListTypes[0].equalsIgnoreCase(dataListTypes[cnt]))
					elementPrefixPart1 = "specimen[";
				else
					elementPrefixPart1 = dataListTypes[cnt].toLowerCase().trim()+"[";
				result = true;
			}
		}
		return result;
	}
	
	private List getDataList()
	{
		List l = new ArrayList();
		//setting the data list to be used to display the specimens.
		if(dataList == null || dataList.isEmpty())
		{
			if(specimenSummaryForm != null)
			{
				if(dataListType.equalsIgnoreCase(dataListTypes[0]))
					l = specimenSummaryForm.getSpecimenList();
				else if(dataListType.equalsIgnoreCase(dataListTypes[1]))
					l = specimenSummaryForm.getAliquotList();
				else if(dataListType.equalsIgnoreCase(dataListTypes[2]))
					l = specimenSummaryForm.getDerivedList();
			}
		}
		else	// get the list from ViewSpecimenSummaryForm based on list type
		{
			l = dataList;
		}
		if(dataListType.equalsIgnoreCase(dataListTypes[0]))
		{	
			xtra = 3;	
			isParentList = true;
			
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
			{str = obj.toString();}
		return str;
	}

	private String getHTMLFormattedValue(Object obj)
	{
		String str = getFormattedValue(obj);
		return (str.trim().length()>0 ? str : "&nbsp;");
	}

	// -------------- For generic display ---------------------------------------
	private String generateHeaderForDisplay() //throws IOException
	{
		StringBuffer sb = new StringBuffer();
		sb.append("<tr class=\"tableheading\">");
		for(int cnt=0;cnt < columnHeaderList.size(); cnt++ )
		{
				if(((String)columnHeaderList.get(cnt)).trim().length()>0)
					sb.append("<td class=\"black_ar_b\" scope=\"col\">"+ApplicationProperties.getValue((String)columnHeaderList.get(cnt))+"</th>");
				else
					sb.append("<td class=\"black_ar_b\" scope=\"col\">&nbsp;</th>");
		}
		sb.append("</tr>");
		
		String output =sb.toString();		
		return output;
	}
	
	private boolean validateLists()
	{
		boolean result = true;
		ServletRequest request = pageContext.getRequest(); 
		ActionErrors errors = (ActionErrors)request.getAttribute(Globals.ERROR_KEY);
		if(errors == null)
		{
			errors = new ActionErrors();
		}

		if(columnHeaderList.size() != displayColumnList.size())
		{
			errors.add(ActionErrors.GLOBAL_ERROR,new ActionError("Column Header List is not matching the Display Column List."));
			result =  false;
		}
		request.setAttribute(Globals.ERROR_KEY,errors);
		return result;
	}
		
	private void setFixedColumnsList(List list)
	{
		if(list == null){list = new ArrayList();}
		for(int i=0;i< GenericSpecimenDetailsTag.COLUMN_NAMES.length; i++)
			list.add(GenericSpecimenDetailsTag.COLUMN_NAMES[i]);				
	}

	/*
	 * Method to generate row output for generic specimen
	 */
	private String generateRowOutput() throws IOException
	{
		StringBuffer sb = new StringBuffer();
		sb.append("");
		 List specimenList = getDataList();
		 
		 for(int counter=0; counter<specimenList.size(); counter++)
		 {
			 GenericSpecimen specimen = (GenericSpecimen)specimenList.get(counter);
			 if(Constants.TRUE.equalsIgnoreCase(isReadOnly) || specimen.getReadOnly() == true)
			 {
				 addReadOnlyRow(sb, counter, specimen);
			 }
			 else
			 {	 addEditableRow(sb, counter, specimen);	}
		 } // outer most loop for specimenList
		 sb.append("");
		 String output =sb.toString();				
		 return output;
	}
		
		private void addEditableRow(StringBuffer sb,int counter,GenericSpecimen specimen)
		{
			 sb.append("<tr>");
			 StringBuffer hiddenElements = new StringBuffer();
			 hiddenElements.append("");
			 String elementNamePrefix = elementPrefixPart1+counter+"].";

			 for(int columnCounter=0; columnCounter<columnList.size(); columnCounter++)
			 {
				 if(displayColumnList.contains(columnList.get(columnCounter)))	// show element
				 {
					 sb.append(getElement(columnCounter,specimen, elementNamePrefix));
				 }	// end of show element
				 else	// start of hide element
				 {
					 getHiddenElement(columnCounter,hiddenElements, specimen, elementNamePrefix);
				 }	// end of hide element
			 }
			 sb.append("<td class=\"black_ar\" width='0'>");
			 sb.append(hiddenElements.toString());
			 addRemainingSpecimenElements(sb, elementNamePrefix, specimen);
			 sb.append("</td>");
			 sb.append("</tr>");
		}
		private void getHiddenElement(int counter, StringBuffer hiddenFields, GenericSpecimen specimen, String elementNamePrefix)
		{
			String nameValue[] = getElementAt( counter,  specimen, elementNamePrefix);
			if(nameValue.length > 2)
			{
				for(int i=0; i<nameValue.length; i+=2)
					hiddenFields.append("<input type=\"hidden\" name=\""+nameValue[i]+"\" value=\""+nameValue[i+1]+"\">");
			}
			else
				hiddenFields.append("<input type=\"hidden\" name=\""+nameValue[0]+"\" value=\""+nameValue[1]+"\">");
		}
		// TODO	// ------- To work here
		private String getElement(int counter, GenericSpecimen specimen, String elementNamePrefix)
		{
			String nameValue[] = getElementAt( counter,  specimen, elementNamePrefix);

			StringBuffer sb = new StringBuffer() ;
			if(GenericSpecimenDetailsTag.COLUMN_NAMES[0].equalsIgnoreCase(columnList.get(counter).toString()))
			{
				if(showParentId == false)
				{
					nameValue[1] = getFormattedValue(specimen.getUniqueIdentifier());
					createParentRadioComponent(sb, nameValue);
				}
				else
				 {createTextComponent(sb, nameValue, "black_ar", 8);}
//				 return sb.toString(); 
			}
			else if(GenericSpecimenDetailsTag.COLUMN_NAMES[1].equalsIgnoreCase(columnList.get(counter).toString()))
			{
				//	3June08 to uncomment after vaishali changes are commited
				if(specimenSummaryForm.getShowLabel() == true && specimen.getShowLabel() == true)
				// if(specimenSummaryForm.getShowLabel() == true)
				 {
					 createTextComponent(sb, nameValue,  "black_ar", 8);
				 }
				 else{createEmptyCell(sb);}
//				 return sb.toString(); 
			}
			else if(GenericSpecimenDetailsTag.COLUMN_NAMES[2].equalsIgnoreCase(columnList.get(counter).toString()))
			{
				//	3June08 to uncomment after vaishali changes are commited
				 if(specimenSummaryForm.getShowbarCode() == true && specimen.getShowBarcode() == true)
				//if(specimenSummaryForm.getShowbarCode() == true)
				 {
					 createTextComponent(sb, nameValue,  "black_ar", 8);
				 }
				 else{createEmptyCell(sb);}
//				return sb.toString();
			}
			else if(GenericSpecimenDetailsTag.COLUMN_NAMES[3].equalsIgnoreCase(columnList.get(counter).toString()))
			{	
				createTextComponent(sb, nameValue,  "black_ar", (14+xtra));				
//				return sb.toString();
			}
			else if(GenericSpecimenDetailsTag.COLUMN_NAMES[4].equalsIgnoreCase(columnList.get(counter).toString()))
			{
				createTextComponent(sb, nameValue,  "black_ar", 4);				
//				return sb.toString();
			}
			else if(GenericSpecimenDetailsTag.COLUMN_NAMES[5].equalsIgnoreCase(columnList.get(counter).toString()))
			{
				createTextComponent(sb, nameValue,  "black_ar", 4);				
//				return sb.toString();
			}
			else if(GenericSpecimenDetailsTag.COLUMN_NAMES[6].equalsIgnoreCase(columnList.get(counter).toString()))
			{
				createNewStorageComponent(sb, nameValue, specimen);
				//if(specimen.getStorageContainerForSpecimen()!= null && specimen.getStorageContainerForSpecimen().equalsIgnoreCase("Virtual"))
				//if((specimen.getStorageContainerForSpecimen()!= null && "Virtual".equalsIgnoreCase(specimen.getStorageContainerForSpecimen())) || (getFormattedValue(specimen.getSelectedContainerName()).trim().length() ==0))
//				if(specimen.getStorageContainerForSpecimen()!= null && "Virtual".equalsIgnoreCase(specimen.getStorageContainerForSpecimen()))
//				{
////					createVirtualStorageComponent( sb, nameValue);
//					createNewStorageComponent(sb, nameValue, specimen);
//				}
//				else
//				{
//					//createStorageComponent(sb, nameValue, specimen);
//					createNewStorageComponent(sb, nameValue, specimen);
//				}
				
//				return sb.toString();
			}
			else if(GenericSpecimenDetailsTag.COLUMN_NAMES[7].equalsIgnoreCase(columnList.get(counter).toString()))
			{
				//Mandar : to add collected functionality only for selected specimen
				if(isParentList == true && specimenSummaryForm.getSelectedSpecimenId().equals(specimen.getUniqueIdentifier()))
				{
					functionCall = "onclick=\"onClickCollected(this)\"";
				}
				else{functionCall = "";}
				createCollectedComponent(sb, nameValue, "getElement");				
//				return sb.toString();
			}
			else if(GenericSpecimenDetailsTag.COLUMN_NAMES[8].equalsIgnoreCase(columnList.get(counter).toString()))
			{
				createTextComponent(sb, nameValue,  "black_ar", 8);				
//				return sb.toString();
			}
			else if(GenericSpecimenDetailsTag.COLUMN_NAMES[9].equalsIgnoreCase(columnList.get(counter).toString()))
			{
				createTextComponent(sb, nameValue,  "black_ar", 8);				
//				return sb.toString();
			}
			else if(GenericSpecimenDetailsTag.COLUMN_NAMES[10].equalsIgnoreCase(columnList.get(counter).toString()))
			{
				createTextComponent(sb, nameValue,  "black_ar", 8);				
//				return sb.toString();
			}
			else if(GenericSpecimenDetailsTag.COLUMN_NAMES[11].equalsIgnoreCase(columnList.get(counter).toString()))
			{
				createTextComponent(sb, nameValue,  "black_ar", 8);				
//				return sb.toString();
			}
			
			return sb.toString();	
		}
		
		private String[] getElementAt(int counter, GenericSpecimen specimen, String elementNamePrefix)
		{
			String s[] = new String[2] ;
			if(GenericSpecimenDetailsTag.COLUMN_NAMES[0].equalsIgnoreCase(columnList.get(counter).toString()))
			{	s[0] = elementNamePrefix+"parentName";	s[1] =getFormattedValue(specimen.getParentName()); }
			else if(GenericSpecimenDetailsTag.COLUMN_NAMES[1].equalsIgnoreCase(columnList.get(counter).toString()))
			{	s[0] = elementNamePrefix+"displayName";		s[1] =getFormattedValue(specimen.getDisplayName()); }
			else if(GenericSpecimenDetailsTag.COLUMN_NAMES[2].equalsIgnoreCase(columnList.get(counter).toString()))
			{	s[0] = elementNamePrefix+"barCode";	s[1] =getFormattedValue(specimen.getBarCode()); }
			else if(GenericSpecimenDetailsTag.COLUMN_NAMES[3].equalsIgnoreCase(columnList.get(counter).toString()))
			{	s[0] = elementNamePrefix+"type";	s[1] =getFormattedValue(specimen.getType()); }
			else if(GenericSpecimenDetailsTag.COLUMN_NAMES[4].equalsIgnoreCase(columnList.get(counter).toString()))
			{	s[0] = elementNamePrefix+"quantity";	s[1] =getFormattedValue(specimen.getQuantity()); }
			else if(GenericSpecimenDetailsTag.COLUMN_NAMES[5].equalsIgnoreCase(columnList.get(counter).toString()))
			{	s[0] = elementNamePrefix+"concentration";	s[1] =getFormattedValue(specimen.getConcentration()); }
			else if(GenericSpecimenDetailsTag.COLUMN_NAMES[6].equalsIgnoreCase(columnList.get(counter).toString()))
			{
				//if((specimen.getStorageContainerForSpecimen()!= null && specimen.getStorageContainerForSpecimen().equalsIgnoreCase("Virtual")) || (getFormattedValue(specimen.getSelectedContainerName()).trim().length() ==0))
				if((specimen.getStorageContainerForSpecimen()!= null && "Virtual".equalsIgnoreCase(specimen.getStorageContainerForSpecimen())))
				{
//					s[0] = elementNamePrefix+"storageContainerForSpecimen";	s[1] =getFormattedValue(specimen.getStorageContainerForSpecimen());
					s = new String[10];
					s[0] = elementNamePrefix+"selectedContainerName";	s[1] =getFormattedValue("");
					s[2] = elementNamePrefix+"positionDimensionOne";	s[3] =getFormattedValue("");
					s[4] = elementNamePrefix+"positionDimensionTwo";	s[5] =getFormattedValue("");
					s[6] = elementNamePrefix+"containerId";				s[7] =getFormattedValue("");
					s[8] = elementNamePrefix+"storageContainerForSpecimen";	s[9] =getFormattedValue(specimen.getStorageContainerForSpecimen());

				}
				else
				{
						s = new String[10];
						s[0] = elementNamePrefix+"selectedContainerName";	s[1] =getFormattedValue(specimen.getSelectedContainerName());
						s[2] = elementNamePrefix+"positionDimensionOne";	s[3] =getFormattedValue(specimen.getPositionDimensionOne());
						s[4] = elementNamePrefix+"positionDimensionTwo";	s[5] =getFormattedValue(specimen.getPositionDimensionTwo());
						s[6] = elementNamePrefix+"containerId";				s[7] =getFormattedValue(specimen.getContainerId());
						s[8] = elementNamePrefix+"storageContainerForSpecimen";	s[9] =getFormattedValue(specimen.getStorageContainerForSpecimen());
				}
			}
			else if(GenericSpecimenDetailsTag.COLUMN_NAMES[7].equalsIgnoreCase(columnList.get(counter).toString()))
			{	s[0] = elementNamePrefix+"checkedSpecimen";	s[1] =getFormattedValue(specimen.getCheckedSpecimen()); }
			else if(GenericSpecimenDetailsTag.COLUMN_NAMES[8].equalsIgnoreCase(columnList.get(counter).toString()))
			{	s[0] = elementNamePrefix+"className";	s[1] =getFormattedValue(specimen.getClassName()); }
			else if(GenericSpecimenDetailsTag.COLUMN_NAMES[9].equalsIgnoreCase(columnList.get(counter).toString()))
			{	s[0] = elementNamePrefix+"tissueSite";	s[1] =getFormattedValue(specimen.getTissueSite()); }
			else if(GenericSpecimenDetailsTag.COLUMN_NAMES[10].equalsIgnoreCase(columnList.get(counter).toString()))
			{	s[0] = elementNamePrefix+"tissueSide";	s[1] =getFormattedValue(specimen.getTissueSide()); }
			else if(GenericSpecimenDetailsTag.COLUMN_NAMES[11].equalsIgnoreCase(columnList.get(counter).toString()))
			{	s[0] = elementNamePrefix+"pathologicalStatus";	s[1] =getFormattedValue(specimen.getPathologicalStatus()); }

			return s;
		}

		private void createTextComponent(StringBuffer sb, String[] nameValue, String styleClass, int size)
		{
			 sb.append("<td class=\"black_ar\" >"); 
			 sb.append("<input type=\"text\" name=\""+nameValue[0]+"\" value=\""+nameValue[1]+"\" class=\""+styleClass+"\" size=\""+size+"\">");
			 sb.append("</td>");
		}
		
		private void createStorageComponent(StringBuffer sb, String[] nameValue, GenericSpecimen specimen)
		{

			 sb.append("<td class=\"black_ar\" >");
//			 sb.append(getHTMLFormattedValue(specimen.getStorageContainerForSpecimen()));

			 String specimenId = getFormattedValue(specimen.getUniqueIdentifier());
			 String specimenClass = getFormattedValue(specimen.getClassName());
			 Long collectionProtocolId = specimen.getCollectionProtocolId();
			 
			  String containerId = "containerId_"+specimenId;
			  String selectedContainerName = "selectedContainerName_"+specimenId;
			  String positionDimensionOne = "positionDimensionOne_"+specimenId;
			  String positionDimensionTwo = "positionDimensionTwo_"+specimenId;
			  String specimenClassName = (String)specimenClass;
			  String cpId = getFormattedValue(collectionProtocolId);
			  String functionCall="showMap('" + selectedContainerName + "','"+
											positionDimensionOne +"','"
											+ positionDimensionTwo +"','"
											+containerId +"','"+
											specimenClassName +"','"+
											cpId +"')" ;
			  int scSize=12 +xtra;
			 
				 sb.append("<table style=\"font-size:1em\" size=\"100%\">");
				 	sb.append("<tr>");
				 		sb.append("<td>");
				 			sb.append("<input type=\"text\" name=\""+nameValue[0]+"\" value=\"" + nameValue[1]+"\" size=\""+scSize+"\" class=\"black_ar\" onmouseover=\"showTip(this.id)\" id=\""+selectedContainerName+"\" >");
				 		sb.append("</td>");
				 		sb.append("<td>");
				 			sb.append("<input type=\"text\" name=\""+nameValue[2]+"\" value=\"" + nameValue[3]+"\" size=\"2\" class=\"black_ar\" id=\""+positionDimensionOne+"\" >");
				 		sb.append("</td>");
				 		sb.append("<td>");
				 			sb.append("<input type=\"text\" name=\""+nameValue[4]+"\" value=\"" + nameValue[5]+"\" size=\"2\" class=\"black_ar\" id=\""+positionDimensionTwo+"\" >");
				 		sb.append("</td>");
				 		sb.append("<td>");
				 			sb.append("<a href=\"#\" onclick=\""+functionCall+"\">");
				 			sb.append("<img src=\"images/Tree.gif\" border=\"0\" width=\"13\" height=\"15\" title=\'View storage locations\'>");
				 			sb.append("</a>");
				 			sb.append("<input type=\"hidden\" name=\""+nameValue[6]+"\" value=\""+nameValue[7]+"\" id=\""+containerId+"\">");
				 		sb.append("</td>");
				 	sb.append("</tr>");										
				 sb.append("</table>");
				 
				 sb.append("</td>");
		}

		private void createVirtualStorageComponent(StringBuffer sb, String[] nameValue)
		{
			 sb.append("<td class=\"black_ar\" >");
			 sb.append(getHTMLFormattedValue(nameValue[1]));
			 sb.append("<input type=\"hidden\" name=\""+nameValue[0]+"\" value=\""+nameValue[1]+"\">");
			 sb.append("</td>");
		}
		
		private void addRemainingSpecimenElements(StringBuffer sb, String elementNamePrefix, GenericSpecimen specimen)
		{
			String nameValue[][] = getRemainingSpecimenElementsData(specimen, elementNamePrefix);
			for(int i =0; i<nameValue.length; i++)
			 sb.append("<input type=\"hidden\" name=\""+nameValue[i][0]+"\" value=\""+nameValue[i][1]+"\">");
		}
		
		private String[][] getRemainingSpecimenElementsData(GenericSpecimen specimen, String elementNamePrefix)
		{
			String s[][] = new String[4][2];
			s[0][0] = elementNamePrefix+"collectionProtocolId";	s[0][1] = getFormattedValue(specimen.getCollectionProtocolId()); 	
			s[1][0] = elementNamePrefix+"readOnly";				s[1][1] = getFormattedValue(specimen.getReadOnly()); 
			s[2][0] = elementNamePrefix+"uniqueIdentifier";		s[2][1] = getFormattedValue(specimen.getUniqueIdentifier()); 
			s[3][0] = elementNamePrefix+"id";					s[3][1] = getFormattedValue(specimen.getId()); 
			return s;
		}

		private void addReadOnlyRow(StringBuffer sb,int counter,GenericSpecimen specimen)
		{
			 sb.append("<tr>");
			 StringBuffer hiddenElements = new StringBuffer();
			 hiddenElements.append("");
			 String elementNamePrefix = elementPrefixPart1+counter+"].";

			 for(int columnCounter=0; columnCounter<columnList.size(); columnCounter++)
			 {
				 if(displayColumnList.contains(columnList.get(columnCounter)))	// show element
				 {
					 sb.append(getTextElement(columnCounter,specimen, elementNamePrefix));
				 }	// end of show element
				 getHiddenElement(columnCounter,hiddenElements, specimen, elementNamePrefix);
			 }
			 sb.append("<td class=\"black_ar\" width='0' >");
			 sb.append(hiddenElements.toString());
			 addRemainingSpecimenElements(sb, elementNamePrefix, specimen);
			 sb.append("</td>");
			 sb.append("</tr>");
		}

		private String getTextElement(int counter, GenericSpecimen specimen, String elementNamePrefix)
		{
			String nameValue[] = getElementAt( counter,  specimen, elementNamePrefix);
			StringBuffer sb = new StringBuffer() ;
			if(GenericSpecimenDetailsTag.COLUMN_NAMES[1].equalsIgnoreCase(columnList.get(counter).toString()))
			{
					//3June08 to uncomment after vaishali changes are commited
					 if(specimenSummaryForm.getShowLabel() == true && specimen.getShowLabel() == true)
//					if(specimenSummaryForm.getShowLabel() == true)
					 {
							sb.append("<td class=\"black_ar\" >");
							sb.append(getHTMLFormattedValue(nameValue[1]));	
							sb.append("</td>");
					 }
					 else{createEmptyCell(sb);}
			}
			else if(GenericSpecimenDetailsTag.COLUMN_NAMES[0].equalsIgnoreCase(columnList.get(counter).toString()))
			{
				if(showParentId == false)
				{
					nameValue[1] = getFormattedValue(specimen.getUniqueIdentifier());
					createParentRadioComponent(sb, nameValue);
				}
				else
				{
					sb.append("<td class=\"black_ar\" >");
						sb.append(getHTMLFormattedValue(nameValue[1]));
					sb.append("</td>");
				}
			}
			else if(GenericSpecimenDetailsTag.COLUMN_NAMES[7].equalsIgnoreCase(columnList.get(counter).toString()))
			{
//				Mandar : to add collected functionality only for selected specimen
				if(isParentList == true && specimenSummaryForm.getSelectedSpecimenId().equals(specimen.getUniqueIdentifier()))
				{
					functionCall = "onclick=\"onClickCollected(this)\"";
				}
				else{functionCall = "";}

				createCollectedComponent(sb, nameValue,"getTextElement");				
			}
			else
			{
				sb.append("<td class=\"black_ar\" nowrap >");
				if(nameValue.length > 2)
				{
					if(nameValue[1].trim().length()>0)
					{
						sb.append(nameValue[1]);sb.append(":");sb.append(nameValue[3]);sb.append(",");sb.append(nameValue[5]);
					}
					else
					{
						sb.append(getHTMLFormattedValue(specimen.getStorageContainerForSpecimen()));
					}
				}
				else
				{
					sb.append(getHTMLFormattedValue(nameValue[1]));
				}
				sb.append("</td>");
			}
			return sb.toString();
		}

		// Generic for testing to be deleted if not used later.
		private void createRadioComponent(StringBuffer sb, String[] nameValue, String elementNamePrefix)
		{
			 sb.append("<td class=\"dataCellText\" >");
			 if(specimenSummaryForm.getSelectedSpecimenId().equalsIgnoreCase(nameValue[1]))
				 sb.append("<input type=\"radio\" name=\"selectedSpecimenId\" value=\""+nameValue[1]+"\" checked=\"checked\" onclick=\" form.action=\'GenericSpecimenSummary.do\'; submit()\">");	 
			 else
				 sb.append("<input type=\"radio\" name=\"selectedSpecimenId\" value=\""+nameValue[1]+"\" onclick=\" form.action=\'GenericSpecimenSummary.do\'; submit()\">");	 
			 sb.append("</td>");
		}

		private void createParentRadioComponent(StringBuffer sb, String[] nameValue)
		{
			 sb.append("<td class=\"black_ar\" >");
			 if(specimenSummaryForm.getSelectedSpecimenId().equalsIgnoreCase(nameValue[1]))
				 sb.append("<input type=\"radio\" name=\"selectedSpecimenId\" value=\""+nameValue[1]+"\" checked=\"checked\" onclick=\"onParentRadioBtnClick()\">");	 
			 else
				 sb.append("<input type=\"radio\" name=\"selectedSpecimenId\" value=\""+nameValue[1]+"\" onclick=\"onParentRadioBtnClick()\">");
//			 sb.append(nameValue[0]);
			 sb.append("</td>");
		}

		private void createCollectedComponent(StringBuffer sb, String[] nameValue, String calledFrom)
		{
			 sb.append("<td class=\"black_ar\" >");
			 if(specimenSummaryForm.getShowCheckBoxes() == true)
			 {
				 if("getTextElement".equalsIgnoreCase(calledFrom))
				 {
					 sb.append("<input type=\"checkbox\" name=\""+nameValue[0]+"\" value=\"true\" disabled=\"true\" checked=\"checked\">");
				 }
				 else
				 {
					 if(Constants.TRUE.equalsIgnoreCase(nameValue[1]))
						 sb.append("<input type=\"checkbox\" name=\""+nameValue[0]+"\" value=\"on\" checked=\"checked\" "+functionCall+">");
					 else
						 sb.append("<input type=\"checkbox\" name=\""+nameValue[0]+"\" value=\"on\" "+functionCall+">");
				}
			 }
			 else
	 			 sb.append(getHTMLFormattedValue(""));
 			 sb.append("</td>");
		}

		private void createEmptyCell(StringBuffer sb)
		{
			 sb.append("<td class=\"black_ar\" >"); 
			 sb.append("&nbsp;");
			 sb.append("</td>");
		}
		
		private void createNewStorageComponent(StringBuffer sb, String[] nameValue, GenericSpecimen specimen)
		{

			 sb.append("<td class=\"black_ar\" >");
//			 sb.append(getHTMLFormattedValue(specimen.getStorageContainerForSpecimen()));

			 String specimenId = getFormattedValue(specimen.getUniqueIdentifier());
			 String specimenClass = getFormattedValue(specimen.getClassName());
			 Long collectionProtocolId = specimen.getCollectionProtocolId();
			 
			  String containerId = "containerId_"+specimenId;
			  String selectedContainerName = "selectedContainerName_"+specimenId;
			  String positionDimensionOne = "positionDimensionOne_"+specimenId;
			  String positionDimensionTwo = "positionDimensionTwo_"+specimenId;
			  String specimenClassName = (String)specimenClass;
			  String cpId = getFormattedValue(collectionProtocolId);
			  String functionCall="showMap('" + selectedContainerName + "','"+
											positionDimensionOne +"','"
											+ positionDimensionTwo +"','"
											+containerId +"','"+
											specimenClassName +"','"+
											cpId +"')" ;
			  int scSize=12 +xtra;
	 			String sid = specimen.getUniqueIdentifier();
	 			String isDisabled = ""; 
			  
				 sb.append("<table style=\"font-size:1em\" size=\"100%\">");
				 	sb.append("<tr>");
				 		sb.append("<td>");
				 			sb.append("");

				 			sb.append("<select name=\""+nameValue[8]+"\" size=\"1\" onchange=\"scForSpecimen(this,'"+ sid +"','"+specimenClassName+"')\" class=\"black_new\" id=\""+nameValue[9]+"\">");

							  if("Virtual".equals(specimen.getStorageContainerForSpecimen()))
							  {
								  sb.append("<option value=\"Virtual\" selected=\"selected\">Virtual</option>");
								  isDisabled = "disabled='disabled'";
							  }
							  else
							  {
								  sb.append("<option value=\"Virtual\">Virtual</option>");
							  }
							  if("Auto".equals(specimen.getStorageContainerForSpecimen()))
							  {
								  sb.append("<option value=\"Auto\" selected=\"selected\">Auto</option>");
							  }
							  else
							  {
								  sb.append("<option value=\"Auto\">Auto</option>");
							  }
							  if("Manual".equals(specimen.getStorageContainerForSpecimen()))
							  {
								  sb.append("<option value=\"Manual\" selected=\"selected\">Manual</option>");
							  }
							  else
							  {
								  sb.append("<option value=\"Manual\">Manual</option>");
							  }
							  sb.append("</select>");  
				 		sb.append("</td>");

				 		sb.append("<td>");
				 			sb.append("<input type=\"text\" name=\""+nameValue[0]+"\" value=\"" + nameValue[1]+"\" size=\""+scSize+"\" class=\"black_ar\" id=\""+selectedContainerName+"\" "+ isDisabled +" >");
				 		sb.append("</td>");
				 		sb.append("<td>");
				 			sb.append("<input type=\"text\" name=\""+nameValue[2]+"\" value=\"" + nameValue[3]+"\" size=\"2\" class=\"black_ar\" id=\""+positionDimensionOne+"\" "+ isDisabled +" >");
				 		sb.append("</td>");
				 		sb.append("<td>");
				 			sb.append("<input type=\"text\" name=\""+nameValue[4]+"\" value=\"" + nameValue[5]+"\" size=\"2\" class=\"black_ar\" id=\""+positionDimensionTwo+"\" "+ isDisabled +" >");
				 		sb.append("</td>");
				 		sb.append("<td>");
				 			sb.append("<a href=\"#\" onclick=\""+functionCall+"\">");
				 			sb.append("<img src=\"images/Tree.gif\" border=\"0\" width=\"13\" height=\"15\" title=\'View storage locations\'>");
				 			sb.append("</a>");
				 			sb.append("<input type=\"hidden\" name=\""+nameValue[6]+"\" value=\""+nameValue[7]+"\" id=\""+containerId+"\">");
				 		sb.append("</td>");
				 	sb.append("</tr>");										
				 sb.append("</table>");
				 
				 sb.append("</td>");
		}

}

