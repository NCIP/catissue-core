/**
 * <p>Title: SpecimenTreeBizLogic Class>
 * <p>Description:	SpecimenTreeBizLogic contains the bizlogic required to display Specimen hierarchy in tree form for ordering system module.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Ramya Nagraj
 * @version 1.00
 */

package edu.wustl.catissuecore.bizlogic;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.tree.SpecimenTreeNode;
import edu.wustl.catissuecore.tree.TreeDataInterface;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.util.logger.Logger;

/**
 * SpecimenTreeBizLogic contains the bizlogic required to display Specimen hierarchy in tree form in the ordering system module.
 * @author ramya_nagraj
 */

public class SpecimenTreeBizLogic extends CatissueDefaultBizLogic implements TreeDataInterface
{

	private transient final Logger logger = Logger.getCommonLogger(SpecimenTreeBizLogic.class);
	/**
	 * Long containing the id of specimen/specimen Collection group Id.
	 */
	private Long id;

	private boolean specimenCollGroup;

	/**
	 * No-Args Constructor
	 */
	public SpecimenTreeBizLogic()
	{

	}

	/**
	 * Parametrized Constructor
	 * @param specimenList
	 */
	public SpecimenTreeBizLogic(Long id, boolean specimenCollGroup)
	{
		this.id = id;
		this.specimenCollGroup = specimenCollGroup;
	}

	/**
	 * This function returns the Specimen and its child nodes in vector form for given specimen.
	 * @param specimenId int
	 * @return Vector Vector of Specimens
	 */

	public Vector getTreeViewData()
	{
		Vector allNodes = new Vector();
		try
		{
			//Retrieve the Specimen instance for that particular id.
			final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
			final IBizLogic defaultBizLogic = factory.getBizLogic(-1);

			//If specimenId is sent (Incase of biospecimen in orderlist or in defined array)
			if (this.specimenCollGroup == false)
			{
				final Object object = defaultBizLogic.retrieve(Specimen.class.getName(), this.id);

				//The list consists of only one element that is:- specimen instance
				final Specimen specimenObj = (Specimen) object;
				final Collection childColl = (Collection) defaultBizLogic.retrieveAttribute(
						Specimen.class.getName(), this.id, "elements(childrenSpecimen)");
				specimenObj.setChildSpecimenCollection(childColl);
				allNodes = this.formTreeNode(specimenObj);
			}
			else
			//If specimenCollectiongroupId is sent(Incase of pathological case)
			{
				final Object object = defaultBizLogic.retrieve(SpecimenCollectionGroup.class
						.getName(), this.id);

				//The list consists of only one element that is:- specimencollecitongroup instance
				final SpecimenCollectionGroup specimenCollGrpObj = (SpecimenCollectionGroup) object;
				final Long scgId = specimenCollGrpObj.getId();
				final Collection specimenSet = (Collection) defaultBizLogic.retrieveAttribute(
						SpecimenCollectionGroup.class.getName(), scgId,
						"elements(specimenCollection)");

				//Get the list of specimens for this group(SCG)
				//			Set specimenSet = (Set)specimenCollGrpObj.getSpecimenCollection();
				final Iterator specimenSetItr = specimenSet.iterator();
				while (specimenSetItr.hasNext())
				{
					final Specimen specimenObj = (Specimen) specimenSetItr.next();
					allNodes = this.formTreeNode(specimenObj);
				}
			}

		}
		catch (final BizLogicException exp)
		{
			this.logger.error(exp.getMessage(), exp);
			exp.printStackTrace();
		}
		return allNodes;
	}

	/**
	 * This function accepts specimen object and sets treenode for each specimen. 
	 * @param specimenObj
	 * @return rootNode SpecimentreeNode object
	 */
	private Vector formTreeNode(Specimen specimenObj) throws BizLogicException
	{
		Collection specimenSet = new HashSet();
		final Vector specimenTreeVector = new Vector();

		final SpecimenTreeNode rootNode = new SpecimenTreeNode();
		rootNode.setIdentifier(specimenObj.getId());
		rootNode.setType(specimenObj.getSpecimenType());
		rootNode.setValue(specimenObj.getLabel());
		rootNode.setSpecimenClass(specimenObj.getClassName());

		rootNode.setParentIdentifier("0");
		rootNode.setParentValue("");

		specimenTreeVector.add(rootNode);

		final DefaultBizLogic defaultBizLogic = new DefaultBizLogic();
		specimenSet = (Collection) defaultBizLogic.retrieveAttribute(Specimen.class.getName(),
				specimenObj.getId(), "elements(childCollection)");
		//specimenObj.setChildrenSpecimen(childColl);

		//Get the list of specimens derived from this specimen.
		//specimenSet = (Set)specimenObj.getChildrenSpecimen();

		//formSpecimenTree() constructs tree form of specimens and returns the nodes in vector form.
		final Vector allNodes = this.formSpecimenTree(specimenTreeVector, rootNode, specimenSet);

		if (allNodes != null)
		{
			rootNode.setChildNodes(allNodes);
		}

		return allNodes;
	}

	/**
	 * This function constructs the tree indicating the parent-child relationship of the given specimen.
	 * @param parent SpecimenTreeNode
	 * @param childNodes Vector
	 * @return Vector
	 */
	private Vector formSpecimenTree(Vector specimenTreeVector, SpecimenTreeNode parent,
			Collection childNodes) throws BizLogicException
	{
		final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		final IBizLogic defaultBizLogic = factory.getBizLogic(-1);
		//If no childNodes present then tree will contain only the root node.
		if (childNodes == null)
		{
			return specimenTreeVector;
		}
		//Otherwise
		final Iterator specimenItr = childNodes.iterator();
		while (specimenItr.hasNext())
		{
			final Specimen specimen = (Specimen) specimenItr.next();
			final SpecimenTreeNode treeNode = new SpecimenTreeNode();

			treeNode.setIdentifier(specimen.getId());
			treeNode.setType(specimen.getSpecimenType());
			treeNode.setValue(specimen.getLabel());
			treeNode.setSpecimenClass(specimen.getClassName());

			treeNode.setParentIdentifier(parent.getIdentifier().toString());
			treeNode.setParentValue(parent.getValue());

			specimenTreeVector.add(treeNode);
			final Collection childColl = (Collection) defaultBizLogic.retrieveAttribute(
					Specimen.class.getName(), specimen.getId(), "elements(childrenSpecimen)");
			specimen.setChildSpecimenCollection(childColl);
			final Vector subChildNodesVector = this.formSpecimenTree(specimenTreeVector, treeNode,
					specimen.getChildSpecimenCollection());

			if (subChildNodesVector != null)
			{
				treeNode.setChildNodes(subChildNodesVector);
			}

		}
		return specimenTreeVector;
	}

	public Vector getTreeViewData(SessionDataBean arg0, Map arg1, List arg2)
	{

		return null;
	}

}
