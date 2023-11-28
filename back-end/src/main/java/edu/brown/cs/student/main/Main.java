package edu.brown.cs.student.main;

import edu.brown.cs.student.main.parsing.CreatorFromRow;
import edu.brown.cs.student.main.parsing.FactoryFailureException;
import edu.brown.cs.student.main.parsing.Parser;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

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
  private Main(String[] args) {
//    // program will exit if not enough arguments are inputted
//    if (args.length < 3 || args.length > 5 || args.length == 4) {
//      System.err.println(
//          "Incorrect number of arguments, please reference readme for" + " guide to inputs");
//      System.exit(0);
//    }
//    try {
//      FileReader reader = new FileReader(args[0]);
//      CreatorFromRow<List<String>> creator = new StringListCreator();
//      Parser<List<String>> parser = new Parser<>(reader, args[2], creator);
//      Search searcher = new Search(parser, args[1]);
//
//      // input in form: [file], [target], [header t/f], [name/col], [actual name/col]
//      if (args.length == 3) {
//        searcher.basicSearch();
//
//      } else if (args.length == 5) {
//        if (args[3].equals("name") && args[2].equals("true")) {
//          searcher.headerSearch(args[4]);
//
//        } else if (args[3].equals("name") && !args[2].equals("true")) {
//          System.err.println(
//              "You cannot search the column of a header without having a header"
//                  + ", please ensure that third argument inputted is true if want to do this "
//                  + "search");
//        } else if (args[3].equals("col")) {
//          searcher.colSearch(args[4]);
//        }
//      }
//
//    } catch (FileNotFoundException e) {
//      System.err.println("File not Found");
//      System.exit(0);
//    } catch (FactoryFailureException e) {
//
//    } catch (IOException e){
//
//    }
  }

  /** Method called when the program runs. */
  private void run() {
    // dear student: you can remove this. you can remove anything. you're in cs32. you're free!
    // System.out.println(
    // "Your horoscope for this project:\n"
    // + "Entrust in the Strategy pattern, and it shall give thee the sovereignty to "
    // + "decide and the dexterity to change direction in the realm of thy code.");
  }
}
