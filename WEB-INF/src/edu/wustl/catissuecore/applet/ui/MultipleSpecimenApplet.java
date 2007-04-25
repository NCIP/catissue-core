package edu.wustl.catissuecore.applet.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
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
import edu.wustl.catissuecore.applet.util.CommonAppletUtil;

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
		final MultipleSpecimenTableModel model = new MultipleSpecimenTableModel(columnNumber,getInitDataMap());
				
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
		
		 InputMap im = table.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		 KeyStroke tab = KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0);
		 
	        //  Override the default tab behaviour
	        //  Tab across a column and not across columns
	 
	        /**
	         *  Following code is added for proper TAB ordering on multiple specimen applet -- Santosh
	         */
	        final Action oldTabAction1 = table.getActionMap().get(im.get(tab));
	        
	        Action tabAction1 = new AbstractAction() 
						        {
						        	int lastSelectedRow;
						        	int lastSelectedColumn;
						        	
						            public void actionPerformed(ActionEvent e)
						            {
						            	oldTabAction1.actionPerformed( e );
					 	                JTable table = (JTable)e.getSource();
						                int rowCount = table.getRowCount();
						                int columnCount = table.getColumnCount();
						                
						             /*   int row = model.getLastCellRow();
						                int column = model.getLastCellColumn();*/
						                
						                int row = table.getSelectedRow();
						                int column = table.getSelectedColumn();
						                
						                boolean flag = true; 
						                 
						                if(Math.abs(lastSelectedRow - row)>1)
						                {
						                	flag = false;
						                }
						                
						                
						                if (columnCount != 1)  
						                {
						                 if(lastSelectedColumn == columnCount-1 && flag)
						                 {						                	 
						                	column = columnCount-1;
						                 }
						                 else
						                 {
											    row += 1;
												column -= 1;
						
												if (row == rowCount) 
												{
													row = 0;
													column += 1;
												}
						                 }
						                }
						                
						               if(column==-1)
						               {
						            	   row-=1;
						            	   column = columnCount-1;
						               }
						           
						               table.changeSelection(row, column, false, false);
						               setLastSelectedRow(row);
						               setLastSelectedColumn(column);
						             
						            }

									/**
									 * @return Returns the lastSelectedColumn.
									 */
									public int getLastSelectedColumn() {
										return lastSelectedColumn;
									}

									/**
									 * @param lastSelectedColumn The lastSelectedColumn to set.
									 */
									public void setLastSelectedColumn(int lastSelectedColumn) {
										this.lastSelectedColumn = lastSelectedColumn;
									}

									/**
									 * @return Returns the lastSelectedRow.
									 */
									public int getLastSelectedRow() {
										return lastSelectedRow;
									}

									/**
									 * @param lastSelectedRow The lastSelectedRow to set.
									 */
									public void setLastSelectedRow(int lastSelectedRow) {
										this.lastSelectedRow = lastSelectedRow;
									}
						        };
	        table.getActionMap().put(im.get(tab), tabAction1); 
		
//		JScrollPane scrollPane = new JScrollPane( table );
		JScrollPane scrollPane= new FixedColumnScrollPane(table);
		table.setSize(WIDTH,getHeight());
		Dimension dim = new Dimension(WIDTH,getHeight()-65) ;
//		table.setPreferredSize( dim);
//		table.setPreferredScrollableViewportSize(new Dimension(WIDTH,getHeight()));

//		scrollPane.setSize(table.getWidth(),table.getHeight());
		scrollPane.setPreferredSize(dim );

		scrollPane.setSize(dim );   
		
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER  );  
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS); 

		getContentPane().add(scrollPane);
		setBackground(appletColor);
		//not in jdk1.3
//		getContentPane().setFocusable(true );
	/*	table.setFocusCycleRoot(true);
		table.setFocusable(true);
		table.setFocusTraversalPolicy(new MyOwnFocusTraversalPolicyForJTable(table));*/
		this.requestFocus(); 
		table.requestDefaultFocus(); 
		
    } 
    
    private void createButtonPanel(JPanel panel)
    { 
    	JButton copy = new JButton(AppletConstants.MULTIPLE_SPECIMEN_COPY  );
    	
    	final JButton temp = new JButton(" "); 
       	Border  empty = BorderFactory.createEmptyBorder();
        temp.setPreferredSize(new Dimension(20,20));    
    
       // BevelBorder bLevel = new BevelBorder(BevelBorder.LOWERED); 
       
        final Color c = this.getForeground();   
        temp.setBackground(c);
        temp.setForeground(c);
       // temp.setco(panel.getForeground());
        BevelBorder bLevel = new BevelBorder(BevelBorder.LOWERED,panel.getBackground(),panel.getBackground(),panel.getBackground(),panel.getBackground());
      //  bLevel.paintBorder(temp,new Graphics(),temp.getBounds().x,temp.getBounds().y,temp.getBounds().width,temp.getBounds().height);
        temp.setBorder(new CompoundBorder(empty,bLevel));  
        temp.setPreferredSize(new Dimension(20,20));   
          
        JButton paste = new JButton(AppletConstants.MULTIPLE_SPECIMEN_PASTE);
    	copy.setMnemonic(AppletConstants.MULTIPLE_SPECIMEN_COPY_ACCESSKEY);
    	paste.setMnemonic(AppletConstants.MULTIPLE_SPECIMEN_PASTE_ACCESSKEY);
    	//copy.setEnabled(false);
    	paste.setEnabled(false); 
    	copy.addActionListener(new MultipleSpecimenCopyActionHandler(table, paste));
    	paste.addActionListener(new MultipleSpecimenPasteActionHandler(table));
    	/**
    	 *  This button is purposely added to adjust the focus
    	 */
    //    temp.setPreferredSize(new Dimension(10,10));   
    	temp.addFocusListener(new FocusListener(){ 
    		public void focusGained(FocusEvent e) {
    			
    			Component temp = table.getComponentAt(table.getSelectedRow(),table.getSelectedColumn());
    			if (temp != null)
    			{
    			   temp.requestFocus();
    			}
			}

    		
			public void focusLost(FocusEvent e) {   
			}});
    	
    	
    	temp.setBackground(null);
    	temp.setForeground(null);
    	
    	temp.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {}
			public void mousePressed(MouseEvent e) {}
			public void mouseReleased(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {
				temp.setOpaque(true);
}
			public void mouseExited(MouseEvent e) {}
		});
    	
    	JButton addSpecimen = new JButton(AppletConstants.MULTIPLE_SPECIMEN_ADD_SPECIMEN);
    	addSpecimen.addActionListener(new AddColumnHandler(table,this) );
    	
    	//deleteLast button
    	JButton deleteLast = new JButton(AppletConstants.MULTIPLE_SPECIMEN_DELETE_LAST);
    	deleteLast.setMnemonic(AppletConstants.MULTIPLE_SPECIMEN_DELETE_LAST_ACCESSKEY);
    	deleteLast.addActionListener(new DeleteLastHandler(table,this) );

    	
    	JLabel placeHolder = new JLabel("     ");
		panel.add(temp);
		panel.add(copy);
		panel.add(placeHolder);
		panel.add(paste);
		panel.add(placeHolder);  
		panel.add(addSpecimen);
		panel.add(placeHolder);
		panel.add(deleteLast);
		panel.add(placeHolder);
		
		JCheckBox chk = new JCheckBox("Virtually Locate All The Specimens");
	    panel.add(chk);
		JButton submit = new JButton(AppletConstants.MULTIPLE_SPECIMEN_SUBMIT);
		submit.addActionListener(new SpecimenSubmitButtonHandler(table));
		placeHolder = new JLabel("                                     ");  
	//	panel.add(placeHolder);
		panel.add(submit);
		
		chk.addActionListener(new ActionListener() 
				{
		public void actionPerformed(ActionEvent e) {  
			MultipleSpecimenTableModel multipleSpecimenTableModel = CommonAppletUtil.getMultipleSpecimenTableModel(table);
			multipleSpecimenTableModel.setVirtuallyLocatedCheckBox(((JCheckBox)e.getSource()).isSelected());
			}
		}
		);
		
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
		
		int actualColNo =  Integer.parseInt(specimenMapKey);
		int displayColNo = tableModel.getDisplayColumnNo(actualColNo); 

		SpecimenColumnModel columnModel = (SpecimenColumnModel) table.getColumnModel().getColumn(actualColNo).getCellRenderer();
//		SpecimenColumnModel columnModel = (SpecimenColumnModel) table.getColumnModel().getColumn(displayColNo).getCellRenderer();		
		tableModel.setStorageDetails(specimenMapKey, storageId, storageType, xPos, yPos);
		//String storageValue = (String) tableModel.getValueAt(AppletConstants.SPECIMEN_STORAGE_LOCATION_ROW_NO,displayColNo);
	//	columnModel.setLocation(storageValue);
		String storageValue = null;
		columnModel.setLocationFromJS(storageValue);
// Mandar : 4oct06 : testing setting of storage location data in model.
	}
	
	/**
	 * This method is called from Javascript to set the caption and tooltip  of the button
	 * @param specimenMapKey Identifier of the specimen
	 * @param toolTip toolTip to be set to event button
	 */
	public void setButtonCaption(String specimenMapKey, String toolTip) 
	{
		MultipleSpecimenTableModel tableModel = (MultipleSpecimenTableModel) table.getModel();
		String columnString = specimenMapKey.substring(AppletConstants.SPECIMEN_PREFIX.length(),specimenMapKey.indexOf("_"));
		int actualColNo =  Integer.parseInt(columnString)-1;
		int displayColNo = tableModel.getDisplayColumnNo(actualColNo); 
	//	tableModel.setCaptionInMap(specimenMapKey,buttonType);
		
		/**
		* Patch ID: Entered_Events_Need_To_Be_Visible_3
		* See also: 1-5
		* Description: set the toolTipText for event button and update map in table model
		*/  
		SpecimenColumnModel columnModel =  (SpecimenColumnModel)table.getColumnModel().getColumn(displayColNo).getCellRenderer();
		columnModel.setToolTipToEventButton(toolTip);
		columnModel.setEventstToolTipText(toolTip);
		tableModel.setEventsToolTipText(toolTip, actualColNo+1);
		columnModel.setEditCaptionFromJS(specimenMapKey,AppletConstants.EDIT,tableModel);

	}
	
	/**
	 * This method will be called when the AddSpecimen button is clicked. 
	 * It will update the applet UI to display the column or the page buttons.
	 *
	 */
	public void updateOnAddSpecimen()
	{
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
		MultipleSpecimenTableModel tableModel = (MultipleSpecimenTableModel) table.getModel();
		int actualColNo =  Integer.parseInt(specimenColumn);
		int displayColNo = tableModel.getDisplayColumnNo(actualColNo); 
		
		SpecimenColumnModel columnModel = (SpecimenColumnModel) table.getColumnModel().getColumn(actualColNo).getCellRenderer();
		columnModel.updateComponentValue(AppletConstants.SPECIMEN_TISSUE_SITE_ROW_NO,value);
	}

	// ------------------------- For Delete Last Column
	/**
	 * This method updates the applet and Page link panel when the delete last button is clicked.
	 */
	public void updateOnDeleteLastSpecimen()
	{
				MultipleSpecimenTableModel tableModel = (MultipleSpecimenTableModel) table.getModel();
				
				//jiitendra
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
