package com.sky.mvpcore.proxy;

import com.sky.mvpcore.factory.PresenterFactory;
import com.sky.mvpcore.presenter.BasePresenter;
import com.sky.mvpcore.view.IBaseView;

/**
 * @author
 * @description 代理接口
 */
public interface PresenterProxyInterface<V extends IBaseView, P extends BasePresenter<V>> {


    /**
     * 设置创建Presenter的工厂
     *
     * @param presenterFactory PresenterFactory类型
     */
    void setPresenterFactory(PresenterFactory<V, P> presenterFactory);

    /**
     * 获取Presenter的工厂类
     *
     * @return 返回PresenterMvpFactory类型
     */
    PresenterFactory<V, P> getPresenterFactory();


    /**
     * 获取创建的Presenter
     *
     * @return 指定类型的Presenter
     */
    P getPresenter();


}
