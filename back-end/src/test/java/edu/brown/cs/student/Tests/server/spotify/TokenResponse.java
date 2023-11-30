package edu.brown.cs.student.Tests.server.spotify;


public record TokenResponse(String access_token, String token_type, int expires_in) {
}
