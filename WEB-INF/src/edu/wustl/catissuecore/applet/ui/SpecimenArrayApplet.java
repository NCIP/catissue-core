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
package edu.wustl.catissuecore.applet.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableModel;

import edu.wustl.catissuecore.applet.component.SpecimenArrayTable;
import edu.wustl.catissuecore.applet.model.AppletModelInterface;
import edu.wustl.catissuecore.applet.model.BaseAppletModel;
import edu.wustl.catissuecore.applet.model.SpecimenArrayTableModel;
import edu.wustl.catissuecore.applet.AppletServerCommunicator;

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
	 * Specify the quantityTextField field 
	 */
	private JTextField quantityTextField = null;
	
	/**
	 * Specify the concentrationTextField field 
	 */
	private JTextField concentrationTextField = null;
	
	/**
	 * Specify the applyButton field 
	 */
	private JButton applyButton = null;
	
/*	*//**
	 * Specify the rowCount field 
	 *//*
	private int rowCount;
	
	*//**
	 * Specify the columnCount field 
	 *//*
	private int columnCount;
	
	*//**
	 * Specify the tableModelMap field 
	 *//*
	private Map tableModelMap = null;
*/	
	/**
	 * @see edu.wustl.catissuecore.appletui.applet.BaseApplet#doInit()
	 */
	protected void doInit() {
		super.doInit();
		String enterSpecimenBy = "Label";
		
		int rowCount = 3;
		int columnCount = 3;
		Map tableModelMap = getTableData();
		
		/*		for (int i=0; i < rowCount ; i++) {
		  for (int j=0;j < columnCount; j++) {
			for(int k=0; k < AppletConstants.ARRAY_CONTENT_ATTRIBUTE_NAMES.length; k++) {
				tableModelMap.put(SpecimenArrayUtil.getArrayMapKey(i,j,columnCount,k),"");
			}
		  }
		}
*/
		JLabel concLabel = new JLabel("Concentration");
		concLabel.setOpaque(false);
		JLabel quantityLabel = new JLabel("Quantity");
		quantityLabel.setOpaque(false);
		concentrationTextField = new JTextField();
		concentrationTextField.setName("concentrationTextField");
		Dimension concDimension = new Dimension(100,concentrationTextField.getPreferredSize().height);
		concentrationTextField.setPreferredSize(concDimension);
		JPanel concPanel = new JPanel();
		concPanel.setOpaque(false);
		concPanel.setLayout(new FlowLayout(FlowLayout.LEFT,5,0));
		concPanel.add(concLabel);
		concPanel.add(concentrationTextField);
		
		quantityTextField = new JTextField();
		quantityTextField.setName("quantityTextField");
		Dimension quantityDimension = new Dimension(100,quantityTextField.getPreferredSize().height);
		quantityTextField.setPreferredSize(quantityDimension);
		System.out.println(quantityTextField.getPreferredSize());
		JPanel quantityPanel = new JPanel();
		quantityPanel.setOpaque(false);
		quantityPanel.setLayout(new FlowLayout(FlowLayout.LEFT,5,0));
		quantityPanel.add(quantityLabel);
		quantityPanel.add(quantityTextField);
		
		applyButton = new JButton("Apply");
		applyButton.setEnabled(false);
		
		JPanel applyPanel = new JPanel();
		applyPanel.setOpaque(false);
		applyPanel.setLayout(new FlowLayout(FlowLayout.LEFT,10,0));
		applyPanel.add(concPanel);
		applyPanel.add(quantityPanel);
		applyPanel.add(applyButton);
		
		//tableModelMap.put(AppletConstants.ARRAY_GRID_COMPONENT_KEY,gridContentList);
		TableModel tableModel = new SpecimenArrayTableModel(tableModelMap,rowCount,columnCount,enterSpecimenBy);
		//tableModel.addTableModelListener(new SpecimenArrayTableModelListener());
		SpecimenArrayTable arrayTable = new SpecimenArrayTable(tableModel);
		
/*		arrayTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                System.out.println(" aaa " + arrayTable.getSelectedColumns().length + arrayTable.getSelectedRows().length);
            }
        });
*/        
		arrayTable.getColumnModel().setColumnSelectionAllowed(true);
		arrayTable.setCellSelectionEnabled(true);
		arrayTable.setRowSelectionAllowed(true);
		arrayTable.getColumnModel().getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane scrollPane = new JScrollPane(arrayTable);
        this.getContentPane().add(applyPanel,BorderLayout.CENTER);
        this.getContentPane().add(scrollPane,BorderLayout.SOUTH);
	}
	
	/**
	 * @return table data map 
	 */
	private Map getTableData() {
		Map tableDataMap = null;
		String urlString = "http://localhost:8080/catissuecore/SpecimenArrayAppletAction.do";
		AppletModelInterface model = new BaseAppletModel();
		model.setData(new HashMap());
		
		try {
			model = (AppletModelInterface) AppletServerCommunicator.doAppletServerCommunication(urlString,model);
			tableDataMap = model.getData();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return tableDataMap;
	}

	/**
	 * @return Returns the concentrationTextField.
	 */
	public JTextField getConcentrationTextField() {
		return concentrationTextField;
	}

	/**
	 * @param concentrationTextField The concentrationTextField to set.
	 */
	public void setConcentrationTextField(JTextField concentrationTextField) {
		this.concentrationTextField = concentrationTextField;
	}

	/**
	 * @return Returns the quantityTextField.
	 */
	public JTextField getQuantityTextField() {
		return quantityTextField;
	}

	/**
	 * @param quantityTextField The quantityTextField to set.
	 */
	public void setQuantityTextField(JTextField quantityTextField) {
		this.quantityTextField = quantityTextField;
	}

	/**
	 * @return Returns the applyButton.
	 */
	public JButton getApplyButton() {
		return applyButton;
	}

	/**
	 * @param applyButton The applyButton to set.
	 */
	public void setApplyButton(JButton applyButton) {
		this.applyButton = applyButton;
	}
}
