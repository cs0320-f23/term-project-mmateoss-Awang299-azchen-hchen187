package edu.brown.cs.student.main.server.spotify.data.mockedJsonSources;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import edu.brown.cs.student.main.server.spotify.records.recommendationRecords.Recommendation;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

public class RecommendationDataSource {

  /** Instance variables that are used throughout the class. */
  private Moshi moshi;
  private BufferedReader reader;
  private String line;
  private Recommendation json;


  /**
   * Constructor for JsonLoader object
   */
  public RecommendationDataSource(Reader inputReader){
    if(inputReader != null){
      this.reader = new BufferedReader(inputReader);
      this.line = "";
    }
  }

  /**
   * Method that loads in a JsonFile to the RecommendationDataSource Class.
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
    JsonAdapter<Recommendation> dataAdapter = this.moshi.adapter(Recommendation.class);
    this.json = dataAdapter.fromJson(ourJson);
  }

  /**
   * Method that returns the object created from the JSON file which the user will be able to use.
   *
   * @return - object
   */
  public Recommendation getJsonObj(){
    // return a defensive copy
    Recommendation copy = this.json;
    return copy;
  }





}
