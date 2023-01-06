package kr.co.simplebestapp.LolApiTest.common;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import kr.co.simplebestapp.LolApiTest.R;


public class CommonWebViewActivity extends BaseActivity {

    private ProgressBar progress;
    private WebView content;

    private boolean isPostSend = false;
    private String where = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_common_webview);

        Bundle b = getIntent().getBundleExtra(Constants.BUNDLE_PARAM_NAME);
        String title = b.getString("title", "");
        String url = b.getString("url", "");
        String parameter = b.getString("url_param", "");
        where = b.getString("where", "");

        if(url.equals(Constants.TUTORIAL_URL)) {
            AppPreferences.setTutorialYn(true);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        TextView toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);

//        toolbar_title.setText(title);
//        toolbar_title.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });

        ConstraintLayout toolbar_arrowLayout = (ConstraintLayout) toolbar.findViewById(R.id.toolbar_arrowLayout);
        toolbar_arrowLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        progress = (ProgressBar) findViewById(R.id.progress);
        content = (WebView) findViewById(R.id.content);
        content.getSettings().setJavaScriptEnabled(true);
        content.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            content.getSettings().setSafeBrowsingEnabled(false); //default true!!
        }

        content.setWebViewClient(new WebViewClient() {

            @TargetApi(Build.VERSION_CODES.N)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

                //Log.e(LOGTAG, "shouldOverrideUrlLoading 1");

                String url = request.getUrl().toString();

                return checkUrl(view, url);
            }

            @SuppressWarnings("deprecation")
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                //Log.e(LOGTAG, "shouldOverrideUrlLoading 2");

                //Log.e(LOGTAG, "url = " + url);
                return checkUrl(view, url);

            }


            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                // TODO Auto-generated method stub
                super.onPageStarted(view, url, favicon);
                progress.setVisibility(ProgressBar.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                //Log.e(LOGTAG, "onPageFinished");
                progress.setVisibility(ProgressBar.GONE);
            }

        });

        content.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progress.setProgress(newProgress);

                progress.setVisibility(ProgressBar.VISIBLE);

                if(newProgress > 100) {
                    progress.setVisibility(ProgressBar.GONE);
                }
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, final android.webkit.JsResult result) {
                new AlertDialog.Builder(CommonWebViewActivity.this)
                        .setTitle("알림")
                        .setMessage(message)
                        .setPositiveButton(android.R.string.ok, new AlertDialog.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                result.confirm();
                            }
                        })
                        .setCancelable(false)
                        .create()
                        .show();

                return true;
            }

        });

        /*Log.e(LOGTAG, "url = " + url);
        Log.e(LOGTAG, "parameter = " + parameter);*/

        if(TextUtils.isEmpty(parameter)) {
            content.loadUrl(url);
        } else {
            isPostSend = true;
            content.postUrl(url, parameter.getBytes());
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if ((keyCode == KeyEvent.KEYCODE_BACK)) {

            if(TextUtils.equals("blog", where)) {

                finish();

            } else {

                if (content.canGoBack() && !isPostSend) {
                    content.goBack();
                } else {
                    finish();
                }

            }

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private boolean checkUrl(WebView view, String url) {

        //Log.e(LOGTAG, "url = " + url);

        if (url != null && (url.startsWith("http://") || url.startsWith("https://"))) {

            view.loadUrl(url);

        } else {

            try {

                Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);

                if(intent == null) return true;

                intent.addCategory("android.intent.category.BROWSABLE");
                intent.setComponent(null);
                intent.setSelector(null);
                startActivity(intent);

                /*Intent existPackage = getPackageManager().getLaunchIntentForPackage(intent.getPackage());
                if (existPackage != null) {
                    startActivity(intent);
                } else {
                    Intent marketIntent = new Intent(Intent.ACTION_VIEW);
                    marketIntent.setData(Uri.parse("market://details?id="+intent.getPackage()));
                    startActivity(marketIntent);
                }*/

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return true;
    }


}
