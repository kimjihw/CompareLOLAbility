package kr.co.simplebestapp.LolApiTest;

import androidx.multidex.MultiDexApplication;


public class DefaultApplication extends MultiDexApplication {

    public 	static DefaultApplication mInstance = null;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;

        //KakaoSdk.init(this, "8843bcaa2d39017f6562d467f8513aa6");

    }
}
