package edu.brown.cs.student.main.server.spotify.data.mockedJsonSources;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import edu.brown.cs.student.main.server.spotify.records.audioFeaturesRecords.FeaturesProp;
import edu.brown.cs.student.main.server.spotify.records.recommendationRecords.Recommendation;
import edu.brown.cs.student.main.server.spotify.records.searchRecords.Song;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

public class SongDataSource {

  /** Instance variables that are used throughout the class. */
  private Moshi moshi;
  private BufferedReader reader;
  private String line;
  private Song json;


  /**
   * Constructor for JsonLoader object
   */
  public SongDataSource(Reader inputReader){
    if(inputReader != null){
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

    while(this.line != null){
      ourJson += this.line;
      this.line = this.reader.readLine();
    }

    // making target type and then using Moshi to turn it into whatever type we want
    this.moshi = new Moshi.Builder().build();
    JsonAdapter<Song> dataAdapter = this.moshi.adapter(Song.class);
    this.json = dataAdapter.fromJson(ourJson);
  }

  /**
   * Method that returns the object created from the JSON file which the user will be able to use.
   *
   * @return - object
   */
  public Song getJsonObj(){
    // return a defensive copy
    Song copy = this.json;
    return copy;
  }

}
