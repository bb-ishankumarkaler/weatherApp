package com.project.weatherApp;

import static org.junit.jupiter.api.Assertions.*;

import com.project.weatherApp.dal.WeatherDal;
import com.project.weatherApp.di.DaggerDepComponent;
import com.project.weatherApp.di.DepComponent;
import com.project.weatherApp.service.WeatherService;
import io.reactivex.Single;
import io.vertx.core.http.HttpMethod;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.core.http.HttpServerRequest;
import kotlin.Pair;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

class ControllerTest {

  Controller controller;
  @Mock
  Vertx vertx;

  @Mock
  WeatherService weatherService;

  @Mock
  WeatherDal weatherDal;
  @Mock
  HttpServerRequest request;
  @BeforeEach
  public void setup() throws IllegalAccessException {
    MockitoAnnotations.openMocks(this);
    DepComponent component = DaggerDepComponent.create();
    controller = component.buildController();
    // controller = new Controller(vertx);
    FieldUtils.writeField(controller, "vertx", vertx, true);
    // FieldUtils.writeField(controller, "weatherDal", weatherDal, true);
    FieldUtils.writeField(controller, "weatherService", weatherService, true);
  }

  @Test
  public void testNullHttpRequest(){
    controller.handle(null);
    Mockito.verifyNoInteractions(request);

  }
  @Test
  public void testAllMethodInteractions(){
    controller.handle(request);
    Mockito.verify(request, Mockito.times(3)).method();
  }
  @Test
//  @Test(expected=NullPointerException.class)  Doesn't work why?
  public void testGETCalled(){
    Mockito.when(request.method()).thenReturn(HttpMethod.GET);
    assertThrows(AssertionError.class, () -> controller.handle(request));
    Mockito.verify(request, Mockito.times(1)).method();
  }
  @Test
  public void testPOSTCalled(){
    Mockito.when(request.method()).thenReturn(HttpMethod.POST);
    assertThrows(AssertionError.class, () -> controller.handle(request));
    Mockito.verify(request, Mockito.times(2)).method();
  }
  @Test
  public void testPUTCalled(){
    Mockito.when(request.method()).thenReturn(HttpMethod.PUT);
    assertThrows(AssertionError.class, () -> controller.handle(request));
    Mockito.verify(request, Mockito.times(3)).method();
  }
  @Test
  public void testSearchCalled() throws IllegalAccessException {
    Controller controller1 = Mockito.mock(Controller.class);
    FieldUtils.writeField(controller1, "weatherService", weatherService, true);
    FieldUtils.writeField(controller1, "vertx", vertx, true);
    Mockito.doCallRealMethod().when(controller1).handle(request);
    String searchURI = "http://localhost:8080/weatherApp/v1/search/weather?lat=20&lon=20";
    Mockito.when(request.method()).thenReturn(HttpMethod.GET);
    Mockito.when(controller1.getLatLon(request)).thenReturn(new Pair<>("20", "20"));
    Mockito.when(request.absoluteURI()).thenReturn(searchURI);
    Mockito.when(weatherService.search("20", "20")).thenReturn(Single.just("Search Response!"));
//    assertThrows(NullPointerException.class, () -> controller1.handle(request));
    controller1.handle(request);
    Mockito.verify(weatherService, Mockito.times(1)).search("20", "20");
  }
}
