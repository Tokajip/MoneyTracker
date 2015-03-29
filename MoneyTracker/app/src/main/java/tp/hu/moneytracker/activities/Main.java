package tp.hu.moneytracker.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import tp.hu.moneytracker.R;


public class Main extends Activity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        });
        }
}
