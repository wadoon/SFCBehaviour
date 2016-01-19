package edu.kit.iti.sfc.diff;

import java.util.List;

import edu.kit.iti.sfc.ast.Expression;
import edu.kit.iti.sfc.tsg.TwinEdge;
import edu.kit.iti.sfc.tsg.TwinNode;

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
