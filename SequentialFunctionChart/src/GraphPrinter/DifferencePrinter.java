package GraphPrinter;

import Differences.*;
import SFC.Assignment;
import SFC.Expression;
import SFC.Step;
import SFC.Transition;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import BehaviourChecker.VariableDuo;

public class DifferencePrinter {
	
	public static void print(List<PointDifference> diff,String path){
		try {
		      File file = new File(path+"/"+"diff"+".txt");
		      file.createNewFile();
		      
		      List<String> arr = new ArrayList<String>();
		      
		      for(PointDifference x:diff){
		    	  if(x instanceof ActionDifference){
		    		  arr.add("Difference in Output during Step " + ((ActionDifference)x).getNode().getIdentifier());
		    		  arr.add("Different Output Variables:");
		    		  for(VariableDuo duo :((ActionDifference) x).getVariable()){
		    			  arr.add(duo.getLeft().getName() + "!=" + duo.getRight().getName());
		    		  }
		    		  arr.add("Condition for difference:");
		    		  arr.add(((ActionDifference)x).getPath_left().printExpr().replace('Åò', '@'));
		    		  arr.add(((ActionDifference)x).getPath_right().printExpr().replace('Åò', '@'));
		    		  for(Expression ex : x.getPremise()){
		    			  arr.add(ex.printExpr().replace('Åò', '@'));
		    		  }
		    	  }
		    	  if(x instanceof GuardDifference){
		    		 if(((GuardDifference) x).isUnsat()){
		    			 arr.add("Difference in Guards for Transition " + ((GuardDifference)x).getTg().getSourceId() + "->" + ((GuardDifference) x).getTg().getDestId());
		    			 arr.add(((GuardDifference) x).getTg().getGuardleft().printExpr().replace('Åò', '@')+ "and" + ((GuardDifference)x).getTg().getGuardright().printExpr().replace('Åò', '@') + "not fullfillable under premise:");
		    		 }else{
		    			 arr.add("Difference in Guards for Transition " + ((GuardDifference)x).getTg().getSourceId() + "->" + ((GuardDifference) x).getTg().getDestId());
		    			 arr.add(((GuardDifference) x).getTg().getGuardleft().printExpr().replace('Åò', '@')+ "and" + ((GuardDifference)x).getTg().getGuardright().printExpr().replace('Åò', '@') + "not equivalent under premise:");
		    		}
		    		  arr.add(((GuardDifference)x).getPath_left().printExpr().replace('Åò', '@'));
		    		  arr.add(((GuardDifference)x).getPath_right().printExpr().replace('Åò', '@'));
		    		  for(Expression ex : x.getPremise()){
		    			  arr.add(ex.printExpr().replace('Åò', '@'));
		    		  }
		    	  }
		    	  if(x instanceof MatchDifference){
		    		  arr.add("Match Difference");
		    	  }
		    	  arr.add("");
		      }
		      
		      FileWriter writer = new FileWriter(path+"/"+"diff.txt"); 
		      for(String str: arr) {
		        writer.write(str);
		        writer.write(System.lineSeparator());
		      }
		      writer.close();
		      
		      
	    	} catch (IOException e) {
		      e.printStackTrace();
		}
		
	}
}
