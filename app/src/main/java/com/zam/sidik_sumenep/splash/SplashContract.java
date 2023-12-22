package com.zam.sidik_sumenep.splash;

public interface SplashContract {

    interface SplashView {
        void onRequestStart();

        void onRequestSuccess(SplashResponse response);

        void onRequestError(String message);
    }

    interface Presenter {
        void requestData();

        void cancelRequest();
    }
}