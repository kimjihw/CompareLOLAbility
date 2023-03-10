package kr.co.simplebestapp.LolApiTest.common;

import static kr.co.simplebestapp.LolApiTest.common.Constants.MY_KEY;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import kr.co.simplebestapp.LolApiTest.R;
import kr.co.simplebestapp.LolApiTest.interfaces.RetrofitInterface;
import retrofit2.Retrofit;

public class BaseActivity extends AppCompatActivity implements MyHandelerInterface {

    protected String LOGTAG = this.getClass().getSimpleName();

    protected View pDialog;

    protected final MyHandler mHandler = new MyHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void handleMessage(Message msg) {

    }

    protected void startActivity(Context packageContext, Class<?> cls) {

        Intent i = new Intent(packageContext, cls);
        startActivity(i);
        overridePendingTransition(R.anim.move_right_in_activity, R.anim.move_left_out_activity);

    }

    protected void startActivity(Context packageContext, Class<?> cls, Bundle b) {

        Intent i = new Intent(packageContext, cls);
        i.putExtra(Constants.BUNDLE_PARAM_NAME, b);
        startActivity(i);
        overridePendingTransition(R.anim.move_right_in_activity, R.anim.move_left_out_activity);

    }

    protected void startActionActivity(String action, Bundle b) {

        Intent i = new Intent(action);
        i.putExtra(Constants.BUNDLE_PARAM_NAME, b);
        startActivity(i);
        overridePendingTransition(R.anim.move_right_in_activity, R.anim.move_left_out_activity);

    }

    protected void startActivityForResult(Context packageContext, Class<?> cls, int requestCode) {

        Intent i = new Intent(packageContext, cls);
        startActivityForResult(i, requestCode);
        overridePendingTransition(R.anim.move_right_in_activity, R.anim.move_left_out_activity);

    }

    protected void startActivityForResult(Context packageContext, Class<?> cls, Bundle b, int requestCode) {

        Intent i = new Intent(packageContext, cls);
        i.putExtra(Constants.BUNDLE_PARAM_NAME, b);
        startActivityForResult(i, requestCode);
        overridePendingTransition(R.anim.move_right_in_activity, R.anim.move_left_out_activity);

    }

    //?????? ???????????????
    protected void startProgressDialog() {

        if(pDialog != null && !pDialog.isShown()) {

            pDialog.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });

            pDialog.setVisibility(View.VISIBLE);
        }


    }

    protected void stopProgressDialog() {

        if(pDialog != null && pDialog.isShown()) {
            pDialog.setVisibility(View.GONE);
        }
    }

    /**
     * ????????? ???
     *
     * @param sMsg  ?????????
     * @param listener ????????? ????????? ????????? ?????????
     */
    protected void AlertMessageBox(String sMsg, CommAlertMessageBox.PositiveClickListener listener) {

        CommAlertMessageBox alertMessageBox = new CommAlertMessageBox(this);
        alertMessageBox.AlertMessageBox(sMsg, listener);

    }

    protected void AlertMessageBox(String sMsg) {
        CommAlertMessageBox alertMessageBox = new CommAlertMessageBox(this);
        alertMessageBox.AlertMessageBox(sMsg, null);
    }

    /**
     * ?????? ????????? ???
     *
     * @param sMsg  ?????????
     * @param listener ?????? ?????? ?????????
     */
    protected void ConfirmMessageBox(String sMsg, CommAlertMessageBox.ConfirmClickListener listener) {

        CommAlertMessageBox alertMessageBox = new CommAlertMessageBox(this);
        alertMessageBox.ConfirmMessageBox(sMsg, listener);

    }

    /**
     * ?????? ????????? ???
     * @param sMsg ?????????
     * @param listener ?????? ?????? ?????????
     * @param negaBtStr ???????????? string
     * @param posBtStr ???????????? string
     */
    protected void ConfirmMessageBox(String sMsg, CommAlertMessageBox.ConfirmClickListener listener, String negaBtStr, String posBtStr) {

        CommAlertMessageBox alertMessageBox = new CommAlertMessageBox(this);
        alertMessageBox.setFirstButtonText(negaBtStr);
        alertMessageBox.setSecondButtonText(posBtStr);
        alertMessageBox.ConfirmMessageBox(sMsg, listener);

    }

    /**
     * ?????? ????????? ???
     * @param title ?????????
     * @param sMsg ?????????
     * @param listener ?????? ?????? ?????????
     * @param negaBtStr ???????????? string
     * @param posBtStr ???????????? string
     */
    protected void ConfirmMessageBox(String title, String sMsg, CommAlertMessageBox.ConfirmClickListener listener, String negaBtStr, String posBtStr) {

        CommAlertMessageBox alertMessageBox = new CommAlertMessageBox(this);
        alertMessageBox.setFirstButtonText(negaBtStr);
        alertMessageBox.setSecondButtonText(posBtStr);
        alertMessageBox.setTitle(title);
        alertMessageBox.ConfirmMessageBox(sMsg, listener);

    }

    protected void ConfirmMessageBox(String sMsg) {
        CommAlertMessageBox alertMessageBox = new CommAlertMessageBox(this);
        alertMessageBox.ConfirmMessageBox(sMsg, null);
    }


}
