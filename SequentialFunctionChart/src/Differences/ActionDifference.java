package Differences;

import java.util.List;

import BehaviourChecker.VariableDuo;
import SFC.Expression;
import TSG.TwinNode;

public class ActionDifference extends PointDifference {
	private List<VariableDuo> variable;

	public ActionDifference(TwinNode node, Expression path_left, Expression path_right, List<Expression> premise,
			List<VariableDuo> variable) {
		super(node, path_left, path_right, premise);
		this.variable = variable;
	}

	public List<VariableDuo> getVariable() {
		return variable;
	}

	public void setVariable(List<VariableDuo> variable) {
		this.variable = variable;
	}
	
	
	
}
