package tp.hu.moneytracker;

import android.app.Application;

import tp.hu.moneytracker.datastorage.TransactionDbLoader;

/**
 * Created by Peti on 2015.03.24..
 */
public class MoneyTrackerApplication extends Application {
    private static TransactionDbLoader dbLoader;

    public static TransactionDbLoader getTransationDbLoader() {
        return dbLoader;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        dbLoader = new TransactionDbLoader(this);
        dbLoader.open();
    }

    @Override
    public void onTerminate() {
        // Close the internal db
        dbLoader.close();
        super.onTerminate();
    }
    public static boolean isActivityVisible() {
        return activityVisible;
    }

    public static void activityResumed() {
        activityVisible = true;
    }

    public static void activityPaused() {
        activityVisible = false;
    }

    private static boolean activityVisible;
}
