package com.project.weatherApp.dal;

import io.reactivex.Single;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.sqlclient.Row;
import io.vertx.reactivex.sqlclient.RowSet;

public interface WeatherDal {
  Single<RowSet<Row>> query(String queryString);
}
