package edu.brown.cs.student.Tests.ActualTests;

import edu.brown.cs.student.main.parsing.CreatorFromRow;
import edu.brown.cs.student.main.parsing.FactoryFailureException;
import edu.brown.cs.student.main.parsing.Parser;
import edu.brown.cs.student.main.searching.Search;
import edu.brown.cs.student.main.searching.StringListCreator;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.testng.Assert;

/** Class that tests the methods of the Search class. */
public class SearchTests {

  /**
   * Tests the basicSearch method from the search class. Done by using provided files and some of my
   * own.
   */
  @Test
  public void basicSearchTest() {

    // regular search case
    try {
      FileReader reader =
          new FileReader(
              "/Users/marcelmatsal/Desktop/CS320/csv-MarcelMatsal/data/stars/ten-star.csv");
      CreatorFromRow<List<String>> creator = new StringListCreator();
      Parser<List<String>> parser = new Parser<>(reader, "true", creator);
      Search searcher = new Search(parser, "Proxima Centauri");
      ArrayList<List<String>> actual = searcher.basicSearch();
      Assert.assertEquals(
          List.of("70667", "Proxima Centauri", "-0.47175", "-0.36132", "-1.15037"), actual.get(0));

      // checking to see that the same row won't be printed twice
      FileReader reader2 =
          new FileReader(
              "/Users/marcelmatsal/Desktop/CS320/csv-MarcelMatsal/data/stars/ten-star.csv");
      Parser<List<String>> parser2 = new Parser<>(reader2, "true", creator);
      Search searcher2 = new Search(parser2, "0");
      ArrayList<List<String>> actual2 = searcher2.basicSearch();
      Assert.assertEquals(actual2.size(), 1);
      Assert.assertEquals(actual2.get(0), List.of("0", "Sol", "0", "0", "0"));

      // checking case when the target is not in the file
      FileReader reader3 =
          new FileReader(
              "/Users/marcelmatsal/Desktop/CS320/csv-MarcelMatsal/data/stars/ten-star.csv");
      CreatorFromRow<List<String>> creator3 = new StringListCreator();
      Parser<List<String>> parser3 = new Parser<>(reader3, "true", creator3);
      Search searcher3 = new Search(parser3, "Proxi");
      ArrayList<List<String>> actual3 = searcher3.basicSearch();
      ArrayList<List<String>> expected3 = new ArrayList<>();
      Assert.assertEquals(expected3, actual3);

      // testing with a file I created with no header
      FileReader reader4 =
          new FileReader("src/test/java/edu/brown/cs/student/Tests/TestCsvs/test2 - Sheet1.csv");
      CreatorFromRow<List<String>> creator4 = new StringListCreator();
      Parser<List<String>> parser4 = new Parser<>(reader4, "false", creator4);
      Search searcher4 = new Search(parser4, "i");
      ArrayList<List<String>> actual4 = searcher4.basicSearch();
      Assert.assertEquals(List.of("i", "j", "k", "l"), actual4.get(0));

      // testing on an empty file
      FileReader reader5 =
          new FileReader("src/test/java/edu/brown/cs/student/Tests/TestCsvs/test3 - Sheet1.csv");
      CreatorFromRow<List<String>> creator5 = new StringListCreator();
      Parser<List<String>> parser5 = new Parser<>(reader5, "false", creator5);
      Search searcher5 = new Search(parser5, "i");
      ArrayList<List<String>> actual5 = searcher5.basicSearch();
      ArrayList<List<String>> expected5 = new ArrayList<>();
      Assert.assertEquals(expected5, actual5);

    } catch (FileNotFoundException e) {
      System.out.println("file not found");
    } catch (Exception e){

    }
  }

  /**
   * Test that tests the colSearch method from the search class. It is done by using some provided
   * files. Tests normal and edge cases
   */
  @Test
  public void colSearchTest() {
    // testing on a provided file
    try {
      FileReader reader =
          new FileReader(
              "/Users/marcelmatsal/Desktop/CS320/csv-MarcelMatsal/data/stars/stardata.csv");
      CreatorFromRow<List<String>> creator = new StringListCreator();
      Parser<List<String>> parser = new Parser<>(reader, "true", creator);
      Search searcher = new Search(parser, "Hamilton");
      ArrayList<List<String>> actual = searcher.colSearch("1");
      List<String> expected = List.of("31", "Hamilton", "542.88504", "0.92945", "25.36898");
      Assert.assertEquals(actual.get(0), expected);
      Assert.assertEquals(actual.size(), 1);

      // testing where target is not in the specified col
      FileReader reader2 =
          new FileReader(
              "/Users/marcelmatsal/Desktop/CS320/csv-MarcelMatsal/data/stars/stardata.csv");
      CreatorFromRow<List<String>> creator2 = new StringListCreator();
      Parser<List<String>> parser2 = new Parser<>(reader2, "true", creator2);
      Search searcher2 = new Search(parser2, "Hamilton");
      ArrayList<List<String>> actual2 = searcher2.colSearch("2");
      Assert.assertEquals(actual2.size(), 0);
    } catch (FileNotFoundException e) {
    } catch (Exception e){

    }

    // testing on a file I created
    try {
      FileReader reader =
          new FileReader(
              "/Users/marcelmatsal/Desktop/CS320/csv-MarcelMatsal/src/"
                  + "test/java/edu/brown/cs/student/Tests/TestCsvs/test4 - Sheet1.csv");
      CreatorFromRow<List<String>> creator = new StringListCreator();
      Parser<List<String>> parser = new Parser<>(reader, "true", creator);
      Search searcher = new Search(parser, "pink");
      ArrayList<List<String>> actual = searcher.colSearch("2");
      List<String> expected = List.of("Sally", "12", "pink");
      Assert.assertEquals(actual.get(0), expected);
      Assert.assertEquals(actual.size(), 1);

    } catch (FileNotFoundException e) {
    } catch (Exception e){

    }

    // testing on a file I created with no header
    try {
      FileReader reader =
          new FileReader("src/test/java/edu/brown/cs/student/Tests/TestCsvs/test2 - Sheet1.csv");
      CreatorFromRow<List<String>> creator = new StringListCreator();
      Parser<List<String>> parser = new Parser<>(reader, "false", creator);
      Search searcher = new Search(parser, "n");
      ArrayList<List<String>> actual = searcher.colSearch("1");
      List<String> expected = List.of("m", "n", "o", "p");
      Assert.assertEquals(actual.get(0), expected);
      Assert.assertEquals(actual.size(), 1);

    } catch (FileNotFoundException e) {
    } catch (Exception e){

    }

    // testing on an empty file
    try {
      FileReader reader =
          new FileReader("src/test/java/edu/brown/cs/student/Tests/TestCsvs/test3 - Sheet1.csv");
      CreatorFromRow<List<String>> creator = new StringListCreator();
      Parser<List<String>> parser = new Parser<>(reader, "false", creator);
      Search searcher = new Search(parser, "n");
      ArrayList<List<String>> actual = searcher.colSearch("1");
      List<String> expected = new ArrayList<>();
      Assert.assertEquals(actual, expected);
      Assert.assertEquals(actual.size(), 0);

    } catch (FileNotFoundException e) {
    } catch (Exception e){

    }

    // col asked for is not a col that is possible (index out of bounds)
    try {
      FileReader reader =
          new FileReader("src/test/java/edu/brown/cs/student/Tests/TestCsvs/test2 - Sheet1.csv");
      CreatorFromRow<List<String>> creator = new StringListCreator();
      Parser<List<String>> parser = new Parser<>(reader, "false", creator);
      Search searcher = new Search(parser, "n");
      ArrayList<List<String>> actual = searcher.colSearch("10");
      List<String> expected = new ArrayList<>();
      Assert.assertEquals(actual, expected);
      Assert.assertEquals(actual.size(), 0);

    } catch (FileNotFoundException e) {
    } catch (Exception e){

    }
    /*
    Not testing for a case where trying to do a colSearch by inputting a word instead of a col.
    In readme its explained that we assume the user knows what they are inputting. Also, not testing
    that inputted to search is null because the number of arguments are being checked in main and
    will not allow that to happen.
    */
  }

  /**
   * Test that test the search classes headerSearch method. Done by using the provided files and
   * files I created. Check for some edge cases throughout these tests.
   */
  @Test
  public void headerSearchTest() {
    // testing it on a big file but should only return one row
    try {
      FileReader reader =
          new FileReader(
              "/Users/marcelmatsal/Desktop/CS320/csv-MarcelMatsal/data/stars/stardata.csv");
      CreatorFromRow<List<String>> creator = new StringListCreator();
      Parser<List<String>> parser = new Parser<>(reader, "true", creator);
      Search searcher = new Search(parser, "Hamilton");
      ArrayList<List<String>> actual = searcher.headerSearch("ProperName");
      List<String> expected = List.of("31", "Hamilton", "542.88504", "0.92945", "25.36898");
      Assert.assertEquals(actual.get(0), expected);
      Assert.assertEquals(actual.size(), 1);

    } catch (FileNotFoundException e) {
    } catch (Exception e){

    }
    // testing it on a simple file
    try {
      FileReader reader =
          new FileReader(
              "/Users/marcelmatsal/Desktop/CS320/csv-MarcelMatsal/data/census"
                  + "/dol_ri_earnings_disparity.csv");
      CreatorFromRow<List<String>> creator = new StringListCreator();
      Parser<List<String>> parser = new Parser<>(reader, "true", creator);
      Search searcher = new Search(parser, "Black");
      ArrayList<List<String>> actual = searcher.headerSearch("Data Type");
      List<String> expected = List.of("RI", "Black", "$770.26", "30424.80376", "$0.73", "6%");
      Assert.assertEquals(actual.get(0), expected);
      Assert.assertEquals(actual.size(), 1);

    } catch (FileNotFoundException e) {
    } catch (Exception e){

    }

    // testing it on one where lots of the rows should match the target in that column
    try {
      FileReader reader =
          new FileReader(
              "/Users/marcelmatsal/Desktop/CS320/csv-MarcelMatsal/data/census"
                  + "/income_by_race_edited.csv");
      CreatorFromRow<List<String>> creator = new StringListCreator();
      Parser<List<String>> parser = new Parser<>(reader, "true", creator);
      Search searcher = new Search(parser, "2020");
      ArrayList<List<String>> actual = searcher.headerSearch("ID Year");
      List<String> expected =
          List.of(
              "0",
              "Total",
              "2020",
              "2020",
              "85413",
              "6122",
              "Bristol County, RI",
              "05000US44001",
              "bristol-county-ri");
      Assert.assertEquals(actual.get(0), expected);
      Assert.assertEquals(actual.size(), 40);

    } catch (FileNotFoundException e) {
    } catch (Exception e){

    }

    // trying it on a csv file that I created
    try {
      FileReader reader =
          new FileReader(
              "/Users/marcelmatsal/Desktop/CS320/csv-MarcelMatsal/src/"
                  + "test/java/edu/brown/cs/student/Tests/TestCsvs/test4 - Sheet1.csv");
      CreatorFromRow<List<String>> creator = new StringListCreator();
      Parser<List<String>> parser = new Parser<>(reader, "true", creator);
      Search searcher = new Search(parser, "pink");
      ArrayList<List<String>> actual = searcher.headerSearch("favColor");
      List<String> expected = List.of("Sally", "12", "pink");
      Assert.assertEquals(actual.get(0), expected);
      Assert.assertEquals(actual.size(), 1);

    } catch (FileNotFoundException e) {
    } catch (Exception e){

    }

    // case where what I am searching for is not in the column with that header name
    try {
      FileReader reader =
          new FileReader(
              "/Users/marcelmatsal/Desktop/CS320/csv-MarcelMatsal/src/"
                  + "test/java/edu/brown/cs/student/Tests/TestCsvs/test4 - Sheet1.csv");
      CreatorFromRow<List<String>> creator = new StringListCreator();
      Parser<List<String>> parser = new Parser<>(reader, "true", creator);
      Search searcher = new Search(parser, "deep blue");
      ArrayList<List<String>> actual = searcher.headerSearch("favColor");
      List<String> expected = new ArrayList<>();
      Assert.assertEquals(actual, expected);
      Assert.assertEquals(actual.size(), 0);

    } catch (FileNotFoundException e) {
    } catch (Exception e){

    }

    // testing where header inputted is not a header
    try {
      FileReader reader =
          new FileReader("src/test/java/edu/brown/cs/student/Tests/TestCsvs/test4 - Sheet1.csv");
      CreatorFromRow<List<String>> creator = new StringListCreator();
      Parser<List<String>> parser = new Parser<>(reader, "true", creator);
      Search searcher = new Search(parser, "pink");
      ArrayList<List<String>> actual = searcher.headerSearch("favColors");
      List<String> expected = new ArrayList<>();
      Assert.assertEquals(actual, expected);
      Assert.assertEquals(actual.size(), 0);

    } catch (FileNotFoundException e) {
    } catch (Exception e){

    }

    /*
    not testing for a case where trying to do a headerSearch when there is no header because that
    is already accounted for in the main class, where it does not let you run a headerSerch
    when the header is not set equal to "true"
    */

  }
}
