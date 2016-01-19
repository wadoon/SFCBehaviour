package edu.kit.iti.sfc.matching;

import edu.kit.iti.sfc.extree.ExecutionEdge;

public class TransitionMatch {
	private ExecutionEdge left;
	private ExecutionEdge right;
	private boolean equalleft;
	private boolean equalright;
	private boolean nomatch;
	public TransitionMatch(ExecutionEdge left, ExecutionEdge right, boolean equalleft,boolean equalright, boolean nomatch) {
		super();
		this.left = left;
		this.right = right;
		this.equalleft = equalleft;
		this.equalright = equalright;
		this.nomatch = nomatch;
	}
	public ExecutionEdge getLeft() {
		return left;
	}
	public void setLeft(ExecutionEdge left) {
		this.left = left;
	}
	public ExecutionEdge getRight() {
		return right;
	}
	public void setRight(ExecutionEdge right) {
		this.right = right;
	}
	public boolean isEqualleft() {
		return equalleft;
	}
	public void setEqualleft(boolean equal) {
		this.equalleft = equal;
	}
	
	public boolean isEqualright() {
		return equalright;
	}
	public void setEqualright(boolean equalright) {
		this.equalright = equalright;
	}
	public boolean isNomatch() {
		return nomatch;
	}
	public void setNomatch(boolean nomatch) {
		this.nomatch = nomatch;
	}
	
	
}
