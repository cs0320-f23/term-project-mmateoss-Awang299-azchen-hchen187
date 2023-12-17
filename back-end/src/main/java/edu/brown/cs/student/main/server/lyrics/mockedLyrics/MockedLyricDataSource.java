package edu.brown.cs.student.main.server.lyrics.mockedLyrics;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import edu.brown.cs.student.main.server.lyrics.lyricsRecords.LyricsObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

/**
 * Class that servers as the main way of getting the data from the lyrics jsonfile. Parses it and
 * turns it into an object.
 */
public class MockedLyricDataSource {

  /** Instance variables that are used throughout the class. */
  private Moshi moshi;

  private BufferedReader reader;
  private String line;
  private LyricsObject json;

  /** Constructor for JsonLoader object */
  public MockedLyricDataSource(Reader inputReader) {
    if (inputReader != null) {
      this.reader = new BufferedReader(inputReader);
      this.line = "";
    }
  }

  /**
   * Method that loads in a JsonFile to the JSONDataSource Class.
   *
   * @throws IOException exception thrown when the file cant be read
   */
  public void loadJson() throws IOException {
    String ourJson = "";
    this.line = this.reader.readLine();

    while (this.line != null) {
      ourJson += this.line;
      this.line = this.reader.readLine();
    }

    // making target type and then using Moshi to turn it into whatever type we want
    this.moshi = new Moshi.Builder().build();
    JsonAdapter<LyricsObject> dataAdapter = this.moshi.adapter(LyricsObject.class);
    this.json = dataAdapter.fromJson(ourJson);
  }

  /**
   * Method that returns the object created from the JSON file which the user will be able to use.
   *
   * @return - the created json object
   */
  public LyricsObject getJsonObj() {
    // return a defensive copy
    LyricsObject copy = this.json;
    return copy;
  }
}
