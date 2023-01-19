package com.project.weatherApp.service;

import static org.junit.jupiter.api.Assertions.*;

import com.project.weatherApp.dal.WeatherDal;
import com.project.weatherApp.utils.EncodeUtil;
import io.reactivex.Single;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.buffer.Buffer;
import io.vertx.reactivex.ext.web.client.HttpRequest;
import io.vertx.reactivex.ext.web.client.WebClient;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.web.client.HttpResponse;
import io.vertx.reactivex.ext.web.codec.BodyCodec;
import io.vertx.reactivex.sqlclient.Row;
import io.vertx.reactivex.sqlclient.RowSet;
import io.vertx.reactivex.sqlclient.SqlResult;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.LinkedHashMap;

class WeatherServiceImplTest {

  WeatherService weatherService;
  @Mock
  Vertx vertx;
  @Mock
  WeatherDal dal;

  @Mock
  WebClient webClient;

//  @Mock
//  String URI;

  @Mock
  HttpRequest<Buffer> bufferHttpRequest;

  @Mock
  HttpRequest<JsonObject> jsonObjectHttpRequest;
  @Mock
  HttpResponse<JsonObject> jsonObjectHttpResponse;
  @Mock
  LinkedHashMap<String, String> dataMap;
  @Mock
  RowSet<Row> rowSet;
  @Mock
  Single<RowSet<Row>> rowSetSingle;
  @Mock
  JsonObject jsonObject;
  @Mock
  Single<String> stringSingle;
  @BeforeEach
  public void setup() throws IllegalAccessException {
    MockitoAnnotations.openMocks(this);
    weatherService = new WeatherServiceImpl(vertx);
    FieldUtils.writeField(weatherService, "dal", dal, true);
  }
  @Test
  public void testFetch(){
    String URI = "";
    try (MockedStatic<WebClient> webClientMockedStatic = Mockito.mockStatic(WebClient.class)){
      webClientMockedStatic.when(() -> WebClient.create(vertx)).thenReturn(webClient);
      // Mockito.when(weatherService.getFetchURI(Mockito.anyString(), Mockito.anyString())).thenReturn(URI);
      Mockito.when(webClient.get(Mockito.anyString(), Mockito.anyString())).thenReturn(bufferHttpRequest);
      Mockito.when(bufferHttpRequest.as(BodyCodec.jsonObject())).thenReturn(jsonObjectHttpRequest);
      Mockito.when(jsonObjectHttpRequest.rxSend()).thenReturn(Single.just(jsonObjectHttpResponse));
      Single<HttpResponse<JsonObject>> responseSingle = weatherService.fetch("20", "20");
      Assertions.assertNotNull(responseSingle);
    }
  }
  @Test
  public void testInsert(){
    Mockito.when(dal.query(Mockito.anyString())).thenReturn(rowSetSingle);
    Mockito.when(rowSetSingle.map(EncodeUtil::rowSetToString)).thenReturn(stringSingle);
    JsonObject jsonObject = new JsonObject();

    jsonObject.put("id", "1278609");
    jsonObject.put("name", "Anekal");
    jsonObject.put("lat", "77.1");
    jsonObject.put("lon", "12.7");
    jsonObject.put("country_code", "IN");
    jsonObject.put("weather", "clear");
    jsonObject.put("temp", "298.85");

    Single<String> value = weatherService.insert(jsonObject);
    Assertions.assertNotNull(value);
  }

}
