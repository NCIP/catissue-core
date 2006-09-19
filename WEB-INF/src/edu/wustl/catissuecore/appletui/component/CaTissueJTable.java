package edu.wustl.catissuecore.appletui.component;

import javax.swing.JTable;
import javax.swing.table.TableModel;


/**
 * Base table for caTissue UI.
 * @author Mandar Deshmukh
 * @author Rahul Ner
 */
public class CaTissueJTable extends JTable
{

	
	public CaTissueJTable(TableModel model)
	{
		super(model);
	}
}
