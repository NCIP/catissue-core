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
import edu.wustl.catissuecore.applet.ui.MultipleSpecimenApplet;

/**
 * This class handles the Add Specimen button events. 
 * 
 * Created on Sep 26, 2006 
 * @author mandar_deshmukh
 *
 */
public class AddColumnHandler extends ButtonHandler {

	private MultipleSpecimenApplet applet;
	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.applet.listener.BaseActionHandler#handleAction(java.awt.event.ActionEvent)
	 */
	protected void handleAction(ActionEvent event) {
		System.out.println("Inside AddColumnHandler");
		System.out.println("table.getColumnCount() : "+ table.getColumnCount());
		int tableColumnCount = table.getColumnCount();
		MultipleSpecimenTableModel model = (MultipleSpecimenTableModel)table.getModel();
		System.out.println("table.getColumnCount() : "+ table.getColumnCount()+" | model.getColumnsPerPage(): "+model.getColumnsPerPage());
		if(tableColumnCount == model.getColumnsPerPage())
		{
			System.out.println("Applet to be updated for page buttons.");
			model.addColumn(); 
			applet.updateOnAddSpecimen();
			
		}else if(tableColumnCount < model.getColumnsPerPage())
		{
			addColumnToTable(table);	
		}
	}

	public AddColumnHandler(JTable table, MultipleSpecimenApplet applet) {
		super(table);
		this.applet = applet;
	}

	//	 This method adds a new column to table without reconstructing
    // all the other columns.
    private void addColumnToTable(JTable table) 
    {
    	BaseTable baseTable = (BaseTable )table;
    	
    	if (baseTable.getAutoCreateColumnsFromModel()) 
    	{
            throw new IllegalStateException();
        }
    	MultipleSpecimenTableModel model = (MultipleSpecimenTableModel)baseTable.getModel();
    	System.out.println("Start -- Table colCount : " +table.getColumnCount() + " Model ColCount : " + model.getColumnCount());
    	  
    	String headerLabel =model.getColumnName(model.getColumnCount());
    	TableColumn col = new TableColumn(table.getColumnCount());
    	col.setHeaderValue(headerLabel);

    	//    	//1
    	table.addColumn(col);
    	System.out.println("After Table AddColumn -- Table colCount : " +table.getColumnCount() + " Model ColCount : " + model.getColumnCount());

    	int colno = table.getColumnCount() - 1;
    	new SpecimenColumnModel(baseTable,colno );

    	System.out.println("End -- Table colCount : " +table.getColumnCount() + " Model ColCount : " + model.getColumnCount());
    	model.addColumn(); 

    	//removes last added extra column
    	col = new TableColumn(table.getColumnCount());
    	table.removeColumn(col );
    	table.getColumnModel().removeColumn(col ); 
    }
}
