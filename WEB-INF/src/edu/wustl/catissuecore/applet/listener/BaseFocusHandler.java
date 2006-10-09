/**
 * This is base Handler class for all the Focus events on the components in caTissuecore.
 * 
 * @author Mandar Deshmukh
 * Created on Sep 22, 2006
 * 
 */
package edu.wustl.catissuecore.applet.listener;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JTextField;

import edu.wustl.catissuecore.applet.component.BaseTable;
import edu.wustl.catissuecore.applet.model.MultipleSpecimenTableModel;

/**
 * This is base Handler class for all the Focus events on the components in caTissuecore.
 * 
 * @author Mandar Deshmukh
 * Created on Sep 22, 2006
 * 
 */
public class BaseFocusHandler implements FocusListener {

	
	protected JTable table;
	
	public BaseFocusHandler(JTable table)
	{
		this.table = table;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.FocusListener#focusGained(java.awt.event.FocusEvent)
	 */
	public void focusGained(FocusEvent e)
	{System.out.println("\n\n**************************************\n");
		System.out.println("In BaseFocusHandler Gained");
		MultipleSpecimenTableModel tablemodel = (MultipleSpecimenTableModel)((BaseTable)table).getModel();
		
		tablemodel.setLastCellColumn(table.getSelectedColumn()  );
		tablemodel.setLastCellRow(table.getSelectedRow()  );
		
		System.out.println("In BaseFocusHandler Gained - Last Selected Col Set as : " + tablemodel.getLastCellColumn()+"  | Last Selected Row Set as : "+tablemodel.getLastCellRow()+"\n Value present in model : "+tablemodel.getValueAt(tablemodel.getLastCellColumn(),tablemodel.getLastCellRow())  );
		
		if (e.getSource() instanceof JTextField )
		{
			JTextField obj = (JTextField)e.getSource();
			System.out.println("Value in UI: "+obj.getText());
		}
		System.out.println("\n\n**************************************\n");
		//to do
//		if(e.getSource() instanceof JPanel)
//		{
//			JPanel src = (JPanel)e.getSource();
//			Component comps[] = src.getComponents();
//			for(int cnt=0; cnt<comps.length; cnt++)
//			{
//				if(comps[cnt].isEnabled())
//				{
//					
//				}
//			}
//		}
	}

	/* (non-Javadoc)
	 * @see java.awt.event.FocusListener#focusLost(java.awt.event.FocusEvent)
	 */
	public void focusLost(FocusEvent e)
	{
		System.out.println("\n\n--------------------------------------------\n");
		System.out.println("In BaseFocusHandler Lost");
		
		//---------------09Oct06
		MultipleSpecimenTableModel tablemodel = (MultipleSpecimenTableModel)((BaseTable)table).getModel();
		
		int lastSelectedRow = tablemodel.getLastCellRow();
		int lastSelectedCol = tablemodel.getLastCellColumn();
		
		System.out.println("In BaseFocusHandler Lost Last selected Row: " + lastSelectedRow+" Last Selected Col : "+lastSelectedCol+" Value in Model :"+ tablemodel.getValueAt(lastSelectedRow ,lastSelectedCol) );
		table.getModel().setValueAt(getSelectedValue(e),lastSelectedRow ,lastSelectedCol );
		System.out.println("In BaseFocusHandler Lost Last Selected Row : " + lastSelectedRow +" Last Selected Col : "+lastSelectedCol+"AfterSetting :- Value in Model :"+ tablemodel.getValueAt(lastSelectedRow ,lastSelectedCol) );
		System.out.println("\n\n--------------------------------------------\n");
		// --------------- 09Oct06
//		System.out.println(e.getSource().getClass());
//		if(e.getSource() instanceof JButton)
//		{
//				// ignore focus lost
//		}else
//		{
//			table.getModel().setValueAt(getSelectedValue(e),table.getSelectedRow(),table.getSelectedColumn());
//			System.out.println(getSelectedValue(e)+" Row: "+ table.getSelectedRow()+ " Col: " + table.getSelectedColumn()); 
//		}
	}
	
	protected Object getSelectedValue(FocusEvent e)
	{
		Object data=null;
		if(e.getSource() instanceof JTextField)
		{
			JTextField sourceObject = (JTextField)e.getSource();
			data = sourceObject.getText(); 
		}
		return  data ;
	}

}
