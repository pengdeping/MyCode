package com.android.binderpool;

import android.os.IBinder;
import android.os.RemoteException;

import java.util.List;

/**
 * @Description: java类作用描述
 * @CreateDate: 2021/11/30 16:04
 */
public class BinderPoolManager extends IBinderPoolManager.Stub {
    public static final int CODE_WEATHER = 1;
    public static final int CODE_COMPUTER = 2;

    private BinderPoolService mBinderPoolService ;

    public BinderPoolManager(BinderPoolService binderPoolService) {
        mBinderPoolService = binderPoolService;
    }

    @Override
    public IBinder queryBinder(int binderCode) throws RemoteException {
        switch (binderCode){
            case CODE_WEATHER:
                return new IWeatherManager.Stub(){

                    @Override
                    public List<Weather> getWeather() throws RemoteException {
                        return mBinderPoolService.weathers;
                    }

                    @Override
                    public void addWeather(Weather weather) throws RemoteException {
                        mBinderPoolService.weathers.add(weather);
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
}
