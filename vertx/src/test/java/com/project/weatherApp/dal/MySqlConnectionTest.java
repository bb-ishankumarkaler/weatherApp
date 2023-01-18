package com.project.weatherApp.dal;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import io.github.cdimascio.dotenv.Dotenv;
import io.vertx.mysqlclient.MySQLConnectOptions;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.mysqlclient.MySQLPool;
import io.vertx.reactivex.sqlclient.SqlClient;
import io.vertx.sqlclient.PoolOptions;

public class MySqlConnectionTest {
  @Mock
  private Dotenv dotenv;

  @Mock
  private Vertx vertx;

  @Mock
  private MySQLConnectOptions connectOptions;

  @Mock
  private PoolOptions poolOptions;

  private MySqlConnection mySqlConnection;

  @Mock
  private SqlClient sqlClient;

  @BeforeEach
  public void setUp() throws IllegalAccessException {
    MockitoAnnotations.openMocks(this);
    mySqlConnection = new MySqlConnection(vertx);
    FieldUtils.writeField(mySqlConnection, "dotenv", dotenv, true);
    FieldUtils.writeField(mySqlConnection, "connectOptions", connectOptions, true);
    FieldUtils.writeField(mySqlConnection, "poolOptions", poolOptions, true);
  }

  @Test
  public void testGetClient() {
    try (MockedStatic<MySQLPool> poolMockedStatic = Mockito.mockStatic(MySQLPool.class)) {
      poolMockedStatic.when(() -> MySQLPool.client(vertx, connectOptions, poolOptions)).thenReturn(sqlClient);

      SqlClient client = mySqlConnection.getClient();
      Assertions.assertNotNull(client);
    }
  }
}
