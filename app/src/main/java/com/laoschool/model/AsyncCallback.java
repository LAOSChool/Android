package com.laoschool.model;

/**
 * Created by Tran An on 29/03/2016.
 */
public interface AsyncCallback<T> {

    void onSuccess(T result);

    void onFailure(String message);

    void onAuthFail(String message);
}

