package kr.co.simplebestapp.LolApiTest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.android.installreferrer.api.InstallReferrerClient;
import com.android.installreferrer.api.InstallReferrerStateListener;
import com.android.installreferrer.api.ReferrerDetails;
import com.google.android.gms.appset.AppSet;
import com.google.android.gms.appset.AppSetIdClient;
import com.google.android.gms.appset.AppSetIdInfo;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.StringTokenizer;

import kr.co.simplebestapp.LolApiTest.common.AppPreferences;
import kr.co.simplebestapp.LolApiTest.common.BaseActivity;
import kr.co.simplebestapp.LolApiTest.common.CommAlertMessageBox;
import kr.co.simplebestapp.LolApiTest.common.Constants;
import kr.co.simplebestapp.LolApiTest.service.TransferService;
import kr.co.simplebestapp.LolApiTest.utis.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IntroActivity extends BaseActivity {

    private final int HTTP_SUCCESS_setAccessLog = 1;    //setAccessLog 성공
    private final int HTTP_SUCCESS_getAdId = 3;    //광고 ID가져오기
    private final int EXCEPTION = -1;

    //리퍼러
    private InstallReferrerClient referrerClient;
    private String recommendMember = "";
    private String channel = "";

    private boolean isNeedFinish = false;

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        Intent i = getIntent();
        isNeedFinish = i.getBooleanExtra("appFinish", false);

        if(isNeedFinish) {

            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    stopProgressDialog();
                    finish();

                }
            }, 500);

            return;
        }

        //로딩 설정
        pDialog = findViewById(R.id.loadingLayout);

        //리퍼러 검사
        referrerClient = InstallReferrerClient.newBuilder(this).build();
        if(AppPreferences.isFirstRun()) {

            referrerClient.startConnection(new InstallReferrerStateListener() {
                @Override
                public void onInstallReferrerSetupFinished(int i) {

                    //Log.e(LOGTAG, "i = " + i);

                    switch (i) {
                        case InstallReferrerClient.InstallReferrerResponse.OK:
                            // Connection established
                            getReferrerDetail();
                            break;
                        case InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED:
                            // API not available on the current Play Store app
                            //Log.e(LOGTAG, "API not available on the current Play Store app");
                            break;
                        case InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE:
                            // Connection could not be established
                            //Log.e(LOGTAG, "Connection could not be established");
                            break;
                    }

                }

                @Override
                public void onInstallReferrerServiceDisconnected() {
                    Log.e(LOGTAG, "onInstallReferrerServiceDisconnected!!!");
                }
            });

        }

        try {

            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;//Version Name
            int verCode = pInfo.versionCode;//Version Code

            AppPreferences.setVersion(version);

        } catch (Exception e) {
            e.printStackTrace();
        }

        startProgressDialog();
        getMessageKey();
    }

    @Override
    public void handleMessage(Message msg) {

        try {

            switch (msg.what) {

                case HTTP_SUCCESS_setAccessLog :

                    HashMap dataMap = (HashMap) msg.obj;

                    if(dataMap != null){

                        if(dataMap.get("kakaoTitle") != null && !"".equals(dataMap.get("kakaoTitle").toString())) {
                            AppPreferences.setKakaoTitle(dataMap.get("kakaoTitle").toString());
                        }

                        if(dataMap.get("kakaoDesc") != null && !"".equals(dataMap.get("kakaoDesc").toString())) {
                            AppPreferences.setKakaoDesc(dataMap.get("kakaoDesc").toString());
                        }

                        if(dataMap.get("kakaoImage") != null && !"".equals(dataMap.get("kakaoImage").toString())) {
                            AppPreferences.setKakaoImage(dataMap.get("kakaoImage").toString());
                        }

                        if(dataMap.get("shareDesc") != null && !"".equals(dataMap.get("shareDesc").toString())) {
                            AppPreferences.setShareDesc(dataMap.get("shareDesc").toString());
                        }

                        if(dataMap.get("testDevices") != null && !"".equals(dataMap.get("testDevices").toString())) {
                            AppPreferences.setTestDevices(dataMap.get("testDevices").toString());
                        }

                    }

                    goMain();

                    break;

                case HTTP_SUCCESS_getAdId:

                    setAccessLog();

                    break;

                case EXCEPTION:

                    String exceptionMsg = (String) msg.obj;

                    if (!"".equals(exceptionMsg)) {
                        AlertMessageBox(exceptionMsg, new CommAlertMessageBox.PositiveClickListener() {
                            @Override
                            public void clickListener() {
                                finish();
                            }
                        });    //Exception
                    } else {
                        AlertMessageBox(getString(R.string.comm_error), new CommAlertMessageBox.PositiveClickListener() {
                            @Override
                            public void clickListener() {
                                finish();
                            }
                        });
                    }

                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setAccessLog() {

        HashMap<String, String> params = Constants.getCommParam();
        params.put("os_v", Build.VERSION.SDK_INT + "");
        params.put("message_key", AppPreferences.getNewFcmToken());
        params.put("recommend", AppPreferences.getRecommendMember());
        params.put("channel", AppPreferences.getRecommendChannel());
        try {
            params.put("phone_model", Build.MODEL);
            params.put("manufacturer", Build.MANUFACTURER);
        } catch (Exception ignored) {}
        //Log.e(LOGTAG, "params = " + params);

        TransferService service = Constants.retrofit.create(TransferService.class);
        Call<HashMap> call = service.setAccess(params);
        call.enqueue(new Callback<HashMap>() {
            @Override
            public void onResponse(Call<HashMap> call, Response<HashMap> response) {

                HashMap dataMap = response.body();
                mHandler.sendMessage(mHandler.obtainMessage(HTTP_SUCCESS_setAccessLog, dataMap));

            }

            @Override
            public void onFailure(Call<HashMap> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    private void getMessageKey() {

        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {

            if (!task.isSuccessful()) {
                Log.w(LOGTAG, "Fetching FCM registration token failed", task.getException());
                return;
            }

            // Get new FCM registration token
            String token = task.getResult();

            if (!"".equals(token)) {

                AppPreferences.setNewFcmToken(token);

                if (Util.ISLOG) Log.e(LOGTAG, "newToken = " + token);
                if (Util.ISLOG) Log.e(LOGTAG, "getFcmToken = " + AppPreferences.getFcmToken());

            }

            getAdId();


        }).addOnFailureListener(e -> {
            getAdId();
        });


    }

    private void getAdId() {

        Runnable r = new Runnable() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(HTTP_SUCCESS_getAdId);
            }
        };

        Context context = getApplicationContext();
        AppSetIdClient client = AppSet.getClient(context);
        Task<AppSetIdInfo> task = client.getAppSetIdInfo();

        task.addOnSuccessListener(new OnSuccessListener<AppSetIdInfo>() {
            @Override
            public void onSuccess(AppSetIdInfo info) {
                // Determine current scope of app set ID.
                if(info != null) {

                    int scope = info.getScope();

                    // Read app set ID value, which uses version 4 of the
                    // universally unique identifier (UUID) format.
                    String id = info.getId();
                    //Log.e("MainActivity", "AppSetIdClient id - " + id);

                    if (!TextUtils.isEmpty(id)) {
                        AppPreferences.setAdIdNo(id);
                    } else {
                        AppPreferences.setAdIdNo("알수없음");
                    }

                    if (Util.ISLOG) Log.e(LOGTAG, "AdId = " + AppPreferences.getAdIdNo());

                } else {
                    AppPreferences.setAdIdNo("알수없음");
                }

                mHandler.removeCallbacks(r);
                mHandler.sendEmptyMessage(HTTP_SUCCESS_getAdId);
            }

        });

        mHandler.postDelayed(r, 1000);

    }

    private void getReferrerDetail() {

        try {

            ReferrerDetails response = referrerClient.getInstallReferrer();
            String referrer = response.getInstallReferrer();
            HashMap<String, String> referrerMap = getMapParam(referrer);

            //Log.e(LOGTAG, "referrer = " + referrer);

            if(referrerMap.get("recommend") != null) {
                recommendMember = referrerMap.get("recommend");
                Log.d(LOGTAG, "recommendMember = " + recommendMember);
            }

            if(referrerMap.get("channel") != null) {
                channel = referrerMap.get("channel");
                Log.d(LOGTAG, "channel = " + channel);
            }

            response.getReferrerClickTimestampSeconds();
            response.getInstallBeginTimestampSeconds();

            AppPreferences.setFirstRun(false);
            AppPreferences.setRecommendMember(recommendMember);
            AppPreferences.setRecommendChannel(channel);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private HashMap<String, String> getMapParam(String referrerParam) {

        HashMap<String, String> param = new HashMap<String, String>();

        if(referrerParam != null && !"".equals(referrerParam)) {

            StringTokenizer st = new StringTokenizer(referrerParam, "&");
            while(st.hasMoreTokens()) {
                String[] stArr = st.nextToken().split("=");
                if(stArr.length > 1) {
                    param.put(stArr[0], stArr[1]);
                } else {
                    param.put(stArr[0], "");
                }
            }
        }

        return param;
    }

    private void goMain() {

        if(isNeedFinish) {

            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    stopProgressDialog();
                    finish();

                }
            }, 500);

        } else {

            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    stopProgressDialog();

                    startActivity(IntroActivity.this, MainActivity.class);

                    finish();

                }
            }, 500);


        }

    }

}
