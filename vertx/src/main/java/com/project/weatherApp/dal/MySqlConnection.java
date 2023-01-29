package com.project.weatherApp.dal;

import io.github.cdimascio.dotenv.Dotenv;
import io.vertx.reactivex.core.Vertx;
import io.vertx.mysqlclient.MySQLConnectOptions;
import io.vertx.reactivex.mysqlclient.MySQLPool;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.reactivex.sqlclient.SqlClient;

import javax.inject.Inject;

public class MySqlConnection {
  Dotenv dotenv;
  Vertx vertx;
  MySQLConnectOptions connectOptions;
  PoolOptions poolOptions;

  public void init() {
    dotenv = Dotenv.configure()
      .directory("/home/ishankumark/Documents/code/vertx/src/main/resources")
      .filename("env")
      .load();
    connectOptions = new MySQLConnectOptions()
      .setPort(dotenv.get("SQL_PORT") == null ? 3306 : Integer.parseInt(dotenv.get("SQL_PORT")))
      .setHost(dotenv.get("SQL_HOST"))
      .setDatabase(dotenv.get("SQL_DATABASE"))
      .setUser(dotenv.get("SQL_USER"))
      .setPassword(dotenv.get("SQL_PASSWORD"))
      .setConnectTimeout(dotenv.get("SQL_TIMEOUT") == null ? 3000 : Integer.parseInt(dotenv.get("SQL_TIMEOUT")))
      .setConnectTimeout(1000);

    poolOptions = new PoolOptions().setMaxSize(5).setMaxWaitQueueSize(1000);
  }
  @Inject
  public MySqlConnection(Vertx vertx) {
    this.vertx = vertx;
    init();
  }
  public MySqlConnection() {
    this.vertx = Vertx.currentContext().owner();
    init();
  }

  public SqlClient getClient(){
    return MySQLPool.client(vertx, connectOptions, poolOptions);
  }
}
