package tp.hu.moneytracker.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import tp.hu.moneytracker.R;
import tp.hu.moneytracker.data.TileDatas;

/**
 * Created by Peti on 2015.03.31..
 */
public class TileAdapter extends BaseAdapter {

    private final Context ctx;
    TileDatas data;

    public TileAdapter(Context context, TileDatas datas){
        ctx=context;
        data= datas;
    }

    @Override
    public int getCount() {
        return data.getTitles().length;
    }

    @Override
    public Object getItem(int position) {
        return data.getTitles()[position];
    }

    @Override
    public long getItemId(int position) {
        return data.getIcons()[position];
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView==null){
            LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
             convertView = inflater.inflate(R.layout.tile, null);
        }

        TextView tv = (TextView) convertView.findViewById(R.id.tile_title);
        tv.setText(data.getTitles()[position]);
        ImageView iv = (ImageView) convertView.findViewById(R.id.tile_icon);
        iv.setImageResource(data.getIcons()[position]);
        convertView.setBackgroundColor(Color.parseColor("#ff086212"));
        return convertView;
    }
}
