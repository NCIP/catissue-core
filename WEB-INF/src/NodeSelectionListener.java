/**
 * <p>Title: NodeSelectionListener Class>
 * <p>Description: NodeSelectionListener handles the node selction event of the tree 
 * in the applet.</p>
 * Copyright: Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 */

import java.applet.AppletContext;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import edu.wustl.catissuecore.util.global.Constants;

/**
 * NodeSelectionListener handles the node selction event of the tree in the applet.
 * @author gautam_shetty
 */
public class NodeSelectionListener implements TreeSelectionListener, ActionListener
{
    /**
     * The URL of the host from which the applet is loaded.
     */
    private URL codeBase = null;
    
    /**
     * Corresponds to an applet environment.
     */
    private AppletContext appletContext = null;
    
    /**
     * Defines the type of view to be showed on tree node slection.
     */
    private String viewType = new String(Constants.SPREADSHEET_VIEW);
    
    /**
     * Status whether a node is selected or not.
     */
    private boolean nodeSelectionStatus = false;
    
    /**
     * Name of selected node.
     */
    private String nodeName = null;
    
    /**
     * Initializes an empty NodeSelectionListener.
     */
    public NodeSelectionListener()
    {
    }
    
    /**
     * Creates and initializes a NodeSelectionListener with the codeBase and appletContext.
     * @param codeBase2
     * @param appletContext2
     */
    public NodeSelectionListener(URL codeBase, AppletContext appletContext)
    {
        this.codeBase = codeBase;
        this.appletContext = appletContext;
    }

    /**
     * Returns true if a node is selected, else returns false.
     * @return true if a node is selected, else returns false.
     */
    private boolean isNodeSelected()
    {
        return nodeSelectionStatus;
    }

    /**
     * Implements and overrides the valueChanged method in TreeSelectionListener.
     */
    public void valueChanged(TreeSelectionEvent e)
    {
        Object object = e.getSource();
        JTree t = null;
        if (object instanceof JTree)
        {
            t = (JTree) object;
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)
            							   t.getLastSelectedPathComponent();
            this.nodeName = (String) node.getUserObject();
            
            try
            {
                String urlSuffix = null;
                
                //If the node selected is Root and view is individual view, don't show anything.
                if (!(nodeName.equals(Constants.ROOT) && viewType.equals(Constants.OBJECT_VIEW)))
                {
                    this.nodeSelectionStatus = true;
                    showDataView();
                }
            }
            catch (MalformedURLException malExp)
            {
            }
        }
    }
    
    /**
     * Shows the data view according to the view type and node selected.
     * @throws MalformedURLException
     */
    private void showDataView() throws MalformedURLException
    {
    	String protocol = codeBase.getProtocol();
    	String host = codeBase.getHost();
        int port = codeBase.getPort();

        String urlSuffix = Constants.DATA_VIEW_ACTION + nodeName + 
        			   "&"+ Constants.VIEW_TYPE + "=" + viewType;
        
        URL dataURL = new URL(protocol,host,port,urlSuffix);
        appletContext.showDocument(dataURL,Constants.DATA_VIEW_FRAME);
    }

    /**
     * Action performed on selecting the radio buttons.
     */
    public void actionPerformed(ActionEvent e)
    {
        String actionCommand = e.getActionCommand();
        
        if (actionCommand.equals(Constants.SPREADSHEET_VIEW))
        {
            this.viewType = new String(Constants.SPREADSHEET_VIEW);
        }
        else
        {
            this.viewType = new String(Constants.OBJECT_VIEW);
        }
        
        if (isNodeSelected() && !(nodeName.equals(Constants.ROOT)))
        {
            try
            {
                showDataView();
            }
            catch (MalformedURLException malExp)
            {
                
            }
        }
    }
}
