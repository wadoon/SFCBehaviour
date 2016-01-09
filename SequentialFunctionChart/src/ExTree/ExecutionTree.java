package ExTree;

import java.util.ArrayList;
import java.util.List;

public class ExecutionTree {
	
	private List<ExecutionNode> v;
	private List<ExecutionEdge> e;
	public ExecutionTree(List<ExecutionNode> v, List<ExecutionEdge> e) {
		super();
		this.v = v;
		this.e = e;
	}
	public List<ExecutionNode> getV() {
		return v;
	}
	public void setV(List<ExecutionNode> v) {
		this.v = v;
	}
	public List<ExecutionEdge> getE() {
		return e;
	}
	public void setE(List<ExecutionEdge> e) {
		this.e = e;
	}
	
	public ExecutionNode getRoot(){
		for(ExecutionNode n : getV())
            if(n.isRoot())
                return n;
        return null;
	}
	
	public ExecutionNode getSingleNode(String name){
		for(ExecutionNode node : getV())
            if(node.getPath().equals(name))
                return node;
        return null;
	}
	
	public List<ExecutionEdge> getSuccessor(ExecutionNode node){
		List<ExecutionEdge> r = new ArrayList<ExecutionEdge>();
		if(node == null){return r;}
		for(ExecutionEdge e : getE()){
			if(e.getSourcePath().equals(node.getPath())){
				r.add(e);
			}
		}
		return(r);
	}
	
	public List<ExecutionEdge> getPath(ExecutionNode source, ExecutionNode destination){
		List<ExecutionEdge> r = new ArrayList<ExecutionEdge>();
		ExecutionNode x = source;
		while(!x.getPath().equals(destination.getPath()))
			for(ExecutionEdge e : getE()){
				if(e.getSourcePath().equals(x.getPath())){
					r.add(e);
					getSingleNode(e.getDestPath());
				}
			}
		return(r);
	}
}
