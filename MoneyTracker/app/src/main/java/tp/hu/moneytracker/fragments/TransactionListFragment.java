package tp.hu.moneytracker.fragments;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import tp.hu.moneytracker.MoneyTrackerApplication;
import tp.hu.moneytracker.R;
import tp.hu.moneytracker.adapter.TransactionAdapter;
import tp.hu.moneytracker.datastorage.TransactionDbLoader;

/**
 * Created by Peti on 2015.04.13..
 */
public class TransactionListFragment extends Fragment {
    private Context ctx;
    private GetAllTask getAllTask;
    private TransactionDbLoader dbLoader;
    private TransactionAdapter adapter;
    private ListView list;
    private long min_date;
    private long max_date;


    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        Bundle args = getArguments();
        min_date = args.getLong("min");
        max_date = args.getLong("max");

        ctx = getActivity().getApplicationContext();
        dbLoader = MoneyTrackerApplication.getTransationDbLoader();
        View root = View.inflate(getActivity(), R.layout.fragment_list, null);
        list = (ListView) root.findViewById(R.id.frag_list);
        refreshList(min_date, max_date);
        return root;
    }

    private void refreshList(long mindate, long maxdate) {
        if (getAllTask != null) {
            getAllTask.cancel(false);
        }

        getAllTask = new GetAllTask();
        Long[] params = new Long[]{mindate, maxdate};
        getAllTask.execute(params);
    }

    private class GetAllTask extends AsyncTask<Long, Void, Cursor> {

        private static final String TAG = "GetAllTask";

        @Override
        protected Cursor doInBackground(Long[] params) {
            Log.i(TAG, params[0].toString());
            try {
                Cursor result = dbLoader.fetchByDate(params[0].toString(), params[1].toString());

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
                    adapter = new TransactionAdapter(ctx, result);
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

}
