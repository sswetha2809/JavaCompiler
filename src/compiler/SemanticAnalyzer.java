package compiler;

import java.util.Hashtable;
import java.util.Stack;
import java.util.Vector;

/**
 * The Class SemanticAnalyzer.
 */
public class SemanticAnalyzer {

	/** The gui. */
	private static Gui gui;
	
	/** The Constant DT_INT. */
	private static final String DT_INT = "INTEGER";
	
	/** The Constant DT_FLOAT. */
	private static final String DT_FLOAT = "FLOAT";
	
	/** The Constant DT_STRING. */
	private static final String DT_STRING = "STRING";
	
	/** The Constant DT_BOOLEAN. */
	private static final String DT_BOOLEAN = "BOOLEAN";
	
	/** The Constant DT_ERROR. */
	private static final String DT_ERROR = "ERROR";
	
	/** The Constant DT_OK. */
	private static final String DT_OK = "OK";

	/** The Constant ARRAY_INT. */
	private static final Integer ARRAY_INT = 0;
	
	/** The Constant ARRAY_FLOAT. */
	private static final Integer ARRAY_FLOAT = 1;
	
	/** The Constant ARRAY_CHAR. */
	private static final Integer ARRAY_CHAR = 2;
	
	/** The Constant ARRAY_STRING. */
	private static final Integer ARRAY_STRING = 3;
	
	/** The Constant ARRAY_BOOLEAN. */
	private static final Integer ARRAY_BOOLEAN = 4;
	
	/** The Constant ARRAY_VOID. */
	private static final Integer ARRAY_VOID = 5;
	
	/** The Constant ARRAY_ERROR. */
	private static final Integer ARRAY_ERROR = 6;

	//  a data structure for the cube of types

	/** The Constant CUBE_BINARY_MINUS. */
	private static final String[][] CUBE_BINARY_MINUS = {
			{ DT_INT, DT_FLOAT, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR,
					DT_ERROR },
			{ DT_FLOAT, DT_FLOAT, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR,
					DT_ERROR },
			{ DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR,
					DT_ERROR },
			{ DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR,
					DT_ERROR },
			{ DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR,
					DT_ERROR },
			{ DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR,
					DT_ERROR },
			{ DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR,
					DT_ERROR }, };
	
	/** The Constant CUBE_MULTIPLICATION. */
	private static final String[][] CUBE_MULTIPLICATION = {
			{ DT_INT, DT_FLOAT, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR,
					DT_ERROR },
			{ DT_FLOAT, DT_FLOAT, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR,
					DT_ERROR },
			{ DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR,
					DT_ERROR },
			{ DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR,
					DT_ERROR },
			{ DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR,
					DT_ERROR },
			{ DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR,
					DT_ERROR },
			{ DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR,
					DT_ERROR }, };
	
	/** The Constant CUBE_DIVISION. */
	private static final String[][] CUBE_DIVISION = {
			{ DT_INT, DT_FLOAT, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR,
					DT_ERROR },
			{ DT_FLOAT, DT_FLOAT, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR,
					DT_ERROR },
			{ DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR,
					DT_ERROR },
			{ DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR,
					DT_ERROR },
			{ DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR,
					DT_ERROR },
			{ DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR,
					DT_ERROR },
			{ DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR,
					DT_ERROR }, };
	
	/** The Constant CUBE_PLUS. */
	private static final String[][] CUBE_PLUS = {
			{ DT_INT, DT_FLOAT, DT_ERROR, DT_STRING, DT_ERROR, DT_ERROR,
					DT_ERROR },
			{ DT_FLOAT, DT_FLOAT, DT_ERROR, DT_STRING, DT_ERROR, DT_ERROR,
					DT_ERROR },
			{ DT_ERROR, DT_ERROR, DT_ERROR, DT_STRING, DT_ERROR, DT_ERROR,
					DT_ERROR },
			{ DT_ERROR, DT_ERROR, DT_ERROR, DT_STRING, DT_ERROR, DT_ERROR,
					DT_ERROR },
			{ DT_STRING, DT_STRING, DT_STRING, DT_STRING, DT_STRING, DT_ERROR,
					DT_ERROR },
			{ DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR,
					DT_ERROR },
			{ DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR,
					DT_ERROR }, };
	
	/** The Constant CUBE_LESSER_THAN. */
	private static final String[][] CUBE_LESSER_THAN = {
			{ DT_BOOLEAN, DT_BOOLEAN, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR,
					DT_ERROR },
			{ DT_BOOLEAN, DT_BOOLEAN, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR,
					DT_ERROR },
			{ DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR,
					DT_ERROR },
			{ DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR,
					DT_ERROR },
			{ DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR,
					DT_ERROR },
			{ DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR,
					DT_ERROR },
			{ DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR,
					DT_ERROR }, };
	
	/** The Constant CUBE_GREATER_THAN. */
	private static final String[][] CUBE_GREATER_THAN = {
			{ DT_BOOLEAN, DT_BOOLEAN, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR,
					DT_ERROR },
			{ DT_BOOLEAN, DT_BOOLEAN, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR,
					DT_ERROR },
			{ DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR,
					DT_ERROR },
			{ DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR,
					DT_ERROR },
			{ DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR,
					DT_ERROR },
			{ DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR,
					DT_ERROR },
			{ DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR,
					DT_ERROR }, };
	
	/** The Constant CUBE_NOT_EQUALS. */
	private static final String[][] CUBE_NOT_EQUALS = {
			{ DT_BOOLEAN, DT_BOOLEAN, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR,
					DT_ERROR },
			{ DT_BOOLEAN, DT_BOOLEAN, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR,
					DT_ERROR },
			{ DT_ERROR, DT_ERROR, DT_BOOLEAN, DT_ERROR, DT_ERROR, DT_ERROR,
					DT_ERROR },
			{ DT_ERROR, DT_ERROR, DT_ERROR, DT_BOOLEAN, DT_ERROR, DT_ERROR,
					DT_ERROR },
			{ DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR, DT_BOOLEAN, DT_ERROR,
					DT_ERROR },
			{ DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR,
					DT_ERROR },
			{ DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR,
					DT_ERROR }, };
	
	/** The Constant CUBE_DOUBLE_EQUALS. */
	private static final String[][] CUBE_DOUBLE_EQUALS = {
			{ DT_BOOLEAN, DT_BOOLEAN, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR,
					DT_ERROR },
			{ DT_BOOLEAN, DT_BOOLEAN, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR,
					DT_ERROR },
			{ DT_ERROR, DT_ERROR, DT_BOOLEAN, DT_ERROR, DT_ERROR, DT_ERROR,
					DT_ERROR },
			{ DT_ERROR, DT_ERROR, DT_ERROR, DT_BOOLEAN, DT_ERROR, DT_ERROR,
					DT_ERROR },
			{ DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR, DT_BOOLEAN, DT_ERROR,
					DT_ERROR },
			{ DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR,
					DT_ERROR },
			{ DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR,
					DT_ERROR }, };
	
	/** The Constant CUBE_AND. */
	private static final String[][] CUBE_AND = {
			{ DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR,
					DT_ERROR },
			{ DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR,
					DT_ERROR },
			{ DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR,
					DT_ERROR },
			{ DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR,
					DT_ERROR },
			{ DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR, DT_BOOLEAN, DT_ERROR,
					DT_ERROR },
			{ DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR,
					DT_ERROR },
			{ DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR,
					DT_ERROR }, };
	
	/** The Constant CUBE_OR. */
	private static final String[][] CUBE_OR = {
			{ DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR,
					DT_ERROR },
			{ DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR,
					DT_ERROR },
			{ DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR,
					DT_ERROR },
			{ DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR,
					DT_ERROR },
			{ DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR, DT_BOOLEAN, DT_ERROR,
					DT_ERROR },
			{ DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR,
					DT_ERROR },
			{ DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR,
					DT_ERROR }, };

	/** The Constant CUBE_EQUALS. */
	private static final String[][] CUBE_EQUALS = {
			{ DT_OK, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR },
			{ DT_OK, DT_OK, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR },
			{ DT_ERROR, DT_ERROR, DT_OK, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR },
			{ DT_ERROR, DT_ERROR, DT_ERROR, DT_OK, DT_ERROR, DT_ERROR, DT_ERROR },
			{ DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR, DT_OK, DT_ERROR, DT_ERROR },
			{ DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR, DT_OK, DT_ERROR },
			{ DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR,
					DT_ERROR }, };

	/** The Constant CUBE_UNARY_NOT. */
	private static final String[][] CUBE_UNARY_NOT = { { DT_ERROR, DT_ERROR,
			DT_ERROR, DT_ERROR, DT_BOOLEAN, DT_ERROR, DT_ERROR }, };

	/** The Constant CUBE_UNARY_MINUS. */
	private static final String[][] CUBE_UNARY_MINUS = { { DT_INT, DT_FLOAT,
			DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR, DT_ERROR }, };
	
	/** The Constant dataTypeTable. */
	private static final Hashtable<String, String> dataTypeTable = new Hashtable<String, String>();

	/** The Constant cubeTable. */
	private static final Hashtable<String, String[][]> cubeTable = new Hashtable<String, String[][]>();
	
	/** The Constant symbolTable. */
	private static final Hashtable<String, Vector<SymbolTableItem>> symbolTable = new Hashtable<String, Vector<SymbolTableItem>>();
	
	/** The stack. */
	private static Stack stack = new Stack();

	static {
		stack = new Stack();
		cubeTable.put("&B", CUBE_AND);
		cubeTable.put("=B", CUBE_EQUALS);
		cubeTable.put("-B", CUBE_BINARY_MINUS);
		cubeTable.put("|B", CUBE_OR);
		cubeTable.put("/B", CUBE_DIVISION);
		cubeTable.put("==B", CUBE_DOUBLE_EQUALS);
		cubeTable.put(">B", CUBE_GREATER_THAN);
		cubeTable.put("<B", CUBE_LESSER_THAN);
		cubeTable.put("*B", CUBE_MULTIPLICATION);
		cubeTable.put("!=B", CUBE_NOT_EQUALS);
		cubeTable.put("+B", CUBE_PLUS);
		cubeTable.put("-U", CUBE_UNARY_MINUS);
		cubeTable.put("!U", CUBE_UNARY_NOT);
		dataTypeTable.put("int", "INTEGER");
		dataTypeTable.put("float", "FLOAT");
		dataTypeTable.put("string", "STRING");
		dataTypeTable.put("char", "CHARACTER");
		dataTypeTable.put("boolean", "BOOLEAN");
		dataTypeTable.put("void", "VOID");
		dataTypeTable.put("error", "ERROR");

	}

	/**
	 * Gets the symbol table.
	 *
	 * @return the symbol table
	 */
	public static Hashtable<String, Vector<SymbolTableItem>> getSymbolTable() {
		return symbolTable;
	}

	/**
	 * Gets the data type table.
	 *
	 * @return the data type table
	 */
	public static Hashtable<String, String> getDataTypeTable() {
		return dataTypeTable;
	}

	/**
	 * Check variable.
	 *
	 * @param type the type
	 * @param id the id
	 * @param lineNo the line no
	 */
	public static void checkVariable(String type, String id, int lineNo) {

		CodeGenerator.addVariable(type, id);

		if (!symbolTable.containsKey(id)) {
			Vector v = new Vector();
			v.add(new SymbolTableItem(type, "global", "0"));
			symbolTable.put(id, v);
		} else {
			error(gui, 1, lineNo, id);
		}

	}

	/**
	 * Push stack.
	 *
	 * @param type the type
	 */
	public static void pushStack(String type) {

		stack.push(type);
	}

	/**
	 * Pop stack.
	 *
	 * @return the string
	 */
	public static String popStack() {
		String result = "";
		if (stack.size() != 0)
			result = (String) stack.pop();

		return result;
	}

	/**
	 * Clear stack.
	 */
	public static void clearStack() {
		while (!stack.isEmpty()) {
			stack.pop();
		}
	}

	/**
	 * Calculate cube.
	 *
	 * @param type the type
	 * @param operator the operator
	 * @return the string
	 */
	public static String calculateCube(String type, String operator) {
		String result = "";

		String[][] cube = cubeTable.get(operator + "U");
		switch (type) {
		case "INTEGER":
			result = cube[0][ARRAY_INT];
			break;
		case "FLOAT":
			result = cube[0][ARRAY_FLOAT];
			break;
		case "CHARACTER":
			result = cube[0][ARRAY_CHAR];
			break;
		case "STRING":
			result = cube[0][ARRAY_STRING];
			break;
		case "BOOLEAN":
			result = cube[0][ARRAY_BOOLEAN];
			break;
		case "VOID":
			result = cube[0][ARRAY_VOID];
			break;
		case "ERROR":
			result = cube[0][ARRAY_ERROR];
			break;
		default:
			result = DT_ERROR;

		}
		return result;
	}

	/**
	 * Calculate cube.
	 *
	 * @param type1 the type1
	 * @param type2 the type2
	 * @param operator the operator
	 * @return the string
	 */
	public static String calculateCube(String type1, String type2,
			String operator) {
		String result = "";

		String[][] cube = cubeTable.get(operator + "B");

		int t1_index = 0;
		int t2_index = 0;
		switch (type1) {
		case "INTEGER":
			t1_index = ARRAY_INT;
			break;
		case "FLOAT":
			t1_index = ARRAY_FLOAT;
			break;
		case "CHARACTER":
			t1_index = ARRAY_CHAR;
			break;
		case "STRING":
			t1_index = ARRAY_STRING;
			break;
		case "BOOLEAN":
			t1_index = ARRAY_BOOLEAN;
			break;
		case "VOID":
			t1_index = ARRAY_VOID;
			break;
		case "error":
			t1_index = ARRAY_ERROR;
			break;
		default:
			t1_index = ARRAY_ERROR;
		}

		switch (type2) {
		case "INTEGER":
			t2_index = ARRAY_INT;
			break;
		case "FLOAT":
			t2_index = ARRAY_FLOAT;
			break;
		case "CHARACTER":
			t2_index = ARRAY_CHAR;
			break;
		case "STRING":
			t2_index = ARRAY_STRING;
			break;
		case "BOOLEAN":
			t2_index = ARRAY_BOOLEAN;
			break;
		case "VOID":
			t2_index = ARRAY_VOID;
			break;
		case "ERROR":
			t2_index = ARRAY_ERROR;
			break;
		default:
			t2_index = ARRAY_ERROR;

		}
		result = cube[t2_index][t1_index];
		return result;
	}

	/**
	 * Error.
	 *
	 * @param gui the gui
	 * @param err the err
	 * @param n the n
	 * @param id the id
	 */
	public static void error(Gui gui, int err, int n, String id) {
		switch (err) {
		case 1:
			gui.writeConsole("Line" + n + ": variable <" + id
					+ "> is already defined");
			break;
		case 2:
			gui.writeConsole("Line" + n + ": incompatible types: type mismatch");
			break;
		case 3:
			gui.writeConsole("Line" + n
					+ ": incompatible types: expected boolean");
			break;

		case 4:
			gui.writeConsole("Line" + n + ": variable <" + id + "> undefined");
			break;

		}
	}

	/**
	 * Initialize gui.
	 *
	 * @param gui1 the gui1
	 */
	public static void initializeGUI(Gui gui1) {
		gui = gui1;
	}

}
