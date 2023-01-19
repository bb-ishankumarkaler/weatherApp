package com.project.weatherApp.dal;

import io.reactivex.Single;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.sqlclient.Row;
import io.vertx.reactivex.sqlclient.RowSet;
import io.vertx.reactivex.sqlclient.SqlClient;

import javax.inject.Inject;
public class WeatherDalImpl implements WeatherDal {
  MySqlConnection mySqlConnection;
  Vertx vertx;
  @Inject
  public WeatherDalImpl(Vertx vertx, MySqlConnection mySqlConnection){
    this.vertx = vertx;
    // this.vertx = Vertx.currentContext() == null ? Vertx.vertx():Vertx.currentContext().owner();
    this.mySqlConnection = mySqlConnection;
  }
//  @Inject
//  public WeatherDalImpl(Vertx vertx) {
//    this.vertx = vertx;
//    mySqlConnection = new MySqlConnection(vertx);
//  }
  @Override
  public Single<RowSet<Row>> query(String queryString) {
    SqlClient sqlClient = mySqlConnection.getClient();
    System.out.println(queryString);
    return sqlClient.query(queryString).rxExecute();
  }
}
