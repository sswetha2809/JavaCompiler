package compiler;

import java.util.Vector;

/**
 * The Class Lexer.
 */
public class Lexer {

	/** The text. */
	private String text;

	/** The tokens. */
	private Vector<Token> tokens;

	/** The Constant UNDERSCORE. */
	private static final char UNDERSCORE = '_';

	/** The Constant DOLLAR. */
	private static final char DOLLAR = '$';

	/** The Constant B. */
	private static final int B = 0;

	/** The Constant ZERO. */
	private static final int ZERO = 1;

	/** The Constant ONE. */
	private static final int ONE = 2;

	/** The Constant _2TO7. */
	private static final int _2TO7 = 3;

	/** The Constant X. */
	private static final int X = 4;

	/** The Constant _8OR9. */
	private static final int _8OR9 = 5;

	/** The Constant _ACDF. */
	private static final int _ACDF = 6;

	/** The Constant _GTOZ$_. */
	private static final int _GTOZ$_ = 7;

	/** The Constant DOT. */
	private static final int DOT = 8;

	/** The Constant E. */
	private static final int E = 9;

	/** The Constant PLUS_MINUS. */
	private static final int PLUS_MINUS = 10;

	/** The Constant SINGLE_QUOTE. */
	private static final int SINGLE_QUOTE = 11;

	/** The Constant BACK_SLASH. */
	private static final int BACK_SLASH = 12;

	/** The Constant DOUBLE_QUOTE. */
	private static final int DOUBLE_QUOTE = 13;

	/** The Constant OTHER. */
	private static final int OTHER = 14;

	/** The Constant DELIMITER. */
	private static final int DELIMITER = 15;

	/** The Constant STOP. */
	private static final int STOP = -2;

	/** The Constant zero_state. */
	private static final int zero_state = 1;

	/** The Constant bin_inter_1. */
	private static final int bin_inter_1 = 2;

	/** The Constant binary_state. */
	private static final int binary_state = 3;

	/** The Constant oct_state. */
	private static final int oct_state = 4;

	/** The Constant hex_inter_1. */
	private static final int hex_inter_1 = 5;

	/** The Constant hex_state. */
	private static final int hex_state = 6;

	/** The Constant int_state. */
	private static final int int_state = 7;

	/** The Constant id_state. */
	private static final int id_state = 8;

	/** The Constant dot_state. */
	private static final int dot_state = 9;

	/** The Constant flt_inter_1. */
	private static final int flt_inter_1 = 10;

	/** The Constant flt_inter_2. */
	private static final int flt_inter_2 = 11;

	/** The Constant float_state. */
	private static final int float_state = 12;

	/** The Constant SQ_state. */
	private static final int SQ_state = 13;

	/** The Constant chr_inter_1. */
	private static final int chr_inter_1 = 14;

	/** The Constant char_state. */
	private static final int char_state = 15;

	/** The Constant BS_state_1. */
	private static final int BS_state_1 = 16;

	/** The Constant str_inter_1. */
	private static final int str_inter_1 = 17;

	/** The Constant string_state. */
	private static final int string_state = 18;

	/** The Constant BS_state_2. */
	private static final int BS_state_2 = 19;

	/** The Constant dot_inter_1. */
	private static final int dot_inter_1 = 20;

	/** The Constant ERROR. */
	private static final int ERROR = 21;

	/** The Constant CERROR. */
	private static final int CERROR = 22;
	// states table; - to define lexer rules. A DFA is converted to this state
	// table
	/** The Constant stateTable. */
	private static final int[][] stateTable = {

			// b 0 1 2-7 X 8-9 acdf _GTOZ$_ . E/e +/- ' \ " other del
			{ id_state, zero_state, int_state, int_state, id_state, int_state,
					id_state, id_state, dot_inter_1, id_state, STOP, SQ_state,
					ERROR, str_inter_1, ERROR, STOP },
			{ bin_inter_1, oct_state, oct_state, oct_state, hex_inter_1, ERROR,
					ERROR, ERROR, dot_state, flt_inter_1, STOP, STOP, ERROR,
					STOP, ERROR, STOP },
			{ ERROR, binary_state, binary_state, ERROR, ERROR, ERROR, ERROR,
					ERROR, ERROR, ERROR, ERROR, STOP, ERROR, STOP, ERROR, STOP },
			{ ERROR, binary_state, binary_state, ERROR, ERROR, ERROR, ERROR,
					ERROR, ERROR, ERROR, STOP, STOP, ERROR, STOP, ERROR, STOP },
			{ ERROR, oct_state, oct_state, oct_state, ERROR, ERROR, ERROR,
					ERROR, ERROR, ERROR, STOP, STOP, ERROR, STOP, ERROR, STOP },
			{ hex_state, hex_state, hex_state, hex_state, ERROR, hex_state,
					hex_state, ERROR, ERROR, hex_state, ERROR, STOP, ERROR,
					STOP, ERROR, STOP },
			{ hex_state, hex_state, hex_state, hex_state, ERROR, hex_state,
					hex_state, ERROR, ERROR, hex_state, STOP, STOP, ERROR,
					STOP, ERROR, STOP },
			{ ERROR, int_state, int_state, int_state, ERROR, int_state, ERROR,
					ERROR, dot_state, flt_inter_1, STOP, STOP, ERROR, STOP,
					ERROR, STOP },
			{ id_state, id_state, id_state, id_state, id_state, id_state,
					id_state, id_state, ERROR, id_state, STOP, STOP, ERROR,
					STOP, ERROR, STOP },
			{ ERROR, dot_state, dot_state, dot_state, ERROR, dot_state, ERROR,
					ERROR, ERROR, flt_inter_1, STOP, STOP, ERROR, STOP, ERROR,
					STOP },
			{ ERROR, float_state, float_state, float_state, ERROR, float_state,
					ERROR, ERROR, ERROR, ERROR, flt_inter_2, STOP, ERROR, STOP,
					ERROR, STOP },
			{ ERROR, float_state, float_state, float_state, ERROR, float_state,
					ERROR, ERROR, ERROR, ERROR, ERROR, STOP, ERROR, STOP,
					ERROR, STOP },
			{ ERROR, float_state, float_state, float_state, ERROR, float_state,
					ERROR, ERROR, ERROR, ERROR, STOP, STOP, ERROR, STOP, ERROR,
					STOP },
			{ chr_inter_1, chr_inter_1, chr_inter_1, chr_inter_1, chr_inter_1,
					chr_inter_1, chr_inter_1, chr_inter_1, chr_inter_1,
					chr_inter_1, chr_inter_1, STOP, BS_state_1, STOP,
					chr_inter_1, chr_inter_1 },
			{ CERROR, CERROR, CERROR, CERROR, CERROR, CERROR, CERROR, CERROR,
					CERROR, CERROR, STOP, char_state, CERROR, STOP, CERROR,
					STOP },
			{ ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR,
					ERROR, ERROR, STOP, ERROR, STOP, ERROR, STOP },
			{ chr_inter_1, chr_inter_1, chr_inter_1, chr_inter_1, chr_inter_1,
					chr_inter_1, chr_inter_1, chr_inter_1, chr_inter_1,
					chr_inter_1, chr_inter_1, chr_inter_1, chr_inter_1,
					chr_inter_1, chr_inter_1, chr_inter_1 },
			{ str_inter_1, str_inter_1, str_inter_1, str_inter_1, str_inter_1,
					str_inter_1, str_inter_1, str_inter_1, str_inter_1,
					str_inter_1, str_inter_1, str_inter_1, BS_state_2,
					string_state, str_inter_1, str_inter_1 },
			{ ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR,
					ERROR, ERROR, STOP, ERROR, STOP, ERROR, STOP },
			{ str_inter_1, str_inter_1, str_inter_1, str_inter_1, str_inter_1,
					str_inter_1, str_inter_1, str_inter_1, str_inter_1,
					str_inter_1, str_inter_1, str_inter_1, str_inter_1,
					str_inter_1, str_inter_1, str_inter_1 },
			{ ERROR, dot_state, dot_state, dot_state, ERROR, dot_state, ERROR,
					ERROR, ERROR, ERROR, STOP, STOP, ERROR, STOP, ERROR, STOP },
			{ ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR,
					ERROR, STOP, STOP, ERROR, STOP, ERROR, STOP },
			{ CERROR, CERROR, CERROR, CERROR, CERROR, CERROR, CERROR, CERROR,
					CERROR, CERROR, STOP, STOP, CERROR, STOP, CERROR, STOP } };

	/**
	 * Instantiates a new lexer.
	 * 
	 * @param text
	 *            the text
	 */
	public Lexer(String text) {
		this.text = text;
	}

	/**
	 * Run.
	 */
	public void run() {
		tokens = new Vector<Token>();
		String line;
		int counterOfLines = 1;
		// split lines
		do {
			int eolAt = text.indexOf("\n");
			if (eolAt >= 0) {
				line = text.substring(0, eolAt);
				if (text.length() > 0)
					text = text.substring(eolAt + 1);
			} else {
				line = text;
				text = "";
			}
			splitLine(counterOfLines, line);
			counterOfLines++;
		} while (!text.equals(""));
	}

	/**
	 * Split line.
	 * 
	 * @param row
	 *            the row
	 * @param line
	 *            the line
	 */
	private void splitLine(int row, String line) {
		int state = 0;
		int index = 0;
		char currentChar;
		String string = "";
		if (line.equals(""))
			return;
		// DFA working
		int go;
		do {
			currentChar = line.charAt(index);
			go = calculateNextState(state, currentChar);
			if (go != STOP) {
				string = string + currentChar;
				state = go;

			}

			index++;
			if (state == string_state || state == char_state)
				go = STOP;
		} while (index < line.length() && go != STOP);

		// review final state
		if (state == binary_state) {
			tokens.add(new Token(string, "BINARY", row));
		} else if (state == oct_state) {
			tokens.add(new Token(string, "OCTAL", row));
		} else if (state == hex_state) {
			tokens.add(new Token(string, "HEXADECIMAL", row));
		} else if (state == int_state || state == zero_state) {
			tokens.add(new Token(string, "INTEGER", row));
		} else if (state == id_state) {
			if (isKeyword(string)) {
				tokens.add(new Token(string, "KEYWORD", row));
			} else {
				tokens.add(new Token(string, "IDENTIFIER", row));

			}
		} else if (state == float_state || state == dot_state) {
			tokens.add(new Token(string, "FLOAT", row));

		} else if (state == char_state) {
			tokens.add(new Token(string, "CHARACTER", row));

		} else if (state == string_state) {
			tokens.add(new Token(string, "STRING", row));

		}

		else if (state != CERROR) {

			if (!string.equals(""))
				tokens.add(new Token(string, "ERROR", row));

		}

		if ((state == zero_state || state == dot_state || state == hex_state
				|| state == float_state || state == id_state
				|| state == oct_state || state == int_state
				|| state == binary_state || state == ERROR)
				&& (currentChar == '"' || currentChar == '\'')
				&& state != CERROR) {
			index--;
		}

		if (state == CERROR) {
			if (currentChar == '\'') {
				tokens.add(new Token(string + currentChar, "ERROR", row));
			} else if (currentChar == '"') {
				tokens.add(new Token(string, "ERROR", row));
				index--;
			} else {
				tokens.add(new Token(string, "ERROR", row));

			}

		}
		// current char
		if (isDelimiter(currentChar)) {
			if (state != str_inter_1)
				tokens.add(new Token(currentChar + "", "DELIMITER", row));
		} else if (isOperator(currentChar) || currentChar == '+'
				|| currentChar == '-') {

			if (currentChar == '=') {
				currentChar = ' ';

				if (index < line.length()) {
					currentChar = line.charAt(index);
				}
				if (currentChar == '=') {
					tokens.add(new Token("==", "OPERATOR", row));
					index++;
				}

				else {
					tokens.add(new Token("=", "OPERATOR", row));

				}
			} else if (currentChar == '!') {
				currentChar = ' ';

				if (index < line.length()) {
					currentChar = line.charAt(index);
				}
				if (currentChar == '=') {
					tokens.add(new Token("!=", "OPERATOR", row));
					index++;
				}

				else {
					tokens.add(new Token("!", "OPERATOR", row));

				}
			} else {
				tokens.add(new Token(currentChar + "", "OPERATOR", row));

			}

		}
		// loop
		if (index < line.length())
			splitLine(row, line.substring(index));
	}

	/**
	 * Calculate next state.
	 * 
	 * @param state
	 *            the state
	 * @param currentChar
	 *            the current char
	 * @return the int
	 */
	private int calculateNextState(int state, char currentChar) {
		if (isSpace(currentChar) || isDelimiter(currentChar)
				|| isOperator(currentChar))
			return stateTable[state][DELIMITER];
		else if (currentChar == 'b' || currentChar == 'B')
			return stateTable[state][B];
		else if (currentChar == '0')
			return stateTable[state][ZERO];
		else if (currentChar == '1')
			return stateTable[state][ONE];
		else if (currentChar == 'x' || currentChar == 'X')
			return stateTable[state][X];
		else if (is2to7(currentChar))
			return stateTable[state][_2TO7];
		else if (is8or9(currentChar))
			return stateTable[state][_8OR9];
		else if (isACDEF(currentChar))
			return stateTable[state][_ACDF];
		else if (isGtoZ(currentChar) || currentChar == DOLLAR
				|| currentChar == UNDERSCORE)
			return stateTable[state][_GTOZ$_];
		else if (currentChar == 'e' || currentChar == 'E')
			return stateTable[state][E];
		else if (currentChar == '.')
			return stateTable[state][DOT];
		else if (currentChar == '+' || currentChar == '-')
			return stateTable[state][PLUS_MINUS];
		else if (currentChar == '\'')
			return stateTable[state][SINGLE_QUOTE];
		else if (currentChar == '\\')
			return stateTable[state][BACK_SLASH];
		else if (currentChar == '"')
			return stateTable[state][DOUBLE_QUOTE];

		return stateTable[state][OTHER];
	}

	/**
	 * Checks if is delimiter.
	 * 
	 * @param c
	 *            the c
	 * @return true, if is delimiter
	 */
	private boolean isDelimiter(char c) {
		char[] delimiters = { ':', ';', '}', '{', '[', ']', '(', ')', ',' };
		for (int x = 0; x < delimiters.length; x++) {
			if (c == delimiters[x])
				return true;
		}
		return false;
	}

	/**
	 * Checks if is operator.
	 * 
	 * @param o
	 *            the o
	 * @return true, if is operator
	 */
	private boolean isOperator(char o) {
		// == and != should be handled in splitLine
		char[] operators = { '*', '/', '<', '>', '=', '!', '&', '|' };
		for (int x = 0; x < operators.length; x++) {
			if (o == operators[x])
				return true;
		}
		return false;
	}

	/**
	 * Checks if is quotation mark.
	 * 
	 * @param o
	 *            the o
	 * @return true, if is quotation mark
	 */
	private boolean isQuotationMark(char o) {
		char[] quote = { '"', '\'' };
		for (int x = 0; x < quote.length; x++) {
			if (o == quote[x])
				return true;
		}
		return false;
	}

	/**
	 * Checks if is space.
	 * 
	 * @param o
	 *            the o
	 * @return true, if is space
	 */
	private boolean isSpace(char o) {
		if (o == ' ' || o == '\t')
			return true;
		return false;
	}

	/**
	 * Checks if is 2to7.
	 * 
	 * @param x
	 *            the x
	 * @return true, if is 2to7
	 */
	public boolean is2to7(char x) {
		char[] TWO_SEVEN = { '2', '3', '4', '5', '6', '7' };
		for (int i = 0; i < TWO_SEVEN.length; i++) {
			if (x == TWO_SEVEN[i]) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if is acdef.
	 * 
	 * @param x
	 *            the x
	 * @return true, if is acdef
	 */
	public boolean isACDEF(char x) {
		char[] A_CDF = { 'a', 'c', 'd', 'f', 'A', 'C', 'D', 'F' };

		for (int i = 0; i < A_CDF.length; i++) {
			if (x == A_CDF[i]) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if is 8or9.
	 * 
	 * @param x
	 *            the x
	 * @return true, if is 8or9
	 */
	public boolean is8or9(char x) {
		if (x == '8' || x == '9')
			return true;
		return false;
	}

	/**
	 * Checks if is g to z.
	 * 
	 * @param x
	 *            the x
	 * @return true, if is gto z
	 */
	public boolean isGtoZ(char x) {
		char[] GTOZ = { 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q',
				'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'G', 'H', 'I',
				'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U',
				'V', 'W', 'X', 'Y', 'Z' };
		for (int i = 0; i < GTOZ.length; i++) {
			if (x == GTOZ[i]) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if is keyword.
	 * 
	 * @param word
	 *            the word
	 * @return true, if is keyword
	 */
	public boolean isKeyword(String word) {
		String[] KEYWORD = { "if", "else", "while", "switch", "case", "return",
				"int", "float", "void", "char", "string", "boolean", "true",
				"false", "print", "default", "break" };
		for (int i = 0; i < KEYWORD.length; i++) {
			if (word.equals(KEYWORD[i])) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Gets the tokens.
	 * 
	 * @return the tokens
	 */
	public Vector<Token> getTokens() {
		return tokens;
	}

}
