// IWeatherManager.aidl
package com.skyworth.aidl;
import com.skyworth.aidl.Weather;
import com.skyworth.aidl.listener.IWeatherChangeListener;

interface IWeatherManager {
    List<Weather> getWeather();
    void addWeather(in Weather weather);
    void addListener(in IWeatherChangeListener listener);
    void removeListener(in IWeatherChangeListener listener);
}