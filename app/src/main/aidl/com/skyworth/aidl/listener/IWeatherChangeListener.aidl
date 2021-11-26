// IWeatherChangeListener.aidl
package com.skyworth.aidl.listener;
import com.skyworth.aidl.Weather;

interface IWeatherChangeListener {
    void onWeatherChange(in Weather newWeather);
}