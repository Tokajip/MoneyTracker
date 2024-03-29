package tp.hu.moneytracker.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import tp.hu.moneytracker.R;
import tp.hu.moneytracker.data.Transaction;
import tp.hu.moneytracker.datastorage.TransactionDbLoader;

/**
 * Created by Peti on 2015.03.22..
 */
public class TransactionAdapter extends CursorAdapter {


    public TransactionAdapter(Context context, Cursor cursor) {
        super(context, cursor, false);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        final LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.transation_item, null);
        bindView(row, context, cursor);
        return row;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView title = (TextView) view.findViewById(R.id.transation_item_title);
        TextView price = (TextView) view.findViewById(R.id.transation_item_price);
        TextView date = (TextView) view.findViewById(R.id.transation_item_date);

        Transaction transaction = TransactionDbLoader.getTransationByCursor(cursor);

        title.setText(transaction.getTitle());
        date.setText(convertDate(transaction.getDate(),"yyyy-MM-dd"));
        price.setText(String.valueOf(transaction.getPrice()));
    }

    @Override
    public Object getItem(int position) {
        getCursor().moveToPosition(position);
        return TransactionDbLoader.getTransationByCursor(getCursor());
    }

    public static String convertDate(long time, String form){
        Date date = new Date(time);
        Format format = new SimpleDateFormat(form);
        return format.format(date);
    }
}
