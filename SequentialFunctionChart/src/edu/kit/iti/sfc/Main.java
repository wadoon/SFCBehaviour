package edu.kit.iti.sfc;

import java.io.IOException;

import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;

import java.util.*;

import edu.kit.iti.sfc.ast.*;
import edu.kit.iti.sfc.behaviourcheck.*;
import edu.kit.iti.sfc.diff.PointDifference;
import edu.kit.iti.sfc.extree.*;
import edu.kit.iti.sfc.input.*;
import edu.kit.iti.sfc.matching.*;
import edu.kit.iti.sfc.printing.DifferencePrinter;
import edu.kit.iti.sfc.printing.ExTreePrinter;
import edu.kit.iti.sfc.printing.SFCPrinter;
import edu.kit.iti.sfc.printing.TwinGraphPrinter;
import edu.kit.iti.sfc.tsg.*;

public class Main {
	public static String run(String leftSideFilename, String rightSideFilename, String outputPath, int bound) throws IOException {
		CharStream stream = new ANTLRFileStream(leftSideFilename);
		SFCALexer lexer = new SFCALexer(stream);
		SFCAParser parser = new SFCAParser(new CommonTokenStream(lexer));

		SFCAParser.Start_sfcContext sfccontext = parser.start_sfc();
		SFCA a = sfccontext.ast;

		stream = new ANTLRFileStream(rightSideFilename);
		lexer = new SFCALexer(stream);
		parser = new SFCAParser(new CommonTokenStream(lexer));

		sfccontext = parser.start_sfc();
		SFCA b = sfccontext.ast;

		//
		a.getSteps().get(0).setInit(true);
		b.getSteps().get(0).setInit(true);
		//

		//
		SFCPrinter.print(a, outputPath);
		SFCPrinter.print(b, outputPath);
		//
		List<VariableDuo> input = new ArrayList<VariableDuo>();
		List<VariableDuo> output = new ArrayList<VariableDuo>();
		for (Variable in : a.getInput()) {
			for (Variable in2 : b.getInput()) {
				if (in.getName().equals(in2.getName())) {
					input.add(new VariableDuo(in, in2));
				}
			}
		}
		for (Variable out : a.getOutput()) {
			for (Variable out2 : b.getOutput()) {
				if (out.getName().equals(out2.getName())) {
					output.add(new VariableDuo(out, out2));
				}
			}
		}

		SFCRenamer.appendIdentifier(a, b, null, null);

		ExecutionTree ex_a = SymEx.executeSFC(a, bound);
		ExecutionTree ex_b = SymEx.executeSFC(b, bound);

		ExTreePrinter.print(ex_a, outputPath, a.getName() + "_tree");
		ExTreePrinter.print(ex_b, outputPath, b.getName() + "_tree");
		GraphMatcher gm = new GraphMatcher(new SMTTransitionMatcher());
		TwinGraph tg = gm.matchGraph(ex_a, ex_b);

		TwinGraphPrinter.print(tg, outputPath, "Twingraph");

		BehaviourChecker bc = new BehaviourChecker(tg, a, b, output, input);

		List<PointDifference> result = bc.check();

		if (result.size() == 0) {
			System.out.println("No Difference in Behaviour found");
		} else {
			System.out.println(result.size() + " different behaving point(s) found");
			DifferencePrinter.print(result, outputPath);
		}

		return "";
	}

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

		run(filename, filename2, path, bound);
	}
}
