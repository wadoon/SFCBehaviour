package edu.kit.iti.sfc.diff;

import java.util.List;

import edu.kit.iti.sfc.ast.Expression;
import edu.kit.iti.sfc.tsg.TwinNode;

public class MatchDifference extends PointDifference {
	boolean lookaheadmatch;
	TwinNode dest;
	
	

	public MatchDifference(TwinNode node, Expression path_left, Expression path_right, List<Expression> premise,
			boolean lookaheadmatch,TwinNode dest) {
		super(node, path_left, path_right, premise);
		this.lookaheadmatch = lookaheadmatch;
		this.dest = dest;
	}

	public boolean isLookaheadmatch() {
		return lookaheadmatch;
	}

	public void setLookaheadmatch(boolean lookaheadmatch) {
		this.lookaheadmatch = lookaheadmatch;
	}

	public TwinNode getDest() {
		return dest;
	}

	public void setDest(TwinNode dest) {
		this.dest = dest;
	}
	
	
	
}
