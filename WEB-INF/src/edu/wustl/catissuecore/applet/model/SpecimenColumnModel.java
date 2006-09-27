package edu.wustl.catissuecore.applet.model;

import java.awt.Component;

import javax.swing.AbstractCellEditor;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import edu.wustl.catissuecore.applet.AppletConstants;
import edu.wustl.catissuecore.applet.component.BaseTable;
import edu.wustl.catissuecore.applet.listener.BaseFocusHandler;
import edu.wustl.catissuecore.applet.listener.ButtonHandler;
import edu.wustl.catissuecore.applet.listener.ClassComboBoxHandler;
import edu.wustl.catissuecore.applet.listener.CollectionGroupItemHandler;
import edu.wustl.catissuecore.applet.listener.ComboBoxHandler;
import edu.wustl.catissuecore.applet.listener.ExternalIdentifierButtonHandler;
import edu.wustl.catissuecore.applet.listener.MapButtonHandler;
import edu.wustl.catissuecore.applet.listener.ParentSpecimenItemHandler;
import edu.wustl.catissuecore.applet.listener.TextFieldHandler;
import edu.wustl.catissuecore.applet.listener.TypeComboBoxHandler;
import edu.wustl.catissuecore.util.global.Constants;

/**
 * This Class is a column model for multiple specimen page.
 *  
 * @author mandar_deshmukh
 */
public class SpecimenColumnModel extends AbstractCellEditor
		implements
			TableCellRenderer,
			TableCellEditor
{
	/**
	 * Default Serial Version ID
	 */
	private static final long serialVersionUID = 1L;

	//Reference of Table
	BaseTable table;

	// Fields as per Model.

	//Specimen Collection Group
	JTextField specimenCollectionGroup;
	JRadioButton rbspecimenGroup;
	JPanel collectionGroupPanel;
	
	//Parent Specimen 
	JTextField parentSpecimen;
	JRadioButton rbparentSpecimen;
	JPanel parentSpecimenPanel;

	//Group for radiobuttons
	ButtonGroup radioGroup;

	// Label
	JTextField label;
	
	// Barcode
	JTextField barCode;

	//Specimen Class
	JComboBox classList;

	//Specimen Type
	JComboBox typeList;
	
	//TissueSite
	JComboBox tissueSiteList;
	
	//TissueSide
	JComboBox tissueSideList;

	//PathologicalStatus 
	JComboBox pathologicalStatusList;
	
	// Quantity
	JTextField quantity;
	JLabel unit;
	JPanel quantityUnitPanel;

	// Concentration
	JTextField concentration;
	
	//For Storage Location
	JButton mapButton;
	JTextField location;
	JPanel storageLocationPanel;
	
	//For Comments
	JButton comments;
	
	//Events 
	JButton eventsButton;
	
	//External Identifier
	JButton externalIdentifierButton;
	
	//BioHazard
	JButton bioHazardButton;

	//Derive
	JButton deriveButton;

	// For holding component value
	String text="";

	/**
	 * Constructs the SpecimenColumnModel object based on the parameters.
	 * @param table Reference of the table.
	 * @param column Number of column to work on.
	 */
	public SpecimenColumnModel(BaseTable table, int column) 
	{
		super();
		this.table = table;
		MultipleSpecimenTableModel model = (MultipleSpecimenTableModel)table.getModel();
		// set row height based on contents	
		for(int rowno = 0; rowno<table.getRowCount();rowno++)
		{
			//For combobox
			if(rowno == AppletConstants.SPECIMEN_CLASS_ROW_NO || rowno == AppletConstants.SPECIMEN_TYPE_ROW_NO || 
				rowno == AppletConstants.SPECIMEN_TISSUE_SITE_ROW_NO || rowno == AppletConstants.SPECIMEN_TISSUE_SIDE_ROW_NO || 
				rowno == AppletConstants.SPECIMEN_PATHOLOGICAL_STATUS_ROW_NO )
			{
				table.setRowHeight(rowno,25);
			}
			//for textfield
			else if(rowno == AppletConstants.SPECIMEN_LABEL_ROW_NO || rowno == AppletConstants.SPECIMEN_BARCODE_ROW_NO ||
					rowno == AppletConstants.SPECIMEN_COMMENTS_ROW_NO || rowno == AppletConstants.SPECIMEN_CONCENTRATION_ROW_NO)
			{
				table.setRowHeight(rowno,20);
			}
			//for panels
			else if(rowno == AppletConstants.SPECIMEN_COLLECTION_GROUP_ROW_NO || rowno == AppletConstants.SPECIMEN_PARENT_ROW_NO ||
					rowno == AppletConstants.SPECIMEN_QUANTITY_ROW_NO || rowno == AppletConstants.SPECIMEN_STORAGE_LOCATION_ROW_NO)
			{
				table.setRowHeight(rowno,35);
			}
			//for others (Buttons etc)
			else
			{
				table.setRowHeight(rowno,30);	
			}
		}
		// ---------------- Object creation --------------------
		instantiateObjects(model,column);
		// --------------- Adding Listeners ---------------------
		addListeners();
		// ------------------------------------

		TableColumnModel columnModel = table.getColumnModel();
		columnModel.getColumn(column).setCellRenderer(this);
		columnModel.getColumn(column).setCellEditor(this);
		columnModel.getColumn(column).setResizable(false );
		columnModel.getColumn(column).setPreferredWidth(175 );
		
	}
	

	/**
	 *  This method returns the component used as cell renderer. 
	 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
			boolean hasFocus, int row, int column)
	{
		text = (value == null) ? "" : value.toString();
		Component component = getComponentAt(row, column, hasFocus, isSelected);
		return component;
	}

	/** 
	 * This method returns the component used as cell editor.
	 * @see javax.swing.table.TableCellEditor#getTableCellEditorComponent(javax.swing.JTable, java.lang.Object, boolean, int, int)
	 */
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected,
			int row, int column)
	{
		text = (value == null) ? "" : value.toString();
		Component component = getComponentAt(row, column, false, isSelected);
		return component;
	}

	/**
	 * This method returns the value of the current editor component. 
	 * @see javax.swing.CellEditor#getCellEditorValue()
	 */
	public Object getCellEditorValue()
	{
		return text;
	}

	/**
	 * Returns a component at specified co-ordinate and sets appropriate foregroung and background.
	 * 
	 * @param row Row No
	 * @param col Col No
	 * @param hasFocus 
	 * @param isSelected
	 * @return Component
	 */
	private Component getComponentAt(int row, int col, boolean hasFocus, boolean isSelected)
	{
		Component comp = null; 

		switch (row)
		{
		//Specimen Collection Group
		case AppletConstants.SPECIMEN_COLLECTION_GROUP_ROW_NO :
			comp = collectionGroupPanel;
			break;
		//Parent Specimen 
		case AppletConstants.SPECIMEN_PARENT_ROW_NO :
			comp = parentSpecimenPanel;
			break;
		// Label
		case AppletConstants.SPECIMEN_LABEL_ROW_NO :
			comp = label;
			break;
		// Barcode
		case AppletConstants.SPECIMEN_BARCODE_ROW_NO :
			comp = barCode;
			break;
		//Specimen Class
		case AppletConstants.SPECIMEN_CLASS_ROW_NO :
			comp = classList;
			break;
		//Specimen Type
		case AppletConstants.SPECIMEN_TYPE_ROW_NO :
			comp = typeList;
			break;
		//TissueSite
		case AppletConstants.SPECIMEN_TISSUE_SITE_ROW_NO :
			comp = tissueSiteList;
			break;
		//TissueSide
		case AppletConstants.SPECIMEN_TISSUE_SIDE_ROW_NO :
			comp = tissueSideList;
			break;
		//PathologicalStatus 
		case AppletConstants.SPECIMEN_PATHOLOGICAL_STATUS_ROW_NO : 
			comp = pathologicalStatusList;
			break;
		// Quantity
		case AppletConstants.SPECIMEN_QUANTITY_ROW_NO :
			comp = quantityUnitPanel;
			break;
		// Concentration
		case AppletConstants.SPECIMEN_CONCENTRATION_ROW_NO :
			comp = concentration;
			break;
		//For Storage Location
		case AppletConstants.SPECIMEN_STORAGE_LOCATION_ROW_NO :
			comp = storageLocationPanel;
			break;
		//For Comments
		case AppletConstants.SPECIMEN_COMMENTS_ROW_NO :
			comp = comments;
			break;
		//Events 
		case AppletConstants.SPECIMEN_EVENTS_ROW_NO :
			comp = eventsButton;
			break;
		//External Identifier
		case AppletConstants.SPECIMEN_EXTERNAL_IDENTIFIERS_ROW_NO :
			comp = externalIdentifierButton;
			break;
		//BioHazard
		case AppletConstants.SPECIMEN_BIOHAZARDS_ROW_NO :
			comp = bioHazardButton;
			break;
		//Derive
		case AppletConstants.SPECIMEN_DERIVE_ROW_NO :
			comp = deriveButton;
			break;
		default :
			comp = new JTextField(10);
		}
		
		formatComponent(comp, hasFocus, isSelected);
		return comp;
	}
	
	/*
	 * This method formats the component as per the parameters provided.
	 */
	private void formatComponent(Component comp, boolean hasFocus, boolean isSelected )
	{
		if (hasFocus)
		{
			comp.setForeground(table.getForeground());
			comp.setBackground(UIManager.getColor("List.background"));
		}
		else if (isSelected)
		{
			comp.setForeground(table.getSelectionForeground());
			comp.setBackground(table.getSelectionBackground());
		}
		else
		{
			comp.setForeground(table.getForeground());
			comp.setBackground(UIManager.getColor("List.background"));
		}
		comp.repaint(); 
	}
	
	/*
	 * This method initialises the components.
	 */
	private void instantiateObjects(MultipleSpecimenTableModel model, int column)
	{
		//Specimen Collection Group
		specimenCollectionGroup = new JTextField(10);
		rbspecimenGroup = new JRadioButton();
		collectionGroupPanel = new JPanel();
		rbspecimenGroup.setSelected(true);
		
		//Parent Specimen 
		parentSpecimen = new JTextField(10);
		rbparentSpecimen = new JRadioButton();
		parentSpecimenPanel = new JPanel();
		parentSpecimen.setEnabled(false);
		
		//Group for radiobuttons
		radioGroup = new ButtonGroup();

		// adding radiobuttons to group
		radioGroup.add(rbspecimenGroup);
		radioGroup.add(rbparentSpecimen);

		//Adding components to the panel
		collectionGroupPanel.add(rbspecimenGroup);
		collectionGroupPanel.add(specimenCollectionGroup);
		parentSpecimenPanel.add(rbparentSpecimen);
		parentSpecimenPanel.add(parentSpecimen);
				
		// Label
		label = new JTextField(10);
		
		// Barcode
		barCode = new JTextField(10);

		//Specimen Class
		classList = new JComboBox(model.getSpecimenClassValues());

		String type[] = {Constants.SELECT_OPTION};
		//Specimen Type
		typeList = new JComboBox(model.getSpecimenTypeValues(null));
		
		//TissueSite
		tissueSiteList = new JComboBox(model.getTissueSiteValues());
		
		//TissueSide
		tissueSideList = new JComboBox(model.getTissueSideValues());

		//PathologicalStatus 
		pathologicalStatusList = new JComboBox(model.getPathologicalStatusValues());
		
		// Quantity
		quantity = new JTextField(10);
		unit = new JLabel();
		quantityUnitPanel = new JPanel();
		
		quantityUnitPanel.add(quantity);
		quantityUnitPanel.add(unit);

		// Concentration
		concentration = new JTextField(10);
		
		//For Storage Location
		mapButton = new JButton("Map");
		location = new JTextField(10);
		storageLocationPanel = new JPanel();
		
		storageLocationPanel.add(location);
		storageLocationPanel.add(mapButton);
		
		//For Comments
		comments = new JButton("Add");
		
		//Events 
		eventsButton = new JButton("E");
		
		//External Identifier
		externalIdentifierButton = new JButton("EI");
		
		//BioHazard
		bioHazardButton = new JButton("B");

		//Derive
		deriveButton = new JButton("D");

		// For holding component value
		text=new String();
	}

	/*
	 * This method adds listeners to the components.
	 */
	private void addListeners()
	{
		// Listeners
		TextFieldHandler textHandler = new TextFieldHandler(table); 
		ParentSpecimenItemHandler parentSpecimenItemHandler = new ParentSpecimenItemHandler(table);
		CollectionGroupItemHandler collectionGroupItemHandler = new CollectionGroupItemHandler(table);
		ClassComboBoxHandler classComboBoxHandler = new ClassComboBoxHandler(table);
		TypeComboBoxHandler typeComboBoxHandler = new TypeComboBoxHandler(table);
		ComboBoxHandler comboBoxHandler = new ComboBoxHandler(table);
		MapButtonHandler mapButtonHandler = new MapButtonHandler(table);
		ButtonHandler buttonHandler = new ButtonHandler(table);
		BaseFocusHandler baseFocusHandler = new BaseFocusHandler(table);
		
		//Specimen Collection Group
		specimenCollectionGroup.addActionListener(textHandler);
		rbspecimenGroup.addItemListener(collectionGroupItemHandler );
		specimenCollectionGroup.addFocusListener(baseFocusHandler);
		
		//Parent Specimen 
		parentSpecimen.addActionListener(textHandler);
		rbparentSpecimen.addItemListener(parentSpecimenItemHandler);
		parentSpecimen.addFocusListener(baseFocusHandler);
		
		// Label
		label.addActionListener(textHandler);
		label.addFocusListener(baseFocusHandler);
		
		// Barcode
		barCode.addActionListener(textHandler);
		barCode.addFocusListener(baseFocusHandler);

		//Specimen Class
		classList.addActionListener(classComboBoxHandler);

		//Specimen Type
		typeList.addActionListener(typeComboBoxHandler);
		
		//TissueSite
		tissueSiteList.addActionListener(comboBoxHandler);
		
		//TissueSide
		tissueSideList.addActionListener(comboBoxHandler);

		//PathologicalStatus 
		pathologicalStatusList.addActionListener(comboBoxHandler);
		
		// Quantity
		quantity.addActionListener(textHandler);
		quantity.addFocusListener(baseFocusHandler);

		// Concentration
		concentration.addActionListener(textHandler);
		concentration.addFocusListener(baseFocusHandler);
		
		//For Storage Location
		mapButton.addActionListener(mapButtonHandler);
		
		//For Comments
		comments.addActionListener(buttonHandler);
		
		//Events 
		//eventsButton.addActionListener(buttonHandler);
		
		//External Identifier
		externalIdentifierButton.addActionListener(new ExternalIdentifierButtonHandler(table));
		
		//BioHazard
		//bioHazardButton.addActionListener(buttonHandler);

		//Derive
	//	deriveButton.addActionListener(buttonHandler);
	}

	public void specimenClassUpdated(String name)
	{
		//Specimen Type JComboBox typeList;
		setTypeListModel(name); 
		setConcentrationStatus();
		
	}

	//--------------- GETTER SETTER ------------------------------------
	/**
	 * @return Returns the barCode.
	 */
	public String getBarCode() {
		return barCode.getText();
	}
	/**
	 * @param barCode The barCode to set.
	 */
	public void setBarCode(String barCode) {
		this.barCode.setText(barCode);
	}
	/**
	 * @return Returns the classList.
	 */
	public String getClassList() {
		return classList.getSelectedItem().toString()  ;
	}
	/**
	 * @param classList The classList to set.
	 */
	public void setClassList(String className) {
		this.classList.setSelectedItem(className);
	}
	/**
	 * @return Returns the comments.
	 */
	public String getComments() {
		return comments.getText() ;
	}
	/**
	 * @param comments The comments to set.
	 */
	public void setComments(String comments) {
		this.comments.setText(comments);
	}
	/**
	 * @return Returns the concentration.
	 */
	public String getConcentration() {
		return concentration.getText() ;
	}
	/**
	 * @param concentration The concentration to set.
	 */
	public void setConcentration(String concentration) {
		this.concentration.setText(concentration);
	}
	/**
	 * @return Returns the label.
	 */
	public String getLabel() {
		return label.getText() ;
	}
	/**
	 * @param label The label to set.
	 */
	public void setLabel(String label) {
		this.label.setText(label);
	}
	/**
	 * @return Returns the location.
	 */
	public String getLocation() {
		return location.getText() ;
	}
	/**
	 * @param location The location to set.
	 */
	public void setLocation(String location) {
		this.location.setText(location);
	}
	/**
	 * @return Returns the parentSpecimen.
	 */
	public String getParentSpecimen() {
		return parentSpecimen.getText() ;
	}
	/**
	 * @param parentSpecimen The parentSpecimen to set.
	 */
	public void setParentSpecimen(String parentSpecimen) {
		this.parentSpecimen.setText(parentSpecimen);
	}
	/**
	 * @return Returns the pathologicalStatusList.
	 */
	public String getPathologicalStatusList() {
		return pathologicalStatusList.getSelectedItem().toString()   ;
	}
	/**
	 * @param pathologicalStatusList The pathologicalStatusList to set.
	 */
	public void setPathologicalStatusList(String pathologicalStatus) {
		this.pathologicalStatusList.setSelectedItem(pathologicalStatus);
	}
	/**
	 * @return Returns the quantity.
	 */
	public String getQuantity() {
		return quantity.getText() ;
	}
	/**
	 * @param quantity The quantity to set.
	 */
	public void setQuantity(String quantity) {
		this.quantity.setText(quantity);
	}
	/**
	 * @return Returns the rbparentSpecimen.
	 */
	public boolean getRbparentSpecimen() {
		return rbparentSpecimen.isSelected() ;
	}
	/**
	 * @param rbparentSpecimen The rbparentSpecimen to set.
	 */
	public void setRbparentSpecimen(boolean rbparentSpecimen) {
		this.rbparentSpecimen.setSelected(rbparentSpecimen);
	}
	/**
	 * @return Returns the rbspecimenGroup.
	 */
	public boolean getRbspecimenGroup() {
		return rbspecimenGroup.isSelected() ;
	}
	/**
	 * @param rbspecimenGroup The rbspecimenGroup to set.
	 */
	public void setRbspecimenGroup(boolean rbspecimenGroup) {
		this.rbspecimenGroup.setSelected(rbspecimenGroup);
	}
	/**
	 * @return Returns the specimenCollectionGroup.
	 */
	public String getSpecimenCollectionGroup() {
		return specimenCollectionGroup.getText() ;
	}
	/**
	 * @param specimenCollectionGroup The specimenCollectionGroup to set.
	 */
	public void setSpecimenCollectionGroup(String specimenCollectionGroup) {
		this.specimenCollectionGroup.setText(specimenCollectionGroup);
	}
	/**
	 * @return Returns the tissueSideList.
	 */
	public String getTissueSideList() {
		return tissueSideList.getSelectedItem().toString()  ;
	}
	/**
	 * @param tissueSideList The tissueSideList to set.
	 */
	public void setTissueSideList(String tissueSide) {
		this.tissueSideList.setSelectedItem(tissueSide);
	}
	/**
	 * @return Returns the tissueSiteList.
	 */
	public String getTissueSiteList() {
		return tissueSiteList.getSelectedItem().toString()  ;
	}
	/**
	 * @param tissueSiteList The tissueSiteList to set.
	 */
	public void setTissueSiteList(String tissueSite) {
		this.tissueSiteList.setSelectedItem( tissueSite);
	}
	/**
	 * @return Returns the typeList.
	 */
	public String getTypeList() {
		return typeList.getSelectedItem().toString()  ;
	}
	/**
	 * @param typeList The typeList to set.
	 */
	public void setTypeList(String type) {
		this.typeList.setSelectedItem(type);
	}
	/**
	 * @return Returns the unit.
	 */
	public String getUnit() {
		return unit.getText() ;
	}
	/**
	 * @param unit The unit to set.
	 */
	public void setUnit(String unit) {
		this.unit.setText(unit);
	}

	private void setTypeListModel(String className) {
		System.out.println("IN SCM setTypeListModel b4 tableModel");
		MultipleSpecimenTableModel model = (MultipleSpecimenTableModel)table.getModel();
		System.out.println("IN SCM setTypeListModel tableModel retrieved");
		 Object o[] = model.getSpecimenTypeValues(className);
		DefaultComboBoxModel typeComboModel = new DefaultComboBoxModel(o) ;
		this.typeList.setModel(typeComboModel);
		System.out.println("IN SCM setTypeListModel Type set");
	}

	private void setConcentrationStatus() {
		System.out.println("IN SCM enableConcentration b4 tableModel");
		MultipleSpecimenTableModel model = (MultipleSpecimenTableModel)table.getModel();
		int col = table.getSelectedColumn() ;
		this.concentration.setEnabled(model.getConcentrationStatus(col) );
		
		System.out.println("IN SCM enableConcentration concentration refreshed");
	}

	
}