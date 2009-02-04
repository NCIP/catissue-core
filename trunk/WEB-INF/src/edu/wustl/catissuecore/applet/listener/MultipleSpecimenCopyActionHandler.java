
package edu.wustl.catissuecore.applet.listener;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JTable;

import edu.wustl.catissuecore.applet.AppletConstants;
import edu.wustl.catissuecore.applet.AppletServerCommunicator;
import edu.wustl.catissuecore.applet.CopyPasteOperationValidatorModel;
import edu.wustl.catissuecore.applet.model.BaseAppletModel;
import edu.wustl.catissuecore.applet.model.MultipleSpecimenTableModel;
import edu.wustl.catissuecore.applet.ui.MultipleSpecimenApplet;
import edu.wustl.catissuecore.applet.util.CommonAppletUtil;

/**
 * <p>This class handles multiple specimen copy operation.This handler gets called
 * when user clicks on Copy button of multiple specimen.</p>
 * @author Ashwin Gupta
 * @author mandar_deshmukh
 * @version 1.1
 */
public class MultipleSpecimenCopyActionHandler extends AbstractCopyActionHandler
{

	private JButton paste;

	/**
	 * Empty constructor
	 */
	public MultipleSpecimenCopyActionHandler()
	{
		super();
	}

	/**
	 * @param table
	 */
	public MultipleSpecimenCopyActionHandler(JTable table, JButton paste)
	{
		super(table);
		this.paste = paste;
	}

	/**
	 * @see edu.wustl.catissuecore.applet.listener.AbstractCopyActionHandler#doActionPerformed(java.awt.event.ActionEvent)
	 */
	protected void doActionPerformed(ActionEvent event)
	{
		CommonAppletUtil.getAllDataOnPage(table); 
		
		super.doActionPerformed(event);
		paste.setEnabled(true);
	
		if (isValidateSuccess)
		{
			MultipleSpecimenTableModel multipleSpecimenTableModel = CommonAppletUtil.getMultipleSpecimenTableModel(table);
			multipleSpecimenTableModel.showMapData();

			CopyPasteOperationValidatorModel validatorModel = multipleSpecimenTableModel.getCopyPasteOperationValidatorModel();
			List selectedCopiedRows = validatorModel.getSelectedCopiedRows();
			/**
			 *  check if button(s) also copied
			 */
			boolean isButtonCopied = false;
			for (int i = 0; i < selectedCopiedRows.size(); i++)
			{
				int copiedRow = ((Integer) selectedCopiedRows.get(i)).intValue();
				if (copiedRow >= AppletConstants.SPECIMEN_COMMENTS_ROW_NO)
				{
					isButtonCopied = true;
					break;
				}
			}

			if (isButtonCopied)
			{
				List selectedCopiedCols = validatorModel.getSelectedCopiedCols();
				List tempSelectedCopiedCols = new ArrayList();
				for (int i = 0; i < selectedCopiedCols.size(); i++)
				{
					int copiedCol = ((Integer) selectedCopiedCols.get(i)).intValue();
					Integer tempCopiedCol = new Integer(multipleSpecimenTableModel.getActualColumnNo(copiedCol));
					tempSelectedCopiedCols.add(tempCopiedCol);
				}

				validatorModel.setSelectedCopiedCols(tempSelectedCopiedCols);
				validatorModel.setPageIndexWhenCopied(multipleSpecimenTableModel.getCurrentPageIndex());
				BaseAppletModel appletModel = new BaseAppletModel();
				Map validatorDataMap = new HashMap();
				validatorDataMap.put(AppletConstants.VALIDATOR_MODEL, validatorModel);
				appletModel.setData(validatorDataMap);
				try
				{
					MultipleSpecimenApplet applet = (MultipleSpecimenApplet) CommonAppletUtil.getBaseApplet(table);
					String url = applet.getServerURL() + "/MultipleSpecimenCopyPasteAction.do?method=copy";

					appletModel = (BaseAppletModel) AppletServerCommunicator.doAppletServerCommunication(url, appletModel);

				}
				catch (Exception e)
				{
					e.printStackTrace();
					System.out.println("Exception");
				}
				validatorModel.setSelectedCopiedCols(selectedCopiedCols);
			}
		}

		System.out.println("\n >>>>>>>>>>>>>>  DONE >>>>>>>>>>>>");
	}

	/**
	 * @return JS method name for this button.
	 */
	protected String getJSMethodName()
	{
		return "showErrorMessage";
	}

	/**
	 * @return total coumn count
	 */
	protected int getColumnCount()
	{
		return CommonAppletUtil.getMultipleSpecimenTableModel(table).getTotalColumnCount();
	}

}