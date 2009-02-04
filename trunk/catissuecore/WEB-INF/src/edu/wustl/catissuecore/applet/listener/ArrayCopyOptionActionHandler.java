package edu.wustl.catissuecore.applet.listener;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JTable;

import edu.wustl.catissuecore.applet.AppletConstants;
import edu.wustl.catissuecore.applet.model.SpecimenArrayTableModel;
import edu.wustl.catissuecore.applet.ui.SpecimenArrayApplet;
import edu.wustl.catissuecore.applet.util.CommonAppletUtil;
import edu.wustl.catissuecore.applet.util.SpecimenArrayAppletUtil;


/**
 * <p>This class is used to handle Array level copy operation.</p>
 * @author Ashwin Gupta
 * @version 1.1
 */
public class ArrayCopyOptionActionHandler extends AbstractCopyActionHandler 
{

	/**
	 * constructor with table to persist table objet.
	 * @param table table used in applet
	 */
	public ArrayCopyOptionActionHandler(JTable table)
	{
		super(table);
	}
	
	/**
	 * @see edu.wustl.catissuecore.applet.listener.AbstractCopyActionHandler#doActionPerformed()
	 */
	protected void doActionPerformed(ActionEvent e) {
		JMenuItem menuItem = (JMenuItem) e.getSource();
		((SpecimenArrayTableModel) table.getModel()).setCopySelectedOption(menuItem.getText());
		super.doActionPerformed(e);
		// enable paste button
		SpecimenArrayApplet arrayApplet = (SpecimenArrayApplet) CommonAppletUtil.getBaseApplet(table);
		arrayApplet.getPasteButton().setEnabled(true);
	}
	
	/**
	 * @see edu.wustl.catissuecore.applet.listener.AbstractCopyActionHandler#getValueList(int, int)
	 */
	protected List getValueList(int rowIndex,int columnIndex)
	{
		List valueList = new ArrayList();
		SpecimenArrayTableModel model = ((SpecimenArrayTableModel) table.getModel());
		if (model.getCopySelectedOption().equals(AppletConstants.ARRAY_COPY_OPTION_LABELBAR))
		{
			valueList.add(getCopiedLabelOrBarcode(model,rowIndex,columnIndex));
		}
		else if (model.getCopySelectedOption().equals(AppletConstants.ARRAY_COPY_OPTION_QUANTITY))
		{
			valueList = new ArrayList(getCopiedQuantity(model,rowIndex,columnIndex));
		}
		else if (model.getCopySelectedOption().equals(AppletConstants.ARRAY_COPY_OPTION_CONCENTRATION))
		{	
			valueList.add(getCopiedConcentration(model,rowIndex,columnIndex));
		}
		else if (model.getCopySelectedOption().equals(AppletConstants.ARRAY_COPY_OPTION_ALL))
		{
			valueList = new ArrayList(getCopiedAll(model,rowIndex,columnIndex));
		}
		return valueList;
	}
	
	/** 
	 * Gets label or barcode depends upon enter specimen by options is selected.
	 * @param model model
	 * @param rowIndex
	 * @param columnIndex
	 * @return
	 */
	private String getCopiedLabelOrBarcode(SpecimenArrayTableModel model,int rowIndex,int columnIndex)
	{
		if (model.getEnterSpecimenBy().equalsIgnoreCase("Label"))
		{
			return (String) model.getSpecimenArrayModelMap().get(SpecimenArrayAppletUtil.getArrayMapKey(rowIndex,columnIndex,model.getColumnCount(),AppletConstants.ARRAY_CONTENT_ATTR_LABEL_INDEX));
		}
		else
		{
			return (String) model.getSpecimenArrayModelMap().get(SpecimenArrayAppletUtil.getArrayMapKey(rowIndex,columnIndex,model.getColumnCount(),AppletConstants.ARRAY_CONTENT_ATTR_BARCODE_INDEX));
		}
	}
	

	/**
	 * Gets selected quantity for copy.
	 * @param model
	 * @param rowIndex
	 * @param columnIndex
	 * @return
	 */
	private List getCopiedQuantity(SpecimenArrayTableModel model,int rowIndex,int columnIndex)
	{
		List valueList = new ArrayList();
		valueList.add((String) model.getSpecimenArrayModelMap().get(SpecimenArrayAppletUtil.getArrayMapKey(rowIndex,columnIndex,model.getColumnCount(),AppletConstants.ARRAY_CONTENT_ATTR_QUANTITY_INDEX)));
		//valueList.add((String) model.getSpecimenArrayModelMap().get(SpecimenArrayAppletUtil.getArrayMapKey(rowIndex,columnIndex,model.getColumnCount(),AppletConstants.ARRAY_CONTENT_ATTR_QUANTITY_ID_INDEX)));
		return valueList;		
	}

	/**
	 * Gets selected Concentration for copy.
	 * @param model
	 * @param rowIndex
	 * @param columnIndex
	 * @return
	 */
	private String getCopiedConcentration(SpecimenArrayTableModel model,int rowIndex,int columnIndex)
	{
		return (String) model.getSpecimenArrayModelMap().get(SpecimenArrayAppletUtil.getArrayMapKey(rowIndex,columnIndex,model.getColumnCount(),AppletConstants.ARRAY_CONTENT_ATTR_CONC_INDEX));
	}
	
	/**
	 * get all copied data.it is used when "All" option is selected among copy popup options.
	 * @param model
	 * @param rowIndex
	 * @param columnIndex
	 * @return
	 */
	private List getCopiedAll(SpecimenArrayTableModel model,int rowIndex,int columnIndex)
	{
		List valueList = new ArrayList();
		valueList.add(getCopiedLabelOrBarcode(model,rowIndex,columnIndex));
		valueList.add((String) model.getSpecimenArrayModelMap().get(SpecimenArrayAppletUtil.getArrayMapKey(rowIndex,columnIndex,model.getColumnCount(),AppletConstants.ARRAY_CONTENT_ATTR_QUANTITY_INDEX)));
		//valueList.add((String) model.getSpecimenArrayModelMap().get(SpecimenArrayAppletUtil.getArrayMapKey(rowIndex,columnIndex,model.getColumnCount(),AppletConstants.ARRAY_CONTENT_ATTR_QUANTITY_ID_INDEX)));
		valueList.add(getCopiedConcentration(model,rowIndex,columnIndex));
		return valueList;
	}
	
	/**
	 * @return total coumn count
	 */
	protected int getColumnCount()
	{
		return table.getColumnCount();		
	}
}
