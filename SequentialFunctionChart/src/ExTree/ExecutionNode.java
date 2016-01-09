package ExTree;

import java.util.Map;

import SFC.Variable;

public class ExecutionNode {
	private String path;
	private String step;
	private Map<Variable,String> state;
	private boolean root;
	

	public ExecutionNode(String path, String step, Map<Variable, String> state) {
		super();
		this.path = path;
		this.step = step;
		this.state = state;
		this.root = false;
	}
	
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getStep() {
		return step;
	}
	public void setStep(String step) {
		this.step = step;
	}
	public Map<Variable, String> getState() {
		return state;
	}
	public void setState(Map<Variable, String> state) {
		this.state = state;
	}

	public boolean isRoot() {
		return root;
	}

	public void setRoot(boolean root) {
		this.root = root;
	}
	
	

	
	

}
