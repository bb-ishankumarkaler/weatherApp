package com.project.weatherApp.service;

import io.vertx.core.json.JsonObject;
import io.reactivex.Single;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.web.client.HttpResponse;
import io.vertx.reactivex.ext.web.client.WebClient;

public interface WeatherService {
  //  Future<String> job();
  // public CompletableFuture<String> fetch(HttpServerRequest request,String lat, String lon) throws InterruptedException;
//  Future<String> fetch(String lat, String lon) throws InterruptedException;
  Single<HttpResponse<JsonObject>> fetch(String lat, String lon);
  Single<String> update(String lat, String lon, JsonObject data);
  String getFetchURI(String lat, String lon);
  WebClient getWebClient(Vertx vertx);
  Single<String> insert(JsonObject data);

  Single<String> search(String lat, String lon);
}
