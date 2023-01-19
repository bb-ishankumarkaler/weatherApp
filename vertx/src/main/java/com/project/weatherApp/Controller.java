package com.project.weatherApp;

import com.project.weatherApp.di.DaggerDepComponent;
import com.project.weatherApp.di.DepComponent;
import com.project.weatherApp.service.WeatherService;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.core.http.HttpServerRequest;
import com.project.weatherApp.utils.EncodeUtil;
import kotlin.Pair;

import javax.inject.Inject;

public class Controller implements Handler<HttpServerRequest> {
  Vertx vertx;
  WeatherService weatherService;
  @Inject
  Controller(Vertx vertx) {
    this.vertx = vertx;
    DepComponent component = DaggerDepComponent.create();
    weatherService = component.buildWeatherServiceImpl();
    // weatherService = new WeatherServiceImpl(vertx);
  }
  public Pair<String, String> getLatLon(HttpServerRequest request){
    return new Pair<>(EncodeUtil.getParam(request, "lat"),
      EncodeUtil.getParam(request, "lon"));
  }
  @Override
  public void handle(HttpServerRequest request) {
    System.out.println(request);
    if (request == null){
      System.out.println("null http request");
      return;
    }

    if (request.method() == HttpMethod.GET) {
        Pair<String, String> pair = getLatLon(request);
        String lat = pair.getFirst();
        String lon = pair.getSecond();
        System.out.println(lat + " " + lon);
//      String lat = EncodeUtil.getParam(request, "lat");
//      String lon = EncodeUtil.getParam(request, "lon");
      assert lat != null;
      assert lon != null;
      if (request.absoluteURI().contains("search")) {
        weatherService.search(lat, lon).subscribe(res -> {
          if (request.response() != null) request.response().end(res);
        }, Throwable::printStackTrace);
      } else {
        weatherService.fetch(lat, lon).subscribe(res -> request.response().end(EncodeUtil.extractFeaturesJson(res.body()).encodePrettily())
          , Throwable::printStackTrace);
      }
    } else if (request.method() == HttpMethod.POST) {
      String lat = EncodeUtil.getParam(request, "lat");
      String lon = EncodeUtil.getParam(request, "lon");
      assert lat != null;
      assert lon != null;
      request.body(ctx -> {
        weatherService.insert(ctx.result().toJsonObject()).subscribe(res -> {
          request.response().end(res);
        }, Throwable::printStackTrace);
      });
    } else if (request.method() == HttpMethod.PUT) {
      String lat = EncodeUtil.getParam(request, "lat");
      String lon = EncodeUtil.getParam(request, "lon");
      assert lat != null;
      assert lon != null;
      request.body(ctx -> {
        weatherService.update(lat, lon, ctx.result().toJsonObject()).subscribe(res -> {
          request.response().end(res);
        }, Throwable::printStackTrace);
      });
    }
  }
}
