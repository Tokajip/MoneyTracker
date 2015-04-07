package tp.hu.moneytracker.gcm;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;

/**
 * Created by Peti on 2015.04.06..
 */
public class GCMRegistrator extends AsyncTask<Context,Void,String> {

    GoogleCloudMessaging gcm;
    String regid;
    String PROJECT_NUMBER = "411370586599";
    private String TAG = getClass().getSimpleName();

    @Override
    protected String doInBackground(Context... params) {
        String msg = "";
        try {
            if (gcm == null) {
                gcm = GoogleCloudMessaging.getInstance(params[0]);
            }
            regid = gcm.register(PROJECT_NUMBER);
            msg = "Device registered, registration ID=" + regid;
            Log.i("GCM", msg);

        } catch (IOException ex) {
            msg = "Error :" + ex.getMessage();
            Log.e(TAG,ex.toString());

        }
        return msg;
    }

}
