package GraphPrinter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import SFC.Expression;
import SFC.Variable;
import TSG.*;

public class TwinGraphPrinter {
	public static void print(TwinGraph tg, String path,String name){
		try {
		      File file = new File(path+"/"+name+".dot");
		      file.createNewFile();
		      
		      List<String> arr = new ArrayList<String>();
		      arr.add("digraph "+name+"{");
		      for(TwinNode ex: tg.getV()){
		    	  if(ex.getRight() != null){
		    		  if(ex.getLeft()!= null){
		    			  	arr.add(ex.getIdentifier().replace('%', '_').replace("//", "__") + "[shape = record,label =\"{" + ex.getLeft().getStep() + "||");
		    		  		for(Map.Entry<Variable, Expression> sig: ex.getLeft().getState().entrySet()){
		    		  			arr.add(sig.getKey().getName() + ":=" + sig.getValue().printExpr().replace('Åò', '@')+";\\n");
		    		  		}
		    		  		arr.add("}|{");
		    		  		arr.add(ex.getRight().getStep() + "||");
		    		  		for(Map.Entry<Variable, Expression> sig: ex.getRight().getState().entrySet()){
		    		  			arr.add(sig.getKey().getName() + ":=" + sig.getValue().printExpr().replace('Åò', '@')+";\\n");
		    		  		}
		    		  		arr.add("}\"]");
		    		  }else{
		    			  	arr.add(ex.getIdentifier().replace('%', '_').replace("//", "__") + "[shape = record,label =\"{" + "{Empty}" + "||");
		    		  		arr.add("}|{");
		    		  		arr.add(ex.getRight().getStep() + "||");
		    		  		for(Map.Entry<Variable, Expression> sig: ex.getRight().getState().entrySet()){
		    		  			arr.add(sig.getKey().getName() + ":=" + sig.getValue().printExpr().replace('Åò', '@')+";\\n");
		    		  		}
		    		  		arr.add("}\"]"); 
		    		  }
		    	  }else{
		    		  arr.add(ex.getIdentifier().replace('%', '_').replace("//", "__") + "[shape = record,label =\"{" + ex.getLeft().getStep() + "||");
	    		  		for(Map.Entry<Variable, Expression> sig: ex.getLeft().getState().entrySet()){
	    		  			arr.add(sig.getKey().getName() + ":=" + sig.getValue().printExpr().replace('Åò', '@')+";\\n");
	    		  		}
	    		  		arr.add("}|{");
	    		  		arr.add("{Empty}" + "||");
	    		  		arr.add("}\"]");
		    	  }
		      }
		      
		      for(TwinEdge t:tg.getE()){
		    	  arr.add(t.getSourceId().replace('%','_').replace("//", "__") + "->" + t.getDestId().replace('%','_').replace("//", "__") + "[label=\"" + t.getGuardleft().printExpr().replace('Åò', '@')+"//"+t.getGuardright().printExpr().replace('Åò', '@') +"\"]");
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
