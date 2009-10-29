/*
 * <p>Title: SpecimenArrayApplet.java</p>
 * <p>Description:	This class initializes the fields of SpecimenArrayApplet.java</p>
 * Copyright: Copyright (c) year 2006
 * Company: Washington University, School of Medicine, St. Louis.
 * @version 1.1
 * Created on Sep 18, 2006
 */
/**
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
import javax.swing.ScrollPaneConstants;
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

public class SpecimenArrayApplet extends BaseApplet
{

	/**
	 * logger.
	 */
	private transient final Logger logger = Logger.getCommonLogger(SpecimenArrayApplet.class);
	/**
	 * Default Serial Version ID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Specify the quantityTextField field.
	 */
	private JTextField quantityTextField = null;

	/**
	 * Specify the concentrationTextField field.
	 */
	private JTextField concentrationTextField = null;

	/**
	 * Specify the applyButton field.
	 */
	private JButton applyButton = null;

	/**
	 * Specify the copyButton field.
	 */
	JButton copyButton = null;

	/**
	 * Specify the pasteButton field.
	 */
	JButton pasteButton = null;
	/**
	 * arrayTable.
	 */
	private SpecimenArrayTable arrayTable = null;

	/**
	 * Specify the session_id field.
	 */
	String session_id = null;

	/**
	 * Specify the enterSpecimenBy field.
	 */
	String enterSpecimenBy = null;

	/**
	 * @see edu.wustl.catissuecore.appletui.applet.BaseApplet#doInit()
	 */
	@Override
	protected void doInit()
	{
		super.doInit();

		/*
				int rowCount = 3;
				int columnCount = 3;
				String specimenClass = "Molecular";
				Map tableModelMap = new HashMap();
				String enterSpecimenBy = "Label";
		*/
		this.session_id = this.getParameter("session_id");

		final int rowCount = new Integer(this.getParameter("rowCount")).intValue();
		final int columnCount = new Integer(this.getParameter("columnCount")).intValue();
		final String specimenClass = this.getParameter("specimenClass");
		this.enterSpecimenBy = this.getParameter("enterSpecimenBy");
		final Map tableModelMap = this.getTableModelData();

		final JLabel concLabel = new JLabel("Concentration");
		concLabel.setOpaque(false);
		final JLabel quantityLabel = new JLabel("Quantity");
		quantityLabel.setOpaque(false);
		this.concentrationTextField = new JTextField();
		this.concentrationTextField.setName("concentrationTextField");
		this.concentrationTextField.setEnabled(false);
		final Dimension concDimension = new Dimension(100, this.concentrationTextField
				.getPreferredSize().height);
		this.concentrationTextField.setPreferredSize(concDimension);
		final JPanel concPanel = new JPanel();
		concPanel.setOpaque(false);
		concPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
		concPanel.add(concLabel);
		concPanel.add(this.concentrationTextField);

		this.quantityTextField = new JTextField();
		this.quantityTextField.setName("quantityTextField");
		this.quantityTextField.setEnabled(false);
		final Dimension quantityDimension = new Dimension(100, this.quantityTextField
				.getPreferredSize().height);
		this.quantityTextField.setPreferredSize(quantityDimension);
		//System.out.println(quantityTextField.getPreferredSize());
		final JPanel quantityPanel = new JPanel();
		quantityPanel.setOpaque(false);
		quantityPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
		quantityPanel.add(quantityLabel);
		quantityPanel.add(this.quantityTextField);

		this.applyButton = new JButton("Apply");
		this.applyButton.setEnabled(false);

		final JPanel applyPanel = new JPanel();
		applyPanel.setBackground(AppletConstants.BG_COLOR);
		//applyPanel.setOpaque(false);
		applyPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0));
		applyPanel.add(concPanel);
		applyPanel.add(quantityPanel);
		applyPanel.add(this.applyButton);

		final TableModel tableModel = new SpecimenArrayTableModel(tableModelMap, rowCount,
				columnCount, this.enterSpecimenBy, specimenClass);
		this.arrayTable = new SpecimenArrayTable(tableModel);
		this.arrayTable.setOpaque(false);
		this.arrayTable.getColumnModel().setColumnSelectionAllowed(true);
		this.arrayTable.setCellSelectionEnabled(true);
		this.arrayTable.setRowSelectionAllowed(true);
		this.arrayTable.getColumnModel().getSelectionModel().setSelectionMode(
				ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		final JScrollPane scrollPane = new JScrollPane(this.arrayTable);
		scrollPane.setPreferredSize(new Dimension(100, 100));
		scrollPane.setOpaque(false);
		//scrollPane.setBackground(Color.WHITE);
		scrollPane.getViewport().setBackground(AppletConstants.BG_COLOR);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		this.applyButton.addActionListener(new ApplyButtonActionHandler(this.arrayTable));
		final JPopupMenu popupMenu = new JPopupMenu();
		final ArrayCopyOptionActionHandler actionHandler = new ArrayCopyOptionActionHandler(
				this.arrayTable);

		final JMenuItem labelMenuItem = new JMenuItem(AppletConstants.ARRAY_COPY_OPTION_LABELBAR);
		labelMenuItem.addActionListener(actionHandler);
		final JMenuItem quantityMenuItem = new JMenuItem(AppletConstants.ARRAY_COPY_OPTION_QUANTITY);
		quantityMenuItem.addActionListener(actionHandler);
		final JMenuItem concMenuItem = new JMenuItem(
				AppletConstants.ARRAY_COPY_OPTION_CONCENTRATION);
		concMenuItem.addActionListener(actionHandler);
		final JMenuItem allMenuItem = new JMenuItem(AppletConstants.ARRAY_COPY_OPTION_ALL);
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

		this.copyButton = new JButton("Copy");
		//copyButton.addActionListener(new SpecimenArrayCopyActionHandler(popupMenu));
		this.copyButton.addMouseListener(new SpecimenArrayCopyMouseHandler(popupMenu));
		applyPanel.add(this.copyButton);
		this.pasteButton = new JButton("Paste");
		this.pasteButton.addActionListener(new SpecimenArrayPasteActionHandler(this.arrayTable));
		applyPanel.add(this.pasteButton);
		this.copyButton.setEnabled(false);
		this.pasteButton.setEnabled(false);
		//System.out.println(" decrease gap :: 10");
		this.getContentPane().setLayout(new VerticalLayout(0, 10));
		this.getContentPane().add(applyPanel);
		this.getContentPane().add(scrollPane);
		this.getContentPane().setBackground(AppletConstants.BG_COLOR);
	}

	/**
	 * Gets table model data.
	 * @return table data map.
	 */
	private Map getTableModelData()
	{
		Map tableDataMap = null;
		final String urlString = this.serverURL + AppletConstants.SPECIMEN_ARRAY_APPLET_ACTION
				+ ";jsessionid=" + this.session_id + "?" + AppletConstants.APPLET_ACTION_PARAM_NAME
				+ "=getArrayData";
		AppletModelInterface model = new BaseAppletModel();
		model.setData(new HashMap());

		try
		{
			model = AppletServerCommunicator.doAppletServerCommunication(urlString, model);
			tableDataMap = model.getData();
			//System.out.println(" getTableModelData()   tableDataMap :: " + tableDataMap);
		}
		catch (final IOException e)
		{
			this.logger.error(e.getMessage(), e);
			e.printStackTrace();
		}
		catch (final ClassNotFoundException e)
		{
			this.logger.error(e.getMessage(), e);
			e.printStackTrace();
		}
		return tableDataMap;
	}

	/**
	 * update session data.
	 */
	public void updateSessionData()
	{
		// set last cell data
		this.setLastCellData();
		final String urlString = this.serverURL + AppletConstants.SPECIMEN_ARRAY_APPLET_ACTION
				+ ";jsessionid=" + this.session_id + "?" + AppletConstants.APPLET_ACTION_PARAM_NAME
				+ "=updateSessionData";
		AppletModelInterface model = new BaseAppletModel();
		final Map arrayContentDataMap = ((SpecimenArrayTableModel) this.arrayTable.getModel())
				.getSpecimenArrayModelMap();
		model.setData(arrayContentDataMap);
		try
		{
			model = AppletServerCommunicator.doAppletServerCommunication(urlString, model);
			//arrayContentDataMap = model.getData();
		}
		catch (final IOException e)
		{
			this.logger.error(e.getMessage(), e);
			e.printStackTrace();
		}
		catch (final ClassNotFoundException e)
		{
			this.logger.error(e.getMessage(), e);
			e.printStackTrace();
		}
	}

	/**
	 * Set Last Cell Data.
	 */
	private void setLastCellData()
	{
		try
		{
			System.out.println(" start setLastCellData() method");
			final int selectedRow = this.arrayTable.getSelectedRow();
			final int selectedColumn = this.arrayTable.getSelectedColumn();
			/*
			System.out.println("  model map:: " + ((SpecimenArrayTableModel)
			 arrayTable.getModel()).getSpecimenArrayModelMap());
			System.out.println(" selectedRow " + selectedRow);
			System.out.println(" selectedColumn " + selectedColumn);
			*/
			if ((selectedRow > -1) && (selectedColumn > -1)
					&& (selectedRow <= this.arrayTable.getRowCount())
					&& (selectedColumn <= this.arrayTable.getColumnCount()))
			{
				this.arrayTable.getCellEditor(selectedRow, selectedColumn).stopCellEditing();
			}
		}
		catch (final Exception exception)
		{
			this.logger.error(exception.getMessage(), exception);
			System.out.println(" Exception occurred in setLastCellData() method"
					+ exception.getMessage());
		}
		System.out.println(" end setLastCellData() method");
		//System.out.println("  after stopping model map:: " +
		//((SpecimenArrayTableModel) arrayTable.getModel()).getSpecimenArrayModelMap());
	}

	/**
	 * @param enterSpecimenBy enter specimen by
	 */
	public void changeEnterSpecimenBy(String enterSpecimenBy)
	{
		this.logger.info(" Enter Specimen By " + enterSpecimenBy);
		System.out.println(" Enter Specimen By " + enterSpecimenBy);
		this.enterSpecimenBy = enterSpecimenBy;
		((SpecimenArrayTableModel) this.arrayTable.getModel())
				.changeEnterSpecimenBy(enterSpecimenBy);
	}

	/**
	 * @return Returns the concentrationTextField.
	 */
	public JTextField getConcentrationTextField()
	{
		return this.concentrationTextField;
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
		return this.quantityTextField;
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
		return this.applyButton;
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
		return this.copyButton;
	}

	/**
	 * @return Returns the pasteButton.
	 */
	public JButton getPasteButton()
	{
		return this.pasteButton;
	}
}
