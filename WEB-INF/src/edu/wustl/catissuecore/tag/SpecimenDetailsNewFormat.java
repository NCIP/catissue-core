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
public class SpecimenDetailsNewFormat extends TagSupport
{
	private static final long serialVersionUID = 1234567890L;
	// data type list used to display different types of data.
	private transient final String dataListTypes[] = {"Parent","Aliquot","Derived"};
	
	public static final String [] COLUMN_NAMES = {
		"Parent",
		"Label",
		"Barcode",
		"Type",
		"Quantity",
		"Concentration",
		"Location",
		"Collected",
		"PrintLabel"		
	};

	public static final String [] COLUMN_LABELS = {
	"specimen.label", 
	"specimen.barcode", 
	"specimen.subType", 
	"anticipatorySpecimen.Quantity", 
	"anticipatorySpecimen.Concentration", 
	"anticipatorySpecimen.Location", 
	"anticipatorySpecimen.Collected",
	"specimen.printLabel"	
	};

	// ----------- Mandar : 2Dec08 for New UI format start -----------------------------
	public static final String [] HDR1_COLS = {
		"Parent",
		"Label",
		"Barcode",
		"Type",
		"Quantity",
		"Concentration",
		"Location",
		"Collected",
		"PrintLabel"
	};
	public static final String [] HDR2_COLS = {
	"Type",
	"Pathological Status",
	"Tissue Side",
	"Tissue Site"
	};


	public static final String [] H1COL_LBLS = {
	"specimen.label", 
	"specimen.barcode", 
	"specimen.subType", 
	"anticipatorySpecimen.Quantity", 
	"anticipatorySpecimen.Concentration", 
	"anticipatorySpecimen.Location", 
	"anticipatorySpecimen.Collected",
	"specimen.printLabel"
	};

	public static final String [] H2COL_LBLS = {
	"specimen.subType",
	"specimen.pathologicalStatus",
	"specimen.tissueSide",
	"specimen.tissueSite"
	};
	
	// ----------- Mandar : 2Dec08 for New UI format end -------------------------------
	
	private static transient final String TR_OPEN = "<TR>";
	private static transient final String TR_CLOSE = "</TR>";
	private static transient final String TD_OPEN = "<TD>";
	private static transient final String TD_CLOSE = "</TD>";
	private static transient final String STYLE_CLASS = "black_ar";
	private static transient final String SPACE = "&nbsp;";
	private static transient final String TR_GRAY = "<TR class='tr_anti_bg_gray'>";
	private static transient final String TR_BLUE = "<TR class='tr_anti_bg_blue'>";
	private static transient final String TD_1HLF = "<TD width='";
	private static transient final String TD_2HLF = "%'>";

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
	private transient List columnList = null;	// List containing column names
	
	private transient  List columnHeaderList = null;
	private transient  List displayColumnList = null;	// List of columns to show. If name not present hide the column.
	private transient  List dataList = null;
	private transient  boolean showParentId = false;
	private String elementPrefixPart1="";
	private transient String functionCall="";
	
	private transient int xtra=0;
	private transient int colNum=0;
	private  transient boolean isParentList = false;
	private transient int pWd=10;
	private transient int cWd=10;
	SpecimenDetailsInfo specimenSummaryForm = null;
//	--------------- Attribute Section end ------------

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
		colNum = 0;
		pWd = 10;
		cWd = 10;

		
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
		else if(!Constants.TRUE.equalsIgnoreCase(isReadOnly) && (formName == null || formName.trim().length()<1))
		{
			errors.add(ActionErrors.GLOBAL_ERROR,new ActionError("Form name is null or empty"));
			result =  false;
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
//		columnHeaderList = (List)request.getAttribute(columnHeaderListName);
		dataList = (List)request.getAttribute(dataListName);
//		displayColumnList = (List)request.getAttribute(displayColumnListName);
//		columnList = (List)request.getAttribute(columnListName);
//		if(columnHeaderList == null || columnHeaderList.isEmpty())
		{
			columnHeaderList = new ArrayList();
			if(dataListType.equalsIgnoreCase(dataListTypes[0]))
				{ columnHeaderList.add(""); }
			else
				{ columnHeaderList.add("anticipatorySpecimen.Parent"); }
			
			for(int i=0;i< SpecimenDetailsNewFormat.COLUMN_LABELS.length; i++)
			{	columnHeaderList.add(SpecimenDetailsNewFormat.COLUMN_LABELS[i]); }				// 0
			
		}

		if(!dataListType.equalsIgnoreCase(dataListTypes[0]))
		{
			showParentId = true;
		}
//		if(displayColumnList == null || displayColumnList.isEmpty())
		{
			displayColumnList = new ArrayList();
			setFixedColumnsList(displayColumnList);	
		}
//		if(columnList == null || columnList.isEmpty())
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
				{
					elementPrefixPart1 = "specimen[";
					colNum = 8;
				}
				else
				{	
					elementPrefixPart1 = dataListTypes[cnt].toLowerCase().trim()+"[";
					colNum = 9;
				}
				result = true;
			}
		}
		return result;
	}
	
	private List getDataList()
	{
		List lst = new ArrayList();
		//setting the data list to be used to display the specimens.
		if(dataList == null || dataList.isEmpty())
		{
			if(specimenSummaryForm != null)
			{
				if(dataListType.equalsIgnoreCase(dataListTypes[0]))
				{	lst = specimenSummaryForm.getSpecimenList(); }
				else if(dataListType.equalsIgnoreCase(dataListTypes[1]))
				{	lst = specimenSummaryForm.getAliquotList(); }
				else if(dataListType.equalsIgnoreCase(dataListTypes[2]))
				{	lst = specimenSummaryForm.getDerivedList(); }
			}
		}
		else	// get the list from ViewSpecimenSummaryForm based on list type
		{
			lst = dataList;
		}
		if(dataListType.equalsIgnoreCase(dataListTypes[0]))
		{	
			xtra = 6;	
			isParentList = true;
			pWd = 5;
			cWd = 12;
		}
		if(dataListType.equalsIgnoreCase(dataListTypes[2]))
		{	
			xtra = 3;	
		}

		return lst;
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
		return (str.trim().length()>0 ? str : SPACE);
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
			result = false;
		}
		request.setAttribute(Globals.ERROR_KEY,errors);
		return result;
	}
		
	private void setFixedColumnsList(List list)
	{
		if(list == null){list = new ArrayList();}
		for(int i=0;i< SpecimenDetailsNewFormat.COLUMN_NAMES.length; i++)
		{	list.add(SpecimenDetailsNewFormat.COLUMN_NAMES[i]); }				
	}

	/*
	 * Method to generate row output for generic specimen
	 */
	private String generateRowOutput() throws IOException
	{
		StringBuffer sb = new StringBuffer();
		sb.append("");
		 List specimenList = getDataList();
		 if(dataListTypes[1].equalsIgnoreCase(dataListType) )
		 {
			 sb.append("<TABLE border=0 width='100%'>");
		 }
		 for(int counter=0; counter<specimenList.size(); counter++)
		 {
			 GenericSpecimen specimen = (GenericSpecimen)specimenList.get(counter);
			 if(Constants.TRUE.equalsIgnoreCase(isReadOnly) || specimen.getReadOnly())
			 {
//				 addReadOnlyRow(sb, counter, specimen);
				 addEditableRow(sb, counter, specimen,true);
			 }
			 else
			 {	 addEditableRow(sb, counter, specimen,false);	}
		 } // outer most loop for specimenList
		 sb.append("");
		 if(dataListTypes[1].equalsIgnoreCase(dataListType) )
		 {
			 sb.append("</TABLE>");
		 }

		 //String output =sb.toString();				
		 return sb.toString();
	}

	private void addEditableRow(StringBuffer sb,int counter,GenericSpecimen specimen, boolean isTextRow)
	{
			 // ------------ Mandar : 2Dec08 New Code for new formatUI start----------------
		if(dataListTypes[1].equalsIgnoreCase(dataListType) )	// for aliquots
		{
			 String elementNamePrefix = elementPrefixPart1+counter+"].";
			 if(counter ==0)
			 {
				 createHeaderRow1(sb,TR_GRAY,specimen);	// row1 containing headers for first half (editable fields) 
			 }
			 createFieldRow(sb, counter, specimen, elementNamePrefix,isTextRow);		// row2 containing actual editable fields (first half) 
		}
		else
		{
			 String elementNamePrefix = elementPrefixPart1+counter+"].";
			 if((counter+1)%2 == 0)
			 {
				 sb.append(TR_BLUE);	 
			 }
			 else
			 {
				 sb.append(TR_GRAY);
			 }
			 
			 sb.append(TD_OPEN);
			 sb.append("<TABLE border=0 width='100%'>");
			 createHeaderRow1(sb,TR_OPEN,specimen);	// row1 containing headers for first half (editable fields)
			 createFieldRow(sb, counter, specimen, elementNamePrefix,isTextRow);		// row2 containing actual editable fields (first half) 
			 createHeaderRow2(sb);	// row3 containing headers for second half (text fields)
			 createTextFieldRow(sb, counter, specimen, elementNamePrefix);	// row containing text data (second half)
			
			 sb.append("</TABLE>");
			 sb.append(TD_CLOSE);
			 sb.append(TR_CLOSE);
		}

			 // ------------ Mandar : 2Dec08 New Code for new formatUI end----------------
			 
	}
	 // ------------ Mandar : 2Dec08 New Code for new formatUI start----------------
	private void createParentComponent(StringBuffer sb, GenericSpecimen specimen, String elementNamePrefix, boolean isTextRow)
	{
	 	String nameValue[] = get1EleDetAt(0, specimen, elementNamePrefix);
	 	if(!showParentId )
		{
	 		sb.append("<TD rowspan=3 width='"+pWd+"%'>");
	 		nameValue[1] = getFormattedValue(specimen.getUniqueIdentifier());
			createParentRadioComponent(sb, nameValue);
		}
		else
		{
			sb.append(TD_1HLF+pWd+TD_2HLF);
			if(isTextRow)
			{
				sb.append("<SPAN class="+STYLE_CLASS+">"+nameValue[1]+"</SPAN>"); 
			}
			else
			{
				createTextComponent(sb, nameValue, STYLE_CLASS, 8);
			}
		}
	 	sb.append(TD_CLOSE);
	}
	
	private void createParentRadioComponent(StringBuffer sb, String[] nameValue)
	{
//		 sb.append("<td class=\"black_ar_md\" >");
		 if(specimenSummaryForm.getSelectedSpecimenId().equalsIgnoreCase(nameValue[1]))
		 {	 sb.append("<input type=\"radio\" name=\"selectedSpecimenId\" value=\""+nameValue[1]+"\" checked=\"checked\" onclick=\"onParentRadioBtnClick()\">"); }	 
		 else
		 { sb.append("<input type=\"radio\" name=\"selectedSpecimenId\" value=\""+nameValue[1]+"\" onclick=\"onParentRadioBtnClick()\">"); }
//		 sb.append(TD_CLOSE);
	}

	private void createTextComponent(StringBuffer sb, String[] nameValue, String styleClass, int size)
	{
//		 sb.append("<td class=\"black_ar_md\" >"); 
		 sb.append("<input type=\"text\" name=\""+nameValue[0]+"\" value=\""+nameValue[1]+"\" class=\""+styleClass+"\" size=\""+size+"\">");
//		 if(nameValue.length == 3)
//		 {
//			 sb.append("<BR><SPAN nowrap>"+nameValue[2]+"</SPAN>");
//		 }
//	 sb.append(TD_CLOSE);
	}


	private void createHeaderRow1(StringBuffer sb, String tr,GenericSpecimen specimen)
	{
		 sb.append(tr);
		 int tmpd=0,tmpcwd = 0;
		 if(dataListType.equalsIgnoreCase(dataListTypes[2]))
		 {
			 tmpd = 3;
		 }
		 tmpcwd = cWd;
		 for(int cnt=0; cnt < columnHeaderList.size(); cnt++ )
		 {
			 if(cnt==5)//Concentration
			 {cWd = cWd-tmpd;}
			 else
			 { 
				 cWd = tmpcwd;
			 }
				if((((String)columnHeaderList.get(cnt)).trim().length() > 0) )
				{
				 	// to be displayed only in case of aliquots.	|| 		
				 	if((cnt == 3 && !dataListType.equalsIgnoreCase(dataListTypes[1])) ||
				 		(cnt == 5 && !dataListType.equalsIgnoreCase(dataListTypes[2]))) // to be displayed only in case of derived.
				 	{
				 		continue;
				 	}
				 	else if((cnt ==1 && !(specimenSummaryForm.getShowLabel() && specimen.getShowLabel())) || 
				 			(cnt ==2 && !(specimenSummaryForm.getShowbarCode() && specimen.getShowBarcode())))
				 	{
				 		sb.append(TD_1HLF+"1"+TD_2HLF);
				 		sb.append(SPACE);		
				 	}
				 	else if(cnt == 6)//Location
				 	{
				 		sb.append("<TD colspan=4 width="+(50-pWd+tmpd)+"%>");// 3 35
				 		sb.append("<SPAN class=black_ar_b>"+ApplicationProperties.getValue((String)columnHeaderList.get(6))+"</SPAN>");
				 	}
				 	else if(cnt==7 && !specimenSummaryForm.getShowCheckBoxes())//Collected
				 	{
				 		sb.append(TD_1HLF+3+TD_2HLF);//bug 11169
				 		sb.append(SPACE);	
				 	}
				 	else if(cnt==8)//print bug 11169
			 		{
				 		sb.append(TD_1HLF+3+TD_2HLF);
				 		sb.append("<center><SPAN class=black_ar_b>"+ApplicationProperties.getValue((String)columnHeaderList.get(8))+"</SPAN></center>");
					}
				 	else
				 	{
				 		//bug 11169 start
				 		if(cnt==7)
				 		{
				 			sb.append(TD_1HLF+3+TD_2HLF);
				 			sb.append("<center><SPAN class=black_ar_b>"+ApplicationProperties.getValue((String)columnHeaderList.get(cnt))+"</SPAN></center>");
				 		}
				 		else
				 		{
				 		   sb.append(TD_1HLF+cWd+TD_2HLF);
				 		   sb.append("<SPAN class=black_ar_b>"+ApplicationProperties.getValue((String)columnHeaderList.get(cnt))+"</SPAN>");
				 		}
				 		//bug 11169 end
				 	}			 	
				 
				}
				else
				{
					sb.append(TD_1HLF+pWd+TD_2HLF);	sb.append(SPACE);	
				}
			sb.append(TD_CLOSE);
		 }
	 	sb.append(TR_CLOSE);
	}

	private void createHeaderRow2(StringBuffer sb)
	{
		String colspan="";
		int cols=0;
		int colsp=1;
		if(dataListType.equalsIgnoreCase(dataListTypes[2]))
		{
			colspan="colspan=2";
			colsp=2;
		}
		 sb.append(TR_OPEN);
		 for(int cnt=0; cnt < H2COL_LBLS.length; cnt++ )
		 {
			 if(dataListType.equalsIgnoreCase(dataListTypes[2]) && cnt > 1)
			 {
				 sb.append("<TD colspan=2 width=22%>&nbsp;</TD>" );
				 cols = cols +2;
			 }
			 else if(cnt ==1)
			 {
				 sb.append("<TD colspan=2 width=22%>");
				 	sb.append("<SPAN class=black_ar_b>"+ApplicationProperties.getValue(H2COL_LBLS[cnt])+"</SPAN>");
				 sb.append(TD_CLOSE);
				 cols = cols +2;
			 }
			 else
			 {
				sb.append("<TD "+ colspan + " width="+(colsp*15)+"% >" );	
					sb.append("<SPAN class=black_ar_b>"+ApplicationProperties.getValue(H2COL_LBLS[cnt])+"</SPAN>");
			 	sb.append(TD_CLOSE);
			 	cols = cols+colsp;
			 }
		 }
		 sb.append("<TD colspan="+(colNum - cols)+">&nbsp;</TD>" );
	 	sb.append(TR_CLOSE);
	}

	private String [] get1EleDetAt(int counter, GenericSpecimen specimen, String elementNamePrefix)
	{
		String str[] = new String[2] ;
		if(SpecimenDetailsNewFormat.HDR1_COLS[0].equalsIgnoreCase(columnList.get(counter).toString()))
		{	str[0] = elementNamePrefix+"parentName";	str[1] =getFormattedValue(specimen.getParentName()); }
		else if(SpecimenDetailsNewFormat.HDR1_COLS[1].equalsIgnoreCase(columnList.get(counter).toString()))
		{	str[0] = elementNamePrefix+"displayName";		str[1] =getFormattedValue(specimen.getDisplayName()); }
		else if(SpecimenDetailsNewFormat.HDR1_COLS[2].equalsIgnoreCase(columnList.get(counter).toString()))
		{	str[0] = elementNamePrefix+"barCode";	str[1] =getFormattedValue(specimen.getBarCode()); }
		else if(SpecimenDetailsNewFormat.HDR1_COLS[3].equalsIgnoreCase(columnList.get(counter).toString()))
		{	str[0] = elementNamePrefix+"type";	str[1] =getFormattedValue(specimen.getType()); }
		else if(SpecimenDetailsNewFormat.HDR1_COLS[4].equalsIgnoreCase(columnList.get(counter).toString()))
		{	str[0] = elementNamePrefix+"quantity";	str[1] =getFormattedValue(specimen.getQuantity()); }
		else if(SpecimenDetailsNewFormat.HDR1_COLS[5].equalsIgnoreCase(columnList.get(counter).toString()))
		{	str[0] = elementNamePrefix+"concentration";	str[1] =getFormattedValue(specimen.getConcentration()); }
		else if(SpecimenDetailsNewFormat.HDR1_COLS[6].equalsIgnoreCase(columnList.get(counter).toString()))
		{
			str = new String[10];
			str[0] = elementNamePrefix+"selectedContainerName";	str[1] =getFormattedValue(specimen.getSelectedContainerName());
			str[2] = elementNamePrefix+"positionDimensionOne";	str[3] =getFormattedValue(specimen.getPositionDimensionOne());
			str[4] = elementNamePrefix+"positionDimensionTwo";	str[5] =getFormattedValue(specimen.getPositionDimensionTwo());
			str[6] = elementNamePrefix+"containerId";				str[7] =getFormattedValue(specimen.getContainerId());
			str[8] = elementNamePrefix+"storageContainerForSpecimen";	str[9] =getFormattedValue(specimen.getStorageContainerForSpecimen());

			//if((specimen.getStorageContainerForSpecimen()!= null && specimen.getStorageContainerForSpecimen().equalsIgnoreCase("Virtual")) || (getFormattedValue(specimen.getSelectedContainerName()).trim().length() ==0))
			if((specimen.getStorageContainerForSpecimen()!= null && "Virtual".equalsIgnoreCase(specimen.getStorageContainerForSpecimen())))
			{
				str[1] =getFormattedValue("");
				str[3] =getFormattedValue("");
				str[5] =getFormattedValue("");
				str[7] =getFormattedValue("");
			}
		}
		else if(SpecimenDetailsNewFormat.HDR1_COLS[7].equalsIgnoreCase(columnList.get(counter).toString()))
		{	str[0] = elementNamePrefix+"checkedSpecimen";	str[1] =getFormattedValue(specimen.getCheckedSpecimen()); }
		//bug 11169 start
		else if(SpecimenDetailsNewFormat.HDR1_COLS[8].equalsIgnoreCase(columnList.get(counter).toString()))
		{	str[0] = elementNamePrefix+"printSpecimen";	str[1] =getFormattedValue(specimen.getPrintSpecimen()); }
		//bug 11169 end
		return str;
	}
	private String [] get2EleDetAt(int counter, GenericSpecimen specimen, String elementNamePrefix)
	{
		String str[] = new String[2] ;
		if(counter == 0)
		{	str[0] = elementNamePrefix+"type";	str[1] =getFormattedValue(specimen.getType());  }
		else if(counter == 1)
		{	str[0] = elementNamePrefix+"pathologicalStatus";	str[1] =getFormattedValue(specimen.getPathologicalStatus());  }
		else if(counter == 2)
		{	str[0] = elementNamePrefix+"tissueSide";	str[1] =getFormattedValue(specimen.getTissueSide()); }
		else if(counter == 3)
		{	str[0] = elementNamePrefix+"tissueSite";	str[1] =getFormattedValue(specimen.getTissueSite());  }

		return str;
	}

	private int[] sizes = {8,8,8,8,8,5,8,8,8,8};
	private void createFieldRow(StringBuffer sb, int counter, GenericSpecimen specimen, String elementNamePrefix, boolean isTextRow)
	{
		 int tmpd=0,tmpcwd = 0;
		 
		 if(dataListType.equalsIgnoreCase(dataListTypes[2]))
		 {
			 tmpd = 3;
		 }
		 tmpcwd = cWd;

		sb.append(TR_OPEN);
			createParentComponent(sb, specimen,elementNamePrefix,isTextRow);
			for(int columnCounter=1; columnCounter<columnList.size(); columnCounter++)
			{
				if(columnCounter==5)
				{cWd = cWd-tmpd;}
				else
				{ cWd = tmpcwd;}
				
				String [] nameValue = get1EleDetAt(columnCounter, specimen, elementNamePrefix);
			 	// to be displayed only in case of aliquots.	|| 		
			 	if((columnCounter == 3 && !dataListType.equalsIgnoreCase(dataListTypes[1])) ||
			 		(columnCounter == 5 && !dataListType.equalsIgnoreCase(dataListTypes[2]))) // to be displayed only in case of derived.
			 	{
			 		continue;	// should be passed to hidden elements
			 	}
			 	else if((columnCounter ==1 && !(specimenSummaryForm.getShowLabel() && specimen.getShowLabel())) || 
			 			(columnCounter ==2 && !(specimenSummaryForm.getShowbarCode() && specimen.getShowBarcode())))
			 	{
			 		sb.append(TD_1HLF+"1"+TD_2HLF);
			 		sb.append(SPACE);
			 	}
			 	else if(columnCounter == 6)
			 	{
			 		
			 		if(isTextRow)
			 		{
			 			sb.append("<TD colspan=4 width="+(60-pWd+tmpd)+"% class='"+STYLE_CLASS+"'>");//bug 11169
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
			 			sb.append("<TD colspan=4 width="+(50-pWd+tmpd)+"% class='"+STYLE_CLASS+"'>");//bug 11169
			 			if(dataListTypes[0].equalsIgnoreCase(dataListType) && (specimenSummaryForm.isMultipleSpEditMode() && !specimenSummaryForm.getShowParentStorage() ))
			 			{
							if(nameValue[1].trim().length()>0)
							{
								sb.append(nameValue[1]);sb.append(":");sb.append(nameValue[3]);sb.append(",");sb.append(nameValue[5]);
							}
							else
							{
								sb.append(getHTMLFormattedValue(specimen.getStorageContainerForSpecimen()));
							}
							for(int ind1=0;ind1<9;ind1+=2)
							{
								String [] tmpAr = {nameValue[ind1],nameValue[ind1+1]};
								createHiddenElement(sb, tmpAr);
							}
		 			}
				 		else
				 		{
				 			createNewStorageComponent(sb,nameValue,specimen);
				 		}
			 		}
			 	}
			 	else if(columnCounter == 7)
			 	{
			 		sb.append(TD_1HLF+3+TD_2HLF);//bug 11169
					if(isParentList && specimenSummaryForm.getSelectedSpecimenId().equals(specimen.getUniqueIdentifier()))
					{
						functionCall = "onclick=\"onClickCollected(this)\"";
					}
					else{functionCall = "";}
			 		createCollectedComponent(sb,nameValue,isTextRow);
			 		//addRemainingSpecimenElements(sb,elementNamePrefix,specimen, isTextRow);
			 	}
			 	//bug 11169 start
			 	else if(columnCounter == 8)
			 	{
			 		sb.append(TD_1HLF+3+TD_2HLF);
					functionCall = "";
			 		createPrintComponent(sb,nameValue);
			 		addRemainingSpecimenElements(sb,elementNamePrefix,specimen, isTextRow);
			 	}
			 	//bug 11169 end
			 	else
			 	{
			 		if(isTextRow && columnCounter == 1)
			 		{
			 		  sb.append(TD_1HLF+5+TD_2HLF);
			 		}
			 		else
			 		{
			 		  sb.append(TD_1HLF+cWd+TD_2HLF);
			 		}
			 		if(isTextRow || columnCounter == 3)
			 		{
			 			sb.append("<SPAN class="+STYLE_CLASS+">"+getHTMLFormattedValue(nameValue[1])+"</SPAN>");
			 		}
			 		else
			 		{	
			 			createTextComponent(sb,nameValue,STYLE_CLASS,sizes[columnCounter]);	
			 		}	
			 	}
			 	sb.append(TD_CLOSE);
			}
		sb.append(TR_CLOSE);
	}
	
	private void createNewStorageComponent(StringBuffer sb, String[] nameValue, GenericSpecimen specimen)
	{

//		 sb.append("<td class=\"black_ar_md\" >");

		 final String specimenId = getFormattedValue(specimen.getUniqueIdentifier());
		 final String specimenClass = getFormattedValue(specimen.getClassName());
		 final Long collectionProtocolId = specimen.getCollectionProtocolId();
		 
		  final String containerId = "containerId_"+specimenId;
		  final String selectedContainerName = "selectedContainerName_"+specimenId;
		  final String positionDimensionOne = "positionDimensionOne_"+specimenId;
		  final String positionDimensionTwo = "positionDimensionTwo_"+specimenId;
		  final String specimenClassName = (String)specimenClass;
		  final String cpId = getFormattedValue(collectionProtocolId);
		  final String functionCall="showMap('" + selectedContainerName + "','"+
										positionDimensionOne +"','"
										+ positionDimensionTwo +"','"
										+containerId +"','"+
										specimenClassName +"','"+
										cpId +"')" ;
		  int scSize=17 +xtra;
		  final String sid = specimen.getUniqueIdentifier();
		  String isDisabled = ""; 
		  
			 sb.append("<table style=\"font-size:1em\" size=\"100%\">");
			 	sb.append(TR_OPEN);
			 		sb.append(TD_OPEN);
			 			sb.append("");

			 			sb.append("<select name=\""+nameValue[8]+"\" size=\"1\" onchange=\"scForSpecimen(this,'"+ sid +"','"+specimenClassName+"')\" class=\"black_new_md\" id=\""+nameValue[8]+"\">");

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
			 		sb.append(TD_CLOSE);

			 		sb.append(TD_OPEN);
			 			sb.append("<input type=\"text\" name=\""+nameValue[0]+"\" value=\"" + nameValue[1]+"\" size=\""+scSize+"\" class=\"black_ar_md\" onmouseover=\" showTip(this.id)\"  id=\""+selectedContainerName+"\" "+ isDisabled +" >");
			 		sb.append(TD_CLOSE);
			 		sb.append(TD_OPEN);
			 			sb.append("<input type=\"text\" name=\""+nameValue[2]+"\" value=\"" + nameValue[3]+"\" size=\"2\" class=\"black_ar_md\" id=\""+positionDimensionOne+"\" "+ isDisabled +" >");
			 		sb.append(TD_CLOSE);
			 		sb.append(TD_OPEN);
			 			sb.append("<input type=\"text\" name=\""+nameValue[4]+"\" value=\"" + nameValue[5]+"\" size=\"2\" class=\"black_ar_md\" id=\""+positionDimensionTwo+"\" "+ isDisabled +" >");
			 		sb.append(TD_CLOSE);
			 		sb.append(TD_OPEN);
			 			sb.append("<a href=\"#\" onclick=\""+functionCall+"\">");
			 			sb.append("<img src=\"images/Tree.gif\" border=\"0\" width=\"13\" height=\"15\" title=\'View storage locations\'>");
			 			sb.append("</a>");
			 			sb.append("<input type=\"hidden\" name=\""+nameValue[6]+"\" value=\""+nameValue[7]+"\" id=\""+containerId+"\">");
			 		sb.append(TD_CLOSE);
			 	sb.append(TR_CLOSE);										
			 sb.append("</table>");
	}
	
	private void createPrintComponent(StringBuffer sb, String[] nameValue)
	{
		if(specimenSummaryForm.getShowCheckBoxes())
		 {
			 sb.append("<center><input type=\"checkbox\" name=\""+nameValue[0]+"\" value=\"true\" checked=\"checked\"></center>");
    	 }
		 else
		 {
			createHiddenElement(sb,nameValue);
		 }
	}

	private void createCollectedComponent(StringBuffer sb, String[] nameValue,  boolean isTextRow)
	{
//		 sb.append("<td class=\"black_ar_md\" >");
		 if(specimenSummaryForm.getShowCheckBoxes())
		 {
//			 if("getTextElement".equalsIgnoreCase(calledFrom))
			 if(isTextRow)
			 {
				 sb.append("<center><input type=\"checkbox\" name=\""+nameValue[0]+"\" value=\"true\" disabled=\"true\" checked=\"checked\"></center>");
			 }
			 else
			 {
				 if(Constants.TRUE.equalsIgnoreCase(nameValue[1]))
				 {	 sb.append("<center><input type=\"checkbox\" name=\""+nameValue[0]+"\" value=\"on\" checked=\"checked\" "+functionCall+"></center>"); }
				 else
				 { sb.append("<center><input type=\"checkbox\" name=\""+nameValue[0]+"\" value=\"on\" "+functionCall+"></center>"); }
			}
		 }
		 else
		 {
			 //sb.append(getHTMLFormattedValue(""));
			 createHiddenElement( sb,  nameValue);
		 }
//			 sb.append(TD_CLOSE);
	}
	
	// Mandar : 4Dec08
	private void createTextFieldRow(StringBuffer sb, int counter, GenericSpecimen specimen, String elementNamePrefix)
	{
		
		String colspan="";
		int cols=0;
		int colsp=1;
		if(dataListType.equalsIgnoreCase(dataListTypes[2]))
		{
			colspan="colspan=2";
			colsp=2;
		}
		sb.append(TR_OPEN);
		for(int cnt=0; cnt < H2COL_LBLS.length; cnt++ )
		{
			String nameValue[] = get2EleDetAt( cnt,  specimen,  elementNamePrefix);

			if(dataListType.equalsIgnoreCase(dataListTypes[2]) && cnt > 1)
			{
				sb.append("<TD colspan=2 class='black_ar'>&nbsp;</TD>" );
				cols = cols +2;
			}
			else if(cnt ==1)
			{
				sb.append("<TD colspan=2 class='black_ar'>");
					sb.append(nameValue[1]);
				sb.append(TD_CLOSE);
				cols = cols +2;
			}
			else
			{
				sb.append("<TD "+ colspan + " class='black_ar'>" );	
					sb.append(nameValue[1]);
			 	sb.append(TD_CLOSE);
			 	cols = cols+colsp;
			}
		 }
		sb.append("<TD colspan="+(colNum - cols)+" class='black_ar'>&nbsp;");
//			addRemainingSpecimenElements(sb,elementNamePrefix,specimen);
		sb.append(TD_CLOSE);
	 	sb.append(TR_CLOSE);
		
	}
	private void addRemainingSpecimenElements(StringBuffer sb, String elementNamePrefix, GenericSpecimen specimen, boolean isTextRow)
	{
		String nameValue[][] = getRemainingSpecimenElementsData(specimen, elementNamePrefix);
		for(int i =0; i<nameValue.length; i++)
		{ sb.append("<input type=\"hidden\" name=\""+nameValue[i][0]+"\" value=\""+nameValue[i][1]+"\" id=\""+nameValue[i][0]+"\">"); }
		
		if(isTextRow)
		{
			String nV[] = get1EleDetAt(1,specimen,elementNamePrefix);
			sb.append("<input type=\"hidden\" name=\""+nV[0]+"\" value=\""+nV[1]+"\" id=\""+nV[0]+"\">"); 
	
			nV = get1EleDetAt(2,specimen,elementNamePrefix);
			sb.append("<input type=\"hidden\" name=\""+nV[0]+"\" value=\""+nV[1]+"\" id=\""+nV[0]+"\">"); 

			nV = get1EleDetAt(4,specimen,elementNamePrefix);
			sb.append("<input type=\"hidden\" name=\""+nV[0]+"\" value=\""+nV[1]+"\" id=\""+nV[0]+"\">"); 

			nV = get1EleDetAt(6,specimen,elementNamePrefix);
			sb.append("<input type=\"hidden\" name=\""+nV[0]+"\" value=\""+nV[1]+"\" id=\""+nV[0]+"\">"); 
			sb.append("<input type=\"hidden\" name=\""+nV[2]+"\" value=\""+nV[3]+"\" id=\""+nV[2]+"\">");
			sb.append("<input type=\"hidden\" name=\""+nV[4]+"\" value=\""+nV[5]+"\" id=\""+nV[4]+"\">");
			sb.append("<input type=\"hidden\" name=\""+nV[6]+"\" value=\""+nV[7]+"\" id=\""+nV[6]+"\">");
			sb.append("<input type=\"hidden\" name=\""+nV[8]+"\" value=\""+nV[9]+"\" id=\""+nV[8]+"\">");
			
			nV = get1EleDetAt(7,specimen,elementNamePrefix);
			sb.append("<input type=\"hidden\" name=\""+nV[0]+"\" value=\""+nV[1]+"\" id=\""+nV[0]+"\">"); 
			
			nV = get1EleDetAt(8,specimen,elementNamePrefix);//bug 11169
			sb.append("<input type=\"hidden\" name=\""+nV[0]+"\" value=\""+nV[1]+"\" id=\""+nV[0]+"\">");
		}
	}
	
	private String[][] getRemainingSpecimenElementsData(GenericSpecimen specimen, String elementNamePrefix)
	{
		String str[][] = new String[9][2];
		if(!dataListTypes[2].equals(dataListType))
		{
			str = new String[10][2];
			str[9][0] = elementNamePrefix+"concentration";			str[9][1] =getFormattedValue(specimen.getConcentration());
		}
			
		str[0][0] = elementNamePrefix+"collectionProtocolId";	str[0][1] = getFormattedValue(specimen.getCollectionProtocolId()); 	
		str[1][0] = elementNamePrefix+"readOnly";				str[1][1] = getFormattedValue(specimen.getReadOnly()); 
		str[2][0] = elementNamePrefix+"uniqueIdentifier";		str[2][1] = getFormattedValue(specimen.getUniqueIdentifier()); 
		str[3][0] = elementNamePrefix+"id";						str[3][1] = getFormattedValue(specimen.getId());
		
		str[4][0] = elementNamePrefix+"type";					str[4][1] =getFormattedValue(specimen.getType());  
		str[5][0] = elementNamePrefix+"pathologicalStatus";		str[5][1] =getFormattedValue(specimen.getPathologicalStatus()); 
		str[6][0] = elementNamePrefix+"tissueSide";				str[6][1] =getFormattedValue(specimen.getTissueSide());
		str[7][0] = elementNamePrefix+"tissueSite";				str[7][1] =getFormattedValue(specimen.getTissueSite());
		str[8][0] = elementNamePrefix+"className";				str[8][1] =getFormattedValue(specimen.getClassName());
		
		if(!dataListTypes[2].equals(dataListType))
		{
			str[9][0] = elementNamePrefix+"concentration";			str[9][1] =getFormattedValue(specimen.getConcentration());
		}

		return str;
	}

	private void createHiddenElement(StringBuffer sb, String[] nameValue)
	{
//		 sb.append("<input type=\"hidden\" name=\""+nameValue[0]+"\" value=\""+nameValue[1]+"\">");
		 sb.append("<input type=\"hidden\" name=\""+nameValue[0]+"\" value=\""+nameValue[1]+"\" id=\""+nameValue[0]+"\">");
	}

	 // ------------ Mandar : 2Dec08 New Code for new formatUI end----------------
	
		

}

