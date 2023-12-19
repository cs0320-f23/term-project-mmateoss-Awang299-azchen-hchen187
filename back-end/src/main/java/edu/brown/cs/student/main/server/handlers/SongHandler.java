package edu.brown.cs.student.main.server.handlers;

import spark.Request;
import spark.Response;
import spark.Route;

public class SongHandler implements Route {

  /** Constructor for the SongHandler class */
  public SongHandler() {}

  /**
   * Handle method that takes care of getting the query parameters and then building a Song based on
   * the string.
   *
   * @param req request object used to get the query parameters.
   * @param res response object used to create a response.
   * @return returns what is mapped to the server. A song object in this case
   */
  @Override
  public Object handle(Request req, Response res) {
    return null;
  }
}
