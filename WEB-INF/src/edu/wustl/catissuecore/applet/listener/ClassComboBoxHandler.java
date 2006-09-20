/*
 * Created on Sep 20, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package edu.wustl.catissuecore.applet.listener;

import java.awt.event.ActionEvent;

import javax.swing.JTable;

/**
 * @author mandar_deshmukh
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ClassComboBoxHandler extends ComboBoxHandler {

	/**
	 * @param table
	 */
	public ClassComboBoxHandler(JTable table) {
		super(table);
	}
	

	protected void handleAction(ActionEvent e)
	{
		super.handleAction(e);
		System.out.println("Inside ClassComboBoxHandler");
//		if (e.getSource() instanceof JComboBox)
//		{
//			//	String [] classList = {"AAA","BBB","CCC","DDD","EEE"};
//			JComboBox cbx = (JComboBox) e.getSource();
//			Object selectedObject = cbx.getSelectedItem();
//			int ind = cbx.getSelectedIndex();
//			cbx.setSelectedItem(selectedObject);
//			System.out.println(selectedObject + " : Combo Index : " + ind + " : Row : "
//					+ table.getSelectedRow());
//			System.out.println("\nSearch : "
//					+ (Arrays.binarySearch(listItems, selectedObject.toString())));
//
//			if ((Arrays.binarySearch(listItems, selectedObject.toString())) >= 0)
//			{
//				//typeList.removeAllItems();
//				String[] newList = new String[5];
//				for (int i = 0; i < 5; i++)
//					newList[i] = new String(selectedObject.toString() + "_" + (i + 1));
//				DefaultComboBoxModel newcbxModel = new DefaultComboBoxModel(newList);
//				typeList.setModel(newcbxModel);
//			}
//
//			selectedValue = selectedObject;
//		}

	}
}
