package tp.hu.moneytracker.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.util.Calendar;
import java.util.GregorianCalendar;

import tp.hu.moneytracker.MoneyTrackerApplication;
import tp.hu.moneytracker.R;
import tp.hu.moneytracker.fragments.MonthListFragment;
import tp.hu.moneytracker.fragments.TransactionListFragment;
import tp.hu.moneytracker.fragments.YearListFragment;

public class Date extends ActionBarActivity {

    private ListView listView;
    private Context ctx;
    FragmentManager fragmentManager = getSupportFragmentManager();
    private TextView selected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date);
        ctx = Date.this;
        getOption();

    }

    private void getOption() {
        TextView tv_month = (TextView) findViewById(R.id.month);
        tv_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.date_frame, new MonthListFragment());
                fragmentTransaction.commit();
                setSelectionColor((TextView) v);

            }
        });
        TextView tv_year = (TextView) findViewById(R.id.year);
        tv_year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.date_frame, new YearListFragment());
                fragmentTransaction.commit();
                setSelectionColor((TextView) v);
            }
        });
        TextView tv_day = (TextView) findViewById(R.id.day);
        tv_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelectionColor((TextView) v);
                handleCalendar();

            }
        });
        TextView tv_today = (TextView) findViewById(R.id.today);
        tv_today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelectionColor((TextView) v);
                loadListByDate(getToday());
            }
        });
    }

    private void handleCalendar() {
        CaldroidFragment caldroidFragment = new CaldroidFragment();
        Bundle args = new Bundle();
        Calendar cal = Calendar.getInstance();
        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
        caldroidFragment.setArguments(args);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.date_frame, caldroidFragment);
        fragmentTransaction.commit();
        CaldroidListener listener = new CaldroidListener() {
            @Override
            public void onSelectDate(java.util.Date date, View view) {
                loadListByDate(date);
            }
        };
        caldroidFragment.setCaldroidListener(listener);
    }

    private void loadListByDate(java.util.Date date) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        TransactionListFragment list_frag = new TransactionListFragment();
        Bundle arg = new Bundle();
        arg.putLong("min", date.getTime());
        arg.putLong("max", nextDay(date));
        list_frag.setArguments(arg);
        if(selected.getText().equals("Napra")){
            fragmentTransaction.replace(R.id.date_frame, list_frag, "list");
        }else {
            fragmentTransaction.replace(R.id.date_frame, list_frag);
        }
        fragmentTransaction.commit();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        MoneyTrackerApplication.activityResumed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MoneyTrackerApplication.activityPaused();
    }

    public static long nextDay(java.util.Date date) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, 1);
        return ((java.util.Date) cal.getTime()).getTime();
    }

    private java.util.Date getToday() {
        Calendar date = new GregorianCalendar();
// reset hour, minutes, seconds and millis
        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);
        return date.getTime();
    }

    private void setSelectionColor(TextView now) {
        if (selected == null) {
            selected = now;
            selected.setTextColor(Color.WHITE);
        } else {
            selected.setTextColor(Color.BLACK);
            selected = now;
            selected.setTextColor(Color.WHITE);
        }
    }

    @Override
    public void onBackPressed() {
        if (fragmentManager.findFragmentByTag("list") != null) {
            selected.performClick();
        } else {
            Intent intent = new Intent(getApplicationContext(),Main.class);
            startActivity(intent);
        }

    }
}
