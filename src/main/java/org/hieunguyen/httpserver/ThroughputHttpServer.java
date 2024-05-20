package org.hieunguyen.httpserver;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.Executors;

public class ThroughputHttpServer {

  private static final String INPUT_FILE = "/Users/hieunguyen/Documents/Working/Repo"
      + "/Java_Multithreading_Concurrency_Performance_Optimization/src/main/resources/throughput"
      + "/war_and_peace.txt";
  private static final int NUM_THREADS = 5;

  public static void main(String[] args) throws IOException {
    String content = new String(Files.readAllBytes(Paths.get(INPUT_FILE)));
    HttpServer httpServer = HttpServer.create(new InetSocketAddress(8000), 0);
    httpServer.createContext("/search", new WordCounter(content));
    httpServer.setExecutor(Executors.newFixedThreadPool(NUM_THREADS));
    httpServer.start();

  }

  private static class WordCounter implements HttpHandler {

    private final String content;

    public WordCounter(String content) {
      this.content = content;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
      String query = exchange.getRequestURI().getQuery();
      String[] keyValue = query.split("=");
      String word = keyValue[1];
      int index = 0, counter = 0;
      while (index >= 0) {
        index = content.indexOf(word, index);
        if (index >= 0) {
          index += 1;
          counter += 1;
        }
      }
      byte[] bytes = Long.toString(counter).getBytes(StandardCharsets.UTF_8);
      exchange.sendResponseHeaders(200, bytes.length);
      OutputStream responseBody = exchange.getResponseBody();
      responseBody.write(bytes);
      responseBody.close();
    }
  }

}
