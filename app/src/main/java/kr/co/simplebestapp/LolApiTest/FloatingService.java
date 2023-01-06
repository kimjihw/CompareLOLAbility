package kr.co.simplebestapp.LolApiTest;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

public class FloatingService extends Service {

    public static boolean isForegroundServiceRunning = false;
    private int notiId = 2660;

    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            //Log.e(LOGTAG, "action = " + intent.getAction());

            if ((getPackageName() + ".STOP_SERVICE").equals(intent.getAction())) {

                stopSelf();

            }

        }

    };

    @Override
    public void onCreate() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        IntentFilter mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(getPackageName() + ".STOP_SERVICE");

        registerReceiver(receiver, mIntentFilter);

        //노티바
        NotificationCompat.Builder builder;
        String CHANNEL_ID = getPackageName();

        NotificationManager mNotificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, getString(R.string.app_name), NotificationManager.IMPORTANCE_DEFAULT);

            mNotificationManager.createNotificationChannel(channel);

            builder = new NotificationCompat.Builder(this, CHANNEL_ID);

        } else {

            builder = new NotificationCompat.Builder(this, CHANNEL_ID);

        }

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, IntroActivity.class), PendingIntent.FLAG_MUTABLE);
        builder.setOnlyAlertOnce(true);
        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentText(getString(R.string.app_name) + " 실행 중 입니다.")
                .setContentTitle(getString(R.string.app_name))
                .setContentIntent(pendingIntent);

        mNotificationManager.notify(notiId, builder.build());

        startForeground(notiId, builder.build());
        isForegroundServiceRunning = true;

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
        return null;
    }

    @Override
    public void onDestroy() {

        try {

            if (receiver != null) {
                unregisterReceiver(receiver);
                receiver = null;
            }

            //Log.e(LOGTAG, "onDestroy!!! 서비스 중지!!!");

            isForegroundServiceRunning = false;

        } catch (Exception e) {
            e.printStackTrace();
        }

        super.onDestroy();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);

        isForegroundServiceRunning = false;
    }
}
