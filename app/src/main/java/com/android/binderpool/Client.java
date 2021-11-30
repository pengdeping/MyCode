package com.android.binderpool;

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
import android.widget.Toast;

import com.skyworth.aidl.BaseActivity;
import com.skyworth.myapp.R;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-12-17
 */
public class Client extends BaseActivity implements View.OnClickListener{

    private static final String TAG = "Client";
    private ServiceConnection serviceConnection = null;
    private IBinder.DeathRecipient deathRecipient = null;
    private IBinderPoolManager binderPoolManager;
    private IWeatherManager weatherManager;
    private IComputerManager computerManager;
    private TextView tv_content;

    private CountDownLatch mDownLatch ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        findViewById(R.id.btn_add).setOnClickListener(this);
        findViewById(R.id.btn_query).setOnClickListener(this);
        findViewById(R.id.btn_average).setOnClickListener(this);
        tv_content = (TextView) findViewById(R.id.tv_content);
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                try {
                    binderPoolManager = IBinderPoolManager.Stub.asInterface(service);
                    binderPoolManager.asBinder().linkToDeath(deathRecipient, 0);
//                    weatherManager = IWeatherManager.Stub.asInterface(
//                            binderPoolManager.queryBinder(BinderPoolManager.CODE_WEATHER));
//                    computerManager = IComputerManager.Stub.asInterface(
//                            binderPoolManager.queryBinder(BinderPoolManager.CODE_COMPUTER));
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                mDownLatch.countDown();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                binderPoolManager = null;
            }
        };

        deathRecipient = new IBinder.DeathRecipient() {
            @Override
            public void binderDied() {
                //移出之前的死亡容器
                binderPoolManager.asBinder().unlinkToDeath(deathRecipient, 0);
                binderPoolManager = null;

                //重新连接
                bindServer();
            }
        };
        bindServer();
    }

    private void bindServer(){
        mDownLatch = new CountDownLatch(1);
        Intent intent = new Intent(this, BinderPoolService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        try {
            mDownLatch.await();//2.准备:所有线程准备
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Log.e(TAG, "bind Pool Service！");
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_query){
            try {
                //调用远程服务端接口时，客户端进程会挂起，勿在主线程中调用耗时远程操作
                List<Weather> weathers = weatherManager.getWeather();
                StringBuilder sb = new StringBuilder();
                for (Weather weather : weathers){
                    sb.append(weather.cityName).append("\n");
                    sb.append("humidity:").append(weather.humidity)
                            .append("temperature").append(weather.temperature)
                            .append("weather").append(weather.weather).append("\n");
                }
                tv_content.setText(sb);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }else if (v.getId() == R.id.btn_add){
            Weather weather = new Weather();
            weather.weather = Weather.AllWeather.cloudy;
            weather.humidity = 25.5;
            weather.temperature = 19.5;
            weather.cityName = "罗湖";
            try {
                //调用远程服务端接口时，客户端进程会挂起，勿在主线程中调用耗时远程操作
                weatherManager.addWeather(weather);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }else if (v.getId() == R.id.btn_average){
            try {
                Toast.makeText(getApplicationContext(),computerManager.computeAverageTemperature(weatherManager.getWeather())+"",Toast.LENGTH_SHORT).show();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
        binderPoolManager.asBinder().unlinkToDeath(deathRecipient, 0);
    }
}