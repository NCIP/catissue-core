/*
 * <p>Title: SpecimenArrayApplet.java</p>
 * <p>Description:	This class initializes the fields of SpecimenArrayApplet.java</p>
 * Copyright: Copyright (c) year 2006
 * Company: Washington University, School of Medicine, St. Louis.
 * @version 1.1
 * Created on Sep 18, 2006
 */
/**
 * 
 */
package edu.wustl.catissuecore.appletui.specimenarray.applet;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableModel;

import edu.wustl.catissuecore.appletui.applet.BaseApplet;
import edu.wustl.catissuecore.appletui.constant.AppletConstants;
import edu.wustl.catissuecore.appletui.specimenarray.component.SpecimenArrayTable;
import edu.wustl.catissuecore.appletui.specimenarray.model.SpecimenArrayTableModel;
import edu.wustl.catissuecore.appletui.specimenarray.util.SpecimenArrayUtil;

/**
 * <p>This class specifies the methods used to render specimen array applet.It is extending
 * BaseApplet class.
 * </p>
 * @author Ashwin Gupta
 * @version 1.1
 */

public class SpecimenArrayApplet extends BaseApplet {

	/**
	 * Default Serial Version ID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @see edu.wustl.catissuecore.appletui.applet.BaseApplet#doInit()
	 */
	protected void doInit() {
		super.doInit();
		int rowCount = 3;
		int columnCount = 3;
		String enterSpecimenBy = "Label";
		Map tableModelMap = new HashMap();
		for (int i=0; i < rowCount ; i++) {
		  for (int j=0;j < columnCount; j++) {
			for(int k=0; k < AppletConstants.ARRAY_CONTENT_ATTRIBUTE_NAMES.length; k++) {
				tableModelMap.put(SpecimenArrayUtil.getArrayMapKey(i,j,columnCount,k),"");
			}
		  }
		}
		
		JLabel concLabel = new JLabel("Concentration");
		concLabel.setOpaque(false);
		JLabel quantityLabel = new JLabel("Quantity");
		quantityLabel.setOpaque(false);
		JTextField concentrationTextField = new JTextField();
		concentrationTextField.setName("concentrationTextField");
		Dimension concDimension = new Dimension(100,concentrationTextField.getPreferredSize().height);
		concentrationTextField.setPreferredSize(concDimension);
		JPanel concPanel = new JPanel();
		concPanel.setOpaque(false);
		concPanel.setLayout(new FlowLayout(FlowLayout.LEFT,5,0));
		concPanel.add(concLabel);
		concPanel.add(concentrationTextField);
		
		JTextField quantityTextField = new JTextField();
		quantityTextField.setName("quantityTextField");
		Dimension quantityDimension = new Dimension(100,quantityTextField.getPreferredSize().height);
		quantityTextField.setPreferredSize(quantityDimension);
		System.out.println(quantityTextField.getPreferredSize());
		JPanel quantityPanel = new JPanel();
		quantityPanel.setOpaque(false);
		quantityPanel.setLayout(new FlowLayout(FlowLayout.LEFT,5,0));
		quantityPanel.add(quantityLabel);
		quantityPanel.add(quantityTextField);
		
		
		JButton applyButton = new JButton("Apply");
		applyButton.setEnabled(false);
		
		JPanel applyPanel = new JPanel();
		applyPanel.setOpaque(false);
		applyPanel.setLayout(new FlowLayout(FlowLayout.LEFT,10,0));
		
		applyPanel.add(concPanel);
		applyPanel.add(quantityPanel);
		applyPanel.add(applyButton);
		
		//tableModelMap.put(AppletConstants.ARRAY_GRID_COMPONENT_KEY,gridContentList);
		TableModel tableModel = new SpecimenArrayTableModel(tableModelMap,rowCount,columnCount,enterSpecimenBy);
		SpecimenArrayTable arrayTable = new SpecimenArrayTable(tableModel);
		arrayTable.getColumnModel().setColumnSelectionAllowed(true);
		arrayTable.setCellSelectionEnabled(true);
		arrayTable.getColumnModel().getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane scrollPane = new JScrollPane(arrayTable);
        this.getContentPane().add(applyPanel,BorderLayout.CENTER);
        this.getContentPane().add(scrollPane,BorderLayout.SOUTH);
	}
}
