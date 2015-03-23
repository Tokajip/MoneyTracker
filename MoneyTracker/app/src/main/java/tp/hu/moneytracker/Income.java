package tp.hu.moneytracker;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import tp.hu.moneytracker.adapter.TransationAdapter;


public class Income extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income);

        Transation t = new Transation();
        t.setTitle("Test");
        t.setDate("2015-02-02");
        t.setPrice(15000);
        ArrayList<Transation> listTransation = new ArrayList<>();
        for(int i=0; i<25; i++){
            listTransation.add(t);
        }
        ListView list = (ListView) findViewById(R.id.incomeList);
        list.setAdapter(new TransationAdapter(getApplication(),listTransation));
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
*/
}
