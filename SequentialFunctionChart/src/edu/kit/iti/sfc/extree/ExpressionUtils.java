package edu.kit.iti.sfc.extree;

import java.util.Map;

import edu.kit.iti.sfc.ast.BinOpExpr;
import edu.kit.iti.sfc.ast.Expression;
import edu.kit.iti.sfc.ast.LiteralExpr;
import edu.kit.iti.sfc.ast.UnOpExpr;
import edu.kit.iti.sfc.ast.Variable;
import edu.kit.iti.sfc.ast.VariableExpr;

public final class ExpressionUtils {
	
	public static UnOpExpr replace(UnOpExpr expr, Map<Variable,Expression> m){
		UnOpExpr r = expr;
		Expression nested = expr.getExpr();
		if(nested instanceof LiteralExpr){
			return(r);
		}else{
			if(nested instanceof VariableExpr){
				if(m.containsKey(((VariableExpr) nested).getIdentifier())){
					r.setExpr(m.get(((VariableExpr) nested).getIdentifier()));
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
	
	public static BinOpExpr replace(BinOpExpr expr, Map<Variable,Expression> m){
		BinOpExpr r = expr;
		Expression left = expr.getLeft();
		Expression right = expr.getRight();
		if(left instanceof LiteralExpr){	}else{
			if(left instanceof VariableExpr){
				if(m.containsKey(((VariableExpr) left).getIdentifier())){
					r.setLeft(m.get(((VariableExpr) left).getIdentifier()));
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
					r.setRight(m.get(((VariableExpr) right).getIdentifier()));
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
