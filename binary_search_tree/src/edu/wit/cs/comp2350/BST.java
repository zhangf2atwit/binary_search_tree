package edu.wit.cs.comp2350;

// @Override tags tell the compiler that the method is implementing a method
// defined in an ancestor class

// TODO: document class
/**
 * This is a Binary Search Tree (BST) is extends an abstract LocationContainer.
 * Class DiskLocation, the node has parent, left, and right references for
 * binary trees. Every time it pass the DiskLocation object, and all the
 * functions below are help it to build the Binary Search Tree.
 */
public class BST extends LocationContainer {

	@Override
	public DiskLocation find(DiskLocation d) {
		// TODO: implement method
		if (root == nil) {
			return nil;
		}

		// For find the target value, it search from the root.
		DiskLocation curr = root;

		// When the DiskLocation is not empty and not equals nil, to do the below things
		// inside of while loop.
		while (curr != null && curr != nil) {
			if (d.equals(curr)) {
				return curr;
			}

			// If d value grater than the current value, then find it in right subtree.
			// otherwise, find the value in the left tree.
			if (d.isGreaterThan(curr)) {
				curr = curr.right;

			} else {
				curr = curr.left;
			}
		}

		return nil; // if didn't find the same value, return the nil.
	}

	// Function to find the next value of this DiskLocation
	@Override
	public DiskLocation next(DiskLocation d) {
		// TODO: implement method
		DiskLocation curr = find(d);                             // find location of itself.

		if (curr.right != nil) {                                 // when it has the right child or subtree,
			DiskLocation min = findMin_right(curr.right);        // find the minimum of the right sub tree will be the next value
			return min;
		} else {
			return up(curr);
		}
	}

	// Function to find the minimum on the right sutree
	// this value will between the next element of the object b.
	private DiskLocation findMin_right(DiskLocation d) {

		// Track the right tree to the bottom,
		// the last element of the left will be the minimum of right subtree tree.
		while (d.left != nil) {
			d = d.left;                                           // track down to the most left element on right side
		}
		return d;
	}

	// call up function to help the value get the right place
	private DiskLocation up(DiskLocation x) {
		DiskLocation p = x.parent;
		if (p == nil)
			return p;
		else if (x == p.left) {
			return p;
		} else
			return up(p);
	}

	
	@Override
	public DiskLocation prev(DiskLocation d) {
		// TODO: implement method
		DiskLocation curr = find(d);

		if (curr.left != nil) {
			DiskLocation max = findMax_left(curr.left); 			
			return max;
		}
		else {
			return prevUp(d);
		}
	}

	// entering left sub tree, use findMax_left to find maximun in that sub tree
	private DiskLocation findMax_left(DiskLocation d) {

		while (d.right != nil) {
			d = d.right;                                         // track down to the most right element on left side
		}
		return d;
	}

	// This is a surport functionfor prev function,if right child not excits
	// preUp will help current to the it parent's postion in the right subtree.
	private DiskLocation prevUp(DiskLocation d) {
		DiskLocation curr = d.parent;

		if (curr == nil || d == curr.right) {
			return curr;
		} else
			return prevUp(curr);
	}

	@Override
	public void insert(DiskLocation d) {
		// TODO: implement method
		// init all the things as nil to hold each node's memory
		d.left = nil;
		d.right = nil;

		if (root == null) {
			root = nil;
		}

		d.parent = findParent(d, root, nil);

		if (d.parent == nil) {
			root = d;
		} else {
			if (d.parent.isGreaterThan(d)) {                        // Equals (d < d.parent)
				d.parent.left = d;

			} else {
				d.parent.right = d;
			}
		}
	}

	// find parent is a recursion function to help the value find their parent
	private DiskLocation findParent(DiskLocation n, DiskLocation curr, DiskLocation parent) {
		if (curr == nil) {
			return parent;
		} else {
			if (n.isGreaterThan(curr)) {
				return findParent(n, curr.right, curr);
				
			} else {
				return findParent(n, curr.left, curr);
			}
		}
	}

	// keep track the height of the tree
	// note: if the tree only have a single note, the height will be 0.
	@Override
	public int height() {
		// TODO: implement method
		if (root == nil) {
			return 0;
		}
		return findHeight(root) - 1;
	}

	// find the number of Max between the left size and right size
	private int findHeight(DiskLocation d) {
		if (d == nil) {
			return 0;
		}
		// math function to find the max height of the left tree and right tree
		int max_size = Math.max(findHeight(d.left), findHeight(d.right)) + 1;
		
		return max_size;     // line 30 in the LocationContainer class
							 // return 1 + _size(D.left) + _size(D.right);
	}
}
