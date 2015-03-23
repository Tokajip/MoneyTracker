package tp.hu.moneytracker;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.view.KeyEvent;
import android.widget.Toast;

/**
 * Created by Peti on 2015.03.22..
 */
public class NotificationReceive extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equalsIgnoreCase("android.media.")){
            Toast.makeText(context, "Volume UP!", Toast.LENGTH_LONG).show();
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.ic_launcher)
                            .setContentTitle("My notification")
                            .setContentText("Hello World!");

            NotificationManager mNotificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

// notificationID allows you to update the notification later on.
            mNotificationManager.notify(1,mBuilder.build());
        }
//            Toast.makeText(getApplicationContext(), "Volume DOWN!", Toast.LENGTH_LONG).show();

    }
}
