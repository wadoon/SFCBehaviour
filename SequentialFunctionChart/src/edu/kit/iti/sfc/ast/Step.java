package edu.kit.iti.sfc.ast;

import java.util.ArrayList;
import java.util.List;

public class Step {
	private String name;
	private boolean init;
	private List<Assignment> actions;
	
	public Step() {
		super();
		this.init = false;
		this.actions = new ArrayList<Assignment>();

	}
	
	public Step(String name, boolean init, List<Assignment> actions) {
		super();
		this.name = name;
		this.init = init;
		if(actions != null){
		this.actions = actions;}else{this.actions = new ArrayList<Assignment>();}

	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isInit() {
		return init;
	}
	public void setInit(boolean init) {
		this.init = init;
	}
	public List<Assignment> getActions() {
		return actions;
	}
	public void setActions(List<Assignment> actions) {
		this.actions = actions;
	}

	
}
