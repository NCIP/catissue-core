/*
 * <p>Title: SpecimenArrayTable.java</p>
 * <p>Description:	This class initializes the fields of SpecimenArrayTable.java</p>
 * Copyright: Copyright (c) year 2006
 * Company: Washington University, School of Medicine, St. Louis.
 * @version 1.1
 * Created on Sep 18, 2006
 */
/**
 * 
 */
package edu.wustl.catissuecore.applet.component;

import javax.swing.table.TableModel;

import edu.wustl.catissuecore.applet.listener.SpecimenArrayTableMouseHandler;




/**
 * <p>This class initializes the fields of SpecimenArrayTable.java</p>
 * @author Ashwin Gupta
 * @version 1.1
 */

public class SpecimenArrayTable extends BaseTable {

	/**
	 * Default Serial Version Id
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor with model
	 * @param tableModel table model
	 */
	public SpecimenArrayTable(TableModel tableModel) {
		super(tableModel);
		initUI();
	}
	
	/**
	 * Init UI 
	 */
	private void initUI() {
		addMouseListener(new SpecimenArrayTableMouseHandler());
	}
}
