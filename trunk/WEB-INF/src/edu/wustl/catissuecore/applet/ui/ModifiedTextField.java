/*
 * Created on Dec 22, 2006
 *
 * This class is used as an editor for rows where textfield is required.
 * It gets the default focus when the control comes to the respective cell.
 */
package edu.wustl.catissuecore.applet.ui;

import javax.swing.JTextField;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.text.Document;


/**
 * @author mandar_deshmukh
 * Created on Dec 22, 2006
 *
 * This class is used as an editor for rows where textfield is required.
 * It gets the default focus when the control comes to the respective cell.
 */
public class ModifiedTextField extends JTextField 
{
	
	/**
	 * 
	 */
	public ModifiedTextField()
	{
		super();
		addListener();
		// TODO Auto-generated constructor stub
	}
	/**
	 * @param columns
	 */
	public ModifiedTextField(int columns)
	{
		super(columns);
		addListener();
		// TODO Auto-generated constructor stub
	}
	/**
	 * @param text
	 */
	public ModifiedTextField(String text)
	{
		super(text);
		addListener();
		// TODO Auto-generated constructor stub
	}
	/**
	 * @param text
	 * @param columns
	 */
	public ModifiedTextField(String text, int columns)
	{
		super(text, columns);
		addListener();
		// TODO Auto-generated constructor stub
	}
	/**
	 * @param doc
	 * @param text
	 * @param columns
	 */
	public ModifiedTextField(Document doc, String text, int columns)
	{
		super(doc, text, columns);
		addListener();
		// TODO Auto-generated constructor stub
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

