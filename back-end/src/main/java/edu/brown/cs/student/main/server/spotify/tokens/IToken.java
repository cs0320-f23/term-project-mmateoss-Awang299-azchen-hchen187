package edu.brown.cs.student.main.server.spotify.tokens;

/**
 * Interface used to allow dependency injection in the handler. Allowing us to use either mocked
 * or a real token.
 */
public interface IToken {


  /**
   * Method used to return the generated or mocked Toke.
   *
   * @return The generated or mocked token.
   */
  public String getToken() throws Exception;


}
