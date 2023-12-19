package edu.brown.cs.student.main.server.translate.data;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableList;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;

import edu.brown.cs.student.main.server.translate.LanguageNotSupportedException;
import edu.brown.cs.student.main.server.translate.records.LanguageCode;
import edu.brown.cs.student.main.server.translate.records.TranslateResult;

public class CachedTranslateData implements ITranslateData {
    // private LibreTranslateData libreTranslateData;
    // private AzureTranslateData azureTranslateData;

    // List of all available translation APIs in the order of preference
    private ArrayList<ITranslateData> translateDataList;
    private LoadingCache<ImmutableList<String>, TranslateResult> translationCache;

    /**
     * Constructor for the CachedTranslateData class. Serves as a proxy for the
     * LyricsData class
     * allowing us to cache the data, increasing performance and reducing number of
     * API calls.
     */
    public CachedTranslateData(ArrayList<ITranslateData> translateDataList) {
        // this.libreTranslateData = new LibreTranslateData();
        // this.azureTranslateData = new AzureTranslateData();

        // Create priority list of available APIs
        this.translateDataList = translateDataList;

        // building the cache that will hold a song object for specific song names
        this.translationCache = CacheBuilder.newBuilder()
                .maximumSize(40)
                .expireAfterWrite(2, TimeUnit.MINUTES)
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
     * private wrapper for getTranslation that dispatches to available APIs
     * 
     * @param params- params to ITranslate method call
     * @return TranslateResult from ITranslate method call
     * @throws Exception
     */
    private TranslateResult getTranslationWrapper(ImmutableList<String> params) throws Exception {
        String text = params.get(0);
        String fromLanguage = params.get(1);
        String toLanguage = params.get(2);
        for (ITranslateData translateData : this.translateDataList) {
            if (this.CheckAPIAvailability(translateData)) {
                return translateData.getTranslation(text, fromLanguage, toLanguage);
            }
        }
        throw new Exception("No Translation APIs available");
    }

    /**
     * 
     * @param translateData- ITranslateData object that we are checking the
     *                       availability of
     * @return boolean- true if API is available, false otherwise
     */
    private boolean CheckAPIAvailability(ITranslateData translateData) {
        return true;
    }

    @Override
    public LanguageCode getLanguageCode(String language) throws LanguageNotSupportedException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getLanguageCode'");
    }

}
