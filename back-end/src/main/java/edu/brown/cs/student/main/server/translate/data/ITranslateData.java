package edu.brown.cs.student.main.server.translate.data;

public interface ITranslateData {

    public String getTranslation(String text, String fromLanguage, String toLanguage) throws Exception;
}
