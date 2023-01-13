package com.project.weatherApp.service;
import io.vertx.core.Future;

public interface WeatherService {
  Future<String> job();
  // public CompletableFuture<String> fetch(HttpServerRequest request,String lat, String lon) throws InterruptedException;
  Future<String> fetch(String lat, String lon) throws InterruptedException;
}
