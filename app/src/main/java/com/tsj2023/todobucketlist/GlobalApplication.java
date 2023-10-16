package com.tsj2023.todobucketlist;

import android.app.Application;

import com.kakao.sdk.common.KakaoSdk;

public class GlobalApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        //kakao sdk initialize.. [네이티브 앱키 등록]
        KakaoSdk.init(this,"22facd1f44a722dac69dfc08e00d2147");
    }
}
