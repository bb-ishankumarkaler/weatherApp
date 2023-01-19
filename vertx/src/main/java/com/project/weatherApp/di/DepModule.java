package com.project.weatherApp.di;

import com.project.weatherApp.dal.MySqlConnection;
import dagger.Module;
import dagger.Provides;
import io.vertx.reactivex.core.Vertx;


@Module
public class DepModule {
  @Provides
  public MySqlConnection provideMySqlConnection(){
    return new MySqlConnection(provideVertx());
  }
  @Provides
  public Vertx provideVertx(){
    if (Vertx.currentContext() == null) return Vertx.vertx();
    return Vertx.currentContext().owner();
  }
}
