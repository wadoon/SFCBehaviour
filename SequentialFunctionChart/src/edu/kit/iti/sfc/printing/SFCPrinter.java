package edu.kit.iti.sfc.printing;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import edu.kit.iti.sfc.ast.*;

public final class SFCPrinter {
	
	public static void print(SFCA sfca, String path){
		try {
		      File file = new File(path+"/"+sfca.getName()+".dot");
		      file.createNewFile();
		      
		      List<String> arr = new ArrayList<String>();
		      arr.add("digraph "+sfca.getName()+"{");
		      for(Step s: sfca.getSteps()){
		    	  arr.add(s.getName() + "[shape = record,label =\"{" + s.getName() + "||");
		    	  for(Assignment a: s.getActions()){
		    		  arr.add(a.getVar().getName() + ":=" + a.getExpr().printExpr()+ ";\\n");
		    	  }
		    	  arr.add("}\"]");
		      }
		      
		      for(Transition t:sfca.getTrans()){
		    	  arr.add(t.getSource() + "->" + t.getDestination() + "[label=\"" + t.getGuard().printExpr()+ "\"]");
		      }
		      arr.add("}");
		      FileWriter writer = new FileWriter(path+"/"+sfca.getName()+".dot"); 
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
