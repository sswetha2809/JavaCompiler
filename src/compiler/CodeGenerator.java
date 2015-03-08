package compiler;

import java.util.Vector;


/**
 * The Class CodeGenerator.
 *
 * @author swetha
 */
public class CodeGenerator {

	/** The Constant variables. */
	private static final Vector<String> variables = new Vector<>();
	
	/** The Constant labels. */
	private static final Vector<String> labels = new Vector<>();
	
	/** The Constant instructions. */
	public static final Vector<String> instructions = new Vector<>();

	/**
	 * Adds the instruction.
	 *
	 * @param instruction the instruction
	 * @param p1 the p1
	 * @param p2 the p2
	 */
	public static void addInstruction(String instruction, String p1, String p2) {
		instructions.add(instruction + " " + p1 + ", " + p2);
	}

	/**
	 * Adds the label.
	 *
	 * @param name the name
	 * @param value the value
	 */
	public static void addLabel(String name, int value) {
		labels.add("#" + name + ", int, " + value);
	}

	/**
	 * Adds the variable.
	 *
	 * @param type the type
	 * @param name the name
	 */
	public static void addVariable(String type, String name) {
		variables.add(name + ", " + type + ", global, 0");
	}

	/**
	 * Write code.
	 *
	 * @param gui the gui
	 */
	public static void writeCode(Gui gui) {
		for (String variable : variables) {
			gui.writeCode(variable);
		}
		for (String label : labels) {
			gui.writeCode(label);
		}
		gui.writeCode("@");
		for (String instruction : instructions) {
			gui.writeCode(instruction);
		}

	}

	/**
	 * Clear.
	 *
	 * @param gui the gui
	 */
	public static void clear(Gui gui) {
		variables.clear();
		instructions.clear();
		labels.clear();
		Parser.labelCount=0;
	}
}
