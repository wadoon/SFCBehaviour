package SFC;

public class VariableExpr extends Expression {
	private Variable identifier;
	
	public VariableExpr() {
		super();
	}
	
	public VariableExpr(Variable id) {
		super();
		this.identifier = id;
	}
	public Variable getIdentifier() {
		return identifier;
	}
	public void setIdentifier(Variable id) {
		this.identifier = id;
	}

	@Override
	public String printExpr(){
		return(identifier.getName());
	}
	
	public Vartype getType(){
		return(identifier.getType());
	}
	
}
