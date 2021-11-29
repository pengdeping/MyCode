package com.sky.mvpdemo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.sky.mvpcore.view.AbstractMvpAppCompatActivity;
import com.sky.mvpdemo.request.WeatherBean;
import com.sky.mvpdemo.utils.FieldView;
import com.sky.mvpdemo.utils.ViewFind;
import com.sky.mydemo.R;

public class MvpActivity extends AbstractMvpAppCompatActivity<IRequestView,RequestPresenter> implements IRequestView {

    @FieldView(R.id.tv_text)
    TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mvp);
        ViewFind.bind(this);
        if(savedInstanceState != null){
            Log.e("perfect-mvp","MvpActivity  onCreate 测试  = " + savedInstanceState.getString("test") );
        }
    }

    //mTextView 的点击事件
    public void showText(View view){
        // TODO
        startActivity(new Intent(MvpActivity.this,SecondActivity.class));
    }

    @Override
    public void requestLoading() {
        getPresenter().startRequset("101010100");
    }

    @Override
    public void resultSuccess(WeatherBean result) {
        mTextView.setText(result.getWeatherinfo().toString());
    }

    @Override
    public void resultFailure(String result) {
        mTextView.setText(result);
    }
}