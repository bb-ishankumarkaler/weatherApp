package com.project.weatherApp.service;

import com.project.weatherApp.service.WeatherService;
import io.vertx.core.*;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.codec.BodyCodec;
import com.project.weatherApp.encodeUtil;
import java.util.LinkedHashMap;
import java.util.concurrent.CompletableFuture;

public class WeatherServiceImpl implements WeatherService{
  Vertx vertx;
  public WeatherServiceImpl(Vertx vertx){
    this.vertx = vertx;
  }

  @Override
  public Future<String> job(){
    Promise<String> promise = Promise.promise();
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
    promise.complete("Promise completed");
    return promise.future();
  }

  @Override
  public Future<String> fetch(String lat, String lon){
    WebClient webClient = WebClient.create(vertx);
    Promise<String> promise = Promise.promise();
    String requestURI = String.format("/data/2.5/weather?lat=%s&lon=%s&appid=bf8e8cbe2b49324b1be3c9e9b6170d1b", lat, lon);
    webClient.get("api.openweathermap.org", requestURI).as(BodyCodec.jsonObject())
      .send().onSuccess(res -> {
        JsonObject jsonBody = res.body();
        LinkedHashMap<String, String> map = encodeUtil.getDataMap(jsonBody);
        promise.complete(jsonBody.encodePrettily());
      })
      .onFailure(res -> {
          promise.fail(res.getMessage());
      });
    return promise.future();
  }
//  public CompletableFuture<String> fetch(String lat, String lon) throws InterruptedException {
//    WebClient webClient = WebClient.create(vertx);
//    CompletableFuture<String> result = new CompletableFuture<>();
//    webClient.get("api.openweathermap.org/data/2.5/weather?lat=12.9&lon=77.3&appid=bf8e8cbe2b49324b1be3c9e9b6170d1b")
//      .send().onSuccess(response -> {
//        result.complete(String.valueOf(response.statusCode()));
//      })
//        .onFailure(response -> {
//          result.completeExceptionally(response.getCause());
//        });
//    return result;
//  }

}
