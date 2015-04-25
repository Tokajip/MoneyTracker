package tp.hu.moneytracker.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import tp.hu.moneytracker.R;
import tp.hu.moneytracker.adapter.RulesAdapter;
import tp.hu.moneytracker.util.ProcessPushNotification;

public class Rules extends ActionBarActivity {

    private Context ctx;
    private ListView listView;
    private List outgo_categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);
        ctx= this;
        outgo_categories=new ArrayList(Arrays.asList(getResources().getStringArray(R.array.categories_outgo)));
        listView = (ListView) findViewById(R.id.rules_list);
        listView.setAdapter(new RulesAdapter(getRules()));
        listItemClick();
    }

    private Map<String, ?> getRules() {
        SharedPreferences sp = getSharedPreferences(ProcessPushNotification.PREF_NAME,MODE_PRIVATE);
        return sp.getAll();
    }

    private void listItemClick() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                createValueModifyWindow(view);
            }
        });
    }

    private void refreshList() {
        listView.setAdapter(new RulesAdapter(getRules()));
    }

    private void createValueModifyWindow(final View view) {
        final String key = (String) ((TextView)view.findViewById(R.id.key)).getText();
        final String value = (String) ((TextView)view.findViewById(R.id.value)).getText();

        final Dialog dialog = new Dialog(ctx);
        dialog.setContentView(R.layout.modify_value);
        dialog.setTitle(key);

        final Spinner spinner_category = (Spinner) dialog.findViewById(R.id.spinner_category);
        ArrayAdapter<CharSequence> adapter_category;
        if(outgo_categories.contains(value)){
           adapter_category = ArrayAdapter.createFromResource(this,
                    R.array.categories_outgo, R.layout.spinner_item);
        }else{
             adapter_category = ArrayAdapter.createFromResource(this,
                    R.array.categories_income, R.layout.spinner_item);
        }
        adapter_category.setDropDownViewResource(R.layout.spinner_checkeditem);
        spinner_category.setAdapter(adapter_category);

        Button ok = (Button) dialog.findViewById(R.id.btn_ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = getSharedPreferences(ProcessPushNotification.PREF_NAME, MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString(key,(String)spinner_category.getSelectedItem());
                editor.apply();
                refreshList();
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

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_rules, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/
}
