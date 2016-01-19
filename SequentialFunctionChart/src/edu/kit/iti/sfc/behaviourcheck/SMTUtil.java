package edu.kit.iti.sfc.behaviourcheck;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.microsoft.z3.*;

import edu.kit.iti.sfc.ast.*;

public final class SMTUtil {
	
	public static Map<String,Expr> addVariables(Context ctx, List<Variable> x,int k,Map<String,Expr> m){
		Map<String,Expr> result = new HashMap<String,Expr>(m);
		for(Variable xs : x){
			if(xs.isIn()){
				if(xs.getType().equals(Vartype.BOOL)){
					result.put(xs.getName()+"��"+k,ctx.mkBoolConst(xs.getName()+"��"+k));
				}
				if(xs.getType().equals(Vartype.INT)){
					result.put(xs.getName()+"��"+k,ctx.mkIntConst(xs.getName()+"��"+k));
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
	
	
	public static Expr addExpr(Context ctx, Expression g, Map<String,Expr> vardecl){

		if (g instanceof LiteralExpr){
			switch(((LiteralExpr) g).getType()){
			case INT :
				if(isNumeric(((LiteralExpr) g).getLit())){
					return(ctx.mkInt(Integer.parseInt(((LiteralExpr) g).getLit())));
				}else{
					return(vardecl.get(((LiteralExpr) g).getLit()));
				}
			case BOOL:
				if(((LiteralExpr) g).getLit().equals("TRUE")){
					return(ctx.mkTrue());
				}else{
					if(((LiteralExpr) g).getLit().equals("FALSE")){
						return(ctx.mkFalse());
					}else
						return(((vardecl.get(((LiteralExpr) g).getLit()))));
					}
				}
			}
			
		else{
			if (g instanceof UnOpExpr){
				switch(((UnOpExpr) g).getOp()){
				case MINUS :	
						return(ctx.mkUnaryMinus((ArithExpr)addExpr(ctx,((UnOpExpr) g).getExpr(),vardecl)));
							
				case NEGATE :
						return(ctx.mkNot((BoolExpr)addExpr(ctx,((UnOpExpr) g).getExpr(),vardecl)));
				
				}	
			}else{
				if (g instanceof BinOpExpr){
					switch(((BinOpExpr) g).getOp()){
					case ADD:
						ArithExpr add1 = (ArithExpr)addExpr(ctx,((BinOpExpr) g).getLeft(),vardecl);
						ArithExpr add2 = (ArithExpr)addExpr(ctx,((BinOpExpr) g).getRight(),vardecl);
						return(ctx.mkAdd(add1,add2));
					case MULT:
						ArithExpr mul1 = (ArithExpr)addExpr(ctx,((BinOpExpr) g).getLeft(),vardecl);
						ArithExpr mul2 = (ArithExpr)addExpr(ctx,((BinOpExpr) g).getRight(),vardecl);
						return(ctx.mkMul(mul1,mul2));
					case SUB:
						ArithExpr sub1 = (ArithExpr)addExpr(ctx,((BinOpExpr) g).getLeft(),vardecl);
						ArithExpr sub2 = (ArithExpr)addExpr(ctx,((BinOpExpr) g).getRight(),vardecl);
						return(ctx.mkSub(sub1,sub2));
					case DIV:
						ArithExpr div1 = (ArithExpr)addExpr(ctx,((BinOpExpr) g).getLeft(),vardecl);
						ArithExpr div2 = (ArithExpr)addExpr(ctx,((BinOpExpr) g).getRight(),vardecl);
						return(ctx.mkDiv(div1,div2));
					case MOD:
						IntExpr mod1 = (IntExpr)addExpr(ctx,((BinOpExpr) g).getLeft(),vardecl);
						IntExpr mod2 = (IntExpr)addExpr(ctx,((BinOpExpr) g).getRight(),vardecl);
						return(ctx.mkMod(mod1, mod2));
					case AND:
						BoolExpr and1 = (BoolExpr)addExpr(ctx,((BinOpExpr) g).getLeft(),vardecl);
						BoolExpr and2 = (BoolExpr)addExpr(ctx,((BinOpExpr) g).getRight(),vardecl);
						return(ctx.mkAnd(and1, and2));
					case OR:
						BoolExpr or1 = (BoolExpr)addExpr(ctx,((BinOpExpr) g).getLeft(),vardecl);
						BoolExpr or2 = (BoolExpr)addExpr(ctx,((BinOpExpr) g).getRight(),vardecl);
						return(ctx.mkOr(or1, or2));
					case XOR:
						BoolExpr xor1 = (BoolExpr)addExpr(ctx,((BinOpExpr) g).getLeft(),vardecl);
						BoolExpr xor2 = (BoolExpr)addExpr(ctx,((BinOpExpr) g).getRight(),vardecl);
						return(ctx.mkXor(xor1, xor2));
					case EQUALS:
						Expr expr1 = addExpr(ctx,((BinOpExpr) g).getLeft(),vardecl);
						Expr expr2 = addExpr(ctx, ((BinOpExpr) g).getRight(),vardecl);
						return(ctx.mkEq(expr1, expr2));
					case NOT_EQUALS:
						Expr nexpr1 = addExpr(ctx,((BinOpExpr) g).getLeft(),vardecl);
						Expr nexpr2 = addExpr(ctx, ((BinOpExpr) g).getRight(),vardecl);
						return(ctx.mkNot(ctx.mkEq(nexpr1, nexpr2)));
					case POWER:
						ArithExpr pow1 = (ArithExpr)addExpr(ctx,((BinOpExpr) g).getLeft(),vardecl);
						ArithExpr pow2= (ArithExpr)addExpr(ctx,((BinOpExpr) g).getRight(),vardecl);
						return(ctx.mkPower(pow1,pow2));
					case LESS_THAN:
						ArithExpr lt1 = (ArithExpr)addExpr(ctx,((BinOpExpr) g).getLeft(),vardecl);
						ArithExpr lt2 = (ArithExpr)addExpr(ctx,((BinOpExpr) g).getRight(),vardecl);
						return(ctx.mkLt(lt1,lt2));
					case GREATER_THAN:
						ArithExpr gt1 = (ArithExpr)addExpr(ctx,((BinOpExpr) g).getLeft(),vardecl);
						ArithExpr gt2 = (ArithExpr)addExpr(ctx,((BinOpExpr) g).getRight(),vardecl);
						return(ctx.mkGt(gt1,gt2));
					case GREATER_EQUALS:
						ArithExpr ge1 = (ArithExpr)addExpr(ctx,((BinOpExpr) g).getLeft(),vardecl);
						ArithExpr ge2 = (ArithExpr)addExpr(ctx,((BinOpExpr) g).getRight(),vardecl);
						return(ctx.mkGe(ge1,ge2));
					case LESS_EQUALS:
						ArithExpr le1 = (ArithExpr)addExpr(ctx,((BinOpExpr) g).getLeft(),vardecl);
						ArithExpr le2 = (ArithExpr)addExpr(ctx,((BinOpExpr) g).getRight(),vardecl);
						return(ctx.mkLe(le1,le2));
					}
				}else{
					return(null);
				}
			}
		}
		return null;
	}
	
	public static void addState(Solver s,Context ctx,List<Expression> m, Map<String,Expr> vardecl){
		for(Expression entry : m){
			s.add((BoolExpr)addExpr(ctx,entry,vardecl));
		}
	}
	
	private static boolean isNumeric(String s) {  
	    return s.matches("[-+]?\\d*\\.?\\d+");  
	}  
	

}
