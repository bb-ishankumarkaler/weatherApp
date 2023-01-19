package com.project.weatherApp.di;

import com.project.weatherApp.Controller;
import com.project.weatherApp.dal.WeatherDalImpl;
import com.project.weatherApp.service.WeatherServiceImpl;
import dagger.Component;

@Component(modules = DepModule.class)
public interface DepComponent {
  WeatherDalImpl buildWeatherDalImpl();

  WeatherServiceImpl buildWeatherServiceImpl();
  Controller buildController();
}
