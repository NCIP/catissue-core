
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
	 * @param actionEvent : e
	 */
	@Override
	protected void doActionPerformed(ActionEvent actionEvent)
	{
		final JMenuItem menuItem = (JMenuItem) actionEvent.getSource();
		((SpecimenArrayTableModel) this.table.getModel()).setCopySelectedOption(menuItem.getText());
		super.doActionPerformed(actionEvent);
		// enable paste button
		final SpecimenArrayApplet arrayApplet = (SpecimenArrayApplet) CommonAppletUtil
				.getBaseApplet(this.table);
		arrayApplet.getPasteButton().setEnabled(true);
	}

	/**
	 * @see edu.wustl.catissuecore.applet.listener.AbstractCopyActionHandler#getValueList(int, int)
	 * @param columnIndex : columnIndex
	 * @param rowIndex : rowIndex
	 * @return List
	 */
	@Override
	protected List getValueList(int rowIndex, int columnIndex)
	{
		List valueList = new ArrayList();
		final SpecimenArrayTableModel model = ((SpecimenArrayTableModel) this.table.getModel());
		if (model.getCopySelectedOption().equals(AppletConstants.ARRAY_COPY_OPTION_LABELBAR))
		{
			valueList.add(this.getCopiedLabelOrBarcode(model, rowIndex, columnIndex));
		}
		else if (model.getCopySelectedOption().equals(AppletConstants.ARRAY_COPY_OPTION_QUANTITY))
		{
			valueList = new ArrayList(this.getCopiedQuantity(model, rowIndex, columnIndex));
		}
		else if (model.getCopySelectedOption().equals(
				AppletConstants.ARRAY_COPY_OPTION_CONCENTRATION))
		{
			valueList.add(this.getCopiedConcentration(model, rowIndex, columnIndex));
		}
		else if (model.getCopySelectedOption().equals(AppletConstants.ARRAY_COPY_OPTION_ALL))
		{
			valueList = new ArrayList(this.getCopiedAll(model, rowIndex, columnIndex));
		}
		return valueList;
	}

	/**
	 * Gets label or barcode depends upon enter specimen by options is selected.
	 * @param model model
	 * @param rowIndex : rowIndex
	 * @param columnIndex : columnIndex
	 * @return String
	 */
	private String getCopiedLabelOrBarcode(SpecimenArrayTableModel model, int rowIndex,
			int columnIndex)
	{
		String modelMap = "" ;
		if (model.getEnterSpecimenBy().equalsIgnoreCase("Label"))
		{
			modelMap = (String) model.getSpecimenArrayModelMap().get(
					SpecimenArrayAppletUtil.getArrayMapKey(rowIndex, columnIndex, model
							.getColumnCount(), AppletConstants.ARRAY_CONTENT_ATTR_LABEL_INDEX));
		}
		else
		{
			modelMap = (String) model.getSpecimenArrayModelMap().get(
					SpecimenArrayAppletUtil.getArrayMapKey(rowIndex, columnIndex, model
							.getColumnCount(), AppletConstants.ARRAY_CONTENT_ATTR_BARCODE_INDEX));
		}
		return modelMap ;
	}

	/**
	 * Gets selected quantity for copy.
	 * @param model : model
	 * @param rowIndex : rowIndex
	 * @param columnIndex : columnIndex
	 * @return List
	 */
	private List getCopiedQuantity(SpecimenArrayTableModel model, int rowIndex, int columnIndex)
	{
		final List valueList = new ArrayList();
		valueList.add(model.getSpecimenArrayModelMap().get(
				SpecimenArrayAppletUtil.getArrayMapKey(rowIndex, columnIndex, model
						.getColumnCount(), AppletConstants.ARRAY_CONTENT_ATTR_QUANTITY_INDEX)));
		//valueList.add((String) model.getSpecimenArrayModelMap().
		//get(SpecimenArrayAppletUtil.getArrayMapKey(rowIndex,columnIndex,model.getColumnCount(),
		//AppletConstants.ARRAY_CONTENT_ATTR_QUANTITY_ID_INDEX)));
		return valueList;
	}

	/**
	 * Gets selected Concentration for copy.
	 * @param model : model
	 * @param rowIndex : rowIndex
	 * @param columnIndex : columnIndex
	 * @return String
	 */
	private String getCopiedConcentration(SpecimenArrayTableModel model, int rowIndex,
			int columnIndex)
	{
		return (String) model.getSpecimenArrayModelMap().get(
				SpecimenArrayAppletUtil.getArrayMapKey(rowIndex, columnIndex, model
						.getColumnCount(), AppletConstants.ARRAY_CONTENT_ATTR_CONC_INDEX));
	}

	/**
	 * get all copied data.it is used when "All" option is selected among copy popup options.
	 * @param model : model
	 * @param rowIndex : rowIndex
	 * @param columnIndex : columnIndex
	 * @return List
	 */
	private List getCopiedAll(SpecimenArrayTableModel model, int rowIndex, int columnIndex)
	{
		final List valueList = new ArrayList();
		valueList.add(this.getCopiedLabelOrBarcode(model, rowIndex, columnIndex));
		valueList.add(model.getSpecimenArrayModelMap().get(
				SpecimenArrayAppletUtil.getArrayMapKey(rowIndex, columnIndex, model
						.getColumnCount(), AppletConstants.ARRAY_CONTENT_ATTR_QUANTITY_INDEX)));
		//valueList.add((String) model.getSpecimenArrayModelMap().
		//get(SpecimenArrayAppletUtil.getArrayMapKey(rowIndex,columnIndex,
		//model.getColumnCount(),AppletConstants.ARRAY_CONTENT_ATTR_QUANTITY_ID_INDEX)));
		valueList.add(this.getCopiedConcentration(model, rowIndex, columnIndex));
		return valueList;
	}

	/**
	 * @return total coumn count
	 */
	@Override
	protected int getColumnCount()
	{
		return this.table.getColumnCount();
	}

	@Override
	protected void postActionPerformed(ActionEvent actionEvent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void preActionPerformed(ActionEvent actionEvent) {
		// TODO Auto-generated method stub
		
	}
}
