package edu.brown.cs.student.main.server.translate;

public class LanguageNotSupportedException extends Exception {
    // Constructor with no arguments
    public LanguageNotSupportedException() {
        super();
    }

    // Constructor that accepts a message
    public LanguageNotSupportedException(String message) {
        super(message);
    }
}