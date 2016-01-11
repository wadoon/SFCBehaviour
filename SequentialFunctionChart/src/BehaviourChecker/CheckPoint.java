package BehaviourChecker;

import java.util.List;

import SFC.Expression;
import TSG.TwinNode;

public class CheckPoint {
	private TwinNode node;
	private List<Expression> premise;
	public CheckPoint(TwinNode node, List<Expression> premise) {
		super();
		this.node = node;
		this.premise = premise;
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
	
}
