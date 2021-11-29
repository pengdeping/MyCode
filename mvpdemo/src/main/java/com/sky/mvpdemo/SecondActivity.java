package com.sky.mvpdemo;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.sky.mvpcore.view.AbstractMvpAppCompatActivity;
import com.sky.mvpdemo.request.WeatherBean;

/**
 * @Description: java类作用描述
 * @CreateDate: 2021/9/30 16:59
 */
public class SecondActivity extends AbstractMvpAppCompatActivity<IRequestView,RequestPresenter> implements IRequestView{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void requestLoading() {

    }

    @Override
    public void resultSuccess(WeatherBean result) {

    }

    @Override
    public void resultFailure(String result) {

    }
}
