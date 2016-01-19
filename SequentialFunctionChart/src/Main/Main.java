package Main;

import java.io.IOException;

import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;

import java.util.*;
import BehaviourChecker.*;
import Differences.PointDifference;
import ExTree.*;
import GraphPrinter.DifferencePrinter;
import GraphPrinter.ExTreePrinter;
import GraphPrinter.SFCPrinter;
import GraphPrinter.TwinGraphPrinter;
import Matcher.*;
import SFC.*;
import TSG.*;
import antlr4.sfcaLexer;
import antlr4.sfcaParser;

public class Main {
	
	public static void main(String[] args) throws IOException {
		String filename = "ExampleSFC/example.sfc";
		String filename2 ="ExampleSFC/example2.sfc";
		String path = "ExampleSFC";
		int bound = 10;
		if (args.length == 4) {
			System.out.println("loading");
			filename = args[0];
			filename2 = args[1];
			bound = Integer.parseInt(args[2]);
			path = args[3];
		}

		CharStream stream = new ANTLRFileStream(filename);
		sfcaLexer lexer = new sfcaLexer(stream);
		sfcaParser parser = new sfcaParser(new CommonTokenStream(lexer));
		
		sfcaParser.Start_sfcContext sfccontext = parser.start_sfc();
		SFCA a = sfccontext.ast;
		
		stream = new ANTLRFileStream(filename2);
		lexer = new sfcaLexer(stream);
		parser = new sfcaParser(new CommonTokenStream(lexer));
		
		sfccontext = parser.start_sfc();
		SFCA b = sfccontext.ast; 
		
		//
		a.getSteps().get(0).setInit(true);
		b.getSteps().get(0).setInit(true);
		//
		
		//
		SFCPrinter.print(a,path);
		SFCPrinter.print(b,path);
		//
		List<VariableDuo> input = new ArrayList<VariableDuo>();
		List<VariableDuo> output = new ArrayList<VariableDuo>();
		for(Variable in : a.getInput()){
			for(Variable in2 : b.getInput()){
				if(in.getName().equals(in2.getName())){
					input.add(new VariableDuo(in,in2));
				}
			}
		}
		for(Variable out : a.getOutput()){
			for(Variable out2 : b.getOutput()){
				if(out.getName().equals(out2.getName())){
					output.add(new VariableDuo(out,out2));
				}
			}
		}
		
		SFCRenamer.appendIdentifier(a, b, null, null);
		
		ExecutionTree ex_a = SymEx.executeSFC(a, bound);
		ExecutionTree ex_b = SymEx.executeSFC(b, bound);
		
		ExTreePrinter.print(ex_a, path, a.getName()+"_tree");
		ExTreePrinter.print(ex_b, path, b.getName()+"_tree");
		GraphMatcher gm = new GraphMatcher(new SMTTransitionMatcher());
		TwinGraph tg = gm.matchGraph(ex_a, ex_b);
		
		TwinGraphPrinter.print(tg, path, "Twingraph");
	
		BehaviourChecker bc = new BehaviourChecker(tg,a,b,output,input);
		
		List<PointDifference> result = bc.check();
		
		if (result.size() == 0){
			System.out.println("No Difference in Behaviour found");
		}else{
			System.out.println(result.size() + " different behaving point(s) found");
			DifferencePrinter.print(result,path);
		}
	}
}
