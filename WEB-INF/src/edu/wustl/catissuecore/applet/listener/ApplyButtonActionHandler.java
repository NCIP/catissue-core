/*
 * <p>Title: ApplyButtonActionHandler.java</p>
 * <p>Description:	This class initializes the fields of ApplyButtonActionHandler.java</p>
 * Copyright: Copyright (c) year 2006
 * Company: Washington University, School of Medicine, St. Louis.
 * @version 1.1
 * Created on Sep 20, 2006
 */

package edu.wustl.catissuecore.applet.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;

import edu.wustl.catissuecore.applet.AppletConstants;
import edu.wustl.catissuecore.applet.component.SpecimenArrayTable;
import edu.wustl.catissuecore.applet.model.SpecimenArrayTableModel;
import edu.wustl.catissuecore.applet.ui.SpecimenArrayApplet;
import edu.wustl.catissuecore.applet.util.CommonAppletUtil;
import edu.wustl.catissuecore.applet.util.SpecimenArrayAppletUtil;

/**
 * <p>This class initializes the fields of ApplyButtonActionHandler.java</p>.
 * @author Ashwin Gupta
 * @version 1.1
 * @see java.awt.event.ActionListener
 */
public class ApplyButtonActionHandler implements ActionListener
{

	/**
	 * Specify the table field.
	 */
	private JTable table = null;

	/**
	 * Default constructor.
	 */
	public ApplyButtonActionHandler()
	{
	}

	/**
	 * constructor with table.
	 * @param table JTable
	 */
	public ApplyButtonActionHandler(JTable table)
	{
		this.table = table;
	}

	/**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 * @param e : e
	 */
	public void actionPerformed(ActionEvent e)
	{
		SpecimenArrayTable arrayTable = (SpecimenArrayTable) table;
		JApplet applet = CommonAppletUtil.getBaseApplet(arrayTable);

		if (applet instanceof SpecimenArrayApplet)
		{
			SpecimenArrayApplet arrayApplet = (SpecimenArrayApplet) applet;
			String conc = arrayApplet.getConcentrationTextField().getText();
			String quantity = arrayApplet.getQuantityTextField().getText();
			//Validator validator = new Validator();

			if ((!CommonAppletUtil.isDoubleNumeric(conc))
					|| (!CommonAppletUtil.isDoubleNumeric(quantity)))
			{
				JFrame frame = new JFrame();
				JOptionPane.showMessageDialog(frame,
						"Please enter valid numeric value for concentration & quantity",
						"Invalid Numeric Value", JOptionPane.WARNING_MESSAGE);
				return;
			}

			int[] rows = arrayTable.getSelectedRows();
			int[] columns = arrayTable.getSelectedColumns();
			SpecimenArrayTableModel model = (SpecimenArrayTableModel) arrayTable.getModel();
			String concKey = null;
			String quantityKey = null;
			Map modelMap = model.getSpecimenArrayModelMap();

			for (int i = 0; i < rows.length; i++)
			{
				for (int j = 0; j < columns.length; j++)
				{
					concKey = SpecimenArrayAppletUtil.getArrayMapKey(rows[i], columns[j], model
							.getColumnCount(), AppletConstants.
							ARRAY_CONTENT_ATTR_CONC_INDEX);
					modelMap.put(concKey, conc);
					quantityKey = SpecimenArrayAppletUtil.getArrayMapKey(rows[i],
							columns[j], model
							.getColumnCount(), AppletConstants.
							ARRAY_CONTENT_ATTR_QUANTITY_INDEX);
					modelMap.put(quantityKey, quantity);
					/*				System.out.println(" rows [" + i + "] :
					 * : " + rows[i]);
									System.out.println(" columns [" + i + "] :
									: " + columns[j]);
					*/}
			}
		}
	}

}
