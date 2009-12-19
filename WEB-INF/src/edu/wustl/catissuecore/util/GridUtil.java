

package edu.wustl.catissuecore.util;



/**
 * Utility class for grid display.
 *
 * @author nitesh_marwaha
 *
 */
public final class GridUtil {
	/**
	 * Adding a private constructor to avoid instantiation of this class.
	 */
	private GridUtil() {

	}

	/**
	 * start head tag for XML.
	 */
	public static final String HEAD_START_TAG = "<head>";
	/**
	 * End head tag for XML.
	 */
	public static final String HEAD_END_TAG = "</head>";
	/**
	 * Column width tag for XML.
	 */
	public static final String SETTINGS_WIDTH_PERCENTAGE = "<settings><colwidth>%</colwidth></settings>";
	/**
	 * tag for left Alignment of elements.
	 */
	public static final String ALIGN_LEFT = "left";
	/**
	 * tag for right Alignment of elements.
	 */
	public static final String ALIGN_RIGHT = "right";
	/**
	 * tag for center Alignment of elements.
	 */
	public static final String ALIGN_CENTER = "center";
	/**
	 * tag for read only cell type.
	 */
	public static final String CELL_TYPE_READ_ONLY = "ro";
	/**
	 * tag for radio cell type.
	 */
	public static final String CELL_TYPE_RADIO = "ra";
	/**
	 * tag for link cell type.
	 */
	public static final String CELL_TYPE_LINK = "link";

	public static final String CELL_SORT_STR = "str";
	public static final String CELL_SORT_INT = "int";
	public static final String CELL_SORT_DATE = "date";

	/**
	 * This will return XML doc type.
	 *
	 * @return XML doc type.
	 */
	public static String getXMLStringDocType() {
		return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
	}

	/**
	 * Returns the XML node for specimen.
	 *
	 * @return the XML node for specimen.
	 */
	public static String getXMLNodeForSpecimen() {
		return null;
	}

	/**
	 * This will return the row start tag.
	 *
	 * @return the row start tag.
	 */
	public static String getRowsStartTag() {
		return "<rows>";
	}

	/**
	 * this will return the end row tag.
	 *
	 * @return the end row tag.
	 */
	public static String getRowsEndTag() {
		return "</rows>";
	}

	/**
	 * This will return the start tag for the row with given id.
	 *
	 * @param rowId
	 * @return the start tag for the row with given id.
	 */
	public static String getEachRowStartTag(int rowId) {
		return "<row id=\"" + rowId + "\">";
	}

	/**
	 * This will return the end tag for the row.
	 *
	 * @return the end tag for the row.
	 */
	public static String getEachRowEndTag() {
		return "</row>";
	}

	/**
	 * This will return the start tag for the grid cell.
	 *
	 * @return the start tag for the grid cell.
	 */
	public static String getCellStartTag() {
		return "<cell>";
	}

	/**
	 * This will return the end tag for the grid cell.
	 *
	 * @return the end tag for the grid cell.
	 */
	public static String getCellEndTag() {
		return "</cell>";
	}

	/**
	 * This will return the start tag for the columns.
	 *
	 * @param type
	 * @param size
	 *            size of column
	 * @param align
	 *            alignment of the column
	 * @return start tag for the columns.
	 */
	public static String getColumnStartTag(String type, String size,
			String align, String sort) {
		return "<column type=\"" + type + "\" width=\"" + size + "\" align=\""
				+ align + "\" sort=\"" + sort + "\">";
	}

	/**
	 * This will return the end tag for the columns.
	 *
	 * @return the end tag for the columns.
	 */
	public static String getColumnEndTag() {
		return "</column>";
	}

	/**
	 * This will return the user data start tag.
	 *
	 * @param name
	 *            is the value to be displayed on the grid.
	 * @return the user data start tag.
	 */
	public static String getUserDataStartTag(String name) {
		return "<userdata name=\"" + name + "\">";
	}

	/**
	 * This will return the user data end tag.
	 *
	 * @return the user data end tag.
	 */
	public static String getUserDataEndTag() {
		return "</userdata>";
	}

	public static String getHeadStartTag() {
		return "<head>";
	}

	/**
	 * This will return the head end tag.
	 *
	 * @return the head end tag.
	 */
	public static String getHeadEndTag() {
		return "</head>";
	}

	/**
	 * This will return the column XML.
	 *
	 * @param width
	 *            width of the column
	 * @param type
	 * @param caption
	 * @param align
	 *            alignment of column
	 * @return
	 */
	public static String getColumnXML(String width, String type,
			String caption, String align, String sort) {
		return GridUtil.getColumnStartTag(type, width, align, sort) + caption
				+ GridUtil.getColumnEndTag();
	}

	/**
	 * This will return the cell XML.
	 *
	 * @param colValue
	 * @return the cell XML.
	 */
	public static String getCellXML(String colValue) {
		StringBuffer cellXML = new StringBuffer(300);
		cellXML.append(GridUtil.getCellStartTag());
		if(colValue!=null)
		{
			cellXML.append(formatString(org.apache.commons.lang.StringEscapeUtils
				.escapeXml(colValue)));
		}
		cellXML.append(GridUtil.getCellEndTag());
		try
		{
//		final InitialContext ctx = new InitialContext();
//		final DataSource ds = (DataSource) ctx.lookup("java:/catissuecore");
		}
		catch (Exception e)
		{
			System.out.println("");
		}
		return cellXML.toString();
	}

	/**
	 * This will return the values of the user data for a row.
	 *
	 * @param valueForUserData
	 * @return values of the user data for a row.
	 */
	public static String getUserDataForRow(String valueForUserData) {
		StringBuffer userdata = new StringBuffer(300);
		userdata.append(valueForUserData);
		userdata.append(GridUtil.getUserDataEndTag());
		return userdata.toString();
	}

	/**
	 * This will format the XML string and removes the new line characters from
	 * the XML.
	 *
	 * @param explorerTreeXML
	 * @return
	 */
	private static String formatString(String explorerTreeXML) {
		explorerTreeXML = explorerTreeXML.replaceAll(
				"[\\n]", "");
		explorerTreeXML = explorerTreeXML.replaceAll(
				"[\\n]", "");
		int explorerTreeXML2Ctr = explorerTreeXML.indexOf("\n ");
		while (explorerTreeXML2Ctr != -1) {
			explorerTreeXML = explorerTreeXML.replaceAll("\n ", "\n");
			explorerTreeXML2Ctr = explorerTreeXML.indexOf("\n ");
		}
		return explorerTreeXML;
	}
}
