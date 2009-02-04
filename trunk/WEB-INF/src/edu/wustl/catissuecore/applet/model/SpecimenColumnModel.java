
package edu.wustl.catissuecore.applet.model;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.Map;

import javax.swing.AbstractCellEditor;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
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
import edu.wustl.catissuecore.applet.listener.SpecimenCheckBoxHandler;
import edu.wustl.catissuecore.applet.listener.TextFieldHandler;
import edu.wustl.catissuecore.applet.listener.TissueSiteTreeMapButtonHandler;
import edu.wustl.catissuecore.applet.listener.TypeComboBoxHandler;
import edu.wustl.catissuecore.applet.ui.ModifiedButton;
import edu.wustl.catissuecore.applet.ui.ModifiedComboBox;
import edu.wustl.catissuecore.applet.ui.ModifiedTextField;
import edu.wustl.catissuecore.util.global.Constants;

/**
 * This Class is a column model for multiple specimen page.
 *  
 * @author mandar_deshmukh
 */
public class SpecimenColumnModel extends AbstractCellEditor implements TableCellRenderer, TableCellEditor
{

	/**
	 * Default Serial Version ID
	 */
	private static final long serialVersionUID = 1L;

	private final int HGAP = 1, VGAP = 0;
	//Reference of Table
	BaseTable table;

	// Fields as per Model.

	//For Specimen Checkbox
	JCheckBox specimenCheckBox;

	//Specimen Collection Group
	ModifiedComboBox specimenCollectionGroup;
	JRadioButton rbspecimenGroup;
	JPanel collectionGroupPanel;

	//Parent Specimen 
	ModifiedTextField parentSpecimen;
	JRadioButton rbparentSpecimen;
	JPanel parentSpecimenPanel;

	//Group for radiobuttons
	ButtonGroup radioGroup;

	// Label
	ModifiedTextField label;

	// Barcode
	ModifiedTextField barCode;

	//Specimen Class
	ModifiedComboBox classList;

	//Specimen Type
	ModifiedComboBox typeList;

	//TissueSite
	ModifiedComboBox tissueSiteList;
	//JLabel treeLabel;
	JButton tissueSiteTreeButton;
	JPanel tissueSitePanel;

	//TissueSide
	ModifiedComboBox tissueSideList;

	//PathologicalStatus 
	ModifiedComboBox pathologicalStatusList;

	// Quantity
	ModifiedTextField quantity;
	JLabel unit;
	JPanel quantityUnitPanel;

	// Concentration
	ModifiedTextField concentration;

	//For Storage Location
	//	Mandar: 06Nov06: location removed since auto allocation will take place.
	//	JButton mapButton;
	//	JTextField location;
	//	JPanel storageLocationPanel;

	//For Comments
	ModifiedButton comments;
	JPanel commentsPanel;

	//Events 
	ModifiedButton eventsButton;
	JPanel eventsPanel;

	//External Identifier
	ModifiedButton externalIdentifierButton;
	JPanel externalIdentifierPanel;

	//BioHazard
	ModifiedButton bioHazardButton;
	JPanel bioHazardPanel;

	//Derive
	ModifiedButton deriveButton;
	JPanel derivePanel;

	// For holding component value
	String text = "";

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
		MultipleSpecimenTableModel model = (MultipleSpecimenTableModel) table.getModel();
		columnIndex = column;
		// set row height based on contents	
		for (int rowno = 0; rowno < table.getRowCount(); rowno++)
		{
			//For combobox
			if (rowno == AppletConstants.SPECIMEN_CHECKBOX_ROW_NO)
			{
				table.setRowHeight(rowno, 20);
			}
			else if (rowno == AppletConstants.SPECIMEN_CLASS_ROW_NO || rowno == AppletConstants.SPECIMEN_TYPE_ROW_NO
					|| rowno == AppletConstants.SPECIMEN_TISSUE_SIDE_ROW_NO || rowno == AppletConstants.SPECIMEN_PATHOLOGICAL_STATUS_ROW_NO)
			{
				table.setRowHeight(rowno, 25);
			}
			//for textfield
			else if (rowno == AppletConstants.SPECIMEN_LABEL_ROW_NO || rowno == AppletConstants.SPECIMEN_BARCODE_ROW_NO
					|| rowno == AppletConstants.SPECIMEN_CONCENTRATION_ROW_NO)
			{
				table.setRowHeight(rowno, 20);
			}
			//for panels
			else if (rowno == AppletConstants.SPECIMEN_COLLECTION_GROUP_ROW_NO || rowno == AppletConstants.SPECIMEN_PARENT_ROW_NO
					|| rowno == AppletConstants.SPECIMEN_QUANTITY_ROW_NO || rowno == AppletConstants.SPECIMEN_TISSUE_SITE_ROW_NO)
			{
				table.setRowHeight(rowno, 35);
			}
			//for others (Buttons etc)
			else
			{
				//rowno == AppletConstants.SPECIMEN_COMMENTS_ROW_NO || rowno == AppletConstants.SPECIMEN_STORAGE_LOCATION_ROW_NO 
				table.setRowHeight(rowno, 30);
			}
		}
		// ---------------- Object creation --------------------
		instantiateObjects(model, column);
		// --------------- Adding Listeners ---------------------
		addListeners();
		// -------------Setting tab order-----------------------
		//setTabOrder();

		System.out.println("SpecimenColumnModel : Col no : " + column);
		TableColumnModel columnModel = table.getColumnModel();
		System.out.println("SpecimenColumnModel columnModel.getColumnCount() : " + columnModel.getColumnCount());
		System.out.println("SpecimenColumnModel Table.getColumnCount() : " + table.getColumnCount());
		columnModel.getColumn(column).setCellRenderer(this);
		columnModel.getColumn(column).setCellEditor(this);
		columnModel.getColumn(column).setResizable(false);
		columnModel.getColumn(column).setPreferredWidth(175);

	}

	/**
	 *  This method returns the component used as cell renderer. 
	 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
	{
		//		System.out.println("getTableCellRendererComponent(table, value: "+ value+" , isSelected: " +isSelected+" , hasFocus: "+hasFocus + ", row: "+row+ " , column: "+column);
		text = (value == null) ? "" : value.toString();
		Component component = getComponentAt(row, column, hasFocus, isSelected);
		//		System.out.println("getTableCellRendererComponent -- Text Value of R: "+row+ " ,C : "+column + " :- "+text);
		text = getComponentValue(row);
		//		System.out.println("getTableCellRendererComponent -- Text Value of R: "+row+ " ,C : "+column + " :- "+text);
		setActiveElement(component, "Renderer");
		//to display tooltip
		ToolTipManager.sharedInstance().setInitialDelay(0);
		((JComponent) component).setToolTipText(text);
		return component;
	}

	/** 
	 * This method returns the component used as cell editor.
	 * @see javax.swing.table.TableCellEditor#getTableCellEditorComponent(javax.swing.JTable, java.lang.Object, boolean, int, int)
	 */
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column)
	{
		System.out.println("getTableCellEditorComponent(table, value: " + value + " , isSelected: " + isSelected + " , row: " + row + " , column: "
				+ column);
		text = (value == null) ? "" : value.toString();
		boolean hasFocus = isSelected;
		Component component = getComponentAt(row, column, hasFocus, isSelected);
		//Mandar:05Oct06
		//		System.out.println("getTableCellEditorComponent -- Text Value of R: "+row+ " ,C : "+column + " :- "+text);
		text = getComponentValue(row);
		//		System.out.println("getTableCellEditorComponent -- Text Value of R: "+row+ " ,C : "+column + " :- "+text);
		setActiveElement(component, "Editor");
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
			//Specimencheckbox
			case AppletConstants.SPECIMEN_CHECKBOX_ROW_NO :
				comp = specimenCheckBox;
				break;

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
				comp = tissueSitePanel; //tissueSiteList;  30Oct06 : Mandar to display tree map.				
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
			/*
			 * Code commented as storage location will be auto allocated.
			 * 
			 case AppletConstants.SPECIMEN_STORAGE_LOCATION_ROW_NO :
			 comp = storageLocationPanel;
			 break;
			 
			 */

			//For Comments
			case AppletConstants.SPECIMEN_COMMENTS_ROW_NO :
				comp = commentsPanel;
				break;
			//Events 
			case AppletConstants.SPECIMEN_EVENTS_ROW_NO :
				comp = eventsPanel;
				break;
			//External Identifier
			case AppletConstants.SPECIMEN_EXTERNAL_IDENTIFIERS_ROW_NO :
				comp = externalIdentifierPanel;
				break;
			//BioHazard
			case AppletConstants.SPECIMEN_BIOHAZARDS_ROW_NO :
				comp = bioHazardPanel;
				break;
			//Derive
			case AppletConstants.SPECIMEN_DERIVE_ROW_NO :
				comp = derivePanel;
				break;
			default :
				comp = new ModifiedTextField(10);
		}

		formatComponent(comp, hasFocus, isSelected);
		return comp;
	}

	/*
	 * This method formats the component as per the parameters provided.
	 */
	private void formatComponent(Component comp, boolean hasFocus, boolean isSelected)
	{
		if(comp != specimenCheckBox )
		{
		if (hasFocus)
		{
			comp.setForeground(table.getForeground());
			comp.setBackground(UIManager.getColor("List.background"));
			//			20Oct06<*>			comp.requestFocusInWindow();
			comp.requestFocus();
		}
		if (isSelected)
		{
			comp.setForeground(table.getSelectionForeground());
			comp.setBackground(table.getSelectionBackground());
			//			20Oct06<*>	comp.requestFocusInWindow();
			comp.requestFocus();
		}
		else
		{
			comp.setForeground(table.getForeground());
			comp.setBackground(UIManager.getColor("List.background"));
		}
		comp.repaint();
	}
	}

	/*
	 * This method initialises the components.
	 */
	private void instantiateObjects(MultipleSpecimenTableModel model, int column)
	{
		// specimen check box
		specimenCheckBox = new JCheckBox(model.getColumnName(column), model.getSpecimenCheckBoxValueAt(column));
		specimenCheckBox.setActionCommand("" + (column + 1));
		specimenCheckBox.setBackground(Color.lightGray  );
		specimenCheckBox.setOpaque(true );

		//Specimen Collection Group
		specimenCollectionGroup = new ModifiedComboBox(model.getSpecimenCollectionGroupValues());
		specimenCollectionGroup.setPreferredSize(new Dimension(150, (int) specimenCollectionGroup.getPreferredSize().getHeight()));

		if (model.getSpecimenCollectionGroupName() != null)
		{
			specimenCollectionGroup.setSelectedItem(model.getSpecimenCollectionGroupName());

		}
		rbspecimenGroup = new JRadioButton();
		collectionGroupPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, HGAP, VGAP));
		rbspecimenGroup.setSelected(true);

		//Parent Specimen 
		parentSpecimen = new ModifiedTextField(10);
		rbparentSpecimen = new JRadioButton();
		parentSpecimen.setPreferredSize(new Dimension(100, (int) specimenCollectionGroup.getPreferredSize().getHeight()));
		//rbparentSpecimen.setEnabled(false);
		parentSpecimenPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, HGAP, VGAP));
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
		label = new ModifiedTextField(10);

		// Barcode
		barCode = new ModifiedTextField(10);

		//Specimen Class
		classList = new ModifiedComboBox(model.getSpecimenClassValues());

		String type[] = {Constants.SELECT_OPTION};
		//Specimen Type
		typeList = new ModifiedComboBox(model.getSpecimenTypeValues(null));

		//TissueSite
		tissueSiteList = new ModifiedComboBox(model.getTissueSiteValues());
		tissueSiteList.setPreferredSize(new Dimension(150, (int) specimenCollectionGroup.getPreferredSize().getHeight()));
		//to display notspecified by default
		if (model.getSpecimenCollectionGroupName() == null)
		{
			tissueSiteList.setSelectedItem(Constants.NOTSPECIFIED);
		}

		//Mandar : 30Oct06 : To display Tissue Site Tree
		System.out.println("ImagePath : " + getClass().getClassLoader().getResource("images/Tree.gif"));
		ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("images/Tree.gif"));
		System.out.println("icon:" + icon);
		//treeLabel = new JLabel(icon);
		tissueSiteTreeButton = new JButton(icon);
		tissueSiteTreeButton.setPreferredSize(new Dimension(20, 28));
		tissueSitePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, HGAP, VGAP));

		tissueSitePanel.add(tissueSiteList);
		tissueSitePanel.add(tissueSiteTreeButton);
		//		tissueSitePanel.add(treeLabel);

		//TissueSide
		tissueSideList = new ModifiedComboBox(model.getTissueSideValues());
		//to display notspecified by default
		tissueSideList.setSelectedItem(Constants.NOTSPECIFIED);

		//PathologicalStatus 
		pathologicalStatusList = new ModifiedComboBox(model.getPathologicalStatusValues());
		//to display notspecified by default
		pathologicalStatusList.setSelectedItem(Constants.NOTSPECIFIED);
	/*	if (model.getSpecimenCollectionGroupName() == null)
		{
			pathologicalStatusList.setSelectedItem(Constants.NOTSPECIFIED);
		}
		else
		{
			pathologicalStatusList.setSelectedItem(model.getPathologicalStatus());
		}*/

		// Quantity
		quantity = new ModifiedTextField("0", 17);
		unit = new JLabel();
		quantity.setPreferredSize(new Dimension(110, (int) specimenCollectionGroup.getPreferredSize().getHeight()));
		quantityUnitPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, HGAP, VGAP));

		quantityUnitPanel.add(quantity);
		quantityUnitPanel.add(unit);

		// Concentration
		concentration = new ModifiedTextField(10);

//		if (model.getSpecimenCollectionGroupName() != null)
//		{
//			classList.setSelectedItem(model.getSpecimenClass());
//			specimenClassUpdated(model.getSpecimenClass());
//			typeList.setSelectedItem(model.getSpecimenType());
//			tissueSiteList.setSelectedItem(model.getTissueSite());
//		}

		//For Storage Location
		// Mandar : 06Nov06 : 	Location removed since auto allocation will take place		
		//		mapButton = new JButton(AppletConstants.MULTIPLE_SPECIMEN_MAP);
		//		location = new JTextField(13);
		//		location.setPreferredSize(new Dimension(120, (int) specimenCollectionGroup
		//				.getPreferredSize().getHeight()));
		//		location.setEditable(false);
		//	Mandar: 06Nov06: location removed since auto allocation will take place.
		//		//Mandar : 30Oct06 : Map button enabled by default.
		//		//		mapButton.setEnabled(false); 
		//		storageLocationPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, HGAP, VGAP));
		//
		//		storageLocationPanel.add(location);
		//		storageLocationPanel.add(mapButton);
		//		//storageLocationPanel.setF 

		//For Comments

		Map buttonStatusMap = model.getButtonStatusMap();
		int actualPageNo = model.getActualColumnNo(columnIndex);
		String label = (String) buttonStatusMap.get(actualPageNo + AppletConstants.MULTIPLE_SPECIMEN_BUTTON_MAP_KEY_SEPARATOR
				+ AppletConstants.MULTIPLE_SPECIMEN_COMMENTS_STRING);
		if (label == null)
		{
			label = AppletConstants.ADD;
		}

		comments = new ModifiedButton(label);
		commentsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, HGAP, VGAP));
		commentsPanel.add(comments);

		//Events 

		label = (String) buttonStatusMap.get(actualPageNo + AppletConstants.MULTIPLE_SPECIMEN_BUTTON_MAP_KEY_SEPARATOR
				+ AppletConstants.MULTIPLE_SPECIMEN_EVENTS_STRING);
		if (label == null)
		{
			label = AppletConstants.ADD;
		}

		eventsButton = new ModifiedButton(label);
		eventsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, HGAP, VGAP));
		eventsPanel.add(eventsButton);

		//External Identifier
		label = (String) buttonStatusMap.get(actualPageNo + AppletConstants.MULTIPLE_SPECIMEN_BUTTON_MAP_KEY_SEPARATOR
				+ AppletConstants.MULTIPLE_SPECIMEN_EXTERNAL_IDENTIFIERS_STRING);
		if (label == null)
		{
			label = AppletConstants.ADD;
		}

		externalIdentifierButton = new ModifiedButton(label);
		externalIdentifierPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, HGAP, VGAP));
		externalIdentifierPanel.add(externalIdentifierButton);

		//BioHazard
		label = (String) buttonStatusMap.get(actualPageNo + AppletConstants.MULTIPLE_SPECIMEN_BUTTON_MAP_KEY_SEPARATOR
				+ AppletConstants.MULTIPLE_SPECIMEN_BIOHAZARDS_STRING);
		if (label == null)
		{
			label = AppletConstants.ADD;
		}

		bioHazardButton = new ModifiedButton(label);

		bioHazardPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, HGAP, VGAP));
		bioHazardPanel.add(bioHazardButton);

		//Derive
		label = (String) buttonStatusMap.get(actualPageNo + AppletConstants.MULTIPLE_SPECIMEN_BUTTON_MAP_KEY_SEPARATOR
				+ AppletConstants.MULTIPLE_SPECIMEN_DERIVE_STRING);
		if (label == null)
		{
			label = AppletConstants.ADD;
		}

		deriveButton = new ModifiedButton(label);

		//Mandar : 30Oct06 : Map button enabled by default.
		//		deriveButton.setEnabled(false );
		derivePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, HGAP, VGAP));
		derivePanel.add(deriveButton);

		// For holding component value
		text = new String();

		//AddFocus Traversal 11-Dec-06
		//		collectionGroupPanel.setFocusTraversalPolicy( new MyOwnFocusTraversalPolicy(collectionGroupPanel) );
		//		parentSpecimenPanel.setFocusTraversalPolicy( new MyOwnFocusTraversalPolicy(parentSpecimenPanel) );
		//		tissueSitePanel.setFocusTraversalPolicy( new MyOwnFocusTraversalPolicy(tissueSitePanel) );

		//TODO
		//to be removed once keyevents handling is done. Evaluation only.
		//		if(classList.getInputMap() != null)
		//		{
		//			System.out.println("InputMap18Dec06\n");
		//			KeyStroke ks[] = classList.getInputMap().allKeys();
		//			if(ks != null)
		//			for(int i=0;i<ks.length;i++)
		//			{
		//				Object o = classList.getInputMap().get(ks[i] );
		//				if(o != null)
		//					System.out.println(ks[i].getKeyChar()+ " : "+ o.toString()  );
		//			}
		//		}
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
		MapButtonHandler mapButtonHandler = new MapButtonHandler(table, rbspecimenGroup);
		ButtonHandler buttonHandler = new ButtonHandler(table);
		BaseFocusHandler baseFocusHandler = new BaseFocusHandler(table);
		CollectionGroupComboBoxHandler collectionGroupComboBoxHandler = new CollectionGroupComboBoxHandler(table);
		//For TissueSite Tree
		TissueSiteTreeMapButtonHandler tissueSiteTreeMapButtonHandler = new TissueSiteTreeMapButtonHandler(table);

		//******Specimen checkbox
		SpecimenCheckBoxHandler specimenCheckBoxHandler = new SpecimenCheckBoxHandler(table);

		//specimen checkbox
		specimenCheckBox.addItemListener(specimenCheckBoxHandler);

		//Specimen Collection Group
		specimenCollectionGroup.addActionListener(collectionGroupComboBoxHandler);
		//		specimenCollectionGroup.addFocusListener(new FocusAdapter()
		//				{
		//					public void focusGained(FocusEvent fe)
		//					{
		//						specimenCollectionGroup.showPopup(); 				
		//					}
		//					public void focusLost(FocusEvent fe)
		//					{
		//						specimenCollectionGroup.hidePopup();  
		//					}
		//
		//				});
		//		specimenCollectionGroup.addKeyListener(new KeyAdapter()
		//		{
		//
		//			public void keyPressed(KeyEvent e)
		//			{
		//				System.out
		//						.println("\n::::::::::::::::::::::   Inside keyPressed of SCG ***************\n");
		//				System.out.println("e.getKeyCode() : " + e.getKeyCode() + " , e.getKeyChar():"
		//						+ e.getKeyChar() + " : KeyEvent.VK_DOWN : " + KeyEvent.VK_DOWN);
		//				System.out.println("specimenCollectionGroup.isPopupVisible() : "
		//						+ specimenCollectionGroup.isPopupVisible()+ " e.isAltDown() : "+ e.isAltDown());
		//				if (!specimenCollectionGroup.isPopupVisible())
		//				{
		//					if (e.getKeyCode() == KeyEvent.VK_DOWN && e.isAltDown())
		//					{
		//						//						20Oct06<*>				FocusListener focusListeners[] = specimenCollectionGroup.getFocusListeners();
		//						FocusListener focusListeners[] = (FocusListener[]) specimenCollectionGroup
		//								.getListeners(FocusAdapter.class);
		//						for (int i = 0; i < focusListeners.length; i++)
		//						{
		//							FocusListener listener = focusListeners[i];
		//							listener.focusLost(new FocusEvent(specimenCollectionGroup,
		//									FocusEvent.FOCUS_LOST));
		//						}
		//						specimenCollectionGroup.getParent().requestFocus();
		//						System.out.println("\n FOCUS Transfered\n");
		//					}
		//				}
		//			}
		//		});

		rbspecimenGroup.addItemListener(collectionGroupItemHandler);
		//		collectionGroupPanel.addFocusListener(new FocusAdapter()
		//		{
		//
		//			public void focusGained(FocusEvent fe)
		//			{
		//				System.out.println("Inside focusGained by Panel");
		//				if (specimenCollectionGroup.isEnabled())
		//				{
		//					//					20Oct06<*>	FocusListener focusListeners[] = specimenCollectionGroup.getFocusListeners();
		//					FocusListener focusListeners[] = (FocusListener[]) specimenCollectionGroup
		//							.getListeners(FocusAdapter.class);
		//					for (int i = 0; i < focusListeners.length; i++)
		//					{
		//						FocusListener listener = focusListeners[i];
		//						listener.focusGained(new FocusEvent(specimenCollectionGroup,
		//								FocusEvent.FOCUS_GAINED));
		//						//uncommented to check drop down of combo using keyboard
		//												//PopupMenuListener []pl = specimenCollectionGroup.getPopupMenuListeners();
		//						PopupMenuListener pl[] = (PopupMenuListener[]) specimenCollectionGroup
		//						.getListeners(PopupMenuListener.class);
		//												for(int j=0;j<pl.length; j++ )
		//												{
		//													PopupMenuListener plist = pl[j];
		//													plist.popupMenuWillBecomeVisible(new PopupMenuEvent(specimenCollectionGroup )   ); 
		//												}
		//
		//					}
		//					specimenCollectionGroup.showPopup();
		//
		//					System.out.println("Focus set on SCG : ");
		//				}
		//			}
		//		});
		//		specimenCollectionGroup.addFocusListener(baseFocusHandler); // to set value on click.

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
		tissueSiteTreeButton.addActionListener(tissueSiteTreeMapButtonHandler);

		//TissueSide
		tissueSideList.addActionListener(comboBoxHandler);
		//----------------Mandar 12-dec-06 start
		//		tissueSideList.addKeyListener(new KeyAdapter()
		//				{
		//
		//					public void keyPressed(KeyEvent e)
		//					{
		//						System.out
		//								.println("\n::::::::::::::::::::::   Inside keyPressed of SCG ***************\n");
		//						System.out.println("e.getKeyCode() : " + e.getKeyCode() + " , e.getKeyChar():"
		//								+ e.getKeyChar() + " : KeyEvent.VK_DOWN : " + KeyEvent.VK_DOWN);
		//						System.out.println("specimenCollectionGroup.isPopupVisible() : "
		//								+ specimenCollectionGroup.isPopupVisible()+ " e.isAltDown() : "+ e.isAltDown());
		//						if (!tissueSideList.isPopupVisible())
		//						{
		//							if (e.getKeyCode() == KeyEvent.VK_DOWN && e.isAltDown())
		//							{
		////								//						20Oct06<*>				FocusListener focusListeners[] = specimenCollectionGroup.getFocusListeners();
		////								FocusListener focusListeners[] = (FocusListener[]) tissueSideList
		////										.getListeners(FocusAdapter.class);
		////								for (int i = 0; i < focusListeners.length; i++)
		////								{
		////									FocusListener listener = focusListeners[i];
		////									listener.focusLost(new FocusEvent(specimenCollectionGroup,
		////											FocusEvent.FOCUS_LOST));
		////								}
		//								tissueSideList.showPopup(); 
		//								System.out.println("\n FOCUS Transfered\n");
		//							}
		//						}
		//					}
		//				});

		//----------------Mandar 12-dec-06 end

		//PathologicalStatus 
		pathologicalStatusList.addActionListener(comboBoxHandler);

		// Quantity
		quantity.addActionListener(textHandler);
		quantity.addFocusListener(baseFocusHandler);

		// Concentration
		concentration.addActionListener(textHandler);
		concentration.addFocusListener(baseFocusHandler);

		//For Comments
		comments.addActionListener(buttonHandler);

		//Events 
		eventsButton.addActionListener(new EventsButtonHandler(table));

		//External Identifier
		externalIdentifierButton.addActionListener(new ExternalIdentifierButtonHandler(table));

		//BioHazard
		bioHazardButton.addActionListener(new BioHazardButtonHandler(table));

		//Derive
		deriveButton.addActionListener(new DerivedSpecimenButtonHandler(table, rbspecimenGroup));
	}

	public void specimenClassUpdated(String name)
	{
		//Specimen Type ModifiedComboBox typeList;
		setTypeListModel(name);
		setConcentrationStatus();
		//Mandar: 30Oct06: commented as buttons are enabled by default.
		//		updateButtons();
		MultipleSpecimenTableModel model = (MultipleSpecimenTableModel) table.getModel();
		setUnit(model.getQuantityUnit(columnIndex));
        
		refreshComponent(unit);
	}

	//--------------- GETTER SETTER ------------------------------------
	/**
	 * @return Returns the barCode.
	 */
	public String getBarCode()
	{
		return barCode.getText();
	}

	/**
	 * @param barCode The barCode to set.
	 */
	public void setBarCode(String barCode)
	{
		this.barCode.setText(barCode);
	}

	/**
	 * @return Returns the classList.
	 */
	public String getClassList()
	{
		return classList.getSelectedItem().toString();
	}

	/**
	 * @param classList The classList to set.
	 */
	public void setClassList(String className)
	{
		this.classList.setSelectedItem(className);
	}

	/**
	 * @return Returns the comments.
	 */
	public String getComments()
	{
		return comments.getText();
	}

	/**
	 * @return Returns the comments.
	 */
	public String getCommentsLabel()
	{
		return comments.getLabel();
	}

	/**
	 * @return Returns the comments.
	 */
	public String getEventsLabel()
	{
		return eventsButton.getLabel();
	}

	/**
	 * @return Returns the comments.
	 */
	public String getBiohazardsLable()
	{
		return bioHazardButton.getLabel();
	}

	/**
	 * @return Returns the comments.
	 */
	public String getExtenalidentifierLable()
	{
		return externalIdentifierButton.getLabel();
	}

	/**
	 * @return Returns the comments.
	 */
	public String getDerivedLabel()
	{
		return deriveButton.getLabel();
	}

	/**
	 * @param comments The comments to set.
	 */
	public void setComments(String comments)
	{
		this.comments.setText(comments);
	}

	/**
	 * @return Returns the concentration.
	 */
	public String getConcentration()
	{
		return concentration.getText();
	}

	/**
	 * @param concentration The concentration to set.
	 */
	public void setConcentration(String concentration)
	{
		this.concentration.setText(concentration);
	}

	/**
	 * @return Returns the label.
	 */
	public String getLabel()
	{
		return label.getText();
	}

	/**
	 * @param label The label to set.
	 */
	public void setLabel(String label)
	{
		this.label.setText(label);
	}

	//	Mandar: 06Nov06: location removed since auto allocation will take place.
	/**
	 * @return Returns the location.
	 */
	//	public String getLocation()
	//	{
	//		return location.getText();
	//	}
	/**
	 * @param location The location to set.
	 */
	//	public void setLocation(String location)
	//	{
	//		this.location.setText(location);
	//	}
	/**
	 * @return Returns the parentSpecimen.
	 */
	public String getParentSpecimen()
	{
		return parentSpecimen.getText();
	}

	/**
	 * @param parentSpecimen The parentSpecimen to set.
	 */
	public void setParentSpecimen(String parentSpecimen)
	{
		this.parentSpecimen.setText(parentSpecimen);
	}

	/**
	 * @return Returns the pathologicalStatusList.
	 */
	public String getPathologicalStatusList()
	{
		return pathologicalStatusList.getSelectedItem().toString();
	}

	/**
	 * @param pathologicalStatusList The pathologicalStatusList to set.
	 */
	public void setPathologicalStatusList(String pathologicalStatus)
	{
		this.pathologicalStatusList.setSelectedItem(pathologicalStatus);
	}

	/**
	 * @return Returns the quantity.
	 */
	public String getQuantity()
	{
		return quantity.getText();
	}

	/**
	 * @param quantity The quantity to set.
	 */
	public void setQuantity(String quantity)
	{
		this.quantity.setText(quantity);
	}

	/**
	 * @return Returns the rbparentSpecimen.
	 */
	public boolean getRbparentSpecimen()
	{
		return rbparentSpecimen.isSelected();
	}

	/**
	 * @param rbparentSpecimen The rbparentSpecimen to set.
	 */
	public void setRbparentSpecimen(boolean rbparentSpecimen)
	{
		this.rbparentSpecimen.setSelected(rbparentSpecimen);
	}

	/**
	 * @return Returns the rbspecimenGroup.
	 */
	public boolean getRbspecimenGroup()
	{
		return rbspecimenGroup.isSelected();
	}

	/**
	 * @param rbspecimenGroup The rbspecimenGroup to set.
	 */
	public void setRbspecimenGroup(boolean rbspecimenGroup)
	{
		this.rbspecimenGroup.setSelected(rbspecimenGroup);
	}

	/**
	 * @return Returns the specimenCollectionGroup.
	 */
	public String getSpecimenCollectionGroup()
	{
		return specimenCollectionGroup.getSelectedItem().toString();
	}

	/**
	 * @param specimenCollectionGroup The specimenCollectionGroup to set.
	 */
	public void setSpecimenCollectionGroup(String specimenCollectionGroup)
	{
		this.specimenCollectionGroup.setSelectedItem(specimenCollectionGroup);
	}

	/**
	 * @return Returns the tissueSideList.
	 */
	public String getTissueSideList()
	{
		return tissueSideList.getSelectedItem().toString();
	}

	/**
	 * @param tissueSideList The tissueSideList to set.
	 */
	public void setTissueSideList(String tissueSide)
	{
		this.tissueSideList.setSelectedItem(tissueSide);
	}

	/**
	 * @return Returns the tissueSiteList.
	 */
	public String getTissueSiteList()
	{
		return tissueSiteList.getSelectedItem().toString();
	}

	/**
	 * @param tissueSiteList The tissueSiteList to set.
	 */
	public void setTissueSiteList(String tissueSite)
	{
		this.tissueSiteList.setSelectedItem(tissueSite);
	}

	/**
	 * @return Returns the typeList.
	 */
	public String getTypeList()
	{
		return typeList.getSelectedItem().toString();
	}

	/**
	 * @param typeList The typeList to set.
	 */
	public void setTypeList(String type)
	{
		this.typeList.setSelectedItem(type);
	}

	/**
	 * @return Returns the unit.
	 */
	public String getUnit()
	{
		return unit.getText();
	}

	/**
	 * @param unit The unit to set.
	 */
	public void setUnit(String unit)
	{
		this.unit.setText(unit);
	}

	private void setTypeListModel(String className)
	{
		System.out.println("IN SCM setTypeListModel b4 tableModel");
		MultipleSpecimenTableModel model = (MultipleSpecimenTableModel) table.getModel();
		System.out.println("IN SCM setTypeListModel tableModel retrieved");
		Object o[] = model.getSpecimenTypeValues(className);
		if (o != null)
		{
			DefaultComboBoxModel typeComboModel = new DefaultComboBoxModel(o);
			this.typeList.setModel(typeComboModel);
		}
		System.out.println("IN SCM setTypeListModel Type set");
	}

	private void setConcentrationStatus()
	{
		System.out.println("IN SCM enableConcentration b4 tableModel");
		MultipleSpecimenTableModel model = (MultipleSpecimenTableModel) table.getModel();
		//	int col = table.getSelectedColumn() ;
		this.concentration.setEnabled(model.getConcentrationStatus(columnIndex));

		System.out.println("IN SCM enableConcentration concentration refreshed");
	}

	/**
	 * This method is used to update the columns as per the data in the model.
	 *
	 */
	public void updateColumn()
	{
		MultipleSpecimenTableModel model = (MultipleSpecimenTableModel) table.getModel();
		String value = "";
		//-----------------
		//		Specimen Collection Group
		value = (String) model.getValueAt(AppletConstants.SPECIMEN_COLLECTION_GROUP_ROW_NO, columnIndex);
		if (model.getCollectionGroupRadioButtonValueAt(columnIndex))
		{
			setRbspecimenGroup(true);
			if (!isNull(value))
			{
				setSpecimenCollectionGroup(value);
				this.specimenCollectionGroup.setEnabled(true);
				//			Mandar: 30Oct06: commented as buttons are enabled by default.
				//			updateButtons();
			}

		}
		else
		{
			setRbparentSpecimen(true);
		}
		//Parent Specimen 
		value = (String) model.getValueAt(AppletConstants.SPECIMEN_PARENT_ROW_NO, columnIndex);
		if (!isNull(value))
		{

			setParentSpecimen(value);
			this.parentSpecimen.setEnabled(true);
		}

		// Label
		value = (String) model.getValueAt(AppletConstants.SPECIMEN_LABEL_ROW_NO, columnIndex);
		if (!isNull(value))
		{
			setLabel(value);
		}

		// Barcode
		value = (String) model.getValueAt(AppletConstants.SPECIMEN_BARCODE_ROW_NO, columnIndex);
		if (!isNull(value))
		{
			setBarCode(value);
		}

		//Specimen Class
		value = (String) model.getValueAt(AppletConstants.SPECIMEN_CLASS_ROW_NO, columnIndex);
		if (!isNull(value))
		{
			setClassList(value);

			//Specimen Type
			setTypeListModel(value); // classname
			value = (String) model.getValueAt(AppletConstants.SPECIMEN_TYPE_ROW_NO, columnIndex);
			if (!isNull(value))
				setTypeList(value);

			//set unit
			value = (String) model.getQuantityUnit(columnIndex);
			if (!isNull(value))
				setUnit(value);

			//set buttons
			//			Mandar: 30Oct06: commented as buttons are enabled by default.
			//			updateButtons();
		}

		//TissueSite
		value = (String) model.getValueAt(AppletConstants.SPECIMEN_TISSUE_SITE_ROW_NO, columnIndex);
		if (!isNull(value))
		{
			setTissueSiteList(value);
		}

		//TissueSide
		value = (String) model.getValueAt(AppletConstants.SPECIMEN_TISSUE_SIDE_ROW_NO, columnIndex);
		if (!isNull(value))
		{
			setTissueSideList(value);
		}

		//PathologicalStatus
		value = (String) model.getValueAt(AppletConstants.SPECIMEN_PATHOLOGICAL_STATUS_ROW_NO, columnIndex);
		if (!isNull(value))
		{
			setPathologicalStatusList(value);
		}

		// Quantity
		value = (String) model.getValueAt(AppletConstants.SPECIMEN_QUANTITY_ROW_NO, columnIndex);
		if (!isNull(value))
		{
			setQuantity(value);
		}

		// Concentration
		value = (String) model.getValueAt(AppletConstants.SPECIMEN_CONCENTRATION_ROW_NO, columnIndex);
		if (!isNull(value))
		{
			setConcentration(value);
			setConcentrationStatus();
		}

		//For Storage Location
		//		Mandar: 06Nov06: location removed since auto allocation will take place.
		//		System.out.println("Storage location key is " + model.getKey(AppletConstants.SPECIMEN_STORAGE_LOCATION_ROW_NO ,columnIndex ));
		//		value = (String) model.getValueAt(AppletConstants.SPECIMEN_STORAGE_LOCATION_ROW_NO ,columnIndex);
		//		System.out.println("Storage location value is " + value);
		//		String key = model.getMapTempKey(columnIndex);
		//		System.out.println("Storage location key : " + key);
		//		if (!isNull(key))
		//		{
		//			value = model.getMapTempValue(key);
		//			System.out.println("Storage location key : " + key + "value is " + value
		//					+ " for Column : " + columnIndex);
		//			if ((value.trim().length() > 0))
		//			{
		//				setLocation(value);
		//			}
		//		}
		// ----------------
	}

	private boolean isNull(Object obj)
	{
		if (obj == null)
			return true;
		else
			return false;
	}

	private void setTabOrder()
	{
		//Specimen Collection Group
		collectionGroupPanel.setNextFocusableComponent(rbspecimenGroup);
		rbspecimenGroup.setNextFocusableComponent(specimenCollectionGroup);
		specimenCollectionGroup.setNextFocusableComponent(rbparentSpecimen);

		//Parent Specimen 
		rbparentSpecimen.setNextFocusableComponent(parentSpecimen);
		parentSpecimen.setNextFocusableComponent(label);

		// Label
		label.setNextFocusableComponent(barCode);

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
		concentration.setNextFocusableComponent(comments);

		//For Storage Location
		//		Mandar: 06Nov06: location removed since auto allocation will take place.
		//		mapButton.setNextFocusableComponent(comments);

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
		System.out.println("In setLocationFromJS : Column No : " + columnIndex);
		//		Mandar: 06Nov06: location removed since auto allocation will take place.
		//		setLocation(storageValue);

	}

	public void setEditCaptionFromJS(String objectKey, String status, MultipleSpecimenTableModel tableModel)
	{
		objectKey = objectKey.toLowerCase();
		Map buttonStatusMap = tableModel.getButtonStatusMap();
		int actuaColumnNo = tableModel.getActualColumnNo(columnIndex);
		if (objectKey.indexOf(AppletConstants.MULTIPLE_SPECIMEN_COMMENTS_STRING) != -1)
		{
			this.comments.setLabel(status);
			refreshComponent(this.comments);
			buttonStatusMap.put(actuaColumnNo + AppletConstants.MULTIPLE_SPECIMEN_BUTTON_MAP_KEY_SEPARATOR
					+ AppletConstants.MULTIPLE_SPECIMEN_COMMENTS_STRING, status);
		}
		else if (objectKey.indexOf(AppletConstants.MULTIPLE_SPECIMEN_EVENTS_STRING) != -1)
		{
			this.eventsButton.setLabel(status);
			refreshComponent(this.eventsButton);
			buttonStatusMap.put(actuaColumnNo + AppletConstants.MULTIPLE_SPECIMEN_BUTTON_MAP_KEY_SEPARATOR
					+ AppletConstants.MULTIPLE_SPECIMEN_EVENTS_STRING, status);
		}
		else if (objectKey.indexOf(AppletConstants.MULTIPLE_SPECIMEN_DERIVE_STRING) != -1)
		{
			this.deriveButton.setLabel(status);
			refreshComponent(this.deriveButton);
			buttonStatusMap.put(actuaColumnNo + AppletConstants.MULTIPLE_SPECIMEN_BUTTON_MAP_KEY_SEPARATOR
					+ AppletConstants.MULTIPLE_SPECIMEN_DERIVE_STRING, status);
		}
		else if (objectKey.indexOf(AppletConstants.MULTIPLE_SPECIMEN_EXTERNAL_IDENTIFIERS_STRING) != -1)
		{
			this.externalIdentifierButton.setLabel(status);
			refreshComponent(this.externalIdentifierButton);
			buttonStatusMap.put(actuaColumnNo + AppletConstants.MULTIPLE_SPECIMEN_BUTTON_MAP_KEY_SEPARATOR
					+ AppletConstants.MULTIPLE_SPECIMEN_EXTERNAL_IDENTIFIERS_STRING, status);
		}
		else if (objectKey.indexOf(AppletConstants.MULTIPLE_SPECIMEN_BIOHAZARDS_STRING) != -1)
		{
			this.bioHazardButton.setLabel(status);
			refreshComponent(this.bioHazardButton);
			buttonStatusMap.put(actuaColumnNo + AppletConstants.MULTIPLE_SPECIMEN_BUTTON_MAP_KEY_SEPARATOR
					+ AppletConstants.MULTIPLE_SPECIMEN_BIOHAZARDS_STRING, status);
		}
	}

	/**
	 * Method to get the text value of various components
	 * @author mandar_deshmukh
	 *
	 */
	private String getComponentValue(int row)
	{
		String value = "";

		switch (row)
		{
			//checkbox
			case AppletConstants.SPECIMEN_CHECKBOX_ROW_NO :
				value = String.valueOf(specimenCheckBox.isSelected());
				break;
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
				value = label.getText();
				break;
			// Barcode
			case AppletConstants.SPECIMEN_BARCODE_ROW_NO :
				value = barCode.getText();
				break;
			//Specimen Class
			case AppletConstants.SPECIMEN_CLASS_ROW_NO :
				value = classList.getSelectedItem().toString();
				break;
			//Specimen Type
			case AppletConstants.SPECIMEN_TYPE_ROW_NO :
				value = typeList.getSelectedItem().toString();
				break;
			//TissueSite
			case AppletConstants.SPECIMEN_TISSUE_SITE_ROW_NO :
				value = tissueSiteList.getSelectedItem().toString();
				break;
			//TissueSide
			case AppletConstants.SPECIMEN_TISSUE_SIDE_ROW_NO :
				value = tissueSideList.getSelectedItem().toString();
				break;
			//PathologicalStatus 
			case AppletConstants.SPECIMEN_PATHOLOGICAL_STATUS_ROW_NO :
				value = pathologicalStatusList.getSelectedItem().toString();
				break;
			// Quantity
			case AppletConstants.SPECIMEN_QUANTITY_ROW_NO :
				value = quantity.getText();
				break;
			// Concentration
			case AppletConstants.SPECIMEN_CONCENTRATION_ROW_NO :
				value = concentration.getText();
				break;
			//For Storage Location
			//				Mandar: 06Nov06: location removed since auto allocation will take place.		
			//			case AppletConstants.SPECIMEN_STORAGE_LOCATION_ROW_NO :
			//				value = location.getText();
			//				break;
			default :
				value = "";
		}
		return value;
	}

	//	/**
	//	 * This method is used to update the state of derive and map buttons based on the collection group and class values. 
	//	 *
	//	 */
	//	public void updateButtons()
	//	{
	//Mandar : 30Oct06: commented as the buttons are to be enabled
	// derive and map butons are enabled by default and no validation is done on collection group or class.

	//		if(!specimenCollectionGroup.getSelectedItem().toString().equals(Constants.SELECT_OPTION) && 
	//			!(classList.getSelectedItem().toString().equals(Constants.SELECT_OPTION)))
	//		{
	//			deriveButton.setEnabled(true);
	//			mapButton.setEnabled(true); 
	//		}
	//		else
	//		{
	//			deriveButton.setEnabled(false);
	//			mapButton.setEnabled(false); 
	//		}
	//		refreshComponent(deriveButton);
	//		refreshComponent(mapButton);
	//	}

	/* (non-Javadoc)
	 * @see javax.swing.AbstractCellEditor#fireEditingStopped()
	 */
	protected void fireEditingStopped()
	{
		System.out.println("SpecimenColumnModel in fireEditingStopped  doing nothing");
		super.fireEditingStopped();
	}

	// ------------ Mandar : 11Oct06 To update cells after paste. -------

	/**
	 * This method updates the specified component and sets the given value to it.
	 */
	public void updateComponentValue(int row, String value)
	{
		System.out.println("\n\n\n<updateComponentValue : row : " + row + " , value : " + value + ">\n\n\n");
		JComponent comp = null;
		switch (row)
		{
			//checkbox
			case AppletConstants.SPECIMEN_CHECKBOX_ROW_NO :
				boolean status = false;
				if(value.equalsIgnoreCase("true"))
				{
					status = true;
				}
				specimenCheckBox.setSelected(status);
				comp = specimenCheckBox;
				break;

			//Specimen Collection Group
			case AppletConstants.SPECIMEN_COLLECTION_GROUP_ROW_NO :
				specimenCollectionGroup.setSelectedItem(value);
				comp = specimenCollectionGroup;
				break;
			//Parent Specimen 
			case AppletConstants.SPECIMEN_PARENT_ROW_NO :
				parentSpecimen.setText(value);
				comp = parentSpecimen;
				break;
			// Label
			case AppletConstants.SPECIMEN_LABEL_ROW_NO :
				label.setText(value);
				comp = label;
				break;
			// Barcode
			case AppletConstants.SPECIMEN_BARCODE_ROW_NO :
				barCode.setText(value);
				comp = barCode;
				break;
			//Specimen Class
			case AppletConstants.SPECIMEN_CLASS_ROW_NO :
				classList.setSelectedItem(value);
				comp = classList;
				break;
			//Specimen Type
			case AppletConstants.SPECIMEN_TYPE_ROW_NO :
				typeList.setSelectedItem(value);
				comp = typeList;
				break;
			//TissueSite
			case AppletConstants.SPECIMEN_TISSUE_SITE_ROW_NO :
				tissueSiteList.setSelectedItem(value);
				comp = tissueSiteList;
				break;
			//TissueSide
			case AppletConstants.SPECIMEN_TISSUE_SIDE_ROW_NO :
				tissueSideList.setSelectedItem(value);
				comp = tissueSideList;
				break;
			//PathologicalStatus 
			case AppletConstants.SPECIMEN_PATHOLOGICAL_STATUS_ROW_NO :
				pathologicalStatusList.setSelectedItem(value);
				comp = pathologicalStatusList;
				break;
			// Quantity
			case AppletConstants.SPECIMEN_QUANTITY_ROW_NO :
				quantity.setText(value);
				comp = quantity;
				break;
			// Concentration
			case AppletConstants.SPECIMEN_CONCENTRATION_ROW_NO :
				concentration.setText(value);
				comp = concentration;
				break;
		//For Storage Location
		//				Mandar: 06Nov06: location removed since auto allocation will take place.
		//			case AppletConstants.SPECIMEN_STORAGE_LOCATION_ROW_NO :
		//				location.setText(value);
		//				comp = location;
		//				break;
		}
		if (comp != null)
			refreshComponent(comp);
		System.out.println(" Component at " + row + " Updated");
	}

	/**
	 * This method refreshes the specified component.
	 * @param row
	 */
	public void updateComponent(int row)
	{
		JComponent comp = null;
		switch (row)
		{
			//checkbox
			case AppletConstants.SPECIMEN_CHECKBOX_ROW_NO :
				comp = specimenCheckBox;
				break;

			//Specimen Collection Group
			case AppletConstants.SPECIMEN_COLLECTION_GROUP_ROW_NO :
				comp = specimenCollectionGroup;
				break;
			//Parent Specimen 
			case AppletConstants.SPECIMEN_PARENT_ROW_NO :
				comp = parentSpecimen;
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
				comp = quantity;
				break;
			// Concentration
			case AppletConstants.SPECIMEN_CONCENTRATION_ROW_NO :
				comp = concentration;
				break;
		//For Storage Location
		//				Mandar: 06Nov06: location removed since auto allocation will take place.
		//			case AppletConstants.SPECIMEN_STORAGE_LOCATION_ROW_NO :
		//				comp = location;
		//				break;
		}
		if (comp != null)
			refreshComponent(comp);
	}

	/*
	 * Refreshes the UI.
	 */
	private void refreshComponent(Component comp)
	{
		SwingUtilities.updateComponentTreeUI(comp);
	}

	/*
	 * This method checks the component type and sets the focus accordingly. 
	 * @author mandar_deshmukh
	 * 
	 */
	private void setActiveElement(Component comp, String calledFrom)
	{
		if (comp.isEnabled())
		{
			//			System.out.println("Inside setActiveElement calledFrom : "+ calledFrom);
			String comptype = "";
			if (comp instanceof JPanel)
			{
				Component[] childComponents = ((JPanel) comp).getComponents();
				for (int count = 0; count < childComponents.length; count++)
				{
					if ((childComponents[count] instanceof ModifiedComboBox || childComponents[count] instanceof ModifiedTextField || childComponents[count] instanceof JRadioButton)
							&& childComponents[count].isEnabled())
					{
						comptype = childComponents[count].getClass().toString();
						//				System.out.println("Focus received : " +childComponents[count].requestFocusInWindow());
						break;
					}
				}
			}
			else
			{
				comptype = comp.getClass().toString();
				//			System.out.println("Focus received : " +comp.requestFocusInWindow());
			}
			//		System.out.println(comptype + "set as Active Element.");	
		}
	}

	public boolean isCellEnabled(int rowNo)
	{
		System.out.println("IN SCM isCellEnabled : rowno:- " + rowNo);
		boolean result = false;
		switch (rowNo)
		{
			case AppletConstants.SPECIMEN_COLLECTION_GROUP_ROW_NO :
				result = rbspecimenGroup.isSelected();
				break;
			case AppletConstants.SPECIMEN_PARENT_ROW_NO :
				result = rbparentSpecimen.isSelected();
				break;
			//				Mandar: 06Nov06: location removed since auto allocation will take place.
			//			case AppletConstants.SPECIMEN_STORAGE_LOCATION_ROW_NO :
			//				result = location.isEnabled();
			//				break;
			default :
				result = true;
		}
		System.out.println("Row is enabled : " + result);
		return result;
	}

	// -------------------Mandar For Focus Handling 11-Dec-06 start
	/*	
	 public class MyOwnFocusTraversalPolicy extends FocusTraversalPolicy 
	 {
	 public MyOwnFocusTraversalPolicy(JComponent root)
	 {
	 this.root = root;
	 comps = root.getComponents();
	 }
	 JComponent root;
	 Component comps[];
	 
	 private Component getFirstComponent()
	 {
	 if(comps.length > 0)
	 {
	 return comps[0];
	 }
	 else
	 return null;
	 }

	 public Component getComponentAfter(Container focusCycleRoot, Component aComponent)
	 {
	 Component comp=null;
	 if(comps.length > 0)
	 {
	 for(int i=0;i<comps.length;i++)
	 {
	 if((i+1) < comps.length)
	 {
	 if(comps[i] == aComponent)
	 {
	 comp = comps[i+1];
	 }
	 }
	 }
	 }
	 return comp;
	 }

	 public Component getComponentBefore(Container focusCycleRoot, Component aComponent)
	 {
	 Component comp=null;
	 if(comps.length > 0)
	 {
	 for(int i=0;i<comps.length;i++)
	 {
	 if((i+1) < comps.length)
	 {
	 if(comps[i] == aComponent)
	 {
	 comp = comps[i-1];
	 }
	 }
	 }
	 }
	 return comp;
	 }

	 public Component getDefaultComponent(Container focusCycleRoot)
	 {
	 return getFirstComponent();
	 }

	 public Component getLastComponent(Container focusCycleRoot)
	 {
	 if(comps.length > 0)
	 {
	 return (JComponent)comps[comps.length-1];
	 }
	 else
	 return null;
	 }

	 public Component getFirstComponent(Container focusCycleRoot)
	 {
	 return getFirstComponent();
	 }
	 }
	 // -------------------Mandar For Focus Handling 11-Dec-06 end
	 */
}
//TODO 
//Delete the class as moved to a separate file

// -----------Class to override default ModifiedComboBox and request focus when displayed
//class ModifiedComboBox extends JComboBox 
//{
//	
//	/**
//	 * @param items
//	 */
//	public ModifiedComboBox(Object[] items)
//	{
//		super(items);
//		addListener();
//	}
//	/**
//	 * @param arg0
//	 */
//	public ModifiedComboBox(Vector arg0)
//	{
//		super(arg0);
//		addListener();
//	}
//	/**
//	 * @param aModel
//	 */
//	public ModifiedComboBox(ComboBoxModel aModel)
//	{
//		super(aModel);
//		addListener();
//	}
//
//	public ModifiedComboBox()
//	{
//		addListener();
//	}
//	private void addListener()
//	{
//	 	addAncestorListener( new AncestorListener()
//	 	{
//	 		public void ancestorAdded(AncestorEvent e)
//	 		{
//	 			requestFocus();
//	 		}
//	 		public void ancestorMoved(AncestorEvent e){}
//	 		public void ancestorRemoved(AncestorEvent e){}
//	 	});
//	 	
//	 	addFocusListener(new FocusAdapter(){
//	 		public void focusGained(FocusEvent fe)
//	 		{
//	 			showPopup(); 
//	 			System.out.println("Focus Gained 15Dec06");
//	 		}
////	 		public void focusLost(FocusEvent fe)
////	 		{
////	 			System.out.println("Focus Lost 15Dec06");
////	 			//transferFocus();
////	 			
////	 			if(getPeer() !=null)
////	 			{
////	 				System.out.println("getPeer().getClass().getName()  : "+getPeer().getClass().getName()  );	
////	 				//getParent().getPeer().requestFocus();
////	 				//getPeer().requestFocus();
////	 			}
////	 			else
////	 			{
////	 				System.out.println("getPeer().getClass().getName() : NULL"); 
////	 				getParent().requestFocus();
////	 			}
////	 		}
//	 		
// 		});
//	}
//}

//-----class for textfield
//class ModifiedTextField extends JTextField 
//{
//	
//	/**
//	 * 
//	 */
//	public ModifiedTextField()
//	{
//		super();
//		addListener();
//		// TODO Auto-generated constructor stub
//	}
//	/**
//	 * @param columns
//	 */
//	public ModifiedTextField(int columns)
//	{
//		super(columns);
//		addListener();
//		// TODO Auto-generated constructor stub
//	}
//	/**
//	 * @param text
//	 */
//	public ModifiedTextField(String text)
//	{
//		super(text);
//		addListener();
//		// TODO Auto-generated constructor stub
//	}
//	/**
//	 * @param text
//	 * @param columns
//	 */
//	public ModifiedTextField(String text, int columns)
//	{
//		super(text, columns);
//		addListener();
//		// TODO Auto-generated constructor stub
//	}
//	/**
//	 * @param doc
//	 * @param text
//	 * @param columns
//	 */
//	public ModifiedTextField(Document doc, String text, int columns)
//	{
//		super(doc, text, columns);
//		addListener();
//		// TODO Auto-generated constructor stub
//	}
//	private void addListener()
//	{
//	 	addAncestorListener( new AncestorListener()
//	 	{
//	 		public void ancestorAdded(AncestorEvent e)
//	 		{
//	 			requestFocus();
//	 		}
//	 		public void ancestorMoved(AncestorEvent e){}
//	 		public void ancestorRemoved(AncestorEvent e){}
//	 	});
//	}
//}
//
// -----Class for focused buttons
//class ModifiedButton extends JButton 
//{
//	
//	/**
//	 * 
//	 */
//	public ModifiedButton()
//	{
//		super();
//		addListener();
//	}
//	/**
//	 * @param text
//	 */
//	public ModifiedButton(String text)
//	{
//		super(text);
//		addListener();
//	}
//	/**
//	 * @param text
//	 * @param icon
//	 */
//	public ModifiedButton(String text, Icon icon)
//	{
//		super(text, icon);
//		addListener();
//	}
//	/**
//	 * @param a
//	 */
//	public ModifiedButton(Action a)
//	{
//		super(a);
//		addListener();
//	}
//	/**
//	 * @param icon
//	 */
//	public ModifiedButton(Icon icon)
//	{
//		super(icon);
//		addListener();
//	}
//	
//	private void addListener()
//	{
//	 	addAncestorListener( new AncestorListener()
//	 	{
//	 		public void ancestorAdded(AncestorEvent e)
//	 		{
//	 			requestFocus();
//	 		}
//	 		public void ancestorMoved(AncestorEvent e){}
//	 		public void ancestorRemoved(AncestorEvent e){}
//	 	});
//	}
//}

