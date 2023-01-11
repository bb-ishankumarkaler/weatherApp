package com.project.weatherApp;

import io.vertx.core.Vertx;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.codec.BodyCodec;
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

import java.sql.Time;
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
      webClient.get(8080, "localhost", String.format("/data/%s/%s", p.getFirst(), p.getSecond())).send(testContext.succeeding(
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

  @Test
  @DisplayName("Test 2- get")
  @Timeout(5000)
  void verticle_put(Vertx vertx, VertxTestContext testContext) throws Throwable {
    Checkpoint requestSent = testContext.checkpoint(2);
    Checkpoint responseReceived = testContext.checkpoint(2);
    WebClient webClient = WebClient.create(vertx);

    List<Pair<Float, Float>> locations = new ArrayList<Pair<Float, Float>>();
    locations.add(new Pair(1.0, 2.0));
    locations.add(new Pair(3.0, 5.0));
    for (Pair<Float, Float> p:locations){
      webClient.put(8080, "localhost", String.format("/data/update/%s/%s", p.getFirst(), p.getSecond())).send(testContext.succeeding(
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
