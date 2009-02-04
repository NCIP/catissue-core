/*
 * Created on Dec 22, 2006
 *
 * This class is used as an editor for rows where button is required.
 * It gets the default focus when the control comes to the respective cell.
 */
package edu.wustl.catissuecore.applet.ui;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;


/**
 * @author mandar_deshmukh
 * Created on Dec 22, 2006
 *
 * This class is used as an editor for rows where button is required.
 * It gets the default focus when the control comes to the respective cell.
 */
public class ModifiedButton extends JButton 
{
	
	/**
	 * 
	 */
	public ModifiedButton()
	{
		super();
		addListener();
	}
	/**
	 * @param text
	 */
	public ModifiedButton(String text)
	{
		super(text);
		addListener();
	}
	/**
	 * @param text
	 * @param icon
	 */
	public ModifiedButton(String text, Icon icon)
	{
		super(text, icon);
		addListener();
	}
	/**
	 * @param a
	 */
	public ModifiedButton(Action a)
	{
		super(a);
		addListener();
	}
	/**
	 * @param icon
	 */
	public ModifiedButton(Icon icon)
	{
		super(icon);
		addListener();
	}
	
	private void addListener()
	{
	 	addAncestorListener( new AncestorListener()
	 	{
	 		public void ancestorAdded(AncestorEvent e)
	 		{
	 			requestFocus();
	 		}
	 		public void ancestorMoved(AncestorEvent e){}
	 		public void ancestorRemoved(AncestorEvent e){}
	 	});
	}
}
