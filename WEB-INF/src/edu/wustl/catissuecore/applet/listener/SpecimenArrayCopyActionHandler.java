
package edu.wustl.catissuecore.applet.listener;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

/**
 * <p>This class initializes the fields of SpecimenArrayCopyActionHandler.java</p>.
 * @author Ashwin Gupta
 * @version 1.1
 */
public class SpecimenArrayCopyActionHandler implements ActionListener
{
	/**
	 * popupMenu.
	 */
	JPopupMenu popupMenu = null;

	/**
	 * @param popupMenu pop menu
	 */
	public SpecimenArrayCopyActionHandler(JPopupMenu popupMenu)
	{
		this.popupMenu = popupMenu;
	}

	/**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 * @param e : e
	 */
	public void actionPerformed(ActionEvent e)
	{
		Component c = (Component) e.getSource();

		int px = c.getX();
		int py = c.getY() + c.getHeight() + 50;
		Point point = new Point(px, py);
		//Point point = c.getLocation();
		SwingUtilities.convertPointFromScreen(point, c);

		if (!popupMenu.isShowing())
		{
			popupMenu.show(c, point.x, point.y);
		}
		else
		{
			popupMenu.setVisible(false);
		}
	}

}
