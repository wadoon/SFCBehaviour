package GraphPrinter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import SFC.*;
import ExTree.*;

public class ExTreePrinter {
	public static void print(ExecutionTree extree, String path,String name){
		try {
		      File file = new File(path+"/"+name+".dot");
		      file.createNewFile();
		      
		      List<String> arr = new ArrayList<String>();
		      arr.add("digraph "+name+"{");
		      for(ExecutionNode ex: extree.getV()){
		    	  arr.add(ex.getPath().replace('%', '_') + "[shape = record,label =\"{" + ex.getStep() + "||");
		    	  for(Map.Entry<Variable, Expression> sig: ex.getState().entrySet()){
		    		  arr.add(sig.getKey().getName() + ":=" + sig.getValue().printExpr().replace('Åò', '@')+";\\n");

		    	  }
		    	  arr.add("}\"]");
		      }
		      
		      for(ExecutionEdge t:extree.getE()){
		    	  arr.add(t.getSourcePath().replace('%','_') + "->" + t.getDestPath().replace('%','_') + "[label=\"" + t.getGuard().printExpr().replace('Åò', '@')+ "\"]");
		      }
		      arr.add("}");
		      FileWriter writer = new FileWriter(path+"/"+name+".dot"); 
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
