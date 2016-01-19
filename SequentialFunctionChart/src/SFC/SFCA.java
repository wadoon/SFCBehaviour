package SFC;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class SFCA {

	private String name;
	private List<Variable> variables;
	private List<Step> steps;
	private List<Transition> trans;
	
	public SFCA() {
		super();
		this.variables = new ArrayList<Variable>();
		this.steps = new ArrayList<Step>();
		this.trans = new ArrayList<Transition>();
		
	}
	
	public SFCA(String name, List<Variable> variables, List<Step> steps, List<Transition> trans) {
		super();
		this.name = name;
		this.variables = variables;
		this.steps = steps;
		this.trans = trans;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Variable> getVariables() {
		return variables;
	}
	public void setVariables(List<Variable> variables) {
		this.variables = variables;
	}
	public List<Step> getSteps() {
		return steps;
	}
	public void setSteps(List<Step> steps) {
		this.steps = steps;
	}
	
	
	public List<Transition> getTrans() {
		return trans;
	}

	public void setTrans(List<Transition> trans) {
		this.trans = trans;
	}

	public Step initialStep(){
		for(Step step : getSteps())
            if(step.isInit())
                return step;
        return null;
	}
	
	public Step getSingleStep(String name){
		for(Step step : getSteps())
            if(step.getName().equals(name))
                return step;
        return null;
	}
	
	public List<Variable> getInput(){
		List<Variable> r = new LinkedList<Variable>(this.variables);
		for (Iterator<Variable> iterator = r.iterator(); iterator.hasNext(); ) {
		    Variable v = iterator.next();
		    if (!(v.isIn())) {
		        iterator.remove();
		    }
		}
		return r;
	}
	
	public List<Variable> getOutput(){
		List<Variable> r = new LinkedList<Variable>(this.variables);
		for (Iterator<Variable> iterator = r.iterator(); iterator.hasNext(); ) {
		    Variable v = iterator.next();
		    if (!(v.isOut())) {
		        iterator.remove();
		    }
		}
		return r;
	}
	
	
}
