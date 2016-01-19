package Matcher;

import java.util.*;

import com.microsoft.z3.ArithExpr;
import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.Expr;
import com.microsoft.z3.IntExpr;
import com.microsoft.z3.Solver;
import com.microsoft.z3.Status;

import BehaviourChecker.VariableDuo;
import ExTree.ExecutionEdge;
import ExTree.ExecutionTree;
import SFC.BinOpExpr;
import SFC.BinOperator;
import SFC.Expression;
import SFC.LiteralExpr;
import SFC.UnOpExpr;

public class SMTTransitionMatcher implements TransitionMatcher{

	@Override
	public List<TransitionMatch> match(List<ExecutionEdge> lsucc, List<ExecutionEdge> rsucc, ExecutionTree lt,
			ExecutionTree rt) {
		List<TransitionMatch> result = new ArrayList<TransitionMatch>();
		for(ExecutionEdge l : lsucc){
			for(ExecutionEdge r : rsucc){
				if(matchStep(l,r)){
					result.add(new TransitionMatch(l, r, true,true,false));
				}
			}
		}
		
		for(ExecutionEdge l : lsucc){
			if(notmatched(l,result)){
				result.add(new TransitionMatch(l,null,true,true,true));
			}
		}
		for(ExecutionEdge r : rsucc){
			if(notmatched(r,result)){
				result.add(new TransitionMatch(null,r,true,true,true));
			}
		}
		
		return(result);
	}
	

	private boolean notmatched(ExecutionEdge l, List<TransitionMatch> t){
		boolean b = true;
		for(TransitionMatch tm : t){
			if(tm.getLeft()!= null){
			if(tm.getLeft().getDestPath().equals(l.getDestPath())){
				b = false;
			}}
			if(tm.getRight()!=null){
			if(tm.getRight().getDestPath().equals(l.getDestPath())){
				b = false;
			}}
		}
		
		return b;
	}
	
	private boolean matchStep(ExecutionEdge l, ExecutionEdge r){
		String[] left = l.getDestPath().split("%");
		String[] right = r.getDestPath().split("%");
		if(left[left.length-1].equals(right[right.length-1])){
			return true;
		}
		if(expr_sat(l.getGuard(),r.getGuard())){
			return true;
		}
		return false;
	}
	
	private boolean expr_sat(Expression l, Expression r){
		List<Expression> premise = new ArrayList<Expression>();
		HashMap<String, String> cfg = new HashMap<String, String>();
        cfg.put("model", "true");
        Context ctx = new Context(cfg);
        BoolExpr guard1 = (BoolExpr)addGuard(ctx,l);
        BoolExpr guard2 = (BoolExpr)addGuard(ctx,r);
        Solver s = ctx.mkSolver();
        s.add(ctx.mkAnd(guard1, guard2));
        Status status = s.check();
		for(BoolExpr bexpr : s.getAssertions()){
			System.out.println(bexpr.toString());
		}
        if(status.equals(Status.SATISFIABLE)){
        	return(true);
        }
        System.out.println("case");
        return(false);
	}
	
	public static Expr addGuard(Context ctx, Expression g){

		if (g instanceof LiteralExpr){
			switch(((LiteralExpr) g).getType()){
			case INT :
				if(isNumeric(((LiteralExpr) g).getLit())){
					return(ctx.mkInt(Integer.parseInt(((LiteralExpr) g).getLit())));
				}else{
					return(ctx.mkIntConst(((LiteralExpr) g).getLit().replace("_left", "").replace("_right", "")));
				}
			case BOOL:
				if(((LiteralExpr) g).getLit().equals("TRUE")){
					return(ctx.mkTrue());
				}else{
					if(((LiteralExpr) g).getLit().equals("FALSE")){
						return(ctx.mkFalse());
					}else{
						return(ctx.mkBoolConst(((LiteralExpr) g).getLit().replace("_left", "").replace("_right", "")));
					}
				}
			}
			
		}else{
			if (g instanceof UnOpExpr){
				switch(((UnOpExpr) g).getOp()){
				case MINUS :	
						return(ctx.mkUnaryMinus((ArithExpr)addGuard(ctx,((UnOpExpr) g).getExpr())));
							
				case NEGATE :
						return(ctx.mkNot((BoolExpr)addGuard(ctx,((UnOpExpr) g).getExpr())));
				
				}	
			}else{
				if (g instanceof BinOpExpr){
					switch(((BinOpExpr) g).getOp()){
					case ADD:
						ArithExpr add1 = (ArithExpr)addGuard(ctx,((BinOpExpr) g).getLeft());
						ArithExpr add2 = (ArithExpr)addGuard(ctx,((BinOpExpr) g).getRight());
						return(ctx.mkAdd(add1,add2));
					case MULT:
						ArithExpr mul1 = (ArithExpr)addGuard(ctx,((BinOpExpr) g).getLeft());
						ArithExpr mul2 = (ArithExpr)addGuard(ctx,((BinOpExpr) g).getRight());
						return(ctx.mkMul(mul1,mul2));
					case SUB:
						ArithExpr sub1 = (ArithExpr)addGuard(ctx,((BinOpExpr) g).getLeft());
						ArithExpr sub2 = (ArithExpr)addGuard(ctx,((BinOpExpr) g).getRight());
						return(ctx.mkSub(sub1,sub2));
					case DIV:
						ArithExpr div1 = (ArithExpr)addGuard(ctx,((BinOpExpr) g).getLeft());
						ArithExpr div2 = (ArithExpr)addGuard(ctx,((BinOpExpr) g).getRight());
						return(ctx.mkDiv(div1,div2));
					case MOD:
						IntExpr mod1 = (IntExpr)addGuard(ctx,((BinOpExpr) g).getLeft());
						IntExpr mod2 = (IntExpr)addGuard(ctx,((BinOpExpr) g).getRight());
						return(ctx.mkMod(mod1, mod2));
					case AND:
						BoolExpr and1 = (BoolExpr)addGuard(ctx,((BinOpExpr) g).getLeft());
						BoolExpr and2 = (BoolExpr)addGuard(ctx,((BinOpExpr) g).getRight());
						return(ctx.mkAnd(and1, and2));
					case OR:
						BoolExpr or1 = (BoolExpr)addGuard(ctx,((BinOpExpr) g).getLeft());
						BoolExpr or2 = (BoolExpr)addGuard(ctx,((BinOpExpr) g).getRight());
						return(ctx.mkOr(or1, or2));
					case XOR:
						BoolExpr xor1 = (BoolExpr)addGuard(ctx,((BinOpExpr) g).getLeft());
						BoolExpr xor2 = (BoolExpr)addGuard(ctx,((BinOpExpr) g).getRight());
						return(ctx.mkXor(xor1, xor2));
					case EQUALS:
						Expr expr1 = addGuard(ctx,((BinOpExpr) g).getLeft());
						Expr expr2 = addGuard(ctx, ((BinOpExpr) g).getRight());
						return(ctx.mkEq(expr1, expr2));
					case NOT_EQUALS:
						Expr nexpr1 = addGuard(ctx,((BinOpExpr) g).getLeft());
						Expr nexpr2 = addGuard(ctx, ((BinOpExpr) g).getRight());
						return(ctx.mkNot(ctx.mkEq(nexpr1, nexpr2)));
					case POWER:
						ArithExpr pow1 = (ArithExpr)addGuard(ctx,((BinOpExpr) g).getLeft());
						ArithExpr pow2= (ArithExpr)addGuard(ctx,((BinOpExpr) g).getRight());
						return(ctx.mkPower(pow1,pow2));
					case LESS_THAN:
						ArithExpr lt1 = (ArithExpr)addGuard(ctx,((BinOpExpr) g).getLeft());
						ArithExpr lt2 = (ArithExpr)addGuard(ctx,((BinOpExpr) g).getRight());
						return(ctx.mkLt(lt1,lt2));
					case GREATER_THAN:
						ArithExpr gt1 = (ArithExpr)addGuard(ctx,((BinOpExpr) g).getLeft());
						ArithExpr gt2 = (ArithExpr)addGuard(ctx,((BinOpExpr) g).getRight());
						return(ctx.mkGt(gt1,gt2));
					case GREATER_EQUALS:
						ArithExpr ge1 = (ArithExpr)addGuard(ctx,((BinOpExpr) g).getLeft());
						ArithExpr ge2 = (ArithExpr)addGuard(ctx,((BinOpExpr) g).getRight());
						return(ctx.mkGe(ge1,ge2));
					case LESS_EQUALS:
						ArithExpr le1 = (ArithExpr)addGuard(ctx,((BinOpExpr) g).getLeft());
						ArithExpr le2 = (ArithExpr)addGuard(ctx,((BinOpExpr) g).getRight());
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
