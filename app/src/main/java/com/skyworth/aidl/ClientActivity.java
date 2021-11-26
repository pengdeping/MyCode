package com.skyworth.aidl;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.skyworth.aidl.listener.IWeatherChangeListener;
import com.skyworth.myapp.R;

import java.util.List;

/**
 * @Description: java类作用描述
 * @CreateDate: 2021/11/25 15:52
 */
public class ClientActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = ClientActivity.class.getSimpleName();
    private ServiceConnection serviceConnection = null;
    private IBinder.DeathRecipient deathRecipient = null;
    private IWeatherChangeListener listener = null;
    private IWeatherManager weatherManager;
    private TextView tv_content;
    private TextView tv_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        findViewById(R.id.btn_add).setOnClickListener(this);
        findViewById(R.id.btn_query).setOnClickListener(this);
        findViewById(R.id.btn_remove_listener).setOnClickListener(this);
        tv_content = (TextView) findViewById(R.id.tv_content);
        tv_add = (TextView) findViewById(R.id.tv_add);
        listener = new IWeatherChangeListener.Stub() {

            @Override
            public void onWeatherChange(Weather newWeather) throws RemoteException {
                Log.i(TAG,
                        "client has been notified that " + newWeather.cityName + " has been added");
                tv_add.setText(newWeather.cityName + "has been added");
            }
        };
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                weatherManager = IWeatherManager.Stub.asInterface(service);
                try {
                    weatherManager.asBinder().linkToDeath(deathRecipient, 0);
                    weatherManager.addListener(listener);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                weatherManager = null;
            }
        };

        deathRecipient = new IBinder.DeathRecipient() {
            @Override
            public void binderDied() {
                //移出之前的死亡容器
                weatherManager.asBinder().unlinkToDeath(deathRecipient, 0);
                weatherManager = null;

                //重新连接
                bindServer();
            }
        };
        bindServer();
    }

    private void bindServer() {
        Intent intent = new Intent(this, WeatherManagerService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_query) {
            try {
                //调用远程服务端接口时，客户端进程会挂起，勿在主线程中调用耗时远程操作
                Log.i(TAG, "client is getting weather");
                List<Weather> weathers = weatherManager.getWeather();
                Log.i(TAG, "client has gotten weather");
                StringBuilder sb = new StringBuilder();
                for (Weather weather : weathers) {
                    sb.append(weather.cityName).append("\n");
                    sb.append("humidity:").append(weather.humidity)
                            .append("temperature").append(weather.temperature)
                            .append("weather").append(weather.weather).append("\n");
                }
                tv_content.setText(sb);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else if (v.getId() == R.id.btn_add) {
            Weather weather = new Weather();
            weather.weather = Weather.AllWeather.cloudy;
            weather.humidity = 25.5;
            weather.temperature = 19.5;
            weather.cityName = "罗湖";
            try {
                //调用远程服务端接口时，客户端进程会挂起，勿在主线程中调用耗时远程操作
                Log.i(TAG, "client is adding weather " + weather.cityName);
                weatherManager.addWeather(weather);
                Log.i(TAG, "client has added weather " + weather.cityName);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else if (v.getId() == R.id.btn_remove_listener) {
            try {
                weatherManager.removeListener(listener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
        try {
            weatherManager.asBinder().linkToDeath(deathRecipient, 0);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
