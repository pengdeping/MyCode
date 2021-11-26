package com.skyworth.aidl;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.Nullable;

import com.skyworth.aidl.listener.IWeatherChangeListener;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @Description: java类作用描述
 * @CreateDate: 2021/11/25 14:29
 */
public class WeatherManagerService extends Service {

    private static final String TAG = WeatherManagerService.class.getSimpleName();
    public CopyOnWriteArrayList<Weather> weathers = new CopyOnWriteArrayList<>();
    public RemoteCallbackList<IWeatherChangeListener> listeners = new RemoteCallbackList<>();

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
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, START_STICKY, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
//        int permission = checkCallingPermission("com.android.permission.WRITEWEATHERPERMISSION");
//        //检测客户端是否声明权限
//        if (permission == PackageManager.PERMISSION_DENIED){
//            L.e("permission denied");
//            return null;
//        }
        return mBinder;
    }

    private Binder mBinder = new IWeatherManager.Stub() {
        @Override
        public List<Weather> getWeather() throws RemoteException {
            Log.i(TAG,"server returns all of the weathers");
            return weathers;
        }

        @Override
        public void addWeather(Weather weather) throws RemoteException {
            weathers.add(weather);
            Log.i(TAG,"server add new Weather:" + weather.cityName);

            int count = listeners.beginBroadcast();
            for (int i=0; i<count;i++){
                IWeatherChangeListener listener = listeners.getBroadcastItem(i);
                listener.onWeatherChange(weather);
            }
            listeners.finishBroadcast();

        }

        @Override
        public void addListener(IWeatherChangeListener listener) throws RemoteException {
            Log.i(TAG,"server adding listener");
            listeners.register(listener);
        }

        @Override
        public void removeListener(IWeatherChangeListener listener) throws RemoteException {
            Log.i(TAG,"server removing listener");
            listeners.unregister(listener);
        }

        @Override
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags)
                throws RemoteException {
            int permission = checkCallingPermission("com.android.permission.WRITEWEATHERPERMISSION");
            //检测客户端是否有申明权限
            if (permission == PackageManager.PERMISSION_DENIED){
                Log.e(TAG,"permission denied");
                return false;
            }
            Log.i(TAG,"permission granted");
            String[] packages = getPackageManager().getPackagesForUid(getCallingUid());
            if (packages != null && packages.length > 0){
                String pkgName = packages[0];
                if (!pkgName.startsWith("com.skyworth")){
                    Log.i(TAG, "package name not accept");
                    return false;
                }
                Log.i(TAG, "onTransact: package name accept");
            }
            return super.onTransact(code, data, reply, IBinder.FLAG_ONEWAY);
        }
    };
}
