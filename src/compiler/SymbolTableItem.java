package compiler;

// TODO: Auto-generated Javadoc
/**
 * The Class SymbolTableItem.
 */
public class SymbolTableItem {

	/** The type. */
	private String type;
	
	/** The scope. */
	private String scope;
	
	/** The value. */
	private String value;

	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * Sets the type.
	 *
	 * @param type the new type
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Sets the value.
	 *
	 * @param value the new value
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * Gets the scope.
	 *
	 * @return the scope
	 */
	public String getScope() {
		return scope;
	}

	/**
	 * Sets the scope.
	 *
	 * @param scope the new scope
	 */
	public void setScope(String scope) {
		this.scope = scope;
	}

	/**
	 * Instantiates a new symbol table item.
	 *
	 * @param type the type
	 * @param scope the scope
	 * @param value the value
	 */
	public SymbolTableItem(String type, String scope, String value) {
		this.type = type;
		this.scope = scope;
		this.value = value;
	}

}
