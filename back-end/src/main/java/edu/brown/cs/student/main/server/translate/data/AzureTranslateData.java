package edu.brown.cs.student.main.server.translate.data;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import edu.brown.cs.student.main.server.translate.records.AzureRecords.DetectTranslateInfo;
import edu.brown.cs.student.main.server.translate.records.AzureRecords.TranslateInfo;
import edu.brown.cs.student.main.server.translate.records.AzureRecords.Translation;
import edu.brown.cs.student.main.server.translate.LanguageNotSupportedException;
import edu.brown.cs.student.main.server.translate.records.LanguageCode;
import edu.brown.cs.student.main.server.translate.records.TranslateResult;
import io.github.cdimascio.dotenv.Dotenv;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
// import io.github.cdimascio.dotenv.Dotenv;
import java.util.Map;

public class AzureTranslateData implements ITranslateData {
  private Map<String, String> languageMap;

  public AzureTranslateData() {
    this.languageMap = new HashMap<String, String>();
    languageMap.put("afrikaans", "af");
    languageMap.put("albanian", "sq");
    languageMap.put("amharic", "am");
    languageMap.put("arabic", "ar");
    languageMap.put("armenian", "hy");
    languageMap.put("assamese", "as");
    languageMap.put("azerbaijani (latin)", "az");
    languageMap.put("azerbaijani", "az");
    languageMap.put("bangla", "bn");
    languageMap.put("bashkir", "ba");
    languageMap.put("basque", "eu");
    languageMap.put("bhojpuri", "bho");
    languageMap.put("bodo", "brx");
    languageMap.put("bosnian", "bs");
    languageMap.put("bulgarian", "bg");
    languageMap.put("cantonese", "yue");
    languageMap.put("catalan", "ca");
    languageMap.put("chinese (literary)", "lzh");
    languageMap.put("chinese simplified", "zh-Hans");
    languageMap.put("chinese traditional", "zh-Hant");
    languageMap.put("chinese", "zh-Hans");
    languageMap.put("chishona", "sn");
    languageMap.put("croatian", "hr");
    languageMap.put("czech", "cs");
    languageMap.put("danish", "da");
    languageMap.put("dari", "prs");
    languageMap.put("divehi", "dv");
    languageMap.put("dogri", "doi");
    languageMap.put("dutch", "nl");
    languageMap.put("english", "en");
    languageMap.put("estonian", "et");
    languageMap.put("faroese", "fo");
    languageMap.put("fijian", "fj");
    languageMap.put("filipino", "fil");
    languageMap.put("finnish", "fi");
    languageMap.put("french", "fr");
    languageMap.put("french (canada)", "fr-ca");
    languageMap.put("galician", "gl");
    languageMap.put("georgian", "ka");
    languageMap.put("german", "de");
    languageMap.put("greek", "el");
    languageMap.put("gujarati", "gu");
    languageMap.put("haitian creole", "ht");
    languageMap.put("hausa", "ha");
    languageMap.put("hebrew", "he");
    languageMap.put("hindi", "hi");
    languageMap.put("hmong daw (latin)", "mww");
    languageMap.put("hungarian", "hu");
    languageMap.put("icelandic", "is");
    languageMap.put("igbo", "ig");
    languageMap.put("indonesian", "id");
    languageMap.put("inuinnaqtun", "ikt");
    languageMap.put("inuktitut", "iu");
    languageMap.put("inuktitut (latin)", "iu-Latn");
    languageMap.put("irish", "ga");
    languageMap.put("italian", "it");
    languageMap.put("japanese", "ja");
    languageMap.put("kannada", "kn");
    languageMap.put("kashmiri", "ks");
    languageMap.put("kazakh", "kk");
    languageMap.put("khmer", "km");
    languageMap.put("kinyarwanda", "rw");
    languageMap.put("klingon", "tlh-Latn");
    languageMap.put("klingon (plqad)", "tlh-Piqd");
    languageMap.put("konkani", "gom");
    languageMap.put("korean", "ko");
    languageMap.put("kurdish (central)", "ku");
    languageMap.put("kurdish (northern)", "kmr");
    languageMap.put("kurdish", "ku");
    languageMap.put("kyrgyz (cyrillic)", "ky");
    languageMap.put("kyrgyz", "ky");
    languageMap.put("lao", "lo");
    languageMap.put("latvian", "lv");
    languageMap.put("lithuanian", "lt");
    languageMap.put("lingala", "ln");
    languageMap.put("lower sorbian", "dsb");
    languageMap.put("luganda", "lug");
    languageMap.put("macedonian", "mk");
    languageMap.put("maithili", "mai");
    languageMap.put("malagasy", "mg");
    languageMap.put("malay (latin)", "ms");
    languageMap.put("malay", "ms");
    languageMap.put("malayalam", "ml");
    languageMap.put("maltese", "mt");
    languageMap.put("maori", "mi");
    languageMap.put("marathi", "mr");
    languageMap.put("mongolian (cyrillic)", "mn-Cyrl");
    languageMap.put("mongolian (traditional)", "mn-Mong");
    languageMap.put("mongolian", "mn-Cyrl");
    languageMap.put("myanmar", "my");
    languageMap.put("nepali", "ne");
    languageMap.put("norwegian", "nb");
    languageMap.put("nyanja", "nya");
    languageMap.put("odia", "or");
    languageMap.put("pashto", "ps");
    languageMap.put("persian", "fa");
    languageMap.put("polish", "pl");
    languageMap.put("portuguese (brazil)", "pt");
    languageMap.put("portuguese (portugal)", "pt-pt");
    languageMap.put("portuguese", "pt");
    languageMap.put("punjabi", "pa");
    languageMap.put("queretaro otomi", "otq");
    languageMap.put("romanian", "ro");
    languageMap.put("rundi", "run");
    languageMap.put("russian", "ru");
    languageMap.put("samoan", "sm");
    languageMap.put("serbian (cyrillic)", "sr-Cyrl");
    languageMap.put("serbian (latin)", "sr-Latn");
    languageMap.put("serbian", "sr-Latn");
    languageMap.put("sesotho", "st");
    languageMap.put("sesotho sa leboa", "nso");
    languageMap.put("setswana", "tn");
    languageMap.put("sindhi", "sd");
    languageMap.put("sinhala", "si");
    languageMap.put("slovak", "sk");
    languageMap.put("slovenian", "sl");
    languageMap.put("somali (arabic)", "so");
    languageMap.put("somali", "so");
    languageMap.put("spanish", "es");
    languageMap.put("swahili (latin)", "sw");
    languageMap.put("swahili", "sw");
    languageMap.put("swedish", "sv");
    languageMap.put("tahitian", "ty");
    languageMap.put("tamil", "ta");
    languageMap.put("tatar (latin)", "tt");
    languageMap.put("tatar", "tt");
    languageMap.put("telugu", "te");
    languageMap.put("thai", "th");
    languageMap.put("tibetan", "bo");
    languageMap.put("tigrinya", "ti");
    languageMap.put("tongan", "to");
    languageMap.put("turkish", "tr");
    languageMap.put("turkmen", "tk");
    languageMap.put("ukrainian", "uk");
    languageMap.put("upper sorbian", "hsb");
    languageMap.put("urdu", "ur");
    languageMap.put("uyghur (arabic)", "ug");
    languageMap.put("uyghur", "ug");
    languageMap.put("uzbek (latin)", "uz");
    languageMap.put("uzbek", "uz");
    languageMap.put("vietnamese", "vi");
    languageMap.put("welsh", "cy");
    languageMap.put("xhosa", "xh");
    languageMap.put("yoruba", "yo");
    languageMap.put("yucatec maya", "yua");
    languageMap.put("zulu", "zu");
  }

  /**
   * @param text-         String to be translated
   * @param fromLanguage- String representing the ISO 639-1 code of the language
   *                      to be translated
   *                      from
   * @param toLanguage-   String representing the ISO 639-1 code of the language
   *                      to be translated to
   * @return TranslateResult containing the translated lyrics and fromLanguage.
   * @throws IOException
   * @throws InterruptedException
   * @throws URISyntaxException
   */
  @Override
  public TranslateResult getTranslation(String text, String fromLanguage, String toLanguage)
      throws Exception {
    if (fromLanguage.equals(toLanguage) || text.equals("")) {
      return new TranslateResult(text, fromLanguage);
    }

    toLanguage = this.getLanguageCode(toLanguage).code();
    fromLanguage = this.getLanguageCode(fromLanguage).code();

    String endpoint = "https://api.cognitive.microsofttranslator.com";
    String route = fromLanguage != null
        ? "/translate?api-version=3.0&from=" + fromLanguage + "&to=" + toLanguage
        : "/translate?api-version=3.0&to=" + toLanguage;
    String url = endpoint.concat(route);
    Dotenv dotenv = Dotenv.load();
    String key = dotenv.get("AZURE_KEY");

    HttpRequest buildRequest = HttpRequest.newBuilder()
        .uri(new URI(url))
        .header("Ocp-Apim-Subscription-Key", key)
        .header("Content-Type", "application/json")
        .POST(HttpRequest.BodyPublishers.ofString("[{\"Text\": \"" + text + "\"}]"))
        .build();
    // building a response with the HttpRequest
    HttpResponse<String> response = HttpClient.newBuilder().build().send(buildRequest,
        HttpResponse.BodyHandlers.ofString());

    // using Moshi to turn it into a List<> object depending on if language
    // detection is used or not
    Moshi moshi = new Moshi.Builder().build();
    Translation translation;
    if (fromLanguage != null) {
      JsonAdapter<List<TranslateInfo>> dataAdapter = moshi
          .adapter(Types.newParameterizedType(List.class, TranslateInfo.class));
      List<TranslateInfo> body = dataAdapter.fromJson(response.body());
      translation = body.get(0).translations().get(0);
    } else { // Not dead code, we are assigning fromLanguage to detected language here
      JsonAdapter<List<DetectTranslateInfo>> dataAdapter = moshi
          .adapter(Types.newParameterizedType(List.class, DetectTranslateInfo.class));
      List<DetectTranslateInfo> body = dataAdapter.fromJson(response.body());
      translation = body.get(0).translations().get(0);
      fromLanguage = body.get(0).detectedLanguage().language();
    }

    if (translation.to().equals(toLanguage)) {
      return new TranslateResult(translation.text(), fromLanguage);
    } else {
      throw new Exception("Translation failed");
    }

    // JsonAdapter<Map<String, String>> dataAdapter = moshi
    // .adapter(Types.newParameterizedType(Map.class, String.class, String.class));
    // Map<String, String> body = dataAdapter.fromJson(response.body());
    // return body.get("translatedText");
  }

  /**
   * @param language- String to be detected
   * @return String representing the ISO 639-1 code of the language detected
   * @throws IOException
   * @throws InterruptedException
   * @throws URISyntaxException
   */
  @Override
  public LanguageCode getLanguageCode(String language) throws LanguageNotSupportedException {
    if (language == null) {
      return null;
    } else if (this.languageMap.containsKey(language)) {
      return new LanguageCode(language, this.languageMap.get(language));
    } else {
      throw new LanguageNotSupportedException("Language not supported");
    }
  }
}
