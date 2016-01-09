package BehaviourChecker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.microsoft.z3.*;
import SFC.*;

public final class SMTUtil {
	
	public static Map<String,Expr> addVariables(Context ctx, List<Variable> x,int k){
		Map<String,Expr> result = new HashMap<String,Expr>();
		for(Variable xs : x){
			if(xs.isIn()){
				if(xs.getType().equals(Vartype.BOOL)){
					result.put(xs.getName()+"Åòk",ctx.mkBoolConst(xs.getName()+"Åòk"));
				}
				if(xs.getType().equals(Vartype.INT)){
					result.put(xs.getName()+"Åòk",ctx.mkIntConst(xs.getName()+"Åòk"));
				}
			}else{
				if(xs.getType().equals(Vartype.BOOL)){
					result.put(xs.getName(),ctx.mkBoolConst(xs.getName()));
				}
				if(xs.getType().equals(Vartype.INT)){
					result.put(xs.getName(),ctx.mkIntConst(xs.getName()));
				}
			}
		}
		return(result);
	}
	
	public static Expr addGuard(Context ctx, Expression g, Map<Variable,Expr> vardecl){
		if (g instanceof LiteralExpr){
			switch(((LiteralExpr) g).getType()){
			case INT :
				if(isNumeric(((LiteralExpr) g).getLit())){
					return(ctx.mkInt(Integer.parseInt(((LiteralExpr) g).getLit())));
				}else{
					return((IntExpr)vardecl.get(((LiteralExpr) g).getLit()));
				}
			case BOOL:
				if(((LiteralExpr) g).getLit().equals("TRUE")){
					return(ctx.mkTrue());
				}else{
					if(((LiteralExpr) g).getLit().equals("FALSE")){
						return(ctx.mkFalse());
					}else{
						return(((BoolExpr)(vardecl.get(((LiteralExpr) g).getLit()))));
					}
				}
			}
			
		}else{
			if (g instanceof UnOpExpr){
				switch(((UnOpExpr) g).getOp()){
				case MINUS :	
						return(ctx.mkUnaryMinus((ArithExpr)addGuard(ctx,((UnOpExpr) g).getExpr(),vardecl)));
							
				case NEGATE :
						return(ctx.mkNot((BoolExpr)addGuard(ctx,((UnOpExpr) g).getExpr(),vardecl)));
				
				}	
			}else{
				if (g instanceof BinOpExpr){
					switch(((BinOpExpr) g).getOp()){
					case ADD:
						ArithExpr add1 = (ArithExpr)addGuard(ctx,((BinOpExpr) g).getLeft(),vardecl);
						ArithExpr add2 = (ArithExpr)addGuard(ctx,((BinOpExpr) g).getRight(),vardecl);
						return(ctx.mkAdd(add1,add2));
					case MULT:
						ArithExpr mul1 = (ArithExpr)addGuard(ctx,((BinOpExpr) g).getLeft(),vardecl);
						ArithExpr mul2 = (ArithExpr)addGuard(ctx,((BinOpExpr) g).getRight(),vardecl);
						return(ctx.mkMul(mul1,mul2));
					case SUB:
						ArithExpr sub1 = (ArithExpr)addGuard(ctx,((BinOpExpr) g).getLeft(),vardecl);
						ArithExpr sub2 = (ArithExpr)addGuard(ctx,((BinOpExpr) g).getRight(),vardecl);
						return(ctx.mkSub(sub1,sub2));
					case DIV:
						ArithExpr div1 = (ArithExpr)addGuard(ctx,((BinOpExpr) g).getLeft(),vardecl);
						ArithExpr div2 = (ArithExpr)addGuard(ctx,((BinOpExpr) g).getRight(),vardecl);
						return(ctx.mkDiv(div1,div2));
					case MOD:
						IntExpr mod1 = (IntExpr)addGuard(ctx,((BinOpExpr) g).getLeft(),vardecl);
						IntExpr mod2 = (IntExpr)addGuard(ctx,((BinOpExpr) g).getRight(),vardecl);
						return(ctx.mkMod(mod1, mod2));
					case AND:
						BoolExpr and1 = (BoolExpr)addGuard(ctx,((BinOpExpr) g).getLeft(),vardecl);
						BoolExpr and2 = (BoolExpr)addGuard(ctx,((BinOpExpr) g).getRight(),vardecl);
						return(ctx.mkAnd(and1, and2));
					case OR:
						BoolExpr or1 = (BoolExpr)addGuard(ctx,((BinOpExpr) g).getLeft(),vardecl);
						BoolExpr or2 = (BoolExpr)addGuard(ctx,((BinOpExpr) g).getRight(),vardecl);
						return(ctx.mkOr(or1, or2));
					case XOR:
						BoolExpr xor1 = (BoolExpr)addGuard(ctx,((BinOpExpr) g).getLeft(),vardecl);
						BoolExpr xor2 = (BoolExpr)addGuard(ctx,((BinOpExpr) g).getRight(),vardecl);
						return(ctx.mkXor(xor1, xor2));
					case EQUALS:
						Expr expr1 = addGuard(ctx,((BinOpExpr) g).getLeft(),vardecl);
						Expr expr2 = addGuard(ctx, ((BinOpExpr) g).getRight(),vardecl);
						return(ctx.mkEq(expr1, expr2));
					case NOT_EQUALS:
						Expr nexpr1 = addGuard(ctx,((BinOpExpr) g).getLeft(),vardecl);
						Expr nexpr2 = addGuard(ctx, ((BinOpExpr) g).getRight(),vardecl);
						return(ctx.mkNot(ctx.mkEq(nexpr1, nexpr2)));
					case POWER:
						ArithExpr pow1 = (ArithExpr)addGuard(ctx,((BinOpExpr) g).getLeft(),vardecl);
						ArithExpr pow2= (ArithExpr)addGuard(ctx,((BinOpExpr) g).getRight(),vardecl);
						return(ctx.mkPower(pow1,pow2));
					case LESS_THAN:
						ArithExpr lt1 = (ArithExpr)addGuard(ctx,((BinOpExpr) g).getLeft(),vardecl);
						ArithExpr lt2 = (ArithExpr)addGuard(ctx,((BinOpExpr) g).getRight(),vardecl);
						return(ctx.mkLt(lt1,lt2));
					case GREATER_THAN:
						ArithExpr gt1 = (ArithExpr)addGuard(ctx,((BinOpExpr) g).getLeft(),vardecl);
						ArithExpr gt2 = (ArithExpr)addGuard(ctx,((BinOpExpr) g).getRight(),vardecl);
						return(ctx.mkGt(gt1,gt2));
					case GREATER_EQUALS:
						ArithExpr ge1 = (ArithExpr)addGuard(ctx,((BinOpExpr) g).getLeft(),vardecl);
						ArithExpr ge2 = (ArithExpr)addGuard(ctx,((BinOpExpr) g).getRight(),vardecl);
						return(ctx.mkGe(ge1,ge2));
					case LESS_EQUALS:
						ArithExpr le1 = (ArithExpr)addGuard(ctx,((BinOpExpr) g).getLeft(),vardecl);
						ArithExpr le2 = (ArithExpr)addGuard(ctx,((BinOpExpr) g).getRight(),vardecl);
						return(ctx.mkLe(le1,le2));
					}
				}else{
					return(null);
				}
			}
		}
		return null;
	}
	
	private static boolean isNumeric(String s) {  
	    return s.matches("[-+]?\\d*\\.?\\d+");  
	}  
	

}
