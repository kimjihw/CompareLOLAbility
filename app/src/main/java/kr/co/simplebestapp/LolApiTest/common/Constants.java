package kr.co.simplebestapp.LolApiTest.common;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import kr.co.simplebestapp.LolApiTest.DefaultApplication;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Constants {

    public static final String HOST_URL = "https://measure.simplebestapp.co.kr/";
    public static final String AD_HOST_URL = "https://simplebestapp.co.kr/";

    public static final String KR_URL ="https://kr.api.riotgames.com";
    public static final String ASIA_URL = "https://asia.api.riotgames.com";

    public static final String MY_KEY = "RGAPI-7cd88431-f170-49fd-8f5e-5e1a2779db3a";

    /*private static final OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request().newBuilder().addHeader("From-Device", "mobile").build();
            return chain.proceed(request);
        }
    }).connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build();*/

    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(HOST_URL)
            .client(new OkHttpClient.Builder()
                    .readTimeout(30, TimeUnit.SECONDS)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS).build())
            //.client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public static final Retrofit adRetrofit = new Retrofit.Builder()
            .baseUrl(AD_HOST_URL)
            .client(new OkHttpClient.Builder()
                    .readTimeout(30, TimeUnit.SECONDS)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS).build())
            //.client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public static final Retrofit krRetrofit = new Retrofit.Builder()
            .baseUrl(KR_URL)
            .client(new OkHttpClient.Builder()
                    .readTimeout(30, TimeUnit.SECONDS)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS).build())
            //.client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public static final Retrofit asiaRetrofit = new Retrofit.Builder()
            .baseUrl(ASIA_URL)
            .client(new OkHttpClient.Builder()
                    .readTimeout(30, TimeUnit.SECONDS)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS).build())
            //.client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public static final String TUTORIAL_URL = HOST_URL + "/compass_tutorial";

    public static final String BUNDLE_PARAM_NAME = "bundle";

    /*
    일반 텍스트용
     */
    public static HashMap<String, String> getCommParam() {

        HashMap<String, String> paramMap = new HashMap<String, String>();

        paramMap.put("version", AppPreferences.getVersion());
        paramMap.put("ad_id", AppPreferences.getAdIdNo());
        paramMap.put("package_name", DefaultApplication.mInstance.getPackageName());

        return paramMap;
    }
}
