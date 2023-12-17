package edu.brown.cs.student.main.server.translate.records.AzureRecords;

import java.util.List;

public record DetectTranslateInfo(DetectLanguage detectedLanguage, List<Translation> translations) {

}
