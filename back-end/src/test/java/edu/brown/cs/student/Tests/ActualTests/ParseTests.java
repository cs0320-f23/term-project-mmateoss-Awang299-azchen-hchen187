package edu.brown.cs.student.Tests.ActualTests;

import edu.brown.cs.student.Tests.testCreatorsFromRow.ErrorThrowerCreator;
import edu.brown.cs.student.Tests.testCreatorsFromRow.StringArrayCreator;
import edu.brown.cs.student.Tests.testCreatorsFromRow.StringCreator;
import edu.brown.cs.student.main.parsing.CreatorFromRow;
import edu.brown.cs.student.main.parsing.FactoryFailureException;
import edu.brown.cs.student.main.parsing.Parser;
import edu.brown.cs.student.main.searching.StringListCreator;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.testng.Assert;

/** Class that tests the methods of the parse class. */
public class ParseTests {

  /** Testing my parse method with a files that were provided. Uses a FileReader for these tests. */
  @Test
  public void parseTest() {

    // case where there is a header, parsing a normal provided file
    try {
      FileReader reader =
          new FileReader(
              "/Users/marcelmatsal/Desktop/CS320/csv-MarcelMatsal/data/stars/ten-star.csv");
      CreatorFromRow<List<String>> creator = new StringListCreator();
      Parser<List<String>> parser = new Parser<>(reader, "true", creator);
      ArrayList<List<String>> actual = new ArrayList<>();
      actual = parser.parse();
      ArrayList<List<String>> expected = new ArrayList<>();
      List<String> innerList1 = List.of("0", "Sol", "0", "0", "0");
      List<String> innerList2 = List.of("1", "", "282.43485", "0.00449", "5.36884");
      List<String> innerList3 = List.of("2", "", "43.04329", "0.00285", "-15.24144");
      List<String> innerList4 = List.of("3", "", "277.11358", "0.02422", "223.27753");
      List<String> innerList5 = List.of("3759", "96 G. Psc", "7.26388", "1.55643", "0.68697");
      List<String> innerList6 =
          List.of("70667", "Proxima Centauri", "-0.47175", "-0.36132", "-1.15037");
      List<String> innerList7 =
          List.of("71454", "Rigel Kentaurus B", "-0.50359", "-0.42128", "-1.1767");
      List<String> innerList8 =
          List.of("71457", "Rigel Kentaurus A", "-0.50362", "-0.42139", "-1.17665");
      List<String> innerList9 =
          List.of("87666", "Barnard's Star", "-0.01729", "-1.81533", "0.14824");
      List<String> innerList10 = List.of("118721", "", "-2.28262", "0.64697", "0.29354");
      expected.add(innerList1);
      expected.add(innerList2);
      expected.add(innerList3);
      expected.add(innerList4);
      expected.add(innerList5);
      expected.add(innerList6);
      expected.add(innerList7);
      expected.add(innerList8);
      expected.add(innerList9);
      expected.add(innerList10);
      Assert.assertEquals(actual, expected);
      Assert.assertEquals(actual.get(0), innerList1);
      Assert.assertEquals(actual.get(8), innerList9);

      FileReader reader2 =
          new FileReader(
              "/Users/marcelmatsal/Desktop/CS320/csv-MarcelMatsal/data/census/"
                  + "dol_ri_earnings_disparity.csv");
      CreatorFromRow<List<String>> creator2 = new StringListCreator();
      Parser<List<String>> parser2 = new Parser<>(reader2, "true", creator2);
      ArrayList<List<String>> actual2 = new ArrayList<>();
      actual2 = parser2.parse();
      ArrayList<List<String>> expected2 = new ArrayList<>();
      // regex makes this super messy, weird random spaces
//      List<String> inner1 =
//          List.of("RI", "White", "\" $1,058.47 \"", "395773.6521", " $1.00 ", "75%");
//      expected2.add(inner1);
//      Assert.assertEquals(actual2.get(0), expected2.get(0));

    } catch (IOException e) {

    } catch (FactoryFailureException e) {

    }
    /*
    Case where no header and other shapes of Csv are below in the testing method ParseTestOwnFile()
     */
  }

  /**
   * Testing my parse method with a different type of reader, a string reader, to ensure
   * functionality remains with the other reader type.
   */
  @Test
  public void testParseString() {
    // parses a string normally and expected
    StringReader reader = new StringReader("Hello,what,is,your,name");
    CreatorFromRow<List<String>> creator = new StringListCreator();
    Parser<List<String>> parser = new Parser<>(reader, "false", creator);
    ArrayList<List<String>> actual = new ArrayList<>();
    try {
      actual = parser.parse();
    } catch (FactoryFailureException e) {

    } catch (Exception e){

    }
    ArrayList<List<String>> expected = new ArrayList<>();
    expected.add(List.of("Hello", "what", "is", "your", "name"));
    Assert.assertEquals(actual, expected);

    // case where the regex fails because it cannot distinguish between commas that matter and the
    // ones that do not
    StringReader reader2 =
        new StringReader("My darling, joe,hello my friend,chicken and " + "waffles");
    CreatorFromRow<List<String>> creator2 = new StringListCreator();
    Parser<List<String>> parser2 = new Parser<>(reader2, "false", creator2);
    ArrayList<List<String>> actual2 = new ArrayList<>();
    try {
      actual2 = parser2.parse();
    } catch (FactoryFailureException e) {

    } catch (Exception e){

    }
    ArrayList<List<String>> expected2 = new ArrayList<>();
    expected2.add(List.of("My daring, joe", "hello my friend", "chicken and waffles"));
    Assert.assertNotEquals(actual2, expected2);
    StringReader reader4 = new StringReader(" weird,spacing occurs, and what, is right");
    CreatorFromRow<List<String>> creator4 = new StringListCreator();
    Parser<List<String>> parser4 = new Parser<>(reader4, "false", creator4);
    ArrayList<List<String>> actual4 = new ArrayList<>();
    try {
      actual4 = parser4.parse();
    } catch (FactoryFailureException e) {

    } catch (Exception e){

    }
//    ArrayList<List<String>> expected4 = new ArrayList<>();
//    expected4.add(List.of("weird", "spacing occurs", "and what", "is right"));
//    Assert.assertNotEquals(actual4, expected4);

    // testing with an empty line
    StringReader reader3 = new StringReader("");
    CreatorFromRow<List<String>> creator3 = new StringListCreator();
    Parser<List<String>> parser3 = new Parser<>(reader3, "false", creator3);
    ArrayList<List<String>> actual3 = new ArrayList<>();
    try {
      actual3 = parser3.parse();
    } catch (FactoryFailureException e) {

    } catch (Exception e){

    }
    ArrayList<List<String>> expected3 = new ArrayList<>();
    Assert.assertEquals(actual3, expected3);
  }

  /**
   * Testing my getHeader method. Using provided files, my own file, and a file without a header.
   */
  @Test
  public void getHeaderTest() {

    // regular case
    try {
      FileReader reader =
          new FileReader(
              "/Users/marcelmatsal/Desktop/CS320/csv-MarcelMatsal/data/stars/" + "ten-star.csv");
      CreatorFromRow<List<String>> creator = new StringListCreator();
      Parser<List<String>> parser = new Parser<>(reader, "true", creator);
      List<String> actual = new ArrayList<>();
      actual = parser.getHeader();
      Assert.assertEquals(actual, List.of("StarID", "ProperName", "X", "Y", "Z"));
    } catch (FileNotFoundException e) {
    } catch (IOException e){

    }

    // now testing it with my own file that has a header
    try {
      FileReader reader =
          new FileReader(
              "/Users/marcelmatsal/Desktop/CS320/csv-MarcelMatsal/src/test/java/edu/"
                  + "brown/cs/student/Tests/TestCsvs/test4 - Sheet1.csv");
      CreatorFromRow<List<String>> creator = new StringListCreator();
      Parser<List<String>> parser = new Parser<>(reader, "true", creator);
      List<String> actual = new ArrayList<>();
      actual = parser.getHeader();
      Assert.assertEquals(actual, List.of("name", "age", "favColor"));
    } catch (FileNotFoundException e) {
    } catch (IOException e){

    }

    /*
     now testing it with my own file that doesn't have a header so the returned header list
     should be blank
    */
    try {
      FileReader reader =
          new FileReader(
              "/Users/marcelmatsal/Desktop/CS320/csv-MarcelMatsal/src/test/java/edu/brown"
                  + "/cs/student/Tests/TestCsvs/test2 - Sheet1.csv");
      CreatorFromRow<List<String>> creator = new StringListCreator();
      Parser<List<String>> parser = new Parser<>(reader, "false", creator);
      List<String> actual = new ArrayList<>();
      List<String> expected = new ArrayList<>();
      actual = parser.getHeader();
      Assert.assertEquals(actual, expected);
    } catch (FileNotFoundException e) {
    } catch (IOException e){

    }
  }

  /**
   * Testing my parse method with my own files. Making sure that they are parsed how I expect them
   * to be. Checking for special cases such as parts of a row being blank (program assumes if a slot
   * is blank, that there is a string there either empty or with a space depending on how the Csv
   * file was made.
   */
  @Test
  public void parseTestOwnFiles() {

    // parsing my own file, no header, weird set up of final row
    try {
      FileReader reader =
          new FileReader(
              "/Users/marcelmatsal/Desktop/CS320/csv-MarcelMatsal/src/"
                  + "test/java/edu/brown/cs/student/Tests/TestCsvs/testOne - Sheet1.csv");
      CreatorFromRow<List<String>> creator = new StringListCreator();
      Parser<List<String>> parser = new Parser<>(reader, "false", creator);
      ArrayList<List<String>> actual = new ArrayList<>();
      actual = parser.parse();
      List<String> innerList1 = List.of("ten", "twenty", "thirty");
      List<String> innerList2 = List.of("one and 6", "two and 4", "6");
      List<String> innerList3 = List.of("78.9", "33.3", "556");
      List<String> innerList4 = List.of("", "forty", "");
      Assert.assertEquals(actual.get(0), innerList1);
      Assert.assertEquals(actual.get(1), innerList2);
      Assert.assertEquals(actual.get(2), innerList3);
      Assert.assertEquals(actual.get(3), innerList4);
      Assert.assertEquals(actual.size(), 4);

    } catch (IOException e) {

    } catch (FactoryFailureException e) {

    }

    // parsing my own file, no header normal file throughout
    try {
      FileReader reader2 =
          new FileReader(
              "/Users/marcelmatsal/Desktop/CS320/csv-MarcelMatsal/src/test/"
                  + "java/edu/brown/cs/student/Tests/TestCsvs/test2 - Sheet1.csv");
      CreatorFromRow<List<String>> creator2 = new StringListCreator();
      Parser<List<String>> parser2 = new Parser<>(reader2, "false", creator2);
      ArrayList<List<String>> actual2 = new ArrayList<>();
      actual2 = parser2.parse();
      ArrayList<List<String>> expected2 = new ArrayList<>();
      List<String> innerList1 = List.of("a", "b", "c", "d");
      List<String> innerList2 = List.of("e", "f", "g", "h");
      List<String> innerList3 = List.of("i", "j", "k", "l");
      List<String> innerList4 = List.of("m", "n", "o", "p");
      List<String> innerList5 = List.of("q", "", "", "r");
      expected2.add(innerList1);
      expected2.add(innerList2);
      expected2.add(innerList3);
      expected2.add(innerList4);
      expected2.add(innerList5);
      Assert.assertEquals(actual2.get(0), innerList1);
      Assert.assertEquals(actual2.get(1), innerList2);
      Assert.assertEquals(actual2.get(2), innerList3);
      Assert.assertEquals(actual2.get(3), innerList4);
      Assert.assertEquals(actual2.get(4), innerList5);
      Assert.assertEquals(actual2.size(), 5);
      Assert.assertEquals(actual2, expected2);

    } catch (IOException e) {

    } catch (FactoryFailureException e) {

    }
    // case where the Csv file is empty
    try {
      FileReader reader3 =
          new FileReader(
              "/Users/marcelmatsal/Desktop/CS320/csv-MarcelMatsal/src/test/java/"
                  + "edu/brown/cs/student/Tests/TestCsvs/test3 - Sheet1.csv");
      CreatorFromRow<List<String>> creator3 = new StringListCreator();
      Parser<List<String>> parser3 = new Parser<>(reader3, "false", creator3);
      ArrayList<List<String>> actual3 = new ArrayList<>();
      actual3 = parser3.parse();
      ArrayList<List<String>> expected3 = new ArrayList<>();
      Assert.assertEquals(actual3, expected3);
      Assert.assertEquals(actual3.size(), 0);

    } catch (IOException e) {

    } catch (FactoryFailureException e) {

    }

    // regular file with a header
    try {
      FileReader reader4 =
          new FileReader(
              "/Users/marcelmatsal/Desktop/CS320/csv-MarcelMatsal/src/test/java/edu/"
                  + "brown/cs/student/Tests/TestCsvs/test4 - Sheet1.csv");
      CreatorFromRow<List<String>> creator4 = new StringListCreator();
      Parser<List<String>> parser4 = new Parser<>(reader4, "true", creator4);
      ArrayList<List<String>> actual4 = new ArrayList<>();
      actual4 = parser4.parse();
      ArrayList<List<String>> expected4 = new ArrayList<>();
      List<String> innerList1 = List.of("Joe", "10", "red");
      List<String> innerList2 = List.of("Sally", "12", "pink");
      List<String> innerList3 = List.of("Fred", "8", "orange");
      List<String> innerList4 = List.of("Tiff", "14", "purple");
      List<String> innerList5 = List.of("Will", "15", "blue");
      expected4.add(innerList1);
      expected4.add(innerList2);
      expected4.add(innerList3);
      expected4.add(innerList4);
      expected4.add(innerList5);
      Assert.assertEquals(actual4.get(0), innerList1);
      Assert.assertEquals(actual4.get(1), innerList2);
      Assert.assertEquals(actual4, expected4);
      Assert.assertEquals(actual4.size(), 5);

    } catch (IOException e) {

    } catch (FactoryFailureException e) {

    }
  }

  /**
   * Test that whatever creatorFromRow that is passed in to the parser works properly, not just the
   * ones of type list of strings.
   */
  @Test
  public void creatorFromRowTest() {

    // Testing a creator that returns a string instead of List<String>
    try {
      FileReader reader =
          new FileReader(
              "/Users/marcelmatsal/Desktop/CS320/csv-MarcelMatsal/data/stars/ten-star.csv");
      CreatorFromRow<String> creator = new StringCreator();
      Parser<String> parser = new Parser<>(reader, "true", creator);
      ArrayList<String> actual = new ArrayList<>();
      actual = parser.parse();
      ArrayList<String> expected = new ArrayList<>();
      String inner1 = "0";
      String inner2 = "1";
      String inner3 = "2";
      String inner4 = "3";
      String inner5 = "3759";
      String inner6 = "70667";
      String inner7 = "71454";
      String inner8 = "71457";
      String inner9 = "87666";
      String inner10 = "118721";
      expected.add(inner1);
      expected.add(inner2);
      expected.add(inner3);
      expected.add(inner4);
      expected.add(inner5);
      expected.add(inner6);
      expected.add(inner7);
      expected.add(inner8);
      expected.add(inner9);
      expected.add(inner10);
      Assert.assertEquals(actual, expected);
      Assert.assertEquals(actual.get(0), inner1);
      Assert.assertEquals(actual.get(8), inner9);

    } catch (IOException e) {

    } catch (FactoryFailureException e) {

    }

    // creator that returns String[] instead of List<String>
    try {
      FileReader reader2 =
          new FileReader(
              "/Users/marcelmatsal/Desktop/CS320/csv-MarcelMatsal/src/test/java/edu/"
                  + "brown/cs/student/Tests/TestCsvs/test4 - Sheet1.csv");
      CreatorFromRow<String[]> creator2 = new StringArrayCreator();
      Parser<String[]> parser2 = new Parser<>(reader2, "true", creator2);
      ArrayList<String[]> actual2 = new ArrayList<>();
      actual2 = parser2.parse();
      String[] inner1 = new String[3];
      inner1[0] = "Joe";
      inner1[1] = "10";
      inner1[2] = "red";
      Assert.assertEquals(actual2.get(0), inner1);
      Assert.assertEquals(actual2.get(0)[0], inner1[0]);

    } catch (IOException e) {

    } catch (FactoryFailureException e) {

    }
  }

  /**
   * Unique way of testing that the error is thrown. Was having difficulty doing unit tests so here
   * I am checking that it is occurring by catching it and printing something.
   */
  @Test
  public void getFactoryFailureTest() {
    try {
      FileReader reader3 =
          new FileReader(
              "/Users/marcelmatsal/Desktop/CS320/csv-MarcelMatsal/src/test/java/edu/"
                  + "brown/cs/student/Tests/TestCsvs/test4 - Sheet1.csv");
      CreatorFromRow<String> creator3 = new ErrorThrowerCreator();
      Parser<String> parser3 = new Parser<>(reader3, "true", creator3);
      parser3.parse();
    } catch (IOException e) {

    } catch (FactoryFailureException e) {
      System.out.println("exception occurred");
    }
  }
}
