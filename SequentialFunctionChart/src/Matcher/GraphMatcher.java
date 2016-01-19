package Matcher;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import ExTree.ExecutionEdge;
import ExTree.ExecutionNode;
import ExTree.ExecutionTree;
import SFC.EmptyExpr;
import TSG.TwinEdge;
import TSG.TwinGraph;
import TSG.TwinNode;

public class GraphMatcher {
	
	private TransitionMatcher tm;

	public GraphMatcher(TransitionMatcher tm) {
		super();
		this.tm = tm;
	}
	
	public TwinGraph matchGraph(ExecutionTree leftTree, ExecutionTree rightTree){
		List<TwinNode> vreturn = new LinkedList<TwinNode>();
		List<TwinEdge> ereturn = new LinkedList<TwinEdge>();
		Queue<TwinNode> queue = new LinkedList<TwinNode>();
		TwinNode tn = new TwinNode(leftTree.getRoot(),rightTree.getRoot());
		tn.setRoot(true);
		
		vreturn.add(tn);
		queue.add(tn);
		
		while(!queue.isEmpty()){
			TwinNode tx = queue.poll();
			List<TransitionMatch> ms = tm.match(leftTree.getSuccessor(tn.getLeft()), rightTree.getSuccessor(tn.getRight()), leftTree, rightTree);
			for(TransitionMatch x : ms){
				if(x.isNomatch()){
					if(x.getLeft() == null){
						tn = new TwinNode(null,rightTree.getSingleNode(x.getRight().getDestPath()));
						vreturn.add(tn);
						ereturn.add(new TwinEdge(tx.getIdentifier(),tn.getIdentifier(),new EmptyExpr(),x.getRight().getGuard()));
						addNoMatch(tn,vreturn,ereturn,rightTree);
					}else{
						tn = new TwinNode(leftTree.getSingleNode(x.getLeft().getDestPath()),null);
						vreturn.add(tn);
						ereturn.add(new TwinEdge(tx.getIdentifier(),tn.getIdentifier(),x.getLeft().getGuard(),new EmptyExpr()));
						addNoMatch(tn,vreturn,ereturn,leftTree);
					}
				}else{
					if(x.isEqualleft()){
						if(x.isEqualright()){
							tn = new TwinNode(leftTree.getSingleNode(x.getLeft().getDestPath()),rightTree.getSingleNode(x.getRight().getDestPath()));
							vreturn.add(tn);
							ereturn.add(new TwinEdge(tx.getIdentifier(),tn.getIdentifier(),x.getLeft().getGuard(),x.getRight().getGuard()));
							queue.add(tn);
						}else{
							queue.add(addAheadRight(tx,x,vreturn,ereturn,leftTree,rightTree));
						}
					}else{
						if(x.isEqualright()){
							queue.add(addAheadLeft(tx,x,vreturn,ereturn,leftTree,rightTree));
						}else{
							queue.add(addAheadMatch(tx,x,vreturn,ereturn,leftTree,rightTree));
						}
					}
				}
			}
		}
		return(new TwinGraph(vreturn,ereturn));
	}
	
	private void addNoMatch(TwinNode tn,List<TwinNode> vs, List<TwinEdge> es,ExecutionTree ext){
		if(tn.getLeft() == null){
			Queue<TwinNode> queue = new LinkedList<TwinNode>();
			queue.add(tn);
			while(!queue.isEmpty()){
				TwinNode t = queue.poll();
				ExecutionNode x = t.getRight();
				List<ExecutionEdge> list= ext.getSuccessor(x);
				for(ExecutionEdge edge : list){
					TwinNode rn = new TwinNode(null,ext.getSingleNode(edge.getDestPath()));
					vs.add(rn);
					es.add(new TwinEdge(t.getIdentifier(),rn.getIdentifier(),new EmptyExpr(),edge.getGuard()));
					queue.add(rn);
				}
			}
		}else{
			Queue<TwinNode> queue = new LinkedList<TwinNode>();
			queue.add(tn);
				TwinNode t = queue.poll();
				ExecutionNode x = t.getLeft();
				List<ExecutionEdge> list= ext.getSuccessor(x);
				for(ExecutionEdge edge : list){
					TwinNode rn = new TwinNode(ext.getSingleNode(edge.getDestPath()),null);
					vs.add(rn);
					es.add(new TwinEdge(t.getIdentifier(),rn.getIdentifier(),edge.getGuard(),new EmptyExpr()));
					queue.add(rn);
				}
			}
		}
	
	private TwinNode addAheadMatch(TwinNode prec,TransitionMatch tm, List<TwinNode> vs,List<TwinEdge> es, ExecutionTree left, ExecutionTree right){
		//right side
		TwinNode last = new TwinNode(left.getSingleNode(tm.getLeft().getDestPath()),right.getSingleNode(tm.getRight().getDestPath()));
		TwinNode prev = prec;
		for(ExecutionEdge ex : right.getPath(prec.getRight(), right.getSingleNode(tm.getRight().getDestPath()))){
			if(!ex.getDestPath().equals(tm.getRight().getDestPath())){
				TwinNode rn = new TwinNode(null,right.getSingleNode(ex.getDestPath()));
				vs.add(rn);
				es.add(new TwinEdge(prev.getIdentifier(),rn.getIdentifier(),new EmptyExpr(),ex.getGuard()));
				prev = rn;
			}
		}
		es.add(new TwinEdge(prev.getIdentifier(),last.getIdentifier(),new EmptyExpr(),tm.getRight().getGuard()));
		//left side
		prev = prec;
		for(ExecutionEdge ex : left.getPath(prec.getLeft(), left.getSingleNode(tm.getLeft().getDestPath()))){
			if(!ex.getDestPath().equals(tm.getLeft().getDestPath())){
				TwinNode rn = new TwinNode(left.getSingleNode(ex.getDestPath()),null);
				vs.add(rn);
				es.add(new TwinEdge(prev.getIdentifier(),rn.getIdentifier(),ex.getGuard(),new EmptyExpr()));
				prev = rn;
			}
		}
		es.add(new TwinEdge(prev.getIdentifier(),last.getIdentifier(),tm.getLeft().getGuard(),new EmptyExpr()));
		vs.add(last);
		return(last);
	}
	private TwinNode addAheadLeft(TwinNode prec,TransitionMatch tm, List<TwinNode> vs,List<TwinEdge> es, ExecutionTree left, ExecutionTree right){
				//left side
				TwinNode prev = prec;
				TwinNode last = new TwinNode(left.getSingleNode(tm.getLeft().getDestPath()),right.getSingleNode(tm.getRight().getDestPath()));
				for(ExecutionEdge ex : left.getPath(prec.getLeft(), left.getSingleNode(tm.getLeft().getDestPath()))){
					if(!ex.getDestPath().equals(tm.getLeft().getDestPath())){
						TwinNode rn = new TwinNode(left.getSingleNode(ex.getDestPath()),null);
						vs.add(rn);
						es.add(new TwinEdge(prev.getIdentifier(),rn.getIdentifier(),ex.getGuard(),tm.getRight().getGuard()));
						prev = rn;
					}
				}
				es.add(new TwinEdge(prev.getIdentifier(),last.getIdentifier(),tm.getLeft().getGuard(),tm.getRight().getGuard()));
				vs.add(last);
				return(last);
	}
	private TwinNode addAheadRight(TwinNode prec,TransitionMatch tm, List<TwinNode> vs,List<TwinEdge> es, ExecutionTree left, ExecutionTree right){
		//right side
				TwinNode last = new TwinNode(left.getSingleNode(tm.getLeft().getDestPath()),right.getSingleNode(tm.getRight().getDestPath()));
				TwinNode prev = prec;
				for(ExecutionEdge ex : right.getPath(prec.getRight(), right.getSingleNode(tm.getRight().getDestPath()))){
					if(!ex.getDestPath().equals(tm.getRight().getDestPath())){
						TwinNode rn = new TwinNode(null,right.getSingleNode(ex.getDestPath()));
						vs.add(rn);
						es.add(new TwinEdge(prev.getIdentifier(),rn.getIdentifier(),tm.getLeft().getGuard(),ex.getGuard()));
						prev = rn;
					}
				}
				es.add(new TwinEdge(prev.getIdentifier(),last.getIdentifier(),tm.getLeft().getGuard(),tm.getRight().getGuard()));
				return(last);
	}
}
