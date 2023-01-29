package com.project.weatherApp;

import com.project.weatherApp.di.DaggerDepComponent;
import com.project.weatherApp.di.DepComponent;
import com.project.weatherApp.service.WeatherService;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.core.http.HttpServerRequest;
import com.project.weatherApp.utils.EncodeUtil;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.RoutingContext;
import kotlin.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public class Controller{
  private static final Logger LOGGER = LoggerFactory.getLogger(Controller.class);

  private final String PREFIX_V1 = "/weatherApp/v1/";
  private final String QUERY = PREFIX_V1 + "weather";
  private final String SEARCH = PREFIX_V1 + "search";
  private final String UPDATE = PREFIX_V1 + "update";


  Vertx vertx;
  WeatherService weatherService;
  Router router;
  private void init(){
    router.get(QUERY).handler(ctx -> {
      String lat = EncodeUtil.getParam(ctx.request(), "lat");
      String lon = EncodeUtil.getParam(ctx.request(), "lon");
      weatherService.fetch(lat, lon).subscribe(res -> {
        ctx.request().response().end(EncodeUtil.extractFeaturesJson(res.body()).encodePrettily());
      }, Throwable::printStackTrace);
    });
    router.get(SEARCH).handler(ctx -> {
      String lat = EncodeUtil.getParam(ctx.request(), "lat");
      String lon = EncodeUtil.getParam(ctx.request(), "lon");
      weatherService.search(lat, lon).subscribe(res -> {
        ctx.request().response().end(res);
      }, Throwable::printStackTrace);
    });
    router.post(UPDATE).handler(ctx -> {
      ctx.request().body(ctx2 -> {
        weatherService.insert(ctx2.result().toJsonObject()).subscribe(res -> {
          ctx.request().response().end(res);
        }, Throwable::printStackTrace);
      });
    });
    router.put(UPDATE).handler(ctx -> {
      String lat = EncodeUtil.getParam(ctx.request(), "lat");
      String lon = EncodeUtil.getParam(ctx.request(), "lon");
      ctx.request().body(ctx2 -> {
        weatherService.update(lat, lon, ctx2.result().toJsonObject()).subscribe(res -> {
          ctx.request().response().end(res);
        }, Throwable::printStackTrace);
      });
    });
  }
  @Inject
  Controller(Vertx vertx, Router router) {
    this.vertx = vertx;
    this.router = router;
    DepComponent component = DaggerDepComponent.create();
    weatherService = component.buildWeatherServiceImpl();
    init();
    // weatherService = new WeatherServiceImpl(vertx);
  }
  public Pair<String, String> getLatLon(HttpServerRequest request){
    return new Pair<>(EncodeUtil.getParam(request, "lat"),
      EncodeUtil.getParam(request, "lon"));
  }
//  @Override
//  public void handle(HttpServerRequest request) {
//    System.out.println(request);
//    if (request == null){
//      System.out.println("null http request");
//      return;
//    }
//
//    if (request.method() == HttpMethod.GET) {
//        Pair<String, String> pair = getLatLon(request);
//        String lat = pair.getFirst();
//        String lon = pair.getSecond();
//        System.out.println(lat + " " + lon);
////      String lat = EncodeUtil.getParam(request, "lat");
////      String lon = EncodeUtil.getParam(request, "lon");
//      assert lat != null;
//      assert lon != null;
//      if (request.absoluteURI().contains("search")) {
//        weatherService.search(lat, lon).subscribe(res -> {
//          if (request.response() != null) request.response().end(res);
//        }, Throwable::printStackTrace);
//      } else {
//        weatherService.fetch(lat, lon).subscribe(res -> request.response().end(EncodeUtil.extractFeaturesJson(res.body()).encodePrettily())
//          , Throwable::printStackTrace);
//      }
//    } else if (request.method() == HttpMethod.POST) {
//      String lat = EncodeUtil.getParam(request, "lat");
//      String lon = EncodeUtil.getParam(request, "lon");
//      assert lat != null;
//      assert lon != null;
//      request.body(ctx -> {
//        weatherService.insert(ctx.result().toJsonObject()).subscribe(res -> {
//          request.response().end(res);
//        }, Throwable::printStackTrace);
//      });
//    } else if (request.method() == HttpMethod.PUT) {
//      String lat = EncodeUtil.getParam(request, "lat");
//      String lon = EncodeUtil.getParam(request, "lon");
//      assert lat != null;
//      assert lon != null;
//      request.body(ctx -> {
//        weatherService.update(lat, lon, ctx.result().toJsonObject()).subscribe(res -> {
//          request.response().end(res);
//        }, Throwable::printStackTrace);
//      });
//    }
//  }
}
