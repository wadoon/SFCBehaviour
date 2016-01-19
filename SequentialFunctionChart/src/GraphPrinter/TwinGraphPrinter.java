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
	private static final char CHAR_Q = 0;
	private static final char CHAR_S = 0;
	private static final char CHAR_L = 0;
	private static final char CHAR_Z = 0;
	private static final char CHAR_O = 0;
	private static final char CHAR_P = 0;

	public static void print(TwinGraph tg, String path, String name) {
		try {
			File file = new File(path + "/" + name + ".dot");
			file.createNewFile();

			List<String> arr = new ArrayList<String>();
			arr.add("digraph " + name + "{");
			for (TwinNode ex : tg.getV()) {
				if (ex.getRight() != null) {
					if (ex.getLeft() != null) {
						arr.add(ex.getIdentifier().replace('%', '_').replace("//", "__") + "[shape = record,label =\"{"
								+ ex.getLeft().getStep() + "||");
						for (Map.Entry<Variable, Expression> sig : ex.getLeft().getState().entrySet()) {
							arr.add(sig.getKey().getName() + ":=" + sig.getValue().printExpr().replace(CHAR_Z, '@')
									+ ";\\n");
						}
						arr.add("}|{");
						arr.add(ex.getRight().getStep() + "||");
						for (Map.Entry<Variable, Expression> sig : ex.getRight().getState().entrySet()) {
							arr.add(sig.getKey().getName() + ":=" + sig.getValue().printExpr().replace(CHAR_O, '@')
									+ ";\\n");
						}
						arr.add("}\"]");
					} else {
						arr.add(ex.getIdentifier().replace('%', '_').replace("//", "__") + "[shape = record,label =\"{"
								+ "{Empty}" + "||");
						arr.add("}|{");
						arr.add(ex.getRight().getStep() + "||");
						for (Map.Entry<Variable, Expression> sig : ex.getRight().getState().entrySet()) {
							arr.add(sig.getKey().getName() + ":=" + sig.getValue().printExpr().replace(CHAR_L, '@')
									+ ";\\n");
						}
						arr.add("}\"]");
					}
				} else {
					arr.add(ex.getIdentifier().replace('%', '_').replace("//", "__") + "[shape = record,label =\"{"
							+ ex.getLeft().getStep() + "||");
					for (Map.Entry<Variable, Expression> sig : ex.getLeft().getState().entrySet()) {
						arr.add(sig.getKey().getName() + ":=" + sig.getValue().printExpr().replace(CHAR_P, '@')
								+ ";\\n");
					}
					arr.add("}|{");
					arr.add("{Empty}" + "||");
					arr.add("}\"]");
				}
			}

			for (TwinEdge t : tg.getE()) {
				arr.add(t.getSourceId().replace('%', '_').replace("//", "__") + "->"
						+ t.getDestId().replace('%', '_').replace("//", "__") + "[label=\""
						+ t.getGuardleft().printExpr().replace(CHAR_Q, '@') + "//"
						+ t.getGuardright().printExpr().replace(CHAR_S, '@') + "\"]");
			}
			arr.add("}");
			FileWriter writer = new FileWriter(path + "/" + name + ".dot");
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
