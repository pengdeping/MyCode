package com.sky.mvpdemo.request;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * @author
 * @description 请求接口
 */
public interface ApiService {

    /**
     * 请求天气接口
     * @param cityId 城市
     * @return Call
     */
    @GET("data/cityinfo/{cityId}.html")
    Call<WeatherBean> requestWeather(@Path("cityId") String cityId);
}
