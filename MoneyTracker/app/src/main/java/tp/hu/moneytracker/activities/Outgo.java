package tp.hu.moneytracker.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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


public class Outgo extends ActionBarActivity {

    // Log tag
    public static final String TAG = "TodoListFragment";

    // State
    private LocalBroadcastManager lbm;

    // DBloader
    private TransactionDbLoader dbLoader;
    private GetAllTask getAllTask;
    private ListView list;
    private TransactionAdapter adapter;
    private Context ctx;
    private TextView selected;
    public Outgo() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outgo);
        list = (ListView) findViewById(R.id.list);
        list.setEmptyView(findViewById(R.id.empty));
        ctx = Outgo.this;
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
                R.array.categories_outgo, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_checkeditem);
        spinner.setAdapter(adapter);
        spinner.setPadding(5,5,5,5);

        Button ok = (Button) dialog.findViewById(R.id.btn_ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedTran.setCategory((String)spinner.getSelectedItem());
                if(dbLoader.update(selectedTran)){
                    Toast.makeText(ctx,"Sikeres kategória váltás",Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(ctx,"Sikertelen kategória váltás",Toast.LENGTH_LONG).show();
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
        final TextView tv_food = (TextView) findViewById(R.id.food);
        tv_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshList("Élelmiszer",tv_food);
            }
        });
        final TextView tv_clothes = (TextView) findViewById(R.id.clothes);
        tv_clothes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshList("Ruházat",tv_clothes);
            }
        });
        final TextView tv_ent = (TextView) findViewById(R.id.entertainment);
        tv_ent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshList("Szórakozás",tv_ent);
            }
        });
        final TextView tv_bill = (TextView) findViewById(R.id.bills);
        tv_bill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshList("Számlák",tv_bill);
            }
        });
        final TextView tv_travel = (TextView) findViewById(R.id.travel);
        tv_travel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshList("Utazás",tv_travel);
            }
        });
        final TextView tv_sport = (TextView) findViewById(R.id.sport);
        tv_sport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshList("Sport",tv_sport);
            }
        });
        final TextView tv_atm = (TextView) findViewById(R.id.atm);
        tv_atm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshList("ATM",tv_atm);
            }
        });
        final TextView tv_other = (TextView) findViewById(R.id.other_outgo);
        tv_other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshList("Egyéb kiadás",tv_other);
                ;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        MoneyTrackerApplication.activityResumed();

        // Kódból regisztraljuk az adatbazis modosulasara figyelmezteto     Receiver-t
  /*      IntentFilter filter = new IntentFilter(
                DbConstants.ACTION_DATABASE_CHANGED);
        lbm.registerReceiver(updateDbReceiver, filter);
*/
        // Frissitjuk a lista tartalmat, ha visszater a user
    }

    @Override
    protected void onPause() {
        super.onPause();
        MoneyTrackerApplication.activityPaused();
        // Kiregisztraljuk az adatbazis modosulasara figyelmezteto  Receiver-t

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


    private void refreshList(String category,TextView textView) {
        setSelectionColor(textView);
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

            return super.onOptionsItemSelected(item);
        }*/
    private void setSelectionColor(TextView now){
        if(selected==null){
            selected=now;
            selected.setTextColor(Color.WHITE);
        }
        else{
            selected.setTextColor(Color.BLACK);
            selected=now;
            selected.setTextColor(Color.WHITE);
        }
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(),Main.class);
        startActivity(intent);
    }
}
