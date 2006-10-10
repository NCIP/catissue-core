package edu.wustl.catissuecore.applet.listener;

import java.awt.event.ActionEvent;

import javax.swing.JTable;

/**
 * <p>This class initializes the fields of SpecimenArrayPasteActionHandler.java</p>
 * @author Ashwin Gupta
 * @version 1.1
 */
public class SpecimenArrayPasteActionHandler extends AbstractPasteActionHandler {

	/**
	 * constructor with table to persist table
	 * @param table table used in applet
	 */
	public SpecimenArrayPasteActionHandler(JTable table) {
		super(table);
	}
	
	/**
	 * @see edu.wustl.catissuecore.applet.listener.AbstractPasteActionHandler#doActionPerformed(java.awt.event.ActionEvent)
	 */
	protected void doActionPerformed(ActionEvent e) {
	}

}
