package Matcher;

import java.util.List;

import ExTree.ExecutionEdge;
import ExTree.ExecutionTree;

public interface TransitionMatcher {
	
	public List<TransitionMatch> match(List<ExecutionEdge> lsucc, List<ExecutionEdge> rsucc,ExecutionTree lt, ExecutionTree rt);

}
