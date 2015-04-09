package tp.hu.moneytracker.gcm;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import tp.hu.moneytracker.MoneyTrackerApplication;
import tp.hu.moneytracker.datastorage.TransactionDbLoader;
import tp.hu.moneytracker.util.ProcessPushNotification;

public class GcmMessageHandler extends IntentService {

    private static final String TAG = GcmMessageHandler.class.getSimpleName();
    String mes;
    private Handler handler;
    private TransactionDbLoader dbLoader;

    public GcmMessageHandler() {
        super("GcmMessageHandler");
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        handler = new Handler();
        dbLoader = MoneyTrackerApplication.getTransationDbLoader();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();

        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        mes = extras.getString("payload");
        new ProcessPushNotification(getApplicationContext()).processDatas(mes,handler);

        Log.i("GCM", "Received : (" + messageType + ")  " + extras.getString("title"));

        GcmBroadcastReceiver.completeWakefulIntent(intent);

    }

    public void showToast() {
        handler.post(new Runnable() {
            public void run() {
                Toast.makeText(getApplicationContext(), mes, Toast.LENGTH_LONG).show();
            }
        });

    }
}