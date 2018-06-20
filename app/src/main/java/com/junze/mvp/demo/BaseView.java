package com.junze.mvp.demo;

/**
 * 基本接口
 *
 * @author 2018/6/14 11:05 / mengwei
 */
public interface BaseView<T> {
    void setPresenter(T presenter);
}
