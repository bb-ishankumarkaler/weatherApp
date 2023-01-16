package com.project.weatherApp.dal;

import com.project.weatherApp.di.MySqlConnection;
import io.reactivex.Single;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.Vertx;
import com.project.weatherApp.utils.EncodeUtil;

import java.util.LinkedHashMap;

public class WeatherDalImpl implements WeatherDal {
  MySqlConnection mySqlConnection;
  Vertx vertx;

  public WeatherDalImpl(Vertx vertx) {
    this.vertx = vertx;
    mySqlConnection = new MySqlConnection(vertx);
  }

  @Override
  public Single<String> update(String lat, String lon, JsonObject data) {
    LinkedHashMap<String, String> dataMap = EncodeUtil.getDataMapFromRequestJson(data);
    String updateQuery = String.format("UPDATE weather SET id=%s, name='%s', lat=%s, lon=%s, country_code='%s', weather='%s', temp=%s WHERE (lat=%s AND lon=%s)",
      dataMap.get("id"), dataMap.get("name"), dataMap.get("lat"), dataMap.get("lon"), dataMap.get("country_code"), dataMap.get("weather"),
      dataMap.get("temp"), dataMap.get("lat"), dataMap.get("lon"));
    return mySqlConnection.query(updateQuery).map(EncodeUtil::rowSetToString);
  }

  @Override
  public Single<String> insert(JsonObject data) {
    LinkedHashMap<String, String> dataMap = EncodeUtil.getDataMapFromRequestJson(data);
    String insertQuery = String.format("INSERT INTO weather VALUES (%s, '%s', %s, %s, '%s', '%s', %s)", dataMap.get("id"), dataMap.get("name"),
      dataMap.get("lat"), dataMap.get("lon"), dataMap.get("country_code"), dataMap.get("weather"), dataMap.get("temp"));
    return mySqlConnection.query(insertQuery).map(EncodeUtil::rowSetToString);
  }

  @Override
  public Single<String> search(String lat, String lon) {
    String searchQuery = String.format("SELECT * FROM weather WHERE lat=%s AND lon=%s", lat, lon);
    return mySqlConnection.query(searchQuery).map(EncodeUtil::rowSetToString);
  }
}
