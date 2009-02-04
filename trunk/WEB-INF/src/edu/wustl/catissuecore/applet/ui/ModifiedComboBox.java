/*
 * Created on Dec 22, 2006
 *
 * This class is used as an editor for rows where combobox is required.
 * It gets the default focus when the control comes to the respective cell.
 */
package edu.wustl.catissuecore.applet.ui;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.Vector;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;


/**
 * @author mandar_deshmukh
 * Created on Dec 22, 2006
 * This class is used as an editor for rows where combobox is required.
 * It gets the default focus when the control comes to the respective cell.
 */
public class ModifiedComboBox extends JComboBox 
{
	
	/**
	 * @param items
	 */
	public ModifiedComboBox(Object[] items)
	{
		super(items);
		addListener();
	}
	/**
	 * @param arg0
	 */
	public ModifiedComboBox(Vector arg0)
	{
		super(arg0);
		addListener();
	}
	/**
	 * @param aModel
	 */
	public ModifiedComboBox(ComboBoxModel aModel)
	{
		super(aModel);
		addListener();
	}

	public ModifiedComboBox()
	{
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
	 	
	 	addFocusListener(new FocusAdapter(){
	 		public void focusGained(FocusEvent fe)
	 		{
	 			showPopup(); 
	 			System.out.println("Focus Gained 15Dec06");
	 		}
//	 		public void focusLost(FocusEvent fe)
//	 		{
//	 			System.out.println("Focus Lost 15Dec06");
//	 			//transferFocus();
//	 			
//	 			if(getPeer() !=null)
//	 			{
//	 				System.out.println("getPeer().getClass().getName()  : "+getPeer().getClass().getName()  );	
//	 				//getParent().getPeer().requestFocus();
//	 				//getPeer().requestFocus();
//	 			}
//	 			else
//	 			{
//	 				System.out.println("getPeer().getClass().getName() : NULL"); 
//	 				getParent().requestFocus();
//	 			}
//	 		}
	 		
 		});
	}
}
