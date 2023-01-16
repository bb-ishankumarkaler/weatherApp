package com.project.weatherApp.service;

//import io.vertx.core.Future;

import io.vertx.core.json.JsonObject;
import io.reactivex.Single;
import io.vertx.reactivex.ext.web.client.HttpResponse;

public interface WeatherService {
  //  Future<String> job();
  // public CompletableFuture<String> fetch(HttpServerRequest request,String lat, String lon) throws InterruptedException;
//  Future<String> fetch(String lat, String lon) throws InterruptedException;
  Single<HttpResponse<JsonObject>> fetch(String lat, String lon);
}
