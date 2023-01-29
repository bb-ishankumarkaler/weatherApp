package com.project.weatherApp.utils;

import com.project.weatherApp.MainVerticle;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.http.HttpServerRequest;
import io.vertx.reactivex.sqlclient.Row;
import io.vertx.reactivex.sqlclient.RowSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;

public class EncodeUtil {
  static final Logger LOGGER = LoggerFactory.getLogger(EncodeUtil.class);

  public static String getParam(HttpServerRequest request, String param){
    assert request != null;
    return (request.params() == null) ? null : request.params().get(param);
  }
  public static String rowSetToString(RowSet<Row> rSet) {
    assert rSet != null : "rowSetToString(null rSet)";
    LOGGER.debug(String.valueOf(rSet));
    JsonArray jsonArray = new JsonArray();
    for (Row row : rSet) {
      JsonObject jsonObject = row.toJson();
      jsonArray.add(jsonObject);
    }
    if (jsonArray.size() > 0) return jsonArray.getJsonObject(0).encodePrettily();
    else return jsonArray.encodePrettily();
  }
  public static JsonObject extractFeaturesJson(JsonObject object) {
    assert object != null;
    LinkedHashMap<String, String> mp = getDataMap(object);
    return mapToJson(mp);
  }

  public static LinkedHashMap<String, String> getDataMapFromRequestJson(JsonObject jsonBody){
    assert jsonBody != null;
    LinkedHashMap<String, String> dataMap = new LinkedHashMap<String, String>();
    dataMap.put("id", jsonBody.getString("id"));
    dataMap.put("name", jsonBody.getString("name"));
    dataMap.put("lat", jsonBody.getString("lat"));
    dataMap.put("lon", jsonBody.getString("lon"));
    dataMap.put("country_code", jsonBody.getString("country_code"));
    dataMap.put("weather", jsonBody.getString("weather"));
    dataMap.put("temp", jsonBody.getString("temp"));
    return dataMap;
  }

  public static LinkedHashMap<String, String> getDataMap(JsonObject jsonBody) {
    assert jsonBody != null;
    LOGGER.debug("Json body to map: " + jsonBody.encodePrettily());
    // search for raw data types java
    LinkedHashMap<String, String> dataMap = new LinkedHashMap<String, String>();
    // Get data fields fron JSON
    dataMap.put("id", jsonBody.getInteger("id").toString());
    dataMap.put("name", jsonBody.getString("name"));
    dataMap.put("lat", jsonBody.getJsonObject("coord").getFloat("lat").toString());
    dataMap.put("lon", jsonBody.getJsonObject("coord").getFloat("lon").toString());
    dataMap.put("country_code", jsonBody.getJsonObject("sys").getString("country"));
    dataMap.put("weather", jsonBody.getJsonArray("weather").getJsonObject(0).getString("main"));
    dataMap.put("temp", jsonBody.getJsonObject("main").getFloat("temp").toString());
    return dataMap;
  }

  public static JsonObject mapToJson(LinkedHashMap<String, String> dataMap){
    assert dataMap != null;
    JsonObject json = new JsonObject();
    for (Map.Entry<String, String> entry : dataMap.entrySet()) {
      String key = entry.getKey();
      String value = entry.getValue();
      LOGGER.debug(String.format("%s, %s%n", key, value));
      json.put(key, value);
    }
    return json;
  }
}
