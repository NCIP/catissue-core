/**
 * This class will handle events related to buttons. Common functionality will be handled in this class.
 * For specific functionality a sub class of this will be used.
 * 
 * Created on Sep 20, 2006 
 * @author mandar_deshmukh
 *
 */

package edu.wustl.catissuecore.applet.listener;

import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JTable;

import edu.wustl.catissuecore.applet.model.MultipleSpecimenTableModel;
import edu.wustl.catissuecore.applet.util.CommonAppletUtil;
import edu.wustl.catissuecore.util.global.Constants;

/**
 * This class will handle events related to buttons. Common functionality will be handled in this class.
 * For specific functionality a sub class of this will be used.
 * 
 * Created on Sep 20, 2006 
 * @author mandar_deshmukh
 *
 */

public class ButtonHandler extends BaseActionHandler
{

	/**
	 * @param table
	 */
	public ButtonHandler(JTable table)
	{
		super(table);
	}

	/**
	 * @see edu.wustl.catissuecore.applet.listener.BaseActionHandler#handleAction(java.awt.event.ActionEvent)
	 */
	protected void handleAction(ActionEvent event)
	{
		int colNo = table.getSelectedColumn();
		int rowNo = table.getSelectedRow();
		String key = ((MultipleSpecimenTableModel) table.getModel()).getKey(rowNo, colNo);

		Object[] parameters = {Constants.ADD, key};
		CommonAppletUtil.callJavaScriptFunction((JButton) event.getSource(),
				getJSMethodName(), parameters);
	}

	/**
	 * @return JS method name for this button.
	 */
	protected String getJSMethodName()
	{
		return "showCommentsDialog";
	}

	/** 
	 * @see edu.wustl.catissuecore.applet.listener.BaseActionHandler#getSelectedValue(java.awt.event.ActionEvent)
	 */
	protected Object getSelectedValue(ActionEvent event)
	{
		return "submit";
	}

	/** (non-Javadoc)
	 * @see edu.wustl.catissuecore.applet.listener.BaseActionHandler#preActionPerformed(java.awt.event.ActionEvent)
	 */
	protected void preActionPerformed(ActionEvent event)
	{
		// Mandar --- changes for focus handling (16/10/2006)
		CommonAppletUtil.getSelectedData(table);
	}
	
	

}
