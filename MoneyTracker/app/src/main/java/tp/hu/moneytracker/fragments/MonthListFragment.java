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

import java.util.Calendar;
import java.util.GregorianCalendar;

import tp.hu.moneytracker.R;

/**
 * Created by Peti on 2015.04.12..
 */
public class MonthListFragment extends Fragment {


    private ListView list;
    private Context ctx;
    FragmentManager fragmentManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        fragmentManager=getFragmentManager();
        ctx = getActivity().getApplicationContext();
        View view = View.inflate(getActivity(), R.layout.fragment_list, null);
        list = (ListView) view.findViewById(R.id.frag_list);
        list.setEmptyView(view.findViewById(R.id.empty));
        lisItemClick();

        String[] values = new String[]{"Január",
                "Február",
                "Március",
                "Április",
                "Május",
                "Június",
                "Július",
                "Augusztus",
                "Szeptember",
                "Október",
                "November",
                "December"
        };
        list.setAdapter(new ArrayAdapter<String>(container.getContext(), R.layout.fragment_list_item, R.id.fragment_list_title, values));
        return view;
    }

    private void lisItemClick() {
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                loadListByDate(getMonth(position));
            }
        });
    }

    private java.util.Date getMonth(int month) {
        Calendar date = new GregorianCalendar();
// reset hour, minutes, seconds and millis
        date.set(Calendar.MONTH,month);
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
        arg.putLong("max", nextMonth(date));
        list_frag.setArguments(arg);
        fragmentTransaction.replace(R.id.date_frame, list_frag,"list");
        fragmentTransaction.commit();
    }
    public static long nextMonth(java.util.Date date) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, 1);
        return cal.getTime().getTime();
    }
}
