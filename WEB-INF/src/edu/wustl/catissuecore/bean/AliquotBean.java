/**
 * 
 */
package edu.wustl.catissuecore.bean;

import java.util.HashMap;
import java.util.Map;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.util.tag.ScriptGenerator;

/**
 * @author mandar_deshmukh
 *
 */
public class AliquotBean {

	public AliquotBean()
	{
		super();
	}
	
	public AliquotBean(int aliquotNo, Map aliquotMap, Map dataMap)
	{
		super();
		this.aliquotNo = aliquotNo;
		this.aliquotMap = aliquotMap;
		this.dataMap = dataMap;
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

	 private String[] attrNames=null;
	 private String buttonOnClicked = null;
	 
	 private String onChange = "";		 
	 private String[] initValues = null ;
	 private String rowNumber = "";
	 private String jsScript = "";
	 
	 /**
	  * To be sent from the calling method.
	  * 
	  * @param parentSPId
	  * @param collectionProtocolId
	  * @param className
	  */
	public void setAllData(String parentSPId, String collectionProtocolId, String className, String CPQuery)
	{
		// ------------------ used outside the bean
		 setOutsideData();
		 
		 //------- internal use only
		 String containerKey = "value(Specimen:" + aliquotNo + "_StorageContainer_id)";
		 String pos1Key = "value(Specimen:" + aliquotNo + "_positionDimensionOne)";
		 String pos2Key = "value(Specimen:" + aliquotNo + "_positionDimensionTwo)";
		 String rbKey = "radio_" + aliquotNo +"";
		 String frameUrl="";
		 attrNames = new String[3] ;
		 attrNames[0] = containerKey;
		 attrNames[1] = pos1Key;
         attrNames[2] = pos2Key;
         
		if(CPQuery!= null)
		{
			onChange = "onCustomListBoxChangeInAliquot(this,'CPQueryCreateAliquots.do?pageOf=pageOfCreateAliquot&operation=add&menuSelected=15&method=executeContainerChange&CPQuery=true&"+Constants.PARENT_SPECIMEN_ID+"="+parentSPId+"')";	
		}

		 initValues[0] = (String)aliquotMap.get("Specimen:" + aliquotNo + "_StorageContainer_id");
		 initValues[1] = (String)aliquotMap.get("Specimen:" + aliquotNo + "_positionDimensionOne");
		 initValues[2] = (String)aliquotMap.get("Specimen:" + aliquotNo + "_positionDimensionTwo");
		
		addToMap(rbKey);
		int radioSelected = Integer.parseInt(aliquotMap.get(rbKey).toString());
		if(radioSelected == 1)
		{
			dropDownDisable = true;
			textBoxDisable = true;
		}
		else if(radioSelected == 2)
		{									
			textBoxDisable = true;
		}
		else if(radioSelected == 3)
		{
			dropDownDisable = true;									
	    }					
	
	frameUrl = "ShowFramedPage.do?pageOf=pageOfAliquot&amp;containerStyleId=" + containerIdStyle + "&amp;xDimStyleId=" + pos1Style + "&amp;yDimStyleId=" + pos2Style
	+ "&amp;containerStyle=" + containerStyle 
	+ "&amp;" + Constants.CAN_HOLD_SPECIMEN_CLASS+"="+className
	+ "&amp;" + Constants.CAN_HOLD_COLLECTION_PROTOCOL +"=" + collectionProtocolId ;
	
	buttonOnClicked = "mapButtonClickedInAliquot('"+frameUrl+"','"+aliquotNo+"')"; 
	
	jsScript = ScriptGenerator.getJSEquivalentFor(dataMap,rowNumber);
}

	private void setOutsideData()
	{
		labelKey = "value(Specimen:" + aliquotNo + "_label)";
		 qtyKey = "value(Specimen:" + aliquotNo + "_quantity)";
		 barKey = "value(Specimen:" + aliquotNo + "_barcode)";
		 containerMap = "value(mapButton_" + aliquotNo + ")";
		 containerMapStyle = "mapButton_" + aliquotNo ;
		//Keys for container if selected from Map
		 containerIdFromMapKey = "value(Specimen:" + aliquotNo + "_StorageContainer_id_fromMap)";
		 containerNameFromMapKey = "value(Specimen:" + aliquotNo + "_StorageContainer_name_fromMap)";
		 pos1FromMapKey = "value(Specimen:" + aliquotNo + "_positionDimensionOne_fromMap)";
		 pos2FromMapKey = "value(Specimen:" + aliquotNo + "_positionDimensionTwo_fromMap)";
		 stContSelection = "value(radio_" + aliquotNo + ")";
		 containerStyle = "container_" + aliquotNo + "_0";
		 containerIdStyle = "containerId_" + aliquotNo + "_0";
		 pos1Style = "pos1_" + aliquotNo + "_1";
		 pos2Style = "pos2_" + aliquotNo + "_2";
		 dropDownDisable = false;
		 textBoxDisable = false;
		 onChange = "onCustomListBoxChangeInAliquot(this,'CreateAliquots.do?pageOf=pageOfCreateAliquot&operation=add&menuSelected=15&method=executeContainerChange')";		 
		 initValues = new String[3] ;
		 rowNumber = String.valueOf(aliquotNo);
	}

	private void addToMap(String rbKey)
	{
		if(aliquotMap.get(rbKey)==null)
		{
		   aliquotMap.put(rbKey,"2");
	    }
	}

	
	public String getJsScript()
	{
		return jsScript;
	}
	//-------------- GET SET -----------------------
	public Map getAliquotMap() {
		return aliquotMap;
	}

	public void setAliquotMap(Map aliquotMap) {
		this.aliquotMap = aliquotMap;
	}

	public int getAliquotNo() {
		return aliquotNo;
	}

	public void setAliquotNo(int i) {
		this.aliquotNo = i;
	}

	public String[] getAttrNames() {
		return attrNames;
	}

	public String getBarKey() {
		return barKey;
	}

	public String getButtonOnClicked() {
		return buttonOnClicked;
	}

	public String getContainerIdFromMapKey() {
		return containerIdFromMapKey;
	}

	public String getContainerIdStyle() {
		return containerIdStyle;
	}

	public String getContainerMap() {
		return containerMap;
	}

	public String getContainerMapStyle() {
		return containerMapStyle;
	}

	public String getContainerNameFromMapKey() {
		return containerNameFromMapKey;
	}

	public String getContainerStyle() {
		return containerStyle;
	}

	public boolean isDropDownDisable() {
		return dropDownDisable;
	}

	public String[] getInitValues() {
		return initValues;
	}

	public String getLabelKey() {
		return labelKey;
	}

	public String getOnChange() {
		return onChange;
	}

	public String getPos1FromMapKey() {
		return pos1FromMapKey;
	}

	public String getPos1Style() {
		return pos1Style;
	}

	public String getPos2FromMapKey() {
		return pos2FromMapKey;
	}

	public String getPos2Style() {
		return pos2Style;
	}

	public String getQtyKey() {
		return qtyKey;
	}

	public String getRowNumber() {
		return rowNumber;
	}

	public String getStContSelection() {
		return stContSelection;
	}

	public boolean isTextBoxDisable() {
		return textBoxDisable;
	}

}
