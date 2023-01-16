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
    assert request != null;
    String lat = EncodeUtil.getParam(request, "lat");
    String lon = EncodeUtil.getParam(request, "lon");
    assert lat != null;
    assert lon != null;
    if (request.method() == HttpMethod.GET) {
      if (request.absoluteURI().contains("search")) {
        weatherService.search(lat, lon).subscribe(res -> {
          System.out.println(res);
          request.response().end(res);
        }, Throwable::printStackTrace);
      } else {
        weatherService.fetch(lat, lon).subscribe(res -> request.response().end(EncodeUtil.extractFeaturesJson(res.body()).encodePrettily())
          , Throwable::printStackTrace);
      }
    } else if (request.method() == HttpMethod.POST) {
      request.body(ctx -> {
        weatherService.insert(ctx.result().toJsonObject()).subscribe(res -> {
          request.response().end(res);
        }, Throwable::printStackTrace);
      });
    } else if (request.method() == HttpMethod.PUT) {
      request.body(ctx -> {
        weatherService.update(lat, lon, ctx.result().toJsonObject()).subscribe(res -> {
          request.response().end(res);
        }, Throwable::printStackTrace);
      });
    }
  }
}
