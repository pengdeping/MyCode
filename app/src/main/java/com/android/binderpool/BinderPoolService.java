package com.android.binderpool;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Description: binder连接池Server
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-12-17
 */
public class BinderPoolService extends Service{
    private IBinderPoolManager iBinderPoolManager;

    //支持并发读写的list
    public CopyOnWriteArrayList<Weather> weathers = new CopyOnWriteArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        Weather nanshan = new Weather();
        nanshan.cityName = "南山";
        nanshan.temperature = 20.5;
        nanshan.humidity = 45;
        nanshan.weather = Weather.AllWeather.cloudy;

        Weather futian = new Weather();
        futian.cityName = "福田";
        futian.temperature = 21.5;
        futian.humidity = 48;
        futian.weather = Weather.AllWeather.rain;

        weathers.add(nanshan);
        weathers.add(futian);
        iBinderPoolManager = new BinderPoolManager(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return iBinderPoolManager.asBinder();
    }

    //获取Binder
    public IBinder queryBinder(int binderCode){
        IBinder binder = null;
        try {
            if (null != iBinderPoolManager){
                binder = iBinderPoolManager.queryBinder(binderCode);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return binder;
    }
}