package edu.brown.cs.student.main.server.spotify.tokens;

/**
 * Class used for mocking the token generation, allowing for us to check the handler with mocked
 * data.
 */
public class MockedToken implements IToken{

  /**
   * Constructor for the MockedToken class.
   */
  public MockedToken(){

  }

  /**
   * Method that returns a mockedToken used for testing the handler without making calls to the
   * spotify API.
   *
   * @return A mocked Spotify Token
   */
  @Override
  public String getToken(){
    return "mockedTokenABCDEFG";
  }

}
