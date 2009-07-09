
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
		this.popupMenu = null;
		this.popupMenu = popupMenu;
	}

	/**
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 * @param e : e
	 */
	public void mouseClicked(MouseEvent e)
	{
		super.mouseClicked(e);
		if (!popupMenu.isShowing())
		{
			popupMenu.show(e.getComponent(), e.getX(), e.getY());
		}
		else
		{
			popupMenu.setVisible(false);
		}
	}
}
