package BehaviourChecker;

import TSG.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import com.microsoft.z3.Context;
import com.microsoft.z3.Expr;
import com.microsoft.z3.Solver;
import com.microsoft.z3.Status;

import SFC.*;

public class BehaviourChecker {
	
	private TwinGraph tg;
	private List<Expression> extraInformation;
	private SFCA sfcleft;
	private SFCA sfcright;
	private List<VariableDuo> sameOutput;
	
	public BehaviourChecker(TwinGraph tg, SFCA sfcleft, SFCA sfcright, List<VariableDuo> sameOutput) {
		super();
		this.tg = tg;
		this.extraInformation = new ArrayList<Expression>();
		this.sfcleft = sfcleft;
		this.sfcright = sfcright;
		this.sameOutput = sameOutput;
	}
	public TwinGraph getTg() {
		return tg;
	}
	public void setTg(TwinGraph tg) {
		this.tg = tg;
	}
	public List<Expression> getExtraInformation() {
		return extraInformation;
	}
	public void setExtraInformation(List<Expression> extraInformation) {
		this.extraInformation = extraInformation;
	}
	
	public SFCA getSfcleft() {
		return sfcleft;
	}

	public void setSfcleft(SFCA sfcleft) {
		this.sfcleft = sfcleft;
	}

	public SFCA getSfcright() {
		return sfcright;
	}

	public void setSfcright(SFCA sfcright) {
		this.sfcright = sfcright;
	}

	public void check(){
		Queue<CheckPoint> queue = new LinkedList<CheckPoint>();
		Queue<NodeDifference> differences = new LinkedList<NodeDifference>();
		queue.add(new CheckPoint(tg.getRoot(),extraInformation));
		HashMap<String, String> cfg = new HashMap<String, String>();
        cfg.put("model", "true");
        Context ctx = new Context(cfg);
        Map<String,Expr> vardecl_left = SMTUtil.addVariables(ctx, sfcleft.getVariables(), 0,new HashMap<String,Expr>());
		Map<String,Expr> vardecl_right =SMTUtil.addVariables(ctx, sfcright.getVariables(), 0,new HashMap<String,Expr>());
		while(!queue.isEmpty()){
            boolean cont = true;
            CheckPoint cp = queue.poll();
			TwinNode twinnode = cp.getNode();
			List<Expression>premise = cp.getPremise();
			if(twinnode.getLeft() != null){
				String pathleft = twinnode.getLeft().getPath();
				int kleft = 0;
				for( int i=0; i<pathleft.length(); i++ ) {
					if( pathleft.charAt(i) == '%' ) {
						kleft++;
					} 
				}
				if(twinnode.getRight() != null){
					String pathright = twinnode.getRight().getPath();
					int kright = 0;
					for( int i=0; i<pathright.length(); i++ ) {
						if( pathright.charAt(i) == '%' ) {
							kright++;
						} 
					}
					System.out.println(kleft);
					vardecl_left = SMTUtil.addVariables(ctx, sfcleft.getVariables(), kleft,vardecl_left);
					vardecl_right =SMTUtil.addVariables(ctx, sfcright.getVariables(), kright,vardecl_right);
					Map<String,Expr> vardecl_both = new HashMap<String,Expr>();
					vardecl_both.putAll(vardecl_left);
					vardecl_both.putAll(vardecl_right);
					
					Solver s = ctx.mkSolver();
					
					SMTUtil.addState(s, ctx, premise,vardecl_both);
					s.push();
					for(VariableDuo xy : sameOutput){
						s.push();
						s.add(ctx.mkEq(vardecl_left.get(xy.getLeft().getName()),SMTUtil.addExpr(ctx, twinnode.getLeft().getState().get(xy.getLeft()), vardecl_left)));
						s.add(ctx.mkEq(vardecl_right.get(xy.getRight().getName()),SMTUtil.addExpr(ctx, twinnode.getRight().getState().get(xy.getRight()), vardecl_right)));
						s.add(ctx.mkNot(ctx.mkEq(vardecl_left.get(xy.getLeft().getName()), vardecl_right.get(xy.getRight().getName()))));
						Status status = s.check();
						if(status.equals(Status.SATISFIABLE)){
							System.out.println("Variables "+ xy.getLeft().getName() +" and "+xy.getRight().getName()+" are different in "+twinnode.getIdentifier());
					//Do stuff
						}else{
							if(status.equals(Status.UNSATISFIABLE)){
								System.out.println("Variables "+ xy.getLeft().getName() +" and "+xy.getRight().getName()+" are identical in "+twinnode.getIdentifier());
							}
						}
						s.pop();
					}
					if(cont){
						for(TwinEdge edge : tg.getSuccessor(twinnode)){
							s.push();
							s.add(ctx.mkEq(SMTUtil.addExpr(ctx, edge.getGuardleft(), vardecl_left), SMTUtil.addExpr(ctx, edge.getGuardright(), vardecl_right)));
							Status status = s.check();
							if(status.equals(Status.UNSATISFIABLE)){
								System.out.println("Guard " + edge.getSourceId() + "->" + edge.getDestId() +" not Possible");
							}
							s.pop();
							s.add(ctx.mkNot(ctx.mkEq(SMTUtil.addExpr(ctx, edge.getGuardleft(), vardecl_left), SMTUtil.addExpr(ctx, edge.getGuardright(), vardecl_right))));
							status = s.check();
							if(status.equals(Status.SATISFIABLE)){
								System.out.println("Guard " + edge.getSourceId() + "->" + edge.getDestId() +" not Equivalent");
							}else{
								if(status.equals(Status.UNSATISFIABLE)){
									System.out.println("Guard " + edge.getSourceId() + "->" + edge.getDestId() +" Equivalent");
									
									queue.add(new CheckPoint(tg.getSingleNode(edge.getDestId()),premise));
								}
							}
						}
					}				
					s.pop();
				}else{
					System.out.println("No right step matched");
				}
			}else{
				if(twinnode.getRight()!= null){
					System.out.println("No left step matched");
				}
			}
		}
		ctx.dispose();
	}	
	
}
