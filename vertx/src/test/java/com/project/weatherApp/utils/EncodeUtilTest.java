package com.project.weatherApp.utils;

import static org.junit.jupiter.api.Assertions.*;

import io.vertx.core.http.impl.headers.HeadersMultiMap;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.MultiMap;
import io.vertx.reactivex.core.http.HttpServerRequest;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.LinkedHashMap;

class EncodeUtilTest {
  @Mock
  HttpServerRequest request;
  @BeforeEach
  public void setup(){
    MockitoAnnotations.openMocks(this);
  }
  @Test
  public void testGetDataMap(){
    // Create Json Object
    JsonObject jsonObject = new JsonObject();
    jsonObject.put("id", 1278609);
    jsonObject.put("name", "Anekal");
    jsonObject.put("coord", new JsonObject().put("lat", 77.1).put("lon", 76.1));
    jsonObject.put("sys", new JsonObject().put("country", "IN"));
    jsonObject.put("weather", new JsonArray().add(new JsonObject().put("main", "clear")));
    jsonObject.put("main", new JsonObject().put("temp", 298.8));

    Assertions.assertEquals("1278609", EncodeUtil.getDataMap(jsonObject).get("id"));
    Assertions.assertEquals("Anekal", EncodeUtil.getDataMap(jsonObject).get("name"));
    Assertions.assertEquals("77.1", EncodeUtil.getDataMap(jsonObject).get("lat"));
    Assertions.assertEquals("76.1", EncodeUtil.getDataMap(jsonObject).get("lon"));
    Assertions.assertEquals("IN", EncodeUtil.getDataMap(jsonObject).get("country_code"));
    Assertions.assertEquals("298.8", EncodeUtil.getDataMap(jsonObject).get("temp"));
    Assertions.assertEquals("clear", EncodeUtil.getDataMap(jsonObject).get("weather"));
  }
  @Test
  public void testMapToJson(){
    LinkedHashMap<String, String> map = new LinkedHashMap<>();
    map.put("id", "1278609");
    map.put("name", "Anekal");
    map.put("lat", "77.1");
    map.put("lon", "12.7");
    map.put("country_code", "IN");
    map.put("weather", "clear");
    map.put("temp", "298.85");
    Assertions.assertEquals("1278609", EncodeUtil.mapToJson(map).getString("id"));
    Assertions.assertEquals("Anekal", EncodeUtil.mapToJson(map).getString("name"));
    Assertions.assertEquals("77.1", EncodeUtil.mapToJson(map).getString("lat"));
    Assertions.assertEquals("12.7", EncodeUtil.mapToJson(map).getString("lon"));
    Assertions.assertEquals("IN", EncodeUtil.mapToJson(map).getString("country_code"));
    Assertions.assertEquals("clear", EncodeUtil.mapToJson(map).getString("weather"));
    Assertions.assertEquals("298.85", EncodeUtil.mapToJson(map).getString("temp"));

  }
  @Test
  public void testGetDataMapFromRequest(){
    JsonObject jsonObject = new JsonObject();
    jsonObject.put("id", "1278609");
    jsonObject.put("name", "Anekal");
    jsonObject.put("lat", "77.1");
    jsonObject.put("lon", "12.7");
    jsonObject.put("country_code", "IN");
    jsonObject.put("weather", "clear");
    jsonObject.put("temp", "298.85");
    Assertions.assertEquals("1278609", EncodeUtil.getDataMapFromRequestJson(jsonObject).get("id"));
    Assertions.assertEquals("Anekal", EncodeUtil.getDataMapFromRequestJson(jsonObject).get("name"));
    Assertions.assertEquals("77.1", EncodeUtil.getDataMapFromRequestJson(jsonObject).get("lat"));
    Assertions.assertEquals("12.7", EncodeUtil.getDataMapFromRequestJson(jsonObject).get("lon"));
    Assertions.assertEquals("IN", EncodeUtil.getDataMapFromRequestJson(jsonObject).get("country_code"));
    Assertions.assertEquals("clear", EncodeUtil.getDataMapFromRequestJson(jsonObject).get("weather"));
    Assertions.assertEquals("298.85", EncodeUtil.getDataMapFromRequestJson(jsonObject).get("temp"));
  }
  @Test
  public void testGetParamNullCheck(){
    Mockito.when(request.params()).thenReturn(null);
    Assertions.assertNull(EncodeUtil.getParam(request, "lat"));
  }
  @Test
  public void testGetParam(){
    MultiMap mp = MultiMap.newInstance(new HeadersMultiMap());
    mp.add("lon", "2");
    Mockito.when(request.params()).thenReturn(mp);
    Assertions.assertEquals("2", EncodeUtil.getParam(request, "lon"));
  }

}
