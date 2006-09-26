package edu.wustl.catissuecore.applet.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JTable;


/**
 * <p>This class initializes the fields of ArrayCopyOptionActionHandler.java</p>
 * @author Ashwin Gupta
 * @version 1.1
 */
public class ArrayCopyOptionActionHandler implements ActionListener {

	/**
	 * Specify the table field 
	 */
	private JTable table = null;

	public ArrayCopyOptionActionHandler(JTable table) {
		this.table = table;
	}
	/**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		JMenuItem menuItem = (JMenuItem) e.getSource();
		System.out.println(menuItem.getText());
	}

}
