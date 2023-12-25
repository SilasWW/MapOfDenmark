
package Map_of_Denmark.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The TreeNode class in which K-D trees are built.
 * The K-D trees will be built of TreeNodes that are all related to one another.
 */
public class TreeNode implements Serializable {
	
	private Way[] ways;
	private TreeNode leftChild;
	private TreeNode rightChild;
	
	private double minX;
	private double maxX;
	private double minY;
	private double maxY;
	
	private int layer;
	private int leafSize;
	
	public static boolean SortX;
	
	/**
	 * TreeNode constructor
	 * @param layer int that defines what layer the TreeNode is in the full K-D tree of which it is a part.
	 */
	public TreeNode(int layer) {
		this.layer = layer;
		
		this.leafSize = 500;
	}
	
	/**
	 * The method to build the K-D tree, as well as continue it to the child nodes.
	 * @param wayList defines the list of Ways from which to build the remaining K-D tree.
	 * @return the TreeNode with all its children.
	 */
	public TreeNode build(List<Way> wayList) {

		if(layer % 2 == 0) SortX = false;
		else SortX = true;
		Collections.sort(wayList);
		
		if(wayList.size() < leafSize) {
			ways = new Way[wayList.size()];
			for(int i = 0; i < wayList.size(); i++) ways[i] = wayList.get(i);
		} else {
			this.ways = new Way[] {
					wayList.get(0), 
					wayList.get(wayList.size() - 1) 
				};
		}
		
		for(int i = 0; i < wayList.size(); i++) {
			if(i == 0) {
				minX = wayList.get(i).getMinX();
				maxX = wayList.get(i).getMaxX();
				minY = wayList.get(i).getMinY();
				maxY = wayList.get(i).getMaxY();
				continue;
			}
			
			if(wayList.get(i).getMinX() < minX) minX = wayList.get(i).getMinX();
			if(wayList.get(i).getMaxX() > maxX) maxX = wayList.get(i).getMaxX();
			if(wayList.get(i).getMinY() < minY) minY = wayList.get(i).getMinY();
			if(wayList.get(i).getMaxY() > maxY) maxY = wayList.get(i).getMaxY();
		}

		
		if(wayList.size() >= leafSize) {
			List<Way> splitWaysLeft = wayList.subList(0, wayList.size() / 2);
			List<Way> splitWaysRight = wayList.subList(wayList.size() / 2, wayList.size());

			this.leftChild = new TreeNode(layer + 1).build(splitWaysLeft);
			this.rightChild = new TreeNode(layer + 1).build(splitWaysRight);
		}
		
		return this;
	}

	
	/**
	 * The method to build the K-D tree, as well as continue it to the child nodes.
	 * Specific to the Highway class.
	 * @param wayList defines the list of Ways from which to build the remaining K-D tree.
	 * @return the TreeNode with all its children.
	 */
	public TreeNode buildHighway(List<Highway> wayList) {

		if(layer % 2 == 0) SortX = false;
		else SortX = true;
		Collections.sort(wayList);
		
		if(wayList.size() < leafSize) {
			ways = new Way[wayList.size()];
			for(int i = 0; i < wayList.size(); i++) ways[i] = wayList.get(i);
		} else {
			this.ways = new Way[] {
					wayList.get(0), 
					wayList.get(wayList.size() - 1) 
				};
		}
		
		for(int i = 0; i < wayList.size(); i++) {
			if(i == 0) {
				minX = wayList.get(i).getMinX();
				maxX = wayList.get(i).getMaxX();
				minY = wayList.get(i).getMinY();
				maxY = wayList.get(i).getMaxY();
				continue;
			}
			
			if(wayList.get(i).getMinX() < minX) minX = wayList.get(i).getMinX();
			if(wayList.get(i).getMaxX() > maxX) maxX = wayList.get(i).getMaxX();
			if(wayList.get(i).getMinY() < minY) minY = wayList.get(i).getMinY();
			if(wayList.get(i).getMaxY() > maxY) maxY = wayList.get(i).getMaxY();
		}
		

		
		if(wayList.size() >= leafSize) {
			List<Highway> splitWaysLeft = wayList.subList(0, wayList.size() / 2);
			List<Highway> splitWaysRight = wayList.subList(wayList.size() / 2, wayList.size());

			this.leftChild = new TreeNode(layer + 1).buildHighway(splitWaysLeft);
			this.rightChild = new TreeNode(layer + 1).buildHighway(splitWaysRight);
		}
		
		return this;
	}

	/**
	 * The method to begin searching through a K-D tree, taking only the boundaries of the search area as parameters.
	 * @param minX defines the lowest allowed x-coordinate for the K-D tree leaves, in order to be returned in this method.
	 * @param minY defines the lowest allowed y-coordinate for the K-D tree leaves, in order to be returned in this method.
	 * @param maxX defines the height allowed x-coordinate for the K-D tree leaves, in order to be returned in this method.
	 * @param maxY defines the height allowed y-coordinate for the K-D tree leaves, in order to be returned in this method.
	 * @return a list of Ways contained in all the leaves found within the boundaries set.
	 */
	public List<Way> findLeaf(double minX, double minY, double maxX, double maxY) {
		return findLeaf(this, true, minX, minY, maxX, maxY);
	}
	
	/**
	 * The method used to read through the rest of the K-D tree.
	 * If 
	 * @param treeNode defines the node of the over all K-D tree to check.
	 * @param checkX defines the boolean to use to check whether to check the x- or y-coordinate of the TreeNode for validity.
	 * @param minX defines the lowest allowed x-coordinate for the K-D tree leaves, in order to be returned in this method.
	 * @param minY defines the lowest allowed y-coordinate for the K-D tree leaves, in order to be returned in this method.
	 * @param maxX defines the height allowed x-coordinate for the K-D tree leaves, in order to be returned in this method.
	 * @param maxY defines the height allowed y-coordinate for the K-D tree leaves, in order to be returned in this method.
	 * @return a list of ways contained in all the leaves found within the boundaries set.
	 */
	public List<Way> findLeaf(TreeNode treeNode, boolean checkX, double minX, double minY, double maxX, double maxY) {
		ArrayList<Way> ways = new ArrayList<Way>();
		if(!treeNode.hasChildren()) {
			for(int i = 0; i < treeNode.getWays().length; i++) {
				ways.add(treeNode.getWays()[i]);
			}
			return ways;
		}
		if(checkX) {
			if(treeNode.getRightChild().getMaxX() >= minX && treeNode.getRightChild().getMinX() <= maxX)
				ways.addAll(findLeaf(treeNode.getRightChild(), !checkX, minX, minY, maxX, maxY));
			if(treeNode.getLeftChild().getMaxX() >= minX && treeNode.getLeftChild().getMinX() <= maxX)
				ways.addAll(findLeaf(treeNode.getLeftChild(), !checkX, minX, minY, maxX, maxY));
		} else {
			if(treeNode.getRightChild().getMaxY() >= minY && treeNode.getRightChild().getMinY() <= maxY)
				ways.addAll(findLeaf(treeNode.getRightChild(), !checkX, minX, minY, maxX, maxY));
			if(treeNode.getLeftChild().getMaxY() >= minY && treeNode.getLeftChild().getMinY() <= maxY)
				ways.addAll(findLeaf(treeNode.getLeftChild(), !checkX, minX, minY, maxX, maxY));
		}
		
		return ways;
	}
	
	/**
	 * Sets the TreeNode's left child.
	 * @param leftChild the TreeNode to set.
	 */
	public void setLeftChild(TreeNode leftChild) {
		this.leftChild = leftChild;
	}

	
	/**
	 * Sets the TreeNode's right child.
	 * @param rightChild the TreeNode to set.
	 */
	public void setRightChild(TreeNode rightChild) {
		this.rightChild = rightChild;
	}

	
	/**
	 * @return the TreeNode's left child.
	 */
	public TreeNode getLeftChild() {
		return leftChild;
	}


	/**
	 * @return Gets the TreeNode's right child.
	 */
	public TreeNode getRightChild() {
		return rightChild;
	}


	/**
	 * @return Gets the TreeNode's list of Ways
	 */
	protected Way[] getWays() {
		return ways;
	}


	/**
	 * @return if the TreeNode has children or if it is considered a leaf.
	 */
	public boolean hasChildren() {
		return (leftChild != null || rightChild != null);
	}
	
	/**
	 * Sets the TreeNode's minimum and maximum coordinates.
	 * @param minX the TreeNode's minimum x-coordinate.
	 * @param maxX the TreeNode's maximum x-coordinate.
	 * @param minY the TreeNode's minimum y-coordinate.
	 * @param maxY the TreeNode's maximum y-coordinate.
	 */
	public void setBoundaries(double minX, double maxX, double minY, double maxY) {
		this.minX = minX;
		this.maxX = maxX;
		this.minY = minY;
		this.maxY = maxY;
	}
	
	/*
	 * @return the TreeNode's minimum x-coordinate
	 */
	public double getMinX() { return minX; }
	/*
	 * @return the TreeNode's maximum x-coordinate
	 */
	public double getMaxX() { return maxX; }
	/*
	 * @return the TreeNode's minimum y-coordinate
	 */
	public double getMinY() { return minY; }
	/*
	 * @return the TreeNode's maximum y-coordinate
	 */
	public double getMaxY() { return maxY; }
	
}
