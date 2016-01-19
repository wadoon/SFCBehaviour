package SFC;

public class UnOpExpr extends Expression {
	private Expression expr;
	private UnOperator op;
	
	
	public UnOpExpr() {
		super();
	}
	public UnOpExpr(Expression expr, UnOperator op) {
		super();
		this.expr = expr;
		this.op = op;
	}
	public Expression getExpr() {
		return expr;
	}
	public void setExpr(Expression expr) {
		this.expr = expr;
	}
	public UnOperator getOp() {
		return op;
	}
	public void setOp(UnOperator op) {
		this.op = op;
	}
	
	@Override
	public String printExpr(){
		return(op.symbol+ " " + "(" + expr.printExpr() + ")");
	}
	

}
