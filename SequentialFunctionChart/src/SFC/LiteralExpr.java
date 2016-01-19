package SFC;

public class LiteralExpr extends Expression {
	private String lit;
	private Vartype type;
	
	
	public LiteralExpr() {
		super();
	}
	
	public LiteralExpr(String lit, Vartype type) {
		super();
		this.lit = lit;
		this.type = type;
	}
	public String getLit() {
		return lit;
	}
	public void setLit(String lit) {
		this.lit = lit;
	}
	public Vartype getType() {
		return type;
	}
	public void setType(Vartype type) {
		this.type = type;
	}
	
	@Override
	public String printExpr(){
		return(lit);
	}
}
