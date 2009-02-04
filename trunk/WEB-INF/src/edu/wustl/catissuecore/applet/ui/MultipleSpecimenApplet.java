package edu.wustl.catissuecore.applet.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;

import edu.wustl.catissuecore.applet.AppletConstants;
import edu.wustl.catissuecore.applet.AppletServerCommunicator;
import edu.wustl.catissuecore.applet.component.MultipleSpecimenTable;
import edu.wustl.catissuecore.applet.listener.AddColumnHandler;
import edu.wustl.catissuecore.applet.listener.DeleteLastHandler;
import edu.wustl.catissuecore.applet.listener.MultipleSpecimenCopyActionHandler;
import edu.wustl.catissuecore.applet.listener.MultipleSpecimenPasteActionHandler;
import edu.wustl.catissuecore.applet.listener.MultipleSpecimenTableKeyHandler;
import edu.wustl.catissuecore.applet.listener.PageLinkHandler;
import edu.wustl.catissuecore.applet.listener.SpecimenSubmitButtonHandler;
import edu.wustl.catissuecore.applet.model.BaseAppletModel;
import edu.wustl.catissuecore.applet.model.MultipleSpecimenTableModel;
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
	private MultipleSpecimenTable table;
	private JPanel buttonPanel;
	private JPanel linkPanel;
	
	private int totalPages=0;
	private final int WIDTH=1000;
	
	Color appletColor;
	public void doInit()
    {
		int columnNumber = Integer.parseInt(this.getParameter("noOfSpecimen"));		
		MultipleSpecimenTableModel model = new MultipleSpecimenTableModel(columnNumber,getInitDataMap());
				
		table = new MultipleSpecimenTable(model);
        table.getTableHeader().setReorderingAllowed(false);
        
        //****** Remove columnHeaders
        table.setTableHeader(null );

		//to set the focus to the editor. Mandar: 16Oct06.
        	//not in jdk1.3 so commented.
        //table.setSurrendersFocusOnKeystroke(true );

        table.addKeyListener(new MultipleSpecimenTableKeyHandler(table));

		// Creating Layout
		//getContentPane().setLayout(new FlowLayout(FlowLayout.LEFT ));
		this.getContentPane().setLayout(new VerticalLayout(0,0));
		
		buttonPanel = new JPanel( new FlowLayout(FlowLayout.LEFT ) );
		linkPanel = new JPanel( new FlowLayout(FlowLayout.LEFT ) );
		
		createButtonPanel(buttonPanel);
		createLinkPanel(linkPanel);

		//--- gbl
	    System.out.println("Applet size :- W : "+getWidth()+ " ,H : "+getHeight() );
	    getContentPane().add(buttonPanel);
	    getContentPane().add(linkPanel);
		// --------------------

    //    table.getModel().addTableModelListener(new TableModelChangeHandler(table));
			
		//table.getColumnModel().setColumnSelectionAllowed(true);
		table.setColumnSelectionAllowed(true);
		table.setCellSelectionEnabled(true );
//		20Oct06<*>		table.setFocusTraversalKeysEnabled(true );
		//table.setRowHeight(3,50);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	
        //set current page to 1
        model.setCurrentPageIndex(1);
        //  Create custom columns
		for(int cnt = 0; cnt < model.getColumnCount()  ; cnt++)
		{
			new SpecimenColumnModel(table, cnt);
		}
		table.setAutoCreateColumnsFromModel(false);
//		JScrollPane scrollPane = new JScrollPane( table );
		JScrollPane scrollPane= new FixedColumnScrollPane(table);
		System.out.println("Table Size : "+table.getWidth()+","+table.getHeight());
		table.setSize(WIDTH,getHeight());
		Dimension dim = new Dimension(WIDTH,getHeight()-65) ;
//		table.setPreferredSize( dim);
//		table.setPreferredScrollableViewportSize(new Dimension(WIDTH,getHeight()));
		System.out.println("Table Size after set : "+table.getWidth()+","+table.getHeight());
//		scrollPane.setSize(table.getWidth(),table.getHeight());
		scrollPane.setPreferredSize(dim );
		System.out.println("\nscrollPane.getPreferredSize(): "+ scrollPane.getPreferredSize());
		scrollPane.setSize(dim );   
		
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER  );  
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS); 

		getContentPane().add(scrollPane);
		setBackground(appletColor);
		//not in jdk1.3
//		getContentPane().setFocusable(true );
		this.requestFocus(); 
		table.requestDefaultFocus(); 
		
    }
    
    private void createButtonPanel(JPanel panel)
    {
    	JButton copy = new JButton(AppletConstants.MULTIPLE_SPECIMEN_COPY  );
    	JButton paste = new JButton(AppletConstants.MULTIPLE_SPECIMEN_PASTE);
    	copy.setMnemonic(AppletConstants.MULTIPLE_SPECIMEN_COPY_ACCESSKEY);
    	paste.setMnemonic(AppletConstants.MULTIPLE_SPECIMEN_PASTE_ACCESSKEY);
    	//copy.setEnabled(false);
    	paste.setEnabled(false);
    	copy.addActionListener(new MultipleSpecimenCopyActionHandler(table, paste));
    	paste.addActionListener(new MultipleSpecimenPasteActionHandler(table));
    	
    	JButton addSpecimen = new JButton(AppletConstants.MULTIPLE_SPECIMEN_ADD_SPECIMEN);
    	addSpecimen.addActionListener(new AddColumnHandler(table,this) );
    	
    	//deleteLast button
    	JButton deleteLast = new JButton(AppletConstants.MULTIPLE_SPECIMEN_DELETE_LAST);
    	deleteLast.setMnemonic(AppletConstants.MULTIPLE_SPECIMEN_DELETE_LAST_ACCESSKEY);
    	deleteLast.addActionListener(new DeleteLastHandler(table,this) );

    	
    	JLabel placeHolder = new JLabel("     ");
    	panel.add(copy);panel.add(placeHolder );panel.add(paste );
    	panel.add(placeHolder );panel.add(addSpecimen);
    	panel.add(placeHolder );panel.add(deleteLast);
    	int usedWidth = copy.getPreferredSize().width+ paste.getPreferredSize().width + deleteLast.getPreferredSize().width + (3 * placeHolder.getPreferredSize().width) ;
    	usedWidth = usedWidth + placeHolder.getPreferredSize().width + addSpecimen.getPreferredSize().width ;  
    	
    	//Temporary added till adjusting height
    	JPanel submitButtonPanel = new JPanel( new FlowLayout(FlowLayout.RIGHT  ) );
    	JButton submit = new JButton(AppletConstants.MULTIPLE_SPECIMEN_SUBMIT);
    	submit.addActionListener(new SpecimenSubmitButtonHandler(table));
    	placeHolder = new JLabel("                         ");
    	int submitButtonPanelWidth = getWidth()-usedWidth-20;
    	System.out.println("submitButtonPanelWidth : "+ submitButtonPanelWidth + " usedWidth: "+usedWidth);
    	submitButtonPanel.setPreferredSize(new Dimension(submitButtonPanelWidth,(int) submit.getPreferredSize().height+5  ) );
    	submitButtonPanel.add(placeHolder );submitButtonPanel.add(submit );panel.add(submitButtonPanel );
    	appletColor = panel.getBackground(); 
    }
    
    private JButton createLinkPanel(JPanel panel)
    {
    	MultipleSpecimenTableModel tableModel = (MultipleSpecimenTableModel) table.getModel();
    	JButton link1 = null;
    	if(tableModel.getTotalPageCount()>1)
    	{
       	int startIndex = 1;
        	int endIndex = tableModel.getColumnsPerPage()  ;
        	//JButton link1 = null;
        	if(tableModel.getTotalPageCount()>10)
        		panel.setLayout(new GridLayout(2,(int)(tableModel.getTotalPageCount()/2),0,0));
        	for(int pageNo = 1; pageNo<=tableModel.getTotalPageCount();pageNo++  )
        	{
        		link1 = new JButton(String.valueOf(startIndex )+ " - "+String.valueOf(endIndex ));
        		link1.setActionCommand(String.valueOf(pageNo ));
             	LineBorder border =(LineBorder) BorderFactory.createLineBorder(getBackground() );
             	link1.setToolTipText(String.valueOf(startIndex )+ " - "+String.valueOf(endIndex ));
             	link1.setBorder(border );
             	link1.setPreferredSize(new Dimension(AppletConstants.LINK_BUTTON_WIDTH,(int)link1.getPreferredSize().getHeight()) );
             	link1.addActionListener(new PageLinkHandler(table));
             	panel.add(link1);//panel.add(new JLabel("    ") );
             	startIndex =startIndex +tableModel.getColumnsPerPage()  ;
             	endIndex = endIndex +tableModel.getColumnsPerPage()  ;
             	totalPages= pageNo;
        	}
        	
        	SwingUtilities.updateComponentTreeUI(linkPanel);
    	}
        	return link1;
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
	/**
	 * This method is called from Javascript toset the value of the selected location in the applet.
	 * @param specimenMapKey Identifier of the specimen.
	 * @param storageId Identifier of the storageContainer.
	 * @param storageType Label of storage type.
	 * @param xPos Position (x-axis) in container.
	 * @param yPos Position (y-axis) in container.
	 */
	public void setStorageDetails(String specimenMapKey, String storageId,String storageType,String xPos,String yPos) {
		MultipleSpecimenTableModel tableModel = (MultipleSpecimenTableModel) table.getModel();
		System.out.println("setStorageDetails :-specimenMapKey "+ specimenMapKey+" | storageId " + storageId+ " | storageType " + storageType+ " | xPos " + xPos+ " | yPos " + yPos);
		int actualColNo =  Integer.parseInt(specimenMapKey);
		int displayColNo = tableModel.getDisplayColumnNo(actualColNo); 
		System.out.println("table.getColumnModel().getColumnCount() : " + table.getColumnModel().getColumnCount()); 
		System.out.println("In applets storage details : display col no" + displayColNo + "actual col no" + actualColNo);
		SpecimenColumnModel columnModel = (SpecimenColumnModel) table.getColumnModel().getColumn(actualColNo).getCellRenderer();
//		SpecimenColumnModel columnModel = (SpecimenColumnModel) table.getColumnModel().getColumn(displayColNo).getCellRenderer();		
		tableModel.setStorageDetails(specimenMapKey, storageId, storageType, xPos, yPos);
		//String storageValue = (String) tableModel.getValueAt(AppletConstants.SPECIMEN_STORAGE_LOCATION_ROW_NO,displayColNo);
	//	columnModel.setLocation(storageValue);
		String storageValue = null;
		columnModel.setLocationFromJS(storageValue);
// Mandar : 4oct06 : testing setting of storage location data in model.
		System.out.println("Column : "+ displayColNo);
	}
	
	/**
	 * This method is called from Javascript toset the value of the selected location in the applet.
	 * @param specimenMapKey Identifier of the specimen.
	 * @param storageId Identifier of the storageContainer.
	 * @param storageType Label of storage type.
	 * @param xPos Position (x-axis) in container.
	 * @param yPos Position (y-axis) in container.
	 */
	public void setButtonCaption(String specimenMapKey) {
		MultipleSpecimenTableModel tableModel = (MultipleSpecimenTableModel) table.getModel();
		String columnString = specimenMapKey.substring(AppletConstants.SPECIMEN_PREFIX.length(),specimenMapKey.indexOf("_"));
		int actualColNo =  Integer.parseInt(columnString)-1;
		int displayColNo = tableModel.getDisplayColumnNo(actualColNo); 
	//	tableModel.setCaptionInMap(specimenMapKey,buttonType);
			
		SpecimenColumnModel columnModel =  (SpecimenColumnModel)table.getColumnModel().getColumn(displayColNo).getCellRenderer();
		columnModel.setEditCaptionFromJS(specimenMapKey,AppletConstants.EDIT,tableModel);

	}
	
	/**
	 * This method will be called when the AddSpecimen button is clicked. 
	 * It will update the applet UI to display the column or the page buttons.
	 *
	 */
	public void updateOnAddSpecimen()
	{
		System.out.println("Inside updateOnAddSpecimen()");
				MultipleSpecimenTableModel tableModel = (MultipleSpecimenTableModel) table.getModel();
				if(totalPages< tableModel.getTotalPageCount()  )
				{
					totalPages++;
					linkPanel.removeAll();  
					SwingUtilities.updateComponentTreeUI(linkPanel);
					createLinkPanel(linkPanel );
					SwingUtilities.updateComponentTreeUI(linkPanel);
					Runnable panelUpdated = new Runnable() 
					{
					     public void run()
						 {
					     	SwingUtilities.updateComponentTreeUI(linkPanel);
					     	System.out.println("Applet link panel refreshed");
						 }
					 };
					 SwingUtilities.invokeLater(panelUpdated);
				}
	}
	/**
	 * This method is called to set the TissueSite value selected from the TissueSiteTreeApplet.
	 *  
	 * @param specimenColumn Column to update the TissueSite
	 * @param value Value to set for the TissueSite.
	 */
	public void setTissueSiteFromTreeMap(String specimenColumn, String value)
	{
		System.out.println("\n\n*******************     setTissueSiteFromTreeMap   ****************************\n\n");
		MultipleSpecimenTableModel tableModel = (MultipleSpecimenTableModel) table.getModel();
		System.out.println("setTissueSiteFromTreeMap :-specimenColumn "+ specimenColumn);
		int actualColNo =  Integer.parseInt(specimenColumn);
		int displayColNo = tableModel.getDisplayColumnNo(actualColNo); 
		System.out.println("In applets TissueSiteFromTreeMap : display col no" + displayColNo + "actual col no" + actualColNo);
		SpecimenColumnModel columnModel = (SpecimenColumnModel) table.getColumnModel().getColumn(actualColNo).getCellRenderer();
		columnModel.updateComponentValue(AppletConstants.SPECIMEN_TISSUE_SITE_ROW_NO,value);
		System.out.println("\n\n*******************     setTissueSiteFromTreeMap   ****************************\n\n");
	}

	// ------------------------- For Delete Last Column
	/**
	 * This method updates the applet and Page link panel when the delete last button is clicked.
	 */
	public void updateOnDeleteLastSpecimen()
	{
		System.out.println("Inside updateOnDeleteLastSpecimen()");
				MultipleSpecimenTableModel tableModel = (MultipleSpecimenTableModel) table.getModel();
				
				//jiitendra
				System.out.println("totalPages-----"+totalPages + "tableModel.getTotalPageCount()--"+tableModel.getTotalPageCount());
				if(totalPages > tableModel.getTotalPageCount()  )
				{
					totalPages--;
					linkPanel.removeAll();  
					SwingUtilities.updateComponentTreeUI(linkPanel);
					JButton link1 = createLinkPanel(linkPanel);
					
					if (link1 == null)
					{
						link1 = new JButton();
						link1.setActionCommand("1");
						link1.addActionListener(new PageLinkHandler(table));
					}
//					ActionListener[] actionListeners = link1.getActionListeners();
//		        	for compilation in jdk1.3
			        	ActionListener[] actionListeners = (ActionListener[])link1.getListeners(ActionListener.class);
			        	for (int i = 0; i < actionListeners.length; i++) 
			        	{
				        	ActionListener actionListener = actionListeners[i];
				            if (actionListener instanceof PageLinkHandler) {
				            	actionListener.actionPerformed(new ActionEvent(link1,
				                        ActionEvent.ACTION_FIRST,"action"));
				                break;
				            }
				        }
					SwingUtilities.updateComponentTreeUI(linkPanel);
				}
	}

}
