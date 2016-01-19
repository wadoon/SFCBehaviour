package SFC;

public class BinOpExpr extends Expression {
	private Expression left;
	private BinOperator op;
	private Expression right;
	
	public Expression getLeft() {
		return left;
	}

	public void setLeft(Expression left) {
		this.left = left;
	}

	public BinOperator getOp() {
		return op;
	}

	public void setOp(BinOperator op) {
		this.op = op;
	}

	public Expression getRight() {
		return right;
	}

	public void setRight(Expression right) {
		this.right = right;
	}
	
	
	public BinOpExpr() {
		super();
	}

	public BinOpExpr(Expression left, BinOperator op, Expression right) {
		super();
		this.left = left;
		this.op = op;
		this.right = right;
	}
	
	@Override
	public String printExpr(){
		return("(" + left.printExpr() + ")" + " " + op.symbol + " " + "(" + right.printExpr() + ")");
	}
	

}
