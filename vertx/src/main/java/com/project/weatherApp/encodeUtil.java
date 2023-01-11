package com.project.weatherApp;

import io.vertx.core.json.JsonObject;

import java.net.Inet4Address;
import java.util.LinkedHashMap;
import java.util.Map;

public class encodeUtil {
  public static LinkedHashMap<String, String> getDataMap(JsonObject jsonBody) throws IllegalArgumentException{
    System.out.println("Json body to map: " + jsonBody.toString());
    if (jsonBody == null){
      throw new IllegalArgumentException("Null Json object found");
    }
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
  public static JsonObject stringToJson(String str) throws IllegalArgumentException{
    if (str == null){
      throw new IllegalArgumentException("String is null");
    }
    JsonObject json = new JsonObject();
    json.put("data", str);
    return json;
  }
  public static JsonObject mapToJson(LinkedHashMap<String, String> dataMap) throws IllegalArgumentException{
    if (dataMap == null){
      throw new IllegalArgumentException("Map is null");
    }
    JsonObject json = new JsonObject();
    for (Map.Entry<String, String> entry : dataMap.entrySet()){
      String key = entry.getKey();
      String value = entry.getValue();
      System.out.printf("%s, %s%n", key, value);
      json.put(key, value);
    }
    return json;
  }
}
