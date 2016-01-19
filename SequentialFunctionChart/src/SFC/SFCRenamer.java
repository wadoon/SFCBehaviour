package SFC;

import java.util.*;

import BehaviourChecker.VariableDuo;
public final class SFCRenamer {

	public static void appendIdentifier(SFCA left,SFCA right,List<VariableDuo> input, List<VariableDuo> output){
		Map<String,String> left_map = new HashMap<String,String>();
		Map<String,String> right_map = new HashMap<String,String>();
		for(Variable x : left.getVariables()){
			left_map.put(x.getName(), x.getName()+"_left");
			x.setName(left_map.get(x.getName()));
		}
		for(Variable y : right.getVariables()){
			right_map.put(y.getName(), y.getName()+"_right");
			y.setName(right_map.get(y.getName()));
		}
		
		//for(VariableDuo duo_i : input){
		//	duo_i.getLeft().setName(left_map.get(duo_i.getLeft().getName()));
		//	duo_i.getRight().setName(right_map.get(duo_i.getRight().getName()));
	//	}
		
		//for(VariableDuo duo_o : output){
	//		duo_o.getLeft().setName(left_map.get(duo_o.getLeft().getName()));
		//	duo_o.getRight().setName(right_map.get(duo_o.getRight().getName()));
	//	}
	}
	
	
}
