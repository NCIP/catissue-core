/**
 * <p>Title: QueryTree Class>
 * <p>Description:	QueryTree builds the applet for the tree representation 
 * of query result view.</p>
 * Copyright: Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 */

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JApplet;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTree;

import edu.wustl.catissuecore.storage.GenerateStorageTree;
import edu.wustl.catissuecore.util.global.Constants;

/**
 * QueryTree builds the applet for the tree representation 
 * of query result view.
 * @author gautam_shetty
 */
public class QueryTree extends JApplet
{

    /**
     * Initializes the applet.
     */
    public void init()
    {
        ObjectInputStream in = null;

        try
        {
            URL codeBase = getCodeBase();
            String protocol = codeBase.getProtocol();
            String host = codeBase.getHost();
            int port = codeBase.getPort();
            
            String pageOf = this.getParameter(Constants.PAGEOF);
            String storageContainerType = null;
            int treeType = Constants.TISSUE_SITE_TREE_ID;
            
            if (pageOf.equals(Constants.PAGEOF_STORAGE_LOCATION))
            {
                storageContainerType = this.getParameter(Constants.STORAGE_CONTAINER_TYPE);
                treeType = Constants.STORAGE_CONTAINER_TREE_ID;
            }
            
//            codeBase.
            String urlSuffix = Constants.TREE_DATA_ACTION+"?"+Constants.PAGEOF+"="+pageOf;

            URL dataURL = new URL(protocol, host, port, urlSuffix);
            
            //Establish connection with the TreeDataAction and get the JTree object. 
            URLConnection connection = dataURL.openConnection();
            connection.setUseCaches(false);
            
            in = new ObjectInputStream(connection.getInputStream());
            
            Vector treeDataVector = (Vector) in.readObject();

//            //Creates tree.
//            GenerateTree generateTree = new GenerateTree();
//            JTree tree = generateTree.createTreeView(dataList);
            
            GenerateStorageTree generateTree = new GenerateStorageTree();
            JTree tree = generateTree.createTree(treeDataVector, treeType);
            
            Container contentPane = getContentPane();
            contentPane.setLayout(new BorderLayout());
            
            if (pageOf.equals(Constants.PAGEOF_QUERY_RESULTS))
            {
                //Preparing radio buttons for configuring different views.
                JPanel radioButtonPanel = new JPanel(new GridLayout(2, 1));

                JRadioButton spreadsheetViewRadioButton = new JRadioButton(
                        Constants.SPREADSHEET_VIEW);
                spreadsheetViewRadioButton
                        .setActionCommand(Constants.SPREADSHEET_VIEW);
                spreadsheetViewRadioButton.setSelected(true);
                spreadsheetViewRadioButton.setPreferredSize(new Dimension(80, 40));

                JRadioButton individualViewRadioButton = new JRadioButton(
                        Constants.OBJECT_VIEW);
                individualViewRadioButton.setActionCommand(Constants.OBJECT_VIEW);
                individualViewRadioButton.setPreferredSize(new Dimension(80, 40));

                ButtonGroup radioButtonGroup = new ButtonGroup();
                radioButtonGroup.add(spreadsheetViewRadioButton);
                radioButtonGroup.add(individualViewRadioButton);

                radioButtonPanel.add(spreadsheetViewRadioButton);
                radioButtonPanel.add(individualViewRadioButton);
                //Radio buttons finish.
                
                //Put the radioButton panel on the Applet.
                contentPane.add(radioButtonPanel,BorderLayout.PAGE_START);
            }
            
            JPanel treePanel = new JPanel(new GridLayout(1, 0));
            JScrollPane scroll = new JScrollPane(tree);
            treePanel.add(scroll);
            treePanel.setOpaque(true);
            treePanel.setVisible(true);
            
            //Add listeners for the tree.
//            NodeSelectionListener nodeSelectionListener = new NodeSelectionListener(
//                    this.getCodeBase(), this.getAppletContext());
            StorageLocationViewListener viewListener 
            		= new StorageLocationViewListener(this.getCodeBase(), this.getAppletContext()); 
            viewListener.setStorageContainerType(storageContainerType);
            tree.addTreeSelectionListener(viewListener);

            //Add listeners for the radio buttons.
//            spreadsheetViewRadioButton.addActionListener(nodeSelectionListener);
//            individualViewRadioButton.addActionListener(nodeSelectionListener);
            
            //Put the tree panel on the Applet.
            contentPane.add(treePanel, BorderLayout.CENTER);
        }
        catch (MalformedURLException malExp)
        {
//            malExp.printStackTrace();
        }
        catch (IOException ioExp)
        {
//            ioExp.printStackTrace();
        }
        catch (ClassNotFoundException classNotExp)
        {
//            classNotExp.printStackTrace();
        }
        catch (Exception excp)
        {
//            excp.printStackTrace();
        }
        finally
        {
            try
            {
                in.close();
            }
            catch (IOException ioExp)
            {
//                ioExp.printStackTrace();
            }
        }
    }
}