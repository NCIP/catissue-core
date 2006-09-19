package edu.wustl.catissuecore.appletui.multiplespecimen.applet;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JScrollPane;

import edu.wustl.catissuecore.appletui.applet.BaseApplet;
import edu.wustl.catissuecore.appletui.component.BaseTable;
import edu.wustl.catissuecore.appletui.listener.caTissueHandler;
import edu.wustl.catissuecore.appletui.multiplespecimen.model.MultipleSpecimenTableModel;
import edu.wustl.catissuecore.appletui.multiplespecimen.model.SpecimenColumnModel;

/**
 * This applet displays main UI for multiple specimen page.
 * @author mandar_deshmukh
 */
public class MultipleSpecimenApplet extends BaseApplet {

	/**
	 * Default Serial Version ID
	 */
	private static final long serialVersionUID = 1L;

	public void doInit()
    {
 
		MultipleSpecimenTableModel model = new MultipleSpecimenTableModel(3);
        
		BaseTable table = new BaseTable(model)
        {
            public Class getColumnClass(int column)
            {
                return getValueAt(0, column).getClass();
            }
        };
        
			
		//table.getColumnModel().setColumnSelectionAllowed(true);
		table.setColumnSelectionAllowed(true);
		table.setRowHeight(3,50);
        JScrollPane scrollPane = new JScrollPane( table );
        getContentPane().add( scrollPane );
		JButton showAll = new JButton("ShowData");
		
		showAll.addActionListener(new caTissueHandler(table));

		getContentPane().add( showAll,BorderLayout.SOUTH );

        //  Create custom column
		SpecimenColumnModel SpecimenFieldColumn1= new SpecimenColumnModel(table, 1);
		SpecimenColumnModel SpecimenFieldColumn2 = new SpecimenColumnModel(table, 2);
		SpecimenColumnModel SpecimenFieldColumn3 = new SpecimenColumnModel(table, 3);
		
    }
 
    public static void main(String[] args)
    {
    	MultipleSpecimenApplet frame = new MultipleSpecimenApplet();
    }

}
