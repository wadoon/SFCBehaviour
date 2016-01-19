package BehaviourChecker;

import TSG.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.Expr;
import com.microsoft.z3.Solver;
import com.microsoft.z3.Status;
import com.microsoft.z3.enumerations.Z3_ast_print_mode;

import Differences.*;
import SFC.*;

public class BehaviourChecker {
	
	private TwinGraph tg;
	private List<Expression> extraInformation;
	private SFCA sfcleft;
	private SFCA sfcright;
	private List<VariableDuo> sameOutput;
	private List<VariableDuo> sameInput;
	
	public BehaviourChecker(TwinGraph tg, SFCA sfcleft, SFCA sfcright, List<VariableDuo> sameOutput, List<VariableDuo> sameInput) {
		super();
		this.tg = tg;
		this.extraInformation = new ArrayList<Expression>();
		this.sfcleft = sfcleft;
		this.sfcright = sfcright;
		this.sameOutput = sameOutput;
		this.sameInput = sameInput;
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
	
	public List<VariableDuo> getSameOutput() {
		return sameOutput;
	}
	public void setSameOutput(List<VariableDuo> sameOutput) {
		this.sameOutput = sameOutput;
	}
	public List<VariableDuo> getSameInput() {
		return sameInput;
	}
	public void setSameInput(List<VariableDuo> sameInput) {
		this.sameInput = sameInput;
	}
	public List<PointDifference> check(){
		List<String> arr = new ArrayList<String>();
		boolean equiv = false;
		Queue<CheckPoint> queue = new LinkedList<CheckPoint>();
		List<PointDifference> result = new ArrayList<PointDifference>();
		queue.add(new CheckPoint(tg.getRoot(),extraInformation,new LiteralExpr("TRUE",Vartype.BOOL),new LiteralExpr("TRUE",Vartype.BOOL),equiv));
		HashMap<String, String> cfg = new HashMap<String, String>();
        cfg.put("model", "true");
        Context ctx = new Context(cfg);
        ctx.setPrintMode(Z3_ast_print_mode.Z3_PRINT_SMTLIB2_COMPLIANT);
        Map<String,Expr> vardecl_left = SMTUtil.addVariables(ctx, sfcleft.getVariables(), 0,new HashMap<String,Expr>());
		Map<String,Expr> vardecl_right =SMTUtil.addVariables(ctx, sfcright.getVariables(), 0,new HashMap<String,Expr>());
		while(!queue.isEmpty()){
            boolean cont = true;
            equiv = equiv || false;
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
					vardecl_left = SMTUtil.addVariables(ctx, sfcleft.getVariables(), kleft,vardecl_left);
					vardecl_right =SMTUtil.addVariables(ctx, sfcright.getVariables(), kright,vardecl_right);
					Map<String,Expr> vardecl_both = new HashMap<String,Expr>();
					vardecl_both.putAll(vardecl_left);
					vardecl_both.putAll(vardecl_right);
				
					Solver s = ctx.mkSolver();
					addInputEquality(premise,kleft,kright);
					SMTUtil.addState(s, ctx, premise,vardecl_both);
					s.push();
					List<VariableDuo> diff_var = new ArrayList<VariableDuo>();
					for(VariableDuo xy : sameOutput){
						s.push();
						s.add(ctx.mkEq(vardecl_left.get(xy.getLeft().getName()),SMTUtil.addExpr(ctx, twinnode.getLeft().getState().get(xy.getLeft()), vardecl_left)));
						s.add(ctx.mkEq(vardecl_right.get(xy.getRight().getName()),SMTUtil.addExpr(ctx, twinnode.getRight().getState().get(xy.getRight()), vardecl_right)));
						s.add(ctx.mkNot(ctx.mkEq(vardecl_left.get(xy.getLeft().getName()), vardecl_right.get(xy.getRight().getName()))));
						Status status = s.check();
						
						arr.add("Assertion for variable check " + xy.getLeft().getName() + " and " + xy.getRight().getName());
						for(BoolExpr bexpr : s.getAssertions()){
							arr.add(bexpr.toString().replace("Åò", "@"));
						}
						if(status.equals(Status.SATISFIABLE)){
							diff_var.add(xy);
						}else{
							if(status.equals(Status.UNSATISFIABLE)){
								
							}
						}
						s.pop();
					}
					if(diff_var.size() > 0){
						result.add(new ActionDifference(twinnode,cp.getPathexpr_left(),cp.getPathexpr_right(),premise,diff_var));
					}
					if(cont){
						for(TwinEdge edge : tg.getSuccessor(twinnode)){
							if(!(edge.getGuardleft() instanceof EmptyExpr)){
								if(!(edge.getGuardright() instanceof EmptyExpr)){						
									s.push();
									s.add(ctx.mkEq(SMTUtil.addExpr(ctx, edge.getGuardleft(), vardecl_left), SMTUtil.addExpr(ctx, edge.getGuardright(), vardecl_right)));
									Status status = s.check();
									
									arr.add("Assertion for first guard check " + edge.getGuardleft().printExpr() + " and " + edge.getGuardright().printExpr());
									for(BoolExpr bexpr : s.getAssertions()){
										arr.add(bexpr.toString().replace("Åò", "@"));
									}
									if(status.equals(Status.UNSATISFIABLE)){
										result.add(new GuardDifference(twinnode,cp.getPathexpr_left(),cp.getPathexpr_right(),premise,edge,true));
									}
									s.pop();
									s.push();
									s.add(ctx.mkNot(ctx.mkEq(SMTUtil.addExpr(ctx, edge.getGuardleft(), vardecl_left), SMTUtil.addExpr(ctx, edge.getGuardright(), vardecl_right))));
									status = s.check();
									
									arr.add("Assertion for second guard check " + edge.getGuardleft().printExpr() + " and " + edge.getGuardright().printExpr());
									for(BoolExpr bexpr : s.getAssertions()){
										arr.add(bexpr.toString().replace("Åò", "@"));
									}
									if(status.equals(Status.SATISFIABLE)){
										result.add(new GuardDifference(twinnode,cp.getPathexpr_left(),cp.getPathexpr_right(),premise,edge,false));
									}else{
										if(status.equals(Status.UNSATISFIABLE)){
											queue.add(new CheckPoint(tg.getSingleNode(edge.getDestId()),premise,new BinOpExpr(cp.getPathexpr_left(),BinOperator.AND,edge.getGuardleft()),new BinOpExpr(cp.getPathexpr_right(),BinOperator.AND,edge.getGuardright()),equiv));
										}
									}
									s.pop();
								}else{
									result.add(new MatchDifference(twinnode,cp.getPathexpr_left(),cp.getPathexpr_right(),premise,false,tg.getSingleNode(edge.getDestId())));
									//queue.add(new CheckPoint(tg.getSingleNode(edge.getDestId()),premise,new BinOpExpr(cp.getPathexpr_left(),BinOperator.AND,edge.getGuardleft()),cp.getPathexpr_right(),equiv));
								}
							}else{
								result.add(new MatchDifference(twinnode,cp.getPathexpr_left(),cp.getPathexpr_right(),premise,false,tg.getSingleNode(edge.getDestId())));
								//queue.add(new CheckPoint(tg.getSingleNode(edge.getDestId()),premise,cp.getPathexpr_left(),new BinOpExpr(cp.getPathexpr_right(),BinOperator.EQUALS,edge.getGuardright()),equiv));
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
		
		try {
		      File file = new File("SMTAsserts.txt");
		      file.createNewFile();
		      FileWriter writer = new FileWriter("SMTAsserts.txt"); 
		      for(String str: arr) {
		        writer.write(str);
		        writer.write(System.lineSeparator());
		      }
		      writer.close();
		      
		      
	    	} catch (IOException e) {
		      e.printStackTrace();
		}
		return(result);
	}	
	
	private void addInputEquality(List<Expression> premise,int k_left, int k_right){
		for(VariableDuo duo : sameInput){
			premise.add(new BinOpExpr(new LiteralExpr(duo.getLeft().getName()+"Åò"+k_left,duo.getLeft().getType()),BinOperator.EQUALS,new LiteralExpr(duo.getRight().getName()+"Åò"+k_right,duo.getRight().getType())));
		}
	}
	
}
