package BehaviourChecker;

import java.util.List;

import SFC.Expression;
import TSG.TwinNode;

public class CheckPoint {
	private boolean notequivalent;
	private TwinNode node;
	private List<Expression> premise;
	private Expression pathexpr_left;
	private Expression pathexpr_right;
	public CheckPoint(TwinNode node, List<Expression> premise, Expression pathexpr_left,Expression pathexpr_right,boolean notequivalent) {
		super();
		this.node = node;
		this.premise = premise;
		this.pathexpr_left = pathexpr_left;
		this.pathexpr_right = pathexpr_right;
		this.notequivalent = notequivalent;
	}
	public TwinNode getNode() {
		return node;
	}
	public void setNode(TwinNode node) {
		this.node = node;
	}
	public List<Expression> getPremise() {
		return premise;
	}
	public void setPremise(List<Expression> premise) {
		this.premise = premise;
	}
	public Expression getPathexpr_left() {
		return pathexpr_left;
	}
	public void setPathexpr_left(Expression pathexpr_left) {
		this.pathexpr_left = pathexpr_left;
	}
	public Expression getPathexpr_right() {
		return pathexpr_right;
	}
	public void setPathexpr_right(Expression pathexpr_right) {
		this.pathexpr_right = pathexpr_right;
	}
	public boolean isNotequivalent() {
		return notequivalent;
	}
	public void setNotequivalent(boolean notequivalent) {
		this.notequivalent = notequivalent;
	}

	
	
	
	
	
}
