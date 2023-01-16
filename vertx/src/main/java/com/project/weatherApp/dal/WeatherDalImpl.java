package com.project.weatherApp.dal;

import io.reactivex.Single;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.Vertx;
import com.project.weatherApp.utils.EncodeUtil;
import io.vertx.reactivex.mysqlclient.MySQLPool;
import io.vertx.reactivex.sqlclient.Row;
import io.vertx.reactivex.sqlclient.RowSet;
import io.vertx.reactivex.sqlclient.SqlClient;

import java.util.LinkedHashMap;

public class WeatherDalImpl implements WeatherDal {
  MySqlConnection mySqlConnection;
  Vertx vertx;

  public WeatherDalImpl(Vertx vertx) {
    this.vertx = vertx;
    mySqlConnection = new MySqlConnection(vertx);
  }
  @Override
  public Single<RowSet<Row>> query(String queryString) {
    SqlClient sqlClient = mySqlConnection.getClient();
    System.out.println(queryString);
    return sqlClient.query(queryString).rxExecute();
  }
}
