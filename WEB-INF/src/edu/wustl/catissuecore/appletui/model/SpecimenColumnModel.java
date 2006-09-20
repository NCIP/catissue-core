package edu.wustl.catissuecore.applet.model;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.AbstractCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import edu.wustl.catissuecore.applet.component.BaseTable;

/**
 * This is column model for multiple specimen page.
 *  
 * @author mandar_deshmukh
 */
public class SpecimenColumnModel extends AbstractCellEditor
		implements
			TableCellRenderer,
			TableCellEditor,
			ActionListener
{
	/**
	 * Default Serial Version ID
	 */
	private static final long serialVersionUID = 1L;


	BaseTable table;
	
	JPanel panel;
	ImageIcon icon;
	JComboBox classList;
	JComboBox typeList;
	JTextField location;
	JTextField name;
	JButton mapButton;
	String text;
	String[] listItems1 = {"111", "222", "333", "444", "555"};
	String[] listItems = {"AAA", "BBB", "CCC", "DDD", "EEE"};

	public SpecimenColumnModel(BaseTable table, int column) //, String[] typeList, String[] classList
	{
		super();
		this.table = table;
		panel = new JPanel();
		icon = new ImageIcon("Tree.gif");
		classList = new JComboBox(listItems);
		typeList = new JComboBox(listItems1);
		location = new JTextField(10);
		name = new JTextField();
		mapButton = new JButton("Map");
		mapButton.addActionListener(this);
		panel.add(location);
		panel.add(mapButton);
		JLabel iconLabel = new JLabel(icon);
		panel.add(iconLabel);

		name.addActionListener(this);
		classList.addActionListener(this);
		typeList.addActionListener(this);

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

	public void actionPerformed(ActionEvent e)
	{
		Object selectedValue = "";

		if (e.getSource() instanceof JComboBox)
		{
			//	String [] classList = {"AAA","BBB","CCC","DDD","EEE"};
			JComboBox cbx = (JComboBox) e.getSource();
			Object selectedObject = cbx.getSelectedItem();
			int ind = cbx.getSelectedIndex();
			cbx.setSelectedItem(selectedObject);
			System.out.println(selectedObject + " : Combo Index : " + ind + " : Row : "
					+ table.getSelectedRow());
			System.out.println("\nSearch : "
					+ (Arrays.binarySearch(listItems, selectedObject.toString())));

			if ((Arrays.binarySearch(listItems, selectedObject.toString())) >= 0)
			{
				//typeList.removeAllItems();
				String[] newList = new String[5];
				for (int i = 0; i < 5; i++)
					newList[i] = new String(selectedObject.toString() + "_" + (i + 1));
				DefaultComboBoxModel newcbxModel = new DefaultComboBoxModel(newList);
				typeList.setModel(newcbxModel);
			}

			selectedValue = selectedObject;
		}
		else if (e.getSource() instanceof JTextField)
		{
			selectedValue = ((JTextField) (e.getSource())).getText();
		}

		fireEditingStopped();
		table.getModel().setValueAt(selectedValue, table.getSelectedRow(),
				table.getSelectedColumn());
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
			case 0 :
				comp = name;
				break;
			case 1 :
				comp = classList;
				break;
			case 2 :
				comp = typeList;
				break;
			case 3 :
				comp = panel;
				break;
			default :
				comp = new JTextField();
		}

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