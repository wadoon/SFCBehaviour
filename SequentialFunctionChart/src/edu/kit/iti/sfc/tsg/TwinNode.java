package edu.kit.iti.sfc.tsg;

import edu.kit.iti.sfc.extree.*;


public class TwinNode {
	private String identifier;
	private ExecutionNode left;
	private ExecutionNode right;
	private boolean root;
	
	public TwinNode(ExecutionNode left, ExecutionNode right) {
		super();
		String l,r;
		if(left != null){l = left.getPath();}else{l = "$EMPTY$";}
		if(right != null){r = right.getPath();}else{r = "$EMPTY$";}
		this.identifier = l + "//" + r;
		this.left = left;
		this.right = right;
		this.root = false;
	}
	
	
	
	public String getIdentifier() {
		return identifier;
	}



	public ExecutionNode getLeft() {
		return left;
	}


	public void setLeft(ExecutionNode left) {
		this.left = left;
	}


	public ExecutionNode getRight() {
		return right;
	}


	public void setRight(ExecutionNode right) {
		this.right = right;
	}


	public boolean isRoot() {
		return root;
	}

	public void setRoot(boolean root) {
		this.root = root;
	}

}
