package tp.hu.moneytracker.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import tp.hu.moneytracker.R;
import tp.hu.moneytracker.TransactionApplication;
import tp.hu.moneytracker.adapter.TransactionAdapter;
import tp.hu.moneytracker.datastorage.TransactionDbLoader;
import tp.hu.moneytracker.db.DbConstants;

//Todo: Activityből származni
public class Income extends ActionBarActivity{

    // Log tag
    public static final String TAG = "TodoListFragment";

    // State
    private LocalBroadcastManager lbm;

    // DBloader
    private TransactionDbLoader dbLoader;
    private GetAllTask getAllTask;
    private ListView list;
    private TransactionAdapter adapter;

    public Income() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income);
        list = (ListView) findViewById(R.id.incomeList);

        lbm = LocalBroadcastManager.getInstance(getApplicationContext());
        dbLoader = TransactionApplication.getTransationDbLoader();
        refreshList("Food");
        categorySelection();
    }

    private void categorySelection() {
        TextView tv_food = (TextView) findViewById(R.id.food);
        tv_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshList("Food");
            }
        });
        TextView tv_clothes = (TextView) findViewById(R.id.clothes);
        tv_clothes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshList("Clothes");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Kódból regisztraljuk az adatbazis modosulasara figyelmezteto     Receiver-t
        IntentFilter filter = new IntentFilter(
                DbConstants.ACTION_DATABASE_CHANGED);
        lbm.registerReceiver(updateDbReceiver, filter);
        // Frissitjuk a lista tartalmat, ha visszater a user
        refreshList("Food");
    }
    @Override
    protected void onPause() {
        super.onPause();
        // Kiregisztraljuk az adatbazis modosulasara figyelmezteto  Receiver-t
        lbm.unregisterReceiver(updateDbReceiver);

        if (getAllTask != null) {
            getAllTask.cancel(false);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Ha van Cursor rendelve az Adapterhez, lezarjuk
        if (adapter != null && adapter.getCursor() != null) {
            adapter.getCursor().close();
        }
    }

    private class GetAllTask extends AsyncTask<String, Void, Cursor> {

        private static final String TAG = "GetAllTask";

        @Override
        protected Cursor doInBackground(String[] params) {
            Log.i(TAG,params[0]);
            try {
                Cursor result = dbLoader.fetchByCategory(params[0]);

                if (!isCancelled()) {
                    return result;
                } else {
                    Log.d(TAG, "Cancelled, closing cursor");
                    if (result != null) {
                        result.close();
                    }

                    return null;
                }
            } catch (Exception e) {
                Log.e(TAG, e.toString());
                return null;
            }
        }

        @Override
        protected void onPostExecute(Cursor result) {
            super.onPostExecute(result);

            Log.d(TAG, "Fetch completed, displaying cursor results!");
            try {
                if (adapter == null) {
                    adapter = new TransactionAdapter(getApplicationContext(), result);
                    list.setAdapter(adapter);
                } else {
                    adapter.changeCursor(result);
                }

                getAllTask = null;
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        }

    }

    private BroadcastReceiver updateDbReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            refreshList("Food");
        }
    };

    private void refreshList(String category) {
        if (getAllTask != null) {
            getAllTask.cancel(false);
        }

        getAllTask = new GetAllTask();
        String[] params = new String[]{category};
        getAllTask.execute(params);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_income, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            dbLoader.deleteAll();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
