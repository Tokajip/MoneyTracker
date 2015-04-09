package tp.hu.moneytracker.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import tp.hu.moneytracker.MoneyTrackerApplication;
import tp.hu.moneytracker.R;
import tp.hu.moneytracker.adapter.TileAdapter;
import tp.hu.moneytracker.data.TileDatas;
import tp.hu.moneytracker.gcm.GCMRegistrator;


public class Main extends Activity {


    private Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ctx = getApplicationContext();

        GridView gridView = (GridView) findViewById(R.id.gridview);
        gridView.setAdapter(new TileAdapter(getApplicationContext(), new TileDatas()));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0: Intent intent = new Intent(getApplicationContext(), Income.class);
                            startActivity(intent);
                            return;
                    case 1: intent = new Intent(getApplicationContext(), Outgo.class);
                            startActivity(intent);
                            return;
                    case 2: intent = new Intent(getApplicationContext(), Date.class);
                        startActivity(intent);
                        return;
                    default:return;
                }
            }
        });

        new GCMRegistrator().execute(new Context[]{ctx});
/*
        TextView outgo = (TextView) findViewById(R.id.outgo_tile);
        outgo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Outgo.class);
                startActivity(intent);
            }
        });
        TextView income = (TextView) findViewById(R.id.income_tile);
        income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Income.class);
                startActivity(intent);
            }
        });*/
        }

    @Override
    protected void onResume() {
        super.onResume();
        MoneyTrackerApplication.activityResumed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MoneyTrackerApplication.activityPaused();
    }
}
