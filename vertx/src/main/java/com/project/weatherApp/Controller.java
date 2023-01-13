package com.project.weatherApp;

import com.project.weatherApp.service.WeatherService;
import com.project.weatherApp.service.WeatherServiceImpl;
import io.vertx.core.*;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.ext.web.Router;

public class Controller implements Handler<HttpServerRequest> {
  Vertx vertx;
  Router router;
  WeatherService service;
  Controller(Vertx vertx){
    this.vertx = vertx;
    router = Router.router(vertx);
    service = new WeatherServiceImpl(vertx);
  }
  @Override
  public void handle(HttpServerRequest request) {
    if (request.method() == HttpMethod.GET) {
      System.out.println("Get method received");
      MultiMap params = request.params();
      System.out.println(params.toString());
      String lat = params.get("lat");
      String lon = params.get("lon");
      System.out.printf("(lat, lon) = %s, %s", lat, lon);
      try {
        service.fetch(lat, lon)
          .onSuccess(res -> {
            request.response().end(res);
          })
          .onFailure(res -> {
            request.response().setStatusCode(400).end(res.getMessage());
          });
      } catch (InterruptedException e) {
        request.response().setStatusCode(404).end(e.getMessage());
      }
//      System.out.println(request.body().onSuccess(System.out::println));
//      try {
//        service.fetch(request, lat, lon);
//      } catch (Exception e) {
//        System.out.println("Exception" + e.getMessage());
//      }
    }
    else if (request.method() == HttpMethod.POST){
     service.job().onSuccess(res -> {
       System.out.println(res + " OK!!");
       request.response().end(res);
     })
     .onFailure(res -> {
       System.out.println("Failed promise job!!");
     });
    }
    else if (request.method() == HttpMethod.PUT){
      request.response().end("PUT");
    }
  }
}
