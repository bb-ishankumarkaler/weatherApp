package com.project.weatherApp.dal;

import com.project.weatherApp.di.DaggerDepComponent;
import com.project.weatherApp.di.DepComponent;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import io.reactivex.Single;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.sqlclient.Query;
import io.vertx.reactivex.sqlclient.Row;
import io.vertx.reactivex.sqlclient.RowSet;
import io.vertx.reactivex.sqlclient.SqlClient;

public class WeatherDalImplTest {

  private WeatherDal weatherDal;

  @Mock
  private MySqlConnection mySqlConnection;

  @Mock
  private SqlClient sqlClient;

  @Mock
  private Vertx vertx;

  @Mock
  private Query<RowSet<Row>> query;

  @Mock
  private RowSet<Row> rowSet;

  @BeforeEach
  public void setUp() throws IllegalAccessException {
    MockitoAnnotations.openMocks(this);
    DepComponent component = DaggerDepComponent.create();
    weatherDal = component.buildWeatherDalImpl();
//    weatherService = new WeatherServiceImpl(vertx);
    FieldUtils.writeField(weatherDal, "vertx", vertx, true);
    FieldUtils.writeField(weatherDal, "mySqlConnection", mySqlConnection, true);
  }
  @Test
  public void testQueryRows(){
    Mockito.when(mySqlConnection.getClient()).thenReturn(sqlClient);
    Mockito.when(sqlClient.query("SELECT * FROM weather")).thenReturn(query);
    Mockito.when(query.rxExecute()).thenReturn(Single.just(rowSet)); /*Custom rowSet to test, like maybe new RowSet<Row>(8))*/
    Single<RowSet<Row>> value = weatherDal.query("SELECT * FROM weather");
    System.out.println("test query rows");
    Assertions.assertNotNull(value);

  }
  @Test
  public void testQuery() {
    Mockito.when(mySqlConnection.getClient()).thenReturn(sqlClient);
    Mockito.when(sqlClient.query(Mockito.anyString())).thenReturn(query);
    Mockito.when(query.rxExecute()).thenReturn(Single.just(rowSet));
    Single<RowSet<Row>> value = weatherDal.query("test");
    Assertions.assertNotNull(value);
  }
}
