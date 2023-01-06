package kr.co.simplebestapp.LolApiTest.config;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import kr.co.simplebestapp.LolApiTest.R;
import kr.co.simplebestapp.LolApiTest.common.BaseActivity;
import kr.co.simplebestapp.LolApiTest.FloatingService;
import kr.co.simplebestapp.LolApiTest.common.AppPreferences;
import kr.co.simplebestapp.LolApiTest.common.CommonWebViewActivity;
import kr.co.simplebestapp.LolApiTest.common.Constants;
import kr.co.simplebestapp.LolApiTest.interfaces.RecommendResult;
import kr.co.simplebestapp.LolApiTest.utis.Util;
import kr.co.simplebestapp.skipjump.common.KaKaoLinkHelper;

public class ConfigActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        TextView toolbar_title = (TextView) findViewById(R.id.toolbar_title);

//        toolbar_title.setText(R.string.config_title);
//        toolbar_title.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });

        findViewById(R.id.toolbar_arrowLayout).setOnClickListener(v -> {
            finish();
        });

        ConstraintLayout themeSwitchLayout = (ConstraintLayout) findViewById(R.id.themeSwitchLayout);
        View themeSwitchLayoutUnderLine = (View) findViewById(R.id.themeSwitchLayoutUnderLine);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            themeSwitchLayout.setVisibility(View.VISIBLE);
            themeSwitchLayoutUnderLine.setVisibility(View.VISIBLE);
        } else {
            themeSwitchLayout.setVisibility(View.GONE);
            themeSwitchLayoutUnderLine.setVisibility(View.GONE);
        }

        if(AppPreferences.isNotiYn() && !FloatingService.isForegroundServiceRunning) {
            AppPreferences.setNotiYn(false);
        }

        ((SwitchCompat) findViewById(R.id.themeSetSwitch)).setChecked(AppPreferences.isNotiYn());
        ((SwitchCompat) findViewById(R.id.themeSetSwitch)).setOnCheckedChangeListener((buttonView, isChecked) -> {

            if(isChecked) {

                //FirebaseAnalytics.getInstance(ConfigActivity.this).logEvent("노티바켜기", new Bundle());

                stateServiceStart();

            } else {

                //FirebaseAnalytics.getInstance(ConfigActivity.this).logEvent("노티바끄기", new Bundle());

                //if(Util.isLaunchingService(this)) {
                if(FloatingService.isForegroundServiceRunning) {
                    Intent i = new Intent();
                    i.setAction(getPackageName() + ".STOP_SERVICE");
                    sendBroadcast(i);
                }

            }

            AppPreferences.setNotiYn(isChecked);

        });

        TextView versionText = (TextView) findViewById(R.id.versionText);
        versionText.setText("v " + AppPreferences.getVersion());

        findViewById(R.id.recommendLayout).setOnClickListener(v -> {

            KaKaoLinkHelper.kakaoLink(this, new RecommendResult() {
                @Override
                public void onSuccess() {

                    //FirebaseAnalytics.getInstance(ConfigActivity.this).logEvent("카톡추천_설정에서", new Bundle());
                    //setRecommend();
                }

                @Override
                public void onFail(String reason) {

                    AlertMessageBox(reason);
                }
            }, "config");
        });

        ConstraintLayout reviewLayout = findViewById(R.id.reviewLayout);
        View reviewLayoutUnderLine = findViewById(R.id.reviewLayoutUnderLine);

        reviewLayout.setOnClickListener(v -> {

            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
            }

        });

        if(Util.isOneStore) {
            reviewLayout.setVisibility(View.GONE);
            reviewLayoutUnderLine.setVisibility(View.GONE);
        }

        findViewById(R.id.tutorailLayout).setOnClickListener(v -> {

            Bundle b = new Bundle();
            b.putString("title", getString(R.string.config_tutorialTitle));
            b.putString("url", Constants.TUTORIAL_URL);
            startActivity(this, CommonWebViewActivity.class, b);

        });

        findViewById(R.id.customerLayout).setOnClickListener(v -> {
            KaKaoLinkHelper.kakaoChannel(this);
        });
    }

    private void stateServiceStart() {

        //if(!Util.isLaunchingService(this)) {
        try {
            if (!FloatingService.isForegroundServiceRunning) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(new Intent(this, FloatingService.class));
                } else {
                    startService(new Intent(this, FloatingService.class));
                }

            }
        } catch (Exception e) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(new Intent(this, FloatingService.class));
            } else {
                startService(new Intent(this, FloatingService.class));
            }

        }
    }

}


