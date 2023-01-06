package kr.co.simplebestapp.LolApiTest.common;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import kr.co.simplebestapp.LolApiTest.R;

/**
 * Created by choijingyu on 2019. 4. 19..
 */

public class CommAlertMessageBox {

    //private Context activity;
    private Activity activity;

    private String firstButtonText = "";
    private String secondButtonText = "";
    private boolean isCancel = false;
    private String title = "";

    public CommAlertMessageBox(Activity c) {
        this.activity = c;
    }

    public void AlertMessageBox(String sMsg, final PositiveClickListener clickListener) {

        if(!activity.isFinishing()) {

            final Dialog builderSingle = new Dialog(activity);
            builderSingle.requestWindowFeature(Window.FEATURE_NO_TITLE);
            builderSingle.setContentView(R.layout.dialog_alert_custom);

            WindowManager.LayoutParams params = builderSingle.getWindow().getAttributes();
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            builderSingle.getWindow().setAttributes(params);
            builderSingle.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            //타이틀
            TextView AlertTitle = (TextView) builderSingle.findViewById(R.id.custom_alert_content);

            if (!"".equals(sMsg)) AlertTitle.setText(sMsg);

            //원버튼
            ConstraintLayout custom_alert_oneBt = (ConstraintLayout) builderSingle.findViewById(R.id.custom_alert_oneBt);

            //투버튼
            ConstraintLayout custom_alert_twoBt = (ConstraintLayout) builderSingle.findViewById(R.id.custom_alert_twoBt);

            custom_alert_oneBt.setVisibility(View.VISIBLE);
            custom_alert_twoBt.setVisibility(View.GONE);

            //원버튼 레이아웃 button
            TextView custom_alert_oneOkBt = (TextView) builderSingle.findViewById(R.id.custom_alert_oneOkBt);
            if (!"".equals(firstButtonText)) custom_alert_oneOkBt.setText(firstButtonText);

            custom_alert_oneOkBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (clickListener != null) clickListener.clickListener();
                    builderSingle.dismiss();
                }
            });


            builderSingle.setCancelable(isCancel);
            builderSingle.show();

        }

    }

    public void ConfirmMessageBox(String sMsg, final ConfirmClickListener clickListener) {

        if(!activity.isFinishing()) {

            final Dialog builderSingle = new Dialog(activity);
            builderSingle.requestWindowFeature(Window.FEATURE_NO_TITLE);
            builderSingle.setContentView(R.layout.dialog_alert_custom);

            WindowManager.LayoutParams params = builderSingle.getWindow().getAttributes();
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            builderSingle.getWindow().setAttributes(params);
            builderSingle.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            //타이틀
            TextView custom_alert_title = (TextView) builderSingle.findViewById(R.id.custom_alert_title);
            if (!TextUtils.isEmpty(title)) {
                custom_alert_title.setText(title);
            }

            TextView AlertTitle = (TextView) builderSingle.findViewById(R.id.custom_alert_content);

            if (!"".equals(sMsg)) AlertTitle.setText(sMsg);

            //원버튼
            ConstraintLayout custom_alert_oneBt = (ConstraintLayout) builderSingle.findViewById(R.id.custom_alert_oneBt);

            //투버튼
            ConstraintLayout custom_alert_twoBt = (ConstraintLayout) builderSingle.findViewById(R.id.custom_alert_twoBt);

            custom_alert_oneBt.setVisibility(View.GONE);
            custom_alert_twoBt.setVisibility(View.VISIBLE);

            TextView custom_alert_cancelBt = (TextView) builderSingle.findViewById(R.id.custom_alert_cancelBt);
            if (!"".equals(firstButtonText)) custom_alert_cancelBt.setText(firstButtonText);

            custom_alert_cancelBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clickListener != null) clickListener.negativeClickListener();
                    builderSingle.dismiss();
                }
            });

            //투버튼 레이아웃 button
            TextView custom_alert_okBt = (TextView) builderSingle.findViewById(R.id.custom_alert_okBt);
            if (!"".equals(secondButtonText)) custom_alert_okBt.setText(secondButtonText);

            custom_alert_okBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (clickListener != null) clickListener.positiveclickListener();
                    builderSingle.dismiss();

                }
            });

            builderSingle.setCancelable(isCancel);
            builderSingle.show();

        }

    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCancel(boolean cancel) {
        isCancel = cancel;
    }

    public void setFirstButtonText(String resStr) {
        firstButtonText = resStr;
    }

    public void setSecondButtonText(String resStr) {
        secondButtonText = resStr;
    }

    public interface PositiveClickListener {
        void clickListener();
    }

    public interface ConfirmClickListener {
        void positiveclickListener();
        void negativeClickListener();
    }
}
