package compiler;

import java.util.Vector;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * The Class Parser. Defines various rules for parsing a program
 * 
 * @author swetha
 */
public class Parser {

	/** The root. */
	private static DefaultMutableTreeNode root;

	/** The tokens. */
	private static Vector<Token> tokens;

	/** The current token. */
	private static int currentToken;

	/** The gui. */
	private static Gui gui;

	/** The follow. */
	boolean first = false, follow = false;

	/** The label count. */
	public static int labelCount = 0;

	/**
	 * Run.
	 * 
	 * @param t
	 *            the t
	 * @param gui
	 *            the gui
	 * @return the default mutable tree node
	 */
	public static DefaultMutableTreeNode run(Vector<Token> t, Gui gui) {
		SemanticAnalyzer.initializeGUI(gui);

		Parser.gui = gui;
		tokens = t;
		currentToken = 0;
		root = new DefaultMutableTreeNode("program");

		rule_program(root);
		CodeGenerator.addInstruction("OPR", "0", "0");
		gui.writeSymbolTable(SemanticAnalyzer.getSymbolTable());
		SemanticAnalyzer.getSymbolTable().clear();
		SemanticAnalyzer.clearStack();
		CodeGenerator.writeCode(gui);
		CodeGenerator.clear(gui);

		return root;
	}

	/**
	 * Rule_program.
	 * 
	 * @param parent
	 *            the parent
	 */
	public static void rule_program(DefaultMutableTreeNode parent) {
		DefaultMutableTreeNode node;
		if ((currentToken < tokens.size())
				&& tokens.get(currentToken).getWord().equals("{")) {
			currentToken++;
			node = new DefaultMutableTreeNode("{");
			parent.add(node);

		} else
			error(1);
		node = new DefaultMutableTreeNode("body");
		parent.add(node);
		rule_body(node);
		if ((currentToken < tokens.size())
				&& tokens.get(currentToken).getWord().equals("}")) {
			currentToken++;
			node = new DefaultMutableTreeNode("}");
			parent.add(node);
		} else
			error(2);
	}

	/**
	 * Rule_body.
	 * 
	 * @param parent
	 *            the parent
	 */
	public static void rule_body(DefaultMutableTreeNode parent) {
		DefaultMutableTreeNode node;

		while ((currentToken < tokens.size())
				&& !tokens.get(currentToken).getWord().equals("}")) {
			if ((currentToken < tokens.size())
					&& tokens.get(currentToken).getToken().equals("IDENTIFIER")) {
				node = new DefaultMutableTreeNode("assignment");
				parent.add(node);

				if (SemanticAnalyzer.getSymbolTable().containsKey(
						tokens.get(currentToken).getWord())) {
					Vector<SymbolTableItem> item = SemanticAnalyzer
							.getSymbolTable().get(
									tokens.get(currentToken).getWord());
					String pushVariable = SemanticAnalyzer.getDataTypeTable()
							.get(item.get(0).getType());
					SemanticAnalyzer.pushStack(pushVariable);

				} else {
					SemanticAnalyzer.error(gui, 4, tokens.get(currentToken)
							.getLine(), tokens.get(currentToken).getWord());
					SemanticAnalyzer.pushStack("ERROR");
				}

				rule_assignment(node);
				if ((currentToken < tokens.size())
						&& tokens.get(currentToken).getWord().equals(";")) {
					currentToken++;
					node = new DefaultMutableTreeNode(";");
					parent.add(node);
				} else
					recovery(3);
			}

			else if ((currentToken < tokens.size())
					&& tokens.get(currentToken).getToken().equals("KEYWORD")) {
				if ((currentToken < tokens.size())
						&& tokens.get(currentToken).getWord().equals("print")) {
					node = new DefaultMutableTreeNode("print");
					parent.add(node);

					rule_print(node);
					if ((currentToken < tokens.size())
							&& tokens.get(currentToken).getWord().equals(";")) {
						currentToken++;
						node = new DefaultMutableTreeNode(";");
						parent.add(node);
					} else
						recovery(3);

				} else if ((currentToken < tokens.size())
						&& isDataType(tokens.get(currentToken).getWord())) {
					node = new DefaultMutableTreeNode("variable");
					parent.add(node);

					rule_variable(node);
					if ((currentToken < tokens.size())
							&& tokens.get(currentToken).getWord().equals(";")) {
						currentToken++;
						node = new DefaultMutableTreeNode(";");
						parent.add(node);
					} else
						recovery(3);

				}

				else if ((currentToken < tokens.size())
						&& tokens.get(currentToken).getWord().equals("return")) {
					rule_return(parent);
					if ((currentToken < tokens.size())
							&& tokens.get(currentToken).getWord().equals(";")) {
						currentToken++;
						node = new DefaultMutableTreeNode(";");
						parent.add(node);
					} else
						recovery(3);
				}

				else if ((currentToken < tokens.size())
						&& tokens.get(currentToken).getWord().equals("while")) {
					node = new DefaultMutableTreeNode("while");
					parent.add(node);
					rule_while(node);
				}

				else if ((currentToken < tokens.size())
						&& tokens.get(currentToken).getWord().equals("if")) {
					node = new DefaultMutableTreeNode("if");
					parent.add(node);
					rule_if(node);
				} else if ((currentToken < tokens.size())
						&& tokens.get(currentToken).getWord().equals("switch")) {
					node = new DefaultMutableTreeNode("switch");
					parent.add(node);
					rule_switch(node);
				}
			}

			else {
				error(4);
				recovery(4);

			}
		}
	}

	/**
	 * Rule_switch.
	 * 
	 * @param parent
	 *            the parent
	 */
	private static void rule_switch(DefaultMutableTreeNode parent) {

		String switchlabel = "L" + (++labelCount);
		String caseVariable = null;
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(tokens.get(
				currentToken).getWord());
		parent.add(node);
		boolean isIncr = incrementToken();
		if (isIncr && (currentToken < tokens.size())
				&& tokens.get(currentToken).getWord().equals("(")) {
			currentToken++;
			node = new DefaultMutableTreeNode("(");
			parent.add(node);
		} else
			error(8);
		if (currentToken < tokens.size()
				&& tokens.get(currentToken).getToken().equals("IDENTIFIER")) {

			if (SemanticAnalyzer.getSymbolTable().containsKey(
					tokens.get(currentToken).getWord())) {

				caseVariable = tokens.get(currentToken).getWord();

			} else {
				SemanticAnalyzer.error(gui, 4, tokens.get(currentToken)
						.getLine(), tokens.get(currentToken).getWord());
			}
			node = new DefaultMutableTreeNode("identifier" + "("
					+ tokens.get(currentToken).getWord() + ")");
			parent.add(node);
			isIncr = incrementToken();
		} else {
			error(6);
		}

		if (isIncr && (currentToken < tokens.size())
				&& tokens.get(currentToken).getWord().equals(")")) {
			currentToken++;
			node = new DefaultMutableTreeNode(")");
			parent.add(node);
		} else {
			error(7);
			currentToken++;
			isIncr = true;
		}
		if (isIncr && (currentToken < tokens.size())
				&& tokens.get(currentToken).getWord().equals("{")) {
			currentToken++;
			node = new DefaultMutableTreeNode("{");
			parent.add(node);
		} else {
			error(1);
		}

		rule_cases(parent, caseVariable, switchlabel);

		if (isIncr && (currentToken < tokens.size())
				&& tokens.get(currentToken).getWord().equals("default")) {
			node = new DefaultMutableTreeNode("default");
			parent.add(node);
			rule_default(node);
		}

		if (isIncr && (currentToken < tokens.size())
				&& tokens.get(currentToken).getWord().equals("}")) {
			currentToken++;
			node = new DefaultMutableTreeNode("}");
			parent.add(node);
			CodeGenerator.addLabel(switchlabel,
					CodeGenerator.instructions.size() + 1);

		} else {
			error(2);
		}

	}

	/**
	 * Rule_cases.
	 * 
	 * @param parent
	 *            the parent
	 * @param caseVariable
	 *            the case variable
	 * @param switchLabel
	 *            the switch label
	 */
	private static void rule_cases(DefaultMutableTreeNode parent,
			String caseVariable, String switchLabel) {
		boolean isCasePresent = false;
		while ((currentToken < tokens.size())
				&& tokens.get(currentToken).getWord().equals("case")) {
			isCasePresent = true;
			CodeGenerator.addInstruction("LOD", caseVariable, "0");
			DefaultMutableTreeNode caseNode = new DefaultMutableTreeNode("case");
			DefaultMutableTreeNode node;

			parent.add(caseNode);
			node = new DefaultMutableTreeNode("case");
			caseNode.add(node);
			incrementToken();

			if (currentToken < tokens.size()
					&& tokens.get(currentToken).getToken().equals("INTEGER")) {
				node = new DefaultMutableTreeNode("integer" + "("
						+ tokens.get(currentToken).getWord() + ")");
				caseNode.add(node);
				CodeGenerator.addInstruction("LIT", tokens.get(currentToken)
						.getWord(), "0");
			} else if (currentToken < tokens.size()
					&& tokens.get(currentToken).getToken().equals("FLOAT")) {
				node = new DefaultMutableTreeNode("float" + "("
						+ tokens.get(currentToken).getWord() + ")");
				caseNode.add(node);
				CodeGenerator.addInstruction("LIT", tokens.get(currentToken)
						.getWord(), "0");
			} else if (currentToken < tokens.size()
					&& tokens.get(currentToken).getToken().equals("OCTAL")) {
				node = new DefaultMutableTreeNode("octal" + "("
						+ tokens.get(currentToken).getWord() + ")");
				caseNode.add(node);
				CodeGenerator.addInstruction("LIT", tokens.get(currentToken)
						.getWord(), "0");
			} else if (currentToken < tokens.size()
					&& tokens.get(currentToken).getToken()
							.equals("HEXADECIMAL")) {
				node = new DefaultMutableTreeNode("hexadecimal" + "("
						+ tokens.get(currentToken).getWord() + ")");
				caseNode.add(node);
				CodeGenerator.addInstruction("LIT", tokens.get(currentToken)
						.getWord(), "0");
			} else if (currentToken < tokens.size()
					&& tokens.get(currentToken).getToken().equals("BINARY")) {
				node = new DefaultMutableTreeNode("binary" + "("
						+ tokens.get(currentToken).getWord() + ")");
				caseNode.add(node);
				CodeGenerator.addInstruction("LIT", tokens.get(currentToken)
						.getWord(), "0");
			} else {
				error(11);
			}

			boolean isIncr = incrementToken();
			if (isIncr && (currentToken < tokens.size())
					&& tokens.get(currentToken).getWord().equals(":")) {
				node = new DefaultMutableTreeNode(":");
				caseNode.add(node);
				currentToken++;
			} else {
				recovery(10);
			}

			CodeGenerator.addInstruction("OPR", "15", "0");
			String label = "L" + (++labelCount);
			CodeGenerator.addInstruction("JMC", "#" + label, "false");
			node = new DefaultMutableTreeNode("program");
			caseNode.add(node);
			rule_program(node);
			CodeGenerator
					.addLabel(label, CodeGenerator.instructions.size() + 2);
			CodeGenerator.addInstruction("JMP", "#" + switchLabel, "0");

		}

		if (!isCasePresent) {
			error(12);
		}
	}

	/**
	 * Rule_default.
	 * 
	 * @param parent
	 *            the parent
	 */
	private static void rule_default(DefaultMutableTreeNode parent) {
		DefaultMutableTreeNode node = new DefaultMutableTreeNode("default");
		parent.add(node);
		boolean isIncr = incrementToken();
		if (isIncr && (currentToken < tokens.size())
				&& tokens.get(currentToken).getWord().equals(":")) {
			node = new DefaultMutableTreeNode(":");
			parent.add(node);
			currentToken++;
		} else {
			recovery(10);
		}
		node = new DefaultMutableTreeNode("program");
		parent.add(node);
		rule_program(node);

	}

	/**
	 * Rule_print.
	 * 
	 * @param parent
	 *            the parent
	 */
	private static void rule_print(DefaultMutableTreeNode parent) {
		boolean error;
		DefaultMutableTreeNode node = new DefaultMutableTreeNode("print");
		parent.add(node);

		boolean isIncr = incrementToken();

		if (isIncr && (currentToken < tokens.size())
				&& tokens.get(currentToken).getWord().equals("(")) {
			isIncr = incrementToken();
			node = new DefaultMutableTreeNode("(");
			parent.add(node);
		} else
			error(8);
		node = new DefaultMutableTreeNode("expression");
		parent.add(node);
		if (isIncr) {
			error = rule_expression(node);
			if (error)
				error(9);
		} else
			error(9);

		if (isIncr && (currentToken < tokens.size())
				&& tokens.get(currentToken).getWord().equals(")")) {
			incrementToken();
			node = new DefaultMutableTreeNode(")");
			parent.add(node);
			CodeGenerator.addInstruction("OPR", "21", "0");

		} else
			error(7);
	}

	/**
	 * Rule_return.
	 * 
	 * @param parent
	 *            the parent
	 */
	private static void rule_return(DefaultMutableTreeNode parent) {
		DefaultMutableTreeNode node = new DefaultMutableTreeNode("return");
		parent.add(node);
		CodeGenerator.addInstruction("OPR", "1", "0");
		incrementToken();
	}

	/**
	 * Rule_if.
	 * 
	 * @param parent
	 *            the parent
	 */
	private static void rule_if(DefaultMutableTreeNode parent) {

		boolean error;
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(tokens.get(
				currentToken).getWord());
		parent.add(node);
		boolean isIncr = incrementToken();
		if (isIncr && (currentToken < tokens.size())
				&& tokens.get(currentToken).getWord().equals("(")) {
			currentToken++;
			node = new DefaultMutableTreeNode("(");
			parent.add(node);
		} else
			error(8);
		node = new DefaultMutableTreeNode("expression");
		parent.add(node);
		if (isIncr) {
			error = rule_expression(node);
			if (error)
				error(9);
		} else
			error(9);
		String x = SemanticAnalyzer.popStack();
		if (!x.equals("BOOLEAN")) {
			SemanticAnalyzer.error(gui, 3, tokens.get(currentToken).getLine(),
					null);
		}

		if (isIncr && (currentToken < tokens.size())
				&& tokens.get(currentToken).getWord().equals(")")) {
			currentToken++;
			node = new DefaultMutableTreeNode(")");
			parent.add(node);
		} else
			error(7);

		if (!isIncr) {
			currentToken++;
		}
		String label = "L" + (++labelCount);
		CodeGenerator.addInstruction("JMC", "#" + label, "false");
		node = new DefaultMutableTreeNode("program");
		parent.add(node);
		rule_program(node);

		if ((currentToken < tokens.size())
				&& tokens.get(currentToken).getWord().equals("else")) {
			node = new DefaultMutableTreeNode(tokens.get(currentToken)
					.getWord());
			parent.add(node);
			currentToken++;
			CodeGenerator
					.addLabel(label, CodeGenerator.instructions.size() + 2);

			label = "L" + (++labelCount);
			CodeGenerator.addInstruction("JMP", "#" + label, "0");
			node = new DefaultMutableTreeNode("program");
			parent.add(node);
			rule_program(node);
			CodeGenerator
					.addLabel(label, CodeGenerator.instructions.size() + 1);

		} else {
			CodeGenerator
					.addLabel(label, CodeGenerator.instructions.size() + 1);

		}

	}

	/**
	 * Rule_while.
	 * 
	 * @param parent
	 *            the parent
	 */
	private static void rule_while(DefaultMutableTreeNode parent) {
		boolean error;
		int whileNo = CodeGenerator.instructions.size() + 1;

		DefaultMutableTreeNode node = new DefaultMutableTreeNode(tokens.get(
				currentToken).getWord());
		parent.add(node);
		boolean isIncr = incrementToken();
		if (isIncr && (currentToken < tokens.size())
				&& tokens.get(currentToken).getWord().equals("(")) {
			isIncr = incrementToken();
			node = new DefaultMutableTreeNode("(");
			parent.add(node);
		} else
			error(8);

		node = new DefaultMutableTreeNode("expression");
		parent.add(node);
		if (isIncr) {
			error = rule_expression(node);
			if (error)
				error(9);
		} else
			error(9);

		String x = SemanticAnalyzer.popStack();
		if (!x.equals("BOOLEAN")) {
			SemanticAnalyzer.error(gui, 3, tokens.get(currentToken).getLine(),
					null);
		}
		if (isIncr && (currentToken < tokens.size())
				&& tokens.get(currentToken).getWord().equals(")")) {
			currentToken++;
			node = new DefaultMutableTreeNode(")");
			parent.add(node);
		} else {
			error(7);
		}

		if (!isIncr) {
			currentToken++;
		}
		String label = "L" + (++labelCount);
		CodeGenerator.addInstruction("JMC", "#" + label, "false");
		node = new DefaultMutableTreeNode("program");
		parent.add(node);
		rule_program(node);
		CodeGenerator.addLabel(label, CodeGenerator.instructions.size() + 2);
		label = "L" + (++labelCount);
		CodeGenerator.addInstruction("JMP", "#" + label, "0");
		CodeGenerator.addLabel(label, whileNo);

	}

	/**
	 * Rule_variable.
	 * 
	 * @param parent
	 *            the parent
	 */
	private static void rule_variable(DefaultMutableTreeNode parent) {
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(tokens.get(
				currentToken).getWord());
		parent.add(node);

		// currentToken++;
		boolean isIncr = incrementToken();
		if (isIncr && (currentToken < tokens.size())
				&& tokens.get(currentToken).getToken().equals("IDENTIFIER")) {
			SemanticAnalyzer.checkVariable(tokens.get(currentToken - 1)
					.getWord(), tokens.get(currentToken).getWord(),
					tokens.get(currentToken).getLine());

			node = new DefaultMutableTreeNode("identifier" + "("
					+ tokens.get(currentToken).getWord() + ")");
			parent.add(node);
			// currentToken++;
			incrementToken();
		} else
			error(6);

	}

	/**
	 * Rule_assignment.
	 * 
	 * @param parent
	 *            the parent
	 */
	private static void rule_assignment(DefaultMutableTreeNode parent) {
		boolean error;
		DefaultMutableTreeNode node = new DefaultMutableTreeNode("identifier"
				+ "(" + tokens.get(currentToken).getWord() + ")");
		parent.add(node);

		String identifier = tokens.get(currentToken).getWord();

		boolean isIncr = incrementToken();

		if (isIncr && (currentToken < tokens.size())
				&& tokens.get(currentToken).getWord().equals("=")) {
			node = new DefaultMutableTreeNode("=");
			parent.add(node);
			isIncr = incrementToken();
		} else {
			error(5);
		}
		node = new DefaultMutableTreeNode("expression");
		parent.add(node);
		if (isIncr) {
			error = rule_expression(node);
			String x = SemanticAnalyzer.popStack();
			String y = SemanticAnalyzer.popStack();
			System.out.println("x" + x + "y" + y);
			String result = SemanticAnalyzer.calculateCube(x, y, "=");
			System.out.println(result);
			if (!result.equals("OK")) {
				SemanticAnalyzer.error(gui, 2, tokens.get(currentToken)
						.getLine(), null);
			}

			if (error)
				error(9);

		} else
			error(9);
		CodeGenerator.addInstruction("STO", identifier, "0");

	}

	/**
	 * Rule_expression.
	 * 
	 * @param parent
	 *            the parent
	 * @return true, if successful
	 */
	private static boolean rule_expression(DefaultMutableTreeNode parent) {
		boolean error;
		boolean twiceHere = false;
		DefaultMutableTreeNode node = new DefaultMutableTreeNode("X");
		parent.add(node);
		error = rule_X(node);
		while (!error && currentToken < tokens.size()
				&& tokens.get(currentToken).getWord().equals("|")) {
			twiceHere = true;
			node = new DefaultMutableTreeNode("|");
			parent.add(node);
			incrementToken();
			node = new DefaultMutableTreeNode("X");
			parent.add(node);
			error = rule_X(node);
			if (twiceHere) {
				String x = SemanticAnalyzer.popStack();
				String y = SemanticAnalyzer.popStack();
				System.out.println("Expression x" + x + "y" + y);

				String result = SemanticAnalyzer.calculateCube(x, y, "&");
				SemanticAnalyzer.pushStack(result);
				CodeGenerator.addInstruction("OPR", "8", "0");
			}
		}

		return error;

	}

	/**
	 * Rule_ x.
	 * 
	 * @param parent
	 *            the parent
	 * @return true, if successful
	 */
	private static boolean rule_X(DefaultMutableTreeNode parent) {
		boolean error;
		boolean twiceHere = false;
		DefaultMutableTreeNode node = new DefaultMutableTreeNode("Y");
		parent.add(node);
		error = rule_Y(node);
		while (!error && currentToken < tokens.size()
				&& tokens.get(currentToken).getWord().equals("&")) {
			twiceHere = true;
			node = new DefaultMutableTreeNode("&");
			parent.add(node);
			incrementToken();
			node = new DefaultMutableTreeNode("Y");
			parent.add(node);
			error = rule_Y(node);
			if (twiceHere) {
				String x = SemanticAnalyzer.popStack();
				String y = SemanticAnalyzer.popStack();

				String result = SemanticAnalyzer.calculateCube(x, y, "&");
				SemanticAnalyzer.pushStack(result);
				CodeGenerator.addInstruction("OPR", "9", "0");
			}
		}

		return error;
	}

	/**
	 * Rule_ y.
	 * 
	 * @param parent
	 *            the parent
	 * @return true, if successful
	 */
	private static boolean rule_Y(DefaultMutableTreeNode parent) {
		boolean error;
		boolean operatorWasUsed = false;
		DefaultMutableTreeNode node;

		if (currentToken < tokens.size()
				&& tokens.get(currentToken).getWord().equals("!")) {
			operatorWasUsed = true;
			node = new DefaultMutableTreeNode("!");
			parent.add(node);
			incrementToken();

		}

		node = new DefaultMutableTreeNode("R");
		parent.add(node);
		error = rule_R(node);
		if (operatorWasUsed) {
			String x = SemanticAnalyzer.popStack();
			System.out.println("Y x" + x);

			String result = SemanticAnalyzer.calculateCube(x, "!");
			SemanticAnalyzer.pushStack(result);
			CodeGenerator.addInstruction("OPR", "10", "0");
		}
		return error;
	}

	/**
	 * Rule_ r.
	 * 
	 * @param parent
	 *            the parent
	 * @return true, if successful
	 */
	private static boolean rule_R(DefaultMutableTreeNode parent) {
		boolean error;
		boolean twiceHere = false;
		String passingOperator = "";
		DefaultMutableTreeNode node = new DefaultMutableTreeNode("E");
		parent.add(node);
		error = rule_E(node);

		while (!error
				&& ((currentToken < tokens.size()) && (tokens.get(currentToken)
						.getWord().equals(">")
						|| tokens.get(currentToken).getWord().equals("<")
						|| tokens.get(currentToken).getWord().equals("==") || tokens
						.get(currentToken).getWord().equals("!=")))) {
			twiceHere = true;
			passingOperator = tokens.get(currentToken).getWord();
			node = new DefaultMutableTreeNode(tokens.get(currentToken)
					.getWord());
			parent.add(node);
			incrementToken();
			node = new DefaultMutableTreeNode("E");
			parent.add(node);
			error = rule_E(node);

			if (twiceHere) {
				String x = SemanticAnalyzer.popStack();
				String y = SemanticAnalyzer.popStack();
				String result = SemanticAnalyzer.calculateCube(x, y,
						passingOperator);
				SemanticAnalyzer.pushStack(result);
				switch (passingOperator) {
				case ">":
					CodeGenerator.addInstruction("OPR", "11", "0");
					break;
				case "<":
					CodeGenerator.addInstruction("OPR", "12", "0");
					break;
				case "==":
					CodeGenerator.addInstruction("OPR", "15", "0");
					break;
				case "!=":
					CodeGenerator.addInstruction("OPR", "16", "0");
					break;
				}
			}
		}

		return error;

	}

	/**
	 * Rule_ e.
	 * 
	 * @param parent
	 *            the parent
	 * @return true, if successful
	 */
	private static boolean rule_E(DefaultMutableTreeNode parent) {
		boolean error;
		boolean twiceHere = false;
		String passingOperator = "";
		DefaultMutableTreeNode node;
		node = new DefaultMutableTreeNode("A");
		parent.add(node);
		error = rule_A(node);
		while (!error
				&& (currentToken < tokens.size()
						&& tokens.get(currentToken).getWord().equals("+") || currentToken < tokens
						.size()
						&& tokens.get(currentToken).getWord().equals("-"))) {
			twiceHere = true;
			if (currentToken < tokens.size()
					&& tokens.get(currentToken).getWord().equals("+")) {
				passingOperator = "+";
				node = new DefaultMutableTreeNode("+");
				parent.add(node);
				incrementToken();
				node = new DefaultMutableTreeNode("A");
				parent.add(node);
				error = rule_A(node);
			} else if (currentToken < tokens.size()
					&& tokens.get(currentToken).getWord().equals("-")) {
				passingOperator = "-";
				node = new DefaultMutableTreeNode("-");
				parent.add(node);
				incrementToken();
				node = new DefaultMutableTreeNode("A");
				parent.add(node);
				error = rule_A(node);
			}
			if (twiceHere) {
				String x = SemanticAnalyzer.popStack();
				String y = SemanticAnalyzer.popStack();
				String result = SemanticAnalyzer.calculateCube(x, y,
						passingOperator);
				SemanticAnalyzer.pushStack(result);
				if (passingOperator.equals("-"))
					CodeGenerator.addInstruction("OPR", "3", "0");
				else if (passingOperator.equals("+"))
					CodeGenerator.addInstruction("OPR", "2", "0");
			}
		}

		return error;
	}

	/**
	 * Rule_ a.
	 * 
	 * @param parent
	 *            the parent
	 * @return true, if successful
	 */
	private static boolean rule_A(DefaultMutableTreeNode parent) {
		boolean error;
		boolean twiceHere = false;
		String passingOperator = "";
		DefaultMutableTreeNode node = new DefaultMutableTreeNode("B");
		parent.add(node);
		error = rule_B(node);
		while (!error
				&& (currentToken < tokens.size()
						&& tokens.get(currentToken).getWord().equals("*") || currentToken < tokens
						.size()
						&& tokens.get(currentToken).getWord().equals("/"))) {
			twiceHere = true;

			if (currentToken < tokens.size()
					&& tokens.get(currentToken).getWord().equals("*")) {
				passingOperator = "*";
				node = new DefaultMutableTreeNode("*");
				parent.add(node);
				incrementToken();
				node = new DefaultMutableTreeNode("B");
				parent.add(node);

				error = rule_B(node);

			} else if (currentToken < tokens.size()
					&& tokens.get(currentToken).getWord().equals("/")) {
				passingOperator = "/";
				node = new DefaultMutableTreeNode("/");
				parent.add(node);
				node = new DefaultMutableTreeNode("B");
				parent.add(node);
				incrementToken();
				error = rule_B(node);
			}

			if (twiceHere) {
				String x = SemanticAnalyzer.popStack();
				String y = SemanticAnalyzer.popStack();

				String result = SemanticAnalyzer.calculateCube(x, y,
						passingOperator);
				SemanticAnalyzer.pushStack(result);

				if (passingOperator.equals("*"))
					CodeGenerator.addInstruction("OPR", "4", "0");
				else if (passingOperator.equals("/"))
					CodeGenerator.addInstruction("OPR", "5", "0");
			}

		}

		return error;
	}

	/**
	 * Rule_ b.
	 * 
	 * @param parent
	 *            the parent
	 * @return true, if successful
	 */
	private static boolean rule_B(DefaultMutableTreeNode parent) {
		boolean error;
		DefaultMutableTreeNode node;
		boolean operatorWasUsed = false;
		if (currentToken < tokens.size()
				&& tokens.get(currentToken).getWord().equals("-")) {
			operatorWasUsed = true;
			CodeGenerator.addInstruction("LIT", "0", "0");
			node = new DefaultMutableTreeNode("-");
			parent.add(node);
			incrementToken();
		}

		node = new DefaultMutableTreeNode("C");
		parent.add(node);
		error = rule_C(node);
		if (operatorWasUsed) {
			String x = SemanticAnalyzer.popStack();

			String result = SemanticAnalyzer.calculateCube(x, "-");
			SemanticAnalyzer.pushStack(result);
			CodeGenerator.addInstruction("OPR", "3", "0");

		}
		return error;
	}

	/**
	 * Rule_ c.
	 * 
	 * @param parent
	 *            the parent
	 * @return true, if successful
	 */
	private static boolean rule_C(DefaultMutableTreeNode parent) {
		boolean error = false;
		DefaultMutableTreeNode node;
		if (currentToken < tokens.size()
				&& tokens.get(currentToken).getToken().equals("INTEGER")) {
			SemanticAnalyzer.pushStack(tokens.get(currentToken).getToken());
			node = new DefaultMutableTreeNode("integer" + "("
					+ tokens.get(currentToken).getWord() + ")");
			parent.add(node);
			CodeGenerator.addInstruction("LIT", tokens.get(currentToken)
					.getWord(), "0");
			incrementToken();
		} else if (currentToken < tokens.size()
				&& tokens.get(currentToken).getToken().equals("FLOAT")) {
			SemanticAnalyzer.pushStack(tokens.get(currentToken).getToken());
			node = new DefaultMutableTreeNode("float" + "("
					+ tokens.get(currentToken).getWord() + ")");
			parent.add(node);
			CodeGenerator.addInstruction("LIT", tokens.get(currentToken)
					.getWord(), "0");
			incrementToken();
		} else if (currentToken < tokens.size()
				&& tokens.get(currentToken).getToken().equals("OCTAL")) {
			SemanticAnalyzer.pushStack("INTEGER");
			node = new DefaultMutableTreeNode("octal" + "("
					+ tokens.get(currentToken).getWord() + ")");
			parent.add(node);
			CodeGenerator.addInstruction("LIT", tokens.get(currentToken)
					.getWord(), "0");
			incrementToken();
		} else if (currentToken < tokens.size()
				&& tokens.get(currentToken).getToken().equals("HEXADECIMAL")) {
			SemanticAnalyzer.pushStack("INTEGER");
			node = new DefaultMutableTreeNode("hexadecimal" + "("
					+ tokens.get(currentToken).getWord() + ")");
			parent.add(node);
			CodeGenerator.addInstruction("LIT", tokens.get(currentToken)
					.getWord(), "0");
			incrementToken();
		} else if (currentToken < tokens.size()
				&& tokens.get(currentToken).getToken().equals("BINARY")) {
			SemanticAnalyzer.pushStack("INTEGER");
			node = new DefaultMutableTreeNode("binary" + "("
					+ tokens.get(currentToken).getWord() + ")");
			parent.add(node);
			CodeGenerator.addInstruction("LIT", tokens.get(currentToken)
					.getWord(), "0");
			incrementToken();
		} else if (currentToken < tokens.size()
				&& tokens.get(currentToken).getWord().equals("true")) {
			SemanticAnalyzer.pushStack("BOOLEAN");
			node = new DefaultMutableTreeNode(tokens.get(currentToken)
					.getWord());
			parent.add(node);
			CodeGenerator.addInstruction("LIT", tokens.get(currentToken)
					.getWord(), "0");
			incrementToken();
		} else if (currentToken < tokens.size()
				&& tokens.get(currentToken).getWord().equals("false")) {
			SemanticAnalyzer.pushStack("BOOLEAN");
			node = new DefaultMutableTreeNode(tokens.get(currentToken)
					.getWord());
			parent.add(node);
			CodeGenerator.addInstruction("LIT", tokens.get(currentToken)
					.getWord(), "0");
			incrementToken();
		} else if (currentToken < tokens.size()
				&& tokens.get(currentToken).getToken().equals("STRING")) {
			SemanticAnalyzer.pushStack(tokens.get(currentToken).getToken());
			node = new DefaultMutableTreeNode("string" + "("
					+ tokens.get(currentToken).getWord() + ")");
			parent.add(node);
			CodeGenerator.addInstruction("LIT", tokens.get(currentToken)
					.getWord(), "0");
			incrementToken();
		} else if (currentToken < tokens.size()
				&& tokens.get(currentToken).getToken().equals("CHARACTER")) {
			SemanticAnalyzer.pushStack(tokens.get(currentToken).getToken());
			node = new DefaultMutableTreeNode("character" + "("
					+ tokens.get(currentToken).getWord() + ")");
			parent.add(node);
			CodeGenerator.addInstruction("LIT", tokens.get(currentToken)
					.getWord(), "0");
			incrementToken();
		} else if (currentToken < tokens.size()
				&& tokens.get(currentToken).getToken().equals("IDENTIFIER")) {

			if (SemanticAnalyzer.getSymbolTable().containsKey(
					tokens.get(currentToken).getWord())) {
				Vector<SymbolTableItem> item = SemanticAnalyzer
						.getSymbolTable().get(
								tokens.get(currentToken).getWord());
				String pushVariable = SemanticAnalyzer.getDataTypeTable().get(
						item.get(0).getType());
				SemanticAnalyzer.pushStack(pushVariable);
				CodeGenerator.addInstruction("LOD", tokens.get(currentToken)
						.getWord(), "0");

			} else {
				SemanticAnalyzer.error(gui, 4, tokens.get(currentToken)
						.getLine(), tokens.get(currentToken).getWord());
				SemanticAnalyzer.pushStack("ERROR");
			}
			node = new DefaultMutableTreeNode("identifier" + "("
					+ tokens.get(currentToken).getWord() + ")");
			parent.add(node);
			incrementToken();
		} else if (currentToken < tokens.size()
				&& tokens.get(currentToken).getWord().equals("(")) {
			node = new DefaultMutableTreeNode("(");
			parent.add(node);
			incrementToken();
			node = new DefaultMutableTreeNode("expression");
			parent.add(node);
			error = rule_expression(node);
			if (error)
				error(9);
			node = new DefaultMutableTreeNode(")");
			parent.add(node);
			currentToken++;
		} else {
			return true;
		}
		return false;
	}

	/**
	 * Error.
	 * 
	 * @param err
	 *            the err
	 */
	public static void error(int err) {
		int n;

		if ((currentToken < tokens.size())) {

			n = tokens.get(currentToken).getLine();

		} else {
			n = tokens.get(tokens.size() - 1).getLine();
		}
		switch (err) {
		case 1:
			gui.writeConsole("Line" + n + ": expected {");
			break;
		case 2:
			gui.writeConsole("Line" + n + ": expected }");
			break;
		case 3:
			gui.writeConsole("Line" + n + ": expected ;");
			break;
		case 4:
			break;
		case 5:
			gui.writeConsole("Line" + n + ": expected =");
			break;
		case 6:
			gui.writeConsole("Line" + n + ": expected identifier");
			break;
		case 7:
			gui.writeConsole("Line" + n + ": expected )");
			break;
		case 8:
			gui.writeConsole("Line" + n + ": expected (");
			break;
		case 9:
			gui.writeConsole("Line" + n + ": expected value, identifier, (");
			break;
		case 10:
			gui.writeConsole("Line" + n + ": expected :");
			break;
		case 11:
			gui.writeConsole("Line" + n
					+ ": expected integer, octal, hexadecimal or binary");
			break;
		case 12:
			gui.writeConsole("Line" + n + ": expected case inside a switch");
			break;
		}
	}

	/**
	 * Recovery.
	 * 
	 * @param err
	 *            the err
	 */
	public static void recovery(int err) {
		int n;

		if ((currentToken < tokens.size())) {
			if (tokens.get(currentToken).getLine() > tokens.get(
					currentToken - 1).getLine()) {
				n = tokens.get(currentToken - 1).getLine();
			} else {
				n = tokens.get(currentToken).getLine();
			}
		} else {
			n = tokens.get(currentToken - 1).getLine();
		}
		switch (err) {
		case 1:
			gui.writeConsole("Line" + n + ": expected {");
			break;
		case 2:
			gui.writeConsole("Line" + n + ": expected }");
			break;
		case 3:
			int lineNo = tokens.get(currentToken).getLine();
			gui.writeConsole("Line" + n + ": expected ;");
			while (currentToken < tokens.size()
					&& tokens.get(currentToken).getLine() <= lineNo) {
				currentToken++;
			}
			break;

		case 4:

			lineNo = tokens.get(currentToken).getLine();
			gui.writeConsole("Line" + lineNo
					+ ": expected identifier or keyword");
			while (currentToken < tokens.size()
					&& tokens.get(currentToken).getLine() <= lineNo) {
				currentToken++;
			}
			break;
		case 5:
			currentToken++;
			break;
		case 6:
			gui.writeConsole("Line" + n + ": expected identifier");
			break;
		case 7:
			lineNo = tokens.get(currentToken).getLine();
			gui.writeConsole("Line" + lineNo + ": expected )");
			while (currentToken < tokens.size()
					&& tokens.get(currentToken).getLine() <= lineNo) {
				currentToken++;
			}
			break;
		case 8:
			gui.writeConsole("Line" + n + ": expected (");
			break;
		case 9:
			gui.writeConsole("Line" + n + ": expected value, identifier, (");
			break;
		case 10:
			lineNo = tokens.get(currentToken).getLine();

			gui.writeConsole("Line" + lineNo + ": expected :");
			while (currentToken < tokens.size()
					&& tokens.get(currentToken).getLine() <= lineNo) {
				currentToken++;
			}
			break;
		}
	}

	/**
	 * Checks if is data type.
	 * 
	 * @param input
	 *            the input
	 * @return true, if is data type
	 */
	public static boolean isDataType(String input) {
		if (input.equals("int") || input.equals("float")
				|| input.equals("boolean") || input.equals("char")
				|| input.equals("string") || input.equals("void")) {
			return true;
		}
		return false;
	}

	/**
	 * Checks if is new stmt.
	 * 
	 * @return true, if is new stmt
	 */
	public static boolean isNewStmt() {
		if (tokens.get(currentToken - 1).getWord().equals(";")
				|| tokens.get(currentToken).getLine() > tokens.get(
						currentToken - 1).getLine()) {
			return true;
		}
		return false;
	}

	/**
	 * Increment token.
	 * 
	 * @return true, if successful
	 */
	public static boolean incrementToken() {
		if (((currentToken + 1) < tokens.size())
				&& tokens.get(currentToken).getLine() == tokens.get(
						currentToken + 1).getLine()) {
			currentToken++;
			return true;
		}
		return false;

	}

}
