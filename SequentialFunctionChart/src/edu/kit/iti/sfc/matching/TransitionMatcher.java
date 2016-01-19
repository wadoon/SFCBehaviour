package edu.kit.iti.sfc.matching;

import java.util.List;

import edu.kit.iti.sfc.extree.ExecutionEdge;
import edu.kit.iti.sfc.extree.ExecutionTree;

public interface TransitionMatcher {
	
	public List<TransitionMatch> match(List<ExecutionEdge> lsucc, List<ExecutionEdge> rsucc,ExecutionTree lt, ExecutionTree rt);

}
