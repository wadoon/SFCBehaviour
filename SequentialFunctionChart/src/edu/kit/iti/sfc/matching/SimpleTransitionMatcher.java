package edu.kit.iti.sfc.matching;

import java.util.ArrayList;
import java.util.List;

import edu.kit.iti.sfc.ast.BinOpExpr;
import edu.kit.iti.sfc.ast.Expression;
import edu.kit.iti.sfc.ast.LiteralExpr;
import edu.kit.iti.sfc.ast.UnOpExpr;
import edu.kit.iti.sfc.ast.VariableExpr;
import edu.kit.iti.sfc.extree.ExecutionEdge;
import edu.kit.iti.sfc.extree.ExecutionTree;

public class SimpleTransitionMatcher implements TransitionMatcher{

	@Override
	public List<TransitionMatch> match(List<ExecutionEdge> lsucc, List<ExecutionEdge> rsucc,ExecutionTree lt, ExecutionTree rt){
		
		List<TransitionMatch> result = new ArrayList<TransitionMatch>();
		for(ExecutionEdge l : lsucc){
			for(ExecutionEdge r : rsucc){
				if(matchStep(l,r)){
					result.add(new TransitionMatch(l, r, true,true,false));
				}
			}
		}
		
		for(ExecutionEdge l : lsucc){
			if(notmatched(l,result)){
				result.add(new TransitionMatch(l,null,true,true,true));
			}
		}
		for(ExecutionEdge r : rsucc){
			if(notmatched(r,result)){
				result.add(new TransitionMatch(null,r,true,true,true));
			}
		}
		
		return(result);
	}
	
	private boolean matchStep(ExecutionEdge l, ExecutionEdge r){
		String[] left = l.getDestPath().split("%");
		String[] right = r.getDestPath().split("%");
		if(equalExpr(l.getGuard(),r.getGuard()) || left[left.length-1].equals(right[right.length-1])){
			return true;
		}
		return false;
	}
	
	private boolean equalExpr(Expression l, Expression r){
		if(l instanceof VariableExpr && r instanceof VariableExpr && l.printExpr().equals(r.printExpr()) ){
			return(true);
		}
		if(l instanceof LiteralExpr && r instanceof LiteralExpr && l.printExpr().equals(r.printExpr())){
			return(true);
		}
		if(l instanceof BinOpExpr && r instanceof BinOpExpr && l.printExpr().equals(r.printExpr())){
			return(true);
		}
		if(l instanceof UnOpExpr && r instanceof UnOpExpr && l.printExpr().equals(r.printExpr())){
			return(true);
		}
		return(false);
	}
	
	private boolean notmatched(ExecutionEdge l, List<TransitionMatch> t){
		boolean b = true;
		for(TransitionMatch tm : t){
			if(tm.getLeft()!= null){
			if(tm.getLeft().getDestPath().equals(l.getDestPath())){
				b = false;
			}}
			if(tm.getRight()!=null){
			if(tm.getRight().getDestPath().equals(l.getDestPath())){
				b = false;
			}}
		}
		
		return b;
	}
}
