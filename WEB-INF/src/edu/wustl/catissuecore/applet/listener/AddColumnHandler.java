/**
 * This class handles the Add Specimen button events. 
 * 
 * Created on Sep 26, 2006 
 * @author mandar_deshmukh
 *
 */
package edu.wustl.catissuecore.applet.listener;

import java.awt.event.ActionEvent;

import javax.swing.JTable;
import javax.swing.table.TableColumn;

import edu.wustl.catissuecore.applet.component.BaseTable;
import edu.wustl.catissuecore.applet.model.MultipleSpecimenTableModel;
import edu.wustl.catissuecore.applet.model.SpecimenColumnModel;

/**
 * This class handles the Add Specimen button events. 
 * 
 * Created on Sep 26, 2006 
 * @author mandar_deshmukh
 *
 */
public class AddColumnHandler extends ButtonHandler {

	
	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.applet.listener.BaseActionHandler#handleAction(java.awt.event.ActionEvent)
	 */
	protected void handleAction(ActionEvent event) {
		System.out.println("Inside AddColumnHandler");
		addColumnToTable(table);
	}

	public AddColumnHandler(JTable table) {
		super(table);
	}

	//	 This method adds a new column to table without reconstructing
    // all the other columns.
    private void addColumnToTable(JTable table) {
    	BaseTable baseTable = (BaseTable )table;
    	
    	if (baseTable.getAutoCreateColumnsFromModel()) {
            throw new IllegalStateException();
        }

       	
    	MultipleSpecimenTableModel model = (MultipleSpecimenTableModel)baseTable.getModel();
    	System.out.println("Start -- Table colCount : " +table.getColumnCount() + " Model ColCount : " + model.getColumnCount());
    	  
    	String headerLabel =model.getColumnName(model.getColumnCount());
//    	TableColumn col = new TableColumn(model.getColumnCount());
    	TableColumn col = new TableColumn(table.getColumnCount());
    	col.setHeaderValue(headerLabel);

    	//    	//1
    	table.addColumn(col);
    	System.out.println("After Table AddColumn -- Table colCount : " +table.getColumnCount() + " Model ColCount : " + model.getColumnCount());

//    	//2
//    	table.getColumnModel().addColumn(col);
//    	System.out.println("After TableColumnModel AddColumn -- Table colCount : " +table.getColumnCount() + " Model ColCount : " + model.getColumnCount());

//    	commenting to test addition of single column.
    	//3
//    	model.addColumn();
//    	System.out.println("After Model AddColumn -- Table colCount : " +table.getColumnCount() + " Model ColCount : " + model.getColumnCount());
    	
    	int colno = table.getColumnCount() - 1;
//    	int colno = model.getColumnCount() - 1 ;
    	new SpecimenColumnModel(baseTable,colno );

    	System.out.println("End -- Table colCount : " +table.getColumnCount() + " Model ColCount : " + model.getColumnCount());
    	model.addColumn(); 

    	//removes last added extra column
    	col = new TableColumn(table.getColumnCount());
    	table.removeColumn(col );
    	table.getColumnModel().removeColumn(col ); 
    	// ----------- this code adds 2 columns to the ui. Not getting Y.
//   	
//    	MultipleSpecimenTableModel model = (MultipleSpecimenTableModel)baseTable.getModel();
//    	int colno = model.getColumnCount();
//    	
//    	String headerLabel =model.getColumnName(model.getColumnCount());
//    	TableColumn col = new TableColumn(model.getColumnCount());
//    	col.setHeaderValue(headerLabel);
//    	table.addColumn(col);
//    	table.getColumnModel().addColumn(col);
//    	new SpecimenColumnModel(baseTable,colno );
//    	model.addColumn();
    	// ----------- this code adds 2 columns to the ui. Not getting Y.
       
    }
}
