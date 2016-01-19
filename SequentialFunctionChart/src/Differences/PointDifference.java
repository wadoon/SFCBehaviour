package Differences;

import java.util.List;

import SFC.Expression;
import TSG.TwinNode;

public abstract class PointDifference {
	private TwinNode node;
	private Expression path_left;
	private Expression path_right;
	private List<Expression> premise;
	public PointDifference(TwinNode node, Expression path_left, Expression path_right, List<Expression> premise) {
		super();
		this.node = node;
		this.path_left = path_left;
		this.path_right = path_right;
		this.premise = premise;
	}
	public TwinNode getNode() {
		return node;
	}
	public void setNode(TwinNode node) {
		this.node = node;
	}
	public Expression getPath_left() {
		return path_left;
	}
	public void setPath_left(Expression path_left) {
		this.path_left = path_left;
	}
	public Expression getPath_right() {
		return path_right;
	}
	public void setPath_right(Expression path_right) {
		this.path_right = path_right;
	}
	public List<Expression> getPremise() {
		return premise;
	}
	public void setPremise(List<Expression> premise) {
		this.premise = premise;
	}
		

	
	
	
}
