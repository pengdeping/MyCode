package com.android.binderpool;

import static android.os.Binder.getCallingUid;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Description: binder连接池Server
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-12-17
 */
public class BinderPoolService extends Service{
    private static final String TAG = "BinderPoolService";

    private IBinderPoolManager iBinderPoolManager;
    public static final int CODE_WEATHER = 1;
    public static final int CODE_COMPUTER = 2;


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
//        int permission = checkCallingPermission("com.android.permission.WRITEWEATHERPERMISSION");
//        //检测客户端是否声明权限
//        if (permission == PackageManager.PERMISSION_DENIED){
//            L.e("permission denied");
//            return null;
//        }
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

    //静态内部类,软引用外部Service
    public static class BinderPoolManager extends IBinderPoolManager.Stub {

        final WeakReference<BinderPoolService> mService;

        public BinderPoolManager(BinderPoolService binderPoolService) {
            mService = new WeakReference<>(binderPoolService);
        }

        @Override
        public IBinder queryBinder(int binderCode) throws RemoteException {
            switch (binderCode){
                case CODE_WEATHER:
                    return new IWeatherManager.Stub(){

                        @Override
                        public List<Weather> getWeather() throws RemoteException {
                            return mService.get().weathers;
                        }

                        @Override
                        public void addWeather(Weather weather) throws RemoteException {
                            mService.get().weathers.add(weather);
                        }
                    };
                case CODE_COMPUTER:
                    return new IComputerManager.Stub() {
                        @Override
                        public double computeAverageTemperature(List<Weather> weathers) throws RemoteException {
                            double sum = 0;
                            for (int i=0; i<weathers.size(); i++){
                                sum += weathers.get(i).temperature;
                            }
                            return sum/weathers.size();
                        }
                    };
                default:
                    return null;
            }
        }

        @Override
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags)
                throws RemoteException {
            int permission = mService.get().checkPermission();
            //检测客户端是否有申明权限
            if (permission == PackageManager.PERMISSION_DENIED){
                Log.e(TAG,"permission denied");
                return false;
            }
            Log.i(TAG,"permission granted");
            String[] packages = mService.get().getPackages();
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
    }

    private int checkPermission(){
        return checkCallingPermission("com.android.permission.WRITEWEATHERPERMISSION");
    }

    private String[] getPackages(){
        return getPackageManager().getPackagesForUid(getCallingUid());
    }

}