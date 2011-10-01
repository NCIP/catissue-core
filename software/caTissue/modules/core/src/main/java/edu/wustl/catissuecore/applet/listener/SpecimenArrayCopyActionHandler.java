
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
	 * @param actionEvent : e
	 */
	public void actionPerformed(ActionEvent actionEvent)
	{
		final Component comp = (Component) actionEvent.getSource();

		final int pointX = comp.getX();
		final int pointY = comp.getY() + comp.getHeight() + 50;
		final Point point = new Point(pointX, pointY);
		//Point point = c.getLocation();
		SwingUtilities.convertPointFromScreen(point, comp);

		if (this.popupMenu.isShowing())
		{
			this.popupMenu.setVisible(false);
		}
		else
		{
			this.popupMenu.show(comp, point.x, point.y);
		}
	}

}
