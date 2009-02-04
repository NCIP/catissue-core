package edu.wustl.catissuecore.applet.listener;

import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Map;

import javax.swing.JTable;

import edu.wustl.catissuecore.applet.AppletConstants;
import edu.wustl.catissuecore.applet.model.SpecimenArrayTableModel;
import edu.wustl.catissuecore.applet.ui.SpecimenArrayApplet;
import edu.wustl.catissuecore.applet.util.CommonAppletUtil;
import edu.wustl.catissuecore.applet.util.SpecimenArrayAppletUtil;

/**
 * <p>This class initializes the fields of SpecimenArrayPasteActionHandler.java</p>
 * @author Ashwin Gupta
 * @version 1.1
 */
public class SpecimenArrayPasteActionHandler extends AbstractPasteActionHandler {

	/**
	 * constructor with table to persist table
	 * @param table table used in applet
	 */
	public SpecimenArrayPasteActionHandler(JTable table) {
		super(table);
	}
	
	/**
	 * @see edu.wustl.catissuecore.applet.listener.AbstractPasteActionHandler#doActionPerformed(java.awt.event.ActionEvent)
	 */
	protected void doActionPerformed(ActionEvent e) {
		super.doActionPerformed(e);
	}

	/**
	 * @see edu.wustl.catissuecore.applet.listener.AbstractPasteActionHandler#doPasteData(int, int, java.lang.Object)
	 */
	protected void doPasteData(int selectedRow, int selectedCol, List valueList)
	{
		if ((table != null) && (!valueList.isEmpty()))
		{
			SpecimenArrayTableModel model = (SpecimenArrayTableModel) table.getModel();
			if (model.getCopySelectedOption().equals(AppletConstants.ARRAY_COPY_OPTION_LABELBAR))
			{
				// update model to set copied data
				if (model.getEnterSpecimenBy().equalsIgnoreCase("Label"))
				{
					model.getSpecimenArrayModelMap().put(SpecimenArrayAppletUtil.getArrayMapKey(selectedRow,selectedCol,model.getColumnCount(),AppletConstants.ARRAY_CONTENT_ATTR_LABEL_INDEX),valueList.get(0));
				}
				else
				{
					model.getSpecimenArrayModelMap().put(SpecimenArrayAppletUtil.getArrayMapKey(selectedRow,selectedCol,model.getColumnCount(),AppletConstants.ARRAY_CONTENT_ATTR_BARCODE_INDEX),valueList.get(0));
				}
			}
			else if (model.getCopySelectedOption().equals(AppletConstants.ARRAY_COPY_OPTION_QUANTITY))
			{
				String value = (String) valueList.get(0);
				if (value == null)
				{
					value = "";
				}
				model.getSpecimenArrayModelMap().put(SpecimenArrayAppletUtil.getArrayMapKey(selectedRow,selectedCol,model.getColumnCount(),AppletConstants.ARRAY_CONTENT_ATTR_QUANTITY_INDEX),value);
				// update quantity text field details
				((SpecimenArrayApplet) CommonAppletUtil.getBaseApplet(table)).getQuantityTextField().setText(value);
				//model.getSpecimenArrayModelMap().put(SpecimenArrayAppletUtil.getArrayMapKey(selectedRow,selectedCol,model.getColumnCount(),AppletConstants.ARRAY_CONTENT_ATTR_QUANTITY_ID_INDEX),valueList.get(1));
			}
			else if (model.getCopySelectedOption().equals(AppletConstants.ARRAY_COPY_OPTION_CONCENTRATION))
			{
				String value = (String) valueList.get(0);
				if (value == null)
				{
					value = "";
				}
				model.getSpecimenArrayModelMap().put(SpecimenArrayAppletUtil.getArrayMapKey(selectedRow,selectedCol,model.getColumnCount(),AppletConstants.ARRAY_CONTENT_ATTR_CONC_INDEX),value);
				// update concentration text field details
				((SpecimenArrayApplet) CommonAppletUtil.getBaseApplet(table)).getConcentrationTextField().setText(value);
			}
			else if (model.getCopySelectedOption().equals(AppletConstants.ARRAY_COPY_OPTION_ALL))
			{
				if (model.getEnterSpecimenBy().equalsIgnoreCase("Label"))
				{
					model.getSpecimenArrayModelMap().put(SpecimenArrayAppletUtil.getArrayMapKey(selectedRow,selectedCol,model.getColumnCount(),AppletConstants.ARRAY_CONTENT_ATTR_LABEL_INDEX),valueList.get(0));
				}
				else
				{
					model.getSpecimenArrayModelMap().put(SpecimenArrayAppletUtil.getArrayMapKey(selectedRow,selectedCol,model.getColumnCount(),AppletConstants.ARRAY_CONTENT_ATTR_BARCODE_INDEX),valueList.get(0));
				}
				String valueQuantity = (String) valueList.get(1);
				if (valueQuantity == null)
				{
					valueQuantity = "";
				}

				model.getSpecimenArrayModelMap().put(SpecimenArrayAppletUtil.getArrayMapKey(selectedRow,selectedCol,model.getColumnCount(),AppletConstants.ARRAY_CONTENT_ATTR_QUANTITY_INDEX),valueQuantity);
				((SpecimenArrayApplet) CommonAppletUtil.getBaseApplet(table)).getQuantityTextField().setText(valueQuantity);
				
				String valueConc = (String) valueList.get(2);
				if (valueConc == null)
				{
					valueConc = "";
				}
				model.getSpecimenArrayModelMap().put(SpecimenArrayAppletUtil.getArrayMapKey(selectedRow,selectedCol,model.getColumnCount(),AppletConstants.ARRAY_CONTENT_ATTR_CONC_INDEX),valueConc);
				((SpecimenArrayApplet) CommonAppletUtil.getBaseApplet(table)).getConcentrationTextField().setText(valueConc);
			}
			doPasteDimensions(model,selectedRow,selectedCol);
		}
	}
	
	/**
	 * @return total coumn count
	 */
	protected int getColumnCount()
	{
		return table.getColumnCount();		
	}
	
	/**
	 * @param model
	 * @param selectedRow
	 * @param selectedCol
	 */
	private void doPasteDimensions(SpecimenArrayTableModel model,int selectedRow, int selectedCol)
	{
		String posOneDimKey = SpecimenArrayAppletUtil.getArrayMapKey(selectedRow,selectedCol,model.getColumnCount(),AppletConstants.ARRAY_CONTENT_ATTR_POS_DIM_ONE_INDEX);
		String posTwoDimKey = SpecimenArrayAppletUtil.getArrayMapKey(selectedRow,selectedCol,model.getColumnCount(),AppletConstants.ARRAY_CONTENT_ATTR_POS_DIM_TWO_INDEX);
		Map specimenArrayModelMap = model.getSpecimenArrayModelMap();
		System.out.println(" In doPasteDimensions() function");
		if ((specimenArrayModelMap.get(posOneDimKey) == null) || (specimenArrayModelMap.get(posOneDimKey).toString().equals(""))) 
		{
			model.getSpecimenArrayModelMap().put(posOneDimKey,String.valueOf(selectedRow));
			model.getSpecimenArrayModelMap().put(posTwoDimKey,String.valueOf(selectedCol));
		}
	}
}
