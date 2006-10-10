package edu.wustl.catissuecore.applet.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTable;


/**
 * <p>This class initializes the fields of AbstractPasteActionHandler.java</p>
 * @author Ashwin Gupta
 * @version 1.1
 */
public abstract class AbstractPasteActionHandler implements ActionListener {

	/**
	 * Table component used in applet 
	 */
	protected JTable table; 
	
	/**
	 * Default constructor 
	 */
	public AbstractPasteActionHandler()
	{
	}
	
	/**
	 * constructor with table to persist table
	 * @param table table used in applet
	 */
	public AbstractPasteActionHandler(JTable table)
	{
		this.table = table;
	}
	
	/**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		preActionPerformed(e);
		doActionPerformed(e);
		postActionPerformed(e);
	}
	
	/**
	 * Pre action performed method for paste operation 
	 */
	protected void preActionPerformed(ActionEvent e)
	{
	}
	
	/**
	 * do action performed method for paste operation.
	 * Other paste listeners should override this method. 
	 */
	protected abstract void doActionPerformed(ActionEvent e);
	
	/**
	 * Post action performed method for paste operation 
	 */
	protected void postActionPerformed(ActionEvent e)
	{
	}
}
