/**
 * This is a Handler class for item events on the Specimen Collection Group radio button.
 * 
 * @author Mandar Deshmukh
 * Created on Sep 20, 2006
 * 
 */
package edu.wustl.catissuecore.applet.listener;

import java.awt.event.ItemEvent;

import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.table.TableColumnModel;

import edu.wustl.catissuecore.applet.model.SpecimenColumnModel;
import edu.wustl.catissuecore.applet.util.CommonAppletUtil;

/**
 * @author mandar_deshmukh
 *
 *  This is a Handler class for item events on the Specimen Collection Group radio button.
 * 
 */
public class CollectionGroupItemHandler extends BaseItemHandler {

	/**
	 * @param table
	 */
	public CollectionGroupItemHandler(JTable table)
	{
		super(table);
	}
	
	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.applet.listener.BaseItemHandler#handleAction(java.awt.event.ItemEvent)
	 */
	protected void handleAction(ItemEvent event)
	{
		super.handleAction(event);
		System.out.println("In CollectionGroupItemHandler");
		if(table.getSelectedColumn() != -1)
		{
			CommonAppletUtil.getMultipleSpecimenTableModel(table).setCollectionGroupRadioButtonValueAt(table.getSelectedColumn(),((JRadioButton)event.getSource()).isSelected()  );
            
               /**
              * Patch ID: 3835_1_33
              * See also: 1_1 to 1_5
              * Description :If Specimen group name checkbox is enabled then createdOn should be disabled  
              */   

             TableColumnModel columnModel = table.getColumnModel();

             SpecimenColumnModel scm = (SpecimenColumnModel) columnModel.getColumn(table.getSelectedColumn()).getCellEditor();

             if(scm.getRbspecimenGroup())
             {
                 //made createdon readonly and blank                 
                 scm.setCreatedOnStatus(false);
             }
             else
             {
                 //set creted on current date.
                 scm.setCreatedOnStatus(true); 
             }                               
		}
	}

}
