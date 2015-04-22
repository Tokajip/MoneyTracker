package tp.hu.moneytracker.activities;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import tp.hu.moneytracker.MoneyTrackerApplication;
import tp.hu.moneytracker.R;
import tp.hu.moneytracker.adapter.TransactionAdapter;
import tp.hu.moneytracker.data.Transaction;
import tp.hu.moneytracker.datastorage.TransactionDbLoader;
import tp.hu.moneytracker.db.DbConstants;

//Todo: Activityből származni
public class Income extends ActionBarActivity {

    // Log tag
    public static final String TAG = "Income";

    // State
    private LocalBroadcastManager lbm;

    // DBloader
    private TransactionDbLoader dbLoader;
    private GetAllTask getAllTask;
    private ListView list;
    private TransactionAdapter adapter;
    private Context ctx;
    private TextView selected;

    public Income() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income);
        list = (ListView) findViewById(R.id.list);
        list.setEmptyView(findViewById(R.id.empty));
        ctx = Income.this;
        lbm = LocalBroadcastManager.getInstance(getApplicationContext());
        dbLoader = MoneyTrackerApplication.getTransationDbLoader();
        categorySelection();
        lisItemClick();
    }

    private void lisItemClick() {
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Transaction selectedTran = (Transaction) parent.getItemAtPosition(position);
                createCategoryView(selectedTran);
            }
        });
    }

    private void createCategoryView(final Transaction selectedTran) {
        final Dialog dialog = new Dialog(ctx);
        dialog.setContentView(R.layout.category_selection);
        dialog.setTitle(selectedTran.getTitle());

        final Spinner spinner = (Spinner) dialog.findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categories_income, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_checkeditem);
        spinner.setAdapter(adapter);

        Button ok = (Button) dialog.findViewById(R.id.btn_ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedTran.setCategory((String) spinner.getSelectedItem());
                if (dbLoader.update(selectedTran)) {
                    Toast.makeText(ctx, "Sikeres kategória váltás", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(ctx, "Sikertelen kategória váltás", Toast.LENGTH_LONG).show();
                }
                selected.performClick();
                dialog.dismiss();
            }
        });

        Button cancel = (Button) dialog.findViewById(R.id.btn_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void categorySelection() {
        TextView tv_salary = (TextView) findViewById(R.id.salary);
        tv_salary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshList("Fizetés", (TextView) v);
            }
        });
        TextView tv_pocketmoney = (TextView) findViewById(R.id.pocketmoney);
        tv_pocketmoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshList("Zsebpénz", (TextView) v);
            }
        });
        TextView tv_pension = (TextView) findViewById(R.id.pension);
        tv_pension.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshList("Nyugdíj", (TextView) v);
            }
        });
        TextView tv_scholarship = (TextView) findViewById(R.id.scholarship);
        tv_scholarship.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshList("Ösztöndíj", (TextView) v);
            }
        });
        TextView tv_other = (TextView) findViewById(R.id.other_income);
        tv_other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshList("Egyéb bevétel", (TextView) v);
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        MoneyTrackerApplication.activityResumed();
        // Kódból regisztraljuk az adatbazis modosulasara figyelmezteto     Receiver-t
        IntentFilter filter = new IntentFilter(
                DbConstants.ACTION_DATABASE_CHANGED);
        lbm.registerReceiver(updateDbReceiver, filter);
        // Frissitjuk a lista tartalmat, ha visszater a user
    }

    @Override
    protected void onPause() {
        super.onPause();
        MoneyTrackerApplication.activityPaused();
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
            Log.i(TAG, params[0]);
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
        }
    };

    private void refreshList(String category, TextView selected) {
        setSelectionColor(selected);
        if (getAllTask != null) {
            getAllTask.cancel(false);
        }

        getAllTask = new GetAllTask();
        String[] params = new String[]{category};
        getAllTask.execute(params);
    }

/*

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
        if (id == R.id.action_share) {
//           Todo: Email küldés
            String[] list = new String[50];
            int i = 0;
            Cursor c = dbLoader.fetchAll();
            while (c.moveToNext()) {
                list[i++] = TransactionDbLoader.getTransationByCursor(c).toString();
            }
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_EMAIL, "tokajip@gmail.com");
            intent.putExtra(Intent.EXTRA_SUBJECT, "MoneyTracker db rekord");
            intent.putExtra(Intent.EXTRA_TEXT, getMyStringMessage(list));
            startActivity(Intent.createChooser(intent, "Send Email"));
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
*/

    private String getMyStringMessage(String[] arr) {
        StringBuilder builder = new StringBuilder();
        for (String s : arr) {
            builder.append(s);
        }
        return builder.toString();
    }

    private void setSelectionColor(TextView now) {
        if (selected == null) {
            selected = now;
            selected.setTextColor(Color.BLACK);
        } else {
            selected.setTextColor(Color.WHITE);
            selected = now;
            selected.setTextColor(Color.BLACK);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(),Main.class);
        startActivity(intent);
    }
}
