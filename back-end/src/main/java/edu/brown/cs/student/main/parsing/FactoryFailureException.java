package edu.brown.cs.student.main.parsing;

import java.util.ArrayList;
import java.util.List;

/**
 * This is an error provided to catch any error that may occur when you create an object from a row.
 * Feel free to expand or supplement or use it for other purposes.
 */
public class FactoryFailureException extends Exception {

  /** Instance variable used in this exception. */
  final List<String> row;

  /**
   * Constructor for the exception.
   *
   * @param message message to go along with the exception.
   * @param row row where error happened.
   */
  public FactoryFailureException(String message, List<String> row) {
    super(message);
    this.row = new ArrayList<>(row);
  }
}
