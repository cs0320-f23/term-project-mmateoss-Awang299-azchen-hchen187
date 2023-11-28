package edu.brown.cs.student.Tests.server.csv;

import edu.brown.cs.student.main.parsing.CreatorFromRow;
import edu.brown.cs.student.main.parsing.Parser;
import edu.brown.cs.student.main.searching.StringListCreator;
import edu.brown.cs.student.main.server.csv.CsvLoader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.testng.Assert;

public class CsvLoaderTests {

  /**
   * Method that tests the setCsvData method from the CsvLoader class.
   */
  @Test
  public void setCsvDataTest(){
    CsvLoader loader = new CsvLoader();

    // testing where nothing has been loaded
    Assert.assertEquals(loader.getCsvData(), null);

    // case where what was set is null and therefore nothing should be returned
    Assert.assertEquals(loader.setCsvData(null), false);

    // case where we set something that could actually be data
    List<List<String>> list = new ArrayList<>();
    list.add(new ArrayList<>());
    Assert.assertEquals(loader.setCsvData(list), true);

    // case where we set an empty list of list
    List<List<String>> list2 = new ArrayList<>();
    loader.setCsvData(list2);
    Assert.assertEquals(loader.setCsvData(list2), false);

  }

  /**
   * Method that tests the getCsvData method from the CsvLoader class.
   */
  @Test
  public void getCsvDataTest(){
    CsvLoader loader = new CsvLoader();

    // testing where nothing has been loaded in yet, but we are tying to get data
    Assert.assertEquals(loader.getCsvData(), null);

    // case where what was set is null and therefore nothing should be returned
    loader.setCsvData(null);
    Assert.assertEquals(loader.getCsvData(), null);

    // case where we set something that could actually be data
    List<List<String>> list = new ArrayList<>();
    list.add(new ArrayList<>());
    loader.setCsvData(list);
    Assert.assertEquals(loader.getCsvData(),list);

    // case where we set an empty list of list
    List<List<String>> list2 = new ArrayList<>();
    loader.setCsvData(list2);
    Assert.assertEquals(loader.getCsvData(), null);

  }


  /**
   * Method that tests the setCsvParser method from the CSVLoader class.
   */
  @Test
  public void setCSVParserTest(){
    try{
      CsvLoader loader = new CsvLoader();
      FileReader reader = new FileReader("");
      CreatorFromRow<List<String>> creator = new StringListCreator();
      Parser<List<String>> parser = new Parser<List<String>>(reader,"true", creator);

      // case where we actually set something
      Assert.assertEquals(loader.setCsvParser(parser), true);

      // case where we set something that's null
      Assert.assertEquals(loader.setCsvParser(null), false);

    } catch (Exception e){

    }
  }

  /**
   * Method that tests the getCSVParser method from the CSVLoader class.
   */
  @Test
  public void getCSVParserTest(){
    try{
      CsvLoader loader = new CsvLoader();
      FileReader reader = new FileReader("");
      CreatorFromRow<List<String>> creator = new StringListCreator();
      Parser<List<String>> parser = new Parser<List<String>>(reader,"true", creator);

      // case where we nver set a parser
      Assert.assertEquals(loader.getCsvParser(), null);

      // case where we can actually get a parser
      loader.setCsvParser(parser);
      Assert.assertEquals(loader.getCsvParser(), parser);

      // case where we set something that's null
      loader.setCsvParser(null);
      Assert.assertEquals(loader.getCsvParser(), null);

    } catch (Exception e){

    }
  }
}
