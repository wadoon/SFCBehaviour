package edu.kit.iti.sfc.tsg;

import java.util.ArrayList;
import java.util.List;


public class TwinGraph {
	
	private List<TwinNode> v;
	private List<TwinEdge> e;
	public TwinGraph(List<TwinNode> v, List<TwinEdge> e) {
		super();
		this.v = v;
		this.e = e;
	}
	public List<TwinNode> getV() {
		return v;
	}
	public void setV(List<TwinNode> v) {
		this.v = v;
	}
	public List<TwinEdge> getE() {
		return e;
	}
	public void setE(List<TwinEdge> e) {
		this.e = e;
	}
	
	public TwinNode getRoot(){
		for(TwinNode n : getV())
            if(n.isRoot())
                return n;
        return null;
	}
	
	public TwinNode getSingleNode(String name){
		for(TwinNode node : getV())
            if(node.getIdentifier().equals(name))
                return node;
        return null;
	}
	
	public List<TwinEdge> getSuccessor(TwinNode node){
		List<TwinEdge> r = new ArrayList<TwinEdge>();
		if(node == null){return r;}
		for(TwinEdge e : getE()){
			if(e.getSourceId().equals(node.getIdentifier())){
				r.add(e);
			}
		}
		return(r);
	}
	
	public List<TwinEdge> getPath(TwinNode source, TwinNode destination){
		List<TwinEdge> r = new ArrayList<TwinEdge>();
		TwinNode x = source;
		while(!x.getIdentifier().equals(destination.getIdentifier()))
			for(TwinEdge e : getE()){
				if(e.getSourceId().equals(x.getIdentifier())){
					r.add(e);
					getSingleNode(e.getDestId());
				}
			}
		return(r);
	}
}
