package com.project.weatherApp;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.junit5.Checkpoint;
import io.vertx.junit5.Timeout;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import kotlin.Pair;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
@DisplayName("Basic tests!!")
@ExtendWith(VertxExtension.class)
public class TestMainVerticle {
//  Vertx vertx;
  long startTime;
  @BeforeEach
  void deploy_verticle(Vertx vertx, VertxTestContext testContext) {
    vertx.deployVerticle(new MainVerticle(), testContext.succeeding(id -> testContext.completeNow()));
    startTime = System.currentTimeMillis();
  }

  @Test
  @DisplayName("Test 1- get")
  @Timeout(5000)
  void verticle_get(Vertx vertx, VertxTestContext testContext) throws Throwable {
    Checkpoint requestSent = testContext.checkpoint(2);
    Checkpoint responseReceived = testContext.checkpoint(2);
    WebClient webClient = WebClient.create(vertx);

    List<Pair<Float, Float>> locations = new ArrayList<Pair<Float, Float>>();
    locations.add(new Pair(1.0, 2.0));
    locations.add(new Pair(3.0, 5.0));
    for (Pair<Float, Float> p:locations){
      webClient.get(8080, "localhost", String.format("/weatherApp/v1/weather?lat=%s&lon=%s", p.getFirst(), p.getSecond())).send(testContext.succeeding(
        resp -> {
          requestSent.flag();
          testContext.verify(() -> {
            System.out.println(resp.statusCode());
            assertThat(resp.statusCode()).isEqualTo(200);
            responseReceived.flag();
          });
        }
      ));
    }
  }
  // TODO: add body to request
  @Test
  @DisplayName("Test 2- put")
  @Timeout(5000)
  void verticle_put(Vertx vertx, VertxTestContext testContext) throws Throwable {
    Checkpoint requestSent = testContext.checkpoint(1);
    Checkpoint responseReceived = testContext.checkpoint(1);
    WebClient webClient = WebClient.create(vertx);
    JsonObject jsonObject = new JsonObject();
    jsonObject.put("id", "1278609");
    jsonObject.put("name", "Anekal");
    jsonObject.put("lat", "77.1");
    jsonObject.put("lon", "12.7");
    jsonObject.put("country_code", "IN");
    jsonObject.put("weather", "clear");
    jsonObject.put("temp", "298.85");
    List<Pair<Float, Float>> locations = new ArrayList<Pair<Float, Float>>();
    locations.add(new Pair(77.1, 12.7));
    for (Pair<Float, Float> p:locations){
      webClient.put(8080, "localhost", String.format("/weatherApp/v1/weather?lat=%s&lon=%s", p.getFirst(), p.getSecond()))
        .sendJsonObject(jsonObject, testContext.succeeding(
        resp -> {
          requestSent.flag();
          testContext.verify(() -> {
            System.out.println(resp.statusCode());
            assertThat(resp.statusCode()).isEqualTo(200);
            responseReceived.flag();
          });
        }
      ));
    }
  }
  @AfterEach
  void cleanup(Vertx vertx){
    long timeElasped = System.currentTimeMillis() - startTime;
    System.out.println("Time elasped: " + timeElasped);
    vertx.close();
  }
}
