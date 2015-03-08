package compiler;

/**
 * The Class Token.
 */
public class Token {

  /** The word. */
  private String word;
  
  /** The token. */
  private String token;
  
  /** The line. */
  private int line;

  /**
   * Instantiates a new token.
   *
   * @param word the word
   * @param token the token
   * @param line the line
   */
  public Token(String word, String token, int line) {
    this.word = word;
    this.token = token;
    this.line = line;
  }

  /**
   * Instantiates a new token.
   *
   * @param word the word
   * @param token the token
   */
  public Token(String word, String token) {
    this.word = word;
    this.token = token;
    this.line = 0;
  }

  /**
   * Gets the word.
   *
   * @return the word
   */
  public String getWord() {
    return word;
  }

  /**
   * Sets the word.
   *
   * @param word the new word
   */
  public void setWord(String word) {
    this.word = word;
  }

  /**
   * Gets the token.
   *
   * @return the token
   */
  public String getToken() {
    return token;
  }

  /**
   * Sets the token.
   *
   * @param token the new token
   */
  public void setToken(String token) {
    this.token = token;
  }

  /**
   * Gets the line.
   *
   * @return the line
   */
  public int getLine() {
    return line;
  }

  /**
   * Sets the line.
   *
   * @param line the new line
   */
  public void setLine(int line) {
    this.line = line;
  }

}

