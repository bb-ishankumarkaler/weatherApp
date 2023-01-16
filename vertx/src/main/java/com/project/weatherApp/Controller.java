package com.project.weatherApp;

import com.project.weatherApp.dal.WeatherDal;
import com.project.weatherApp.dal.WeatherDalImpl;
import com.project.weatherApp.service.WeatherService;
import com.project.weatherApp.service.WeatherServiceImpl;
import io.vertx.core.Handler;
import io.vertx.reactivex.core.MultiMap;
import io.vertx.core.http.HttpMethod;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.core.http.HttpServerRequest;
import com.project.weatherApp.utils.EncodeUtil;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.client.WebClient;
import io.vertx.reactivex.ext.web.codec.BodyCodec;
import rx.Single;

public class Controller implements Handler<HttpServerRequest> {
  Vertx vertx;
  WeatherService weatherService;
  WeatherDal weatherDal;

  Controller(Vertx vertx) {
    this.vertx = vertx;
    weatherService = new WeatherServiceImpl(vertx);
    weatherDal = new WeatherDalImpl(vertx);
  }

  @Override
  public void handle(HttpServerRequest request) {
    if (request.method() == HttpMethod.GET) {
      MultiMap params = request.params();
      System.out.println(params.toString());
      String lat = params.get("lat");
      String lon = params.get("lon");
      System.out.printf("(lat, lon) = %s, %s", lat, lon);
      if (request.absoluteURI().contains("search")) {
        weatherDal.search(lat, lon).subscribe(res -> {
          System.out.println(res);
          request.response().end(res);
        }, Throwable::printStackTrace);
      } else {
        weatherService.fetch(lat, lon).subscribe(res -> request.response().end(EncodeUtil.extractFeaturesJson(res.body()).encodePrettily())
          , Throwable::printStackTrace);
      }
    } else if (request.method() == HttpMethod.POST) {
      request.body(ctx -> {
        weatherDal.insert(ctx.result().toJsonObject()).subscribe(res -> {
          request.response().end(res);
        }, Throwable::printStackTrace);
      });
    } else if (request.method() == HttpMethod.PUT) {
      MultiMap params = request.params();
      System.out.println(params.toString());
      String lat = params.get("lat");
      String lon = params.get("lon");
      System.out.printf("(lat, lon) = %s, %s", lat, lon);
      request.body(ctx -> {
        weatherDal.update(lat, lon, ctx.result().toJsonObject()).subscribe(res -> {
          request.response().end(res);
        }, Throwable::printStackTrace);
      });
    }
  }
}
