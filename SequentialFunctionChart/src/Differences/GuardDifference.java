package Differences;

import java.util.List;

import SFC.Expression;
import TSG.TwinEdge;
import TSG.TwinNode;

public class GuardDifference extends PointDifference{
	private TwinEdge tg;

	boolean unsat;
	public GuardDifference(TwinNode node, Expression path_left, Expression path_right, List<Expression> premise,
			TwinEdge tg, Boolean unsat) {
		super(node, path_left, path_right, premise);
		this.tg = tg;
		this.unsat = unsat;
	}



	public TwinEdge getTg() {
		return tg;
	}



	public void setTg(TwinEdge tg) {
		this.tg = tg;
	}



	public boolean isUnsat() {
		return unsat;
	}

	public void setUnsat(boolean unsat) {
		this.unsat = unsat;
	}
	
	
	
	
}
