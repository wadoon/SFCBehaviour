package ExTree;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import SFC.Assignment;
import SFC.BinOpExpr;
import SFC.Expression;
import SFC.LiteralExpr;
import SFC.SFCA;
import SFC.Step;
import SFC.Transition;
import SFC.UnOpExpr;
import SFC.Variable;
import SFC.VariableExpr;
import SFC.Vartype;

public final class SymEx {
	
	public static ExecutionTree executeSFC(SFCA sfca,int limit){
		int k = 0;
		List<ExecutionNode> vreturn = new LinkedList<ExecutionNode>();
		List<ExecutionEdge> ereturn = new LinkedList<ExecutionEdge>();
		
		Step s = sfca.initialStep();
		Map<Variable,Expression> prevstate = initiateState(sfca.getVariables());
		prevstate = symInput(prevstate,k,sfca.getInput());
		ExecutionNode v = new ExecutionNode(s.getName(),s.getName(),exSeqAssignment(prevstate,s.getActions()));
		v.setRoot(true);
		vreturn.add(v);

		Queue<ExecutionNode> queue = new LinkedList<ExecutionNode>();
		queue.add(v);
		
		while(!(queue.isEmpty()) && (k < limit)){	
			ExecutionNode n = queue.poll();
			
			String p = n.getPath();
			int counter = 0;
			for( int i=0; i<p.length(); i++ ) {
			    if( p.charAt(i) == '%' ) {
			        counter++;
			    } 
			}
			
			if(counter == k){
				k++;
			}

			prevstate = new HashMap<Variable,Expression>(n.getState());
			prevstate = symInput(prevstate,k,sfca.getInput());
			
			
			for(Transition t : sfca.getTrans()){
				if(t.getSource().equals(n.getStep())){
					v = new ExecutionNode(n.getPath()+"%"+t.getDestination(),t.getDestination(),exSeqAssignment(prevstate,sfca.getSingleStep(t.getDestination()).getActions()));
					ExecutionEdge e = new ExecutionEdge(n.getPath(),v.getPath(),insertState(t.getGuard(),prevstate));
					vreturn.add(v);
					ereturn.add(e);
					queue.add(v);
				}
			}
		
			
		}
		
		return(new ExecutionTree(vreturn,ereturn));
	}

	public static Map<Variable,Expression> initiateState(List<Variable> x){
		List<Variable> list = new LinkedList<Variable>(x);
		Map<Variable,Expression> r = new HashMap<Variable,Expression>();
		for(Variable v : list){
			if(v.getType().equals(Vartype.INT)){
				String i = "0";
				r.put(v, new LiteralExpr(i,Vartype.INT));
			}else{
				String b = "FALSE";
				r.put(v, new LiteralExpr(b,Vartype.BOOL));
			}
		}
		return r;
	} 
	
	public static Map<Variable,Expression> exSingleAssignment(Map<Variable,Expression> m, Assignment a){
	
		Assignment e = insertState(a,m);
		
		Map<Variable,Expression> r = new HashMap<Variable,Expression>(m);
		r.put(e.getVar(), e.getExpr());
		return(r);
		
	}
	
	public static Map<Variable,Expression> exSeqAssignment(Map<Variable,Expression> m, List<Assignment> a){
		Map<Variable,Expression> r = new HashMap<Variable,Expression>(m);
		for(Assignment as : a){
			r = exSingleAssignment(r,as);
		}
		return r;
		
	}
	
	public static Assignment insertState(Assignment a, Map<Variable,Expression> m){
		Expression r = a.getExpr();
		if(r instanceof LiteralExpr){
			return(a);
		}else{
			if(r instanceof VariableExpr){
				if(m.containsKey(((VariableExpr) r).getIdentifier())){
					return(new Assignment(a.getVar(), m.get(((VariableExpr) r).getIdentifier())));
				}else{
					//should never happen
					return(a);	
				}
			}else{
				if(r instanceof UnOpExpr){
					return(new Assignment(a.getVar(),ExpressionUtils.replace((UnOpExpr)r, m)));
				}else{
					return(new Assignment(a.getVar(),ExpressionUtils.replace((BinOpExpr)r, m)));
				}
			}
		}
	}
	
	public static Expression insertState(Expression e, Map<Variable,Expression> m){
		Expression r = e;
		if(r instanceof LiteralExpr){
			return(r);
		}else{
			if(r instanceof VariableExpr){
				if(m.containsKey(((VariableExpr) r).getIdentifier())){
					return(m.get(((VariableExpr) r).getIdentifier()));
				}else{
					//should never happen
					return(r);	
				}
			}else{
				if(r instanceof UnOpExpr){
					return(ExpressionUtils.replace((UnOpExpr)r, m));
				}else{
					return(ExpressionUtils.replace((BinOpExpr)r, m));
				}
			}
		}
	}
	
	public static Map<Variable,Expression> symInput(Map<Variable,Expression> m, int k, List<Variable> in){
		List<Variable> list = new LinkedList<Variable>(in);
		Map<Variable,Expression> r = new HashMap<Variable,Expression>(m);
		for(Variable v : list){
			r.put(new Variable(v.getName(),v.isIn(),v.isOut(),v.getType()), new LiteralExpr(v.getName() + "Åò" + k,v.getType()));
		}
		return r;
	}
}
