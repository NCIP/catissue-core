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
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

// TODO: Auto-generated Javadoc
/**
 * This tag will accept the list of specimens and display them in
 * editable or readonly mode.
 * It accepts following attributes:
 * columnHeaderListName
 * formName
 * dataListName
 * dataListType
 * columnListName
 * isReadOnly
 * displayColumnListName
 *
 * @author mandar_deshmukh
 */
public class SpecimenDetailsNewFormat extends TagSupport
{

	/** The logger. */
	private transient final Logger logger = Logger.getCommonLogger(SpecimenDetailsNewFormat.class);

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1234567890L;
	// data type list used to display different types of data.
	/** The data list types. */
	private transient final String dataListTypes[] = {"Parent", "Aliquot", "Derived"};

	/** The Constant COLUMN_NAMES. */
	public static final String[] COLUMN_NAMES = {"Parent", "Label", "Barcode", "Type", "Quantity",
			"Concentration", "Location", "Collected", "PrintLabel"};

	/** The Constant COLUMN_LABELS. */
	public static final String[] COLUMN_LABELS = {"specimen.label", "specimen.barcode",
			"specimen.subType", "anticipatorySpecimen.Quantity",
			"anticipatorySpecimen.Concentration", "anticipatorySpecimen.Location",
			"anticipatorySpecimen.Collected", "specimen.printLabel"};

	// ----------- Mandar : 2Dec08 for New UI format start -----------------------------
	/** The Constant HDR1_COLS. */
	public static final String[] HDR1_COLS = {"Parent", "Label", "Barcode", "Type", "Quantity",
			"Concentration", "Location", "Collected", "PrintLabel"};

	/** The Constant HDR2_COLS. */
	public static final String[] HDR2_COLS = {"Type", "Pathological Status", "Tissue Side",
			"Tissue Site"};

	/** The Constant H1COL_LBLS. */
	public static final String[] H1COL_LBLS = {"specimen.label", "specimen.barcode",
			"specimen.subType", "anticipatorySpecimen.Quantity",
			"anticipatorySpecimen.Concentration", "anticipatorySpecimen.Location",
			"anticipatorySpecimen.Collected", "specimen.printLabel"};

	/** The Constant H2COL_LBLS. */
	public static final String[] H2COL_LBLS = {"specimen.subType", "specimen.pathologicalStatus",
			"specimen.tissueSide", "specimen.tissueSite"};

	// ----------- Mandar : 2Dec08 for New UI format end -------------------------------

	/** The Constant TR_OPEN. */
	private static transient final String TR_OPEN = "<TR>";

	/** The Constant TR_CLOSE. */
	private static transient final String TR_CLOSE = "</TR>";

	/** The Constant TD_OPEN. */
	private static transient final String TD_OPEN = "<TD>";

	/** The Constant TD_CLOSE. */
	private static transient final String TD_CLOSE = "</TD>";

	/** The Constant STYLE_CLASS. */
	private static transient final String STYLE_CLASS = "black_ar";

	/** The Constant SPACE. */
	private static transient final String SPACE = "&nbsp;";

	/** The Constant TR_GRAY. */
	private static transient final String TR_GRAY = "<TR class='tr_anti_bg_gray'>";

	/** The Constant TR_BLUE. */
	private static transient final String TR_BLUE = "<TR class='tr_anti_bg_blue'>";

	/** The Constant TD_1HLF. */
	private static transient final String TD_1HLF = "<TD width='";

	/** The Constant TD_2HLF. */
	private static transient final String TD_2HLF = "%'>";

	//--------------- TAG Attribute Section start [Will be provided by the user of the tag thru the TAG.]------------
	/** The display column list name. */
	private String displayColumnListName = "";

	/** The column header list name. */
	private String columnHeaderListName = "";

	/** The data list name. */
	private String dataListName = "";

	/** The is read only. */
	private String isReadOnly = ""; // ------- as decided

	/** The form name. */
	private String formName = "";

	/** The data list type. */
	private String dataListType = "";

	/** The column list name. */
	private String columnListName = "";
	//--------------- TAG Attribute Section end ------------

	//---------------  Attribute Section start ------------
	/** The column list. */
	private transient List columnList = null; // List containing column names

	/** The column header list. */
	private transient List columnHeaderList = null;

	/** The display column list. */
	private transient List displayColumnList = null; // List of columns to show. If name not present hide the column.

	/** The data list. */
	private transient List dataList = null;

	/** The show parent id. */
	private transient boolean showParentId = false;

	/** The element prefix part1. */
	private String elementPrefixPart1 = "";

	/** The function call. */
	private transient String functionCall = "";

	/** The xtra. */
	private transient int xtra = 0;

	/** The col num. */
	private transient int colNum = 0;

	/** The is parent list. */
	private transient boolean isParentList = false;

	/** The p wd. */
	private transient int pWd = 10;

	/** The c wd. */
	private transient int cWd = 10;

	/** The specimen summary form. */
	SpecimenDetailsInfo specimenSummaryForm = null;

	/** The generate label. */
	private boolean generateLabel;

	/** The display name. */
	private String displayName;

	/** The parent name. */
	private String parentName;
	//	--------------- Attribute Section end ------------



	/**
	 * Gets the display name.
	 *
	 * @return the display name
	 */
	public String getDisplayName()
	{
		return displayName;
	}



	/**
	 * Sets the display name.
	 *
	 * @param displayName the new display name
	 */
	public void setDisplayName(String displayName)
	{
		this.displayName = displayName;
	}


	/**
	 * Checks if is generate label.
	 *
	 * @return true, if is generate label
	 */
	public boolean isGenerateLabel()
	{
		return generateLabel;
	}


	/**
	 * Sets the generate label.
	 *
	 * @param generateLabel the new generate label
	 */
	public void setGenerateLabel(boolean generateLabel)
	{
		this.generateLabel = generateLabel;
	}

	// ------------------Getter - Setter Section start ---------
	/**
	 * Gets the column header list name.
	 *
	 * @return the column header list name
	 */
	public String getColumnHeaderListName()
	{
		return this.columnHeaderListName;
	}

	/**
	 * Sets the column header list name.
	 *
	 * @param columnHeaderListName the new column header list name
	 */
	public void setColumnHeaderListName(String columnHeaderListName)
	{
		this.columnHeaderListName = columnHeaderListName;
	}

	/**
	 * Gets the data list name.
	 *
	 * @return the data list name
	 */
	public String getDataListName()
	{
		return this.dataListName;
	}

	/**
	 * Sets the data list name.
	 *
	 * @param dataListName the new data list name
	 */
	public void setDataListName(String dataListName)
	{
		this.dataListName = dataListName;
	}

	/**
	 * Gets the display column list name.
	 *
	 * @return the display column list name
	 */
	public String getDisplayColumnListName()
	{
		return this.displayColumnListName;
	}

	/**
	 * Sets the display column list name.
	 *
	 * @param displayColumnListName the new display column list name
	 */
	public void setDisplayColumnListName(String displayColumnListName)
	{
		this.displayColumnListName = displayColumnListName;
	}

	/**
	 * Gets the checks if is read only.
	 *
	 * @return the checks if is read only
	 */
	public String getIsReadOnly()
	{
		return this.isReadOnly;
	}

	/**
	 * Sets the checks if is read only.
	 *
	 * @param isReadOnly the new checks if is read only
	 */
	public void setIsReadOnly(String isReadOnly)
	{
		this.isReadOnly = isReadOnly;
	}

	/**
	 * Gets the form name.
	 *
	 * @return the form name
	 */
	public String getFormName()
	{
		return this.formName;
	}

	/**
	 * Sets the form name.
	 *
	 * @param formName the new form name
	 */
	public void setFormName(String formName)
	{
		this.formName = formName;
	}

	/**
	 * Gets the data list type.
	 *
	 * @return the data list type
	 */
	public String getDataListType()
	{
		return this.dataListType;
	}

	/**
	 * Sets the data list type.
	 *
	 * @param dataListType the new data list type
	 */
	public void setDataListType(String dataListType)
	{
		this.dataListType = dataListType;
	}

	/**
	 * Gets the column list name.
	 *
	 * @return the column list name
	 */
	public String getColumnListName()
	{
		return this.columnListName;
	}

	/**
	 * Sets the column list name.
	 *
	 * @param columnListName the new column list name
	 */
	public void setColumnListName(String columnListName)
	{
		this.columnListName = columnListName;
	}

	// ------------------Getter - Setter Section end ---------

	/**
	 * A call back function, which gets executed by JSP runtime when opening tag for this
	 * custom tag is encountered.
	 *
	 * @return the int
	 *
	 * @throws JspException the jsp exception
	 */
	public int doStartTag() throws JspException
	{
		try
		{


			final JspWriter out = this.pageContext.getOut();

			out.print("");
			if (this.validateTagAttributes())
			{
				this.initialiseElements();
				if (this.validateLists())
				{
					//					out.print(generateHeaderForDisplay());
					out.print(this.generateRowOutput());
				}
				else
				{
					out
							.print("<b>Column header list is not matching with the display column list.</b>");
				}
			}
			else
			{
				out
						.print("<b>Some of the attributes of the tag are missing or are not proper.</b>");
			}
		}
		catch (final IOException ioe)
		{
			this.logger.error(ioe.getMessage(), ioe);
			ioe.printStackTrace();
			throw new JspTagException("Error:IOException while writing to the user");
		}
		return SKIP_BODY;
	}

	/**
	 * A call back function.
	 *
	 * @return the int
	 *
	 * @throws JspException the jsp exception
	 */
	public int doEndTag() throws JspException
	{
		this.displayColumnListName = "";
		this.columnHeaderListName = "";
		this.dataListName = "";
		this.isReadOnly = "";
		this.formName = "";
		this.dataListType = "";
		this.columnList = null;
		this.columnHeaderList = null;
		this.displayColumnList = null;
		this.dataList = null;
		this.showParentId = false;
		this.elementPrefixPart1 = "";
		this.specimenSummaryForm = null;
		this.functionCall = "";
		this.xtra = 0;
		this.colNum = 0;
		this.pWd = 10;
		this.cWd = 10;

		return EVAL_PAGE;
	}

	/* method to validate the given values for the attributes.
	 * Returns true if all required attributes are in proper valid format. Otherwise returns false.
	 */
	/**
	 * Validate tag attributes.
	 *
	 * @return true, if successful
	 */
	private boolean validateTagAttributes()
	{
		boolean result = true;
		final ServletRequest request = this.pageContext.getRequest();
		ActionErrors errors = (ActionErrors) request.getAttribute(Globals.ERROR_KEY);
		if (errors == null)
		{
			errors = new ActionErrors();
		}
		if (this.columnHeaderListName == null || this.columnHeaderListName.trim().length() < 1)
		{
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
					"Column Header List Name is null or empty"));
			result = false;
		}
		if (this.isReadOnly == null || this.isReadOnly.trim().length() < 1)
		{
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
					"DisplayOnly parameter is null or empty"));
			result = false;
		}
		else if (!Constants.TRUE.equalsIgnoreCase(this.isReadOnly)
				&& (this.formName == null || this.formName.trim().length() < 1))
		{
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("Form name is null or empty"));
			result = false;
		}
		if (this.dataListName == null || this.dataListName.trim().length() < 1)
		{
			errors.add(ActionErrors.GLOBAL_ERROR,
					new ActionError("Data List Name is null or empty"));
			result = false;
		}
		if (this.dataListType == null || this.dataListType.trim().length() < 1)
		{
			errors.add(ActionErrors.GLOBAL_ERROR,
					new ActionError("Data List Type is null or empty"));
			result = false;
		}
		else if (!this.isListDataTypeOK())
		{
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("Data List Type is invalid"));
			result = false;
		}
		if (this.displayColumnListName == null || this.displayColumnListName.trim().length() < 1)
		{
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
					"Display Column List Name is null or empty"));
			result = false;
		}

		request.setAttribute(Globals.ERROR_KEY, errors);
		return result;
	}

	/**
	 * Initialise elements.
	 */
	private void initialiseElements()
	{
		final ServletRequest request = this.pageContext.getRequest();

		this.specimenSummaryForm = (SpecimenDetailsInfo) request.getAttribute(this.formName);
		//		columnHeaderList = (List)request.getAttribute(columnHeaderListName);
		this.dataList = (List) request.getAttribute(this.dataListName);
		//		displayColumnList = (List)request.getAttribute(displayColumnListName);
		//		columnList = (List)request.getAttribute(columnListName);
		//		if(columnHeaderList == null || columnHeaderList.isEmpty())
		{
			this.columnHeaderList = new ArrayList();
			if (this.dataListType.equalsIgnoreCase(this.dataListTypes[0]))
			{
				this.columnHeaderList.add("");
			}
			else
			{
				this.columnHeaderList.add("anticipatorySpecimen.Parent");
			}

			for (final String element : SpecimenDetailsNewFormat.COLUMN_LABELS)
			{
				this.columnHeaderList.add(element);
			} // 0

		}

		if (!this.dataListType.equalsIgnoreCase(this.dataListTypes[0]))
		{
			this.showParentId = true;
		}
		//		if(displayColumnList == null || displayColumnList.isEmpty())
		{
			this.displayColumnList = new ArrayList();
			this.setFixedColumnsList(this.displayColumnList);
		}
		//		if(columnList == null || columnList.isEmpty())
		{
			this.columnList = new ArrayList();
			this.setFixedColumnsList(this.columnList);
		}
		final List specimenList = this.getDataList();
		if(specimenList != null && !specimenList.isEmpty())
		{
			final GenericSpecimen specimen = (GenericSpecimen) specimenList.get(0);
//			this.generateLabel=specimen.isGenerateLabel();
//			this.specimenSummaryForm.setGenerateLabel(generateLabel);
		}
	}

	/**
	 * Checks if is list data type ok.
	 *
	 * @return true, if is list data type ok
	 */
	private boolean isListDataTypeOK()
	{
		boolean result = false;
		for (final String dataListType2 : this.dataListTypes)
		{
			if (this.dataListType.equalsIgnoreCase(dataListType2))
			{
				if (this.dataListTypes[0].equalsIgnoreCase(dataListType2))
				{
					this.elementPrefixPart1 = "specimen[";
					this.colNum = 8;
				}
				else
				{
					this.elementPrefixPart1 = dataListType2.toLowerCase().trim() + "[";
					this.colNum = 9;
				}
				result = true;
			}
		}
		return result;
	}

	/**
	 * Gets the data list.
	 *
	 * @return the data list
	 */
	private List getDataList()
	{
		List lst = new ArrayList();
		//setting the data list to be used to display the specimens.
		if (this.dataList == null || this.dataList.isEmpty())
		{
			if (this.specimenSummaryForm != null)
			{
				if (this.dataListType.equalsIgnoreCase(this.dataListTypes[0]))
				{
					lst = this.specimenSummaryForm.getSpecimenList();
				}
				else if (this.dataListType.equalsIgnoreCase(this.dataListTypes[1]))
				{
					lst = this.specimenSummaryForm.getAliquotList();
				}
				else if (this.dataListType.equalsIgnoreCase(this.dataListTypes[2]))
				{
					lst = this.specimenSummaryForm.getDerivedList();
				}
			}
		}
		else
		// get the list from ViewSpecimenSummaryForm based on list type
		{
			lst = this.dataList;
		}
		if (this.dataListType.equalsIgnoreCase(this.dataListTypes[0]))
		{
			this.xtra = 6;
			this.isParentList = true;
			this.pWd = 5;
			this.cWd = 12;
		}
		if (this.dataListType.equalsIgnoreCase(this.dataListTypes[2]))
		{
			this.xtra = 3;
		}

		return lst;
	}

	/**
	 * Gets the formatted value.
	 *
	 * @param obj the obj
	 *
	 * @return the formatted value
	 */
	private String getFormattedValue(Object obj)
	{
		String str = "";
		if (obj == null || obj.toString().trim().length() == 0)
		{
			str = "";
		}
		else
		{
			str = obj.toString();
		}
		return str;
	}

	/**
	 * Gets the hTML formatted value.
	 *
	 * @param obj the obj
	 *
	 * @return the hTML formatted value
	 */
	private String getHTMLFormattedValue(Object obj)
	{
		final String str = this.getFormattedValue(obj);
		return (str.trim().length() > 0 ? str : SPACE);
	}

	/**
	 * Validate lists.
	 *
	 * @return true, if successful
	 */
	private boolean validateLists()
	{
		boolean result = true;
		final ServletRequest request = this.pageContext.getRequest();
		ActionErrors errors = (ActionErrors) request.getAttribute(Globals.ERROR_KEY);
		if (errors == null)
		{
			errors = new ActionErrors();
		}
		if (this.columnHeaderList.size() != this.displayColumnList.size())
		{
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
					"Column Header List is not matching the Display Column List."));
			result = false;
		}
		request.setAttribute(Globals.ERROR_KEY, errors);
		return result;
	}

	/**
	 * Sets the fixed columns list.
	 *
	 * @param list the new fixed columns list
	 */
	private void setFixedColumnsList(List list)
	{
		if (list == null)
		{
			list = new ArrayList();
		}
		for (final String element : SpecimenDetailsNewFormat.COLUMN_NAMES)
		{
			list.add(element);
		}
	}

	/*
	 * Method to generate row output for generic specimen
	 */
	/**
	 * Generate row output.
	 *
	 * @return the string
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private String generateRowOutput() throws IOException
	{
		final StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("");
		final List specimenList = this.getDataList();
		if (this.dataListTypes[1].equalsIgnoreCase(this.dataListType))
		{
			stringBuffer.append("<TABLE border=0 width='100%'>");
		}
		for (int counter = 0; counter < specimenList.size(); counter++)
		{
			final GenericSpecimen specimen = (GenericSpecimen) specimenList.get(counter);
			displayName = specimen.getDisplayName();
			parentName = specimen.getParentName();
			if(Validator.isEmpty(parentName) && specimen.getParentSpecimen() != null && Validator.isEmpty(specimen.getParentSpecimen().getLabel()))
			{
				parentName = specimen.getParentSpecimen().getLabel();
				specimen.setParentName(parentName);
			}

			this.generateLabel=specimen.isGenerateLabel();
//			this.specimenSummaryForm.setGenerateLabel(generateLabel);
			if (Constants.TRUE.equalsIgnoreCase(this.isReadOnly) || specimen.getReadOnly())
			{
				//				 addReadOnlyRow(sb, counter, specimen);
				this.addEditableRow(stringBuffer, counter, specimen, true);
			}
			else
			{
				this.addEditableRow(stringBuffer, counter, specimen, false);
			}
		} // outer most loop for specimenList
		stringBuffer.append("");
		if (this.dataListTypes[1].equalsIgnoreCase(this.dataListType))
		{
			stringBuffer.append("</TABLE>");
		}

		//String output =sb.toString();
		return stringBuffer.toString();
	}

	/**
	 * Adds the editable row.
	 *
	 * @param stringBuffer the string buffer
	 * @param counter the counter
	 * @param specimen the specimen
	 * @param isTextRow the is text row
	 */
	private void addEditableRow(StringBuffer stringBuffer,
			int counter, GenericSpecimen specimen,
			boolean isTextRow)
	{
		// ------------ Mandar : 2Dec08 New Code for new formatUI start----------------
		if (this.dataListTypes[1].equalsIgnoreCase(this.dataListType)) // for aliquots
		{
			final String elementNamePrefix = this.elementPrefixPart1 + counter + "].";
			if (counter == 0)
			{
				this.createHeaderRow1(stringBuffer, TR_GRAY, specimen); // row1 containing headers for first half (editable fields)
			}
			this.createFieldRow(stringBuffer, counter, specimen, elementNamePrefix, isTextRow); // row2 containing actual editable fields (first half)
		}
		else
		{
			final String elementNamePrefix = this.elementPrefixPart1 + counter + "].";
			if ((counter + 1) % 2 == 0)
			{
				stringBuffer.append(TR_BLUE);
			}
			else
			{
				stringBuffer.append(TR_GRAY);
			}

			stringBuffer.append(TD_OPEN);
			stringBuffer.append("<TABLE border=0 width='100%'>");
			this.createHeaderRow1(stringBuffer, TR_OPEN, specimen); // row1 containing headers for first half (editable fields)
			this.createFieldRow(stringBuffer, counter, specimen, elementNamePrefix, isTextRow); // row2 containing actual editable fields (first half)
			this.createHeaderRow2(stringBuffer); // row3 containing headers for second half (text fields)
			this.createTextFieldRow(stringBuffer, counter, specimen, elementNamePrefix); // row containing text data (second half)

			stringBuffer.append("</TABLE>");
			stringBuffer.append(TD_CLOSE);
			stringBuffer.append(TR_CLOSE);
		}

		// ------------ Mandar : 2Dec08 New Code for new formatUI end----------------

	}

	// ------------ Mandar : 2Dec08 New Code for new formatUI start----------------
	/**
	 * Creates the parent component.
	 *
	 * @param stringBuffer the string buffer
	 * @param specimen the specimen
	 * @param elementNamePrefix the element name prefix
	 * @param isTextRow the is text row
	 */
	private void createParentComponent(StringBuffer stringBuffer,
			GenericSpecimen specimen,
			String elementNamePrefix, boolean isTextRow)
	{
		final String nameValue[] = this.get1EleDetAt(0, specimen, elementNamePrefix);
		if (this.showParentId)
		{
			stringBuffer.append(TD_1HLF + this.pWd + TD_2HLF);
			if (isTextRow)
			{
				stringBuffer.append("<SPAN class=" + STYLE_CLASS + ">" + nameValue[1] + "</SPAN>");
			}
			else
			{
				this.createTextComponent(stringBuffer, nameValue, STYLE_CLASS, 8);
			}
		}
		else
		{
			stringBuffer.append("<TD rowspan=3 width='" + this.pWd + "%'>");
			nameValue[1] = this.getFormattedValue(specimen.getUniqueIdentifier());
			this.createParentRadioComponent(stringBuffer, nameValue);
		}
		stringBuffer.append(TD_CLOSE);
	}

	/**
	 * Creates the parent radio component.
	 *
	 * @param stringBufffer the string bufffer
	 * @param nameValue the name value
	 */
	private void createParentRadioComponent(StringBuffer stringBufffer,
			String[] nameValue)
	{
		//		 sb.append("<td class=\"black_ar_md\" >");
		if (this.specimenSummaryForm.getSelectedSpecimenId().equalsIgnoreCase(nameValue[1]))
		{
			stringBufffer.append("<input type=\"radio\" name=\"selectedSpecimenId\" value=\"" + nameValue[1]
					+ "\" checked=\"checked\" onclick=\"onParentRadioBtnClick()\">");
		}
		else
		{
			stringBufffer.append("<input type=\"radio\" name=\"selectedSpecimenId\" value=\"" + nameValue[1]
					+ "\" onclick=\"onParentRadioBtnClick()\">");
		}
		//		 sb.append(TD_CLOSE);
	}

	/**
	 * Creates the text component.
	 *
	 * @param stringBuffer the string buffer
	 * @param nameValue the name value
	 * @param styleClass the style class
	 * @param size the size
	 */
	private void createTextComponent(StringBuffer stringBuffer,
			String[] nameValue, String styleClass,
			int size)
	{
		//		 sb.append("<td class=\"black_ar_md\" >");
		/*stringBuffer.append("<input type=\"text\" name=\"" + nameValue[0] + "\" value=\"" + nameValue[1]
				+ "\" class=\"" + styleClass + "\" size=\"" + size + "\">");*/
//		if(!edu.wustl.catissuecore.util.global.Variables.isSpecimenLabelGeneratorAvl
		if ((nameValue[0].contains("parentName")) )
		{
			stringBuffer.append("<input type=\"text\" name=\"" + nameValue[0] + "\" value=\"" + nameValue[1]
  				+ "\" class=\"" + styleClass + "\" size=\"" + size + "\" disabled=\"disabled\""+"\">");
		}
		else
		{
			if(this.generateLabel && Validator.isEmpty(this.displayName) && nameValue[0].contains("displayName"))
			{
				stringBuffer.append("<span class='black_ar'>AutoGenerated</span>");
			}
			else if(this.generateLabel && !Validator.isEmpty(this.displayName) && nameValue[0].contains("displayName") && nameValue[1].equals("AutoGenerated"))
			{
				stringBuffer.append("<span class='black_ar'>AutoGenerated</span>");
			}
			else if(this.generateLabel && Validator.isEmpty(this.parentName) && nameValue[0].contains("parentName"))
			{

			}
			else if(this.generateLabel && nameValue[0].contains("parentName") && !Validator.isEmpty(nameValue[1]) && nameValue[1].equals("AutoGenerated"))
			{

			}
			else
			{
				stringBuffer.append("<input type=\"text\" name=\"" + nameValue[0] + "\" value=\"" + nameValue[1]
				+ "\" class=\"" + styleClass + "\" size=\"" + size + "\">");
			}
		}

	}

	/**
	 * Creates the header row1.
	 *
	 * @param stringBuffer the string buffer
	 * @param string the string
	 * @param specimen the specimen
	 */
	private void createHeaderRow1(StringBuffer stringBuffer,
			String string, GenericSpecimen specimen)
	{
		stringBuffer.append(string);
		int tmpd = 0, tmpcwd = 0;
		if (this.dataListType.equalsIgnoreCase(this.dataListTypes[2]))
		{
			tmpd = 3;
		}
		tmpcwd = this.cWd;
		for (int cnt = 0; cnt < this.columnHeaderList.size(); cnt++)
		{
			if (cnt == 5)//Concentration
			{
				this.cWd = this.cWd - tmpd;
			}
			else
			{
				this.cWd = tmpcwd;
			}
			if ((((String) this.columnHeaderList.get(cnt)).trim().length() > 0))
			{
				// to be displayed only in case of aliquots.	||
				if ((cnt == 3 && !this.dataListType.equalsIgnoreCase(this.dataListTypes[1]))
						|| (cnt == 5 && !this.dataListType.equalsIgnoreCase(this.dataListTypes[2]))) // to be displayed only in case of derived.
				{
					continue;
				}
				else if ((cnt == 1 && !(this.specimenSummaryForm.getShowLabel() && specimen
						.getShowLabel()))
						|| (cnt == 2 && !(this.specimenSummaryForm.getShowbarCode() && specimen
								.getShowBarcode())))
				{
					stringBuffer.append(TD_1HLF + "1" + TD_2HLF);
					stringBuffer.append(SPACE);
				}
				else if (cnt == 6)//Location
				{
					stringBuffer.append("<TD colspan=4 width=" + (50 - this.pWd + tmpd) + "%>");// 3 35
					stringBuffer.append("<SPAN class=black_ar_b>"
							+ ApplicationProperties.getValue((String) this.columnHeaderList.get(6))
							+ "</SPAN>");
				}
				else if (cnt == 7 && !this.specimenSummaryForm.getShowCheckBoxes())//Collected
				{
					stringBuffer.append(TD_1HLF + 3 + TD_2HLF);//bug 11169
					stringBuffer.append(SPACE);
				}
				else if (cnt == 8)//print bug 11169
				{
					stringBuffer.append(TD_1HLF + 3 + TD_2HLF);
					stringBuffer.append("<center><SPAN class=black_ar_b>"
							+ ApplicationProperties.getValue((String) this.columnHeaderList.get(8))
							+ "</SPAN></center>");
				}
				else
				{
					//bug 11169 start
					if (cnt == 7)
					{
						stringBuffer.append(TD_1HLF + 3 + TD_2HLF);
						stringBuffer.append("<center><SPAN class=black_ar_b>"
								+ ApplicationProperties.getValue((String) this.columnHeaderList
										.get(cnt)) + "</SPAN></center>");
					}
					else
					{
//						if(Validator.isEmpty(this.displayName) && this.generateLabel && this.columnHeaderList
//								.get(cnt).equals("specimen.label"))
//						{
//							stringBuffer.append(TD_1HLF + this.cWd + TD_2HLF);
//						}
//						else if(!Validator.isEmpty(this.displayName) && this.displayName.equals("AutoGenerated") && this.generateLabel && this.columnHeaderList
//								.get(cnt).equals("specimen.label"))
//						{
//							stringBuffer.append(TD_1HLF + this.cWd + TD_2HLF);
//						}
						if(cnt == 0 && !Validator.isEmpty(this.parentName) && this.parentName.equals("AutoGenerated") && this.generateLabel && this.columnHeaderList
								.get(cnt).equals("anticipatorySpecimen.Parent"))
						{
							stringBuffer.append(TD_1HLF + this.cWd + TD_2HLF);
						}
						else if(cnt == 0 && this.parentName != null && this.parentName.equals("") && this.generateLabel && this.columnHeaderList
								.get(cnt).equals("anticipatorySpecimen.Parent"))
						{
							stringBuffer.append(TD_1HLF + this.cWd + TD_2HLF);
						}
//						else if(cnt == 0 && Validator.isEmpty(this.parentName) && this.generateLabel && this.columnHeaderList
//								.get(cnt).equals("anticipatorySpecimen.Parent"))
//						{
//							stringBuffer.append(TD_1HLF + this.cWd + TD_2HLF);
//						}
						else
						{
						stringBuffer.append(TD_1HLF + this.cWd + TD_2HLF);
						stringBuffer.append("<SPAN class=black_ar_b>"
								+ ApplicationProperties.getValue((String) this.columnHeaderList
										.get(cnt)) + "</SPAN>");
						}
					}
					//bug 11169 end
				}

			}
			else
			{
				stringBuffer.append(TD_1HLF + this.pWd + TD_2HLF);
				stringBuffer.append(SPACE);
			}
			stringBuffer.append(TD_CLOSE);
		}
		stringBuffer.append(TR_CLOSE);
	}

	/**
	 * Creates the header row2.
	 *
	 * @param stringBuffer the string buffer
	 */
	private void createHeaderRow2(StringBuffer stringBuffer)
	{
		String colspan = "";
		int cols = 0;
		int colsp = 1;
		if (this.dataListType.equalsIgnoreCase(this.dataListTypes[2]))
		{
			colspan = "colspan=2";
			colsp = 2;
		}
		stringBuffer.append(TR_OPEN);
		for (int cnt = 0; cnt < H2COL_LBLS.length; cnt++)
		{
			if (this.dataListType.equalsIgnoreCase(this.dataListTypes[2]) && cnt > 1)
			{
				stringBuffer.append("<TD colspan=2 width=22%>&nbsp;</TD>");
				cols = cols + 2;
			}
			else if (cnt == 1)
			{
				stringBuffer.append("<TD colspan=2 width=22%>");
				stringBuffer.append("<SPAN class=black_ar_b>"
						+ ApplicationProperties.getValue(H2COL_LBLS[cnt]) + "</SPAN>");
				stringBuffer.append(TD_CLOSE);
				cols = cols + 2;
			}
			else
			{
				stringBuffer.append("<TD " + colspan + " width=" + (colsp * 15) + "% >");
				stringBuffer.append("<SPAN class=black_ar_b>"
						+ ApplicationProperties.getValue(H2COL_LBLS[cnt]) + "</SPAN>");
				stringBuffer.append(TD_CLOSE);
				cols = cols + colsp;
			}
		}
		stringBuffer.append("<TD colspan=" + (this.colNum - cols) + ">&nbsp;</TD>");
		stringBuffer.append(TR_CLOSE);
	}

	/**
	 * Gets the 1 ele det at.
	 *
	 * @param counter the counter
	 * @param specimen the specimen
	 * @param elementNamePrefix the element name prefix
	 *
	 * @return the 1 ele det at
	 */
	private String[] get1EleDetAt(int counter, GenericSpecimen specimen, String elementNamePrefix)
	{
		String str[] = new String[2];
		if (SpecimenDetailsNewFormat.HDR1_COLS[0].equalsIgnoreCase(this.columnList.get(counter)
				.toString()))
		{
			str[0] = elementNamePrefix + "parentName";
			if("AutoGenerated".equals(specimen.getParentName()))
			{
				str[1] = this.getFormattedValue("");
			}
			else
			{
				str[1] = this.getFormattedValue(specimen.getParentName());
			}
		}
		else if (SpecimenDetailsNewFormat.HDR1_COLS[1].equalsIgnoreCase(this.columnList
				.get(counter).toString()))
		{
			str[0] = elementNamePrefix + "displayName";
			str[1] = this.getFormattedValue(specimen.getDisplayName());
		}
		else if (SpecimenDetailsNewFormat.HDR1_COLS[2].equalsIgnoreCase(this.columnList
				.get(counter).toString()))
		{
			str[0] = elementNamePrefix + "barCode";
			str[1] = this.getFormattedValue(specimen.getBarCode());
		}
		else if (SpecimenDetailsNewFormat.HDR1_COLS[3].equalsIgnoreCase(this.columnList
				.get(counter).toString()))
		{
			str[0] = elementNamePrefix + "type";
			str[1] = this.getFormattedValue(specimen.getType());
		}
		else if (SpecimenDetailsNewFormat.HDR1_COLS[4].equalsIgnoreCase(this.columnList
				.get(counter).toString()))
		{
			str[0] = elementNamePrefix + "quantity";
			str[1] = this.getFormattedValue(specimen.getQuantity());
		}
		else if (SpecimenDetailsNewFormat.HDR1_COLS[5].equalsIgnoreCase(this.columnList
				.get(counter).toString()))
		{
			str[0] = elementNamePrefix + "concentration";
			str[1] = this.getFormattedValue(specimen.getConcentration());
		}
		else if (SpecimenDetailsNewFormat.HDR1_COLS[6].equalsIgnoreCase(this.columnList
				.get(counter).toString()))
		{
			str = new String[10];
			str[0] = elementNamePrefix + "selectedContainerName";
			str[1] = this.getFormattedValue(specimen.getSelectedContainerName());
			str[2] = elementNamePrefix + "positionDimensionOne";
			str[3] = this.getFormattedValue(specimen.getPositionDimensionOne());
			str[4] = elementNamePrefix + "positionDimensionTwo";
			str[5] = this.getFormattedValue(specimen.getPositionDimensionTwo());
			str[6] = elementNamePrefix + "containerId";
			str[7] = this.getFormattedValue(specimen.getContainerId());
			str[8] = elementNamePrefix + "storageContainerForSpecimen";
			str[9] = this.getFormattedValue(specimen.getStorageContainerForSpecimen());

			//if((specimen.getStorageContainerForSpecimen()!= null && specimen.getStorageContainerForSpecimen().equalsIgnoreCase("Virtual")) || (getFormattedValue(specimen.getSelectedContainerName()).trim().length() ==0))
			if ((specimen.getStorageContainerForSpecimen() != null && "Virtual"
					.equalsIgnoreCase(specimen.getStorageContainerForSpecimen())))
			{
				str[1] = this.getFormattedValue("");
				str[3] = this.getFormattedValue("");
				str[5] = this.getFormattedValue("");
				str[7] = this.getFormattedValue("");
			}
		}
		else if (SpecimenDetailsNewFormat.HDR1_COLS[7].equalsIgnoreCase(this.columnList
				.get(counter).toString()))
		{
			str[0] = elementNamePrefix + "checkedSpecimen";
			str[1] = this.getFormattedValue(specimen.getCheckedSpecimen());
		}
		//bug 11169 start
		else if (SpecimenDetailsNewFormat.HDR1_COLS[8].equalsIgnoreCase(this.columnList
				.get(counter).toString()))
		{
			str[0] = elementNamePrefix + "printSpecimen";
			str[1] = this.getFormattedValue(specimen.getPrintSpecimen());
		}
		//bug 11169 end
		return str;
	}

	/**
	 * Gets the 2 ele det at.
	 *
	 * @param counter the counter
	 * @param specimen the specimen
	 * @param elementNamePrefix the element name prefix
	 *
	 * @return the 2 ele det at
	 */
	private String[] get2EleDetAt(int counter, GenericSpecimen specimen, String elementNamePrefix)
	{
		final String str[] = new String[2];
		if (counter == 0)
		{
			str[0] = elementNamePrefix + "type";
			str[1] = this.getFormattedValue(specimen.getType());
		}
		else if (counter == 1)
		{
			str[0] = elementNamePrefix + "pathologicalStatus";
			str[1] = this.getFormattedValue(specimen.getPathologicalStatus());
		}
		else if (counter == 2)
		{
			str[0] = elementNamePrefix + "tissueSide";
			str[1] = this.getFormattedValue(specimen.getTissueSide());
		}
		else if (counter == 3)
		{
			str[0] = elementNamePrefix + "tissueSite";
			str[1] = this.getFormattedValue(specimen.getTissueSite());
		}

		return str;
	}

	/** The sizes. */
	private final int[] sizes = {8, 8, 8, 8, 8, 5, 8, 8, 8, 8};

	/**
	 * Creates the field row.
	 *
	 * @param stringBuffer the string buffer
	 * @param counter the counter
	 * @param specimen the specimen
	 * @param elementNamePrefix the element name prefix
	 * @param isTextRow the is text row
	 */
	private void createFieldRow(StringBuffer stringBuffer,
			int counter, GenericSpecimen specimen,
			String elementNamePrefix, boolean isTextRow)
	{
		int tmpd = 0, tmpcwd = 0;

		if (this.dataListType.equalsIgnoreCase(this.dataListTypes[2]))
		{
			tmpd = 3;
		}
		tmpcwd = this.cWd;

		stringBuffer.append(TR_OPEN);
		this.createParentComponent(stringBuffer, specimen, elementNamePrefix, isTextRow);
		for (int columnCounter = 1; columnCounter < this.columnList.size(); columnCounter++)
		{
			if (columnCounter == 5)
			{
				this.cWd = this.cWd - tmpd;
			}
			else
			{
				this.cWd = tmpcwd;
			}

			final String[] nameValue = this
					.get1EleDetAt(columnCounter, specimen, elementNamePrefix);
			// to be displayed only in case of aliquots.	||
			if ((columnCounter == 3 && !this.dataListType.equalsIgnoreCase(this.dataListTypes[1]))
					|| (columnCounter == 5 && !this.dataListType
							.equalsIgnoreCase(this.dataListTypes[2]))) // to be displayed only in case of derived.
			{
				continue; // should be passed to hidden elements
			}
			else if ((columnCounter == 1 && !(this.specimenSummaryForm.getShowLabel() && specimen
					.getShowLabel()))
					|| (columnCounter == 2 && !(this.specimenSummaryForm.getShowbarCode() && specimen
							.getShowBarcode())))
			{
				stringBuffer.append(TD_1HLF + "1" + TD_2HLF);
				stringBuffer.append(SPACE);
			}
			else if (columnCounter == 6)
			{

				if (isTextRow)
				{
					stringBuffer.append("<TD colspan=4 width=" + (60 - this.pWd + tmpd) + "% class='"
							+ STYLE_CLASS + "'>");//bug 11169
					if (nameValue[1].trim().length() > 0)
					{
						stringBuffer.append(nameValue[1]);
						stringBuffer.append(":");
						stringBuffer.append(nameValue[3]);
						stringBuffer.append(",");
						stringBuffer.append(nameValue[5]);
					}
					else
					{
						stringBuffer.append(this.getHTMLFormattedValue(specimen
								.getStorageContainerForSpecimen()));
					}
				}
				else
				{
					stringBuffer.append("<TD colspan=4 width=" + (50 - this.pWd + tmpd) + "% class='"
							+ STYLE_CLASS + "'>");//bug 11169
					if (this.dataListTypes[0].equalsIgnoreCase(this.dataListType)
							&& (this.specimenSummaryForm.isMultipleSpEditMode() && !this.specimenSummaryForm
									.getShowParentStorage()))
					{
						if (nameValue[1].trim().length() > 0)
						{
							stringBuffer.append(nameValue[1]);
							stringBuffer.append(":");
							stringBuffer.append(nameValue[3]);
							stringBuffer.append(",");
							stringBuffer.append(nameValue[5]);
						}
						else
						{
							stringBuffer.append(this.getHTMLFormattedValue(specimen
									.getStorageContainerForSpecimen()));
						}
						for (int ind1 = 0; ind1 < 9; ind1 += 2)
						{
							final String[] tmpAr = {nameValue[ind1], nameValue[ind1 + 1]};
							this.createHiddenElement(stringBuffer, tmpAr);
						}
					}
					else
					{
						this.createNewStorageComponent(stringBuffer, nameValue, specimen);
					}
				}
			}
			else if (columnCounter == 7)
			{
				stringBuffer.append(TD_1HLF + 3 + TD_2HLF);//bug 11169
				this.functionCall = "";
				//commented to fix bug 15702
				/*if (this.isParentList
						&& this.specimenSummaryForm.getSelectedSpecimenId().equals(
								specimen.getUniqueIdentifier()))
				{
					this.functionCall = "onclick=\"onClickCollected(this)\"";
				}
				else
				{
					this.functionCall = "";
				}*/
				this.createCollectedComponent(stringBuffer, nameValue, isTextRow);
				//addRemainingSpecimenElements(sb,elementNamePrefix,specimen, isTextRow);
			}
			//bug 11169 start
			else if (columnCounter == 8)
			{
				stringBuffer.append(TD_1HLF + 3 + TD_2HLF);
				this.functionCall = "";
				this.createPrintComponent(stringBuffer, nameValue);
				this.addRemainingSpecimenElements(stringBuffer, elementNamePrefix, specimen, isTextRow);
			}
			//bug 11169 end
			else
			{
				if (isTextRow && columnCounter == 1)
				{
					stringBuffer.append(TD_1HLF + 5 + TD_2HLF);
				}
				else
				{
					stringBuffer.append(TD_1HLF + this.cWd + TD_2HLF);
				}
				if (isTextRow || columnCounter == 3)
				{
					stringBuffer.append("<SPAN class=" + STYLE_CLASS + ">"
							+ this.getHTMLFormattedValue(nameValue[1]) + "</SPAN>");
				}
				else
				{
					this.createTextComponent(stringBuffer, nameValue, STYLE_CLASS, this.sizes[columnCounter]);
				}
			}
			stringBuffer.append(TD_CLOSE);
		}
		stringBuffer.append(TR_CLOSE);
	}

	/**
	 * Creates the new storage component.
	 *
	 * @param stringBuffer the string buffer
	 * @param nameValue the name value
	 * @param specimen the specimen
	 */
	private void createNewStorageComponent(StringBuffer stringBuffer,
			String[] nameValue, GenericSpecimen specimen)
	{

		//		 sb.append("<td class=\"black_ar_md\" >");

		final String specimenId = this.getFormattedValue(specimen.getUniqueIdentifier());
		final String specimenClass = this.getFormattedValue(specimen.getClassName());
		final String specimenType = this.getFormattedValue(specimen.getType());
		final Long collectionProtocolId = specimen.getCollectionProtocolId();

		final String containerId = "containerId_" + specimenId;
		final String selectedContainerName = "selectedContainerName_" + specimenId;
		final String positionDimensionOne = "positionDimensionOne_" + specimenId;
		final String positionDimensionTwo = "positionDimensionTwo_" + specimenId;
		final String specimenClassName = (String) specimenClass;
		final String specimenTypeName = (String) specimenType;
		final String cpId = this.getFormattedValue(collectionProtocolId);
		final String functionCall = "showMap('" + selectedContainerName + "','"
				+ positionDimensionOne + "','" + positionDimensionTwo + "','" + containerId + "','"
				+ specimenClassName + "','" + specimenTypeName + "','"+ cpId + "')";
		final int scSize = 17 + this.xtra;
		final String sid = specimen.getUniqueIdentifier();
		String isDisabled = "";

		stringBuffer.append("<table style=\"font-size:1em\" size=\"100%\">");
		stringBuffer.append(TR_OPEN);
		stringBuffer.append(TD_OPEN);
		stringBuffer.append("");

		stringBuffer.append("<select name=\"" + nameValue[8]
				+ "\" size=\"1\" onchange=\"scForSpecimen(this,'" + sid + "','" + specimenClassName
				+ "')\" class=\"black_new_md\" id=\"" + nameValue[8] + "\">");

		if ("Virtual".equals(specimen.getStorageContainerForSpecimen()))
		{
			stringBuffer.append("<option value=\"Virtual\" selected=\"selected\">Virtual</option>");
			isDisabled = "disabled='disabled'";
		}
		else
		{
			stringBuffer.append("<option value=\"Virtual\">Virtual</option>");
		}
		if ("Auto".equals(specimen.getStorageContainerForSpecimen()))
		{
			stringBuffer.append("<option value=\"Auto\" selected=\"selected\">Auto</option>");
		}
		else
		{
			stringBuffer.append("<option value=\"Auto\">Auto</option>");
		}
		if ("Manual".equals(specimen.getStorageContainerForSpecimen()))
		{
			stringBuffer.append("<option value=\"Manual\" selected=\"selected\">Manual</option>");
		}
		else
		{
			stringBuffer.append("<option value=\"Manual\">Manual</option>");
		}
		stringBuffer.append("</select>");
		stringBuffer.append(TD_CLOSE);

		stringBuffer.append(TD_OPEN);
		stringBuffer.append("<input type=\"text\" name=\"" + nameValue[0] + "\" value=\"" + nameValue[1]
				+ "\" size=\"" + scSize
				+ "\" class=\"black_ar_md\" onmouseover=\" showTip(this.id)\"  id=\""
				+ selectedContainerName + "\" " + isDisabled + " >");
		stringBuffer.append(TD_CLOSE);
		stringBuffer.append(TD_OPEN);
		stringBuffer.append("<input type=\"text\" name=\"" + nameValue[2] + "\" value=\"" + nameValue[3]
				+ "\" size=\"2\" class=\"black_ar_md\" id=\"" + positionDimensionOne + "\" "
				+ isDisabled + " >");
		stringBuffer.append(TD_CLOSE);
		stringBuffer.append(TD_OPEN);
		stringBuffer.append("<input type=\"text\" name=\"" + nameValue[4] + "\" value=\"" + nameValue[5]
				+ "\" size=\"2\" class=\"black_ar_md\" id=\"" + positionDimensionTwo + "\" "
				+ isDisabled + " >");
		stringBuffer.append(TD_CLOSE);
		stringBuffer.append(TD_OPEN);
		stringBuffer.append("<a href=\"#\" onclick=\"" + functionCall + "\">");
		stringBuffer
				.append("<img src=\"images/Tree.gif\" border=\"0\" width=\"13\" height=\"15\" title=\'View storage locations\'>");
		stringBuffer.append("</a>");
		stringBuffer.append("<input type=\"hidden\" name=\"" + nameValue[6] + "\" value=\"" + nameValue[7]
				+ "\" id=\"" + containerId + "\">");
		stringBuffer.append(TD_CLOSE);
		stringBuffer.append(TR_CLOSE);
		stringBuffer.append("</table>");
	}

	/**
	 * Creates the print component.
	 *
	 * @param stringBuffer the string buffer
	 * @param nameValue the name value
	 */
	private void createPrintComponent(StringBuffer stringBuffer, String[] nameValue)
	{
		if (this.specimenSummaryForm.getShowCheckBoxes())
		{
			if (Constants.TRUE.equalsIgnoreCase(nameValue[1]))
			{
				stringBuffer.append("<center><input type=\"checkbox\" name=\"" + nameValue[0]
						+ "\" value=\"true\" checked=\"checked\"></center>");
			}
			else
			{
				stringBuffer.append("<center><input type=\"checkbox\" name=\"" + nameValue[0]
						+ "\" value=\"on\"></center>");
			}
		}
		else
		{
			this.createHiddenElement(stringBuffer, nameValue);
		}
	}

	/**
	 * Creates the collected component.
	 *
	 * @param stringBuffer the string buffer
	 * @param nameValue the name value
	 * @param isTextRow the is text row
	 */
	private void createCollectedComponent(StringBuffer stringBuffer,
			String[] nameValue, boolean isTextRow)
	{
		//		 sb.append("<td class=\"black_ar_md\" >");
		if (this.specimenSummaryForm.getShowCheckBoxes())
		{
			//			 if("getTextElement".equalsIgnoreCase(calledFrom))
			if (isTextRow)
			{
				stringBuffer.append("<center><input type=\"checkbox\" name=\"" + nameValue[0]
						+ "\" value=\"true\" disabled=\"true\" checked=\"checked\"></center>");
			}
			else
			{
				if (Constants.TRUE.equalsIgnoreCase(nameValue[1]))
				{
					stringBuffer.append("<center><input type=\"checkbox\" name=\"" + nameValue[0]
							+ "\" value=\"on\" checked=\"checked\" " + this.functionCall
							+ "></center>");
				}
				else
				{
					stringBuffer.append("<center><input type=\"checkbox\" name=\"" + nameValue[0]
							+ "\" value=\"on\" " + this.functionCall + "></center>");
				}
			}
		}
		else
		{
			//sb.append(getHTMLFormattedValue(""));
			this.createHiddenElement(stringBuffer, nameValue);
		}
		//			 sb.append(TD_CLOSE);
	}

	// Mandar : 4Dec08
	/**
	 * Creates the text field row.
	 *
	 * @param stringBuffer the string buffer
	 * @param counter the counter
	 * @param specimen the specimen
	 * @param elementNamePrefix the element name prefix
	 */
	private void createTextFieldRow(StringBuffer stringBuffer,
			int counter, GenericSpecimen specimen,
			String elementNamePrefix)
	{

		String colspan = "";
		int cols = 0;
		int colsp = 1;
		if (this.dataListType.equalsIgnoreCase(this.dataListTypes[2]))
		{
			colspan = "colspan=2";
			colsp = 2;
		}
		stringBuffer.append(TR_OPEN);
		for (int cnt = 0; cnt < H2COL_LBLS.length; cnt++)
		{
			final String nameValue[] = this.get2EleDetAt(cnt, specimen, elementNamePrefix);

			if (this.dataListType.equalsIgnoreCase(this.dataListTypes[2]) && cnt > 1)
			{
				stringBuffer.append("<TD colspan=2 class='black_ar'>&nbsp;</TD>");
				cols = cols + 2;
			}
			else if (cnt == 1)
			{
				stringBuffer.append("<TD colspan=2 class='black_ar'>");
				stringBuffer.append(nameValue[1]);
				stringBuffer.append(TD_CLOSE);
				cols = cols + 2;
			}
			else
			{
				stringBuffer.append("<TD " + colspan + " class='black_ar'>");
				stringBuffer.append(nameValue[1]);
				stringBuffer.append(TD_CLOSE);
				cols = cols + colsp;
			}
		}
		stringBuffer.append("<TD colspan=" + (this.colNum - cols) + " class='black_ar'>&nbsp;");
		//			addRemainingSpecimenElements(sb,elementNamePrefix,specimen);
		stringBuffer.append(TD_CLOSE);
		stringBuffer.append(TR_CLOSE);

	}

	/**
	 * Adds the remaining specimen elements.
	 *
	 * @param stringBuffer the string buffer
	 * @param elementNamePrefix the element name prefix
	 * @param specimen the specimen
	 * @param isTextRow the is text row
	 */
	private void addRemainingSpecimenElements(StringBuffer stringBuffer, String elementNamePrefix,
			GenericSpecimen specimen, boolean isTextRow)
	{
		final String nameValue[][] = this.getRemainingSpecimenElementsData(specimen,
				elementNamePrefix);
		for (final String[] element : nameValue)
		{
			stringBuffer.append("<input type=\"hidden\" name=\"" + element[0] + "\" value=\"" + element[1]
					+ "\" id=\"" + element[0] + "\">");
		}

		if (isTextRow)
		{
			String stringArrayNV[] = this.get1EleDetAt(1, specimen, elementNamePrefix);
			stringBuffer.append("<input type=\"hidden\" name=\"" + stringArrayNV[0] + "\" value=\"" + stringArrayNV[1] + "\" id=\""
					+ stringArrayNV[0] + "\">");

			stringArrayNV = this.get1EleDetAt(2, specimen, elementNamePrefix);
			stringBuffer.append("<input type=\"hidden\" name=\"" + stringArrayNV[0] + "\" value=\"" + stringArrayNV[1] + "\" id=\""
					+ stringArrayNV[0] + "\">");

			stringArrayNV = this.get1EleDetAt(4, specimen, elementNamePrefix);
			stringBuffer.append("<input type=\"hidden\" name=\"" + stringArrayNV[0] + "\" value=\"" + stringArrayNV[1] + "\" id=\""
					+ stringArrayNV[0] + "\">");

			stringArrayNV = this.get1EleDetAt(6, specimen, elementNamePrefix);
			stringBuffer.append("<input type=\"hidden\" name=\"" + stringArrayNV[0] + "\" value=\"" + stringArrayNV[1] + "\" id=\""
					+ stringArrayNV[0] + "\">");
			stringBuffer.append("<input type=\"hidden\" name=\"" + stringArrayNV[2] + "\" value=\"" + stringArrayNV[3] + "\" id=\""
					+ stringArrayNV[2] + "\">");
			stringBuffer.append("<input type=\"hidden\" name=\"" + stringArrayNV[4] + "\" value=\"" + stringArrayNV[5] + "\" id=\""
					+ stringArrayNV[4] + "\">");
			stringBuffer.append("<input type=\"hidden\" name=\"" + stringArrayNV[6] + "\" value=\"" + stringArrayNV[7] + "\" id=\""
					+ stringArrayNV[6] + "\">");
			stringBuffer.append("<input type=\"hidden\" name=\"" + stringArrayNV[8] + "\" value=\"" + stringArrayNV[9] + "\" id=\""
					+ stringArrayNV[8] + "\">");

			stringArrayNV = this.get1EleDetAt(7, specimen, elementNamePrefix);
			stringBuffer.append("<input type=\"hidden\" name=\"" + stringArrayNV[0] + "\" value=\"" + stringArrayNV[1] + "\" id=\""
					+ stringArrayNV[0] + "\">");

			stringArrayNV = this.get1EleDetAt(8, specimen, elementNamePrefix);//bug 11169
			stringBuffer.append("<input type=\"hidden\" name=\"" + stringArrayNV[0] + "\" value=\"" + stringArrayNV[1] + "\" id=\""
					+ stringArrayNV[0] + "\">");
		}
	}

	/**
	 * Gets the remaining specimen elements data.
	 *
	 * @param specimen the specimen
	 * @param elementNamePrefix the element name prefix
	 *
	 * @return the remaining specimen elements data
	 */
	private String[][] getRemainingSpecimenElementsData(GenericSpecimen specimen,
			String elementNamePrefix)
	{
		String str[][] = new String[10][2];
		if (!this.dataListTypes[2].equals(this.dataListType))
		{
			str = new String[11][2];
			str[10][0] = elementNamePrefix + "concentration";
			str[10][1] = this.getFormattedValue(specimen.getConcentration());
		}

		str[0][0] = elementNamePrefix + "collectionProtocolId";
		str[0][1] = this.getFormattedValue(specimen.getCollectionProtocolId());
		str[1][0] = elementNamePrefix + "readOnly";
		str[1][1] = this.getFormattedValue(specimen.getReadOnly());
		str[2][0] = elementNamePrefix + "uniqueIdentifier";
		str[2][1] = this.getFormattedValue(specimen.getUniqueIdentifier());
		str[3][0] = elementNamePrefix + "id";
		str[3][1] = this.getFormattedValue(specimen.getId());

		str[4][0] = elementNamePrefix + "type";
		str[4][1] = this.getFormattedValue(specimen.getType());
		str[5][0] = elementNamePrefix + "pathologicalStatus";
		str[5][1] = this.getFormattedValue(specimen.getPathologicalStatus());
		str[6][0] = elementNamePrefix + "tissueSide";
		str[6][1] = this.getFormattedValue(specimen.getTissueSide());
		str[7][0] = elementNamePrefix + "tissueSite";
		str[7][1] = this.getFormattedValue(specimen.getTissueSite());
		str[8][0] = elementNamePrefix + "className";
		str[8][1] = this.getFormattedValue(specimen.getClassName());

		str[9][0] = elementNamePrefix + "generateLabel";
		str[9][1] = this.getFormattedValue(specimen.isGenerateLabel());

		if (!this.dataListTypes[2].equals(this.dataListType))
		{
			str[10][0] = elementNamePrefix + "concentration";
			str[10][1] = this.getFormattedValue(specimen.getConcentration());
		}

		return str;
	}

	/**
	 * Creates the hidden element.
	 *
	 * @param stringBuffer the string buffer
	 * @param nameValue the name value
	 */
	private void createHiddenElement(StringBuffer stringBuffer,
			String[] nameValue)
	{
		//		 sb.append("<input type=\"hidden\" name=\""+nameValue[0]+"\" value=\""+nameValue[1]+"\">");
		stringBuffer.append("<input type=\"hidden\" name=\"" + nameValue[0] + "\" value=\"" + nameValue[1]
				+ "\" id=\"" + nameValue[0] + "\">");
	}

	// ------------ Mandar : 2Dec08 New Code for new formatUI end----------------

}
