package tp.hu.moneytracker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import tp.hu.moneytracker.R;

/**
 * Created by Peti on 2015.04.25..
 */
public class RulesAdapter extends BaseAdapter {

    private final Map map;
    private List keyList= new ArrayList();

    public RulesAdapter(Map map) {
        this.map = map;
        keyList.addAll(map.keySet());
    }

    @Override
    public int getCount() {
        return keyList.size();
    }

    @Override
    public Object getItem(int position) {
        return keyList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.rules_item, null);
        }
        TextView tv_key = (TextView) convertView.findViewById(R.id.key);
        tv_key.setText(keyList.get(position).toString());

        TextView tv_values = (TextView) convertView.findViewById(R.id.value);
        tv_values.setText(map.get(keyList.get(position)).toString());
        return convertView;
    }
}
