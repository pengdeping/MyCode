package com.sky.mvpdemo;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.Nullable;

import com.sky.mvpcore.presenter.BasePresenter;
import com.sky.mvpdemo.request.WeatherBean;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @Description: java类作用描述
 * @CreateDate: 2021/9/30 16:02
 */
public class RequestPresenter extends BasePresenter<IRequestView> {
    private final RequestMode mRequestMode;

    public RequestPresenter() {
        mRequestMode = new RequestMode();
    }

    public void startRequset(final String cityId){
        Log.e("perfect-mvp","点击事件 P ");
        if(getView() != null){
            getView().requestLoading();
        }
        //模拟网络延迟，可以显示出加载中
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mRequestMode.request(cityId, new Callback<WeatherBean>() {
                    @Override
                    public void onResponse(Call<WeatherBean> call, Response<WeatherBean> response) {
                        Log.e("perfect-mvp","点击事件 P  onResponse");
                        if (getView() != null){
                            getView().resultSuccess(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<WeatherBean> call, Throwable t) {
                        Log.e("perfect-mvp","点击事件 P  onFailure");
                        if (getView() != null){
                            getView().resultFailure(Log.getStackTraceString(t));
                        }
                    }
                });
            }
        }, 2000);
    }

    @Override
    public void onCreatePersenter(@Nullable Bundle savedState) {
        super.onCreatePersenter(savedState);
        if(savedState != null){
            Log.e("perfect-mvp","RequestPresenter5  onCreatePersenter 测试  = " + savedState.getString("test2") );
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.e("perfect-mvp","RequestPresenter  onSaveInstanceState 测试 " );
        outState.putString("test2","test_save2");
    }

    @Override
    public void onDestroyPersenter() {
        super.onDestroyPersenter();
    }

    public void interruptHttp(){
        mRequestMode.interruptHttp();
    }
}
