package edu.wustl.catissuecore.bean;

import java.util.HashMap;
import java.util.Map;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.util.tag.ScriptGenerator;

/**
 * @author mandar_deshmukh
 *
 */
public class AliquotBean
{
	/**
	 * constructor
	 */
    public AliquotBean()
	{
		super();
	}
    /**
     * @param aliquotNoParam - aliquotNoParam
     * @param aliquotMapParam - aliquotMapParam
     * @param dataMapParam - dataMapParam
     */
	public AliquotBean(int aliquotNoParam, Map aliquotMapParam, Map dataMapParam)
	{
		super();
		this.aliquotNo = aliquotNoParam;
		this.aliquotMap = aliquotMapParam;
		this.dataMap = dataMapParam;
	}

	private int aliquotNo;
	private Map aliquotMap = new HashMap();
	private Map dataMap = new HashMap();

	private String labelKey = "";
	private String qtyKey = "";
	private String barKey = "";
	private String containerMap = "";
	private String containerMapStyle = "";
	//Keys for container if selected from Map
	private String containerIdFromMapKey = "";
	private String containerNameFromMapKey = "";
	private String pos1FromMapKey = "";
	private String pos2FromMapKey = "";
	private String stContSelection = "";
	private String containerStyle = "";
	private String containerIdStyle = "";
	private String pos1Style = "";
	private String pos2Style = "";

	private boolean dropDownDisable = false;
	private boolean textBoxDisable = false;

	private String[] attrNames = null;
	private String buttonOnClicked = null;

	private String onChange = "";
	private String[] initValues = null;
	private String rowNumber = "";
	private String jsScript = "";

	/**
	 * To be sent from the calling method.
	 * @param parentSPId - parentSPId
	 * @param collectionProtocolId - collectionProtocolId
	 * @param className -className
	 * @param CPQuery - CPQuery
	 */
	public void setAllData(String parentSPId, String collectionProtocolId, String className,
			String CPQuery)
	{
		// ------------------ used outside the bean
		this.setOutsideData();

		//------- internal use only
		final String containerKey = "value(Specimen:" + this.aliquotNo + "_StorageContainer_id)";
		final String pos1Key = "value(Specimen:" + this.aliquotNo + "_positionDimensionOne)";
		final String pos2Key = "value(Specimen:" + this.aliquotNo + "_positionDimensionTwo)";
		final String rbKey = "radio_" + this.aliquotNo + "";
		String frameUrl = "";
		this.attrNames = new String[3];
		this.attrNames[0] = containerKey;
		this.attrNames[1] = pos1Key;
		this.attrNames[2] = pos2Key;

		if (CPQuery != null)
		{
			this.onChange = "onCustomListBoxChangeInAliquot(this,'CPQueryCreateAliquots.do?pageOf=pageOfCreateAliquot&operation=add&menuSelected=15&method=executeContainerChange&CPQuery=true&"
					+ Constants.PARENT_SPECIMEN_ID + "=" + parentSPId + "')";
		}

		this.initValues[0] = (String) this.aliquotMap.get( "Specimen:" + this.aliquotNo
				+ "_StorageContainer_id" );
		this.initValues[1] = (String) this.aliquotMap.get( "Specimen:" + this.aliquotNo
				+ "_positionDimensionOne" );
		this.initValues[2] = (String) this.aliquotMap.get( "Specimen:" + this.aliquotNo
				+ "_positionDimensionTwo" );

		this.addToMap( rbKey );
		final int radioSelected = Integer.parseInt( this.aliquotMap.get( rbKey ).toString() );
		if (radioSelected == 1)
		{
			this.dropDownDisable = true;
			this.textBoxDisable = true;
		}
		else if (radioSelected == 2)
		{
			this.textBoxDisable = true;
		}
		else if (radioSelected == 3)
		{
			this.dropDownDisable = true;
		}

		frameUrl = "ShowFramedPage.do?pageOf=pageOfAliquot&amp;containerStyleId="
				+ this.containerIdStyle + "&amp;xDimStyleId=" + this.pos1Style
				+ "&amp;yDimStyleId=" + this.pos2Style + "&amp;containerStyle="
				+ this.containerStyle + "&amp;" + Constants.CAN_HOLD_SPECIMEN_CLASS + "="
				+ className + "&amp;" + Constants.CAN_HOLD_COLLECTION_PROTOCOL + "="
				+ collectionProtocolId;

		this.buttonOnClicked = "mapButtonClickedInAliquot('" + frameUrl + "','" + this.aliquotNo
				+ "')";

		this.jsScript = ScriptGenerator.getJSEquivalentFor( this.dataMap, this.rowNumber );
	}
	/**
	 *
	 */
    private void setOutsideData()
	{
		this.labelKey = "value(Specimen:" + this.aliquotNo + "_label)";
		this.qtyKey = "value(Specimen:" + this.aliquotNo + "_quantity)";
		this.barKey = "value(Specimen:" + this.aliquotNo + "_barcode)";
		this.containerMap = "value(mapButton_" + this.aliquotNo + ")";
		this.containerMapStyle = "mapButton_" + this.aliquotNo;
		//Keys for container if selected from Map
		this.containerIdFromMapKey = "value(Specimen:" + this.aliquotNo
				+ "_StorageContainer_id_fromMap)";
		this.containerNameFromMapKey = "value(Specimen:" + this.aliquotNo
				+ "_StorageContainer_name_fromMap)";
		this.pos1FromMapKey = "value(Specimen:" + this.aliquotNo + "_positionDimensionOne_fromMap)";
		this.pos2FromMapKey = "value(Specimen:" + this.aliquotNo + "_positionDimensionTwo_fromMap)";
		this.stContSelection = "value(radio_" + this.aliquotNo + ")";
		this.containerStyle = "container_" + this.aliquotNo + "_0";
		this.containerIdStyle = "containerId_" + this.aliquotNo + "_0";
		this.pos1Style = "pos1_" + this.aliquotNo + "_1";
		this.pos2Style = "pos2_" + this.aliquotNo + "_2";
		this.dropDownDisable = false;
		this.textBoxDisable = false;
		this.onChange = "onCustomListBoxChangeInAliquot(this,'CreateAliquots.do?pageOf=pageOfCreateAliquot&operation=add&menuSelected=15&method=executeContainerChange')";
		this.initValues = new String[3];
		this.rowNumber = String.valueOf( this.aliquotNo );
	}
    /**
     * @param rbKey - rbKey
     */
	private void addToMap(String rbKey)
	{
		if (this.aliquotMap.get( rbKey ) == null)
		{
			this.aliquotMap.put( rbKey, "2" );
		}
	}
    /**
     * @return - String
     */
	public String getJsScript()
	{
		return this.jsScript;
	}

	//-------------- GET SET -----------------------
	/**
	 * @return - Map
	 */
	public Map getAliquotMap()
	{
		return this.aliquotMap;
	}
    /**
     * @param aliquotMapParam - aliquotMapParam
     */
	public void setAliquotMap(Map aliquotMapParam)
	{
		this.aliquotMap = aliquotMapParam;
	}
    /**
     * @return aliquotNo
     */
	public int getAliquotNo()
	{
		return this.aliquotNo;
	}
    /**
     * @param i - aliquotNo
     */
	public void setAliquotNo(int i)
	{
		this.aliquotNo = i;
	}
    /**
     * @return - array of String
     */
	public String[] getAttrNames()
	{
		return this.attrNames;
	}
    /**
     * @return - barKey
     */
	public String getBarKey()
	{
		return this.barKey;
	}
	/**
	 * @return - buttonOnClicked
	 */
	public String getButtonOnClicked()
	{
		return this.buttonOnClicked;
	}
	/**
	 * @return - containerIdFromMapKey
	 */
	public String getContainerIdFromMapKey()
	{
		return this.containerIdFromMapKey;
	}
    /**
     * @return - containerIdStyle
     */
	public String getContainerIdStyle()
	{
		return this.containerIdStyle;
	}
    /**
     * @return - String
     */
	public String getContainerMap()
	{
		return this.containerMap;
	}
    /**
     * @return - String
     */
	public String getContainerMapStyle()
	{
		return this.containerMapStyle;
	}
	/**
	 * @return - containerNameFromMapKey
	 */
	public String getContainerNameFromMapKey()
	{
		return this.containerNameFromMapKey;
	}
    /**
     * @return - containerStyle
     */
	public String getContainerStyle()
	{
		return this.containerStyle;
	}
    /**
     * @return - boolean
     */
	public boolean isDropDownDisable()
	{
		return this.dropDownDisable;
	}
    /**
     * @return - array of String
     */
	public String[] getInitValues()
	{
		return this.initValues;
	}
    /**
     * @return - labelKey
     */
	public String getLabelKey()
	{
		return this.labelKey;
	}
    /**
     * @return - String
     */
	public String getOnChange()
	{
		return this.onChange;
	}
    /**
     * @return - String
     */
	public String getPos1FromMapKey()
	{
		return this.pos1FromMapKey;
	}
    /***
     * @return - pos1Style
     */
	public String getPos1Style()
	{
		return this.pos1Style;
	}
	/**
	 * @return - pos2FromMapKey
	 */
	public String getPos2FromMapKey()
	{
		return this.pos2FromMapKey;
	}
    /**
     * @return - pos2Style
     */
	public String getPos2Style()
	{
		return this.pos2Style;
	}
    /**
     * @return - qtyKey
     */
	public String getQtyKey()
	{
		return this.qtyKey;
	}
    /**
     * @return - rowNumber
     */
	public String getRowNumber()
	{
		return this.rowNumber;
	}
    /**
     * @return - stContSelection
     */
	public String getStContSelection()
	{
		return this.stContSelection;
	}
    /**
     * @return - textBoxDisable
     */
	public boolean isTextBoxDisable()
	{
		return this.textBoxDisable;
	}

}
