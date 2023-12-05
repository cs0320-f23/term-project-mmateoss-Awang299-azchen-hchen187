package edu.brown.cs.student.Tests.server.spotify.tokens;


/**
 * Record used to parse the token response into an object
 * @param access_token the Spotify API access token
 * @param token_type what type the token is for the spotify API
 * @param expires_in how long the token will last
 */
public record TokenResponse(String access_token, String token_type, int expires_in) {
}
