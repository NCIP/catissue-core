import java.applet.Applet;
import java.applet.AppletContext;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import netscape.javascript.JSObject;
import edu.wustl.catissuecore.tissuesite.TissueSiteTreeNode;
import edu.wustl.catissuecore.util.global.Constants;


/**
 * @author gautam_shetty
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TissueSiteTreeNodeListener implements TreeSelectionListener
{
    /**
     * Corresponds to an applet environment.
     */
    private AppletContext appletContext = null;
    
    /**
     * @return Returns the appletContext.
     */
    public AppletContext getAppletContext()
    {
        return appletContext;
    }
    
    /**
     * @param appletContext The appletContext to set.
     */
    public void setAppletContext(AppletContext appletContext)
    {
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
            TissueSiteTreeNode  treeNode = (TissueSiteTreeNode) node
                    .getUserObject();
            
            //Set the values in the parent window.
            Applet applet = this.appletContext.getApplet(Constants.TREE_APPLET_NAME);
            JSObject window = JSObject.getWindow(applet);
            String propertyName = applet.getParameter(Constants.PROPERTY_NAME);
            String setValue = "setParentWindowValue('"+propertyName+"','"+treeNode.toString()+"')";
            window.eval(setValue);
            window.eval("closeWindow()");
        }
    }

}
