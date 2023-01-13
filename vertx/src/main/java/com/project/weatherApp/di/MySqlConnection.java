package com.project.weatherApp.di;

import io.github.cdimascio.dotenv.Dotenv;
import io.vertx.core.Vertx;
import io.vertx.mysqlclient.MySQLConnectOptions;
import io.vertx.mysqlclient.MySQLPool;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.sqlclient.SqlClient;

public class MySqlConnection {
  Dotenv dotenv;
  Vertx vertx;
  MySQLConnectOptions connectOptions;
  PoolOptions poolOptions;
  public void load_env(){
    Dotenv dotenv = Dotenv.configure()
      .directory("/home/ishankumark/Documents/code/vertx/src/main/resources")
      .filename("env")
      .load();
  }
  MySqlConnection(Vertx vertx){
    this.vertx = vertx;
    connectOptions = new MySQLConnectOptions()
      .setPort(dotenv.get("SQL_PORT") == null ? 3306 : Integer.parseInt(dotenv.get("SQL_PORT")))
      .setHost(dotenv.get("SQL_HOST"))
      .setDatabase(dotenv.get("SQL_DATABASE"))
      .setUser(dotenv.get("SQL_USER"))
      .setPassword(dotenv.get("SQL_PASSWORD"))
      .setConnectTimeout(dotenv.get("SQL_TIMEOUT") == null ? 3000 : Integer.parseInt(dotenv.get("SQL_TIMEOUT")));

    poolOptions = new PoolOptions().setMaxSize(5);
  }
  public void query(String queryString){
    SqlClient sqlClient = MySQLPool.client(vertx, connectOptions, poolOptions);

  }
}
