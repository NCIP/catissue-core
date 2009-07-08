/*
 * <p>Title: BaseTable.java</p>
 * <p>Description:	This class initializes the fields of BaseTable.java</p>
 * Copyright: Copyright (c) year 2006
 * Company: Washington University, School of Medicine, St. Louis.
 * @version 1.1
 * Created on Sep 18, 2006
 */
/**
 *
 */

package edu.wustl.catissuecore.applet.component;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

/**
 * <p> Base Table component for all tables used as grid like specimen array/
 * multiple specimen etc.All table should extend this class.
 * </p>
 * @author Ashwin Gupta
 * @version 1.1
 * @see javax.swing.JTable
 */

public class BaseTable extends JTable
{

	/**
	 * Default Serial Version ID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Default Constructor.
	 */
	public BaseTable()
	{
		super();
		initUI();
	}

	/**
	 * Constructor with Table model.
	 * @param tableModel : tableModel
	 */
	public BaseTable(TableModel tableModel)
	{
		super(tableModel);
		initUI();
	}

	/**
	 * Constructor with Table model,table column model.
	 * @param tableModel table model
	 * @param tableColumnModel table column model
	 */
	public BaseTable(TableModel tableModel, TableColumnModel tableColumnModel)
	{
		super(tableModel, tableColumnModel);
		initUI();
	}

	/**
	 * Constructor with Table model,table column model,lise selection model.
	 * @param tableModel table model
	 * @param tableColumnModel table column model
	 * @param listSelectionModel list selection model
	 */
	public BaseTable(TableModel tableModel, TableColumnModel tableColumnModel,
			ListSelectionModel listSelectionModel)
	{
		super(tableModel, tableColumnModel, listSelectionModel);
		initUI();
	}

	/**
	 * Initialize UI for table.
	 */
	private void initUI()
	{
		getTableHeader().setReorderingAllowed(false);
		//this.selectionBackground = AppletConstants.CELL_SELECTION_COLOR;
	}

}
