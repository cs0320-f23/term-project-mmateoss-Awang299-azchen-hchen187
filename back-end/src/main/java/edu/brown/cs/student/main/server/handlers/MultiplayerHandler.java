package edu.brown.cs.student.main.server.handlers;

import java.util.concurrent.ConcurrentHashMap;

import static spark.Spark.*;
import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.api.annotations.*;

@WebSocket
public class MultiplayerHandler {
  private static final ConcurrentHashMap<String, String> matchQueue = new ConcurrentHashMap<String, String>();

  @OnWebSocketConnect
  public void onConnect(Session user) throws Exception {
    // Logic for new connection
  }

  @OnWebSocketClose
  public void onClose(Session user, int statusCode, String reason) {
    // Logic for closed connection
  }

  @OnWebSocketMessage
  public void onMessage(Session user, String message) {
    // Logic for handling received message
  }

  @OnWebSocketError
  public void onError(Throwable error) {
    // Logic for handling error
  }

}
