package com.skyworth.aidl;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @Description: java类作用描述
 * @CreateDate: 2021/11/25 14:11
 */
public class Weather implements Parcelable {

    public String cityName;
    public double temperature;
    public double humidity;
    public AllWeather weather;

    public enum AllWeather{
        sunny,cloudy,rain,snowy
    }

    public Weather(Parcel in) {
        temperature = in.readDouble();
        humidity = in.readDouble();
        //写入枚举
        weather = AllWeather.values()[in.readInt()];
        cityName = in.readString();
    }

    public Weather(){}

    public final static Creator<Weather> CREATOR = new Creator<Weather>() {
        @Override
        public Weather createFromParcel(Parcel source) {
            return new Weather(source);
        }

        @Override
        public Weather[] newArray(int size) {
            return new Weather[size];
        }
    } ;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(temperature);
        dest.writeDouble(humidity);
        dest.writeInt(weather.ordinal());
        dest.writeString(cityName);
    }
}
