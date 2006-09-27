package edu.wustl.catissuecore.applet.ui;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import edu.wustl.catissuecore.applet.AppletConstants;
import edu.wustl.catissuecore.applet.AppletServerCommunicator;
import edu.wustl.catissuecore.applet.component.BaseTable;
import edu.wustl.catissuecore.applet.listener.SpecimenSubmitButtonHandler;
import edu.wustl.catissuecore.applet.listener.TableModelChangeHandler;
import edu.wustl.catissuecore.applet.model.BaseAppletModel;
import edu.wustl.catissuecore.applet.model.MultipleSpecimenTableModel;
import edu.wustl.catissuecore.applet.model.RowHeaderColumnModel;
import edu.wustl.catissuecore.applet.model.SpecimenColumnModel;

/**
 * This applet displays main UI for multiple specimen page.
 * @author mandar_deshmukh
 */
public class MultipleSpecimenApplet extends BaseApplet {

	/**
	 * Default Serial Version ID
	 */
	private static final long serialVersionUID = 1L;
	private BaseTable table;
	
	public void doInit()
    {
		int columnNumber = Integer.parseInt(this.getParameter("noOfSpecimen"));
//		int columnNumber = 4;
		MultipleSpecimenTableModel model = new MultipleSpecimenTableModel(columnNumber,getInitDataMap());		

		table = new BaseTable(model)
        {
            public Class getColumnClass(int column)
            {
                return getValueAt(0, column).getClass();
            }
        };
		// Creating Layout
		getContentPane().setLayout(new FlowLayout());

		JPanel buttonPanel;
		JPanel linkPanel;
		JPanel tablePanel;
		JPanel footerPanel;
		JPanel outerPanel;
		
		buttonPanel = new JPanel();
		linkPanel = new JPanel();
		tablePanel = new JPanel();
		footerPanel = new JPanel();
		outerPanel = new JPanel();
		
		outerPanel.setLayout(new GridLayout(4,1));
		createButtonPanel(buttonPanel);
		createLinkPanel(linkPanel);
		createFooterPanel(footerPanel);
		
		//--gblayout
		GridBagLayout gbl = new GridBagLayout();
		outerPanel.setLayout(gbl);
	    
		// Place a component at cell location (1,1)
	    GridBagConstraints gbc = new GridBagConstraints();
	    gbc.gridx = 1;
	    gbc.gridy = 1;
	    gbc.fill = GridBagConstraints.NONE;
	    gbl.setConstraints(buttonPanel, gbc);
	    outerPanel.add(buttonPanel);

		// Place a component at cell location (2,1)
	    gbc = new GridBagConstraints();
	    gbc.gridx = 1;
	    gbc.gridy = 2;
	    gbc.fill = GridBagConstraints.NONE;
	    gbl.setConstraints(linkPanel, gbc);
	    outerPanel.add(linkPanel);

		// Place a component at cell location (3,1)
	    gbc = new GridBagConstraints();
	    gbc.gridx = 1;
	    gbc.gridy = 3;
	    gbc.fill = GridBagConstraints.NONE;
	    gbl.setConstraints(tablePanel, gbc);
	    outerPanel.add(tablePanel);

		// Place a component at cell location (4,1)
	    gbc = new GridBagConstraints();
	    gbc.gridx = 1;
	    gbc.gridy = 4;
	    gbc.fill = GridBagConstraints.NONE;
	    gbl.setConstraints(footerPanel, gbc);
	    outerPanel.add(footerPanel);


		//--- gbl
//		outerPanel.add(buttonPanel);
//		outerPanel.add(linkPanel);
//		outerPanel.add(tablePanel);
//		outerPanel.add(footerPanel);
		
	    getContentPane().add(outerPanel);
		// --------------------
	    

        table.getModel().addTableModelListener(new TableModelChangeHandler(table));
			
		//table.getColumnModel().setColumnSelectionAllowed(true);
		table.setColumnSelectionAllowed(true);
		//table.setRowHeight(3,50);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		 
		JScrollPane scrollPane = new JScrollPane( table );

//        getContentPane().add( scrollPane );
//		JButton showAll = new JButton("ShowData");
		
//		showAll.addActionListener(new BaseActionHandler(table));

//		getContentPane().add( showAll,BorderLayout.SOUTH );

        //  Create custom columns
		new RowHeaderColumnModel(table);		//	for zeroth column
		for(int cnt = 1; cnt < columnNumber; cnt++)
		{
			new SpecimenColumnModel(table, cnt);
		}

		tablePanel.add( scrollPane );
    }
 
    public static void main(String[] args)
    {
    	MultipleSpecimenApplet frame = new MultipleSpecimenApplet();
    }
    
    private void createButtonPanel(JPanel panel)
    {
    	JButton copy = new JButton("Copy");
    	JButton paste = new JButton("Paste");
    	JLabel placeHolder = new JLabel("     ");
    	panel.add(copy);panel.add(placeHolder );panel.add(paste );
    }
    
    private void createLinkPanel(JPanel panel)
    {
     	JLabel label1 = new JLabel("1 - 10");
    	JLabel label2 = new JLabel("11 - 20");
    	JLabel label3 = new JLabel("21 - 30");
    	panel.add(label1);panel.add(label2);panel.add(label3);
    }
    
    private void createFooterPanel(JPanel panel)
    {
    	JButton submit = new JButton("Submit");
    	submit.addActionListener(new SpecimenSubmitButtonHandler(table));
    	JLabel placeHolder = new JLabel("       ");
    	panel.add(placeHolder );panel.add(submit );
    }
	/**
	 * This method initialize data lists 
	 */
	private Map getInitDataMap()
	{
		BaseAppletModel appletModel = new BaseAppletModel();
		appletModel.setData(new HashMap());
		try
		{
			String url = getServerURL() + "/MultipleSpecimenAppletAction.do?method=initData";

			appletModel = (BaseAppletModel) AppletServerCommunicator.doAppletServerCommunication(
					url, appletModel);

			return appletModel.getData();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public void setStorageDetails(String specimenMapKey, String storageId,String storageType,String xPos,String yPos) {
		int colNo = Integer.parseInt(specimenMapKey);
		MultipleSpecimenTableModel tableModel = (MultipleSpecimenTableModel) table.getModel();
		SpecimenColumnModel columnModel = (SpecimenColumnModel) table.getColumnModel().getColumn(colNo).getCellRenderer();
		
		tableModel.setStorageDetails(specimenMapKey, storageId, storageType, xPos, yPos);
		String storageValue = (String) tableModel.getValueAt(AppletConstants.SPECIMEN_STORAGE_LOCATION_ROW_NO,colNo);
		columnModel.setLocation(storageValue);
		
	}
}
