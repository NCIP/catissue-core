/*
 * This class will handle all the events of Map button.
 * Created on Sep 19, 2006
 * @author mandar_deshmukh
 */
package edu.wustl.catissuecore.applet.listener;

import java.awt.event.ActionEvent;

import javax.swing.JTable;

/**
 * @author mandar_deshmukh
 * 
 * This class will handle all the events of Map button.
 */
public class MapButtonHandler extends ButtonHandler {

	protected void handleAction(ActionEvent event) {
		super.handleAction(event);
		System.out.println("Inside MapButtonHandler");
	}
	
	/**
	 * @param table
	 */
	public MapButtonHandler(JTable table) {
		super(table);
	}

}