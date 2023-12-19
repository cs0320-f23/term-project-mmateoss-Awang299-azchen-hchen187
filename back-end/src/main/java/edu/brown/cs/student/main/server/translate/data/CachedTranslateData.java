package edu.brown.cs.student.main.server.translate.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableList;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;

import edu.brown.cs.student.main.server.database.PostgresConnection;
import edu.brown.cs.student.main.server.translate.LanguageNotSupportedException;
import edu.brown.cs.student.main.server.translate.records.LanguageCode;
import edu.brown.cs.student.main.server.translate.records.TranslateResult;

public class CachedTranslateData implements ITranslateData {

    private ArrayList<ITranslateData> translateDataList; // List of all available translation APIs in the order of
                                                         // preference
    HashSet<HashMap<String, Object>> APILimitTable; // TableView of API limits from Database, updated regularly
    private Map<ITranslateData, String> databaseNameMap; // Map of ITranslateData to the name of the database table that
                                                         // stores the API limits
    private LoadingCache<ImmutableList<String>, TranslateResult> translationCache; // Caches on a per line basis

    private PostgresConnection postgresConnection; // Database connection
    private ITranslateData chosenDataSource; // The currently Chosen DataSource, initialized as null
    private int charCount; // The current characterCount for this.chosenDataSource that hasn't been updated
                           // to database

    /**
     * Constructor for the CachedTranslateData class. Serves as a proxy for the
     * LyricsData class, and also Dispatches to the appropriate API datasource
     * allowing us to cache the data, increasing performance and reducing number of
     * API calls.
     * 
     * @param translateDataList- List of all available translation APIs in the order
     *                           of preference
     * @param databaseNameMap-   Map of ITranslateData to the name of the database
     *                           table that stores the API limits
     */

    public CachedTranslateData(ArrayList<ITranslateData> translateDataList,
            Map<ITranslateData, String> databaseNameMap, PostgresConnection postgresConnection) {
        // Connect to Database
        this.postgresConnection = postgresConnection;
        try {
            this.APILimitTable = postgresConnection.getTranslationLimitTable();
        } catch (SQLException e) {
            this.APILimitTable = null;
        }

        this.databaseNameMap = databaseNameMap;
        this.charCount = 0;
        this.chosenDataSource = null;
        this.translateDataList = translateDataList;

        // building the cache that will hold a song object for specific song names
        this.translationCache = CacheBuilder.newBuilder()
                .maximumSize(1000)
                .expireAfterWrite(15, TimeUnit.MINUTES)
                .recordStats().build(
                        new CacheLoader<ImmutableList<String>, TranslateResult>() {
                            @Override
                            public TranslateResult load(ImmutableList<String> params) throws Exception {
                                try {
                                    return getTranslationWrapper(params);
                                } catch (Exception e) {
                                    // Caches error messages if lyrics aren't found
                                    return new TranslateResult(null, e.getMessage());
                                }
                            }
                        });
    }

    /**
     * Adds translated lyrics to defaultLyrics in place using the API dispatcher and
     * database
     * 
     * Updates the database with the number of characters translated once done
     * 
     * @param defaultLyrics- ArrayList of String arrays containing the default
     *                       lyrics. Format of String[3]: [0] = time, [1] =
     *                       originalLyrics, [2] = ""
     * @return void- modifies the defaultLyrics ArrayList to contain the translated
     *         lyrics in [2]
     */
    public void TranslateAll(ArrayList<String[]> defaultLyrics, String fromLanguage, String toLanguage)
            throws Exception {
        // Update our database view of the API limits
        this.APILimitTable = this.postgresConnection.getTranslationLimitTable();

        // Choose API datasource
        for (ITranslateData translateData : this.translateDataList) {
            if (this.CheckAPIAvailability(translateData, fromLanguage, toLanguage)) {
                this.chosenDataSource = translateData;
                break;
            }
        }
        if (this.chosenDataSource == null) {
            throw new Exception("No Translation APIs available");
        }

        // Translate lyrics
        for (String[] line : defaultLyrics) {
            if (line[1].equals("")) {
                continue;
            }

            line[2] = this.getTranslation(line[1].replaceAll("\"", "").replace("\'", ""), fromLanguage, toLanguage)
                    .translatedText();
        }
        // Update database in a batch
        this.updateCharacterLimit();
        this.chosenDataSource = null;
    }

    /**
     * @param text-         String to be translated
     * @param fromLanguage- String representing the ISO 639-1 code of the language
     *                      to be translated from
     * @param toLanguage-   String representing the ISO 639-1 code of the language
     *                      to be translated to
     * @return String containing the translated lyrics.
     * @throws Exception
     */
    @Override
    public TranslateResult getTranslation(String text, String fromLanguage, String toLanguage) throws Exception {
        ImmutableList<String> params = ImmutableList.of(text, fromLanguage, toLanguage);
        TranslateResult res = this.translationCache.get(params);
        if (res.translatedText() == null) {
            throw new Exception(res.fromLanguage());
        } else {
            return res;
        }
    }

    /**
     * private wrapper for getTranslation that translates a single line at a time.
     * Dispatches to the appropriate API datasource only if necessary
     * 
     * @param params- params to ITranslate method call
     * @return TranslateResult from ITranslate method call
     * @throws Exception
     */
    private TranslateResult getTranslationWrapper(ImmutableList<String> params) throws Exception {
        String text = params.get(0);
        String fromLanguage = params.get(1);
        String toLanguage = params.get(2);

        // datasource has already been chosen
        if (this.chosenDataSource != null) {
            this.charCount += text.length(); // Increment translation counts
            return this.chosenDataSource.getTranslation(text, fromLanguage, toLanguage);
        }

        // Update our database view of the API limits
        this.APILimitTable = this.postgresConnection.getTranslationLimitTable();

        // Choose API Datasource
        for (ITranslateData translateData : this.translateDataList) {
            if (this.CheckAPIAvailability(translateData, fromLanguage, toLanguage)) {
                this.charCount += text.length(); // Increment translation counts
                this.chosenDataSource = translateData;
                this.updateCharacterLimit();
                this.chosenDataSource = null;
                return translateData.getTranslation(text, fromLanguage, toLanguage);
            }
        }
        throw new Exception("No Translation APIs available");
    }

    /**
     * 
     * @param translateData- ITranslateData object that we are checking the
     *                       availability of
     * @param fromLanguage-  String representing the ISO 639-1 code of the language
     *                       to be translated from
     * @param toLanguage-    String representing the ISO 639-1 code of the language
     *                       to be translated to
     * 
     * @return boolean- true if API is available, false otherwise
     */
    private boolean CheckAPIAvailability(ITranslateData translateData, String fromLanguage, String toLanguage) {
        // Make sure languages are supported
        try {
            translateData.getLanguageCode(fromLanguage);
            translateData.getLanguageCode(toLanguage);
        } catch (LanguageNotSupportedException e) {
            return false;
        }

        if (this.APILimitTable == null) {
            return false;
        }

        // Check character limit in database
        for (HashMap<String, Object> tuple : this.APILimitTable) {
            if (tuple.get("name").equals(this.databaseNameMap.get(translateData))) {
                return (tuple.get("max_count") == null)
                        || (((Integer) tuple.get("max_count")) > ((Integer) tuple.get("current_count")));
            }
        }
        return false;
    }

    /**
     * Increases character limit for this.chosenDataSource by this.charCount
     * in the database
     */
    public void updateCharacterLimit() {
        if (this.chosenDataSource == null || this.APILimitTable == null) {
            return;
        }
        try {
            this.postgresConnection.incrementTranslationLimit(this.charCount,
                    this.databaseNameMap.get(this.chosenDataSource));
            this.charCount = 0;
        } catch (SQLException e) {
            this.APILimitTable = null;
        }
    }

    /**
     * @param language- name of the language
     * @return String representing the ISO 639-1 of the language
     */

    @Override
    public LanguageCode getLanguageCode(String language) throws LanguageNotSupportedException {
        for (ITranslateData translateData : this.translateDataList) {
            try {
                return translateData.getLanguageCode(language);
            } catch (LanguageNotSupportedException e) {
                continue;
            }
        }
        throw new LanguageNotSupportedException("Language not supported");
    }
}
