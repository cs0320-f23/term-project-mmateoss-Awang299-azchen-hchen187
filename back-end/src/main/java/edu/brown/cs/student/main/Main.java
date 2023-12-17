package edu.brown.cs.student.main;

/** The Main class of our project. This is where execution begins. */
public final class Main {
  /**
   * The initial method called when execution begins.
   *
   * @param args An array of command line arguments
   */
  public static void main(String[] args) {
    new Main(args).run();
  }

  /**
   * Method called by main, adds a layer of protection to the program. Run the program by being in
   * the source directory of the project and then inputting into the command line: First: mvn
   * package Second: ./run [file] [target] [true/false] [name/col] [actual name/col] where the third
   * argument tells if the file has a header, the fourth if you want to search by name of
   * column(name) or index of column(col) and the fifth argument being the actual name of the header
   * at that column or the index of the column. The fourth and fifth arguments are optional, but if
   * included they both have to be included.
   *
   * @param args - the arguments inputted to the command line by the user
   */
  private Main(String[] args) {}

  /** Method called when the program runs. */
  private void run() {}
}
