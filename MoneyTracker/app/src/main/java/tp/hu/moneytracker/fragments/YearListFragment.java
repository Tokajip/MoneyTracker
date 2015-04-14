package tp.hu.moneytracker.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.GregorianCalendar;

import tp.hu.moneytracker.R;

/**
 * Created by Peti on 2015.04.12..
 */
public class YearListFragment extends Fragment {


    private FragmentManager fragmentManager;
    private Context ctx;
    private ListView list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        fragmentManager = getFragmentManager();
        ctx = getActivity().getApplicationContext();
        View view = View.inflate(getActivity(), R.layout.fragment_list, null);
        list = (ListView) view.findViewById(R.id.frag_list);
        lisItemClick();

        String[] values = new String[]{"2015",
                "2014",
                "2013"};
        list.setAdapter(new ArrayAdapter<String>(container.getContext(), R.layout.fragment_list_item, R.id.fragment_list_title, values));
        return view;
    }

    private void lisItemClick() {
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int year = Integer.parseInt(((TextView) view.findViewById(R.id.fragment_list_title)).getText().toString());
                loadListByDate(getYear(year));
            }
        });
    }

    private java.util.Date getYear(int year) {
        Calendar date = new GregorianCalendar();
// reset hour, minutes, seconds and millis
        date.set(Calendar.YEAR, year);
        date.set(Calendar.MONTH, 0);
        date.set(Calendar.DAY_OF_MONTH, 1);
        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);
        return date.getTime();
    }

    private void loadListByDate(java.util.Date date) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        TransactionListFragment list_frag = new TransactionListFragment();
        Bundle arg = new Bundle();
        arg.putLong("min", date.getTime());
        arg.putLong("max", nextYear(date));
        list_frag.setArguments(arg);
        fragmentTransaction.replace(R.id.date_frame, list_frag,"list");
        fragmentTransaction.commit();
    }

    public static long nextYear(java.util.Date date) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.YEAR, 1);
        return cal.getTime().getTime();
    }
}
