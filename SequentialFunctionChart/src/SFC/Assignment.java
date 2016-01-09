package SFC;

public class Assignment {
	private Variable var;
	private Expression expr;
	
	public Assignment(Variable var, Expression expr) {
		super();
		this.var = var;
		this.expr = expr;
	}
	public Variable getVar() {
		return var;
	}
	public void setVar(Variable var) {
		this.var = var;
	}
	public Expression getExpr() {
		return expr;
	}
	public void setExpr(Expression expr) {
		this.expr = expr;
	}
	
	
}
