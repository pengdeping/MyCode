package com.sky.mvpdemo;

import com.sky.mvpcore.view.IBaseView;
import com.sky.mvpdemo.request.WeatherBean;

/**
 * @Description: java类作用描述
 * @CreateDate: 2021/9/30 16:02
 */
public interface IRequestView extends IBaseView {
    void requestLoading();
    void resultSuccess(WeatherBean result);
    void resultFailure(String result);
}
