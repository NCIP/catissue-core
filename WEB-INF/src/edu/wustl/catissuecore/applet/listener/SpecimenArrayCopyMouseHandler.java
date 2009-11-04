
package edu.wustl.catissuecore.applet.listener;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPopupMenu;

/**
 * <p>This class initializes the fields of SpecimenArrayCopyMouseHandler.java</p>.
 * @author Ashwin Gupta
 * @version 1.1
 */
public class SpecimenArrayCopyMouseHandler extends MouseAdapter
{

	/**
	 * popupMenu.
	 */
	JPopupMenu popupMenu;

	/**
	 * @param popupMenu : popupMenu
	 */
	public SpecimenArrayCopyMouseHandler(JPopupMenu popupMenu)
	{
		super();
		this.popupMenu = null;
		this.popupMenu = popupMenu;
	}

	/**
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 * @param actionEvent : e
	 */
	@Override
	public void mouseClicked(MouseEvent actionEvent)
	{
		super.mouseClicked(actionEvent);
		if (this.popupMenu.isShowing())
		{
			this.popupMenu.setVisible(false);
		}
		else
		{
			this.popupMenu.show(actionEvent.getComponent(), actionEvent.getX(), actionEvent.getY());
		}
	}
}
