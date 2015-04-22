package tp.hu.moneytracker.util;

import android.content.Context;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import tp.hu.moneytracker.data.Transaction;

/**
 * Created by pvo on 21.10.14.
 */
public class HandleJSON {

    public static Transaction readStream(String filePath, Context ctx, Class c) {
        String data = "";
        Gson gson = new Gson();
        Transaction t =null;
        try {
            InputStream is = ctx.getAssets().open(filePath);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder out = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                out.append(line);
            }

            data = out.toString();
            reader.close();
            t = (Transaction) gson.fromJson(data, c);
            if(t.getPrice()<0){
                t.setType(0);
            }else {
                t.setType(1);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return t;
    }

    public static Transaction readString(String json, Context ctx, Class c){
        Gson gson = new Gson();
        Transaction t = (Transaction)gson.fromJson(json,c);
        if(t.getPrice()<0){
            t.setType(0);
        }else {
            t.setType(1);
        }
        return  t ;
    }
public static String toJSON(Transaction transaction){
    Gson gson = new Gson();
    return gson.toJson(transaction);
}

}
