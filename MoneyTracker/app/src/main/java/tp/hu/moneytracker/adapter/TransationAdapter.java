package tp.hu.moneytracker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import tp.hu.moneytracker.R;
import tp.hu.moneytracker.Transation;

/**
 * Created by Peti on 2015.03.22..
 */
public class TransationAdapter extends BaseAdapter {

    private Context mContext;
    private final List<Transation> list;

    public TransationAdapter(final Context ctx, final ArrayList<Transation> list) {
        this.list = list;
        this.mContext = ctx;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Transation transation = list.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.transation_item, null);
        }
        TextView title = (TextView) convertView.findViewById(R.id.transation_item_title);
        title.setText(transation.getTitle());

        TextView date = (TextView) convertView.findViewById(R.id.transation_item_date);
        date.setText(transation.getDate());

        TextView price = (TextView) convertView.findViewById(R.id.transation_item_price);
        price.setText(String.valueOf(transation.getPrice()));

        return convertView;
    }

    public void deleteItem(Transation item) {
        if (list.contains(item)) {
            list.remove(item);
        }
    }
}
