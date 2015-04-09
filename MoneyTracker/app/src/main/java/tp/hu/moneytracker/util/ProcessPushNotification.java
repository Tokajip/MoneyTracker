package tp.hu.moneytracker.util;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;

import tp.hu.moneytracker.MoneyTrackerApplication;
import tp.hu.moneytracker.R;
import tp.hu.moneytracker.activities.Income;
import tp.hu.moneytracker.activities.Outgo;
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
        dbLoader= MoneyTrackerApplication.getTransationDbLoader();
    };

    public void processDatas(String message, Handler handler){
        Transaction t = HandleJSON.readString(message,ctx,Transaction.class);
        Cursor cursor = dbLoader.fetchByTitle(t.getTitle());
        categorization(cursor, t);
        userNotify(t,handler);
        dbLoader.createTransition(t);
    }

    private  void sendNotification(Transaction t) {
        mNotificationManager = (NotificationManager)
                ctx.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = null;
        if(t.getPrice()<0){
            contentIntent = PendingIntent.getActivity(ctx, 0,
                    new Intent(ctx, Outgo.class), 0);
        }else {
            contentIntent = PendingIntent.getActivity(ctx, 0,
                    new Intent(ctx, Income.class), 0);
        }


        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(ctx)
                        .setSmallIcon(R.drawable.app_icon)
                        .setContentTitle("MoneyTracker")
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(t.getTitle()))
                        .setContentText("A(z) "+t.getTitle()+" tranzakció be lett téve a(z) "+t.getCategory()+" kategóriába.");

        mBuilder.setContentIntent(contentIntent);
        mBuilder.setAutoCancel(true);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    private void categorization(Cursor cursor,Transaction t){
        if (cursor.moveToFirst()){
            Transaction loaded =  TransactionDbLoader.getTransationByCursor(cursor);
            t.setCategory(loaded.getCategory());
        }
        else{
            t.setCategory("Other");

        }
    }
    private void userNotify(final Transaction t, Handler handler) {
        if(MoneyTrackerApplication.isActivityVisible()){
            /*handler.post(new Runnable() {
                @Override
                public void run() {
                    Dialog dialog = new Dialog(ctx);
                    dialog.setContentView(R.layout.transaction_popup);
                    TextView title = (TextView) dialog.findViewById(R.id.popup_title);
                    title.setText(t.getTitle());
                    dialog.show();
                }
            });*/
            sendNotification(t);
        }else{
            sendNotification(t);
        }
    }
}
