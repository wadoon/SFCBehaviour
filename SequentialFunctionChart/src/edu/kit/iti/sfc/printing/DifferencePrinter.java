package edu.kit.iti.sfc.printing;

import edu.kit.iti.sfc.ast.Assignment;
import edu.kit.iti.sfc.ast.Expression;
import edu.kit.iti.sfc.ast.Step;
import edu.kit.iti.sfc.ast.Transition;
import edu.kit.iti.sfc.behaviourcheck.VariableDuo;
import edu.kit.iti.sfc.diff.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class DifferencePrinter {

	private static final char CHAR_A = 0;
	private static final char CHAR_B = 0;
	private static final char CHAR_G = 0;
	private static final char CHAR_C = 0;
	private static final char CHAR_D = 0;
	private static final char CHAR_X = 0;
	private static final char CHAR_Y = 0;
	private static final char CHAR_Z = 0;
	private static final char CHAR_F = 0;
	private static final char CHAR_E = 0;

	public static void print(List<PointDifference> diff, String path) {
		try {
			File file = new File(path + "/" + "diff" + ".txt");
			file.createNewFile();

			List<String> arr = new ArrayList<String>();

			for (PointDifference x : diff) {
				if (x instanceof ActionDifference) {
					arr.add("Difference in Output during Step " + ((ActionDifference) x).getNode().getIdentifier());
					arr.add("Different Output Variables:");
					for (VariableDuo duo : ((ActionDifference) x).getVariable()) {
						arr.add(duo.getLeft().getName() + "!=" + duo.getRight().getName());
					}
					arr.add("Condition for difference:");
					arr.add(((ActionDifference) x).getPath_left().printExpr().replace(CHAR_X, '@'));
					arr.add(((ActionDifference) x).getPath_right().printExpr().replace(CHAR_Y, '@'));
					for (Expression ex : x.getPremise()) {
						arr.add(ex.printExpr().replace(CHAR_Z, '@'));
					}
				}
				if (x instanceof GuardDifference) {
					if (((GuardDifference) x).isUnsat()) {
						arr.add("Difference in Guards for Transition " + ((GuardDifference) x).getTg().getSourceId()
								+ "->" + ((GuardDifference) x).getTg().getDestId());
						arr.add(((GuardDifference) x).getTg().getGuardleft().printExpr().replace(CHAR_A, '@') + "and"
								+ ((GuardDifference) x).getTg().getGuardright().printExpr().replace(CHAR_F, '@')
								+ "not fullfillable under premise:");
					} else {
						arr.add("Difference in Guards for Transition " + ((GuardDifference) x).getTg().getSourceId()
								+ "->" + ((GuardDifference) x).getTg().getDestId());
						arr.add(((GuardDifference) x).getTg().getGuardleft().printExpr().replace(CHAR_B, '@') + "and"
								+ ((GuardDifference) x).getTg().getGuardright().printExpr().replace(CHAR_G, '@')
								+ "not equivalent under premise:");
					}
					arr.add(((GuardDifference) x).getPath_left().printExpr().replace(CHAR_C, '@'));
					arr.add(((GuardDifference) x).getPath_right().printExpr().replace(CHAR_D, '@'));
					for (Expression ex : x.getPremise()) {
						arr.add(ex.printExpr().replace(CHAR_E, '@'));
					}
				}
				if (x instanceof MatchDifference) {
					arr.add("Match Difference");
				}
				arr.add("");
			}

			FileWriter writer = new FileWriter(path + "/" + "diff.txt");
			for (String str : arr) {
				writer.write(str);
				writer.write(System.lineSeparator());
			}
			writer.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
