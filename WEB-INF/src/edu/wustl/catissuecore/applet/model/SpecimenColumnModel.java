package edu.wustl.catissuecore.applet.model;

import java.awt.Component;

import javax.swing.AbstractCellEditor;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import edu.wustl.catissuecore.applet.AppletConstants;
import edu.wustl.catissuecore.applet.component.BaseTable;
import edu.wustl.catissuecore.applet.listener.BaseFocusHandler;
import edu.wustl.catissuecore.applet.listener.BioHazardButtonHandler;
import edu.wustl.catissuecore.applet.listener.ButtonHandler;
import edu.wustl.catissuecore.applet.listener.ClassComboBoxHandler;
import edu.wustl.catissuecore.applet.listener.CollectionGroupComboBoxHandler;
import edu.wustl.catissuecore.applet.listener.CollectionGroupItemHandler;
import edu.wustl.catissuecore.applet.listener.ComboBoxHandler;
import edu.wustl.catissuecore.applet.listener.DerivedSpecimenButtonHandler;
import edu.wustl.catissuecore.applet.listener.EventsButtonHandler;
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
	JComboBox specimenCollectionGroup;
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
//	JLabel treeLabel;
//	JPanel tissueSitePanel;
	
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

	// column no of this column
	int columnIndex;

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
		columnIndex = column;
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
		// -------------Setting tab order-----------------------
		//setTabOrder();
		
		System.out.println("SpecimenColumnModel : Col no : "+column);
		TableColumnModel columnModel = table.getColumnModel();
		System.out.println("SpecimenColumnModel columnModel.getColumnCount() : " + columnModel.getColumnCount());
		System.out.println("SpecimenColumnModel Table.getColumnCount() : " + table.getColumnCount());
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
//		System.out.println("getTableCellRendererComponent -- Text Value of R: "+row+ " ,C : "+column + " :- "+text);
		text = getComponentValue(row);
//		System.out.println("getTableCellRendererComponent -- Text Value of R: "+row+ " ,C : "+column + " :- "+text);

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
		//Mandar:05Oct06
//		System.out.println("getTableCellEditorComponent -- Text Value of R: "+row+ " ,C : "+column + " :- "+text);
		text = getComponentValue(row);
//		System.out.println("getTableCellEditorComponent -- Text Value of R: "+row+ " ,C : "+column + " :- "+text);
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
			comp = tissueSiteList;				//tissueSitePanel ;
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
		specimenCollectionGroup = new JComboBox(model.getSpecimenCollectionGroupValues());
		rbspecimenGroup = new JRadioButton();
		collectionGroupPanel = new JPanel();
		rbspecimenGroup.setSelected(true);
		
		//Parent Specimen 
		parentSpecimen = new JTextField(10);
		rbparentSpecimen = new JRadioButton();
		rbparentSpecimen.setEnabled(false );		//	temporarily disabling since issue in Map Data Parser after discussing  Santosh 
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
//		System.out.println("ImagePath : "+getClass().getClassLoader().getResource("/images/Tree.gif"));
//		ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("/images/Tree.gif"));
//		treeLabel = new JLabel(icon); 
//		tissueSitePanel = new JPanel();
//		
//		tissueSitePanel.add(tissueSiteList);
//		tissueSitePanel.add(treeLabel);

		
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
		mapButton = new JButton(AppletConstants.MULTIPLE_SPECIMEN_MAP );
		location = new JTextField(10);
		location.setEditable(false );
		mapButton.setEnabled(false); 
		storageLocationPanel = new JPanel();
		
		storageLocationPanel.add(location);
		storageLocationPanel.add(mapButton);
		//storageLocationPanel.setF 
		
		//For Comments
		comments = new JButton(AppletConstants.MULTIPLE_SPECIMEN_COMMENTS);
		
		//Events 
		eventsButton = new JButton(AppletConstants.MULTIPLE_SPECIMEN_EVENTS );
		
		//External Identifier
		externalIdentifierButton = new JButton(AppletConstants.MULTIPLE_SPECIMEN_EXTERNAL_IDENTIFIERS );
		
		//BioHazard
		bioHazardButton = new JButton(AppletConstants.MULTIPLE_SPECIMEN_BIOHAZARDS);

		//Derive
		deriveButton = new JButton(AppletConstants.MULTIPLE_SPECIMEN_DERIVE);
		deriveButton.setEnabled(false ); 

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
		
	//Mandar : 07Oct06 : commented due to problems in fetching selected row and column in focuslost event 
		BaseFocusHandler baseFocusHandler = new BaseFocusHandler(table);
		CollectionGroupComboBoxHandler collectionGroupComboBoxHandler = new CollectionGroupComboBoxHandler(table);
		
		//Specimen Collection Group
		specimenCollectionGroup.addActionListener(collectionGroupComboBoxHandler);
		rbspecimenGroup.addItemListener(collectionGroupItemHandler );
//		specimenCollectionGroup.addFocusListener(baseFocusHandler); // to set value on click.
		
		//Parent Specimen 
		parentSpecimen.addActionListener(textHandler);
		rbparentSpecimen.addItemListener(parentSpecimenItemHandler);
//		parentSpecimen.addFocusListener(baseFocusHandler);
		
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
		//testing textchanged listener
//		location.getDocument().addDocumentListener(new DocumentListener(){
//			 // This method is called after an insert into the document
//	        public void insertUpdate(DocumentEvent evt) {
////	            // Get index of newly inserted characters
////	            int off = evt.getOffset();
////	    
////	            // Get length of new inserted characters
////	            int len = evt.getLength();
////	    
////	            try {
////	                // Get inserted string
////	                String str = evt.getDocument().getText(off, len);
////	            } catch (BadLocationException e) {
////	            }
//	        }
//	    
//	        // This method is called after a removal from the document
//	        public void removeUpdate(DocumentEvent evt) {
////	            // Get starting index of removed characters
////	            int off = evt.getOffset();
////	    
////	            // Get length of removed characters
////	            int len = evt.getLength();
////	    
////	            // The removed characters are not available
//	        }
//	    
//	        // This method is called after one or more attributes have changed.
//	        // This method is not called when characters are inserted with attributes.
//	        public void changedUpdate(DocumentEvent evt) {
//	            // Get starting index of characters whose attributes have changed
//	            int off = evt.getOffset();
//	            System.out.println("inside changedUpdate of Location field.");
//	            // Get length of characters whose attributes have changed
//	            int len = evt.getLength();
//	            try {
//                // Get inserted string
//                String str = evt.getDocument().getText(off, len);
//                System.out.println("data:"+str); 
//	            } 
//	            catch (BadLocationException e)
//				{
//				}
//	        }
//		}) ;
		
		//For Comments
		comments.addActionListener(buttonHandler);
		
		//Events 
		eventsButton.addActionListener(new EventsButtonHandler(table));
		
		//External Identifier
		externalIdentifierButton.addActionListener(new ExternalIdentifierButtonHandler(table));
		
		//BioHazard
		bioHazardButton.addActionListener(new BioHazardButtonHandler(table));

		//Derive
	    deriveButton.addActionListener(new DerivedSpecimenButtonHandler(table));
	}

	public void specimenClassUpdated(String name)
	{
		//Specimen Type JComboBox typeList;
		setTypeListModel(name); 
		setConcentrationStatus();
		updateButtons();
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
		return specimenCollectionGroup.getSelectedItem().toString()   ;
	}
	/**
	 * @param specimenCollectionGroup The specimenCollectionGroup to set.
	 */
	public void setSpecimenCollectionGroup(String specimenCollectionGroup) {
		this.specimenCollectionGroup.setSelectedItem(specimenCollectionGroup);
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
	//	int col = table.getSelectedColumn() ;
		this.concentration.setEnabled(model.getConcentrationStatus(columnIndex ) );
		
		System.out.println("IN SCM enableConcentration concentration refreshed");
	}
	
	/**
	 * This method is used to update the columns as per the data in the model.
	 *
	 */
	public void updateColumn()
	{
		MultipleSpecimenTableModel model = (MultipleSpecimenTableModel)table.getModel();
		String value = "";
		//-----------------
//		Specimen Collection Group
		value = (String) model.getValueAt(AppletConstants.SPECIMEN_COLLECTION_GROUP_ROW_NO,columnIndex);
		if(!isNull(value ))
		{
			setRbspecimenGroup(true );
			setSpecimenCollectionGroup(value);
			this.specimenCollectionGroup.setEnabled(true );
			updateButtons();
		}

		//Parent Specimen 
		value = (String) model.getValueAt(AppletConstants.SPECIMEN_PARENT_ROW_NO,columnIndex);
		if(!isNull(value ))
		{
			setRbparentSpecimen( true);
			setParentSpecimen(value );
			this.parentSpecimen.setEnabled( true); 
		}
		
		// Label
		value = (String) model.getValueAt(AppletConstants.SPECIMEN_LABEL_ROW_NO,columnIndex) ;
		if(!isNull(value ))
		{
			setLabel(value);
		}
		 
		
		// Barcode
		value = (String) model.getValueAt(AppletConstants.SPECIMEN_BARCODE_ROW_NO ,columnIndex);
		if(!isNull(value ))
		{
			setBarCode(value);
		}

		//Specimen Class
		value = (String) model.getValueAt(AppletConstants.SPECIMEN_CLASS_ROW_NO ,columnIndex) ;
		if(!isNull(value ))
		{
			setClassList(value);

			//Specimen Type
			setTypeListModel(value); // classname
			value = (String) model.getValueAt(AppletConstants.SPECIMEN_TYPE_ROW_NO ,columnIndex);
			if(!isNull(value ))
				setTypeList(value );

			//set unit
			value = (String) model.getQuantityUnit(columnIndex );
			if(!isNull(value ))
				setUnit(value);
			
			//set buttons 
			updateButtons();
		}
		
		//TissueSite
		value = (String) model.getValueAt(AppletConstants.SPECIMEN_TISSUE_SITE_ROW_NO ,columnIndex) ;
		if(!isNull(value ))
		{
			setTissueSiteList(value);
		}
		
		//TissueSide
		value = (String) model.getValueAt(AppletConstants.SPECIMEN_TISSUE_SIDE_ROW_NO ,columnIndex);
		if(!isNull(value ))
		{
			setTissueSideList( value);
		}

		//PathologicalStatus
		value = (String) model.getValueAt(AppletConstants.SPECIMEN_PATHOLOGICAL_STATUS_ROW_NO ,columnIndex);
		if(!isNull(value ))
		{
			setPathologicalStatusList(value );
		}
		
		// Quantity
		value = (String) model.getValueAt(AppletConstants.SPECIMEN_QUANTITY_ROW_NO  ,columnIndex);
		if(!isNull(value ))
		{
			setQuantity(value);
		}
		
		// Concentration
		value = (String) model.getValueAt(AppletConstants.SPECIMEN_CONCENTRATION_ROW_NO ,columnIndex);
		if(!isNull(value ))
		{
			setConcentration(value );
			setConcentrationStatus() ;
		}

		//For Storage Location
//		System.out.println("Storage location key is " + model.getKey(AppletConstants.SPECIMEN_STORAGE_LOCATION_ROW_NO ,columnIndex ));
//		value = (String) model.getValueAt(AppletConstants.SPECIMEN_STORAGE_LOCATION_ROW_NO ,columnIndex);
//		System.out.println("Storage location value is " + value);
		String key = model.getMapTempKey(columnIndex );
		System.out.println("Storage location key : "+ key );
		if(!isNull(key ))
		{
			value = model.getMapTempValue(key );
			System.out.println("Storage location key : "+ key + "value is " + value+" for Column : "+columnIndex );
			if((value.trim().length()>0  ) )
			{
				setLocation(value);	
			}
		}
		// ----------------
	}
	
	private boolean isNull(Object obj)
	{
		if(obj == null)
			return true;
		else
			return false;
	}
	
	private void setTabOrder()
	{
		//Specimen Collection Group
		collectionGroupPanel.setNextFocusableComponent(rbspecimenGroup );
		rbspecimenGroup.setNextFocusableComponent(specimenCollectionGroup );
		specimenCollectionGroup.setNextFocusableComponent(rbparentSpecimen );
		
		//Parent Specimen 
		rbparentSpecimen.setNextFocusableComponent(parentSpecimen );
		parentSpecimen.setNextFocusableComponent(label );

		// Label
		label.setNextFocusableComponent(barCode );
		
		// Barcode
		barCode.setNextFocusableComponent(classList);

		//Specimen Class
		classList.setNextFocusableComponent(typeList);

		//Specimen Type
		typeList.setNextFocusableComponent(tissueSiteList);
		
		//TissueSite
		tissueSiteList.setNextFocusableComponent(tissueSideList);
		
		//TissueSide
		tissueSideList.setNextFocusableComponent(pathologicalStatusList);

		//PathologicalStatus 
		pathologicalStatusList.setNextFocusableComponent(quantity);
		
		// Quantity
		quantity.setNextFocusableComponent(concentration);

		// Concentration
		concentration.setNextFocusableComponent(mapButton);
		
		//For Storage Location
		mapButton.setNextFocusableComponent(comments);
		
		//For Comments
		comments.setNextFocusableComponent(eventsButton);
		
		//Events 
		eventsButton.setNextFocusableComponent(externalIdentifierButton);
		
		//External Identifier
		externalIdentifierButton.setNextFocusableComponent(bioHazardButton);
		
		//BioHazard
		bioHazardButton.setNextFocusableComponent(deriveButton);

		//Derive
//		deriveButton.setNextFocusableComponent();
	}

	public void setLocationFromJS(String storageValue)
	{
		System.out.println("In setLocationFromJS : Column No : "+ columnIndex );
		setLocation( storageValue);
		
	}
	
	/**
	 * Method to set the text value of various components
	 * @author mandar_deshmukh
	 *
	 * TODO To change the template for this generated type comment go to
	 * Window - Preferences - Java - Code Style - Code Templates
	 */
	private String getComponentValue(int row)
	{
		String value="";

		switch (row)
		{
		//Specimen Collection Group
		case AppletConstants.SPECIMEN_COLLECTION_GROUP_ROW_NO :
			value = specimenCollectionGroup.getSelectedItem().toString();   
			break;
		//Parent Specimen 
		case AppletConstants.SPECIMEN_PARENT_ROW_NO :
			value = parentSpecimen.getText();  
			break;
		// Label
		case AppletConstants.SPECIMEN_LABEL_ROW_NO :
			value = label.getText() ;
			break;
		// Barcode
		case AppletConstants.SPECIMEN_BARCODE_ROW_NO :
			value = barCode.getText() ;
			break;
		//Specimen Class
		case AppletConstants.SPECIMEN_CLASS_ROW_NO :
			value = classList.getSelectedItem().toString()  ;
			break;
		//Specimen Type
		case AppletConstants.SPECIMEN_TYPE_ROW_NO :
			value = typeList.getSelectedItem().toString()  ;
			break;
		//TissueSite
		case AppletConstants.SPECIMEN_TISSUE_SITE_ROW_NO :
			value = tissueSiteList.getSelectedItem().toString()  ;
			break;
		//TissueSide
		case AppletConstants.SPECIMEN_TISSUE_SIDE_ROW_NO :
			value = tissueSideList.getSelectedItem().toString()  ;
			break;
		//PathologicalStatus 
		case AppletConstants.SPECIMEN_PATHOLOGICAL_STATUS_ROW_NO : 
			value = pathologicalStatusList.getSelectedItem().toString()  ;
			break;
		// Quantity
		case AppletConstants.SPECIMEN_QUANTITY_ROW_NO :
			value = quantity.getText()  ;
			break;
		// Concentration
		case AppletConstants.SPECIMEN_CONCENTRATION_ROW_NO :
			value = concentration.getText() ;
			break;
		//For Storage Location
		case AppletConstants.SPECIMEN_STORAGE_LOCATION_ROW_NO :
			value = location.getText()   ;
			break;
		default :
			value = "";
		}		
		return value;
	}
	
//	/**
//	 * This method is used to update the state of derive and map buttons based on the collection group value. 
//	 * @param enableDeriveButton state of buttons to set. 
//	 */
//	public void collectionGroupUpdated(boolean enableDeriveButton)
//	{
//		deriveButton.setEnabled(enableDeriveButton);
//		mapButton.setEnabled(enableDeriveButton); 
//	}
	
	/**
	 * This method is used to update the state of derive and map buttons based on the collection group and class values. 
	 *
	 */
	public void updateButtons()
	{
		if(!specimenCollectionGroup.getSelectedItem().toString().equals(Constants.SELECT_OPTION) && 
			!(classList.getSelectedItem().toString().equals(Constants.SELECT_OPTION)))
		{
			deriveButton.setEnabled(true);
			mapButton.setEnabled(true); 
		}
		else
		{
			deriveButton.setEnabled(false);
			mapButton.setEnabled(false); 
		}
		SwingUtilities.updateComponentTreeUI(deriveButton);
		SwingUtilities.updateComponentTreeUI(mapButton);
	}
	
	
	/* (non-Javadoc)
	 * @see javax.swing.AbstractCellEditor#fireEditingStopped()
	 */
	protected void fireEditingStopped() {
		System.out.println("SpecimenColumnModel in fireEditingStopped  doing nothing");
		//super.fireEditingStopped();
	}
}