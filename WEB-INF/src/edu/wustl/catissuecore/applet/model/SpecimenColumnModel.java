package edu.wustl.catissuecore.applet.model;

import java.awt.Component;

import javax.swing.AbstractCellEditor;
import javax.swing.ButtonGroup;
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
import edu.wustl.catissuecore.applet.listener.ButtonHandler;
import edu.wustl.catissuecore.applet.listener.ClassComboBoxHandler;
import edu.wustl.catissuecore.applet.listener.CollectionGroupItemHandler;
import edu.wustl.catissuecore.applet.listener.ComboBoxHandler;
import edu.wustl.catissuecore.applet.listener.MapButtonHandler;
import edu.wustl.catissuecore.applet.listener.ParentSpecimenItemHandler;
import edu.wustl.catissuecore.applet.listener.TextFieldHandler;
import edu.wustl.catissuecore.applet.listener.TypeComboBoxHandler;
import edu.wustl.catissuecore.util.global.Constants;

/**
 * This is a column model for multiple specimen page.
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
	JTextField comments;
	
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

	public SpecimenColumnModel(BaseTable table, int column) 
	{
		super();
		this.table = table;
		MultipleSpecimenTableModel model = (MultipleSpecimenTableModel)table.getModel();
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
		//Specimen Collection Group
		specimenCollectionGroup = new JTextField(10);
		rbspecimenGroup = new JRadioButton();
		collectionGroupPanel = new JPanel();
		
		//Parent Specimen 
		parentSpecimen = new JTextField(10);
		rbparentSpecimen = new JRadioButton();
		parentSpecimenPanel = new JPanel();
		
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
		typeList = new JComboBox(type);
		
		
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
		comments = new JTextField(10);
		
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

		// --------------- Adding Listeners ---------------------
		// Listeners
		TextFieldHandler textHandler = new TextFieldHandler(table); 
		ParentSpecimenItemHandler parentSpecimenItemHandler = new ParentSpecimenItemHandler(table);
		CollectionGroupItemHandler collectionGroupItemHandler = new CollectionGroupItemHandler(table);
		ClassComboBoxHandler classComboBoxHandler = new ClassComboBoxHandler(table);
		TypeComboBoxHandler typeComboBoxHandler = new TypeComboBoxHandler(table);
		ComboBoxHandler comboBoxHandler = new ComboBoxHandler(table);
		MapButtonHandler mapButtonHandler = new MapButtonHandler(table);
		ButtonHandler buttonHandler = new ButtonHandler(table);
		
		//Specimen Collection Group
		specimenCollectionGroup.addActionListener(textHandler);
		rbspecimenGroup.addItemListener(collectionGroupItemHandler );
		
		//Parent Specimen 
		parentSpecimen.addActionListener(textHandler);
		rbparentSpecimen.addItemListener(parentSpecimenItemHandler);
		
		// Label
		label.addActionListener(textHandler);
		
		// Barcode
		barCode.addActionListener(textHandler);

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

		// Concentration
		concentration.addActionListener(textHandler);
		
		//For Storage Location
		mapButton.addActionListener(mapButtonHandler);
		
		//For Comments
		comments.addActionListener(textHandler);
		
		//Events 
		eventsButton.addActionListener(buttonHandler);
		
		//External Identifier
		externalIdentifierButton.addActionListener(buttonHandler);
		
		//BioHazard
		bioHazardButton.addActionListener(buttonHandler);

		//Derive
		deriveButton.addActionListener(buttonHandler);
		// ------------------------------------

		TableColumnModel columnModel = table.getColumnModel();
		columnModel.getColumn(column).setCellRenderer(this);
		columnModel.getColumn(column).setCellEditor(this);
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
			boolean hasFocus, int row, int column)
	{
		text = (value == null) ? "" : value.toString();

		Component component = getComponentAt(row, column, hasFocus, isSelected);

/*	if (component instanceof JComboBox)
			((JComboBox) component).setSelectedItem((value == null) ? "" : value.toString());
		if (component instanceof JTextField)
		{
			System.out.println(((JTextField) component).getText());
		}

*/		return component;
	}

	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected,
			int row, int column)
	{
		text = (value == null) ? "" : value.toString();

		Component component = getComponentAt(row, column, false, isSelected);

/*		if (component instanceof JComboBox)
			((JComboBox) component).setSelectedItem((value == null) ? "" : value.toString());
*/
		return component;
	}

	/**
	 * @see javax.swing.CellEditor#getCellEditorValue()
	 */
	public Object getCellEditorValue()
	{
		return text;
	}

//	public void actionPerformed(ActionEvent e)
//	{
//
//		fireEditingStopped();
//		table.getModel().setValueAt(selectedValue, table.getSelectedRow(),
//				table.getSelectedColumn());
//	}
	

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
		// ------------------
//		Specimen Collection Group
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
		// ------------------

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

		//classList.setSelectedItem( (value == null) ? "" : value.toString() );
		return comp;
	}

}