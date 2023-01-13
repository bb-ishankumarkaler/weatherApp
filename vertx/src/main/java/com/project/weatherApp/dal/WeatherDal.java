package com.project.weatherApp.dal;

import io.vertx.core.json.JsonObject;

public interface WeatherDal {
  public void update(String lat, String lon, JsonObject data);
}
