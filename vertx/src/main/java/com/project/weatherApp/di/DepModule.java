package com.project.weatherApp.di;

import com.project.weatherApp.dal.MySqlConnection;
import dagger.Module;
import dagger.Provides;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.web.Router;


@Module
public class DepModule {
  @Provides
  public Vertx provideVertx(){
    if (Vertx.currentContext() == null) return Vertx.vertx();
    return Vertx.currentContext().owner();
  }
  @Provides
  public MySqlConnection provideMySqlConnection(){
    return new MySqlConnection(provideVertx());
  }

  @Provides
  public Router provideRouter(){
    return Router.router(provideVertx());
  }
}
