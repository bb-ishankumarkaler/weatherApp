package com.project.weatherApp.dal;

import io.reactivex.Single;
import io.vertx.core.json.JsonObject;

public interface WeatherDal {
  Single<String> update(String lat, String lon, JsonObject data);

  Single<String> insert(JsonObject data);

  Single<String> search(String lat, String lon);
}
