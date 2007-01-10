/*
 * Class for fixing the RowHeader scroll.
 * 
 * Created on Sep 26, 2006
 * @author mandar_deshmukh
 * 
 */
package edu.wustl.catissuecore.applet.ui;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import edu.wustl.catissuecore.applet.component.BaseTable;
import edu.wustl.catissuecore.applet.model.MultipleSpecimenTableModel;
import edu.wustl.catissuecore.applet.model.RowHeaderColumnModel;

/**
 * @author mandar_deshmukh
 *
 * Class for fixing the RowHeader scroll.
 */
public class FixedColumnScrollPane extends JScrollPane {
//	 public FixedColumnScrollPane(JTable main, int fixedColumns)
//	    {
//	        super( main );
//	 
//	        //  Use the table to create a new table sharing
//	        //  the DataModel and ListSelectionModel
//	 
//	        BaseTable fixed = new BaseTable( main.getModel() );
//	        fixed.setFocusable( false );
//	        fixed.setSelectionModel( main.getSelectionModel() );
//	        fixed.getTableHeader().setReorderingAllowed( false );
////	        fixed.getTableHeader().setResizingAllowed( false );
//	        fixed.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
//	        main.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
//	 
//	        //  Remove the fixed columns from the main table
//	 
//	        for (int i = 0; i < fixedColumns; i++)
//	        {
//	            TableColumnModel columnModel = main.getColumnModel();
//	            columnModel.removeColumn( columnModel.getColumn( 0 ) );
//	        }
//	 
//	        //  Remove the non-fixed columns from the fixed table
//	 
//	        while (fixed.getColumnCount() > fixedColumns)
//	        {
//	            TableColumnModel columnModel = fixed.getColumnModel();
//	            columnModel.removeColumn( columnModel.getColumn( fixedColumns ) );
//	        }
//	        //setting row height
//	        new RowHeaderColumnModel(fixed);		//	for zeroth column
//	        for(int i=0;i<main.getRowCount();i++ )
//	        {
//	        	fixed.setRowHeight(i,main.getRowHeight(i));
//	        }
//	        TableColumnModel columnModel = fixed.getColumnModel();
//			columnModel.getColumn(0).setResizable(false );
//			columnModel.getColumn(0).setPreferredWidth(175 );
//	        //  Add the fixed table to the scroll pane
//	 
//	        fixed.setPreferredScrollableViewportSize(fixed.getPreferredSize());
//	        setRowHeaderView( fixed );
//	        setCorner(JScrollPane.UPPER_LEFT_CORNER, fixed.getTableHeader());
//	    }
	
	 // ---------------------------------------MD 28Sep06
	 public FixedColumnScrollPane(JTable main)
	    {
	        super( main );
	 
	 //       DefaultTableModel model = new DefaultTableModel (MultipleSpecimenTableModel.getRowHeaders().length ,1);
	        Object header[] = {""};
	        DefaultTableModel model = new DefaultTableModel (header,MultipleSpecimenTableModel.getRowHeaders().length );
	        BaseTable fixed = new BaseTable(model);
//	        fixed.setFocusable( false );
	        fixed.setSelectionModel( main.getSelectionModel() );
	        fixed.getTableHeader().setReorderingAllowed( false );
	        fixed.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
	        main.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
	 
	        //setting row height
	        new RowHeaderColumnModel(fixed);		//	for zeroth column
	        for(int i=0;i<main.getRowCount();i++ )
	        {
	        	fixed.setRowHeight(i,main.getRowHeight(i));
	        }
	        TableColumnModel columnModel = fixed.getColumnModel();
			columnModel.getColumn(0).setResizable(false );
			columnModel.getColumn(0).setPreferredWidth(175 );
	        //  Add the fixed table to the scroll pane
	 
	        fixed.setPreferredScrollableViewportSize(fixed.getPreferredSize());
	        setRowHeaderView( fixed );
	        setCorner(JScrollPane.UPPER_LEFT_CORNER, fixed.getTableHeader());
	    }
		 
	 // ---------------------------------------MD 28Sep06
}