package tp.hu.moneytracker.util;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import tp.hu.moneytracker.R;
import tp.hu.moneytracker.TransactionApplication;
import tp.hu.moneytracker.activities.Main;
import tp.hu.moneytracker.data.Transaction;
import tp.hu.moneytracker.datastorage.TransactionDbLoader;

/**
 * Created by Peti on 2015.04.07..
 */
public class ProcessPushNotification {
    private static TransactionDbLoader dbLoader;
    private Context ctx;
    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;

    public ProcessPushNotification(Context c){
        ctx=c;
        dbLoader= TransactionApplication.getTransationDbLoader();
    };

    public void processDatas(String message){
        Transaction t = HandleJSON.readString(message,ctx,Transaction.class);
        sendNotification(t.getTitle());
//        dbLoader.createTransition(t);
    }
    private  void sendNotification(String msg) {
        mNotificationManager = (NotificationManager)
                ctx.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(ctx, 0,
                new Intent(ctx, Main.class), 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(ctx)
                        .setSmallIcon(R.drawable.app_icon)
                        .setContentTitle("MoneyTracker")
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg))
                        .setContentText(msg);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
}
