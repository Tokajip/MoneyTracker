package tp.hu.moneytracker.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import tp.hu.moneytracker.R;

/**
 * Created by Peti on 2015.04.12..
 */
public class MonthListFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);

        View view = View.inflate(getActivity(),R.layout.fragment_list, null);
        ListView list = (ListView) view.findViewById(R.id.frag_list);


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
        list.setAdapter(new ArrayAdapter<String>(container.getContext(), R.layout.transation_item, R.id.transation_item_title, values));
        return view;
    }

}
