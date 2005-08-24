/*
 * Created on Jul 29, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
import java.applet.AppletContext;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import edu.wustl.catissuecore.storage.StorageContainerTreeNode;
import edu.wustl.catissuecore.util.global.Constants;

/**
 * @author gautam_shetty
 */
public class StorageLocationViewListener implements TreeSelectionListener
{

    /**
     * The URL of the host from which the applet is loaded.
     */
    private URL codeBase = null;

    /**
     * Corresponds to an applet environment.
     */
    private AppletContext appletContext = null;

    private String storageContainerType = null;

    private String pageOf = null;

    /**
     * Initializes an empty NodeSelectionListener.
     */
    public StorageLocationViewListener()
    {
    }

    /**
     * @return Returns the storageContainerType.
     */
    public String getStorageContainerType()
    {
        return storageContainerType;
    }

    /**
     * @param storageContainerType The storageContainerType to set.
     */
    public void setStorageContainerType(String storageContainerType)
    {
        this.storageContainerType = storageContainerType;
    }

    /**
     * @return Returns the pageOf.
     */
    public String getPageOf()
    {
        return pageOf;
    }

    /**
     * @param pageOf The pageOf to set.
     */
    public void setPageOf(String pageOf)
    {
        this.pageOf = pageOf;
    }

    /**
     * Creates and initializes a NodeSelectionListener with the codeBase and appletContext.
     * @param codeBase2
     * @param appletContext2
     */
    public StorageLocationViewListener(URL codeBase, AppletContext appletContext)
    {
        this.codeBase = codeBase;
        this.appletContext = appletContext;
    }

    /**
     * (non-Javadoc)
     * @see javax.swing.event.TreeSelectionListener#valueChanged(javax.swing.event.TreeSelectionEvent)
     */
    public void valueChanged(TreeSelectionEvent event)
    {
        Object object = event.getSource();
        JTree tree = null;

        if (object instanceof JTree)
        {
            tree = (JTree) object;
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree
                    .getLastSelectedPathComponent();
            StorageContainerTreeNode treeNode = (StorageContainerTreeNode) node
                    .getUserObject();

            try
            {
                String urlSuffix = null;

                if ((!treeNode.getStorageContainerName().equals(
                        Constants.CATISSUE_CORE))
                        && (treeNode.getStorageContainerType() != null))
                {
                    String protocol = codeBase.getProtocol();
                    String host = codeBase.getHost();
                    int port = codeBase.getPort();

                    urlSuffix = Constants.SHOW_STORAGE_CONTAINER_GRID_VIEW_ACTION
                            + "?" + Constants.IDENTIFIER + "=" + treeNode.getStorageContainerIdentifier()
                            + "&" + Constants.STORAGE_CONTAINER_TYPE + "=" + this.getStorageContainerType()
                            + "&" + Constants.PAGEOF + "=" + this.pageOf;
                    
                    if (pageOf.equals(Constants.PAGEOF_SPECIMEN))
                    {
                    	urlSuffix = Constants.SHOW_STORAGE_CONTAINER_GRID_VIEW_ACTION
                        + "?" + Constants.IDENTIFIER + "=" + treeNode.getStorageContainerIdentifier()
                        + "&" + Constants.PAGEOF + "=" + this.pageOf;
                    }

                    URL dataURL = new URL(protocol, host, port, urlSuffix);
                    appletContext.showDocument(dataURL,
                            				   Constants.DATA_VIEW_FRAME);
                }
            }
            catch (MalformedURLException malExp)
            {
            }
        }
    }
}