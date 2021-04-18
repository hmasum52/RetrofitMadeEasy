package com.hmasum18.retrofitmadeeasy.api;

/**
 * @author Hasan Masum. Email: connectwithmasum@gmail.com
 * @param <T> is the type of response
 */
public interface OnFinishListener<T>{
    void onSuccess(T t);
    void onFailure(Exception e);
}
