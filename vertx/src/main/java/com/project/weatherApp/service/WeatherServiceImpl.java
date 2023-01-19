package com.project.weatherApp.service;

import com.project.weatherApp.dal.WeatherDal;
import com.project.weatherApp.dal.WeatherDalImpl;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.web.client.HttpResponse;
import io.vertx.reactivex.ext.web.client.WebClient;
import io.vertx.reactivex.ext.web.codec.BodyCodec;
import com.project.weatherApp.utils.EncodeUtil;
import io.reactivex.Single;


import java.util.LinkedHashMap;

public class WeatherServiceImpl implements WeatherService{
  Vertx vertx;
  WeatherDal dal;
  public WeatherServiceImpl(Vertx vertx){
    this.vertx = vertx;
    dal = new WeatherDalImpl(vertx);
  }
  @Override
  public WebClient getWebClient(Vertx vertx){
    return WebClient.create(vertx);
  }
  @Override
  public String getFetchURI(String lat, String lon){
    assert lat != null;
    assert lon != null;
    return String.format("/data/2.5/weather?lat=%s&lon=%s&appid=bf8e8cbe2b49324b1be3c9e9b6170d1b", lat, lon);
  }
  @Override
  public Single<HttpResponse<JsonObject>> fetch(String lat, String lon){
    WebClient webClient = getWebClient(vertx);
    String requestURI = getFetchURI(lat, lon);
    return webClient.get("api.openweathermap.org", requestURI).as(BodyCodec.jsonObject()).rxSend();
  }
  @Override
  public Single<String> update(String lat, String lon, JsonObject data) {
    LinkedHashMap<String, String> dataMap = EncodeUtil.getDataMapFromRequestJson(data);
    String updateQuery = String.format("UPDATE weather SET id=%s, name='%s', lat=%s, lon=%s, country_code='%s', weather='%s', temp=%s WHERE (lat=%s AND lon=%s)",
      dataMap.get("id"), dataMap.get("name"), dataMap.get("lat"), dataMap.get("lon"), dataMap.get("country_code"), dataMap.get("weather"),
      dataMap.get("temp"), dataMap.get("lat"), dataMap.get("lon"));
    return dal.query(updateQuery).map(EncodeUtil::rowSetToString);
  }

  @Override
  public Single<String> insert(JsonObject data) {
    LinkedHashMap<String, String> dataMap = EncodeUtil.getDataMapFromRequestJson(data);
    String insertQuery = String.format("INSERT INTO weather VALUES (%s, '%s', %s, %s, '%s', '%s', %s)", dataMap.get("id"), dataMap.get("name"),
      dataMap.get("lat"), dataMap.get("lon"), dataMap.get("country_code"), dataMap.get("weather"), dataMap.get("temp"));
    return dal.query(insertQuery).map(EncodeUtil::rowSetToString);
  }

  @Override
  public Single<String> search(String lat, String lon) {
    String searchQuery = String.format("SELECT * FROM weather WHERE lat=%s AND lon=%s", lat, lon);
    return dal.query(searchQuery).map(EncodeUtil::rowSetToString);
  }

//  @Override
//  public Future<String> fetch(String lat, String lon){
//    WebClient webClient = WebClient.create(vertx);
//    Promise<String> promise = Promise.promise();
//    String requestURI = String.format("/data/2.5/weather?lat=%s&lon=%s&appid=bf8e8cbe2b49324b1be3c9e9b6170d1b", lat, lon);
//    webClient.get("api.openweathermap.org", requestURI).as(BodyCodec.jsonObject())
//      .send().onSuccess(res -> {
//        JsonObject jsonBody = res.body();
//        LinkedHashMap<String, String> map = encodeUtil.getDataMap(jsonBody);
//        promise.complete(jsonBody.encodePrettily());
//      })
//      .onFailure(res -> {
//          promise.fail(res.getMessage());
//      });
//    return promise.future();
//  }
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
