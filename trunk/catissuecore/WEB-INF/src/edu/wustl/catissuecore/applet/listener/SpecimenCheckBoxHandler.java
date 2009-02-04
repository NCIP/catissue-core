/*
 * @author mandar_deshmukh
 * Created on Dec 22, 2006
 * 
 * This class handles events of the checkbox for selecting specimen.
 */
package edu.wustl.catissuecore.applet.listener;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JCheckBox;
import javax.swing.JTable;

import edu.wustl.catissuecore.applet.util.CommonAppletUtil;


/**
 * @author mandar_deshmukh
 * Created on Dec 22, 2006
 * 
 * This class handles events of the checkbox for selecting specimen.
 */
public class SpecimenCheckBoxHandler implements ItemListener
{
	private JTable table;

	/**
	 * Default Constructor.
	 */
	public SpecimenCheckBoxHandler(JTable table)
	{
		this.table = table;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
	 */
	public void itemStateChanged(ItemEvent e)
	{
		System.out.println("\nInside SpecimenCheckBoxHandler\n");
		JCheckBox chk = (JCheckBox)e.getSource();
		System.out.println("\nchk.getActionCommand() : "+chk.getActionCommand()+", chk.isSelected():"+chk.isSelected()+"\n");
		CommonAppletUtil.getMultipleSpecimenTableModel(table).setSpecimenCheckBoxValueAt(Integer.parseInt(chk.getActionCommand())-1,chk.isSelected()  );
		
	}

}
