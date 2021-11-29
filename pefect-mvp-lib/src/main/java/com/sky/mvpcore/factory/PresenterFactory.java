package com.sky.mvpcore.factory;

import com.sky.mvpcore.presenter.BasePresenter;
import com.sky.mvpcore.view.IBaseView;

/**
 * @author
 * @description Presenter工厂接口
 */
public interface PresenterFactory<V extends IBaseView,P extends BasePresenter<V>> {

    /**
     * 创建Presenter的接口方法
     * @return 需要创建的Presenter
     */
    P createPresenter();
}
