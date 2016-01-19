package edu.kit.iti.sfc.behaviourcheck;

import edu.kit.iti.sfc.ast.Variable;

public class VariableDuo{
	private Variable left;
	private Variable right;
	public VariableDuo(Variable left, Variable right) {
		super();
		this.left = left;
		this.right = right;
	}
	public Variable getLeft() {
		return left;
	}
	public void setLeft(Variable left) {
		this.left = left;
	}
	public Variable getRight() {
		return right;
	}
	public void setRight(Variable right) {
		this.right = right;
	}
	
}