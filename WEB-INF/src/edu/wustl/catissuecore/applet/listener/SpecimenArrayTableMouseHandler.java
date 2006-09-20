package edu.wustl.catissuecore.applet.listener;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JApplet;
import javax.swing.table.TableModel;

import edu.wustl.catissuecore.applet.component.SpecimenArrayTable;
import edu.wustl.catissuecore.applet.model.SpecimenArrayTableModel;
import edu.wustl.catissuecore.applet.ui.SpecimenArrayApplet;
import edu.wustl.catissuecore.applet.util.SpecimenArrayAppletUtil;

/**
 * <p>This class initializes the fields of SpecimenArrayTableMouseHandler.java</p>
 * @author Ashwin Gupta
 * @version 1.1
 */
public class SpecimenArrayTableMouseHandler extends MouseAdapter {

	public void mouseClicked(MouseEvent e) {
		super.mouseClicked(e);
		SpecimenArrayTable arrayTable = (SpecimenArrayTable) e.getSource();
		int column = arrayTable.getSelectedColumn();
		int row = arrayTable.getSelectedRow();
		TableModel model = arrayTable.getModel();
		SpecimenArrayTableModel tableModel = null;
		
		if (model instanceof SpecimenArrayTableModel) {
			tableModel = (SpecimenArrayTableModel) model;
			String concentration = (String) tableModel.getSpecimenArrayModelMap().get(SpecimenArrayAppletUtil.getArrayMapKey(row,column,tableModel.getColumnCount(),3));
			String quantity = (String) tableModel.getSpecimenArrayModelMap().get(SpecimenArrayAppletUtil.getArrayMapKey(row,column,tableModel.getColumnCount(),2));
			JApplet applet = getBaseApplet(arrayTable);
			
			if (applet instanceof SpecimenArrayApplet) {
				SpecimenArrayApplet arrayApplet = (SpecimenArrayApplet) applet;
				arrayApplet.getConcentrationTextField().setText(concentration);
				arrayApplet.getQuantityTextField().setText(quantity);
				arrayApplet.getApplyButton().setEnabled(true);
			}
		}
		System.out.println(e.getSource());
	}
	
	public JApplet getBaseApplet(Component component) {
		while (component != null) {
			if (component instanceof JApplet) {
				return ((JApplet) component);
			}
			component = component.getParent();
		}
		return null;
	}

}
