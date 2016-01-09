package ExTree;

import java.util.Map;

import SFC.BinOpExpr;
import SFC.Expression;
import SFC.LiteralExpr;
import SFC.UnOpExpr;
import SFC.Variable;
import SFC.VariableExpr;

public final class ExpressionUtils {
	
	public static UnOpExpr replace(UnOpExpr expr, Map<Variable,String> m){
		UnOpExpr r = expr;
		Expression nested = expr.getExpr();
		if(nested instanceof LiteralExpr){
			return(r);
		}else{
			if(nested instanceof VariableExpr){
				if(m.containsKey(((VariableExpr) nested).getIdentifier())){
					r.setExpr(new LiteralExpr(m.get(((VariableExpr) nested).getIdentifier()),((VariableExpr) nested).getType()));
					return(r);
				}else{
					//should never happen
					return(r);	
				}
			}else{
				if(nested instanceof UnOpExpr){
					r.setExpr(replace((UnOpExpr)nested, m));
					return(r);
				}else{
					r.setExpr(replace((BinOpExpr)nested, m));
					return(r);
				}
			}
		}
	}
	
	public static BinOpExpr replace(BinOpExpr expr, Map<Variable,String> m){
		BinOpExpr r = expr;
		Expression left = expr.getLeft();
		Expression right = expr.getRight();
		if(left instanceof LiteralExpr){	}else{
			if(left instanceof VariableExpr){
				if(m.containsKey(((VariableExpr) left).getIdentifier())){
					r.setLeft(new LiteralExpr(m.get(((VariableExpr) left).getIdentifier()),((VariableExpr) left).getType()));
				}else{
					//should never happen	
				}
			}else{
				if(left instanceof UnOpExpr){
					r.setLeft(replace((UnOpExpr)left, m));

				}else{
					r.setLeft(replace((BinOpExpr)left, m));
				}
			}
		}
		if(right instanceof LiteralExpr){	}else{
			if(right instanceof VariableExpr){
				if(m.containsKey(((VariableExpr) right).getIdentifier())){
					r.setRight(new LiteralExpr(m.get(((VariableExpr) right).getIdentifier()),((VariableExpr) right).getType()));
				}else{
					//should never happen	
				}
			}else{
				if(right instanceof UnOpExpr){
					r.setRight(replace((UnOpExpr)right, m));

				}else{
					r.setRight(replace((BinOpExpr)right, m));
				}
			}
		}
		
		return(r);
	}

}
