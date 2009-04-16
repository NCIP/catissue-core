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

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableModel;

import edu.wustl.catissuecore.applet.AppletConstants;
import edu.wustl.catissuecore.applet.AppletServerCommunicator;
import edu.wustl.catissuecore.applet.component.SpecimenArrayTable;
import edu.wustl.catissuecore.applet.listener.ApplyButtonActionHandler;
import edu.wustl.catissuecore.applet.listener.ArrayCopyOptionActionHandler;
import edu.wustl.catissuecore.applet.listener.SpecimenArrayCopyMouseHandler;
import edu.wustl.catissuecore.applet.listener.SpecimenArrayPasteActionHandler;
import edu.wustl.catissuecore.applet.model.AppletModelInterface;
import edu.wustl.catissuecore.applet.model.BaseAppletModel;
import edu.wustl.catissuecore.applet.model.SpecimenArrayTableModel;
import edu.wustl.common.util.logger.Logger;

/**
 * <p>This class specifies the methods used to render specimen array applet.It is extending
 * BaseApplet class.
 * </p>
 * @author Ashwin Gupta
 * @version 1.1
 */

public class SpecimenArrayApplet extends BaseApplet {
	
	private transient Logger logger = Logger.getCommonLogger(SpecimenArrayApplet.class);
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
	
	/**
	 * Specify the copyButton field 
	 */
	JButton copyButton = null; 	
	
	/**
	 * Specify the pasteButton field 
	 */
	JButton pasteButton = null;
	
	
	private SpecimenArrayTable arrayTable = null;
	
	/**
	 * Specify the session_id field 
	 */
	String session_id = null;
	
	/**
	 * Specify the enterSpecimenBy field 
	 */
	String enterSpecimenBy = null;
	
	/**
	 * @see edu.wustl.catissuecore.appletui.applet.BaseApplet#doInit()
	 */
	protected void doInit() 
	{
		super.doInit();
//		
		
/*
		int rowCount = 3;
		int columnCount = 3;
		String specimenClass = "Molecular";
		Map tableModelMap = new HashMap();
		String enterSpecimenBy = "Label";
*/		
		session_id = getParameter("session_id");
		
		int rowCount = new Integer(getParameter("rowCount")).intValue();
		int columnCount = new Integer(getParameter("columnCount")).intValue();
		String specimenClass = getParameter("specimenClass");
		enterSpecimenBy = getParameter("enterSpecimenBy");
		Map tableModelMap = getTableModelData();
		
		JLabel concLabel = new JLabel("Concentration");
		concLabel.setOpaque(false);
		JLabel quantityLabel = new JLabel("Quantity");
		quantityLabel.setOpaque(false);
		concentrationTextField = new JTextField();
		concentrationTextField.setName("concentrationTextField");
		concentrationTextField.setEnabled(false);
		Dimension concDimension = new Dimension(100,concentrationTextField.getPreferredSize().height);
		concentrationTextField.setPreferredSize(concDimension);
		JPanel concPanel = new JPanel();
		concPanel.setOpaque(false);
		concPanel.setLayout(new FlowLayout(FlowLayout.LEFT,5,0));
		concPanel.add(concLabel);
		concPanel.add(concentrationTextField);
		
		quantityTextField = new JTextField();
		quantityTextField.setName("quantityTextField");
		quantityTextField.setEnabled(false);
		Dimension quantityDimension = new Dimension(100,quantityTextField.getPreferredSize().height);
		quantityTextField.setPreferredSize(quantityDimension);
		//System.out.println(quantityTextField.getPreferredSize());
		JPanel quantityPanel = new JPanel();
		quantityPanel.setOpaque(false);
		quantityPanel.setLayout(new FlowLayout(FlowLayout.LEFT,5,0));
		quantityPanel.add(quantityLabel);
		quantityPanel.add(quantityTextField);
		
		applyButton = new JButton("Apply");
		applyButton.setEnabled(false);
		
		JPanel applyPanel = new JPanel();
		applyPanel.setBackground(AppletConstants.BG_COLOR);
		//applyPanel.setOpaque(false);
		applyPanel.setLayout(new FlowLayout(FlowLayout.LEFT,10,0));
		applyPanel.add(concPanel);
		applyPanel.add(quantityPanel);
		applyPanel.add(applyButton);
		
		TableModel tableModel = new SpecimenArrayTableModel(tableModelMap,rowCount,columnCount,enterSpecimenBy,specimenClass);
		arrayTable = new SpecimenArrayTable(tableModel);
		arrayTable.setOpaque(false);
		arrayTable.getColumnModel().setColumnSelectionAllowed(true);
		arrayTable.setCellSelectionEnabled(true);
		arrayTable.setRowSelectionAllowed(true);
		arrayTable.getColumnModel().getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane scrollPane = new JScrollPane(arrayTable);
        scrollPane.setPreferredSize(new Dimension(100,100));
        scrollPane.setOpaque(false);
        //scrollPane.setBackground(Color.WHITE);
        scrollPane.getViewport().setBackground(AppletConstants.BG_COLOR);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        applyButton.addActionListener(new ApplyButtonActionHandler(arrayTable));
        JPopupMenu popupMenu = new JPopupMenu();
        ArrayCopyOptionActionHandler actionHandler = new ArrayCopyOptionActionHandler(arrayTable);
        
        JMenuItem labelMenuItem = new JMenuItem(AppletConstants.ARRAY_COPY_OPTION_LABELBAR);
        labelMenuItem.addActionListener(actionHandler);
        JMenuItem quantityMenuItem = new JMenuItem(AppletConstants.ARRAY_COPY_OPTION_QUANTITY);
        quantityMenuItem.addActionListener(actionHandler);
        JMenuItem concMenuItem = new JMenuItem(AppletConstants.ARRAY_COPY_OPTION_CONCENTRATION);
        concMenuItem.addActionListener(actionHandler);
        JMenuItem allMenuItem = new JMenuItem(AppletConstants.ARRAY_COPY_OPTION_ALL);
        allMenuItem.addActionListener(actionHandler);
        
        if (!specimenClass.equalsIgnoreCase("Molecular"))
        {
        	quantityMenuItem.setEnabled(false);
        	concMenuItem.setEnabled(false);
        }
        
        popupMenu.add(labelMenuItem);
        popupMenu.add(quantityMenuItem);
        popupMenu.add(concMenuItem);
        popupMenu.add(allMenuItem);
      
        copyButton = new JButton("Copy");
        //copyButton.addActionListener(new SpecimenArrayCopyActionHandler(popupMenu));
        copyButton.addMouseListener(new SpecimenArrayCopyMouseHandler(popupMenu));
        applyPanel.add(copyButton);
        pasteButton = new JButton("Paste");
        pasteButton.addActionListener(new SpecimenArrayPasteActionHandler(arrayTable));
        applyPanel.add(pasteButton);
        copyButton.setEnabled(false);
        pasteButton.setEnabled(false);
        //System.out.println(" decrease gap :: 10");
        this.getContentPane().setLayout(new VerticalLayout(0,10));
        this.getContentPane().add(applyPanel);
        this.getContentPane().add(scrollPane);
        this.getContentPane().setBackground(AppletConstants.BG_COLOR);
	}
	
	/**
	 * Gets table model data
	 * @return table data map 
	 */
	private Map getTableModelData() 
	{
		Map tableDataMap = null;
		String urlString = serverURL + AppletConstants.SPECIMEN_ARRAY_APPLET_ACTION + ";jsessionid="+session_id+"?" + AppletConstants.APPLET_ACTION_PARAM_NAME + "=getArrayData";
		AppletModelInterface model = new BaseAppletModel();
		model.setData(new HashMap());
		
		try {
			model = (AppletModelInterface) AppletServerCommunicator.doAppletServerCommunication(urlString,model);
			tableDataMap = model.getData();
			//System.out.println(" getTableModelData()   tableDataMap :: " + tableDataMap);
		} catch (IOException e) {
			logger.debug(e.getMessage(), e);
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			logger.debug(e.getMessage(), e);
			e.printStackTrace();
		}
		return tableDataMap;
	}
	
	/**
	 * update session data  
	 */
	public void updateSessionData() 
	{
		// set last cell data
		setLastCellData();
		String urlString = serverURL + AppletConstants.SPECIMEN_ARRAY_APPLET_ACTION + ";jsessionid="+session_id+"?" + AppletConstants.APPLET_ACTION_PARAM_NAME + "=updateSessionData";
		AppletModelInterface model = new BaseAppletModel();
		Map arrayContentDataMap = ((SpecimenArrayTableModel) arrayTable.getModel()).getSpecimenArrayModelMap();
		model.setData(arrayContentDataMap);
		try {
			model = (AppletModelInterface) AppletServerCommunicator.doAppletServerCommunication(urlString,model);
			//arrayContentDataMap = model.getData();
		} catch (IOException e) {
			logger.debug(e.getMessage(), e);
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			logger.debug(e.getMessage(), e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Set Last Cell Data 
	 */
	private void setLastCellData()
	{
		try
		{
			System.out.println(" start setLastCellData() method");
			int selectedRow = arrayTable.getSelectedRow();
			int selectedColumn = arrayTable.getSelectedColumn();
			/*
			System.out.println("  model map:: " + ((SpecimenArrayTableModel) arrayTable.getModel()).getSpecimenArrayModelMap());
			System.out.println(" selectedRow " + selectedRow);
			System.out.println(" selectedColumn " + selectedColumn);
			*/
			if ((selectedRow > -1) && (selectedColumn > -1) && (selectedRow <= arrayTable.getRowCount()) && (selectedColumn <= arrayTable.getColumnCount()))
			{
				arrayTable.getCellEditor(selectedRow,selectedColumn).stopCellEditing();
			}
		}
		catch(Exception exception)
		{
			logger.debug(exception.getMessage(), exception);
			System.out.println(" Exception occurred in setLastCellData() method" + exception.getMessage());
		}
		System.out.println(" end setLastCellData() method");
		//System.out.println("  after stopping model map:: " + ((SpecimenArrayTableModel) arrayTable.getModel()).getSpecimenArrayModelMap());
	}
	
	/**
	 * @param enterSpecimenBy enter specimen by
	 */
	public void changeEnterSpecimenBy(String enterSpecimenBy)
	{
		logger.info(" Enter Specimen By " + enterSpecimenBy);
		System.out.println(" Enter Specimen By " + enterSpecimenBy);
		this.enterSpecimenBy = enterSpecimenBy;
		((SpecimenArrayTableModel) arrayTable.getModel()).changeEnterSpecimenBy(enterSpecimenBy);
	}

	/**
	 * @return Returns the concentrationTextField.
	 */
	public JTextField getConcentrationTextField() 
	{
		return concentrationTextField;
	}

	/**
	 * @param concentrationTextField The concentrationTextField to set.
	 */
	public void setConcentrationTextField(JTextField concentrationTextField) 
	{
		this.concentrationTextField = concentrationTextField;
	}

	/**
	 * @return Returns the quantityTextField.
	 */
	public JTextField getQuantityTextField() 
	{
		return quantityTextField;
	}

	/**
	 * @param quantityTextField The quantityTextField to set.
	 */
	public void setQuantityTextField(JTextField quantityTextField) 
	{
		this.quantityTextField = quantityTextField;
	}

	/**
	 * @return Returns the applyButton.
	 */
	public JButton getApplyButton() 
	{
		return applyButton;
	}

	/**
	 * @param applyButton The applyButton to set.
	 */
	public void setApplyButton(JButton applyButton) 
	{
		this.applyButton = applyButton;
	}

	/**
	 * @return Returns the copyButton.
	 */
	public JButton getCopyButton()
	{
		return copyButton;
	}

	/**
	 * @return Returns the pasteButton.
	 */
	public JButton getPasteButton()
	{
		return pasteButton;
	}
}
